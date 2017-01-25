package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ViewLearnerEnrollmentForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems = new ArrayList<ViewLearnerEntitlementItem>();
	private List<AdminLearnerEnrollmentSearch> adminSearchMemberList = new ArrayList<AdminLearnerEnrollmentSearch>();
	private List<Map> learnerItems = new ArrayList<Map>();
	private List<CourseItem> courseItems = new ArrayList<CourseItem>();
	private long id = 0;
	// search related things
	private String searchType = "simpleSearch";
	private boolean advancedSearch = false;
	private String courseSearchType = "simplesearch";
	private String firstName = null;
	private String lastName = null;
	private String emailAddress = null;
	private String simpleSearchKey = null; 
	private long totalRecord =0;
	private String userName = null;
	// course search related things
	private String courseSearchKey = ""; 
	private String courseName = "";
	private String courseId = "";
	private String courseKeyword = "";
	//sorting and paging items
	private int pageIndex = 0;
	private int coursePageIndex = 0;
	private int sortDirection = 0;
	private String sortField = "";
	private boolean enableCertificate;
	
	private List<EnrollmentCourseView> enrollmentList = new ArrayList<EnrollmentCourseView>(); // [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing courses irrespective of contract and enrollments availability
	
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
	 * @return the sortField
	 */
	public String getSortField() {
		return sortField;
	}
	/**
	 * @param sortField the sortField to set
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public void reset(){
		adminSearchMemberList = null;
		viewLearnerEntitlementItems=null;
		learnerItems =null;
		courseItems =null;
		id = 0;
		// search related things
		searchType = "simpleSearch";
		courseSearchType = "simpleSearch";
		firstName = null;
		lastName = null;
		emailAddress = null;
		simpleSearchKey = null; 
		totalRecord =0;
		userName = null;
		// course search related things
		courseSearchKey = null; 
		courseName = null;
		courseId = null;
		courseKeyword = null;
		//sorting and paging items
		pageIndex = 0;
		coursePageIndex = 0;
		this.enrollmentList = new ArrayList<EnrollmentCourseView>();
		
	}
	public void addViewLearnerEntitlementItems(ViewLearnerEntitlementItem viewLearnerEntitlementItem) {
		viewLearnerEntitlementItems.add(viewLearnerEntitlementItem);
	}
	public List<ViewLearnerEntitlementItem> getViewLearnerEntitlementItems() {
		return viewLearnerEntitlementItems;
	}

	public void setViewLearnerEntitlementItems(
			List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems) {
		this.viewLearnerEntitlementItems = viewLearnerEntitlementItems;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
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
	public String getSimpleSearchKey() {
		return simpleSearchKey;
	}
	public void setSimpleSearchKey(String simpleSearchKey) {
		this.simpleSearchKey = simpleSearchKey;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public List<Map> getLearnerItems() {
		return learnerItems;
	}
	public void setLearnerItems(List<Map> learnerItems) {
		this.learnerItems = learnerItems;
	}
	public long getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCourseSearchType() {
		return courseSearchType;
	}
	public void setCourseSearchType(String courseSearchType) {
		this.courseSearchType = courseSearchType;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseKeyword() {
		return courseKeyword;
	}
	public void setCourseKeyword(String courseKeyword) {
		this.courseKeyword = courseKeyword;
	}
	public int getCoursePageIndex() {
		return coursePageIndex;
	}
	public void setCoursePageIndex(int coursePageIndex) {
		this.coursePageIndex = coursePageIndex;
	}
	public String getCourseSearchKey() {
		return courseSearchKey;
	}
	public void setCourseSearchKey(String courseSearchKey) {
		this.courseSearchKey = courseSearchKey;
	}
	public List<CourseItem> getCourseItems() {
		return courseItems;
	}
	public void setCourseItems(List<CourseItem> courseItems) {
		this.courseItems = courseItems;
	}
	/**
	 * @return the adminSearchMemberList
	 */
	public List<AdminLearnerEnrollmentSearch> getAdminSearchMemberList() {
		return adminSearchMemberList;
	}
	/**
	 * @param adminSearchMemberList the adminSearchMemberList to set
	 */
	public void setAdminSearchMemberList(
			List<AdminLearnerEnrollmentSearch> adminSearchMemberList) {
		this.adminSearchMemberList = adminSearchMemberList;
	}
	/**
	 * @param enrollmentList the enrollmentList to set
	 */
	public void setEnrollmentList(List<EnrollmentCourseView> enrollmentList) {
		this.enrollmentList = enrollmentList;
	}
	/**
	 * @return the enrollmentList
	 */
	public List<EnrollmentCourseView> getEnrollmentList() {
		return enrollmentList;
	}
	public boolean isEnableCertificate() {
		return enableCertificate;
	}
	public void setEnableCertificate(boolean enableCertificate) {
		this.enableCertificate = enableCertificate;
	}
	
	
}