package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class LMSFeature implements Serializable {
	private static final long serialVersionUID = -3362881694023390814L;
	
	private Long id;
	private String featureCode = null;
	private String featureDescription = null;
	private String featureName = null;
	private String featureGroup = null;
	private String lmsMode = null;
	private String roleType = LMSRole.ROLE_LEARNER;
	private Integer displayOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public String getFeatureDescription() {
		return featureDescription;
	}

	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getLmsMode() {
		return lmsMode;
	}

	public void setLmsMode(String lmsMode) {
		this.lmsMode = lmsMode;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getFeatureGroup() {
		return featureGroup;
	}

	public void setFeatureGroup(String featureGroup) {
		this.featureGroup = featureGroup;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
}