package com.softech.vu360.lms.web.controller.validator;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.AddLearnerGroupForm;

public class AssignLearnerGroupValidator implements Validator {

	private static final Logger log = Logger.getLogger(AssignLearnerGroupValidator.class.getName());

	public boolean supports(Class clazz) {
		return AddLearnerGroupForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddLearnerGroupForm form = (AddLearnerGroupForm)obj;
		validateFirstPage(form,errors);
		//validateSecondPage(form,errors);
	}

	public void validateFirstPage(AddLearnerGroupForm form, Errors errors) {
		if( form.getSelectedLearners() == null || form.getSelectedLearners().size() <= 0 ) {
			errors.rejectValue("learners", "error.addMember.LearnerRequired");
		}
	}

}