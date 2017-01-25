/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.DateTimeCreditReportingField;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCreditReportingField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCreditReportingFiled;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.TelephoneNumberCreditReportingField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.util.TextFormatter;
import com.softech.vu360.lms.web.controller.model.UserForm;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Arijit
 *
 */
public class UserValidator implements Validator {

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object command, Errors errors) {
		UserForm form = (UserForm)command;
		VU360User vu360User = form.getVu360User();
		if(StringUtils.isBlank(vu360User.getFirstName())){
			errors.rejectValue("vu360User.firstName", "error.editLearnerFirstName.required","");
		}else if (FieldsValidation.isInValidGlobalName(vu360User.getFirstName())){
			errors.rejectValue("vu360User.firstName", "error.editLearnerFirstName.required","");
		}
		if (StringUtils.isNotBlank(vu360User.getMiddleName())){
			if (FieldsValidation.isInValidGlobalName(vu360User.getMiddleName())){
				errors.rejectValue("vu360User.middleName", "error.editLearnerMiddleName.invalid","");
			}
		}
		if(StringUtils.isBlank(vu360User.getLastName())){
			errors.rejectValue("vu360User.lastName", "error.editLearnerFirstName.invalid","");
		}else if (FieldsValidation.isInValidGlobalName(vu360User.getLastName())){
			errors.rejectValue("vu360User.lastName", "error.editLearnerLastName.invalid","");
		}
		if (StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress())){
			if (FieldsValidation.isInValidAddress(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress())){
				errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress.streetAddress", "error.all.invalidText","");
			}
		}
		if (StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress2())){
			if (FieldsValidation.isInValidAddress(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getStreetAddress2())){
				errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress.streetAddress2", "error.all.invalidText","");
			}
		}
		if (StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getStreetAddress())){
			if (FieldsValidation.isInValidAddress(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getStreetAddress())){
				errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress2.streetAddress", "error.all.invalidText","");
			}
		}
		if (StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getStreetAddress2())){
			if (FieldsValidation.isInValidAddress(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getStreetAddress2())){
				errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress2.streetAddress2", "error.all.invalidText","");
			}
		}
		if (StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getCity())){
			if (FieldsValidation.isInValidGlobalName(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getCity())){
				errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress.city", "error.all.invalidText","");
			}
		}
		if (StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getCity())){
			if (FieldsValidation.isInValidGlobalName(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getCity())){
				errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress2.city", "error.all.invalidText","");
			}
		}
		
		
		if (StringUtils.isBlank(vu360User.getUsername())){
			
			 errors.rejectValue("vu360User.username", "error.addNewLearner.username.required","");
		}
		
		
