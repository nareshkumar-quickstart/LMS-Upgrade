package com.softech.vu360.lms.web.controller.validator.Instructor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.instructor.ResourceTypeForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Saptarshi
 */
public class ResourceTypeValidator implements Validator {

	private static final Logger log = Logger.getLogger(ResourceTypeValidator.class.getName());

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return ResourceTypeForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		ResourceTypeForm form = (ResourceTypeForm)obj;
		validateAddResouceType(form, errors);
	}
	
	public void validateAddResouceType(ResourceTypeForm form, Errors errors) {

		errors.setNestedPath("resourceType");
		
		if( StringUtils.isBlank(form.getResourceType().getName()) ) {
			errors.rejectValue("name", "error.instructor.addResourceType.name","");
		}else if(FieldsValidation.isInValidGlobalName(form.getResourceType().getName())){
			errors.rejectValue("name", "error.instructor.addResourceType.name.invalidText","");
		}
		
		errors.setNestedPath("");
	}
}
