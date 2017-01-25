package com.softech.vu360.lms.webservice;

import com.softech.vu360.lms.webservice.message.webBasedCourseCreation.CourseCreationResponse;
import com.softech.vu360.lms.webservice.message.webBasedCourseCreation.WebBasedCourseCreationRequest;


public interface WebBasedCourseCreationWS {
	public CourseCreationResponse WebBasedCourseCreationRequestCompleted(WebBasedCourseCreationRequest webBasedCourseCreationRequest);
	
}
