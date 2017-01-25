package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author haider.ali
 *
 */
public class ManageLicenseForm implements ILMSBaseInterface {

	private static final long serialVersionUID = 1L;
	private Long[] licenseOfLearnerId;
	private long[] licenseAlertId;
	
	private String licenseType = "";
	private List<LicenseOfLearner> licenseOfLearner = new ArrayList<LicenseOfLearner>();
	private List<LearnerLicenseAlert> licenseOfLearnerAlert = new ArrayList<LearnerLicenseAlert>();
	
	
	
	
	public Long[] getLicenseOfLearnerId() {
		return licenseOfLearnerId;
	}
	public void setLicenseOfLearnerId(Long[] licenseOfLearnerId) {
		this.licenseOfLearnerId = licenseOfLearnerId;
	}
	public long[] getLicenseAlertId() {
		return licenseAlertId;
	}
	public void setLicenseAlertId(long[] licenseAlertId) {
		this.licenseAlertId = licenseAlertId;
	}
	public List<LicenseOfLearner> getLicenseOfLearner() {
		return licenseOfLearner;
	}
	public void setLicenseOfLearner(List<LicenseOfLearner> licenseOfLearner) {
		this.licenseOfLearner = licenseOfLearner;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public List<LearnerLicenseAlert> getLicenseOfLearnerAlert() {
		return licenseOfLearnerAlert;
	}
	public void setLicenseOfLearnerAlert(
			List<LearnerLicenseAlert> licenseOfLearnerAlert) {
		this.licenseOfLearnerAlert = licenseOfLearnerAlert;
	}

	
	
}
