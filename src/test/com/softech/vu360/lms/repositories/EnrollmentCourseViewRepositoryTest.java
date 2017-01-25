package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.EnrollmentCourseView;

public class EnrollmentCourseViewRepositoryTest extends SpringJUnitConfigAbstractTest {

	@Inject
	private EnrollmentCourseViewRepository enrollmentCourseViewRepository;
	
	//@Test
	public void getCoursesForEnrollmentWithPaging() {
		Long customerId = 60455L; 
		String courseName = "Dev Testing Course KK";
		String courseCode = "1";
		String contractName = "The Able Few";
		int startIndex = 1; 
		int endIndex = 10;
		
		try {
			List<EnrollmentCourseView> enrollmentCourseViews = enrollmentCourseViewRepository.getCoursesForEnrollment(customerId, courseName, courseCode, contractName, startIndex, endIndex);
		    System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void getCoursesForEnrollment() {
		Long customerId = 60455L; 
		String courseName = "Dev Testing Course KK";
		String courseCode = "1";
		String contractName = "The Able Few";
		Date expirationDate = null;
		
		try {
			List<EnrollmentCourseView> enrollmentCourseViews = enrollmentCourseViewRepository.getCoursesForEnrollment(customerId, courseName, courseCode, contractName, expirationDate);
		    System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
