package com.softech.vu360.lms.widget.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertQueue;
import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.vo.WidgetData;
import com.softech.vu360.lms.widget.WidgetLogic;

public class AlertWidgetLogic implements WidgetLogic {

	protected static final Logger log = Logger.getLogger(AlertWidgetLogic.class);
	private static String editAlertLink = "lrn_myAlert.do?method=showEditAlertPage&alertId=";
	protected SurveyService surveyService;
	
	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
	
	@Override
	public Collection<WidgetData> getWidgetDataList(VU360User vu360User, Map<String, Object> params, HttpServletRequest request) {
		log.debug("in getWidgetDataList vu360User.id=" + vu360User.getId() + " vu360User.username=" + vu360User.getUsername() + " params=" + params);
		List<WidgetData> datas = new ArrayList<WidgetData>();
		//String filter = (String) ((params.get("filter")!=null) ? params.get("filter") : "all");
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		List<AlertTrigger> triggerList = null;
		AlertTrigger trigger = null;
		
		List<AlertQueue> alertQueue = surveyService.getAlertQueueByUserId(vu360User.getId());
		for (Iterator<AlertQueue> iterator = alertQueue.iterator(); iterator.hasNext();) {
			AlertQueue alert = iterator.next();
			log.debug(" " + alert.getMessageSubject());
			WidgetData widgetData = new WidgetData();
			Map<String, Object> data = widgetData.getDataMap();
			widgetData.setId((long)datas.size());
//			data.put("alertId", alert.getId().toString());
			data.put("subject", alert.getMessageSubject());
			data.put("messageBody", alert.getMessageBody());
			data.put("date", alert.getEventTime().getMonth()+"/"+alert.getEventTime().getDate()+"/"+(alert.getEventTime().getYear()+1900));
			if(alert.getEventDueDate()!=null)
			data.put("eventDueDate", alert.getEventDueDate().getMonth()+"/"+alert.getEventDueDate().getDate()+"/"+(alert.getEventDueDate().getYear()+1900));
			data.put("eventType", alert.getEventType());
			data.put("triggerType", alert.getTriggerType());
			Alert actualAlert = surveyService.getAlertByID(alert.getAlert_Id());
			data.put("alertId", actualAlert.getId().toString());
			data.put("name", actualAlert.getAlertName());
			data.put("link", editAlertLink  + actualAlert.getId());
			data.put("createdDate", actualAlert.getCreatedDate().getMonth()+"/"+actualAlert.getCreatedDate().getDate()+"/"+(actualAlert.getCreatedDate().getYear()+1900));
			triggerList = (List<AlertTrigger>) surveyService.getAllAlertTriggerByAlertId(alert.getAlert_Id());
			if (triggerList != null && triggerList.size() > 0) {
				trigger = triggerList.get(0) ;
			} else {
				trigger = null;
			}
			if(trigger!= null && trigger.getDaysAfterEvent() != null){
				data.put("numDays", String.valueOf(trigger.getDaysAfterEvent()));
				data.put("before", "false");
				data.put("after", "true");
			}
			if(trigger != null && trigger.getDaysBeforeEvent() != null){
				data.put("numDays", String.valueOf(trigger.getDaysBeforeEvent()));
				data.put("before", "true");
				data.put("after", "false");

			}
			if(trigger!= null && trigger.getTriggerSingleDate() != null){
				data.put("numDays",null);
				data.put("before", "false");
				data.put("after", "false");
			}
			datas.add(widgetData);
		}
		
		return datas;
	}

}
