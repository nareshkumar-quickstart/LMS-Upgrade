package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupAlertFilter;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class RecipientLearnerGroupForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Long recipientId;
	    private VU360User user=new VU360User();
	    
		private LearnerGroupAlertFilter learnerGroupAlertFilter= new LearnerGroupAlertFilter();
		
		List<VU360User> users =  new ArrayList<VU360User>();
		List<LearnerGroup> learnerGroupListFromDB =  new ArrayList<LearnerGroup>();
		List<OrganizationalGroup> orgGroupListFromDB =  new ArrayList<OrganizationalGroup>();
		
		String learnerGroup[] ;
		List<LearnerGroup> selectedLearnerGroupList =  new ArrayList<LearnerGroup>();
		private List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems = new ArrayList<LearnerGroupEnrollmentItem>();
		
		
		private String  filterName="";
		private String  pageIndex="0";

		
		public List<LearnerGroup> getLearnerGroupListFromDB() {
			return learnerGroupListFromDB;
		}



		public void setLearnerGroupListFromDB(List<LearnerGroup> learnerGroupListFromDB) {
			this.learnerGroupListFromDB = learnerGroupListFromDB;
		}



		public List<OrganizationalGroup> getOrgGroupListFromDB() {
			return orgGroupListFromDB;
		}



		public void setOrgGroupListFromDB(List<OrganizationalGroup> orgGroupListFromDB) {
			this.orgGroupListFromDB = orgGroupListFromDB;
		}

		public List<LearnerGroup> getSelectedLearnerGroupList() {
			return selectedLearnerGroupList;
		}



		public void setSelectedLearnerGroupList(
				List<LearnerGroup> selectedLearnerGroupList) {
			this.selectedLearnerGroupList = selectedLearnerGroupList;
		}



		public String[] getLearnerGroup() {
			return learnerGroup;
		}



		public void setLearnerGroup(String[] learnerGroup) {
			this.learnerGroup = learnerGroup;
		}




		public List<VU360User> getUsers() {
			return users;
		}



		public void setUsers(List<VU360User> users) {
			this.users = users;
		}


		public LearnerGroupAlertFilter getLearnerGroupAlertFilter() {
			return learnerGroupAlertFilter;
		}


		public void setLearnerGroupAlertFilter(
				LearnerGroupAlertFilter learnerGroupAlertFilter) {
			this.learnerGroupAlertFilter = learnerGroupAlertFilter;
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






		public String getPageIndex() {
			return pageIndex;
		}



		public void setPageIndex(String pageIndex) {
			this.pageIndex = pageIndex;
		}



		public List<LearnerGroupEnrollmentItem> getLearnerGroupEnrollmentItems() {
			return learnerGroupEnrollmentItems;
		}



		public void setLearnerGroupEnrollmentItems(
				List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems) {
			this.learnerGroupEnrollmentItems = learnerGroupEnrollmentItems;
		}



		public Long getrecipientId() {
			return recipientId;
		}



		public void setrecipientId(Long recipientId) {
			this.recipientId = recipientId;
		}




}
