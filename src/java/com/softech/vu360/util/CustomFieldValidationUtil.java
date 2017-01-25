package com.softech.vu360.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomFieldValidationUtil {
	
	/**isNumeric: Validate a number using Java regex.
	* This method checks if the input string contains all numeric characters.
	* @param number String. Number to validate
	* @return boolean: true if the input is all numeric, false otherwise.
	*/
	public static boolean isNumeric(String number){
		boolean isValid = false;

		/*Number   : A numeric value will have following format:
		 * ^[-+]?  : Starts with an optional "+" or "-" sign.
		 * [0-9]*  : May have one or more digits.
		 * \\.?    : May contain an optional "." (decimal point) character.
		 * [0-9]+$ : ends with numeric digit.
		 */

		/* Initialize reg ex for numeric data. */
		String expression = "^[-+]?[0-9]*\\.?[0-9]+$";
		CharSequence inputStr = number;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if(matcher.matches()){
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Validate whether the argument string can be parsed into a 
	 * legal date.<br />
	 * 
	 * Does check for formating errors and illegal data (so an invalid
	 * month or day number is detected).
	 * @param allowPast set to true to allow dates in the past, false if
	 * only dates in the future should be allowed.
	 * @param formatStr date format string to be used to validate against
	 * @return true if a correct date and conforms to the restrictions
	 */
	public static  boolean isValidDate(String dateStr, 
			boolean allowPast, 
			String formatStr)
	{
		if (formatStr == null) return false;
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
		Date testDate = null;
		try
		{
			testDate = df.parse(dateStr);
		}
		catch (ParseException e)
		{
			/* invalid date format */
			return false;
		}
		if (!allowPast)
		{
			/* Initialize the calendar to midnight to prevent 
			 * the current day from being rejected
			 */
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			if (cal.getTime().after(testDate)) return false;
		}
		// now test for legal values of parameters
		if (!df.format(testDate).equals(dateStr)) return false;
		return true;
	}

	/** isSSNValid: Validate Social Security number (SSN) using Java reg ex.
	 * This method checks if the input string is a valid SSN.
	 * @param ssn String. Social Security number to validate
	 * @return boolean: true if social security number is valid, false otherwise.
	 */
	public static  boolean isSSNValid(String ssn){
		boolean isValid = false;

		/* SSN format xxx-xx-xxxx, xxxxxxxxx, xxx xx xxxx;
		 *
		 * ^([0-6]\d{2}|7[0-6]\d|77[0-2]) : In the first group, the valid numbers are 001 through 772. 
		 * The "^" at the front says the value must start with this - there can't be anything before it in 
		 * the value. 000 will be handled later. So this part of the regular expression checks for the values
		 * 000 through 772. Note that there are three groups of checks that are "or"ed together using the | 
		 * character. If the numbers 0 through 6 appear in the first position (that's what "[0-6]" says - the 
		 * group of characters in ASCII order between "0" and "6", inclusive), then any two numbers 
		 * (\d means "digit" and {2} means "exactly two") can follow it. If the number 7 is followed by any 
		 * number 0 through 6, then the last number can be any digit ("{1}" isn't needed, but could be used). 
		 * If the first two numbers are 77, then the last number must be in the range 0 through 2. So that's 
		 * what the first part of the regular expression is doing.
		 *  
		 * [- ]? : This checks for the separator. The value can be either a space or a dash, and must occur 
		 * zero or one time (zero allows for no punctuation). The "?" character means "the preceding must 
		 * occur a maximum of one time, but it doesn't have to appear". The parentheses around the grouping 
		 * will "remember" the value. But there's parentheses around the first part as well, so this is the 
		 * second group that is remembered.
		 * 
		 * \\d{2} : This is the check for the second group of numbers. Since "00" will be handled later, 
		 * we can check for any two digits. After that, we repeat the check for the second remembered group 
		 * in the regular expression. So, if a dash was used to separate the first and second groups, a dash 
		 * must be used here.
		 * 
		 * [- ]? : May contain an optional second "-" character.
		 * 
		 * \\d{4} : Finally, the last group must be four digits (we'll check for "0000" later on) and this 
		 * must terminate the string (that's what the "$" character does). So the entire regular expression 
		 * checks for strings like "123-45-6789". "773-45-6789" will fail ("772" in the first group is the 
		 * largest value allowed). But "000-00-0000" will pass when we don't want it to.
		 *	
		 *	Examples: 772-89-8989; 772878789 etc.
		 */

		//Initialize reg ex for SSN.
		String lFirstCounter ="";
		String lSecondCounter ="";
		String lThirdCounter ="";
		String lFouthCounter ="";
		String lFifthCounter ="";

		//String expression = "^([0-6]\\d{2}|7[0-6]\\d|77[0-2])([- ]?)(\\d{2})([- ]?)(\\d{4})$";
		String expression = "^([0-8]\\d{2})([- ]?)(\\d{2})([- ]?)(\\d{4})$";
		CharSequence inputStr = ssn;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if(matcher.matches()){
			isValid = true;
			lFirstCounter= matcher.group(1)==null?"":matcher.group(1);
			lSecondCounter= matcher.group(2)==null?"":matcher.group(2);
			lThirdCounter = matcher.group(3)==null?"":matcher.group(3);
			lFouthCounter= matcher.group(4)==null?"":matcher.group(4);
			lFifthCounter= matcher.group(5)==null?"":matcher.group(5);
			if(!lSecondCounter.equals(lFouthCounter))isValid = false;
			if(lFirstCounter.equals("000"))isValid = false;
			if(lThirdCounter.equals("00"))isValid = false;
			if(lFifthCounter.equals("0000"))isValid = false;
		}
		return isValid;
	}
	
	
	/**isTelephoneValid: Validate a telephoneNumber using Java regex.
	* This method checks if the input string contains valid telephone number.
	* @param number String. Number to validate
	* @return boolean: true if the input is all numeric, false otherwise.
	*/
	public static boolean isTelephoneNumberValid(String telephoneNumber){
		boolean isValid = false;

		/*Telephone Number 		: A telephone number value will have following format:
		 * [(]\\d{3}[)]  		: Starts with an open round bracket then 3 digit in between and ending round bracket.
		 * \\d{3}				: Then have space and 3 digits
		 * -					: Then hyphen sign
		 * \\d{4}$				: End with 4 digits
		 */

		/* Initialize reg ex for telephone number data. */
		String expression = "[(]\\d{3}[)] \\d{3}-\\d{4}$";
		CharSequence inputStr = telephoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if(matcher.matches()){
			isValid = true;
		}
		return isValid;
	}
	
	public static boolean isValidBirthDate(String dateStr, String format, int minYears) {
		if (format == null) return false;
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date testDate = null;
		try
		{
			testDate = df.parse(dateStr);
		}
		catch (ParseException e)
		{
			/* invalid date format */
			return false;
		}
		if(getDiffYears(new Date(), testDate)  < minYears) return false;
		return true;
	}
	
	public static int getDiffYears(Date first, Date last) {
	    Calendar a = getCalendar(first);
	    Calendar b = getCalendar(last);
	    int years1 = b.get(Calendar.YEAR);
	    int years2 = a.get(Calendar.YEAR);
	    int diff =  years2-years1;
	    if (a.get(Calendar.MONTH) < b.get(Calendar.MONTH) || 
	        (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) < b.get(Calendar.DATE))) {
	        diff--;
	    }
	    return diff;
	}

	public static Calendar getCalendar(Date date) {
	    Calendar cal = Calendar.getInstance(Locale.US);
	    cal.setTime(date);
	    return cal;
	}

	
}
