package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Learner;

public class LearnerValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		// TODO Auto-generated method stub
		return Learner.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		ValidationUtils.rejectIfEmpty(e, "emailAddress", "learner.emailAddress.empty");
		Learner learner = (Learner) obj;
        if (learner.getVu360User().getPassword().length() < 8) {
            e.rejectValue("password", "learner.password.smallpassword");
        } 
    }

	

}
