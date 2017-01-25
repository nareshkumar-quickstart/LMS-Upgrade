package com.softech.vu360.lms.service.impl.lmsapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.lmsapi.CustomerEntitlementServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.EnrollmentServiceLmsApi;

public class EnrollmentServiceImplLmsApi implements EnrollmentServiceLmsApi {

	private static final Logger log = Logger.getLogger(EnrollmentServiceImplLmsApi.class.getName());
	
	private CustomerEntitlementServiceLmsApi customerEntitlementServiceLmsApi;
	private EnrollmentService enrollmentService;
	private StatisticsService statsService;
	
	public Map<String, Map<String, ?>> doEnrollment(Customer customer, Learner learner, 
			Map<com.softech.vu360.lms.model.Course, Object> courseEntitlementMap, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		
		log.debug("doEnrollment() start for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		
		Map<String, Map<String, ?>> courseMap = new HashMap<String, Map<String, ?>>();
		Map<com.softech.vu360.lms.model.Course, String> enrollErrorMap = new HashMap<com.softech.vu360.lms.model.Course, String>();
		Map<String, Map<com.softech.vu360.lms.model.Course, String>> courseErrorMap = new HashMap<String, Map<com.softech.vu360.lms.model.Course, String>>();
		Map<String, List<LearnerEnrollment>> courseSuccessMap = new HashMap<String, List<LearnerEnrollment>>();
		List<LearnerEnrollment> learnerEnrollmentList = new ArrayList<LearnerEnrollment>();
		for (Map.Entry<com.softech.vu360.lms.model.Course, Object> entry : courseEntitlementMap.entrySet()) {
			//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
			com.softech.vu360.lms.model.Course course = entry.getKey();
			Object value = entry.getValue();
			
			if (value instanceof String) {
				String errorMessage = (String)value;
				enrollErrorMap.put(course, errorMessage);
			} else if (value instanceof List<?>) {
				List<CustomerEntitlement> customerEntitlementList = (List<CustomerEntitlement>)value;
				try {
					LearnerEnrollment newEnrollment = getEnrollmentForCourse(customer, learner, course, enrollmentStartDate, enrollmentEndDate);
					learnerEnrollmentList.add(newEnrollment);
				} catch (Exception e) {
					String errorMessage = e.getMessage();
					enrollErrorMap.put(course, errorMessage);
				}
			}	
		} //end of for()
		
		if (!learnerEnrollmentList.isEmpty()) {
			courseSuccessMap.put(learner.getVu360User().getUsername(), learnerEnrollmentList);
		}
		
		if (!enrollErrorMap.isEmpty()) {
			courseErrorMap.put(learner.getVu360User().getUsername(), enrollErrorMap);
		}
		
		if (!courseSuccessMap.isEmpty()) {
			courseMap.put("successfulCoursesMap", courseSuccessMap);
		}
		
		if (!courseErrorMap.isEmpty()) {
			courseMap.put("unSuccessfulCoursesMap", courseErrorMap);
		}
		
		log.debug("doEnrollment() end for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		return courseMap;
	
	} //end of doEnrollment()
	
	/**
	 * Gets existing enrollments or creates a new enrollment for user 
	 * @param course_id
	 * @param customer_id
	 * @param context
	 * @param cust
	 * @param course
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private LearnerEnrollment getEnrollmentForCourse(Customer customer,  Learner learner, com.softech.vu360.lms.model.Course course, 
			Date enrollmentStartDate, Date enrollmentEndDate)throws Exception {
		
		log.debug("getEnrollmentForCourse() start for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		String errorMessage = null;
		List<CustomerEntitlement> customerEntitlementList = customerEntitlementServiceLmsApi.getCustomerEntitlementListForCourse(customer, course);
		if ( customerEntitlementList.isEmpty() ) { //if no contract found then exit
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not exist for customer: " + customer.getName();
			throwException(errorMessage);
		} 
		
		Map<String, Object> customerEntitlementMap = customerEntitlementServiceLmsApi.getCustomerEntitlementHavingMaximumEntitlementEndDate(customerEntitlementList, enrollmentStartDate, enrollmentEndDate);
		Object customerEntitlement = customerEntitlementMap.get("customerEntitlement");
		
		// catter Available seats are less than the no. of users to be enrolled.
		if (customerEntitlement == null) {
			errorMessage = "Entitlement to course: "+ course.getCourseGUID() +" has expired or does not have seats available for customer: " + customer.getCustomerCode();
			throwException(errorMessage);
		}
		
		CustomerEntitlement custEntitlement = (CustomerEntitlement)customerEntitlement;
		
		LearnerEnrollment result = getEnrollmentForCourse(customer, learner, course, custEntitlement, enrollmentStartDate, enrollmentEndDate);
		return result;
	}
	
	private LearnerEnrollment getEnrollmentForCourse(Customer customer,  Learner learner, com.softech.vu360.lms.model.Course course,
			CustomerEntitlement customerEntitlement, Date enrollmentStartDate, Date enrollmentEndDate)throws Exception {
		
		log.debug("getEnrollmentForCourse() start for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		
		//Get active enrollment for course. 
		LearnerEnrollment enrollment = getLearnerEnrollment(learner, course);
		
		//If no active enrollment exists, enroll into a valid contract
		if (enrollment == null ) {
			enrollment = addEnrollment(learner, customerEntitlement, course, enrollmentStartDate, enrollmentEndDate);
		} else {
			try {
				boolean result = learnerEnrollmentValidations(enrollment);
				if (result) {
					enrollment = addEnrollment(learner, customerEntitlement, course, enrollmentStartDate, enrollmentEndDate);
				}
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				throwException(errorMessage);
			}
		}
		log.debug("getEnrollmentForCourse() end for learner: " + learner.getId() + ", " + learner.getVu360User().getUsername());
		return enrollment;
	}
	
	private LearnerEnrollment getLearnerEnrollment(Learner learner, com.softech.vu360.lms.model.Course course) {
		
		//Get active enrollment for course. 
		LearnerEnrollment learnerEnrollment = enrollmentService.getAICCLearnerEnrollment(null, learner.getId(),course.getId());
		return learnerEnrollment;
	}
	
	private boolean learnerEnrollmentValidations(LearnerEnrollment enrollment) throws Exception {
		
		log.debug("learnerEnrollmentValidations() start for learner: " + enrollment.getLearner().getId() + ", " + enrollment.getLearner().getVu360User().getUsername());
		boolean isExpired = false;
		String errorMessage = null;
		if (!(enrollment.getEnrollmentEndDate().compareTo(new Date(System.currentTimeMillis()))<0)) {    // date1.compareTo(date2)<0 --> Date1 is before Date2
			errorMessage = "Already Enrolled";
			throwException(errorMessage);
		} else {
			isExpired = true;
		}
		
		if (isExpired) {
			return true;
		}
		
		String enrollmentStatus = enrollment.getEnrollmentStatus();
		if (enrollmentStatus.equalsIgnoreCase("active")) {
			errorMessage = "Already Enrolled";
			throwException(errorMessage);
		}
		
		LearnerCourseStatistics courseStats= statsService.loadForUpdateLearnerCourseStatistics(enrollment.getCourseStatistics().getId());
		if (!courseStats.isCourseCompleted()) {
			errorMessage = "Already Enrolled";
			throwException(errorMessage);
		}
		log.debug("learnerEnrollmentValidations() start for learner: " + enrollment.getLearner().getId() + ", " + enrollment.getLearner().getVu360User().getUsername());
		return true;
	}
	
	private LearnerEnrollment addEnrollment(Learner learner, CustomerEntitlement custEntitlement, 
			com.softech.vu360.lms.model.Course course, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception{
		
		// create new enrollment
		log.debug("creating new enrollment");
		LearnerEnrollment newEnrollment = new LearnerEnrollment();
		newEnrollment.setCourse(course);
		newEnrollment.setCustomerEntitlement(custEntitlement);
		newEnrollment.setLearner(learner);
		//newEnrollment.setEnrollmentStartDate(new Date(System.currentTimeMillis()));
		newEnrollment.setEnrollmentStartDate(enrollmentStartDate);
		newEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
		//newEnrollment.setEnrollmentEndDate(custEntitlement.getEntitlementEndDate());
		newEnrollment.setEnrollmentEndDate(enrollmentEndDate);
		log.debug("calling service to create enrollment");
		//LearnerEnrollment enrollment = enrollmentService.addEnrollment(learner, custEntitlement, null, newEnrollment);
		
		OrgGroupEntitlement orgGroupContract = null;
		LearnerEnrollment enrollment = enrollmentService.addEnrollment(newEnrollment, custEntitlement, orgGroupContract);
		log.debug("creating new enrollment complete");
		return enrollment;
	}
	
	public List<LearnerEnrollment> getLearnerSuccessfullCoursesEnrolledList(Learner learner, Map<String, Map<String, ?>> courseMap) throws Exception {
		
		String errrorMessage = null;
		
		if (learner == null) {
			errrorMessage = "Learner is null";
			throwException(errrorMessage);
		}
		
		if (courseMap == null || courseMap.isEmpty()) {
			errrorMessage = "Course map is null or empty";
			throwException(errrorMessage);
		}
		
		Object successMap = courseMap.get("successfulCoursesMap");
		if (successMap == null) {
			return null;
		}
		
		Map<String, List<LearnerEnrollment>> courseSuccessMap = (Map<String, List<LearnerEnrollment>>)successMap;
		List<LearnerEnrollment> successfullCoursesEnrolledList = courseSuccessMap.get(learner.getVu360User().getUsername());
		
		if (successfullCoursesEnrolledList != null && !successfullCoursesEnrolledList.isEmpty()) {
			return successfullCoursesEnrolledList;
		}
		
		return null;
		
	} //end of getLearnerSuccessfullCoursesEnrolledList
	
	public Map<com.softech.vu360.lms.model.Course, String> getLearnerUnsuccessfullCoursesErrorMap(Learner learner, Map<String, Map<String, ?>> courseMap) throws Exception {
		
		String errrorMessage = null;
		
		if (learner == null) {
			errrorMessage = "Learner is null";
			throwException(errrorMessage);
		}
		
		if (courseMap == null || courseMap.isEmpty()) {
			errrorMessage = "Course map is null or empty";
			throwException(errrorMessage);
		}
		
		Object errorMap = courseMap.get("unSuccessfulCoursesMap");
		if (errorMap == null) {
			return null;
		}
		
		Map<String, Map<com.softech.vu360.lms.model.Course, String>> courseErrorMap = (Map<String, Map<com.softech.vu360.lms.model.Course, String>>)errorMap;
		for (Map.Entry<String, Map<com.softech.vu360.lms.model.Course, String>> courseErrorEntry : courseErrorMap.entrySet()) {
			//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
			String userName = courseErrorEntry.getKey();
			if (userName.equals(learner.getVu360User().getUsername())) {
				Map<com.softech.vu360.lms.model.Course, String> enrollErrorMap = courseErrorEntry.getValue();
				return enrollErrorMap;	
			}	
		}
		
		return null;
		
	} //end of getLearnerUnsuccessfullCoursesErrorMap()
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}
	
	
}
