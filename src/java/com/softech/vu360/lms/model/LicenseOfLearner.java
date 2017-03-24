package com.softech.vu360.lms.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "LICENSE_LEARNER")
public class LicenseOfLearner  implements SearchableKey {

	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "LICENSE_LEARNER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LICENSE_LEARNER", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LICENSE_LEARNER_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="licenseindustry_id")
	private LicenseIndustry licenseIndustry ;
	
	@OneToOne
    @JoinColumn(name="INDUSTRIY_CREDENTIAL_ID")
	private IndustryCredential industryCredential ;
	
	@Column(name="LICENSE_CERTIFICATE")
	private String licenseCertificate;
	
	@Column(name="LICENSE_STATE")
	private String state;
	
	@OneToOne
    @JoinColumn(name="LICENSETYPE_LEARNER")
	private LearnerLicenseType learnerLicenseType ;
	
	@Column(name="SUPPORTINGINFORMAION")
	private String supportingInformation;
	
	@Column(name="ACTIVE")
	private Boolean active	;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LEARNER_ID")
	private Learner learner ; 
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="UPDATEBY")
	private VU360User updatedBy ;
	
	@Column(name="UPDATEON")
	private Timestamp updateOn; 
	
	
	
	public IndustryCredential getIndustryCredential() {
		return industryCredential;
	}
	public void setIndustryCredential(IndustryCredential industryCredential) {
		this.industryCredential = industryCredential;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public LicenseIndustry getLicenseIndustry() {
		return licenseIndustry;
	}
	public void setLicenseIndustry(LicenseIndustry licenseIndustry) {
		this.licenseIndustry = licenseIndustry;
	}
	public String getLicenseCertificate() {
		return licenseCertificate;
	}
	public void setLicenseCertificate(String licenseCertificate) {
		this.licenseCertificate = licenseCertificate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getSupportingInformation() {
		return supportingInformation;
	}
	public void setSupportingInformation(String supportingInformation) {
		this.supportingInformation = supportingInformation;
	}
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	public Timestamp getUpdateOn() {
		return updateOn;
	}
	public void setUpdateOn(Timestamp updateOn) {
		this.updateOn = updateOn;
	}
	
	public LearnerLicenseType getLearnerLicenseType() {
		return learnerLicenseType;
	}
	public void setLearnerLicenseType(LearnerLicenseType learnerLicenseType) {
		this.learnerLicenseType = learnerLicenseType;
	}
	public Learner getLearner() {
		return learner;
	}
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	public VU360User getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(VU360User updatedBy) {
		this.updatedBy = updatedBy;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
