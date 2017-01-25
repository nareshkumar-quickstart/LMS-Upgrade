package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.RecipientOrgGrpForm;

public class AddRecipientOrgGrpValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return RecipientOrgGrpForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		RecipientOrgGrpForm form = (RecipientOrgGrpForm) obj;
		validateOrgGroupSelected(form, errors);

	}
	
	public void validateOrgGroupSelected(RecipientOrgGrpForm form, Errors errors) {
		
		if(form.getOrgGroup() == null || form.getOrgGroup().length <= 0) {
			errors.rejectValue("selectedOrgGroupList", "error.recipient.orggroup.not.selected", "Please Select Organization Group(s)");
		}
		
	}

}
