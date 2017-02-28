package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


public class AddTrainingPlanForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String selectedTrainingPlanId = null;
	private String trainingPlanName=null;
	private String description=null;
	private String firstName = null;
	private String lastName = null;
	private String emailAddress = null;
	private String trainingPlanMethod = "Learner";
	private String[] orgGroups;
	private String[] selectedLearnerGroups;
	
	private String allCourseStartDate = null;
	private String allCourseStartDateRange = null;
	private String allCourseEndDate = null;
	private String allCourseEnrollmentMinEndDate = null;
	
	private String action = "";
	private String searchType = "";
	private String searchKey=null;
	private String searchFirstName=null;
	private String searchLastName=null;
	private String searchEmailAddress=null;
	
	
	
	private String searchEntitlementName = null;
	private String searchDateLimit = null;
	private String courseSearchType = null;
	private int courseSearchPageNumber = 0;
	private int courseSearchResultsPageSize = 0;
	private String courseSearchDirection = null;
	private String courseSearchShowAll = null;
	private int courseSearchStart = 0;
	private int courseSearchEnd = 0;
	private String courseSearchStay = null;
	
	private String searchCourseName=null;
	private String searchCourseID=null;
	private String searchKeyword=null;
	
	private String simpleSearchKey=null;
	
	//sorting and paging items
	private int sortColumnIndex = 0;
	private int sortDirection = 1;
	private int pageIndex = 0;
	
	private List<TrainingCustomerEntitlement> customerEntitlements = new ArrayList<TrainingCustomerEntitlement>();
	private List<LearnerItemForm> selectedLearners = new ArrayList<LearnerItemForm>();
	private List<LearnerItemForm> learners = new ArrayList<LearnerItemForm>();
	private List<LearnerGroupEnrollmentItem> learnerGroupTrainingItems = new ArrayList<LearnerGroupEnrollmentItem>();
	private List<CourseEntitlementItem> selectedCourseEntitlementItems = new ArrayList<CourseEntitlementItem>();
	private List<EnrollmentCourseView> enrollmentCourseViewList = new ArrayList<EnrollmentCourseView>();
	
	private boolean isSyncCourseSelected=false;
	private boolean isNonSyncCourseSelected=false;
	
	private boolean duplicates = false;
	private boolean onConfirmation = true;
	private boolean enrollConfirmation = false;

	// for forwarding to pages
	private String toFirst = "false";
	private String prevToDate = "true";
	
	// for result values
	private int enrolledSuccessfully = 0;
	private int attemptedToEnroll = 0;
	private int enrollmentsCreated = 0;
	private int numerOfLearnersFailed = 0;
	private int coursesAssigned = 0;
	private List<Learner> learnersFailedToEnroll = new ArrayList <Learner>();
	private boolean editMode = false;
	
	
	public boolean isEditMode() {
		return editMode;
	}
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	public int getAttemptedToEnroll() {
		return attemptedToEnroll;
	}
	public void setAttemptedToEnroll(int attemptedToEnroll) {
		this.attemptedToEnroll = attemptedToEnroll;
	}
	public int getEnrolledSuccessfully() {
		return enrolledSuccessfully;
	}
	public void setEnrolledSuccessfully(int enrolledSuccessfully) {
		this.enrolledSuccessfully = enrolledSuccessfully;
	}
	public int getEnrollmentsCreated() {
		return enrollmentsCreated;
	}
	public void setEnrollmentsCreated(int enrollmentsCreated) {
		this.enrollmentsCreated = enrollmentsCreated;
	}
	public int getNumerOfLearnersFailed() {
		return numerOfLearnersFailed;
	}
	public void setNumerOfLearnersFailed(int numerOfLearnersFailed) {
		this.numerOfLearnersFailed = numerOfLearnersFailed;
	}
	public int getCoursesAssigned() {
		return coursesAssigned;
	}
	public void setCoursesAssigned(int coursesAssigned) {
		this.coursesAssigned = coursesAssigned;
	}
	public List<Learner> getLearnersFailedToEnroll() {
		return learnersFailedToEnroll;
	}
	public void setLearnersFailedToEnroll(List<Learner> learnersFailedToEnroll) {
		this.learnersFailedToEnroll = learnersFailedToEnroll;
	}
	public String getPrevToDate() {
		return prevToDate;
	}
	public void setPrevToDate(String prevToDate) {
		this.prevToDate = prevToDate;
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
	public String getTrainingPlanMethod() {
		return trainingPlanMethod;
	}
	public void setTrainingPlanMethod(String trainingPlanMethod) {
		this.trainingPlanMethod = trainingPlanMethod;
	}
	public String[] getOrgGroups() {
		return orgGroups;
	}
	public void setOrgGroups(String[] orgGroups) {
		this.orgGroups = orgGroups;
	}
	public String[] getSelectedLearnerGroups() {
		return selectedLearnerGroups;
	}
	public void setSelectedLearnerGroups(String[] selectedLearnerGroups) {
		this.selectedLearnerGroups = selectedLearnerGroups;
	}
	public String getAllCourseStartDate() {
		return allCourseStartDate;
	}
	public void setAllCourseStartDate(String allCourseStartDate) {
		this.allCourseStartDate = allCourseStartDate;
	}
	public String getAllCourseEndDate() {
		return allCourseEndDate;
	}
	public void setAllCourseEndDate(String allCourseEndDate) {
		this.allCourseEndDate = allCourseEndDate;
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
	public List<LearnerItemForm> getLearners() {
		return learners;
	}
	public void setLearners(List<LearnerItemForm> learners) {
		this.learners = learners;
	}
	public boolean isDuplicates() {
		return duplicates;
	}
	public void setDuplicates(boolean duplicates) {
		this.duplicates = duplicates;
	}
	public boolean isOnConfirmation() {
		return onConfirmation;
	}
	public void setOnConfirmation(boolean onConfirmation) {
		this.onConfirmation = onConfirmation;
	}
	public boolean isEnrollConfirmation() {
		return enrollConfirmation;
	}
	public void setEnrollConfirmation(boolean enrollConfirmation) {
		this.enrollConfirmation = enrollConfirmation;
	}
	public List<LearnerGroupEnrollmentItem> getLearnerGroupTrainingItems() {
		return learnerGroupTrainingItems;
	}
	public void setLearnerGroupTrainingItems(
			List<LearnerGroupEnrollmentItem> learnerGroupTrainingItems) {
		this.learnerGroupTrainingItems = learnerGroupTrainingItems;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/*public List<AddTrainingCourse> getTrainingCourse() {
		return trainingCourse;
	}
	public void setTrainingCourse(List<AddTrainingCourse> trainingCourse) {
		this.trainingCourse = trainingCourse;
	}*/
	/**
	 * @return the trainingPlanName
	 */
	public String getTrainingPlanName() {
		return trainingPlanName;
	}
	/**
	 * @param trainingPlanName the trainingPlanName to set
	 */
	public void setTrainingPlanName(String trainingPlanName) {
		this.trainingPlanName = trainingPlanName;
	}
	/**
	 * @return the searchCourseName
	 */
	public String getSearchCourseName() {
		return searchCourseName;
	}
	/**
	 * @param searchCourseName the searchCourseName to set
	 */
	public void setSearchCourseName(String searchCourseName) {
		this.searchCourseName = searchCourseName;
	}
	/**
	 * @return the searchCourseID
	 */
	public String getSearchCourseID() {
		return searchCourseID;
	}
	/**
	 * @param searchCourseID the searchCourseID to set
	 */
	public void setSearchCourseID(String searchCourseID) {
		this.searchCourseID = searchCourseID;
	}
	/**
	 * @return the searchKeyword
	 */
	public String getSearchKeyword() {
		return searchKeyword;
	}
	/**
	 * @param searchKeyword the searchKeyword to set
	 */
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
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
	 * @return the sortColumnIndex
	 */
	public int getSortColumnIndex() {
		return sortColumnIndex;
	}
	/**
	 * @param sortColumnIndex the sortColumnIndex to set
	 */
	public void setSortColumnIndex(int sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}
	/**
	 * @return the sortDirection
	 */
	public int getSortDirection() {
		return sortDirection;
	}
	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}
	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}
	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<TrainingCustomerEntitlement> getCustomerEntitlements() {
		return customerEntitlements;
	}
	public void setCustomerEntitlements(
			List<TrainingCustomerEntitlement> customerEntitlements) {
		this.customerEntitlements = customerEntitlements;
	}
	public String getSelectedTrainingPlanId() {
		return selectedTrainingPlanId;
	}
	public void setSelectedTrainingPlanId(String selectedTrainingPlanId) {
		this.selectedTrainingPlanId = selectedTrainingPlanId;
	}
	public String getToFirst() {
		return toFirst;
	}
	public void setToFirst(String toFirst) {
		this.toFirst = toFirst;
	}
	public List<LearnerItemForm> getSelectedLearners() {
		return selectedLearners;
	}
	public void setSelectedLearners(List<LearnerItemForm> selectedLearners) {
		this.selectedLearners = selectedLearners;
	}
	/**
	 * @return the allCourseEnrollmentMinEndDate
	 */
	public String getAllCourseEnrollmentMinEndDate() {
		return allCourseEnrollmentMinEndDate;
	}
	/**
	 * @param allCourseEnrollmentMinEndDate the allCourseEnrollmentMinEndDate to set
	 */
	public void setAllCourseEnrollmentMinEndDate(
			String allCourseEnrollmentMinEndDate) {
		this.allCourseEnrollmentMinEndDate = allCourseEnrollmentMinEndDate;
	}
	/**
	 * @return the allCourseStartDateRange
	 */
	public String getAllCourseStartDateRange() {
		return allCourseStartDateRange;
	}
	/**
	 * @param allCourseStartDateRange the allCourseStartDateRange to set
	 */
	public void setAllCourseStartDateRange(String allCourseStartDateRange) {
		this.allCourseStartDateRange = allCourseStartDateRange;
	}
	public String getSearchCourseId() {
		return searchCourseID;
	}
	/**
	 * @param searchCourseId the searchCourseId to set
	 */
	public void setSearchCourseId(String searchCourseId) {
		this.searchCourseID = searchCourseId;
	}
	public String getSearchEntitlementName() {
		return searchEntitlementName;
	}
	/**
	 * @param searchEntitlementName the searchEntitlementName to set
	 */
	public void setSearchEntitlementName(String searchEntitlementName) {
		this.searchEntitlementName = searchEntitlementName;
	}
	public String getSearchDateLimit() {
		return searchDateLimit;
	}
	/**
	 * @param searchDateLimit the searchDateLimit to set
	 */
	public void setSearchDateLimit(String searchDateLimit) {
		this.searchDateLimit = searchDateLimit;
	}
	public String getCourseSearchType() {
		return courseSearchType;
	}
	public void setCourseSearchType(String courseSearchType) {
		this.courseSearchType = courseSearchType;
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
	public List<CourseEntitlementItem> getSelectedCourseEntitlementItems() {
		return selectedCourseEntitlementItems;
	}
	public void setSelectedCourseEntitlementItems(
			List<CourseEntitlementItem> selectedCourseEntitlementItems) {
		this.selectedCourseEntitlementItems = selectedCourseEntitlementItems;
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
	public List<EnrollmentCourseView> getEnrollmentCourseViewList() {
		return enrollmentCourseViewList;
	}
	public void setEnrollmentCourseViewList(
			List<EnrollmentCourseView> enrollmentCourseViewList) {
		this.enrollmentCourseViewList = enrollmentCourseViewList;
	}
	public boolean isSyncCourseSelected() {
		return isSyncCourseSelected;
	}
	public void setSyncCourseSelected(boolean isSyncCourseSelected) {
		this.isSyncCourseSelected = isSyncCourseSelected;
	}
	public boolean isNonSyncCourseSelected() {
		return isNonSyncCourseSelected;
	}
	public void setNonSyncCourseSelected(boolean isNonSyncCourseSelected) {
		this.isNonSyncCourseSelected = isNonSyncCourseSelected;
	}
	
	

	
}