package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.web.controller.model.accreditation.AssetForm;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddApprovalAssetValidator implements Validator {

	public boolean supports(Class clazz) {
		return AssetForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AssetForm form = (AssetForm)obj;
		
		if(form.getAsset() instanceof Affidavit){
			validateAffidavit(form, errors);
		}
	}

	public void validateAffidavit(AssetForm form, Errors errors) {

//		if ( StringUtils.isBlank(form.getSelectedCertificateID()) ) {
//			errors.rejectValue("selectedCertificateID", "error.editApproval.certificate.required");
//		}
	}
}