package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.Brander;

public class LearnerDetailsForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields=new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
	private VU360User vu360User;
	private List<CustomFieldValue> customFieldValueList = new ArrayList<CustomFieldValue>(); //for add CustomField


	private String stateLabel;
	private String stateLabel2;
	private String countryLabel;
	private String countryLabel2;

	public String getStateLabel() {
		return stateLabel;
	}
	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	public String getStateLabel2() {
		return stateLabel2;
	}

	public void setStateLabel2(String stateLabel2) {
		this.stateLabel2 = stateLabel2;
	}

	public String getCountryLabel() {
		return countryLabel;
	}

	public void setCountryLabel(String countryLabel) {
		this.countryLabel = countryLabel;
	}

	public String getCountryLabel2() {
		return countryLabel2;
	}

	public void setCountryLabel2(String countryLabel2) {
		this.countryLabel2 = countryLabel2;
	}

	public LearnerDetailsForm() {
	}

	private Brander brander=null;
	
	private String defaultRoleName;
	private List<String> roles = new ArrayList<String>();
	
	//The Step 1 form fields
	private String userName;
	private String oldUserName	;
	
	public String getOldUserName() {
		return oldUserName;
	}

	public void setOldUserName(String oldUserName) {
		this.oldUserName = oldUserName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	private String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	private String middleName;

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	private String lastName;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	private String officePhone;

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	
	private String officePhoneExtn;

	public String getOfficePhoneExtn() {
		return officePhoneExtn;
	}

	public void setOfficePhoneExtn(String officePhoneExtn) {
		this.officePhoneExtn = officePhoneExtn;
	}
	
	private String mobilePhone;

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	private String streetAddress1;

	public String getStreetAddress1() {
		return streetAddress1;
	}

	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}
	
	private String streetAddress1a;

	public String getStreetAddress1a() {
		return streetAddress1a;
	}

	public void setStreetAddress1a(String streetAddress1a) {
		this.streetAddress1a = streetAddress1a;
	}
	
	
	
	private String streetAddress2;

	public String getStreetAddress2() {
		return streetAddress2;
	}

	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}
	
	private String streetAddress2a;

	public String getStreetAddress2a() {
		return streetAddress2a;
	}

	public void setStreetAddress2a(String streetAddress2a) {
		this.streetAddress2a = streetAddress2a;
	}
	
	
	
	
	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city1) {
		this.city = city1;
	}
	private String city2;

	public String getCity2() {
		return city2;
	}

	public void setCity2(String city2) {
		this.city2 = city2;
	}
	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	private String state2;

	public String getState2() {
		return state2;
	}

	public void setState2(String state2) {
		this.state2 = state2;
	}
	
	
	private String zipcode;

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	private String zipcode2;

	public String getZipcode2() {
		return zipcode2;
	}

	public void setZipcode2(String zipcode2) {
		this.zipcode2 = zipcode2;
	}
	
	private String country;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country= country;
	}
	
	private String country2;

	public String getCountry2() {
		return country2;
	}

	public void setCountry2(String country2) {
		this.country2 = country2;
	}
	
	
	private String emailAddress;
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	private String password;
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	private String confirmPassword;
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	private boolean accountNonLocked=true;
	
	public boolean getAccountNonLocked() {
		return accountNonLocked;
	}
	
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

    private boolean accountNonExpired=true;

    public boolean getAccountNonExpired() {
    	return accountNonExpired;
    }
    
    public void setAccountNonExpired(boolean accountNonExpired) {
    	this.accountNonExpired = accountNonExpired;
    }
    
    private boolean enabled=true;
    
    public boolean getEnabled() {
    	return enabled;
    }
    
    public void setEnabled(boolean enabled) {
    	this.enabled = enabled;
    }

	private boolean visibleOnReport=true;

	public boolean getVisibleOnReport() {
		return visibleOnReport;
	}

	public void setVisibleOnReport(boolean visibleOnReport) {
		this.visibleOnReport = visibleOnReport;
	}

	private boolean changePasswordOnLogin=false;

	public boolean getChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}
	
	public void setChangePasswordOnLogin(boolean changePasswordOnLogin) {
		this.changePasswordOnLogin = changePasswordOnLogin;
	}
	
	private String role;

	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	private String roleName;
	
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	private String expirationDate;

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	//Step 2 form fields
	private String[] groups;
	
	public String[] getGroups() {
		return groups;
	}
	
	public void setGroups(String[] groups) {
		this.groups = groups;
	}
	private String[] groupnames;
	
	private String[] availableLearnerGroups;

	public String[] getAvailableLearnerGroups() {
		return availableLearnerGroups;
	}

	public void setAvailableLearnerGroups(String[] learnerGroups) {
		this.availableLearnerGroups = learnerGroups;
	}
	
	private String[] selectedLearnerGroupNames;
	
	private String[] selectedLearnerGroups;
	
	public String[] getSelectedLearnerGroups() {
		return selectedLearnerGroups;
	}

	public void setSelectedLearnerGroups(String[] selectedLearnerGroups) {
		this.selectedLearnerGroups = selectedLearnerGroups;
	}

	public String getDefaultRoleName() {
		return defaultRoleName;
	}

	public void setDefaultRoleName(String defaultRoleName) {
		this.defaultRoleName = defaultRoleName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	

	/**
	 * @return the groupnames
	 */
	public String[] getGroupnames() {
		return groupnames;
	}

	/**
	 * @param groupnames the groupnames to set
	 */
	public void setGroupnames(String[] groupnames) {
		this.groupnames = groupnames;
	}

	/**
	 * @return the selectedLearnerGroupNames
	 */
	public String[] getSelectedLearnerGroupNames() {
		return selectedLearnerGroupNames;
	}

	/**
	 * @param selectedLearnerGroupNames the selectedLearnerGroupNames to set
	 */
	public void setSelectedLearnerGroupNames(String[] selectedLearnerGroupNames) {
		this.selectedLearnerGroupNames = selectedLearnerGroupNames;
	}

	/**
	 * @return the brander
	 */
	public Brander getBrander() {
		return brander;
	}

	/**
	 * @param brander the brander to set
	 */
	public void setBrander(Brander brander) {
		this.brander = brander;
	}

	public String eventSource = null;

	/**
	 * @return the eventSource
	 */
	public String getEventSource() {
		return eventSource;
	}

	/**
	 * @param eventSource the eventSource to set
	 */
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}

	/**
	 * @return the customFields
	 */
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFields() {
		return customFields;
	}

	/**
	 * @param customFields the customFields to set
	 */
	public void setCustomFields(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		this.customFields = customFields;
	}

	/**
	 * @return the vu360User
	 */
	public VU360User getVu360User() {
		return vu360User;
	}

	/**
	 * @param vu360User the vu360User to set
	 */
	public void setVu360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}

	/**
	 * @return the customFieldValueList
	 */
	public List<CustomFieldValue> getCustomFieldValueList() {
		return customFieldValueList;
	}

	/**
	 * @param customFieldValueList the customFieldValueList to set
	 */
	public void setCustomFieldValueList(List<CustomFieldValue> customFieldValueList) {
		this.customFieldValueList = customFieldValueList;
	}
}