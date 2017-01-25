package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class AssignSurveyForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	//The Step 1 form fields
	private String firstName = null;
	private String lastName = null;
	private String emailAddress = null;
	private String enrollmentMethod = "Learner";
	private String[] groups;
	private String[] selectedLearnerGroups;
	private String surveyDateAssignment = null;
	private String allSurveyStartDate = null;
	private String allSurveyEndDate = null;
	
	//search criteria
	private String action = "search";
	private String searchType = "";
	private String searchKey = null;
	private String searchFirstName = null;
	private String searchLastName = null;
	private String searchEmailAddress = null;
	
	private String searchSurveyName = null;
	 
	private String searchSurveyStatus = null;
	private String searchSurveyRetired = null;
	private String surveySearchType = null;
	private int courseSearchPageNumber = 0;
	private int courseSearchResultsPageSize = 0;
	private String courseSearchDirection = null;
	private String courseSearchShowAll = null;
	private int courseSearchStart = 0;
	private int courseSearchEnd = 0;
	//sorting and paging items
	private int sortColumnIndex = 0;
	private int sortDirection = 1;
	private int pageIndex = 0;
	
	private int enrollmentsCreated = 0;
	private int surveysAssigned = 0;
	private int attemptedToEnroll = 0;
	private int learnersEnrolledSuccessfully = 0;
	private int  enrollmentsUpdated = 0;
	
	private String courseSearchStay = null;
 
	
	private List<SurveyItem> surveyItemList = new ArrayList<SurveyItem>();
 

	private List<LearnerItemForm> learners = new ArrayList<LearnerItemForm>();
	private List<LearnerItemForm> selectedLearners = new ArrayList<LearnerItemForm>();
	private List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems = new ArrayList<LearnerGroupEnrollmentItem>();
	private List<CustomerItemForm> customers = new ArrayList<CustomerItemForm>();
	private List<CustomerItemForm> selectedCustomers = new ArrayList<CustomerItemForm>();
	
	// validation purpose
	private String learnerGroupSearchAction = "search";
	
	private List<Learner> learnersNotEnrolled = new ArrayList<Learner>();
	
	//Enrollment Summary
	private String prevToDate = "true";
	private boolean sendSurveyNotificationToLearners = false;
	private boolean emailOnConfirmation = false;
	private boolean emailOnCompletion = false;
 
	
	public List<CustomerItemForm> getCustomers() {
		return customers;
	}
	
	public void setCustomers(List<CustomerItemForm> customers) {
		this.customers = customers;
	}
	
	public List<CustomerItemForm> getSelectedCustomers() {
		return selectedCustomers;
	}
	
	public void setSelectedCustomers(List<CustomerItemForm> selectedCustomers) {
		this.selectedCustomers = selectedCustomers;
	}
	
	public List<LearnerGroupEnrollmentItem> getLearnerGroupEnrollmentItems() {
		return learnerGroupEnrollmentItems;
	}
	
	public void setLearnerGroupEnrollmentItems(List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems) {
		this.learnerGroupEnrollmentItems = learnerGroupEnrollmentItems;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEnrollmentMethod() {
		return enrollmentMethod;
	}
	public void setEnrollmentMethod(String enrollmentMethod) {
		this.enrollmentMethod = enrollmentMethod;
	}

	public String[] getGroups() {
		return groups;
	}
	
	public void setGroups(String[] groups) {
		this.groups = groups;
	}
	
	public String[] getSelectedLearnerGroups() {
		return selectedLearnerGroups;
	}

	public void setSelectedLearnerGroups(String[] selectedLearnerGroups) {
		this.selectedLearnerGroups = selectedLearnerGroups;
	}
	public List<LearnerItemForm> getLearners() {
		return learners;
	}
	public void setLearners(List<LearnerItemForm> learners) {
		this.learners = learners;
	}

	public List<Learner> getLearnersNotEnrolled() {
		return learnersNotEnrolled;
	}
	public void setLearnersNotEnrolled(List<Learner> learnersNotEnrolled) {
		this.learnersNotEnrolled = learnersNotEnrolled;
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
	public int getSortColumnIndex() {
		return sortColumnIndex;
	}
	public void setSortColumnIndex(int sortColumn) {
		this.sortColumnIndex = sortColumn;
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
	public void setPageIndex(int sortPageIndex) {
		this.pageIndex = sortPageIndex;
	}
	public int getEnrollmentsCreated() {
		return enrollmentsCreated;
	}
	public void setEnrollmentsCreated(int enrollmentsCreated) {
		this.enrollmentsCreated = enrollmentsCreated;
	}
	public int getSurveysAssigned() {
		return surveysAssigned;
	}
	public void setSurveysAssigned(int surveysAssigned) {
		this.surveysAssigned = surveysAssigned;
	}
	public int getAttemptedToEnroll() {
		return attemptedToEnroll;
	}
	public void setAttemptedToEnroll(int attemptedToEnroll) {
		this.attemptedToEnroll = attemptedToEnroll;
	}
	public int getLearnersEnrolledSuccessfully() {
		return learnersEnrolledSuccessfully;
	}
	public void setLearnersEnrolledSuccessfully(int learnersEnrolledSucssessfully) {
		this.learnersEnrolledSuccessfully = learnersEnrolledSucssessfully;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the selectedLearners
	 */
	public List<LearnerItemForm> getSelectedLearners() {
		return selectedLearners;
	}
	/**
	 * @param selectedLearners the selectedLearners to set
	 */
	public void setSelectedLearners(List<LearnerItemForm> selectedLearners) {
		this.selectedLearners = selectedLearners;
	}
	public int getEnrollmentsUpdated() {
		return enrollmentsUpdated;
	}
	public void setEnrollmentsUpdated(int enrollmentsUpdated) {
		this.enrollmentsUpdated = enrollmentsUpdated;
	}
	public String getPrevToDate() {
		return prevToDate;
	}
	public void setPrevToDate(String prevToDate) {
		this.prevToDate = prevToDate;
	}
	public String getLearnerGroupSearchAction() {
		return learnerGroupSearchAction;
	}
	public void setLearnerGroupSearchAction(String learnerGroupSearchAction) {
		this.learnerGroupSearchAction = learnerGroupSearchAction;
	}
 
 
 
	public int getCourseSearchPageNumber() {
		return courseSearchPageNumber;
	}
	public void setCourseSearchPageNumber(int courseSearchPageNumber) {
		this.courseSearchPageNumber = courseSearchPageNumber;
	}
	public String getCourseSearchDirection() {
		return courseSearchDirection;
	}
	public void setCourseSearchDirection(String courseSearchDirection) {
		this.courseSearchDirection = courseSearchDirection;
	}
	public String getCourseSearchShowAll() {
		return courseSearchShowAll;
	}
	public void setCourseSearchShowAll(String courseSearchShowAll) {
		this.courseSearchShowAll = courseSearchShowAll;
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
 
	public String getCourseSearchStay() {
		return courseSearchStay;
	}
	public void setCourseSearchStay(String courseSearchStay) {
		this.courseSearchStay = courseSearchStay;
	}
	public int getCourseSearchResultsPageSize() {
		return courseSearchResultsPageSize;
	}
	public void setCourseSearchResultsPageSize(int courseSearchResultsPageSize) {
		this.courseSearchResultsPageSize = courseSearchResultsPageSize;
	}
  
	public String getSearchSurveyName() {
		return searchSurveyName;
	}
	public void setSearchSurveyName(String searchSurveyName) {
		this.searchSurveyName = searchSurveyName;
	}
	public String getSearchSurveyStatus() {
		return searchSurveyStatus;
	}
	public void setSearchSurveyStatus(String searchSurveyStatus) {
		this.searchSurveyStatus = searchSurveyStatus;
	}
	public String getSearchSurveyRetired() {
		return searchSurveyRetired;
	}
	public void setSearchSurveyRetired(String searchSurveyRetired) {
		this.searchSurveyRetired = searchSurveyRetired;
	}
	public String getSurveySearchType() {
		return surveySearchType;
	}
	public void setSurveySearchType(String surveySearchType) {
		this.surveySearchType = surveySearchType;
	}
	/**
	 * @param surveyItemList the surveyItemList to set
	 */
	public void setSurveyItemList(List<SurveyItem> surveyItemList) {
		this.surveyItemList = surveyItemList;
	}
	/**
	 * @return the surveyItemList
	 */
	public List<SurveyItem> getSurveyItemList() {
		return surveyItemList;
	}
	/**
	 * @param surveyDateAssignment the surveyDateAssignment to set
	 */
	public void setSurveyDateAssignment(String surveyDateAssignment) {
		this.surveyDateAssignment = surveyDateAssignment;
	}
	/**
	 * @return the surveyDateAssignment
	 */
	public String getSurveyDateAssignment() {
		return surveyDateAssignment;
	}
	/**
	 * @return the allSurveyStartDate
	 */
	public String getAllSurveyStartDate() {
		return allSurveyStartDate;
	}
	/**
	 * @param allSurveyStartDate the allSurveyStartDate to set
	 */
	public void setAllSurveyStartDate(String allSurveyStartDate) {
		this.allSurveyStartDate = allSurveyStartDate;
	}
	/**
	 * @return the allSurveyEndDate
	 */
	public String getAllSurveyEndDate() {
		return allSurveyEndDate;
	}
	/**
	 * @param allSurveyEndDate the allSurveyEndDate to set
	 */
	public void setAllSurveyEndDate(String allSurveyEndDate) {
		this.allSurveyEndDate = allSurveyEndDate;
	}
	/**
	 * @return the sendSurveyNotificationToLearners
	 */
	public boolean isSendSurveyNotificationToLearners() {
		return sendSurveyNotificationToLearners;
	}
	/**
	 * @param sendSurveyNotificationToLearners the sendSurveyNotificationToLearners to set
	 */
	public void setSendSurveyNotificationToLearners(
			boolean sendSurveyNotificationToLearners) {
		this.sendSurveyNotificationToLearners = sendSurveyNotificationToLearners;
	}
	/**
	 * @return the emailOnConfirmation
	 */
	public boolean isEmailOnConfirmation() {
		return emailOnConfirmation;
	}
	/**
	 * @param emailOnConfirmation the emailOnConfirmation to set
	 */
	public void setEmailOnConfirmation(boolean emailOnConfirmation) {
		this.emailOnConfirmation = emailOnConfirmation;
	}
	/**
	 * @return the emailOnCompletion
	 */
	public boolean isEmailOnCompletion() {
		return emailOnCompletion;
	}
	/**
	 * @param emailOnCompletion the emailOnCompletion to set
	 */
	public void setEmailOnCompletion(boolean emailOnCompletion) {
		this.emailOnCompletion = emailOnCompletion;
	}
	 
 

}