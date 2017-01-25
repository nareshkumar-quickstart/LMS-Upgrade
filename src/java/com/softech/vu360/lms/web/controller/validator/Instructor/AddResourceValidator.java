package com.softech.vu360.lms.web.controller.validator.Instructor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.instructor.AddCustomFieldWizardController;
import com.softech.vu360.lms.web.controller.model.instructor.AddResourceForm;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Dyutiman
 * created on 23 Mar 2010
 *
 */
public class AddResourceValidator implements Validator {
	
	private AccreditationService accreditationService = null;

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return AddResourceForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddResourceForm form = (AddResourceForm)obj;
		validateFirstPage(form, errors);
		validateEditPage(form, errors);
		validateAddCustomFieldPage(form, errors);
	}

	public void validateFirstPage(AddResourceForm form, Errors errors) {
		
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getName()) ) {
			errors.rejectValue("name", "error.addResources.description.name.required", "");
		} else if( FieldsValidation.isInValidGlobalName(form.getName()) ){
			errors.rejectValue("name", "error.addResources.description.name.invalidText", "");
		}
		if( form.getResourceTypeId().longValue() == 0L ) {
			errors.rejectValue("resourceTypeId", "error.addResources.description.resourceType.required", "");
		}
		if( StringUtils.isBlank(form.getAssetTagNumber()) ) {
			errors.rejectValue("assetTagNumber", "error.addResources.description.assetTagNumber.required", "");
		}
	}
	
	public void validateEditPage(AddResourceForm form, Errors errors) {
		
		errors.setNestedPath("resource");
		if( StringUtils.isBlank(form.getResource().getName()) ) {
			errors.rejectValue("name", "error.addResources.description.name.required", "");
		} else if( FieldsValidation.isInValidGlobalName(form.getResource().getName()) ){
			errors.rejectValue("name", "error.addResources.description.name.invalidText", "");
		}
		if( StringUtils.isBlank(form.getResource().getAssetTagNumber()) ) {
			errors.rejectValue("assetTagNumber", "error.addResources.description.assetTagNumber.required", "");
		}
		errors.setNestedPath("");
		if( form.getResourceTypeId().longValue() == 0L ) {
			errors.rejectValue("resourceTypeId", "error.addResources.description.resourceType.required", "");
		}
	}
	
	public void validateAddCustomFieldPage(AddResourceForm form, Errors errors) {
		
		errors.setNestedPath("customField");
		if( StringUtils.isBlank(form.getCustomField().getFieldLabel()) ) {
			errors.rejectValue("fieldLabel", "error.resourceCustomField.fieldLabel.required");
		} else if( FieldsValidation.isInValidGlobalName(form.getCustomField().getFieldLabel())) {
			// invalid text
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.invalidText");
		}
		if(! accreditationService.isUniqueCustomFieldName(form.getEntity(),form.getCustomField().getFieldLabel()) ) {
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.duplicate");
		}
		errors.setNestedPath("");
		String fieldType = form.getFieldType();
		if(fieldType.equalsIgnoreCase(AddCustomFieldWizardController.CUSTOMFIELD_RADIO_BUTTON)){
			/*if (StringUtils.isBlank(form.getOption()))		{
				errors.rejectValue("option", "error.option.required");
			}*/
			this.validateOptions(form.getOptionList(), errors);
		}else if(fieldType.equalsIgnoreCase(AddCustomFieldWizardController.CUSTOMFIELD_CHECK_BOX)){
			this.validateOptions(form.getOptionList(), errors);
		} else if(fieldType.equalsIgnoreCase(AddCustomFieldWizardController.CUSTOMFIELD_CHOOSE)){
			this.validateOptions(form.getOptionList(), errors);
		}
	}
	
	private void validateOptions(List<String> options, Errors errors) {
		if( options == null || options.size() <= 0 ) {
			errors.rejectValue("option", "error.customField.option.required");
		}else if( options.size() == 1 ) {
			errors.rejectValue("option", "error.customField.optionList.multiple");
		}
	}
	
	public void validateCustomFields( List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields, Errors errors ) {
		
		int fieldindex = 0;
		if ( customFields.size() > 0 ) {

			for( com.softech.vu360.lms.web.controller.model.customfield.CustomField tempCustomField : customFields ) {

				CustomField customField = tempCustomField.getCustomFieldRef();
				CustomFieldValue customFieldValue = tempCustomField.getCustomFieldValueRef();

				if( customField.getFieldRequired() ) {
					if( customField instanceof MultiSelectCustomField ) {

						if( ((MultiSelectCustomField) customField).getCheckBox() ) {
							int count = 0;
							for( com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice  customFieldValueChoice : tempCustomField.getCustomFieldValueChoices()){
								if (customFieldValueChoice.isSelected()){count=count+1;}
							}
							if(count==0){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", customField.getFieldLabel()+"' field required");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						} else {
							if(tempCustomField.getSelectedChoices() == null){
								errors.rejectValue("customFields["+fieldindex+"].selectedChoices", "custom.field.required", customField.getFieldLabel()+"' field required");
								tempCustomField.setStatus(2);
							}else if (tempCustomField.getSelectedChoices().length==0){
								errors.rejectValue("customFields["+fieldindex+"].selectedChoices", "custom.field.required", customField.getFieldLabel()+"' field required");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}
					} else {
						/*Object[] errorArgs = new Object[1];
						errorArgs[0] = customField.getFieldLabel();
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customFields["+fieldindex+"].customFieldValueRef.value" , "custom.field.required", errorArgs,customField.getFieldLabel()+" is required");*/
						if( customFieldValue.getValue() == null ) {
							errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", customField.getFieldLabel()+"' field required");
							tempCustomField.setStatus(2);
						}else if(StringUtils.isBlank(customFieldValue.getValue().toString())){
							errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", customField.getFieldLabel()+"' field required");
							tempCustomField.setStatus(2);
						}else{
							tempCustomField.setStatus(1);
						}
					}
				}
				if( customField instanceof NumericCusomField ) {
					if( customFieldValue.getValue() != null ) {
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isNumeric(customFieldValue.getValue().toString())){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid number.");
								tempCustomField.setStatus(2);
							}else {
								tempCustomField.setStatus(1);
							}
						}
					}
				}else if( customField instanceof DateTimeCustomField ) {
					if( customFieldValue.getValue() != null ) {
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isValidDate(customFieldValue.getValue().toString(),true,"MM/dd/yyyy")){
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid date.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}
					}
				}else if( customField instanceof SSNCustomFiled ) {
					if( customFieldValue.getValue() != null ) {
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
	
	/**
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
}