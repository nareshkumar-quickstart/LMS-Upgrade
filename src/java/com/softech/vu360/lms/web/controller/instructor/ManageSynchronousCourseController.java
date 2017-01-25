package com.softech.vu360.lms.web.controller.instructor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.util.CustomerUtil;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Noman Ali
 * 
 */
public class ManageSynchronousCourseController extends MultiActionController
		implements InitializingBean {

	private static final Logger log = Logger
			.getLogger(ManageSynchronousCourseController.class.getName());

	private static final String MANAGE_USER_DEFAULT_ACTION = "default";
	private String[] courseTypes = { "All", "Discussion Forum",
			"SCORM Package", SynchronousCourse.COURSE_TYPE, "Weblink Course", WebinarCourse.COURSE_TYPE , 
			HomeworkAssignmentCourse.COURSE_TYPE,
			InstructorConnectCourse.COURSE_TYPE };
	private String[] courseStatuses = { "All", "Active", "Retired" };

	private String failureTemplate = null;
	private VelocityEngine velocityEngine;
	private String manageSynchronousCourseTemplate = null;
	private String editSynchronousCourseTemplate = null;
	private String loginTemplate = null;
	private String closeTemplate = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;

	private static final int SEARCH_RESULT_PAGE_SIZE = 10;
	private static final String ACTION_SEARCH_NONE = "showNone";
	private static final String ACTION_SHOW_ALL = "showAll";
	private static final String ACTION_SHOW_PREV = "showPrev";
	private static final String ACTION_SHOW_NEXT = "showNext";
	private static final String ACTION_RETIRE_COURSE = "delete";

	// [1/27/2011] LMS-7183 :: Retired Course Functionality II (Refactored Code)
	@SuppressWarnings("unchecked")
	public ModelAndView searchSynchronousCourse(HttpServletRequest request,
			HttpServletResponse response) {

		log.debug("IN searchSynchronousCourse");
		Map<Object, Object> context = new HashMap<Object, Object>();

		int totalRecords = -1, recordShowing = 0, pageIndex = 0, pageSize = SEARCH_RESULT_PAGE_SIZE, sortDirection = 0;
		String sortColumn = "NAME", courseType = "All", courseStatus = "Active";

		try {
			Customer customer = getCustomer();
			ContentOwner contentOwner = null;
			if (customer.isDistributorRepresentative()) {
				contentOwner = customer.getDistributor().getContentOwner();
			}
			if (contentOwner == null)
				contentOwner = customer.getContentOwner();

			if(!CustomerUtil.checkB2BCustomer(customer)){
				
				courseTypes = new String[7];
				courseTypes[0] = "All";
				courseTypes[1] = "Discussion Forum";
				courseTypes[2] = SynchronousCourse.COURSE_TYPE;
				courseTypes[3] = "Weblink Course";
				courseTypes[4] = HomeworkAssignmentCourse.COURSE_TYPE; //Not included in ILT Sprint I
				courseTypes[5] = InstructorConnectCourse.COURSE_TYPE; 
				courseTypes[6] = WebinarCourse.COURSE_TYPE; 
			}
			else{
				courseTypes = new String[8];
				courseTypes[0] = "All";
				courseTypes[1] = "SCORM Package";
				courseTypes[2] = "Discussion Forum";
				courseTypes[3] = SynchronousCourse.COURSE_TYPE;
				courseTypes[4] = "Weblink Course";
				courseTypes[5] = HomeworkAssignmentCourse.COURSE_TYPE; 
				courseTypes[6] = InstructorConnectCourse.COURSE_TYPE; 
				courseTypes[7] = WebinarCourse.COURSE_TYPE; 
			}
			
			
			
			// Get request parameters
			// change by K.O
			// String action =
			// StringUtils.isNotBlank(request.getParameter("action")) ?
			// request.getParameter("action") : ACTION_SEARCH_NONE;
			
			String action = null;
			
			if(request.getParameter("action") != null)
			{
				action = request.getParameter("action");
			}
			else
			{
				pageSize = 10;
				action = "search";
			}
			//action = StringUtils.isNotBlank(request.getParameter("action")) ? request.getParameter("action"): ACTION_SHOW_ALL;
			String courseTitle = request.getParameter("courseTitle");
			String courseId = request.getParameter("courseId");
			courseType = StringUtils.isNotBlank(request.getParameter("courseType")) ? request.getParameter("courseType") : courseType;
			courseStatus = StringUtils.isNotBlank(request.getParameter("courseStatus")) ? request.getParameter("courseStatus") : courseStatus;
			sortColumn = StringUtils.isNotBlank(request.getParameter("sortColumn")) ? request.getParameter("sortColumn") : sortColumn;
			sortDirection = StringUtils.isNotBlank(request.getParameter("sortDirection")) ? Integer.valueOf(request.getParameter("sortDirection")) : sortDirection;
			pageIndex = StringUtils.isNotBlank(request.getParameter("pageIndex")) ? Integer.valueOf(request.getParameter("pageIndex")) : pageIndex;
			pageIndex = (pageIndex <= 0) ? 0 : pageIndex;

			
			if (action.equalsIgnoreCase(ACTION_SEARCH_NONE)) {
				request.setAttribute("newPage", true);
			} else {
				if (action.equalsIgnoreCase(ACTION_RETIRE_COURSE)) {
					String[] selectedCourseValues = request
							.getParameterValues("selectedCourses");

					if (selectedCourseValues != null) {
						this.courseAndCourseGroupService
								.retireCourse(selectedCourseValues);
					}
				} else if (action.equalsIgnoreCase(ACTION_SHOW_ALL)) {
					pageSize = -1;
					pageIndex = 0;
				} else if (action.equalsIgnoreCase(ACTION_SHOW_PREV)) {
					pageIndex = (pageIndex <= 0) ? 0 : (pageIndex - 1);
				} else if (action.equals(ACTION_SHOW_NEXT)) {
					pageIndex = pageIndex < 0 ? 0 : (pageIndex + 1);
				}

				Map<String, Object> resultSet = null;
				
				if(!CustomerUtil.checkB2BCustomer(customer) && courseType.equalsIgnoreCase("All")){
					String[] searchedCourseTypes = { DiscussionForumCourse.COURSE_TYPE, SynchronousCourse.COURSE_TYPE, WebinarCourse.COURSE_TYPE, WebLinkCourse.COURSE_TYPE, HomeworkAssignmentCourse.COURSE_TYPE,InstructorConnectCourse.COURSE_TYPE };
					
					resultSet = this.courseAndCourseGroupService.getCourses(contentOwner.getId(), courseTitle,
								courseId, courseType, searchedCourseTypes, courseStatus, sortColumn,
								sortDirection, pageIndex, pageSize);
				}
				else{
					resultSet = this.courseAndCourseGroupService.getCourses(contentOwner.getId(), courseTitle,
							courseId, courseType, courseStatus, sortColumn,
							sortDirection, pageIndex, pageSize);
				}
				List<Course> courseList = (List<Course>) resultSet.get("list");
				totalRecords = Integer.valueOf(resultSet.get("recordSize")
						.toString());
				pageSize = (pageSize == -1) ? totalRecords : pageSize;

				if (action.equalsIgnoreCase(ACTION_SHOW_ALL)) {
					recordShowing = courseList.size();
				} else {
					recordShowing = courseList.size() < SEARCH_RESULT_PAGE_SIZE ? totalRecords
							: (pageIndex + 1) * SEARCH_RESULT_PAGE_SIZE;
				}

				
				//context.put("pageIndex", pageIndex);
				//context.put("recordShowing", recordShowing);
				//context.put("totalRecords", totalRecords);
				context.put("courseTitle", courseTitle);
				context.put("courseId", courseId);
				context.put("courseList", courseList);
			}
		} catch (Exception e) {
			log.debug("exception", e);
		}

		context.put("courseTypes", courseTypes);
		context.put("courseStatuses", courseStatuses);
		context.put("courseType", courseType);
		context.put("courseStatus", courseStatus);
		context.put("totalRecords", totalRecords);
		context.put("recordShowing", recordShowing);
		context.put("pageIndex", pageIndex);
		context.put("pageSize", pageSize);
		context.put("sortDirection", sortDirection);
		context.put("sortColumn", sortColumn);

		return new ModelAndView(manageSynchronousCourseTemplate, "context",
				context);
	}

	private Customer getCustomer() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Customer customer = null;
		if (auth.getDetails() != null && auth.getDetails() instanceof VU360UserAuthenticationDetails) {
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();
			customer = details.getCurrentCustomer();
		}
		if (customer == null) {
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			customer = loggedInUser.getLearner().getCustomer();
		}
		return customer;
	}

	/*
	 * Change name to ViewLearnerInfo
	 */

	public ModelAndView editSynchronousCourse(HttpServletRequest request,
			HttpServletResponse response) {

		// List<VU360User> resultList = new ArrayList<VU360User>();
		Course course = null;
		List<Course> courseList = new ArrayList<Course>();

		String action = request.getParameter("action");
		action = (action == null) ? MANAGE_USER_DEFAULT_ACTION : action;
		boolean customerSelected = false;
		Map<Object, Object> context = new HashMap<Object, Object>();

		Long id = new Long(request.getParameter("id"));

		Enumeration enum1 = request.getParameterNames();
		while (enum1.hasMoreElements()) {
			String paramName = enum1.nextElement().toString();
			log.debug("param name: " + paramName);
			log.debug("param value: " + request.getParameter(paramName));
		}

		course = courseAndCourseGroupService.getCourseById(id);

		log.debug("course id: " + course.getCourseId());
		log.debug("course name: " + course.getCourseTitle());
		log.debug("course description: " + course.getDescription());

		context.put("course", course);

		return new ModelAndView(editSynchronousCourseTemplate, "context",
				context);
	}

	@SuppressWarnings("unchecked")
	private boolean validateData(HttpServletRequest request, Map context,
			Course course) {

		boolean check = false;

		if (request.getParameter("action") != null
				&& request.getParameter("action").equalsIgnoreCase("publish")) {
			List<CourseGroup> courseGroups = courseAndCourseGroupService
					.getCourseGroupsForCourse(course);

			if (courseGroups == null || courseGroups.size() <= 0) {
				context.put("validateCoursePublishing",
						"error.publishSynchronousCourse.courseNotInCourseGroup");
				check = true;
			}
		} else {
			if (StringUtils.isBlank(request.getParameter("courseTitle"))) {
				context.put("validateCourseName",
						"error.addNewSynchrounousCourse.courseName.required");
				course.setCourseTitle(null);
				check = true;
			} else if (FieldsValidation.isInValidGlobalName(request
					.getParameter("courseTitle"))) {
				context.put("validateCourseName",
						"error.addNewSynchrounousCourse.courseName.all.invalidText");
				course.setCourseTitle(null);
				check = true;
			} else {
				course.setCourseTitle(request.getParameter("courseTitle"));
			}

			if (StringUtils.isBlank(request.getParameter("courseId"))) {
				context.put("validateCourseID",
						"error.addNewSynchrounousCourse.courseID.required");
				course.setCourseId(null);
				check = true;
			} else if (FieldsValidation.isInValidGlobalName(request
					.getParameter("courseId"))) {
				context.put("validateCourseID",
						"error.addNewSynchrounousCourse.courseID.all.invalidText");
				course.setCourseId(null);
				check = true;
			} else {
				course.setCourseId(request.getParameter("courseId"));
			}

			if (StringUtils.isBlank(request.getParameter("description"))) {
				context.put("validateDescription",
						"error.addNewSynchrounousCourse.description.required");
				course.setDescription(null);
				check = true;
			} else if (FieldsValidation.isInValidGlobalName(request
					.getParameter("description"))) {
				context.put("validateDescription",
						"error.addNewSynchrounousCourse.description.all.invalidText");
				course.setDescription(null);
				check = true;
			} else {
				course.setDescription(request.getParameter("description"));
			}
		}
		return check;
	}

	@SuppressWarnings("unchecked")
	public ModelAndView updateSynchronousCourse(HttpServletRequest request,
			HttpServletResponse response) {
		Map context = new HashMap();
		Course course = null;
		Long id = new Long(request.getParameter("id"));

		course = courseAndCourseGroupService.loadForUpdateCourse(id);

		if (request.getParameter("action") != null
				&& request.getParameter("action").equalsIgnoreCase("publish")) {

			if (!validateData(request, context, course)) {
				String distributorCode = getCustomer().getDistributor().getDistributorCode();
				if (publishSynchronousCourse(course, distributorCode)) {

					course.setPublishedonstorefront(true);
					course.setCourseStatus(Course.PUBLISHED);
					courseAndCourseGroupService.saveCourse(course);
				} else {
					context.put("publishingError",
							"error.publishSynchronousCourse.publishingError");
					context.put("course", course);
					return new ModelAndView(editSynchronousCourseTemplate,
							"context", context);

				}
			} else {
				context.put("course", course);
				return new ModelAndView(editSynchronousCourseTemplate,
						"context", context);
			}

			context.put("totalRecord", 0);
			context.put("recordShowing", 0);
			context.put("pageNo", 0);
			context.put("sortDirection", 0);
			return new ModelAndView(manageSynchronousCourseTemplate, "context",
					context);

		}

		if (!validateData(request, context, course)) {
			/* everthing is fine lets update it and return to main screen */
			courseAndCourseGroupService.saveCourse(course);
			context.put("totalRecord", 0);
			context.put("recordShowing", 0);
			context.put("pageNo", 0);
			context.put("sortDirection", 0);
			return new ModelAndView(manageSynchronousCourseTemplate, "context",
					context);
		} else {
			/*
			 * some of the value are missing so prompt the user and get the
			 * values in required input fields
			 */
			context.put("course", course);
			return new ModelAndView(editSynchronousCourseTemplate, "context",
					context);
		}

	}

	/*
	 * getter & setters.
	 */

	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}

	/**
	 * @return the loginTemplate
	 */
	public String getLoginTemplate() {
		return loginTemplate;
	}

	/**
	 * @param loginTemplate
	 *            the loginTemplate to set
	 */
	public void setLoginTemplate(String loginTemplate) {
		this.loginTemplate = loginTemplate;
	}

	/*
	 * public String getManagerTemplate() { return managerTemplate; }
	 * 
	 * public void setManagerTemplate(String managerTemplate) {
	 * this.managerTemplate = managerTemplate; }
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * @return the redirectToSearchPageTemplate
	 */
	/*
	 * public String getRedirectToSearchPageTemplate() { return
	 * redirectToSearchPageTemplate; }
	 */
	/**
	 * @param redirectToSearchPageTemplate
	 *            the redirectToSearchPageTemplate to set
	 */
	/*
	 * public void setRedirectToSearchPageTemplate(String
	 * redirectToSearchPageTemplate) { this.redirectToSearchPageTemplate =
	 * redirectToSearchPageTemplate; }
	 */
	/**
	 * @return the failureTemplate
	 */
	public String getFailureTemplate() {
		return failureTemplate;
	}

	/**
	 * @param failureTemplate
	 *            the failureTemplate to set
	 */
	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}

	public String getManageSynchronousCourseTemplate() {
		return manageSynchronousCourseTemplate;
	}

	public void setManageSynchronousCourseTemplate(
			String manageSynchronousCourseTemplate) {
		this.manageSynchronousCourseTemplate = manageSynchronousCourseTemplate;
	}

	/*
	 * public SynchronousClassService getSynchronousClassService() { return
	 * synchronousClassService; }
	 * 
	 * public void setSynchronousClassService( SynchronousClassService
	 * synchronousClassService) { this.synchronousClassService =
	 * synchronousClassService; }
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public String getEditSynchronousCourseTemplate() {
		return editSynchronousCourseTemplate;
	}

	public void setEditSynchronousCourseTemplate(
			String editSynchronousCourseTemplate) {
		this.editSynchronousCourseTemplate = editSynchronousCourseTemplate;
	}

	private boolean publishSynchronousCourse(Course course, String distributorCode) {
		List<CourseGroup> courseGroups = courseAndCourseGroupService
				.getCourseGroupsForCourse(course);
		return new StorefrontClientWSImpl().publishCourseEvent(course,
				courseGroups, distributorCode);

	}

}