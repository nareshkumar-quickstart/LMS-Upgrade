package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.AlertTriggerFilter;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseAlertTriggeerFilter;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAlertFilter;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupAlertFilter;
import com.softech.vu360.lms.model.LegacyCourse;
import com.softech.vu360.lms.model.OrgGroupAlertFilterTrigger;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.ScormCourse;
import com.softech.vu360.lms.model.SelfPacedCourse;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageFilterForm;
import com.softech.vu360.lms.web.controller.model.MngFilter;
import com.softech.vu360.lms.web.controller.validator.ManageFilterValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FilterSort;

public class ManageFilterController extends VU360BaseMultiActionController {

	private String editFilterTemplate;
	private String displayTemplate;
	private String searcheditAlertTemplate;
	private SurveyService surveyService;
	private EntitlementService entitlementService = null;

	private static final String LEARNERS = "learners";
	private static final String COURSES = "courses";

	private static final Logger log = Logger
			.getLogger(ManageFilterController.class.getName());

	private String[] courseTypes = { "All", "Discussion Forum",
			"SCORM Package", SynchronousCourse.COURSE_TYPE, "Weblink Course",
			HomeworkAssignmentCourse.COURSE_TYPE,
			InstructorConnectCourse.COURSE_TYPE, SelfPacedCourse.COURSE_TYPE,
			WebinarCourse.COURSE_TYPE };

	public ManageFilterController() {
		super();
	}

	public ManageFilterController(Object delegate) {
		super(delegate);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		if (command instanceof ManageFilterForm) {
			ManageFilterForm form = (ManageFilterForm) command;
			if (methodName.equals("searchEditFilterPageLearners")
					|| methodName.equals("searchEditFilterPageLearnerGroups")
					|| methodName
							.equals("searchEditFilterPageOrganisationGroups")
					|| methodName.equals("searchEditFilterPageCourse")) {

				long filterId = 0;
				long triggerId = 0;
				filterId = Long.parseLong((request.getParameter("filterId"))
						.trim());
				if (request.getParameter("filterId") != null)
					form.setFilterId(filterId);
				if (form.getId() != 0) {
					triggerId = form.getId();
					form.setId(triggerId);
				}

			}
		}

	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return new ModelAndView(displayTemplate);

	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
	}

