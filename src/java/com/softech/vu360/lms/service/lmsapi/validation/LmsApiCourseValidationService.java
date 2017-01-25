package com.softech.vu360.lms.service.lmsapi.validation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;

public interface LmsApiCourseValidationService {

	List<Course> getValidCourses(Customer customer, Map<String, Course> coursesMap, Date todayDate, Date enrollmentStartDate, Date enrollmentEndDate);
	Map<String, String> getInValidCourses(Customer customer, Map<String, Course> coursesMap, Date todayDate, Date enrollmentStartDate, Date enrollmentEndDate);
	
}
