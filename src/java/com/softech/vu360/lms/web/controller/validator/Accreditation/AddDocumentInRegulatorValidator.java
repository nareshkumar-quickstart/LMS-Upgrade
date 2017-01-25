package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.web.controller.model.DocumentUploadForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author tapas
 */
public class AddDocumentInRegulatorValidator implements Validator{
	
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return DocumentUploadForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		DocumentUploadForm form = (DocumentUploadForm)obj;
		validateFirstPage(form, errors);
	}
	
	public void validateFirstPage(DocumentUploadForm form, Errors errors) {

		Document document = form.getDocument();
		errors.setNestedPath("document");
		if( StringUtils.isBlank(document.getName()) ) {
			errors.rejectValue("name", "error.AddDocuments.name");
		} else if ( FieldsValidation.isInValidGlobalName(document.getName()) ) {
			errors.rejectValue("name", "error.AddDocuments.name.invalidText");
		}
		if( StringUtils.isNotBlank(document.getDescription()) ) {
			if( FieldsValidation.isInValidGlobalName(document.getDescription()) ) {
				errors.rejectValue("description", "error.AddDocuments.description.invalidText");
			}
		}
		errors.setNestedPath("");
		if( form.getFile() == null  ) {
			errors.rejectValue("file", "error.AddDocuments.fileName");
		} else if ( form.getFile().getSize() == 0 ) {
			errors.rejectValue("file", "error.AddDocuments.fileName");
		}
	}
}