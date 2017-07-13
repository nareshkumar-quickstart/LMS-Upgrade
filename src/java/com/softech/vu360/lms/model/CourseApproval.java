/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.*;

import javax.persistence.*;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COURSEAPPROVAL")
@DiscriminatorValue("CourseApproval")
public class CourseApproval extends RegulatoryApproval {

	private static final long serialVersionUID = 1L;
	
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
	private Double originalCourseApprovalFee = 0.00;
	
	@Column(name = "TAGS")
	private String tag = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="COURSE_ID")
	private Course course = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="PROVIDER_ID")
	private Provider provider = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="ASSET_ID")
	private Certificate certificate = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="AFFIDAVIT_ID")
	private Affidavit affidavit = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="COURSECONFIGURATIONTEMPLATE_ID")
	private CourseConfigurationTemplate template = null;
	
	@OneToOne (cascade = { CascadeType.REMOVE },orphanRemoval=true)
    @JoinColumn(name="RENEWEDFROMCOURSEAPPROVAL_ID")
	private CourseApproval renewedFrom = null;
	
	@ManyToMany
    @JoinTable(name="COURSEAPPROVAL_PROCTORCODE", joinColumns = @JoinColumn(name="COURSEAPPROVAL_ID"),inverseJoinColumns = @JoinColumn(name="PROCTORCODE_ID"))
    private List<ProctorCode> proctorCodes =new ArrayList<ProctorCode>();
	
	@Column(name = "ACTIVE")
	private Boolean active = Boolean.TRUE;
	
	@Column(name = "USE_PURCHASED_CERTIFICATE_NO")
	private Boolean usePurchasedCertificateNumbers = Boolean.FALSE;
	
	//for purchase certificate numbers
	@OneToMany
    @JoinTable(name="COURSEAPPROVAL_PURCHASECERTIFICATE", joinColumns = @JoinColumn(name="COURSEAPPROVALID"),inverseJoinColumns = @JoinColumn(name="PURCHASECERTIFICATENUMBERID"))
    private Set<PurchaseCertificateNumber> purchaseCertificateNumbers = new HashSet<PurchaseCertificateNumber>();

	@Column(name = "USE_CERTIFICATE_NUMBER_GENERATOR")
	private Boolean useCertificateNumberGenerator = Boolean.FALSE;
	
	@Column(name = "CERTIFICATENUMBERGENERATOR_PREFIX")
	private String certificateNumberGeneratorPrefix;
	
	@Column(name = "CERTIFICATENUMBERGENERATOR_NUMBERFORMAT")
	private String certificateNumberGeneratorNumberFormat;
	
	@Column(name = "CERTIFICATENUMBERGENERATOR_NEXTNUMBER")
	private Long certificateNumberGeneratorNextNumber = 0L ;
	
	@Column(name = "SELF_REPORTED")
	private Boolean selfReported = Boolean.FALSE;
	
	@ManyToMany
    @JoinTable(name="COURSEAPPROVAL_COURSEGROUP", joinColumns = @JoinColumn(name="COURSEAPPROVAL_ID"),inverseJoinColumns = @JoinColumn(name="COURSEGROUP_ID"))
    private List<CourseGroup> courseGroups =new ArrayList<CourseGroup>();

	@Column(name = "CERTIFICATE_EXPIRATION_DURATION")
	private Integer certificateExpirationPeriod;
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */


	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.RegulatoryApproval#getApprovalType()
	 */



	/**
	 * @return the id
	 */


	public CourseApproval() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the approvedCourseName
	 */
	public String getApprovedCourseName() {
		return approvedCourseName;
	}

	/**
	 * @param approvedCourseName the approvedCourseName to set
	 */
	public void setApprovedCourseName(String approvedCourseName) {
		this.approvedCourseName = approvedCourseName;
	}

	/**
	 * @return the courseApprovalNumber
	 */
	public String getCourseApprovalNumber() {
		return courseApprovalNumber;
	}

	/**
	 * @param courseApprovalNumber the courseApprovalNumber to set
	 */
	public void setCourseApprovalNumber(String courseApprovalNumber) {
		this.courseApprovalNumber = courseApprovalNumber;
	}

	/**
	 * @return the courseApprovaltype
	 */
	public String getCourseApprovaltype() {
		return courseApprovaltype;
	}

	/**
	 * @param courseApprovaltype the courseApprovaltype to set
	 */
	public void setCourseApprovaltype(String courseApprovaltype) {
		this.courseApprovaltype = courseApprovaltype;
	}

	/**
	 * @return the approvedCreditHours
	 */
	public String getApprovedCreditHours() {
		return approvedCreditHours;
	}

	/**
	 * @param approvedCreditHours the approvedCreditHours to set
	 */
	public void setApprovedCreditHours(String approvedCreditHours) {
		this.approvedCreditHours = approvedCreditHours;
	}

	/**
	 * @return the courseApprovalStatus
	 */
	public String getCourseApprovalStatus() {
		return courseApprovalStatus;
	}

	/**
	 * @param courseApprovalStatus the courseApprovalStatus to set
	 */
	public void setCourseApprovalStatus(String courseApprovalStatus) {
		this.courseApprovalStatus = courseApprovalStatus;
	}

	/**
	 * @return the courseApprovalRenewalFee
	 */
	public String getCourseApprovalRenewalFee() {
		return courseApprovalRenewalFee;
	}

	/**
	 * @param courseApprovalRenewalFee the courseApprovalRenewalFee to set
	 */
	public void setCourseApprovalRenewalFee(String courseApprovalRenewalFee) {
		this.courseApprovalRenewalFee = courseApprovalRenewalFee;
	}

	/**
	 * @return the courseApprovalSubmissionFee
	 */
	public String getCourseApprovalSubmissionFee() {
		return courseApprovalSubmissionFee;
	}

	/**
	 * @param courseApprovalSubmissionFee the courseApprovalSubmissionFee to set
	 */
	public void setCourseApprovalSubmissionFee(String courseApprovalSubmissionFee) {
		this.courseApprovalSubmissionFee = courseApprovalSubmissionFee;
	}

	/**
	 * @return the courseApprovalEffectivelyStartDate
	 */
	public Date getCourseApprovalEffectivelyStartDate() {
		return courseApprovalEffectivelyStartDate;
	}

	/**
	 * @param courseApprovalEffectivelyStartDate the courseApprovalEffectivelyStartDate to set
	 */
	public void setCourseApprovalEffectivelyStartDate(
			Date courseApprovalEffectivelyStartDate) {
		this.courseApprovalEffectivelyStartDate = courseApprovalEffectivelyStartDate;
	}

	/**
	 * @return the courseApprovalEffectivelyEndsDate
	 */
	public Date getCourseApprovalEffectivelyEndsDate() {
		return courseApprovalEffectivelyEndsDate;
	}

	/**
	 * @param courseApprovalEffectivelyEndsDate the courseApprovalEffectivelyEndsDate to set
	 */
	public void setCourseApprovalEffectivelyEndsDate(
			Date courseApprovalEffectivelyEndsDate) {
		this.courseApprovalEffectivelyEndsDate = courseApprovalEffectivelyEndsDate;
	}

	/**
	 * @return the courseSubmissionReminderDate
	 */
	public Date getCourseSubmissionReminderDate() {
		return courseSubmissionReminderDate;
	}

	/**
	 * @param courseSubmissionReminderDate the courseSubmissionReminderDate to set
	 */
	public void setCourseSubmissionReminderDate(Date courseSubmissionReminderDate) {
		this.courseSubmissionReminderDate = courseSubmissionReminderDate;
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
	 * @return the courseApprovalInformation
	 */
	public String getCourseApprovalInformation() {
		return courseApprovalInformation;
	}

	/**
	 * @param courseApprovalInformation the courseApprovalInformation to set
	 */
	public void setCourseApprovalInformation(String courseApprovalInformation) {
		this.courseApprovalInformation = courseApprovalInformation;
	}

	/**
	 * @return the originalCourseApprovalFee
	 */
	public Double getOriginalCourseApprovalFee() {
		return originalCourseApprovalFee;
	}

	/**
	 * @param originalCourseApprovalFee the originalCourseApprovalFee to set
	 */
	public void setOriginalCourseApprovalFee(Double originalCourseApprovalFee) {
		this.originalCourseApprovalFee = originalCourseApprovalFee;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}



	/**
	 * @return the regulators
	 */

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.RegulatoryApproval#getId()
	 */


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
	 * @return the certificate
	 */
	public Certificate getCertificate() {
		return certificate;
	}

	/**
	 * @param certificate the certificate to set
	 */
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	
	/**
	 * @return the affidavit
	 */
	public Affidavit getAffidavit() {
		return affidavit;
	}

	/**
	 * @param affidavit the affidavit to set
	 */
	public void setAffidavit(Affidavit affidavit) {
		this.affidavit = affidavit;
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

	/**
	 * @return the template
	 */
	public CourseConfigurationTemplate getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(CourseConfigurationTemplate template) {
		this.template = template;
	}

	/**
	 * @return the renewedFrom
	 */
	public CourseApproval getRenewedFrom() {
		return renewedFrom;
	}

	/**
	 * @param renewedFrom the renewedFrom to set
	 */
	public void setRenewedFrom(CourseApproval renewedFrom) {
		this.renewedFrom = renewedFrom;
	}

	/**
	 * @return the proctorCodes
	 */
	public List<ProctorCode> getProctorCodes() {
		return proctorCodes;
	}

	/**
	 * @param proctorCodes the proctorCodes to set
	 */
	public void setProctorCodes(List<ProctorCode> proctorCodes) {
		this.proctorCodes = proctorCodes;
	}

	/**
	 * @return the active
	 */
	public  Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}	
	
	/**
	 * @return the purchaseCertificateNumbers
	 */
	public Set<PurchaseCertificateNumber> getPurchaseCertificateNumbers() {
		return purchaseCertificateNumbers;
	}

	/**
	 * @param purchaseCertificateNumbers the purchaseCertificateNumbers to set
	 */
	public void setPurchaseCertificateNumbers(
			Set<PurchaseCertificateNumber> purchaseCertificateNumbers) {
		this.purchaseCertificateNumbers = purchaseCertificateNumbers;
	}

	public  Boolean getUsePurchasedCertificateNumbers() {
		return usePurchasedCertificateNumbers;
	}

	public void setUsePurchasedCertificateNumbers(
			 Boolean usePurchasedCertificateNumbers) {
		this.usePurchasedCertificateNumbers = usePurchasedCertificateNumbers;
	}
	
	public PurchaseCertificateNumber getNextPurchansedCertificateNumber(){
		if(this.usePurchasedCertificateNumbers)
		  for(PurchaseCertificateNumber pcn: purchaseCertificateNumbers)
			if(!pcn.isUsed())
			  return pcn;		  						
		
		return null;
	}
	
	public  Boolean getContainsAffidavit(){
		return getAffidavit() != null && getAffidavit().getId() != null && getAffidavit().getId() > 0;
	}

	public  Boolean getUseCertificateNumberGenerator() {
		if(useCertificateNumberGenerator==null){
			useCertificateNumberGenerator=Boolean.FALSE;
		}
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

	public Long getCertificateNumberGeneratorNextNumber() {
		return certificateNumberGeneratorNextNumber;
	}

	public void setCertificateNumberGeneratorNextNumber(
			Long certificateNumberGeneratorNextNumber) {
		this.certificateNumberGeneratorNextNumber = certificateNumberGeneratorNextNumber;
	}

	public  Boolean getSelfReported() {
		return selfReported;
	}

	public void setSelfReported(Boolean selfReported) {
		this.selfReported = selfReported;
	}

	public List<CourseGroup> getCourseGroups() {
		return courseGroups;
	}

	public void setCourseGroups(List<CourseGroup> courseGroups) {
		this.courseGroups = courseGroups;
	}

	public Integer getCertificateExpirationPeriod() {
		return certificateExpirationPeriod;
	}

	public void setCertificateExpirationPeriod(Integer certificateExpirationPeriod) {
		this.certificateExpirationPeriod = certificateExpirationPeriod;
	}
	
}
