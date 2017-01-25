package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Transient;
/**
 * 
 * @author marium.saud
 *
 */

@Entity
@Table(name = "REGULATORCATEGORY")
public class RegulatorCategory implements SearchableKey {

 	private static final long serialVersionUID = -3550333865404345234L;

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
   	@javax.persistence.TableGenerator(name = "REGULATORCATEGORY_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "REGULATORCATEGORY", allocationSize = 1)
   	@GeneratedValue(strategy = GenerationType.TABLE, generator = "REGULATORCATEGORY_ID")    
	private Long  id;
   	
   	@OneToOne
	@JoinColumn(name = "REGULATOR_ID")
	private Regulator regulator;
   	
   	@Column(name = "CATEGORYTYPE")
	private String  categoryType;
   	
   	@Column(name = "DISPLAYNAME")
	private String  displayName;
   	
   	@Column(name = "VALIDATIONREQUIREDTF")
	private Boolean validationRequired = Boolean.FALSE;
   	
   	@Column(name = "VALIDATIONFREQUENCY")
	private Double  validationFrequency = 0.00;
   	
   	@ManyToMany  (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
   	@JoinTable(name="REGULATORCATEGORY_MODALITY", joinColumns = @JoinColumn(name="REGULATORCATEGORY_ID"),inverseJoinColumns = @JoinColumn(name="MODALITY_ID"))
	private Set <Modality>  modalitiesAllowed = new HashSet<Modality>();
   	
   	@Column(name = "MAXIMUMONLINEHOURS")
	private Integer  maximumOnlineHours = 0;
   	
   	@Column(name = "MINIMUMSEATTIME")
	private Integer  minimumSeatTime = 0;
   	
   	@Column(name = "MINIMUMSEATTIMEUNIT")
	private String  minimimumSeatTimeUnit;
	
   	@Column(name = "PREASSESSMENTREQUIREDTF")
	private Boolean preAssessmentRequired = Boolean.FALSE;
   	
   	@Column(name = "QUIZREQUIREDTF")
	private Boolean quizRequired = Boolean.FALSE;
   	
   	@Column(name = "FINALASSESSMENTREQUIREDTF")
	private Boolean finalAssessmentRequired = Boolean.FALSE;
   	
   	@Column(name = "PREASSESSMENTPASSINGRATEPERCENTAGE")
	private Double  preAssessmentPassingRatePercentage = 0.00;
   	
   	@Column(name = "PREASSESSMENTNUMBEROFQUESTIONS")
	private Integer     preAssessmentNumberOfQuestions = 0;
   	
   	@Column(name = "QUIZPASSINGRATEPERCENTAGE")
	private Double  quizPassingRatePercentage = 0.00;
   	
   	@Column(name = "QUIZNUMBEROFQUESTIONS")
	private Integer     quizNumberOfQuestions = 0;
   	
   	@Column(name = "FINALASSESSMENTPASSINGRATEPERCENTAGE")
	private Double  finalAssessmentPassingRatePercentage = 0.00;
   	
   	@Column(name = "FINALASSESSMENTNUMBEROFQUESTIONS")
	private Integer     finalAssessmentNumberOfQuestions= 0;
	
   	@Column(name = "CERTIFICATE")
	private String  certificate;
   	
   	@Column(name = "REPORTING")
	private String  reporting;
   	
   	@Column(name = "RECIPROCITYNOTES")
	private String  reciprocityNotes;
	
   	@Column(name = "REPEATABLETF")
	private Boolean repeatable = Boolean.FALSE;
   	
   	@Column(name = "REPETABILITYNOTES")
	private String  repetabilityNotes;
   	
   	@Column(name = "PROVIDERAPPROVALREQUIREDTF")
	private Boolean providerApprovalRequired = Boolean.FALSE;
   	
   	@Column(name = "PROVIDERAPPROVALPERIOD")
	private Integer  providerApprovalPeriod = 0;
   	
   	@Column(name = "PROVIDERAPPROVALPERIODUNIT")
	private String  providerApprovalPeriodUnit;
   	
   	@Column(name = "COURSEAPPROVALREQUIREDTF")
	private Boolean courseApprovalRequired = Boolean.FALSE;
   	
   	@Column(name = "COURSEAPPROVALPERIOD")
	private Integer  courseApprovalPeriod = 0;
   	
   	@Column(name = "COURSEAPPROVALPERIODUNIT")
	private String  courseApprovalPeriodUnit;
   	
   	@ManyToMany  (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name="REGULATORCATEGORY_CREDITREPORTINGFIELD", joinColumns = @JoinColumn(name="REGULATORCATEGORY_ID"),inverseJoinColumns = @JoinColumn(name="CREDITREPORTINGFIELD_ID"))
	private List<CreditReportingField> reportingFields = new ArrayList<CreditReportingField>();
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name="REGULATORCATEGORY_CUSTOMFIELD", joinColumns = @JoinColumn(name="REGULATORCATEGORY_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
	private List<CustomField> customFields = new ArrayList<CustomField>();
	
	@ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name="REGULATORCATEGORY_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="REGULATORCATEGORY_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"))
	private List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
	
	@Transient
	private Boolean selected;

	@Column(name = "REPORTINGREQUIREDTF")
	private Boolean reportingRequired = Boolean.FALSE;
	
	@Column(name = "REPORTINGTIMEFRAME")
	private Integer  reportingTimeframe = 0;
	
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



	public  Boolean getValidationRequired() {
		if(validationRequired==null){
			validationRequired = Boolean.FALSE;
		}
		return validationRequired;
	}



	public void setValidationRequired(Boolean validationRequired) {
		if(validationRequired==null){
			this.validationRequired = Boolean.FALSE;	
		}
		else{
			this.validationRequired = validationRequired;
		}
		
	}



	public double getValidationFrequency() {
		if(validationFrequency == null){
			validationFrequency = 0.00;
		}
		return validationFrequency;
	}



	public void setValidationFrequency(Integer validationFrequency) {
		if(validationFrequency == null){
			this.validationFrequency = 0.00;
		}
		else{
			this.validationFrequency = new Double(validationFrequency);
		}
	}

	public Integer getMaximumOnlineHours() {
		if(maximumOnlineHours == null){
			maximumOnlineHours = 0;
		}
		return maximumOnlineHours;
	}



	public void setMaximumOnlineHours(Integer maximumOnlineHours) {
		if(maximumOnlineHours == null){
			this.maximumOnlineHours = 0;
		}
		else{
			this.maximumOnlineHours = maximumOnlineHours;
		}
	}



	public Integer getMinimumSeatTime() {
		if(minimumSeatTime == null){
			minimumSeatTime = 0;
		}
		return minimumSeatTime;
	}



	public void setMinimumSeatTime(Integer minimumSeatTime) {
		if(minimumSeatTime == null){
			this.minimumSeatTime = 0;
		}
		else{
			this.minimumSeatTime = minimumSeatTime;
		}
	}



	public String getMinimimumSeatTimeUnit() {
		return minimimumSeatTimeUnit;
	}



	public void setMinimimumSeatTimeUnit(String minimimumSeatTimeUnit) {
		this.minimimumSeatTimeUnit = minimimumSeatTimeUnit;
	}



	public  Boolean getPreAssessmentRequired() {
		if(preAssessmentRequired==null){
			preAssessmentRequired = Boolean.FALSE;
		}
		return preAssessmentRequired;
	}



	public void setPreAssessmentRequired(Boolean preAssessmentRequired) {
		if(preAssessmentRequired==null){
			this.preAssessmentRequired = Boolean.FALSE;
		}else{
			this.preAssessmentRequired = preAssessmentRequired;
		}
		
	}



	public  Boolean getQuizRequired() {
		if(quizRequired==null){
			quizRequired = Boolean.FALSE;
		}
		return quizRequired;
	}



	public void setQuizRequired(Boolean quizRequired) {
		if(quizRequired==null){
			this.quizRequired = Boolean.FALSE;
		}
		else{
			this.quizRequired = quizRequired;
		}
		
	}



	public  Boolean getFinalAssessmentRequired() {
		if(finalAssessmentRequired==null){
			finalAssessmentRequired = Boolean.FALSE;
		}
		return finalAssessmentRequired;
	}



	public void setFinalAssessmentRequired(Boolean finalAssessmentRequired) {
		if(finalAssessmentRequired==null){
			this.finalAssessmentRequired = Boolean.FALSE;
		}
		else{
			this.finalAssessmentRequired = finalAssessmentRequired;
		}
	}



	public Double getPreAssessmentPassingRatePercentage() {
		if(preAssessmentPassingRatePercentage == null){
			preAssessmentPassingRatePercentage = 0.00;
		}
		return preAssessmentPassingRatePercentage;
	}



	public void setPreAssessmentPassingRatePercentage(
			Double preAssessmentPassingRatePercentage) {
		if(preAssessmentPassingRatePercentage == null){
			this.preAssessmentPassingRatePercentage = 0.00;
		}
		else{
			this.preAssessmentPassingRatePercentage = preAssessmentPassingRatePercentage;
		}
	}



	public Integer getPreAssessmentNumberOfQuestions() {
		if(preAssessmentNumberOfQuestions == 0){
			preAssessmentNumberOfQuestions = 0;
		}
		return preAssessmentNumberOfQuestions;
	}



	public void setPreAssessmentNumberOfQuestions(Integer preAssessmentNumberOfQuestions) {
		if(preAssessmentNumberOfQuestions == 0){
			this.preAssessmentNumberOfQuestions = 0;
		}else{
			this.preAssessmentNumberOfQuestions = preAssessmentNumberOfQuestions;
		}
	}



	public Double getQuizPassingRatePercentage() {
		if(quizPassingRatePercentage == null){
			quizPassingRatePercentage = 0.00;
		}
		return quizPassingRatePercentage;
	}



	public void setQuizPassingRatePercentage(Double quizPassingRatePercentage) {
		if(quizPassingRatePercentage == null){
			this.quizPassingRatePercentage = 0.00;
		}
		else{
			this.quizPassingRatePercentage = quizPassingRatePercentage;
		}
	}



	public Integer getQuizNumberOfQuestions() {
		if(quizNumberOfQuestions==null){
			quizNumberOfQuestions = 0;
		}
		return quizNumberOfQuestions;
	}



	public void setQuizNumberOfQuestions(Integer quizNumberOfQuestions) {
		if(quizNumberOfQuestions==null){
			this.quizNumberOfQuestions = 0;
		}
		else{
			this.quizNumberOfQuestions = quizNumberOfQuestions;
		}
	}



	public Double getFinalAssessmentPassingRatePercentage() {
		if(finalAssessmentPassingRatePercentage == null){
			finalAssessmentPassingRatePercentage = 0.00;
		}
		return finalAssessmentPassingRatePercentage;
	}



	public void setFinalAssessmentPassingRatePercentage(
			Double finalAssessmentPassingRatePercentage) {
		if(finalAssessmentPassingRatePercentage == null){
			this.finalAssessmentPassingRatePercentage = 0.00;
		}
		else{
			this.finalAssessmentPassingRatePercentage = finalAssessmentPassingRatePercentage;
		}
	}



	public Integer getFinalAssessmentNumberOfQuestions() {
		if(finalAssessmentNumberOfQuestions == null){
			finalAssessmentNumberOfQuestions = 0;
		}
		return finalAssessmentNumberOfQuestions;
	}



	public void setFinalAssessmentNumberOfQuestions(
			Integer finalAssessmentNumberOfQuestions) {
		if(finalAssessmentNumberOfQuestions == null){
			this.finalAssessmentNumberOfQuestions = 0;
		}
		else{
			this.finalAssessmentNumberOfQuestions = finalAssessmentNumberOfQuestions;
		}
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



	public  Boolean getRepeatable() {
		if(repeatable==null){
			repeatable = Boolean.FALSE;
		}
		return repeatable;
	}



	public void setRepeatable(Boolean repeatable) {
		if(repeatable==null){
			this.repeatable = Boolean.FALSE;
		}
		else{
			this.repeatable = repeatable;
		}
	}



	public String getRepetabilityNotes() {
		return repetabilityNotes;
	}



	public void setRepetabilityNotes(String repetabilityNotes) {
		this.repetabilityNotes = repetabilityNotes;
	}



	public  Boolean getProviderApprovalRequired() {
		if(providerApprovalRequired==null){
			providerApprovalRequired = Boolean.FALSE;
		}
		return providerApprovalRequired;
	}



	public void setProviderApprovalRequired(Boolean providerApprovalRequired) {
		if(providerApprovalRequired==null){
			this.providerApprovalRequired = Boolean.FALSE;
		}
		else{
			this.providerApprovalRequired = providerApprovalRequired;
		}
	}



	public Integer getProviderApprovalPeriod() {
		if(providerApprovalPeriod == null){
			providerApprovalPeriod = 0;
		}
		return providerApprovalPeriod;
	}



	public void setProviderApprovalPeriod(Integer providerApprovalPeriod) {
		if(providerApprovalPeriod == null){
			this.providerApprovalPeriod = 0;
		}
		else{
			this.providerApprovalPeriod = providerApprovalPeriod;
		}
	}



	public String getProviderApprovalPeriodUnit() {
		return providerApprovalPeriodUnit;
	}



	public void setProviderApprovalPeriodUnit(String providerApprovalPeriodUnit) {
		this.providerApprovalPeriodUnit = providerApprovalPeriodUnit;
	}



	public  Boolean getCourseApprovalRequired() {
		if(courseApprovalRequired==null){
			courseApprovalRequired = Boolean.FALSE;
		}
		return courseApprovalRequired;
	}



	public void setCourseApprovalRequired(Boolean courseApprovalRequired) {
		if(courseApprovalRequired==null){
			this.courseApprovalRequired = Boolean.FALSE;
		}
		else{
			this.courseApprovalRequired = courseApprovalRequired;
		}
	}



	public Integer getCourseApprovalPeriod() {
		if(courseApprovalPeriod == null){
			courseApprovalPeriod = 0;
		}
		return courseApprovalPeriod;
	}



	public void setCourseApprovalPeriod(Integer courseApprovalPeriod) {
		if(courseApprovalPeriod == null){
			this.courseApprovalPeriod = 0;
		}
		else{
			this.courseApprovalPeriod = courseApprovalPeriod;
		}
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



	public Regulator getRegulator() {
		return this.regulator;
	}

	public void setRegulator(Regulator regulator) {
		this.regulator = regulator;		
	}



	public  Boolean getSelected() {
		if(selected==null)
		{
			selected=Boolean.FALSE;
		}
		return selected;
	}



	public void setSelected(Boolean selected) {
		if(selected==null)
		{
			selected=Boolean.FALSE;
		}else{
			this.selected = selected;
		}
	}


	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public  Boolean getReportingRequired() {
		if(reportingRequired == null){
			reportingRequired = Boolean.FALSE;
		}
		return reportingRequired;
	}

	public void setReportingRequired(Boolean reportingRequired) {
		if(reportingRequired == null){
			this.reportingRequired = Boolean.FALSE;
		}
		else{
			this.reportingRequired = reportingRequired;
		}
	}

	public Integer getReportingTimeframe() {
		if(reportingTimeframe == null){
			reportingTimeframe = 0;
		}
		return reportingTimeframe;
	}

	public void setReportingTimeframe(Integer reportingTimeframe) {
		if(reportingTimeframe == null){
			this.reportingTimeframe = 0;
		}
		else{
			this.reportingTimeframe = reportingTimeframe;
		}
	}

}
