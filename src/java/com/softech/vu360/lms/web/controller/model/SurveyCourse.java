package com.softech.vu360.lms.web.controller.model;

import java.sql.Timestamp;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SurveyCourse implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	
	private Timestamp linkingDate;	
	
	private Course course;
	public SurveyCourse() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}
	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
	}
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Timestamp getLinkingDate() {
		return linkingDate;
	}
	
	public void setLinkingDate(Timestamp linkingDate) {
		this.linkingDate = linkingDate;
	}
}
