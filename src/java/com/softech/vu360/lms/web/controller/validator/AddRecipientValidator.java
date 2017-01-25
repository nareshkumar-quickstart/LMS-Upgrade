package com.softech.vu360.lms.web.controller.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.model.RecipientForm;
import com.softech.vu360.util.FieldsValidation;

public class AddRecipientValidator implements Validator{
	
	private SurveyService surveyService = null;
	
	public boolean supports(Class clazz) {
		return RecipientForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		RecipientForm form = (RecipientForm)obj;
		validateFirstPage(form,errors);
		validateEmailPage(form,errors);
		validateLearnerSelectPage(form, errors);
		validateOrganizationGroups(form, errors);
		//validateQuestion(form,errors);
		//validateFinishPage(form,errors);
	}
	public void validateFirstPage( RecipientForm form, Errors errors ) {
		//errors.setNestedPath("alert");
		if( StringUtils.isBlank(form.getRecipientGroupName()) ) {
			errors.rejectValue("recipientGroupName", "error.surveyTemplateFlag.recipientName.required");
		}
		if(form.getAlertId() != null && form.getAlertId() != 0) {
			List<AlertRecipient> alertRecipients = surveyService.getAllAlertRecipientByAlertId(form.getAlertId());
			for(AlertRecipient alertRecipient : alertRecipients) {
				if(alertRecipient.getAlertRecipientGroupName().equals(form.getRecipientGroupName())) {
					errors.rejectValue("recipientGroupName", "error.surveyTemplateFlag.RecipientName.duplicate");
				}
			}
		}
		
		if(form.getRecipientGroupName().length() > 50) {
			errors.rejectValue("recipientGroupName", "error.surveyTemplateFlag.recipientName.invalidLimitName");
		}
		//errors.setNestedPath("");
		
	}
	
	public void validateEmailPage( RecipientForm form, Errors errors ) {
		
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
	
	public void validateLearnerSelectPage(RecipientForm form, Errors errors) {

		if(form.getSelectedLearner() == null || form.getSelectedLearner().length <= 0)
			errors.rejectValue("selectedLearner", "error.recipient.learner.not.selected", "Please select Learner(s).");
		if(form.getLearnerListFromDB() == null || form.getLearnerListFromDB().isEmpty()) {
			errors.rejectValue("selectedLearner", "error.recipient.learner.not.selected", "No Record Found.");
		}
	}
	
	public void validateOrganizationGroups(RecipientForm form, Errors errors) {
		if(form.getOrgGroup() == null || form.getOrgGroup().length <= 0) {
			errors.rejectValue("selectedOrgGroupList", "error.recipient.orggroup.not.selected", "Please select Organization Group(s)");
		}
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
}	