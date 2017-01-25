/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Tapas Mondal
 *
 */
public class DistributorGroupItemForm  implements ILMSBaseInterface, Comparable<DistributorGroupItemForm>{
	private static final long serialVersionUID = 1L;
	private boolean selected = false;
	private Distributor distributor=null;
	
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public int compareTo(DistributorGroupItemForm other) {
		return (this.distributor.getId()).compareTo(other.getDistributor().getId());
	}
	public Distributor getDistributor() {
		return distributor;
	}
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}
}
