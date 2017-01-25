package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class TrainingCourse  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Course course;
	private boolean selected = false;
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}