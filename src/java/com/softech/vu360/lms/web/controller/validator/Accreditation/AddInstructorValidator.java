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
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.model.accreditation.InstructorForm;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Dyutiman
 */
public class AddInstructorValidator implements Validator {

	private VU360UserService vu360UserService;
	private static final Logger log = Logger.getLogger(AddInstructorValidator.class.getName());
	private ActiveDirectoryService activeDirectoryService;
	
	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return InstructorForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		InstructorForm form = (InstructorForm)obj;
		validateFirstPage(form, errors);
	}

	public void validateFirstPage(InstructorForm form, Errors errors) {

		errors.setNestedPath("instructor");
		if( StringUtils.isBlank(form.getInstructor().getAreaOfExpertise()) ) {
			errors.rejectValue("areaOfExpertise", "error.addNewLearner.areaOfExpertise.required","");
		} else if( FieldsValidation.isInValidGlobalName(form.getInstructor().getAreaOfExpertise()) ) {
			// invalid text
			errors.rejectValue("areaOfExpertise", "error.addNewLearner.areaOfExpertise.all.invalidText","");
		}
		errors.setNestedPath("");
		errors.setNestedPath("instructor.user");
		if( StringUtils.isBlank(form.getInstructor().getUser().getFirstName()) ) {
			errors.rejectValue("firstName", "error.addNewLearner.firstName.required","");
		} else if( FieldsValidation.isInValidGlobalName(form.getInstructor().getUser().getFirstName()) ) {
			// invalid text
			errors.rejectValue("firstName", "error.addNewLearner.firstName.all.invalidText","");
		}
		if( StringUtils.isBlank(form.getInstructor().getUser().getMiddleName()) ) {
			errors.rejectValue("middleName", "error.instructor.middlename.required","");
		} else if( FieldsValidation.isInValidGlobalName(form.getInstructor().getUser().getMiddleName()) ) {
			// invalid text
			errors.rejectValue("middleName", "error.addNewLearner.middleName.all.invalidText","");
		}
		if( StringUtils.isBlank(form.getInstructor().getUser().getLastName()) ) {
			errors.rejectValue("lastName", "error.addNewLearner.lastName.required","");
		} else if( FieldsValidation.isInValidGlobalName(form.getInstructor().getUser().getFirstName()) ) {
			// invalid text
			errors.rejectValue("lastName", "error.addNewLearner.lastName.all.invalidText","");
		}
		if( StringUtils.isBlank(form.getInstructor().getUser().getEmailAddress()) ) {
			errors.rejectValue("emailAddress", "error.addNewLearner.email.required","");
		} else if( !FieldsValidation.isEmailValid(form.getInstructor().getUser().getEmailAddress()) ) {
			// invalid email
			errors.rejectValue("emailAddress", "error.addNewLearner.email.invalidformat","");
		} else if( vu360UserService.countUserByEmailAddress(form.getInstructor().getUser().
				getEmailAddress()) != 0 ) {
			// email already exists
			errors.rejectValue("emailAddress", "error.addNewLearner.email.existEmail","");
		} else if (activeDirectoryService.findADUser(form.getInstructor().getUser().
				getEmailAddress())){
			errors.rejectValue("emailAddress", "error.addNewUser.AD.existEmail","");
		}
		errors.setNestedPath("");
		errors.setNestedPath("profile");
		if( StringUtils.isBlank(form.getProfile().getOfficePhone()) ) {
			errors.rejectValue("officePhone", "error.instructor.phone.required","");
		}
		if( StringUtils.isBlank(form.getProfile().getOfficePhoneExtn()) ) {
			errors.rejectValue("officePhoneExtn", "error.instructor.phoneExtn.required","");
		}
		errors.setNestedPath("");
		errors.setNestedPath("profile.learnerAddress");

		if( StringUtils.isBlank(form.getProfile().getLearnerAddress().getStreetAddress()) ) {
			errors.rejectValue("streetAddress", "error.instructor.address1.required","");
		} else if ( FieldsValidation.isInValidGlobalName(form.getProfile().getLearnerAddress().
				getStreetAddress()) ) {
			errors.rejectValue("streetAddress", "error.addRegulator.streetAddress1.invalidText","");
		}
		if( StringUtils.isBlank(form.getProfile().getLearnerAddress().getCity()) ) {
			errors.rejectValue("city", "error.instructor.city1.required","");
		} else if ( FieldsValidation.isInValidGlobalName(form.getProfile().getLearnerAddress().getCity()) ) {
			errors.rejectValue("city", "error.addRegulator.city.invalidText","");
		}
		if( StringUtils.isBlank(form.getProfile().getLearnerAddress().getState()) ) {
			errors.rejectValue("state", "error.instructor.state1.required","");
		} else if ( form.getProfile().getLearnerAddress().getState().equalsIgnoreCase("Select a State") ) {
			errors.rejectValue("state", "error.addRegulator.contactState","");
		}
		/*
		 *  validation of zipcode will not be done if country is UAE 
		 */
		if( StringUtils.isNotBlank(form.getProfile().getLearnerAddress().getCountry()) && 
				!form.getProfile().getLearnerAddress().getCountry().equalsIgnoreCase("AE") &&
				!form.getProfile().getLearnerAddress().getCountry().equalsIgnoreCase("United Arab Emirates") ) {

			if( StringUtils.isBlank(form.getProfile().getLearnerAddress().getZipcode()) ) {
				errors.rejectValue("zipcode", "error.instructor.zip1.required","");
			} else {

				if( form.getBrander() != null )	{

					String country = form.getProfile().getLearnerAddress().getCountry();
					String zipCode = form.getProfile().getLearnerAddress().getZipcode();

					if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, form.getBrander(), log) ) {
						//log.debug("ZIP CODE FAILED" );
						errors.rejectValue("zipcode", ZipCodeValidator.getCountryZipCodeError(
								form.getProfile().getLearnerAddress().getCountry(), form.getBrander()), "");
					}				
				}
			}
		}
		errors.setNestedPath("");
		errors.setNestedPath("profile.learnerAddress2");
		/*
		 *  validation of zipcode will not be done if country is UAE 
		 */
		if( StringUtils.isNotBlank(form.getProfile().getLearnerAddress2().getCountry()) && 
				!form.getProfile().getLearnerAddress2().getCountry().equalsIgnoreCase("AE") &&
				!form.getProfile().getLearnerAddress2().getCountry().equalsIgnoreCase("United Arab Emirates") ) {

			if( !StringUtils.isBlank(form.getProfile().getLearnerAddress2().getZipcode()) ) {

				if( form.getBrander() != null )	{

					String country = form.getProfile().getLearnerAddress2().getCountry();
					String zipCode = form.getProfile().getLearnerAddress2().getZipcode();

					if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, form.getBrander(), log) ) {
						//log.debug("ZIP CODE FAILED" );
						errors.rejectValue("zipcode", ZipCodeValidator.getCountryZipCodeError(
								form.getProfile().getLearnerAddress2().getCountry(), form.getBrander()), "");
					}				
				}
			}
		}
		errors.setNestedPath("");
		errors.setNestedPath("profile.learnerAddress");

		if( StringUtils.isBlank(form.getProfile().getLearnerAddress().getCountry()) ) {
			errors.rejectValue("country", "error.instructor.country1.required","");
		} else if ( form.getProfile().getLearnerAddress().getCountry().equalsIgnoreCase("Select a Country") ) {
			errors.rejectValue("country", "error.addRegulator.contactCountry","");
		}
		errors.setNestedPath("");
		errors.setNestedPath("instructor.user");
		if( StringUtils.isBlank(form.getInstructor().getUser().getUsername()) ) {
			errors.rejectValue("username", "error.instructor.username.required","");
		} else if ( FieldsValidation.isInValidGlobalName(form.getInstructor().getUser().getUsername()) ) {
			// invalid text
			errors.rejectValue("username", "error.addNewLearner.username.all.invalidText","");
		}
		if( StringUtils.isBlank(form.getInstructor().getUser().getPassword()) ) {
			errors.rejectValue("password", "error.addNewLearner.password.required","");
		//KS - 2009-12-15 - LMS-3667
//		} else if ( form.getInstructor().getUser().getPassword().length() < 8 ) {
		} else if ( !FieldsValidation.isPasswordCorrect(form.getInstructor().getUser().getPassword()) ) {
			errors.rejectValue("password", "error.addNewLearner.password.invalid","");
		}
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getConfirnPassword()) ) {
			errors.rejectValue("confirnPassword", "error.instructor.conpassword.required","");
		} else if ( !form.getConfirnPassword().equalsIgnoreCase(form.getInstructor().getUser().getPassword())) {
			errors.rejectValue("confirnPassword", "error.instructor.conpassword.invalid","");
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

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

}