package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.model.Contact;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class RegulatorContact  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Contact contact = null;
	private boolean selected = false;
	
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}