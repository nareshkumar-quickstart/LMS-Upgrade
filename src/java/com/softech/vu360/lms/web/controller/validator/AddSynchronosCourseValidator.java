/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.CourseForm;

/**
 * @author Noman
 *
 */
public class AddSynchronosCourseValidator implements Validator {
	
	private static final Logger log = Logger.getLogger(AddSynchronosCourseValidator.class.getName());

	/**
	 * 
	 */
	public AddSynchronosCourseValidator() {
		// TODO Auto-generated constructor stub
	}


	public boolean supports(Class clazz) {
		return CourseForm.class.isAssignableFrom(clazz);
	}


	public void validate(Object obj, Errors errors) {
		CourseForm courseForm = (CourseForm)obj;
		// TODO call the individual page validation routines
		validatePage1(courseForm, errors);
	}


	public void validatePage1(CourseForm courseForm, Errors errors){
		log.debug("Test");
		//ValidationUtils.rejectIfEmpty(errors, "password", "PASSWORD", "Password is required.");
		if (StringUtils.isBlank(courseForm.getCourseName())) {
			errors.rejectValue("courseName", "error.addNewSynchrounousCourse.courseName.required");
		}

		if (StringUtils.isBlank(courseForm.getCourseID())){
			errors.rejectValue("courseID", "error.addNewSynchrounousCourse.courseID.required");
		}
		
		if (StringUtils.isBlank(courseForm.getDescription())){
			errors.rejectValue("description", "error.addNewSynchrounousCourse.description.required");
		}
		
	}

}
