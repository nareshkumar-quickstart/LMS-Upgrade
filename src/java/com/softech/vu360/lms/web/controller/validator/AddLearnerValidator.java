/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.manager.AddLearnerController;
import com.softech.vu360.lms.web.controller.model.LearnerDetailsForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Somnath
 *
 */
public class AddLearnerValidator implements Validator {
	private VU360UserService vu360UserService;
	private static final Logger log = Logger.getLogger(AddLearnerController.class.getName());

	//16-01-2009
	private LearnerService learnerService;
	/**
	 * 
	 */

	private ActiveDirectoryService activeDirectoryService;
	
	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}
	
	public AddLearnerValidator() {
		// TODO Auto-generated constructor stub
	}


	public boolean supports(Class clazz) {
		return LearnerDetailsForm.class.isAssignableFrom(clazz);
	}


	public void validate(Object obj, Errors errors) {
		LearnerDetailsForm learnerDetails = (LearnerDetailsForm)obj;
		// TODO call the individual page validation routines
		validatePage1(learnerDetails, errors);
		validatePage2(learnerDetails, errors);
		validatePage3(learnerDetails, errors);
	}


	public void validatePage1(LearnerDetailsForm learnerDetails, Errors errors  ){
		log.debug("Test");
		//ValidationUtils.rejectIfEmpty(errors, "password", "PASSWORD", "Password is required.");
		
		learnerDetails.setUserName(StringUtils.trim(learnerDetails.getUserName()));
		
		VU360User usr=vu360UserService.findUserByUserName(learnerDetails.getUserName().trim());
		
		if (StringUtils.isBlank(learnerDetails.getEmailAddress()))
		{
			errors.rejectValue("emailAddress", "error.addNewLearner.email.required","Email address required");
		}
		else if
		(!FieldsValidation.isEmailValid(learnerDetails.getEmailAddress())){
			errors.rejectValue("emailAddress", "error.addNewLearner.email.invalidformat","Invalid email address");
		}

		/*
		else if (vu360UserService.getUsersByEmailAddress(learnerDetails.getEmailAddress()).size() != 0) {
			log.debug("Size of list");
			errors.rejectValue("emailAddress", "error.addNewLearner.email.existEmail");
		}
        */
		
		//log.debug("learnerDetails.getUserName()="+learnerDetails.getUserName()+"learnerDetails.getOldUserName()"+learnerDetails.getOldUserName());
		if (StringUtils.isBlank(learnerDetails.getUserName()))
		{
			errors.rejectValue("userName", "error.addNewLearner.username.all.required","User name required");
		}
		else if (FieldsValidation.isInValidUsername(learnerDetails.getUserName())){
			errors.rejectValue("userName", "error.addNewLearner.username.all.invalidText","Bad characters not allowed (Username)");
		}
		else if (learnerDetails.getOldUserName()!=null && learnerDetails.getOldUserName().trim().compareToIgnoreCase(learnerDetails.getUserName().trim())!=0) {
			 			        
			        if (usr!=null && usr.getUsername().trim().compareToIgnoreCase(learnerDetails.getUserName().trim())==0) {
				           errors.rejectValue("userName", "error.addNewLearner.username.all.existUsername","User name already exists");
			         }
		      
		     }else if(usr!=null && usr.getUsername().trim().compareToIgnoreCase(learnerDetails.getUserName().trim())==0){
		    	   
		    	   log.debug("Size of list");
			       errors.rejectValue("userName", "error.addNewLearner.username.all.existUsername","User name already exists");
		    	  		    	  
		      }
		     else if (activeDirectoryService.findADUser(learnerDetails.getUserName())){
					errors.rejectValue("userName", "error.addNewUser.AD.existUsername","Username already exists. Please use different Username or contact support at 888-360-8764 for further assistance.");
				}

		
		//19.01.2009
		//StringUtils.isAlpha(arg0)

		//isValidName
		/*else if (!StringUtils.isAlphanumericSpace(learnerDetails.getFirstName())){
			errors.rejectValue("firstName", "error.all.invalidText");
		}*/

		else if (FieldsValidation.isInValidGlobalName(learnerDetails.getFirstName())){
			errors.rejectValue("firstName", "error.addNewLearner.firstName.all.invalidText" ,"Bad characters not allowed (First name)");
		}
		if (StringUtils.isBlank(learnerDetails.getFirstName())){
			errors.rejectValue("firstName", "error.addNewLearner.firstName.required","First Name required");
		}

		if (!StringUtils.isBlank(learnerDetails.getMiddleName())){
			if (FieldsValidation.isInValidGlobalName(learnerDetails.getMiddleName())){
				errors.rejectValue("middleName", "error.addNewLearner.middleName.all.invalidText","Bad characters not allowed (Middle name)");
			}
		}

		if (StringUtils.isBlank(learnerDetails.getLastName())){
			errors.rejectValue("lastName", "error.addNewLearner.lastName.required","Last Name required");
		}
		else if (FieldsValidation.isInValidGlobalName(learnerDetails.getLastName())){
			errors.rejectValue("lastName", "error.addNewLearner.lastName.all.invalidText","Bad characters not allowed (Last name)");
		}
		//19.01.2009
		/*else if (!StringUtils.isAlphanumericSpace(learnerDetails.getLastName())){
			errors.rejectValue("lastName", "error.all.invalidText");
		}*/
		if (!StringUtils.isBlank(learnerDetails.getOfficePhone())){
			if (FieldsValidation.isInValidOffPhone((learnerDetails.getOfficePhone()))){
				errors.rejectValue("officePhone", "error.addNewLearner.officePhone.all.invalidText", "Bad characters not allowed (Office Phone)");
			}
		}
		if (!StringUtils.isBlank(learnerDetails.getOfficePhoneExtn())){
			if (FieldsValidation.isInValidMobPhone((learnerDetails.getOfficePhoneExtn()))){
				errors.rejectValue("officePhoneExtn", "error.addNewLearner.officePhoneExtn.all.invalidText","Bad characters not allowed (Office Phone Extension)");
			}
		}
		if (!StringUtils.isBlank(learnerDetails.getMobilePhone())){
			if (FieldsValidation.isInValidMobPhone((learnerDetails.getMobilePhone()))){
				errors.rejectValue("mobilePhone", "error.addNewLearner.mobilePhone.all.invalidText","Bad characters not allowed (Mobile Phone)");
			}
		}
		if (!StringUtils.isBlank(learnerDetails.getStreetAddress1())){
			if (FieldsValidation.isInValidAddress((learnerDetails.getStreetAddress1()))){
				errors.rejectValue("streetAddress1", "error.addNewLearner.streetAddress1.all.invalidText","Bad characters not allowed (Address1)");
			}
		}
		if (!StringUtils.isBlank(learnerDetails.getStreetAddress1a())){
			if (FieldsValidation.isInValidAddress((learnerDetails.getStreetAddress1a()))){
				errors.rejectValue("streetAddress1a", "error.addNewLearner.streetAddress1.all.invalidText","Bad characters not allowed (Address1)");
			}
		}

		if (!StringUtils.isBlank(learnerDetails.getStreetAddress2())){
			if (FieldsValidation.isInValidAddress((learnerDetails.getStreetAddress2()))){
				errors.rejectValue("streetAddress2", "error.addNewLearner.streetAddress2.all.invalidText","Bad characters not allowed (Address2)");
			}
		}
		if (!StringUtils.isBlank(learnerDetails.getStreetAddress2a())){
			if (FieldsValidation.isInValidAddress((learnerDetails.getStreetAddress2a()))){
				errors.rejectValue("streetAddress2a", "error.addNewLearner.streetAddress2.all.invalidText","Bad characters not allowed (Address2)");
			}
		}



		if (!StringUtils.isBlank(learnerDetails.getCity())){
			if (FieldsValidation.isInValidGlobalName(learnerDetails.getCity())){
				errors.rejectValue("city", "error.addNewLearner.city1.all.invalidText" ,"Bad characters not allowed (City1)");
			}
		}

		if (!StringUtils.isBlank(learnerDetails.getCity2())){
			if (FieldsValidation.isInValidGlobalName(learnerDetails.getCity2())){
				errors.rejectValue("city2", "error.addNewLearner.city2.all.invalidText" , "Bad characters not allowed (City2)");
			}
		}
/*
 * date : 18 -August - 2009  
*/
		learnerDetails.setZipcode(learnerDetails.getZipcode().trim());
		learnerDetails.setZipcode2(learnerDetails.getZipcode2().trim());
		
		
		if(learnerDetails.getBrander() != null)	{
			String country = null ;
			String zipCode = null ;
			// -----------------------------------------------------------------------------
			// 			for learner address 1 Zip Code   									//
			// -----------------------------------------------------------------------------
			
			country = learnerDetails.getCountry();
			zipCode = learnerDetails.getZipcode();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, learnerDetails.getBrander(), log) ) {
            	log.debug("ZIP CODE FAILED" );
            	errors.rejectValue("zipcode", ZipCodeValidator.getCountryZipCodeError(learnerDetails.getCountry(), learnerDetails.getBrander()) , learnerDetails.getBrander().getBrandElement(ZipCodeValidator.getCountryZipCodeError(learnerDetails.getCountry(), learnerDetails.getBrander())));
            }				
		
			// -----------------------------------------------------------------------------
			// 			for learner address 2 Zip Code   									//
			// -----------------------------------------------------------------------------
			country = learnerDetails.getCountry2();
			zipCode = learnerDetails.getZipcode2();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, learnerDetails.getBrander(), log) ) {
            	log.debug("ZIP CODE FAILED" );
            	errors.rejectValue("zipcode2", ZipCodeValidator.getCountryZipCodeError(learnerDetails.getCountry2(), learnerDetails.getBrander()) , learnerDetails.getBrander().getBrandElement(ZipCodeValidator.getCountryZipCodeError(learnerDetails.getCountry2(), learnerDetails.getBrander())) );
            }	
			
		}


        validatePasswords(learnerDetails, errors); //joong LMS-3343

		if (!StringUtils.isBlank(learnerDetails.getExpirationDate())){
			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date expirationDate = null;
			Date date = new Date();
			try {
				expirationDate = formatter.parse(learnerDetails.getExpirationDate());
				if (!formatter.format(expirationDate).equals(learnerDetails.getExpirationDate())) {
					errors.rejectValue("expirationDate", "error.addNewLearner.expDate.invalidDate","Invalid Date");

				}else {
					if( expirationDate.before(date)  ) {
						errors.rejectValue("expirationDate", "error.addNewLearner.expDate.invalidDate","Invalid Date");
					}
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
				errors.rejectValue("expirationDate", "error.addNewLearner.expDate.invalidDate","Invalid Date");
			}
			
		}
	}

    //refactored code to validate both passwords per LMS-3343
    private void validatePasswords(LearnerDetailsForm learnerDetails, Errors errors) {
        if (StringUtils.isBlank(learnerDetails.getPassword())) {
            errors.rejectValue("password", "error.password.required","Password required");
//        KS - 2009-12-09 - LMS-3337
//        } else if (StringUtils.length(learnerDetails.getPassword()) < 8) {
//            errors.rejectValue("password", "error.password.invalidlength","Invalid length of password.");
//        }
        } else if ( !FieldsValidation.isPasswordCorrect(learnerDetails.getPassword()) ) {

        	errors.rejectValue("password", "error.password.invalidlength","	Password must contain alphabets and numbers and must be at least 8 characters long");
        }

        if (StringUtils.isBlank(learnerDetails.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "error.addNewLearner.confirmPassword.required","Confirm Password required");
        } else if (StringUtils.length(learnerDetails.getConfirmPassword()) < 8) {
            errors.rejectValue("confirmPassword", "error.addNewLearner.confirmPassword.invalidlength" , "Invalid length of Confirm password");
        }

        if (!StringUtils.equals(learnerDetails.getPassword(), learnerDetails.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "error.password.matchPassword","Password and confirm password do not match");
        }
    }

    public void validatePage2(LearnerDetailsForm learnerDetails, Errors errors){


		//TODO:


		String[] orgGroupsList = learnerDetails.getGroups();

		if (orgGroupsList !=null){
			if (orgGroupsList.length<=0){
				errors.rejectValue("selectedLearnerGroups", "error.password.nonSelected","");
			}
			//orgGroupsNameList.
			//orgGroupsList!=null && orgGroupsList.length>0
		}
		else{
			errors.rejectValue("selectedLearnerGroups", "error.org.nonSelected","");
		}


		/*String[] learnerGroupsList = learnerDetails.getSelectedLearnerGroups();
		if (learnerGroupsList !=null){
			if (learnerGroupsList.length<=0){
				errors.rejectValue("selectedLearnerGroups", "error.learner.nonSelected");
			}
			//orgGroupsNameList.
			//orgGroupsList!=null && orgGroupsList.length>0
		}
		else{
			errors.rejectValue("selectedLearnerGroups", "error.learner.nonSelected");
		}*/
	}

	
    public void validatePage3(LearnerDetailsForm learnerDetails, Errors errors){}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	/**
	 * @param LearnerService the LearnerService to set
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	
}
