package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class CourseApprovalVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String courseApprovalId;
	private String holdingRegulator;
	private String approvedCredithours;
	private String creditType;
	private String credentialName;
	
	public String getHoldingRegulator() {
		return holdingRegulator;
	}
	public void setHoldingRegulator(String holdingRegulator) {
		this.holdingRegulator = holdingRegulator;
	}
	public String getCreditType() {
		return creditType;
	}
	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}
	public String getCourseApprovalId() {
		return courseApprovalId;
	}
	public void setCourseApprovalId(String courseApprovalId) {
		this.courseApprovalId = courseApprovalId;
	}
	public String getApprovedCredithours() {
		return approvedCredithours;
	}
	public void setApprovedCredithours(String approvedCredithours) {
		this.approvedCredithours = approvedCredithours;
	}
	public String getCredentialName() {
		return credentialName;
	}
	public void setCredentialName(String credentialName) {
		this.credentialName = credentialName;
	}
	
	
	
}
