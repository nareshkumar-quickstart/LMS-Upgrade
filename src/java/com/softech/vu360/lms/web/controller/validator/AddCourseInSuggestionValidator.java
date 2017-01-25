package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.AddCourseInSuggestedTrainingForm;
import com.softech.vu360.lms.web.controller.model.CourseItem;

/**
 * @author Dyutiman
 * created on 29th May 2010.
 *
 */
public class AddCourseInSuggestionValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return AddCourseInSuggestedTrainingForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object command, Errors errors) {
		AddCourseInSuggestedTrainingForm form = (AddCourseInSuggestedTrainingForm)command;
		validateFirstPage(form, errors);
	}

	public void validateFirstPage( AddCourseInSuggestedTrainingForm form, Errors errors ) {
		boolean selected = false;
		for( CourseItem c : form.getCourses() ) {
			if( c.getSelected() ) {
				selected = true;
				break;
			}
		}
		if(!selected) {
			errors.rejectValue("courses", "error.editApproval.course.required");
		}
	}

}