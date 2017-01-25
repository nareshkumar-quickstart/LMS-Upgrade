package com.softech.vu360.lms.web.controller.validator.Instructor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.instructor.AddCustomFieldWizardForLocationController;
import com.softech.vu360.lms.web.controller.model.instructor.CustomFieldLocationForm;
import com.softech.vu360.util.FieldsValidation;

public class AddCustomLocationValidator implements Validator{
	
	private AccreditationService accreditationService = null;
	
	public boolean supports(Class clazz) {
		return CustomFieldLocationForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		CustomFieldLocationForm form = (CustomFieldLocationForm)obj;
		validateAddCustomFieldPage(form, errors);
	}
	
	public void validateAddCustomFieldPage(CustomFieldLocationForm form, Errors errors) {
		errors.setNestedPath("customField");
		if (StringUtils.isBlank(form.getCustomField().getFieldLabel()))		{
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.required");
		} else if( FieldsValidation.isInValidGlobalName(form.getCustomField().getFieldLabel())) {
			// invalid text
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.invalidText");
		}
			if(! accreditationService.isUniqueCustomFieldName(form.getEntity(),form.getCustomField().getFieldLabel())){
				errors.rejectValue("fieldLabel", "error.customField.fieldLabel.duplicate");
			}
			
		errors.setNestedPath("");
		String fieldType = form.getFieldType();
		if(fieldType.equalsIgnoreCase(AddCustomFieldWizardForLocationController.CUSTOMFIELD_RADIO_BUTTON)){
			/*if (StringUtils.isBlank(form.getOption()))		{
				errors.rejectValue("option", "error.option.required");
			}*/
			this.validateOptions(form.getOptionList(), errors);
		}else if(fieldType.equalsIgnoreCase(AddCustomFieldWizardForLocationController.CUSTOMFIELD_CHECK_BOX)){
			this.validateOptions(form.getOptionList(), errors);
		} else if(fieldType.equalsIgnoreCase(AddCustomFieldWizardForLocationController.CUSTOMFIELD_CHOOSE)){
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

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	
}
