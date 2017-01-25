package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ApprovalCredential implements ILMSBaseInterface{

	private Credential credential;
	private boolean selected = false;
	List<ApprovalRequirement> requirements = new ArrayList <ApprovalRequirement>();
	private static final long serialVersionUID = 1L;
	public Credential getCredential() {
		return credential;
	}
	public void setCredential(Credential credential) {
		this.credential = credential;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public List<ApprovalRequirement> getRequirements() {
		return requirements;
	}
	public void setRequirements(List<ApprovalRequirement> requirements) {
		this.requirements = requirements;
	}
}