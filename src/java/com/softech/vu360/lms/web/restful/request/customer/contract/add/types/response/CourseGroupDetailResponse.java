package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

public class CourseGroupDetailResponse {

	private CourseGroups courseGroups;
	private String errorMessage;
	private String errorCode;
	
	public CourseGroups getCourseGroups() {
		return courseGroups;
	}
	
	public void setCourseGroups(CourseGroups courseGroups) {
		this.courseGroups = courseGroups;
	}
	
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
