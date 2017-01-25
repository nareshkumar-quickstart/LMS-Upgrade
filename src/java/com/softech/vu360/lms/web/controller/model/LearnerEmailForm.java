package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class LearnerEmailForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String emailMethod = "Learner";
	private String[] groups;
	private String[] selectedLearnerGroups;
	// search criteria
	private String action = "search";
	private String learnerGroupSearchAction = "search";
	private String searchType = "";
	private String searchKey = null;
	private String searchFirstName = null;
	private String searchLastName = null;
	private String searchEmailAddress = null;
	// sorting and paging items
	private String sortColumn = "";
	private int sortDirection = 1;
	private int sortPageIndex = 0;
	// mail items
	private String mailSubject = "";
	private String message = "";
	private String notifyMe = "";
	private String fromEmail = "";
	private String toEmails = "";
	private List<LearnerGroupMailItem> learnerGroupMailItems = new ArrayList<LearnerGroupMailItem>();
	private List<LearnerItemForm> learners = new ArrayList<LearnerItemForm>();
	private List<LearnerItemForm> selectedLearners = new ArrayList<LearnerItemForm>();
	private List<Learner> leranersToBeMailed = new ArrayList <Learner>();

	/*
	 * getter & setters. 
	 */
	public void formReset(){

		emailMethod = "Learner";
		groups=null;
		selectedLearnerGroups=null;
		fromEmail="";
		toEmails="";	 
		// search criteria
		action = "search";
		searchType = "";
		searchKey = null;
		searchFirstName = null;
		searchLastName = null;
		searchEmailAddress = null;
		// sorting and paging items
		sortColumn = "";
		sortDirection = 1;
		sortPageIndex = 0;
		// mail items
		mailSubject = "";
		message = "";
		notifyMe = "";

		learnerGroupMailItems =null;
		learners = null;
		selectedLearners = null;
		leranersToBeMailed = null;
		leranersToBeMailed = new ArrayList <Learner>();
		learnerGroupMailItems = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(LearnerGroupMailItem.class));
		learners = new ArrayList<LearnerItemForm>();
		selectedLearners = new ArrayList<LearnerItemForm>();

	}
	public String getEmailMethod() {
		return emailMethod;
	}

	public void setEmailMethod(String emailMethod) {
		this.emailMethod = emailMethod;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
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

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public int getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	public int getSortPageIndex() {
		return sortPageIndex;
	}

	public void setSortPageIndex(int sortPageIndex) {
		this.sortPageIndex = sortPageIndex;
	}

	public List<LearnerItemForm> getLearners() {
		return learners;
	}

	public void setLearners(List<LearnerItemForm> learners) {
		this.learners = learners;
	}

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public List<LearnerGroupMailItem> getLearnerGroupMailItems() {
		return learnerGroupMailItems;
	}

	public void setLearnerGroupMailItems(
			List<LearnerGroupMailItem> learnerGroupMailItems) {
		this.learnerGroupMailItems = learnerGroupMailItems;
	}

	public String[] getSelectedLearnerGroups() {
		return selectedLearnerGroups;
	}

	public void setSelectedLearnerGroups(String[] selectedLearnerGroups) {
		this.selectedLearnerGroups = selectedLearnerGroups;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Learner> getLeranersToBeMailed() {
		return leranersToBeMailed;
	}

	public void setLeranersToBeMailed(List<Learner> leranersToBeMailed) {
		this.leranersToBeMailed = leranersToBeMailed;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	public List<LearnerItemForm> getSelectedLearners() {
		return selectedLearners;
	}

	public void setSelectedLearners(List<LearnerItemForm> selectedLearners) {
		this.selectedLearners = selectedLearners;
	}

	public String getNotifyMe() {
		return notifyMe;
	}

	public void setNotifyMe(String notifyMe) {
		this.notifyMe = notifyMe;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getToEmails() {
		return toEmails;
	}
	public void setToEmails(String toEmails) {
		this.toEmails = toEmails;
	}
	public String getLearnerGroupSearchAction() {
		return learnerGroupSearchAction;
	}
	public void setLearnerGroupSearchAction(String learnerGroupSearchAction) {
		this.learnerGroupSearchAction = learnerGroupSearchAction;
	}

}