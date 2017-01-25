package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.CommissionablePartyForm;

public class CommissionablePartyValidator implements Validator {

	@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		CommissionablePartyForm form = (CommissionablePartyForm)obj;
		validateFirstPage(form,errors);
	}
    
	public void validateFirstPage( CommissionablePartyForm form, Errors errors ) {		
		
		if(form.getName().length()>=255)  {
			errors.rejectValue("name", "error.resellerCommission.commissionablePartyName.limit");
		}
		if( StringUtils.isBlank(form.getName()) ) {
			errors.rejectValue("name", "error.resellerCommission.commissionablePartyName.required");
		}		
	}

}
