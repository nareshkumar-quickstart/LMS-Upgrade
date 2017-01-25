package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class LearnerLicenseAlertVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long learnerLicenseAlertId;
	private String learnerLicenseAlertName;
	
	public Long getLearnerLicenseAlertId() {
		return learnerLicenseAlertId;
	}
	public void setLearnerLicenseAlertId(Long learnerLicenseAlertId) {
		this.learnerLicenseAlertId = learnerLicenseAlertId;
	}
	public String getLearnerLicenseAlertName() {
		return learnerLicenseAlertName;
	}
	public void setLearnerLicenseAlertName(String learnerLicenseAlertName) {
		this.learnerLicenseAlertName = learnerLicenseAlertName;
	}
	
}
