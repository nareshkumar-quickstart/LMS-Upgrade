package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class EnrollmentDetailsForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	//The Step 1 form fields
	private String firstName = null;
	private String lastName = null;
	private String emailAddress = null;
	private String enrollmentMethod = "Learner";
	private String[] groups;
	private String currentPage=null;
	private String prev=null;
	private String[] users;
	private String[] selectedLearnerGroups;
	private String[] surveys;
	private String[] enrollCourses;
	private String[] enrollmentStartDate;
	private String[] enrollmentEndDate;
	private boolean modifyAllEntitlements = true;
	private String allCourseStartDate = null;
	private String allCourseEndDate = null;
	Map<Object, Object> context = new HashMap<Object, Object>();
	//search criteria
	private String action = "search";
	private String searchType = "";
	private String searchKey = null;
	private String searchFirstName = null;
	private String searchLastName = null;
	private String searchEmailAddress = null;
	private String searchCourseName = null;
	private String searchCourseId = null;
	private String searchEntitlementName = null;
	private String searchDateLimit = null;
	private String courseSearchType = null;
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
	private int coursesAssigned = 0;
	private int attemptedToEnroll = 0;
	private int learnersEnrolledSuccessfully = 0;
	private int  enrollmentsUpdated = 0;
	private List<Long> learnerIdList = new ArrayList<Long>();
	private String courseSearchStay = null;
	private Long maxPossibleEnrollment = 0l;
	private int learnersToBeEnrolled = 0;
	private boolean selectedSyncCourse = false;
	private boolean selectedNonSyncCourse = false;
	private List<Course> selectedEnrollCourses=  new ArrayList<Course>();
	private List<CourseEntitlementItem> courseEntitlementItems = new ArrayList<CourseEntitlementItem>();
	private List<CourseEntitlementItem> selectedCourseEntitlementItems = new ArrayList<CourseEntitlementItem>();
	private ArrayList<Map<Object, Object>> listMap = new ArrayList<Map<Object, Object>>();
	private List<LearnerItemForm> learners = new ArrayList<LearnerItemForm>();
	private List<LearnerItemForm> selectedLearners = new ArrayList<LearnerItemForm>();
	private List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems = new ArrayList<LearnerGroupEnrollmentItem>();
	private List<Survey> surveyList = new ArrayList<Survey>();
	private List<Survey> selectedSurveys = new ArrayList<Survey>();
	private List<SortedCourseEntitlementItem> sortedCourseEntitlementItemList = new ArrayList<SortedCourseEntitlementItem>();

	private List<EnrollmentCourseView> enrollmentCourseViewList = new ArrayList<EnrollmentCourseView>();
	private List<LearnerEnrollment> learnerEnrollmentCourseList = new ArrayList<LearnerEnrollment>();
	// validation purpose
	private String learnerGroupSearchAction = "search";
	
	private List<Learner> learnersNotEnrolled = new ArrayList<Learner>();
	
	//Enrollment Summary
	private String prevToDate = "true";
	private boolean duplicates = false;
	private boolean onConfirmation = false;
	private boolean enrollConfirmation = false;
	
	List <SurveySuggestedCourse> surveySuggestedCourses = new ArrayList<SurveySuggestedCourse>();
	
	
	public boolean getDuplicates() {
		return duplicates;
	}
	public void setDuplicates(boolean duplicates) {
		this.duplicates = duplicates;
	}
	public boolean getOnConfirmation() {
		return onConfirmation;
	}
	public void setOnConfirmation(boolean onConfirmation) {
		this.onConfirmation = onConfirmation;
	}
	public boolean getEnrollConfirmation() {
		return enrollConfirmation;
	}
	public void setEnrollConfirmation(boolean enrollConfirmation) {
		this.enrollConfirmation = enrollConfirmation;
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
	/**
	 * @param courseEntitlementDetails the courseEntitlementDetails to set
	 */
	public void setCourseEntitlementItems(List<CourseEntitlementItem> courseEntitlementItems) {
		this.courseEntitlementItems = courseEntitlementItems;
	}
	/**
	 * @return the courseEntitlementDetails
	 */
	public List<CourseEntitlementItem> getCourseEntitlementItems() {
		return courseEntitlementItems;
	}
	public boolean isModifyAllEntitlements() {
		return modifyAllEntitlements;
	}
	public void setModifyAllEntitlements(boolean modifyAllEntitlements) {
		this.modifyAllEntitlements = modifyAllEntitlements;
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
	public int getCoursesAssigned() {
		return coursesAssigned;
	}
	public void setCoursesAssigned(int coursesAssigned) {
		this.coursesAssigned = coursesAssigned;
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
	 * @return the searchCourseId
	 */
	public String getSearchCourseId() {
		return searchCourseId;
	}
	/**
	 * @param searchCourseId the searchCourseId to set
	 */
	public void setSearchCourseId(String searchCourseId) {
		this.searchCourseId = searchCourseId;
	}
	/**
	 * @return the searchEntitlementName
	 */
	public String getSearchEntitlementName() {
		return searchEntitlementName;
	}
	/**
	 * @param searchEntitlementName the searchEntitlementName to set
	 */
	public void setSearchEntitlementName(String searchEntitlementName) {
		this.searchEntitlementName = searchEntitlementName;
	}
	/**
	 * @return the searchDateLimit
	 */
	public String getSearchDateLimit() {
		return searchDateLimit;
	}
	/**
	 * @param searchDateLimit the searchDateLimit to set
	 */
	public void setSearchDateLimit(String searchDateLimit) {
		this.searchDateLimit = searchDateLimit;
	}
	/**
	 * @return the sortedCourseEntitlementItemList
	 */
	public List<SortedCourseEntitlementItem> getSortedCourseEntitlementItemList() {
		return sortedCourseEntitlementItemList;
	}
	/**
	 * @param sortedCourseEntitlementItemList the sortedCourseEntitlementItemList to set
	 */
	public void setSortedCourseEntitlementItemList(
			List<SortedCourseEntitlementItem> sortedCourseEntitlementItemList) {
		this.sortedCourseEntitlementItemList = sortedCourseEntitlementItemList;
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
	public Long getMaxPossibleEnrollment() {
		return maxPossibleEnrollment;
	}
	public void setMaxPossibleEnrollment(Long maxPossibleEnrollment) {
		this.maxPossibleEnrollment = maxPossibleEnrollment;
	}
	public int getLearnersToBeEnrolled() {
		return learnersToBeEnrolled;
	}
	public void setLearnersToBeEnrolled(int learnersToBeEnrolled) {
		this.learnersToBeEnrolled = learnersToBeEnrolled;
	}
	/**
	 * @param selectedSyncCourse the selectedSyncCourse to set
	 */
	public void setSelectedSyncCourse(boolean selectedSyncCourse) {
		this.selectedSyncCourse = selectedSyncCourse;
	}
	/**
	 * @return the selectedSyncCourse
	 */
	public boolean isSelectedSyncCourse() {
		return selectedSyncCourse;
	}
	/**
	 * @param selectedNonSyncCourse the selectedNonSyncCourse to set
	 */
	public void setSelectedNonSyncCourse(boolean selectedNonSyncCourse) {
		this.selectedNonSyncCourse = selectedNonSyncCourse;
	}
	/**
	 * @return the selectedNonSyncCourse
	 */
	public boolean isSelectedNonSyncCourse() {
		return selectedNonSyncCourse;
	}
	public List<Survey> getSurveyList() {
		return surveyList;
	}
	public void setSurveyList(List<Survey> surveyList) {
		this.surveyList = surveyList;
	}
	public List<Survey> getSelectedSurveys() {
		return selectedSurveys;
	}
	public void setSelectedSurveys(List<Survey> selectedSurveys) {
		this.selectedSurveys = selectedSurveys;
	}
	public String[] getSurveys() {
		return surveys;
	}
	public void setSurveys(String[] surveys) {
		this.surveys = surveys;
	}
	public Map<Object, Object> getContext() {
		return context;
	}
	public void setContext(Map<Object, Object> context) {
		this.context = context;
	}
	public ArrayList<Map<Object, Object>> getListMap() {
		return listMap;
	}
	public void setListMap(ArrayList<Map<Object, Object>> listMap) {
		this.listMap = listMap;
	}
	public String[] getEnrollCourses() {
		return enrollCourses;
	}
	public void setEnrollCourses(String[] enrollCourses) {
		this.enrollCourses = enrollCourses;
	}
	public List<Course> getSelectedEnrollCourses() {
		return selectedEnrollCourses;
	}
	public void setSelectedEnrollCourses(List<Course> selectedEnrollCourses) {
		this.selectedEnrollCourses = selectedEnrollCourses;
	}
	public List<LearnerEnrollment> getLearnerEnrollmentCourseList() {
		return learnerEnrollmentCourseList;
	}
	public void setLearnerEnrollmentCourseList(
			List<LearnerEnrollment> learnerEnrollmentCourseList) {
		this.learnerEnrollmentCourseList = learnerEnrollmentCourseList;
	}
	public String[] getEnrollmentEndDate() {
		return enrollmentEndDate;
	}
	public void setEnrollmentEndDate(String[] enrollmentEndDate) {
		this.enrollmentEndDate = enrollmentEndDate;
	}
	public String[] getEnrollmentStartDate() {
		return enrollmentStartDate;
	}
	public void setEnrollmentStartDate(String[] enrollmentStartDate) {
		this.enrollmentStartDate = enrollmentStartDate;
	}
	public List<SurveySuggestedCourse> getSurveySuggestedCourses() {
		return surveySuggestedCourses;
	}
	public void setSurveySuggestedCourses(
			List<SurveySuggestedCourse> surveySuggestedCourses) {
		this.surveySuggestedCourses = surveySuggestedCourses;
	}
	public String[] getUsers() {
		return users;
	}
	public void setUsers(String[] users) {
		this.users = users;
	}
	public List<Long> getLearnerIdList() {
		return learnerIdList;
	}
	public void setLearnerIdList(List<Long> learnerIdList) {
		this.learnerIdList = learnerIdList;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getPrev() {
		return prev;
	}
	public void setPrev(String prev) {
		this.prev = prev;
	}

}