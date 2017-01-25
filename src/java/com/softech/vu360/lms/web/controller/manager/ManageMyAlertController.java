package com.softech.vu360.lms.web.controller.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageMyAlertForm;
import com.softech.vu360.lms.web.controller.model.MngAlert;
import com.softech.vu360.lms.web.controller.validator.ManageMyAlertValidator;
import com.softech.vu360.util.MyAlertSort;


public class ManageMyAlertController extends VU360BaseMultiActionController{
	
	private String redirectTemplate;
	private SurveyService surveyService;
    private String displayTemplate;
    private String editAlertTemplate;
    private HttpSession session = null;
    private static transient Logger log = Logger.getLogger(ManageRecipientController.class.getName());
    
	
	public ManageMyAlertController() {
		super();
	}
	public ManageMyAlertController(Object delegate) {
		super(delegate);
	}
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {


	}
 

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {


		Map<Object,Object> context = new HashMap<Object,Object>();
        context.put("target", "displayMagAlert");

        return new ModelAndView(redirectTemplate,"context",context);	}


	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
	}
	public ModelAndView displayMagAlert( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {

		ManageMyAlertForm form = (ManageMyAlertForm)command;
		
		try {			
			
			form.setAvailableAlertEvents(surveyService.getAllAvailableAlertEvents());			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		
		return new ModelAndView(displayTemplate);
	}
	
	public ModelAndView displayAlert(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		ManageMyAlertForm form = (ManageMyAlertForm)command;
		
		List<Alert> alerts = new ArrayList<Alert>();
		com.softech.vu360.lms.vo.VU360User logInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		alerts = surveyService.findAlert(logInUser.getId() , form.getAlertName());
		List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
		session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();

		for(Alert alert : alerts){			
			MngAlert mngAlt =new MngAlert();
			
			if(alert.getIsDelete()==false){
				mngAlt.setAlert(alert);
				mngAlerts.add(mngAlt);
			}
			//mngAlt.setRecipents(alert.getRecipents());
			
		}
		

		//============================For Sorting============================
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) {
			if(session.getAttribute("pageCurrIndex")==null)pageIndex="0";
			else pageIndex = session.getAttribute("pageCurrIndex").toString();
		}
		Map<String,Object> pagerAttributeMap = new HashMap<String,Object>();
		pagerAttributeMap.put("pageIndex",pageIndex);

		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					MyAlertSort sort = new MyAlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					MyAlertSort sort = new MyAlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			} 
		}	

		//Set<SurveyResult> surveyResults = surveyService.getAllSurvey(surveyowner);
		form.setMngAlerts(mngAlerts);
		// TODO
		return new ModelAndView(displayTemplate, "context" , context);
	}
	
	public ModelAndView deleteAlert( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {
		ManageMyAlertForm form = (ManageMyAlertForm)command;
		String[] selectedAlerts= request.getParameterValues("rowone") ;
		if (selectedAlerts!=null && selectedAlerts.length>0)
		{
			log.debug("====== deleteFlags ==========> >>>>>>  " + selectedAlerts.length);
			long[] selectedAlertsIds = new long [selectedAlerts.length];
			int count=0;
			for(String selectedTrigger : selectedAlerts){
				selectedAlertsIds[count]= Long.parseLong(selectedTrigger.trim());
				count++;
			}
			surveyService.deleteAlerts(selectedAlertsIds);
		}
		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "displayAfterDelete");
		
		
		
		return displayAfterDelete (  request,  response,  command,  errors );
	}
