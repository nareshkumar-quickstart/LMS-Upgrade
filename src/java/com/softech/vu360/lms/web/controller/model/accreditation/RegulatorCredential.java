package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class RegulatorCredential  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private Credential credential = null;
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public Credential getCredential() {
		return credential;
	}
	public void setCredential(Credential credential) {
		this.credential = credential;
	}
}