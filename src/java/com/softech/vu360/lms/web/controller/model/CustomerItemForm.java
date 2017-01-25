package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CustomerItemForm  implements ILMSBaseInterface, Comparable<CustomerItemForm>{
	private static final long serialVersionUID = 1L;
	private boolean isSelected = false;
	private Customer customer = null;
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer user) {
		this.customer = user;
	}

	public int compareTo(CustomerItemForm other) {
		return (this.customer.getId()).compareTo(other.getCustomer().getId());
	}
	
	
	
}