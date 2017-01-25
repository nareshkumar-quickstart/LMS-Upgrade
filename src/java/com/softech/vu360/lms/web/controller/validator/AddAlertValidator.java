package com.softech.vu360.lms.web.controller.validator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.model.AddAlertForm;

/**
 * 
 * 
 *
 */
public class AddAlertValidator implements Validator {
	
	//private String selected;
	private SurveyService surveyService;
	public boolean supports(Class clazz) {
		return AddAlertForm.class.isAssignableFrom(clazz);
	}

	public void validate(HttpServletRequest request,Object obj, Errors errors) {
		AddAlertForm form = (AddAlertForm)obj;
		validateFirstPage(form,errors);
		//if  ( request.getParameter("default") != null )
			//selected=request.getParameter("default");
		}
		//validateSecondPage(form,errors);
		//validateQuestion(form,errors);
		//validateFinishPage(form,errors);
	
	public void validateFirstPage( AddAlertForm form, Errors errors ) {
		List<Alert> alerts = new ArrayList<Alert>();
		com.softech.vu360.lms.vo.VU360User logInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String regex =
			"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)"; 
		errors.setNestedPath("alert");
		if( StringUtils.isBlank(form.getAlert().getAlertName()) ) {
			errors.rejectValue("alertName", "error.surveyTemplateFlag.alertName.required");
		}
		//if(selected.equalsIgnoreCase("false")){
		if(form.getIsDefaultMessage()!=null && form.getIsDefaultMessage().equalsIgnoreCase("false")){
			if( !(form.getFromName()).matches(regex) )	{
				errors.rejectValue("fromName", "error.surveyTemplateFlag.from.required");
			}
			if( StringUtils.isBlank(form.getAlertSubject()) )	{
				errors.rejectValue("alertSubject", "error.surveyTemplateFlag.subject.required");
			}
		}
		if(form.getAlert().getAlertName()!=null && form.getAlert().getAlertName()!="" ) {
			alerts = surveyService.findAlert(logInUser.getId() , "");
			for(Alert alert : alerts){
				if(alert.getAlertName().equals(form.getAlert().getAlertName())){
					errors.rejectValue("alertName", "error.surveyTemplateFlag.alertNameDuplicate.required");
					break;
				}
			}
		}
		errors.setNestedPath("");
		//}
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	
}