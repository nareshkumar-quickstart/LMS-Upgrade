/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.AddPurchasedCertificateForm;


/**
 * @author syed.mahmood
 *
 */
public class AddPurchasedCertificateValidator implements Validator{

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return AddPurchasedCertificateForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddPurchasedCertificateForm form = (AddPurchasedCertificateForm)obj;
		validateFirstPage(form, errors);
	}
	
	public void validateFirstPage(AddPurchasedCertificateForm form, Errors errors) {
		errors.setNestedPath("");
		if( form.getFile() == null  ) {
			errors.rejectValue("file", "error.AddDocuments.fileName");
		} else if ( form.getFile().getSize() == 0 ) {
			errors.rejectValue("file", "error.AddDocuments.fileName");
		} else if(form.getFile()!=null){
			 String fileName = form.getFile().getOriginalFilename().trim().replaceAll(" ", "");
			 String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(csv))$)";
			 Pattern pattern;
		     Matcher matcher;
		     pattern = Pattern.compile(IMAGE_PATTERN);
			 matcher = pattern.matcher(fileName);
			 boolean isCSVFile =  matcher.matches();
			 
			 if(!isCSVFile)
				 errors.rejectValue("file", "error.AddDocuments.fileType");
		}
	}	
	

}
