package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.ScheduleForm;


public class AddCourseScheduleValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return ScheduleForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
	}

}
