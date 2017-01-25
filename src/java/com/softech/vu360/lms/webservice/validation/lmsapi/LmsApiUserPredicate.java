package com.softech.vu360.lms.webservice.validation.lmsapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address;
import com.softech.vu360.util.FieldsValidation;

public class LmsApiUserPredicate {

	public static boolean isValidUserName(String userName) {
		return isValid(userName);
	}
	
	public static boolean isInValidGlobalName(String name) {
		boolean invalidGlobalName = false;
		if (isValid(name)) {
			if (FieldsValidation.isInValidGlobalName(name)){
				invalidGlobalName = true;
			}
		}
		return invalidGlobalName;
	}
	
	public static boolean isValidPassword(String password) {
		return isValid(password);
	}
	
	public static boolean isValidPasswordPattern(String password) {
        return FieldsValidation.isPasswordCorrect(password);
	}
	
	public static boolean isValidEmailAddress(String emailAddress) {
		return isValid(emailAddress);
	}
	
	public static boolean isValidEmailAddressPattern(String emailAddress) {
		return FieldsValidation.isEmailValid(emailAddress);
	}
	
	public static boolean isValidFirstName(String firstName) {
		return isValid(firstName);
	}
	
	public static boolean isValidMiddleName(String middleName) {
		boolean valid = true;
		if (isValid(middleName)) {
			if (FieldsValidation.isInValidGlobalName(middleName)) {
				valid = false;
			}
		}
		return valid;
	}
	
	public static boolean isValidLastName(String lastName) {
		return isValid(lastName);
	}
	
	public static boolean isValidPhone(String phone) {
		boolean valid = true;
		if (isValid(phone)) {
			if (FieldsValidation.isInValidOffPhone((phone))){
				valid = false;
			}
		}
		return valid;
	}
	
	public static boolean isValidExtension(String extension) {
		boolean valid = true;
		if (isValid(extension)) {
			if (FieldsValidation.isInValidMobPhone((extension))){
				valid = false;
			}
		}
		return valid;
	}
	
	public static boolean isValidMobilePhone(String mobilePhone) {
		boolean valid = true;
		if (isValid(mobilePhone)) {
			if (FieldsValidation.isInValidMobPhone((mobilePhone))){
				valid = false;
			}
		}
		return valid;
	}
	
	public static boolean isValidStreetAddress1(Address address) {
		boolean valid = true;
		if (isValidAddress(address)) {
			String streetAddress1 = address.getStreetAddress1();
			if (isValid(streetAddress1)) {
				if ((FieldsValidation.isInValidAddress((streetAddress1)))){
					valid = false;
				}
			}
		}
		return valid;
	}
	
	public static boolean isValidStreetAddress2(Address address) {
		boolean valid = true;
		if (isValidAddress(address)) {
			String streetAddress2 = address.getStreetAddress2();
			if (isValid(streetAddress2)) {
				if ((FieldsValidation.isInValidAddress((streetAddress2)))){
					valid = false;
				}
			}
		}
		return valid;
	}
	

	public static boolean isValidCity(Address address) {
		boolean valid = true;
		if (isValidAddress(address)) {
			String city = address.getCity();
			if (isValid(city)) {
				if (isInValidGlobalName(city)){
					valid = false;
				}
			}
		}
		return valid;
	}
	
	public static boolean isValidCountryAndZipCode(Address address) {
		boolean valid = true;
		if (isValidAddress(address)) {
			String country = address.getCountry();
			String zipCode = address.getZipCode();
			if (isValid(country) && isValid(zipCode)) {
				if (!ZipCodeValidator.isZipCodeValid(country, zipCode, null, null) ) {
					valid = false;
				}
			}
		}
		return valid;
	}
	
	public static boolean isValidAccountExpirationDateFormat(String accountExpiryDate) {
		boolean valid = true;
		if (isValid(accountExpiryDate)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date expirationDate = null;
			try {
				expirationDate = formatter.parse(accountExpiryDate);
				if (!formatter.format(expirationDate).equals(accountExpiryDate)) {
					valid = false;
				}	
			} catch (ParseException e) {
				valid = false;
			}
		}
		return valid;
	}
	
	public static boolean isAccountExpirationDateBeforeTodayDate(String accountExpiryDate) {
		
		boolean valid = true;
		if (isValid(accountExpiryDate)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date expirationDate = null;
			Date todayDate = new Date();
			try {
				expirationDate = formatter.parse(accountExpiryDate);
				if( expirationDate.before(todayDate)) {
					valid = false;
				}
			} catch (ParseException e) {
				valid = false;
			}
		}
		
		return valid;
	}
	
	public static boolean isCustomerHasManager(VU360User manager) {
		return manager != null;
	}
	
	private static boolean isValid(String value) {
		return StringUtils.isNotEmpty(value) || StringUtils.isNotBlank(value);
	}
	
	private static boolean isValidAddress(Address address) {
		return address != null;
	}
	
}
