package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.TreeNode;

public class AddCustomerEntitlementsForm  implements ILMSBaseInterface{

	private static final long serialVersionUID = 1L;
	public AddCustomerEntitlementsForm() {
	}
	
	// For page 1
	private String entitlementName=null;
	private boolean maxEnrollments=true;
	private String noOfMaxEnrollments=null;
	private boolean allowSelfEnrollments=true;
	private boolean entitlementType=false;
	private String startDate=null;
	private boolean termsOfService=false;
	private String days=null;
	private String fiexedEndDate=null;
	
	// For page 2
	private List<TreeNode> treeAsList = null;
	private List<CustomerEntitlementOrgGroup> selectedOrgGroups = new ArrayList<CustomerEntitlementOrgGroup>();
	//private List organisationalGroupEntitlementItems = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(OrganisationalGroupEntitlementItem.class));
	private List <OrganisationalGroupEntitlementItem>organisationalGroupEntitlementItems =new ArrayList<OrganisationalGroupEntitlementItem>();
	// For page 3 Course
	private String[] courseGroups;
	private String action = "search";
	private String courseSearchType = "";
	private String searchCourseName=null;
	private String searchCourseID=null;
	private String searchCourseKeyword=null;
	
	private String courseSimpleSearchKey=null;
	
	private List<SurveyCourse> selectedCourses = new ArrayList<SurveyCourse>();
	
	//sorting and paging items
	private int sortColumnIndex = 0;
	private int sortDirection = 1;
	private int pageIndex = 0;
	
	// For page 3 CourseGroup
	//private String action = "search";
	private String[] groups;
	private String courseGroupSearchType = "";
	private String searchCourseGroupName=null;
	private String searchCourseGroupID=null;
	private String searchCourseGroupKeyword=null;
	
	private String courseGroupSimpleSearchKey=null;
	
	private List<CustomerEntitlementsCourseGroup> selectedCourseGroups = new ArrayList<CustomerEntitlementsCourseGroup>();
	
	// For page 4

	//For update customer entitlement  
	private long entId = 0;
	private String seatUsed = "0";
	private String seatRemaining = "0";
	private CustomerEntitlement customerEntitlement = null;
	
	
	
	/**
	 * @return the entitlementName
	 */
	public String getEntitlementName() {
		return entitlementName;
	}

	/**
	 * @param entitlementName the entitlementName to set
	 */
	public void setEntitlementName(String entitlementName) {
		this.entitlementName = entitlementName;
	}

	/**
	 * @return the maxEnrollments
	 */
	public boolean isMaxEnrollments() {
		return maxEnrollments;
	}

	/**
	 * @param maxEnrollments the maxEnrollments to set
	 */
	public void setMaxEnrollments(boolean maxEnrollments) {
		this.maxEnrollments = maxEnrollments;
	}

	/**
	 * @return the noOfMaxEnrollments
	 */
	public String getNoOfMaxEnrollments() {
		return noOfMaxEnrollments;
	}

	/**
	 * @param noOfMaxEnrollments the noOfMaxEnrollments to set
	 */
	public void setNoOfMaxEnrollments(String noOfMaxEnrollments) {
		this.noOfMaxEnrollments = noOfMaxEnrollments;
	}

	/**
	 * @return the allowSelfEnrollments
	 */
	public boolean isAllowSelfEnrollments() {
		return allowSelfEnrollments;
	}

	/**
	 * @param allowSelfEnrollments the allowSelfEnrollments to set
	 */
	public void setAllowSelfEnrollments(boolean allowSelfEnrollments) {
		this.allowSelfEnrollments = allowSelfEnrollments;
	}

	/**
	 * @return the entitlementType
	 */
	public boolean isEntitlementType() {
		return entitlementType;
	}

