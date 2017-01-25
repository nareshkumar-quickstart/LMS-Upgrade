package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "ALERTQUEUE")
public class AlertQueue  implements ILMSBaseInterface{
	
	@Id
    @javax.persistence.TableGenerator(name = "ALERTQUEUE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ALERTQUEUE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ALERTQUEUE_ID")
	private Long id;
	
	@Column(name = "LEARNERFIRSTNAME")
	private String learnerFirstName;
	
	@Column(name = "LEARNERLASTNAME")
	private String learnerLastName;
	
	@Column(name = "MESSAGEBODY")
	private String messageBody;
	
	@Column(name = "MESSAGESUBJECT")
	private String messageSubject;
	
	@Column(name = "PENDINGMAILSTATUS")
	private Boolean pendingMailStatus = Boolean.FALSE;
	
	@Column(name = "EVENTTIME")
	private Date eventTime;
	
	@Column(name = "EMAILADDRESS")
	private String emailAddress;
	
	@Column(name = "EVENTTYPE")
	private String eventType;
	
	@Column(name = "TRIGGERTYPE")
	private String triggerType;
	
	@OneToOne
    @JoinColumn(name="LEARNER_ID")
	private Learner learner ;
	
	@Column(name = "TABLENAME")
	private String tableName;
	
	@Column(name = "TABLENAME_ID")
	private long tableNameId;
	
	@Column(name = "ALERTID")
	private long alert_Id;
	
	@Column(name = "USERID")
	private long userId;
	
	@Column(name = "TRIGGERID")
	private long triggerId;
	
	@Column(name = "EVENTDUEDATE")
	private Date eventDueDate;
	
	//*********************************************************************************************
	@Column(name = "RECURRENCE")
	private Boolean recurrence = Boolean.FALSE;
	
	@Column(name = "RECURRENCETYPE")
	private String recurrenceType;
	
	@Column(name = "DAILYEVERYWEEKDAY")
	private Boolean dailyEveryWeekDay;
	
	@Column(name = "DAILYEVERYDAYINTERVAL")
	private Integer dailyEveryDayInterval;
	
	@Column(name = "WEEKLYEVERYWEEKINTERVAL")
	private Integer weeklyEveryWeekInterval;
	
	@Column(name = "WEEKLYEVERYWEEKON")
	private String weeklyEveryWeekOn;
	
	
	// for monthly
	@Column(name = "MONTHLYRECURRENCETHEEVERY")
	private String monthlyRecurrenceTheEvery;
	
	@Column(name = "MONTHLYRECURRENCETYPE")
	private Integer monthlyRecurrenceType;	// int value
	
	@Column(name = "MONTHLYRECURRENCETYPEDESCRIPTOR")
	private String monthlyRecurrenceTypeDescriptor; // month day year
	
	@Column(name = "MONTHLYRECURRENCEMONTH")
	private Integer monthlyRecurrenceMonth;
	
	
	// for yearly
	@Column(name = "YEARLYRECURRENCETHEEVERY")
	private String yearlyRecurrenceTheEvery;
	
	@Column(name = "YEARLYEVERYMONTHNAME")
	private String yearlyEveryMonthName;
	
	@Column(name = "YEARLYEVERYMONTHDAY")
	private Integer yearlyEveryMonthDay;
	
	@Column(name = "YEARLYTHEDAYTERM")
	private String yearlyTheDayTerm;
	
	@Column(name = "YEARLYTHEDAYDESCRIPTION")
	private String yearlyTheDayDescription;
	
	@Column(name = "YEARLYTHEMONTHDESCRIPTION")
	private String yearlyTheMonthDescription;
	
	@Column(name = "STARTDATE")
	private Date startDate;
	
	@Column(name = "ENDDATE")
	private Date endDate;
	
	@Column(name = "MAXOCCURANCES")
	private Integer maxOccurences;
	
	@Column(name = "OCCURANCECOUNT")
	private Integer occuranceCount;	
	
	@Transient
	private Integer dayCount;
	
	@Transient
	private Integer weekCount;
	
