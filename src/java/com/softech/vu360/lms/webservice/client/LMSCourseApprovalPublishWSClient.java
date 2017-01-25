package com.softech.vu360.lms.webservice.client;

import com.softech.vu360.lms.model.CourseApproval;

public interface LMSCourseApprovalPublishWSClient {
	
	public abstract String publishCourseApproval(CourseApproval objCA) throws Exception;

}
