package com.softech.vu360.lms.service.impl.lmsapi.validation;

import static com.softech.vu360.lms.webservice.validation.lmsapi.CoursePredicate.isCourseNull;
import static com.softech.vu360.lms.webservice.validation.lmsapi.CoursePredicate.isCoursePublished;
import static com.softech.vu360.lms.webservice.validation.lmsapi.CoursePredicate.isCourseRetired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.lmsapi.LmsApiEntitlementService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiCourseValidationService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiEnrollmentValidationService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiEntitlementValidationService;

@Service
public class LmsApiCourseValidationServiceImpl implements LmsApiCourseValidationService {

	@Autowired
	private EntitlementService entitlementService;
	
	@Autowired
	private LmsApiEntitlementService lmsApiEntitlementService;
	
	@Autowired
	private LmsApiEntitlementValidationService lmsApiEntitlementValidationService;
	
	@Autowired
	private LmsApiEnrollmentValidationService lmsApiEnrollmentValidationService;
	
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	
	public void setLmsApiEntitlementService(LmsApiEntitlementService lmsApiEntitlementService) {
		this.lmsApiEntitlementService = lmsApiEntitlementService;
	}
	
	public void setLmsApiEntitlementValidationService(LmsApiEntitlementValidationService lmsApiEntitlementValidationService) {
		this.lmsApiEntitlementValidationService = lmsApiEntitlementValidationService;
	}

	public void setLmsApiEnrollmentValidationService(LmsApiEnrollmentValidationService lmsApiEnrollmentValidationService) {
		this.lmsApiEnrollmentValidationService = lmsApiEnrollmentValidationService;
	}

	@Override
	public List<Course> getValidCourses(Customer customer, Map<String, Course> coursesMap, Date todayDate, Date enrollmentStartDate, Date enrollmentEndDate) {
		List<Course> courses = null;
		if (!CollectionUtils.isEmpty(coursesMap)) {
			courses = new ArrayList<>();
			for (Map.Entry<String, Course> entry : coursesMap.entrySet()) {
				Course course = entry.getValue();
				if (isValidCourseForEnrollment(customer, course, todayDate, enrollmentStartDate, enrollmentEndDate)) {
					courses.add(course);
				}
			}
		}
		return courses;
	}

	@Override
	public Map<String, String> getInValidCourses(Customer customer, Map<String, Course> coursesMap, Date todayDate, Date enrollmentStartDate, Date enrollmentEndDate) {
		Map<String, String> invalidCoursesMap = null;
		if (!CollectionUtils.isEmpty(coursesMap)) {
			invalidCoursesMap = new HashMap<>();
			
			String customerCode = customer.getCustomerCode();
			for (Map.Entry<String, Course> entry : coursesMap.entrySet()) {
				
				String courseGuid = entry.getKey();
				Course course = entry.getValue();
				if (!isValidCourseForEnrollment(customer, course, todayDate, enrollmentStartDate, enrollmentEndDate)) {
					String errorMessage = "";
					if (!isValidAtCourseLevel(course)) {
						errorMessage = getCourseLevelErrorMessage(courseGuid, course);
					} else {
						List<CustomerEntitlement> customerEntitlements = entitlementService.getCustomerEntitlementsByCourseId(customer, course.getId());
						errorMessage = lmsApiEntitlementValidationService.getEntitlementLevelErrorMessage(customerCode, courseGuid, customerEntitlements, todayDate);
						if (StringUtils.isBlank(errorMessage)) {
							errorMessage = lmsApiEnrollmentValidationService.getEnrollmentLevelErrorMessage(customerEntitlements, todayDate, enrollmentStartDate, enrollmentEndDate);
						}
					}
					invalidCoursesMap.put(courseGuid, errorMessage);
				}
			}
		}
		
		return invalidCoursesMap;
	}
	
	private boolean isValidCourseForEnrollment(Customer customer, Course course, Date todayDate, Date enrollmentStartDate, Date enrollmentEndDate) {
		boolean validCourse = false;
		boolean validAtCourselevel = isValidAtCourseLevel(course);
		boolean validAtEntitlementLevel = false;
		boolean validAtEnrollmentLevel = false;
		if (validAtCourselevel) {
			Map<Boolean, List<CustomerEntitlement>> seatsAvailableEntitlementsMap = lmsApiEntitlementService.getSeatAvailableEntitlementsMap(customer, course.getId());
			validAtEntitlementLevel = lmsApiEntitlementValidationService.isValidCourseEntitlement(seatsAvailableEntitlementsMap, todayDate);
			if (validAtEntitlementLevel) {
				validAtEnrollmentLevel = isValidAtEnrollmentLevel(seatsAvailableEntitlementsMap, todayDate, enrollmentStartDate, enrollmentEndDate);
			}
		}
		
		if (validAtCourselevel && validAtEntitlementLevel && validAtEnrollmentLevel) {
			validCourse = true;
		}
		
		return validCourse;
	}
	
	private boolean isValidAtCourseLevel(Course course) {
		return !isCourseNull(course) &&
			   isCoursePublished(course) &&
			   !isCourseRetired(course);
	}
	
	private String getCourseLevelErrorMessage(String courseGuid, Course course) {
		
		String errorMessage = "";
		
		if (isCourseNull(course)) {
			errorMessage = "No course found for " + courseGuid;
		} else if (!isCoursePublished(course)) {
			errorMessage = "Course is not published.";
		} else if (isCourseRetired(course)) {
			errorMessage = "Course is retired";
		}
		return errorMessage;
	}
	
	private boolean isValidAtEnrollmentLevel(Map<Boolean, List<CustomerEntitlement>> seatsAvailableEntitlementsMap, Date todayDate, Date enrollmentStartDate, Date enrollmentEndDate) {
		
		boolean validAtEnrollmentLevel = false;
		if (!CollectionUtils.isEmpty(seatsAvailableEntitlementsMap)) {
			List<CustomerEntitlement> seatsAvailableCustomerEntitlements  = seatsAvailableEntitlementsMap.get(Boolean.TRUE);
			if (!CollectionUtils.isEmpty(seatsAvailableCustomerEntitlements)) {
				List<Date> entitlementStartDates = lmsApiEntitlementService.getEntitlementStartDates(seatsAvailableCustomerEntitlements, todayDate);
				List<Date> entitlementEndDates = lmsApiEntitlementService.getEntitlementEndDates(seatsAvailableCustomerEntitlements, todayDate);
				validAtEnrollmentLevel = lmsApiEnrollmentValidationService.isValidAtEnrollmentLevel(enrollmentStartDate, enrollmentEndDate, entitlementStartDates, entitlementEndDates);
			}
		}
		return validAtEnrollmentLevel;
	}

}
