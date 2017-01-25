/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "PROVIDERAPPROVAL")
@DiscriminatorValue("ProviderApproval")
public class ProviderApproval extends  RegulatoryApproval {
	//private Long id;
	@Column(name = "APPROVEDPROVIDERNAME")
    private String approvedProviderName = null;
	
	@Column(name = "PROVIDERAPPROVALNUMBER")
    private String providerApprovalNumber = null;
	
	@Column(name = "PROVIDERAPPROVALSTATUS")
    private String providerApprovalStatus = null;
	
	@Column(name = "PROVIDERAPPROVALPERIOD")
    private String providerApprovalPeriod = null;
	
	@Column(name = "PROVIDERDIRECTOR")
    private String providerDirector = null;
	
	@Column(name = "PROVIDERINSTRUCTOROFRECORD")
    private String providerInstructorOfRecord = null;
	
	@Column(name = "PROVIDERAPPROVALEFFECTIVELYSTARTSDATE")
    private Date providerApprovalEffectivelyStartDate =  null;
	
	@Column(name = "COURSEAPPROVALEFFECTIVELYENDSDATE")
    private Date courseApprovalEffectivelyEndDate = null;
	
	@Column(name = "PROVIDERMOSTRECENTLYSUBMITTEDFORAPPROVALDATE")
    private Date providerMostRecentlySubmittedForApprovalDate = null;
	
	@Column(name = "PROVIDERORIGINALLYAPPROVEDDATE")
    private Date providerOriginallyApprovedDate = null;
	
	@Column(name = "PROVIDERSUBMISSIONREMINDERDATE")
    private Date providerSubmissionReminderDate = null;
	
