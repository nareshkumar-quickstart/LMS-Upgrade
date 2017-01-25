package com.softech.vu360.lms.web.controller.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.web.controller.model.AddDistributorForm;
import com.softech.vu360.lms.web.controller.model.AddDistributorGroups;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FieldsValidation;

public class AddDistributorValidator implements Validator {
	
	private static final Logger log = Logger.getLogger(AddDistributorValidator.class.getName());
	private DistributorService distributorService = null;
	private VU360UserService vu360UserService = null;
	private ActiveDirectoryService activeDirectoryService;
	
	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}
	public boolean supports(Class clazz) {
		return AddDistributorForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddDistributorForm distributorForm = (AddDistributorForm)obj;
		validateProfilePage(distributorForm,errors);
		validateDistributorGroupPage(distributorForm,errors);
	}

	
	public void validateProfilePage(AddDistributorForm form, Errors errors) {
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		if (StringUtils.isBlank(form.getDistributorName()))		{
			errors.rejectValue("distributorName", "error.addDistributor.distributorName.required");
		} else if(distributorService.findDistributorsByName(form.getDistributorName(), loggedInUser, true, 0 ,-1 ,new ResultSet(),null,0).size() > 0) {
			errors.rejectValue("distributorName", "error.addDistributor.distributorName.exists");
		} else if(StringUtils.isNotEmpty(form.getDistributorCode()) && distributorService.findDistibutorByDistributorCode(form.getDistributorCode() ) != null) {
			errors.rejectValue("distributorCode", "error.addDistributor.distributorCode.invalidText");
		} else if (FieldsValidation.isInValidGlobalName(form.getDistributorName())){
			errors.rejectValue("distributorName", "error.distributorName.invalid");
		}
		
		if (StringUtils.isBlank(form.getFirstName()))		{
			errors.rejectValue("firstName", "error.addDistributor.firstName.required");
		}else if (FieldsValidation.isInValidGlobalName(form.getFirstName())){
			errors.rejectValue("firstName", "error.addDistributor.firstName.invalid");
		}
		
		if (StringUtils.isBlank(form.getLastName()))		{
			errors.rejectValue("lastName", "error.addDistributor.lastName.required");
		}else if (FieldsValidation.isInValidGlobalName(form.getLastName())){
			errors.rejectValue("lastName", "error.addDistributor.lastName.invalid");
		}
		
		if (!StringUtils.isBlank(form.getEmailAdd())){
			if (!(FieldsValidation.isEmailValid(form.getEmailAdd()))){
				errors.rejectValue("emailAdd", "error.addDistributor.email.invalid");
			}
		}

		if (!StringUtils.isBlank(form.getPhone())){
			if (FieldsValidation.isInValidOffPhone(form.getPhone())){
				errors.rejectValue("phone", "error.addDistributor.phone.invalid");
			}
		}
		
		if (!StringUtils.isBlank(form.getExt())){
			if (FieldsValidation.isInValidMobPhone(form.getExt())){
				errors.rejectValue("ext", "error.addDistributor.ext.invalid");
			}
		}
			
		if (!StringUtils.isBlank(form.getAddress1Line1())){
			if ((FieldsValidation.isInValidAddress((form.getAddress1Line1())))){
				errors.rejectValue("address1Line1", "error.addDistributor.address1.invalid");
			}
		}
		if (!StringUtils.isBlank(form.getAddress1Line2())){
			if ((FieldsValidation.isInValidAddress((form.getAddress1Line2())))){
				errors.rejectValue("address1Line2", "error.addDistributor.address1.invalid");
			}
		}
		
		if (!StringUtils.isBlank(form.getAddress2Line1())){
			if ((FieldsValidation.isInValidAddress((form.getAddress2Line1())))){
				errors.rejectValue("address2Line1", "error.addDistributor.address2.invalid");
			}
		}
		
		if (!StringUtils.isBlank(form.getAddress2Line2())){
			if ((FieldsValidation.isInValidAddress((form.getAddress2Line2())))){
				errors.rejectValue("address2Line2", "error.addDistributor.address2.invalid");
			}
		}
		
		// Sana Majeed | 12/30/2009 | LMS-955
		if (!StringUtils.isBlank(form.getWesiteURL())){
			if (!FieldsValidation.isValidWebURL(form.getWesiteURL())) {
				errors.rejectValue("wesiteURL", "error.addDistributor.wesiteURL.invalid");
			}
		}

		
		/*if (!StringUtils.isBlank(form.getCity1())){
			if (!StringUtils.isAlpha(form.getCity1())){
				errors.rejectValue("city1", "error.addDistributor.city1.invalid");
			}
		}
		if (!StringUtils.isBlank(form.getCity2())){
			if (!StringUtils.isAlpha(form.getCity2())){
				errors.rejectValue("city2", "error.addDistributor.city2.invalid");
			}
		}*/
		
		String country = null ;
		String zipCode = null ;
		
		if(form.getBrander() != null)	{
			// -----------------------------------------------------------------------------
			// 			for learner address 1 Zip Code   									//
			// -----------------------------------------------------------------------------
			
			country = form.getCountry1();
			zipCode = form.getZip1();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, form.getBrander(), log) ) {
            	log.debug("ZIP CODE FAILED" );
            	errors.rejectValue("zip1", ZipCodeValidator.getCountryZipCodeError(form.getCountry1(),form.getBrander()),"");
            }				
		
			// -----------------------------------------------------------------------------
			// 			for learner address 2 Zip Code   									//
			// -----------------------------------------------------------------------------
			country = form.getCountry2();
			zipCode = form.getZip2();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode,form.getBrander() , log) ) {
            	log.debug("ZIP CODE FAILED" );
            	errors.rejectValue("zip2", ZipCodeValidator.getCountryZipCodeError(form.getCountry2(),form.getBrander()),"");
            }				

			
		}
		
		if (StringUtils.isBlank(form.getLoginEmailID())) {
			errors.rejectValue("loginEmailID", "error.addNewLearner.email.required");
		} else if (!FieldsValidation.isEmailValid(form.getLoginEmailID())) {
			errors.rejectValue("loginEmailID", "error.addNewLearner.email.invalidformat");
		}if (vu360UserService.countUserByEmailAddress(form.getLoginEmailID()) != 0) {
			log.debug("Size of list");
			errors.rejectValue("loginEmailID", "error.addNewLearner.email.existEmail");
		}else if (activeDirectoryService.findADUser(form.getLoginEmailID())){
			errors.rejectValue("loginEmailID", "error.addNewUser.AD.existEmail");
		}
		
		if (StringUtils.isBlank(form.getPassword())) {
			errors.rejectValue("password", "error.password.required");
        } else if ( !FieldsValidation.isPasswordCorrect(form.getPassword()) ) {
			errors.rejectValue("password", "error.password.invalidlength");
		}

		if (StringUtils.isBlank(form.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "error.addNewLearner.confirmPassword.required");
		} else if (StringUtils.length(form.getConfirmPassword())<8) {
			errors.rejectValue("confirmPassword", "error.addNewLearner.confirmPassword.invalidlength");
		} else if (!form.getConfirmPassword().equals(form.getPassword())){
				errors.rejectValue("confirmPassword", "error.password.matchPassword");
		}
/*	
		if (!StringUtils.isBlank(form.getZip1())){
			if (StringUtils.length(form.getZip1())>5){
				errors.rejectValue("zip1", "error.addDistributor.zip1.invalidlength");
			}
			else if (!StringUtils.isNumeric(form.getZip1())){
				errors.rejectValue("zip1", "error.addDistributor.zip1.invalidText");
			}

		}
		if (!StringUtils.isBlank(form.getZip2())){
			if (StringUtils.length(form.getZip2())>5){
				errors.rejectValue("zip2", "error.addDistributor.zip2.invalidlength");
			}
			else if (!StringUtils.isNumeric(form.getZip2())){
				errors.rejectValue("zip2", "error.addDistributor.zip2.invalidText");
			}

		}
*/
	}
	
	public void validateDistributorGroupPage(AddDistributorForm form, Errors errors) {
		List<AddDistributorGroups> distributorGroups = form.getDistributors();
		int flag = 0;
		for (int i=0;i<distributorGroups.size();i++) 
			if(distributorGroups.get(i).isSelected() == true) {
				flag = 1;
				break;
			}
		if (flag == 0){
			errors.rejectValue("distributors", "error.distributorGroups.required");
		}
	}
	
	
	

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	
}