	public ModelAndView displayAlertTriggerFilter(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		ManageFilterForm form = (ManageFilterForm) command;
		long triggerId = 0;
		List<AlertTriggerFilter> filters = new ArrayList<AlertTriggerFilter>();
		HttpSession session = request.getSession();
		if ((request.getParameter("triggerId")) != null
				&& (request.getParameter("triggerId")).matches("\\d+")) {
			triggerId = Long.parseLong((request.getParameter("triggerId"))
					.trim());
			form.setId(triggerId);
		}

		Map<Object, Object> context = new HashMap<Object, Object>();
		filters = surveyService
				.getAllAlertTriggerFilterByAlertTriggerId(triggerId);
		List<MngFilter> mngAlerts = new ArrayList<MngFilter>();
		String showAll = request.getParameter("showAll");
		if (showAll == null)
			showAll = "false";
		context.put("showAll", showAll);
		for (int i = 0; i < filters.size(); i++) {
			MngFilter mngAlt = new MngFilter();
			if (filters.get(i).isDelete() == false) {
				mngAlt.setFilterName(filters.get(i).getFilterName());
				mngAlt.setId(filters.get(i).getId());
				if (filters.get(i) instanceof LearnerAlertFilter) {
					mngAlt.setFilterType("Learner");
				}
				if (filters.get(i) instanceof LearnerGroupAlertFilter) {
					mngAlt.setFilterType("UserGroup");

				}
				if (filters.get(i) instanceof OrgGroupAlertFilterTrigger) {
					mngAlt.setFilterType("OrganizationalGroup");
				}
				if (filters.get(i) instanceof CourseAlertTriggeerFilter) {
					mngAlt.setFilterType("Course");
				}
				mngAlerts.add(mngAlt);
			}
		}

		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if (sortColumnIndex == null
				&& session.getAttribute("sortColumnIndex") != null)
			sortColumnIndex = session.getAttribute("sortColumnIndex")
					.toString();
		String sortDirection = request.getParameter("sortDirection");
		if (sortDirection == null
				&& session.getAttribute("sortDirection") != null)
			sortDirection = session.getAttribute("sortDirection").toString();
		String pageIndex = request.getParameter("pageCurrIndex");
		if (pageIndex == null) {
			if (session.getAttribute("pageCurrIndex") == null)
				pageIndex = "0";
			else
				pageIndex = session.getAttribute("pageCurrIndex").toString();
		}
		Map<String, Object> pagerAttributeMap = new HashMap<String, Object>();
		pagerAttributeMap.put("pageIndex", pageIndex);

		if (sortColumnIndex != null && sortDirection != null) {

			if (sortColumnIndex.equalsIgnoreCase("0")) {
				if (sortDirection.equalsIgnoreCase("0")) {
					FilterSort sort = new FilterSort();
					sort.setSortBy("filterName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts, sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					FilterSort sort = new FilterSort();
					sort.setSortBy("filterName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts, sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			}
		}

		form.setMngAlerts(mngAlerts);
		return new ModelAndView(displayTemplate, "context", context);

	}

	public ModelAndView deleteTriggerFilter(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ManageFilterForm form = (ManageFilterForm) command;
		String[] selectedFilters = request.getParameterValues("rowone");
		if (selectedFilters != null && selectedFilters.length > 0) {
			log.debug("====== deleteFlags ==========> >>>>>>  "
					+ selectedFilters.length);
			long[] selectedFiltersIds = new long[selectedFilters.length];
			int count = 0;
			for (String selectedFilter : selectedFilters) {
				selectedFiltersIds[count] = Long.parseLong(selectedFilter
						.trim());
				count++;
			}
			surveyService.deleteAlertTriggerFilter(selectedFiltersIds);
		}
		Map<String, String> context = new HashMap<String, String>();
		context.put("target", "displayAfterDelete");

		return new ModelAndView(
				"redirect:mgr_manageFilter.do?method=displayAlertTriggerFilter&triggerId="
						+ form.getId());
	}

	public ModelAndView displayAfterDelete(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {

		ManageFilterForm form = (ManageFilterForm) command;
		long triggerId = 0;
		List<AlertTriggerFilter> filters = new ArrayList<AlertTriggerFilter>();
		triggerId = form.getId();
		filters = surveyService
				.getAllAlertTriggerFilterByAlertTriggerId(triggerId);
		List<MngFilter> mngAlerts = new ArrayList<MngFilter>();

		for (AlertTriggerFilter filter : filters) {
			MngFilter mngAlt = new MngFilter();
			if (filter.isDelete() == false) {
				mngAlt.setFilterName(filter.getFilterName());
				mngAlerts.add(mngAlt);
			}
		}

		form.setMngAlerts(mngAlerts);
		return new ModelAndView(displayTemplate);
	}

	public ModelAndView showEditFilterSummaryPage(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ManageFilterForm form = (ManageFilterForm) command;
		long filterId = 0;
		long triggerId = 0;
		if (request.getParameter("filterId") != null) {
			filterId = Long.parseLong(request.getParameter("filterId"));
			form.setFilterId(filterId);
		}
		if ((request.getParameter("triggerId")) != null) {
			triggerId = form.getId();
			form.setId(triggerId);
		} else if (form.getId() != 0) {
			triggerId = form.getId();
			form.setId(triggerId);
		}
		AlertTriggerFilter alertTriggerFilter = (AlertTriggerFilter) surveyService
				.getAlertTriggerFilterByID(filterId);
		alertTriggerFilter = surveyService
				.loadAlertTriggerFilterForUpdate(filterId);
		form.setFilter(alertTriggerFilter);
		form.getFilter().setFilterName(alertTriggerFilter.getFilterName());

		if (alertTriggerFilter instanceof LearnerAlertFilter) {
			form.setFiltertType("learners");
		}
		if (alertTriggerFilter instanceof LearnerGroupAlertFilter) {
			form.setFiltertType("learnergroups");
		}
		if (alertTriggerFilter instanceof OrgGroupAlertFilterTrigger) {
			form.setFiltertType("organizationgroups");
		}
		if (alertTriggerFilter instanceof CourseAlertTriggeerFilter) {
			form.setFiltertType("courses");
		}
		// surveyService.gets
		return new ModelAndView(editFilterTemplate);
	}

	public ModelAndView searchEditFilterPageLearners(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {

		ManageFilterForm form = (ManageFilterForm) command;

		long filterId = 0;
		long triggerId = 0;
		String firstName, lastName, emailAddress;

		if (form.getId() != 0) {
			triggerId = form.getId();
			form.setId(triggerId);
		}

		filterId = Long.parseLong((request.getParameter("filterId")).trim());
		form.setFilterId(filterId);

		firstName = Optional.ofNullable(request.getParameter("firstName"))
				.filter(s -> s != null && !s.isEmpty()).orElse("");
		lastName = Optional.ofNullable(request.getParameter("lastName"))
				.filter(s -> s != null && !s.isEmpty()).orElse("");
		emailAddress = Optional
				.ofNullable(request.getParameter("emailAddress"))
				.filter(s -> s != null && !s.isEmpty()).orElse("");

		if (form.getFiltertType().equals(COURSES)) {
			List<Course> courseList = getSearchCriteriaCourses(
					form.getCourseName(), form.getCourseType());
			form.setCoursesFromDBList(courseList);
		} else if (form.getFiltertType().equals(LEARNERS)) {
			List<Learner> learners = surveyService
					.getLearnersUnderAlertTriggerFilter(form.getFilterId(),
							firstName, lastName, emailAddress);
			form.setLearnerListFromDB(learners);
		}

		form.setFirstName(null);
		form.setLastName(null);
		form.setEmailAddress(null);
		form.setCourse("");
		form.setCourseType("");
		return new ModelAndView(searcheditAlertTemplate);

	}

	/*
	 * Method to search courses by name and type. LMS-14308
	 */
	public List<Course> getSearchCriteriaCourses(String courseName,
			String courseType) {

		Customer customer = ((VU360UserAuthenticationDetails) SecurityContextHolder
				.getContext().getAuthentication().getDetails())
				.getCurrentCustomer();
		List<Course> courses = new ArrayList<Course>();
		List<Course> courseList = new ArrayList<Course>();
		if (!StringUtils.isEmpty(courseType)
				&& courseType.equalsIgnoreCase("All")) {
			courseType = "";
		}

		if (courseName.equals("") && courseType.equals("")) {
			courses = entitlementService.getAllCoursesByEntitlement(customer
					.getId());
			courseList = entitlementService.getAllCoursesByEntitlement(customer
					.getId());
		} else {
			courses = entitlementService.getCoursesByNameAndCourseType(
					customer, courseName, courseType);
			courseList = entitlementService.getCoursesByNameAndCourseType(
					customer, courseName, courseType);
		}

		for (Course selectedCourse : courses) {
			if (selectedCourse instanceof LegacyCourse) {
				courseList.remove(selectedCourse);
			}
		}
		return courseList;
	}

	public ModelAndView searchEditFilterPageLearnerGroups(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		request.setAttribute("search", "doSearch");
		ManageFilterForm form = (ManageFilterForm) command;
		String learnergroupName = Optional
				.ofNullable(request.getParameter("learnergroupName"))
				.filter(s -> s != null && !s.isEmpty()).orElse("");

		List<LearnerGroup> learnergroup = surveyService
				.getLearnerGroupsUnderAlertTriggerFilter(form.getFilterId(),
						learnergroupName);
		form.setLearnerGroupListFromDB(learnergroup);

		return new ModelAndView(searcheditAlertTemplate);

	}

	public ModelAndView searchEditFilterPageOrganisationGroups(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		ManageFilterForm form = (ManageFilterForm) command;
		String orgGroupName = Optional
				.ofNullable(request.getParameter("organizationlgroupName"))
				.filter(s -> s != null && !s.isEmpty()).orElse("");
		List<OrganizationalGroup> organizationlgroup = surveyService
				.getOrganisationGroupsUnderAlertTriggerFilter(
						form.getFilterId(), orgGroupName);
		form.setOrganizationalGroupListFromDB(organizationlgroup);
		form.setFiltertType("organizationgroups");

		return new ModelAndView(searcheditAlertTemplate);
	}

	public ModelAndView searchEditFilterPageCourse(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		ManageFilterForm form = (ManageFilterForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();

		String courseName = Optional
				.ofNullable(request.getParameter("courseName"))
				.filter(s -> s != null && !s.isEmpty()).orElse("");
		String courseType = Optional
				.ofNullable(request.getParameter("courseType"))
				.filter(s -> s != null && !s.isEmpty()).orElse("All");

		// Added by Marium Saud : LMS-21490 -- Inorder to match the Course Type requested from UI to Course Type enum in Course Model class.
		if (courseType.equals("SCORM Package"))
			courseType = ScormCourse.COURSE_TYPE;
		if (courseType.equals("Discussion Forum"))
			courseType = DiscussionForumCourse.COURSE_TYPE;
		if (courseType.equals("Weblink Course"))
			courseType = WebLinkCourse.COURSE_TYPE;

		List<Course> courseList = surveyService
				.getCourseUnderAlertTriggerFilter(form.getFilterId(),
						courseName, courseType);

		form.setCoursesFromDBList(courseList);

		context.put("courseTypes", courseTypes);
		return new ModelAndView(searcheditAlertTemplate, "context", context);

	}

	public ModelAndView deleteLearner(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		int j = 0;

		ManageFilterForm form = (ManageFilterForm) command;

		LearnerAlertFilter learnerAlertFilter = (LearnerAlertFilter) surveyService
				.loadAlertTriggerFilterForUpdate(form.getFilterId());

		String[] selectedLearners = request.getParameterValues("rowone");

		form.setSelectedLearnersId(selectedLearners);

		if (selectedLearners != null && selectedLearners.length > 0) {
			log.debug("====== deleteFlags ==========> >>>>>> "
					+ selectedLearners.length);
			long[] selectedLearnerIds = new long[selectedLearners.length];
			int count = 0;
			for (String selectedLearner : selectedLearners) {
				selectedLearnerIds[count] = Long.parseLong(selectedLearner);
				count++;
			}

			for (long Id : selectedLearnerIds) {
				for (j = 0; j < learnerAlertFilter.getLearners().size(); j++) {
					if (learnerAlertFilter.getLearners().get(j).getId() == Id) {
						learnerAlertFilter.getLearners().remove(j);
					}
				}
			}

			surveyService.addAlertTriggerFilter(learnerAlertFilter);

			for (long Id : selectedLearnerIds) {
				for (int s = 0; s < form.getLearnerListFromDB().size(); s++) {
					if (form.getLearnerListFromDB().get(s).getId()
							.compareTo(Id) == 0) {
						form.getLearnerListFromDB().remove(s);
					}
				}
			}
		}

		Map<String, String> context = new HashMap<String, String>();
		context.put("target", "displayAfterDeleteLearners");

		return displayAfterDeleteLearners(request, response, command, errors);

	}

	public ModelAndView displayAfterDeleteLearners(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {

		ManageFilterForm form = (ManageFilterForm) command;
		for (int i = 0; i < form.getLearnerListFromDB().size(); i++) {
			for (int j = 0; j < form.getSelectedLearnersId().length; j++) {
				if (form.getLearnerListFromDB().get(i).getId() == Long
						.parseLong((form.getSelectedLearnersId())[j])) {
					form.getLearnerListFromDB().remove(i);
				}
			}
		}

		return new ModelAndView(searcheditAlertTemplate);

	}

	public ModelAndView deleteLearnerGroups(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		int j = 0;
		ManageFilterForm form = (ManageFilterForm) command;

		LearnerGroupAlertFilter learnerGroupAlertFilter = (LearnerGroupAlertFilter) surveyService
				.loadAlertTriggerFilterForUpdate(form.getFilterId());

		String[] selectedLearners = request.getParameterValues("rowone");
		form.setSelectedLearnersId(selectedLearners);

		if (selectedLearners != null && selectedLearners.length > 0) {
			log.debug("====== deleteFlags ==========> >>>>>> "
					+ selectedLearners.length);

			long[] selectedLearnerIds = new long[selectedLearners.length];
			int count = 0;

			for (String selectedLearner : selectedLearners) {
				selectedLearnerIds[count] = Long.parseLong(selectedLearner
						.trim());
				count++;
			}

			for (long Id : selectedLearnerIds) {
				for (j = 0; j < learnerGroupAlertFilter.getLearnerGroup()
						.size(); j++) {
					if (learnerGroupAlertFilter.getLearnerGroup().get(j)
							.getId() == Id) {
						learnerGroupAlertFilter.getLearnerGroup().remove(j);
					}
				}
			}

			surveyService.addAlertTriggerFilter(learnerGroupAlertFilter);

			for (long Id : selectedLearnerIds) {
				for (int s = 0; s < form.getLearnerGroupListFromDB().size(); s++) {
					if (form.getLearnerGroupListFromDB().get(s).getId()
							.compareTo(Id) == 0) {
						form.getLearnerGroupListFromDB().remove(s);
					}
				}
			}
		}

		Map<String, String> context = new HashMap<String, String>();
		context.put("target", "displayAfterDeleteLearnersGroup");

		return displayAfterDeleteLearnerGroup(request, response, command,
				errors);

	}

	public ModelAndView displayAfterDeleteLearnerGroup(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {

		ManageFilterForm form = (ManageFilterForm) command;
		for (int i = 0; i < form.getLearnerGroupListFromDB().size(); i++) {
			for (int j = 0; j < form.getSelectedLearnersId().length; j++) {
				if (form.getLearnerGroupListFromDB().get(i).getId() == Long
						.parseLong((form.getSelectedLearnersId())[j])) {
					form.getLearnerGroupListFromDB().remove(i);
				}
			}
		}

		return new ModelAndView(searcheditAlertTemplate);

	}

	public ModelAndView deleteOrganisationGroups(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		int j = 0;
		ManageFilterForm form = (ManageFilterForm) command;

		OrgGroupAlertFilterTrigger orgGroupAlertFilterTrigger = (OrgGroupAlertFilterTrigger) surveyService
				.loadAlertTriggerFilterForUpdate(form.getFilterId());

		String[] selectedOrgGroups = request.getParameterValues("rowone");

		form.setSelectedLearnersId(selectedOrgGroups);

		if (selectedOrgGroups != null && selectedOrgGroups.length > 0) {
			log.debug("====== deleteFlags ==========> >>>>>> "
					+ selectedOrgGroups.length);
			long[] selectedOrgIds = new long[selectedOrgGroups.length];
			int count = 0;
			for (String selectedOrg : selectedOrgGroups) {
				selectedOrgIds[count] = Long.parseLong(selectedOrg);
				count++;
			}

			for (long Id : selectedOrgIds) {
				for (j = 0; j < orgGroupAlertFilterTrigger.getOrgGroups()
						.size(); j++) {
					if (orgGroupAlertFilterTrigger.getOrgGroups().get(j)
							.getId() == Id) {
						orgGroupAlertFilterTrigger.getOrgGroups().remove(j);
					}
				}
			}

			surveyService.addAlertTriggerFilter(orgGroupAlertFilterTrigger);

			for (long Id : selectedOrgIds) {
				for (int s = 0; s < form.getOrganizationalGroupListFromDB()
						.size(); s++) {
					if (form.getOrganizationalGroupListFromDB().get(s).getId()
							.compareTo(Id) == 0) {
						form.getOrganizationalGroupListFromDB().remove(s);
					}
				}
			}
		}

		Map<String, String> context = new HashMap<String, String>();
		context.put("target", "displayAfterDeleteLearnersGroup");

		return displayAfterDeleteLearnersGroup(request, response, command,
				errors);

	}

	public ModelAndView displayAfterDeleteLearnersGroup(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {

		ManageFilterForm form = (ManageFilterForm) command;
		for (int i = 0; i < form.getOrganizationalGroupListFromDB().size(); i++) {
			for (int j = 0; j < form.getSelectedLearnersId().length; j++) {
				if (form.getOrganizationalGroupListFromDB().get(i).getId() == Long
						.parseLong((form.getSelectedLearnersId())[j])) {
					form.getOrganizationalGroupListFromDB().remove(i);
				}
			}
		}

		return new ModelAndView(searcheditAlertTemplate);

	}

	public ModelAndView deleteCourses(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)

	throws Exception {

		int j = 0;

		ManageFilterForm form = (ManageFilterForm) command;
		CourseAlertTriggeerFilter courseAlertTriggeerFilter = (CourseAlertTriggeerFilter) surveyService
				.loadAlertTriggerFilterForUpdate(form.getFilterId());
		String[] selectedCourses = request.getParameterValues("rowone");
		form.setSelectedLearnersId(selectedCourses);
		if (selectedCourses != null && selectedCourses.length > 0) {

			log.debug("====== deleteFlags ==========> >>>>>> "
					+ selectedCourses.length);
			long[] selectedCourseIds = new long[selectedCourses.length];
			int count = 0;

			for (String selectedCourse : selectedCourses) {
				selectedCourseIds[count] = Long.parseLong(selectedCourse);
				count++;
			}

			for (long Id : selectedCourseIds) {
				for (j = 0; j < courseAlertTriggeerFilter.getCourses().size(); j++) {
					if (courseAlertTriggeerFilter.getCourses().get(j).getId() == Id) {
						courseAlertTriggeerFilter.getCourses().remove(j);
					}
				}
			}

			surveyService.addAlertTriggerFilter(courseAlertTriggeerFilter);

			for (long Id : selectedCourseIds) {
				for (int s = 0; s < form.getCoursesFromDBList().size(); s++) {
					if (form.getCoursesFromDBList().get(s).getId()
							.compareTo(Id) == 0) {
						form.getCoursesFromDBList().remove(s);
					}
				}
			}
		}

		Map<String, String> context = new HashMap<String, String>();
		context.put("target", "displayAfterDeleteLearnersGroup");

		return displayAfterDeleteEmailAddress(request, response, command,
				errors);

	}

	public ModelAndView displayAfterDeleteEmailAddress(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {
		Map<Object, Object> context = new HashMap<Object, Object>();

		ManageFilterForm form = (ManageFilterForm) command;
		for (int i = 0; i < form.getCoursesFromDBList().size(); i++) {
			for (int j = 0; j < form.getSelectedLearnersId().length; j++) {
				if (form.getCoursesFromDBList().get(i).getId() == Long
						.parseLong((form.getSelectedLearnersId())[j])) {
					form.getCoursesFromDBList().remove(i);
				}
			}
		}
		context.put("courseTypes", courseTypes);
		return new ModelAndView(searcheditAlertTemplate, "context", context);
	}

	public ModelAndView updateFilterSummaryDetails(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ManageFilterForm form = (ManageFilterForm) command;
		ManageFilterValidator validator = (ManageFilterValidator) this
				.getValidator();
		validator.validateFirstPage(form, errors);
		if (errors.hasErrors()) {

			return new ModelAndView(editFilterTemplate);
		}

		form.setFilterName(form.getFilterName().substring(0,
				Math.min(form.getFilterName().length(), 100)));

		surveyService.addAlertTriggerFilter(form.getFilter());
		return new ModelAndView(
				"redirect:mgr_manageFilter.do?method=displayAlertTriggerFilter&triggerId="
						+ form.getId());
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public String getEditFilterTemplate() {
		return editFilterTemplate;
	}

	public void setEditFilterTemplate(String editFilterTemplate) {
		this.editFilterTemplate = editFilterTemplate;
	}

	public String getDisplayTemplate() {
		return displayTemplate;
	}

	public void setDisplayTemplate(String displayTemplate) {
		this.displayTemplate = displayTemplate;
	}

	public String getSearcheditAlertTemplate() {
		return searcheditAlertTemplate;
	}

	public void setSearcheditAlertTemplate(String searcheditAlertTemplate) {
		this.searcheditAlertTemplate = searcheditAlertTemplate;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

}