public ModelAndView displayAfterDelete ( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ){
	com.softech.vu360.lms.vo.VU360User logInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
	    ManageMyAlertForm form = (ManageMyAlertForm)command;
		List<Alert> alerts = new ArrayList<Alert>();
		alerts=surveyService.getAllAlertByCreatedUserId(logInUser.getId());
	 	List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
		session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();

		for(Alert alert : alerts){
			MngAlert mngAlt =new MngAlert();
			if(alert.getIsDelete()==false){
				mngAlt.setAlert(alert);
				mngAlerts.add(mngAlt);	
			}
		}
		

		//============================For Sorting============================
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) {
			if(session.getAttribute("pageCurrIndex")==null)pageIndex="0";
			else pageIndex = session.getAttribute("pageCurrIndex").toString();
		}
		Map<String,Object> pagerAttributeMap = new HashMap<String,Object>();
		pagerAttributeMap.put("pageIndex",pageIndex);

		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					MyAlertSort sort = new MyAlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					MyAlertSort sort = new MyAlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			} 
		}	

		//Set<SurveyResult> surveyResults = surveyService.getAllSurvey(surveyowner);
		form.setMngAlerts(mngAlerts);
		// TODO
		return new ModelAndView(displayTemplate);
	}
public ModelAndView showEditAlertPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	ManageMyAlertForm form = (ManageMyAlertForm)command;
	String alertId=request.getParameter("alertId");
	Alert alert=null;
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	if (alertId !=null);
	alert=surveyService.loadAlertForUpdate(Long.parseLong(alertId));
	form.setAlert(alert);
	if( StringUtils.isBlank(form.getAlert().getAlertMessageBody() ) )
		form.getAlert().setAlertMessageBody("");
	form.getAlert().setAlertMessageBody(form.getAlert().getAlertMessageBody().replaceAll("\"","'"));

	form.getAlert().setAlertMessageBody( form.getAlert().getAlertMessageBody().replaceAll("\r\n", "") );
	form.getAlert().setAlertMessageBody( form.getAlert().getAlertMessageBody().replaceAll("\n", "<br>") );
	form.getAlert().setAlertMessageBody( form.getAlert().getAlertMessageBody().replaceAll("\r", "<br>") );
	form.getAlert().setFromName(form.getAlert().getFromName());
	form.getAlert().setAlertSubject(form.getAlert().getAlertSubject());
	
	/*LMS-11099 */
	List<AvailableAlertEvent> availablealertevents = new ArrayList<AvailableAlertEvent>();
	for(AvailableAlertEvent availablealert : surveyService.getAllAvailableAlertEvents())
	{
		if(!availablealert.getDbColumnName().equals("CE_DUE_REMINDER"))
		{
			availablealertevents.add(availablealert);
		}
			
	}
	form.setAvailableAlertEvents(availablealertevents);	
	//form.setAvailableAlertEvents(surveyService.getAllAvailableAlertEvents());
	
	if( (surveyService.getAllAlertTriggerByAlertId(form.getAlert().getId())).size()>0){
		form.setTrigger( (surveyService.getAllAlertTriggerByAlertId(form.getAlert().getId())).get(0) );
		if(form.getTrigger().getDaysAfterEvent() != null){
			form.setDays(form.getTrigger().getDaysAfterEvent());
			form.setAfter(true);
			form.setDate("");
			form.setBefore(false);
		}
		if(form.getTrigger().getDaysBeforeEvent() != null){
			form.setDays(form.getTrigger().getDaysBeforeEvent());
			form.setAfter(false);
			form.setBefore(true);
			form.setDate("");
		}
		if(form.getTrigger().getTriggerSingleDate() != null){
			form.setDays(null);
			form.setAfter(false);
			form.setBefore(false);		
			form.setDate(df.format(form.getTrigger().getTriggerSingleDate()));
		}
		
		form.setSelectedAvailableAlertEvents(form.getTrigger().getAvailableAlertEvents());
	}
	
	/*LMS-11099 */
	if(form.getTrigger()!=null){
		if(form.getTrigger().getIsRecurring()){
			form.setRecurring(form.getTrigger().getIsRecurring());
			form.setRecurrrenceSchedule(form.getTrigger().getRecurrrenceSchedule());
			//form.setDailyRecurrrenceEvery(form.getTrigger().getDailyRecurrrenceEvery());
			if(form.getTrigger().getDailyRecurrrenceEveryWeekDay()!=null){
			if(form.getTrigger().getDailyRecurrrenceEveryWeekDay().equalsIgnoreCase("everyDay")){
				form.setDailyRecurrrenceEveryWeekDay(form.getTrigger().getDailyRecurrrenceEveryWeekDay());
				form.setDailyRecurrrenceEveryDay(form.getTrigger().getDailyRecurrrenceEveryDay());
			}
			else if(form.getTrigger().getDailyRecurrrenceEveryWeekDay().equalsIgnoreCase("everyWeekDay")){
				form.setDailyRecurrrenceEveryWeekDay(form.getTrigger().getDailyRecurrrenceEveryWeekDay());
				form.setDailyRecurrrenceEveryDay(null);
			}
		}
			form.setWeeklyRecureEveryWeek(form.getTrigger().getWeeklyRecureEveryWeek());
			//form.setWeeklyRecureEveryWeekOn(form.getTrigger().getWeeklyRecureEveryWeekOn());
			form.setMonthlyRecurrenceTheEvery(form.getTrigger().getMonthlyRecurrenceTheEvery());
			//form.setMonthlyRecurrenceType(form.getTrigger().getMonthlyRecurrenceType());
			
			form.setMonthlyRecurrenceMonth(form.getTrigger().getMonthlyRecurrenceMonth());
			form.setMonthlyRecurrenceTheEvery(form.getTrigger().getMonthlyRecurrenceTheEvery());
			if(form.getTrigger().getYearlyRecurrenceTheEvery()!=null){
				if(form.getTrigger().getYearlyRecurrenceTheEvery().equalsIgnoreCase("every")){
					form.setYearlyRecurrenceTheEvery(form.getTrigger().getYearlyRecurrenceTheEvery());
					form.setYearlyEveryMonthName(form.getTrigger().getYearlyEveryMonthName());
					form.setYearlyEveryMonthDay(form.getTrigger().getYearlyEveryMonthDay());
				}
				else if(form.getTrigger().getYearlyRecurrenceTheEvery().equalsIgnoreCase("the")){
						form.setYearlyRecurrenceTheEvery(form.getTrigger().getYearlyRecurrenceTheEvery());
						form.setYearlyTheDayTerm(form.getTrigger().getYearlyTheDayTerm());
						form.setYearlyTheDayDescription(form.getTrigger().getYearlyTheDayDescription());
						form.setYearlyTheMonthDescription(form.getTrigger().getYearlyTheMonthDescription());
						form.setYearlyEveryMonthDay(0);
			}
				}
			if(form.getTrigger().getMonthlyRecurrenceTheEvery()!=null){
			if(form.getTrigger().getMonthlyRecurrenceTheEvery().equalsIgnoreCase("day")){
				form.getTrigger().setMonthlyRecurrenceTheEvery("day");
				form.setMonthlyRecurrence(String.valueOf(form.getTrigger().getMonthlyRecurrenceMonth()));
				form.setMonthlyRecurrenceMonth(0);
				form.setMonthlyRecurrenceType(form.getTrigger().getMonthlyRecurrenceType());
				form.setTheColMonthly(null);
				form.setMonthlyRecurrenceTypeDescriptor(null);
				}
				else if(form.getMonthlyRecurrenceTheEvery().equalsIgnoreCase("the"))
				{
					form.getTrigger().setMonthlyRecurrenceTheEvery("the");
					form.setMonthlyRecurrenceMonth(form.getTrigger().getMonthlyRecurrenceMonth());
					form.setMonthlyRecurrence(null);
					form.setTheColMonthly(String.valueOf(form.getTrigger().getMonthlyRecurrenceType()));
					form.setMonthlyRecurrenceType(0);
					form.setMonthlyRecurrenceTypeDescriptor(form.getTrigger().getMonthlyRecurrenceTypeDescriptor());
				}
			}
			if(form.getTrigger().getRangeOfRecurrrenceEndAfter()!=0 && form.getTrigger().getRangeOfRecurrrenceEndDay()==null){
				form.setRangeRadio("1");
				
			}
			else if(form.getTrigger().getRangeOfRecurrrenceEndAfter()==0 && form.getTrigger().getRangeOfRecurrrenceEndDay()==null){
				form.setRangeRadio("0");
			}
			else{
				form.setRangeRadio("2");
			}
			form.setRangeOfRecurrrenceEndAfter(form.getTrigger().getRangeOfRecurrrenceEndAfter()+"");
			form.setRangeOfRecurrrenceStartTime(form.getTrigger().getRangeOfRecurrrenceStartTime());
			form.setRangeOfRecurrrenceEndTime(form.getTrigger().getRangeOfRecurrrenceEndTime());
			form.setRangeOfRecurrrenceDuration(form.getTrigger().getRangeOfRecurrrenceDuration());
			if(form.getTrigger().getRangeOfRecurrrenceStartDay()!=null){
			form.setRangeOfRecurrrenceStartDay(df.format(form.getTrigger().getRangeOfRecurrrenceStartDay()));
			}
			else 
			{
				form.setRangeOfRecurrrenceStartDay("");
			}
			if(form.getTrigger().getRangeOfRecurrrenceEndDay()!=null){
			form.setRangeOfRecurrrenceEndDay(df.format(form.getTrigger().getRangeOfRecurrrenceEndDay()));
			}
			else 
			{
				form.setRangeOfRecurrrenceEndDay("");
			}
			if(form.getTrigger().getWeeklyRecureEveryWeekOn()!=null){
			form.setWeeklyRecureEveryWeekOn(form.getTrigger().getWeeklyRecureEveryWeekOn().split(":"));
			}
			
			
		}
	}
	// surveyService.gets
	return new ModelAndView(editAlertTemplate);
}

