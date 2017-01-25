package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class UserForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields = new ArrayList<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField>();
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields=new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
	private VU360User vu360User;
	private String eventSource = null;
	private List<LicenseOfLearner> learnerOfLicense=null;
	private boolean notifyOnLicenseExpire;
	/**
	 * @return the creditReportingFields
	 */
	public List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> getCreditReportingFields() {
		return creditReportingFields;
	}
	/**
	 * @param creditReportingFields the creditReportingFields to set
	 */
	public void setCreditReportingFields(
			List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields) {
		this.creditReportingFields = creditReportingFields;
	}
	/**
	 * @return the vu360User
	 */
	public VU360User getVu360User() {
		return vu360User;
	}
	/**
	 * @param vu360User the vu360User to set
	 */
	public void setVu360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}
	/**
	 * @return the eventSource
	 */
	public String getEventSource() {
		return eventSource;
	}
	/**
	 * @param eventSource the eventSource to set
	 */
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}

	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFields() {
		return customFields;
	}
	public void setCustomFields(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		this.customFields = customFields;
	}
	
	public List<LicenseOfLearner> getLearnerOfLicense() {
		return learnerOfLicense;
	}
	public void setLearnerOfLicense(List<LicenseOfLearner> learnerOfLicense) {
		this.learnerOfLicense = learnerOfLicense;
	}
	
	public boolean isNotifyOnLicenseExpire() {
		return notifyOnLicenseExpire;
	}
	
	public void setNotifyOnLicenseExpire(boolean notifyOnLicenseExpire) {
		this.notifyOnLicenseExpire = notifyOnLicenseExpire;
	}
	
	
}
