package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.ManageMyAlertForm;

public class ManageMyAlertValidator implements Validator{
	
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		ManageMyAlertForm form = (ManageMyAlertForm)obj;
		validateFirstPage(form,errors);
		
	}
	
public void validateFirstPage( ManageMyAlertForm form, Errors errors ) {
		
		if( StringUtils.isBlank(form.getAlert().getAlertName()) ) {
			errors.rejectValue("alertName", "error.surveyTemplateFlag.alertName.required");
		}
		if(form.getEvent_date()!=null && form.getEvent_date().equals("event")){
		if( form.getSelectedEventsId()== null )	{
			errors.rejectValue("selectedEventsId", "error.surveyTemplateFlag.event.required");
		}
		}
		if(form.getEvent_date()!=null && form.getEvent_date().equals("date")){
		if( StringUtils.isBlank(form.getDate()) )	{
			errors.rejectValue("date", "error.surveyTemplateFlag.date.required");
		}
		}
		/*if(((form.getDailyRecurrrenceEveryWeekDay().equals("everyDay")&&form.getDailyRecurrrenceEveryDay().equalsIgnoreCase("0")))||(form.getDailyRecurrrenceEveryWeekDay().equals("everyDay")&&StringUtils.isBlank(form.getDailyRecurrrenceEveryDay()))) {
			if(form.getEvent_date().equals("date")&& form.isRecurring()){
			errors.rejectValue("dailyRecurrrenceEveryDay", "error.surveyTemplateFlag.validnumber.required");
			}
		}*/
		
	}

}
