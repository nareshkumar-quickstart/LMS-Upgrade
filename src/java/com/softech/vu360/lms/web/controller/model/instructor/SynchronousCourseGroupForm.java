package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


 

public class SynchronousCourseGroupForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String[] groups;
	private String newGroupName = "";
	private String parentGroupName = "";
	private long parentCourseGroupId = 0;
	private long courseGroupId = 0;

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public String getNewGroupName() {
		return newGroupName;
	}

	public void setNewGroupName(String newGroupName) {
		this.newGroupName = newGroupName;
	}

	public String getParentGroupName() {
		return parentGroupName;
	}

	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}

	/**
	 * @param parentCourseGroupId the parentCourseGroupId to set
	 */
	public void setParentCourseGroupId(long parentCourseGroupId) {
		this.parentCourseGroupId = parentCourseGroupId;
	}

	/**
	 * @return the parentCourseGroupId
	 */
	public long getParentCourseGroupId() {
		return parentCourseGroupId;
	}

	/**
	 * @param courseGroupId the courseGroupId to set
	 */
	public void setCourseGroupId(long courseGroupId) {
		this.courseGroupId = courseGroupId;
	}

	/**
	 * @return the courseGroupId
	 */
	public long getCourseGroupId() {
		return courseGroupId;
	}
}
 
