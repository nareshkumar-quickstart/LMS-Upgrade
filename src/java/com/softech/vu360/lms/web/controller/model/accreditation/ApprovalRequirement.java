package com.softech.vu360.lms.web.controller.model.accreditation;

import java.io.Serializable;

import com.softech.vu360.lms.model.CredentialCategoryRequirement;

public class ApprovalRequirement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CredentialCategoryRequirement requirement;
	private boolean selected = false;
	
	public CredentialCategoryRequirement getRequirement() {
		return requirement;
	}
	public void setRequirement(CredentialCategoryRequirement requirement) {
		this.requirement = requirement;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}