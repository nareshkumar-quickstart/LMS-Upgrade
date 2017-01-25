package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 * 
 */
public class AddDistributors implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private Distributor distributor;

	public AddDistributors() {
		super();
		// TODO Auto-generated constructor stub
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
