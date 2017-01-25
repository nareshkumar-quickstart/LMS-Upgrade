package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

import java.util.List;

public class CourseGroups {

	private String errorCode;
	private String errorMessage;
    private List<CourseGroup> courseGroup;
    private InvalidCourseGroups invalidCourseGroups;
    
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
	
	public List<CourseGroup> getCourseGroup() {
		return courseGroup;
	}
	
	public void setCourseGroup(List<CourseGroup> courseGroup) {
		this.courseGroup = courseGroup;
	}

	public InvalidCourseGroups getInvalidCourseGroups() {
		return invalidCourseGroups;
	}

	public void setInvalidCourseGroups(InvalidCourseGroups invalidCourseGroups) {
		this.invalidCourseGroups = invalidCourseGroups;
	}
	
}
