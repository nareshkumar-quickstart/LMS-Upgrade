package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.ManageAlertForm;

public class ManageAlertValidator implements Validator{

	@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		ManageAlertForm form = (ManageAlertForm)obj;
		validateFirstPage(form,errors);
	}
	
	public void validateFirstPage( ManageAlertForm form, Errors errors ) {
		String regex =
			"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)"; 
		errors.setNestedPath("alert");
		if( StringUtils.isBlank(form.getAlert().getAlertName()) ) {
			errors.rejectValue("alertName", "error.surveyTemplateFlag.alertName.required");
		}
		//if(selected.equalsIgnoreCase("false")){
		if( !(form.getAlert().getFromName()).matches(regex) )	{
			errors.rejectValue("fromName", "error.surveyTemplateFlag.from.required");
		}
		if( StringUtils.isBlank(form.getAlert().getAlertSubject()) )	{
			errors.rejectValue("alertSubject", "error.surveyTemplateFlag.subject.required");
		}
		errors.setNestedPath("");
		//}
	}
}
