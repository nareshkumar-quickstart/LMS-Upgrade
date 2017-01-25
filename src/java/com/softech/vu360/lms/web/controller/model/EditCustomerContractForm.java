package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.TreeNode;

public class EditCustomerContractForm  implements ILMSBaseInterface
{
	public EditCustomerContractForm()
	{	}
	private static final long serialVersionUID = 1L;
	private String contractName=null;
	private boolean maximumEnrollmentsUnLimited=true;
	private String maximumEnrollmentsLimitedValue=null;
	private boolean allowSelfEnrollment=true;
	private boolean enrollmentType=true;
	private String startDate=null;
	private boolean termsOfServices=true;
	private String termsOfServicesValue=null;
	private String fixedEndDate=null;
	private long entId = 0;
	public String[] getGroups() {
		return groups;
	}


	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	private String[] groups;
	
	private CustomerEntitlement customerEntitlement = null;
	
	public CustomerEntitlement getCustomerEntitlement() {
		return customerEntitlement;
	}


	public void setCustomerEntitlement(CustomerEntitlement customerEntitlement) {
		this.customerEntitlement = customerEntitlement;
	}


	public long getEntId() {
		return entId;
	}
	

	public void setEntId(long entId) {
		this.entId = entId;
	}

	public String getSeatUsed() {
		return seatUsed;
	}

	public void setSeatUsed(String seatUsed) {
		this.seatUsed = seatUsed;
	}

	public String getSeatRemaining() {
		return seatRemaining;
	}

	public void setSeatRemaining(String seatRemaining) {
		this.seatRemaining = seatRemaining;
	}

	private String seatUsed = "0";
	private String seatRemaining = "0";
	
	// For page 2
	private List<TreeNode> treeAsList = null;
	private List<CustomerEntitlementOrgGroup> selectedOrgGroups = new ArrayList<CustomerEntitlementOrgGroup>();
	//private List organisationalGroupEntitlementItems = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(OrganisationalGroupEntitlementItem.class));
	private List <OrganisationalGroupEntitlementItem>organisationalGroupEntitlementItems =new ArrayList<OrganisationalGroupEntitlementItem>();
	
	
	// For page 3 Course
	
	private List organizationGroups =null;
	
	
	public List<TreeNode> getTreeAsList() {
		return treeAsList;
	}

	public void setTreeAsList(List<TreeNode> treeAsList) {
		this.treeAsList = treeAsList;
	}

	public List<CustomerEntitlementOrgGroup> getSelectedOrgGroups() {
		return selectedOrgGroups;
	}

	public void setSelectedOrgGroups(
			List<CustomerEntitlementOrgGroup> selectedOrgGroups) {
		this.selectedOrgGroups = selectedOrgGroups;
	}

	public List<OrganisationalGroupEntitlementItem> getOrganisationalGroupEntitlementItems() {
		return organisationalGroupEntitlementItems;
	}

	public void setOrganisationalGroupEntitlementItems(
			List<OrganisationalGroupEntitlementItem> organisationalGroupEntitlementItems) {
		this.organisationalGroupEntitlementItems = organisationalGroupEntitlementItems;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public boolean isMaximumEnrollmentsUnLimited() {
		return maximumEnrollmentsUnLimited;
	}

	public void setMaximumEnrollmentsUnLimited(boolean maximumEnrollmentsUnLimited) {
		this.maximumEnrollmentsUnLimited = maximumEnrollmentsUnLimited;
	}

	public String getMaximumEnrollmentsLimitedValue() {
		return maximumEnrollmentsLimitedValue;
	}

	public void setMaximumEnrollmentsLimitedValue(
			String maximumEnrollmentsLimitedValue) {
		this.maximumEnrollmentsLimitedValue = maximumEnrollmentsLimitedValue;
	}

	public boolean isAllowSelfEnrollment() {
		return allowSelfEnrollment;
	}

	public void setAllowSelfEnrollment(boolean allowSelfEnrollment) {
		this.allowSelfEnrollment = allowSelfEnrollment;
	}

	public boolean isEnrollmentType() {
		return enrollmentType;
	}

	public void setEnrollmentType(boolean enrollmentType) {
		this.enrollmentType = enrollmentType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public boolean isTermsOfServices() {
		return termsOfServices;
	}

	public void setTermsOfServices(boolean termsOfServices) {
		this.termsOfServices = termsOfServices;
	}
	
	public String getTermsOfServicesValue() {
		return termsOfServicesValue;
	}

	public void setTermsOfServicesValue(String termsOfServicesValue) {
		this.termsOfServicesValue = termsOfServicesValue;
	}

	public String getFixedEndDate() {
		return fixedEndDate;
	}

	public void setFixedEndDate(String fixedEndDate) {
		this.fixedEndDate = fixedEndDate;
	}

	public List getOrganizationGroups() {
		return organizationGroups;
	}

	public void setOrganizationGroups(List organizationGroups) {
		this.organizationGroups = organizationGroups;
	}

}
