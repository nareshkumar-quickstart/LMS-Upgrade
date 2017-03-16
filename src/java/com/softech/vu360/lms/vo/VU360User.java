package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.softech.vu360.util.ProctorStatusEnum;

public class VU360User implements UserDetails, Serializable{

	private static final long serialVersionUID = 52260678146352048L;

	private Long id;
	private String userGUID = null;
	private String username = null;
	private String domain = null;
	private String emailAddress = null;
	private String firstName = null;
	private String lastName = null;
	private String middleName = null;
	private Date createdDate = new Date();
	private Date lastUpdatedDate = new Date();
	private Boolean  accountNonExpired = Boolean.TRUE;
	private boolean  accountNonLocked = true;
	private Boolean  credentialsNonExpired = Boolean.TRUE;
	private String password = null;
	private Boolean  enabled = Boolean.TRUE;
	private Integer numLogons = 0;
	private Date lastLogonDate;
	private Boolean  newUser = Boolean.TRUE;
	private Boolean  acceptedEULA = Boolean.FALSE;
	private Boolean  changePasswordOnLogin = Boolean.FALSE;
	private Boolean  vissibleOnReport = Boolean.TRUE;
	private Date expirationDate = null;
	private Boolean  notifyOnLicenseExpire = Boolean.TRUE;
	private Learner learner;
	private TrainingAdministrator trainingAdministrator;
	private LMSAdministrator lmsAdministrator;
	private Proctor proctor;
	private RegulatoryAnalyst regulatoryAnalyst;
	private Instructor instructor;
	private Set<LMSRole> lmsRoles = new HashSet<LMSRole>();

	private LMSRole logInAsManagerRole = null;
	private  Boolean passWordChanged = Boolean.FALSE;
	private  Boolean isAdminMode = Boolean.FALSE;
	private  Boolean isProctorMode = Boolean.FALSE;
	private  Boolean isManagerMode = Boolean.FALSE;
	private  Boolean isLearnerMode = Boolean.FALSE;
	private  Boolean isInstructorMode = Boolean.FALSE;
	private  Boolean isAccreditationMode = Boolean.FALSE;

	private Boolean showGuidedTourScreenOnLogin = Boolean.TRUE;
	private Long roleID;
	private String roleName;
	private Integer accountNonLockedInt;
	private Language language;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getName() {
		StringBuffer sb = new StringBuffer();
		sb.append(firstName);
		sb.append(" ");
		sb.append(lastName);
		return sb.toString();
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

	public  Boolean getChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}

