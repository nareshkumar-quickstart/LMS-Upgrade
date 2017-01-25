package com.softech.vu360.lms.service.impl.lmsapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.lmsapi.CourseAndCourseGroupServiceLmsApi;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.Course;



public class CourseAndCourseGroupServiceImplLmsApi implements CourseAndCourseGroupServiceLmsApi {
	
private static final Logger log = Logger.getLogger(CourseAndCourseGroupServiceImplLmsApi.class.getName());
	
	private CourseAndCourseGroupService courseCourseGrpService;
	private EntitlementService entitlementService;
	
	public CourseAndCourseGroupService getCourseCourseGrpService() {
		return courseCourseGrpService;
	}

	public void setCourseCourseGrpService(CourseAndCourseGroupService courseCourseGrpService) {
		this.courseCourseGrpService = courseCourseGrpService;
	}
	
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	
    public Map<String, Object> getCoursesMap(Customer customer, List<String> courseGuidList) throws Exception {
		 
		Map<String, Object> coursesMap = new LinkedHashMap<String, Object>();
		Map<String, String> invalidCoursesMap = new LinkedHashMap<String, String>();
		List<com.softech.vu360.lms.model.Course> validCoursesList = new ArrayList<com.softech.vu360.lms.model.Course>();
		for (String courseGuid : courseGuidList) {
			try {
				com.softech.vu360.lms.model.Course newCourse = getValidCourse(customer, courseGuid);
				validCoursesList.add(newCourse);
			} catch (Exception e) {
				String errorMessgae = e.getMessage();
				invalidCoursesMap.put(courseGuid, errorMessgae);
			}
		} 
		
		if (!validCoursesList.isEmpty()) {
			coursesMap.put("validCoursesList", validCoursesList);
		}
		
		if (!invalidCoursesMap.isEmpty()) {
			coursesMap.put("invalidCoursesMap", invalidCoursesMap);
		}
		
		return coursesMap;
	
    }
    
    public com.softech.vu360.lms.model.Course getValidCourse(Customer customer, String courseGUID) throws Exception {
		
		com.softech.vu360.lms.model.Course course = validateCourseGuid(courseGUID);
		isValidCourse(customer, course);
		
		return course;
	}
	
	public com.softech.vu360.lms.model.Course validateCourseGuid(String courseGUID) throws Exception {
		 
		String errorMessage = null;
		
		if (StringUtils.isEmpty(courseGUID) && StringUtils.isBlank(courseGUID)) {
			errorMessage = "Course Id can not be empty";
			throwException(errorMessage);
		}
		
		com.softech.vu360.lms.model.Course newCourse = courseCourseGrpService.getCourseByGUID(courseGUID);
		if (newCourse == null) {
			errorMessage = "No course found for " + courseGUID;
			throwException(errorMessage);
		}
		
		return newCourse;
		
	 }

	public boolean validateCourse(Customer customer, String courseGUID) throws Exception {
		
		com.softech.vu360.lms.model.Course newCourse = validateCourseGuid(courseGUID);
		isValidCourse(customer, newCourse);
		
		return true;
	}
	
	public boolean isValidCourse(Customer customer, com.softech.vu360.lms.model.Course course) throws Exception {
		
		courseLevelEnrollmentValidations(course);
		
		if (customer != null) {
			GregorianCalendar todayDate = getTodayDate();
			Date enrollmentStartDate = todayDate.getTime();
			entitlementLevelEnrollmentValidations(customer, course, enrollmentStartDate);
		}
		
		return true;
	}
	
	public boolean courseLevelEnrollmentValidations(com.softech.vu360.lms.model.Course course) throws Exception {
		
		String courseGuid = course.getCourseGUID();
		
		log.debug("courseLevelEnrollmentValidations() start for course: " + courseGuid);
		String errorMessage = null;
		
		//If not active enrollment exists and course is retired, then exit. 
		if(course.isRetired()) {
			errorMessage = "Course is retired: " + courseGuid;
			throwException(errorMessage);
		}
				
		if(!course.getCourseStatus().equalsIgnoreCase("Published")) {
			errorMessage = "Course is not published: " + courseGuid;
			throwException(errorMessage);
		}
			
		log.debug("courseLevelEnrollmentValidations() end for course: " + courseGuid);
		return true;
		
	}
	
