package com.softech.vu360.lms.service.lmsapi;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.Course;

public interface CourseAndCourseGroupServiceLmsApi {
	
	
	public com.softech.vu360.lms.model.Course validateCourseGuid(String courseGUID) throws Exception;
	public boolean updateCoursesCache(List<Course> courseList) throws Exception;
	public boolean refreshCoursesInCache(List<com.softech.vu360.lms.model.Course> courseList) throws Exception;
	
	public boolean isValidCourse(Customer customer, com.softech.vu360.lms.model.Course course) throws Exception;
	public com.softech.vu360.lms.model.Course getValidCourse(Customer customer, String courseGUID) throws Exception;
	public Map<String, Object> getCoursesMap(Customer customer, List<String> courseGuidList) throws Exception;
	
	public boolean courseLevelEnrollmentValidations(com.softech.vu360.lms.model.Course course) throws Exception; 
	public List<CustomerEntitlement> getCourseValidEntitlementList(Customer customer, com.softech.vu360.lms.model.Course course, 
			Date enrollmentStartDate, Date enrollmentEndDate) throws Exception;
	
}
