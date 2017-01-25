package com.softech.vu360.lms.web.controller.validator.Accreditation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.web.controller.model.accreditation.AddapprovalForm;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalCredential;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRequirement;
import com.softech.vu360.lms.web.controller.validator.ValidationUtil;
import com.softech.vu360.util.CustomFieldValidationUtil;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Dyutiman
 * created on 8-july-2009
 *
 */
public class AddApprovalValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return AddapprovalForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddapprovalForm form = (AddapprovalForm)obj;
		validateSecondPage(form,errors);
		validateFourthPage(form,errors);
		validateFifthPage(form,errors);
		validateSixthPage(form,errors);
		validateEightPage(form,errors);
		validateNinthPage(form,errors);
		validateTenthPage(form,errors);
		validateEleventhPage(form,errors);
		validateTwelvthPage(form,errors);
		validateFourteenthPage(form,errors);
		validateSelectAssetPage(form, errors, Asset.ASSET_TYPE_CERTIFICATE);
		validateSelectAssetPage(form, errors, Asset.ASSET_TYPE_AFFIDAVIT);
	}


	public void validateSecondPage(AddapprovalForm form, Errors errors) {
		
		if(StringUtils.isBlank(form.getSelectedRegulatorCategoryId()) || !StringUtils.isNumeric(form.getSelectedRegulatorCategoryId())){
			errors.rejectValue("regulatorCategories", "error.approval.regulator.required");
		}
			
//		for( ApprovalRegulatorCategory regCat : form.getRegulatorCategories()) {
//			if( regCat.isSelected() ) {
//				sel = true;
//				break;
//			}
//		}
	}


	public void validateFourthPage(AddapprovalForm form, Errors errors) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;

		if( form.getCourseApproval() != null ) {
			errors.setNestedPath("courseApproval");
			if( StringUtils.isBlank(form.getCourseApproval().getApprovedCourseName()) ) {
				errors.rejectValue("approvedCourseName", "error.approval.courseName.required","");
			} else if( FieldsValidation.isInValidGlobalName(form.getCourseApproval().getApprovedCourseName()) ){
				errors.rejectValue("approvedCourseName", "error.approval.courseName.invalidText","");
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
			
			if(	StringUtils.isBlank(form.getCourseApproval().getCourseApprovalNumber()) ) {
				errors.rejectValue("courseApprovalNumber", "error.approval.courseNumber.required","");
			} /* LMS-5127
				else if( !StringUtils.isNumeric(form.getCourseApproval().getCourseApprovalNumber()) ) {
				errors.rejectValue("courseApprovalNumber", "error.approval.courseNumber.numeric","");

			}*/
			if(	StringUtils.isBlank(form.getCourseApproval().getCourseApprovalStatus()) ) {
				errors.rejectValue("courseApprovalStatus", "error.approval.courseStatus.required","");
			} else if( FieldsValidation.isInValidGlobalName(form.getCourseApproval().getCourseApprovalStatus()) ){
				errors.rejectValue("courseApprovalStatus", "error.approval.courseStatus.invalidText","");
			}
			if(	StringUtils.isBlank(form.getCourseApproval().getApprovedCreditHours()) ) {
				errors.rejectValue("approvedCreditHours", "error.approval.credithours.required","");
			}else{
				if(!ValidationUtil.isValidDecimal(form.getCourseApproval().getApprovedCreditHours())){
					errors.rejectValue("approvedCreditHours", "error.approval.approvedCreditHours.invalid", "");
				}
			}
			if(	StringUtils.isBlank(form.getCourseApproval().getCourseApprovalRenewalFee()) ) {
				errors.rejectValue("courseApprovalRenewalFee", "error.approval.courseRenewalFee.required","");
			} else if( !FieldsValidation.isNumeric(form.getCourseApproval().getCourseApprovalRenewalFee()) ) {
				errors.rejectValue("courseApprovalRenewalFee", "error.approval.courseRenewalFee.numeric","");
			}
			if(	StringUtils.isBlank(form.getCourseApproval().getCourseApprovalSubmissionFee()) ) {
				errors.rejectValue("courseApprovalSubmissionFee", "error.approval.courseSubmissionFee.required","");
			} else if( !FieldsValidation.isNumeric(form.getCourseApproval().getCourseApprovalSubmissionFee()) ) {
				errors.rejectValue("courseApprovalSubmissionFee", "error.approval.courseSubmissionFee.numeric","");
			}
		}
		errors.setNestedPath("");
		if( StringUtils.isNotBlank(form.getCourseApproval().getCourseApprovaltype()) ||
				StringUtils.isBlank(form.getCourseApproval().getCourseApprovaltype()) ) {
			if( FieldsValidation.isInValidGlobalName(form.getCourseApproval().getCourseApprovaltype()) ){
				errors.rejectValue("courseApproval", "error.approval.type.invalidText","");
			}
		}
		if( StringUtils.isNotBlank(form.getCourseApproval().getCourseApprovalInformation()) ||
				StringUtils.isBlank(form.getCourseApproval().getCourseApprovalInformation()) ) {
			if( FieldsValidation.isInValidGlobalName(form.getCourseApproval().getCourseApprovalInformation()) ){
				errors.rejectValue("courseApproval", "error.approval.information.invalidText","");
			}
		}
		if( StringUtils.isNotBlank(form.getCourseApproval().getTag()) || StringUtils.isBlank(form.getCourseApproval().getTag()) ) {
			if( FieldsValidation.isInValidGlobalName(form.getCourseApproval().getTag()) ){
				errors.rejectValue("courseApproval", "error.approval.tag.invalidText","");
			}
		}
		if( StringUtils.isBlank(form.getEffectiveStartDate()) ) {
			errors.rejectValue("effectiveStartDate", "error.approval.effectiveStartDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getEffectiveStartDate())) {
			errors.rejectValue("effectiveStartDate", "error.approval.effectiveStartDate.invalid","");
		} else {
			try {
				sDate = formatter.parse(form.getEffectiveStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//validateIfDateIsAtleastCurrentDate(sDate, "effectiveStartDate", errors);
		}
		if( StringUtils.isBlank(form.getEffectiveEndDate()) ) {
			errors.rejectValue("effectiveEndDate", "error.approval.effectiveEndDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getEffectiveEndDate())) {
			errors.rejectValue("effectiveEndDate", "error.approval.effectiveEndDate.invalid","");
		} else {
			try {
				eDate = formatter.parse(form.getEffectiveEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
                        validateIfDateIsAtleastCurrentDate(eDate, "effectiveEndDate", errors);
		}
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("effectiveEndDate", "error.approval.invalidDate.crossed","");
			}
		}                
		if( StringUtils.isBlank(form.getRescentSubmittedDate()) ) {
			errors.rejectValue("rescentSubmittedDate", "error.approval.rescentSubmittedDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getRescentSubmittedDate())) {
			errors.rejectValue("rescentSubmittedDate", "error.approval.rescentSubmittedDate.invalid","");
		}
		if( StringUtils.isBlank(form.getSubmissionReminderDate()) ) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.invalid","");
		}else{
         	validateIfDateIsAtleastCurrentDate(DateUtil.getDateObject(form.getSubmissionReminderDate()), "submissionReminderDate", errors);
		}
		
		errors.setNestedPath("courseApproval");
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
		
		if(form.getCourseApproval().getUsePurchasedCertificateNumbers() &&  form.getCourseApproval().getUseCertificateNumberGenerator())
			errors.rejectValue("courseApproval.usePurchasedCertificateNumbers", "error.approval.UsePurchasedCertificateNumberAndUseCertificateNumberGenerator.condition", "");
		
	}

        /**
         * Validates the date and makes sure if date is at least current date, 
         * and not form the past.
         * 
         * @param date
         * @param rejectedFieldName
         * @param errors 
         */
        private void validateIfDateIsAtleastCurrentDate(Date date, String rejectedFieldName, Errors errors){
            String rejectedFieldBrandingLabel = "error.approval." + rejectedFieldName + ".pastdate";
            Date currentDate = new Date();
            
            if( DateUtils.isSameDay(currentDate, date) == false){
                if( date.compareTo(currentDate) < 0){
                    errors.rejectValue( rejectedFieldName, rejectedFieldBrandingLabel,"");
                }
            }
        }

	public void validateFifthPage(AddapprovalForm form, Errors errors) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;
		
		if( form.getProviderApproval() != null ) {
			errors.setNestedPath("providerApproval");
			if( StringUtils.isBlank(form.getProviderApproval().getApprovedProviderName()) ) {
				errors.rejectValue("approvedProviderName", "error.approval.providerName.required","");
			} else if( FieldsValidation.isInValidGlobalName(form.getProviderApproval().getApprovedProviderName()) ){
				errors.rejectValue("approvedProviderName", "error.approval.providerName.invalidText","");
			}
			if(	StringUtils.isBlank(form.getProviderApproval().getProviderApprovalNumber()) ) {
				errors.rejectValue("providerApprovalNumber", "error.approval.providerNumber.required","");
			} else if( !FieldsValidation.isValidAlphaNumeric(form.getProviderApproval().getProviderApprovalNumber()) ) {
				errors.rejectValue("providerApprovalNumber", "error.approval.providerNumber.numeric","");
			}
			if(	StringUtils.isBlank(form.getProviderApproval().getProviderApprovalStatus()) ) {
				errors.rejectValue("providerApprovalStatus", "error.approval.providerStatus.required","");
			} else if( FieldsValidation.isInValidGlobalName(form.getProviderApproval().getProviderApprovalStatus()) ){
				errors.rejectValue("providerApprovalStatus", "error.approval.providerStatus.invalidText","");
			}
			if(	StringUtils.isBlank(form.getProviderApproval().getProviderApprovalPeriod()) ) {
				errors.rejectValue("providerApprovalPeriod", "error.approval.providerPeriod.required","");
			} else if( !StringUtils.isNumeric(form.getProviderApproval().getProviderApprovalPeriod()) ) {
				errors.rejectValue("providerApprovalPeriod", "error.approval.providerPeriod.numeric","");
			}
			if(	StringUtils.isBlank(form.getProviderApproval().getProviderDirector()) ) {
				errors.rejectValue("providerDirector", "error.approval.providerDirector.required","");
			}
			if( StringUtils.isNotBlank(form.getProviderApproval().getOtherProviderRepresentative()) ) {
				if( FieldsValidation.isInValidGlobalName(form.getProviderApproval().getOtherProviderRepresentative()) ){
					errors.rejectValue("otherProviderRepresentative", "error.approval.representative.invalidText","");
				}
			}
		}
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getEffectiveStartDate())) {
			errors.rejectValue("effectiveStartDate", "error.approval.effectiveStartDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getEffectiveStartDate())) {
			errors.rejectValue("effectiveStartDate", "error.approval.effectiveStartDate.invalid","");
		} else {
			try {
				sDate = formatter.parse(form.getEffectiveStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( StringUtils.isBlank(form.getEffectiveEndDate())) {
			errors.rejectValue("effectiveEndDate", "error.approval.effectiveEndDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getEffectiveEndDate())) {
			errors.rejectValue("effectiveEndDate", "error.approval.effectiveEndDate.invalid","");
		} else {
			try {
				eDate = formatter.parse(form.getEffectiveEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("approvalEffectivelyEndDate", "error.approval.invalidDate.crossed","");
			}
		}
		
		if( StringUtils.isBlank(form.getRescentSubmittedDate())) {
			errors.rejectValue("rescentSubmittedDate", "error.approval.rescentSubmittedDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getRescentSubmittedDate())) {
			errors.rejectValue("rescentSubmittedDate", "error.approval.rescentSubmittedDate.invalid","");
		}
		if( StringUtils.isBlank(form.getOriginalApprovadDate())) {
			errors.rejectValue("originalApprovadDate", "error.approval.originalApprovadDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getOriginalApprovadDate())) {
			errors.rejectValue("originalApprovadDate", "error.approval.originalApprovadDate.invalid","");
		}
		if( StringUtils.isBlank(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.invalid","");
		}
	}

	public void validateSixthPage(AddapprovalForm form, Errors errors) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;
		
		if( form.getInstructorApproval() != null ) {
			errors.setNestedPath("instructorApproval");
			if( StringUtils.isBlank(form.getInstructorApproval().getApprovedInstructorName()) ) {
				errors.rejectValue("approvedInstructorName", "error.approval.instructorName.required","");
			} else if( FieldsValidation.isInValidGlobalName(form.getInstructorApproval().getApprovedInstructorName()) ){
				errors.rejectValue("approvedInstructorName", "error.approval.instructorName.invalidText","");
			}
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorApprovalNumber())) {
				errors.rejectValue("instructorApprovalNumber", "error.approval.instructorNumber.required","");
			} else if( !FieldsValidation.isValidAlphaNumeric(form.getInstructorApproval().getInstructorApprovalNumber()) ) {
				errors.rejectValue("instructorApprovalNumber", "error.approval.instructorNumber.numeric","");
			}
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorApprovalStatus())) {
				errors.rejectValue("instructorApprovalStatus", "error.approval.instructorStatus.required","");
			} else if( FieldsValidation.isInValidGlobalName(form.getInstructorApproval().getInstructorApprovalStatus()) ){
				errors.rejectValue("instructorApprovalStatus", "error.approval.instructorStatus.invalidText","");
			}
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorApprovalperiod())) {
				errors.rejectValue("instructorApprovalperiod", "error.approval.instructorPeriod.required","");
			} /*else if( !StringUtils.isNumeric(form.getInstructorApproval().getInstructorApprovalperiod()) ) {
				errors.rejectValue("instructorApprovalperiod", "error.approval.instructorPeriod.numeric","");
			}*/
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorApprovalFee())) {
				errors.rejectValue("instructorApprovalFee", "error.approval.initialInstructorFee.required","");
			} else if( !FieldsValidation.isNumeric(form.getInstructorApproval().getInstructorApprovalFee()) ) {
				errors.rejectValue("instructorApprovalFee", "error.approval.initialInstructorFee.numeric","");
			}
			if( StringUtils.isBlank(form.getInstructorApproval().getInstructorRenewalFee())) {
				errors.rejectValue("instructorRenewalFee", "error.approval.instructorRenewalFee.required","");
			} else if( !FieldsValidation.isNumeric(form.getInstructorApproval().getInstructorRenewalFee()) ) {
				errors.rejectValue("instructorRenewalFee", "error.approval.instructorRenewalFee.numeric","");
			}
		}
		errors.setNestedPath("");
		if( StringUtils.isBlank(form.getEffectiveStartDate())) {
			errors.rejectValue("effectiveStartDate", "error.approval.effectiveStartDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getEffectiveStartDate())) {
			errors.rejectValue("effectiveStartDate", "error.approval.effectiveStartDate.invalid","");
		}  else {
			try {
				sDate = formatter.parse(form.getEffectiveStartDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} 
		if( StringUtils.isBlank(form.getEffectiveEndDate())) {
			errors.rejectValue("effectiveEndDate", "error.approval.effectiveEndDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getEffectiveEndDate())) {
			errors.rejectValue("effectiveEndDate", "error.approval.effectiveEndDate.invalid","");
		} else {
			try {
				eDate = formatter.parse(form.getEffectiveEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if( sDate != null && eDate != null ) {
			if( sDate.after(eDate) ) {
				errors.rejectValue("approvalEffectivelyEndDate", "error.approval.invalidDate.crossed","");
			}
		}
		
		if( StringUtils.isBlank(form.getRescentSubmittedDate())) {
			errors.rejectValue("rescentSubmittedDate", "error.approval.rescentSubmittedDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getRescentSubmittedDate())) {
			errors.rejectValue("rescentSubmittedDate", "error.approval.rescentSubmittedDate.invalid","");
		}
		if( StringUtils.isBlank(form.getOriginalApprovadDate())) {
			errors.rejectValue("originalApprovadDate", "error.approval.originalApprovadDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getOriginalApprovadDate())) {
			errors.rejectValue("originalApprovadDate", "error.approval.originalApprovadDate.invalid","");
		}
		if( StringUtils.isBlank(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.required","");
		} else if (!FieldsValidation.isValidDate(form.getSubmissionReminderDate())) {
			errors.rejectValue("submissionReminderDate", "error.approval.submissionReminderDate.invalid","");
		}
	}

	public void validateEightPage(AddapprovalForm form, Errors errors) {
		//if( form.getMethod().equalsIgnoreCase("providerApproval") || form.getMethod().equalsIgnoreCase("instructorApproval") ) {
		if( StringUtils.isBlank(form.getSelectedProviderId()) ) {
			errors.rejectValue("selectedProviderId", "error.approval.provider.required","");
		}
		//}
	}

	public void validateNinthPage(AddapprovalForm form, Errors errors) {
		if( form.getMethod().equalsIgnoreCase("instructorApproval") ) {
			if( StringUtils.isBlank(form.getSelectedInstructorId()) ) {
				errors.rejectValue("selectedInstructorId", "error.approval.instructor.required","");
			}
		}
	}

	public void validateTenthPage(AddapprovalForm form, Errors errors) {
		boolean sel = false;
		boolean req = false;
		if( form.getMethod().equalsIgnoreCase("courseApproval") ) {
			for( ApprovalCredential cred : form.getCredentials() ) {
				if( cred.isSelected() ) {
					sel = true;
					if( !cred.getRequirements().isEmpty() ) {
						req = true;
					}
				}
			}
			if( !sel ) {
				errors.rejectValue("credentials", "error.approval.credential.required","");
			} else {
				if( !req ) {
					errors.rejectValue("credentials", "error.approval.credential.noRequirement","");
				}
			}
		}
	}

	public void validateEleventhPage(AddapprovalForm form, Errors errors) {
		if( form.getMethod().equalsIgnoreCase("courseApproval") || form.getMethod().equalsIgnoreCase("instructorApproval") ) {
			if( StringUtils.isBlank(form.getSelectedCourseId()) ) {
				errors.rejectValue("selectedCourseId", "error.approval.course.required","");
			}
		}
	}

	public void validateCourseGroupPage(AddapprovalForm form, Errors errors) {
		if( form.getMethod().equalsIgnoreCase("courseApproval") ) {
			if( form.getCourseGroupId()!=null ) {
				
				try{
					if(Long.valueOf(form.getCourseGroupId())<=0)
						errors.rejectValue("courseGroupId", "error.editApproval.coursegroup.required","");
				}catch(Exception exs){
					errors.rejectValue("courseGroupId", "error.editApproval.coursegroup.required","");
				}
				
			}
			
		}
	}
	
	public void validateTwelvthPage(AddapprovalForm form, Errors errors) {
		boolean sel = false;
		if( form.getMethod().equalsIgnoreCase("courseApproval") ) {
			for( ApprovalCredential cred : form.getCredentials() ) {
				if( cred.isSelected() ) {
					for( ApprovalRequirement req : cred.getRequirements() ) {
						if( req.isSelected() ) {
							sel = true;
							break;
						}
					}
				}
			}
			if( !sel ) {
				errors.rejectValue("method", "error.approval.requirement.required","");
			}
		}
	}

	public void validateFourteenthPage(AddapprovalForm form, Errors errors) {
		if( form.getMethod().equalsIgnoreCase("courseApproval") ) {
			/*if( StringUtils.isBlank(form.getSelectedTemplateId()) ) {
				errors.rejectValue("selectedTemplateId", "error.approval.template.required","");
			}*/
		}
	}

	public void validateSelectAssetPage(AddapprovalForm form, Errors errors, String assetType) {
		if( form.getMethod().equalsIgnoreCase("courseApproval") ) {
			
			// LMS-12857 - comment out the following check because CERTIFICATE is optional in Course approval now
			/*if(assetType.equals(Asset.ASSET_TYPE_CERTIFICATE)){
				if( StringUtils.isBlank(form.getSelectedCertificateId()) ) {
					errors.rejectValue("selectedCertificateId", "error.approval.certificate.required","");
				}
			}else */
			
			if(assetType.equals(Asset.ASSET_TYPE_AFFIDAVIT)){
				if( StringUtils.isBlank(form.getSelectedAffidavitId()) ) {
					errors.rejectValue("selectedAffidavitId", "error.approval.affidavit.required","");
				}
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

}