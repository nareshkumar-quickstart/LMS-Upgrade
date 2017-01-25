package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class TrainingCustomerEntitlement  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private CustomerEntitlement customerEntitlement;
	private List<TrainingCourse> courses = new ArrayList <TrainingCourse>();
	private boolean selected = false;
	private boolean expired = false;
	//private List<TrainingCourseGroup> courseGroups = new ArrayList <TrainingCourseGroup>();
	
	public CustomerEntitlement getCustomerEntitlement() {
		return customerEntitlement;
	}
	public void setCustomerEntitlement(CustomerEntitlement customerEntitlement) {
		this.customerEntitlement = customerEntitlement;
	}
	public List<TrainingCourse> getCourses() {
		return courses;
	}
	public void setCourses(List<TrainingCourse> courses) {
		this.courses = courses;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}