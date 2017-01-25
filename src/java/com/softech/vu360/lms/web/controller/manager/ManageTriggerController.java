package com.softech.vu360.lms.web.controller.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageAlertTriggerForm;
import com.softech.vu360.lms.web.controller.model.MngAlert;
import com.softech.vu360.lms.web.controller.validator.ManageTriggerValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.IApplicationConstants;
import com.softech.vu360.util.TriggerSort;

/**
 * 
 * @author haider.ali
 *
 */
public class ManageTriggerController extends VU360BaseMultiActionController {
	private String redirectTemplate;
	private String displayTemplate;
	private HttpSession session = null;
	private SurveyService surveyService;
	private String editTriggerTemplate;
	final String LBL_EVENT_TYPE_DATE = "date";
	private LearnerService learnerService;
	private LearnerLicenseService learnerLicenseServices;

	private static transient Logger log = Logger
			.getLogger(ManageRecipientController.class.getName());

	public ManageTriggerController() {
		super();
	}

	public ManageTriggerController(Object delegate) {
		super(delegate);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {

	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "displayAlertTrigger");

		return new ModelAndView(redirectTemplate, "context", context);
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
	}

	public ModelAndView displayAlertTrigger(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		ManageAlertTriggerForm form = (ManageAlertTriggerForm) command;
		List<AlertTrigger> triggers = new ArrayList<AlertTrigger>();
		String paging = request.getParameter("paging");
		session = request.getSession();
		if ((request.getParameter("alertId")) != null)
			form.setId(Long.parseLong((request.getParameter("alertId")).trim()));
		Map<Object, Object> context = new HashMap<Object, Object>();
		triggers = surveyService.getAllAlertTriggerByAlertId(form.getId());
		List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
		String showAll = request.getParameter("showAll");
		if (showAll == null)
			showAll = "false";
		context.put("showAll", showAll);
		for (AlertTrigger trigger : triggers) {
			MngAlert mngAlt = new MngAlert();
			if (trigger.isDelete() == false) {
				mngAlt.setTrigger(trigger);
				mngAlerts.add(mngAlt);
			}
		}

		// ============================For Sorting============================
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
					TriggerSort sort = new TriggerSort();
					sort.setSortBy("triggerName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts, sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					TriggerSort sort = new TriggerSort();
					sort.setSortBy("triggerName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts, sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			}
		}

		// Set<SurveyResult> surveyResults =
		// surveyService.getAllSurvey(surveyowner);
		form.setMngAlerts(mngAlerts);
		// TODO
		return new ModelAndView(displayTemplate, "context", context);
	}

	public ModelAndView showEditTriggerPage(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ManageAlertTriggerForm form = (ManageAlertTriggerForm) command;
		Long triggerId = null;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		List<Map<Object, Object>> license = null;
		List<Credential> credentials = new ArrayList<Credential>();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Long customerId = null;
		
		if (loggedInUser.isLMSAdministrator()) {
			customerId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
		} else {
			if (loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
				customerId= loggedInUser.getLearner().getCustomer().getId();
			}
		}

		license = learnerLicenseServices.getLicenseName(customerId);
		if (!license.isEmpty()) {
			HashMap<Long, String> hashMap = new HashMap<Long, String>();

			for (Map map : license) {
				hashMap.put(Long.parseLong(map.get("ID").toString()),
						map.get("OFFICIALLICENSENAME").toString());
			}

			for (Long key : hashMap.keySet()) {
				Credential credential = new Credential();
				credential.setOfficialLicenseName(hashMap.get(key));
				credential.setId(key);

				credentials.add(credential);
			}

			form.setCredentials(credentials);
		}

		if (request.getParameter("triggerId") != null)
			triggerId = Long.parseLong(request.getParameter("triggerId"));
		AlertTrigger trigger;
		if (triggerId != null)
			;
		trigger = surveyService.loadAlertTriggerForUpdate(triggerId);
		form.setTrigger(trigger);
		form.getTrigger().setTriggerName(form.getTrigger().getTriggerName());
		if ((form.getTrigger().getDaysAfterEvent()) != null
				&& (form.getTrigger().getDaysBeforeEvent()) != null) {
			form.setDays(form.getTrigger().getDaysBeforeEvent());
			form.setAfter(true);
			form.setBefore(true);
			form.setLicenseExpireDate(com.softech.vu360.util.DateUtil
					.getStringDate(form.getTrigger()
							.getLicenseExpiratrionDate()));
			form.setLicenseTypeID(form.getSelectedCredentialId());
		} else if ((form.getTrigger().getDaysAfterEvent()) != null) {
			form.setAfter(true);
			form.setBefore(false);
			form.setDays(form.getTrigger().getDaysAfterEvent());
			form.setEvent_date("event");
			form.setLicenseExpireDate(com.softech.vu360.util.DateUtil
					.getStringDate(form.getTrigger()
							.getLicenseExpiratrionDate()));
			form.setLicenseTypeID(form.getSelectedCredentialId());

		} else if ((form.getTrigger().getDaysBeforeEvent()) != null) {
			form.setBefore(true);
			form.setAfter(false);
			form.setDays(form.getTrigger().getDaysBeforeEvent());
			form.setEvent_date("event");
			form.setLicenseExpireDate(com.softech.vu360.util.DateUtil
					.getStringDate(form.getTrigger()
							.getLicenseExpiratrionDate()));
			form.setLicenseTypeID(form.getSelectedCredentialId());
		} else {
			form.setBefore(false);
			form.setAfter(false);
			form.setDays(null);
			form.setEvent_date(LBL_EVENT_TYPE_DATE);

		}

		if (form.getTrigger().getTriggerSingleDate() != null
				&& form.getTrigger().getLicenseExpiratrionDate() == null) {
			form.setDate(df.format(form.getTrigger().getTriggerSingleDate()));

			/*
			 * LMS-14107 If trigger type is 'Date', Need to set the following
			 * properties
			 */
			form.setBefore(false);
			form.setAfter(false);
			form.setDays(null);
			form.setEvent_date(LBL_EVENT_TYPE_DATE);
		} else {
			form.setDate("");
		}

		List<AvailableAlertEvent> alertEventList = surveyService
				.getAllAvailableAlertEvents();

		// remove CE DUE Reminder from list if user not not have access of
		// feature.
		AvailableAlertEvent ce_due_reminder_event = null;
		if (!UserPermissionChecker.hasAccessToFeature("LMS-LRN-0013", loggedInUser, request.getSession(true))) {
			for (AvailableAlertEvent availableAlertEvent : alertEventList) {
				if (availableAlertEvent.getDbDisplayName().equals(
						IApplicationConstants.CE_DUE_REMINDER))
					ce_due_reminder_event = availableAlertEvent;
			}
			alertEventList.remove(ce_due_reminder_event);
		}

		form.setAvailableAlertEvents(alertEventList);
		form.setSelectedAvailableAlertEvents(form.getTrigger()
				.getAvailableAlertEvents());
		form.getTrigger().setIsRecurring(form.getTrigger().getIsRecurring());
		form.setRecurring(form.getTrigger().getIsRecurring());

		if (form.getTrigger().getIsRecurring()) {
			form.setRecurrrenceSchedule(form.getTrigger()
					.getRecurrrenceSchedule());
			if (form.getTrigger().getDailyRecurrrenceEveryDay() != null) {
				if (form.getTrigger().getDailyRecurrrenceEveryWeekDay() != null
						&& form.getTrigger().getDailyRecurrrenceEveryWeekDay()
								.equalsIgnoreCase("everyDay")) {
					form.setDailyRecurrrenceEveryWeekDay(form.getTrigger()
							.getDailyRecurrrenceEveryWeekDay());
					form.setDailyRecurrrenceEveryDay(form.getTrigger()
							.getDailyRecurrrenceEveryDay());
				} else if (form.getTrigger().getDailyRecurrrenceEveryWeekDay() != null
						&& form.getTrigger().getDailyRecurrrenceEveryWeekDay()
								.equalsIgnoreCase("everyWeekDay")) {
					form.setDailyRecurrrenceEveryWeekDay(form.getTrigger()
							.getDailyRecurrrenceEveryWeekDay());
					form.setDailyRecurrrenceEveryDay(null);
				}
			}
			form.setWeeklyRecureEveryWeek(form.getTrigger()
					.getWeeklyRecureEveryWeek());
			form.setMonthlyRecurrenceTheEvery(form.getTrigger()
					.getMonthlyRecurrenceTheEvery());
			form.setMonthlyRecurrenceMonth(form.getTrigger()
					.getMonthlyRecurrenceMonth());
			form.setMonthlyRecurrenceTheEvery(form.getTrigger()
					.getMonthlyRecurrenceTheEvery());
			if (form.getTrigger().getYearlyRecurrenceTheEvery() != null) {
				if (form.getTrigger().getYearlyRecurrenceTheEvery()
						.equalsIgnoreCase("every")) {
					form.setYearlyRecurrenceTheEvery(form.getTrigger()
							.getYearlyRecurrenceTheEvery());
					form.setYearlyEveryMonthName(form.getTrigger()
							.getYearlyEveryMonthName());
					form.setYearlyEveryMonthDay(form.getTrigger()
							.getYearlyEveryMonthDay());
				} else if (form.getTrigger().getYearlyRecurrenceTheEvery()
						.equalsIgnoreCase("the")) {
					form.setYearlyRecurrenceTheEvery(form.getTrigger()
							.getYearlyRecurrenceTheEvery());
					form.setYearlyTheDayTerm(form.getTrigger()
							.getYearlyTheDayTerm());
					form.setYearlyTheDayDescription(form.getTrigger()
							.getYearlyTheDayDescription());
					form.setYearlyTheMonthDescription(form.getTrigger()
							.getYearlyTheMonthDescription());
					form.setYearlyEveryMonthDay(0);
				}

			}

			if (form.getTrigger().getMonthlyRecurrenceTheEvery() != null) {
				if (form.getTrigger().getMonthlyRecurrenceTheEvery()
						.equalsIgnoreCase("day")) {
					form.getTrigger().setMonthlyRecurrenceTheEvery("day");
					form.setMonthlyRecurrence(String.valueOf(form.getTrigger()
							.getMonthlyRecurrenceMonth()));
					form.setMonthlyRecurrenceMonth(0);
					form.setMonthlyRecurrenceType(form.getTrigger()
							.getMonthlyRecurrenceType());
					form.setTheColMonthly(null);
					form.setMonthlyRecurrenceTypeDescriptor(null);
				} else if (form.getMonthlyRecurrenceTheEvery()
						.equalsIgnoreCase("the")) {
					form.getTrigger().setMonthlyRecurrenceTheEvery("the");
					form.setMonthlyRecurrenceMonth(form.getTrigger()
							.getMonthlyRecurrenceMonth());
					form.setMonthlyRecurrence(null);
					form.setTheColMonthly(String.valueOf(form.getTrigger()
							.getMonthlyRecurrenceType()));
					form.setMonthlyRecurrenceType(0);
					form.setMonthlyRecurrenceTypeDescriptor(form.getTrigger()
							.getMonthlyRecurrenceTypeDescriptor());
				}
			}
			if (form.getTrigger().getRangeOfRecurrrenceEndAfter() != 0
					&& form.getTrigger().getRangeOfRecurrrenceEndDay() == null) {
				form.setRangeRadio("1");

			} else if (form.getTrigger().getRangeOfRecurrrenceEndAfter() == 0
					&& form.getTrigger().getRangeOfRecurrrenceEndDay() == null) {
				form.setRangeRadio("0");
			} else {
				form.setRangeRadio("2");
			}
			form.setRangeOfRecurrrenceEndAfter(form.getTrigger()
					.getRangeOfRecurrrenceEndAfter() + "");
			form.setRangeOfRecurrrenceStartTime(form.getTrigger()
					.getRangeOfRecurrrenceStartTime());
			form.setRangeOfRecurrrenceEndTime(form.getTrigger()
					.getRangeOfRecurrrenceEndTime());
			form.setRangeOfRecurrrenceDuration(form.getTrigger()
					.getRangeOfRecurrrenceDuration());
			if (form.getTrigger().getRangeOfRecurrrenceStartDay() != null) {
				form.setRangeOfRecurrrenceStartDay(df.format(form.getTrigger()
						.getRangeOfRecurrrenceStartDay()));
			} else {
				form.setRangeOfRecurrrenceStartDay("");
			}
			if (form.getTrigger().getRangeOfRecurrrenceEndDay() != null) {
				form.setRangeOfRecurrrenceEndDay(df.format(form.getTrigger()
						.getRangeOfRecurrrenceEndDay()));
			} else {
				form.setRangeOfRecurrrenceEndDay("");
			}
			if (form.getTrigger().getWeeklyRecureEveryWeekOn() != null) {
				form.setWeeklyRecureEveryWeekOn(form.getTrigger()
						.getWeeklyRecureEveryWeekOn().split(":"));
			}

		}

		return new ModelAndView(editTriggerTemplate);
	}

	public ModelAndView updateTriggerDetails(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ManageAlertTriggerForm form = (ManageAlertTriggerForm) command;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		if (form.getAvailableAlertEvents() != null) {
			int i = 0, j = 0;
			AvailableAlertEvent item0;
			String selecteditem0;
			List<AvailableAlertEvent> selectedAvailableAlertEvents = new ArrayList<AvailableAlertEvent>();

			if (form.getSelectedEventsId() != null)
				for (; i < form.getAvailableAlertEvents().size();) {
					item0 = form.getAvailableAlertEvents().get(i);
					for (; j < form.getSelectedEventsId().length;) {
						selecteditem0 = (form.getSelectedEventsId())[j];
						if (item0 != null) {
							if (item0.getId() == (Long.parseLong(selecteditem0))) {
								selectedAvailableAlertEvents.add(item0);
								break;
							}
						}
						j++;
					}
					j = 0;
					i++;
				}
			form.setSelectedAvailableAlertEvents(selectedAvailableAlertEvents);
			form.getTrigger().setAvailableAlertEvents(
					selectedAvailableAlertEvents);
		}
		if (form.getEvent_date() != null
				&& form.getEvent_date().equals(LBL_EVENT_TYPE_DATE)) {
			form.getTrigger().setAvailableAlertEvents(null);
			if (form.getDate() != null && form.getDate() != "") {
				Date date = new Date(form.getDate());
				form.getTrigger().setTriggerSingleDate(date);
			}
		} else if (form.getEvent_date() != null
				&& form.getEvent_date().equals("event")) {
			form.getTrigger().setTriggerSingleDate(null);
			int i = 0, j = 0;
			AvailableAlertEvent item0;
			String selecteditem0;
			List<AvailableAlertEvent> selectedevents = new ArrayList<AvailableAlertEvent>();
			if (form.getSelectedEventsId() != null) {
				for (; i < form.getAvailableAlertEvents().size();) {
					item0 = form.getAvailableAlertEvents().get(i);
					for (; j < form.getSelectedEventsId().length;) {
						selecteditem0 = (form.getSelectedEventsId())[j];

						if (item0.getId() == (Long.parseLong(selecteditem0))) {
							selectedevents.add(item0);
							break;
						}
						j++;
					}
					j = 0;
					i++;
				}
			}
			if (selectedevents.size() > 0) {
				form.setSelectedAvailableAlertEvents(selectedevents);
				form.getTrigger().setAvailableAlertEvents(selectedevents);
			}
			if (request.getParameter("before") != null
					&& request.getParameter("after") != null) {
				if (request.getParameter("before").equals("true")
						&& request.getParameter("after").equals("true")) {
					form.getTrigger().setDaysAfterEvent(form.getDays());
					form.getTrigger().setDaysBeforeEvent(form.getDays());
				}
			}
			if (request.getParameter("after") != null) {
				if (request.getParameter("after").equals("true")) {
					form.setBefore(false);
					form.getTrigger().setDaysAfterEvent(form.getDays());
					form.getTrigger().setDaysBeforeEvent(null);
				}
			}
			if (request.getParameter("before") != null) {
				if (request.getParameter("before").equals("true")) {
					form.setAfter(false);
					form.getTrigger().setDaysAfterEvent(null);
					form.getTrigger().setDaysBeforeEvent(form.getDays());
				}
			}

			if (form.getLicenseExpireDate() != null
					&& !StringUtils.isEmpty(form.getLicenseExpireDate())) {
				Calendar c = Calendar.getInstance();
				if (form.getTrigger().getDaysBeforeEvent() != null) {
					c.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(form
							.getLicenseExpireDate()));
					c.add(Calendar.DATE, -form.getDays());
					form.getTrigger().setTriggerSingleDate(c.getTime());
				}

				if (form.getTrigger().getDaysAfterEvent() != null) {
					c.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(form
							.getLicenseExpireDate()));
					c.add(Calendar.DATE, form.getDays());
					form.getTrigger().setTriggerSingleDate(c.getTime());
				}

				form.getTrigger().setLicenseExpiratrionDate(
						DateUtil.getDateObject(form.getLicenseExpireDate()));
			}
			form.getTrigger().setLicenseTypeID(form.getSelectedCredentialId());
		}

