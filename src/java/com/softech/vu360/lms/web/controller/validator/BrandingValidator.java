package com.softech.vu360.lms.web.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.BrandingForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * 
 * 
 *
 */
public class BrandingValidator implements Validator {
	
	
	@Override
	public void validate(Object brandingForm, Errors errors) {
		BrandingForm form = (BrandingForm) brandingForm;
		int MAX_FILE_SIZE=1024*50;//50kb
		boolean validateLogo= false; 
		
		
		if(form.getCoursePlayerLogo()!=null && form.getCoursePlayerLogo().getSize()==0){
			validateLogo=true;
		}
		
		if (validateLogo && form.getLmsLogo()!=null && form.getLmsLogo().getSize()>0 &&(form.getLmsLogo().getContentType().toLowerCase().indexOf("image")<0 || form.getLmsLogo().getSize()>MAX_FILE_SIZE)) {
			errors.rejectValue("lmsLogo", "error.editRegulatorCategory.displayName", "");
			form.setLmsLogo(null);
		}
		validateLogo=false;
		if(form.getLmsLogo()!=null && form.getLmsLogo().getSize()==0){
			validateLogo=true;
		}
		if (validateLogo && form.getCoursePlayerLogo()!=null && form.getCoursePlayerLogo().getSize()>0 && (form.getCoursePlayerLogo().getContentType().toLowerCase().indexOf("image")<0 || form.getCoursePlayerLogo().getSize()>MAX_FILE_SIZE)){
			errors.rejectValue("coursePlayerLogo", "error.editRegulatorCategory.displayName", "");
			form.setCoursePlayerLogo(null);
		} 
		if (form.getEmailAddresses()!=null && !FieldsValidation.isEmailValid(form.getEmailAddresses())) {	            
			  errors.rejectValue("emailAddresses", "error.editRegulatorCategory.displayName", "");
	        }
	        if (form.getEmailTemplateText()!=null ) {
	        	try{
	        		String str= form.getEmailTemplateText();
	        	}
	        	catch(Exception e){
	        		errors.rejectValue("emailTemplateText", "error.editRegulatorCategory.displayName", "");
	        	}				  
		   }    

		
	}
		@Override
		public boolean supports(Class arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	

	
}