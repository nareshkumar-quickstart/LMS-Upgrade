/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.SessionForm;
import com.softech.vu360.util.FormUtil;

/**
 * @author Noman
 *
 */

public class AddSynchronousSessionRecurrenceValidator implements Validator {
	
	private static final Logger log = Logger.getLogger(AddSynchronousSessionRecurrenceValidator.class.getName());

	/**
	 * 
	 */
	public AddSynchronousSessionRecurrenceValidator() {
		// TODO Auto-generated constructor stub
	}


	public boolean supports(Class clazz) {
		return SessionForm.class.isAssignableFrom(clazz);
	}


	public void validate(Object obj, Errors errors) {
		SessionForm sessionForm = (SessionForm)obj;
		// TODO call the individual page validation routines
		validatePage1(sessionForm, errors);
	}


	public void validatePage1(SessionForm sessionForm, Errors errors){
		log.debug("Test");

		if (!StringUtils.isBlank(sessionForm.getEndDate().toString()) && !StringUtils.isBlank(sessionForm.getEndTime().toString())) {
			try {
				sessionForm.setEndDateTime(FormUtil.getInstance().getDate(sessionForm.getEndDate() + " " + sessionForm.getEndTime(), "MM/dd/yyyy hh:mm a"));
			} catch (Exception e) {
				errors.rejectValue("endDate", "End date or time is not correct");
			}
		}
		
		if (!StringUtils.isBlank(sessionForm.getStartDate().toString()) && !StringUtils.isBlank(sessionForm.getStartTime().toString())) {			
			try {
				sessionForm.setStartDateTime(FormUtil.getInstance().getDate(sessionForm.getStartDate() + " " + sessionForm.getStartTime(), "MM/dd/yyyy hh:mm a"));
			} catch (Exception e) {
				errors.rejectValue("startDate", "Start date or time is not correct");
			}
		}
		
		if (sessionForm.getStartDateTime() != null && sessionForm.getEndDateTime() != null ) {
			if (sessionForm.getStartDateTime().after(sessionForm.getEndDateTime())) 
				errors.rejectValue("startDate", "Start date and time must come before end date and time.");
		}
		
		if (StringUtils.isBlank(sessionForm.getStartDate().toString())) {
			errors.rejectValue("startDate", "Start date is required");
		} 
		
		if (StringUtils.isBlank(sessionForm.getEndDate().toString())) {
			errors.rejectValue("endDate", "End date is required");
		}
		
		if (StringUtils.isBlank(sessionForm.getStartTime().toString())) {
			errors.rejectValue("startTime", "Start time is required");
		}
		
		if (StringUtils.isBlank(sessionForm.getEndTime().toString())) {
			errors.rejectValue("endTime", "End time is required");
		}

	
	}

}
