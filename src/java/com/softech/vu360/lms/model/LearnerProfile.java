/**
 * 
 */
package com.softech.vu360.lms.model;

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

/**
 * 
 * @author muhammad.saleem
 *
 */

@Entity
@Table(name = "LEARNERPROFILE")
public class LearnerProfile implements SearchableKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4387003110197587305L;

	@Id
	@javax.persistence.TableGenerator(name = "LEARNERPROFILE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNERPROFILE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNERPROFILE_ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY, cascade={ CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "LEARNER_ID")
	private Learner learner ;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="TIMEZONE_ID")
	private TimeZone timeZone ;
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })//Don't set FetchType.Lazy (LMS-18797)
    @JoinColumn(name="ADDRESS_ID")
	private Address learnerAddress = null;
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })//Don't set FetchType.Lazy (LMS-18797)
    @JoinColumn(name="ADDRESS2_ID")
	private Address learnerAddress2 = null;
	
	@Column(name="OFFICEPHONE")
	private String officePhone = null;
	
	@Column(name="OFFICEPHONEEXT")
	private String officePhoneExtn = null;
	
	@Column(name="MOBILEPHONE")
	private String mobilePhone = null;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE } , fetch=FetchType.LAZY)
    @JoinTable(name="LEARNER_CUSTOMFIELDVALUE", joinColumns = @JoinColumn(name="LEARNERPROFILE_ID",referencedColumnName="ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELDVALUE_ID",referencedColumnName="ID"))
    private List<CustomFieldValue> customFieldValues=null;
	
  /*
    @OneToMany(mappedBy = "learnerprofile" )
    private List<CreditReportingFieldValue> creditReportingFieldValues = null ;
    
    
    
	public List<CreditReportingFieldValue> getCreditReportingFieldValues() {
		return creditReportingFieldValues;
	}

	public void setCreditReportingFieldValues(
			List<CreditReportingFieldValue> creditReportingFieldValues) {
		this.creditReportingFieldValues = creditReportingFieldValues;
	}
	*/

	public String getKey() {
		return id.toString();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the learner
	 */
	public Learner getLearner() {
		return this.learner;
	}

	/**
	 * @param learner
	 *            the learner to set
	 */
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	/**
	 * @return the learner
	 */
	public TimeZone getTimeZone() {
		return this.timeZone;
	}

	/**
	 * @param learner
	 *            the learner to set
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 * @return the learnerAddress
	 */
	public Address getLearnerAddress() {
		return learnerAddress;
	}

	/**
	 * @param learnerAddress
	 *            the learnerAddress to set
	 */
	public void setLearnerAddress(Address learnerAddress) {
		this.learnerAddress = learnerAddress;
	}

	/**
	 * @return the officePhone
	 */
	public String getOfficePhone() {
		return officePhone;
	}

	/**
	 * @param officePhone
	 *            the officePhone to set
	 */
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	/**
	 * @return the officePhoneExtn
	 */
	public String getOfficePhoneExtn() {
		return officePhoneExtn;
	}

	/**
	 * @param officePhoneExtn
	 *            the officePhoneExtn to set
	 */
	public void setOfficePhoneExtn(String officePhoneExtn) {
		this.officePhoneExtn = officePhoneExtn;
	}

	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * @param mobilePhone
	 *            the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return the learnerAddress2
	 */
	public Address getLearnerAddress2() {
		return learnerAddress2;
	}

	/**
	 * @param learnerAddress2
	 *            the learnerAddress2 to set
	 */
	public void setLearnerAddress2(Address learnerAddress2) {
		this.learnerAddress2 = learnerAddress2;
	}

	public List<CustomFieldValue> getCustomFieldValues() {
		return customFieldValues;
	}

	public void setCustomFieldValues(List<CustomFieldValue> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}

	public CustomFieldValue getCustomFieldValue(CustomField customField){
		for(CustomFieldValue value: this.customFieldValues)
			if(value !=null && value.getCustomField()!=null && value.getCustomField().getId()== customField.getId())
		   return value;	
		
		return null;
		
	}
}
