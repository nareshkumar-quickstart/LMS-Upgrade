package com.softech.vu360.lms.service.impl.lmsapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.lmsapi.LmsApiCourseService;

@Service
public class LmsApiCourseServiceImpl implements LmsApiCourseService {

	@Autowired
	private CourseAndCourseGroupService courseCourseGrpService;
	
	public void setCourseCourseGrpService(CourseAndCourseGroupService courseCourseGrpService) {
		this.courseCourseGrpService = courseCourseGrpService;
	}

	@Override
	public Map<String, Course> getCoursesMap(List<String> coursesGuid) {
		Map<String, Course> courses = null;
		if (!CollectionUtils.isEmpty(coursesGuid)) {
			courses = new HashMap<>();
			for (String courseGuid : coursesGuid) {
				try {
				    Course course = courseCourseGrpService.getCourseByGUID(courseGuid);
				    courses.put(courseGuid, course);
				} catch(Exception e) {
					courses.put(courseGuid, null);
				}
			} 
		}
		return courses;
	}

}
