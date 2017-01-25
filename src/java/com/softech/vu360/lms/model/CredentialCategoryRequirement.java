package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "CREDENTIALCATEGORYREQUIREMENT")
public class CredentialCategoryRequirement implements SearchableKey {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "CREDENTIALCATEGORYREQUIREMENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CREDENTIALCATEGORYREQUIREMENT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CREDENTIALCATEGORYREQUIREMENT_ID")
	private Long id;
	
	@Column(name = "PREREQUISITES")
	private String preRequisites = null;
	
	@Column(name = "NUMBEROFHOURS")
	private double numberOfHours = 0;
	
	@Column(name = "NUMBEROFETHICSHOURS")
	private double numberOfEthicsHours = 0;
	
	@Column(name = "OTHERSPECIALEDUCATIONALREQUIREMENTS")
	private String specialEducationalRequirements  = null;
	
	@Column(name = "SEATTIMEMEASUREMENT")
	private String seatTimeMeasurement = null;
	
	@Column(name = "RECIPROCITY")
	private String reciprocity = null;
	
	@Column(name = "COURSEVALIDATION")
	private String courseValidation = null;
	
	@Column(name = "COURSEAPPROVALPERIOD")
	private String courseApprovalPeriod = null;
	
	@Column(name = "REPORTINGPERIOD")
	private Date reportingPeriod = null;
	
	@Column(name = "ONLINECEALLOWEDTF")
	private Boolean onlineCEAllowed = true;
	
	@Column(name = "CORRESPONDENCECEALLOWEDTF")
	private Boolean correspondenceCEAllowed = true;
	
	@Column(name = "COURSEASSESSMENTS")
	private String courseAssessments = null;
	
	@Column(name = "CORRESPONDENCEALLOWEDFORPRELICENSINGTF")
	private Boolean correspondenceAllowedPreLicensing = true;
	
	@Column(name = "CORRESPONDENCEALLOWEDFORPOSTLICENSINGTF")
	private Boolean correspondenceAllowedPostLicensing = true;
	
	@Column(name = "CERTIFICATEPROCEDURE")
	private String certificateProcedure = null;
	
	@Column(name = "WHOREPORTSCREDITS")
	private String whoReportsCredits = null;
	
	@Column(name = "REPORTINGFEES")
	private double reportingFees = 0.00;
	
	@Column(name = "CEREQUIRESPROVIDERAPPROVALTF")
	private Boolean CERequiresProviderApproval = true;
	
	@Column(name = "UPCOMINGCEDEADLINEORLICENSERENEWALDATE")
	private Date licenseRenewalDate = null;
	
	@Column(name = "ONLINEPRELICENSEALLOWEDTF")
	private Boolean onlinePreLicenseAllowed = true;
	
	@Column(name = "PRELICENSINGREQUIREPROVIDERAPPROVALTF")
	private Boolean prelicensingRequireProviderApproval = true;
	
	@Column(name = "ONLINEPOSTLICENSEREQUIREDTF")
	private Boolean onlinePostLicenseRequired = true;
	
	@Column(name = "POSTLICENSEREQUIRENOTESTF")
	private Boolean postLicenseRequireNotes = true;
	
	@Column(name = "POSTLICENSEREQUIREPROVIDERAPPROVALTF")
	private Boolean postLicenseRequireProviderApproval = true;
	
	@Column(name = "CEREQUIREMENTDEADLINE")
	private Date CERequirementDeadline = null;
	
	@Column(name = "LICENSERENEWALDATETIMEFRAME")
	private String timeframe = null;
	
	@Column(name = "APPROXIMATENUMBEROFDAYSTOGAINPROVIDERAPPROVALFROMSUBMISSION")
	private Integer daysToGainProviderApproval = 0;
	
	@Column(name = "APPROXIMATENUMBEROFDAYSTOGAINCOURSEAPPROVALFROMSUBMISSION")
	private Integer daysToGainCourseApproval = 0;
	
	@Column(name = "APPROXIMATENUMBEROFDAYSTOGAINCOURSERENEWALFROMSUBMISSION")
	private Integer daysToGainCourseRenewal  = 0;
	
	@OneToOne
	@JoinColumn(name = "CREDENTIALCATEGORY_ID")
	private CredentialCategory credentialCategory = null;
	
	@Column(name = "NAME")
	private String name = null;
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE }) //LMS-20502
	@JoinTable(name="CREDENTIALCATEGORYREQUIREMENT_CUSTOMFIELD", joinColumns = @JoinColumn(name="CREDENTIALCATEGORYREQUIREMENT_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
	private List<CustomField> customfields = new ArrayList<CustomField>();
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE }) //LMS-20502
	@JoinTable(name="CREDENTIALCATEGORYREQUIREMENT_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="CREDENTIALCATEGORYREQUIREMENT_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"))
	private List<CustomFieldValue>customfieldValues = new ArrayList<CustomFieldValue>();
	
	@Column(name = "DESCRIPTION")
	private String description = null;
	
	@Column(name = "COVERAGE")
	private String coverage= null;
	
	@Column(name = "COVERAGEEXPLANATION")
	private String coverageExplanation= null;

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the preRequisites
	 */
	public String getPreRequisites() {
		return preRequisites;
	}


	/**
	 * @param preRequisites the preRequisites to set
	 */
	public void setPreRequisites(String preRequisites) {
		this.preRequisites = preRequisites;
	}


	/**
	 * @return the numberOfHours
	 */
	public double getNumberOfHours() {
		return numberOfHours;
	}


	/**
	 * @param numberOfHours the numberOfHours to set
	 */
	public void setNumberOfHours(double numberOfHours) {
		this.numberOfHours = numberOfHours;
	}


	/**
	 * @return the numberOfEthicsHours
	 */
	public double getNumberOfEthicsHours() {
		return numberOfEthicsHours;
	}


	/**
	 * @param numberOfEthicsHours the numberOfEthicsHours to set
	 */
	public void setNumberOfEthicsHours(double numberOfEthicsHours) {
		this.numberOfEthicsHours = numberOfEthicsHours;
	}


	/**
	 * @return the specialEducationalRequirements
	 */
	public String getSpecialEducationalRequirements() {
		return specialEducationalRequirements;
	}


	/**
	 * @param specialEducationalRequirements the specialEducationalRequirements to set
	 */
	public void setSpecialEducationalRequirements(
			String specialEducationalRequirements) {
		this.specialEducationalRequirements = specialEducationalRequirements;
	}


	/**
	 * @return the seatTimeMeasurement
	 */
	public String getSeatTimeMeasurement() {
		return seatTimeMeasurement;
	}


	/**
	 * @param seatTimeMeasurement the seatTimeMeasurement to set
	 */
	public void setSeatTimeMeasurement(String seatTimeMeasurement) {
		this.seatTimeMeasurement = seatTimeMeasurement;
	}


	/**
	 * @return the reciprocity
	 */
	public String getReciprocity() {
		return reciprocity;
	}


	/**
	 * @param reciprocity the reciprocity to set
	 */
	public void setReciprocity(String reciprocity) {
		this.reciprocity = reciprocity;
	}


	/**
	 * @return the courseValidation
	 */
	public String getCourseValidation() {
		return courseValidation;
	}


	/**
	 * @param courseValidation the courseValidation to set
	 */
	public void setCourseValidation(String courseValidation) {
		this.courseValidation = courseValidation;
	}


	/**
	 * @return the courseApprovalPeriod
	 */
	public String getCourseApprovalPeriod() {
		return courseApprovalPeriod;
	}


	/**
	 * @param courseApprovalPeriod the courseApprovalPeriod to set
	 */
	public void setCourseApprovalPeriod(String courseApprovalPeriod) {
		this.courseApprovalPeriod = courseApprovalPeriod;
	}


	/**
	 * @return the reportingPeriod
	 */
	public Date getReportingPeriod() {
		return reportingPeriod;
	}


	/**
	 * @param reportingPeriod the reportingPeriod to set
	 */
	public void setReportingPeriod(Date reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
	}


	/**
	 * @return the onlineCEAllowed
	 */
	public  Boolean isOnlineCEAllowed() {
		return onlineCEAllowed;
	}


	/**
	 * @param onlineCEAllowed the onlineCEAllowed to set
	 */
	public void setOnlineCEAllowed(Boolean onlineCEAllowed) {
		this.onlineCEAllowed = onlineCEAllowed;
	}


	/**
	 * @return the correspondenceCEAllowed
	 */
	public  Boolean isCorrespondenceCEAllowed() {
		return correspondenceCEAllowed;
	}


	/**
	 * @param correspondenceCEAllowed the correspondenceCEAllowed to set
	 */
	public void setCorrespondenceCEAllowed(Boolean correspondenceCEAllowed) {
		this.correspondenceCEAllowed = correspondenceCEAllowed;
	}


	/**
	 * @return the courseAssessments
	 */
	public String getCourseAssessments() {
		return courseAssessments;
	}


	/**
	 * @param courseAssessments the courseAssessments to set
	 */
	public void setCourseAssessments(String courseAssessments) {
		this.courseAssessments = courseAssessments;
	}


	/**
	 * @return the correspondenceAllowedPreLicensing
	 */
	public  Boolean isCorrespondenceAllowedPreLicensing() {
		return correspondenceAllowedPreLicensing;
	}


	/**
	 * @param correspondenceAllowedPreLicensing the correspondenceAllowedPreLicensing to set
	 */
	public void setCorrespondenceAllowedPreLicensing(
			 Boolean correspondenceAllowedPreLicensing) {
		this.correspondenceAllowedPreLicensing = correspondenceAllowedPreLicensing;
	}


	/**
	 * @return the correspondenceAllowedPostLicensing
	 */
	public  Boolean isCorrespondenceAllowedPostLicensing() {
		return correspondenceAllowedPostLicensing;
	}


	/**
	 * @param correspondenceAllowedPostLicensing the correspondenceAllowedPostLicensing to set
	 */
	public void setCorrespondenceAllowedPostLicensing(
			 Boolean correspondenceAllowedPostLicensing) {
		this.correspondenceAllowedPostLicensing = correspondenceAllowedPostLicensing;
	}


	/**
	 * @return the certificateProcedure
	 */
	public String getCertificateProcedure() {
		return certificateProcedure;
	}


	/**
	 * @param certificateProcedure the certificateProcedure to set
	 */
	public void setCertificateProcedure(String certificateProcedure) {
		this.certificateProcedure = certificateProcedure;
	}


	/**
	 * @return the whoReportsCredits
	 */
	public String getWhoReportsCredits() {
		return whoReportsCredits;
	}


	/**
	 * @param whoReportsCredits the whoReportsCredits to set
	 */
	public void setWhoReportsCredits(String whoReportsCredits) {
		this.whoReportsCredits = whoReportsCredits;
	}


	/**
	 * @return the reportingFees
	 */
	public double getReportingFees() {
		return reportingFees;
	}


	/**
	 * @param reportingFees the reportingFees to set
	 */
	public void setReportingFees(double reportingFees) {
		this.reportingFees = reportingFees;
	}


	/**
	 * @return the cERequiresProviderApproval
	 */
	public  Boolean isCERequiresProviderApproval() {
		return CERequiresProviderApproval;
	}


	/**
	 * @param requiresProviderApproval the cERequiresProviderApproval to set
	 */
	public void setCERequiresProviderApproval(Boolean requiresProviderApproval) {
		CERequiresProviderApproval = requiresProviderApproval;
	}


	/**
	 * @return the licenseRenewalDate
	 */
	public Date getLicenseRenewalDate() {
		return licenseRenewalDate;
	}


	/**
	 * @param licenseRenewalDate the licenseRenewalDate to set
	 */
	public void setLicenseRenewalDate(Date licenseRenewalDate) {
		this.licenseRenewalDate = licenseRenewalDate;
	}


	/**
	 * @return the onlinePreLicenseAllowed
	 */
	public  Boolean isOnlinePreLicenseAllowed() {
		return onlinePreLicenseAllowed;
	}


	/**
	 * @param onlinePreLicenseAllowed the onlinePreLicenseAllowed to set
	 */
	public void setOnlinePreLicenseAllowed(Boolean onlinePreLicenseAllowed) {
		this.onlinePreLicenseAllowed = onlinePreLicenseAllowed;
	}


	/**
	 * @return the prelicensingRequireProviderApproval
	 */
	public  Boolean isPrelicensingRequireProviderApproval() {
		return prelicensingRequireProviderApproval;
	}


	/**
	 * @param prelicensingRequireProviderApproval the prelicensingRequireProviderApproval to set
	 */
	public void setPrelicensingRequireProviderApproval(
			 Boolean prelicensingRequireProviderApproval) {
		this.prelicensingRequireProviderApproval = prelicensingRequireProviderApproval;
	}


	/**
	 * @return the onlinePostLicenseRequired
	 */
	public  Boolean isOnlinePostLicenseRequired() {
		return onlinePostLicenseRequired;
	}


	/**
	 * @param onlinePostLicenseRequired the onlinePostLicenseRequired to set
	 */
	public void setOnlinePostLicenseRequired(Boolean onlinePostLicenseRequired) {
		this.onlinePostLicenseRequired = onlinePostLicenseRequired;
	}


	/**
	 * @return the postLicenseRequireNotes
	 */
	public  Boolean isPostLicenseRequireNotes() {
		return postLicenseRequireNotes;
	}


	/**
	 * @param postLicenseRequireNotes the postLicenseRequireNotes to set
	 */
	public void setPostLicenseRequireNotes(Boolean postLicenseRequireNotes) {
		this.postLicenseRequireNotes = postLicenseRequireNotes;
	}


	/**
	 * @return the postLicenseRequireProviderApproval
	 */
	public  Boolean isPostLicenseRequireProviderApproval() {
		return postLicenseRequireProviderApproval;
	}


	/**
	 * @param postLicenseRequireProviderApproval the postLicenseRequireProviderApproval to set
	 */
	public void setPostLicenseRequireProviderApproval(
			 Boolean postLicenseRequireProviderApproval) {
		this.postLicenseRequireProviderApproval = postLicenseRequireProviderApproval;
	}


	/**
	 * @return the cERequirementDeadline
	 */
	public Date getCERequirementDeadline() {
		return CERequirementDeadline;
	}


	/**
	 * @param requirementDeadline the cERequirementDeadline to set
	 */
	public void setCERequirementDeadline(Date requirementDeadline) {
		CERequirementDeadline = requirementDeadline;
	}


	/**
	 * @return the timeframe
	 */
	public String getTimeframe() {
		return timeframe;
	}


	/**
	 * @param timeframe the timeframe to set
	 */
	public void setTimeframe(String timeframe) {
		this.timeframe = timeframe;
	}


	/**
	 * @return the daysToGainProviderApproval
	 */
	public Integer getDaysToGainProviderApproval() {
		return daysToGainProviderApproval;
	}


	/**
	 * @param daysToGainProviderApproval the daysToGainProviderApproval to set
	 */
	public void setDaysToGainProviderApproval(Integer daysToGainProviderApproval) {
		this.daysToGainProviderApproval = daysToGainProviderApproval;
	}


	/**
	 * @return the daysToGainCourseApproval
	 */
	public Integer getDaysToGainCourseApproval() {
		return daysToGainCourseApproval;
	}


	/**
	 * @param daysToGainCourseApproval the daysToGainCourseApproval to set
	 */
	public void setDaysToGainCourseApproval(Integer daysToGainCourseApproval) {
		this.daysToGainCourseApproval = daysToGainCourseApproval;
	}


	/**
	 * @return the daysToGainCourseRenewal
	 */
	public Integer getDaysToGainCourseRenewal() {
		return daysToGainCourseRenewal;
	}


	/**
	 * @param daysToGainCourseRenewal the daysToGainCourseRenewal to set
	 */
	public void setDaysToGainCourseRenewal(Integer daysToGainCourseRenewal) {
		this.daysToGainCourseRenewal = daysToGainCourseRenewal;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	public List<CustomField> getCustomfields() {
		return customfields;
	}


	public void setCustomfields(List<CustomField> customfields) {
		this.customfields = customfields;
	}


	public List<CustomFieldValue> getCustomfieldValues() {
		return customfieldValues;
	}


	public void setCustomfieldValues(List<CustomFieldValue> customfieldValues) {
		this.customfieldValues = customfieldValues;
	}


	/**
	 * @param credentialCategory the credentialCategory to set
	 */
	public void setCredentialCategory(CredentialCategory credentialCategory) {
		this.credentialCategory = credentialCategory;
	}


	/**
	 * @return the credentialCategory
	 */
	public CredentialCategory getCredentialCategory() {
		return credentialCategory;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	public String getCoverage() {
		return coverage;
	}


	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}


	public String getCoverageExplanation() {
		return coverageExplanation;
	}


	public void setCoverageExplanation(String coverageExplanation) {
		this.coverageExplanation = coverageExplanation;
	}
	
	

}