	/**
	 * @param entitlementType the entitlementType to set
	 */
	public void setEntitlementType(boolean entitlementType) {
		this.entitlementType = entitlementType;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the termsOfService
	 */
	public boolean isTermsOfService() {
		return termsOfService;
	}

	/**
	 * @param termsOfService the termsOfService to set
	 */
	public void setTermsOfService(boolean termsOfService) {
		this.termsOfService = termsOfService;
	}

	/**
	 * @return the days
	 */
	public String getDays() {
		return days;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(String days) {
		this.days = days;
	}

	/**
	 * @return the fiexedEndDate
	 */
	public String getFiexedEndDate() {
		return fiexedEndDate;
	}

	/**
	 * @param fiexedEndDate the fiexedEndDate to set
	 */
	public void setFiexedEndDate(String fiexedEndDate) {
		this.fiexedEndDate = fiexedEndDate;
	}

	/**
	 * @return the selectedOrgGroups
	 */
	public List<CustomerEntitlementOrgGroup> getSelectedOrgGroups() {
		return selectedOrgGroups;
	}

	/**
	 * @param selectedOrgGroups the selectedOrgGroups to set
	 */
	public void setSelectedOrgGroups(
			List<CustomerEntitlementOrgGroup> selectedOrgGroups) {
		this.selectedOrgGroups = selectedOrgGroups;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the courseSearchType
	 */
	public String getCourseSearchType() {
		return courseSearchType;
	}

	/**
	 * @param courseSearchType the courseSearchType to set
	 */
	public void setCourseSearchType(String courseSearchType) {
		this.courseSearchType = courseSearchType;
	}

	/**
	 * @return the searchCourseName
	 */
	public String getSearchCourseName() {
		return searchCourseName;
	}

	/**
	 * @param searchCourseName the searchCourseName to set
	 */
	public void setSearchCourseName(String searchCourseName) {
		this.searchCourseName = searchCourseName;
	}

	/**
	 * @return the searchCourseID
	 */
	public String getSearchCourseID() {
		return searchCourseID;
	}

	/**
	 * @param searchCourseID the searchCourseID to set
	 */
	public void setSearchCourseID(String searchCourseID) {
		this.searchCourseID = searchCourseID;
	}

	/**
	 * @return the searchCourseKeyword
	 */
	public String getSearchCourseKeyword() {
		return searchCourseKeyword;
	}

	/**
	 * @param searchCourseKeyword the searchCourseKeyword to set
	 */
	public void setSearchCourseKeyword(String searchCourseKeyword) {
		this.searchCourseKeyword = searchCourseKeyword;
	}

	/**
	 * @return the courseSimpleSearchKey
	 */
	public String getCourseSimpleSearchKey() {
		return courseSimpleSearchKey;
	}

	/**
	 * @param courseSimpleSearchKey the courseSimpleSearchKey to set
	 */
	public void setCourseSimpleSearchKey(String courseSimpleSearchKey) {
		this.courseSimpleSearchKey = courseSimpleSearchKey;
	}

	/**
	 * @return the selectedCourses
	 */
	public List<SurveyCourse> getSelectedCourses() {
		return selectedCourses;
	}

	/**
	 * @param selectedCourses the selectedCourses to set
	 */
	public void setSelectedCourses(List<SurveyCourse> selectedCourses) {
		this.selectedCourses = selectedCourses;
	}

	/**
	 * @return the courseGroupSearchType
	 */
	public String getCourseGroupSearchType() {
		return courseGroupSearchType;
	}

	/**
	 * @param courseGroupSearchType the courseGroupSearchType to set
	 */
	public void setCourseGroupSearchType(String courseGroupSearchType) {
		this.courseGroupSearchType = courseGroupSearchType;
	}

	/**
	 * @return the searchCourseGroupName
	 */
	public String getSearchCourseGroupName() {
		return searchCourseGroupName;
	}

	/**
	 * @param searchCourseGroupName the searchCourseGroupName to set
	 */
	public void setSearchCourseGroupName(String searchCourseGroupName) {
		this.searchCourseGroupName = searchCourseGroupName;
	}

	/**
	 * @return the searchCourseGroupID
	 */
	public String getSearchCourseGroupID() {
		return searchCourseGroupID;
	}

	/**
	 * @param searchCourseGroupID the searchCourseGroupID to set
	 */
	public void setSearchCourseGroupID(String searchCourseGroupID) {
		this.searchCourseGroupID = searchCourseGroupID;
	}

	/**
	 * @return the searchCourseGroupKeyword
	 */
	public String getSearchCourseGroupKeyword() {
		return searchCourseGroupKeyword;
	}

	/**
	 * @param searchCourseGroupKeyword the searchCourseGroupKeyword to set
	 */
	public void setSearchCourseGroupKeyword(String searchCourseGroupKeyword) {
		this.searchCourseGroupKeyword = searchCourseGroupKeyword;
	}

	/**
	 * @return the courseGroupSimpleSearchKey
	 */
	public String getCourseGroupSimpleSearchKey() {
		return courseGroupSimpleSearchKey;
	}

	/**
	 * @param courseGroupSimpleSearchKey the courseGroupSimpleSearchKey to set
	 */
	public void setCourseGroupSimpleSearchKey(String courseGroupSimpleSearchKey) {
		this.courseGroupSimpleSearchKey = courseGroupSimpleSearchKey;
	}

	/**
	 * @return the selectedCourseGroups
	 */
	public List<CustomerEntitlementsCourseGroup> getSelectedCourseGroups() {
		return selectedCourseGroups;
	}

	/**
	 * @param selectedCourseGroups the selectedCourseGroups to set
	 */
	public void setSelectedCourseGroups(
			List<CustomerEntitlementsCourseGroup> selectedCourseGroups) {
		this.selectedCourseGroups = selectedCourseGroups;
	}

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the treeAsList
	 */
	public List<TreeNode> getTreeAsList() {
		return treeAsList;
	}

	/**
	 * @param treeAsList the treeAsList to set
	 */
	public void setTreeAsList(List<TreeNode> treeAsList) {
		this.treeAsList = treeAsList;
	}

	/**
	 * @return the organisationalGroupEntitlementItems
	 */
	/*public List getOrganisationalGroupEntitlementItems() {
		return organisationalGroupEntitlementItems;
	}*/

	/**
	 * @param organisationalGroupEntitlementItems the organisationalGroupEntitlementItems to set
	 */
	/*public void setOrganisationalGroupEntitlementItems(
			List organisationalGroupEntitlementItems) {
		this.organisationalGroupEntitlementItems = organisationalGroupEntitlementItems;
	}*/

	/**
	 * @return the groups
	 */
	public String[] getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	/**
	 * @return the courseGroups
	 */
	public String[] getCourseGroups() {
		return courseGroups;
	}

	/**
	 * @param courseGroups the courseGroups to set
	 */
	public void setCourseGroups(String[] courseGroups) {
		this.courseGroups = courseGroups;
	}

	/**
	 * @return the sortColumnIndex
	 */
	public int getSortColumnIndex() {
		return sortColumnIndex;
	}

	/**
	 * @param sortColumnIndex the sortColumnIndex to set
	 */
	public void setSortColumnIndex(int sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}

	/**
	 * @return the sortDirection
	 */
	public int getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	/**
	 * @return the entId
	 */
	public long getEntId() {
		return entId;
	}

	/**
	 * @param entId the entId to set
	 */
	public void setEntId(long entId) {
		this.entId = entId;
	}

	/**
	 * @return the seatUsed
	 */
	public String getSeatUsed() {
		return seatUsed;
	}

	/**
	 * @param seatUsed the seatUsed to set
	 */
	public void setSeatUsed(String seatUsed) {
		this.seatUsed = seatUsed;
	}

	/**
	 * @return the seatRemaining
	 */
	public String getSeatRemaining() {
		return seatRemaining;
	}

	/**
	 * @param seatRemaining the seatRemaining to set
	 */
	public void setSeatRemaining(String seatRemaining) {
		this.seatRemaining = seatRemaining;
	}

	/**
	 * @return the customerEntitlement
	 */
	public CustomerEntitlement getCustomerEntitlement() {
		return customerEntitlement;
	}

	/**
	 * @param customerEntitlement the customerEntitlement to set
	 */
	public void setCustomerEntitlement(CustomerEntitlement customerEntitlement) {
		this.customerEntitlement = customerEntitlement;
	}

	/**
	 * @return the organisationalGroupEntitlementItems
	 */
	public List<OrganisationalGroupEntitlementItem> getOrganisationalGroupEntitlementItems() {
		return organisationalGroupEntitlementItems;
	}

	/**
	 * @param organisationalGroupEntitlementItems the organisationalGroupEntitlementItems to set
	 */
	public void setOrganisationalGroupEntitlementItems(
			List<OrganisationalGroupEntitlementItem> organisationalGroupEntitlementItems) {
		this.organisationalGroupEntitlementItems = organisationalGroupEntitlementItems;
	}
	
	
}
