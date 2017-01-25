package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ApprovalRegulator implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Regulator regulator;
	private boolean selected = false;
	private String contactPersonName;
	
	public Regulator getRegulator() {
		return regulator;
	}
	public void setRegulator(Regulator regulator) {
		this.regulator = regulator;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getContactPersonName() {
		return contactPersonName;
	}
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
}