package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class TriggerForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String rangeRadio;
	private String triggerName=null;
	private List<Credential> credentials;
	
	private Integer days=null;
	private Long licenseTypeID=null;
	private String licenseExpireDate=null;
	private String event_date=null;
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	private String[] selectedEventsId=null;
	private String selectedEventsText=null;
	private boolean before=false;
	private boolean after=false;
	private String date=null;
	private boolean recurring=false;
	private String alertId="";
	
	private Long selectedCredentialId;
	
	
	//***********************************************************************************************************
	
	
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
	
	
	//***********************************************************************************************************
	
	public List<Credential> getCredentials() {
		return credentials;
	}
	public void setCredentials(List<Credential> credentials) {
		this.credentials = credentials;
	}
	
	
	public String getAlertId() {
		return alertId;
	}
	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
	private List<AvailableAlertEvent> availableAlertEvents = new ArrayList<AvailableAlertEvent>();
	private List<AvailableAlertEvent> selectedavailableAlertEvents = new ArrayList<AvailableAlertEvent>();
	private AlertTrigger alertTrigger;
	
	public Long getSelectedCredentialId() {
		return selectedCredentialId;
	}
	public void setSelectedCredentialId(Long selectedCredentialId) {
		this.selectedCredentialId = selectedCredentialId;
	}
	
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
	
	public String[] getSelectedEventsId() {
		return selectedEventsId;
	}
	public void setSelectedEventsId(String[] selectedEventsId) {
		this.selectedEventsId = selectedEventsId;
	}
	public List<AvailableAlertEvent> getAvailableAlertEvents() {
		return availableAlertEvents;
	}
	public void setAvailableAlertEvents(
			List<AvailableAlertEvent> availableAlertEvents) {
		this.availableAlertEvents = availableAlertEvents;
	}
	public AlertTrigger getAlertTrigger() {
		return alertTrigger;
	}
	public void setAlertTrigger(AlertTrigger alertTrigger) {
		this.alertTrigger = alertTrigger;
	}
	public String getEvent_date() {
		return event_date;
	}
	public void setEvent_date(String event_date) {
		this.event_date = event_date;
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
	public String[] getWeeklyRecureEveryWeekOn() {
		return weeklyRecureEveryWeekOn;
	}
	public void setWeeklyRecureEveryWeekOn(String[] weeklyRecureEveryWeekOn) {
		this.weeklyRecureEveryWeekOn = weeklyRecureEveryWeekOn;
	}
	public String getRecurrrenceSchedule() {
		return recurrrenceSchedule;
	}
	public void setRecurrrenceSchedule(String recurrrenceSchedule) {
		this.recurrrenceSchedule = recurrrenceSchedule;
	}
	public void setRangeOfRecurrrenceEndAfter(String rangeOfRecurrrenceEndAfter) {
		this.rangeOfRecurrrenceEndAfter = rangeOfRecurrrenceEndAfter;
	}
	public String getRangeOfRecurrrenceEndAfter() {
		return rangeOfRecurrrenceEndAfter;
	}
	public void setRangeOfRecurrrenceStartDay(String rangeOfRecurrrenceStartDay) {
		this.rangeOfRecurrrenceStartDay = rangeOfRecurrrenceStartDay;
	}
	public void setRangeOfRecurrrenceEndDay(String rangeOfRecurrrenceEndDay) {
		this.rangeOfRecurrrenceEndDay = rangeOfRecurrrenceEndDay;
	}
	public String getRangeOfRecurrrenceStartDay() {
		return rangeOfRecurrrenceStartDay;
	}
	public String getRangeOfRecurrrenceEndDay() {
		return rangeOfRecurrrenceEndDay;
	}
	public int getMonthlyRecurrenceMonth() {
		return monthlyRecurrenceMonth;
	}
	public void setMonthlyRecurrenceMonth(int monthlyRecurrenceMonth) {
		this.monthlyRecurrenceMonth = monthlyRecurrenceMonth;
	}
	public int getYearlyEveryMonthDay() {
		return yearlyEveryMonthDay;
	}
	public void setYearlyEveryMonthDay(int yearlyEveryMonthDay) {
		this.yearlyEveryMonthDay = yearlyEveryMonthDay;
	}
	public String getDailyRecurrrenceEveryDay() {
		return dailyRecurrrenceEveryDay;
	}
	public void setDailyRecurrrenceEveryDay(String dailyRecurrrenceEveryDay) {
		this.dailyRecurrrenceEveryDay = dailyRecurrrenceEveryDay;
	}
	public String getTheColMonthly() {
		return theColMonthly;
	}
	public void setTheColMonthly(String theColMonthly) {
		this.theColMonthly = theColMonthly;
	}
	public List<AvailableAlertEvent> getSelectedavailableAlertEvents() {
		return selectedavailableAlertEvents;
	}
	public void setSelectedavailableAlertEvents(
			List<AvailableAlertEvent> selectedavailableAlertEvents) {
		this.selectedavailableAlertEvents = selectedavailableAlertEvents;
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
	public String getLicenseExpireDate() {
		return licenseExpireDate;
	}
	public void setLicenseExpireDate(String licenseExpireDate) {
		this.licenseExpireDate = licenseExpireDate;
	}
	public Long getLicenseTypeID() {
		return licenseTypeID;
	}
	public void setLicenseTypeID(Long licenseTypeID) {
		this.licenseTypeID = licenseTypeID;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getSelectedEventsText() {
		return selectedEventsText;
	}
	public void setSelectedEventsText(String selectedEventsText) {
		this.selectedEventsText = selectedEventsText;
	}

}