	@Column(name = "OTHERPROVIDERREPRESENTATIVE")
    private String otherProviderRepresentative = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="PROVIDER_ID")
    private Provider provider = null;
    
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="DERIVEDFROM_ID")
    private ProviderApproval derivedFrom = null;
    
    @Column(name = "ACTIVE")
    private Boolean active = true;
  
   
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	
	/**
	 * @return the id
	 */

	public ProviderApproval() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the id
	 */
	/*public Long getId() {
		return id;
	}
	*//**
	 * @param id the id to set
	 *//*
	public void setId(Long id) {
		this.id = id;
	}*/
	/**
	 * @return the approvedProviderName
	 */
	public String getApprovedProviderName() {
		return approvedProviderName;
	}
	/**
	 * @param approvedProviderName the approvedProviderName to set
	 */
	public void setApprovedProviderName(String approvedProviderName) {
		this.approvedProviderName = approvedProviderName;
	}
	/**
	 * @return the providerApprovalNumber
	 */
	public String getProviderApprovalNumber() {
		return providerApprovalNumber;
	}
	/**
	 * @param providerApprovalNumber the providerApprovalNumber to set
	 */
	public void setProviderApprovalNumber(String providerApprovalNumber) {
		this.providerApprovalNumber = providerApprovalNumber;
	}
	/**
	 * @return the providerApprovalStatus
	 */
	public String getProviderApprovalStatus() {
		return providerApprovalStatus;
	}
	/**
	 * @param providerApprovalStatus the providerApprovalStatus to set
	 */
	public void setProviderApprovalStatus(String providerApprovalStatus) {
		this.providerApprovalStatus = providerApprovalStatus;
	}
	/**
	 * @return the providerApprovalPeriod
	 */
	public String getProviderApprovalPeriod() {
		return providerApprovalPeriod;
	}
	/**
	 * @param providerApprovalPeriod the providerApprovalPeriod to set
	 */
	public void setProviderApprovalPeriod(String providerApprovalPeriod) {
		this.providerApprovalPeriod = providerApprovalPeriod;
	}
	/**
	 * @return the providerDirector
	 */
	public String getProviderDirector() {
		return providerDirector;
	}
	/**
	 * @param providerDirector the providerDirector to set
	 */
	public void setProviderDirector(String providerDirector) {
		this.providerDirector = providerDirector;
	}
	/**
	 * @return the providerInstructorOfRecord
	 */
	public String getProviderInstructorOfRecord() {
		return providerInstructorOfRecord;
	}
	/**
	 * @param providerInstructorOfRecord the providerInstructorOfRecord to set
	 */
	public void setProviderInstructorOfRecord(String providerInstructorOfRecord) {
		this.providerInstructorOfRecord = providerInstructorOfRecord;
	}
	/**
	 * @return the providerApprovalEffectivelyStartDate
	 */
	public Date getProviderApprovalEffectivelyStartDate() {
		return providerApprovalEffectivelyStartDate;
	}
	/**
	 * @param providerApprovalEffectivelyStartDate the providerApprovalEffectivelyStartDate to set
	 */
	public void setProviderApprovalEffectivelyStartDate(
			Date providerApprovalEffectivelyStartDate) {
		this.providerApprovalEffectivelyStartDate = providerApprovalEffectivelyStartDate;
	}
	/**
	 * @return the courseApprovalEffectivelyEndDate
	 */
	public Date getCourseApprovalEffectivelyEndDate() {
		return courseApprovalEffectivelyEndDate;
	}
	/**
	 * @param courseApprovalEffectivelyEndDate the courseApprovalEffectivelyEndDate to set
	 */
	public void setCourseApprovalEffectivelyEndDate(
			Date courseApprovalEffectivelyEndDate) {
		this.courseApprovalEffectivelyEndDate = courseApprovalEffectivelyEndDate;
	}
	/**
	 * @return the providerMostRecentlySubmittedForApprovalDate
	 */
	public Date getProviderMostRecentlySubmittedForApprovalDate() {
		return providerMostRecentlySubmittedForApprovalDate;
	}
	/**
	 * @param providerMostRecentlySubmittedForApprovalDate the providerMostRecentlySubmittedForApprovalDate to set
	 */
	public void setProviderMostRecentlySubmittedForApprovalDate(
			Date providerMostRecentlySubmittedForApprovalDate) {
		this.providerMostRecentlySubmittedForApprovalDate = providerMostRecentlySubmittedForApprovalDate;
	}
	/**
	 * @return the providerOriginallyApprovedDate
	 */
	public Date getProviderOriginallyApprovedDate() {
		return providerOriginallyApprovedDate;
	}
	/**
	 * @param providerOriginallyApprovedDate the providerOriginallyApprovedDate to set
	 */
	public void setProviderOriginallyApprovedDate(
			Date providerOriginallyApprovedDate) {
		this.providerOriginallyApprovedDate = providerOriginallyApprovedDate;
	}
	/**
	 * @return the providerSubmissionReminderDate
	 */
	public Date getProviderSubmissionReminderDate() {
		return providerSubmissionReminderDate;
	}
	/**
	 * @param providerSubmissionReminderDate the providerSubmissionReminderDate to set
	 */
	public void setProviderSubmissionReminderDate(
			Date providerSubmissionReminderDate) {
		this.providerSubmissionReminderDate = providerSubmissionReminderDate;
	}
	/**
	 * @return the otherProviderRepresentative
	 */
	public String getOtherProviderRepresentative() {
		return otherProviderRepresentative;
	}
	/**
	 * @param otherProviderRepresentative the otherProviderRepresentative to set
	 */
	public void setOtherProviderRepresentative(String otherProviderRepresentative) {
		this.otherProviderRepresentative = otherProviderRepresentative;
	}
	public String getApprovalType(){
		return "PROVIDERAPPROVAL";
	}
	/**
	 * @return the provider
	 */
	public Provider getProvider() {
		return provider;
	}
	/**
	 * @param provider the provider to set
	 */
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	/**
	 * @return the derivedFrom
	 */
	public ProviderApproval getDerivedFrom() {
		return derivedFrom;
	}
	/**
	 * @param derivedFrom the derivedFrom to set
	 */
	public void setDerivedFrom(ProviderApproval derivedFrom) {
		this.derivedFrom = derivedFrom;
	}
	/**
	 * @return the active
	 */
	public  Boolean isActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
}
