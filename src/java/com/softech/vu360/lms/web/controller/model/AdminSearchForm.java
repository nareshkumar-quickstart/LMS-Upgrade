/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Somnath
 *
 */
public class AdminSearchForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	
	public static final String CUSTOMER_SEARCH_TYPE = "customerSearch";
	public static final String CUSTOMER_ADVANCE_SEARCH_TYPE = "advanceCustomerSearch";
	public static final String DISTRIBUTOR_SEARCH_TYPE = "distributorSearch";
	public static final String LEARNER_SEARCH_TYPE = "learnerSearch";
	private boolean advancedSearch = false;
	private boolean constrainedCustomerSearch = false;
	private boolean constrainedLearnerSearch = false;
	private String searchType = null;
	
	//for distributor search
	private String searchDistributorName;
	//for customer search
	private String searchCustomerName;
	private String searchOrderId;
	//for learner search
	private String searchFirstName;
	private String searchLastName;
	private String searchEmailAddress;
	
	//for customer advance search
	private String searchOperatorForCustName;
	private String searchOperatorForOrderId;
	private String searchOperatorForOrderDate;
	
	
	private String searchOrderDate;
	private String searchCusEmailAddress;
	
	List<AdminSearchMember> adminSearchMemberList = null;
	
	//for simple search
	private String simpleSearchKey;
	
	private int currentPageIndex;
	private int totalRecordCount;
	private int firstPageIndex;
	
	// Added By Marium Saud : For Column Sorting
	private String sortColumnIndex;
	private String sortDirection;
	/**
	 * @return the firstPageIndex
	 */
	public int getFirstPageIndex() {
		return firstPageIndex;
	}

	/**
	 * @param firstPageIndex the firstPageIndex to set
	 */
	public void setFirstPageIndex(int firstPageIndex) {
		this.firstPageIndex = firstPageIndex;
	}

	/**
	 * @return the lastPageIndex
	 */
	public int getLastPageIndex() {
		return lastPageIndex;
	}

	/**
	 * @param lastPageIndex the lastPageIndex to set
	 */
	public void setLastPageIndex(int lastPageIndex) {
		this.lastPageIndex = lastPageIndex;
	}

	private int lastPageIndex;
	
	/**
	 * @return the currentPageIndex
	 */
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	/**
	 * @param currentPageIndex the currentPageIndex to set
	 */
	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	/**
	 * @return the totalRecordCount
	 */
	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	/**
	 * @param totalRecordCount the totalRecordCount to set
	 */
	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}
	
	/**
	 * 
	 */
	public void resetAdminSearchForm() {
		advancedSearch = false;
		constrainedCustomerSearch = false;
		constrainedLearnerSearch = false;
		searchType = null;
		searchDistributorName = null;
		searchCustomerName = null;
		searchOrderId = null;
		searchFirstName = null;
		searchLastName = null;
		searchEmailAddress = null;
		searchOrderDate = null;
		searchCusEmailAddress = null;
		adminSearchMemberList = null;
		simpleSearchKey = null;
	}
	
	/**
	 * @return the advancedSearch
	 */
	public boolean isAdvancedSearch() {
		return advancedSearch;
	}

	/**
	 * @param advancedSearch the advancedSearch to set
	 */
	public void setAdvancedSearch(boolean advancedSearch) {
		this.advancedSearch = advancedSearch;
	}
	/**
	 * @return the constrainedCustomerSearch
	 */
	public boolean isConstrainedCustomerSearch() {
		return constrainedCustomerSearch;
	}
	/**
	 * @param constrainedCustomerSearch the constrainedCustomerSearch to set
	 */
	public void setConstrainedCustomerSearch(boolean constrainedCustomerSearch) {
		this.constrainedCustomerSearch = constrainedCustomerSearch;
	}
	/**
	 * @return the constrainedLeanerSearch
	 */
	public boolean isConstrainedLearnerSearch() {
		return constrainedLearnerSearch;
	}
	/**
	 * @param constrainedLearnerSearch the constrainedLearnerSearch to set
	 */
	public void setConstrainedLearnerSearch(boolean constrainedLearnerSearch) {
		this.constrainedLearnerSearch = constrainedLearnerSearch;
	}
	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}
	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	/**
	 * @return the searchCustomerName
	 */
	public String getSearchCustomerName() {
		return searchCustomerName;
	}
	/**
	 * @param searchCustomerName the searchCustomerName to set
	 */
	public void setSearchCustomerName(String searchCustomerName) {
		this.searchCustomerName = searchCustomerName;
	}
	/**
	 * @return the searchOrderId
	 */
	public String getSearchOrderId() {
		return searchOrderId;
	}
	/**
	 * @param searchOrderId the searchOrderId to set
	 */
	public void setSearchOrderId(String searchOrderId) {
		this.searchOrderId = searchOrderId;
	}
	/**
	 * @return the searchDistributorName
	 */
	public String getSearchDistributorName() {
		return searchDistributorName;
	}
	/**
	 * @param searchDistributorName the searchDistributorName to set
	 */
	public void setSearchDistributorName(String searchDistributorName) {
		this.searchDistributorName = searchDistributorName;
	}
	/**
	 * @return the searchEmailAddress
	 */
	public String getSearchEmailAddress() {
		return searchEmailAddress;
	}
	/**
	 * @param searchEmailAddress the searchEmailAddress to set
	 */
	public void setSearchEmailAddress(String searchEmailAddress) {
		this.searchEmailAddress = searchEmailAddress;
	}
	/**
	 * @return the searchFirstName
	 */
	public String getSearchFirstName() {
		return searchFirstName;
	}
	/**
	 * @param searchFirstName the searchFirstName to set
	 */
	public void setSearchFirstName(String searchFirstName) {
		this.searchFirstName = searchFirstName;
	}
	/**
	 * @return the searchLastName
	 */
	public String getSearchLastName() {
		return searchLastName;
	}
	/**
	 * @param searchLastName the searchLastName to set
	 */
	public void setSearchLastName(String searchLastName) {
		this.searchLastName = searchLastName;
	}
	/**
	 * @return the simpleSearchKey
	 */
	public String getSimpleSearchKey() {
		return simpleSearchKey;
	}
	/**
	 * @param simpleSearchKey the simpleSearchKey to set
	 */
	public void setSimpleSearchKey(String simpleSearchKey) {
		this.simpleSearchKey = simpleSearchKey;
	}
	/**
	 * @return the adminSearchMemberList
	 */
	public List<AdminSearchMember> getAdminSearchMemberList() {
		return adminSearchMemberList;
	}
	/**
	 * @param adminSearchMemberList the adminSearchMemberList to set
	 */
	public void setAdminSearchMemberList(
			List<AdminSearchMember> adminSearchMemberList) {
		this.adminSearchMemberList = adminSearchMemberList;
	}

	public String getSearchOperatorForCustName() {
		return searchOperatorForCustName;
	}

	public void setSearchOperatorForCustName(String searchOperatorForCustName) {
		this.searchOperatorForCustName = searchOperatorForCustName;
	}

	public String getSearchOperatorForOrderId() {
		return searchOperatorForOrderId;
	}

	public void setSearchOperatorForOrderId(String searchOperatorForOrderId) {
		this.searchOperatorForOrderId = searchOperatorForOrderId;
	}

	public String getSearchOperatorForOrderDate() {
		return searchOperatorForOrderDate;
	}

	public void setSearchOperatorForOrderDate(String searchOperatorForOrderDate) {
		this.searchOperatorForOrderDate = searchOperatorForOrderDate;
	}

	public String getSearchOrderDate() {
		return searchOrderDate;
	}

	public void setSearchOrderDate(String searchOrderDate) {
		this.searchOrderDate = searchOrderDate;
	}

	public String getSearchCusEmailAddress() {
		return searchCusEmailAddress;
	}

	public void setSearchCusEmailAddress(String searchCusEmailAddress) {
		this.searchCusEmailAddress = searchCusEmailAddress;
	}

	/**
	 * @return the sortColumnIndex
	 */
	public String getSortColumnIndex() {
		return sortColumnIndex;
	}

	/**
	 * @param sortColumnIndex the sortColumnIndex to set
	 */
	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}

	/**
	 * @return the sortDirection
	 */
	public String getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	
}
