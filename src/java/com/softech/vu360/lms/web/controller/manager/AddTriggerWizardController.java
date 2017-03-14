package com.softech.vu360.lms.web.controller.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.TriggerForm;
import com.softech.vu360.lms.web.controller.validator.AddTriggerValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.IApplicationConstants;

public class AddTriggerWizardController extends AbstractWizardFormController{

	private static final Logger log = Logger.getLogger(AddTriggerWizardController.class.getName());
	private String finishTemplate = null;
	private SurveyService surveyService = null;
	private LearnerService learnerService;
	private LearnerLicenseService learnerLicenseServices; 

	

	public AddTriggerWizardController(){
		super();
		setCommandName("addTriggerForm");
		setCommandClass(TriggerForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/survey/manageAlert/manageTrigger/addTrigger/step1"
				,"manager/userGroups/survey/manageAlert/manageTrigger/addTrigger/step2"
		});
	}

	protected Object formBackingObject(HttpServletRequest request)throws Exception {

		Object command = super.formBackingObject(request);
		try {			
			TriggerForm form = (TriggerForm)command;
			
			List<Map<Object, Object>> license = null;
			List<Credential> credentials = new ArrayList<Credential>();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long customerId = null;
			if (loggedInUser.isAdminMode()) {
				customerId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			} else {
				if (loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
					customerId = loggedInUser.getLearner().getCustomer().getId();
				}
			}
			
				license = learnerLicenseServices.getLicenseName(customerId);
				if (!license.isEmpty()) {
				HashMap<Long, String> hashMap = new HashMap<Long, String>();
				
		        for(Map map:license)
				{
					hashMap.put(Long.parseLong(map.get("ID").toString()), map.get("OFFICIALLICENSENAME").toString());
				}
		        
		        for(Long key : hashMap.keySet())
		        {
		        	Credential credential = new Credential();
		        	credential.setOfficialLicenseName(hashMap.get(key));
		        	credential.setId(key);
		        	
		        	credentials.add(credential);
		        }
		        
		        form.setCredentials(credentials);
			  }
			form.setAlertId(request.getParameter("alertId"));
			List<AvailableAlertEvent> alertEventList = surveyService.getAllAvailableAlertEvents();

			//remove CE DUE Reminder from list if user not not have access of feature.
			AvailableAlertEvent ce_due_reminder_event = null;
			if(!UserPermissionChecker.hasAccessToFeature("LMS-LRN-0013", loggedInUser, request.getSession(true)) ) {
				for (AvailableAlertEvent availableAlertEvent : alertEventList) {
					if(availableAlertEvent.getDbDisplayName().equals(IApplicationConstants.CE_DUE_REMINDER))
						ce_due_reminder_event=availableAlertEvent;
				}
				alertEventList.remove(ce_due_reminder_event);
			}
			
			form.setAvailableAlertEvents(alertEventList);
			List<AvailableAlertEvent> defaultSelectedEvent = new ArrayList<AvailableAlertEvent>();
			defaultSelectedEvent.add(alertEventList.get(0));
			form.setSelectedavailableAlertEvents(defaultSelectedEvent);
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception
	{
		long lAlertId=0;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		TriggerForm form = (TriggerForm)command;
		form.setAlertTrigger(new AlertTrigger());

		if(form.getAlertId()!=null)
			lAlertId=Long.parseLong(form.getAlertId().trim());

		Alert alert=surveyService.getAlertByID(lAlertId);
		form.getAlertTrigger().setAlert(alert);

		form.getAlertTrigger().setTriggerName(form.getTriggerName());
		if(form.getEvent_date().equals("event")){
			if(form.isBefore())
				form.getAlertTrigger().setDaysBeforeEvent(form.getDays());
			if(form.isAfter())
				form.getAlertTrigger().setDaysAfterEvent(form.getDays());
		} else if(form.getEvent_date().equals("date")){
			form.getAlertTrigger().setTriggerSingleDate(df.parse(form.getDate()));
		}
		

		if(form.getEvent_date().equals("event")){
		int i=0, j=0;
		AvailableAlertEvent item0;
		String selecteditem0;
		List<AvailableAlertEvent> selectedevents = new ArrayList<AvailableAlertEvent>();
		if(form.getSelectedEventsId()!=null){
			for(;i<form.getAvailableAlertEvents().size();){
				item0 = form.getAvailableAlertEvents().get(i);
				for(;j<form.getSelectedEventsId().length;){
					selecteditem0 = (form.getSelectedEventsId())[j];

					if(item0.getId()==(Long.parseLong(selecteditem0))){
						//addRecipientForm.getSelectedLearnerGroupList().add(item);
						selectedevents.add(item0);


						break;
					}
					j++;
				}
				j=0;
				i++;
			}
			form.getAlertTrigger().setAvailableAlertEvents(selectedevents);
			form.getAlertTrigger().setLicenseExpiratrionDate(DateUtil.getDateObject(form.getLicenseExpireDate()));
			if(form.getAlertTrigger().getLicenseExpiratrionDate() != null)
			{
				Calendar c = Calendar.getInstance();
					DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
					if(form.getAlertTrigger().getDaysBeforeEvent() != null)
					{
						c.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(form.getLicenseExpireDate()));
						c.add(Calendar.DATE, -form.getDays());
						form.getAlertTrigger().setTriggerSingleDate(c.getTime());
					}
					
					if(form.getAlertTrigger().getDaysAfterEvent() != null)
					{
						c.setTime((new SimpleDateFormat("MM/dd/yyyy")).parse(form.getLicenseExpireDate()));
						c.add(Calendar.DATE, form.getDays());
						form.getAlertTrigger().setTriggerSingleDate(c.getTime());
					}
					
					//form.getAlertTrigger().setLicenseExpiratrionDate(DateUtil.getDateObject(form.getLicenseExpireDate()));
			}
			
			//form.getAlertTrigger().setLicenseTypeID(form.getLicenseTypeID());
			form.getAlertTrigger().setLicenseTypeID(form.getSelectedCredentialId());
		}
		}
		
		//Recurring part start
		if(form.isRecurring()){
			form.getAlertTrigger().setIsRecurring(form.isRecurring());
			form.getAlertTrigger().setRecurrrenceSchedule(form.getRecurrrenceSchedule());
			//form.getAlertTrigger().setDailyRecurrrenceEvery(form.getDailyRecurrrenceEvery());
			form.getAlertTrigger().setDailyRecurrrenceEveryDay(form.getDailyRecurrrenceEveryDay());
			form.getAlertTrigger().setDailyRecurrrenceEveryWeekDay(form.getDailyRecurrrenceEveryWeekDay());
			form.getAlertTrigger().setWeeklyRecureEveryWeek(form.getWeeklyRecureEveryWeek());
			//form.getAlertTrigger().setWeeklyRecureEveryWeekOn(form.getWeeklyRecureEveryWeekOn());
			form.getAlertTrigger().setMonthlyRecurrenceTheEvery(form.getMonthlyRecurrenceTheEvery());
			
			form.getAlertTrigger().setMonthlyRecurrenceType(form.getMonthlyRecurrenceType());
			
			form.getAlertTrigger().setMonthlyRecurrenceTypeDescriptor(form.getMonthlyRecurrenceTypeDescriptor());
			
			form.getAlertTrigger().setMonthlyRecurrenceMonth(form.getMonthlyRecurrenceMonth());
			if(form.getMonthlyRecurrenceTheEvery()!=null){
			if(form.getMonthlyRecurrenceTheEvery().equalsIgnoreCase("day")){
			form.getAlertTrigger().setMonthlyRecurrenceTheEvery("day");
			if(form.getMonthlyRecurrence()!=null && form.getMonthlyRecurrence()!=""){
			form.getAlertTrigger().setMonthlyRecurrenceMonth(Integer.parseInt(form.getMonthlyRecurrence()));
			}
			form.getAlertTrigger().setMonthlyRecurrenceType(form.getMonthlyRecurrenceType());
			
			}
			else if(form.getMonthlyRecurrenceTheEvery().equalsIgnoreCase("the"))
			{
				form.getAlertTrigger().setMonthlyRecurrenceTheEvery("the");
				form.getAlertTrigger().setMonthlyRecurrenceMonth(form.getMonthlyRecurrenceMonth());
				form.getAlertTrigger().setMonthlyRecurrenceType(Integer.parseInt(form.getTheColMonthly()));
				
			}
			}
			if(form.getYearlyRecurrenceTheEvery()!=null){
				if(form.getYearlyRecurrenceTheEvery().equalsIgnoreCase("every")){
					form.getAlertTrigger().setYearlyRecurrenceTheEvery(form.getYearlyRecurrenceTheEvery());
					form.getAlertTrigger().setYearlyEveryMonthName(form.getYearlyEveryMonthName());
					form.getAlertTrigger().setYearlyEveryMonthDay(form.getYearlyEveryMonthDay());
				}
				else if(form.getYearlyRecurrenceTheEvery().equalsIgnoreCase("the")){
						form.getAlertTrigger().setYearlyRecurrenceTheEvery(form.getYearlyRecurrenceTheEvery());
						form.getAlertTrigger().setYearlyTheDayTerm(form.getYearlyTheDayTerm());
						form.getAlertTrigger().setYearlyTheDayDescription(form.getYearlyTheDayDescription());
						form.getAlertTrigger().setYearlyTheMonthDescription(form.getYearlyTheMonthDescription());
				}
			}
			
			if(form.getRangeOfRecurrrenceEndAfter()!=""){
			form.getAlertTrigger().setRangeOfRecurrrenceEndAfter(Integer.parseInt(form.getRangeOfRecurrrenceEndAfter()));
			}
			form.getAlertTrigger().setRangeOfRecurrrenceStartTime(form.getRangeOfRecurrrenceStartTime());
			form.getAlertTrigger().setRangeOfRecurrrenceEndTime(form.getRangeOfRecurrrenceEndTime());
			form.getAlertTrigger().setRangeOfRecurrrenceDuration(form.getRangeOfRecurrrenceDuration());
			if(form.getRangeOfRecurrrenceStartDay()!=null && form.getRangeOfRecurrrenceStartDay()!=""){
				form.getAlertTrigger().setRangeOfRecurrrenceStartDay(df.parse(form.getRangeOfRecurrrenceStartDay()));
			}
			else
			{
				form.getAlertTrigger().setRangeOfRecurrrenceStartDay(null);
			}
			if(form.getRangeOfRecurrrenceEndDay()!=null && form.getRangeOfRecurrrenceEndDay()!=""){
				form.getAlertTrigger().setRangeOfRecurrrenceEndDay(df.parse(form.getRangeOfRecurrrenceEndDay()));
			}
			else
			{
				form.getAlertTrigger().setRangeOfRecurrrenceEndDay(null);
			}
			String day="";
			if(form.getWeeklyRecureEveryWeekOn()!=null){
			for(int k=0;k<form.getWeeklyRecureEveryWeekOn().length;k++){
				day+=(form.getWeeklyRecureEveryWeekOn())[k];
				if(k!=form.getWeeklyRecureEveryWeekOn().length){
					day+=":";
				}
			}
			}
			form.getAlertTrigger().setWeeklyRecureEveryWeekOn(day);
			
			if(form.getRangeRadio()!=null){
			if(form.getRangeRadio().equals("0")){
				form.getAlertTrigger().setRangeOfRecurrrenceEndAfter(0);
			}
			else if(form.getRangeRadio().equals("1")){
				form.getAlertTrigger().setRangeOfRecurrrenceEndAfter(Integer.parseInt(form.getRangeOfRecurrrenceEndAfter()));
			}
			
		}
			
		}

		form.getAlertTrigger().setCreatedDate(new Date());
		surveyService.addAlertTrigger(form.getAlertTrigger());
		// TODO Auto-generated method stub
		return new ModelAndView("redirect:mgr_manageTrigger.do?alertId=" + form.getAlertId());
	}


	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		TriggerForm form = (TriggerForm)command;
		log.debug("IN processCancel");
		return new ModelAndView("redirect:mgr_manageTrigger.do?alertId=" + form.getAlertId());
		//return super.processCancel(request, response, command, error);
	}


	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {
		// TODO Auto-generated method stub
		super.onBindAndValidate(request, command, errors, page);
	}


	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int currentPage) throws Exception {
		// TODO Auto-generated method stub
		
		TriggerForm form = (TriggerForm)command;
		
		int i=0, j=0;
		AvailableAlertEvent item0;
		String selecteditem0;
		List<AvailableAlertEvent> selectedevents = new ArrayList<AvailableAlertEvent>();
		
		if(form.getSelectedEventsId() != null){
			for(;i<form.getAvailableAlertEvents().size();){
				item0 = form.getAvailableAlertEvents().get(i);
				for(;j<form.getSelectedEventsId().length;){
					selecteditem0 = (form.getSelectedEventsId())[j];

					if(item0.getId()==(Long.parseLong(selecteditem0))){
						//addRecipientForm.getSelectedLearnerGroupList().add(item);
						selectedevents.add(item0);



						break;
					}
					j++;
				}
				j=0;
				i++;
			}
		}
		if(selectedevents.size()>0){
			form.setSelectedavailableAlertEvents(selectedevents);
		}
		super.postProcessPage(request, command, errors, currentPage);
	}


	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		// TODO Auto-generated method stub
		
		
		return super.referenceData(request, command, errors, page);
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		AddTriggerValidator validator = (AddTriggerValidator)this.getValidator();
		TriggerForm form = (TriggerForm)command;
		switch(page){
		case 0:
			validator.validateFirstPage(form, errors);
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
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
