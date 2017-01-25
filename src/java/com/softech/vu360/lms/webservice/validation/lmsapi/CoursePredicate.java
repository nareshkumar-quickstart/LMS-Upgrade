package com.softech.vu360.lms.webservice.validation.lmsapi;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Course;

public class CoursePredicate {

	public static boolean isCourseNull(Course course) {
		return course == null;
	}
	
	public static boolean isCourseRetired(Course course) {
		return course.isRetired();
	}
	
	public static boolean isCoursePublished(Course course) {
		return course.getCourseStatus().equalsIgnoreCase("Published");
	}
	
	public static boolean isValidCoursesGuid(List<String> coursesGuid) {
		return (!CollectionUtils.isEmpty(coursesGuid));
	}
}
