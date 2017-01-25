package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.impl.VU360UserServiceImpl;
import com.softech.vu360.lms.web.controller.model.AddDistributorEntitlementsForm;
import com.softech.vu360.lms.web.controller.model.DistributorEntitlementCourseGroup;
import com.softech.vu360.util.FieldsValidation;

public class AddDistributorEntitlementValidator implements Validator{
	
	private Logger log = Logger.getLogger(VU360UserServiceImpl.class.getName());

	private EntitlementService entitlementService=null;
	
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return AddDistributorEntitlementsForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddDistributorEntitlementsForm addDistributorEntitlementsForm = (AddDistributorEntitlementsForm)obj;
		validatePage1(addDistributorEntitlementsForm,errors);
		validatePage2(addDistributorEntitlementsForm,errors);

	}

	public void validatePage1(AddDistributorEntitlementsForm form, Errors errors) {
		long diffInMilliseconds =(24 * 60 * 60 * 1000);
		if(StringUtils.isBlank(form.getEntitlementName())){
			errors.rejectValue("entitlementName", "error.customerentitlement.validateNameBlank");
		}else if (FieldsValidation.isInValidGlobalName(form.getEntitlementName())) {
			errors.rejectValue("entitlementName", "error.custEntitlement.entitlementName.invalid");
		}

		if (entitlementService.getDistributorEntitlementByName(form.getEntitlementName(),form.getDistributor().getId() )!=null){
			errors.rejectValue("entitlementName", "error.customerentitlement.DuplicateName");
		}
		if(form.isMaxEnrollments()==false){
			if(StringUtils.isBlank(form.getNoOfMaxEnrollments())){
				errors.rejectValue("noOfMaxEnrollments", "error.customerentitlement.maximumEnrollment");
			}else {
				try {
					Integer.parseInt(form.getNoOfMaxEnrollments());

				} catch (Exception e) {
					errors.rejectValue("noOfMaxEnrollments", "error.customerentitlement.maximumEnrollment");
				}
			}

		}
		
		if(!StringUtils.isBlank(form.getTransactionAmount()) && !FieldsValidation.isNumeric(form.getTransactionAmount())) {
			errors.rejectValue("transactionAmount", "error.customerentitlement.invalidTransactionAmount");
		}
		
		if(StringUtils.isBlank(form.getStartDate())){
			errors.rejectValue("startDate", "error.customerentitlement.invalidStartDate");

		}else{
			if(!FieldsValidation.isValidDate(form.getStartDate())){
				errors.rejectValue("startDate", "error.customerentitlement.invalidStartDate");

			}else{
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date newEnrollmentDate = null;
				try{
					newEnrollmentDate=formatter.parse(form.getStartDate());
					if (!formatter.format(newEnrollmentDate).equals(form.getStartDate())) {
						errors.rejectValue("startDate", "error.customerentitlement.invalidStartDate");

					}
				}
				catch (ParseException e) {
					errors.rejectValue("startDate", "error.customerentitlement.invalidStartDate");
					e.printStackTrace();
					
				}
				Calendar calender=Calendar.getInstance();
				Date date=calender.getTime();


				if( ( newEnrollmentDate.getTime()-date.getTime()  )/ diffInMilliseconds < 0){
					errors.rejectValue("startDate", "error.customerentitlement.invalidStartDate");
				}
			}
		}


		if(form.isTermsOfService()==false && StringUtils.isBlank(form.getFiexedEndDate())){
			errors.rejectValue("fiexedEndDate", "error.customerentitlement.enterdate");
		}else if(form.isTermsOfService()==false && !FieldsValidation.isValidDate(form.getFiexedEndDate())){
			errors.rejectValue("fiexedEndDate", "error.customerentitlement.invalidEndDate");
		}else if(form.isTermsOfService()==false && !StringUtils.isBlank(form.getFiexedEndDate())){
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date newEnrollmentDate = null;
			try{
				newEnrollmentDate=formatter.parse(form.getFiexedEndDate());
				if (!formatter.format(newEnrollmentDate).equals(form.getFiexedEndDate())) {
					errors.rejectValue("fiexedEndDate", "error.customerentitlement.invalidEndDate");

				}
			}
			catch (ParseException e) {
				errors.rejectValue("fiexedEndDate", "error.customerentitlement.invalidEndDate");
				e.printStackTrace();
			}

			Date startDate = null;
			try{
				startDate=formatter.parse(form.getStartDate());
				if (!formatter.format(startDate).equals(form.getStartDate())) {
					errors.rejectValue("startDate", "error.customerentitlement.invalidStartDate");

				}
			}
			catch (ParseException e) {
				errors.rejectValue("startDate", "error.customerentitlement.invalidStartDate");
				e.printStackTrace();
			}
			//Calendar calender=Calendar.getInstance();
			//Date date = calender.getTime();
			if( (  newEnrollmentDate.getTime()-startDate.getTime() )/ diffInMilliseconds < 0){
				errors.rejectValue("fiexedEndDate", "error.customerentitlement.invalidEndDate");
			}
		}

		if( form.isTermsOfService() == true ) {
			if( StringUtils.isBlank(form.getDays()) ) {
				errors.rejectValue("days", "error.customerentitlement.enterdays");
			} else if( !FieldsValidation.isNumeric(form.getDays()) ) {
				errors.rejectValue("days", "error.customerentitlement.InvalidInput");
			} else {
				try {
					int days = Integer.parseInt(form.getDays());
					if( days <= 0 ) {
						errors.rejectValue("days", "error.customerentitlement.negativeDays");
					}
				} catch (Exception e) {
					errors.rejectValue("days", "error.customerentitlement.InvalidInput");
				}
			}
		}
		
		String amt = form.getTransactionAmount();
		try {
			if ( StringUtils.isNotEmpty(amt) ){
				if(StringUtils.isNumeric(Math.round(Double.parseDouble(amt))+"")){
					//Max DB length: 18 digit including 2 precision
					Double val = Double.parseDouble(form.getTransactionAmount());
					if ( String.valueOf(val.longValue()).length() > 16) {
						errors.rejectValue("transactionAmount","error.customerentitlement.invalidTransactionAmount");
					}
				}
			}else{
				form.setTransactionAmount("0.0");
			} 
		} catch (Exception e) {
			log.debug("exception", e);
		}
		
		/*if(request.getParameter("maximumEnrollmentsRadio").equalsIgnoreCase("notUnlimited") 
				&& StringUtils.isBlank(request.getParameter("maximumEnrollments"))){
			context.put("validateMaximumEnrollments","error.customerentitlement.maximumEnrollment");
			check = true;
		}*/

		this.checkCourseGroup(errors);
	}

	public void validatePage2(AddDistributorEntitlementsForm form, Errors errors) {
		boolean anySelected =false;
		if (form.getCourseGroupSearchType().isEmpty()) {
			if (form.getGroups() != null) {
				anySelected = true;
			}
		} else {
			if(form.getSelectedCourseGroups() == null || form.getSelectedCourseGroups().size() == 0) {
				anySelected = false;
			} else {
				for(DistributorEntitlementCourseGroup  distributorEntitlementCourseGroup:form.getSelectedCourseGroups()){
					if(distributorEntitlementCourseGroup.isSelected()){
						anySelected = true;
					}
				}
			}
		}
		if( anySelected == false ) {
			errors.rejectValue("groups", "error.custEntitlement.selectedCourseGroups.required");
		}
		
		if(!StringUtils.isBlank(form.getTransactionAmount()) && !FieldsValidation.isNumeric(form.getTransactionAmount())) {
			errors.rejectValue("transactionAmount", "error.customerentitlement.invalidTransactionAmount");
		}
		
	}

	public void checkCourseGroup(Errors errors) {

		List<CourseGroup> courseGroupList = entitlementService.findCourseGroups("%");

		if ( courseGroupList == null ) {
			errors.rejectValue("entitlementType", "error.custEntitlement.entitlementType.courseGroup");
		}
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
}