		form.getTrigger().setIsRecurring(form.isRecurring());

		if (form.isRecurring()) {
			form.getTrigger().setRecurrrenceSchedule(
					form.getRecurrrenceSchedule());

			form.getTrigger().setDailyRecurrrenceEveryDay(
					form.getDailyRecurrrenceEveryDay());
			form.getTrigger().setDailyRecurrrenceEveryWeekDay(
					form.getDailyRecurrrenceEveryWeekDay());
			form.getTrigger().setWeeklyRecureEveryWeek(
					form.getWeeklyRecureEveryWeek());

			form.getTrigger().setMonthlyRecurrenceTheEvery(
					form.getMonthlyRecurrenceTheEvery());
			if (form.getMonthlyRecurrenceTheEvery() != null) {
				if (form.getMonthlyRecurrenceTheEvery().equalsIgnoreCase("day")) {
					form.getTrigger().setMonthlyRecurrenceTheEvery("day");
					form.getTrigger().setMonthlyRecurrenceMonth(
							Integer.parseInt(form.getMonthlyRecurrence()));
					form.getTrigger().setMonthlyRecurrenceType(
							form.getMonthlyRecurrenceType());
				} else if (form.getMonthlyRecurrenceTheEvery()
						.equalsIgnoreCase("the")) {
					form.getTrigger().setMonthlyRecurrenceTheEvery("the");
					form.getTrigger().setMonthlyRecurrenceMonth(
							form.getMonthlyRecurrenceMonth());
					form.getTrigger().setMonthlyRecurrenceType(
							Integer.parseInt(form.getTheColMonthly()));
					form.getTrigger().setMonthlyRecurrenceTypeDescriptor(
							form.getMonthlyRecurrenceTypeDescriptor());
				}
			}
			if (form.getYearlyRecurrenceTheEvery() != null) {
				if (form.getYearlyRecurrenceTheEvery()
						.equalsIgnoreCase("every")) {
					form.getTrigger().setYearlyRecurrenceTheEvery(
							form.getYearlyRecurrenceTheEvery());
					form.getTrigger().setYearlyEveryMonthName(
							form.getYearlyEveryMonthName());
					form.getTrigger().setYearlyEveryMonthDay(
							form.getYearlyEveryMonthDay());
				} else if (form.getYearlyRecurrenceTheEvery().equalsIgnoreCase(
						"the")) {
					form.getTrigger().setYearlyRecurrenceTheEvery(
							form.getYearlyRecurrenceTheEvery());
					form.getTrigger().setYearlyTheDayTerm(
							form.getYearlyTheDayTerm());
					form.getTrigger().setYearlyTheDayDescription(
							form.getYearlyTheDayDescription());
					form.getTrigger().setYearlyTheMonthDescription(
							form.getYearlyTheMonthDescription());
				}
			}
			form.getTrigger().setYearlyEveryMonthName(
					form.getYearlyEveryMonthName());

			form.getTrigger().setYearlyEveryMonthDay(
					form.getYearlyEveryMonthDay());

			form.getTrigger().setYearlyTheDayTerm(form.getYearlyTheDayTerm());
			form.getTrigger().setYearlyTheDayDescription(
					form.getYearlyTheDayDescription());
			form.getTrigger().setYearlyTheMonthDescription(
					form.getYearlyTheMonthDescription());
			if (request.getParameter("rangeOfRecurrrenceEndAfter") != "") {
				form.getTrigger().setRangeOfRecurrrenceEndAfter(
						Integer.parseInt(request
								.getParameter("rangeOfRecurrrenceEndAfter")));
			}
			form.getTrigger().setRangeOfRecurrrenceStartTime(
					form.getRangeOfRecurrrenceStartTime());
			form.getTrigger().setRangeOfRecurrrenceEndTime(
					form.getRangeOfRecurrrenceEndTime());
			form.getTrigger().setRangeOfRecurrrenceDuration(
					form.getRangeOfRecurrrenceDuration());
			if (form.getRangeOfRecurrrenceStartDay() != null
					&& form.getRangeOfRecurrrenceStartDay() != "") {
				form.getTrigger().setRangeOfRecurrrenceStartDay(
						df.parse(form.getRangeOfRecurrrenceStartDay()));
			}
			if (form.getRangeOfRecurrrenceEndDay() != null
					&& form.getRangeOfRecurrrenceEndDay() != "") {

				form.getTrigger().setRangeOfRecurrrenceEndDay(
						df.parse(form.getRangeOfRecurrrenceEndDay()));
			}
			if (form.getRangeRadio() != null) {
				if (form.getRangeRadio().equals("0")) {
					form.getTrigger().setRangeOfRecurrrenceEndDay(null);
					form.getTrigger().setRangeOfRecurrrenceEndAfter(0);
				} else if (form.getRangeRadio().equals("1")) {
					form.getTrigger().setRangeOfRecurrrenceEndDay(null);
					form.getTrigger().setRangeOfRecurrrenceEndAfter(
							Integer.parseInt(form
									.getRangeOfRecurrrenceEndAfter()));
				} else {
					form.getTrigger().setRangeOfRecurrrenceEndAfter(0);
				}

			}
		} else { /* LMS-14107 */
			form.getTrigger().setRecurrrenceSchedule(null);
			form.getTrigger().setDailyRecurrrenceEveryDay(null);
			form.getTrigger().setDailyRecurrrenceEveryWeekDay(null);
			form.getTrigger().setWeeklyRecureEveryWeek(0);
			form.getTrigger().setMonthlyRecurrenceTheEvery(null);
			form.getTrigger().setMonthlyRecurrenceMonth(0);
			form.getTrigger().setMonthlyRecurrenceType(0);
			form.getTrigger().setMonthlyRecurrenceTypeDescriptor(null);
			form.getTrigger().setYearlyRecurrenceTheEvery(null);
			form.getTrigger().setYearlyEveryMonthName(null);
			form.getTrigger().setYearlyEveryMonthDay(0);
			form.getTrigger().setYearlyTheDayTerm(null);
			form.getTrigger().setYearlyTheDayDescription(null);
			form.getTrigger().setYearlyTheMonthDescription(null);
			form.getTrigger().setRangeOfRecurrrenceEndAfter(0);
			form.getTrigger().setRangeOfRecurrrenceStartTime(null);
			form.getTrigger().setRangeOfRecurrrenceEndTime(null);
			form.getTrigger().setRangeOfRecurrrenceDuration(null);
			form.getTrigger().setRangeOfRecurrrenceStartDay(null);
			form.getTrigger().setRangeOfRecurrrenceEndDay(null);
		}
		ManageTriggerValidator validator = (ManageTriggerValidator) this
				.getValidator();
		validator.validateFirstPage(form, errors);
		if (errors.hasErrors()) {

			return new ModelAndView(editTriggerTemplate);
		}

