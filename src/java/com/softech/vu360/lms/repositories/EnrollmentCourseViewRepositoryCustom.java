package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.EnrollmentCourseView;

public interface EnrollmentCourseViewRepositoryCustom {

	List<EnrollmentCourseView> getCoursesForEnrollment(Long customerId, String courseName, String courseCode, String contractName, int startIndex, int endIndex);
	List<EnrollmentCourseView> getCoursesForEnrollment(Long customerId, String courseName, String courseCode, String contractName, Date date, Long[] customerEntitlementIds);
	
}
