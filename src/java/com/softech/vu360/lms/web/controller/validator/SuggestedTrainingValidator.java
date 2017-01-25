package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.SuggestedTrainingForm;

public class SuggestedTrainingValidator implements Validator{

	@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		SuggestedTrainingForm form = (SuggestedTrainingForm)arg0;
		ValidateAssignMultiple(form,arg1);
	}
	public void ValidateAssignMultiple( SuggestedTrainingForm form, Errors errors ) {
		errors.rejectValue("suggTraining", "error.surveyTemplateFlag.assignMultiple.course.required");
	}
}
