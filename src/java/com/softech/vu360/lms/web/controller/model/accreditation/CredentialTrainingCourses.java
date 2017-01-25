package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CredentialTrainingCourses implements ILMSBaseInterface{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String courseTitle;
	private String bussinesskey;
	private boolean selected = false;
	
	//private Course trainingCourse ;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	
	public String getBussinesskey() {
		return bussinesskey;
	}
	public void setBussinesskey(String bussinesskey) {
		this.bussinesskey = bussinesskey;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	/*
	public Course getTrainingCourse() {
		return trainingCourse;
	}
	public void setTrainingCourse(Course trainingCourse) {
		this.trainingCourse = trainingCourse;
	}*/
	
	
	
	
}