/*		if (StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getZipcode())){
			if (StringUtils.length(vu360User.getLearner().getLearnerProfile().getLearnerAddress().getZipcode())>5){
				errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress.zipcode", "error.editLearnerZip.invalidlength","");
			}
		}
		if (StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getZipcode())){
			if (StringUtils.length(vu360User.getLearner().getLearnerProfile().getLearnerAddress2().getZipcode())>5){
				errors.rejectValue("vu360User.learner.learnerProfile.learnerAddress2.zipcode", "error.editLearnerZip.invalidlength","");
			}
		}
*/		
		if (StringUtils.isNotBlank(vu360User.getLearner().getLearnerProfile().getMobilePhone())){
			if (FieldsValidation.isInValidMobPhone(vu360User.getLearner().getLearnerProfile().getMobilePhone())){
				errors.rejectValue("vu360User.learner.learnerProfile.mobilePhone", "error.editLearnerMobilePhone.invalidText","");
			}
		}
		if (StringUtils.isNotBlank(vu360User.getEmailAddress())){
			if (!FieldsValidation.isEmailValid(vu360User.getEmailAddress())){
				errors.rejectValue("vu360User.emailAddress", "error.editLearnerEmail.invalidformat","");
			}
		}else{			
			   errors.rejectValue("vu360User.emailAddress", "error.learnerRegistrationEmail.required","");
			
	 	}
		/*
		if(form.getCreditReportingFields().size()>0){
			this.validateCustomFields(form.getCreditReportingFields(), errors);
		}
		*/
		if(form.getCreditReportingFields().size()>0){
			this.validateCustomFields(form.getCreditReportingFields(), errors);
		}
	}
	
	public void validateCustomFields( List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> customFields, Errors errors ) {
		int fieldindex=0;
		if (customFields.size()>0){

			for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField tempCustomField : customFields){

				CreditReportingField customField = tempCustomField.getCreditReportingFieldRef();
				CreditReportingFieldValue customFieldValue = tempCustomField.getCreditReportingFieldValueRef();

				if (customField.isFieldRequired()){
					if(customField instanceof MultiSelectCreditReportingField){

						if (((MultiSelectCreditReportingField) customField).isCheckBox()){
							int count=0;
							for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice  customFieldValueChoice : tempCustomField.getCreditReportingFieldValueChoices()){
								if (customFieldValueChoice.isSelected()){count=count+1;}
							}
							if(count==0){
								errors.rejectValue("creditReportingFields["+fieldindex+"].creditReportingFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}else {

							if(tempCustomField.getSelectedChoices() == null){
								errors.rejectValue("creditReportingFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else if (tempCustomField.getSelectedChoices().length==0){
								errors.rejectValue("creditReportingFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}

						}

					}else {
						
						if (customFieldValue.getValue()==null){
							errors.rejectValue("creditReportingFields["+fieldindex+"].creditReportingFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
							tempCustomField.setStatus(2);
						}else if(StringUtils.isBlank(customFieldValue.getValue().toString())){
							errors.rejectValue("creditReportingFields["+fieldindex+"].creditReportingFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
							tempCustomField.setStatus(2);
						}else{
							tempCustomField.setStatus(1);
						}
					}
				}
				if(customField instanceof NumericCreditReportingField){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isNumeric(customFieldValue.getValue().toString())){
								errors.rejectValue("creditReportingFields["+fieldindex+"].creditReportingFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid number.");
								tempCustomField.setStatus(2);
							}else {
								tempCustomField.setStatus(1);
							}
						}
					}
				}else if(customField instanceof DateTimeCreditReportingField){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isValidDate(customFieldValue.getValue().toString(),true,"MM/dd/yyyy")){
								errors.rejectValue("creditReportingFields["+fieldindex+"].creditReportingFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid date.");
								tempCustomField.setStatus(2);
							}
							else if((customField.getFieldLabel().equalsIgnoreCase("Date of Birth")||
									customField.getFieldLabel().equalsIgnoreCase("DOB")) &&
									(!CustomFieldValidationUtil.isValidBirthDate(customFieldValue.getValue().toString(), "MM/dd/yyyy", 10))) {
								errors.rejectValue(
										"creditReportingFields["
												+ fieldindex
												+ "].creditReportingFieldValueRef.value",
												"custom.field.required", 
										"The year you have entered is invalid, please enter a valid year of birth");
								tempCustomField.setStatus(2);
							}
							else{
								tempCustomField.setStatus(1);
							}
						}
					}
				}else if (customField instanceof SSNCreditReportingFiled){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isSSNValid(customFieldValue.getValue().toString())){
								errors.rejectValue("creditReportingFields["+fieldindex+"].creditReportingFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid SSN Number.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						}
					}
				}
				else if (customField instanceof TelephoneNumberCreditReportingField){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isTelephoneNumberValid(customFieldValue.getValue().toString())){
								errors.rejectValue("creditReportingFields["+fieldindex+"].creditReportingFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+" is a invaid Telephone Number.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
								customFieldValue.setValue(TextFormatter.convertTelephoneToNumber(customFieldValue.getValue().toString()));
							}
						}
					}
				}
				fieldindex = fieldindex+1;
			}
		}
	}

	
	public void validateCustomFieldsList(List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields, Errors errors) {
		int fieldindex=0;
		if (customFields.size()>0){

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
						}else {

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

					}else {
						
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
