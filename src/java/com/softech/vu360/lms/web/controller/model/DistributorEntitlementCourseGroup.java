package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class DistributorEntitlementCourseGroup  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private CourseGroup courseGroup;
	
	public DistributorEntitlementCourseGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public CourseGroup getCourseGroup() {
		return courseGroup;
	}

	public void setCourseGroup(CourseGroup courseGroup) {
		this.courseGroup = courseGroup;
	}
}
