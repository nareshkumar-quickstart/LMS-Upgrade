package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.web.controller.model.accreditation.AssetForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Dyutiman
 */
public class AssetValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return AssetForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		AssetForm form = (AssetForm)obj;
		if(form.getAsset() instanceof Document){
			validateDocument(form, errors);
		}
	}
	
	private void validateDocument(AssetForm form, Errors errors){
		
		Document document = (Document)form.getAsset();
		String errorPrefix = "error." + document.getAssetType().toLowerCase();
		String affidavitType = "";
		
		if(form.getAsset() instanceof Affidavit)
		    affidavitType = ((Affidavit)form.getAsset()).getAffidavitType();
		
		errors.setNestedPath("asset");

		if( StringUtils.isBlank(document.getName()) ) {
			errors.rejectValue("name", errorPrefix + ".name");
		} else if ( FieldsValidation.isInValidGlobalName(document.getName()) ) {
			errors.rejectValue("name", errorPrefix + ".name.invalidText");
		}
		if( StringUtils.isNotBlank(document.getName()) && document.getName().trim().length()>50 ) {
			errors.rejectValue("name", errorPrefix + ".namelength");
		}
		
		// verify neither File or Template ID is selected
		if(form.getAsset() instanceof Affidavit && !affidavitType.equals(Affidavit.AFFIDAVIT_TYPE_FILE) && !affidavitType.equals(Affidavit.AFFIDAVIT_TYPE_TEMPLATE))
		{
			errors.rejectValue("fileName", errorPrefix + ".affidavit.type");
		}
		
		// verify a template is select from the drop down, if the Asset is a Affidavit and Template ID radio is selected
		if (form.getAsset() instanceof Affidavit && affidavitType.equals(Affidavit.AFFIDAVIT_TYPE_TEMPLATE) &&
				(((Affidavit) form.getAsset()).getTemplateId() == null || ((Affidavit) form.getAsset()).getTemplateId() == 0))
		{
			errors.rejectValue("templateId", errorPrefix + ".template");
		}
		
		// verify the file is valid, if Asset is a Certificate or Asset is a Affidavit and File radio is selected 
		if ((form.getAsset() instanceof Certificate) 
				|| (form.getAsset() instanceof Affidavit && affidavitType.equals(Affidavit.AFFIDAVIT_TYPE_FILE)))
		{
			if ((form.getAsset().getId() == null || form.getAsset().getId()
					.longValue() <= 0)) {

				if (form.getFile() == null) {
					errors.rejectValue("fileName", errorPrefix + ".fileName");
				} else if (form.getFile().getSize() == 0) {
					errors.rejectValue("fileName", errorPrefix + ".fileName");
				} else if (!form.getFile().getOriginalFilename()
						.endsWith("pdf")) {
					errors.rejectValue("fileName", errorPrefix + ".invalidFile");
				}

			} else {
				if (form.getFile().getSize() > 0
						&& !form.getFile().getOriginalFilename()
								.endsWith("pdf")) {
					errors.rejectValue("fileName", errorPrefix + ".invalidFile");
				}
			}
		}

		if (form.getAsset() instanceof Certificate) {

		} else if (form.getAsset() instanceof Affidavit) {

			Affidavit affidavit = (Affidavit) form.getAsset();

			if (StringUtils.isBlank(affidavit.getContent())) {
				errors.rejectValue("content", errorPrefix + ".content");
			}
			if (StringUtils.isBlank(affidavit.getContent2())) {
				errors.rejectValue("content2", errorPrefix + ".content2");
			}
			if (StringUtils.isBlank(affidavit.getContent3())) {
				errors.rejectValue("content3", errorPrefix + ".content3");
			}
		}

		errors.setNestedPath("");

		/**
		 * Modified By Marium Saud 
		 * LMS-19604 : Validation has been added for Number Of Document Per Page as in TSM the int data type of this attribute has been changed to Integer.
		 * Integer can hold Null value and due to the reason the exception doesn't raise for this Field
		 * So, explicitly handled exception for this field when left blank while 'Updating' Certificate. 
		 
		 */
		if ((form.getAsset().getId() == null || form.getAsset().getId().longValue() <= 0)) {
			if(StringUtils.isBlank(form.getNoOfDocumentsPerPage())){
				errors.rejectValue("noOfDocumentsPerPage", errorPrefix + ".documentsPerPage");
			}else if(!StringUtils.isNumeric(form.getNoOfDocumentsPerPage())){
				errors.rejectValue("noOfDocumentsPerPage", errorPrefix + ".documentsPerPage.invalid");
			}
		}
		else{
			if(document.getNoOfDocumentsPerPage()==null){
				errors.rejectValue("noOfDocumentsPerPage", errorPrefix + ".documentsPerPage");
			}
		}
			}	

}