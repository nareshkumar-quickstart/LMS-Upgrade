package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ViewLearnerEntitlementItem  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private CustomerEntitlement entitlement = null;
	private boolean selected =false;
	List<ViewLearnerEnrollmentItem> viewLearnerEnrollmentItems= new ArrayList<ViewLearnerEnrollmentItem>();
	
	public void addViewLearnerEnrollmentItem(ViewLearnerEnrollmentItem viewLearnerEnrollmentItem) {
		viewLearnerEnrollmentItems.add(viewLearnerEnrollmentItem);
	}
	
	public CustomerEntitlement getEntitlement() {
		return entitlement;
	}
	public void setEntitlement(CustomerEntitlement entitlement) {
		this.entitlement = entitlement;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public List<ViewLearnerEnrollmentItem> getViewLearnerEnrollmentItems() {
		return viewLearnerEnrollmentItems;
	}
	public void setViewLearnerEnrollmentItems(
			List<ViewLearnerEnrollmentItem> viewLearnerEnrollmentItems) {
		this.viewLearnerEnrollmentItems = viewLearnerEnrollmentItems;
	}
}
