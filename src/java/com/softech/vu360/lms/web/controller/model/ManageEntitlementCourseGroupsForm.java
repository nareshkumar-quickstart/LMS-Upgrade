package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author muzammil.shaikh
 *
 */
public class ManageEntitlementCourseGroupsForm  implements ILMSBaseInterface{	
	private static final long serialVersionUID = 1L;
	String contractId = null;
	String[] courseGroups = null;
	String[] courses = null;
	String contractType = null;
	
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String[] getCourseGroups() {
		return courseGroups;
	}

	public void setCourseGroups(String[] courseGroups) {
		this.courseGroups = courseGroups;
	}

	public String[] getCourses() {
		return courses;
	}

	public void setCourses(String[] courses) {
		this.courses = courses;
	}

	public void reset() {
		courseGroups = null;
		courses = null;
		contractType = null;
	}

}