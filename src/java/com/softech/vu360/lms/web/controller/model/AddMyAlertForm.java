package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class AddMyAlertForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public AddMyAlertForm(){
		
	}
	private Alert alert = new Alert();
		
	public Alert getAlert() {
		return alert;
	}
	public void setAlert(Alert alert) {
		this.alert = alert;

}
	private String rangeRadio;
	private String triggerName="";
	private Integer days=null;
	private boolean before=false;
	private boolean after=false;
	private String date=null;
	private boolean recurring=false;
	private String[] selectedEventsId=null;
	private AlertTrigger alertTrigger;
	private String alertId="";
	private String event_date=null;
	private String alertMessageBody = "default";
	private String recurrrenceSchedule = null;
	// for daily
	private String dailyRecurrrenceEveryDay=null;
	private String dailyRecurrrenceEveryWeekDay = null;
	
	// for weekly
	private int weeklyRecureEveryWeek;
	private String weeklyRecureEveryWeekOn[];
	
	// for monthly
	private String monthlyRecurrenceTheEvery;
	private int monthlyRecurrenceType;	// int value
	private String monthlyRecurrenceTypeDescriptor; // month day year
	private int monthlyRecurrenceMonth;
	private String theColMonthly;
	
	
	// for yearly
	private String yearlyRecurrenceTheEvery;
	private String yearlyEveryMonthName;
	private int yearlyEveryMonthDay;
	
	
	private String yearlyTheDayTerm;
	private String yearlyTheDayDescription;
	private String yearlyTheMonthDescription;
	
	
	
	
	private String rangeOfRecurrrenceStartDay = null;
	private String rangeOfRecurrrenceEndAfter = null ;
	private String rangeOfRecurrrenceEndDay = null;
	private String rangeOfRecurrrenceStartTime = null;
	private String rangeOfRecurrrenceEndTime = null;
	private String rangeOfRecurrrenceDuration = null;
	private String monthlyRecurrence;
	
	
	
	private List<AvailableAlertEvent> availableAlertEvents = new ArrayList<AvailableAlertEvent>();
	private List<AvailableAlertEvent> selectedAvailableAlertEvents = new ArrayList<AvailableAlertEvent>();
	
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	
	public boolean isBefore() {
		return before;
	}
	public void setBefore(boolean before) {
		this.before = before;
	}
	public boolean isAfter() {
		return after;
	}
	public void setAfter(boolean after) {
		this.after = after;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isRecurring() {
		return recurring;
	}
	public void setRecurring(boolean recurring) {
		this.recurring = recurring;
	}
	public List<AvailableAlertEvent> getAvailableAlertEvents() {
		return availableAlertEvents;
	}
	public void setAvailableAlertEvents(
			List<AvailableAlertEvent> availableAlertEvents) {
		this.availableAlertEvents = availableAlertEvents;
	}
	public String[] getSelectedEventsId() {
		return selectedEventsId;
	}
	public void setSelectedEventsId(String[] selectedEventsId) {
		this.selectedEventsId = selectedEventsId;
	}
	public AlertTrigger getAlertTrigger() {
		return alertTrigger;
	}
	public void setAlertTrigger(AlertTrigger alertTrigger) {
		this.alertTrigger = alertTrigger;
	}
	public String getAlertId() {
		return alertId;
	}
	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public List<AvailableAlertEvent> getSelectedAvailableAlertEvents() {
		return selectedAvailableAlertEvents;
	}
	public void setSelectedAvailableAlertEvents(
			List<AvailableAlertEvent> selectedAvailableAlertEvents) {
		this.selectedAvailableAlertEvents = selectedAvailableAlertEvents;
	}
	public String getRecurrrenceSchedule() {
		return recurrrenceSchedule;
	}
	public void setRecurrrenceSchedule(String recurrrenceSchedule) {
		this.recurrrenceSchedule = recurrrenceSchedule;
	}
	
	public String getDailyRecurrrenceEveryWeekDay() {
		return dailyRecurrrenceEveryWeekDay;
	}
	public void setDailyRecurrrenceEveryWeekDay(String dailyRecurrrenceEveryWeekDay) {
		this.dailyRecurrrenceEveryWeekDay = dailyRecurrrenceEveryWeekDay;
	}
	public int getWeeklyRecureEveryWeek() {
		return weeklyRecureEveryWeek;
	}
	public void setWeeklyRecureEveryWeek(int weeklyRecureEveryWeek) {
		this.weeklyRecureEveryWeek = weeklyRecureEveryWeek;
	}
	
	public int getMonthlyRecurrenceType() {
		return monthlyRecurrenceType;
	}
	public void setMonthlyRecurrenceType(int monthlyRecurrenceType) {
		this.monthlyRecurrenceType = monthlyRecurrenceType;
	}
	public String getMonthlyRecurrenceTypeDescriptor() {
		return monthlyRecurrenceTypeDescriptor;
	}
	public void setMonthlyRecurrenceTypeDescriptor(
			String monthlyRecurrenceTypeDescriptor) {
		this.monthlyRecurrenceTypeDescriptor = monthlyRecurrenceTypeDescriptor;
	}
	public int getMonthlyRecurrenceMonth() {
		return monthlyRecurrenceMonth;
	}
	public void setMonthlyRecurrenceMonth(int monthlyRecurrenceMonth) {
		this.monthlyRecurrenceMonth = monthlyRecurrenceMonth;
	}
	public String getYearlyEveryMonthName() {
		return yearlyEveryMonthName;
	}
	public void setYearlyEveryMonthName(String yearlyEveryMonthName) {
		this.yearlyEveryMonthName = yearlyEveryMonthName;
	}
	public int getYearlyEveryMonthDay() {
		return yearlyEveryMonthDay;
	}
	public void setYearlyEveryMonthDay(int yearlyEveryMonthDay) {
		this.yearlyEveryMonthDay = yearlyEveryMonthDay;
	}
	public String getYearlyTheDayTerm() {
		return yearlyTheDayTerm;
	}
	public void setYearlyTheDayTerm(String yearlyTheDayTerm) {
		this.yearlyTheDayTerm = yearlyTheDayTerm;
	}
	public String getYearlyTheDayDescription() {
		return yearlyTheDayDescription;
	}
	public void setYearlyTheDayDescription(String yearlyTheDayDescription) {
		this.yearlyTheDayDescription = yearlyTheDayDescription;
	}
	public String getYearlyTheMonthDescription() {
		return yearlyTheMonthDescription;
	}
	public void setYearlyTheMonthDescription(String yearlyTheMonthDescription) {
		this.yearlyTheMonthDescription = yearlyTheMonthDescription;
	}
	
	
	
	public String getRangeOfRecurrrenceStartTime() {
		return rangeOfRecurrrenceStartTime;
	}
	public void setRangeOfRecurrrenceStartTime(String rangeOfRecurrrenceStartTime) {
		this.rangeOfRecurrrenceStartTime = rangeOfRecurrrenceStartTime;
	}
	public String getRangeOfRecurrrenceEndTime() {
		return rangeOfRecurrrenceEndTime;
	}
	public void setRangeOfRecurrrenceEndTime(String rangeOfRecurrrenceEndTime) {
		this.rangeOfRecurrrenceEndTime = rangeOfRecurrrenceEndTime;
	}
	public String getRangeOfRecurrrenceDuration() {
		return rangeOfRecurrrenceDuration;
	}
	public void setRangeOfRecurrrenceDuration(String rangeOfRecurrrenceDuration) {
		this.rangeOfRecurrrenceDuration = rangeOfRecurrrenceDuration;
	}
	public String getMonthlyRecurrenceTheEvery() {
		return monthlyRecurrenceTheEvery;
	}
	public void setMonthlyRecurrenceTheEvery(String monthlyRecurrenceTheEvery) {
		this.monthlyRecurrenceTheEvery = monthlyRecurrenceTheEvery;
	}
	public String getYearlyRecurrenceTheEvery() {
		return yearlyRecurrenceTheEvery;
	}
	public void setYearlyRecurrenceTheEvery(String yearlyRecurrenceTheEvery) {
		this.yearlyRecurrenceTheEvery = yearlyRecurrenceTheEvery;
	}
	public String getEvent_date() {
		return event_date;
	}
	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}
	public String getRangeOfRecurrrenceEndAfter() {
		return rangeOfRecurrrenceEndAfter;
	}
	public void setRangeOfRecurrrenceEndAfter(String rangeOfRecurrrenceEndAfter) {
		this.rangeOfRecurrrenceEndAfter = rangeOfRecurrrenceEndAfter;
	}
	public String[] getWeeklyRecureEveryWeekOn() {
		return weeklyRecureEveryWeekOn;
	}
	public void setWeeklyRecureEveryWeekOn(String[] weeklyRecureEveryWeekOn) {
		this.weeklyRecureEveryWeekOn = weeklyRecureEveryWeekOn;
	}
	public String getRangeOfRecurrrenceStartDay() {
		return rangeOfRecurrrenceStartDay;
	}
	public void setRangeOfRecurrrenceStartDay(String rangeOfRecurrrenceStartDay) {
		this.rangeOfRecurrrenceStartDay = rangeOfRecurrrenceStartDay;
	}
	public String getRangeOfRecurrrenceEndDay() {
		return rangeOfRecurrrenceEndDay;
	}
	public void setRangeOfRecurrrenceEndDay(String rangeOfRecurrrenceEndDay) {
		this.rangeOfRecurrrenceEndDay = rangeOfRecurrrenceEndDay;
	}
	public String getDailyRecurrrenceEveryDay() {
		return dailyRecurrrenceEveryDay;
	}
	public void setDailyRecurrrenceEveryDay(String dailyRecurrrenceEveryDay) {
		this.dailyRecurrrenceEveryDay = dailyRecurrrenceEveryDay;
	}
	public String getRangeRadio() {
		return rangeRadio;
	}
	public void setRangeRadio(String rangeRadio) {
		this.rangeRadio = rangeRadio;
	}
	public String getTheColMonthly() {
		return theColMonthly;
	}
	public void setTheColMonthly(String theColMonthly) {
		this.theColMonthly = theColMonthly;
	}
	public String getMonthlyRecurrence() {
		return monthlyRecurrence;
	}
	public void setMonthlyRecurrence(String monthlyRecurrence) {
		this.monthlyRecurrence = monthlyRecurrence;
	}
	public String getAlertMessageBody() {
		return alertMessageBody;
	}
	public void setAlertMessageBody(String alertMessageBody) {
		this.alertMessageBody = alertMessageBody;
	}
	
}