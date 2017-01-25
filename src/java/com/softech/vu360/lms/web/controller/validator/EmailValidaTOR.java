package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.RecipientEmailForm;
import com.softech.vu360.util.FieldsValidation;

public class EmailValidaTOR implements Validator{
	
	

	public boolean supports(Class clazz) {
		return RecipientEmailForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		RecipientEmailForm form = (RecipientEmailForm)obj;
		
		validateEmailPage(form,errors);
		//validateQuestion(form,errors);
		//validateFinishPage(form,errors);
	}
	//public void validateFirstPage( RecipientEmailForm form, Errors errors ) {
		//errors.setNestedPath("alert");
		/*if( StringUtils.isBlank(form.getRecipientGroupName()) ) {
			errors.rejectValue("recipientGroupName", "error.surveyTemplateFlag.RecipientName.required");
		}*/
		//errors.setNestedPath("");
		
	/*}*/
	
	public void validateEmailPage( RecipientEmailForm form, Errors errors ) {
		
		if (form.getEmailAddress() != null && form.getEmailAddress().length >0) {
			for (String email : form.getEmailAddress()) {
				if (StringUtils.isBlank(email)) {
					errors.rejectValue("emailAddress", "error.addNewLearner.email.required","Email address required");
				} else if (!FieldsValidation.isEmailValid(email)) {
					errors.rejectValue("emailAddress", "error.addNewLearner.email.invalidformat","Invalid email address");
				}
			}
		}
	}
}
