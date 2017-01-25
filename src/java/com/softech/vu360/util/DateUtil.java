package com.softech.vu360.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

public class DateUtil 
{
	private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	private static Logger log = Logger.getLogger(DateUtil.class);

	public static final XMLGregorianCalendar getCurrentDateTimeForXML() 
    {
		XMLGregorianCalendar gregDate =null;
		try 
    	{
			gregDate= DatatypeFactory.newInstance().newXMLGregorianCalendar();
        	Calendar c=Calendar.getInstance();
        	gregDate.setYear(c.get(Calendar.YEAR));
        	gregDate.setMonth(c.get(Calendar.MONTH)+1 );
        	gregDate.setDay(c.get(Calendar.DAY_OF_MONTH));
       		gregDate.setTime(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
    	}catch(Exception e) 
    	{
    		e.printStackTrace();
    	}
    	return gregDate;
    }
	
	// Making an XMLGregorianCalendar for the Current Date and Time
	public static XMLGregorianCalendar getXMLGregorianCalendarNow() {
		XMLGregorianCalendar now = null;
		try {
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			String errorMessage = e.getMessage();
			e.printStackTrace();
		}
			 
		return now;	
	}
	
	public static Date getDateObject(String strDate){
		Date date = null;
		try {
			
			date = formatter.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public int dateDifference(Date today, Date myDate) {
		return (today.compareTo(myDate)); 
	}
	
	
	public static long getDatesDiffInDays(Date startDate, Date endDate){
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diffTime = endTime - startTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		return diffDays;
	}

	
	public static String getStringDate(Date dateObject){
		
		String dateAsString = "";
		try {
			
			if (dateObject != null)
				dateAsString = formatter.format(dateObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateAsString;
		

	}
	
	/*
	 * @Wajahat
	 * This method will return the GMT Date/Time by adjusting timezone hour 
	 */
	public static Date getGMTDateForTimezoneHours(Date dateObject, int gmtHours){
		
		Calendar cal =Calendar.getInstance();
		cal.setTime(dateObject);
		//cal.setTimeZone()
		cal.add(Calendar.HOUR_OF_DAY,-gmtHours); // this will add two hours
		dateObject = cal.getTime();
		//System.out.println("GMT Time::::::::: "+dateObject);
		return dateObject;

	}
	
	/*
	 * @Wajahat
	 * The method will return the GMT Date/Time of the System, running Application Server
	 */
	public static Date getCurrentServerTimeGMT(){
		Calendar c = Calendar.getInstance();
	    
		TimeZone z = c.getTimeZone();
	    int offset = z.getRawOffset();
	    if(z.inDaylightTime(new Date())){
	        offset = offset + z.getDSTSavings();
	    }
	    int offsetHrs = offset / 1000 / 60 / 60;
	    int offsetMins = offset / 1000 / 60 % 60;

	    c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
	    c.add(Calendar.MINUTE, (-offsetMins));

	    //System.out.println("GMT Time Server ::::::::: "+c.getTime());
	    
	    return c.getTime();
	}
}
