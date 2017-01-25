package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.ManageRecipientForm;

public class ManageRecipientValidator implements Validator{

	@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		ManageRecipientForm form = (ManageRecipientForm)obj;
		validateFirstPage(form,errors);
	}
    public void validateFirstPage( ManageRecipientForm form, Errors errors ) {
		if( StringUtils.isBlank(form.getRecipient().getAlertRecipientGroupName()) ) {
			errors.rejectValue("alertRecipientGroupName", "error.surveyTemplateFlag.recipientName.required");
		}
		
		if(form.getRecipient().getAlertRecipientGroupName().length() > 50) {
			errors.rejectValue("alertRecipientGroupName", "error.surveyTemplateFlag.recipientName.invalidLimitName");
		}
		
	}

}
