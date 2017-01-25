package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import  com.softech.vu360.lms.model.ValueHolderInterface;
import java.util.List;

import com.softech.vu360.lms.model.AlertTriggerFilter;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ManageFilterForm  implements ILMSBaseInterface{
	String[] selectedLearnersId=null;
	private static final long serialVersionUID = 1L;
	private String filterName="";
	private String firstName="";
	private String lastName="";
	private String emailAddress="";
	private String course="";
	private String courseName="";
	private String courseType="";
	 List<Learner> learnerListFromDB =  new ArrayList<Learner>();
	 List<LearnerGroup> learnerGroupListFromDB =  new ArrayList<LearnerGroup>();
	 List<OrganizationalGroup> organizationalGroupListFromDB =  new ArrayList<OrganizationalGroup>();
	 List<Course> coursesFromDBList =  new ArrayList<Course>();
	private String filtertType="learners";
	private AlertTriggerFilter filter=new AlertTriggerFilter();
	private List<MngFilter> mngAlerts = new ArrayList<MngFilter>();
	private List<AlertTriggerFilter> filters = new ArrayList<AlertTriggerFilter>();
	private ValueHolderInterface alerttrigger ;

	private long id = 0;
	private long filterId=0;

	public AlertTriggerFilter getFilter() {
		return filter;
	}

	public void setFilter(AlertTriggerFilter filter) {
		this.filter = filter;
	}

	public String getFiltertType() {
		return filtertType;
	}

	public void setFiltertType(String filtertType) {
		this.filtertType = filtertType;
	}

	

	public List<AlertTriggerFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<AlertTriggerFilter> filters) {
		this.filters = filters;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ValueHolderInterface getAlerttrigger() {
		return alerttrigger;
	}

	public void setAlerttrigger(ValueHolderInterface alerttrigger) {
		this.alerttrigger = alerttrigger;
	}

	public List<MngFilter> getMngAlerts() {
		return mngAlerts;
	}

	public void setMngAlerts(List<MngFilter> mngAlerts) {
		this.mngAlerts = mngAlerts;
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

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public List<Learner> getLearnerListFromDB() {
		return learnerListFromDB;
	}

	public void setLearnerListFromDB(List<Learner> learnerListFromDB) {
		this.learnerListFromDB = learnerListFromDB;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public List<LearnerGroup> getLearnerGroupListFromDB() {
		return learnerGroupListFromDB;
	}

	public void setLearnerGroupListFromDB(List<LearnerGroup> learnerGroupListFromDB) {
		this.learnerGroupListFromDB = learnerGroupListFromDB;
	}

	public List<OrganizationalGroup> getOrganizationalGroupListFromDB() {
		return organizationalGroupListFromDB;
	}

	public void setOrganizationalGroupListFromDB(
			List<OrganizationalGroup> organizationalGroupListFromDB) {
		this.organizationalGroupListFromDB = organizationalGroupListFromDB;
	}

	public List<Course> getCoursesFromDBList() {
		return coursesFromDBList;
	}

	public void setCoursesFromDBList(List<Course> coursesFromDBList) {
		this.coursesFromDBList = coursesFromDBList;
	}

	public String[] getSelectedLearnersId() {
		return selectedLearnersId;
	}

	public void setSelectedLearnersId(String[] selectedLearnersId) {
		this.selectedLearnersId = selectedLearnersId;
	}

	public long getFilterId() {
		return filterId;
	}

	public void setFilterId(long filterId) {
		this.filterId = filterId;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

}
