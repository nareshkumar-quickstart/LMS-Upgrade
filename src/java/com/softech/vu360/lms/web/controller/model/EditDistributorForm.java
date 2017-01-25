package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Dyutiman
 *
 */
public class EditDistributorForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public EditDistributorForm() {
	}
	
	private Distributor distributor = null;
	private String name;
	private String firstName;
	private String lastName;
	private String websiteURL;
	private String emailAddress;
	private String officePhone;
	private String officePhoneExt;
	private String brandName;
	private long   brandId;
	private String type;
	private Boolean active;
	private Boolean selfReporting;
	private Boolean markedPrivate;
	private Boolean selfAuthor;
	private Address distributorAddress; 
	private Address distributorAddress2;
	private String distributorCode;
	private boolean isCorporateAuthorVar;
	
	/**
	 * Whether call logging is enabled or not
	 * @since 4.13 {LMS-8108}
	 * @author sm.humayun
	 */
	private Boolean callLoggingEnabled;
	
	/**
	 * Return <code>callLoggingEnabled</code>
	 * @return <code>callLoggingEnabled</code>
	 * @since 4.13 {LMS-8108}
	 * @author sm.humayun 
	 */
	public Boolean getCallLoggingEnabled() {
		return callLoggingEnabled;
	}
	
	/**
	 * Set <code>callLoggingEnabled</code>
	 * @param callLoggingEnabled <code>callLoggingEnabled</code>
	 * @since 4.13 {LMS-8108}
	 * @author sm.humayun
	 */
	public void setCallLoggingEnabled(Boolean callLoggingEnabled) {
		this.callLoggingEnabled = callLoggingEnabled;
	}
	
	public Distributor getDistributor() {
		return distributor;
	}
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
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
	public String getWebsiteURL() {
		return websiteURL;
	}
	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getOfficePhoneExt() {
		return officePhoneExt;
	}
	public void setOfficePhoneExt(String officePhoneExt) {
		this.officePhoneExt = officePhoneExt;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
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
	public Boolean getSelfAuthor() {
		return selfAuthor;
	}
	public Boolean isSelfAuthor() {
		return selfAuthor;
	}
	public void setSelfAuthor(Boolean selfAuthor) {
		this.selfAuthor = selfAuthor;
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

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public boolean isCorporateAuthorVar() {
		return isCorporateAuthorVar;
	}

	public void setCorporateAuthorVar(boolean isCorporateAuthorVar) {
		this.isCorporateAuthorVar = isCorporateAuthorVar;
	} 
	
	
}