public ModelAndView updateAlertDetails(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	ManageMyAlertForm form = (ManageMyAlertForm)command;
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	surveyService.addAlert(form.getAlert());
	
	if(surveyService.getAllAlertTriggerByAlertId(form.getAlert().getId()).size() > 0){
		form.setTrigger( (surveyService.getAllAlertTriggerByAlertId(form.getAlert().getId())).get(0) );
		form.setTrigger( (surveyService.loadAlertTriggerForUpdate(form.getTrigger().getId())) );
		if(form.getAvailableAlertEvents()!=null)
		{
			int i=0, j=0;
			AvailableAlertEvent item0;
			String selecteditem0;
			 List<AvailableAlertEvent> selectedAvailableAlertEvents = new ArrayList<AvailableAlertEvent>();

			
				if(form.getSelectedEventsId()!=null)
					for(;i<form.getAvailableAlertEvents().size();){
						item0 = form.getAvailableAlertEvents().get(i);
						for(;j<form.getSelectedEventsId().length;){
							selecteditem0 = (form.getSelectedEventsId())[j];
							if(item0 != null){
								if(item0.getId()==(Long.parseLong(selecteditem0))){
									//addRecipientForm.getSelectedLearnerGroupList().add(item);
									selectedAvailableAlertEvents.add(item0);



									break;
								}
							}
							j++;
						}
						j=0;
						i++;
					}
			form.setSelectedAvailableAlertEvents(selectedAvailableAlertEvents);
			form.getTrigger().setAvailableAlertEvents(selectedAvailableAlertEvents);
			
		}
		if(request.getParameter("before")!=null && request.getParameter("after")!=null){
			if(request.getParameter("before").equals("true") && request.getParameter("after").equals("true")){
				form.getTrigger().setDaysAfterEvent(form.getDays());
				form.getTrigger().setDaysBeforeEvent(form.getDays());
			}
		}
		if(request.getParameter("after")!=null){
			if(request.getParameter("after").equals("true")){
				form.setBefore(false);
				form.getTrigger().setDaysAfterEvent(form.getDays());
				form.getTrigger().setDaysBeforeEvent(null);
			}
		}
		if(request.getParameter("before")!=null){
			if(request.getParameter("before").equals("true")){
			form.setAfter(false);
			form.getTrigger().setDaysAfterEvent(null);
			form.getTrigger().setDaysBeforeEvent(form.getDays());
		}
		}
		if(form.getEvent_date()!=null && form.getEvent_date().equals("date")){
			form.getTrigger().setAvailableAlertEvents(null);
			if(form.getDate() != null && form.getDate() !=""){
				Date date = new Date(form.getDate());
				form.getTrigger().setTriggerSingleDate(date);
			}
		}
		else if(form.getEvent_date()!=null && form.getEvent_date().equals("event")){
			form.getTrigger().setTriggerSingleDate(null);
			int i=0, j=0;
			AvailableAlertEvent item0;
			String selecteditem0;
			List<AvailableAlertEvent> selectedevents = new ArrayList<AvailableAlertEvent>();		
			if( form.getSelectedEventsId() != null){
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
				form.setSelectedAvailableAlertEvents(selectedevents);
				form.getTrigger().setAvailableAlertEvents(selectedevents);
			}	
		}
		
		if(form.isRecurring()){
			form.getTrigger().setIsRecurring(form.isRecurring());
			form.getTrigger().setRecurrrenceSchedule(form.getRecurrrenceSchedule());
			
			form.getTrigger().setDailyRecurrrenceEveryDay(form.getDailyRecurrrenceEveryDay());
			form.getTrigger().setDailyRecurrrenceEveryWeekDay(form.getDailyRecurrrenceEveryWeekDay());
			form.getTrigger().setWeeklyRecureEveryWeek(form.getWeeklyRecureEveryWeek());
			
			form.getTrigger().setMonthlyRecurrenceTheEvery(form.getMonthlyRecurrenceTheEvery());
			//form.getTrigger().setMonthlyRecurrenceType(form.getMonthlyRecurrenceType());
			
			if(form.getMonthlyRecurrenceTheEvery()!=null){
			if(form.getMonthlyRecurrenceTheEvery().equalsIgnoreCase("day")){
				form.getTrigger().setMonthlyRecurrenceTheEvery("day");
				form.getTrigger().setMonthlyRecurrenceMonth(Integer.parseInt(form.getMonthlyRecurrence()));
				form.getTrigger().setMonthlyRecurrenceType(form.getMonthlyRecurrenceType());
				}
				else if(form.getMonthlyRecurrenceTheEvery().equalsIgnoreCase("the"))
				{
					form.getTrigger().setMonthlyRecurrenceTheEvery("the");
					form.getTrigger().setMonthlyRecurrenceMonth(form.getMonthlyRecurrenceMonth());
					form.getTrigger().setMonthlyRecurrenceType(Integer.parseInt(form.getTheColMonthly()));
					form.getTrigger().setMonthlyRecurrenceTypeDescriptor(form.getMonthlyRecurrenceTypeDescriptor());
				}
			}
			
			//form.getTrigger().setMonthlyRecurrenceTheEvery(form.getMonthlyRecurrenceTheEvery());
			if(form.getYearlyRecurrenceTheEvery()!=null){
				if(form.getYearlyRecurrenceTheEvery().equalsIgnoreCase("every")){
					form.getTrigger().setYearlyRecurrenceTheEvery(form.getYearlyRecurrenceTheEvery());
					form.getTrigger().setYearlyEveryMonthName(form.getYearlyEveryMonthName());
					form.getTrigger().setYearlyEveryMonthDay(form.getYearlyEveryMonthDay());
				}
				else if(form.getYearlyRecurrenceTheEvery().equalsIgnoreCase("the")){
						form.getTrigger().setYearlyRecurrenceTheEvery(form.getYearlyRecurrenceTheEvery());
						form.getTrigger().setYearlyTheDayTerm(form.getYearlyTheDayTerm());
						form.getTrigger().setYearlyTheDayDescription(form.getYearlyTheDayDescription());
						form.getTrigger().setYearlyTheMonthDescription(form.getYearlyTheMonthDescription());
				}
			}
			form.getTrigger().setYearlyEveryMonthName(form.getYearlyEveryMonthName());
			
			form.getTrigger().setYearlyEveryMonthDay(form.getYearlyEveryMonthDay());
	
			form.getTrigger().setYearlyTheDayTerm(form.getYearlyTheDayTerm());
			form.getTrigger().setYearlyTheDayDescription(form.getYearlyTheDayDescription());
			form.getTrigger().setYearlyTheMonthDescription(form.getYearlyTheMonthDescription());
			if(request.getParameter("rangeOfRecurrrenceEndAfter")!=""){
				form.getTrigger().setRangeOfRecurrrenceEndAfter(Integer.parseInt(request.getParameter("rangeOfRecurrrenceEndAfter")));
			}
			/*if(form.getRangeOfRecurrrenceEndAfter()!=""){
				form.getTrigger().setRangeOfRecurrrenceEndAfter(Integer.parseInt(form.getRangeOfRecurrrenceEndAfter()));
			}*/
			form.getTrigger().setRangeOfRecurrrenceStartTime(form.getRangeOfRecurrrenceStartTime());
			form.getTrigger().setRangeOfRecurrrenceEndTime(form.getRangeOfRecurrrenceEndTime());
			form.getTrigger().setRangeOfRecurrrenceDuration(form.getRangeOfRecurrrenceDuration());
			if(form.getRangeOfRecurrrenceStartDay()!=null && form.getRangeOfRecurrrenceStartDay()!=""){
				form.getTrigger().setRangeOfRecurrrenceStartDay(df.parse(form.getRangeOfRecurrrenceStartDay()));
			}
			if(form.getRangeOfRecurrrenceEndDay()!=null && form.getRangeOfRecurrrenceEndDay()!=""){
				
				form.getTrigger().setRangeOfRecurrrenceEndDay(df.parse(form.getRangeOfRecurrrenceEndDay()));
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
			form.getTrigger().setWeeklyRecureEveryWeekOn(day);
			if(form.getRangeRadio()!=null){
			if(form.getRangeRadio().equals("0")){
				form.getTrigger().setRangeOfRecurrrenceEndDay(null);
				form.getTrigger().setRangeOfRecurrrenceEndAfter(0);
			}
			else if(form.getRangeRadio().equals("1")){
				form.getTrigger().setRangeOfRecurrrenceEndDay(null);
				form.getTrigger().setRangeOfRecurrrenceEndAfter(Integer.parseInt(form.getRangeOfRecurrrenceEndAfter()));
			}
			else{
				form.getTrigger().setRangeOfRecurrrenceEndAfter(0);
			}
						
		}
		}
		ManageMyAlertValidator validator=(ManageMyAlertValidator)this.getValidator();
		validator.validateFirstPage(form, errors);
		if( errors.hasErrors() ) {

			return new ModelAndView(editAlertTemplate);
		}
		surveyService.addAlertTrigger(form.getTrigger());
		
		
		
	}
	return new ModelAndView(displayTemplate);
}
public ModelAndView editSaveAlert(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception{
	
	return new ModelAndView(editAlertTemplate);
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
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
	public SurveyService getSurveyService() {
		return surveyService;
	}
	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
	public String getEditAlertTemplate() {
		return editAlertTemplate;
	}
	public void setEditAlertTemplate(String editAlertTemplate) {
		this.editAlertTemplate = editAlertTemplate;
	}



}