	private boolean entitlementLevelEnrollmentValidations(Customer customer, com.softech.vu360.lms.model.Course course, 
			Date enrollmentStartDate) throws Exception {
		
		log.debug("entitlementLevelEnrollmentValidations() start for customer: " + customer.getCustomerCode() + 
				" having course: " + course.getCourseGUID());
			
		String errorMessage = null;
		GregorianCalendar todayDate = getTodayDate();
	    List<CustomerEntitlement> customerEntitlementList = getCustomerEntitlementListForCourse(customer, course);
			
		if ( customerEntitlementList.isEmpty() ) { //if no contract found then exit
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not exist for customer: " + customer.getCustomerCode();
			throwException(errorMessage);
		} 
			
		List<Date> contractEndDatesList = new ArrayList <Date>();
		List<Date> contractStartDatesList = new ArrayList <Date>();
		
		for( CustomerEntitlement ent : customerEntitlementList ) {	
			boolean isSeatAvailable = false;
			if (!ent.isAllowUnlimitedEnrollments()) {
				if (ent.hasAvailableSeats(1)) {
					isSeatAvailable = true;		
				}
			} else {
				isSeatAvailable = true;
			}
			if (isSeatAvailable) {
				if ( ent.getEndDate() != null && !ent.getEndDate().before(todayDate.getTime())) {																
					   contractEndDatesList.add(ent.getEndDate());
					   contractStartDatesList.add(ent.getStartDate());								
				} else{
					Calendar c = Calendar.getInstance();
					c.setTime(ent.getStartDate()); // Now use entitlement start date.
					c.add(Calendar.DATE, ent.getDefaultTermOfServiceInDays()); // Adding days
					Date entEndDate = c.getTime();
					
					if(!entEndDate.before(todayDate.getTime())){ 
					   contractEndDatesList.add(entEndDate);
					   contractStartDatesList.add(ent.getStartDate());
					}
				}	
			}
		} //end of for for( CustomerEntitlement ent : ...)
		
		if (contractStartDatesList.isEmpty()) {
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not have seats available for customer: " + customer.getCustomerCode();
			throwException(errorMessage);
		}
		
		if (contractEndDatesList.isEmpty()) {
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not have seats available for customer: " + customer.getCustomerCode();
			throwException(errorMessage);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
		Date courseStartDate = enrollmentStartDate;
	
		if ( courseStartDate.before(todayDate.getTime()) ) {							
			
			Date minEndDate = Collections.min(contractEndDatesList);
			String minEnrollmentEndDate = sdf.format(minEndDate);
			String todaysDate = sdf.format(todayDate.getTime());
			
			errorMessage = "Start date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
			throwException(errorMessage);
		} 
		
		if( contractEndDatesList != null && !contractEndDatesList.isEmpty() ) {
			Date maxEndDate = Collections.max(contractEndDatesList); 
			if( courseStartDate.after(maxEndDate) ) {
				errorMessage = "Start Date cannot be after any Entitlement's End Date.";
				throwException(errorMessage);
			}
		}
		
		if( contractStartDatesList != null && !contractStartDatesList.isEmpty() ) {
			Date minStartDate = Collections.min(contractStartDatesList);
			if( courseStartDate.before(minStartDate) ) {
				errorMessage = "Start Date cannot be before any Entitlement's Start Date.";
				throwException(errorMessage);
			}
		}
		
		log.debug("entitlementLevelEnrollmentValidations() end for customer: " + customer.getCustomerCode() + 
				" having course: " + course.getCourseGUID());
		return true;
	}
	
	public List<CustomerEntitlement> getCourseValidEntitlementList(Customer customer, com.softech.vu360.lms.model.Course course, 
			Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		
		log.debug("entitlementLevelEnrollmentValidations() start for customer: " + customer.getCustomerCode() + 
				" having course: " + course.getCourseGUID());
			
		String errorMessage = null;
		GregorianCalendar todayDate = getTodayDate();
	    List<CustomerEntitlement> customerEntitlementList = getCustomerEntitlementListForCourse(customer, course);
			
		if ( customerEntitlementList.isEmpty() ) { //if no contract found then exit
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not exist for customer: " + customer.getCustomerCode();
			throwException(errorMessage);
		} 
			
		List<Date> contractEndDatesList = new ArrayList <Date>();
		List<Date> contractStartDatesList = new ArrayList <Date>();
		
		for( CustomerEntitlement ent : customerEntitlementList ) {	
			boolean isSeatAvailable = false;
			if (!ent.isAllowUnlimitedEnrollments()) {
				if (ent.hasAvailableSeats(1)) {
					isSeatAvailable = true;		
				}
			} else {
				isSeatAvailable = true;
			}
			if (isSeatAvailable) {
				if ( ent.getEndDate() != null && !ent.getEndDate().before(todayDate.getTime())) {																
					   contractEndDatesList.add(ent.getEndDate());
					   contractStartDatesList.add(ent.getStartDate());								
				} else{
					Calendar c = Calendar.getInstance();
					c.setTime(ent.getStartDate()); // Now use entitlement start date.
					c.add(Calendar.DATE, ent.getDefaultTermOfServiceInDays()); // Adding days
					Date entEndDate = c.getTime();
					
					if(!entEndDate.before(todayDate.getTime())){ 
					   contractEndDatesList.add(entEndDate);
					   contractStartDatesList.add(ent.getStartDate());
					}
				}	
			}
		} //end of for for( CustomerEntitlement ent : ...)
		
		if (contractStartDatesList.isEmpty()) {
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not have seats available for customer: " + customer.getCustomerCode();
			throwException(errorMessage);
		}
		
		if (contractEndDatesList.isEmpty()) {
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not have seats available for customer: " + customer.getCustomerCode();
			throwException(errorMessage);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
		Date courseStartDate = enrollmentStartDate;
		Date courseEndDate = enrollmentEndDate;
		
		if ( courseStartDate.before(todayDate.getTime()) ) {							
			
			Date minEndDate = Collections.min(contractEndDatesList);
			String minEnrollmentEndDate = sdf.format(minEndDate);
			String todaysDate = sdf.format(todayDate.getTime());
			
			errorMessage = "Start date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
			throwException(errorMessage);
		} 
		
		if( contractEndDatesList != null && !contractEndDatesList.isEmpty() ) {
			Date maxEndDate = Collections.max(contractEndDatesList); 
			if( courseStartDate.after(maxEndDate) ) {
				errorMessage = "Start Date cannot be after any Entitlement's End Date.";
				throwException(errorMessage);
			}
		}
		
		if( contractStartDatesList != null && !contractStartDatesList.isEmpty() ) {
			Date minStartDate = Collections.min(contractStartDatesList);
			if( courseStartDate.before(minStartDate) ) {
				errorMessage = "Start Date cannot be before any Entitlement's Start Date.";
				throwException(errorMessage);
			}
		}
		
		if (enrollmentEndDate != null) {
			
			//LMS-15933
			if (courseEndDate.before(courseStartDate))
			{
				errorMessage = "Start date can not be after End Date";
				throwException(errorMessage);
			}
			//LMS-15933	
			if ( courseEndDate.before(todayDate.getTime()) ) {							
				
				Date minEndDate = Collections.min(contractEndDatesList);
				String minEnrollmentEndDate = sdf.format(minEndDate);
				String todaysDate = sdf.format(todayDate.getTime());
				
				errorMessage = "End date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
				throwException(errorMessage);
			} 
			
			if( contractEndDatesList != null && !contractEndDatesList.isEmpty() ) {
				Date maxEndDate = Collections.max(contractEndDatesList);
				if( courseEndDate.after(maxEndDate) ) {
					
					String minEnrollmentEndDate = sdf.format(maxEndDate);
					errorMessage = "End Date must not be later than " + minEnrollmentEndDate;
					throwException(errorMessage);
					
				}
			}
		}
	
		log.debug("entitlementLevelEnrollmentValidations() end for customer: " + customer.getCustomerCode() + 
				" having course: " + course.getCourseGUID());
		return customerEntitlementList;
	}
	
	private List<CustomerEntitlement> getCustomerEntitlementListForCourse(Customer customer, com.softech.vu360.lms.model.Course course) {
		List<CustomerEntitlement> customerEntitlementList = entitlementService.getCustomerEntitlementsByCourseId(customer, course.getId());
		return customerEntitlementList;
	} //end of getCustomerEntitlementListForCourse()
	
	private GregorianCalendar getTodayDate() {
		
		GregorianCalendar todayDate =new GregorianCalendar();
		todayDate.setTime(new Date());
		todayDate.set(GregorianCalendar.HOUR_OF_DAY,0);
		todayDate.set(GregorianCalendar.MINUTE,0);
		todayDate.set(GregorianCalendar.SECOND,0);
		todayDate.set(GregorianCalendar.MILLISECOND,0);
		
		return todayDate;
		
	} //end of getTodayDate()
	
	public boolean refreshCoursesCache(List<String> courseGuidList) throws Exception {
		try {
			if (courseGuidList != null && !courseGuidList.isEmpty()) {
				String[] courseGuidArray = courseGuidList.toArray(new String[courseGuidList.size()]);
				refreshCoursesCache(courseGuidArray);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return true;
	}
	
	public boolean refreshCoursesCache(String[] courseGuidArray) throws Exception {
		try {
				List<com.softech.vu360.lms.model.Course> refreshCoursesList = courseCourseGrpService.refreshCoursesCache(courseGuidArray);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return true;
	}
	
	public boolean refreshCoursesInCache(List<com.softech.vu360.lms.model.Course> courseList) throws Exception {
		try {
			if (courseList != null && !courseList.isEmpty() ) {
				List<String> courseGuidList = new ArrayList<String>();
				for (com.softech.vu360.lms.model.Course course : courseList) {
					String courseGUID = course.getCourseGUID();
					if (!StringUtils.isEmpty(courseGUID) && !StringUtils.isBlank(courseGUID)) {
						courseGuidList.add(courseGUID);
					}
				}
				refreshCoursesCache(courseGuidList);
			}	
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return true;
	}
	
	public boolean updateCoursesCache(List<Course> courseList) throws Exception {
		try {
			if (courseList != null && !courseList.isEmpty() ) {
				List<String> courseGuidList = new ArrayList<String>();
				for (Course course : courseList) {
					String courseGUID = course.getCourseID();
					if (!StringUtils.isEmpty(courseGUID) && !StringUtils.isBlank(courseGUID)) {
						courseGuidList.add(courseGUID);
					}
				}
				refreshCoursesCache(courseGuidList);
			}	
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return true;
	}
	
	
	
	
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}

}
