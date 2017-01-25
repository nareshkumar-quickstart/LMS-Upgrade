package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "REGULATORCATEGORY_AUDIT")
public class RegulatorCategoryAudit implements Serializable  {

	private static final long serialVersionUID = 1L;

		public static final String[] CATEGORY_TYPES = {
            RegulatorCategory.PRE_LICENSE,
            RegulatorCategory.POST_LICENSE,
            RegulatorCategory.CONTINUING_EDUCATION,
            RegulatorCategory.CERTIFICATION,
            RegulatorCategory.DESIGNATION,
            RegulatorCategory.PROFESSIONAL_DEVELOPMENT };

        public static final String PRE_LICENSE = "Pre-License";
        public static final String POST_LICENSE = "Post License";
        public static final String CONTINUING_EDUCATION = "Continuing Education";
        public static final String CERTIFICATION = "Certification";
        public static final String DESIGNATION = "Designation";
        public static final String PROFESSIONAL_DEVELOPMENT = "Professional Development";
    

    @Id
	@javax.persistence.TableGenerator(name = "REGULATORCATEGORY_AUDIT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "REGULATORCATEGORY_AUDIT", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "REGULATORCATEGORY_AUDIT_ID")
	private Long    id;
    
    @Column(name = "REGULATOR_ID") 
	private Long regulatorId = null;
	
    @Column(name = "CATEGORYTYPE") 
	private String  categoryType;
	
    @Column(name = "DISPLAYNAME")	
	private String  displayName;
	
    @Column(name = "VALIDATIONREQUIREDTF")	
	private Boolean validationRequired;
	
    @Column(name = "VALIDATIONFREQUENCY")	
	private Double  validationFrequency;
	
	@Transient
	private Set <Modality>  modalitiesAllowed = new HashSet<Modality>();
	
	@Column(name = "MAXIMUMONLINEHOURS")	
	private Integer  maximumOnlineHours;
	
	@Column(name = "MINIMUMSEATTIME")	
	private Integer  minimumSeatTime;
	
	@Column(name = "MINIMUMSEATTIMEUNIT")	
	private String  minimimumSeatTimeUnit;
	
	@Column(name = "PREASSESSMENTREQUIREDTF")	
	private Boolean preAssessmentRequired;
	
	@Column(name = "QUIZREQUIREDTF")	
	private Boolean quizRequired;
	
	@Column(name = "FINALASSESSMENTREQUIREDTF")	
	private Boolean finalAssessmentRequired;
	
	@Column(name = "PREASSESSMENTPASSINGRATEPERCENTAGE")	
	private Double  preAssessmentPassingRatePercentage;
	
	@Column(name = "PREASSESSMENTNUMBEROFQUESTIONS")	
	private Integer preAssessmentNumberOfQuestions;
	
	@Column(name = "QUIZPASSINGRATEPERCENTAGE")	
	private Double quizPassingRatePercentage;
	
	@Column(name = "QUIZNUMBEROFQUESTIONS")	
	private Integer quizNumberOfQuestions;
	
	@Column(name = "FINALASSESSMENTPASSINGRATEPERCENTAGE")	
	private Double  finalAssessmentPassingRatePercentage;
	
	@Column(name = "FINALASSESSMENTNUMBEROFQUESTIONS")	
	private Integer     finalAssessmentNumberOfQuestions;
	
	@Column(name = "CERTIFICATE")	
	private String  certificate;
	
	@Column(name = "REPORTING")	
	private String  reporting;
	
	@Column(name = "RECIPROCITYNOTES")	
	private String  reciprocityNotes;
	
	@Column(name = "REPEATABLETF")	
	private Boolean repeatable;
	
	@Column(name = "REPETABILITYNOTES")	
	private String  repetabilityNotes;
	
	@Column(name = "PROVIDERAPPROVALREQUIREDTF")	
	private Boolean providerApprovalRequired;
	
	@Column(name = "PROVIDERAPPROVALPERIOD")	
	private Integer  providerApprovalPeriod;
	
	@Column(name = "PROVIDERAPPROVALPERIODUNIT")	
	private String  providerApprovalPeriodUnit;
	
	@Column(name = "COURSEAPPROVALREQUIREDTF")	
	private Boolean courseApprovalRequired;
	
	@Column(name = "COURSEAPPROVALPERIOD")	
	private Integer  courseApprovalPeriod;
	
	@Column(name = "COURSEAPPROVALPERIODUNIT")	
	private String  courseApprovalPeriodUnit;
	
	@Transient
	private List<CreditReportingField> reportingFields = new ArrayList<CreditReportingField>();
	
	@Transient
	private List<CustomField> customFields = new ArrayList<CustomField>();
	
	@Transient
	private List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
	
	@Transient
	private Boolean selected;

	@Column(name = "REPORTINGREQUIREDTF")	
	private Boolean reportingRequired;
	
	@Column(name = "REPORTINGTIMEFRAME")	
	private Integer  reportingTimeframe;

	@Column(name = "REGULATORCATEGORY_ID")	
	private Long regulatorCategoryAuditId = null;
	
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

	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getCategoryType() {
		return categoryType;
	}



	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}



