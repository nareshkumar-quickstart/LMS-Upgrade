package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


public class ManageMyAlertForm  implements ILMSBaseInterface{
	private String rangeRadio;
	private long id = 0;
	  private String alertName = "";
	  private boolean recurring=false;
	  private String monthlyRecurrence;
	  private String theColMonthly;
	  private static final long serialVersionUID = 1L;
	  private List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
	    private List<Alert> alerts = new ArrayList<Alert>();
	    private List<AlertTrigger> triggers = new ArrayList<AlertTrigger>();

		private AlertTrigger trigger = null;
	    private Alert alert = new Alert();
	    
	    
	    private Integer days=null;
		private boolean before=false;
		private boolean after=false;
		private String date=null;
		
		private String recurrrenceSchedule = null;
		// for daily
		private int dailyRecurrrenceEvery;
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
		
		
		//***********************************************************************************************************
		
		private List<AvailableAlertEvent> availableAlertEvents = new ArrayList<AvailableAlertEvent>();
		private List<AvailableAlertEvent> selectedAvailableAlertEvents = new ArrayList<AvailableAlertEvent>();
		
		
		private String[] selectedEventsId=null;
		
		private String event_date=null;

		public String getAlertName() {
			return alertName;
		}

		public void setAlertName(String alertName) {
			this.alertName = alertName;
		}

		public List<Alert> getAlerts() {
			return alerts;
		}

		public void setAlerts(List<Alert> alerts) {
			this.alerts = alerts;
		}

		public Alert getAlert() {
			return alert;
		}

		public void setAlert(Alert alert) {
			this.alert = alert;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public List<MngAlert> getMngAlerts() {
			return mngAlerts;
		}

		public void setMngAlerts(List<MngAlert> mngAlerts) {
			this.mngAlerts = mngAlerts;
		}

		

		public Integer getDays() {
			return days;
		}

		public void setDays(Integer days) {
			this.days = days;
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

		

		public List<AvailableAlertEvent> getAvailableAlertEvents() {
			return availableAlertEvents;
		}

		public void setAvailableAlertEvents(
				List<AvailableAlertEvent> availableAlertEvents) {
			this.availableAlertEvents = availableAlertEvents;
		}

		public List<AvailableAlertEvent> getSelectedAvailableAlertEvents() {
			return selectedAvailableAlertEvents;
		}

		public void setSelectedAvailableAlertEvents(
				List<AvailableAlertEvent> selectedAvailableAlertEvents) {
			this.selectedAvailableAlertEvents = selectedAvailableAlertEvents;
		}

		

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getEvent_date() {
			return event_date;
		}

		public void setEvent_date(String event_date) {
			this.event_date = event_date;
		}

		public String[] getSelectedEventsId() {
			return selectedEventsId;
		}

		public void setSelectedEventsId(String[] selectedEventsId) {
			this.selectedEventsId = selectedEventsId;
		}

		public String getRecurrrenceSchedule() {
			return recurrrenceSchedule;
		}

		public void setRecurrrenceSchedule(String recurrrenceSchedule) {
			this.recurrrenceSchedule = recurrrenceSchedule;
		}

		public int getDailyRecurrrenceEvery() {
			return dailyRecurrrenceEvery;
		}

		public void setDailyRecurrrenceEvery(int dailyRecurrrenceEvery) {
			this.dailyRecurrrenceEvery = dailyRecurrrenceEvery;
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

		public String[] getWeeklyRecureEveryWeekOn() {
			return weeklyRecureEveryWeekOn;
		}

		public void setWeeklyRecureEveryWeekOn(String[] weeklyRecureEveryWeekOn) {
			this.weeklyRecureEveryWeekOn = weeklyRecureEveryWeekOn;
		}

		public String getMonthlyRecurrenceTheEvery() {
			return monthlyRecurrenceTheEvery;
		}

		public void setMonthlyRecurrenceTheEvery(String monthlyRecurrenceTheEvery) {
			this.monthlyRecurrenceTheEvery = monthlyRecurrenceTheEvery;
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

		public String getYearlyRecurrenceTheEvery() {
			return yearlyRecurrenceTheEvery;
		}

		public void setYearlyRecurrenceTheEvery(String yearlyRecurrenceTheEvery) {
			this.yearlyRecurrenceTheEvery = yearlyRecurrenceTheEvery;
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

		public String getRangeOfRecurrrenceStartDay() {
			return rangeOfRecurrrenceStartDay;
		}

		public void setRangeOfRecurrrenceStartDay(String rangeOfRecurrrenceStartDay) {
			this.rangeOfRecurrrenceStartDay = rangeOfRecurrrenceStartDay;
		}

		public String getRangeOfRecurrrenceEndAfter() {
			return rangeOfRecurrrenceEndAfter;
		}

		public void setRangeOfRecurrrenceEndAfter(String rangeOfRecurrrenceEndAfter) {
			this.rangeOfRecurrrenceEndAfter = rangeOfRecurrrenceEndAfter;
		}

		public String getRangeOfRecurrrenceEndDay() {
			return rangeOfRecurrrenceEndDay;
		}

		public void setRangeOfRecurrrenceEndDay(String rangeOfRecurrrenceEndDay) {
			this.rangeOfRecurrrenceEndDay = rangeOfRecurrrenceEndDay;
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

		public List<AlertTrigger> getTriggers() {
			return triggers;
		}

		public void setTriggers(List<AlertTrigger> triggers) {
			this.triggers = triggers;
		}

		public AlertTrigger getTrigger() {
			return trigger;
		}

		public void setTrigger(AlertTrigger trigger) {
			this.trigger = trigger;
		}

		public boolean isRecurring() {
			return recurring;
		}

		public void setRecurring(boolean recurring) {
			this.recurring = recurring;
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

		public String getMonthlyRecurrence() {
			return monthlyRecurrence;
		}

		public void setMonthlyRecurrence(String monthlyRecurrence) {
			this.monthlyRecurrence = monthlyRecurrence;
		}

		public String getTheColMonthly() {
			return theColMonthly;
		}

		public void setTheColMonthly(String theColMonthly) {
			this.theColMonthly = theColMonthly;
		}

		
	   

	  

}
