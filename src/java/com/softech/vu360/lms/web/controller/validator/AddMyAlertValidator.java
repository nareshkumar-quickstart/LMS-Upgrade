package com.softech.vu360.lms.web.controller.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.model.AddMyAlertForm;

public class AddMyAlertValidator implements Validator{

	
	private SurveyService surveyService;
	public boolean supports(Class clazz) {
		return AddMyAlertForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddMyAlertForm form = (AddMyAlertForm)obj;
		validateFirstPage(form,errors);
		//validateSecondPage(form,errors);
		//validateQuestion(form,errors);
		//validateFinishPage(form,errors);
	}
	public void validateFirstPage( AddMyAlertForm form, Errors errors ) {
		//errors.setNestedPath("alert");
		List<Alert> alerts = new ArrayList<Alert>();
		com.softech.vu360.lms.vo.VU360User logInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if( StringUtils.isBlank(form.getAlert().getAlertName()) ) {
			errors.rejectValue("alert.alertName", "error.surveyTemplateFlag.alertName.required");
		}
		
		/*if(((form.getDailyRecurrrenceEveryWeekDay().equals("everyDay")&&form.getDailyRecurrrenceEveryDay().equalsIgnoreCase("0")))||(form.getDailyRecurrrenceEveryWeekDay().equals("everyDay")&&StringUtils.isBlank(form.getDailyRecurrrenceEveryDay()))) {
			if(form.getEvent_date().equals("date")&& form.isRecurring()){
			errors.rejectValue("dailyRecurrrenceEveryDay", "error.surveyTemplateFlag.validnumber.required");
			}
		}*/
		if(form.getEvent_date()!=null && form.getEvent_date().equals("date")){
			if( StringUtils.isBlank(form.getDate()) )	{
				errors.rejectValue("date", "error.surveyTemplateFlag.date.required");
			}
		}
		if(form.getEvent_date()!=null && form.getEvent_date().equals("event")){
			if( form.getSelectedEventsId() == null )	{
				errors.rejectValue("selectedEventsId", "error.surveyTemplateFlag.event.required");
			}
		}
		if(form.getAlert().getAlertName()!=null && form.getAlert().getAlertName()!="" ) {
			alerts = surveyService.findAlert(logInUser.getId() , "");
			for(Alert alert : alerts){
				if(alert.getAlertName().equals(form.getAlert().getAlertName())){
					errors.rejectValue("alert.alertName", "error.surveyTemplateFlag.alertNameDuplicate.required");
					break;
				}
			}
		}
		
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
	
	
}