	public void setChangePasswordOnLogin(Boolean changePasswordOnLogin) {
		this.changePasswordOnLogin = changePasswordOnLogin;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Learner getLearner() {
		return learner;
	}

	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	public TrainingAdministrator getTrainingAdministrator() {
		return trainingAdministrator;
	}

	public void setTrainingAdministrator(TrainingAdministrator trainingAdministrator) {
		this.trainingAdministrator = trainingAdministrator;
	}

	public LMSAdministrator getLmsAdministrator() {
		return lmsAdministrator;
	}

	public void setLmsAdministrator(LMSAdministrator lmsAdministrator) {
		this.lmsAdministrator = lmsAdministrator;
	}

	public Proctor getProctor() {
		return proctor;
	}

	public void setProctor(Proctor proctor) {
		this.proctor = proctor;
	}

	/*public  Boolean isTrainingAdministrator() {
		if (this.getTrainingAdministrator() == null) {
			return false;
		} else {
			 Boolean isManager = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(
						LMSRole.ROLE_TRAININGMANAGER)
						&& atLeastOnePermssionEnable(role)) {
					isManager = true;
					break;
				}
			}
			return isManager;
		}

	}

	public  Boolean isLMSAdministrator() {
		if (this.getLmsAdministrator() == null) {
			return false;
		} else {
			 Boolean isAdmin = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(
						LMSRole.ROLE_LMSADMINISTRATOR)
						&& atLeastOnePermssionEnable(role)) {
					isAdmin = true;
					break;
				}
			}
			return isAdmin;
		}
	}

	public  Boolean isProctor() {
		if (this.getProctor() == null) {
			return false;
		} else {
			 Boolean isProctor = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_PROCTOR)
						&& atLeastOnePermssionEnable(role)
						&& !(this.getProctor().getStatus()
								.equals(ProctorStatusEnum.Expired.toString()))) {
					isProctor = true;
					break;
				}
			}
			return isProctor;
		}
	}

	public  Boolean isInLearnerRole() {
		if (this.getLearner() == null) {
			return false;
		} else {

			 Boolean isLearner = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)
						&& atLeastOnePermssionEnable(role)) {
					isLearner = true;
					break;
				}
			}
			return isLearner;

		}
	}

	public  Boolean atLeastOnePermssionEnable(LMSRole role) {
		 Boolean result = false;
		for (LMSRoleLMSFeature permission : role.getLmsPermissions()) {
			if (permission.getEnabled()) {
				result = true;
				break;
			}
		}
		return result;

	}*/

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public  boolean isAccountNonExpired() {
		if (accountNonExpired) {
			if (expirationDate != null && expirationDate.before(new Date())) {
				accountNonExpired = false;
			}
		}
		return accountNonExpired;
	}

	public  boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public  boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public  boolean isEnabled() {
		return enabled;
	}

	public String getUserGUID() {
		return userGUID;
	}

	public void setUserGUID(String userGUID) {
		this.userGUID = userGUID;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>(
				this.lmsRoles.size());
		for (LMSRole role : lmsRoles) {
			GrantedAuthority grant = new SimpleGrantedAuthority(
					role.getRoleType());
			authorityList.add(grant);
		}
		return authorityList;
	}

	public Set<LMSRole> getLmsRoles() {
		return lmsRoles;
	}

	public void addLmsRole(LMSRole lmsRole) {
		lmsRoles.add(lmsRole);
	}

	public void setLmsRoles(Set<LMSRole> lmsRoles) {
		this.lmsRoles = lmsRoles;
	}
	
	/*public void setLmsRoles(Set<LMSRole> lmsRoles) {
		this.lmsRoles = lmsRoles;

		this.isAdminMode = false;
		this.isProctorMode = false;
		this.isManagerMode = false;
		this.isLearnerMode = false;
		this.isInstructorMode = false;
		this.isAccreditationMode = false;
		
		if(lmsRoles==null || lmsRoles.size()==0){
			return;
		}

		for (LMSRole role : lmsRoles) {
			String roleType = role.getRoleType();
			if (LMSRole.ROLE_LMSADMINISTRATOR.equalsIgnoreCase(roleType))
				setAdminMode(true);
			else if (LMSRole.ROLE_TRAININGMANAGER.equalsIgnoreCase(roleType))
				setManagerMode(true);
			else if (LMSRole.ROLE_LEARNER.equalsIgnoreCase(roleType))
				setLearnerMode(true);
			else if (LMSRole.ROLE_INSTRUCTOR.equalsIgnoreCase(roleType))
				setInstructorMode(true);
			else if (LMSRole.ROLE_REGULATORYANALYST.equalsIgnoreCase(roleType))
				setAccreditationMode(true);
			else if (LMSRole.ROLE_PROCTOR.equalsIgnoreCase(roleType))
				setProctorMode(true);
		}
	}*/

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public  Boolean getVissibleOnReport() {
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

	public  Boolean isPassWordChanged() {
		return passWordChanged;
	}

	public void setPassWordChanged(Boolean passWordChanged) {
		this.passWordChanged = passWordChanged;
	}

	public RegulatoryAnalyst getRegulatoryAnalyst() {
		return this.regulatoryAnalyst;
	}

	public void setRegulatoryAnalyst(RegulatoryAnalyst regulatoryAnalyst) {
		this.regulatoryAnalyst = regulatoryAnalyst;// .setValue(regulatoryAnalyst);
	}

	/*public  Boolean isRegulatoryAnalyst() {
		if (this.regulatoryAnalyst == null) {
			return false;
		} else {
			 Boolean isRegulator = false;
			for (LMSRole role : lmsRoles) {
				if (role.getRoleType().equalsIgnoreCase(
						LMSRole.ROLE_REGULATORYANALYST)
						&& atLeastOnePermssionEnable(role)) {
					isRegulator = true;
					break;
				}
			}
			return isRegulator;
		}
	}*/

	public Instructor getInstructor() {
		return this.instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public ContentOwner getContentOwner() {
		ContentOwner contentOwner = null;
		try {
			contentOwner = this.getLearner().getCustomer().getContentOwner();
			if (contentOwner == null) {
				contentOwner = this.getLearner().getCustomer().getDistributor().getContentOwner();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contentOwner;
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

	public LMSRole getLogInAsManagerRole() {
		return logInAsManagerRole;
	}

	public void setLogInAsManagerRole(LMSRole logInAsManagerRole) {
		this.logInAsManagerRole = logInAsManagerRole;
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

	public Learner getLearnerProxy() {
		return learner;
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

	public Integer getAccountNonLockedInt() {
		return accountNonLockedInt;
	}

	public void setAccountNonLockedInt(Integer accountNonLockedInt) {
		this.accountNonLockedInt = accountNonLockedInt;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

}
