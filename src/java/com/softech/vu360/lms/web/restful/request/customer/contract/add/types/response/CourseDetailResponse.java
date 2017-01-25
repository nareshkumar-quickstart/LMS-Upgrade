package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

public class CourseDetailResponse {

	private String errorMessage;
	private String errorCode;
	private Courses courses;
	
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
	
	public Courses getCourses() {
		return courses;
	}
	
	public void setCourses(Courses courses) {
		this.courses = courses;
	}
	
}
