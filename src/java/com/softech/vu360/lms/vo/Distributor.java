package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.List;

public class Distributor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private DistributorPreferences distributorPreferences;
	private String officePhone;
	private String officePhoneExtn;
	private String mobilePhone;
	private String websiteUrl;
	private String distributorEmail;
	private String distributorCode;
	private Boolean active = Boolean.TRUE;
	private String firstName;
	private String lastName;

	private String brandName;
	private Boolean selfReporting = Boolean.FALSE;
	private Boolean markedPrivate;
	private List<CustomField> customFields;
	private String type;
	private MyCustomer myCustomer;
	private ContentOwner contentOwner;
	private Boolean isCorporateAuthorVar;
	private Boolean callLoggingEnabled = Boolean.FALSE;
	private Boolean lmsApiEnabledTF;

	private static final String DISTRIBUTOR = "DISTRIBUTOR";
	public static final String CODE_PREFIX = "VURES";

	public Boolean getCallLoggingEnabled() {
		if(callLoggingEnabled==null){
			callLoggingEnabled=Boolean.FALSE;
		}
		return callLoggingEnabled;
	}

	public void setCallLoggingEnabled(Boolean callLoggingEnabled) {
		this.callLoggingEnabled = callLoggingEnabled;
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

	public DistributorPreferences getDistributorPreferences() {
		return distributorPreferences;
	}

	public void setDistributorPreferences(DistributorPreferences distributorPreferences) {
		this.distributorPreferences = distributorPreferences;
	}

	public String getDistributorEmail() {
		return distributorEmail;
	}

	public void setDistributorEmail(String distributorEmail) {
		this.distributorEmail = distributorEmail;
	}

	public Boolean isActive() {
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

	public String getDistributorCodeUI() {
		return CODE_PREFIX + "-" + this.getId();
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

	public Boolean isSelfReporting() {
		return selfReporting;
	}

	public void setSelfReporting(Boolean selfReporting) {
		this.selfReporting = selfReporting;
	}

	public Boolean isMarkedPrivate() {
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

	public MyCustomer getMyCustomer() {
		return myCustomer;
	}

	public void setMyCustomer(MyCustomer myCustomer) {
		this.myCustomer = myCustomer;
	}

	public ContentOwner getContentOwner() {
		return contentOwner;
	}

	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}

	public Boolean isSelfAuthor() {
		if (getContentOwner() != null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	public String getMyCustomerName() {
		if (getMyCustomer() != null) {
			return getMyCustomer().getName();
		} else {
			return "";
		}
	}

	public Boolean isLmsApiEnabledTF() {
		return lmsApiEnabledTF;
	}

	public void setLmsApiEnabledTF(Boolean lmsApiEnabledTF) {
		this.lmsApiEnabledTF = lmsApiEnabledTF;
	}

	public Boolean isCorporateAuthorVar() {
		return isCorporateAuthorVar;
	}

	public void setCorporateAuthorVar(Boolean isCorporateAuthorVar) {
		this.isCorporateAuthorVar = isCorporateAuthorVar;
	}
}
