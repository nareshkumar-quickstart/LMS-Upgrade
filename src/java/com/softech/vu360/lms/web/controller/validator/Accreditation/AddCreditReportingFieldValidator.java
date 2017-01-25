package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.model.accreditation.CreditReportingFieldForm;
import com.softech.vu360.util.FieldsValidation;


/**
 * 
 * @author Saptarshi
 *
 */
public class AddCreditReportingFieldValidator implements Validator {

	private AccreditationService accreditationService = null;
	public boolean supports(Class clazz) {
		return CreditReportingFieldForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		CreditReportingFieldForm form = (CreditReportingFieldForm)obj;
		validateCreditReportingField(form, errors);
	}

	public void validateCreditReportingField(CreditReportingFieldForm form, Errors errors) {
		errors.setNestedPath("creditReportingField");
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		if(accreditationService.isCreditReportingFieldDuplicate(form.getCreditReportingField(),contentOwner.getId())){
			errors.setNestedPath("creditReportingField"); 
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.duplicate");
		}
		if (StringUtils.isBlank(form.getCreditReportingField().getFieldLabel()))		{
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.required");
		} else if( FieldsValidation.isInValidGlobalName(form.getCreditReportingField().getFieldLabel())) {
			// invalid text
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.invalidText");
		}
		errors.setNestedPath("");
		String fieldType = form.getFieldType();
		if(fieldType.equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_RADIO_BUTTON)){
			/*if (StringUtils.isBlank(form.getOption()))		{
				errors.rejectValue("option", "error.option.required");
			}*/
			this.validateOptions(form.getOptionList(), errors);
		}else if(fieldType.equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_CHECK_BOX)){
			this.validateOptions(form.getOptionList(), errors);
		} else if(fieldType.equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_CHOOSE)){
			this.validateOptions(form.getOptionList(), errors);
		}
	}
	
	public void validateCreditReportingField(CreditReportingFieldForm form, Errors errors, long CrfId) {
		errors.setNestedPath("creditReportingField");
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		if(accreditationService.isCreditReportingFieldDuplicate(form.getCreditReportingField(),contentOwner.getId())){
			errors.setNestedPath("creditReportingField"); 
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.duplicate");
		}
		if (StringUtils.isBlank(form.getCreditReportingField().getFieldLabel()))		{
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.required");
		} else if( FieldsValidation.isInValidGlobalName(form.getCreditReportingField().getFieldLabel())) {
			// invalid text
			errors.rejectValue("fieldLabel", "error.customField.fieldLabel.invalidText");
		}else if (form.getCreditReportingField().getId() != CrfId) {
			errors.rejectValue("id", "error.creditReportingField.reportingFieldId.fieldConflict");
		}
		
		errors.setNestedPath("");
		String fieldType = form.getFieldType();
		if(fieldType.equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_RADIO_BUTTON)){
			/*if (StringUtils.isBlank(form.getOption()))		{
				errors.rejectValue("option", "error.option.required");
			}*/
			this.validateOptions(form.getOptionList(), errors);
		}else if(fieldType.equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_CHECK_BOX)){
			this.validateOptions(form.getOptionList(), errors);
		} else if(fieldType.equalsIgnoreCase(CreditReportingFieldForm.REPORTINGFIELD_CHOOSE)){
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
