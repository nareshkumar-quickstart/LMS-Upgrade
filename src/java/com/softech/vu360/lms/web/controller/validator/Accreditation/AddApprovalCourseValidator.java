package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalCourseForm;


/**
 * 
 * @author Saptarshi
 *
 */
public class AddApprovalCourseValidator implements Validator {

	
	public boolean supports(Class clazz) {
		return ApprovalCourseForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		ApprovalCourseForm form = (ApprovalCourseForm)obj;
		validateCourse(form, errors);
	}

	public void validateCourse(ApprovalCourseForm form, Errors errors) {

		if ( StringUtils.isBlank(form.getSelectedCourseId()) ) {
			errors.rejectValue("course", "error.editApproval.course.required");
		}
	}

}