/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.web.controller.model.ClassForm;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.FormUtil;

/**
 * @author Noman
 *
 */

public class AddSynchronousClassScheduleValidator implements Validator {
	
	private static final Logger log = Logger.getLogger(AddSynchronousClassScheduleValidator.class.getName());

	/**
	 * 
	 */
	public AddSynchronousClassScheduleValidator() {
		// TODO Auto-generated constructor stub
	}


	public boolean supports(Class clazz) {
		return ClassForm.class.isAssignableFrom(clazz);
	}


	public void validate(Object obj, Errors errors) {
		ClassForm classForm = (ClassForm)obj;
		// TODO call the individual page validation routines
		validatePage1(classForm, errors);
	}


	public void validatePage1(ClassForm classForm, Errors errors){
		log.debug("Test");

		if (StringUtils.isBlank(classForm.getClassName())) {
			errors.rejectValue("className", "error.addNewSynchrounousClass.className.required");
		}
		if (StringUtils.isBlank(classForm.getCourseName())) {
			errors.rejectValue("courseName", "error.addNewSynchrounousClass.courseName.required");
		}		
		
		if (classForm.isOnlineMeetingTF()) {
			
			String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
			
			// for dimdim meeting type we hide meeting id and passcode and generates at back end. so need for validation if its dimdim
			if(!classForm.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_DIMDIM) && !classForm.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_OTHERS) 
					 && !classForm.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_WEBINAR)){
				if ( StringUtils.isBlank(classForm.getMeetingId()) ) {
					errors.rejectValue("meetingId", "error.addNewSynchrounousClass.meetingID.required");
				}
				if ( StringUtils.isBlank(classForm.getMeetingPasscode()) ) {
					errors.rejectValue("meetingPasscode", "error.addNewSynchrounousClass.meetingPassCode.required");
				}
			} else if (!classForm.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_DIMDIM) && classForm.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_OTHERS)) {
				if ( StringUtils.isBlank(classForm.getMeetingURL()) ) {
					errors.rejectValue("meetingURL", "error.addNewSynchrounousClass.meetingURL.required");
				} else if( !StringUtils.isBlank(classForm.getMeetingURL()) && !IsMatch(classForm.getMeetingURL(), regex) ) {
					errors.rejectValue("meetingURL", "error.addNewSynchrounousClass.meetingURL.invalid");
				}
			}
			else if (classForm.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_WEBINAR) ) {
				if ( StringUtils.isBlank(classForm.getPresenterFirstName()) ) {
					errors.rejectValue("presenterFirstName", "error.addNewSynchrounousClass.presenterFirstName.required");
				}
				if ( StringUtils.isBlank(classForm.getPresenterLastName()) ) {
					errors.rejectValue("presenterLastName", "error.addNewSynchrounousClass.presenterLastName.required");
				}
				if ( StringUtils.isBlank(classForm.getPresenterEmailAddress()) ) {
					errors.rejectValue("presenterEmailAddress", "error.addNewSynchrounousClass.presenterEmailAddress.required");
				}
				else if(!FieldsValidation.isEmailValid(classForm.getPresenterEmailAddress())){
					errors.rejectValue("presenterEmailAddress", "error.addNewSynchrounousClass.presenterEmailAddress.invalid");
				}
			}
		}
		else { //  [10/13/2010] LMS-7029 :: Class size validation for Offline Courses only
			if ( StringUtils.isBlank(classForm.getClassSize()) ||  (!StringUtils.isNumericSpace(classForm.getClassSize()) ||(Integer.parseInt(classForm.getClassSize())<1))) {
				errors.rejectValue("classSize", "error.addNewSynchrounousClass.validClassMaxSize.required");
			}
		}
		
		if (classForm.getEnrollmentCloseDate()==null || StringUtils.isBlank(classForm.getEnrollmentCloseDateString())) {
			errors.rejectValue("enrollmentCloseDate", "error.addNewSynchrounousClass.enrollmentCloseDate.required");
		}
	} 

	public void validatePage2(ClassForm classForm, Errors errors){

		if(!classForm.getPattern().equalsIgnoreCase("once"))
		{ 
			if (StringUtils.isBlank(classForm.getStartDate())) 
				errors.rejectValue("startDate", "error.addNewSynchrounousClass.className.scheduleStartDate");
		}
		
		if ( !StringUtils.isBlank(classForm.getRadioDaily()) && classForm.getPattern().equalsIgnoreCase("daily")) {
			if ( classForm.getRadioDaily().equalsIgnoreCase("EveryDay")) {
				if( StringUtils.isBlank( classForm.getNumberOfWeekDays().trim()) || ! StringUtils.isNumericSpace( classForm.getNumberOfWeekDays().trim() ) ){
					errors.rejectValue("numberOfWeekDays", "error.addNewSynchrounousClass.validDailyRecurrenceDaysValue.required");
				}else{
					try{
						classForm.setWeekDaysNumber(Integer.parseInt(classForm.getNumberOfWeekDays().trim() ) ) ;
					}
					catch(NumberFormatException e){
						classForm.setWeekDaysNumber(0);
						e.printStackTrace();
					}
				}
			}			
		}
		
		if ( classForm.getPattern().equalsIgnoreCase("daily")) {
			if( StringUtils.isBlank(classForm.getRadioDaily()) ){
				errors.rejectValue("numberOfWeekDays", "error.addNewSynchrounousClass.selectDailyRecurrenceValue.required");				
			}
		}
		
		if ( classForm.getPattern().equalsIgnoreCase("weekly")) {
			
			if( StringUtils.isBlank( classForm.getRecureEvery().trim()) || ! StringUtils.isNumericSpace( classForm.getRecureEvery().trim() ) ){
				errors.rejectValue("recureEvery", "error.addNewSynchrounousClass.validWeeklyRecurrenceDaysValue.required");
			}else{
				try{
					classForm.setWeekDaysNumber(Integer.parseInt(classForm.getRecureEvery().trim() ) ) ;
				}
				catch(NumberFormatException e){
					classForm.setWeekDaysNumber(0);
					e.printStackTrace();
				}
			}
						
		}
		
		if (  StringUtils.isBlank( classForm.getEndRange() ) ){			
			errors.rejectValue("endDate", "error.addNewSynchrounousClass.className.scheduleEndDateOption");			 			
		}
		else
		{

			if (   classForm.getEndRange().equalsIgnoreCase("EndAfter")  ){
				if (StringUtils.isBlank(classForm.getTotalOccurences())) {
					errors.rejectValue("endDate", "error.addNewSynchrounousClass.className.scheduleEndAfterValueMissing");
				}			
				else if (!StringUtils.isNumericSpace(classForm.getTotalOccurences())) {
					errors.rejectValue("endDate", "error.addNewSynchrounousClass.className.scheduleEndAfterValueMissing");
				}			
			}
	
			if (   classForm.getEndRange().equalsIgnoreCase("EndDate")  ){
				if (StringUtils.isBlank(classForm.getEndDate())) {
					errors.rejectValue("endDate", "error.addNewSynchrounousClass.className.scheduleEndDate");
				}			
			}
		}	
		
		if (StringUtils.isBlank(classForm.getStartTime())) {
			errors.rejectValue("startTime", "error.addNewSynchrounousClass.className.scheduleStartTime");
		}
		if (StringUtils.isBlank(classForm.getEndTime())) {
			errors.rejectValue("endTime", "error.addNewSynchrounousClass.className.scheduleEndTime");
		}
		
		checkMonthlyOptionsSelected( classForm,  errors);
		checkYearlyOptionsSelected( classForm,  errors);
		
		/*
		 * Setting up Start Date and time
		 */
		if (!StringUtils.isBlank(classForm.getStartDate().toString()) && !StringUtils.isBlank(classForm.getStartTime().toString())) {			
			try {
				classForm.setStartDateTime(FormUtil.getInstance().getDate(classForm.getStartDate() + " " + classForm.getStartTime(), "MM/dd/yyyy hh:mm a"));
			} catch (Exception e) {
				
			}
		}
		
		/*
		 * Setting up End Date and time
		 */
		if (!StringUtils.isBlank(classForm.getEndDate().toString()) && !StringUtils.isBlank(classForm.getEndTime().toString())) {			
			try {
				classForm.setEndDateTime(FormUtil.getInstance().getDate(classForm.getEndDate() + " " + classForm.getEndTime(), "MM/dd/yyyy hh:mm a"));
			} catch (Exception e) {
				
			}
		}
		
		if (classForm.getStartDateTime() != null && classForm.getEndDateTime() != null ) {
			if (classForm.getStartDateTime().after(classForm.getEndDateTime())) 
				errors.rejectValue("startDate", "error.addNewSynchrounousClass.className.scheduleStartEnd");
		}
		
	}

	public void validatePage3(ClassForm classForm, Errors errors){
		log.debug("Test");

		if (!StringUtils.isBlank(classForm.getEndDate().toString()) && !StringUtils.isBlank(classForm.getEndTime().toString())) {
			try {
				classForm.setEndDateTime(FormUtil.getInstance().getDate(classForm.getEndDate() + " " + classForm.getEndTime(), "MM/dd/yyyy hh:mm a"));
			} catch (Exception e) {
				errors.rejectValue("endDate", "End date or time is not correct");
			}
		}
		
		if (!StringUtils.isBlank(classForm.getStartDate().toString()) && !StringUtils.isBlank(classForm.getStartTime().toString())) {			
			try {
				classForm.setStartDateTime(FormUtil.getInstance().getDate(classForm.getStartDate() + " " + classForm.getStartTime(), "MM/dd/yyyy hh:mm a"));
			} catch (Exception e) {
				errors.rejectValue("startDate", "Start date or time is not correct");
			}
		}
		
		if (classForm.getStartDateTime() != null && classForm.getEndDateTime() != null ) {
			if (classForm.getStartDateTime().after(classForm.getEndDateTime())) 
				errors.rejectValue("startDate", "Start date and time must come before end date and time.");
		}
		
		if(classForm.getClassSessionList().isEmpty()){
			if (StringUtils.isBlank(classForm.getStartDate().toString())) {
				errors.rejectValue("startDate", "Start date is required");
			} 
			
			if (StringUtils.isBlank(classForm.getEndDate().toString())) {//will not be checked if 
				errors.rejectValue("endDate", "End date is required");
			}
		}
		if (StringUtils.isBlank(classForm.getStartTime().toString())) {
			errors.rejectValue("startTime", "Start time is required");
		}
		
		if (StringUtils.isBlank(classForm.getEndTime().toString())) {
			errors.rejectValue("endTime", "End time is required");
		}
	
	}
	
	private void checkMonthlyOptionsSelected( ClassForm classForm, Errors errors){
		
		if ( classForm.getPattern().equalsIgnoreCase("monthly")) {
			if( StringUtils.isBlank(classForm.getMonthlyOption()) ){
				errors.rejectValue("monthlyOption", "error.addNewSynchrounousClass.selectMonthlyRecurrenceValue.required");				
			}else if( classForm.getMonthlyOption().trim().equalsIgnoreCase("upperRadioButton") ){

				try{
					classForm.setMonthInputInteger(Integer.parseInt(classForm.getMonthInputString().trim() ) ) ;
					classForm.setDayInputInteger(Integer.parseInt(classForm.getDayInputString().trim() ) ) ;
				}
				catch(NumberFormatException e){
					errors.rejectValue("monthlyOption", "error.addNewSynchrounousClass.validMonthlyRecurrenceValue.required");
					classForm.setMonthInputInteger(0);
					classForm.setDayInputInteger(0);
					e.printStackTrace();
				}
				
			}
			
			else if( classForm.getMonthlyOption().trim().equalsIgnoreCase("lowerRadioButton") ){

				try{
					classForm.setMonthInputInteger(Integer.parseInt(classForm.getMonthInputString2().trim() ) ) ;
					
				}
				catch(NumberFormatException e){
					errors.rejectValue("monthlyOption", "error.addNewSynchrounousClass.validMonthlyRecurrenceValue.required");
					classForm.setMonthInputInteger(0);
					
					e.printStackTrace();
				}
				
			}

		}
		
	}
	private void checkYearlyOptionsSelected( ClassForm classForm, Errors errors){

		
		if ( classForm.getPattern().equalsIgnoreCase("yearly")) {
			if( StringUtils.isBlank(classForm.getYearlyMonthOption() ) ){
				errors.rejectValue("yearlyMonthOption", "error.addNewSynchrounousClass.selectYearlyRecurrenceValue.required");				
			}else if( classForm.getYearlyMonthOption().trim().equalsIgnoreCase("upperRadioButton") ){

				try{
					classForm.setYearlyUpperMonthNumber(Integer.parseInt(classForm.getYearlyUpperMonthNumberString().trim() ) ) ;
					classForm.setYearlyUpperMonthNameInt(Integer.parseInt(classForm.getYearlyUpperMonthString().trim()  )) ;
				}
				catch(NumberFormatException e){
					errors.rejectValue("yearlyMonthOption", "error.addNewSynchrounousClass.validYearlyRecurrenceValue.required");
					classForm.setYearlyUpperMonthNumber(0  ) ;
					classForm.setYearlyUpperMonthNameInt(0) ;
					e.printStackTrace();
				}
				
			}
/*			
			else if( classForm.getMonthlyOption().trim().equalsIgnoreCase("lowerRadioButton") ){

				try{
					classForm.setMonthInputInteger(Integer.parseInt(classForm.getMonthInputString2().trim() ) ) ;
					
				}
				catch(NumberFormatException e){
					errors.rejectValue("monthlyOption", "error.addNewSynchrounousClass.validMonthlyRecurrenceValue.required");
					classForm.setMonthInputInteger(0);
					
					e.printStackTrace();
				}
				
			}
*/
		}		
	}

	private static boolean IsMatch(String s, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
        	return false;
        }  
	}
	
}
