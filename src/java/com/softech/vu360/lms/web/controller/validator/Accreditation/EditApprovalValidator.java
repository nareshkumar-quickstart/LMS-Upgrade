package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalForm;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRegulator;
import com.softech.vu360.lms.web.controller.validator.ValidationUtil;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Saptarshi
 * created on 8-july-2009
 *
 */
public class EditApprovalValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return ApprovalForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		ApprovalForm form = (ApprovalForm)obj;
		validateRegulator(form,errors);
		validateCourseApprovalSummary(form,errors);
		validateProviderApprovalSummary(form,errors);
		validateInstructorApprovalSummary(form,errors);
		validateCourseApprovalRenew(form,errors);
		validateProviderApprovalRenew(form,errors);
		validateInstructorApprovalRenew(form,errors);
	}

	public void validateRegulator(ApprovalForm form, Errors errors) {
		boolean flag = false;
		List<ApprovalRegulator> regulatorList = form.getRegulators();
		for (ApprovalRegulator appRegulator : regulatorList) {
			if (appRegulator.isSelected()) {
				flag = true;
				break;
			}
		}

		if (!flag) {
			errors.rejectValue("regulators", "error.editApproval.regulator.required");
		}
	}
	
	public void validateRegulatorCategory(ApprovalForm form, Errors errors) {
		boolean flag = false;
//		List<ApprovalRegulatorCategory> regulatorCategoryList = form.getRegulatorCategories();
//		for (ApprovalRegulatorCategory appRegulatorCategory : regulatorCategoryList) {
//			if (appRegulatorCategory.isSelected()) {
//				flag = true;
//				break;
//			}
//		}
		
		if(StringUtils.isBlank(form.getSelectedRegulatorCategoryId())){
			errors.rejectValue("regulatorCategories", "error.editApproval.regulatorCategory.required");
		}

//		if (!flag) {
//			errors.rejectValue("regulatorCategories", "error.editApproval.regulator.required");	
//		}
	}
	
	public void validateProviderApprovalSummary(ApprovalForm form, Errors errors) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;
		
		if( form.getProviderApproval() != null ) {
			errors.setNestedPath("providerApproval");
			if( StringUtils.isBlank(form.getProviderApproval().getApprovedProviderName()) ) {
				errors.rejectValue("approvedProviderName", "error.approval.providerName.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getProviderApproval().getApprovedProviderName()) ){
				errors.rejectValue("approvedProviderName", "error.approval.providerName.invalidText", "");
			}
			if( StringUtils.isBlank(form.getProviderApproval().getProviderApprovalNumber()) ) {
				errors.rejectValue("providerApprovalNumber", "error.approval.providerNumber.required", "");
			}
//			else if( !FieldsValidation.isValidAlphaNumeric(form.getProviderApproval().getProviderApprovalNumber()) ) {
//				errors.rejectValue("providerApprovalNumber", "error.approval.providerNumber.numeric", "");
//			}
			if( StringUtils.isBlank(form.getProviderApproval().getProviderApprovalStatus()) ) {
				errors.rejectValue("providerApprovalStatus", "error.approval.providerStatus.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getProviderApproval().getProviderApprovalStatus()) ){
				errors.rejectValue("providerApprovalStatus", "error.approval.providerStatus.invalidText", "");
			}
			if( StringUtils.isBlank(form.getProviderApproval().getProviderApprovalPeriod()) ) {
				errors.rejectValue("providerApprovalPeriod", "error.approval.providerPeriod.required", "");
			} /*else if( !StringUtils.isNumeric(form.getProviderApproval().getProviderApprovalPeriod()) ) {
				errors.rejectValue("providerApprovalPeriod", "error.approval.providerPeriod.numeric", "");
			}*/
			if( StringUtils.isBlank(form.getProviderApproval().getProviderDirector()) ) {
				errors.rejectValue("providerDirector", "error.approval.providerDirector.required", "");
			}
			if( StringUtils.isBlank(form.getProviderApproval().getOtherProviderRepresentative()) ) {
				if( FieldsValidation.isInValidGlobalName(form.getProviderApproval().getOtherProviderRepresentative()) ){
					errors.rejectValue("otherProviderRepresentative", "error.approval.representative.invalidText", "");
				}
			}
		}
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) ) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyStartDate())) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.invalid","");
		} else {
			try {
				sDate = formatter.parse(form.getApprovalEffectivelyStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) ) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyEndDate())) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.invalid","");
		} else {
			try {
				eDate = formatter.parse(form.getApprovalEffectivelyEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("approvalEffectivelyEndDate", "error.approval.invalidDate.crossed","");
			}
		}
		
		if( StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) ) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getMostRecentlySubmittedForApprovalDate())) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.invalid","");
		}
		
		if( StringUtils.isBlank(form.getOriginallyApprovedDate()) ) {
			errors.rejectValue("originallyApprovedDate", "error.approval.originalApprovadDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getOriginallyApprovedDate())) {
			errors.rejectValue("originallyApprovedDate", "error.approval.originalApprovadDate.invalid","");
		} 
		
		if( StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.invalid","");
		}
	}
	
	public void validateCourseApprovalSummary(ApprovalForm form, Errors errors) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;
		
		if( form.getCourseApproval() != null ) {
			errors.setNestedPath("courseApproval");
			if( StringUtils.isBlank(form.getCourseApproval().getApprovedCourseName()) ) {
				errors.rejectValue("approvedCourseName", "error.approval.approvedCourseName.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getCourseApproval().getApprovedCourseName()) ){
				errors.rejectValue("approvedCourseName", "error.approval.approvedCourseName.invalidText", "");
			}
			if( (StringUtils.isNotBlank(form.getCertificateExpirationPeriod())) ) {
				if(!StringUtils.isNumeric(form.getCertificateExpirationPeriod()) )
				   errors.rejectValue("certificateExpirationPeriod", "error.approval.certificateExpirationPeriod.numeric", "");
				if(StringUtils.isNumeric(form.getCertificateExpirationPeriod()) )
				 {
					if(form.getCertificateExpirationPeriod().length() > 4)
					errors.rejectValue("certificateExpirationPeriod", "error.approval.certificateExpirationPeriod.length", "");
					/*
					if(Integer.parseInt(form.getCertificateExpirationPeriod()) <= 0)
						errors.rejectValue("certificateExpirationPeriod", "error.approval.certificateExpirationPeriod.numeric", "");
					*/	
				 }
			} 
			if( StringUtils.isBlank(form.getCourseApproval().getCourseApprovalNumber()) ) {
				errors.rejectValue("courseApprovalNumber", "error.approval.approvedCourseName.required", "");
			} 
//			else if( !StringUtils.isNumeric(form.getCourseApproval().getCourseApprovalNumber()) ) {
//				errors.rejectValue("courseApprovalNumber", "error.approval.courseApprovalNumber.numeric", "");
//			}
			if( StringUtils.isBlank(form.getCourseApproval().getApprovedCreditHours()) ) {
				errors.rejectValue("approvedCreditHours", "error.approval.approvedCreditHours.required", "");
			}else{
				if(!ValidationUtil.isValidDecimal(form.getCourseApproval().getApprovedCreditHours())){
					errors.rejectValue("approvedCreditHours", "error.approval.approvedCreditHours.invalid", "");
				}
			}
			if( StringUtils.isBlank(form.getCourseApproval().getCourseApprovalStatus()) ) {
				errors.rejectValue("courseApprovalStatus", "error.approval.courseApprovalStatus.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getCourseApproval().getCourseApprovalStatus()) ){
				errors.rejectValue("courseApprovalStatus", "error.approval.courseApprovalStatus.invalidText", "");
			}
			if( StringUtils.isBlank(form.getCourseApproval().getCourseApprovalRenewalFee()) ) {
				errors.rejectValue("courseApprovalRenewalFee", "error.approval.courseApprovalRenewalFee.required", "");
			} else if( !ValidationUtil.isValidDecimal(form.getCourseApproval().getCourseApprovalRenewalFee()) ) {
				errors.rejectValue("courseApprovalRenewalFee", "error.approval.courseApprovalRenewalFee.numeric", "");
			}
			if( StringUtils.isBlank(form.getCourseApproval().getCourseApprovalSubmissionFee()) ) {
				errors.rejectValue("courseApprovalSubmissionFee", "error.approval.courseApprovalSubmissionFee.required", "");
			} else if( !ValidationUtil.isValidDecimal(form.getCourseApproval().getCourseApprovalSubmissionFee()) ) {
				errors.rejectValue("courseApprovalSubmissionFee", "error.approval.courseApprovalSubmissionFee.numeric", "");
			}
			
			if(StringUtils.isNotBlank(form.getCourseApproval().getCertificateNumberGeneratorPrefix())){
				if(form.getCourseApproval().getCertificateNumberGeneratorPrefix().length() > 30){
					errors.rejectValue("certificateNumberGeneratorPrefix", "error.approval.certificateNumberGeneratorPrefix.length", "");
				}
			}
			
			if(StringUtils.isNotBlank(form.getCourseApproval().getCertificateNumberGeneratorNumberFormat())){
				if(form.getCourseApproval().getCertificateNumberGeneratorNumberFormat().length() > 50){
					errors.rejectValue("certificateNumberGeneratorNumberFormat", "error.approval.certificateNumberGeneratorNumberFormat.length", "");
				}
			}

			if(StringUtils.isNotBlank(form.getCourseApproval().getCertificateNumberGeneratorPrefix()) && StringUtils.isNotBlank(form.getCourseApproval().getCertificateNumberGeneratorNumberFormat())){
				int totalLength = form.getCourseApproval().getCertificateNumberGeneratorPrefix().length() + form.getCourseApproval().getCertificateNumberGeneratorNumberFormat().length();
				if(totalLength > 50){
					errors.rejectValue("certificateNumberGeneratorPrefix", "error.approval.certificateNumberGeneratorPrefixAndNumberFormat.length", "");
				}
			}
			

			errors.setNestedPath("");
			
			if(StringUtils.isNotBlank(form.getCertificateNumberGeneratorNextNumberString())){
				if(form.getCertificateNumberGeneratorNextNumberString().length() > 19){
					errors.rejectValue("certificateNumberGeneratorNextNumberString", "error.approval.certificateNumberGeneratorNextNumber.length", "");
				}else if(!StringUtils.isNumeric(form.getCertificateNumberGeneratorNextNumberString())){
					errors.rejectValue("certificateNumberGeneratorNextNumberString", "error.approval.certificateNumberGeneratorNextNumber.invalid", "");
				}else if(Long.parseLong(form.getCertificateNumberGeneratorNextNumberString()) <= 0){
					errors.rejectValue("certificateNumberGeneratorNextNumberString", "error.approval.certificateNumberGeneratorNextNumber.required", "");
				}
			}else{
				if(form.getCourseApproval().getUseCertificateNumberGenerator()){
					errors.rejectValue("certificateNumberGeneratorNextNumberString", "error.approval.certificateNumberGeneratorNextNumber.required", "");
				}
				
			}

		
		}
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) ) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyStartDate())) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.invalid","");
		} else {
			try {
				sDate = formatter.parse(form.getApprovalEffectivelyStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) ) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyEndDate())) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.invalid","");
		} else {
			try {
				eDate = formatter.parse(form.getApprovalEffectivelyEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("approvalEffectivelyEndDate", "error.approval.invalidDate.crossed","");
			}
		}
		
		if( StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) ) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getMostRecentlySubmittedForApprovalDate())) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.invalid","");
		}
		
		if( StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.invalid","");
		}
		
		if(form.getCourseApproval().getUsePurchasedCertificateNumbers() &&  form.getCourseApproval().getUseCertificateNumberGenerator())
			errors.rejectValue("courseApproval.usePurchasedCertificateNumbers", "error.approval.UsePurchasedCertificateNumberAndUseCertificateNumberGenerator.condition", "");
		
	}
	
	public void validateInstructorApprovalSummary(ApprovalForm form, Errors errors) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;
		
		if( form.getInstructorApproval() != null ) {
			errors.setNestedPath("instructorApproval");
			if( StringUtils.isBlank(form.getInstructorApproval().getApprovedInstructorName()) ) {
				errors.rejectValue("approvedInstructorName", "error.approval.approvedInstructorName.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getInstructorApproval().getApprovedInstructorName()) ){
				errors.rejectValue("approvedInstructorName", "error.approval.approvedInstructorName.invalidText", "");
			}
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorApprovalNumber()) ) {
				errors.rejectValue("instructorApprovalNumber", "error.approval.instructorApprovalNumber.required", "");
			} 
