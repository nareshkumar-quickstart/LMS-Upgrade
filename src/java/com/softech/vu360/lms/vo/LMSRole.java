package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LMSRole implements Serializable {
	private static final long serialVersionUID = 7950596337438042478L;
	
	private Long id;
	private String roleName = null;
	private String roleType = null;
	private Boolean isDefaultForRegistration = Boolean.FALSE;
	private Boolean isSystemCreated = Boolean.FALSE;

	public static final String ROLE_LEARNER = "ROLE_LEARNER";
	public static final String ROLE_TRAININGMANAGER = "ROLE_TRAININGADMINISTRATOR";
	public static final String ROLE_LMSADMINISTRATOR = "ROLE_LMSADMINISTRATOR";
	public static final String ROLE_REGULATORYANALYST = "ROLE_REGULATORYANALYST";
	public static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
	public static final String ROLE_PROCTOR = "ROLE_PROCTOR";
	private List<LMSRoleLMSFeature> lmsPermissions = new ArrayList<LMSRoleLMSFeature>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public Boolean isDefaultForRegistration() {
		return isDefaultForRegistration;
	}

	public void setDefaultForRegistration(Boolean isDefaultForRegistration) {
		this.isDefaultForRegistration = isDefaultForRegistration;
	}

	public Boolean isSystemCreated() {
		return isSystemCreated;
	}

	public void setSystemCreated(Boolean isSystemCreated) {
		this.isSystemCreated = isSystemCreated;
	}


	public List<LMSRoleLMSFeature> getLmsPermissions() {
		return lmsPermissions;
	}

	public void setLmsPermissions(List<LMSRoleLMSFeature> lmsPermissions) {
		this.lmsPermissions = lmsPermissions;
	}
}