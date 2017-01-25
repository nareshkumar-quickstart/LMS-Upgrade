/**
 * 
 */
package com.softech.vu360.lms.web.controller.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.model.AddCustomerForm;
import com.softech.vu360.lms.web.controller.model.AddDistributors;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Tapas Mondal
 *
 */
public class AddCustomerValidator  implements Validator{

	private static final Logger log = Logger.getLogger(ManageSurveyValidator.class.getName());
	private CustomerService  customerService = null;
	private VU360UserService vu360UserService;
	private ActiveDirectoryService activeDirectoryService;

	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}
	
	public boolean supports(Class clazz) {
		return AddCustomerForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		AddCustomerForm addCustomerForm = (AddCustomerForm)obj;
		validatePage1(addCustomerForm,errors);
		validatePage2(addCustomerForm,errors);
	}
	
	public void validatePage1(AddCustomerForm form, Errors errors) {
		List<AddDistributors> distributor = form.getDistributors();
		int flag = 0;
		for (int i=0;i<distributor.size();i++) 
			if(distributor.get(i).isSelected() == true) {
				flag = 1;
				break;
			}
		if (flag == 0){
			errors.rejectValue("distributors", "error.distributorName.required");
		}
	}

	public void validatePage2(AddCustomerForm form, Errors errors) {
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (StringUtils.isBlank(form.getCustomerName())){
			errors.rejectValue("customerName", "error.administrator.addNewCustomer.required");
		}
		 if (FieldsValidation.isInValidCustomerName(form.getCustomerName())){
			errors.rejectValue("customerName", "error.customerName.invalid");
		}

		if (StringUtils.isBlank(form.getFirstName())) {
			errors.rejectValue("firstName", "error.administrator.addNewCustomer.firstName.required");
		} else if (FieldsValidation.isInValidGlobalName(form.getFirstName())) {
			errors.rejectValue("firstName", "error.administrator.addNewCustomer.firstName.invalid");
		}


		if (StringUtils.isBlank(form.getLastName())) {
			errors.rejectValue("lastName", "error.administrator.addNewCustomer.lastName.required");
		} else if (FieldsValidation.isInValidGlobalName(form.getLastName())){
			errors.rejectValue("lastName", "error.administrator.addNewCustomer.lastName.invalid");
		}
		//TODO will implement after new DAO written comment on 02/04/09.
		/*if (!StringUtils.isBlank(form.getEmailAdd())){
			if (!(FieldsValidation.isEmailValid(form.getEmailAdd()))){
				errors.rejectValue("emailAdd", "error.administrator.addNewCustomer.email.invalid");
			}else if((customerService.findCustomersByEmailAddess(form.getEmailAdd())).size()>0) {
				errors.rejectValue("emailAdd", "error.administrator.addNewCustomer.email.exists");
			} 
		}*/
		//TODO will implement after new DAO written comment on 02/04/09.
		
		
		

		if (!StringUtils.isBlank(form.getExt())){
			if (FieldsValidation.isInValidMobPhone(form.getExt())){
				errors.rejectValue("ext", "error.administrator.addNewCustomer.ext.invalid");
			}
		}

		//No validation required on Telephone number field: LMS-7582/ENGSUP-12061
		/*if (!StringUtils.isBlank(form.getPhone())){
			if (FieldsValidation.isInValidOffPhone(form.getPhone())){
				errors.rejectValue("phone", "error.administrator.addNewCustomer.phone.invalid");
			}
		}*/
/*	
		if (!StringUtils.isBlank(form.getZip1())){
			if (StringUtils.length(form.getZip1())>5){
				errors.rejectValue("zip1", "error.administrator.addNewCustomer.zip1.invalidlength");
			}
			else if (!StringUtils.isNumeric(form.getZip1())){
				errors.rejectValue("zip1", "error.administrator.addNewCustomer.zip1.invalid");
			}
		}
		
		if (!StringUtils.isBlank(form.getZip2())){
			if (StringUtils.length(form.getZip2())>5){
				errors.rejectValue("zip2", "error.administrator.addNewCustomer.zip2.invalidlength");
			}
			else if (!StringUtils.isNumeric(form.getZip2())){
				errors.rejectValue("zip2", "error.administrator.addNewCustomer.zip2.invalid");
			}
		}
*/

		form.setZip1(form.getZip1().trim());
		form.setZip2(form.getZip2().trim());
		
		
		if( form.getBrander() != null)	{
			String country = null ;
			String zipCode = null ;
			// -----------------------------------------------------------------------------
			// 			for learner address 1 Zip Code   									//
			// -----------------------------------------------------------------------------
			
			country = form.getCountry1();
			zipCode = form.getZip1();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, form.getBrander(), log) ) {
            	log.debug("ZIP CODE FAILED" );
            	errors.rejectValue("zip1", ZipCodeValidator.getCountryZipCodeError(form.getCountry1(), form.getBrander()),"");
            }				
		
			// -----------------------------------------------------------------------------
			// 			for learner address 2 Zip Code   									//
			// -----------------------------------------------------------------------------
			country = form.getCountry2();
			zipCode = form.getZip2();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, form.getBrander(), log) ) {
            	log.debug("ZIP CODE FAILED" );
            	errors.rejectValue("zip2", ZipCodeValidator.getCountryZipCodeError(form.getCountry2(), form.getBrander()),"");
            }	
			
		}

		
		
		if (!StringUtils.isBlank(form.getAddress1())){
			if ((FieldsValidation.isInValidAddress((form.getAddress1())))){
				errors.rejectValue("address1", "error.administrator.addNewCustomer.streetAddress1.all.invalidText");
			}
		}
		
		if (!StringUtils.isBlank(form.getAddress1a())){
			if ((FieldsValidation.isInValidAddress((form.getAddress1a())))){
				errors.rejectValue("address1a", "error.administrator.addNewCustomer.streetAddress1.all.invalidText");
			}
		}
		
		if (!StringUtils.isBlank(form.getAddress2())){
			if ((FieldsValidation.isInValidAddress((form.getAddress2())))){
				errors.rejectValue("address2", "error.administrator.addNewCustomer.streetAddress2.all.invalidText");
			}
		}
		
		if (!StringUtils.isBlank(form.getAddress2a())){
			if ((FieldsValidation.isInValidAddress((form.getAddress2a())))){
				errors.rejectValue("address2a", "error.administrator.addNewCustomer.streetAddress2.all.invalidText");
			}
		}

		if (!StringUtils.isBlank(form.getCity1())){
			if (FieldsValidation.isInValidGlobalName(form.getCity1())){
				errors.rejectValue("city1", "error.administrator.addNewCustomer.city1.invalid");
			}
		}
		
		if (!StringUtils.isBlank(form.getCity2())){
			if (FieldsValidation.isInValidGlobalName(form.getCity2())){
				errors.rejectValue("city2", "error.administrator.addNewCustomer.city2.invalid");
			}
		}
		
		if (StringUtils.isBlank(form.getLoginEmailID())) {
			errors.rejectValue("loginEmailID", "error.addNewLearner.email.required");
		} else if (!FieldsValidation.isEmailValid(form.getLoginEmailID())) {
			errors.rejectValue("loginEmailID", "error.addNewLearner.email.invalidformat");
		}if (vu360UserService.countUserByEmailAddress(form.getLoginEmailID()) != 0) {
			log.debug("Size of list");
			errors.rejectValue("loginEmailID", "error.addNewLearner.email.existEmail");
		}else if (activeDirectoryService.findADUser(form.getLoginEmailID()) ){
			errors.rejectValue("loginEmailID", "error.addNewUser.AD.existEmail");
		}
		
		if (StringUtils.isBlank(form.getPassword())) {
			errors.rejectValue("password", "error.password.required");
//	    KS - 2009-12-09 - LMS-3630
//		} else if (StringUtils.length(form.getPassword())<8) {
//			errors.rejectValue("password", "error.password.invalidlength");
//		}
        } else if ( !FieldsValidation.isPasswordCorrect(form.getPassword()) ) {
			errors.rejectValue("password", "error.password.invalidlength");
		}

		if (StringUtils.isBlank(form.getMatchPassword())) {
			errors.rejectValue("confirmPassword", "error.addNewLearner.confirmPassword.required");
		} else if (StringUtils.length(form.getMatchPassword())<8) {
			errors.rejectValue("confirmPassword", "error.addNewLearner.confirmPassword.invalidlength");
		} else if (!form.getMatchPassword().equals(form.getPassword())){
				errors.rejectValue("matchPassword", "error.password.matchPassword");
		}
		if (!StringUtils.isBlank(form.getExpirationDate())){
			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date expirationDate = null;
			Date date = new Date();
			try {
				expirationDate = formatter.parse(form.getExpirationDate());
				if (!formatter.format(expirationDate).equals(form.getExpirationDate())) {
					errors.rejectValue("expirationDate", "error.addNewLearner.expDate.invalidDate");

				}else {
					if( expirationDate.before(date)  ) {
						errors.rejectValue("expirationDate", "error.addNewLearner.expDate.invalidDate");
					}
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
				errors.rejectValue("expirationDate", "error.addNewLearner.expDate.invalidDate");
			}
			
		}
	}


	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

}