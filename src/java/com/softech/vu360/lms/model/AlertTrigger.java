package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "ALERTTRIGGER")
public class AlertTrigger  implements SearchableKey{

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "ALERTTRIGGER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ALERTTRIGGER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ALERTTRIGGER_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="ALERT_ID")
	private Alert alert ;
	
	@Column(name = "DAYSAFTEREVENT")
	private Integer daysAfterEvent = null;
	
	@Column(name = "DAYSBEFOREEVENT")
	private Integer daysBeforeEvent = null;
	
	@Column(name = "RECURRING")
	private Boolean isRecurring = Boolean.FALSE;
	
	@Column(name = "TRIGGERNAME")
	private String triggerName = null;
	
	@Column(name = "TRIGGERTYPE")
	private String triggerType = null;
	
	@Column(name = "TRIGGERSINGLEDATE")
	private Date triggerSingleDate=null;
	
	@Column(name = "LICENSETYPEID")
	private Long licenseTypeID = null;  
	
	@Column(name = "LICENSEEXPIRATIONDATE")
	private Date licenseExpiratrionDate = null; 
	
	@Column(name = "ISDELETE")
	private Boolean isDelete = Boolean.FALSE;
	
	@Transient
	private Long selectedCredentialId;
	
	@Column(name = "RECURRENCESCHEDULE")
	private String recurrrenceSchedule = null;
	// for daily
	@Column(name = "DAILYRECURRENCEEVERYDAY")
	private String dailyRecurrrenceEveryDay=null;
	
	@Column(name = "DAILYRECURRENCEEVERYWEEKDAY")
	private String dailyRecurrrenceEveryWeekDay = null;
	
	// for weekly
	@Column(name = "WEEKLYRECUREEVERYWEEK")
	private Integer weeklyRecureEveryWeek=0;
	
	@Column(name = "WEEKLYRECUREEVERYWEEKON")
	private String weeklyRecureEveryWeekOn;
	
	// for monthly
	@Column(name = "MONTHLYRECURRENCETHEEVERY")
	private String monthlyRecurrenceTheEvery;
	
	@Column(name = "MONTHLYRECURRENCETYPE")
	private Integer monthlyRecurrenceType=0;	// int value
	
	@Column(name = "MONTHLYRECURRENCETYPEDESCRIPTOR")
	private String monthlyRecurrenceTypeDescriptor; // month day year
	
	@Column(name = "MONTHLYRECURRENCEMONTH")
	private Integer monthlyRecurrenceMonth=0;
	
	
	// for yearly
	@Column(name = "YEARLYRECURRENCETHEEVERY")
	private String yearlyRecurrenceTheEvery;
	
	@Column(name = "YEARLYEVERYMONTHNAME")
	private String yearlyEveryMonthName;
	
	@Column(name = "YEARLYEVERYMONTHDAY")
	private Integer yearlyEveryMonthDay=0;
	
	@Column(name = "YEARLYTHEDAYTERM")
	private String yearlyTheDayTerm;
	
	@Column(name = "YEARLYTHEDAYDESCRIPTION")
	private String yearlyTheDayDescription;
	
	@Column(name = "YEARLYTHEMONTHDESCRIPTION")
	private String yearlyTheMonthDescription;
	
	
	
	@Column(name = "RANGEOFRECURRENCESTARTDAY")
	private Date rangeOfRecurrrenceStartDay = null;
	
	@Column(name = "RANGEOFRECURRENCEENDAFTER")
	private Integer rangeOfRecurrrenceEndAfter=0 ;
	
	@Column(name = "RANGEOFRECURRENCEENDDAY")
	private Date rangeOfRecurrrenceEndDay = null;
	
	@Column(name = "RANGEOFRECURRENCESTARTTIME")
	private String rangeOfRecurrrenceStartTime = null;
	
	@Column(name = "RANGEOFRECURRENCEENDTIME")
	private String rangeOfRecurrrenceEndTime = null;
	
	@Column(name = "RANGEOFRECURRENCEDURATION")
	private String rangeOfRecurrrenceDuration = null;
	
	@Column(name = "CREATEDDATE")
	private Date createdDate = null;
	
	@Column(name = "UPDATEDDATE")
	private Date updatedDate = null;
	
	@ManyToMany
    @JoinTable(name="ALERTTRIGGER_AVAILABLEALERTEVENT", joinColumns = @JoinColumn(name="ALERTTRIGGER_id"),inverseJoinColumns = @JoinColumn(name="AVAILABLEALERTEVENT_id"))
	private List<AvailableAlertEvent> availableAlertEvents = new ArrayList<AvailableAlertEvent>();
	

	public Long getSelectedCredentialId() {
		return selectedCredentialId;
	}

	public void setSelectedCredentialId(Long selectedCredentialId) {
		this.selectedCredentialId = selectedCredentialId;
	}
	
	public Integer getDaysAfterEvent() {
		return daysAfterEvent;
	}

	public void setDaysAfterEvent(Integer daysAfterEvent) {
		this.daysAfterEvent = daysAfterEvent;
	}

	public Integer getDaysBeforeEvent() {
		return daysBeforeEvent;
	}

	public void setDaysBeforeEvent(Integer daysBeforeEvent) {
		this.daysBeforeEvent = daysBeforeEvent;
	}

