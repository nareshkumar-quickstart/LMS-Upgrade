package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.ImportAffidavitTemplateIdsForm;

/**
 * 
 * @author haider.ali
 *
 */
public class ImportAffidavitTemplateValidator  implements Validator{

	public void validate(Object obj, Errors errors) {
		ImportAffidavitTemplateIdsForm form = (ImportAffidavitTemplateIdsForm)obj;
		validateFirstPage(form, errors);
	}
	
	public void validateFirstPage(ImportAffidavitTemplateIdsForm form, Errors errors) {
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

	@Override
	public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
		return ImportAffidavitTemplateIdsForm.class.isAssignableFrom(clazz);
	}	
	

}
