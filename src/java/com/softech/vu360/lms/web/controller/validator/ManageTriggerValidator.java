package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.ManageAlertTriggerForm;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.IApplicationConstants;

public class ManageTriggerValidator implements Validator {

	
	
	@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		ManageAlertTriggerForm form = (ManageAlertTriggerForm)obj;
		validateFirstPage(form,errors);
		
	}
	
public void validateFirstPage( ManageAlertTriggerForm form, Errors errors ) {
		
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	Date sDate = null;	
	
	if( StringUtils.isBlank(form.getTrigger().getTriggerName()) ) {
			errors.rejectValue("triggerName", "error.surveyTemplateFlag.triggerName.required");
		}
		if(form.getEvent_date()!=null && form.getEvent_date().equals("event")){
		if( form.getSelectedEventsId()== null )	{
			errors.rejectValue("selectedEventsId", "error.surveyTemplateFlag.event.required");
		}
		/*
		if(form.getSelectedEventsId()!= null && form.getSelectedCredentialId()==null && !form.getLicenseExpireDate().equals("")){
			errors.rejectValue("selectedEventsId", "error.surveyTemplateFlag.event.licenseType.required");
		}
		*/
		if(form.getSelectedEventsId()!= null && form.getSelectedCredentialId()==null && !form.getLicenseExpireDate().equals("")){
			errors.rejectValue("selectedEventsId", "error.surveyTemplateFlag.event.licenseType.required");
		}
		else if(form.getSelectedEventsId()!= null && form.getSelectedCredentialId()==null && IApplicationConstants.CE_DUE_REMINDER.equals(form.getSelectedEventsText()) ){
			errors.rejectValue("selectedEventsId", "error.surveyTemplateFlag.event.licenseType.required");
		}
		if(form.getSelectedEventsId()!= null && form.getSelectedCredentialId() !=null &&  IApplicationConstants.CE_DUE_REMINDER.equals(form.getSelectedEventsText()) ){
			if( StringUtils.isBlank(form.getLicenseExpireDate()) ) {
				errors.rejectValue("licenseExpireDate", "error.surveyTemplateFlag.event.licenseExpireDate.required","");
			} 
			else if (!FieldsValidation.isValidDate(form.getLicenseExpireDate())) 
			{
						errors.rejectValue("licenseExpireDate", "error.surveyTemplateFlag.event.licenseExpireDate.invalid","");
			} 
			else 
			{
						try {
							sDate = formatter.parse(form.getLicenseExpireDate());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						//validateIfDateIsAtleastCurrentDate(sDate, "effectiveStartDate", errors);
			}
		}
		if(IApplicationConstants.CE_DUE_REMINDER.equals(form.getSelectedEventsText()) && form.getSelectedCredentialId() ==null)
		{
			if( StringUtils.isBlank(form.getLicenseExpireDate()) ) {
				errors.rejectValue("licenseExpireDate", "error.surveyTemplateFlag.event.licenseExpireDate.required","");
			} 
			else if (!FieldsValidation.isValidDate(form.getLicenseExpireDate())) 
			{
						errors.rejectValue("licenseExpireDate", "error.surveyTemplateFlag.event.licenseExpireDate.invalid","");
			} 
			else 
			{
						try {
							sDate = formatter.parse(form.getLicenseExpireDate());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						//validateIfDateIsAtleastCurrentDate(sDate, "effectiveStartDate", errors);
			}
		}
		}
		if(form.getEvent_date()!=null && form.getEvent_date().equals("date")){
		if( StringUtils.isBlank(form.getDate()) )	{
			errors.rejectValue("date", "error.surveyTemplateFlag.date.required");
		}
		/**
		 * LMS-19003 Added By marium Saud
		 * Add validation for Selected Date
		 */
		else {
			Date date = new Date(System.currentTimeMillis());
			date=DateUtils.truncate(date, Calendar.DATE);
			Date eventtDate = null;
			try {
				eventtDate = formatter.parse(form.getDate());
				if(eventtDate.before(date)){
					errors.rejectValue("date", "error.surveyTemplateFlag.date.selectDateCannotLessToCurrentDate");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		}
		
		
	}

}
