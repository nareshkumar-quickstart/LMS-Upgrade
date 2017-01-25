package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.instructor.SynchronousCourseGroupForm;

public class AddCourseGroupValidator implements Validator {

	private static final Logger log = Logger.getLogger(AddCourseGroupValidator.class.getName());
	
	public boolean supports(Class clazz) {
		return SynchronousCourseGroupForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		SynchronousCourseGroupForm form = (SynchronousCourseGroupForm)obj;
		validateFirstPage(form, errors);
	}
	
	public void validateFirstPage(SynchronousCourseGroupForm form, Errors errors) {
		if( StringUtils.isBlank(form.getNewGroupName()) ) {
			errors.rejectValue("groups", "error.instructor.manageCourseGroup.addCourse.error.groupNameRequired");
		} /*else if( form.getGroups() == null || form.getGroups().length == 0 ) {
			errors.rejectValue("groups", "error.addOrgGroup.orgGroupRequired");
		}*/
	}
	
}