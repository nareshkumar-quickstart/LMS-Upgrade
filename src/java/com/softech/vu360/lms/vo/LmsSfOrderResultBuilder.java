package com.softech.vu360.lms.vo;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LmsSfOrderAuditLog;
import com.softech.vu360.lms.model.VU360User;

public class LmsSfOrderResultBuilder {

	private Customer customer;
	private VU360User vu360User;
	private List<LmsSfOrderAuditLog> orderAuditList = new ArrayList<LmsSfOrderAuditLog>();
	private boolean hasOneValidCourseGuid;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean hasOneValidCourseGuid() {
		return hasOneValidCourseGuid;
	}

	public void setHasOneValidCourseGuid(boolean hasOneValidCourseGuid) {
		this.hasOneValidCourseGuid = hasOneValidCourseGuid;
	}

	public List<LmsSfOrderAuditLog> getOrderAuditList() {
		return orderAuditList;
	}

	public void setOrderAuditList(List<LmsSfOrderAuditLog> orderAuditList) {
		this.orderAuditList = orderAuditList;
	}

	public VU360User getVU360User() {
		return vu360User;
	}

	public void setVU360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}	
	

}
