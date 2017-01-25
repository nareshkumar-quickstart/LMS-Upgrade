package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CourseForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String courseName = null;
	private String courseID = null;
	private String description = null;
	
	public CourseForm() {
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
}