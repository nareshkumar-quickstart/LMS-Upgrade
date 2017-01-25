package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.model.Resource;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ManageResource implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private Resource resource = null;
	private boolean selected = false;
	
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}