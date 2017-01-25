package com.softech.vu360.lms.web.controller.validator.Instructor;

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
import com.softech.vu360.lms.web.controller.model.instructor.LocationForm;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Saptarshi
 */
public class LocationValidator implements Validator {

	private static final Logger log = Logger.getLogger(LocationValidator.class.getName());

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return LocationForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		LocationForm form = (LocationForm)obj;
		validateLocationPage(form,errors);
	}
	public void validateLocationPage(LocationForm form, Errors errors) {
		
		//errors.setNestedPath("location");
		if( StringUtils.isBlank(form.getLocation().getName()) ) {
			errors.rejectValue("location.name", "error.addLocation.description.name.required", "");
		} else if( FieldsValidation.isInValidGlobalName(form.getLocation().getName()) ){
			errors.rejectValue("location.name", "error.addLocation.description.name.invalidText", "");
		}
		if( StringUtils.isBlank(form.getCity()) ) {
			errors.rejectValue("city", "error.addLocation.description.city.required", "");
		}else if (FieldsValidation.isInValidGlobalName(form.getCity())){
			errors.rejectValue("city", "error.addLocation.description.city.invalidText" ,"");
		}
		if( StringUtils.isBlank(form.getState()) ) {
			errors.rejectValue("state", "error.addLocation.description.state.required", "");
		}
		
		if( form.getCountry() != null && 
				!form.getCountry().equalsIgnoreCase("AE") &&
				!form.getCountry().equalsIgnoreCase("United Arab Emirates") ) {

			if( StringUtils.isBlank(form.getZipcode()) ) {
				errors.rejectValue("zipcode", "error.addLocation.description.zip.required","");
			} else {

				if( form.getBrander() != null )	{

					String country = form.getCountry();
					String zipCode = form.getZipcode();

					if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, form.getBrander(), log) ) {
						//log.debug("ZIP CODE FAILED" );
						errors.rejectValue("zipcode", ZipCodeValidator.getCountryZipCodeError(
								form.getCountry(), form.getBrander()), "");
					}				
				}
			}
		}
		
		if( StringUtils.isBlank(form.getCountry()) ) {
			errors.rejectValue("country", "error.addLocation.description.country.required", "");
		}
		if(FieldsValidation.isInValidMobPhone(form.getLocation().getPhone())){
			errors.rejectValue("location.phone", "error.addLocation.description.phone.invalidText", "");
		}
		if( StringUtils.isBlank(form.getStreetAddress()) ) {
			errors.rejectValue("country", "error.addLocation.description.streetAddress.required", "");
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
}
