package com.softech.vu360.lms.web.controller.model.customfield;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CustomFieldValueChoice  implements ILMSBaseInterface{
	
	public CustomFieldValueChoice(com.softech.vu360.lms.model.CustomFieldValueChoice customFieldValueChoiceRef){
		super();
		this.customFieldValueChoiceRef=customFieldValueChoiceRef;
	}
	private static final long serialVersionUID = 1L;
	private com.softech.vu360.lms.model.CustomFieldValueChoice customFieldValueChoiceRef=null;
	private boolean selected = false;

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

	/**
	 * @return the customFieldValueChoiceRef
	 */
	public com.softech.vu360.lms.model.CustomFieldValueChoice getCustomFieldValueChoiceRef() {
		return customFieldValueChoiceRef;
	}

	/**
	 * @param customFieldValueChoiceRef the customFieldValueChoiceRef to set
	 */
	public void setCustomFieldValueChoiceRef(
			com.softech.vu360.lms.model.CustomFieldValueChoice customFieldValueChoiceRef) {
		this.customFieldValueChoiceRef = customFieldValueChoiceRef;
	}
	
}
