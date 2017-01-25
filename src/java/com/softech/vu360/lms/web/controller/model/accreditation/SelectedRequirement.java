package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SelectedRequirement  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private CredentialCategoryRequirement requirement;
	private boolean selected = false;
	
	/**
	 * @return the requirement
	 */
	public CredentialCategoryRequirement getRequirement() {
		return requirement;
	}
	
	/**
	 * @param requirement the requirement to set
	 */
	public void setRequirement(CredentialCategoryRequirement requirement) {
		this.requirement = requirement;
	}
	
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
