package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COURSEAPPROVAL_AUDIT")
public class CourseApprovalAudit implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "COURSEAPPROVAL_AUDIT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COURSEAPPROVAL_AUDIT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COURSEAPPROVAL_AUDIT_ID")
	private Long id;
	
	@Column(name = "APPROVEDCOURSENAME")
	private String approvedCourseName = null;
	
	@Column(name = "COURSEAPPROVALNUMBER")
	private String courseApprovalNumber = null;
	
	@Column(name = "COURSEAPPROVALTYPE")
	private String courseApprovaltype = null;
	
	@Column(name = "APPROVEDCREDITHOURS")
	private String approvedCreditHours = null;
	
	@Column(name = "COURSEAPPROVALSTATUS")
	private String courseApprovalStatus = null;
	
	@Column(name = "COURSEAPPROVALRENEWALFEE")
	private String courseApprovalRenewalFee = null;
	
	@Column(name = "COURSEAPPROVALSUBMISSIONFEE")
	private String courseApprovalSubmissionFee = null;
	
	@Column(name = "COURSEAPPROVALEFFECTIVELYSTARTDATE")
	private Date courseApprovalEffectivelyStartDate = null;
	
	@Column(name = "COURSEAPPROVALEFFECTIVELYENDSDATE")
	private Date courseApprovalEffectivelyEndsDate = null;
	
	@Column(name = "COURSESUBMISSIONREMINDERDATE")
	private Date courseSubmissionReminderDate = null;
	
	@Column(name = "MOSTRECENTLYSUBMITTEDFORAPPROVALDATE")
	private Date mostRecentlySubmittedForApprovalDate = null;
	
	@Column(name = "COURSEAPPROVALINFORMATION")
	private String courseApprovalInformation = null;
	
	@Column(name = "ORIGINALCOURSEAPPROVALFEE")
	private double originalCourseApprovalFee = 0;
	
	@Column(name = "TAGS")
	private String tag = null;

	@Column(name = "ACTIVE")
	private Boolean active = true;
	
	@Column(name = "USE_PURCHASED_CERTIFICATE_NO")
	private Boolean usePurchasedCertificateNumbers;
	
	@Column(name = "USE_CERTIFICATE_NUMBER_GENERATOR")
	private Boolean useCertificateNumberGenerator;
	
	@Column(name = "CERTIFICATENUMBERGENERATOR_PREFIX")
	private String certificateNumberGeneratorPrefix;
	
	@Column(name = "CERTIFICATENUMBERGENERATOR_NUMBERFORMAT")
	private String certificateNumberGeneratorNumberFormat;
	
	@Column(name = "CERTIFICATENUMBERGENERATOR_NEXTNUMBER")
	private long certificateNumberGeneratorNextNumber;
	
	@Column(name = "SELF_REPORTED")
	private Boolean selfReported;
	
	@Column(name = "ASSET_ID")
	private Long assetid;
	
	@Column(name = "AFFIDAVIT_ID")
	private Long affidavitid;
	
	@Column(name = "COURSECONFIGURATIONTEMPLATE_ID")
	private Long courseconfigurationtemplateid;
	
	@Column(name = "RENEWEDFROMCOURSEAPPROVAL_ID")
	private Long renewedfromcourseapprovalid;
	
	@Column(name = "PROVIDER_ID")
	private Long providerid;
	
	@Column(name = "COURSE_ID")
	private Long courseid;
	
	@Column(name = "COURSEAPPROVAL_ID")
	private Long courseApprovalId;
	
	@Column(name = "OPERATION")
	private String operation = null;
	
	@Column(name = "CREATEDON")
	private Date createdOn = null;
	
	@Column(name = "CREATEDBY")
	private Long createdBy = null;
	
	@Column(name = "UPDATEDON")
	private Date updatedOn = null;
	
	@Column(name = "UPDATEDBY")
	private Long updatedBy = null;
	
	
	public Long getCourseId() {
		return courseid;
	}
    public void setCourseId(Long courseid) {
		this.courseid = courseid;
	}
	public Long getId() {
		return id;
	}
    public void setId(Long id) {
		this.id = id;
	}
	public Long getAffidavitid() {
		return affidavitid;
	}
	public void setAffidavitid(Long affidavitid) {
		this.affidavitid = affidavitid;
	}
	public String getApprovedCourseName() {
		return approvedCourseName;
	}
	public void setApprovedCourseName(String approvedCourseName) {
		this.approvedCourseName = approvedCourseName;
	}
	public String getCourseApprovalNumber() {
		return courseApprovalNumber;
	}
	public void setCourseApprovalNumber(String courseApprovalNumber) {
		this.courseApprovalNumber = courseApprovalNumber;
	}
	public String getCourseApprovaltype() {
		return courseApprovaltype;
	}
	public void setCourseApprovaltype(String courseApprovaltype) {
		this.courseApprovaltype = courseApprovaltype;
	}
	public String getApprovedCreditHours() {
		return approvedCreditHours;
	}
	public void setApprovedCreditHours(String approvedCreditHours) {
		this.approvedCreditHours = approvedCreditHours;
	}
	public String getCourseApprovalStatus() {
		return courseApprovalStatus;
	}
	public void setCourseApprovalStatus(String courseApprovalStatus) {
		this.courseApprovalStatus = courseApprovalStatus;
	}
	public String getCourseApprovalRenewalFee() {
		return courseApprovalRenewalFee;
	}
	public void setCourseApprovalRenewalFee(String courseApprovalRenewalFee) {
		this.courseApprovalRenewalFee = courseApprovalRenewalFee;
	}
	public String getCourseApprovalSubmissionFee() {
		return courseApprovalSubmissionFee;
	}
	public void setCourseApprovalSubmissionFee(String courseApprovalSubmissionFee) {
		this.courseApprovalSubmissionFee = courseApprovalSubmissionFee;
	}
	public Date getCourseApprovalEffectivelyStartDate() {
		return courseApprovalEffectivelyStartDate;
	}
	public void setCourseApprovalEffectivelyStartDate(
			Date courseApprovalEffectivelyStartDate) {
		this.courseApprovalEffectivelyStartDate = courseApprovalEffectivelyStartDate;
	}
	public Date getCourseApprovalEffectivelyEndsDate() {
		return courseApprovalEffectivelyEndsDate;
	}
	public void setCourseApprovalEffectivelyEndsDate(
			Date courseApprovalEffectivelyEndsDate) {
		this.courseApprovalEffectivelyEndsDate = courseApprovalEffectivelyEndsDate;
	}
	public Date getCourseSubmissionReminderDate() {
		return courseSubmissionReminderDate;
	}
	public void setCourseSubmissionReminderDate(Date courseSubmissionReminderDate) {
		this.courseSubmissionReminderDate = courseSubmissionReminderDate;
	}
	public Date getMostRecentlySubmittedForApprovalDate() {
		return mostRecentlySubmittedForApprovalDate;
	}
	public void setMostRecentlySubmittedForApprovalDate(
			Date mostRecentlySubmittedForApprovalDate) {
		this.mostRecentlySubmittedForApprovalDate = mostRecentlySubmittedForApprovalDate;
	}
	public String getCourseApprovalInformation() {
		return courseApprovalInformation;
	}
	public void setCourseApprovalInformation(String courseApprovalInformation) {
		this.courseApprovalInformation = courseApprovalInformation;
	}
	public double getOriginalCourseApprovalFee() {
		return originalCourseApprovalFee;
	}
	public void setOriginalCourseApprovalFee(double originalCourseApprovalFee) {
		this.originalCourseApprovalFee = originalCourseApprovalFee;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public  Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public  Boolean isUsePurchasedCertificateNumbers() {
		return usePurchasedCertificateNumbers;
	}
	public void setUsePurchasedCertificateNumbers(
			 Boolean usePurchasedCertificateNumbers) {
		this.usePurchasedCertificateNumbers = usePurchasedCertificateNumbers;
	}
	public  Boolean isUseCertificateNumberGenerator() {
		return useCertificateNumberGenerator;
	}
	public void setUseCertificateNumberGenerator(
			 Boolean useCertificateNumberGenerator) {
		this.useCertificateNumberGenerator = useCertificateNumberGenerator;
	}
	public String getCertificateNumberGeneratorPrefix() {
		return certificateNumberGeneratorPrefix;
	}
	public void setCertificateNumberGeneratorPrefix(
			String certificateNumberGeneratorPrefix) {
		this.certificateNumberGeneratorPrefix = certificateNumberGeneratorPrefix;
	}
	public String getCertificateNumberGeneratorNumberFormat() {
		return certificateNumberGeneratorNumberFormat;
	}
	public void setCertificateNumberGeneratorNumberFormat(
			String certificateNumberGeneratorNumberFormat) {
		this.certificateNumberGeneratorNumberFormat = certificateNumberGeneratorNumberFormat;
	}
	public long getCertificateNumberGeneratorNextNumber() {
		return certificateNumberGeneratorNextNumber;
	}
	public void setCertificateNumberGeneratorNextNumber(
			long certificateNumberGeneratorNextNumber) {
		this.certificateNumberGeneratorNextNumber = certificateNumberGeneratorNextNumber;
	}
	public  Boolean isSelfReported() {
		return selfReported;
	}
	public void setSelfReported(Boolean selfReported) {
		this.selfReported = selfReported;
	}
	public Long getAssetid() {
		return assetid;
	}
	public void setAssetid(Long assetid) {
		this.assetid = assetid;
	}
	public Long getCourseconfigurationtemplateid() {
		return courseconfigurationtemplateid;
	}
	public void setCourseconfigurationtemplateid(Long courseconfigurationtemplateid) {
		this.courseconfigurationtemplateid = courseconfigurationtemplateid;
	}
	public Long getRenewedfromcourseapprovalid() {
		return renewedfromcourseapprovalid;
	}
	public void setRenewedfromcourseapprovalid(Long renewedfromcourseapprovalid) {
		this.renewedfromcourseapprovalid = renewedfromcourseapprovalid;
	}
	public Long getProviderid() {
		return providerid;
	}
	public void setProviderid(Long providerid) {
		this.providerid = providerid;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Long getCourseApprovalId() {
		return courseApprovalId;
	}
	public void setCourseApprovalId(Long courseApprovalId) {
		this.courseApprovalId = courseApprovalId;
	}


}
