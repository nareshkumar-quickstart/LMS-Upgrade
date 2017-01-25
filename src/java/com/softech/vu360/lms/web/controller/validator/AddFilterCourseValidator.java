package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.FilterCourseForm;

public class AddFilterCourseValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return FilterCourseForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		FilterCourseForm form = (FilterCourseForm) obj;
		validateAddFilterCourse(form, errors);
	}
	
	public void validateAddFilterCourse(FilterCourseForm form, Errors errors) {
		if(form.getCourses() == null || form.getCourses().length < 0) {
			errors.rejectValue("courses", "error.surveyTemplateFlag.filterName.courseNotSelected");
		}
	}

}
