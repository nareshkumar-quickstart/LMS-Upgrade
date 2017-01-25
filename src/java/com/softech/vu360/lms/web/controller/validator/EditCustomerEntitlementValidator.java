package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.web.controller.model.AddCustomerEntitlementsForm;

public class EditCustomerEntitlementValidator implements Validator {

	public boolean supports(Class clazz) {
		return AddCustomerEntitlementsForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)obj;
		validateDetailsPage(form, errors);
		validateOrgGrpEntitlementPage(form, errors);
	}

	public void validateDetailsPage(AddCustomerEntitlementsForm form, Errors errors) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = null;
		Date endDate = null;
		if (StringUtils.isBlank(form.getEntitlementName())) {
			errors.rejectValue("entitlementName", "error.custEntitlement.entitlementName.required");
		}

		if (!form.isMaxEnrollments()) {
			if (StringUtils.isBlank(form.getNoOfMaxEnrollments())) {
				errors.rejectValue("noOfMaxEnrollments", "error.custEntitlement.noOfMaxEnrollments.required");
			} else if (!StringUtils.isNumeric(form.getNoOfMaxEnrollments())) {
				errors.rejectValue("noOfMaxEnrollments", "error.custEntitlement.noOfMaxEnrollments.invalid");
			}
		}

		if (StringUtils.isBlank(form.getStartDate())) {
			errors.rejectValue("startDate", "error.custEntitlement.startDate.required");
		}else {
			//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			//Date startDate = null;
			//Date date = new Date();
			try {
				startDate = formatter.parse(form.getStartDate());
				if (!formatter.format(startDate).equals(form.getStartDate())) {
					errors.rejectValue("startDate", "error.addNewLearner.expDate.invalidDate");
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
				errors.rejectValue("startDate", "error.addNewLearner.expDate.invalidDate");
			}
		}

		if (form.isTermsOfService()) {
			if (StringUtils.isBlank(form.getDays())) {
				errors.rejectValue("days", "error.custEntitlement.days.required");
			} else if (!StringUtils.isNumeric(form.getDays())) {
				errors.rejectValue("days", "error.custEntitlement.days.invalid");
			} else if( Integer.parseInt(form.getDays()) <= 0 ) {
				errors.rejectValue("days", "error.custEntitlement.days.notPositive");
			}
		} else {
			if (StringUtils.isBlank(form.getFiexedEndDate())) {
				errors.rejectValue("fiexedEndDate", "error.custEntitlement.fiexedEndDate.required");
			} else {
				try {
					startDate = formatter.parse(form.getStartDate());
					endDate = formatter.parse(form.getFiexedEndDate());
					if( startDate.equals(endDate) || startDate.after(endDate)) {
						errors.rejectValue("fiexedEndDate", "lms.addEnrollmentValidator.datesCrossed.error");
					}
				} catch (ParseException e) {
					errors.rejectValue("startDate", "error.addNewLearner.expDate.invalidDate");
					e.printStackTrace();
				}
				
			}
		}
	}

	public void validateOrgGrpEntitlementPage(AddCustomerEntitlementsForm form, Errors errors) {
		
		
		/*
		
		
		boolean anySelected = false;
		if(form.getOrganisationalGroupEntitlementItems() == null || 
				form.getOrganisationalGroupEntitlementItems().size() == 0) {
			anySelected = false;
		} else {
			for(OrganisationalGroupEntitlementItem orgGroupEntitlement : 
				(List<OrganisationalGroupEntitlementItem>)form.getOrganisationalGroupEntitlementItems()) {
				if(orgGroupEntitlement.isSelected()) {
					anySelected = true;
				}
			}
			for(OrganisationalGroupEntitlementItem orgGroupEntitlement : 
				(List<OrganisationalGroupEntitlementItem>)form.getOrganisationalGroupEntitlementItems()) {
				if(!orgGroupEntitlement.getMaxEnrollments().isEmpty()) 
					if (!orgGroupEntitlement.isSelected())
						errors.rejectValue("organisationalGroupEntitlementItems", 
								"error.custEntitlement.organisationalGroupEntitlementItems.enroll");
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("organisationalGroupEntitlementItems", 
					"error.custEntitlement.organisationalGroupEntitlementItems.required");
		}
	*/}

}