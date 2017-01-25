package com.softech.vu360.lms.web.controller.model;

import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class EnrollmentCourse  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Course course = null;
	private boolean selected = false;
	private String enrollmentStartDate = null; 
	private String enrollmentEndDate = null; 
	private String selectedSyncClassId=null;
	
	public String getSelectedSyncClassId() {
		return selectedSyncClassId;
	}
	public void setSelectedSyncClassId(String selectedSyncClassId) {
		this.selectedSyncClassId = selectedSyncClassId;
	}
	private List<SynchronousClass> synchronousClass=null;
	
	public List<SynchronousClass> getSynchronousClass() {
		return synchronousClass;
	}
	public void setSynchronousClass(List<SynchronousClass> synchronousClass) {
		this.synchronousClass = synchronousClass;
	}
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
	public String getEnrollmentStartDate() {
		return enrollmentStartDate;
	}
	public void setEnrollmentStartDate(String enrollmentStartDate) {
		this.enrollmentStartDate = enrollmentStartDate;
	}
	public String getEnrollmentEndDate() {
		return enrollmentEndDate;
	}
	public void setEnrollmentEndDate(String enrollmentEndDate) {
		this.enrollmentEndDate = enrollmentEndDate;
	}
}