	@Transient
	private Integer monthCount;
	
	
	public Long getId() {
		return id;
	}
	public  Boolean getRecurrence() {
		return recurrence;
	}
	public void setRecurrence(Boolean recurrence) {
		if(recurrence==null){
			this.recurrence=Boolean.FALSE;
		}
		else{
		this.recurrence = recurrence;
		}
	}
	public String getRecurrenceType() {
		return recurrenceType;
	}
	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
	}
	public  Boolean isDailyEveryWeekDay() {
		return dailyEveryWeekDay;
	}
	public void setDailyEveryWeekDay(Boolean dailyEveryWeekDay) {
		this.dailyEveryWeekDay = dailyEveryWeekDay;
	}
	public Integer getDailyEveryDayInterval() {
		return dailyEveryDayInterval;
	}
	public void setDailyEveryDayInterval(Integer dailyEveryDayInterval) {
		this.dailyEveryDayInterval = dailyEveryDayInterval;
	}
	public Integer getWeeklyEveryWeekInterval() {
		return weeklyEveryWeekInterval;
	}
	public void setWeeklyEveryWeekInterval(Integer weeklyEveryWeekInterval) {
		this.weeklyEveryWeekInterval = weeklyEveryWeekInterval;
	}
	public String getWeeklyEveryWeekOn() {
		return weeklyEveryWeekOn;
	}
	public void setWeeklyEveryWeekOn(String weeklyEveryWeekOn) {
		this.weeklyEveryWeekOn = weeklyEveryWeekOn;
	}
	
	public String getMonthlyRecurrenceTheEvery() {
		return monthlyRecurrenceTheEvery;
	}
	public void setMonthlyRecurrenceTheEvery(String monthlyRecurrenceTheEvery) {
		this.monthlyRecurrenceTheEvery = monthlyRecurrenceTheEvery;
	}
	public Integer getMonthlyRecurrenceType() {
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
		return monthlyRecurrenceMonth;
	}
	public void setMonthlyRecurrenceMonth(Integer monthlyRecurrenceMonth) {
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
	public Integer getYearlyEveryMonthDay() {
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getMaxOccurences() {
		return maxOccurences;
	}
	public void setMaxOccurences(Integer maxOccurences) {
		this.maxOccurences = maxOccurences;
	}
	public Integer getOccuranceCount() {
		return occuranceCount;
	}
	public void setOccuranceCount(Integer occuranceCount) {
		this.occuranceCount = occuranceCount;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getMessageSubject() {
		return messageSubject;
	}
	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}	
	public  Boolean isPendingMailStatus() {
		return pendingMailStatus;
	}
	public void setPendingMailStatus(Boolean pendingMailStatus) {
		if(pendingMailStatus==null){
			this.pendingMailStatus=Boolean.FALSE;
		}
		else{
			this.pendingMailStatus = pendingMailStatus;
		}
	}
	public Date getEventTime() {
		return eventTime;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	/**
	 * @return the learner
	 */
	public Learner getLearner() {
		return learner;
	}
	/**
	 * @param learner the learner to set
	 */
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}	
	public Date getEventDueDate() {
		return eventDueDate;
	}
	public void setEventDueDate(Date eventDueDate) {
		this.eventDueDate = eventDueDate;
	}
	public long getTableNameId() {
		return tableNameId;
	}
	public void setTableNameId(long tableNameId) {
		this.tableNameId = tableNameId;
	}
	public String getLearnerFirstName() {
		return learnerFirstName;
	}
	public void setLearnerFirstName(String learnerFirstName) {
		this.learnerFirstName = learnerFirstName;
	}
	public String getLearnerLastName() {
		return learnerLastName;
	}
	public void setLearnerLastName(String learnerLastName) {
		this.learnerLastName = learnerLastName;
	}
	public Integer getDayCount() {
		return dayCount;
	}
	public void setDayCount(Integer dayCount) {
		this.dayCount = dayCount;
	}
	public Integer getWeekCount() {
		return weekCount;
	}
	public void setWeekCount(Integer weekCount) {
		this.weekCount = weekCount;
	}
	public Integer getMonthCount() {
		return monthCount;
	}
	public void setMonthCount(Integer monthCount) {
		this.monthCount = monthCount;
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getAlert_Id() {
		return alert_Id;
	}
	public void setAlert_Id(long alert_Id) {
		this.alert_Id = alert_Id;
	}
	public long getTrigger_Id() {
		return triggerId;
	}
	public void setTrigger_Id(long trigger_Id) {
		this.triggerId = trigger_Id;
	} 
	


}
