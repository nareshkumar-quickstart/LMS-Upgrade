package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.LearnerEmailForm;
import com.softech.vu360.lms.web.controller.model.LearnerGroupMailItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.util.FieldsValidation;

public class SendMailValidator implements Validator {

	public boolean supports(Class clazz) {
		return LearnerEmailForm.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
		LearnerEmailForm emailForm = (LearnerEmailForm)obj;
		validateFirstPage(emailForm,errors);
		validateSecondPage(emailForm,errors);
		validateThirdPage(emailForm,errors);
		validateFourthPage(emailForm,errors);
		validateFifthPage(emailForm,errors);
	}
	
	public void validateFirstPage(LearnerEmailForm form, Errors errors) {
		if( form.getEmailMethod().equalsIgnoreCase("LearnerGroup") ) {
			if ( form.getLearnerGroupMailItems() == null || form.getLearnerGroupMailItems().isEmpty() ) {
				errors.rejectValue("emailMethod", "error.addEnrollment.learnerGroupNotPresent");
			}
		}
	}
	
	public void validateSecondPage(LearnerEmailForm form, Errors errors) {
		boolean anySelected = false;
		if(form.getLearners() == null || form.getLearners().size() == 0) {
			anySelected = false;
		} else {
			for(LearnerItemForm learner : form.getLearners()) {
				if(learner.isSelected() == true) {
					anySelected = true;
				}
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("learners", "error.sendMail.LearnerRequired");
		}
	}

	public void validateThirdPage(LearnerEmailForm form, Errors errors) {
		if(form.getGroups() == null) {
			errors.rejectValue("groups", "error.sendMail.orgGroupRequired");
		}
	}

	public void validateFourthPage(LearnerEmailForm form, Errors errors) {
		boolean anySelected = false;
		for(LearnerGroupMailItem lgroup : form.getLearnerGroupMailItems()){
			if(lgroup.isSelected() == true) {
				anySelected = true;
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("selectedLearnerGroups", "error.sendMail.learnerGroupRequired");
		}
	}
	
	public void validateFifthPage(LearnerEmailForm form, Errors errors) {
		if( form.getFromEmail() == null || form.getFromEmail().isEmpty() ) {
			errors.rejectValue("fromEmail", "error.sendMail.FromMessageBlank");
		}
		if(!FieldsValidation.isEmailWithNameValid(form.getFromEmail())) {
			errors.rejectValue("fromEmail", "error.sendMail.FromMessageValidEmail");
		}
		if( form.getMailSubject() == null || form.getMailSubject().isEmpty() ) {
			errors.rejectValue("message", "error.sendMail.SubjectMessageBlank");
		}
	}
	
}