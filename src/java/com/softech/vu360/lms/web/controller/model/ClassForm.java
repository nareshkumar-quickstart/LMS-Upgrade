package com.softech.vu360.lms.web.controller.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.FormUtil;


/**
 * @author Noman
 *
 */


public class ClassForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ClassForm.class.getName());

	private Long id = null;
	private String className = null;
	private String courseName = null;
	private String classSize = null;
	private String status = null;
	private String meetingType = null;
	private String meetingId = null;
	private String meetingPasscode = null;
	private String meetingURL = null;
	private int timeZoneId = 0;
	private TimeZone timeZone = null;
	private String enrollmentCloseDateString = null;
	private Date enrollmentCloseDate = null;
	private boolean onlineMeetingTF = true; // Sana Majeed | LMS-4180
	private boolean isManualSession = false; 
	private String recurrenceSessionsToDelete;
	private Map<String,SynchronousSession> recurrenceSessionsToDeleteMap=new HashMap<String,SynchronousSession>(10);
	
	private String presenterFirstName = null;
	private String presenterLastName = null;
	private String presenterEmailAddress = null;
	
	private String courseType = null;
	
	public Map<String, SynchronousSession> getRecurrenceSessionsToDeleteMap() {
		return recurrenceSessionsToDeleteMap;
	}

	public void addRecurrenceSessionsToDeleteMap(String key, SynchronousSession synchronousSession) {
		this.recurrenceSessionsToDeleteMap.put(key, synchronousSession);
	}

	public String getRecurrenceSessionsToDelete() {
		return recurrenceSessionsToDelete;
	}

	public void setRecurrenceSessionsToDelete(String recurrenceSessionsToDelete) {
		this.recurrenceSessionsToDelete = recurrenceSessionsToDelete;
	}

	private List<SessionForm> classSessionList = new ArrayList<SessionForm>(10);
	private List<String> weekDaysList = new ArrayList<String>();
	private List<String> monthList = new ArrayList<String>();
	
	
	private List<SynchronousSession> synchronousSessionList = new ArrayList<SynchronousSession>(10);
	
	/*
	 * it will be set with tht following pattern (manual, daily, weekly, monthly, yearly)
	 * */
	//--------------------------------------- pattern -------------------------//
	private String pattern = null; 
	private static final String ONCE_PATTERN = "once";
	private static final String DAILY_PATTERN = "daily";
	private static final String WEEKLY_PATTERN = "weekly";
	private static final String MONTHLY_PATTERN = "monthly";
	private static final String YEARLY_PATTERN = "yearly";
	//--------------------------------------- pattern -------------------------//

	//-------------------------- Daily recurrence pattern -------------------------//
	private String numberOfWeekDays = null;
	private int WeekDaysNumber = 0;
	private String radioDaily = null;
	private static final String EVERYDAY = "EveryDay";
	private static final String EVERYWEEKDAY = "EveryWeekDay";

	//-----------------------------------------------------------------------------//

	//-------------------------- weekly recurrence pattern ------------------------//
	private String recureEvery = null;
	private String chkdSunday = "0";
	private String chkdMonday = "0";
	private String chkdTuesday = "0";
	private String chkdWednesday = "0";
	private String chkdThursday = "0";
	private String chkdFriday = "0";
	private String chkdSaturday = "0";
	//-----------------------------------------------------------------------------//

	//-------------------------- Monthly recurrence pattern -------------------------//
	private String monthlyOption = null;
	private String dayInputString = null;
	private String monthInputString = null;
	private String monthInputString2 = null; // for lower month option

	private int dayInputInteger ;
	private int MonthInputInteger ;

	private static final String DAY = "day";
	private static final String WEEKDAY = "weekday";
	private static final String WEEKEND = "weekend";
	private static final String SUNDAY = "Sun";
	private static final String MONDAY = "Mon";
	private static final String TUESDAY = "Tues";
	private static final String WEDNESDAY = "Wed";
	private static final String THURSDAY = "Thu";
	private static final String FRIDAY = "Fri";
	private static final String SATURDAY = "Sat";
	private static final String LAST = "0";

	private String theColMonthly = null;
	private String dayColMonthly = null;

	//--------------------------------  yearly pattern  ---------------------------//
	private String yearlyMonthOption = null;
	private String yearlyUpperMonthString = null;
	private String yearlyUpperMonthNumberString = null;
	private int yearlyUpperMonthNameInt= 0;
	private int yearlyUpperMonthNumber= 0;

	//private int dayColMonthly = -1;


	//--------------- Recurrence range with start time and end time ---------------//
	private String ofMonth = null;
	private String theColYearly = null;
	private String dayColYearly = null;
	//-----------------------------------------------------------------------------//

	//--------------- Recurrence range with start time and end time ---------------//
	private String startDate = null;
	private String endDate = null;
	private String startTime = null;
	private String endTime = null;
	private String endRange = null; /** possible values NoEndDate, EndAfter (check for totalOccurences), EndDate */
	private static final String NO_END_DATE = "NoEndDate";
	private static final String END_AFTER = "EndAfter";
	private static final String END_DATE = "EndDate";
	/** 
	 * setting -1 because if it will not set to any number then it will go
	 * without the restriction of total number of occurances 
	 */
	
	Date startDateTime = null;
	Date endDateTime = null;
	
	private boolean automatic = true;

	
	public List<SynchronousSession> getSynchronousSessionList() {
		return synchronousSessionList;
	}

	public void addSynchronousSessionList(SynchronousSession synchronousSession) {
		this.synchronousSessionList.add(synchronousSession);
	}
	
	public boolean isManualSession() {
		return isManualSession;
	}

	public void setManualSession(boolean isManualSession) {
		this.isManualSession = isManualSession;
	}
	
	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	private String totalOccurences = null; 
	//-----------------------------------------------------------------------------//

	public ClassForm() {
		classSessionList.clear();
		weekDaysList.clear();		
	}

	public void initClassSessionList() {
		classSessionList.clear();
	}

	public void initWeekDaysList() {
		weekDaysList.clear();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getClassSize() {
		return classSize;
	}

	public void setClassSize(String classSize) {
		this.classSize = classSize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getMeetingPasscode() {
		return meetingPasscode;
	}

	public void setMeetingPasscode(String meetingPasscode) {
		this.meetingPasscode = meetingPasscode;
	}
	
	public String getMeetingURL() {
		return meetingURL;
	}
	
	public void setMeetingURL(String meetingURL) {
		this.meetingURL = meetingURL;
	}
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getChkdSunday() {
		return chkdSunday;
	}

	public void setChkdSunday(String chkdSunday) {
		this.chkdSunday = chkdSunday;
		weekDaysList.add("Sun");
	}

	public String getChkdMonday() {
		return chkdMonday;
	}

	public void setChkdMonday(String chkdMonday) {
		this.chkdMonday = chkdMonday;
		weekDaysList.add("Mon");
	}

	public String getChkdTuesday() {
		return chkdTuesday;
	}

	public void setChkdTuesday(String chkdTuesday) {
		this.chkdTuesday = chkdTuesday;
		weekDaysList.add("Tue");
	}

	public String getChkdWednesday() {
		return chkdWednesday;
	}

	public void setChkdWednesday(String chkdWednesday) {
		this.chkdWednesday = chkdWednesday;
		weekDaysList.add("Wed");
	}

	public String getChkdThursday() {
		return chkdThursday;
	}

	public void setChkdThursday(String chkdThursday) {
		this.chkdThursday = chkdThursday;
		weekDaysList.add("Thu");
	}

	public String getChkdFriday() {
		return chkdFriday;
	}

	public void setChkdFriday(String chkdFriday) {
		this.chkdFriday = chkdFriday;
		weekDaysList.add("Fri");
	}

	public String getChkdSaturday() {
		return chkdSaturday;
	}

	public void setChkdSaturday(String chkdSaturday) {
		this.chkdSaturday = chkdSaturday;
		weekDaysList.add("Sat");
	}

	public String getStartDate() {
		return startDate;
	}

	public Date getStartDateDTF() {
		Date sDate = FormUtil.getInstance().getDate(startDate, "MM/dd/yyyy hh:mm:ss a");
		return sDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Date getEndDateDTF() {
		Date eDate = FormUtil.getInstance().getDate(endDate, "MM/dd/yyyy hh:mm:ss a"); // LMS-9274
		return eDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getEndRange() {
		return endRange;
	}

	public void setEndRange(String endRange) {
		this.endRange = endRange;
	}

	public String getTotalOccurences() {
		return totalOccurences;
	}

	public void setTotalOccurences(String totalOccurences) {
		this.totalOccurences = totalOccurences;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<SessionForm> getClassSessionList() {
		return classSessionList;
	}

	public void setClassSessionList(List<SessionForm> classSessionList) {
		this.classSessionList = classSessionList;
	}

	public void addClassSessionList(SessionForm classSessionList) {
		this.classSessionList.add(classSessionList);
	}
	
	public String getRadioDaily() {
		return radioDaily;
	}

	public void setRadioDaily(String radioDaily) {
		this.radioDaily = radioDaily;
	}



	public String getTheColMonthly() {
		return theColMonthly;
	}

	public void setTheColMonthly(String theColMonthly) {
		this.theColMonthly = theColMonthly;
	}

	public String getDayColMonthly() {
		return dayColMonthly;
	}

	public void setDayColMonthly(String dayColMonthly) {
		this.dayColMonthly = dayColMonthly;
	}

	public String getTheColYearly() {
		return theColYearly;
	}

	public void setTheColYearly(String theColYearly) {
		this.theColYearly = theColYearly;
	}

	public String getDayColYearly() {
		return dayColYearly;
	}

	public void setDayColYearly(String dayColYearly) {
		this.dayColYearly = dayColYearly;
	}

	public String getOfMonth() {
		return ofMonth;
	}

	public void setOfMonth(String ofMonth) {
		this.ofMonth = ofMonth;
	}

	public void generateSessions() throws ParseException {
		int totalNoOfOccurences = 9999999;

		if (endRange.equalsIgnoreCase(END_AFTER))
			totalNoOfOccurences = Integer.parseInt(totalOccurences);

		if (pattern.equalsIgnoreCase(WEEKLY_PATTERN)) {
			generateWeeklySessions(weekDaysList, startDate, endDate, startTime, endTime, totalNoOfOccurences); /** it will generate sessions in classSessionList member variable */
		}else if (pattern.equalsIgnoreCase(DAILY_PATTERN)) {
			/**
			 * for generating daily session 
			 * we are first creating a list of days session needs to be created in one week
			 * It will create entries in weekDaysList 
			 * Here we have 2 options either create session for whole week or
			 * only on weekdays (exclude weekend) 
			 */
			generateWeekDaysList(radioDaily);
			/**
			 * then call generateWeeklySession because we have a makred list of days in a week, 
			 * we can easily create the daily sessions   
			 */
			//			if( WeekDaysNumber > 0 && radioDaily.equals("EveryDay"))
			generateDailySessions(weekDaysList, startDate, endDate, startTime, endTime, totalNoOfOccurences);
			//			else	
			//				generateWeeklySessions(weekDaysList, startDate, endDate, startTime, endTime, totalNoOfOccurences); 
		} else if (pattern.equalsIgnoreCase(MONTHLY_PATTERN)) {
			generateMonthlySessions(startDate, endDate, startTime, endTime, theColMonthly, dayColMonthly);
		} else if (pattern.equalsIgnoreCase(YEARLY_PATTERN)) {
			generateYearlySessions(startDate, endDate, startTime, endTime, theColYearly, dayColYearly, ofMonth);
		}
	}

	private void generateYearlySessions(String startInputDate, String endInputDate, String startInputTime, 
			String endInputTime, String theInputCol, String dayInputCol, String ofInputMonth) throws ParseException {

		int totalNoOfOccurences = 30;

		Calendar startDateCal = Calendar.getInstance();
		Calendar endDateCal = Calendar.getInstance();

		startDateCal.setTime(FormUtil.getInstance().getDate(startInputDate + " " + startInputTime, "MM/dd/yyyy hh:mm a"));
		if( endRange.equalsIgnoreCase("NoEndDate") || endRange.equalsIgnoreCase("EndAfter") ){
			endDateCal.setTime(FormUtil.getInstance().getDate(startInputDate + " " + startInputTime, "MM/dd/yyyy hh:mm a"));

			if (endRange.equalsIgnoreCase("EndAfter")){
				totalNoOfOccurences = Integer.parseInt(totalOccurences);
				//totalNoOfOccurences = totalNoOfOccurences + 1 ; 
			}
			endDateCal.add(Calendar.YEAR , ( totalNoOfOccurences  ) );

		}else {
			endDateCal.setTime(FormUtil.getInstance().getDate(endInputDate + " " + startInputTime, "MM/dd/yyyy hh:mm a"));
		}
		List<Calendar> yearList = getYearListForYearlyPattern(startDateCal, endDateCal, ofInputMonth);

		Calendar monthlySessionStartTime = null;
		Calendar monthlySessionEndTime = Calendar.getInstance();

		for (Calendar runningMonth:yearList) {
			monthlySessionStartTime = getMonthSessionForYearlyPattern(runningMonth, theInputCol, dayInputCol);
			//			monthlySessionStartTime.add(Calendar.DATE, -1);
			if (monthlySessionStartTime.compareTo(startDateCal) >= 0 && monthlySessionStartTime.compareTo(endDateCal) <= 0 ) {
				SessionForm sessionForm = new SessionForm();
				sessionForm.setStartDateTime(monthlySessionStartTime.getTime());
				sessionForm.setStartDate(getFormattedDate(monthlySessionStartTime.getTime(),timeZone.getHours()));				
				String sessionEndDate = FormUtil.getInstance().getFormatedDateInfo(monthlySessionStartTime.getTime(), "MM/dd/yyyy");
				monthlySessionEndTime.setTime(FormUtil.getInstance().getDate(sessionEndDate + " " + endInputTime, "MM/dd/yyyy hh:mm a"));
				sessionForm.setEndDateTime(monthlySessionEndTime.getTime());
				sessionForm.setEndDate(getFormattedDate(monthlySessionEndTime.getTime(),timeZone.getHours()));
				classSessionList.add(sessionForm);

			} else {
				monthlySessionStartTime = null;
			}
		}
	}


	private void generateMonthlySessions(String startInputDate, String endInputDate, String startInputTime, 
			String endInputTime, String theInputCol, String dayInputCol) throws ParseException {

		Calendar startDateCal = Calendar.getInstance();
		Calendar endDateCal = Calendar.getInstance();

		startDateCal.setTime(FormUtil.getInstance().getDate(startInputDate + " " + startInputTime, "MM/dd/yyyy hh:mm a"));

		if( endRange.equalsIgnoreCase("NoEndDate") || endRange.equalsIgnoreCase("EndAfter") ){
			endDateCal.setTime(FormUtil.getInstance().getDate(startInputDate + " " + startInputTime, "MM/dd/yyyy hh:mm a"));

			endDateCal.add(Calendar.YEAR , 99999999 );

		}else {
			endDateCal.setTime(FormUtil.getInstance().getDate(endInputDate + " " + startInputTime, "MM/dd/yyyy hh:mm a"));
		}


		List<Calendar> monthList = getMonthList(startDateCal, endDateCal);

		Calendar monthlySessionStartTime = null;



		for (Calendar runningMonth:monthList) {

			monthlySessionStartTime = getMonthSession(runningMonth, theInputCol, dayInputCol);
			//			monthlySessionStartTime.add(Calendar.DATE, -1);
			handleRunningMonth(runningMonth , monthlySessionStartTime , startDateCal , endDateCal , endInputTime);


		}
	}

	private void handleRunningMonth( Calendar runningMonth , Calendar monthlySessionStartTime , Calendar startDateCal , Calendar endDateCal , String endInputTime) throws ParseException{
		Calendar monthlySessionEndTime = Calendar.getInstance();
		if( endRange.equalsIgnoreCase("NoEndDate") || endRange.equalsIgnoreCase("EndAfter") ){

			SessionForm sessionForm = new SessionForm();
			sessionForm.setStartDateTime(monthlySessionStartTime.getTime());
			sessionForm.setStartDate(getFormattedDate(monthlySessionStartTime.getTime(),timeZone.getHours()));
			String sessionEndDate = FormUtil.getInstance().getFormatedDateInfo(monthlySessionStartTime.getTime(), "MM/dd/yyyy");
			monthlySessionEndTime.setTime(FormUtil.getInstance().getDate(sessionEndDate + " " + endInputTime, "MM/dd/yyyy hh:mm a"));
			sessionForm.setEndDateTime(monthlySessionEndTime.getTime());
			sessionForm.setEndDate(getFormattedDate(monthlySessionEndTime.getTime(),timeZone.getHours()));
			classSessionList.add(sessionForm);

		}
		else
		{
			if (monthlySessionStartTime.compareTo(startDateCal) >= 0 && monthlySessionStartTime.compareTo(endDateCal) <= 0 ) {
				SessionForm sessionForm = new SessionForm();
				sessionForm.setStartDateTime(monthlySessionStartTime.getTime());
				sessionForm.setStartDate(getFormattedDate(monthlySessionStartTime.getTime(),timeZone.getHours()));
				String sessionEndDate = FormUtil.getInstance().getFormatedDateInfo(monthlySessionStartTime.getTime(), "MM/dd/yyyy");
				monthlySessionEndTime.setTime(FormUtil.getInstance().getDate(sessionEndDate + " " + endInputTime, "MM/dd/yyyy hh:mm a"));
				sessionForm.setEndDateTime(monthlySessionEndTime.getTime());
				sessionForm.setEndDate(getFormattedDate(monthlySessionEndTime.getTime(),timeZone.getHours()));
				classSessionList.add(sessionForm);
			} else {
				monthlySessionStartTime = null;
			}
		}
	}

	/* theInputCol = 0 is for last */
	private Calendar getMonthSession(Calendar runningMonth, String theInputCol, String dayInputCol) {
		Calendar runningMonthSDate = (Calendar) runningMonth.clone();
		Calendar runningMonthEDate = (Calendar) runningMonth.clone(); 

		int lastDate = runningMonthEDate.getActualMaximum(Calendar.DATE);
		runningMonthEDate.set(Calendar.DATE, lastDate);  

		int intTheInputCol = Integer.parseInt(theInputCol);
		int theInputCount = 0;
		Calendar runningDate = null;

		/** if it is the day then deal with adding days in the startDate */
		if( monthlyOption.trim().equalsIgnoreCase("upperRadioButton") ) {

			if (dayInputInteger == 0) 
				return runningMonthEDate;
			else { 
				runningDate = (Calendar) runningMonthSDate.clone();
				runningDate.add(Calendar.DATE, dayInputInteger-1);
				return runningDate;
			}

		}
		else if (dayInputCol.equalsIgnoreCase(DAY)) {
			if (intTheInputCol == 0) 
				return runningMonthEDate;
			else { 
				runningDate = (Calendar) runningMonthSDate.clone();
				runningDate.add(Calendar.DATE, intTheInputCol-1);
				return runningDate;
			}
		}/** intTheInputCol if 0 then it mean it is looking for "Last" combination 
		 so we will reverse the loop to get the last for example sunday, monday etc*/ 
		else if (intTheInputCol == 0) { 
			intTheInputCol = 1;
			for (runningDate = (Calendar) runningMonthEDate.clone(); runningDate.compareTo(runningMonthSDate) >= 0 && intTheInputCol > theInputCount; 
			runningDate.add(Calendar.DATE, -1)) {
				String weekDay = (String) FormUtil.getInstance().getFormatedDateInfo(runningDate.getTime(), "EE");
				if (dayInputCol.equalsIgnoreCase(weekDay)) {
					theInputCount++;
				}
			}
			runningDate.add(Calendar.DATE, 1);
		}/** if it is first, second, third, fourth ... sunday, monday etc 
		then this condition deal with them */ 
		else { 
			for (runningDate = (Calendar) runningMonthSDate.clone(); runningDate.compareTo(runningMonthEDate) <= 0 && intTheInputCol > theInputCount; 
			runningDate.add(Calendar.DATE, 1)) {
				String weekDay = (String) FormUtil.getInstance().getFormatedDateInfo(runningDate.getTime(), "EE");
				if (dayInputCol.equalsIgnoreCase(weekDay)) {
					theInputCount++;
				}
			}
			runningDate.add(Calendar.DATE, -1);
		}
		//if (runningDate.compareTo(anotherCalendar))
		return runningDate;

	}

	/* theInputCol = 0 is for last */
	private Calendar getMonthSessionForYearlyPattern(Calendar runningMonth, String theInputCol, String dayInputCol) {
		Calendar runningMonthSDate = (Calendar) runningMonth.clone();
		Calendar runningMonthEDate = (Calendar) runningMonth.clone(); 

		int lastDate = runningMonthEDate.getActualMaximum(Calendar.DATE);
		runningMonthEDate.set(Calendar.DATE, lastDate);  

		int intTheInputCol = Integer.parseInt(theInputCol);
		int theInputCount = 0;
		Calendar runningDate = null;

		/** if it is the day then deal with adding days in the startDate */
		if( yearlyMonthOption.equalsIgnoreCase("UpperRadioButton")){
			//monthNumberOfDaysInt = Integer.parseInt(monthNumberOfDaysString );
			runningDate = (Calendar) runningMonthSDate.clone();
			runningDate.add(Calendar.DATE, ( yearlyUpperMonthNumber - 1 ) );
			return runningDate;
		}

		else if (dayInputCol.equalsIgnoreCase(DAY)) {
			if (intTheInputCol == 0) 
				return runningMonthEDate;
			else { 
				runningDate = (Calendar) runningMonthSDate.clone();
				runningDate.add(Calendar.DATE, intTheInputCol-1);
				return runningDate;
			}
		}/** intTheInputCol if 0 then it mean it is looking for "Last" combination 
		 so we will reverse the loop to get the last for example sunday, monday etc*/ 
		else if (intTheInputCol == 0) { 
			intTheInputCol = 1;
			for (runningDate = (Calendar) runningMonthEDate.clone(); runningDate.compareTo(runningMonthSDate) >= 0 && intTheInputCol > theInputCount; 
			runningDate.add(Calendar.DATE, -1)) {
				String weekDay = (String) FormUtil.getInstance().getFormatedDateInfo(runningDate.getTime(), "EE");
				if (dayInputCol.equalsIgnoreCase(weekDay)) {
					theInputCount++;
				}
			}
			runningDate.add(Calendar.DATE, 1);
		}/** if it is first, second, third, fourth ... sunday, monday etc 
		then this condition deal with them */ 
		else { 
			for (runningDate = (Calendar) runningMonthSDate.clone(); runningDate.compareTo(runningMonthEDate) <= 0 && intTheInputCol > theInputCount; 
			runningDate.add(Calendar.DATE, 1)) {
				String weekDay = (String) FormUtil.getInstance().getFormatedDateInfo(runningDate.getTime(), "EE");
				if (dayInputCol.equalsIgnoreCase(weekDay)) {
					theInputCount++;
				}
			}
			runningDate.add(Calendar.DATE, -1);
		}

		//if (runningDate.compareTo(anotherCalendar))
		return runningDate;

	}

	/**
	 * it will return the total number of month in the 
	 * startDate and EndDate range 
	 * return format is calendar and it will return the first 
	 * date of the each month 
	 * */
	private List<Calendar> getYearList(Calendar startDateCal, Calendar endDateCal, String ofInputMonth) {
		List<Calendar> monthList = new ArrayList<Calendar>();

		int intOfInputMonth = Integer.parseInt(ofInputMonth);

		//runningDate.add(Calendar.MONTH, 1);
		Calendar endDateLimit = (Calendar) endDateCal.clone();

		endDateLimit.set(Calendar.MONTH, 12);

		startDateCal.set(Calendar.MONTH, intOfInputMonth);

		for (Calendar runningDate = (Calendar) startDateCal.clone(); runningDate.compareTo(endDateLimit) <= 0; runningDate.add(Calendar.YEAR, 1)) {
			Calendar newRunningDate = Calendar.getInstance();
			runningDate.set(Calendar.DATE, 1);
			newRunningDate.setTime(runningDate.getTime());

			monthList.add(newRunningDate);
		}

		Collections.sort(monthList);

		return monthList;
	}

	/**
	 * it will return the total number of month in the 
	 * startDate and EndDate range 
	 * return format is calendar and it will return the first 
	 * date of the each month 
	 * */
	private List<Calendar> getYearListForYearlyPattern(Calendar startDateCal, Calendar endDateCal, String ofInputMonth) {
		List<Calendar> monthList = new ArrayList<Calendar>();

		int intOfInputMonth = Integer.parseInt(ofInputMonth);

		//runningDate.add(Calendar.MONTH, 1);
		Calendar endDateLimit = (Calendar) endDateCal.clone();
		endDateLimit.set(Calendar.MONTH, 12);
		if( yearlyMonthOption.equalsIgnoreCase("UpperRadioButton")){
			startDateCal.set(Calendar.MONTH, yearlyUpperMonthNameInt);
		}
		else
			startDateCal.set(Calendar.MONTH, intOfInputMonth);

		for (Calendar runningDate = (Calendar) startDateCal.clone(); runningDate.compareTo(endDateLimit) <= 0; runningDate.add(Calendar.YEAR, 1)) {
			Calendar newRunningDate = Calendar.getInstance();
			runningDate.set(Calendar.DATE, 1);
			newRunningDate.setTime(runningDate.getTime());

			monthList.add(newRunningDate);
		}

		Collections.sort(monthList);

		return monthList;
	}
	/**
	 * it will return the total number of month in the 
	 * startDate and EndDate range 
	 * return format is calendar and it will return the first 
	 * date of the each month 
	 * */
	private List<Calendar> getMonthList(Calendar startDateCal, Calendar endDateCal) {
		List<Calendar> monthList = new ArrayList<Calendar>();

		//runningDate.add(Calendar.MONTH, 1);
		Calendar endDateLimit = (Calendar) endDateCal.clone();
		endDateLimit.set(Calendar.DATE, 1);
		//int valueForLowerMonthlyRecurrenceOptionOfEveryMonth = 0;
		int valueForMonthlyRecureEveryMonth = 0;

		if( monthlyOption.trim().equalsIgnoreCase("lowerRadioButton") ) {
			valueForMonthlyRecureEveryMonth = ( MonthInputInteger ) ;
		}
		else if( monthlyOption.trim().equalsIgnoreCase("upperRadioButton") ) {
			valueForMonthlyRecureEveryMonth = ( MonthInputInteger  ) ;
		}

		int totalOccurrences = 0 ;
		for (Calendar runningDate = (Calendar) startDateCal.clone(); runningDate.compareTo(endDateLimit) <= 0; runningDate.add(Calendar.MONTH, valueForMonthlyRecureEveryMonth)) {

			if( endRange.equalsIgnoreCase("NoEndDate") || endRange.equalsIgnoreCase("EndAfter") ){

				if (endRange.equalsIgnoreCase("NoEndDate") && totalOccurrences >= 30){
					break; // if more than or equal to 30 occurrences
				}else if (  endRange.equalsIgnoreCase("EndAfter")   ){
					int totalRequiredRecurrences =  Integer.parseInt(totalOccurences); // get user input
					if( totalOccurrences >= totalRequiredRecurrences  )
						break;  // if more than or equal to user desired  occurrences
				}
			}			

			Calendar newRunningDate = Calendar.getInstance();
			runningDate.set(Calendar.DATE, 1);
			newRunningDate.setTime(runningDate.getTime());
			monthList.add(newRunningDate);

			totalOccurrences++;
		}
		Collections.sort(monthList);
		for(Calendar tempDate:monthList) {
			log.debug(tempDate.getTime()) ;
		}
		return monthList;
	}


	private void generateWeekDaysList(String radioDaily) {

		weekDaysList.clear();
		if (radioDaily.equalsIgnoreCase(EVERYDAY)) {
			weekDaysList.add("Sun");
			weekDaysList.add("Mon");
			weekDaysList.add("Tue");
			weekDaysList.add("Wed");
			weekDaysList.add("Thu");
			weekDaysList.add("Fri");
			weekDaysList.add("Sat");
		}else if (radioDaily.equalsIgnoreCase(EVERYWEEKDAY)) {
			weekDaysList.add("Mon");
			weekDaysList.add("Tue");
			weekDaysList.add("Wed");
			weekDaysList.add("Thu");
			weekDaysList.add("Fri");
		}
		/** we can use the generateWeeklySessions if just define the weeklist that which days it has to create the sessions */
	}

	/** it will generate sessions in classSessionList member variable 
	 **/
	private void generateWeeklySessions(List<String> weekDaysForRecurrence, String inputStartDate, String inputEndDate, String inputStartTime, String inputEndTime, int totalNoOcurrences) {
		int totalNoOcurrencesCount = 0;
		int maxOcurrencesCount = 30 ;
		Calendar startDateCal = Calendar.getInstance();
		Calendar endDateCal = Calendar.getInstance();

		Calendar endTimeCal = Calendar.getInstance();

		startDateCal.setTime(FormUtil.getInstance().getDate(inputStartDate + " " + inputStartTime, "MM/dd/yyyy hh:mm a"));

		endTimeCal.setTime(FormUtil.getInstance().getDate(inputStartDate + " " + inputEndTime, "MM/dd/yyyy hh:mm a"));

		if( WeekDaysNumber <= 1 ){

			WeekDaysNumber = 1 ;

		}else{
			if( WeekDaysNumber == 2)
				WeekDaysNumber = 7 ;
			else if( WeekDaysNumber > 2)
				WeekDaysNumber = ( ( WeekDaysNumber - 1 ) * 7) ;
		}

		if( endRange.equalsIgnoreCase("NoEndDate") || endRange.equalsIgnoreCase("EndAfter") ){

			if( endRange.equalsIgnoreCase("EndAfter") ) {
				maxOcurrencesCount = totalNoOcurrences ;	
			}
			//WeekDaysNumber = ( WeekDaysNumber * 7 ) ;
			/* totalNoOccurences check is for for generating only required total number of occurances */
			for (Calendar startCal = (Calendar) startDateCal.clone(); maxOcurrencesCount > totalNoOcurrencesCount; startCal.add(Calendar.DAY_OF_MONTH, WeekDaysNumber )) {

				for( int dayCounter= 0 ; dayCounter < 7 ; dayCounter++ ) {
					String currentDay = (String) FormUtil.getInstance().getFormatedDateInfo(startCal.getTime(), "EE");
					try {
						for (String scheduledDay : weekDaysForRecurrence) {
							if (scheduledDay.equalsIgnoreCase(currentDay)) {
								totalNoOcurrencesCount++;
								SessionForm sessionForm = new SessionForm();
								sessionForm.setStartDateTime(startCal.getTime());
								sessionForm.setStartDate(getFormattedDate(startCal.getTime(),timeZone.getHours()));
								sessionForm.setEndDateTime(endTimeCal.getTime());
								sessionForm.setEndDate(getFormattedDate(endTimeCal.getTime(),timeZone.getHours()));
								//sessionForm.setStartDateTime();
								//sessionForm.setEndDateTime(endTimeCal.getTime());
								classSessionList.add(sessionForm);

							}
						}
						if( WeekDaysNumber > 1 ){
							endTimeCal.add(Calendar.DAY_OF_MONTH, 1 );
							startCal.add(Calendar.DAY_OF_MONTH, 1 );
							//							weekDays = (String) FormUtil.getInstance().getFormatedDateInfo(startCal.getTime(), "EE");
						}else
							break;


						//endTimeCal.add(Calendar.DAY_OF_MONTH, WeekDaysNumber );
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				endTimeCal.add(Calendar.DAY_OF_MONTH, WeekDaysNumber );
			}


		}

		else {
			endDateCal.setTime(FormUtil.getInstance().getDate(inputEndDate + " " + inputEndTime, "MM/dd/yyyy hh:mm a"));

			/* totalNoOccurences check is for for generating only required total number of occurances */
			for (Calendar startCal = (Calendar) startDateCal.clone(); startCal.compareTo(endDateCal) <= 0 && totalNoOcurrences > totalNoOcurrencesCount; startCal.add(Calendar.DAY_OF_MONTH, WeekDaysNumber )) {

				for( int dayCounter= 0 ; dayCounter < 7 ; dayCounter++ ) {
					String currentDay = (String) FormUtil.getInstance().getFormatedDateInfo(startCal.getTime(), "EE");
					try {
						if (weekDaysForRecurrence.contains(currentDay)) {
							totalNoOcurrencesCount++;
							SessionForm sessionForm = new SessionForm();
							sessionForm.setStartDateTime(startCal.getTime());
							sessionForm.setStartDate(getFormattedDate(startCal.getTime(),timeZone.getHours()));
							sessionForm.setEndDateTime(endTimeCal.getTime());
							sessionForm.setEndDate(getFormattedDate(endTimeCal.getTime(),timeZone.getHours()));
							classSessionList.add(sessionForm);	
						}
						if(startCal.compareTo(endDateCal) < 0 && totalNoOcurrences > totalNoOcurrencesCount && WeekDaysNumber > 1 ) {
							endTimeCal.add(Calendar.DAY_OF_MONTH, 1 );
							startCal.add(Calendar.DAY_OF_MONTH, 1 );
						}
						else
							break;
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage(), e);
					}
				}
				endTimeCal.add(Calendar.DAY_OF_MONTH, WeekDaysNumber );
			}
		}
	}

	public String getFormattedDate(Date date, int hours)
	{
		String date12=null;
		
		try {
			Calendar gmtC = new GregorianCalendar(java.util.TimeZone.getTimeZone("GMT"));
			gmtC.setTime(date);
			gmtC.set(Calendar.HOUR_OF_DAY, gmtC.getTime().getHours()-hours);

			DateFormat dateFormat12 = new SimpleDateFormat("EEE MMM dd hh:mm:ss a zzz yyyy");
			dateFormat12.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
			date12 = dateFormat12.format(gmtC.getTime());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return date12;
	}
	/** it will generate sessions in classSessionList member variable 
	 **/
	private void generateDailySessions(List<String> inputWeekDaysList, String inputStartDate, String inputEndDate, String inputStartTime, String inputEndTime, int totalNoOcurrences) {
		int totalNoOcurrencesCount = 0;
		int maxOcurrencesCount = 30 ;
		Calendar startDateCal = Calendar.getInstance();
		Calendar endDateCal = Calendar.getInstance();

		Calendar endTimeCal = Calendar.getInstance();

		startDateCal.setTime(FormUtil.getInstance().getDate(inputStartDate + " " + inputStartTime, "MM/dd/yyyy hh:mm a"));

		endTimeCal.setTime(FormUtil.getInstance().getDate(inputStartDate + " " + inputEndTime, "MM/dd/yyyy hh:mm a"));

		if ( radioDaily.equalsIgnoreCase("EveryWeekDay")) {
			WeekDaysNumber = 1 ;
		}		
		if( endRange.equalsIgnoreCase("NoEndDate") || endRange.equalsIgnoreCase("EndAfter") ){

			if( endRange.equalsIgnoreCase("EndAfter") ) {
				maxOcurrencesCount = totalNoOcurrences ;	
			}
			/* totalNoOccurences check is for for generating only required total number of occurances */
			for (Calendar startCal = (Calendar) startDateCal.clone();  totalNoOcurrencesCount < maxOcurrencesCount ; startCal.add(Calendar.DATE, WeekDaysNumber )) {
				String weekDays = (String) FormUtil.getInstance().getFormatedDateInfo(startCal.getTime(), "EE");
				try {
					for (String weekDay:inputWeekDaysList) {
						if (weekDay.equalsIgnoreCase(weekDays)) {
							totalNoOcurrencesCount++;
							SessionForm sessionForm = new SessionForm();
							sessionForm.setStartDateTime(startCal.getTime());
							sessionForm.setStartDate(getFormattedDate(startCal.getTime(),timeZone.getHours()));
							sessionForm.setEndDateTime(endTimeCal.getTime());
							sessionForm.setEndDate(getFormattedDate(endTimeCal.getTime(),timeZone.getHours()));
							classSessionList.add(sessionForm);

							if(totalNoOcurrencesCount <= maxOcurrencesCount  )
								break;
						}
					}
					endTimeCal.add(Calendar.DATE, WeekDaysNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}


		}

		else {

			if( WeekDaysNumber <= 0 ){

				WeekDaysNumber = 1 ;

			}
			endDateCal.setTime(FormUtil.getInstance().getDate(inputEndDate + " " + inputEndTime, "MM/dd/yyyy hh:mm a"));
			/* totalNoOccurences check is for for generating only required total number of occurances */
			for (Calendar startCal = (Calendar) startDateCal.clone(); startCal.compareTo(endDateCal) <= 0 && totalNoOcurrences > totalNoOcurrencesCount; startCal.add(Calendar.DATE, WeekDaysNumber )) {
				String weekDays = (String) FormUtil.getInstance().getFormatedDateInfo(startCal.getTime(), "EE");
				try {
					for (String weekDay:inputWeekDaysList) {
						if (weekDay.equalsIgnoreCase(weekDays)) {
							totalNoOcurrencesCount++;
							SessionForm sessionForm = new SessionForm();
							sessionForm.setStartDateTime(startCal.getTime());
							sessionForm.setStartDate(getFormattedDate(startCal.getTime(),timeZone.getHours()));
							sessionForm.setEndDateTime(endTimeCal.getTime());
							sessionForm.setEndDate(getFormattedDate(endTimeCal.getTime(),timeZone.getHours()));
							classSessionList.add(sessionForm);
						}
					}
					endTimeCal.add(Calendar.DATE, WeekDaysNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}	
	}
	/**
	 * @param timeZoneId the timeZoneId to set
	 */
	public void setTimeZoneId(int timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	/**
	 * @return the timeZoneId
	 */
	public int getTimeZoneId() {
		return timeZoneId;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the timeZone
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * @param enrollmentCloseDateString the enrollmentCloseDateString to set
	 */
	public void setEnrollmentCloseDateString(String enrollmentCloseDateString) {
		this.enrollmentCloseDateString = enrollmentCloseDateString;
	}

	/**
	 * @return the enrollmentCloseDateString
	 */
	public String getEnrollmentCloseDateString() {
		return enrollmentCloseDateString;
	}

	/**
	 * @param enrollmentCloseDate the enrollmentCloseDate to set
	 */
	public void setEnrollmentCloseDate(Date enrollmentCloseDate) {
		this.enrollmentCloseDate = enrollmentCloseDate;
	}

	/**
	 * @return the enrollmentCloseDate
	 */
	public Date getEnrollmentCloseDate() {
		return enrollmentCloseDate;
	}

	// Sana Majeed | LMS-4180
	/**
	 * @param onlineMeetingTF the onlineMeetingTF to set
	 */
	public void setOnlineMeetingTF(boolean onlineMeetingTF) {
		this.onlineMeetingTF = onlineMeetingTF;
	}

	/**
	 * @return the onlineMeetingTF
	 */
	public boolean isOnlineMeetingTF() {
		if(this.courseType!=null && this.courseType.equals(SynchronousCourse.COURSE_TYPE)){
			onlineMeetingTF=false;
		}
		
		return onlineMeetingTF;
	}

	/**
	 * @param numberOfWeekDays the numberOfWeekDays to set
	 */
	public void setNumberOfWeekDays(String numberOfWeekDays) {
		this.numberOfWeekDays = numberOfWeekDays;
	}

	/**
	 * @return the numberOfWeekDays
	 */
	public String getNumberOfWeekDays() {
		return numberOfWeekDays;
	}

	/**
	 * @param weekDaysNumber the weekDaysNumber to set
	 */
	public void setWeekDaysNumber(int weekDaysNumber) {
		WeekDaysNumber = weekDaysNumber;
	}

	/**
	 * @return the weekDaysNumber
	 */
	public int getWeekDaysNumber() {
		return WeekDaysNumber;
	}

	/**
	 * @param recureEvery the recureEvery to set
	 */
	public void setRecureEvery(String recureEvery) {
		this.recureEvery = recureEvery;
	}

	/**
	 * @return the recureEvery
	 */
	public String getRecureEvery() {
		return recureEvery;
	}

	/**
	 * @return the weekDaysList
	 */
	public List<String> getWeekDaysList() { 
		return weekDaysList;
	}

	/**
	 * @param weekDaysList the weekDaysList to set
	 */
	public void setWeekDaysList(List<String> weekDaysList) {
		this.weekDaysList = weekDaysList;
	}

	/**
	 * @param monthlyOption the monthlyOption to set
	 */
	public void setMonthlyOption(String monthlyOption) {
		this.monthlyOption = monthlyOption;
	}

	/**
	 * @return the monthlyOption
	 */
	public String getMonthlyOption() {
		return monthlyOption;
	}

	/**
	 * @param dayInputString the dayInputString to set
	 */
	public void setDayInputString(String dayInputString) {
		this.dayInputString = dayInputString;
	}

	/**
	 * @return the dayInputString
	 */
	public String getDayInputString() {
		return dayInputString;
	}

	/**
	 * @param monthInputString the monthInputString to set
	 */
	public void setMonthInputString(String monthInputString) {
		this.monthInputString = monthInputString;
	}

	/**
	 * @return the monthInputString
	 */
	public String getMonthInputString() {
		return monthInputString;
	}

	/**
	 * @param dayInputInteger the dayInputInteger to set
	 */
	public void setDayInputInteger(int dayInputInteger) {
		this.dayInputInteger = dayInputInteger;
	}

	/**
	 * @return the dayInputInteger
	 */
	public int getDayInputInteger() {
		return dayInputInteger;
	}

	/**
	 * @param monthInputInteger the monthInputInteger to set
	 */
	public void setMonthInputInteger(int monthInputInteger) {
		MonthInputInteger = monthInputInteger;
	}

	/**
	 * @return the monthInputInteger
	 */
	public int getMonthInputInteger() {
		return MonthInputInteger;
	}

	/**
	 * @param monthInputString2 the monthInputString2 to set
	 */
	public void setMonthInputString2(String monthInputString2) {
		this.monthInputString2 = monthInputString2;
	}

	/**
	 * @return the monthInputString2
	 */
	public String getMonthInputString2() {
		return monthInputString2;
	}

	/**
	 * @param yearlyUpperMonthString the yearlyUpperMonthString to set
	 */
	public void setYearlyUpperMonthString(String yearlyUpperMonthString) {
		this.yearlyUpperMonthString = yearlyUpperMonthString;
	}

	/**
	 * @return the yearlyUpperMonthString
	 */
	public String getYearlyUpperMonthString() {
		return yearlyUpperMonthString;
	}

	/**
	 * @param yearlyMonthOption the yearlyMonthOption to set
	 */
	public void setYearlyMonthOption(String yearlyMonthOption) {
		this.yearlyMonthOption = yearlyMonthOption;
	}

	/**
	 * @return the yearlyMonthOption
	 */
	public String getYearlyMonthOption() {
		return yearlyMonthOption;
	}

	/**
	 * @param yearlyUpperMonthNumberString the yearlyUpperMonthNumberString to set
	 */
	public void setYearlyUpperMonthNumberString(
			String yearlyUpperMonthNumberString) {
		this.yearlyUpperMonthNumberString = yearlyUpperMonthNumberString;
	}

	/**
	 * @return the yearlyUpperMonthNumberString
	 */
	public String getYearlyUpperMonthNumberString() {
		return yearlyUpperMonthNumberString;
	}

	/**
	 * @param yearlyUpperMonthNumber the yearlyUpperMonthNumber to set
	 */
	public void setYearlyUpperMonthNumber(int yearlyUpperMonthNumber) {
		this.yearlyUpperMonthNumber = yearlyUpperMonthNumber;
	}

	/**
	 * @return the yearlyUpperMonthNumber
	 */
	public int getYearlyUpperMonthNumber() {
		return yearlyUpperMonthNumber;
	}


	/**
	 * @param yearlyUpperMonthNameInt the yearlyUpperMonthNameInt to set
	 */
	public void setYearlyUpperMonthNameInt(int yearlyUpperMonthNameInt) {
		this.yearlyUpperMonthNameInt = yearlyUpperMonthNameInt;
	}

	/**
	 * @return the yearlyUpperMonthNameInt
	 */
	public int getYearlyUpperMonthNameInt() {
		return yearlyUpperMonthNameInt;
	}

	public String getPresenterFirstName() {
		return presenterFirstName;
	}

	public void setPresenterFirstName(String presenterFirstName) {
		this.presenterFirstName = presenterFirstName;
	}

	public String getPresenterLastName() {
		return presenterLastName;
	}

	public void setPresenterLastName(String presenterLastName) {
		this.presenterLastName = presenterLastName;
	}

	public String getPresenterEmailAddress() {
		return presenterEmailAddress;
	}

	public void setPresenterEmailAddress(String presenterEmailAddress) {
		this.presenterEmailAddress = presenterEmailAddress;
	}

	public boolean isAutomatic() {
		return automatic;
	}

	public void setAutomatic(boolean automatic) {
		this.automatic = automatic;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	
	

}
