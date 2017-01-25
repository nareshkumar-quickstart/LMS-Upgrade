package com.softech.vu360.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.VU360ReportField;

/**
 * @author jason
 * 
 */
public class FormUtil implements java.io.Serializable {
	
	private static final Logger log = Logger.getLogger(FormUtil.class);

	/** Creates a new new FormUtil */
	private FormUtil() {
	}

	private int percent(double d) {
		return percent(d, 1);
	}

	
	public  double roundVal(double val,int decimalPlaces){
		int factor=0;
		double localVal=0;
		
		switch(decimalPlaces)
		{	
			case 1:factor=10; break;
			case 2:factor=100; break;
			case 3:factor=1000; break;
			case 4:factor=10000; break;
			case 5:factor=100000; break;
			case 6:factor=1000000; break;
			case 7:factor=10000000; break;
			case 8:factor=100000000; break;
			case 9:factor=1000000000; break;
			default:factor=100; break;
		}
		
		localVal=val*factor;
		
		localVal=Math.round(localVal);
		localVal= localVal/factor;
		
		return localVal;
		
	}
	
	public String formatReportField(String formatType, Object val) {

		if (formatType == null || val == null || val.toString() == null
				|| val.toString().equalsIgnoreCase("")) {
			return "";
		}
		log.debug("formatType:" + formatType + ":valueType:"
				+ val.getClass());

		try 
		{
			if(formatType.equalsIgnoreCase(VU360ReportField.FORMAT_DATETIME))
			{
				
				log.debug("DateTime:"+val);
				return formatDate((Date) val, "MM/dd/yyyy hh:mm:ss");
			}
			if (formatType.equalsIgnoreCase(VU360ReportField.DT_STRING)) 
			{
				return val.toString();
			} else if (formatType.equalsIgnoreCase(VU360ReportField.FORMAT_DATE)) 
			{
				log.debug("date:"+val);
				return formatDate((Date) val, "MM/dd/yyyy");
			} else if (formatType.equalsIgnoreCase(VU360ReportField.FORMAT_BOOLEAN)) 
			{
				if (val.toString().equalsIgnoreCase("T") || val.toString().equalsIgnoreCase("true") || val.toString().equalsIgnoreCase("1")) 
				{
					return "Yes";
				}
				return "No";
			} else if (formatType.equalsIgnoreCase(VU360ReportField.FORMAT_INTEGER)) {
				if (val instanceof Long) {
					return formatNumericValue(new Integer(((Long) val)
							.intValue()), "#");
				}
				return formatNumericValue(val, "#");
			} else if (formatType.equalsIgnoreCase(VU360ReportField.FORMAT_DOUBLE)) {
				return formatNumericValue(val, "#.##");
			} else if (formatType.equalsIgnoreCase(VU360ReportField.FORMAT_TIME)) {
				if (val instanceof Double) {
					return formatTimeMediumMinutes(((Double) val).intValue());
				} else {
					return formatTimeMediumMinutes(((Number) val).intValue());
				}
			} else if (formatType.equalsIgnoreCase(VU360ReportField.FORMAT_PERCENT)) {
				return (formatNumericValue(val, "#.##")+"%");
				//
			}else if (formatType.equalsIgnoreCase(VU360ReportField.CONVERT_FORMAT_PERCENT)) {
				return (String.valueOf(percent((Number) val, 3)+"%"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// unknown type
		return "unknown format type:" + formatType;
	}

	public int percent(Number d, int nearest) {
		double value = d.doubleValue() / 100;
		return percent(value, nearest);
	}

	public int formatPercent(BigDecimal d, int nearest) {
		double value = d.doubleValue() / 100;
		return percent(value, nearest);
	}

	private int percent(double d, int nearest) {
		if (d < 0) {
			d = 0;
		}
		if (nearest == 0) {
			nearest = 1;
		}
		return nearest * (int) Math.round(100 * d / nearest);
		//return (nearest * 100 * d) ;
	}

	private int percent(float f) {
		return percent(f, 3);
	}

	private int percent(float f, int nearest) {
		if (nearest == 0) {
			nearest = 1;
		}
		return nearest * Math.round(100 * f / nearest);
	}

	public String getSearchBrandElementNumber(float percentage, int min, int max) {
		float scaled = percentage * (max - min) + min; // scaled between min and
														// max
		return new DecimalFormat("00").format(Math.round(scaled));
	}

	public String formatDateSHORT(Date date, Locale loc) {
		return getDateFormatWith(DateFormat.SHORT, loc).format(date);
	}

	public String formatDateMED(Date date, Locale loc) {
		return getDateFormatWith(DateFormat.MEDIUM, loc).format(date);
	}

	public String formatDateLONG(Date date, Locale loc) {
		return getDateFormatWith(DateFormat.LONG, loc).format(date);
	}

	public String formatDateFULL(Date date, Locale loc) {
		return getDateFormatWith(DateFormat.FULL, loc).format(date);
	}

	public String formatDateMEDTimeSHORT(Date date, Locale loc) {
		return getDateTimeFormatWith(DateFormat.MEDIUM, DateFormat.SHORT, loc)
				.format(date);
	}

	public String formatDateTimeSHORT(Date date, Locale loc) {
		return getDateTimeFormatWith(DateFormat.SHORT, DateFormat.SHORT, loc)
				.format(date);
	}

	public String formatDateTimeMED(Date date, Locale loc) {
		if(date==null)
			return "";
		return getDateTimeFormatWith(DateFormat.MEDIUM, DateFormat.MEDIUM, loc)
				.format(date);
	}

	public String formatDateTimeLONG(Date date, Locale loc) {
		return getDateTimeFormatWith(DateFormat.LONG, DateFormat.LONG, loc).format(
				date);
	}

	public String formatDateTimeFULL(Date date, Locale loc) {
		return getDateTimeFormatWith(DateFormat.FULL, DateFormat.FULL, loc).format(
				date);
	}

	public String formatTimeSHORT(Date date, Locale loc) {
		return getTimeFormatWith(DateFormat.SHORT, loc).format(date);
	}

	public String formatTimeMED(Date date, Locale loc) {
		return getTimeFormatWith(DateFormat.MEDIUM, loc).format(date);
	}

	public String formatTimeLONG(Date date, Locale loc) {
		return getTimeFormatWith(DateFormat.LONG, loc).format(date);
	}

	public String formatTimeFULL(Date date, Locale loc) {
		return getTimeFormatWith(DateFormat.FULL, loc).format(date);
	}
	
	public TimeZone getTimeZoneForLocale(Locale loc) {
		Calendar cal = new GregorianCalendar(loc);
		return cal.getTimeZone();
	}

	public String formatAsCurrency(double number) {
		return NumberFormat.getCurrencyInstance().format(number);
	}

	public String formatAsCurrencyPlain(double number) {
		return new DecimalFormat("############.00").format(number); // fix me
																	// please!!!
	}

	public String formatAsCurrencyPlain(String strNumber) {
		return formatAsCurrencyPlain(new Double(strNumber).doubleValue());
	}

	public String formatTextArea(String str) {
		StringBuffer result = new StringBuffer(" ");

		if (str != null) {
			StringTokenizer tokenizer = new StringTokenizer(str, "\n");
			boolean first = true;
			while (tokenizer.hasMoreTokens()) {
				if (first) {
					first = false;
				} else {
					result.append("<br>");
				}
				result.append(tokenizer.nextToken());
			}
		}

		return result.toString();
	}

	public String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (pattern == null || pattern.equals("")) {
			pattern = "MMM d, yyyy";
		}
		SimpleDateFormat sdf = getSimpleDateFormatWith(pattern);
		return sdf.format(date);
	}
	public String daysToDateConversion(Date date, int numberOfDays)
	{
		String convertedDate=null;
		try 
		{
			DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String strtDate = sdf.format(date);
			Date parsedDate = sdf.parse(strtDate);	 
			Calendar calendarNow = Calendar.getInstance();
			calendarNow.setTime(parsedDate);
			calendarNow.add(Calendar.DAY_OF_MONTH, numberOfDays);	
			convertedDate=sdf.format(calendarNow.getTime());
		}
		catch (Exception e) {
			log.error("invalid date for parsing");
		}
		return convertedDate;
	}

	public String formatMonthDayOnly(Date d) {

		SimpleDateFormat format = getSimpleDateFormatWith("MMM dd");
		return format.format(d);
	}

	public String formatShortTimeOnly(Date d) {

		SimpleDateFormat format = getSimpleDateFormatWith("hh:mm aaa");
		return format.format(d);
	}

	public String formatShortDayOfWeek(Date d) {
		SimpleDateFormat format = getSimpleDateFormatWith("EEE");
		return format.format(d);
	}

	// Get the tens or ones value from the minutes, for example
	// from 12:34 AM returns '3' or '4' depending on if
	// place = 'msd' or 'lsd'
	public String getMinutes(Date date, String place) {
		if (date == null) {
			return "";
		}
		if (place == null) {
			return "";
		}
		if (!place.equals("lsd") && !place.equals("msd")) {
			return "";
		}
		SimpleDateFormat sdf = getSimpleDateFormatWith("mm");
		String minutes = sdf.format(date);
		if (place.equals("msd")) {
			return minutes.substring(0, 1);
		}
		if (place.equals("lsd")) {
			return minutes.substring(1);
		}
		// can't get here
		return "";
	}

	public String formatNumber(double number, String pattern) {
		if (pattern == null || pattern.equals("")) {
			pattern = "#";
		}
		if (number < 0) {
			number = 0;
		}
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(number);
	}

	public String formatNumericValue(Object number, String pattern) {
		if (pattern == null || pattern.equalsIgnoreCase("")) {
			pattern = "##";
		}
		if (number instanceof Double) {
			return formatNumber(((Double) number).doubleValue(), "#.##");
		} else if (number instanceof Number) {
			return formatNumber(((Number) number).doubleValue(), pattern);
		} else if (number instanceof Integer) {
			return formatNumber(((Integer) number).doubleValue(), pattern);
		}
		return null;
	}

	public String formatNumber(String str, String pattern) {
		double d = new Double(str).doubleValue();
		return formatNumber(d, pattern);
	}

	public boolean canLaunch(Date startDate, Date endDate, int windowInHours) {
		Date rightNow = new Date();
		long window = windowInHours * 60 * 60 * 1000;

		if (endDate.after(rightNow)
				&& ((startDate.getTime() - window) - rightNow.getTime()) <= 0) {
			return true;
		}
		return false;
	}

	public String formatTimeMediumMinutes(int timeInMinutes) {
		if (timeInMinutes < 0) {
			timeInMinutes = 0;
		}
		return formatTimeMedium(((long) timeInMinutes) * 60L * 1000L);
	}

	public String formatTimeMediumMinutes(double timeInMinutes) {
		return formatTimeMedium(((long) timeInMinutes) * 60L * 1000L);
	}

	public String formatTimeMediumMinutes(long timeInMinutes) {
		return formatTimeMedium(timeInMinutes * 60L * 1000L);
	}

	public String formatTimeMediumSeconds(long timeInSeconds) {
		return formatTimeMedium(timeInSeconds * 1000L);
	}

	public String formatTimeMediumSeconds(int timeInSeconds) {
		return formatTimeMedium(timeInSeconds * 1000L);
	}

	public  String formatSeconds(int number ){
		return new DecimalFormat("00").format(number);
		
		
	}
	
	public String formatTimeMedium(long timeInMillis) {
		int hours = 0;
		int minutes = 0;
		int seconds = 0;

		hours = (int) (timeInMillis / 1000L / 60L / 60L);
		minutes = (int) ((timeInMillis - hours * 1000L * 60L * 60L) / 1000L / 60L);
		seconds = (int) ((timeInMillis - hours * 1000L * 60L * 60L - minutes * 1000L * 60L) / 1000L / 60L);

		DecimalFormat formatter = new DecimalFormat("00");
		return formatter.format(hours) + ":" + formatter.format(minutes);
	}

	public String formatTimeMediumMinutesRounded(int timeInMinutes, int round) {
		if (round == 0) {
			round = 15;
		}
		return formatTimeMedium((long) ((Math.round((double) timeInMinutes
				/ (double) round) * round) * 60 * 1000));
	}

	public String formatTimeMediumMinutesRoundedOptimistic(int timeInMinutes,
			int round) {
		if (round == 0) {
			round = 15;
		}
		return formatTimeMedium((long) (((Math.round((double) timeInMinutes
				/ (double) round) + 1) * round) * 60 * 1000));
	}

	public String formatTimeShort(long timeInMillis) {
		int hours = 0;
		int minutes = 0;
		int seconds = 0;

		hours = (int) (timeInMillis / 1000L / 60L / 60L);
		minutes = (int) ((timeInMillis - hours * 1000L * 60L * 60L) / 1000L / 60L);
		seconds = (int) ((timeInMillis - hours * 1000L * 60L * 60L - minutes * 1000L * 60L) / 1000L / 60L);

		DecimalFormat formatter = new DecimalFormat("00");
		return formatter.format(hours) + ":" + formatter.format(minutes);
	}

	public String formatTimeVerbose(long timeInMillis) {
		int hours = 0;
		int minutes = 0;
		int seconds = 0;

		hours = (int) (timeInMillis / 1000L / 60L / 60L);
		minutes = (int) ((timeInMillis - hours * 1000L * 60L * 60L) / 1000L / 60L);
		seconds = (int) ((timeInMillis - hours * 1000L * 60L * 60L - minutes * 1000L * 60L) / 1000L);

		DecimalFormat formatter = new DecimalFormat("00");
		return formatter.format(hours) + ":" + formatter.format(minutes) + ":"
				+ formatter.format(seconds);
	}
	
	public String formatTimeVeryShortInt(int timeInSeconds) {
		int hours = 0;
		int minutes = 0;
		int seconds = 0;

		hours = (int) (timeInSeconds / 60L / 60L);
		minutes = (int) ((timeInSeconds - hours * 60L * 60L) / 60L);
		seconds = (int) ((timeInSeconds - hours * 60L * 60L - minutes * 60L) / 60L);
		log.debug("hours:"+hours+" -- minutes:"+minutes+" -- seconds:"+seconds);
		DecimalFormat formatter = new DecimalFormat("00");
		return formatter.format(hours) + ":" + formatter.format(minutes);
	}

	private Calendar getCalendarAtTime(long timeInMillis) {
		java.sql.Time when = new java.sql.Time(timeInMillis);
		Calendar cal = new GregorianCalendar();
		Date whenCorrectedForTZ = new Date(when.getTime()
				- cal.get(java.util.Calendar.ZONE_OFFSET));
		cal.setTime(whenCorrectedForTZ);
		return cal;
	}

	/**
	 * Insert the method's description here.
	 * <p>
	 * 
	 * Creation date: (7/11/2001 6:01:57 PM)
	 * 
	 * @return java.lang.String
	 * @param flag
	 *            boolean
	 */
	public String getChecked(boolean flag) {
		return flag ? "checked" : "";
	}

	/**
	 * Insert the method's description here.
	 * <p>
	 * 
	 * Creation date: (7/18/2001 6:37:23 PM)
	 * 
	 * @return java.lang.String
	 * @param throwable
	 *            java.lang.Throwable
	 */
	public String getHTMLStackTrace(Throwable throwable) {
		StringBuffer buf = new StringBuffer(getStackTrace(throwable));
		for (int i = 0; i < buf.length(); i++) {
			if (buf.charAt(i) == '\n') {
				buf.insert(i + 1, "<br>");
			}
		}
		return buf.toString();
	}

	/**
	 * Method to convert an int to a String
	 */
	public String getNumberAsString(int i) {
		return String.valueOf(i);
	}

	/**
	 * Method to convert a double to a String
	 */
	public String getNumberAsString(double d) {
		return String.valueOf(d);
	}

	/**
	 * Method to convert a String to a double
	 */
	public double getStringAsDouble(String s) {
		double result = 0.0;

		try {
			result = Double.valueOf(s).doubleValue();
		} catch (NumberFormatException nfex) {
			result = 0.0;
		}

		return result;
	}

	/**
	 * Method to convert a double to an int, since velocity doesn't do double
	 * arithmetic or comparisons
	 */
	public int getDoubleAsInt(double d) {
		return new Double(d).intValue();

	}

	/**
	 * Method to convert String to an int
	 */
	public int getStringAsInt(String s) {
		int result = 0;

		try {
			result = Integer.parseInt(s);
		} catch (NumberFormatException nfex) {
			result = 0;
		}

		return result;
	}
	
	public static Long[] getStringAsLong (String[] arrString) {
		
		if (arrString != null && arrString.length > 0) {
			Long[] arrLong = new Long[arrString.length];
			for (int index = 0; index < arrString.length; index++) {
				arrLong[index] = Long.valueOf( arrString[index] );
			}
			return arrLong;
		}
		return (new Long[0]);
	}

	/**
	 * Insert the method's description here.
	 * <p>
	 * 
	 * Creation date: (7/11/2001 6:02:56 PM)
	 * 
	 * @return java.lang.String
	 * @param className
	 *            java.lang.String
	 */
	public String getShortClassName(String className) {
		String result = "";

		if (className != null) {
			result = className.substring(className.lastIndexOf(".") + 1);
		}

		return result;
	}

	/**
	 * Insert the method's description here.
	 * <p>
	 * 
	 * Creation date: (7/18/2001 6:28:18 PM)
	 * 
	 * @return java.lang.String
	 * @param throwable
	 *            java.lang.Throwable
	 */
	public String getStackTrace(Throwable throwable) {
		java.io.StringWriter sw = new java.io.StringWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}

	public static Hashtable getFormFromRequest(HttpServletRequest request,
			String[] keysDesired) {
		Hashtable form = new Hashtable();

		for (int i = 0; i < keysDesired.length; i++) {
			String curVal = request.getParameter(keysDesired[i]);
			if (curVal != null)
				form.put(keysDesired[i], curVal);
		}

		return form;
	}

	public Date getDateFromFormElements(String month, String day, String year) {
		SimpleDateFormat sdf = getSimpleDateFormatWith("MM/dd/yyyy");
		Date newDate = null;
		try {
			String dateStr = month + "/" + day + "/" + year;
			newDate = sdf.parse(dateStr);
		} catch (Exception ex) {
			log.error("invalid date for parsing:" + month
					+ "/" + day + "/" + year);
		}

		return newDate;
	}

	/**
	 * Method to work around Velocity's major shortcoming of not being able to
	 * do string concatenation.
	 * 
	 * @param str1
	 *            java.lang.String
	 * @param str2
	 *            java.lang.String
	 * 
	 */
	public String concatenate(String str1, int num) {
		String[] strs = { str1, String.valueOf(num) };
		return concatenate(strs);
	}

	public String concatenate(String[] strs) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; ++i) {
			sb.append(strs[i]);
		}

		return sb.toString();
	}

	/**
	 * Method to work around Velocity's major shortcoming of not being able to
	 * do any math operations on doubles.
	 * 
	 * @param num1
	 *            double
	 * @param num2
	 *            double
	 * @return double the product of num1 and num2
	 */
	public double getProductOf(double num1, double num2) {
		return num1 * num2;
	}

	public static Hashtable addDateRangeToHashtable(Hashtable form) {

		Calendar cal = new java.util.GregorianCalendar();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, NUM_DAY_OFFSET);

		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		form.put(START_DATE_DAY, String.valueOf(day));
		form.put(START_DATE_MONTH, String.valueOf(month));
		form.put(START_DATE_YEAR, String.valueOf(year));

		cal.setTime(new Date());
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);

