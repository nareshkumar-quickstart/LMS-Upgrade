package com.softech.vu360.lms.web.controller.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.administrator.AddCustomFieldWizardController;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldForm;
import com.softech.vu360.util.FieldsValidation;

public class CustomFieldValidator implements Validator {

	public boolean supports(Class clazz) {
		return CustomFieldForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		CustomFieldForm form = (CustomFieldForm)obj;
		validateAddCustomFieldPage(form, errors);
	}

	public void validateAddCustomFieldPage(CustomFieldForm form, Errors errors) {
		errors.setNestedPath("customField");
		if (StringUtils.isBlank(form.getCustomField().getFieldLabel()))		{
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.required");
		} else if( FieldsValidation.isInValidGlobalName(form.getCustomField().getFieldLabel())) {
			// invalid text
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.invalidText");
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

	private void validateOptions(List<String> options, Errors errors){
		if(options==null || options.size()<=0){
			errors.rejectValue("option", "error.customField.option.required");
		}else if(options.size()==1){
			errors.rejectValue("option", "error.customField.optionList.multiple");
		}
	}
}
