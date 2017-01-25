package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author muzammil.shaikh
 */
public class SearchUsersForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String customerId = "";
	private String distributorId = "";
	private String redirectingFrom = "";
	
	private String searchFirstName = "";
	private String searchLastName = "";
	private String searchEmailAddress = "";	
	private int sortColumnIndex = 0;
	private int sortDirection = 1;
	private boolean showAll = false;
	private int pageIndex = 0;
	private String[] userId;
	private String searchType = "";
	private String performAction = "";
	private List<UserItemForm> users = new ArrayList<UserItemForm>();
	private List<UserItemForm> selectedUsers = new ArrayList<UserItemForm>();
	private String typeOfCustomerUsersToSearch = "";
	
	
	public SearchUsersForm() {
	}

	
	
	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getDistributorId() {
		return distributorId;
	}



	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}



	public String getRedirectingFrom() {
		return redirectingFrom;
	}



	public void setRedirectingFrom(String redirectingFrom) {
		this.redirectingFrom = redirectingFrom;
	}



	public String getSearchFirstName() {
		return searchFirstName;
	}


	public void setSearchFirstName(String searchFirstName) {
		this.searchFirstName = searchFirstName;
	}


	public String getSearchLastName() {
		return searchLastName;
	}


	public void setSearchLastName(String searchLastName) {
		this.searchLastName = searchLastName;
	}


	public String getSearchEmailAddress() {
		return searchEmailAddress;
	}


	public void setSearchEmailAddress(String searchEmailAddress) {
		this.searchEmailAddress = searchEmailAddress;
	}


	
	public int getSortColumnIndex() {
		return sortColumnIndex;
	}



	public void setSortColumnIndex(int sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}



	public int getSortDirection() {
		return sortDirection;
	}



	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}



	public boolean isShowAll() {
		return showAll;
	}



	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}



	public int getPageIndex() {
		return pageIndex;
	}



	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	


	public String[] getUserId() {
		return userId;
	}



	public void setUserId(String[] userId) {
		this.userId = userId;
	}



	public String getSearchType() {
		return searchType;
	}



	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	public String getPerformAction() {
		return performAction;
	}



	public void setPerformAction(String performAction) {
		this.performAction = performAction;
	}



	public List<UserItemForm> getUsers() {
		return users;
	}



	public void setUsers(List<UserItemForm> users) {
		this.users = users;
	}



	public List<UserItemForm> getSelectedUsers() {
		return selectedUsers;
	}



	public void setSelectedUsers(List<UserItemForm> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}



	public String getTypeOfCustomerUsersToSearch() {
		return typeOfCustomerUsersToSearch;
	}



	public void setTypeOfCustomerUsersToSearch(String typeOfCustomerUsersToSearch) {
		this.typeOfCustomerUsersToSearch = typeOfCustomerUsersToSearch;
	}



	public void reset() {
		userId = null;
		performAction = "";
		searchType = "";
		searchFirstName = "";
		searchLastName = "";
		searchEmailAddress = "";
		sortColumnIndex = 0;
		sortDirection = 1;
		showAll = false;
		pageIndex = 0;		
		searchType = "";
		redirectingFrom = "";
		users = new ArrayList<UserItemForm>();
		selectedUsers = new ArrayList<UserItemForm>();
	}
	
}