		form.getTrigger().setUpdatedDate(new Date());
		surveyService.addAlertTrigger(form.getTrigger());
		return new ModelAndView("redirect:mgr_manageTrigger.do?alertId="
				+ form.getId());
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getDisplayTemplate() {
		return displayTemplate;
	}

	public void setDisplayTemplate(String displayTemplate) {
		this.displayTemplate = displayTemplate;
	}

	public ModelAndView deleteTrigger(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		String[] selectedTriggers = request.getParameterValues("rowone");
		if (selectedTriggers != null && selectedTriggers.length > 0) {
			log.debug("====== deleteFlags ==========> >>>>>>  "
					+ selectedTriggers.length);
			long[] selectedTriggersIds = new long[selectedTriggers.length];
			int count = 0;
			for (String selectedTrigger : selectedTriggers) {
				selectedTriggersIds[count] = Long.parseLong(selectedTrigger);
				count++;
			}
			surveyService.deleteAlertTriggers(selectedTriggersIds);
		}
		Map<String, String> context = new HashMap<String, String>();
		context.put("target", "displayAfterDelete");

		return displayAfterDelete(request, response, command, errors);
	}

	// TODO

	public ModelAndView displayAfterDelete(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {

		ManageAlertTriggerForm form = (ManageAlertTriggerForm) command;
		List<AlertTrigger> triggers = new ArrayList<AlertTrigger>();
		triggers = surveyService.getAllAlertTriggerByAlertId(form.getId());
		List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
		session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();

		for (AlertTrigger trigger : triggers) {
			MngAlert mngAlt = new MngAlert();
			if (trigger.isDelete() == false) {
				mngAlt.setTrigger(trigger);
				mngAlerts.add(mngAlt);
			}
		}

		// ============================For Sorting============================
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
					TriggerSort sort = new TriggerSort();
					sort.setSortBy("triggerName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts, sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					TriggerSort sort = new TriggerSort();
					sort.setSortBy("triggerName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts, sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			}
		}

		// Set<SurveyResult> surveyResults =
		// surveyService.getAllSurvey(surveyowner);
		form.setMngAlerts(mngAlerts);
		// TODO
		return new ModelAndView(displayTemplate);
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public String getEditTriggerTemplate() {
		return editTriggerTemplate;
	}

	public void setEditTriggerTemplate(String editTriggerTemplate) {
		this.editTriggerTemplate = editTriggerTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public LearnerLicenseService getLearnerLicenseServices() {
		return learnerLicenseServices;
	}

	public void setLearnerLicenseServices(
			LearnerLicenseService learnerLicenseServices) {
		this.learnerLicenseServices = learnerLicenseServices;
	}
}