//			else if(  !FieldsValidation.isValidAlphaNumeric(form.getInstructorApproval().getInstructorApprovalNumber())  ) {
//				errors.rejectValue("instructorApprovalNumber", "error.approval.instructorApprovalNumber.numeric", "");
//			}
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorApprovalStatus()) ) {
				errors.rejectValue("instructorApprovalStatus", "error.approval.instructorApprovalStatus.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getInstructorApproval().getInstructorApprovalStatus()) ){
				errors.rejectValue("instructorApprovalStatus", "error.approval.instructorApprovalStatus.invalidText", "");
			}
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorApprovalperiod()) ) {
				errors.rejectValue("instructorApprovalperiod", "error.approval.instructorApprovalperiod.required", "");
			} /*else if( !StringUtils.isNumeric(form.getInstructorApproval().getInstructorApprovalperiod()) ) {
				errors.rejectValue("instructorApprovalperiod", "error.approval.instructorApprovalperiod.numeric", "");
			}*/
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorApprovalFee()) ) {
				errors.rejectValue("instructorApprovalFee", "error.approval.instructorApprovalFee.required", "");
			} else if( !FieldsValidation.isNumeric(form.getInstructorApproval().getInstructorApprovalFee()) ) {
				errors.rejectValue("instructorApprovalFee", "error.approval.instructorApprovalFee.numeric", "");
			}
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorRenewalFee()) ) {
				errors.rejectValue("instructorRenewalFee", "error.approval.instructorRenewalFee.required", "");
			} else if( !FieldsValidation.isNumeric(form.getInstructorApproval().getInstructorRenewalFee()) ) {
				errors.rejectValue("instructorRenewalFee", "error.approval.instructorRenewalFee.numeric", "");
			}
		}
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) ) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyStartDate())) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.invalid","");
		} else {
			try {
				sDate = formatter.parse(form.getApprovalEffectivelyStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) ) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyEndDate())) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.invalid","");
		} else {
			try {
				eDate = formatter.parse(form.getApprovalEffectivelyEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("approvalEffectivelyEndDate", "error.approval.invalidDate.crossed","");
			}
		}
		
		if( StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) ) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getMostRecentlySubmittedForApprovalDate())) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.invalid","");
		}
		
		if( StringUtils.isBlank(form.getOriginallyApprovedDate()) ) {
			errors.rejectValue("originallyApprovedDate", "error.approval.originalApprovadDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getOriginallyApprovedDate())) {
			errors.rejectValue("originallyApprovedDate", "error.approval.originalApprovadDate.invalid","");
		} 
		
		if( StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.invalid","");
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
							} else {
								tempCustomField.setStatus(1);
							}
						} else {

							if(tempCustomField.getSelectedChoices() == null){
								errors.rejectValue("customFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							} else if (tempCustomField.getSelectedChoices().length==0){
								errors.rejectValue("customFields["+fieldindex+"].selectedChoices", "custom.field.required", "Please provide a value for the '"+customField.getFieldLabel()+"' field.");
								tempCustomField.setStatus(2);
							} else {
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
	
	
	public void validateCourseApprovalRenew(ApprovalForm form, Errors errors) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;
		
		if( form.getRenewCourseApproval() != null ) {
			errors.setNestedPath("renewCourseApproval");
			if( StringUtils.isBlank(form.getRenewCourseApproval().getApprovedCourseName()) ) {
				errors.rejectValue("approvedCourseName", "error.approval.approvedCourseName.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getRenewCourseApproval().getApprovedCourseName()) ){
				errors.rejectValue("approvedCourseName", "error.approval.approvedCourseName.invalidText", "");
			}
			if( StringUtils.isBlank(form.getRenewCourseApproval().getCourseApprovalNumber()) ) {
				errors.rejectValue("courseApprovalNumber", "error.approval.approvedCourseName.required", "");
			} 
//			else if( !StringUtils.isNumeric(form.getRenewCourseApproval().getCourseApprovalNumber()) ) {
//				errors.rejectValue("courseApprovalNumber", "error.approval.courseApprovalNumber.numeric", "");
//			}
			if( StringUtils.isBlank(form.getRenewCourseApproval().getApprovedCreditHours()) ) {
				errors.rejectValue("approvedCreditHours", "error.approval.approvedCreditHours.required", "");
			}else{
				if(!ValidationUtil.isValidDecimal(form.getRenewCourseApproval().getApprovedCreditHours())){
					errors.rejectValue("approvedCreditHours", "error.approval.approvedCreditHours.invalid", "");
				}
			}
			if( StringUtils.isBlank(form.getRenewCourseApproval().getCourseApprovalStatus()) ) {
				errors.rejectValue("courseApprovalStatus", "error.approval.courseApprovalStatus.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getRenewCourseApproval().getCourseApprovalStatus()) ){
				errors.rejectValue("courseApprovalStatus", "error.approval.courseApprovalStatus.invalidText", "");
			}
			if( StringUtils.isBlank(form.getRenewCourseApproval().getCourseApprovalRenewalFee()) ) {
				errors.rejectValue("courseApprovalRenewalFee", "error.approval.courseApprovalRenewalFee.required", "");
			} else if( !FieldsValidation.isNumeric(form.getRenewCourseApproval().getCourseApprovalRenewalFee()) ) {
				errors.rejectValue("courseApprovalRenewalFee", "error.approval.courseApprovalRenewalFee.numeric", "");
			}
			if( StringUtils.isBlank(form.getRenewCourseApproval().getCourseApprovalSubmissionFee()) ) {
				errors.rejectValue("courseApprovalSubmissionFee", "error.approval.courseApprovalSubmissionFee.required", "");
			} else if( !FieldsValidation.isNumeric(form.getRenewCourseApproval().getCourseApprovalSubmissionFee()) ) {
				errors.rejectValue("courseApprovalSubmissionFee", "error.approval.courseApprovalSubmissionFee.numeric", "");
			}
			
			if(StringUtils.isNotBlank(form.getRenewCourseApproval().getCertificateNumberGeneratorPrefix())){
				if(form.getRenewCourseApproval().getCertificateNumberGeneratorPrefix().length() > 30){
					errors.rejectValue("certificateNumberGeneratorPrefix", "error.approval.certificateNumberGeneratorPrefix.length", "");
				}
			}
			
			if(StringUtils.isNotBlank(form.getRenewCourseApproval().getCertificateNumberGeneratorNumberFormat())){
				if(form.getRenewCourseApproval().getCertificateNumberGeneratorNumberFormat().length() > 50){
					errors.rejectValue("certificateNumberGeneratorNumberFormat", "error.approval.certificateNumberGeneratorNumberFormat.length", "");
				}
			}

			if(StringUtils.isNotBlank(form.getRenewCourseApproval().getCertificateNumberGeneratorPrefix()) && StringUtils.isNotBlank(form.getRenewCourseApproval().getCertificateNumberGeneratorNumberFormat())){
				int totalLength = form.getRenewCourseApproval().getCertificateNumberGeneratorPrefix().length() + form.getRenewCourseApproval().getCertificateNumberGeneratorNumberFormat().length();
				if(totalLength > 50){
					errors.rejectValue("certificateNumberGeneratorPrefix", "error.approval.certificateNumberGeneratorPrefixAndNumberFormat.length", "");
				}
			}
			

			errors.setNestedPath("");
			
			if(StringUtils.isNotBlank(form.getCertificateNumberGeneratorNextNumberString())){
				if(form.getCertificateNumberGeneratorNextNumberString().length() > 19){
					errors.rejectValue("certificateNumberGeneratorNextNumberString", "error.approval.certificateNumberGeneratorNextNumber.length", "");
				}else if(!StringUtils.isNumeric(form.getCertificateNumberGeneratorNextNumberString())){
					errors.rejectValue("certificateNumberGeneratorNextNumberString", "error.approval.certificateNumberGeneratorNextNumber.invalid", "");
				}else if(Long.parseLong(form.getCertificateNumberGeneratorNextNumberString()) <= 0){
					errors.rejectValue("certificateNumberGeneratorNextNumberString", "error.approval.certificateNumberGeneratorNextNumber.required", "");
				}
			}else{
				if(form.getRenewCourseApproval().getUseCertificateNumberGenerator()){
					errors.rejectValue("certificateNumberGeneratorNextNumberString", "error.approval.certificateNumberGeneratorNextNumber.required", "");
				}
				
			}

		
		}
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) ) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyStartDate())) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.invalid","");
		} else {
			try {
				sDate = formatter.parse(form.getApprovalEffectivelyStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) ) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyEndDate())) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.invalid","");
		} else {
			try {
				eDate = formatter.parse(form.getApprovalEffectivelyEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("approvalEffectivelyEndDate", "error.approval.invalidDate.crossed","");
			}else{
				if(!validateIfDateIsAtleastCurrentDate(eDate)){
					errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.pastdate", "");
				}
			}
		}
		
		if( StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) ) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getMostRecentlySubmittedForApprovalDate())) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.invalid","");
		}
		
		if( StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.invalid","");
		}else{
			if(!validateIfDateIsAtleastCurrentDate(DateUtil.getDateObject(form.getSubmissionReminderDate()))){ 
				errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.pastdate", "");
			}
		}
		
		if(form.getRenewCourseApproval().getUsePurchasedCertificateNumbers() &&  form.getRenewCourseApproval().getUseCertificateNumberGenerator())
			errors.rejectValue("renewCourseApproval.usePurchasedCertificateNumbers", "error.approval.UsePurchasedCertificateNumberAndUseCertificateNumberGenerator.condition", "");

	}
	
	public void validateProviderApprovalRenew(ApprovalForm form, Errors errors) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;
		
		if( form.getRenewProviderApproval() != null ) {
			errors.setNestedPath("renewProviderApproval");
			if( StringUtils.isBlank(form.getRenewProviderApproval().getApprovedProviderName()) ) {
				errors.rejectValue("approvedProviderName", "error.approval.providerName.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getRenewProviderApproval().getApprovedProviderName()) ){
				errors.rejectValue("approvedProviderName", "error.approval.providerName.invalidText", "");
			}
			if( StringUtils.isBlank(form.getRenewProviderApproval().getProviderApprovalNumber()) ) {
				errors.rejectValue("providerApprovalNumber", "error.approval.providerNumber.required", "");
			} 
//			else if( !StringUtils.isNumeric(form.getRenewProviderApproval().getProviderApprovalNumber()) ) {
//				errors.rejectValue("providerApprovalNumber", "error.approval.providerNumber.numeric", "");
//			}
			if( StringUtils.isBlank(form.getRenewProviderApproval().getProviderApprovalStatus()) ) {
				errors.rejectValue("providerApprovalStatus", "error.approval.providerStatus.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getRenewProviderApproval().getProviderApprovalStatus()) ){
				errors.rejectValue("providerApprovalStatus", "error.approval.providerStatus.invalidText", "");
			}
			if( StringUtils.isBlank(form.getRenewProviderApproval().getProviderApprovalPeriod()) ) {
				errors.rejectValue("providerApprovalPeriod", "error.approval.providerPeriod.required", "");
			} /*else if( !StringUtils.isNumeric(form.getProviderApproval().getProviderApprovalPeriod()) ) {
				errors.rejectValue("providerApprovalPeriod", "error.approval.providerPeriod.numeric", "");
			}*/
			if( StringUtils.isBlank(form.getRenewProviderApproval().getProviderDirector()) ) {
				errors.rejectValue("providerDirector", "error.approval.providerDirector.required", "");
			}
			if( StringUtils.isBlank(form.getRenewProviderApproval().getOtherProviderRepresentative()) ) {
				if( FieldsValidation.isInValidGlobalName(form.getRenewProviderApproval().getOtherProviderRepresentative()) ){
					errors.rejectValue("otherProviderRepresentative", "error.approval.representative.invalidText", "");
				}
			}
		}
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) ) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyStartDate())) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.invalid","");
		} else {
			try {
				sDate = formatter.parse(form.getApprovalEffectivelyStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) ) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyEndDate())) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.invalid","");
		} else {
			try {
				eDate = formatter.parse(form.getApprovalEffectivelyEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("approvalEffectivelyEndDate", "error.approval.invalidDate.crossed","");
			}
		}
		
		if( StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) ) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getMostRecentlySubmittedForApprovalDate())) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.invalid","");
		}
		
		if( StringUtils.isBlank(form.getOriginallyApprovedDate()) ) {
			errors.rejectValue("originallyApprovedDate", "error.approval.originalApprovadDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getOriginallyApprovedDate())) {
			errors.rejectValue("originallyApprovedDate", "error.approval.originalApprovadDate.invalid","");
		} 
		
		if( StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.invalid","");
		}
		
	}
	
	public void validateInstructorApprovalRenew(ApprovalForm form, Errors errors) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;
		
		if( form.getRenewInstructorApproval() != null ) {
			errors.setNestedPath("renewInstructorApproval");
			if( StringUtils.isBlank(form.getRenewInstructorApproval().getApprovedInstructorName()) ) {
				errors.rejectValue("approvedInstructorName", "error.approval.approvedInstructorName.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getRenewInstructorApproval().getApprovedInstructorName()) ){
				errors.rejectValue("approvedInstructorName", "error.approval.approvedInstructorName.invalidText", "");
			}
			if( StringUtils.isBlank(form.getRenewInstructorApproval().getInstructorApprovalNumber()) ) {
				errors.rejectValue("instructorApprovalNumber", "error.approval.instructorApprovalNumber.required", "");
			} 
//			else if( !FieldsValidation.isValidAlphaNumeric(form.getInstructorApproval().getInstructorApprovalNumber()) ) {
//				errors.rejectValue("instructorApprovalNumber", "error.approval.instructorApprovalNumber.numeric", "");
//			}
			if( StringUtils.isBlank(form.getRenewInstructorApproval().getInstructorApprovalStatus()) ) {
				errors.rejectValue("instructorApprovalStatus", "error.approval.instructorApprovalStatus.required", "");
			} else if( FieldsValidation.isInValidGlobalName(form.getRenewInstructorApproval().getInstructorApprovalStatus()) ){
				errors.rejectValue("instructorApprovalStatus", "error.approval.instructorApprovalStatus.invalidText", "");
			}
			if( StringUtils.isBlank(form.getRenewInstructorApproval().getInstructorApprovalperiod()) ) {
				errors.rejectValue("instructorApprovalperiod", "error.approval.instructorApprovalperiod.required", "");
			} /*else if( !StringUtils.isNumeric(form.getRenewInstructorApproval().getInstructorApprovalperiod()) ) {
				errors.rejectValue("instructorApprovalperiod", "error.approval.instructorApprovalperiod.numeric", "");
			}*/
			if( StringUtils.isBlank(form.getRenewInstructorApproval().getInstructorApprovalFee()) ) {
				errors.rejectValue("instructorApprovalFee", "error.approval.instructorApprovalFee.required", "");
			} else if( !FieldsValidation.isNumeric(form.getRenewInstructorApproval().getInstructorApprovalFee()) ) {
				errors.rejectValue("instructorApprovalFee", "error.approval.instructorApprovalFee.numeric", "");
			}
			if( StringUtils.isBlank(form.getRenewInstructorApproval().getInstructorRenewalFee()) ) {
				errors.rejectValue("instructorRenewalFee", "error.approval.instructorRenewalFee.required", "");
			} else if( !FieldsValidation.isNumeric(form.getRenewInstructorApproval().getInstructorRenewalFee()) ) {
				errors.rejectValue("instructorRenewalFee", "error.approval.instructorRenewalFee.numeric", "");
			}
		}
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getApprovalEffectivelyStartDate()) ) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyStartDate())) {
			errors.rejectValue("approvalEffectivelyStartDate", "error.approval.effectiveStartDate.invalid","");
		} else {
			try {
				sDate = formatter.parse(form.getApprovalEffectivelyStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( StringUtils.isBlank(form.getApprovalEffectivelyEndDate()) ) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getApprovalEffectivelyEndDate())) {
			errors.rejectValue("approvalEffectivelyEndDate", "error.approval.effectiveEndDate.invalid","");
		} else {
			try {
				eDate = formatter.parse(form.getApprovalEffectivelyEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("approvalEffectivelyEndDate", "error.approval.invalidDate.crossed","");
			}
		}
		
		if( StringUtils.isBlank(form.getMostRecentlySubmittedForApprovalDate()) ) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getMostRecentlySubmittedForApprovalDate())) {
			errors.rejectValue("mostRecentlySubmittedForApprovalDate", "error.approval.rescentSubmittedDate.invalid","");
		}
		
		if( StringUtils.isBlank(form.getOriginallyApprovedDate()) ) {
			errors.rejectValue("originallyApprovedDate", "error.approval.originalApprovadDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getOriginallyApprovedDate())) {
			errors.rejectValue("originallyApprovedDate", "error.approval.originalApprovadDate.invalid","");
		} 
		
		if( StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.required", "");
		} else if (!FieldsValidation.isValidDate(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.invalid","");
		}
	}

    /**
     * Validates the date and makes sure if date is at least current date, 
     * and not form the past.
     * 
     * @param date
     * @param rejectedFieldName
     * @param errors 
     */
    private boolean validateIfDateIsAtleastCurrentDate(Date date){
        
        Date currentDate = new Date();
        
        if( DateUtils.isSameDay(currentDate, date) == false){
            if( date.compareTo(currentDate) < 0){
                return false;
            }
        }
        return true;
    }

}