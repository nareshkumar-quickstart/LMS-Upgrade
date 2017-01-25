package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.TreeNode;

public class ApprovalCourseForm implements ILMSBaseInterface{

	public static final String INSTRUCTOR_APPROVAL = "Instructor";
	public static final String COURSE_APPROVAL = "Course";
	
	private static final long serialVersionUID = 1L;
	private Course course = new Course();
	
	private String entity = null;
	private InstructorApproval instructorApproval = null;
	private CourseApproval courseApproval = null;
	private List<Course> courses = new ArrayList<Course>();
	private CredentialCategory category;
	private String courseName = null;
	private String courseId = null;
	
	private String courseGroupId="";
	List<List<TreeNode>> courseGroupTreeList = new ArrayList<List<TreeNode>>();
	List<CourseGroup> lstCourseGroups =new ArrayList<CourseGroup>();
	
	private String selectedCourseId = null;
	 //following variable use to hold course id that user select before the current selected course Id. This variable help to in fixing of LMS-15916
	private String previouslySelectedCourseId="";
	
	// for pagination
	private String pageIndex;
	private String showAll = "false";
	private String pageCurrIndex;
	
	// for sorting
	private String sortColumnIndex;
	private String sortDirection;
	//private String showAll;
	
	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}
	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
	}
	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
	/**
	 * @return the instructorApproval
	 */
	public InstructorApproval getInstructorApproval() {
		return instructorApproval;
	}
	/**
	 * @param instructorApproval the instructorApproval to set
	 */
	public void setInstructorApproval(InstructorApproval instructorApproval) {
		this.instructorApproval = instructorApproval;
	}
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * @return the courseId
	 */
	public String getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	/**
	 * @return the courses
	 */
	public List<Course> getCourses() {
		return courses;
	}
	/**
	 * @param courses the courses to set
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	/**
	 * @return the selectedCourseId
	 */
	public String getSelectedCourseId() {
		return selectedCourseId;
	}
	/**
	 * @param selectedCourseId the selectedCourseId to set
	 */
	public void setSelectedCourseId(String selectedCourseId) {
		this.selectedCourseId = selectedCourseId;
	}
	/**
	 * @return the courseApproval
	 */
	public CourseApproval getCourseApproval() {
		return courseApproval;
	}
	/**
	 * @param courseApproval the courseApproval to set
	 */
	public void setCourseApproval(CourseApproval courseApproval) {
		this.courseApproval = courseApproval;
	}
	/**
	 * @return the pageIndex
	 */
	public String getPageIndex() {
		return pageIndex;
	}
	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	/**
	 * @return the showAll
	 */
	public String getShowAll() {
		return showAll;
	}
	/**
	 * @param showAll the showAll to set
	 */
	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}
	/**
	 * @return the pageCurrIndex
	 */
	public String getPageCurrIndex() {
		return pageCurrIndex;
	}
	/**
	 * @param pageCurrIndex the pageCurrIndex to set
	 */
	public void setPageCurrIndex(String pageCurrIndex) {
		this.pageCurrIndex = pageCurrIndex;
	}
	/**
	 * @return the sortColumnIndex
	 */
	public String getSortColumnIndex() {
		return sortColumnIndex;
	}
	/**
	 * @param sortColumnIndex the sortColumnIndex to set
	 */
	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}
	/**
	 * @return the sortDirection
	 */
	public String getSortDirection() {
		return sortDirection;
	}
	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	
	public CredentialCategory getCategory() {
		return category;
	}
	public void setCategory(CredentialCategory category) {
		this.category = category;
	}
	public String getCourseGroupId() {
		return courseGroupId;
	}
	public void setCourseGroupId(String courseGroupId) {
		this.courseGroupId = courseGroupId;
	}

	public List<List<TreeNode>> getCourseGroupTreeList() {
		return courseGroupTreeList;
	}
	public void setCourseGroupTreeList(List<List<TreeNode>> courseGroupTreeList) {
		this.courseGroupTreeList = courseGroupTreeList;
	}
	public List<CourseGroup> getLstCourseGroups() {
		return lstCourseGroups;
	}
	public void setLstCourseGroups(List<CourseGroup> lstCourseGroups) {
		this.lstCourseGroups = lstCourseGroups;
	}
	public String getPreviouslySelectedCourseId() {
		return previouslySelectedCourseId;
	}
	public void setPreviouslySelectedCourseId(String previouslySelectedCourseId) {
		this.previouslySelectedCourseId = previouslySelectedCourseId;
	}
}
