package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class OrganisationalGroupEntitlementsForm implements ILMSBaseInterface {
	private String customerEntitlementId; 
	private List organisationalGroupEntitlementItems = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(OrganisationalGroupEntitlementItem.class));
	private static final long serialVersionUID = 1L;
	public List getOrganisationalGroupEntitlementItems() {
		return organisationalGroupEntitlementItems;
	}

	public void setOrganisationalGroupEntitlementItems(
			List organisationalGroupEntitlementItems) {
		this.organisationalGroupEntitlementItems = organisationalGroupEntitlementItems;
	}

	public String getCustomerEntitlementId() {
		return customerEntitlementId;
	}

	public void setCustomerEntitlementId(String customerEntitlementId) {
		this.customerEntitlementId = customerEntitlementId;
	}

}
