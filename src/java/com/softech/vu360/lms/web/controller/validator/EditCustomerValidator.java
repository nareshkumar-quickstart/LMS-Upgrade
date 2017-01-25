package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.web.controller.model.EditCustomerForm;
import com.softech.vu360.lms.web.controller.model.ManageSurveyForm;
import com.softech.vu360.util.FieldsValidation;

public class EditCustomerValidator implements Validator {
	private static final Logger log = Logger.getLogger(ManageSurveyValidator.class.getName());
	private CustomerService  customerService;
	public boolean supports(Class clazz) {
		return ManageSurveyForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		EditCustomerForm editCustomerForm = (EditCustomerForm)obj;
		validateProfilePage(editCustomerForm,errors);
	}

	public void validateProfilePage(EditCustomerForm form, Errors errors) {
	
		if (form.getEventSource().equalsIgnoreCase("donotValidate"))
			return ; // if country is changed , then no need to validate
		
		if (StringUtils.isBlank(form.getName())) {
			errors.rejectValue("name", "error.administrator.addNewCustomer.required");
		} else if (FieldsValidation.isInValidCustomerName(form.getName())){
			errors.rejectValue("name", "error.administrator.addNewCustomer.customerName.invalid");
		}
		
		if (StringUtils.isBlank(form.getFirstName())) {
			errors.rejectValue("firstName", "error.administrator.addNewCustomer.firstName.required");
		} else if (FieldsValidation.isInValidGlobalName(form.getFirstName())){
			errors.rejectValue("firstName", "error.administrator.addNewCustomer.firstName.invalid");
		}
		
		if (StringUtils.isBlank(form.getLastName())) {
			errors.rejectValue("lastName", "error.administrator.addNewCustomer.lastName.required");
		} else if (FieldsValidation.isInValidGlobalName(form.getLastName())){
			errors.rejectValue("lastName", "error.administrator.addNewCustomer.lastName.invalid");
		}
		
		String country = null ;
		String zipCode = null ;
		 
		if(form.getBrander() != null)	{
			// -----------------------------------------------------------------------------
			// 			for learner address 1 Zip Code   									//
			// -----------------------------------------------------------------------------
			
			country = form.getBillingCountry();
			zipCode = form.getBillingZip();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, form.getBrander(), log) ) {
            	log.debug("ZIP CODE FAILED" );
            	errors.rejectValue("billingZip", ZipCodeValidator.getCountryZipCodeError(form.getBillingCountry(),form.getBrander()),"");
            }				
		
			// -----------------------------------------------------------------------------
			// 			for learner address 2 Zip Code   									//
			// -----------------------------------------------------------------------------
			country = form.getShippingCountry();
			zipCode = form.getShippingZip();

            if( ! ZipCodeValidator.isZipCodeValid(country, zipCode,  form.getBrander(), log) ) {
            	log.debug("ZIP CODE FAILED" );
            	errors.rejectValue("shippingZip", ZipCodeValidator.getCountryZipCodeError(form.getShippingCountry(), form.getBrander()),"");
            }				

			
		}
		
		//TODO will implement after new DAO written comment on 02/04/09.
		/*if (!StringUtils.isBlank(form.getEmailAddress())){
			if (!(FieldsValidation.isEmailValid(form.getEmailAddress()))){
				errors.rejectValue("emailAddress", "error.administrator.addNewCustomer.email.invalid");
			}else if((customerService.findCustomersByEmailAddess(form.getEmailAddress())).size()>0) {
				List<Customer> custom=customerService.findCustomersByEmailAddess(form.getEmailAddress());
				
				
				for(Customer customerr:custom){
					if(customerr.getId()!=(form.getCustomer().getId())){
						errors.rejectValue("emailAddress", "error.administrator.addNewCustomer.email.exists");
					}
					
				}
			} 
		}*/
		//TODO will implement after new DAO written comment on 02/04/09.
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
}