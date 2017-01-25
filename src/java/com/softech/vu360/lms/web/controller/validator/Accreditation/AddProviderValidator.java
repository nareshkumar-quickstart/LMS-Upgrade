package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.web.controller.model.accreditation.ProviderForm;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author PG
 * @created on 30-june-2009
 *
 */
public class AddProviderValidator implements Validator {

	private static final Logger log = Logger.getLogger(AddProviderValidator.class.getName());

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return ProviderForm.class.isAssignableFrom(clazz);
	}

	public void validate( Object obj, Errors errors ) {
		ProviderForm form = (ProviderForm)obj;
		validateFirstPage(form,errors);
		validateApprovalProvider(form,errors);
	}

	public void validateFirstPage(ProviderForm form, Errors errors) {

		errors.setNestedPath("provider");

		if( StringUtils.isBlank(form.getProvider().getName()) ) {
			errors.rejectValue("name", "error.addProvider.name","");
		}else if(FieldsValidation.isInValidGlobalName(form.getProvider().getName())){
			errors.rejectValue("name", "error.addProvider.name.invalidText","");
		}

		if( StringUtils.isBlank(form.getProvider().getContactName()) ) {
			errors.rejectValue("contactName", "error.addProvider.contactName","");
		}else if(FieldsValidation.isInValidGlobalName(form.getProvider().getContactName())){
			errors.rejectValue("contactName", "error.addProvider.contactName.invalidText","");
		}

		if( StringUtils.isBlank(form.getProvider().getPhone()) ) {
			errors.rejectValue("phone", "error.addProvider.phone","");
		}else if (FieldsValidation.isInValidRegulatorContactPhone((form.getProvider().getPhone()))){
			errors.rejectValue("phone", "error.addProvider.phone.invalidText","");
		}

		if( StringUtils.isBlank(form.getProvider().getFax()) ) {
			errors.rejectValue("fax", "error.addProvider.fax","");
		}else if (FieldsValidation.isInValidRegulatorContactPhone((form.getProvider().getFax()))){
			errors.rejectValue("fax", "error.addProvider.fax.invalidText","");
		}

		if( StringUtils.isBlank(form.getProvider().getWebsite()) ) {
			errors.rejectValue("website", "error.addProvider.website","");
		}

		if (StringUtils.isBlank(form.getProvider().getEmailAddress())) {
			errors.rejectValue("emailAddress", "error.addProvider.email.required","");
		} else if(!FieldsValidation.isEmailValid(form.getProvider().getEmailAddress())) {
			errors.rejectValue("emailAddress", "error.addProvider.email.invalidformat","");
		}

		errors.setNestedPath("provider.address");

		if( StringUtils.isBlank(form.getProvider().getAddress().getStreetAddress()) ) {
			errors.rejectValue("streetAddress", "error.addProvider.address","");
		}else if (FieldsValidation.isInValidAddress((form.getProvider().getAddress().getStreetAddress()))){
			errors.rejectValue("streetAddress", "error.addProvider.address.invalidText","");
		}

		if( StringUtils.isBlank(form.getProvider().getAddress().getCity()) ) {
			errors.rejectValue("city", "error.addProvider.city","");
		}else if (FieldsValidation.isInValidAddress((form.getProvider().getAddress().getStreetAddress()))){
			errors.rejectValue("city", "error.addProvider.city.invalidText","");
		}else if (FieldsValidation.isInValidGlobalName(form.getProvider().getAddress().getCity())){
			errors.rejectValue("city", "error.addProvider.city.invalidText","");
		}

		if( StringUtils.isBlank(form.getProvider().getAddress().getState()) ) {
			errors.rejectValue("state", "error.addProviderr.state","");
		}else if (FieldsValidation.isInValidAddress((form.getProvider().getAddress().getStreetAddress()))){
			errors.rejectValue("state", "error.addProviderr.state.invalidText","");
		}
		/*
		 *  validation of zipcode will not be done if country is UAE 
		 */
		if( StringUtils.isNotBlank(form.getProvider().getAddress().getCountry()) && 
				!form.getProvider().getAddress().getCountry().equalsIgnoreCase("AE") &&
				!form.getProvider().getAddress().getCountry().equalsIgnoreCase("United Arab Emirates") ) {

			if( StringUtils.isBlank(form.getProvider().getAddress().getZipcode()) ) {
				errors.rejectValue("zipcode", "error.addProvider.zip","");
			} else {
				if( form.getBrander() != null )	{

					String country = form.getProvider().getAddress().getCountry();
					String zipCode = form.getProvider().getAddress().getZipcode();

					if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, form.getBrander(), log) ) {
						//log.debug("ZIP CODE FAILED" );
						errors.rejectValue("zipcode", ZipCodeValidator.getCountryZipCodeError(
								form.getProvider().getAddress().getCountry(), form.getBrander()), "");
					}				
				}
			}
		}
		errors.setNestedPath("");
	}

	public void validateCustomFields( List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields, Errors errors ) {
		int fieldindex = 0;
		if ( customFields.size() > 0 ) {

			for (com.softech.vu360.lms.web.controller.model.customfield.CustomField tempCustomField : customFields){

				CustomField customField = tempCustomField.getCustomFieldRef();
				CustomFieldValue customFieldValue = tempCustomField.getCustomFieldValueRef();

				if (customField.getFieldRequired()){
					if(customField instanceof MultiSelectCustomField){

						if (((MultiSelectCustomField) customField).getCheckBox()){
							int count=0;
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : tempCustomField.getCustomFieldValueChoices()){
								if (customFieldValueChoice.isSelected()){count=count+1;}
							}
							if(count==0){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						} else {
							if(tempCustomField.getSelectedChoices() == null){
								errors.rejectValue("customFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else if (tempCustomField.getSelectedChoices().length==0){
								errors.rejectValue("customFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}
					} else {
						/*Object[] errorArgs = new Object[1];
						errorArgs[0] = customField.getFieldLabel();
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customFields["+fieldindex+"].customFieldValueRef.value" , "custom.field.required", errorArgs,customField.getFieldLabel()+" is required");*/
						if (customFieldValue.getValue()==null){
							errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
							tempCustomField.setStatus(2);
						}else if(StringUtils.isBlank(customFieldValue.getValue().toString())){
							errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
							tempCustomField.setStatus(2);
						}else{
							tempCustomField.setStatus(1);
						}
					}
				}
				if(customField instanceof NumericCusomField){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isNumeric(customFieldValue.getValue().toString())){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid number.");
								tempCustomField.setStatus(2);
							}else {
								tempCustomField.setStatus(1);
							}
						}
					}
				}else if(customField instanceof DateTimeCustomField){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isValidDate(customFieldValue.getValue().toString(),true,"MM/dd/yyyy")){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid date.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}
					}
				}else if (customField instanceof SSNCustomFiled){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isSSNValid(customFieldValue.getValue().toString())){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid SSN Number.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}
					}
				}
				fieldindex = fieldindex+1;
			}
		}
	}

	public void validateApprovalProvider(ProviderForm form, Errors errors) {
		if (StringUtils.isBlank(form.getProviderId())) {
			errors.rejectValue("providerId", "error.editApproval.provider.required");
		}
	}

}