package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class AddLearnerGroupForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String learnerGroupId = null;
	private String action = "search";
	private String searchKey = null;
	private String searchType = null;
	private String searchFirstName = null;
	private String searchLastName = null;
	private String searchEmailAddress = null;
	private int pageIndex = 0;
	private List<LearnerItemForm> learners = new ArrayList<LearnerItemForm>();
	private List<LearnerItemForm> selectedLearners = new ArrayList<LearnerItemForm>();
	private String sortBy = "firstName";
	private int sortDirection = 0;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
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
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public List<LearnerItemForm> getLearners() {
		return learners;
	}
	public void setLearners(List<LearnerItemForm> learners) {
		this.learners = learners;
	}
	public List<LearnerItemForm> getSelectedLearners() {
		return selectedLearners;
	}
	public void setSelectedLearners(List<LearnerItemForm> selectedLearners) {

		this.selectedLearners = selectedLearners;
	}
	public String getLearnerGroupId() {
		return learnerGroupId;
	}
	public void setLearnerGroupId(String learnerGroupId) {
		this.learnerGroupId = learnerGroupId;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public int getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}
	
}