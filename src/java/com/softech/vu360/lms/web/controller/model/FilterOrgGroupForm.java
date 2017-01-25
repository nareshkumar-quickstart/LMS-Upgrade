
package com.softech.vu360.lms.web.controller.model;



import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.OrgGroupAlertFilterTrigger;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;



public class FilterOrgGroupForm implements ILMSBaseInterface {

	private static final long serialVersionUID = 1L;
    Long filterId;
    private VU360User user=new VU360User();
    private String filterType="learners";
	private OrgGroupAlertFilterTrigger orgGroupAlertFilterTrigger= new OrgGroupAlertFilterTrigger();
	List<VU360User> users =  new ArrayList<VU360User>();
	List<OrganizationalGroup> orgGroupListFromDB =  new ArrayList<OrganizationalGroup>();
	String orgGroup[] ;
	List<OrganizationalGroup> selectedOrgGroupList =  new ArrayList<OrganizationalGroup>();
	private String  filterName="";

	private String  pageIndex="2";
	private List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems = new ArrayList<LearnerGroupEnrollmentItem>();
	

	@SuppressWarnings("unused")

		

	public void setPageIndex(String pageIndex) {

		this.pageIndex = pageIndex;

	}



	public String getPageIndex() {

		return pageIndex;

	}

	

	public List<OrganizationalGroup> getOrgGroupListFromDB() {
		return orgGroupListFromDB;
	}



	public void setOrgGroupListFromDB(List<OrganizationalGroup> orgGroupListFromDB) {
		this.orgGroupListFromDB = orgGroupListFromDB;
	}



	public String[] getOrgGroup() {
		return orgGroup;
	}



	public void setOrgGroup(String[] orgGroup) {
		this.orgGroup = orgGroup;
	}



	public List<OrganizationalGroup> getSelectedOrgGroupList() {
		return selectedOrgGroupList;
	}



	public void setSelectedOrgGroupList(
			List<OrganizationalGroup> selectedOrgGroupList) {
		this.selectedOrgGroupList = selectedOrgGroupList;
	}
	
	public List<VU360User> getUsers() {
		return users;
	}

	public void setUsers(List<VU360User> users) {
		this.users = users;
	}


	public String getFilterType() {
		return filterType;
	}



	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}



	public OrgGroupAlertFilterTrigger getOrgGroupAlertFilterTrigger() {
		return orgGroupAlertFilterTrigger;
	}



	public void setOrgGroupAlertFilterTrigger(
			OrgGroupAlertFilterTrigger orgGroupAlertFilterTrigger) {
		this.orgGroupAlertFilterTrigger = orgGroupAlertFilterTrigger;
	}




	public String getFilterName() {
		return filterName;
	}



	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}



	public VU360User getUser() {
		return user;
	}



	public void setUser(VU360User user) {
		this.user = user;
	}



	public Long getFilterId() {
		return filterId;
	}



	public void setFilterId(Long filterId) {
		this.filterId = filterId;
	}



	public List<LearnerGroupEnrollmentItem> getLearnerGroupEnrollmentItems() {
		return learnerGroupEnrollmentItems;
	}



	public void setLearnerGroupEnrollmentItems(
			List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems) {
		this.learnerGroupEnrollmentItems = learnerGroupEnrollmentItems;
	}


}



