package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.List;

import com.softech.vu360.lms.model.LearningSession;

public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String phoneExtn;
	private String email;
	private String customerType;
	private String customerCode;
	private String orderId;
	private String websiteUrl;
	private Distributor distributor;
	private String customerGUID;
	private CustomerPreferences customerPreferences;
	private Boolean active = Boolean.TRUE;
	private List<CustomField> customFields;
	private String brandName;
	private Boolean aiccInterfaceEnabled = Boolean.FALSE;
	private Integer lmsProvider = LearningSession.LMS_LS360;
	private Boolean lmsApiEnabledTF;
	private Boolean isDefault;
	private ContentOwner contentOwner;

	private static final String CUSTOMER = "CUSTOMER";
	public static final String B2B = "b2b";
	public static final String B2C = "b2c";
	public static final String CODE_PREFIX = "VUCUS";

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneExtn() {
		return phoneExtn;
	}

	public void setPhoneExtn(String phoneExtn) {
		this.phoneExtn = phoneExtn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public CustomerPreferences getCustomerPreferences() {
		return customerPreferences;
	}

	public void setCustomerPreferences(CustomerPreferences customerPreferences) {
		this.customerPreferences = customerPreferences;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getOwnerType() {
		return CUSTOMER;
	}

	public String getCustomerCode() {
		return this.customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCustomerGUID() {
		return customerGUID;
	}

	public void setCustomerGUID(String customerGUID) {
		this.customerGUID = customerGUID;
	}

	public List<CustomField> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<CustomField> customFields) {
		this.customFields = customFields;
	}

	public Boolean isAiccInterfaceEnabled() {
		return aiccInterfaceEnabled;
	}

	public void setAiccInterfaceEnabled(Boolean aiccInterfaceEnabled) {
		this.aiccInterfaceEnabled = aiccInterfaceEnabled;
	}

	public Integer getLmsProvider() {
		return lmsProvider;
	}

	public void setLmsProvider(Integer lmsProvider) {
		this.lmsProvider = lmsProvider;
	}

	public ContentOwner getContentOwner() {
		return contentOwner;
	}

	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}

	public Boolean isDistributorRepresentative() {
		if (getDistributor() != null
				&& (getDistributor().getMyCustomer() != null && getDistributor().getMyCustomer().getId() == this.id))
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	public Boolean isSelfAuthor() {
		if (getContentOwner() != null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	public Boolean isLmsApiEnabledTF() {
		return lmsApiEnabledTF;
	}

	public void setLmsApiEnabledTF(Boolean lmsApiEnabledTF) {
		this.lmsApiEnabledTF = lmsApiEnabledTF;
	}

	public Boolean isDefault() {
		return isDefault;
	}

	public void setDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Boolean getAiccInterfaceEnabled() {
		return aiccInterfaceEnabled;
	}

}