package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorCredential;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorForm;

/**
 * @author Dyutiman
 * created on 25-june-2009
 *
 */
public class AddCredentialInRegulatorValidator implements Validator {

	public boolean supports(Class clazz) {
		return RegulatorForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		RegulatorForm form = (RegulatorForm)obj;
		validateFirstPage(form, errors);
	}

	public void validateFirstPage(RegulatorForm form, Errors errors) {
		
		boolean selected = false;
		List<RegulatorCredential> regCredentials = form.getRegCredential();
		for( RegulatorCredential regCred : regCredentials ) {
			if( regCred.isSelected() ) {
				selected = true;
				break;
			}
		}
		if( !selected ) {
			errors.rejectValue("regCredential", "error.addCredentialInRegulator.notselected");
		}
	}
}