package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

@Entity
@Table(name = "VU360USER")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type",discriminatorType=DiscriminatorType.STRING)  
@DiscriminatorValue(value="VU360UserNew")
public class VU360UserNew implements Serializable {
	private static final long serialVersionUID = 52260678146352049L;
	private static Logger log = Logger.getLogger(VU360UserNew.class);

	@Id
	@javax.persistence.TableGenerator(name = "VU360USER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "vu360user", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "VU360USER_ID")
	@Column(name = "ID", unique = true, nullable = false)
	protected Long id;
	
	@Column(name = "USERGUID", unique = true, nullable = false)
	protected String userGUID = null;

	@Column(name = "USERNAME", unique = true, nullable = false)
	protected String username = null;

	@Column(name = "DOMAIN")
	protected String domain = null;

	@Column(name = "EMAILADDRESS", unique = false, nullable = false)
	protected String emailAddress = null;

	@Column(name = "FIRSTNAME", unique = false, nullable = false)
	protected String firstName = null;

	@Column(name = "LASTNAME", unique = false, nullable = false)
	protected String lastName = null;

	@Column(name = "MIDDLENAME")
	protected String middleName = null;

	@Column(name = "CREATEDDATE", nullable = false)
	protected Date createdDate = new Date();

	@Column(name = "LASTUPDATEDDATE", nullable = false)
	protected Date lastUpdatedDate = new Date();

	@Column(name = "ACCOUNTNONEXPIREDTF")
	protected Boolean  accountNonExpired = Boolean.TRUE;

	@Column(name = "ACCOUNTNONLOCKEDTF")
	protected Boolean  accountNonLocked = Boolean.TRUE;

	@Column(name = "CREDENTIALSNONEXPIREDTF")
	protected Boolean  credentialsNonExpired = Boolean.TRUE;

	@Column(name = "PASSWORD", unique = false, nullable = false)
	protected String password = null;

	@Column(name = "ENABLEDTF")
	protected Boolean  enabled = Boolean.TRUE;

	@Column(name = "NUMLOGONS")
	protected Integer numLogons = 0;

	@Column(name = "LASTLOGONDATE")
	protected Date lastLogonDate;

	@Column(name = "NEWUSERTF")
	protected Boolean  newUser = true;

	@Column(name = "ACCEPTEDEULATF")
	protected Boolean  acceptedEULA = false;

	@Column(name = "CHANGEPASSWORDONLOGINTF")
	protected Boolean  changePasswordOnLogin = false;

	@Column(name = "VISIBLEONREPORTTF")
	protected Boolean  vissibleOnReport = true;

	public Boolean getVissibleOnReport() {
		return vissibleOnReport;
	}

	public void setVissibleOnReport(Boolean vissibleOnReport) {
		this.vissibleOnReport = vissibleOnReport;
	}

	@Column(name = "EXPIRATIONDATE")
	protected Date expirationDate = null;
	
	@Column(name = "NotifyOnLicenseExpire")
	protected Boolean  notifyOnLicenseExpire = Boolean.TRUE;
	
	@Transient
	protected  Boolean passWordChanged = Boolean.FALSE;

	@Transient
	protected  Boolean isAdminMode = Boolean.FALSE;

	@Transient
	protected  Boolean isProctorMode = Boolean.FALSE;

	@Transient
	protected  Boolean isManagerMode = Boolean.FALSE;

	@Transient
	protected  Boolean isLearnerMode = Boolean.FALSE;

	@Transient
	protected  Boolean isInstructorMode = Boolean.FALSE;

	@Transient
	protected  Boolean isAccreditationMode = Boolean.FALSE;

	@Column(name = "SHOWGUIDEDTOURSCREENONLOGIN")
	protected Boolean showGuidedTourScreenOnLogin = Boolean.TRUE;
	
	@Transient
	protected Long roleID;
	
	@Transient
	protected String roleName;
	
	@Transient
	protected Long learnerID;
	
	@Transient
	protected Integer accountNonLockedInt;

	public VU360UserNew() {
	}
	
	public String getName() {
		StringBuffer sb = new StringBuffer();
		sb.append(firstName);
		sb.append(" ");
		sb.append(lastName);
		return sb.toString();
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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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

	public Boolean getAccountNonExpired() {
		if(accountNonExpired==null){
			accountNonExpired=Boolean.TRUE;
		}
		if (accountNonExpired) {
			if (expirationDate != null && expirationDate.before(new Date())) {
				log.debug("User Account[" + getName() + "] is expired.");
				accountNonExpired = Boolean.FALSE;
			} else {
				log.debug(" No Issue....:)");
			}

		}

		return accountNonExpired;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountNonLocked() {
		if(accountNonLocked==null){
			accountNonLocked=Boolean.TRUE;
		}
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		if(accountNonLocked==null){
			this.accountNonLocked = Boolean.TRUE;
		}else{
			this.accountNonLocked = accountNonLocked;
		}
	}

	public Boolean isCredentialsNonExpired() {
		if(credentialsNonExpired==null){
			credentialsNonExpired=Boolean.TRUE;
		}
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

	public Boolean getEnabled() {
		if(enabled==null){
			enabled=Boolean.TRUE;
		}
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

	public Boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(Boolean newUser) {
		this.newUser = newUser;
	}

	public Boolean isAcceptedEULA() {
		return acceptedEULA;
	}

	public void setAcceptedEULA(Boolean acceptedEULA) {
		this.acceptedEULA = acceptedEULA;
	}

	public Boolean getChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}

	public void setChangePasswordOnLogin(Boolean changePasswordOnLogin) {
		this.changePasswordOnLogin = changePasswordOnLogin;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public  Boolean getNotifyOnLicenseExpire() {
		if(notifyOnLicenseExpire == null)
			notifyOnLicenseExpire = Boolean.TRUE;
		
		return notifyOnLicenseExpire;
	}

	public void setNotifyOnLicenseExpire(Boolean notifyOnLicenseExpire) {
		if(notifyOnLicenseExpire==null)
			notifyOnLicenseExpire = Boolean.TRUE;
		else
			this.notifyOnLicenseExpire = notifyOnLicenseExpire;
	}

	public  Boolean isPassWordChanged() {
		return passWordChanged;
	}

	public void setPassWordChanged(Boolean passWordChanged) {
		this.passWordChanged = passWordChanged;
	}

	public  Boolean isAdminMode() {
		return isAdminMode;
	}

	public void setAdminMode(Boolean isAdminMode) {
		this.isAdminMode = isAdminMode;
	}

	public  Boolean isProctorMode() {
		return isProctorMode;
	}

	public void setProctorMode(Boolean isProctorMode) {
		this.isProctorMode = isProctorMode;
	}

	public  Boolean isManagerMode() {
		return isManagerMode;
	}

	public  Boolean isInstructorMode() {
		return isInstructorMode;
	}

	public void setManagerMode(Boolean isManagerMode) {
		this.isManagerMode = isManagerMode;
	}

	public  Boolean isLearnerMode() {
		return this.isLearnerMode;
	}

	public void setLearnerMode(Boolean isLearnerMode) {
		this.isLearnerMode = isLearnerMode;
	}

	public void setInstructorMode(Boolean isInstructorMode) {
		this.isInstructorMode = isInstructorMode;
	}

	public  Boolean isAccreditationMode() {
		return isAccreditationMode;
	}

	public void setAccreditationMode(Boolean isAccreditationMode) {
		this.isAccreditationMode = isAccreditationMode;
	}

	public  Boolean getShowGuidedTourScreenOnLogin() {
		if(showGuidedTourScreenOnLogin==null){
			showGuidedTourScreenOnLogin = Boolean.TRUE;
		}
		
		return showGuidedTourScreenOnLogin;
	}

	public void setShowGuidedTourScreenOnLogin(
			 Boolean showGuidedTourScreenOnLogin) {
		
		if(showGuidedTourScreenOnLogin==null){
			this.showGuidedTourScreenOnLogin = Boolean.TRUE;
		}
		else{
			this.showGuidedTourScreenOnLogin = showGuidedTourScreenOnLogin;
		}
	}

	public Long getRoleID() {
		return roleID;
	}

	public void setRoleID(Long roleID) {
		this.roleID = roleID;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getLearnerID() {
		return learnerID;
	}

	public void setLearnerID(Long learnerID) {
		this.learnerID = learnerID;
	}

	public Integer getAccountNonLockedInt() {
		return accountNonLockedInt;
	}

	public void setAccountNonLockedInt(Integer accountNonLockedInt) {
		this.accountNonLockedInt = accountNonLockedInt;
	}

	


}
