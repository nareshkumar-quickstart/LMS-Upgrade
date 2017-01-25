package com.softech.vu360.lms.web.controller.instructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.model.MyWebinarPlace;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.TimeZoneService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Noman Ali
 * 
 */
public class ManageSynchronousClassScheduleController extends
		MultiActionController implements InitializingBean {

	private static final Logger log = Logger
			.getLogger(ManageSynchronousClassScheduleController.class.getName());

	private static final String ZERO_TIME_AM = "00:00 AM";
	private static final String ZERO_TIME_PM = "00:00 PM";

	private static final String MANAGE_USER_DEFAULT_ACTION = "default";
	private static final String MANAGE_USER_ALL_SEARCH_ACTION = "allSearch";
	private static final String MANAGE_USER_PREVPAGE_DIRECTION = "prev";
	private static final String MANAGE_USER_NEXTPAGE_DIRECTION = "next";
	private static final int MANAGE_USER_PAGE_SIZE = 10;
	private static final String MANAGE_SYNCLASS_ADVANCED_SEARCH_ACTION = "advanceSearch";
	private static final String MANAGE_SYNCHRONOUSCLASS_DELETE_ACTION = "delete";

	private static final String MANAGE_SYNCHRONOUSCLASS_SESSION_DELETE_ACTION = "deleteSessions";
	private static final String MANAGE_SYNCHRONOUSCLASS_SESSION_EDIT_ACTION = "editSession";
	private static final String MANAGE_SYNCHRONOUSCLASS_SESSION_UPDATE_ACTION = "updateSession";

	private static final String MANAGE_SYNCLASS_ADVANCED_INSTRUCTOR_SEARCH_ACTION = "advanceInstructorSearch";
	private static final String MANAGE_SYNCHRONOUSCLASS_INSTRUCTOR_DELETE_ACTION = "deleteInstructor";
	private static final String MANAGE_SYNCHRONOUSCLASS_INSTRUCTORROLE_EDIT_ACTION = "editInstructorRole";
	private static final String MANAGE_SYNCHRONOUSCLASS_INSTRUCTORROLE_UPDATE_ACTION = "updateInstructorRole";

	private static final String MANAGE_COURSE_SORT_ACTION = "sort";
	private static final String MANAGE_CLASSSCHEDULE_SORT_SECTIONNAME = "sectionName";
	private static final String MANAGE_CLASSSCHEDULE_SORT_CLASSSTARTDATE = "classStartDate";
	private static final String MANAGE_CLASSSCHEDULE_SORT_CLASSENDDATE = "classEndDate";
	/*
	 * private static final String MANAGE_USER_SIMPLE_SEARCH_ACTION =
	 * "simpleSearch"; private static final String EDIT_SYNCHCLASS_UPDATE_ACTION
	 * = "update"; private static final String EDIT_USER_CHANGEGROUP_SAVE =
	 * "save";
	 */

	private String failureTemplate = null;
	private VelocityEngine velocityEngine;

	private String manageSynchronousClassTemplate = null;
	private String editSynchronousClassTemplate = null;

	private String editSynchronousClassSessionTemplate = null;

	private String editSynchronousClassScheduleTemplate = null;
	private String editSynchronousClassInstructorTemplate = null;
	private String editSynchronousClassInstructorRoleTemplate = null;

	private String loginTemplate = null;
	private String closeTemplate = null;
	private String instructorCourseType = null;
	

	private SynchronousClassService synchronousClassService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private TimeZoneService timeZoneService = null;

	@SuppressWarnings("unchecked")
	public ModelAndView searchSynchronousClass(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// List<VU360User> resultList = new ArrayList<VU360User>();
			Map<Object, Object> results = new HashMap<Object, Object>();
			List<Course> courseList = new ArrayList<Course>();

			this.instructorCourseType = request.getParameter("instructorCourseType");
			String error = request.getParameter("error");
			error = (error == null) ? null : error;
			String action = request.getParameter("action");
			action = (action == null) ? MANAGE_USER_DEFAULT_ACTION : action;
			Map<Object, Object> context = new HashMap<Object, Object>();

			String courseTitle = null;
			String courseId = null;
			String courseType = request.getParameter("courseType");
			

			String searchKey = request.getParameter("searchkey");

			String direction = request.getParameter("direction");
			String pageIndex = request.getParameter("pageIndex");
			String sortDirection = request.getParameter("sortDirection");
			String prevSortDirection = "";
			String sortBy = request.getParameter("sortBy");

			log.debug("First sortDirection  " + sortDirection);
			int pageNo = 0;
			int recordShowing = 0;

			searchKey = (searchKey == null) ? "" : searchKey;
			if (sortDirection == null)
				sortDirection = "1";

			HttpSession session = request.getSession();

			direction = (direction == null) ? MANAGE_USER_PREVPAGE_DIRECTION
					: direction;
			pageIndex = (pageIndex == null) ? "0" : pageIndex;
			sortDirection = (sortDirection == null) ? "0" : sortDirection;
			sortBy = (sortBy == null) ? MANAGE_CLASSSCHEDULE_SORT_SECTIONNAME
					: sortBy;

			session.setAttribute("sortBy", sortBy);
			session.setAttribute("direction", direction);
			session.setAttribute("sortDirection", sortDirection);
			log.debug("2nd sortDirection  " + sortDirection);

			if (request.getAttribute("prevSortDirection") != null)
				prevSortDirection = request.getAttribute("prevSortDirection")
						.toString();
			else
				prevSortDirection = "0";

			if (action.equalsIgnoreCase(MANAGE_SYNCLASS_ADVANCED_SEARCH_ACTION)) {
				session.setAttribute("searchType",
						MANAGE_SYNCLASS_ADVANCED_SEARCH_ACTION);
				context = movePage(request, response);
				return new ModelAndView(manageSynchronousClassTemplate,
						"context", context);

			} else if (action.equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION)) {
				session.setAttribute("searchType",
						MANAGE_USER_ALL_SEARCH_ACTION);
				log.debug("searchType " + session.getAttribute("searchType"));
				pageNo = 0;
				session.setAttribute("pageNo", pageNo);
				context = showAll(request, response);
				return new ModelAndView(manageSynchronousClassTemplate,
						"context", context);

			} else if (action.equalsIgnoreCase(MANAGE_COURSE_SORT_ACTION)) {
				log.debug("searchType " + session.getAttribute("searchType"));

				context = sortPage(request, response);
				return new ModelAndView(manageSynchronousClassTemplate,
						"context", context);

			} else if (action
					.equalsIgnoreCase(MANAGE_SYNCHRONOUSCLASS_DELETE_ACTION)) {

				String[] selectedClassValues = request
						.getParameterValues("selectedClasses");
				boolean isInProgress = false;
				boolean isActiveEnrollmentsPresent = false;

				if (selectedClassValues != null) {

					for (int loop = 0; loop < selectedClassValues.length; loop++) {
						String delClassID = selectedClassValues[loop];
						for (SynchronousSession classSession : synchronousClassService
								.getAllClassSessionsOfSynchClass(Long
										.parseLong(delClassID))) {
							if (classSession.getStatus().equalsIgnoreCase(
									SynchronousSession.INPROGRESS)) {

								isInProgress = true;
								break;
							}
						}

						if (synchronousClassService
								.getEnrollmentCountBySynchronousClassId(Long
										.parseLong(delClassID)) > 0)
							isActiveEnrollmentsPresent = true;

					}

				}
				if (!isInProgress && !isActiveEnrollmentsPresent) {

					manageSynchClassDelete(request, response);
				}
				context = manageSynchClassDefault(request, response);
				if (isInProgress) {
					context.put("inProgressError",
							"lms.instructor.editCourse.schedule.delete.activeEnrollment.message");
					context.put("instructorCourseType", this.instructorCourseType);
				} else if (isActiveEnrollmentsPresent) {
					context.put("activeEnrollmentsError",
							"lms.instructor.editCourse.schedule.delete.activeEnrollment.message");
					context.put("instructorCourseType", this.instructorCourseType);
				}
				return new ModelAndView(manageSynchronousClassTemplate,
						"context", context);
			} else if (action.equalsIgnoreCase(MANAGE_USER_DEFAULT_ACTION)) {
				context = manageSynchClassDefault(request, response);
				context.put("instructorCourseType", this.instructorCourseType);
				return new ModelAndView(manageSynchronousClassTemplate,
						"context", context);

			}

			log.debug("before record showing " + results.isEmpty());
			if (!results.isEmpty())
				recordShowing = ((Integer) courseList.size() < MANAGE_USER_PAGE_SIZE) ? Integer
						.parseInt(results.get("recordSize").toString())
						: (Integer.parseInt(session.getAttribute("pageNo")
								.toString()) + 1) * MANAGE_USER_PAGE_SIZE;
			// sortDirection = (sortDirection =="0")?"1":"0";

			log.debug("after record showing " + results.isEmpty());
			if (session.getAttribute("searchType").toString()
					.equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION))
				recordShowing = courseList.size();

			String totalRecord = (results.isEmpty()) ? "-1" : results.get(
					"recordSize").toString();
			log.debug("sortDirection  " + sortDirection);
			log.debug("searchtype   " + session.getAttribute("searchType"));
			context.put("searchKey", session.getAttribute("searchedSearchKey"));
			context.put("searchType", session.getAttribute("searchType"));
			context.put("direction", direction);
			context.put("totalRecord", Integer.parseInt(totalRecord));
			context.put("recordShowing", recordShowing);
			context.put("pageNo", session.getAttribute("pageNo"));
			context.put("sortDirection", sortDirection);
			context.put("sortBy", sortBy);
			context.put("instructorCourseType", this.instructorCourseType);

			return new ModelAndView(manageSynchronousClassTemplate, "context",
					context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(manageSynchronousClassTemplate);
	}

	private void manageSynchClassDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String[] selectedClassValues = request
				.getParameterValues("selectedClasses");

		if (selectedClassValues != null) {
			Long classIdArray[] = new Long[selectedClassValues.length];
			for (int loop = 0; loop < selectedClassValues.length; loop++) {
				String delClassID = selectedClassValues[loop];
				if (delClassID != null)
					classIdArray[loop] = Long.parseLong(delClassID);
			}
			
			// If Synchronous Class has MyWebinarPlace as online option, then delete them
			for(Long id : classIdArray) {
				SynchronousClass syncClass = synchronousClassService.getSynchronousClassById(id);
				if(syncClass.isOnlineMeeting() && syncClass.getMeetingType().equals(SynchronousClass.MEETINGTYPE_WEBINAR)) {
					synchronousClassService.deleteMyWebinarPlaceMeeting(syncClass.getMeetingID());
				}	
			}
			
			/*
			 * this will delete courses and all the records from dependant
			 * tables like SynchronousClass table, Instructor_SynchrounosClass
			 * table, SynchrounsouSession table,
			 */
			synchronousClassService.deleteSynchronousClass(classIdArray);
			
			
			
		}

	}

	private void manageSynchClassSessionDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String[] selectedSessionValues = request
				.getParameterValues("selectedSessions");

		if (selectedSessionValues != null) {
			Long sessionIdArray[] = new Long[selectedSessionValues.length];
			for (int loop = 0; loop < selectedSessionValues.length; loop++) {
				String delClassSessionID = selectedSessionValues[loop];
				if (delClassSessionID != null)
					sessionIdArray[loop] = Long.valueOf(delClassSessionID);
			}
			/*
			 * this will delete sessions from SynchrounsouSession table,
			 */
			synchronousClassService.deleteSynchronousClassSession(sessionIdArray);			
		}

	}

	private void manageSynchClassInstructorDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String[] selectedInstructorIds = request
				.getParameterValues("selectedInstructors");

		if (selectedInstructorIds != null) {
			long synchronousClassInstructorIdArray[] = new long[selectedInstructorIds.length];
			for (int loop = 0; loop < selectedInstructorIds.length; loop++) {
				String delClassID = selectedInstructorIds[loop];
				if (delClassID != null)
					synchronousClassInstructorIdArray[loop] = Long
							.parseLong(delClassID);
			}
			/*
			 * this will delete only the mapping entries from
			 * instructor_synchronousclass Instructor_SynchrounosClass table,
			 * SynchrounsouSession table,
			 */
			synchronousClassService
					.deleteSynchronousClassInstructor(synchronousClassInstructorIdArray);
		}

	}

	private Map manageSynchClassDefault(HttpServletRequest request,
			HttpServletResponse response) {

		Brander brander = VU360Branding.getInstance().getBrander(
				(String) request.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());
		
		String showAllLimit = brander
				.getBrandElement("lms.resultSet.showAll.Limit");
		int intShowAllLimit = Integer.parseInt(showAllLimit.trim());

		Map<Object, Object> results = new HashMap<Object, Object>();
		Map<Object, Object> context = new HashMap<Object, Object>();
		int recordShowing = 0;
		String direction = request.getParameter("direction");
		String pageIndex = request.getParameter("pageIndex");
		String sortDirection = request.getParameter("sortDirection");
		String sortBy = request.getParameter("sortBy");

		if (sortDirection == null)
			sortDirection = "1";
		if (pageIndex == null)
			pageIndex = "0";

		sortBy = (sortBy == null) ? evaluateSortColumn("1")
				: evaluateSortColumn(sortBy);

		HttpSession session = request.getSession();
		session.setAttribute("pageNo", 0);
		session.setAttribute("searchType", "");
		int intSortDirection = Integer.parseInt(sortDirection);
		int intPageIndex = Integer.parseInt(pageIndex);

		Long id = new Long(request.getParameter("id"));
		String courseType = request.getParameter("courseType");
		String searchedclassName = (String) request.getParameter("className");
		String searchedstartDate = (String) request.getParameter("startDate");
		String searchedendDate = (String) request.getParameter("endDate");

		session.setAttribute("searchedclassName", searchedclassName);
		session.setAttribute("searchedstartDate", searchedstartDate);
		session.setAttribute("searchedendDate", searchedendDate);

		results = synchronousClassService.findClassesByCourseId(id,
				searchedclassName, searchedstartDate, searchedendDate,
				intPageIndex, MANAGE_USER_PAGE_SIZE, sortBy, intSortDirection,
				intShowAllLimit);
		Course course = courseAndCourseGroupService.getCourseById(id);

		List<SynchronousClass> synchronousClassList = new ArrayList<SynchronousClass>();
		synchronousClassList = (List<SynchronousClass>) results.get("list");

		/*
		 * take classname as a string to pass it to the add schedule page for
		 * showing user default values as coursename and classname
		 */
		String className = "";
		if (synchronousClassList.size() > 0) {
			className = synchronousClassList.get(0).getSectionName();
		}

		log.debug("before record showing " + results.isEmpty());
		if (!results.isEmpty())
			recordShowing = ((Integer) synchronousClassList.size() < MANAGE_USER_PAGE_SIZE) ? Integer
					.parseInt(results.get("recordSize").toString()) : (Integer
					.parseInt(session.getAttribute("pageNo").toString()) + 1)
					* MANAGE_USER_PAGE_SIZE;
		// sortDirection = (sortDirection =="0")?"1":"0";

		log.debug("after record showing " + results.isEmpty());
		if (session.getAttribute("searchType").toString()
				.equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION))
			recordShowing = synchronousClassList.size();

		String totalRecord = (results.isEmpty()) ? "-1" : results.get(
				"recordSize").toString();

		if (sortDirection.equalsIgnoreCase("1"))
			sortDirection = "0";
		else
			sortDirection = "1";

		context.put("searchKey", session.getAttribute("searchedSearchKey"));
		context.put("searchType", session.getAttribute("searchType"));
		context.put("direction", direction);
		context.put("classes", synchronousClassList);
		context.put("className", className);
		context.put("courseName", course.getCourseTitle());
		session.setAttribute("sync_CourseTitle", course.getCourseTitle());
		context.put("totalRecord", Integer.parseInt(totalRecord));
		context.put("recordShowing", recordShowing);
		context.put("pageNo", session.getAttribute("pageNo"));
		context.put("sortDirection", sortDirection);
		context.put("sortBy", sortBy);
		context.put("id", id);
		context.put("courseType", courseType);

		return context;
	}

	private Map showAll(HttpServletRequest request, HttpServletResponse response) {

		Brander brander = VU360Branding.getInstance().getBrander(
				(String) request.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());

		String showAllLimit = brander
				.getBrandElement("lms.resultSet.showAll.Limit");
		int intShowAllLimit = Integer.parseInt(showAllLimit.trim());

		Map<Object, Object> results = new HashMap<Object, Object>();
		Map<Object, Object> context = new HashMap<Object, Object>();
		int recordShowing = 0;
		String direction = request.getParameter("direction");
		String pageIndex = request.getParameter("pageIndex");
		String sortDirection = request.getParameter("sortDirection");
		String sortBy = request.getParameter("sortBy");

		if (sortDirection == null)
			sortDirection = "0";
		if (pageIndex == null)
			pageIndex = "0";

		sortBy = (sortBy == null) ? evaluateSortColumn("1")
				: evaluateSortColumn(sortBy);

		HttpSession session = request.getSession();

		// session.setAttribute("searchType","");
		int intSortDirection = Integer.parseInt(sortDirection);
		int intPageIndex = Integer.parseInt(pageIndex);

		Long id = new Long(request.getParameter("id"));
		String courseType = request.getParameter("courseType");

		String searchedclassName = null;
		String searchedstartDate = null;
		String searchedendDate = null;

		if (request.getParameter("className") == null
				&& session.getAttribute("searchedclassName") != null)
			searchedclassName = session.getAttribute("searchedclassName")
					.toString();

		if (request.getParameter("startDate") == null
				&& session.getAttribute("searchedstartDate") != null)
			searchedstartDate = session.getAttribute("searchedstartDate")
					.toString();

		if (request.getParameter("endDate") == null
				&& session.getAttribute("searchedendDate") != null)
			searchedendDate = session.getAttribute("searchedendDate")
					.toString();

		results = synchronousClassService.findAllClassesByCourseId(id,
				searchedclassName, searchedstartDate, searchedendDate, sortBy,
				intSortDirection, intShowAllLimit);
		Course course = courseAndCourseGroupService.getCourseById(id);

		List<SynchronousClass> synchronousClassList = new ArrayList<SynchronousClass>();
		synchronousClassList = (List<SynchronousClass>) results.get("list");

		/*
		 * take classname as a string to pass it to the add schedule page for
		 * showing user default values as coursename and classname
		 */
		String className = "";
		if (synchronousClassList.size() > 0) {
			className = synchronousClassList.get(0).getSectionName();
		}

		log.debug("before record showing " + results.isEmpty());
		if (!results.isEmpty())
			recordShowing = ((Integer) synchronousClassList.size() < MANAGE_USER_PAGE_SIZE) ? Integer
					.parseInt(results.get("recordSize").toString()) : (Integer
					.parseInt(session.getAttribute("pageNo").toString()) + 1)
					* MANAGE_USER_PAGE_SIZE;
		// sortDirection = (sortDirection =="0")?"1":"0";

		log.debug("after record showing " + results.isEmpty());
		if (session.getAttribute("searchType").toString()
				.equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION))
			recordShowing = synchronousClassList.size();

		String totalRecord = (results.isEmpty()) ? "-1" : results.get(
				"recordSize").toString();

		context.put("searchKey", session.getAttribute("searchedSearchKey"));
		context.put("searchType", session.getAttribute("searchType"));
		context.put("direction", direction);
		context.put("classes", synchronousClassList);
		context.put("className", className);
		context.put("courseName", course.getCourseTitle());
		context.put("totalRecord", Integer.parseInt(totalRecord));
		context.put("recordShowing", recordShowing);
		context.put("pageNo", session.getAttribute("pageNo"));
		context.put("sortDirection", sortDirection);
		context.put("sortBy", sortBy);
		context.put("id", id);
		context.put("courseType", courseType);
		context.put("showAll", 1);

		return context;
	}

	private Map movePage(HttpServletRequest request,
			HttpServletResponse response) {

		Brander brander = VU360Branding.getInstance().getBrander(
				(String) request.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());

		String showAllLimit = brander
				.getBrandElement("lms.resultSet.showAll.Limit");
		int intShowAllLimit = Integer.parseInt(showAllLimit.trim());

		Map<Object, Object> results = new HashMap<Object, Object>();
		Map<Object, Object> context = new HashMap<Object, Object>();
		int recordShowing = 0;
		String direction = request.getParameter("direction");
		String pageIndex = request.getParameter("pageIndex");
		String sortDirection = request.getParameter("sortDirection");
		String sortBy = request.getParameter("sortBy");

		if (sortDirection == null)
			sortDirection = "0";
		if (pageIndex == null)
			pageIndex = "0";

		sortBy = (sortBy == null) ? evaluateSortColumn("1")
				: evaluateSortColumn(sortBy);

		HttpSession session = request.getSession();

		// session.setAttribute("searchType","");
		int intSortDirection = Integer.parseInt(sortDirection);
		int intPageIndex = Integer.parseInt(pageIndex);

		intPageIndex = this.evaluatePageIndex(intPageIndex, direction); // deciding
																		// for
																		// next
																		// or
																		// previous
																		// direction
		session.setAttribute("pageNo", intPageIndex);
		String courseId = null;
		String courseType = null;

		if (request.getParameter("id") == null)
			courseId = "0";
		else
			courseId = request.getParameter("id");

		courseType = request.getParameter("courseType");

		Long id = new Long(courseId);

		String searchedclassName = null;
		String searchedstartDate = null;
		String searchedendDate = null;

		if (request.getParameter("className") == null
				&& session.getAttribute("searchedclassName") != null)
			searchedclassName = session.getAttribute("searchedclassName")
					.toString();

		if (request.getParameter("startDate") == null
				&& session.getAttribute("searchedstartDate") != null)
			searchedstartDate = session.getAttribute("searchedstartDate")
					.toString();

		if (request.getParameter("endDate") == null
				&& session.getAttribute("searchedendDate") != null)
			searchedendDate = session.getAttribute("searchedendDate")
					.toString();

		results = synchronousClassService.findClassesByCourseId(id,
				searchedclassName, searchedstartDate, searchedendDate,
				intPageIndex, MANAGE_USER_PAGE_SIZE, sortBy, intSortDirection,
				intShowAllLimit);
		Course course = courseAndCourseGroupService.getCourseById(id);

		List<SynchronousClass> synchronousClassList = new ArrayList<SynchronousClass>();
		synchronousClassList = (List<SynchronousClass>) results.get("list");

		/*
		 * take classname as a string to pass it to the add schedule page for
		 * showing user default values as coursename and classname
		 */
		String className = "";
		if (synchronousClassList.size() > 0) {
			className = synchronousClassList.get(0).getSectionName();
		}

		log.debug("before record showing " + results.isEmpty());
		if (!results.isEmpty())
			recordShowing = ((Integer) synchronousClassList.size() < MANAGE_USER_PAGE_SIZE) ? Integer
					.parseInt(results.get("recordSize").toString()) : (Integer
					.parseInt(session.getAttribute("pageNo").toString()) + 1)
					* MANAGE_USER_PAGE_SIZE;
		// sortDirection = (sortDirection =="0")?"1":"0";

		log.debug("after record showing " + results.isEmpty());
		if (session.getAttribute("searchType").toString()
				.equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION))
			recordShowing = synchronousClassList.size();

		String totalRecord = (results.isEmpty()) ? "-1" : results.get(
				"recordSize").toString();

		context.put("searchKey", session.getAttribute("searchedSearchKey"));
		context.put("searchType", session.getAttribute("searchType"));
		context.put("direction", direction);
		context.put("classes", synchronousClassList);
		context.put("className", className);
		context.put("courseName", course.getCourseTitle());
		context.put("totalRecord", Integer.parseInt(totalRecord));
		context.put("recordShowing", recordShowing);
		context.put("pageNo", session.getAttribute("pageNo"));
		context.put("sortDirection", sortDirection);
		context.put("sortBy", sortBy);
		context.put("id", id);
		context.put("courseType", courseType);
		return context;
	}

	private Map sortPage(HttpServletRequest request,
			HttpServletResponse response) {
		
		Brander brander = VU360Branding.getInstance().getBrander(
				(String) request.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());

		String showAllLimit = brander
				.getBrandElement("lms.resultSet.showAll.Limit");
		int intShowAllLimit = Integer.parseInt(showAllLimit.trim());

		Map<Object, Object> results = new HashMap<Object, Object>();
		Map<Object, Object> context = new HashMap<Object, Object>();
		int recordShowing = 0;
		String direction = request.getParameter("direction");
		String pageIndex = request.getParameter("pageIndex");
		String sortDirection = request.getParameter("sortDirection");
		String sortBy = request.getParameter("sortBy");

		if (sortDirection == null)
			sortDirection = "1";
		if (pageIndex == null)
			pageIndex = "0";

		sortBy = (sortBy == null) ? evaluateSortColumn("1")
				: evaluateSortColumn(sortBy);

		HttpSession session = request.getSession();

		// session.setAttribute("searchType","");
		int intSortDirection = Integer.parseInt(sortDirection);
		int intPageIndex = Integer.parseInt(pageIndex);

		intPageIndex = this.evaluatePageIndex(intPageIndex, direction); // deciding
																		// for
																		// next
																		// or
																		// previous
																		// direction
		session.setAttribute("pageNo", intPageIndex);
		String courseId = null;

		if (request.getParameter("id") == null)
			courseId = "0";
		else
			courseId = request.getParameter("id");

		Long id = new Long(courseId);
		String courseType = request.getParameter("courseType");
		String searchedclassName = null;
		String searchedstartDate = null;
		String searchedendDate = null;

		if (request.getParameter("className") == null
				&& session.getAttribute("searchedclassName") != null)
			searchedclassName = session.getAttribute("searchedclassName")
					.toString();

		if (request.getParameter("startDate") == null
				&& session.getAttribute("searchedstartDate") != null)
			searchedstartDate = session.getAttribute("searchedstartDate")
					.toString();

		if (request.getParameter("endDate") == null
				&& session.getAttribute("searchedendDate") != null)
			searchedendDate = session.getAttribute("searchedendDate")
					.toString();

		if (request.getParameter("showAll") != null) {
			if (request.getParameter("showAll").toString()
					.equalsIgnoreCase("1")) {

				context.put("showAll", 1);

				results = synchronousClassService.findAllClassesByCourseId(id,
						searchedclassName, searchedstartDate, searchedendDate,
						sortBy, intSortDirection, intShowAllLimit);
			} else {
				results = synchronousClassService.findClassesByCourseId(id,
						searchedclassName, searchedstartDate, searchedendDate,
						intPageIndex, MANAGE_USER_PAGE_SIZE, sortBy,
						intSortDirection, intShowAllLimit);

			}
		} else {
			results = synchronousClassService.findClassesByCourseId(id,
					searchedclassName, searchedstartDate, searchedendDate,
					intPageIndex, MANAGE_USER_PAGE_SIZE, sortBy,
					intSortDirection, intShowAllLimit);

		}
		Course course = courseAndCourseGroupService.getCourseById(id);

		List<SynchronousClass> synchronousClassList = new ArrayList<SynchronousClass>();
		synchronousClassList = (List<SynchronousClass>) results.get("list");

		/*
		 * take classname as a string to pass it to the add schedule page for
		 * showing user default values as coursename and classname
		 */
		String className = "";
		if (synchronousClassList.size() > 0) {
			className = synchronousClassList.get(0).getSectionName();
		}

		log.debug("before record showing " + results.isEmpty());
		if (!results.isEmpty())
			recordShowing = ((Integer) synchronousClassList.size() < MANAGE_USER_PAGE_SIZE) ? Integer
					.parseInt(results.get("recordSize").toString()) : (Integer
					.parseInt(session.getAttribute("pageNo").toString()) + 1)
					* MANAGE_USER_PAGE_SIZE;
		// sortDirection = (sortDirection =="0")?"1":"0";

		log.debug("after record showing " + results.isEmpty());
		if (session.getAttribute("searchType").toString()
				.equalsIgnoreCase(MANAGE_USER_ALL_SEARCH_ACTION))
			recordShowing = synchronousClassList.size();

		String totalRecord = (results.isEmpty()) ? "-1" : results.get(
				"recordSize").toString();

		if (sortDirection.equalsIgnoreCase("1"))
			sortDirection = "0";
		else
			sortDirection = "1";

		context.put("searchKey", session.getAttribute("searchedSearchKey"));
		context.put("searchType", session.getAttribute("searchType"));
		context.put("direction", direction);
		context.put("classes", synchronousClassList);
		context.put("className", className);
		context.put("courseName", course.getCourseTitle());
		context.put("totalRecord", Integer.parseInt(totalRecord));
		context.put("recordShowing", recordShowing);
		context.put("pageNo", session.getAttribute("pageNo"));
		context.put("sortDirection", sortDirection);
		context.put("sortBy", sortBy);
		context.put("id", id);
		context.put("courseType", courseType);
		return context;

	}

	/*
	 * 
	 */
	// ================================================EDIT SynchronousClass
	// Instructor ==================================================
	public ModelAndView editSynchronousClassInstructor(
			HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> context = new HashMap<Object, Object>();

		// SynchronousClass synchClass = null;
		List<SynchronousClass> synchClassList = new ArrayList<SynchronousClass>();
		Long id = null;

		if (request.getParameter("id") != null
				&& !request.getParameter("id").isEmpty()) {
			id = Long.parseLong(request.getParameter("id").toString());
		}

		String action = request.getParameter("action");
		action = (action == null) ? MANAGE_USER_DEFAULT_ACTION : action;

		if (action
				.equalsIgnoreCase(MANAGE_SYNCHRONOUSCLASS_INSTRUCTOR_DELETE_ACTION)) {
			manageSynchClassInstructorDelete(request, response);

		} else if (action
				.equalsIgnoreCase(MANAGE_SYNCHRONOUSCLASS_INSTRUCTORROLE_EDIT_ACTION)) {
			Long instructorId = new Long(request.getParameter("instructorId"));
			InstructorSynchronousClass instructorSynchronousClass = synchronousClassService
					.findInstructorSynchClassByClassId(id, instructorId);
			context.put("instructorSynchronousClass",
					instructorSynchronousClass);
			context.put("synchClassId", id);
			// context.put("synchCourseId", instructorSynchronousClass
			// .getSynchronousClass().getSynchronousCourse().getId());
			context.put("synchCourseId", instructorSynchronousClass
					.getSynchronousClass().getCourse().getId());
			return new ModelAndView(editSynchronousClassInstructorRoleTemplate,
					"context", context);
		} else if (action
				.equalsIgnoreCase(MANAGE_SYNCHRONOUSCLASS_INSTRUCTORROLE_UPDATE_ACTION)) {
			updateSynchClassInstructorRole(request, response);
		}

		if (action
				.equalsIgnoreCase(MANAGE_SYNCHRONOUSCLASS_INSTRUCTOR_DELETE_ACTION)
				|| action
						.equalsIgnoreCase(MANAGE_SYNCHRONOUSCLASS_INSTRUCTORROLE_UPDATE_ACTION)
				|| action
						.equalsIgnoreCase(MANAGE_SYNCLASS_ADVANCED_INSTRUCTOR_SEARCH_ACTION)
				|| action.equalsIgnoreCase(MANAGE_USER_DEFAULT_ACTION)) {

			String firstName = "";
			String lastName = "";
			String instType = "";

			firstName = (request.getParameter("firstName") == null) ? ""
					: request.getParameter("firstName");
			lastName = (request.getParameter("lastName") == null) ? ""
					: request.getParameter("lastName");
			instType = (request.getParameter("instructorType") == null) ? ""
					: request.getParameter("instructorType");

			List<InstructorSynchronousClass> synchClassInstructors = synchronousClassService
					.findInstructorsByClassId(id, firstName, lastName, instType);

			context.put("synchClassInstructors", synchClassInstructors);
			context.put("totalSynchClassInstructors",
					synchClassInstructors.size());
			context.put("synchClassId", id);
			if (id != null)
				/*context.put("synchCourseId", synchronousClassService
						.getSynchronousClassById(id).getSynchronousCourse()
						.getId());*/
				context.put("synchCourseId", synchronousClassService
						.getSynchronousClassById(id).getCourse()
						.getId());

		}

		return new ModelAndView(editSynchronousClassInstructorTemplate,
				"context", context);
	}

	// ============================================================================================================================================

	public void updateSynchClassInstructorRole(HttpServletRequest request,
			HttpServletResponse response) {

		Long instructorSynchClassId = new Long(
				request.getParameter("instructorSynchClassId"));
		String instructorType = request.getParameter("instType");

		InstructorSynchronousClass instructorSynchronousClass = synchronousClassService
				.loadForUpdateInstructorSynchClassById(instructorSynchClassId);
		instructorSynchronousClass.setInstructorType(instructorType);

		synchronousClassService
				.saveSynchronousClassInstructorRole(instructorSynchronousClass);
	}

	/*
	 * 
	 */
	public ModelAndView editSynchronousClassSchedule(
			HttpServletRequest request, HttpServletResponse response) {

		List<SynchronousClass> synchClassList = new ArrayList<SynchronousClass>();
		Map<Object, Object> context = new HashMap<Object, Object>();

		// get request parameters
		String action = request.getParameter("action");
		action = (action == null) ? MANAGE_USER_DEFAULT_ACTION : action;
		Long id = new Long(request.getParameter("id"));
		String courseType = request.getParameter("courseType");
		boolean isInProgress = false;

		if (action
				.equalsIgnoreCase(MANAGE_SYNCHRONOUSCLASS_SESSION_DELETE_ACTION)) {

			String[] selectedSessionValues = request
					.getParameterValues("selectedSessions");

			if (selectedSessionValues != null) {

				for (int loop = 0; loop < selectedSessionValues.length; loop++) {
					String delClassSessionID = selectedSessionValues[loop];
					if (synchronousClassService
							.findSynchClassSessionBySessionId(
									Long.parseLong(delClassSessionID))
							.getStatus()
							.equalsIgnoreCase(SynchronousSession.INPROGRESS)) {

						isInProgress = true;
						break;
					}

				}
				if (isInProgress) {

					context.put("inProgressError",
							"error.delete.inprogress.session.message");

				} else {
					manageSynchClassSessionDelete(request, response);
				}
			}

		} else if (action
				.equalsIgnoreCase(MANAGE_SYNCHRONOUSCLASS_SESSION_EDIT_ACTION)) {
			Long sessionId = new Long(request.getParameter("sessionId"));
			SynchronousSession synchronousSession = synchronousClassService
					.findSynchClassSessionBySessionId(sessionId);
			context.put("synchronousSession", synchronousSession);
			context.put("synchClassId", id);
			return new ModelAndView(editSynchronousClassSessionTemplate,
					"context", context);
		} else if (action
				.equalsIgnoreCase(MANAGE_SYNCHRONOUSCLASS_SESSION_UPDATE_ACTION)) {
			/*
			 * if updateSynchClassSession return false then everything is ok and
			 * it has update the record else it will redirect it to the same
			 * page with error message that what is missing
			 */
			if (updateSynchClassSession(request, response, context)) {
				context.put("synchClassId", id);
				return new ModelAndView(editSynchronousClassSessionTemplate,
						"context", context);
			}
		}

		// Apply paging here... LMS-4189
		String pageIndex = request.getParameter("pageIndex");
		pageIndex = (pageIndex == null) ? "0" : pageIndex;
		log.debug("pageIndex: " + pageIndex);

		String pageDirection = request.getParameter("pageDirection");
		pageDirection = (pageDirection == null) ? MANAGE_USER_PREVPAGE_DIRECTION
				: pageDirection;
		log.debug("pageDirection: " + pageDirection);

		int targetPageNo = (pageIndex.isEmpty()) ? 0 : Integer
				.parseInt(pageIndex);
		int recordToFetch = MANAGE_USER_PAGE_SIZE;

		if (pageDirection.equalsIgnoreCase(MANAGE_USER_PREVPAGE_DIRECTION)) {
			targetPageNo -= (targetPageNo <= 0) ? 0 : 1;
		} else if (pageDirection
				.equalsIgnoreCase(MANAGE_USER_NEXTPAGE_DIRECTION)) {
			targetPageNo += (targetPageNo < 0) ? 0 : 1;
		} else if (pageDirection.equals(MANAGE_USER_ALL_SEARCH_ACTION)) {
			targetPageNo = 0;
			recordToFetch = 0;
		}
		log.debug("targetPageNo: " + targetPageNo);

		String sortBy = "startDateTime";
		int sortDirection = 0; // ascending

		SynchronousClass synchClass = synchronousClassService
				.getSynchronousClassById(id);
		// SynchronousCourse synchCourse = synchClass.getSynchronousCourse();
		Course synchCourse = synchClass.getCourse();

		Map<Object, Object> result = synchronousClassService
				.getSynchronousSessionsByClassId(id, targetPageNo,
						recordToFetch, sortBy, sortDirection);
		List<SynchronousSession> synchSessions = (List<SynchronousSession>) result
				.get("synchronousSessionList");

		int totalRecords = (result.isEmpty()) ? -1 : Integer.parseInt(result
				.get("totalRecords").toString());
		log.debug("totalRecords: " + totalRecords);

		int currentPageRecordCount = (recordToFetch == 0) ? synchSessions
				.size()
				: ((synchSessions.size() < MANAGE_USER_PAGE_SIZE) ? synchSessions
						.size() : MANAGE_USER_PAGE_SIZE);
		log.debug("currentPageRecordCount: " + currentPageRecordCount);

		context.put("synchCourseId", synchCourse.getId());
		context.put("synchCourseName", synchCourse.getCourseTitle());
		context.put("synchClass", synchClass);
		context.put("synchSessions", synchSessions);
		context.put("pageDirection", pageDirection);
		context.put("totalRecords", totalRecords);
		context.put("currentPageRecordCount", currentPageRecordCount);
		context.put("pageSize", MANAGE_USER_PAGE_SIZE);
		context.put("targetPageNo", targetPageNo);
		context.put("id", id);
		context.put("courseType", courseType);
		return new ModelAndView(editSynchronousClassScheduleTemplate,
				"context", context);
	}

	public boolean updateSynchClassSession(HttpServletRequest request,
			HttpServletResponse response, Map context) {

		Long synchClassSessionId = new Long(request.getParameter("sessionId"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");

		SynchronousSession synchronousSession = synchronousClassService
				.loadForUpdateSynchronousSession(synchClassSessionId);
		/**
		 * first make the startDateTime and endDateTime column null and then
		 * fill it after validation
		 */
		// synchronousSession.setStartDateTime(null);
		// synchronousSession.setEndDateTime(null);
		synchronousSession.setStartDateTime(FormUtil.getInstance().getDate(
				startDate + " " + startTime, "MM/dd/yyyy hh:mm a"));
		synchronousSession.setStartDateTime(FormUtil.getInstance().getDate(
				endDate + " " + endTime, "MM/dd/yyyy hh:mm a"));

		if (!validateSynchSessionData(request, context, synchronousSession)) {
			/* everthing is fine lets update it and return to main screen */
			synchronousClassService.saveSynchronousSession(synchronousSession);
			return false;
		}

		context.put("synchronousSession", synchronousSession);

		return true;

	}

	/*
	 * 
	 */
	public ModelAndView editSynchronousClass(HttpServletRequest request,
			HttpServletResponse response) {

		// SynchronousClass synchClass = null;
		List<SynchronousClass> synchClassList = new ArrayList<SynchronousClass>();

		String action = request.getParameter("action");
		action = (action == null) ? MANAGE_USER_DEFAULT_ACTION : action;

		Map<Object, Object> context = new HashMap<Object, Object>();

		Long id = new Long(request.getParameter("id"));

		Enumeration enum1 = request.getParameterNames();
		while (enum1.hasMoreElements()) {
			String paramName = enum1.nextElement().toString();
			log.debug("param name: " + paramName);
			log.debug("param value: " + request.getParameter(paramName));
		}

		SynchronousClass synchClass = synchronousClassService
				.getSynchronousClassById(id);
		// SynchronousCourse synchCourse = synchClass.getSynchronousCourse();
		Course synchCourse = synchClass.getCourse();

		context.put("timeZoneList", timeZoneService.getAllTimeZone());
		context.put("synchClass", synchClass);
		context.put("synchCourseId", synchCourse.getId());
		context.put("synchCourseName", synchCourse.getCourseTitle());
		context.put("courseType", synchCourse.getCourseType());
		
		
		//Start: LMS-15941 - SRS: 2.3.2 > 2.3.2.1
		boolean showLS360Option = false;
		boolean showOtherProviderOption = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			Customer currentCustomer=details.getCurrentCustomer();
			if(currentCustomer.getCustomerType().equalsIgnoreCase("B2B")&& currentCustomer.getContentOwner()!=null && 
					(currentCustomer.getContentOwner().getPlanTypeId()==null || 
					(currentCustomer.getContentOwner().getPlanTypeId()!=null && currentCustomer.getContentOwner().getPlanTypeId()==1))){
				showLS360Option = true;
				showOtherProviderOption = true;
				
			}
		}
		context.put("showLS360Option", showLS360Option);
		context.put("showOtherProviderOption", showOtherProviderOption);
		//End: LMS-15941 - SRS: 2.3.2 > 2.3.2.1
		return new ModelAndView(editSynchronousClassTemplate, "context",
				context);
	}

	private boolean validateData(HttpServletRequest request, Map context,
			SynchronousClass synchClass) {

		boolean check = false;

		if (StringUtils.isBlank(request.getParameter("courseTitle"))) {
			context.put("validateCourseName", "Course name is required");
			// synchClass.getSynchronousCourse().setCourseTitle(null);
			synchClass.getCourse().setCourseTitle(null);
			check = true;
		} else {
			/*synchClass.getSynchronousCourse().setCourseTitle(
					request.getParameter("courseTitle"));*/
			synchClass.getCourse().setCourseTitle(
					request.getParameter("courseTitle"));
		}

		if (StringUtils.isBlank(request.getParameter("className"))) {
			context.put("validateClassName", "Class name is required");
			synchClass.setSectionName(null);
			check = true;
		} else if (FieldsValidation.isInValidGlobalName(request
				.getParameter("className"))) {
			context.put("validateClassName",
					"Bad characters not allowed (Class name)");
			synchClass.setSectionName(null);
			check = true;
		} else {
			synchClass.setSectionName(request.getParameter("className"));
		}

		if (!StringUtils.isBlank(request.getParameter("minClassSize"))) {
			if (!FieldsValidation.isNumeric(request
					.getParameter("minClassSize"))) {
				context.put("validateMinClassSize",
						"Minimum class size should be digits");
				synchClass.setMinClassSize(null);
				check = true;
			} else {
				synchClass.setMinClassSize(new Long(request
						.getParameter("minClassSize")));
			}
		}

		if (StringUtils.isBlank(request.getParameter("enrollmentCloseDate"))) {
			context.put("validateEnrollmentCloseDate",	"Enrollment close date is required");
			synchClass.setEnrollmentCloseDate(null);
			check = true;
		}
		
		if (!StringUtils.isBlank(request.getParameter("enrollmentCloseDate"))) {
			String enrollCloseDateStr = request
					.getParameter("enrollmentCloseDate");
			try {
				Date enrollCloseDate = FormUtil.getInstance().getDate(
						enrollCloseDateStr + " 11:59:59 PM",
						"MM/dd/yyyy hh:mm:ss a");
				synchClass.setEnrollmentCloseDate(enrollCloseDate);
			} catch (Exception e) {
				context.put("validateEnrollmentCloseDate",
						"Enrollment close date is invalid");
				synchClass.setEnrollmentCloseDate(null);
				check = true;
			}
		}

		// [10/13/2010] LMS-7029 :: Class size validation for Offline Courses
		// only
		if (request.getParameter("onlineMeetingTF") != null) {
			synchClass.setMaxClassSize(null);
			synchClass.setMeetingType(request.getParameter("meetingType"));
			
			if(!request.getParameter("meetingType").equals(SynchronousClass.MEETINGTYPE_OTHERS)
					&& !request.getParameter("meetingType").equals(SynchronousClass.MEETINGTYPE_WEBINAR))	{
				synchClass.setMeetingURL(null);
				if (StringUtils.isBlank(request.getParameter("meetingId"))) {
					context.put("validateMeetingId", "Meeting Id is required");
					check = true;
				} else {
					synchClass.setMeetingID(request.getParameter("meetingId"));
				}
			
				if (StringUtils.isBlank(request.getParameter("meetingPassCode"))) {
					context.put("validateMeetingPasscode",
							"Meeting Passcode is required");
					check = true;
				} else {
					synchClass.setMeetingPassCode(request
							.getParameter("meetingPassCode"));
				}
			}
			else if(request.getParameter("meetingType").equals(SynchronousClass.MEETINGTYPE_WEBINAR))	{
				if (StringUtils.isBlank(request.getParameter("presenterFirstName"))) {
					context.put("validatePresenterFirstName", "Presenter first name is required");
					synchClass.setPresenterFirstName(request.getParameter("presenterFirstName"));
					check = true;
				} else {
					synchClass.setPresenterFirstName(request.getParameter("presenterFirstName"));
				}
			
				if (StringUtils.isBlank(request.getParameter("presenterLastName"))) {
					context.put("validatePresenterLastName","Presenter last name is required");
					synchClass.setPresenterLastName(request.getParameter("presenterLastName"));
					check = true;
				} else {
					synchClass.setPresenterLastName(request.getParameter("presenterLastName"));
				}
				
				if (StringUtils.isBlank(request.getParameter("presenterEmailAddress"))) {
					context.put("validatePresenterEmailAddress","Presenter email address is required");
					synchClass.setPresenterEmailAddress(request.getParameter("presenterEmailAddress"));
					check = true;
				} 
				else if(!FieldsValidation.isEmailValid(request.getParameter("presenterEmailAddress"))){
					context.put("validatePresenterEmailAddress","Presenter email address is invalid");
					synchClass.setPresenterEmailAddress(request.getParameter("presenterEmailAddress"));
					check = true;
				}
				else {
					synchClass.setPresenterEmailAddress(request.getParameter("presenterEmailAddress"));
				}
			}
			else {
				
				String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
				
				if(StringUtils.isBlank(request.getParameter("meetingURL"))) {
					context.put("validateMeetingURL", "Meeting URL is requried");
					check = true;
				} else if(!StringUtils.isBlank(request.getParameter("meetingURL")) && !IsMatch(request.getParameter("meetingURL"), regex)) {
					context.put("validateMeetingURL", "Meeting URL is invalid");
					check = true;
				} else {
					synchClass.setMeetingURL(request.getParameter("meetingURL"));
				}
			}
		} else {
			synchClass.setMeetingType(null);
			if (!StringUtils.isBlank(request.getParameter("maxClassSize"))) {
				if (!StringUtils.isNumericSpace(request
						.getParameter("maxClassSize"))
						|| (Integer.parseInt(request.getParameter(
								"maxClassSize").toString()) < 1)) {
					context.put("validateMaxClassSize",
							"A numeric non zero value is required for Class Max Size");
					synchClass.setMaxClassSize(null);
					check = true;
				} else {
					synchClass.setMaxClassSize(new Long(request
							.getParameter("maxClassSize")));
				}
			} else {
				context.put("validateMaxClassSize",
						"A numeric non zero value is required for Class Max Size");
				synchClass.setMaxClassSize(null);
				check = true;
			}
		}

		return check;
	}

	private boolean validateSynchSessionData(HttpServletRequest request,
			Map context, SynchronousSession synchSession) {

		boolean check = false;

		if (!StringUtils.isBlank(request.getParameter("endDate"))
				&& !StringUtils.isBlank(request.getParameter("endTime"))) {
			try {
				synchSession.setEndDateTime(FormUtil.getInstance().getDate(
						request.getParameter("endDate") + " "
								+ request.getParameter("endTime"),
						"MM/dd/yyyy hh:mm a"));
			} catch (Exception e) {
				context.put("validateEndDate",
						"End date or time is not correct");
				check = true;
			}
		} else if (!StringUtils.isBlank(request.getParameter("endDate"))) {
			synchSession.setEndDateTime(FormUtil.getInstance().getDate(
					request.getParameter("endDate"), "MM/dd/yyyy"));
		}

		if (!StringUtils.isBlank(request.getParameter("startDate"))
				&& !StringUtils.isBlank(request.getParameter("startTime"))) {
			try {
				synchSession.setStartDateTime(FormUtil.getInstance().getDate(
						request.getParameter("startDate") + " "
								+ request.getParameter("startTime"),
						"MM/dd/yyyy hh:mm a"));
			} catch (Exception e) {
				context.put("validateStartDate",
						"Start date or time is not correct");
				check = true;
			}
		} else if (!StringUtils.isBlank(request.getParameter("startDate"))) {
			synchSession.setStartDateTime(FormUtil.getInstance().getDate(
					request.getParameter("startDate"), "MM/dd/yyyy"));
		}

		if (synchSession.getStartDateTime() != null
				&& synchSession.getEndDateTime() != null) {
			if (synchSession.getStartDateTime().compareTo(
					synchSession.getEndDateTime()) > 0) {
				context.put("validateStartDate",
						"Start date and time must come before end date and time.");
				check = true;
			}
		}

		if (StringUtils.isBlank(request.getParameter("startDate"))) {
			context.put("validateStartDate", "Start date is required");
			check = true;
		}

		if (StringUtils.isBlank(request.getParameter("endDate"))) {
			context.put("validateEndDate", "End date is required");
			check = true;
		}

		if (StringUtils.isBlank(request.getParameter("startTime"))
				|| request.getParameter("startTime").equals(ZERO_TIME_AM)
				|| request.getParameter("startTime").equals(ZERO_TIME_PM)) {
			context.put("validateStartTime", "Start time is required");
			check = true;
		}

		if (StringUtils.isBlank(request.getParameter("endTime"))
				|| request.getParameter("endTime").equals(ZERO_TIME_AM)
				|| request.getParameter("endTime").equals(ZERO_TIME_PM)) {
			context.put("validateEndTime", "End time is required");
			check = true;
		}

		return check;
	}

	public ModelAndView updateSynchronousClass(HttpServletRequest request,
			HttpServletResponse response) {
		Map context = new HashMap();
		// SynchronousCourse synchCourse = null;
		Course synchCourse = null;
		Long maxClassSize = null;
		Long minClassSize = null;
		Long id = new Long(request.getParameter("id"));
		String synchCourseId = request.getParameter("synchCourseId");
		SynchronousClass synchClass = synchronousClassService
				.loadForUpdateSynchronousClass(id);
		// synchCourse = synchClass.getSynchronousCourse();
		synchCourse = synchClass.getCourse();

		String courseTitle = request.getParameter("courseTitle");
		synchCourse.setCourseTitle(courseTitle);

		String className = request.getParameter("className");

		if (validateData(request, context, synchClass)) {
			/*
			 * some of the value are missing so prompt the user and get the
			 * values in required input fields
			 */
			// context.put("course", synchCourse);
			context.put("timeZoneList", timeZoneService.getAllTimeZone());
			context.put("synchClass", synchClass);
			context.put("synchCourseName", synchCourse.getCourseTitle());
			context.put("synchCourseId", synchCourseId);
			context.put("courseType", synchCourse.getCourseType());


			boolean showLS360Option = false;
			boolean showOtherProviderOption = false;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				Customer currentCustomer=details.getCurrentCustomer();
				if(currentCustomer.getCustomerType().equalsIgnoreCase("B2B")&& currentCustomer.getContentOwner()!=null && 
						(currentCustomer.getContentOwner().getPlanTypeId()==null || 
						(currentCustomer.getContentOwner().getPlanTypeId()!=null && currentCustomer.getContentOwner().getPlanTypeId()==1))){
					showLS360Option = true;
					showOtherProviderOption = true;
					
				}
			}
			context.put("showLS360Option", showLS360Option);
			context.put("showOtherProviderOption", showOtherProviderOption);
			
			
			return new ModelAndView(editSynchronousClassTemplate, "context",
					context);
		}

		String enrollmentCloseDateString = null;
		if (request.getParameter("maxClassSize") != null
				&& !request.getParameter("maxClassSize").equalsIgnoreCase(""))
			maxClassSize = new Long(request.getParameter("maxClassSize"));

		if (request.getParameter("minClassSize") != null
				&& !request.getParameter("minClassSize").equalsIgnoreCase(""))
			minClassSize = new Long(request.getParameter("minClassSize"));

		if (request.getParameter("enrollmentCloseDate") != null
				&& !request.getParameter("enrollmentCloseDate")
						.equalsIgnoreCase("")) {
			enrollmentCloseDateString = ((String) request
					.getParameter("enrollmentCloseDate")) + " 11:59:59 PM";
			synchClass.setEnrollmentCloseDate(FormUtil.getInstance().getDate(
					enrollmentCloseDateString, "MM/dd/yyyy hh:mm:ss a"));
		}

		String meetingType = request.getParameter("meetingType"); // LMS-4180
		String meetingId = request.getParameter("meetingId");
		String meetingPasscode = request.getParameter("meetingPassCode");
		String meetingURL = request.getParameter("meetingURL");
		int timeZoneId = Integer.parseInt(request.getParameter("timeZoneId"));
		
		String presenterFirstName = request.getParameter("presenterFirstName");
		String presenterLastName = request.getParameter("presenterLastName");
		String presenterEmailAddress = request.getParameter("presenterEmailAddress");

		/**
		 * added by muhammad akif for -- LMS-16163
		 * for course completion automatic/manual
		 */
		Boolean automatic = new Boolean(request.getParameter("automatic"));
		synchClass.setAutomatic(automatic);
		
		
		synchClass.setSectionName(className);
		synchClass.setMaxClassSize(maxClassSize);
		
		synchClass.setMeetingID(meetingId);
		synchClass.setMeetingPassCode(meetingPasscode);
		synchClass.setMeetingURL(meetingURL);
		synchClass.setTimeZone(timeZoneService.getTimeZoneById(timeZoneId));
		
		synchClass.setPresenterFirstName(presenterFirstName);
		synchClass.setPresenterLastName(presenterLastName);
		synchClass.setPresenterEmailAddress(presenterEmailAddress);
		
		// synchClass.setSynchronousCourse(synchCourse);
		synchClass.setCourse(synchCourse);
		
		//Check for myWebinar type
		if(meetingType!=null && meetingType.equals(SynchronousClass.MEETINGTYPE_WEBINAR)){
			// Generate meeting ID
			MyWebinarPlace webinarPlace = populateMyWebinarPlaceBean(synchClass.getClassStartDateString(), synchClass.getClassEndDateString()
					,synchClass.getCourse().getName(), synchClass.getCourse().getName(), synchClass.getClassStartDateString(), synchClass.getClassEndDateString());
			String meetingID = synchronousClassService.getMeetingInfoForSessions(webinarPlace);
			synchClass.setMeetingID(meetingID);
			
			//Email Webinar link to presenter
			//TODO later this will be picked up from brands
			StringBuilder presenterURL = new StringBuilder(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL"));
			presenterURL.append("&");
			presenterURL.append(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.mt"));
			presenterURL.append("=");
			presenterURL.append(meetingID);
			presenterURL.append("&");
			presenterURL.append(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.first_name"));
			presenterURL.append("=");
			presenterURL.append(synchClass.getPresenterFirstName());
			presenterURL.append("&");
			presenterURL.append(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.last_name"));
			presenterURL.append("=");
			presenterURL.append(synchClass.getPresenterLastName());
			presenterURL.append("&");
			presenterURL.append(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.email"));
			presenterURL.append("=");
			presenterURL.append(synchClass.getPresenterEmailAddress());			

			//Send email to presenter
			SendMailService.sendSMTPMessage(synchClass.getPresenterEmailAddress(),
				VU360Properties.getVU360Property("lms.instructor.mywebinarplace.email.from"),
				VU360Properties.getVU360Property("lms.instructor.mywebinarplace.email.from"),
				VU360Properties.getVU360Property("lms.instructor.mywebinarplace.email.subject"), 
				VU360Properties.getVU360Property("lms.instructor.mywebinarplace.email.body") + presenterURL.toString()
			);
		}
		
		//Moved this line below above if else logic. Need to check meeting type in case of edit
		synchClass.setMeetingType(meetingType);// LMS-4180

		if (!validateData(request, context, synchClass)) {
			/* everthing is fine lets update it and return to main screen */
			synchronousClassService.saveSynchronousClass(synchClass);
			/*
			 * context.put("totalRecord", 0); context.put("recordShowing", 0);
			 * context.put("pageNo", 0); context.put("sortDirection", 0);
			 * context.put("id", synchCourse.getId());
			 */// return new ModelAndView(manageSynchronousClassTemplate,
				// "context", context);
			return new ModelAndView(
					"redirect:ins_manageSynchronousClass.do?id="
							+ synchCourse.getId());
		} else {
			/*
			 * some of the value are missing so prompt the user and get the
			 * values in required input fields
			 */
			// context.put("course", synchCourse);
			context.put("timeZoneList", timeZoneService.getAllTimeZone());
			context.put("synchClass", synchClass);
			context.put("synchCourseName", synchCourse.getCourseTitle());
			context.put("synchCourseId", synchCourseId);
			return new ModelAndView(editSynchronousClassTemplate, "context",
					context);
		}

	}
	
	private MyWebinarPlace populateMyWebinarPlaceBean(String strStartDate, String strEndDate, String courseName, String className, String startDateTime, String endDateTime) {
		
		MyWebinarPlace webinar = new MyWebinarPlace();
		webinar.setTopic(className);
		webinar.setAgenda(courseName);
		webinar.setEvent_reference(className);
		
		//Time calculation
		
		Date startDate = DateUtil.getDateObject(strStartDate);
		Date endDate = DateUtil.getDateObject(strEndDate);
		
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(startDate);   // assigns calendar to given date 
		calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		calendar.get(Calendar.HOUR);        // gets hour in 12h format
		calendar.get(Calendar.MONTH);       // gets month number, NOTE this is zero based!
		calendar.get(Calendar.AM_PM);		// Gets AM/PM
		
		Calendar endCalendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		endCalendar.setTime(endDate);   // assigns calendar to given date 
		
		long dminutes = (calendar.getTimeInMillis() - endCalendar.getTimeInMillis()) / (60 * 1000);
		
		
		webinar.setDate(strStartDate);//"2001-11-12"
		webinar.setTime_hrs(String.valueOf(calendar.get(Calendar.HOUR)));
		webinar.setTime_mins(String.valueOf(dminutes));
		webinar.setTime_type(String.valueOf(calendar.get(Calendar.AM_PM)));
		
		
		return webinar;
	}

	// to compute the next or previous page no.
	int evaluatePageIndex(int pageIndex, String direction) {

		if (direction != null) { // checking for null pointer exception
			if (direction.equalsIgnoreCase("next")) {
				pageIndex++; // increment for next page
			} else if (direction.equalsIgnoreCase("prev")) {
				pageIndex--; // decrement for previous page
			}
		}

		return pageIndex;
	}

	// to find sort attribute name
	String evaluateSortColumn(String colNumber) {
		int colNum = 1;
		String columnName = "";
		// safety check
		if (colNumber.trim().length() == 0) {
			columnName = "1";
		}

		try {
			colNum = Integer.parseInt(colNumber);

		} catch (NumberFormatException e) {
			/* do nothing */
			log.debug("NumberFormatException occurred :" + colNumber
					+ " is not a valid number");
		}

		switch (colNum) {
		case 1:
			columnName = MANAGE_CLASSSCHEDULE_SORT_SECTIONNAME;
			break;
		case 2:
			columnName = MANAGE_CLASSSCHEDULE_SORT_CLASSSTARTDATE;
			break;
		case 3:
			columnName = MANAGE_CLASSSCHEDULE_SORT_CLASSENDDATE;
			break;
		default:
			columnName = MANAGE_CLASSSCHEDULE_SORT_SECTIONNAME;
		}

		return columnName;
	}

	/*
	 * getter & setters.
	 */

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
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

	public String getManageSynchronousClassTemplate() {
		return manageSynchronousClassTemplate;
	}

	public void setManageSynchronousClassTemplate(
			String manageSynchronousClassTemplate) {
		this.manageSynchronousClassTemplate = manageSynchronousClassTemplate;
	}

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public String getEditSynchronousClassTemplate() {
		return editSynchronousClassTemplate;
	}

	public void setEditSynchronousClassTemplate(
			String editSynchronousClassTemplate) {
		this.editSynchronousClassTemplate = editSynchronousClassTemplate;
	}

	public String getEditSynchronousClassScheduleTemplate() {
		return editSynchronousClassScheduleTemplate;
	}

	public void setEditSynchronousClassScheduleTemplate(
			String editSynchronousClassScheduleTemplate) {
		this.editSynchronousClassScheduleTemplate = editSynchronousClassScheduleTemplate;
	}

	public String getEditSynchronousClassInstructorTemplate() {
		return editSynchronousClassInstructorTemplate;
	}

	public void setEditSynchronousClassInstructorTemplate(
			String editSynchronousClassInstructorTemplate) {
		this.editSynchronousClassInstructorTemplate = editSynchronousClassInstructorTemplate;
	}

	public String getEditSynchronousClassInstructorRoleTemplate() {
		return editSynchronousClassInstructorRoleTemplate;
	}

	public void setEditSynchronousClassInstructorRoleTemplate(
			String editSynchronousClassInstructorRoleTemplate) {
		this.editSynchronousClassInstructorRoleTemplate = editSynchronousClassInstructorRoleTemplate;
	}

	public String getEditSynchronousClassSessionTemplate() {
		return editSynchronousClassSessionTemplate;
	}

	public void setEditSynchronousClassSessionTemplate(
			String editSynchronousClassSessionTemplate) {
		this.editSynchronousClassSessionTemplate = editSynchronousClassSessionTemplate;
	}

	/**
	 * @return the timeZoneService
	 */
	public TimeZoneService getTimeZoneService() {
		return timeZoneService;
	}

	/**
	 * @param timeZoneService
	 *            the timeZoneService to set
	 */
	public void setTimeZoneService(TimeZoneService timeZoneService) {
		this.timeZoneService = timeZoneService;
	}

	private static boolean IsMatch(String s, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
        	return false;
        }  
	}
	
}