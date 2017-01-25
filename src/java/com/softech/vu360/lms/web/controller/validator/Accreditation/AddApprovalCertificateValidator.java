package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.accreditation.CertificateForm;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddApprovalCertificateValidator implements Validator {

	public boolean supports(Class clazz) {
		return CertificateForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		CertificateForm form = (CertificateForm)obj;
		validateCertificate(form, errors);
	}

	public void validateCertificate(CertificateForm form, Errors errors) {

		if ( StringUtils.isBlank(form.getSelectedCertificateID()) ) {
			errors.rejectValue("selectedCertificateID", "error.editApproval.certificate.required");
		}
	}
}