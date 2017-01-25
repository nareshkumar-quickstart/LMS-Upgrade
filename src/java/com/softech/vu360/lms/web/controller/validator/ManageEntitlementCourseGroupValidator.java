package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.administrator.ManageEntitlementCoursesController;

/**
 * Validator class for Manage Entitlement Course Groups Controller
 * @author muzammil.shaikh
 */

public class ManageEntitlementCourseGroupValidator implements Validator {
	
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return ManageEntitlementCoursesController.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
	}
}