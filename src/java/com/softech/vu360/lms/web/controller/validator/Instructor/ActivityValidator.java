package com.softech.vu360.lms.web.controller.validator.Instructor;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.instructor.ActivityForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * 
 * @author Saptarshi
 *
 */
public class ActivityValidator implements Validator{

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return ActivityForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		ActivityForm form = (ActivityForm)obj;
		validateCourseActivity(form, errors);
	}
	
	public void validateCourseActivity(ActivityForm form, Errors errors) {

		errors.setNestedPath("");
		
		if( StringUtils.isBlank(form.getActivityName()) ) {
			errors.rejectValue("activityName", "error.instructor.editActivity.name","");
		}else if(FieldsValidation.isInValidGlobalName(form.getActivityName())){
			errors.rejectValue("activityName", "error.instructor.editActivity.name.invalidText","");
		}
		
		errors.setNestedPath("");
	}
}
