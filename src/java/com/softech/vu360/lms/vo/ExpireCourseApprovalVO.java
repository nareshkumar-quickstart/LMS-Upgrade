package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.LearnerEnrollment;

public class ExpireCourseApprovalVO implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	private String courseName;
	private String courseApprovalName;
	private Date courseApprovalStartDate;
	private Date courseApprovalExpirationDate;
	private List<LearnerEnrollment> enrollments;
	
	
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseApprovalName() {
		return courseApprovalName;
	}
	
	public void setCourseApprovalName(String courseApprovalName) {
		this.courseApprovalName = courseApprovalName;
	}
	
	public Date getCourseApprovalStartDate() {
		return courseApprovalStartDate;
	}
	
	public void setCourseApprovalStartDate(Date courseApprovalStartDate) {
		this.courseApprovalStartDate = courseApprovalStartDate;
	}
	
	public Date getCourseApprovalExpirationDate() {
		return courseApprovalExpirationDate;
	}
	
	public void setCourseApprovalExpirationDate(Date courseApprovalExpirationDate) {
		this.courseApprovalExpirationDate = courseApprovalExpirationDate;
	}
	
	public List<LearnerEnrollment> getEnrollments() {
		return enrollments;
	}
	
	public void setEnrollments(List<LearnerEnrollment> enrollments) {
		this.enrollments = enrollments;
	}
	
}
