package com.softech.vu360.lms.web.controller.validator;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.AddSecurityRoleForm;

public class AddSecurityRoleValidator implements Validator {

	private static final Logger log = Logger.getLogger(AddSecurityRoleValidator.class.getName());
	
	public boolean supports(Class clazz) {
		return AddSecurityRoleForm.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
		AddSecurityRoleForm form = (AddSecurityRoleForm)obj;
		validateFirstPage(form,errors);
		validateSecondPage(form,errors);
	}

	public void validateFirstPage(AddSecurityRoleForm form, Errors errors) {
		if( form.getRoleId() == null ) {
			errors.rejectValue("roleId", "error.editSecurityRole.roleRequired");
		}
	}

	public void validateSecondPage(AddSecurityRoleForm form, Errors errors) {
		if( form.getSelectedLearners() == null || form.getSelectedLearners().size() <= 0 ) {
			errors.rejectValue("learners", "error.addMember.LearnerRequired");
		}
	}	
}