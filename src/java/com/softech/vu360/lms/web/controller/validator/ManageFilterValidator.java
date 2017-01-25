package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.ManageFilterForm;

public class ManageFilterValidator implements Validator{

	@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		ManageFilterForm form = (ManageFilterForm)obj;
		validateFirstPage(form,errors);
	}
    public void validateFirstPage( ManageFilterForm form, Errors errors ) {
		if( StringUtils.isBlank(form.getFilter().getFilterName()) ) {
			errors.rejectValue("filterName", "error.surveyTemplateFlag.filterName.required");
		}
		
	}

}
