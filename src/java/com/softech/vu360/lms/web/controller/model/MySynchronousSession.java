package com.softech.vu360.lms.web.controller.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.FormUtil;

/**
 * 
 * 
 * @author noman ali
 * 
 *
 */
@SuppressWarnings("unchecked")
public class MySynchronousSession  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
/*	private static final String SUNDAY = "Sunday";
	private static final String Monday = "Mon";
	private static final String Tuesday = "Tue";
	private static final String Wed = "Wed";
	private static final String FifthDayWeek = "Thu";
	private static final String SixthDayWeek = "Fri";
	private static final String SeventhDayWeek = "Sat";
*/	
	private List<MySessionDateTime> currentWeek = new ArrayList<MySessionDateTime>();
	private List<MySessionDateTime> nextWeek = new ArrayList<MySessionDateTime>();
	
	public List<MySessionDateTime> setupSynchSessions(List<SynchronousSession> synchSessions) {
		
		Calendar curWeekStartDt = getCurrentWeekStartDt();

		Calendar curWeekEndDt = (Calendar) curWeekStartDt.clone();
		curWeekEndDt.add(Calendar.DATE, 6);

		List<MySessionDateTime> currentSessionDateTimeList = new ArrayList<MySessionDateTime>();
		
		for (SynchronousSession synchSession:synchSessions) {
			Calendar startDt = Calendar.getInstance();
			Calendar endDt = Calendar.getInstance();
			
			if(synchSession != null && synchSession.getStartDateTime() != null){
				startDt.setTime(synchSession.getStartDateTime());
			}
			if(synchSession != null && synchSession.getEndDateTime() != null){
				endDt.setTime(synchSession.getEndDateTime());
			}
			
			int startLimit = curWeekStartDt.compareTo(startDt);
			int endLimit = curWeekEndDt.compareTo(startDt);
			if (startLimit <= 0 && endLimit >= 0 ) {
				MySessionDateTime sessionDateTime = new MySessionDateTime();
				sessionDateTime.setId(synchSession.getId());
				sessionDateTime.setStartDateTime(startDt);
				sessionDateTime.setEndDateTime(endDt);
				currentSessionDateTimeList.add(sessionDateTime);
			}
		}
		return currentSessionDateTimeList;
	}

	public Calendar getCurrentWeekStartDt () {
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(new Date());
		String weekDays = (String) FormUtil.getInstance().getFormatedDateInfo(cal.getTime(), "EE");
		String currDayNumber = FormUtil.getInstance().getFormatedDateInfo(cal.getTime(), "dd");
		
		int dayNumber = 0;
		Calendar currentStartWeek = (Calendar) cal.clone();
		Calendar currentEndWeek = (Calendar) cal.clone();

		try {
			
			if (weekDays.equalsIgnoreCase("Sun"))
				dayNumber = 0;
			else if (weekDays.equalsIgnoreCase("Mon"))
				dayNumber = 1;
			else if (weekDays.equalsIgnoreCase("Tue"))
				dayNumber = 2;
			else if (weekDays.equalsIgnoreCase("Wed"))
				dayNumber = 3;
			else if (weekDays.equalsIgnoreCase("Thu"))
				dayNumber = 4;
			else if (weekDays.equalsIgnoreCase("Fri"))
				dayNumber = 5;
			else if (weekDays.equalsIgnoreCase("Sat"))
				dayNumber = 6;
			
			currentStartWeek.add(Calendar.DATE, -dayNumber);
			currentEndWeek.add(Calendar.DATE, 7 - dayNumber);

			Calendar startDt = Calendar.getInstance();
			Calendar endDt = Calendar.getInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentStartWeek;
	}

	public static void main (String args[]) {
		MySynchronousSession mySynchronousSession = new MySynchronousSession();
		
		SynchronousSession synchSession = null;
		List<SynchronousSession> synchSessions = new ArrayList<SynchronousSession>();
		
		try {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

			synchSession = new SynchronousSession();
			synchSession.setStartDateTime(df.parse("09/10/009 10:00:00"));
			synchSession.setEndDateTime(df.parse("09/10/2009 11:00:00"));
			synchSessions.add(synchSession);
			
			synchSession = new SynchronousSession();
			synchSession.setStartDateTime(df.parse("09/11/2009 10:00:00"));
			synchSession.setEndDateTime(df.parse("09/11/2009 11:00:00"));
			synchSessions.add(synchSession);
			
			synchSession = new SynchronousSession();
			synchSession.setStartDateTime(df.parse("09/17/2009 10:00:00"));
			synchSession.setEndDateTime(df.parse("09/17/2009 11:00:00"));
			synchSessions.add(synchSession);
			
			synchSession = new SynchronousSession();
			synchSession.setStartDateTime(df.parse("09/18/2009 10:00:00"));
			synchSession.setEndDateTime(df.parse("09/18/2009 11:00:00"));
			synchSessions.add(synchSession);

			synchSession = new SynchronousSession();
			synchSession.setStartDateTime(df.parse("09/24/2009 10:00:00"));
			synchSession.setEndDateTime(df.parse("09/24/2009 11:00:00"));
			synchSessions.add(synchSession);

			synchSession = new SynchronousSession();
			synchSession.setStartDateTime(df.parse("09/25/2009 10:00:00"));
			synchSession.setEndDateTime(df.parse("09/25/2009 11:00:00"));
			synchSessions.add(synchSession);
			
			mySynchronousSession.setupSynchSessions(synchSessions);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
