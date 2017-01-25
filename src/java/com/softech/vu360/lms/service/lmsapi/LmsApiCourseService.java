package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Course;

public interface LmsApiCourseService {

	Map<String, Course> getCoursesMap(List<String> coursesGuid);
	
}
