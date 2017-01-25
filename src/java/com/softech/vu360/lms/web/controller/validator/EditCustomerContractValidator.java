package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.EditCustomerContractForm;

public class EditCustomerContractValidator implements Validator
{
	public EditCustomerContractValidator()
	{	   }

	@Override
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return EditCustomerContractForm.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		EditCustomerContractForm addSummaryForm = (EditCustomerContractForm)obj;
		
		if(StringUtils.isBlank(addSummaryForm.getContractName()))
		{
			errors.rejectValue("contractName", "error.addCustomerContract.contractname");
		}
//		else if (FieldsValidation.isInValidGlobalName(addSummaryForm.getContractName())){
//			errors.rejectValue("contractName", "error.addNewLearner.firstName.all.invalidText");
//		}
		
		if (addSummaryForm.isMaximumEnrollmentsUnLimited() == false ) {
			
			if(StringUtils.isBlank(addSummaryForm.getMaximumEnrollmentsLimitedValue())){
				errors.rejectValue("maximumEnrollmentsLimitedValue", "error.addCustomerContract.maximumEnrollmentsLimitedValue");
			}else if (!StringUtils.isNumeric(addSummaryForm.getMaximumEnrollmentsLimitedValue()))
				errors.rejectValue("maximumEnrollmentsLimitedValue", "error.addCustomerContract.maximumEnrollmentsLimitedValue.numeric");
		}
		
		validateAddCustomerContractsDates(addSummaryForm,errors);
}
	
public void validateAddCustomerContractsDates(EditCustomerContractForm form, Errors errors) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = null;
		Date endDate = null;
		
		if (StringUtils.isBlank(form.getStartDate())) {
			errors.rejectValue("startDate", "error.addCustomerContract.startDate.required");
		}else {
			try {
				startDate = formatter.parse(form.getStartDate());
				if (!formatter.format(startDate).equals(form.getStartDate())) {
					errors.rejectValue("startDate", "error.addCustomerContract.expDate.invalidDate");
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
				errors.rejectValue("startDate", "error.addCustomerContract.expDate.invalidDate");
			}
		}

		if (form.isTermsOfServices()==true) {
			if (StringUtils.isBlank(form.getTermsOfServicesValue())) {
				errors.rejectValue("termsOfServicesValue", "error.addCustomerContract.days.required");
			} else if (!StringUtils.isNumeric(form.getTermsOfServicesValue())) {
				errors.rejectValue("termsOfServicesValue", "error.addCustomerContract.days.invalid");
			} else if( Integer.parseInt(form.getTermsOfServicesValue()) <= 0 ) {
				errors.rejectValue("termsOfServicesValue", "error.addCustomerContract.days.notPositive");
			}
		} else {
			if (StringUtils.isBlank(form.getFixedEndDate())) {
				errors.rejectValue("fixedEndDate", "error.addCustomerContract.fiexedEndDate.required");
			} else {
				try {
					startDate = formatter.parse(form.getStartDate());
					endDate = formatter.parse(form.getFixedEndDate());
					if( startDate.equals(endDate) || startDate.after(endDate)) {
						errors.rejectValue("fixedEndDate", "lms.addCustomerContract.datesCrossed.error");
					}
				} catch (ParseException e) {
					errors.rejectValue("startDate", "error.addCustomerContract.expDate.invalidDate");
					e.printStackTrace();
				}
				
			}
		}
	}
	

}
