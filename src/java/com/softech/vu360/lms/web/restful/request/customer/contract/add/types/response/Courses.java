package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

import java.util.List;

public class Courses {

	private String errorCode;
	private String errorMessage;
    private List<Course> course;
    private InvalidCourses invalidCourses;
    
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public List<Course> getCourse() {
		return course;
	}
	
	public void setCourse(List<Course> course) {
		this.course = course;
	}

	public InvalidCourses getInvalidCourses() {
		return invalidCourses;
	}

	public void setInvalidCourses(InvalidCourses invalidCourses) {
		this.invalidCourses = invalidCourses;
	}

}
