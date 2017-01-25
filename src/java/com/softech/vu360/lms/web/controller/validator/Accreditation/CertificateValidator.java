package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.web.controller.model.accreditation.DocumentForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Dyutiman
 */
public class CertificateValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return DocumentForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		DocumentForm form = (DocumentForm)obj;
		validateAddPage(form, errors);
		validateEditPage(form, errors);
	}

	public void validateEditPage(DocumentForm form, Errors errors) {
		Document document = form.getDocument();
		int certs = ((Certificate)document).getNoOfCertificatePerPage();
		errors.setNestedPath("document");
		if( StringUtils.isBlank(document.getName()) ) {
			errors.rejectValue("name", "error.AddCertificate.name");
		} else if ( FieldsValidation.isInValidGlobalName(document.getName()) ) {
			errors.rejectValue("name", "error.AddCertificate.name.invalidText");
		}
		if( StringUtils.isNotBlank(document.getName()) && document.getName().trim().length()>50 ) {
			errors.rejectValue("name", "error.AddCertificate.namelength");
		}
		if( (Integer)certs == null || certs == 0 ) {
			errors.rejectValue("noOfCertificatePerPage", "error.AddCertificate.certificatePerPage");
		}
		if( form.getFile() != null && form.getFile().getSize() > 0 ) {
			if ( !form.getFile().getOriginalFilename().endsWith("pdf") ) {
				errors.rejectValue("fileName", "error.AddCertificate.invalidFile");
			}
		}
		errors.setNestedPath("");
	}

	public void validateAddPage(DocumentForm form, Errors errors) {
		Document document = form.getDocument();
		int certs = ((Certificate)document).getNoOfCertificatePerPage();
		errors.setNestedPath("document");
		if( StringUtils.isBlank(document.getName()) ) {
			errors.rejectValue("name", "error.AddCertificate.name");
		} else if ( FieldsValidation.isInValidGlobalName(document.getName()) ) {
			errors.rejectValue("name", "error.AddCertificate.name.invalidText");
		}
		if( StringUtils.isNotBlank(document.getName()) && document.getName().trim().length()>50 ) {
			errors.rejectValue("name", "error.AddCertificate.namelength");
		}
		if( (Integer)certs == null || certs == 0 ) {
			errors.rejectValue("noOfCertificatePerPage", "error.AddCertificate.certificatePerPage");
		}
		if( form.getFile() == null  ) {
			errors.rejectValue("fileName", "error.AddCertificate.fileName");
		} else if ( form.getFile().getSize() == 0 ) {
			errors.rejectValue("fileName", "error.AddCertificate.fileName");
		} else if ( !form.getFile().getOriginalFilename().endsWith("pdf") ) {
			errors.rejectValue("fileName", "error.AddCertificate.invalidFile");
		}
		errors.setNestedPath("");
	}
}