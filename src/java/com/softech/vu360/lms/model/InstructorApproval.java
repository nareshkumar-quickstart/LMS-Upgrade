package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "INSTRUCTORAPPROVAL")
@DiscriminatorValue("InstructorApproval")
public class InstructorApproval  extends RegulatoryApproval {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "APPROVEDINSTRUCTORNAME")
	private String approvedInstructorName = null;
	
	@Column(name = "INSTRUCTORAPPROVALNUMBER")
	private String instructorApprovalNumber = null;
	
	@Column(name = "INSTRUCTORAPPROVALPERIOD")
	private String instructorApprovalperiod = null;
	
	@Column(name = "INSTRUCTORAPPROVALSTATUS")
	private String instructorApprovalStatus = null;
	
	@Column(name = "INITIALINSTRUCTORAPPROVALFEE")
	private String instructorApprovalFee = null;
	
	@Column(name = "INSTRUCTORRENEWALFEE")
	private String instructorRenewalFee = null;
	
	@Column(name = "PROVIDERINSTRUCTOROFRECORD")
	private String providerInstructorOfRecord= null;
	
	@Column(name = "INSTRUCTORAPPROVALEFFECTIVELYSTARTSDATE")
	private Date instructorApprovalEffectivelyStartDate = null;
	
	@Column(name = "INSTRUCTORAPPROVALEFFECTIVELYENDSDATE")
	private Date instructorApprovalEffectivelyEndsDate = null;
	
	@Column(name = "INSTRUCTORSUBMISSIONREMINDERDATE")
	private Date instructorSubmissionReminderDate = null;
	
	@Column(name = "INSTRUCTORMOSTRECENTLYSUBMITTEDFORAPPROVALDATE")
	private Date mostRecentlySubmittedForApprovalDate = null;
	
	@Column(name = "INSTRUCTORORIGINALLYAPPROVED")
	private Date inspectorOriginallyApprovalDate = null;
	
	@Transient
	private String InstructorApprovalInformation = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="INSTRUCTOR_ID")
	private Instructor Instructor = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="COURSE_ID")
	private Course course = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="PROVIDER_ID")
	private Provider provider = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="DERIVEDFROM_ID")
	private InstructorApproval derivedFrom = null;
	
	@Column(name = "ACTIVE")
	private Boolean active = true;
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public InstructorApproval() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the approvedInstructorName
	 */
	public String getApprovedInstructorName() {
		return approvedInstructorName;
	}

	/**
	 * @param approvedInstructorName the approvedInstructorName to set
	 */
	public void setApprovedInstructorName(String approvedInstructorName) {
		this.approvedInstructorName = approvedInstructorName;
	}

	/**
	 * @return the instructorApprovalNumber
	 */
	public String getInstructorApprovalNumber() {
		return instructorApprovalNumber;
	}

	/**
	 * @param instructorApprovalNumber the instructorApprovalNumber to set
	 */
	public void setInstructorApprovalNumber(String instructorApprovalNumber) {
		this.instructorApprovalNumber = instructorApprovalNumber;
	}

	/**
	 * @return the instructorApprovalperiod
	 */
	public String getInstructorApprovalperiod() {
		return instructorApprovalperiod;
	}

	/**
	 * @param instructorApprovalperiod the instructorApprovalperiod to set
	 */
	public void setInstructorApprovalperiod(String instructorApprovalperiod) {
		this.instructorApprovalperiod = instructorApprovalperiod;
	}

	/**
	 * @return the instructorApprovalStatus
	 */
	public String getInstructorApprovalStatus() {
		return instructorApprovalStatus;
	}

	/**
	 * @param instructorApprovalStatus the instructorApprovalStatus to set
	 */
	public void setInstructorApprovalStatus(String instructorApprovalStatus) {
		this.instructorApprovalStatus = instructorApprovalStatus;
	}

	/**
	 * @return the instructorApprovalFee
	 */
	public String getInstructorApprovalFee() {
		return instructorApprovalFee;
	}

	/**
	 * @param instructorApprovalFee the instructorApprovalFee to set
	 */
	public void setInstructorApprovalFee(String instructorApprovalFee) {
		this.instructorApprovalFee = instructorApprovalFee;
	}

	/**
	 * @return the instructorRenewalFee
	 */
	public String getInstructorRenewalFee() {
		return instructorRenewalFee;
	}

	/**
	 * @param instructorRenewalFee the instructorRenewalFee to set
	 */
	public void setInstructorRenewalFee(String instructorRenewalFee) {
		this.instructorRenewalFee = instructorRenewalFee;
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
	 * @return the instructorApprovalEffectivelyStartDate
	 */
	public Date getInstructorApprovalEffectivelyStartDate() {
		return instructorApprovalEffectivelyStartDate;
	}

	/**
	 * @param instructorApprovalEffectivelyStartDate the instructorApprovalEffectivelyStartDate to set
	 */
	public void setInstructorApprovalEffectivelyStartDate(
			Date instructorApprovalEffectivelyStartDate) {
		this.instructorApprovalEffectivelyStartDate = instructorApprovalEffectivelyStartDate;
	}

	/**
	 * @return the instructorApprovalEffectivelyEndsDate
	 */
	public Date getInstructorApprovalEffectivelyEndsDate() {
		return instructorApprovalEffectivelyEndsDate;
	}

	/**
	 * @param instructorApprovalEffectivelyEndsDate the instructorApprovalEffectivelyEndsDate to set
	 */
	public void setInstructorApprovalEffectivelyEndsDate(
			Date instructorApprovalEffectivelyEndsDate) {
		this.instructorApprovalEffectivelyEndsDate = instructorApprovalEffectivelyEndsDate;
	}

	/**
	 * @return the instructorSubmissionReminderDate
	 */
	public Date getInstructorSubmissionReminderDate() {
		return instructorSubmissionReminderDate;
	}

	/**
	 * @param instructorSubmissionReminderDate the instructorSubmissionReminderDate to set
	 */
	public void setInstructorSubmissionReminderDate(
			Date instructorSubmissionReminderDate) {
		this.instructorSubmissionReminderDate = instructorSubmissionReminderDate;
	}

	/**
	 * @return the mostRecentlySubmittedForApprovalDate
	 */
	public Date getMostRecentlySubmittedForApprovalDate() {
		return mostRecentlySubmittedForApprovalDate;
	}

	/**
	 * @param mostRecentlySubmittedForApprovalDate the mostRecentlySubmittedForApprovalDate to set
	 */
	public void setMostRecentlySubmittedForApprovalDate(
			Date mostRecentlySubmittedForApprovalDate) {
		this.mostRecentlySubmittedForApprovalDate = mostRecentlySubmittedForApprovalDate;
	}

	/**
	 * @return the inspectorOriginallyApprovalDate
	 */
	public Date getInspectorOriginallyApprovalDate() {
		return inspectorOriginallyApprovalDate;
	}

	/**
	 * @param inspectorOriginallyApprovalDate the inspectorOriginallyApprovalDate to set
	 */
	public void setInspectorOriginallyApprovalDate(
			Date inspectorOriginallyApprovalDate) {
		this.inspectorOriginallyApprovalDate = inspectorOriginallyApprovalDate;
	}

	/**
	 * @return the instructorApprovalInformation
	 */
	public String getInstructorApprovalInformation() {
		return InstructorApprovalInformation;
	}

	/**
	 * @param instructorApprovalInformation the instructorApprovalInformation to set
	 */
	public void setInstructorApprovalInformation(
			String instructorApprovalInformation) {
		InstructorApprovalInformation = instructorApprovalInformation;
	}

	/**
	 * @return the instructor
	 */
	public Instructor getInstructor() {
		return Instructor;
	}

	/**
	 * @param instructor the instructor to set
	 */
	public void setInstructor(Instructor instructor) {
		Instructor = instructor;
	}

	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}

	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	/**
	 * @return the derivedFrom
	 */
	public InstructorApproval getDerivedFrom() {
		return derivedFrom;
	}

	/**
	 * @param derivedFrom the derivedFrom to set
	 */
	public void setDerivedFrom(InstructorApproval derivedFrom) {
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