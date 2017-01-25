package com.softech.vu360.lms.web.controller.model;



import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseAlertTriggeerFilter;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;



public class FilterCourseForm  implements ILMSBaseInterface{

	private static final long serialVersionUID = 1L;
    Long filterId;
    private VU360User user=new VU360User();
   	private String filterType="courses";
	private CourseAlertTriggeerFilter courseAlertTriggeerFilter= new CourseAlertTriggeerFilter();
	List<VU360User> users =  new ArrayList<VU360User>();
	List<Course> courseListFromDB=  new ArrayList<Course>();
	List<Course> selectedCourseList=  new ArrayList<Course>();
	String courses[] ;
	private String courseName = "";
	private String courseType = "";
	private String action = "";
	
	
	
	private String  pageIndex="0";
	private List<CourseEntitlementItem> courseEntitlementItems = new ArrayList<CourseEntitlementItem>();
	
	

	@SuppressWarnings("unused")

	
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





	public String getFilterType() {
		return filterType;
	}



	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}



	


	public CourseAlertTriggeerFilter getCourseAlertTriggeerFilter() {
		return courseAlertTriggeerFilter;
	}



	public void setCourseAlertTriggeerFilter(
			CourseAlertTriggeerFilter courseAlertTriggeerFilter) {
		this.courseAlertTriggeerFilter = courseAlertTriggeerFilter;
	}


	public String[] getCourses() {
		return courses;
	}



	public void setCourses(String[] courses) {
		this.courses = courses;
	}



	

	public List<Course> getCourseListFromDB() {
		return courseListFromDB;
	}



	public void setCourseListFromDB(List<Course> courseListFromDB) {
		this.courseListFromDB = courseListFromDB;
	}



	


	public List<Course> getSelectedCourseList() {
		return selectedCourseList;
	}



	public void setSelectedCourseList(List<Course> selectedCourseList) {
		this.selectedCourseList = selectedCourseList;
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



	public List<CourseEntitlementItem> getCourseEntitlementItems() {
		return courseEntitlementItems;
	}



	public void setCourseEntitlementItems(
			List<CourseEntitlementItem> courseEntitlementItems) {
		this.courseEntitlementItems = courseEntitlementItems;
	}



	public String getCourseType() {
		return courseType;
	}



	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}



	public String getCourseName() {
		return courseName;
	}



	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}




}



