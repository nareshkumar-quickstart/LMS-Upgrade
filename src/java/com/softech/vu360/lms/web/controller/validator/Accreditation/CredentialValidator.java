package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialForm;
import com.softech.vu360.lms.web.controller.validator.ValidationUtil;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.VU360Branding;

public class CredentialValidator implements Validator {

        /** Brander for the page. */
        private Brander brander = VU360Branding.getInstance().getBrander(VU360Branding.DEFAULT_BRAND, new com.softech.vu360.lms.vo.Language());
        
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return CredentialForm.class.isAssignableFrom(clazz);
	}

	public void validate( Object obj, Errors errors ) {
		CredentialForm form = (CredentialForm)obj;
		validateAddCredential(form, errors);
		validateAddRequirement(form, errors);
	}
	
	// -- Changed due to LMS-10697
	// -- Rehan Rana --------------------------------------------------------------------- 

	public void validateAddCredential(CredentialForm form, Errors errors) {

		Credential credential = form.getCredential();
		errors.setNestedPath("credential");
		if( StringUtils.isBlank(credential.getOfficialLicenseName()) ) {
			errors.rejectValue("officialLicenseName", "error.addCredential.officialLicenseName.required","");
		} else if(FieldsValidation.isInValidGlobalName(credential.getOfficialLicenseName())) {
			errors.rejectValue("officialLicenseName", "error.addCredential.officialLicenseName.invalid","");
		}

		if( StringUtils.isBlank(credential.getVerifiedBy())) {
			errors.rejectValue("verifiedBy", "error.addCredential.verifiedBy.required","");
		} else if(FieldsValidation.isInValidGlobalName(credential.getVerifiedBy())) {
			errors.rejectValue("verifiedBy", "error.addCredential.verifiedBy.invalid","");
		}
		
		/*if( StringUtils.isBlank(credential.getDescription()) || StringUtils.isEmpty(credential.getDescription())) {
			errors.rejectValue("description", "error.addCredential.description.required","");
		}*/
		
		/*if(FieldsValidation.isInValidGlobalName(credential.getShortLicenseName()) || StringUtils.isBlank(credential.getShortLicenseName())) {
			errors.rejectValue("shortLicenseName", "error.addCredential.shortLicenseName.invalid","");
		}*/

		if( StringUtils.isNotBlank(credential.getJurisdiction()) ) {
			if( FieldsValidation.isInValidGlobalName((credential.getJurisdiction())) ) {				
				errors.rejectValue("jurisdiction", "	","");
			}
			if(credential.getJurisdiction().equalsIgnoreCase("other")){
				if(StringUtils.isBlank(credential.getOtherJurisdiction())){					
					errors.rejectValue("otherJurisdiction", "error.addCredential.jurisdiction.other","");
				}
			}
		}
		/*if( StringUtils.isBlank(credential.getJurisdiction()) ) {							
				errors.rejectValue("jurisdiction", "error.addCredential.jurisdiction.blankJurisdiction","");			
		}*/
                
        /*  String defaultHardDeadlineMonthOption = brander.getBrandElement("lms.accreditation.addCredential.caption.HardDeadlineMonth.defaultOption");
        if( defaultHardDeadlineMonthOption.equalsIgnoreCase(credential.getHardDeadlineMonth())){
            errors.rejectValue("jurisdiction", "error.addCredential.hardDeadlineMonth.validMonthSelection","");
        }*/

		errors.setNestedPath("");
		/*if (StringUtils.isBlank(form.getTotalNumberOfLicense())) {
			errors.rejectValue("totalNumberOfLicense", "error.addCredential.totalNumberOfLicense.required","");
		} else if (!StringUtils.isNumeric(form.getTotalNumberOfLicense())) {
			errors.rejectValue("totalNumberOfLicense", "error.addCredential.totalNumberOfLicense.invalid","");
		} else {
			try {
				Integer.parseInt(form.getTotalNumberOfLicense());

			} catch (Exception e) {
				errors.rejectValue("totalNumberOfLicense", "error.addCredential.totalNumberOfLicense.invalid","");
			}
		}*/

		
		if (!StringUtils.isBlank(form.getTotalNumberOfLicense())){
			try {
				Integer.parseInt(form.getTotalNumberOfLicense());

			} catch (Exception e) {
				errors.rejectValue("totalNumberOfLicense", "error.addCredential.totalNumberOfLicense.invalid","");
			}
		}
		
		errors.setNestedPath("");
		if (StringUtils.isBlank(form.getInformationLastVerifiedDate())) {
			errors.rejectValue("informationLastVerifiedDate", "error.addCredential.informationLastVerifiedDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getInformationLastVerifiedDate())) {
			errors.rejectValue("informationLastVerifiedDate", "error.addCredential.informationLastVerifiedDate.invalid","");
		} 
	}

	public void validateAddRequirementWithBIFields(CredentialForm form, Errors errors) {

		CredentialCategoryRequirement requirement = form.getRequirement();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		errors.setNestedPath("requirement");

		if( StringUtils.isBlank(requirement.getName()) ) {
			errors.rejectValue("name", "error.addRequirement.name.required");
		}
		
		/*
		 * checking if the error field already has casting error...
		 * happens if non numeric value is given to 'numberOfHours' field.  
		 */
		FieldError r = errors.getFieldError("numberOfHours");
		if( r == null || ( r != null && !r.getField().equals("requirement.numberOfHours") ) ) {
			if( requirement.getNumberOfHours() <= 0 ) {
				errors.rejectValue("numberOfHours", "error.addRequirement.numberOfHours.required","");
			}
		}
		
		if( StringUtils.isBlank(requirement.getSeatTimeMeasurement()) ) {
			errors.rejectValue("seatTimeMeasurement", "error.addRequirement.seatTimeMeasurement.required","");
		}
		if( StringUtils.isBlank(requirement.getReciprocity()) ) {
			errors.rejectValue("reciprocity", "error.addRequirement.reciprocity.required","");
		}
		if( StringUtils.isBlank(requirement.getCourseValidation()) ) {
			errors.rejectValue("courseValidation", "error.addRequirement.courseValidation.required","");
		}
		if( StringUtils.isBlank(requirement.getCourseApprovalPeriod()) ) {
			errors.rejectValue("courseApprovalPeriod", "error.addRequirement.courseApprovalPeriod.required","");
		}
		if( StringUtils.isBlank(requirement.getCourseAssessments()) ) {
			errors.rejectValue("courseAssessments", "error.addRequirement.courseAssessments.required","");
		}
		if( StringUtils.isBlank(requirement.getCertificateProcedure()) ) {
			errors.rejectValue("certificateProcedure", "error.addRequirement.certificateProcedure.required","");
		}
		if( StringUtils.isBlank(requirement.getWhoReportsCredits()) ) {
			errors.rejectValue("whoReportsCredits", "error.addRequirement.whoReportsCredits.required","");
		}
		if( requirement.getReportingFees() < 0.0) {
			errors.rejectValue("reportingFees", "error.addRequirement.reportingFees.required","");
		}

		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getReportingPeriod()) ) {
			errors.rejectValue("reportingPeriod", "error.addRequirement.reportingPeriod.required","");
		} else if (!FieldsValidation.isValidDate(form.getReportingPeriod())) {
			errors.rejectValue("reportingPeriod", "error.addRequirement.reportingPeriod.invalid","");
		} else {
			try {
				Date reportingPeriodDate = formatter.parse(form.getReportingPeriod());
				if (!formatter.format(reportingPeriodDate).equals(form.getReportingPeriod())) {
					errors.rejectValue("reportingPeriod", "error.addRequirement.reportingPeriod.invalid","");
				} 
				Date date = new Date();
				date.setDate(date.getDate()-1);
				if( reportingPeriodDate.before(date)) {
					errors.rejectValue("reportingPeriod", "error.addRequirement.reportingPeriod.invalid","");
				} 
			} catch (ParseException e) {
				errors.rejectValue("reportingPeriod", "error.addRequirement.reportingPeriod.invalid","");
				e.printStackTrace();
			}
		}
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
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+"' is a invalid number.");
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
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+"' is a invalid date.");
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
								errors.rejectValue("customFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+"' is a invalid SSN Number.");
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
	public void validateCredentialRequirementCustomFields( List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields, Errors errors ) {
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
								errors.rejectValue("credentialRequirementCustomFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else{
								tempCustomField.setStatus(1);
							}
						} else {

							if(tempCustomField.getSelectedChoices() == null){
								errors.rejectValue("credentialRequirementCustomFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							}else if (tempCustomField.getSelectedChoices().length==0){
								errors.rejectValue("credentialRequirementCustomFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
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
							errors.rejectValue("credentialRequirementCustomFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
							tempCustomField.setStatus(2);
						}else if(StringUtils.isBlank(customFieldValue.getValue().toString())){
							errors.rejectValue("credentialRequirementCustomFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
							tempCustomField.setStatus(2);/*PROPER MESSAGE BEING CREATED BUT NOT BEING DISPLAYED*/
						}else{
							tempCustomField.setStatus(1);
						}
					}
				}
				if(customField instanceof NumericCusomField){
					if(customFieldValue.getValue()!=null){
						if(StringUtils.isNotBlank(customFieldValue.getValue().toString())){
							if (!CustomFieldValidationUtil.isNumeric(customFieldValue.getValue().toString())){
								errors.rejectValue("credentialRequirementCustomFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+"' is a invalid number.");
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
								errors.rejectValue("credentialRequirementCustomFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+"' is a invalid date.");
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
								errors.rejectValue("credentialRequirementCustomFields["+fieldindex+"].customFieldValueRef.value", "custom.field.required", "'"+customField.getFieldLabel()+"' is a invalid SSN Number.");
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
	
	// [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
	public void validateAddCategory (CredentialForm form, Errors errors) {
	
		CredentialCategory category = form.getCategory();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "category.name", "error.addCredentialCategory.name.required", "");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "category.strHours", "error.addCredentialCategory.hours.required", "");
		
		if ( category.getName().length() > 250) {
			errors.rejectValue("category.name", "error.addCredentialCategory.name.tooLong", "");
		}
		
                
                if( category.getCategoryType().equalsIgnoreCase(brander.getBrandElement("lms.accreditation.addRequirement.caption.type.defaultOption")) ){
                    errors.rejectValue("category.categoryType", "error.addCredential.categoryType.select", "");
                }
//                if( category.getCoverage().equalsIgnoreCase(brander.getBrandElement("lms.accreditation.addRequirement.caption.RequirementCoverageOptions.defaultOption")) ){
//                    errors.rejectValue("category.coverage", "error.addCredential.requirementCoverage.select", "");
//                }
                
		try{
			category.setHours(0);
			if (StringUtils.isNotBlank( category.getStrHours() )) {
				if (NumberUtils.isNumber( category.getStrHours() )) {
                                    if( ValidationUtil.isValidHourWithValidDecimals(category.getStrHours()))
					category.setHours( Float.valueOf(category.getStrHours()) );
                                    else{
                                        errors.rejectValue("category.strHours", "error.addCredentialCategory.hours.extraDecimalPoints", "");
                                    }
				}
				else {
					errors.rejectValue("category.strHours", "error.addCredentialCategory.hours.numeric", "");
				}
				
				if ( category.getHours() < 0.0) {
					errors.rejectValue("category.strHours", "error.addCredentialCategory.hours.negative", "");
				}
			}
		}
		catch (NumberFormatException e) {
			errors.rejectValue("category.strHours", "error.addCredentialCategory.hours.numeric", "");
		}
		
		if ( StringUtils.isNotBlank(category.getDescription()) && category.getDescription().equalsIgnoreCase("<br>") ){
			form.getCategory().setDescription(null);
		}
		
		if ( form.getCredentialRequirementCustomFields()!=null && form.getCredentialRequirementCustomFields().size() > 0 ) {
			this.validateCredentialRequirementCustomFields( form.getCredentialRequirementCustomFields() , errors);
		}
		
		if(category.getCategoryType().equals(CredentialCategory.POST_LICENSE)){						
			try{
				category.setCompletionDeadlineMonths(new Float(0));
				if (StringUtils.isNotBlank( category.getStrCompletionDeadlineMonths())) {				
					if (NumberUtils.isNumber( category.getStrCompletionDeadlineMonths())) {
						category.setCompletionDeadlineMonths(Float.valueOf(category.getStrCompletionDeadlineMonths()));
					}
					else {
						errors.rejectValue("category.strCompletionDeadlineMonths", "error.addRequirement.completionDeadline.nan", "");
					}
					
					if ( category.getCompletionDeadlineMonths()< 0.0) {
						errors.rejectValue("category.strCompletionDeadlineMonths", "error.addRequirement.completionDeadline.nan", "");
					}
				}
			}
			catch (NumberFormatException e) {
				errors.rejectValue("category.strCompletionDeadlineMonths", "error.addRequirement.completionDeadline.nan", "");
			}
						
		}
//		if ( StringUtils.isBlank(category.getCoverage())){
//			errors.rejectValue("category.coverage", "error.addRequirement.coverage.empty", "");
//		}		
	}
	
	
	public void validateAddRequirement (CredentialForm form, Errors errors) {
		
		CredentialCategoryRequirement requirement = form.getRequirement();		
		errors.setNestedPath("");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "requirement.name", "error.addRequirement.name.required", "");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditHours", "error.addRequirement.hours.numeric", "");
		
                String hours = form.getCreditHours();
                
                if (StringUtils.isNotBlank( hours ) ) {				
                    if (NumberUtils.isNumber( hours )) {
                        if( ValidationUtil.isValidHourWithValidDecimals(hours))
                            requirement.setNumberOfHours( Double.valueOf(hours) );
                        else{
                            errors.rejectValue("requirement.numberOfHours", "error.addCredentialCategory.hours.extraDecimalPoints", "");
                        }
                        
                        if ( Double.valueOf(hours) < 0.0) {
                            errors.rejectValue("requirement.numberOfHours", "error.addRequirement.hours.negative", "");
                        }
                    }
                    else {
                            errors.rejectValue("requirement.numberOfHours", "error.addRequirement.hours.numeric", "");
                    }                    
                }
		
		if ( StringUtils.isNotBlank(requirement.getDescription()) && requirement.getDescription().equalsIgnoreCase("<br>") ){
			form.getRequirement().setDescription(null);
		}
		
	}
	
}