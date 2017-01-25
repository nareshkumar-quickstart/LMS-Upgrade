package com.softech.vu360.lms.service.impl;

import java.util.Hashtable;

public class OptimizedBatchImportLearnersErrors {

	private int recordNumber;
	private boolean invalidFormat;
	private boolean orgGroupMissing;
	private boolean learnerGroupMissing;
	private boolean firstNameBlank;
	private boolean lastNameBlank;
	private boolean emailAddressBlank;
	private boolean passwordBlank;
	private boolean shortPassword;
	private boolean countryBlank;
	private boolean invalidEmailAddress;
	private boolean userNameBlank;
	private boolean currentCustomerUser;
	private boolean userAlreadyExists;
	private boolean userAlreadyExistsInAD;
	private boolean invalidZip;
	private boolean invalidCountry;
	private boolean invalidState;
	
	
	
	
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String userName;
	private String country;
	private String state;
	private String zip;
	
	private Hashtable<String, String> customFields=new Hashtable<String, String>();
	private Hashtable<String, String> reportingFields = new Hashtable<String, String>();
	

	public boolean isInvalidFormat() {
		return invalidFormat;
	}

	public void setInvalidFormat(boolean invalidFormat) {
		this.invalidFormat = invalidFormat;
	}

	public int getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}

	public boolean isOrgGroupMissing() {
		return orgGroupMissing;
	}

	public void setOrgGroupMissing(boolean orgGroupMissing) {
		this.orgGroupMissing = orgGroupMissing;
	}

	
	public boolean isLearnerGroupMissing() {
		return learnerGroupMissing;
	}

	public void setLearnerGroupMissing(boolean learnerGroupMissing) {
		this.learnerGroupMissing = learnerGroupMissing;
	}

	public boolean isFirstNameBlank() {
		return firstNameBlank;
	}

	public void setFirstNameBlank(boolean firstNameBlank) {
		this.firstNameBlank = firstNameBlank;
	}

	public boolean isLastNameBlank() {
		return lastNameBlank;
	}

	public void setLastNameBlank(boolean lastNameBlank) {
		this.lastNameBlank = lastNameBlank;
	}

	public boolean isEmailAddressBlank() {
		return emailAddressBlank;
	}

	public void setEmailAddressBlank(boolean emailAddressBlank) {
		this.emailAddressBlank = emailAddressBlank;
	}

	public boolean isPasswordBlank() {
		return passwordBlank;
	}

	public void setPasswordBlank(boolean passwordBlank) {
		this.passwordBlank = passwordBlank;
	}

	public boolean isShortPassword() {
		return shortPassword;
	}

	public void setShortPassword(boolean shortPassword) {
		this.shortPassword = shortPassword;
	}

	public boolean isInvalidEmailAddress() {
		return invalidEmailAddress;
	}

	public void setInvalidEmailAddress(boolean invalidEmailAddress) {
		this.invalidEmailAddress = invalidEmailAddress;
	}

	public boolean isCountryBlank() {
		return countryBlank;
	}

	public void setCountryBlank(boolean countryBlank) {
		this.countryBlank = countryBlank;
	}

	public boolean isUserNameBlank() {
		return userNameBlank;
	}

	public void setUserNameBlank(boolean userNameBlank) {
		this.userNameBlank = userNameBlank;
	}

	public boolean isCurrentCustomerUser() {
		return currentCustomerUser;
	}

	public void setCurrentCustomerUser(boolean currentCustomerUser) {
		this.currentCustomerUser = currentCustomerUser;
	}

	public boolean isUserAlreadyExists() {
		return userAlreadyExists;
	}

	public void setUserAlreadyExists(boolean userAlreadyExists) {
		this.userAlreadyExists = userAlreadyExists;
	}

	public boolean isUserAlreadyExistsInAD() {
		return userAlreadyExistsInAD;
	}

	public void setUserAlreadyExistsInAD(boolean userAlreadyExistsInAD) {
		this.userAlreadyExistsInAD = userAlreadyExistsInAD;
	}

	public boolean isInvalidZip() {
		return invalidZip;
	}

	public void setInvalidZip(boolean invalidZip) {
		this.invalidZip = invalidZip;
	}

	public boolean isInvalidCountry() {
		return invalidCountry;
	}

	public void setInvalidCountry(boolean invalidCountry) {
		this.invalidCountry = invalidCountry;
	}

	public boolean isInvalidState() {
		return invalidState;
	}

	public void setInvalidState(boolean invalidState) {
		this.invalidState = invalidState;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Hashtable<String, String> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Hashtable<String, String> customFields) {
		this.customFields = customFields;
	}

	public Hashtable<String, String> getReportingFields() {
		return reportingFields;
	}

	public void setReportingFields(Hashtable<String, String> reportingFields) {
		this.reportingFields = reportingFields;
	}	
}
