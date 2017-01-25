package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.web.controller.model.SelfRegistrationInvitationForm;


public class SelfRegistrationInvitationValidator implements Validator {

	
	private LearnerService learnerService = null;
	
	public boolean supports(Class clazz) {
		return SelfRegistrationInvitationForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		SelfRegistrationInvitationForm registrationForm = (SelfRegistrationInvitationForm)obj;
		validateInvitation(registrationForm,errors);
		validateInvitationMessage(registrationForm,errors);
	}

	public void validateInvitation(SelfRegistrationInvitationForm form, Errors errors) {
		if (StringUtils.isBlank(form.getInvitationName()))		{
			errors.rejectValue("invitationName", "error.invitationName.required");
		}
		//this.validateInvitationName(form, errors);
		if(StringUtils.isBlank(form.getPassCode())){
			errors.rejectValue("passCode", "error.passCode.required");
		}
		if(!form.isRegistrationUnlimited()){
			if(StringUtils.isBlank(form.getMaximumLimitedRegistration())){
				errors.rejectValue("maximumLimitedRegistration", "error.maximumreglimit.required");
			}else if(!StringUtils.isNumeric(form.getMaximumLimitedRegistration())){
				errors.rejectValue("maximumLimitedRegistration", "error.maximumreglimit.numeric");
			}
		}
		boolean orgGrpSelected = false;
		/*for(TreeNode node : form.getTreeAsList()){
			if(node.isSelected()){
				orgGrpSelected = true;
			}
		}*/
	
		if(form.getGroups() == null) {
			errors.rejectValue("groups", "error.regInv.org.nonSelected");
		}
		
	}
	
	/*public void validateInvitationName(SelfRegistrationInvitationForm form, Errors errors) {
		RegistrationInvitation regInvitation = learnerService.get(form.getSurveyName());
		if(form.getId() == 0) {
			if(regInvitation != null)
				errors.rejectValue("invitationName", "error.surveyName.exists");
		} else {
			if(regInvitation != null && form.getId() != regInvitation.getId())
				errors.rejectValue("invitationName", "error.surveyName.Change");
		}
	}*/
	
	public void validateInvitationMessage(SelfRegistrationInvitationForm form, Errors errors) {
		if (StringUtils.isBlank(form.getMessage()))		{
			errors.rejectValue("message", "error.invitationMessage.required");
		}
		
	}

	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}

	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	
}
