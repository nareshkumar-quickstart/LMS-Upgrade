package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class TrainingCourseGroup  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private CourseGroup courseGroup;
	private boolean selected = false;
	
	public CourseGroup getCourseGroup() {
		return courseGroup;
	}
	public void setCourseGroup(CourseGroup courseGroup) {
		this.courseGroup = courseGroup;
	}
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}