		form.put(END_DATE_DAY, String.valueOf(day));
		form.put(END_DATE_MONTH, String.valueOf(month));
		form.put(END_DATE_YEAR, String.valueOf(year));

		return form;
	}

	public Date getDateFromFormFieldsUseCalendar(String dateMonth,
			String dateDay, String dateYear) {
		return getDateFromFormFieldsUseCalendar(dateMonth, dateDay, dateYear,
				"0", "0", "0");
	}

	public Date getDateFromFormFieldsUseCalendar(String dateMonth,
			String dateDay, String dateYear, String dateHours,
			String dateMinutes, String dateSeconds) {
		Date d = new Date();

		Calendar cal = Calendar.getInstance();
		try {
			int month = Integer.parseInt(dateMonth);
			int day = Integer.parseInt(dateDay);
			int year = Integer.parseInt(dateYear);
			int hours = Integer.parseInt(dateHours);
			int minutes = Integer.parseInt(dateMinutes);
			int seconds = Integer.parseInt(dateSeconds);

			cal.set(year, month, day, hours, minutes, seconds);
			d = cal.getTime();
		} catch (Exception ex) {
			// oh well
		}

		return d;
	}

	/**
	 * Method to get a SimpleDateFormat object.
	 * 
	 * @param String
	 *            pattern
	 * @return DateFormat
	 */
	protected SimpleDateFormat getSimpleDateFormatWith(String pattern) {
		SimpleDateFormat result = null;
		result = new SimpleDateFormat(pattern);
		return result;
	}

	public Date getDate(String inputDate, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date outputDate = null;
		try {
			outputDate = sdf.parse(inputDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputDate;
	}
	
	public static Date formatToDayStart (String date) {
		return formatDate(date + " 00:00:00", "MM/dd/yyyy HH:mm:ss");
	}
	
	public static Date formatToDayStart (Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // to extract date part only
		return formatDate(sdf.format(date) + " 00:00:00", "MM/dd/yyyy HH:mm:ss");
	}
	
	public static Date formatToDayEnd (String date) {
		return formatDate(date + " 23:59:59", "MM/dd/yyyy HH:mm:ss");
	}
	
	public static Date formatToDayEnd (Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // to extract date part only
		return formatDate(sdf.format(date) + " 23:59:59", "MM/dd/yyyy HH:mm:ss");
	}
	
	public static Date formatDate (String date, String pattern) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);		
		Date formattedDate = null;
		
		try {
			formattedDate = sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return formattedDate;
	}
	
	public String getFormatedDateInfo(Date date, String patern) {
		SimpleDateFormat sdf = new SimpleDateFormat(patern);
		String formatedDateInfo = "";
		try {
			formatedDateInfo = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return formatedDateInfo;
	}
	

	/**
	 * Method to get a DateFormat object.
	 * 
	 * @param int dateStyle the date style of the DateFormat (e.g.
	 *        DateFormat.SHORT, DateFormat.MEDIUM, etc.)
	 * @return DateFormat
	 */
	protected DateFormat getDateFormatWith(int dateStyle, Locale loc) {
		DateFormat result = null;
		result = DateFormat.getDateInstance(dateStyle, loc);
		result.setTimeZone(this.getTimeZoneForLocale(loc));
		return result;
	}

	/**
	 * Method to get a DateFormat object.
	 * 
	 * @param int dateStyle the date style of the DateFormat (e.g.
	 *        DateFormat.SHORT, DateFormat.MEDIUM, etc.)
	 * @param int timeStyle the time style of the DateFormat (e.g.
	 *        DateFormat.SHORT, DateFormat.MEDIUM, etc.)
	 * @return DateFormat
	 */
	protected DateFormat getDateTimeFormatWith(int dateStyle, int timeStyle, Locale loc) {
		DateFormat result = null;
		result = DateFormat.getDateTimeInstance(dateStyle, timeStyle, loc);
		result.setTimeZone(this.getTimeZoneForLocale(loc));
		return result;
	}

	/**
	 * Method to get a DateFormat object.
	 * 
	 * @param int timeStyle the time style of the DateFormat (e.g.
	 *        DateFormat.SHORT, DateFormat.MEDIUM, etc.)
	 * @return DateFormat
	 */
	protected DateFormat getTimeFormatWith(int timeStyle, Locale loc) {
		DateFormat result = null;

		result = DateFormat.getTimeInstance(timeStyle, loc);
		result.setTimeZone(this.getTimeZoneForLocale(loc));
		return result;
	}
	
	public int multiply(int left, int right) {
		return left * right;
	}

	public static FormUtil getInstance() {
		if (formUtil == null) {
			formUtil = new FormUtil();
		}
		return formUtil;
	}

	private static final String START_DATE_DAY = "startDateDay";
	private static final String START_DATE_MONTH = "startDateMonth";
	private static final String START_DATE_YEAR = "startDateYear";
	private static final String END_DATE_DAY = "endDateDay";
	private static final String END_DATE_MONTH = "endDateMonth";
	private static final String END_DATE_YEAR = "endDateYear";

	private static final long serialVersionUID = -6441442117860279366L;
	private static final int NUM_DAY_OFFSET = -7;

	private static FormUtil formUtil = null;
	
	public String getLMSDomain(Brander brander){
		String lmsDomain = brander.getBrandElement("lms.domain");
		   
		if(lmsDomain==null || lmsDomain.isEmpty())
		   {
		    // URL is not branded here, fetch domain from property file
			lmsDomain = VU360Properties.getVU360Property("lms.domain");
		   }
		   return lmsDomain;
		}

	
	/**
	 * 
	 * @param businessObjectList list from which array will be made 
	 * @return
	 * @author sultan.mubasher
	 */
	public static Object[] getPropertyArrayFromList(List businessObjectList) {
		return getPropertyArrayFromList(businessObjectList,null);
	}
	
	/**
	 * 
	 * @param businessObjectList list from which array will be made 
	 * @param propertyName provide if want array of specific column
	 * @return
	 * @author sultan.mubasher
	 */
	public static Object[] getPropertyArrayFromListByPropertyName(List businessObjectList,String propertyName) {
		if(StringUtils.isEmpty(propertyName))
			return new Object[0];
		return getPropertyArrayFromList(businessObjectList, Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1) );
	}
	
	/**
	 * Will return array of objects. By default it will call getId method. property name should be as it declared
	 * in business object(BO).
	 * Declared private because it will be used by getPropertyArrayFromList and getPropertyArrayFromListByPropertyName
	 * 
	 * @param businessObjectList list from which array will be made 
	 * @param propertyName provide if want array of specific column if not provided it will by default call ID property of object
	 * 
	 * @return
	 * @author sultan.mubasher
	 */
	private static Object[] getPropertyArrayFromList(List businessObjectList,String propertyName) {
		List<Object> propertyList=new ArrayList<Object>();
		if(businessObjectList==null || businessObjectList.isEmpty()){
			return new Object[0];
		}
		String methodName =null;
		
		if(!StringUtils.isEmpty(propertyName))
			methodName = "get"+propertyName;
		else
			methodName = "getId";
		
		for(Object businessObject:businessObjectList) {
			
			try{
				Method method=businessObject.getClass().getMethod(methodName, null);
				propertyList.add(method.invoke(businessObject, null));
				
			}catch(NoSuchMethodException e){
			}catch (IllegalAccessException e){
			}catch (InvocationTargetException e){
			}
			
		}
		
		return propertyList.toArray();
	}
	
	public static int parseNumber(String strNumber){
		int result = 0;
		
		try{
			result = Integer.parseInt(strNumber);
			
		}
		catch (Exception e) {
			return result;
		}
		
		
		return result;
	}
	
	public static Cookie getCookieByName(HttpServletRequest request,String cookieName) {
		Cookie cookie = null;
		Cookie[] cookies = null;

		if (request != null) {
			cookies = request.getCookies();
		}

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName() != null
					&& cookies[i].getName().equalsIgnoreCase(cookieName)) {
					cookie = cookies[i];
				}
			}
		}
		
		return cookie;
	}

}