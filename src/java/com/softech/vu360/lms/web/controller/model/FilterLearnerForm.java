

package com.softech.vu360.lms.web.controller.model;



import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAlertFilter;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;



public class FilterLearnerForm   implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	String selectedLearner[] ;
    Long filterId;
    private VU360User user=new VU360User();
    
	private String filterType="learners";
	private LearnerAlertFilter learnerAlertFilter= new LearnerAlertFilter();
	List<VU360User> learnerListFromDB =  new ArrayList<VU360User>();
	List<VU360User> users =  new ArrayList<VU360User>();
	
	
	List<Learner> learnerss = new ArrayList<Learner>();
	
	
	

	private String  learners="";

	

	private String  pageIndex="2";

	

	@SuppressWarnings("unused")

	

	

	
	public void setLearners(String learners) {

		this.learners = learners;

	}



	public String getLearners() {

		return learners;

	}



	public void setPageIndex(String pageIndex) {

		this.pageIndex = pageIndex;

	}



	public String getPageIndex() {

		return pageIndex;

	}

	
	public List<VU360User> getUsers() {
		return users;
	}



	public void setUsers(List<VU360User> users) {
		this.users = users;
	}



	public List<Learner> getLearnerss() {
		return learnerss;
	}



	public void setLearnerss(List<Learner> learnerss) {
		this.learnerss = learnerss;
	}



	public String[] getSelectedLearner() {
		return selectedLearner;
	}



	public void setSelectedLearner(String[] selectedLearner) {
		this.selectedLearner = selectedLearner;
	}

	public String getFilterType() {
		return filterType;
	}



	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}



	public LearnerAlertFilter getLearnerAlertFilter() {
		return learnerAlertFilter;
	}



	public void setLearnerAlertFilter(LearnerAlertFilter learnerAlertFilter) {
		this.learnerAlertFilter = learnerAlertFilter;
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



	public List<VU360User> getLearnerListFromDB() {
		return learnerListFromDB;
	}



	public void setLearnerListFromDB(List<VU360User> learnerListFromDB) {
		this.learnerListFromDB = learnerListFromDB;
	}



}


