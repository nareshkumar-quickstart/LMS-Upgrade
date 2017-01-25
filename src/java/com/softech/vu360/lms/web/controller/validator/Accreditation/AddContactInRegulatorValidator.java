package com.softech.vu360.lms.web.controller.validator.Accreditation;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Contact;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Dyutiman
 *
 */
public class AddContactInRegulatorValidator implements Validator {

	private static final Logger log = Logger.getLogger(AddContactInRegulatorValidator.class.getName());

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return RegulatorForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		RegulatorForm form = (RegulatorForm)obj;
		validateFirstPage(form, errors);
	}

	// [2/14/2011] LMS-8993 :: Accreditation Mode > Edit Regulator: System is giving validation message although fields have been specified.
	public void validateFirstPage(RegulatorForm form, Errors errors) {

		Contact contact = form.getContact();
		errors.setNestedPath("contact");
		if( StringUtils.isBlank(contact.getFirstName()) ) {
			errors.rejectValue("firstName", "error.addRegulator.firstName");
		}
		/*if( StringUtils.isBlank(contact.getMiddleName()) ) {
			errors.rejectValue("middleName", "error.addRegulator.middleName");
		}*/
		if( StringUtils.isBlank(contact.getLastName()) ) {
			errors.rejectValue("lastName", "error.addRegulator.lastName");
		}
		if( StringUtils.isBlank(contact.getPhone()) ) {
			errors.rejectValue("phone", "error.addRegulator.phone");
		}
		/*if( StringUtils.isBlank(contact.getPhoneExt())) {
			errors.rejectValue("phoneExt", "error.addRegulator.phoneExt");
		}*/

		/*if( StringUtils.isBlank(contact.getWebsiteURL()) ) {
			errors.rejectValue("websiteURL", "error.addRegulator.websiteURL");
		}*/
		
		if(!FieldsValidation.isEmailValid(contact.getEmailAddress())){
			errors.setNestedPath("contact");
			errors.rejectValue("emailAddress", "error.addRegulator.email.invalidformat");
		}
//		else if(!FieldsValidation.isEmailValid(contact.getEmailAddress())){
//			errors.rejectValue("emailAddress", "error.addRegulator.email.invalidformat");
//		}
//		if( StringUtils.isNotBlank(contact.getTitle()) ) {
//			if( FieldsValidation.isInValidGlobalName(contact.getTitle()) ) {
//				errors.rejectValue("title", "error.addRegulator.title.invalidformat");
//			}
//		}
		errors.setNestedPath("");
		errors.setNestedPath("contact.address");
		if( StringUtils.isBlank(contact.getAddress().getStreetAddress()) ) {
			errors.rejectValue("streetAddress", "error.addRegulator.address");
		}
//		errors.setNestedPath("contact.address2");
//		if( StringUtils.isBlank(contact.getAddress2().getStreetAddress()) ) {
//			errors.rejectValue("streetAddress", "error.addRegulator.address");
//		} 
//		else if (FieldsValidation.isInValidGlobalName(contact.getAddress().getStreetAddress())){
//			errors.rejectValue("streetAddress", "error.addRegulator.streetAddress1.invalidText");
//		}
		/*if( contact.getAddress().getStreetAddress2() == null || 
				contact.getAddress().getStreetAddress2().isEmpty() ) {
			errors.rejectValue("streetAddress2", "error.addRegulator.contactAddress2");
		}else */
//		if ( !StringUtils.isBlank(contact.getAddress().getStreetAddress2()) ) {
//			if (FieldsValidation.isInValidGlobalName(contact.getAddress().getStreetAddress2())){
//				errors.rejectValue("streetAddress2", "error.addRegulator.streetAddress2.invalidText");
//			}
//		}
		errors.setNestedPath("contact.address");
		if( StringUtils.isBlank(contact.getAddress().getCity()) ) {
			errors.rejectValue("city", "error.addRegulator.contactCity");
		}
		
//		errors.setNestedPath("contact.address2");
//		if( StringUtils.isBlank(contact.getAddress2().getCity()) ) {
//			errors.rejectValue("city", "error.addRegulator.contactCity");
//		} 
//		else if ( FieldsValidation.isInValidGlobalName(contact.getAddress().getCity()) ) {
//			errors.rejectValue("city", "error.addRegulator.city.invalidText");
//		}
		errors.setNestedPath("contact.address");
		if( StringUtils.isBlank(contact.getAddress().getState()) ) {
			errors.rejectValue("state", "error.addRegulator.contactState");
		}
		
		if( StringUtils.isBlank(contact.getAddress().getZipcode())) {
			errors.setNestedPath("contact.address");
			errors.rejectValue("zipcode", "error.addRegulator.zip","");
		}
//		errors.setNestedPath("contact.address2");
//		if( StringUtils.isBlank(contact.getAddress2().getState()) ) {
//			errors.rejectValue("state", "error.addRegulator.contactState");
//		}
		/*
		 *  validation of zipcode will not be done if country is UAE 
		 */
		//errors.setNestedPath("contact.address");
		//validateZipCode(contact.getAddress(),errors,form);
		//errors.setNestedPath("contact.address2");
		//validateZipCode(contact.getAddress2(),errors,form);
		/*else if (!StringUtils.isNumeric(contact.getAddress().getZipcode())){
			errors.rejectValue("zipcode", "error.addRegulator.zipcode.invalidText");
		}*/
		errors.setNestedPath("contact.address");	
		if( StringUtils.isBlank(contact.getAddress().getCountry()) ) {
			errors.rejectValue("country", "error.addRegulator.contactCountry");
		}
//		errors.setNestedPath("contact.address2");	
//		if( StringUtils.isBlank(contact.getAddress2().getCountry()) ) {
//			errors.rejectValue("country", "error.addRegulator.contactCountry");
//		}
		errors.setNestedPath("");
	}
	
//	private void validateZipCode(Address address,Errors errors,RegulatorForm form){
//		if( StringUtils.isNotBlank(address.getCountry())&& 
//				!address.getCountry().equalsIgnoreCase("AE") &&
//				!address.getCountry().equalsIgnoreCase("United Arab Emirates") ) {
//
//			if( StringUtils.isBlank(address.getZipcode()) ) {
//				errors.rejectValue("zipcode", "error.addRegulator.contactZip");
//			} else {
//
//				if( form.getBrander() != null ) {
//
//					String country = address.getCountry();
//					String zipCode = address.getZipcode();
//
//					if( ! ZipCodeValidator.isZipCodeValid(country, zipCode, form.getBrander(), log) ) {
//						log.debug("ZIP CODE FAILED" );
//						errors.rejectValue("zipcode", ZipCodeValidator.getCountryZipCodeError(
//								address.getCountry(), form.getBrander()), "");
//					}				
//				}
//			}
//		}
//	}
	
}