	public String getDisplayName() {
		return displayName;
	}



	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}



	public Boolean isValidationRequired() {
		return validationRequired;
	}



	public void setValidationRequired(Boolean validationRequired) {
		this.validationRequired = validationRequired;
	}



	public Double getValidationFrequency() {
		return validationFrequency;
	}



	public void setValidationFrequency(Double validationFrequency) {
		this.validationFrequency = validationFrequency;
	}

	public Integer getMaximumOnlineHours() {
		return maximumOnlineHours;
	}



	public void setMaximumOnlineHours(Integer maximumOnlineHours) {
		this.maximumOnlineHours = maximumOnlineHours;
	}



	public Integer getMinimumSeatTime() {
		return minimumSeatTime;
	}



	public void setMinimumSeatTime(Integer minimumSeatTime) {
		this.minimumSeatTime = minimumSeatTime;
	}



	public String getMinimimumSeatTimeUnit() {
		return minimimumSeatTimeUnit;
	}



	public void setMinimimumSeatTimeUnit(String minimimumSeatTimeUnit) {
		this.minimimumSeatTimeUnit = minimimumSeatTimeUnit;
	}



	public Boolean isPreAssessmentRequired() {
		return preAssessmentRequired;
	}



	public void setPreAssessmentRequired(Boolean preAssessmentRequired) {
		this.preAssessmentRequired = preAssessmentRequired;
	}



	public Boolean isQuizRequired() {
		return quizRequired;
	}



	public void setQuizRequired(Boolean quizRequired) {
		this.quizRequired = quizRequired;
	}



	public Boolean isFinalAssessmentRequired() {
		return finalAssessmentRequired;
	}



	public void setFinalAssessmentRequired(Boolean finalAssessmentRequired) {
		this.finalAssessmentRequired = finalAssessmentRequired;
	}



	public Double getPreAssessmentPassingRatePercentage() {
		return preAssessmentPassingRatePercentage;
	}



	public void setPreAssessmentPassingRatePercentage(
			Double preAssessmentPassingRatePercentage) {
		this.preAssessmentPassingRatePercentage = preAssessmentPassingRatePercentage;
	}



	public Integer getPreAssessmentNumberOfQuestions() {
		return preAssessmentNumberOfQuestions;
	}



	public void setPreAssessmentNumberOfQuestions(Integer preAssessmentNumberOfQuestions) {
		this.preAssessmentNumberOfQuestions = preAssessmentNumberOfQuestions;
	}



	public Double getQuizPassingRatePercentage() {
		return quizPassingRatePercentage;
	}



	public void setQuizPassingRatePercentage(Double quizPassingRatePercentage) {
		this.quizPassingRatePercentage = quizPassingRatePercentage;
	}



	public Integer getQuizNumberOfQuestions() {
		return quizNumberOfQuestions;
	}



	public void setQuizNumberOfQuestions(Integer quizNumberOfQuestions) {
		this.quizNumberOfQuestions = quizNumberOfQuestions;
	}



	public Double getFinalAssessmentPassingRatePercentage() {
		return finalAssessmentPassingRatePercentage;
	}



	public void setFinalAssessmentPassingRatePercentage(
			Double finalAssessmentPassingRatePercentage) {
		this.finalAssessmentPassingRatePercentage = finalAssessmentPassingRatePercentage;
	}



	public Integer getFinalAssessmentNumberOfQuestions() {
		return finalAssessmentNumberOfQuestions;
	}



	public void setFinalAssessmentNumberOfQuestions(
			Integer finalAssessmentNumberOfQuestions) {
		this.finalAssessmentNumberOfQuestions = finalAssessmentNumberOfQuestions;
	}



	public String getCertificate() {
		return certificate;
	}



	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}



	public String getReporting() {
		return reporting;
	}



	public void setReporting(String reporting) {
		this.reporting = reporting;
	}



	public String getReciprocityNotes() {
		return reciprocityNotes;
	}



	public void setReciprocityNotes(String reciprocityNotes) {
		this.reciprocityNotes = reciprocityNotes;
	}



	public Boolean isRepeatable() {
		return repeatable;
	}



	public void setRepeatable(Boolean repeatable) {
		this.repeatable = repeatable;
	}



	public String getRepetabilityNotes() {
		return repetabilityNotes;
	}



	public void setRepetabilityNotes(String repetabilityNotes) {
		this.repetabilityNotes = repetabilityNotes;
	}



	public Boolean isProviderApprovalRequired() {
		return providerApprovalRequired;
	}



	public void setProviderApprovalRequired(Boolean providerApprovalRequired) {
		this.providerApprovalRequired = providerApprovalRequired;
	}



	public Integer getProviderApprovalPeriod() {
		return providerApprovalPeriod;
	}



	public void setProviderApprovalPeriod(Integer providerApprovalPeriod) {
		this.providerApprovalPeriod = providerApprovalPeriod;
	}



	public String getProviderApprovalPeriodUnit() {
		return providerApprovalPeriodUnit;
	}



	public void setProviderApprovalPeriodUnit(String providerApprovalPeriodUnit) {
		this.providerApprovalPeriodUnit = providerApprovalPeriodUnit;
	}



	public Boolean isCourseApprovalRequired() {
		return courseApprovalRequired;
	}



	public void setCourseApprovalRequired(Boolean courseApprovalRequired) {
		this.courseApprovalRequired = courseApprovalRequired;
	}



	public Integer getCourseApprovalPeriod() {
		return courseApprovalPeriod;
	}



	public void setCourseApprovalPeriod(Integer courseApprovalPeriod) {
		this.courseApprovalPeriod = courseApprovalPeriod;
	}



	public String getCourseApprovalPeriodUnit() {
		return courseApprovalPeriodUnit;
	}



	public void setCourseApprovalPeriodUnit(String courseApprovalPeriodUnit) {
		this.courseApprovalPeriodUnit = courseApprovalPeriodUnit;
	}

	
	public List<CreditReportingField> getReportingFields() {
		return reportingFields;
	}

	
	public void setReportingFields(List<CreditReportingField> reportingFields) {
		this.reportingFields = reportingFields;
	}

	public List<CustomField> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(
			List<CustomField> customFields) {
		this.customFields = customFields;
	}



	public List<CustomFieldValue> getCustomFieldValues() {
		return customFieldValues;
	}



	public void setCustomFieldValues(List<CustomFieldValue> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}



	public Set<Modality> getModalitiesAllowed() {
		return modalitiesAllowed;
	}

	public void setModalitiesAllowed(Set<Modality> modalitiesAllowed) {
		this.modalitiesAllowed = modalitiesAllowed;
	}


	public Boolean isSelected() {
		return selected;
	}



	public void setSelected(Boolean selected) {
		this.selected = selected;
	}


	public Boolean isReportingRequired() {
		return reportingRequired;
	}

	public void setReportingRequired(Boolean reportingRequired) {
		this.reportingRequired = reportingRequired;
	}

	public Integer getReportingTimeframe() {
		return reportingTimeframe;
	}

	public void setReportingTimeframe(Integer reportingTimeframe) {
		this.reportingTimeframe = reportingTimeframe;
	}



	public Long getRegulatorCategoryAuditId() {
		return regulatorCategoryAuditId;
	}



	public void setRegulatorCategoryAuditId(Long regulatorCategoryAuditId) {
		this.regulatorCategoryAuditId = regulatorCategoryAuditId;
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



	public Long getRegulatorId() {
		return regulatorId;
	}



	public void setRegulatorId(Long regulatorId) {
		this.regulatorId = regulatorId;
	}



	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

}
