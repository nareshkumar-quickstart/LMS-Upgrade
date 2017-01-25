package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class EditDistributor  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private Distributor distributor;
	
	public EditDistributor() {
		super();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}
	
}