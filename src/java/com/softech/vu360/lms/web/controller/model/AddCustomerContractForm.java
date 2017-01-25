package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.TreeNode;

public class AddCustomerContractForm implements ILMSBaseInterface
{
	private static final long serialVersionUID = 1L;
	public AddCustomerContractForm()
	{	}
	public static final String COURSEGROUPTYPE = "CourseGroup";
	public static final String COURSETYPE = "Course";
	private String contractName=null;
	private boolean maximumEnrollmentsUnLimited=true;
	private String maximumEnrollmentsLimitedValue=null;
	private boolean allowSelfEnrollment=true;
	private String enrollmentType = COURSEGROUPTYPE;
	private String startDate=null;
	private boolean termsOfServices=true;
	private String termsOfServicesValue=null;
	private String fixedEndDate=null;
	private String remainingSeats="0";
	
	private long entId = 0;
	private CustomerEntitlement customerEntitlement = null;
	private List<CourseCustomerEntitlementItem> entitlementItems = new ArrayList<CourseCustomerEntitlementItem>();
	private List<CourseGroupCustomerEntitlementItem> courseGroupItems = new ArrayList<CourseGroupCustomerEntitlementItem>();
	private String courseIds="";

	private String orgGroupEntitlementArray;
	private String duplicateContractName="";
	
	private String transactionAmount = null;

	public String getOrgGroupEntitlementArray() {
		return orgGroupEntitlementArray;
	}

	public void setOrgGroupEntitlementArray(String orgGroupEntitlementArray) {
		this.orgGroupEntitlementArray = orgGroupEntitlementArray;
	}

	
	public String[] getGroups() {
		return groups;
	}


	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	private String[] groups;
	
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

	public String getEnrollmentType() {
		return enrollmentType;
	}

	public void setEnrollmentType(String enrollmentType) {
		this.enrollmentType = enrollmentType;
	}	
	
	public List<CourseGroup> getCourseGroups(){
		List<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
		for(CourseGroupCustomerEntitlementItem item: courseGroupItems){
		courseGroups.add(item.getCourseGroup());	
		}
		return courseGroups;
	}
	
	public List<CourseCustomerEntitlementItem> getEntitlementItems(){
		return entitlementItems;
	}

	public void setEntitlementItems(
			List<CourseCustomerEntitlementItem> entitlementItems) {
		this.entitlementItems = entitlementItems;
	}


	/**
	 * @return the courseGroupItems
	 */
	public List<CourseGroupCustomerEntitlementItem> getCourseGroupItems() {
		return courseGroupItems;
	}


	/**
	 * @param courseGroupItems the courseGroupItems to set
	 */
	public void setCourseGroupItems(
			List<CourseGroupCustomerEntitlementItem> courseGroupItems) {
		this.courseGroupItems = courseGroupItems;
	}

	public String getDuplicateContractName() {
		return duplicateContractName;
	}

	public void setDuplicateContractName(String duplicateContractName) {
		this.duplicateContractName = duplicateContractName;
	}

	public String getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(String courseIds) {
		this.courseIds = courseIds;
	}

	/**
	 * @return the transactionAmount
	 */
	public String getTransactionAmount() {
		return transactionAmount;
	}

	/**
	 * @param transactionAmount the transactionAmount to set
	 */
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getRemainingSeats() {
		return remainingSeats;
	}

	public void setRemainingSeats(String remainingSeats) {
		this.remainingSeats = remainingSeats;
	}
	
	
	
}
