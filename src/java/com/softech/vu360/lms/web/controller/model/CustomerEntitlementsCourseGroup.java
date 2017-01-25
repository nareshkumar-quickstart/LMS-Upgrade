package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CustomerEntitlementsCourseGroup implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private CourseGroup courseGroup;
	
	
	public CustomerEntitlementsCourseGroup(boolean selected,
			CourseGroup courseGroup) {
		super();
		this.selected = selected;
		this.courseGroup = courseGroup;
	}


	public CustomerEntitlementsCourseGroup() {
		super();
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


	/**
	 * @return the courseGroup
	 */
	public CourseGroup getCourseGroup() {
		return courseGroup;
	}


	/**
	 * @param courseGroup the courseGroup to set
	 */
	public void setCourseGroup(CourseGroup courseGroup) {
		this.courseGroup = courseGroup;
	}
}
