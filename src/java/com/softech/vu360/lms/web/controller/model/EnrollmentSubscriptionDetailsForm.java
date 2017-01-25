package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.EnrollmentSubscriptionView;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class EnrollmentSubscriptionDetailsForm  implements ILMSBaseInterface {

	private String searchFirstName = null;
	private String searchLastName = null;
	private String searchEmailAddress = null;
	private String searchSubscriptionName = null;
	private String searchDateLimit = null;
	private String subscriptionCode = null;
	private String enrollmentMethod = "Learner";
	private String action = "search";
	private String searchType = "";
	private String subscriptionSearchType = null;
	private String subscriptionName = null;
	private String subscriptionSearchDirection = null;
	private int subscriptionSearchStart = 0;
	private int numberofLearnerenrolled = 0;
	private int numberofSubscriptionsenrolledin = 0;
	private int subscriptionSearchEnd = 0;
	private int subscriptionSearchPageNumber = 0;
	private boolean selected = false;
	private boolean seatsavailable = false;
	private int sortColumnIndex = 0;
	private int sortDirection = 1;
	private int pageIndex = 0;
	private int courseSearchResultsPageSize = 0;
	private int courseSearchStart = 0;
	private int courseSearchEnd = 0;
	private String courseSearchShowAll = null;
	private List<LearnerItemForm> selectedLearners = new ArrayList<LearnerItemForm>();
	private List<SubscriptionItemForm> selectedSubscriptions = new ArrayList<SubscriptionItemForm>();
	private List<LearnerItemForm> learners = new ArrayList<LearnerItemForm>();
	private List<EnrollmentSubscriptionView> enrollmentSubscriptionViewList = new ArrayList<EnrollmentSubscriptionView>();
	private List<Subscription> subscriptionList = new ArrayList<Subscription>();
	private List<Subscription> selectedsubscriptionList = new ArrayList<Subscription>();
	
	
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
	public String getEnrollmentMethod() {
		return enrollmentMethod;
	}
	public void setEnrollmentMethod(String enrollmentMethod) {
		this.enrollmentMethod = enrollmentMethod;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
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
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public List<LearnerItemForm> getSelectedLearners() {
		return selectedLearners;
	}
	public void setSelectedLearners(List<LearnerItemForm> selectedLearners) {
		this.selectedLearners = selectedLearners;
	}
	public List<LearnerItemForm> getLearners() {
		return learners;
	}
	public void setLearners(List<LearnerItemForm> learners) {
		this.learners = learners;
	}
	
	public String getSubscriptionSearchType() {
		return subscriptionSearchType;
	}
	public void setSubscriptionSearchType(String subscriptionSearchType) {
		this.subscriptionSearchType = subscriptionSearchType;
	}
	public String getSearchSubscriptionName() {
		return searchSubscriptionName;
	}
	public void setSearchSubscriptionName(String searchSubscriptionName) {
		this.searchSubscriptionName = searchSubscriptionName;
	}
	public String getSearchDateLimit() {
		return searchDateLimit;
	}
	public void setSearchDateLimit(String searchDateLimit) {
		this.searchDateLimit = searchDateLimit;
	}
	public String getSubscriptionCode() {
		return subscriptionCode;
	}
	public void setSubscriptionCode(String subscriptionCode) {
		this.subscriptionCode = subscriptionCode;
	}
	public List<EnrollmentSubscriptionView> getEnrollmentSubscriptionViewList() {
		return enrollmentSubscriptionViewList;
	}
	public void setEnrollmentSubscriptionViewList(
			List<EnrollmentSubscriptionView> enrollmentSubscriptionViewList) {
		this.enrollmentSubscriptionViewList = enrollmentSubscriptionViewList;
	}
	public int getSubscriptionSearchStart() {
		return subscriptionSearchStart;
	}
	public void setSubscriptionSearchStart(int subscriptionSearchStart) {
		this.subscriptionSearchStart = subscriptionSearchStart;
	}
	public int getSubscriptionSearchEnd() {
		return subscriptionSearchEnd;
	}
	public void setSubscriptionSearchEnd(int subscriptionSearchEnd) {
		this.subscriptionSearchEnd = subscriptionSearchEnd;
	}
	public String getSubscriptionName() {
		return subscriptionName;
	}
	public void setSubscriptionName(String subscriptionName) {
		this.subscriptionName = subscriptionName;
	}
	public List<SubscriptionItemForm> getSelectedSubscriptions() {
		return selectedSubscriptions;
	}
	public void setSelectedSubscriptions(
			List<SubscriptionItemForm> selectedSubscriptions) {
		this.selectedSubscriptions = selectedSubscriptions;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public List<Subscription> getSubscriptionList() {
		return subscriptionList;
	}
	public void setSubscriptionList(List<Subscription> subscriptionList) {
		this.subscriptionList = subscriptionList;
	}
	public List<Subscription> getSelectedsubscriptionList() {
		return selectedsubscriptionList;
	}
	public void setSelectedsubscriptionList(
			List<Subscription> selectedsubscriptionList) {
		this.selectedsubscriptionList = selectedsubscriptionList;
	}
	public int getCourseSearchResultsPageSize() {
		return courseSearchResultsPageSize;
	}
	public void setCourseSearchResultsPageSize(int courseSearchResultsPageSize) {
		this.courseSearchResultsPageSize = courseSearchResultsPageSize;
	}
	public int getCourseSearchStart() {
		return courseSearchStart;
	}
	public void setCourseSearchStart(int courseSearchStart) {
		this.courseSearchStart = courseSearchStart;
	}
	public int getCourseSearchEnd() {
		return courseSearchEnd;
	}
	public void setCourseSearchEnd(int courseSearchEnd) {
		this.courseSearchEnd = courseSearchEnd;
	}
	public String getCourseSearchShowAll() {
		return courseSearchShowAll;
	}
	public void setCourseSearchShowAll(String courseSearchShowAll) {
		this.courseSearchShowAll = courseSearchShowAll;
	}
	public boolean isSeatsavailable() {
		return seatsavailable;
	}
	public void setSeatsavailable(boolean seatsavailable) {
		this.seatsavailable = seatsavailable;
	}
	public int getNumberofLearnerenrolled() {
		return numberofLearnerenrolled;
	}
	public void setNumberofLearnerenrolled(int numberofLearnerenrolled) {
		this.numberofLearnerenrolled = numberofLearnerenrolled;
	}
	public int getNumberofSubscriptionsenrolledin() {
		return numberofSubscriptionsenrolledin;
	}
	public void setNumberofSubscriptionsenrolledin(
			int numberofSubscriptionsenrolledin) {
		this.numberofSubscriptionsenrolledin = numberofSubscriptionsenrolledin;
	}
	public String getSubscriptionSearchDirection() {
		return subscriptionSearchDirection;
	}
	public void setSubscriptionSearchDirection(String subscriptionSearchDirection) {
		this.subscriptionSearchDirection = subscriptionSearchDirection;
	}
	public int getSubscriptionSearchPageNumber() {
		return subscriptionSearchPageNumber;
	}
	public void setSubscriptionSearchPageNumber(int subscriptionSearchPageNumber) {
		this.subscriptionSearchPageNumber = subscriptionSearchPageNumber;
	}
	
}