	public Boolean getIsRecurring() {
		return isRecurring;
	}

	public void setIsRecurring(Boolean isRecurring) {
		if(isRecurring == null){
			this.isRecurring=Boolean.FALSE;
		}
		else{
			this.isRecurring = isRecurring;
		}
		
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public Date getTriggerSingleDate() {
		return triggerSingleDate;
	}

	public void setTriggerSingleDate(Date triggerSingleDate) {
		this.triggerSingleDate = triggerSingleDate;
	}	

	

	/**
	 * @return the alert
	 */
	public Alert getAlert() {
		return alert;
	}

	/**
	 * @param alert the alert to set
	 */
	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return id.toString();
	}

	public  Boolean isDelete() {
		return isDelete;
	}

	public void setDelete(Boolean isDelete) {
		if(isDelete == null){
			this.isDelete=Boolean.FALSE;
		}
		else{
			this.isDelete = isDelete;
		}		
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

	public Integer getWeeklyRecureEveryWeek() {
		if(weeklyRecureEveryWeek==null){
			weeklyRecureEveryWeek=0;
		}
		return weeklyRecureEveryWeek;
	}

	public void setWeeklyRecureEveryWeek(Integer weeklyRecureEveryWeek) {
		this.weeklyRecureEveryWeek = weeklyRecureEveryWeek;
	}

	public String getWeeklyRecureEveryWeekOn() {
		return weeklyRecureEveryWeekOn;
	}

	public void setWeeklyRecureEveryWeekOn(String weeklyRecureEveryWeekOn) {
		this.weeklyRecureEveryWeekOn = weeklyRecureEveryWeekOn;
	}

	public Integer getMonthlyRecurrenceType() {
		if(monthlyRecurrenceType==null){
			monthlyRecurrenceType=0;
		}
		return monthlyRecurrenceType;
	}

	public void setMonthlyRecurrenceType(Integer monthlyRecurrenceType) {
		this.monthlyRecurrenceType = monthlyRecurrenceType;
	}

	public String getMonthlyRecurrenceTypeDescriptor() {
		return monthlyRecurrenceTypeDescriptor;
	}

	public void setMonthlyRecurrenceTypeDescriptor(
			String monthlyRecurrenceTypeDescriptor) {
		this.monthlyRecurrenceTypeDescriptor = monthlyRecurrenceTypeDescriptor;
	}

	public Integer getMonthlyRecurrenceMonth() {
		if(monthlyRecurrenceMonth==null){
			monthlyRecurrenceMonth=0;
		}
		return monthlyRecurrenceMonth;
	}

	public void setMonthlyRecurrenceMonth(Integer monthlyRecurrenceMonth) {
		this.monthlyRecurrenceMonth = monthlyRecurrenceMonth;
	}

	public String getYearlyEveryMonthName() {
		return yearlyEveryMonthName;
	}

	public void setYearlyEveryMonthName(String yearlyEveryMonthName) {
		this.yearlyEveryMonthName = yearlyEveryMonthName;
	}

	public Integer getYearlyEveryMonthDay() {
		if(yearlyEveryMonthDay==null){
			yearlyEveryMonthDay=0;
		}
		return yearlyEveryMonthDay;
	}

	public void setYearlyEveryMonthDay(Integer yearlyEveryMonthDay) {
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

	public Date getRangeOfRecurrrenceStartDay() {
		return rangeOfRecurrrenceStartDay;
	}

	public void setRangeOfRecurrrenceStartDay(Date rangeOfRecurrrenceStartDay) {
		this.rangeOfRecurrrenceStartDay = rangeOfRecurrrenceStartDay;
	}

	public Integer getRangeOfRecurrrenceEndAfter() {
		if(rangeOfRecurrrenceEndAfter==null){
			rangeOfRecurrrenceEndAfter=0;
		}
		return rangeOfRecurrrenceEndAfter;
	}

	public void setRangeOfRecurrrenceEndAfter(Integer rangeOfRecurrrenceEndAfter) {
		this.rangeOfRecurrrenceEndAfter = rangeOfRecurrrenceEndAfter;
	}

	public Date getRangeOfRecurrrenceEndDay() {
		return rangeOfRecurrrenceEndDay;
	}

	public void setRangeOfRecurrrenceEndDay(Date rangeOfRecurrrenceEndDay) {
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

	public List<AvailableAlertEvent> getAvailableAlertEvents() {
		return availableAlertEvents;
	}

	public void setAvailableAlertEvents(
			List<AvailableAlertEvent> availableAlertEvents) {
		this.availableAlertEvents = availableAlertEvents;
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

	public String getDailyRecurrrenceEveryDay() {
		return dailyRecurrrenceEveryDay;
	}

	public void setDailyRecurrrenceEveryDay(String dailyRecurrrenceEveryDay) {
		this.dailyRecurrrenceEveryDay = dailyRecurrrenceEveryDay;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getLicenseTypeID() {
		return licenseTypeID;
	}

	public void setLicenseTypeID(Long licenseTypeID) {
		this.licenseTypeID = licenseTypeID;
	}

	public Date getLicenseExpiratrionDate() {
		return licenseExpiratrionDate;
	}

	public void setLicenseExpiratrionDate(Date licenseExpiratrionDate) {
		this.licenseExpiratrionDate = licenseExpiratrionDate;
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
