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
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author marium.saud
 * @Modifier Raja Wajahat Ali
 *
 */

@Entity
@Table(name = "DISTRIBUTOR")
@NamedStoredProcedureQuery(name = "Distributor.insertDistribiutorIDsInTmpTbl", procedureName = "INSERT_TEMP_DISTRIBUTOR_ID"
, parameters = {
		  @StoredProcedureParameter(mode = ParameterMode.IN, name = "IDS", type = String.class)
		  }
)
public class Distributor extends Owner implements SearchableKey {

	private static final long serialVersionUID = 2518171447836672166L;

	public static final String CODE_PREFIX="VURES";
    public static final String DISTRIBUTOR= "DISTRIBUTOR";

	
	/*@Id
    @javax.persistence.TableGenerator(name = "DISTRIBUTOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "DISTRIBUTOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "DISTRIBUTOR_ID")
	private Long id;*/
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.TABLE, generator = "seqDistributorId")
	 @GenericGenerator(name = "seqDistributorId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	 @Parameter(name = "table_name", value = "VU360_SEQ"),
	 @Parameter(name = "value_column_name", value = "NEXT_ID"),
	 @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	 @Parameter(name = "segment_value", value = "DISTRIBUTOR") })
	 private Long id;
	 
	
	@Column(name="NAME")
	private String name;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="BRAND_ID")
	private Brand brand ;
	
	@OneToOne(mappedBy = "distributor" , fetch=FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private DistributorPreferences distributorPreferences ;
	
	@OneToOne (fetch=FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="ADDRESS_ID")
	private Address distributorAddress ;
	
	@OneToOne (fetch=FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="ADDRESS_ID_2")
	private Address distributorAddress2 ;
	
	@Column(name="OFFICEPHONE")
	private String officePhone;
	
	@Column(name="PHONEEXTN")
	private String officePhoneExtn;
	
	@Column(name="MOBILEPHONE")
	private String mobilePhone;
	
	@Column(name="WEBSITE")
	private String websiteUrl;
	
	@Column(name="EMAIL")
	private String distributorEmail;
	
	@Column(name="DISTRIBUTORCODE")
	private String distributorCode;
	
	@Column(name="STATUS")
	private Boolean active = Boolean.TRUE;
	
	@Column(name="FIRSTNAME")
	private String firstName;
	
	@Column(name="LASTNAME")
	private String lastName;

	@Column(name="BRANDNAME")
	private String brandName;
	
	@Column(name="SELFREPORTING")
	private Boolean selfReporting = Boolean.FALSE;
	
	@Column(name="MARKEDPRIVATE")
	private Boolean markedPrivate;
	
	@ManyToMany
    @JoinTable(name="DISTRIBUTOR_CUSTOMFIELD", joinColumns = @JoinColumn(name="DISTRIBUTOR_ID",referencedColumnName="ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID",referencedColumnName="ID"))
    private List<CustomField> customFields;
	
	@Column(name="RESELLERTYPE")
	private String type;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="MYCUSTOMER_ID")
	private Customer myCustomer ;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CONTENTOWNER_ID")
    private ContentOwner contentOwner ;
	
	@Column(name="IS_CORPORATE_AUTHOR_VAR")
	private Boolean isCorporateAuthorVar; 

	@Column(name="CALLLOGGINGENABLEDTF")
	private Boolean callLoggingEnabled = Boolean.FALSE;
	
	@Column(name="LmsAPI_Enabled_TF")
	private Boolean lmsApiEnabledTF= Boolean.FALSE;
	
	
	public Boolean getCallLoggingEnabled() {
		if(callLoggingEnabled==null){
			callLoggingEnabled=Boolean.FALSE;
		}
		return callLoggingEnabled;
	}
	
	public void setCallLoggingEnabled(Boolean callLoggingEnabled) {
		this.callLoggingEnabled = callLoggingEnabled;
	}
	
	public Address getDistributorAddress() {
			return distributorAddress;
	}

	public void setDistributorAddress(Address distributorAddress) {
		this.distributorAddress = distributorAddress;
	}

	public Address getDistributorAddress2() {
		return distributorAddress2;
	}

	public void setDistributorAddress2(Address distributorAddress2) {
		this.distributorAddress2 = distributorAddress2;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getOfficePhoneExtn() {
		return officePhoneExtn;
	}

	public void setOfficePhoneExtn(String officePhoneExtn) {
		this.officePhoneExtn = officePhoneExtn;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getKey() {
		return id.toString();
	}

	public DistributorPreferences getDistributorPreferences() {
		return distributorPreferences;
	}

	public void setDistributorPreferences(
			DistributorPreferences distributorPreferences) {
		this.distributorPreferences = distributorPreferences;
	}

	public String getDistributorEmail() {
		return distributorEmail;
	}

	public void setDistributorEmail(String distributorEmail) {
		this.distributorEmail = distributorEmail;
	}

	public  Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOwnerType() {
		return DISTRIBUTOR;
	}

	public String getDistributorCode() {
		return distributorCode;
	}
	
	public String getDistributorCodeUI()
	{
		return CODE_PREFIX+"-"+this.getId();
	}
	
	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public  Boolean isSelfReporting() {
		return selfReporting;
	}

	public void setSelfReporting(Boolean selfReporting) {
		this.selfReporting = selfReporting;
	}

	public  Boolean isMarkedPrivate() {
		return markedPrivate;
	}

	public void setMarkedPrivate(Boolean markedPrivate) {
		this.markedPrivate = markedPrivate;
	}

	public List<CustomField> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<CustomField> customFields) {
		this.customFields = customFields;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Customer getMyCustomer() {
		return myCustomer;
	}

	public void setMyCustomer(Customer customer) {
		this.myCustomer = customer;
	}

	public ContentOwner getContentOwner() {
		return contentOwner;
	}

	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}
	
	public  Boolean isSelfAuthor(){
		if(getContentOwner()!=null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}
	public String getMyCustomerName(){
		if(getMyCustomer()!=null){
			return getMyCustomer().getName();
		}else
		{
			return "";
		}
	}
	
	public  Boolean getLmsApiEnabledTF() {
		if(lmsApiEnabledTF==null){
			lmsApiEnabledTF=Boolean.FALSE;
		}
		return lmsApiEnabledTF;
	}

	public void setLmsApiEnabledTF(Boolean lmsApiEnabledTF) {
		if(lmsApiEnabledTF==null){
			this.lmsApiEnabledTF=Boolean.FALSE;
		}else{
			this.lmsApiEnabledTF = lmsApiEnabledTF;
		}
	}

	public Boolean isCorporateAuthorVar() {
		if(this.isCorporateAuthorVar != null)
			return this.isCorporateAuthorVar;
		else
			return Boolean.FALSE;
	}

	public void setCorporateAuthorVar(Boolean isCorporateAuthorVar) {
		if(isCorporateAuthorVar != null)
			this.isCorporateAuthorVar = isCorporateAuthorVar;
		else
			this.isCorporateAuthorVar = Boolean.FALSE;
	}

	public void initializeOwnerParams(){//This method will be used to set values for Owner while saving survey
		super.setId(this.id);
		super.setOwnerType(getOwnerType());
	}

}
