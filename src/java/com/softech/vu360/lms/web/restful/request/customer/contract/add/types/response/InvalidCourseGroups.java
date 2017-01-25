package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

import com.softech.vu360.lms.web.restful.request.customer.contract.add.types.CourseGroups;

public class InvalidCourseGroups extends CourseGroups {

	private String errorMessage;
	private String errorCode;
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
