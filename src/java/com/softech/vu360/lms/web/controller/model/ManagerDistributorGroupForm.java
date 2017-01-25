/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Tapas Mondal
 *
 */
public class ManagerDistributorGroupForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public ManagerDistributorGroupForm(){
		
	}
	
	private String distributorGroupName = null;
	private boolean allDistributorGroupPerPage = true;
	private String distributorGroupPerPage;

	private List<DistributorGroupItemForm> distributors = new ArrayList<DistributorGroupItemForm>();
	private List<DistributorGroupItemForm> selectedDistributors= new ArrayList<DistributorGroupItemForm>();
	private int[] deleteableDistributorGroups;
	private String action = "";
	private String searchType = ""; //initialization....very very important
	private String searchDistributorGroupName = null;

	/*private String sortColumn = "";
	private int sortDirection = 1;*/
	private int sortColumnIndex = 0;
	private int sortDirection = 1;
	public int getSortColumnIndex() {
		return sortColumnIndex;
	}
	public void setSortColumnIndex(int sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}

	private int pageIndex = 0;


	public List<DistributorGroupItemForm> getSelectedDistributors() {
		return selectedDistributors;
	}
	public void setSelectedDistributors(List<DistributorGroupItemForm> selectedDistributors) {
		this.selectedDistributors = selectedDistributors;
	}
	/**
	 * @return the allQuestionPerPage
	 */
	public boolean isAllDistributorGroupPerPage() {
		return allDistributorGroupPerPage;
	}
	/**
	 * @param allQuestionPerPage the allQuestionPerPage to set
	 */
	public void setAllDistributorGroupPage(boolean allDistributorGroup) {
		this.allDistributorGroupPerPage = allDistributorGroup;
	}
	public String getDistributorGroupName() {
		return distributorGroupName;
	}
	public void setDistributorGroupName(String distributorGroupName) {
		this.distributorGroupName = distributorGroupName;
	}
	public String getDistributorGroupPerPage() {
		return distributorGroupPerPage;
	}
	public void setDistributorGroupPerPage(String distributorGroupPerPage) {
		this.distributorGroupPerPage = distributorGroupPerPage;
	}
	public List<DistributorGroupItemForm> getDistributors() {
		return distributors;
	}
	public void setDistributors(List<DistributorGroupItemForm> distributors) {
		this.distributors = distributors;
	}
	public int[] getDeleteableDistributorGroups() {
		return deleteableDistributorGroups;
	}
	public void setDeleteableDistributorGroups(int[] deleteableDistributorGroups) {
		this.deleteableDistributorGroups = deleteableDistributorGroups;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public int getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public void setAllDistributorGroupPerPage(boolean allDistributorGroupPerPage) {
		this.allDistributorGroupPerPage = allDistributorGroupPerPage;
	}
	public String getSearchDistributorGroupName() {
		return searchDistributorGroupName;
	}
	public void setSearchDistributorGroupName(String searchDistributorGroupName) {
		this.searchDistributorGroupName = searchDistributorGroupName;
	}
	
	public void reset(){

		distributorGroupName=null;
		allDistributorGroupPerPage = true;
		distributorGroupPerPage=null;

		distributors = new ArrayList<DistributorGroupItemForm>();
		searchType=""; // initialization....very very important
		searchDistributorGroupName = null;

		sortDirection = 1;
		pageIndex = 0;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	/*public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}*/

}