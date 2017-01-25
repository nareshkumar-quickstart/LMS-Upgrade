/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.ArrayList;
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
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "CREDENTIALCATEGORY")
public class CredentialCategory implements SearchableKey {
	

	private static final long serialVersionUID = 1L;
	public static final String[] CATEGORY_TYPES = {
			CredentialCategory.PRE_LICENSE,
			CredentialCategory.POST_LICENSE,
			CredentialCategory.CONTINUING_EDUCATION,
			CredentialCategory.CERTIFICATION,
			//CredentialCategory.DESIGNATION,
			CredentialCategory.PROFESSIONAL_DEVELOPMENT };
	// static constants	
	public static final String PRE_LICENSE = "Pre-License";
	public static final String POST_LICENSE = "Post-License";
	public static final String CONTINUING_EDUCATION = "Continuing Education";			
	public static final String CERTIFICATION = "Certification";
	public static final String DESIGNATION = "Designation";
	public static final String PROFESSIONAL_DEVELOPMENT = "Professional Development";
	
	// private members
	@Id
	@javax.persistence.TableGenerator(name = "CREDENTIALCATEGORY_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CREDENTIALCATEGORY", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CREDENTIALCATEGORY_ID")
	private Long id;
	
	@Column(name = "NAME")
	private String name = null;
	
	@Column(name = "CATEGORYTYPE")
	private String categoryType = null;
	
	@Column(name = "HOURS")
	private float hours;
	
	@Column(name = "DESCRIPTION")
	private String description = null;
	
	@OneToOne
	@JoinColumn(name = "CREDENTIAL_ID")
	private Credential credential = null;
	
	@ManyToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(name="CREDENTIALCATEGORY_CUSTOMFIELD", joinColumns = @JoinColumn(name="CREDENTIALCATEGORY_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
	private List<CustomField> customFields = new ArrayList<CustomField>();
	
	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="CREDENTIALCATEGORY_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="CREDENTIALCATEGORY_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"))
	private List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
	
	@Column(name = "COMPLETIONDEADLINEMONTHS")
	private Float completionDeadlineMonths;
	
	@Transient
	private String strCompletionDeadlineMonths;
	
	@Column(name = "COMPLETIONDEADLINEFROM")
	private String completionDeadlineFrom= null;
	
	@Column(name = "COVERAGE")
	private String coverage= null;
	
	@Column(name = "COVERAGEEXPLANATION")
	private String coverageExplanation= null;
	// Form member to avoid type mismatch
	@Transient
	private String strHours = null;
	
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		
		return null;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param categoryType the categoryType to set
	 */
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	/**
	 * @return the categoryType
	 */
	public String getCategoryType() {
		return categoryType;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(float hours) {
		this.hours = hours;
	}

	/**
	 * @return the hours
	 */
	public float getHours() {
		return hours;
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

	/**
	 * @param credential the credential to set
	 */
	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	/**
	 * @return the credential
	 */
	public Credential getCredential() {
		return credential;
	}

	/**
	 * @param strHours the strHours to set
	 */
	public void setStrHours(String strHours) {
		this.strHours = strHours;
	}

	/**
	 * @return the strHours
	 */
	public String getStrHours() {
		return strHours;
	}
	
	public void copy ( CredentialCategory category ) {
		
		// Copy all values except ID
		this.categoryType = category.getCategoryType();
		this.credential = category.getCredential();
		this.description = category.getDescription();
		this.hours = category.getHours();
		this.name = category.getName();
		this.strHours = category.getStrHours();
		this.customFields = category.getCustomFields();
		this.customFieldValues = category.getCustomFieldValues();
	}

	/**
	 * @param customFields the customFields to set
	 */
	public void setCustomFields(List<CustomField> customFields) {
		this.customFields = customFields;
	}

	/**
	 * @return the customFields
	 */
	public List<CustomField> getCustomFields() {
		return customFields;
	}

	/**
	 * @param customFieldValues the customFieldValues to set
	 */
	public void setCustomFieldValues(List<CustomFieldValue> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}

	/**
	 * @return the customFieldValues
	 */
	public List<CustomFieldValue> getCustomFieldValues() {
		return customFieldValues;
	}

	public Float getCompletionDeadlineMonths() {
		return completionDeadlineMonths;
	}

	public void setCompletionDeadlineMonths(Float completionDeadlineMonths) {
		this.completionDeadlineMonths = completionDeadlineMonths;
	}
	
	public String getStrCompletionDeadlineMonths() {
		return strCompletionDeadlineMonths;
	}

	public void setStrCompletionDeadlineMonths(String strCompletionDeadlineMonths) {
		this.strCompletionDeadlineMonths = strCompletionDeadlineMonths;
	}

	public String getCompletionDeadlineFrom() {
		return completionDeadlineFrom;
	}

	public void setCompletionDeadlineFrom(String completionDeadlineFrom) {
		this.completionDeadlineFrom = completionDeadlineFrom;
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
