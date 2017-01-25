	
	package com.softech.vu360.lms.web.controller.model;
	
	
	
	import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseAlertTriggeerFilter;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAlertFilter;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupAlertFilter;
import com.softech.vu360.lms.model.OrgGroupAlertFilterTrigger;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
	
	
	
	public class FilterTriggerForm  implements ILMSBaseInterface{
		private static final long serialVersionUID = 1L;
		String selectedLearner[] ;
		String licenseExpirationdate = "";


		Long triggerId;
	    private VU360User user=new VU360User();
	    private String courseName="";
	    private String courseType="";
		private String filterType="learners";
		private LearnerAlertFilter learnerAlertFilter= new LearnerAlertFilter();
		private LearnerGroupAlertFilter learnerGroupAlertFilter= new LearnerGroupAlertFilter();
		private OrgGroupAlertFilterTrigger orgGroupAlertFilterTrigger= new OrgGroupAlertFilterTrigger();
		private CourseAlertTriggeerFilter courseAlertTriggeerFilter= new CourseAlertTriggeerFilter();
		private String action = "";
		private List<VU360User> users =  new ArrayList<VU360User>();
		private List<VU360User> learnerListFromDB =  new ArrayList<VU360User>();
		private List<LearnerGroup> learnerGroupListFromDB =  new ArrayList<LearnerGroup>();
		private List<OrganizationalGroup> orgGroupListFromDB =  new ArrayList<OrganizationalGroup>();
		private List<Course> courseListFromDB=  new ArrayList<Course>();
		private String learnerGroup[] ;
		private List<LearnerGroup> selectedLearnerGroupList =  new ArrayList<LearnerGroup>();
		private String orgGroup[] ;
		private List<OrganizationalGroup> selectedOrgGroupList =  new ArrayList<OrganizationalGroup>();
		private List<Course> selectedCourseList=  new ArrayList<Course>();
		private String courses[] ;
		private String groups[] ;
		private List<Learner> learnerss = new ArrayList<Learner>();
		private String  filterName="";
		private String  learners="";
		private String  pageIndex="2";
		@SuppressWarnings("unused")
		private int courseSearchResultsPageSize = 0;
		private List<Course>courseList=  new ArrayList<Course>();
		

		private List<CourseEntitlementItem> courseEntitlementItems = new ArrayList<CourseEntitlementItem>();

	

		private List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems = new ArrayList<LearnerGroupEnrollmentItem>();

	    public String getLicenseExpirationdate() {
			return licenseExpirationdate;
		}

		public void setLicenseExpirationdate(String licenseExpirationdate) {
			this.licenseExpirationdate = licenseExpirationdate;
		}
		

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
	
		public void setCourseSearchResultsPageSize(int courseSearchResultsPageSize) {
	
			this.courseSearchResultsPageSize = courseSearchResultsPageSize;
	
		}
	
		public void setLearnerGroupEnrollmentItems(List<LearnerGroupEnrollmentItem> learnerGroupEnrollmentItems) {
	
			this.learnerGroupEnrollmentItems = learnerGroupEnrollmentItems;
	
		}
	
		
		public void setCourseEntitlementItems(List<CourseEntitlementItem> courseEntitlementItems) {
	
			this.courseEntitlementItems = courseEntitlementItems;
	
		}
	
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
	
	
		public String getAction() {
			return action;
		}
	
		public void setAction(String action) {
			this.action = action;
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
	
		public String[] getOrgGroup() {
			return orgGroup;
		}
	
		public void setOrgGroup(String[] orgGroup) {
			this.orgGroup = orgGroup;
		}
	
		public List<OrganizationalGroup> getSelectedOrgGroupList() {
			return selectedOrgGroupList;
		}
	
		public void setSelectedOrgGroupList(
				List<OrganizationalGroup> selectedOrgGroupList) {
			this.selectedOrgGroupList = selectedOrgGroupList;
		}
		
		public List<VU360User> getLearnerListFromDB() {
			return learnerListFromDB;
		}
	
		public void setLearnerListFromDB(List<VU360User> learnerListFromDB) {
			this.learnerListFromDB = learnerListFromDB;
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



		public Long getTriggerId() {
			return triggerId;
		}



		public void setTriggerId(Long triggerId) {
			this.triggerId = triggerId;
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



		public LearnerGroupAlertFilter getLearnerGroupAlertFilter() {
			return learnerGroupAlertFilter;
		}



		public void setLearnerGroupAlertFilter(
				LearnerGroupAlertFilter learnerGroupAlertFilter) {
			this.learnerGroupAlertFilter = learnerGroupAlertFilter;
		}



		public OrgGroupAlertFilterTrigger getOrgGroupAlertFilterTrigger() {
			return orgGroupAlertFilterTrigger;
		}



		public void setOrgGroupAlertFilterTrigger(
				OrgGroupAlertFilterTrigger orgGroupAlertFilterTrigger) {
			this.orgGroupAlertFilterTrigger = orgGroupAlertFilterTrigger;
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



		public String getFilterName() {
			return filterName;
		}



		public void setFilterName(String filterName) {
			this.filterName = filterName;
		}



		public int getCourseSearchResultsPageSize() {
			return courseSearchResultsPageSize;
		}



		public List<CourseEntitlementItem> getCourseEntitlementItems() {
			return courseEntitlementItems;
		}



		public List<LearnerGroupEnrollmentItem> getLearnerGroupEnrollmentItems() {
			return learnerGroupEnrollmentItems;
		}



		public List<Course> getCourseListFromDB() {
			return courseListFromDB;
		}



		public void setCourseListFromDB(List<Course> courseListFromDB) {
			this.courseListFromDB = courseListFromDB;
		}



		public String getCourseName() {
			return courseName;
		}



		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}



		public String getCourseType() {
			return courseType;
		}



		public void setCourseType(String courseType) {
			this.courseType = courseType;
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



		public String[] getGroups() {
			return groups;
		}



		public void setGroups(String[] groups) {
			this.groups = groups;
		}

		public List<Course> getCourseList() {
			return courseList;
		}

		public void setCourseList(List<Course> courseList) {
			this.courseList = courseList;
		}

			
	}
	
	
