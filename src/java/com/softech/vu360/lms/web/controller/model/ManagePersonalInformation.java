package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.AvailablePersonalInformationfieldItem;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 * 04 August, 2010
 *
 */
public class ManagePersonalInformation  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private boolean required = false;
	private AvailablePersonalInformationfieldItem personalInfoItem = null;
	
	
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
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	/**
	 * @return the personalInfoItem
	 */
	public AvailablePersonalInformationfieldItem getPersonalInfoItem() {
		return personalInfoItem;
	}
	/**
	 * @param personalInfoItem the personalInfoItem to set
	 */
	public void setPersonalInfoItem(
			AvailablePersonalInformationfieldItem personalInfoItem) {
		this.personalInfoItem = personalInfoItem;
	}
	
}
