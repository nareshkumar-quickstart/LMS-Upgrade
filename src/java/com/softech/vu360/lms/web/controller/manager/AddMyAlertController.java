package com.softech.vu360.lms.web.controller.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddMyAlertForm;
import com.softech.vu360.lms.web.controller.validator.AddMyAlertValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AddMyAlertController extends AbstractWizardFormController{
	
	private String redirectTemplate=null;
	private static final Logger log = Logger.getLogger(AddMyAlertController.class.getName());
	private SurveyService surveyService = null;
	
	public AddMyAlertController(){

		super();

		setCommandName("addMyAlertForm");
		setCommandClass(AddMyAlertForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/userGroups/survey/myAlert/addMyAlert1"
				,"manager/userGroups/survey/myAlert/addMyAlert2"});
	}
	
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {

		long lAlertId=0;
		
		AddMyAlertForm form = (AddMyAlertForm)command;
		VU360UserNew logInUser = VU360UserAuthenticationDetails.getCurrentSimpleUser();
		form.getAlert().setCreatedBy(logInUser);		
		form.setAlert(surveyService.addAlert(form.getAlert()));
        form.setAlertTrigger(new AlertTrigger());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		//form.getAlertTrigger().setAlert(form.getAlert());
		if(form.getAlertId()!=null)
			lAlertId=Long.parseLong(form.getAlertId().trim());

		Alert alert=surveyService.getAlertByID(lAlertId);
		form.getAlertTrigger().setAlert(alert);
		form.getAlert().setAlertMessageBody(form.getAlertMessageBody());//added 6th april 2010
		form.getAlertTrigger().setTriggerName(form.getTriggerName());
		if(form.getEvent_date().equals("event")){
			form.getAlertTrigger().setTriggerSingleDate(null);
			if(form.isBefore())
				form.getAlertTrigger().setDaysBeforeEvent(form.getDays());
			if(form.isAfter())
				form.getAlertTrigger().setDaysAfterEvent(form.getDays());
		} else if(form.getEvent_date().equals("date")){
			form.getAlertTrigger().setAvailableAlertEvents(null);
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
						selectedevents.add(item0);
						break;
					}
					j++;
				}
				j=0;
				i++;
			}
		}
		form.getAlertTrigger().setAvailableAlertEvents(selectedevents);
		}
		//Recurring part start
		if(form.isRecurring()){
			form.getAlertTrigger().setIsRecurring(form.isRecurring());
			form.getAlertTrigger().setRecurrrenceSchedule(form.getRecurrrenceSchedule());
			form.getAlertTrigger().setDailyRecurrrenceEveryDay(form.getDailyRecurrrenceEveryDay());
			form.getAlertTrigger().setDailyRecurrrenceEveryWeekDay(form.getDailyRecurrrenceEveryWeekDay());
			form.getAlertTrigger().setWeeklyRecureEveryWeek(form.getWeeklyRecureEveryWeek());
			form.getAlertTrigger().setMonthlyRecurrenceTheEvery(form.getMonthlyRecurrenceTheEvery());
			if(form.getMonthlyRecurrenceTheEvery()!=null){
				if(form.getMonthlyRecurrenceTheEvery().equalsIgnoreCase("day")){
					form.getAlertTrigger().setMonthlyRecurrenceTheEvery("day");
					if(form.getMonthlyRecurrence()!=null && form.getMonthlyRecurrence()!=""){
						form.getAlertTrigger().setMonthlyRecurrenceMonth(Integer.parseInt(form.getMonthlyRecurrence()));
					}
					
					form.getAlertTrigger().setMonthlyRecurrenceType(form.getMonthlyRecurrenceType());
				}
				else if(form.getMonthlyRecurrenceTheEvery().equalsIgnoreCase("the")){
					form.getAlertTrigger().setMonthlyRecurrenceTheEvery("the");
					form.getAlertTrigger().setMonthlyRecurrenceMonth(form.getMonthlyRecurrenceMonth());
					form.getAlertTrigger().setMonthlyRecurrenceType(Integer.parseInt(form.getTheColMonthly()));
					form.getAlertTrigger().setMonthlyRecurrenceTypeDescriptor(form.getMonthlyRecurrenceTypeDescriptor());
				}
			}
			if(form.getYearlyRecurrenceTheEvery()!=null){
				if(form.getYearlyRecurrenceTheEvery().equalsIgnoreCase("every")){
					form.getAlertTrigger().setYearlyRecurrenceTheEvery("every");
					form.getAlertTrigger().setYearlyEveryMonthName(form.getYearlyEveryMonthName());
					form.getAlertTrigger().setYearlyEveryMonthDay(form.getYearlyEveryMonthDay());
					form.getAlertTrigger().setYearlyTheDayTerm(null);
					form.getAlertTrigger().setYearlyTheDayDescription(null);
					form.getAlertTrigger().setYearlyTheMonthDescription(null);
					form.getAlertTrigger().setMonthlyRecurrenceTypeDescriptor(null);
				}else if(form.getYearlyRecurrenceTheEvery().equalsIgnoreCase("the")){
					form.getAlertTrigger().setYearlyRecurrenceTheEvery("the");
					form.getAlertTrigger().setYearlyTheDayTerm(form.getYearlyTheDayTerm());
					form.getAlertTrigger().setYearlyTheDayDescription(form.getYearlyTheDayDescription());
					form.getAlertTrigger().setYearlyTheMonthDescription(form.getYearlyTheMonthDescription());
					form.getAlertTrigger().setYearlyEveryMonthName(null);
					form.getAlertTrigger().setYearlyEveryMonthDay(0);
					form.getAlertTrigger().setMonthlyRecurrenceTypeDescriptor(null);
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
			else{
				form.getAlertTrigger().setRangeOfRecurrrenceStartDay(null);
			}

			if(form.getRangeOfRecurrrenceEndDay()!=null && form.getRangeOfRecurrrenceEndDay()!=""){
				form.getAlertTrigger().setRangeOfRecurrrenceEndDay(df.parse(form.getRangeOfRecurrrenceEndDay()));
			}else{
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
				}else if(form.getRangeRadio().equals("1")){
					form.getAlertTrigger().setRangeOfRecurrrenceEndAfter(Integer.parseInt(form.getRangeOfRecurrrenceEndAfter()));
				}
			}
		}
		form.getAlert().setCreatedDate(new Date());
		form.getAlertTrigger().setAlert(form.getAlert());
		surveyService.addAlert(form.getAlert());  
		
		surveyService.addAlertTrigger(form.getAlertTrigger());
		// TODO Auto-generated method stub
		return new ModelAndView(redirectTemplate);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");

		return new ModelAndView(redirectTemplate);
		//return super.processCancel(request, response, command, error);
	}
	
	protected void postProcessPage(HttpServletRequest request, Object command,	Errors errors,int currentPage) throws Exception {
		AddMyAlertForm form = (AddMyAlertForm)command;
		
		int i=0, j=0;
		AvailableAlertEvent item0;
		String selecteditem0;
		List<AvailableAlertEvent> selectedevents = new ArrayList<AvailableAlertEvent>();
		
		if(currentPage==0 && form.getSelectedEventsId() != null){
			for(;i<form.getAvailableAlertEvents().size();){
				item0 = form.getAvailableAlertEvents().get(i);
				for(;j<form.getSelectedEventsId().length;){
					selecteditem0 = (form.getSelectedEventsId())[j];

					if(item0.getId()==(Long.parseLong(selecteditem0))){
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
			form.setSelectedAvailableAlertEvents(selectedevents);
		}
		
		if( StringUtils.isBlank(form.getAlert().getAlertMessageBody() ) )
			form.getAlert().setAlertMessageBody("");
		form.getAlert().setAlertMessageBody(form.getAlert().getAlertMessageBody().replaceAll("\"","'"));

		form.getAlert().setAlertMessageBody( form.getAlert().getAlertMessageBody().replaceAll("\r\n", "") );
		form.getAlert().setAlertMessageBody( form.getAlert().getAlertMessageBody().replaceAll("\n", "<br>") );
		form.getAlert().setAlertMessageBody( form.getAlert().getAlertMessageBody().replaceAll("\r", "<br>") );
		super.postProcessPage(request, command, errors, currentPage);
	}
	
	protected Object formBackingObject(HttpServletRequest request)throws Exception {

		Object command = super.formBackingObject(request);
		try {			
			AddMyAlertForm form = (AddMyAlertForm)command;
			form.setAlertId(request.getParameter("alertId"));
			List<AvailableAlertEvent> availablealertevents = new ArrayList<AvailableAlertEvent>();
			for(AvailableAlertEvent availablealert : surveyService.getAllAvailableAlertEvents())
			{
				if(!availablealert.getDbColumnName().equals("CE_DUE_REMINDER"))
				{
					availablealertevents.add(availablealert);
				}
					
			}
			form.setAvailableAlertEvents(availablealertevents);			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		AddMyAlertValidator validator = (AddMyAlertValidator)this.getValidator();

		AddMyAlertForm form = (AddMyAlertForm)command;

		switch(page){

		case 0:
			validator.validateFirstPage(form, errors);
			break;

		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
}
