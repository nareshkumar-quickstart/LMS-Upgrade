package com.softech.vu360.util;

import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.SelfPacedCourse;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.WebinarCourse;

public class CourseUtil {

	static private String[] courseTypes = { 
			"All", 
			"Discussion Forum",
			"SCORM Package", 
			SynchronousCourse.COURSE_TYPE, 
			"Weblink Course",
			HomeworkAssignmentCourse.COURSE_TYPE,
			InstructorConnectCourse.COURSE_TYPE,
			SelfPacedCourse.COURSE_TYPE,
			WebinarCourse.COURSE_TYPE
			};
	
	
	public static String[] courseTypes() {
		return courseTypes;
	}
	
	
	
}
