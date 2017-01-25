package com.softech.vu360.lms.web.controller.model;

import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.vo.LearnerLicenseAlertVO;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


public class LearnerLicenseAlertForm  implements ILMSBaseInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date triggerSingleDate=null;
	private String expirationDate=null;
    



	private String licensealertname;
	private Long licensealertId;
    private boolean before = false;
    private boolean after = false; 
	private Integer daysAfterEvent = null;
	private Integer daysBeforeEvent = null;
	private Long licenseoflearnerId;
    private Integer days=null;
	private List<LicenseOfLearner> licenseoflearner;
	private LicenseOfLearner selectedLicenseoflearner;
	private List<LearnerLicenseAlertVO> learnerlicensealertvo;
	
	public String getExpirationDate() {
		return expirationDate;
	}




	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	
	public Date getTriggerSingleDate() {
		return triggerSingleDate;
	}




	public void setTriggerSingleDate(Date triggerSingleDate) {
		this.triggerSingleDate = triggerSingleDate;
	}
	
	public Long getLicensealertId() {
		return licensealertId;
	}




	public void setLicensealertId(Long licensealertId) {
		this.licensealertId = licensealertId;
	}

	public Long getLicenseoflearnerId() {
		return licenseoflearnerId;
	}




	public void setLicenseoflearnerId(Long licenseoflearnerId) {
		this.licenseoflearnerId = licenseoflearnerId;
	}
	

	public List<LearnerLicenseAlertVO> getLearnerlicensealertvo() {
		return learnerlicensealertvo;
	}




	public void setLearnerlicensealertvo(
			List<LearnerLicenseAlertVO> learnerlicensealertvo) {
		this.learnerlicensealertvo = learnerlicensealertvo;
	}




	public Integer getDays() {
		return days;
	}




	public List<LicenseOfLearner> getLicenseoflearner() {
		return licenseoflearner;
	}




	public void setLicenseoflearner(List<LicenseOfLearner> licenseoflearner) {
		this.licenseoflearner = licenseoflearner;
	}




	public void setDays(Integer days) {
		this.days = days;
	}




	public Integer getDaysAfterEvent() {
		return daysAfterEvent;
	}




	public void setDaysAfterEvent(Integer daysAfterEvent) {
		this.daysAfterEvent = daysAfterEvent;
	}




	public Integer getDaysBeforeEvent() {
		return daysBeforeEvent;
	}




	public void setDaysBeforeEvent(Integer daysBeforeEvent) {
		this.daysBeforeEvent = daysBeforeEvent;
	}




	public boolean isBefore() {
		return before;
	}




	public void setBefore(boolean before) {
		this.before = before;
	}




	public boolean isAfter() {
		return after;
	}




	public void setAfter(boolean after) {
		this.after = after;
	}




	public String getLicensealertname() {
		return licensealertname;
	}




	public void setLicensealertname(String licensealertname) {
		this.licensealertname = licensealertname;
	}




	public LicenseOfLearner getSelectedLicenseoflearner() {
		return selectedLicenseoflearner;
	}




	public void setSelectedLicenseoflearner(
			LicenseOfLearner selectedLicenseoflearner) {
		this.selectedLicenseoflearner = selectedLicenseoflearner;
	}


	

}
