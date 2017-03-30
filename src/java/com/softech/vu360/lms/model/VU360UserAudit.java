package com.softech.vu360.lms.model;

import java.util.Date;

public class VU360UserAudit implements SearchableKey {

    private static final long serialVersionUID = 52260678146352048L;
    
    
    private Long id;
    private String userGUID = null;
    private String username = null;
    private String emailAddress = null;
    private String firstName = null;
    private String lastName = null;
    private String middleName = null;
    private Date createdDate = new Date();
    private Date lastUpdatedDate = new Date();
    private  Boolean accountNonExpired = true;
    private  Boolean accountNonLocked = true;
    private  Boolean credentialsNonExpired = true;
    private String password = null;
    private  Boolean enabled = true;
    private Integer numLogons = 0;
    private Date lastLogonDate;
    private  Boolean newUser = true;
    private  Boolean acceptedEULA = false;
    private  Boolean changePasswordOnLogin = false;
    private  Boolean vissibleOnReport = true;
    private Date expirationDate = null;
    private  Boolean showGuidedTourScreenOnLogin= true;
    
    
	private Long vu360userId;
	private String operation = null;
	private Date createdOn = null;
	private Long createdBy = null;
	private Date updatedOn = null;
	private Long updatedBy = null;
	

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUserGUID() {
		return userGUID;
	}


	public void setUserGUID(String userGUID) {
		this.userGUID = userGUID;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}


	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}


	public  Boolean isAccountNonExpired() {
		return accountNonExpired;
	}


	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}


	public  Boolean isAccountNonLocked() {
		return accountNonLocked;
	}


	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}


	public  Boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}


	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public  Boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}


	public Integer getNumLogons() {
		return numLogons;
	}


	public void setNumLogons(Integer numLogons) {
		this.numLogons = numLogons;
	}


	public Date getLastLogonDate() {
		return lastLogonDate;
	}


	public void setLastLogonDate(Date lastLogonDate) {
		this.lastLogonDate = lastLogonDate;
	}


	public  Boolean isNewUser() {
		return newUser;
	}


	public void setNewUser(Boolean newUser) {
		this.newUser = newUser;
	}


	public  Boolean isAcceptedEULA() {
		return acceptedEULA;
	}


	public void setAcceptedEULA(Boolean acceptedEULA) {
		this.acceptedEULA = acceptedEULA;
	}


	public  Boolean isChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}


	public void setChangePasswordOnLogin(Boolean changePasswordOnLogin) {
		this.changePasswordOnLogin = changePasswordOnLogin;
	}


	public  Boolean isVissibleOnReport() {
		return vissibleOnReport;
	}


	public void setVissibleOnReport(Boolean vissibleOnReport) {
		this.vissibleOnReport = vissibleOnReport;
	}


	public Date getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}


	public  Boolean isShowGuidedTourScreenOnLogin() {
		return showGuidedTourScreenOnLogin;
	}


	public void setShowGuidedTourScreenOnLogin(Boolean showGuidedTourScreenOnLogin) {
		this.showGuidedTourScreenOnLogin = showGuidedTourScreenOnLogin;
	}


	public Long getVu360userId() {
		return vu360userId;
	}


	public void setVu360userId(Long vu360userId) {
		this.vu360userId = vu360userId;
	}


	public String getOperation() {
		return operation;
	}


	public void setOperation(String operation) {
		this.operation = operation;
	}


	public Date getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public Long getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}


	public Date getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}


	public Long getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}	
}
