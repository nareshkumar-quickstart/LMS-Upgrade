package com.softech.vu360.lms.vo;

import java.io.Serializable;

import com.softech.vu360.lms.vo.LMSFeature;

public class LMSRoleLMSFeature implements Serializable{

	private static final long serialVersionUID = -8080506378791298342L;
	
	private Boolean isLocked = Boolean.FALSE;
	private Long id;
	private Boolean enabled = Boolean.TRUE;
	private LMSFeature lmsFeature = null;
	private LMSRole lmsRole;

	public LMSRole getLmsRole() {
		return lmsRole;
	}

	public void setLmsRole(LMSRole lmsRole) {
		this.lmsRole = lmsRole;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public LMSFeature getLmsFeature() {
		return lmsFeature;
	}

	public void setLmsFeature(LMSFeature lmsFeature) {
		this.lmsFeature = lmsFeature;
	}

	public Boolean isLocked() {
		return isLocked;
	}

	public void setLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public LMSRole getLmsRoleProxy() {
		return lmsRole;
	}

}
