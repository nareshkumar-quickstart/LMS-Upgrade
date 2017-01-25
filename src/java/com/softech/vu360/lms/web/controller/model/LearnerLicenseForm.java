package com.softech.vu360.lms.web.controller.model;

import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.IndustryCredential;
import com.softech.vu360.lms.model.LearnerLicenseType;
import com.softech.vu360.lms.model.LicenseIndustry;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author haider.ali
 *
 */
public class LearnerLicenseForm  implements ILMSBaseInterface {

	private static final long serialVersionUID = 1L;

	private Long Id; //learner License ID



	private String sendEmailParm;
	private String redirectTo;
	private List<IndustryCredential> credentials;
	private List<LicenseIndustry> licenseIndustries;
	private List<LearnerLicenseType> learnerRequestedLicenseTypes;//useless
	
	private Long licenseIndustryId; 
	private String licenseIndustryName; 
	private String licenseOrCertificate;
	private String state;
	
	
    private Long selectedCredentialId;




	private Long selectedLearnerLicenseTypesId;
	private String learnerRequestedLicenseTypeName;
	private LicenseOfLearner licenseOfLearner;
	private String supportingInformation;
	private Date originalLicensedate;
	private String emailtextarea;

	public Date getOriginalLicensedate() {
		return originalLicensedate;
	}


	public void setOriginalLicensedate(Date originalLicensedate) {
		this.originalLicensedate = originalLicensedate;
	}


	public Long getSelectedCredentialId() {
		return selectedCredentialId;
	}


	public void setSelectedCredentialId(Long selectedCredentialId) {
		this.selectedCredentialId = selectedCredentialId;
	}
	
	
	public List<IndustryCredential> getCredentials() {
		return credentials;
	}


	public void setCredentials(List<IndustryCredential> credentials) {
		this.credentials = credentials;
	}


	public Long getId() {
		return Id;
	}


	public void setId(Long id) {
		Id = id;
	}


	public String getSupportingInformation() {
		return supportingInformation;
	}


	public void setSupportingInformation(String supportingInformation) {
		this.supportingInformation = supportingInformation;
	}


	public List<LearnerLicenseType> getLearnerRequestedLicenseTypes() {
		return learnerRequestedLicenseTypes;
	}


	public void setLearnerRequestedLicenseTypes(
			List<LearnerLicenseType> learnerRequestedLicenseTypes) {
		this.learnerRequestedLicenseTypes = learnerRequestedLicenseTypes;
	}


	public String getLicenseOrCertificate() {
		return licenseOrCertificate;
	}


	public void setLicenseOrCertificate(String licenseOrCertificate) {
		this.licenseOrCertificate = licenseOrCertificate;
	}


	public Long getLicenseIndustryId() {
		return licenseIndustryId;
	}


	public void setLicenseIndustryId(Long licenseIndustryId) {
		this.licenseIndustryId = licenseIndustryId;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getSendEmailParm() {
		return sendEmailParm;
	}

	
	public List<LicenseIndustry> getLicenseIndustries() {
		return licenseIndustries;
	}



	public void setLicenseIndustries(List<LicenseIndustry> licenseIndustries) {
		this.licenseIndustries = licenseIndustries;
	}



	public void setSendEmailParm(String sendEmailParm) {
		this.sendEmailParm = sendEmailParm;
	}


	public String getLicenseIndustryName() {
		return licenseIndustryName;
	}


	public void setLicenseIndustryName(String licenseIndustryName) {
		this.licenseIndustryName = licenseIndustryName;
	}


	public Long getSelectedLearnerLicenseTypesId() {
		return selectedLearnerLicenseTypesId;
	}


	public void setSelectedLearnerLicenseTypesId(Long selectedLearnerLicenseTypesId) {
		this.selectedLearnerLicenseTypesId = selectedLearnerLicenseTypesId;
	}


	public String getLearnerRequestedLicenseTypeName() {
		return learnerRequestedLicenseTypeName;
	}


	public void setLearnerRequestedLicenseTypeName(
			String learnerRequestedLicenseTypeName) {
		this.learnerRequestedLicenseTypeName = learnerRequestedLicenseTypeName;
	}


	public LicenseOfLearner getLicenseOfLearner() {
		return licenseOfLearner;
	}


	public void setLicenseOfLearner(LicenseOfLearner licenseOfLearner) {
		this.licenseOfLearner = licenseOfLearner;
	}


	public String getEmailtextarea() {
		return emailtextarea;
	}


	public void setEmailtextarea(String emailtextarea) {
		this.emailtextarea = emailtextarea;
	}


	public String getRedirectTo() {
		return redirectTo;
	}


	public void setRedirectTo(String redirectTo) {
		this.redirectTo = redirectTo;
	}


	


	
	
}
