package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class LearnerCourse  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String  learnerName ="";
	private List<Course> courses = new ArrayList<Course>();
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public String getLearnerName() {
		return learnerName;
	}
	public void setLearnerName(String learnerName) {
		this.learnerName = learnerName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
