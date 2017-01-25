package com.softech.vu360.lms.service.impl.lmsapi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.lmsapi.CourseAndCourseGroupServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.CustomerEntitlementServiceLmsApi;

public class CustomerEntitlementServiceImplLmsApi implements CustomerEntitlementServiceLmsApi {
	
	private static final Logger log = Logger.getLogger(CustomerEntitlementServiceImplLmsApi.class.getName());
	
	private CourseAndCourseGroupServiceLmsApi courseAndCourseGroupServiceLmsApi;
	private EntitlementService entitlementService;
	
	public CourseAndCourseGroupServiceLmsApi getCourseAndCourseGroupServiceLmsApi() {
		return courseAndCourseGroupServiceLmsApi;
	}

	public void setCourseAndCourseGroupServiceLmsApi(CourseAndCourseGroupServiceLmsApi courseAndCourseGroupServiceLmsApi) {
		this.courseAndCourseGroupServiceLmsApi = courseAndCourseGroupServiceLmsApi;
	}
	
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	@Override
	public Map<String, Object> getCustomerEntitlementHavingMaximumEntitlementEndDate(List<CustomerEntitlement> customerEntitlementList, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		
		CustomerEntitlement custEntitlement = null;
		Date maxEntitlementEndDate = null;
		Map<String, Object> customerEntitlementMap = new HashMap<String, Object>();
		Map<CustomerEntitlement, Object> customerEntitlementErrorMap = new HashMap<CustomerEntitlement, Object>();
		
		//Get that entitlement that has maximum entitlement end date
		for( CustomerEntitlement ent : customerEntitlementList ) {
			try {
				entitlementValidation(ent, enrollmentStartDate, enrollmentEndDate);
				if (maxEntitlementEndDate == null) {
					maxEntitlementEndDate = ent.getEntitlementEndDate();
					custEntitlement = ent;
				} else {
					if (ent.getEntitlementEndDate().after(maxEntitlementEndDate)) {
						maxEntitlementEndDate = ent.getEntitlementEndDate();
						custEntitlement = ent;
					}
				}
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				customerEntitlementErrorMap.put(ent, errorMessage);
			}
			
		} //end of for()
		
		if (custEntitlement != null) {
			customerEntitlementMap.put("customerEntitlement", custEntitlement);
		}
		
		if (!customerEntitlementErrorMap.isEmpty()) {
			customerEntitlementMap.put("customerEntitlementErrorMap", customerEntitlementErrorMap);
		}
		
		return customerEntitlementMap;
	}
	
	private boolean entitlementValidation(CustomerEntitlement customerEntitlement, Date enrollmentStartDate, Date enrollmentEndDate) 
			throws Exception {

		log.debug("entitlementValidation() start for CustomerEntitlement: " + customerEntitlement.getId() + ": " + 
		customerEntitlement.getName());	
		
		String errorMessage = null;
		GregorianCalendar todayDate = getTodayDate();	
		Date contractEndDate = null;
		Date contractStartDate = null;
								
		if( customerEntitlement.getEndDate() != null && !customerEntitlement.getEndDate().before(todayDate.getTime())) {																
			contractEndDate = customerEntitlement.getEndDate();
			contractStartDate = customerEntitlement.getStartDate();								
		}else{	
			Calendar c = Calendar.getInstance();
			c.setTime(customerEntitlement.getStartDate()); // Now use entitlement start date.
			c.add(Calendar.DATE, customerEntitlement.getDefaultTermOfServiceInDays()); // Adding days
			Date entEndDate = c.getTime();
					
			if(!entEndDate.before(todayDate.getTime())){ 
				contractEndDate = entEndDate;
				contractStartDate= customerEntitlement.getStartDate();
			}
		}													
			
		if (contractStartDate == null) {
			errorMessage = "Entitlement has expired";
			throwException(errorMessage);
		}
			
		if (contractEndDate == null) {
			errorMessage = "Entitlement has expired";
			throwException(errorMessage);
		}
			
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");	
		if ( enrollmentStartDate.before(todayDate.getTime()) ) {
			String minEnrollmentEndDate = sdf.format(contractEndDate);
			String todaysDate = sdf.format(todayDate.getTime());
			errorMessage = "Start date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
			throwException(errorMessage);
		} 
							
		if( enrollmentStartDate.after(contractEndDate) ) {
			errorMessage = "Start Date cannot be after any Entitlement's End Date.";
			throwException(errorMessage);
		}
			
		if( enrollmentStartDate.before(contractStartDate) ) {
			errorMessage = "Start Date cannot be before any Entitlement's Start Date.";
			throwException(errorMessage);
		}
		
		if (enrollmentEndDate != null) {
			
			if (enrollmentEndDate.before(enrollmentStartDate))
			{
				errorMessage = "Start date can not be after End Date";
				throwException(errorMessage);
			}
			//LMS-15933	
			if ( enrollmentEndDate.before(todayDate.getTime()) ) {							
				
				Date minEndDate = enrollmentEndDate;
				String minEnrollmentEndDate = sdf.format(minEndDate);
				String todaysDate = sdf.format(todayDate.getTime());
				
				errorMessage = "End date must be in between " + todaysDate + " and " + minEnrollmentEndDate;
				throwException(errorMessage);
			} 
			
			if( enrollmentEndDate.after(contractEndDate) ) {	
				String minEnrollmentEndDate = sdf.format(contractEndDate);
				errorMessage = "End Date must not be later than " + minEnrollmentEndDate;
				throwException(errorMessage);		
			}	
		}
		
		boolean isSeatAvailable = false;
		if (!customerEntitlement.isAllowUnlimitedEnrollments()) {
			if (customerEntitlement.hasAvailableSeats(1)) {
				isSeatAvailable = true;		
			}
		} else {
			isSeatAvailable = true;
		}
					
		if (!isSeatAvailable) {
			errorMessage = "Seats not available in Customer Entitlement: " + customerEntitlement.getId() + ": " + customerEntitlement.getName();
			throwException(errorMessage);
		}
			
		log.debug("entitlementValidation() end for CustomerEntitlement: " + customerEntitlement.getId() + ": " + 
				customerEntitlement.getName());	
		return true;
	}
	
	public List<CustomerEntitlement> getCustomerEntitlementListForCourse(Customer customer, com.softech.vu360.lms.model.Course course) {
		List<CustomerEntitlement> customerEntitlementList = entitlementService.getCustomerEntitlementsByCourseId(customer, course.getId());
		return customerEntitlementList;
	} //end of getCustomerEntitlementListForCourse()
	
	public List<CustomerEntitlement> getValidCustomerEntitlementList(Customer customer, com.softech.vu360.lms.model.Course course, 
			Date enrollmentStartDate, Date enrollmentEndDate) throws Exception {
		courseAndCourseGroupServiceLmsApi.courseLevelEnrollmentValidations(course);
		List<CustomerEntitlement> customerEntitlementList = courseAndCourseGroupServiceLmsApi.getCourseValidEntitlementList(customer, course, enrollmentStartDate, enrollmentEndDate);
		return customerEntitlementList;
		
	}
	
	private GregorianCalendar getTodayDate() {
		
		GregorianCalendar todayDate =new GregorianCalendar();
		todayDate.setTime(new Date());
		todayDate.set(GregorianCalendar.HOUR_OF_DAY,0);
		todayDate.set(GregorianCalendar.MINUTE,0);
		todayDate.set(GregorianCalendar.SECOND,0);
		todayDate.set(GregorianCalendar.MILLISECOND,0);
		
		return todayDate;
		
	} //end of getTodayDate()
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}

}
