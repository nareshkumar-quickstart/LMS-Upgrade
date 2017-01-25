package com.softech.vu360.lms.web.controller.model.accreditation;


import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ApprovalRegulatorCategory implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private RegulatorCategory category;
	private Regulator regulator;
	private boolean selected = false;
	
	
	public RegulatorCategory getCategory() {
		return category;
	}
	public void setCategory(RegulatorCategory category) {
		this.category = category;
	}
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
	
}