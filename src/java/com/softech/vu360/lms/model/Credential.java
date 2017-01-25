package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Nationalized;

/**
 * 
 * @author muhammad.saleem
 * @Modified haidera.ali
 *
 */
@Entity
@Table(name = "CREDENTIAL")
public class Credential implements SearchableKey {

	private static final long serialVersionUID = 1L;
	public static final String HARD = "Hard";
	public static final String STAGGERED = "Staggered";
	public static final String NA = "Na";
	
	@Id
	@javax.persistence.TableGenerator(name = "CREDENTIAL_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CREDENTIAL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CREDENTIAL_ID")
	private Long id;
	
	@Column(name = "OFFICIALLICENSENAME")
	private String officialLicenseName = null;
	
	@Column(name = "SHORTLICENSENAME")
	private String shortLicenseName = null;
	
	@Column(name = "INFORMATIONLASTVERIFIEDDATE")
	private Date  informationLastVerifiedDate = null;
	
	@Column(name = "VERIFIEDBY")
	private String verifiedBy = null;
	
	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTENTOWNER_ID")
	private ContentOwner contentOwner = null;
	
	@Column(name = "TOTALNUMBEROFLICENSEES")
	private Integer totalNumberOfLicense = 0;
	
	@Nationalized
	@Column(name = "DESCRIPTION")
	private String description;
	
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinTable(name="CREDENTIAL_CUSTOMFIELD", joinColumns = @JoinColumn(name="CREDENTIAL_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID"))
	private List<CustomField> customfields = new ArrayList<CustomField>();
	
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinTable(name="CREDENTIAL_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="CREDENTIAL_ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID"))
	private List<CustomFieldValue>customfieldValues = new ArrayList<CustomFieldValue>();
	
	@Column(name = "JURISDICTION")
	private String jurisdiction  = null;
	/*used to specify custom jurisdiction*/
	
	@Transient
	private String otherJurisdiction  = null;
	
	@Column(name = "active")
	private Boolean active = true;
	
	@Column(name = "CREDENTIALTYPE")
	private String credentialType;
	
	@Column(name = "RENEWALDEADLINETYPE")
	private String renewalDeadlineType;
	
	@Column(name = "HARDDEADLINEMONTH")
	private String hardDeadlineMonth;
	
	@Column(name = "HARDDEADLINEDAY")
	private String hardDeadlineDay;
	
	@Column(name = "HARDDEADLINEYEAR")
	private String hardDeadlineYear;
	
	@Column(name = "STAGGEREDBY")
	private String staggeredBy;
	
	@Column(name = "STAGGEREDTO")
	private String staggeredTo;
	
	@Column(name = "RENEWALFREQUENCY")
	private String renewalFrequency;
	
	@Column(name = "PREREQUISITE")
	private String preRequisite;
	
	@ManyToMany
	@JoinTable(name="CREDENTIAL_TRAINING_COURSE", joinColumns = @JoinColumn(name="CREDENTIAL_ID"),inverseJoinColumns = @JoinColumn(name="COURSE_ID"))
	private List<Course> trainingCourses = new ArrayList<Course>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOfficialLicenseName() {
		return officialLicenseName;
	}

	public void setOfficialLicenseName(String officialLicenseName) {
		this.officialLicenseName = officialLicenseName;
	}

	public String getShortLicenseName() {
		return shortLicenseName;
	}

	public void setShortLicenseName(String shortLicenseName) {
		this.shortLicenseName = shortLicenseName;
	}

	public Date getInformationLastVerifiedDate() {
		return informationLastVerifiedDate;
	}

	public void setInformationLastVerifiedDate(Date informationLastVerifiedDate) {
		this.informationLastVerifiedDate = informationLastVerifiedDate;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public ContentOwner getContentOwner() {
		return contentOwner;
	}

	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}

	public Integer getTotalNumberOfLicense() {
		return totalNumberOfLicense;
	}

	public void setTotalNumberOfLicense(Integer totalNumberOfLicense) {
		this.totalNumberOfLicense = totalNumberOfLicense;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getOtherJurisdiction() {
		return otherJurisdiction;
	}

	public void setOtherJurisdiction(String otherJurisdiction) {
		this.otherJurisdiction = otherJurisdiction;
	}

	public Boolean getActive() {
		return active;
	}

	public  Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCredentialType() {
		return credentialType;
	}

	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}

	public String getRenewalDeadlineType() {
		return renewalDeadlineType;
	}

	public void setRenewalDeadlineType(String renewalDeadlineType) {
		this.renewalDeadlineType = renewalDeadlineType;
	}

	public String getHardDeadlineMonth() {
		return hardDeadlineMonth;
	}

	public void setHardDeadlineMonth(String hardDeadlineMonth) {
		this.hardDeadlineMonth = hardDeadlineMonth;
	}

	public String getHardDeadlineDay() {
		return hardDeadlineDay;
	}

	public void setHardDeadlineDay(String hardDeadlineDay) {
		this.hardDeadlineDay = hardDeadlineDay;
	}

	public String getHardDeadlineYear() {
		return hardDeadlineYear;
	}

	public void setHardDeadlineYear(String hardDeadlineYear) {
		this.hardDeadlineYear = hardDeadlineYear;
	}

	public String getStaggeredBy() {
		return staggeredBy;
	}

	public void setStaggeredBy(String staggeredBy) {
		this.staggeredBy = staggeredBy;
	}

	public String getStaggeredTo() {
		return staggeredTo;
	}

	public void setStaggeredTo(String staggeredTo) {
		this.staggeredTo = staggeredTo;
	}

	public String getRenewalFrequency() {
		return renewalFrequency;
	}

	public void setRenewalFrequency(String renewalFrequency) {
		this.renewalFrequency = renewalFrequency;
	}

	public String getPreRequisite() {
		return preRequisite;
	}

	public void setPreRequisite(String preRequisite) {
		this.preRequisite = preRequisite;
	}

	public List<Course> getTrainingCourses() {
		return trainingCourses;
	}

	public void setTrainingCourses(List<Course> trainingCourses) {
		this.trainingCourses = trainingCourses;
	}

	@Override
	public String getKey() {
		return id.toString();
	}
	
}
