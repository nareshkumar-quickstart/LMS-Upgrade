package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddDistributorGroups  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private DistributorGroup distributorGroup;
	
	
	public AddDistributorGroups() {
		super();
		// TODO Auto-generated constructor stub
	}


	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public DistributorGroup getDistributorGroup() {
		return distributorGroup;
	}


	public void setDistributorGroup(DistributorGroup distributorGroup) {
		this.distributorGroup = distributorGroup;
	}
	
	
}
