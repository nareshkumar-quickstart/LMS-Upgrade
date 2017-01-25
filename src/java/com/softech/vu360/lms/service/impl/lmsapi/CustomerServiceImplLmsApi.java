package com.softech.vu360.lms.service.impl.lmsapi;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorPreferences;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.service.BrandService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.lmsapi.CustomerServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.LmsApiCustomerService;
import com.softech.vu360.lms.web.controller.model.AddCustomerForm;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Company;
import com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Manager;
import com.softech.vu360.lms.webservice.message.lmsapi.types.customer.RegisterCustomer;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.VU360Branding;

public class CustomerServiceImplLmsApi implements CustomerServiceLmsApi {

	private static final Logger log = Logger.getLogger(CustomerServiceImplLmsApi.class.getName());
	
	private LmsApiCustomerService lmsApiCustomerService;
	private VU360UserService vu360UserService;
	private CustomerService customerService;
	private BrandService brandService;
	
	public LmsApiCustomerService getLmsApiCustomerService() {
		return lmsApiCustomerService;
	}
	
	public void setLmsApiCustomerService(LmsApiCustomerService lmsApiCustomerService) {
		this.lmsApiCustomerService = lmsApiCustomerService;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public BrandService getBrandService() {
		return brandService;
	}

	public void setBrandService(BrandService brandService) {
		this.brandService = brandService;
	}

	public LmsApiCustomer findLmsApiCustomerByKey(String apiKey) throws Exception {
		
		LmsApiCustomer lmsApiCustomer = lmsApiCustomerService.findApiKey(apiKey);
		return lmsApiCustomer;
	}
	
	public Map<String, Object> validateAddCustomerRequest(List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer> customerList) throws Exception {
		
		Map<String, Object> customersMap = new HashMap<String, Object>();
		List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer> validCustomerList = new ArrayList<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer>();
		Map<String, String> invalidCustomerMap = new LinkedHashMap<String, String>();
		
		for (com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer: customerList) {
			try {
				String userName = customer.getUserName();
				VU360User existingUser = vu360UserService.findUserByUserName(userName);
				if (existingUser != null) {
					String error = "user name already exists";
					throwException(error);
				}
				customerValidation(customer);
				validCustomerList.add(customer);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				String userName = customer.getUserName();
				invalidCustomerMap.put(userName, errorMessage);
			}
		}
		
		if (!validCustomerList.isEmpty()) {
			customersMap.put("validCustomerList", validCustomerList);
		}
		
		if (!invalidCustomerMap.isEmpty()) {
			customersMap.put("invalidCustomerMap", invalidCustomerMap);
		}
		
		return customersMap;
		
	}
	
	public Map<String, Object> validateUpdateCustomerRequest(List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer> customerList) throws Exception {
		
		Map<String, Object> customersMap = new HashMap<String, Object>();
		List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer> validCustomerList = new ArrayList<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer>();
		Map<String, String> invalidCustomerMap = new LinkedHashMap<String, String>();
		
		for (com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer: customerList) {
			try {
				String userName = customer.getUserName();
				VU360User existingUser = vu360UserService.findUserByUserName(userName);
				if (existingUser == null) {
					String error = "Unable to update. Username not found: " + userName;
					throwException(error);
				}
				customerValidation(customer);
				validCustomerList.add(customer);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				String userName = customer.getUserName();
				invalidCustomerMap.put(userName, errorMessage);
			}
		}
		
		if (!validCustomerList.isEmpty()) {
			customersMap.put("validCustomerList", validCustomerList);
		}
		
		if (!invalidCustomerMap.isEmpty()) {
			customersMap.put("invalidCustomerMap", invalidCustomerMap);
		}
		
		return customersMap;
		
	}
	
	public boolean customerApiValidation(LmsApiCustomer lmsApiCustomer, String customerCode) throws Exception {
		
		String errorMessage = null;
		
		if (lmsApiCustomer == null) {
			errorMessage = "No Api Key found. Unauthorized Access";
			throwException(errorMessage);
		}
		
		Customer customer = lmsApiCustomer.getCustomer();
		
		if (customer == null) {
			errorMessage = "No Customer found: " + customerCode;
			throwException(errorMessage);
		}
		
		if (!(customer.getCustomerCode().equals(customerCode))){
			errorMessage = "Invalid customer code: " + customerCode;
			throwException(errorMessage);
		}
		
		boolean isApiEnabled = customer.getLmsApiEnabledTF();
		if (!isApiEnabled) {
			errorMessage = "LMS API is not enable for customer: " + customerCode;
			log.debug(errorMessage);
			throwException(errorMessage);
		}
		
		return true;
	}
	
	public boolean customerValidation(com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer) throws Exception {
		
		String error = null;
		String accountExpiryDate = null;
		//Brander brander = getBrander(null, null);
		Company company = customer.getCompany();
		Manager manager = customer.getManager();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = customer.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = customer.getAlternateAddress();
		String userName = customer.getUserName();
		String password = customer.getPassword();
		
		XMLGregorianCalendar accountExpirationDate = customer.getAccountExpirationDate();
		if (accountExpirationDate != null) {
			accountExpiryDate = accountExpirationDate.toString();
		}
		
		if (company == null) {
			error = "company element is required";
			throwException(error);
		}
	
		if (manager == null) {
			error = "manager element is required";
			throwException(error);
		}
	
		String companyName = company.getName();
		String managerFirstName = manager.getManagerFirstName();
		String managerLastName = manager.getManagerLastName();
		String extension = manager.getExtension();
		
		if (StringUtils.isEmpty(companyName) || StringUtils.isBlank(companyName)){
			error = "Company name required";
			throwException(error);
		} else if (FieldsValidation.isInValidCustomerName(companyName)){
			error = "Invalid company name";
			throwException(error);
		}
		
		nameValidation(managerFirstName, null, managerLastName);
		emailValidation(userName);
		
		//VU360User existingUser = vu360UserService.findUserByUserName(userName);
		//if (existingUser != null) {
			//error = "user name already exists";
			//throwException(error);
		//}
		
		passwordValidation(password);
		extensionValidation(extension);
		addressValidation(address1);
		addressValidation(alternateAddress);
		accountExpirationDateValidation(accountExpiryDate);
		
		 return true;
	}
	
	private boolean addressValidation(com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address) throws Exception {
		
		if (address != null) {
			
			String error = null;
			Brander brander = getBrander(null, null);
			String country = address.getCountry();
			String zipCode = address.getZipCode();
			String streetAddress1 = address.getStreetAddress1();
			String streetAddress2 = address.getStreetAddress2();
			String city = address.getCity();
			String state = address.getState();
			
			if (StringUtils.isNotEmpty(streetAddress1) && StringUtils.isNotBlank(streetAddress1)){
				if ((FieldsValidation.isInValidAddress((streetAddress1)))){
					error = "Bad characters not allowed (Street Address1)";
					throwException(error);
				}
			}
			
			if (StringUtils.isNotEmpty(streetAddress2) && StringUtils.isNotBlank(streetAddress2)){
				if ((FieldsValidation.isInValidAddress((streetAddress2)))){
					error = "Bad characters not allowed (Street Address2)";
					throwException(error);
				}
			}
			
			if (StringUtils.isNotEmpty(country) && StringUtils.isNotBlank(country) && StringUtils.isNotEmpty(zipCode) && StringUtils.isNotBlank(zipCode)) {
				
				//	for learner address 1 Zip Code
				if (!ZipCodeValidator.isZipCodeValid(country, zipCode, brander, log) ) {
		        	log.debug("ZIP CODE FAILED" );
		        	error = ZipCodeValidator.getCountryZipCodeError(country, brander);
		        	if (error == "") {
		        		error = "ZIP CODE FAILED";
		        	}
		        	throwException(error);
		        }	
			}
			
			if (StringUtils.isNotEmpty(city) && StringUtils.isNotBlank(city)){
				if (FieldsValidation.isInValidGlobalName(city)){
					error = "Bad characters not allowed (City)";
					throwException(error);
				}
			}	
		} //end of if (address != null)
		
		return true;
		
	}
	
	private boolean accountExpirationDateValidation(String accountExpiryDate) throws Exception {
		
		if (StringUtils.isNotEmpty(accountExpiryDate) && StringUtils.isNotBlank(accountExpiryDate)){
			String error = null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date expirationDate = null;
			Date todayDate = new Date();
			try {
				expirationDate = formatter.parse(accountExpiryDate);
				if (!formatter.format(expirationDate).equals(accountExpiryDate)) {
					error = "Invalid Date";
					throwException(error);
				}else {
					if( expirationDate.before(todayDate)  ) {
						error = "Invalid Date";
						throwException(error);
					}
				}	
			} catch (ParseException e) {
				e.printStackTrace();
				error = e.getMessage() + ". Provide date in yyyy-MM-dd format";
				throwException(error);
			}	
		}
		
		return true;
	}
	
	private boolean emailValidation(String emailAddress) throws Exception {
		String error = null;
		if (StringUtils.isEmpty(emailAddress) || StringUtils.isBlank(emailAddress)) {
			error = "Email address required";
			throwException(error);
		} else if (!FieldsValidation.isEmailValid(emailAddress)){
			error = "Invalid email address";
			throwException(error);
		}
		return true;
	}
	
	private boolean passwordValidation(String password) throws Exception {
		String error = null;
		if (StringUtils.isEmpty(password) || StringUtils.isBlank(password)) {
			error = "Password required";
			throwException(error);
        } else if ( !FieldsValidation.isPasswordCorrect(password) ) {
        	error = "Password must contain alphabets and numbers and must be at least 8 characters long";
        	throwException(error);
        }
		return true;
	}
	
	private boolean nameValidation(String firstName, String middleName, String lastName) throws Exception {
		
		String error = null;
		if (StringUtils.isEmpty(firstName) || StringUtils.isBlank(firstName)) {
			error = "First Name required";
			throwException(error);
		} 
		else if (FieldsValidation.isInValidGlobalName(firstName)){
			error = "Bad characters not allowed (First name)";
			throwException(error);
		}
		
		if (StringUtils.isNotEmpty(middleName) || StringUtils.isNotBlank(middleName)){
			if (FieldsValidation.isInValidGlobalName(middleName)){
				error = "Bad characters not allowed (Middle name)";
				throwException(error);
			}
		}

		if (StringUtils.isEmpty(lastName) || StringUtils.isBlank(lastName)){
			error = "Last Name required";
			throwException(error);
		}
		else if (FieldsValidation.isInValidGlobalName(lastName)){
			error = "Bad characters not allowed (Last name)";
			throwException(error);
		}
		
		return true;
	}
	
	private boolean extensionValidation(String phoneExtension) throws Exception {
		String error = null;
		if (StringUtils.isNotEmpty(phoneExtension) || StringUtils.isNotBlank(phoneExtension)){
			if (FieldsValidation.isInValidMobPhone((phoneExtension))){
				error = "Bad characters not allowed (Phone Extension)";
				throwException(error);
			}
		}
		return true;
	}
	
	private static Brander getBrander(String brandName, com.softech.vu360.lms.vo.Language language ) throws Exception {
		
		if (StringUtils.isEmpty(brandName) && StringUtils.isBlank(brandName)) {
			brandName = "default";
		}
		
		if (language == null) {
			language = new com.softech.vu360.lms.vo.Language();
			language.setLanguage(Language.DEFAULT_LANG);
		}
		Brander brander = VU360Branding.getInstance().getBrander(brandName, language);
		return brander;
	}
	
	private Customer createNewCustomer(Distributor distributor, com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer) throws Exception {
		
		AddCustomerForm addCustomerForm = new AddCustomerForm();
		
		// By default customer preferences are copied from re seller's.
		setResellerPreferencesToAddCustomerForm(addCustomerForm, distributor);
		
		boolean accountStatus = false;
		Company company = customer.getCompany();
		Manager manager = customer.getManager();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = customer.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = customer.getAlternateAddress();
		String companyName = company.getName();
		String managerFirstName = manager.getManagerFirstName();
		String managerLastName = manager.getManagerLastName();
		String companyWebSite = company.getWebsite();
		String emailAddress = company.getEmail();
		String userName = customer.getUserName();
		String managerPhone = manager.getManagerPhone();
		String extension = manager.getExtension();
		String customerType = customer.getCustomerType().value();
		String companyAccountStatus = company.getAccountStatus().value();
		String password = customer.getPassword();
		boolean isAccountExpired = customer.isAccountExpired();
		boolean isAccountLocked = customer.isAccountLocked();
		boolean isChangePasswordOnNextLogin = customer.isChangePasswordOnNextLogin();
		boolean isAccountDisabled = customer.isAccountDisabled();
		boolean isVisibleOnReport = customer.isVisibleOnReport();
		
		BigInteger brandId = customer.getBrandId();
		if (brandId != null) {
			long branderId = brandId.longValue();
			Brand brand = brandService.getBrandById(branderId);
			if (brand == null) {
				addCustomerForm.setBrandId(0);
			} else {
				addCustomerForm.setBrandId(branderId);
			}
			
		} else {
			brandId = new BigInteger("0");
			long branderId = brandId.longValue();
			addCustomerForm.setBrandId(branderId);
		}
		
		if (address1 != null) {
			setAddressToAddCustomerForm(addCustomerForm, address1, true);
		} else {
			address1 = getEmptyAddress();
			setAddressToAddCustomerForm(addCustomerForm, address1, true);
		}
		
		if (alternateAddress != null) {
			setAddressToAddCustomerForm(addCustomerForm, alternateAddress, false);
		} else {
			alternateAddress = getEmptyAddress();
			setAddressToAddCustomerForm(addCustomerForm, alternateAddress, false);
		}
		
		String accountExpiryDate = null;
		/**
		 * We have date in ("yyyy-MM-dd") format but in the CustomerServiceImpl.java it requires date in ("MM/dd/yyyy") format.
		 * Here we are making date in the desired format by splitting date string.
		 */
		XMLGregorianCalendar accountExpirationDate = customer.getAccountExpirationDate();
		if (accountExpirationDate != null) {
			String expiryDate = accountExpirationDate.toString();
			if (expiryDate.indexOf("-") != -1) {
				String[] dateArray = expiryDate.split("-");
				String year = dateArray[0];
				String month = dateArray[1];
				String day = dateArray[2];
				accountExpiryDate = month + "/" + day + "/" + year;
			}
		}
		
		if (companyAccountStatus != null && companyAccountStatus.equalsIgnoreCase("Active")) {
			accountStatus = true;
		}
		//LMS-15930
		if (emailAddress == null) {
			emailAddress= "";
		}
		
		addCustomerForm.setCustomerName(companyName);
		addCustomerForm.setFirstName(managerFirstName);
		addCustomerForm.setLastName(managerLastName);
		addCustomerForm.setWesiteURL(companyWebSite);
		addCustomerForm.setEmailAdd(emailAddress);
		addCustomerForm.setPhone(managerPhone);
		addCustomerForm.setExt(extension);
		addCustomerForm.setCustomerType(customerType);
		addCustomerForm.setStatus(accountStatus);
	
		// In API we are not currently offering self authoring so it is false. If we offer self authoring then we must provide
		// VU360User that has admin rights
		addCustomerForm.setSelfAuthor(false);
		addCustomerForm.setLoginEmailID(userName);
		addCustomerForm.setPassword(password);
		addCustomerForm.setExpired(isAccountExpired);
		addCustomerForm.setLocked(isAccountLocked);
		addCustomerForm.setChangePassword(isChangePasswordOnNextLogin);
		addCustomerForm.setDisabled(isAccountDisabled);
		addCustomerForm.setReport(isVisibleOnReport);
		addCustomerForm.setExpirationDate(accountExpiryDate);
		
		//VU360User user = null;
		Long userId=null;
		Customer registeredCustomer = customerService.addCustomer(userId, distributor, addCustomerForm);
		return registeredCustomer;
		
	}

	private void setResellerPreferencesToAddCustomerForm(AddCustomerForm addcustomerForm, Distributor distributor) throws Exception {
		DistributorPreferences dp = distributor.getDistributorPreferences();
		if( dp != null ) {
			addcustomerForm.setAudio(dp.isAudioEnabled());
			addcustomerForm.setAudioLocked(dp.isAudioLocked());
			addcustomerForm.setCaptioning(dp.isCaptioningEnabled());
			addcustomerForm.setCaptioningLocked(dp.isCaptioningLocked());
			if( dp.getBandwidth().equalsIgnoreCase("high") ) {
				addcustomerForm.setBandwidth(true);
			}	
			if( dp.getBandwidth().equalsIgnoreCase("low") ) {
				addcustomerForm.setBandwidth(false);
			}
			addcustomerForm.setBandwidthLocked(dp.isBandwidthLocked());
			addcustomerForm.setVideo(dp.isVedioEnabled());
			addcustomerForm.setVideoLocked(dp.isVideoLocked());
			addcustomerForm.setRegistrationEmails(dp.isEnableRegistrationEmailsForNewCustomers());
			addcustomerForm.setRegistrationEmailsLocked(dp.isRegistrationEmailLocked());
			addcustomerForm.setEnrollmentEmails(dp.isEnableEnrollmentEmailsForNewCustomers());
			addcustomerForm.setEnrollmentEmailsLocked(dp.isEnrollmentEmailLocked());
			addcustomerForm.setCourseCompCertificateEmails(dp.isCourseCompletionCertificateEmailEnabled() );
			addcustomerForm.setCourseCompCertificateEmailsLocked(dp.isCourseCompletionCertificateEmailLocked());
		}
	}
	
	private String generateApiKey() {
		return GUIDGeneratorUtil.generateGUID();
		//return "1234";
	}
	
	private void setAddressToAddCustomerForm(AddCustomerForm addCustomerForm, com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address, boolean isAddress1) {
		String streetAddress1 = address.getStreetAddress1();
		String streetAddress2 = address.getStreetAddress2();
		String city = address.getCity();
		String country = address.getCountry();
		String state = address.getState();
		String zipCode = address.getZipCode();
		
		if (isAddress1) {
			addCustomerForm.setAddress1(streetAddress1);
			addCustomerForm.setAddress1a(streetAddress2);
			addCustomerForm.setCity1(city);
			addCustomerForm.setCountry1(country);
			addCustomerForm.setState1(state);
			addCustomerForm.setZip1(zipCode);
			addCustomerForm.setCountryLabel1(country);
			
		} else {
			addCustomerForm.setAddress2(streetAddress1);
			addCustomerForm.setAddress2a(streetAddress2);
			addCustomerForm.setCity2(city);
			addCustomerForm.setCountry2(country);
			addCustomerForm.setState2(state);
			addCustomerForm.setZip2(zipCode);
			addCustomerForm.setCountryLabel2(country);
		}
	}
	
	private com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address getEmptyAddress() {
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address = new com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address();
		address.setCity("");
		address.setCountry("");
		address.setState("");
		address.setStreetAddress1("");
		address.setStreetAddress2("");
		address.setZipCode("");
		return address;
	}
	
	public Map<String, Object> processCustomersMap(Distributor distributor, Map<String, Object> customersMap) throws Exception {
		
		Object customersList = customersMap.get("validCustomerList");
		Object customersErrorMap = customersMap.get("invalidCustomerMap");
		Map<String, String> invalidCustomerMap = null;
		Map<String, Object> customersResultMap = new HashMap<String, Object>();
		if (customersErrorMap != null) {
			invalidCustomerMap = (Map<String, String>) customersErrorMap;
		}
		
		if (customersList != null) {
			List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer> validCustomerList = (List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer>)customersList;
			customersResultMap = processCustomersCreation(distributor, validCustomerList);
			if (invalidCustomerMap != null) {
				customersResultMap.put("invalidCustomerMap", invalidCustomerMap);
			}
		}
		
		if (invalidCustomerMap != null) {
			customersResultMap.put("invalidCustomerMap", invalidCustomerMap);
		}
		
		return customersResultMap;
		
	}
	
	public Map<String, Object> processCustomersCreation(Distributor distributor, List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer> customerList) throws Exception {
		
		Map<String, Object> customersResultMap = new HashMap<String, Object>();
		Map<String, String> customersErrorMap = new LinkedHashMap<String, String>();
		Map<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer, Object> customersMap = new LinkedHashMap<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer, Object>();
		
		for (com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer: customerList) {
			try {
				Customer newCustomer = createNewCustomer(distributor, customer);
				boolean apiEnabled = customer.isApiEnabled();
				if (apiEnabled) {
					newCustomer.setLmsApiEnabledTF(apiEnabled);
					newCustomer = customerService.updateCustomer(newCustomer);
					LmsApiCustomer lmsApiCustomer = addCustomerToLmsApi(newCustomer);
					customersMap.put(customer, lmsApiCustomer);
				} else {
					customersMap.put(customer, newCustomer);
				}
				
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				String userName = customer.getUserName();
				customersErrorMap.put(userName, errorMessage);
			}
		}
		
		if (!customersMap.isEmpty()) {
			customersResultMap.put("customersMap", customersMap);
		}
		
		if (!customersErrorMap.isEmpty()) {
			customersResultMap.put("customersErrorMap", customersErrorMap);
		}
		
		return customersResultMap;
	}
	
	private LmsApiCustomer addCustomerToLmsApi(Customer customer) throws Exception {
		
		String environment = "Development";
		String privilegeType = null;
		Map<String, String> privilegeMap = new HashMap<String, String>();
		privilegeMap.put("type", privilegeType);
	   
	    JSONObject json = new JSONObject();
	    json.accumulateAll(privilegeMap);
	     
	    String generatedApiKey = generateApiKey();
	    //String privilege = json.toString();
	     
	    String privilege = null;
		
		LmsApiCustomer lmsApiCustomer = addLmsApiCustomer(customer, generatedApiKey, environment, privilege);
		return lmsApiCustomer;
		
	}
	
	private LmsApiCustomer addLmsApiCustomer(Customer customer, String apiKey, String environment, String privilege) throws Exception {
		
		LmsApiCustomer lmsApiCustomer = new LmsApiCustomer();
		lmsApiCustomer.setCustomer(customer);
		lmsApiCustomer.setApiKey(apiKey);
		lmsApiCustomer.setEnvironment(environment);
		lmsApiCustomer.setPrivilege(privilege);
		
		LmsApiCustomer newLmsApiCustomer = lmsApiCustomerService.addLmsApiCustomer(lmsApiCustomer);
		return newLmsApiCustomer;
	}
	
	public List<RegisterCustomer> getRegisterCustomerList(Map<String, Object> customersResultMap) throws Exception {
		
		List<RegisterCustomer> registerCustomerList = new ArrayList<RegisterCustomer>();
		
		Object customers = customersResultMap.get("customersMap");
		Object customersError = customersResultMap.get("customersErrorMap");
		Object invalidCustomers = customersResultMap.get("invalidCustomerMap");
		
		if (customers != null) {
			Map<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer, Object> customersMap = (Map<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer, Object>)customers;
			List<RegisterCustomer> registerList = processCustomersMap(customersMap);
			registerCustomerList.addAll(registerList);
		}
		
		if (customersError != null) {
			Map<String, String> customersErrorMap = (Map<String, String>)customersError;
			List<RegisterCustomer> registerList = processCustomersErrorMap(customersErrorMap);
			registerCustomerList.addAll(registerList);
		}

		if (invalidCustomers != null) {
			Map<String, String> invalidCustomerMap = (Map<String, String>)invalidCustomers;
			List<RegisterCustomer> registerList = processCustomersErrorMap(invalidCustomerMap);
			registerCustomerList.addAll(registerList);
		}
		
		return registerCustomerList;
	}
	
	private List<RegisterCustomer> processCustomersErrorMap(Map<String, String> customersErrorMap) {
		
		List<RegisterCustomer> registerCustomerList = new ArrayList<RegisterCustomer>();
		
		for (Map.Entry<String, String> entry : customersErrorMap.entrySet()) {
			
			String userName = entry.getKey();
			String errorMessage  = entry.getValue();
			RegisterCustomer registerCustomerError = getRegisterCustomerError("1", errorMessage, userName);
			registerCustomerList.add(registerCustomerError);
			
		}
		
		return registerCustomerList;
		
	}
	
	private List<RegisterCustomer> processCustomersMap(Map<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer, Object> customersMap) {
		
		List<RegisterCustomer> registerCustomerList = new ArrayList<RegisterCustomer>();
		String customerCode = null;
		String apiKey = null;
		
		for (Map.Entry<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer, Object> entry : customersMap.entrySet()) {
			
			com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer = entry.getKey();
			Object value  = entry.getValue();
			if (value instanceof Customer) {
				Customer newCustomer = (Customer) value;
				customerCode = newCustomer.getCustomerCode();
			} else if (value instanceof LmsApiCustomer) {
				LmsApiCustomer lmsApiCustomer = (LmsApiCustomer)value;
				customerCode = lmsApiCustomer.getCustomer().getCustomerCode();
				apiKey = lmsApiCustomer.getApiKey();
			}
			
			RegisterCustomer registerCustomer = getRegisterCustomer(customer, customerCode);
			if (apiKey != null) {
				registerCustomer.setApiKey(apiKey);
			}
			
			registerCustomerList.add(registerCustomer);
		}
		
		return registerCustomerList;
		
	}
	
	private RegisterCustomer getRegisterCustomer(com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer, String customerCode) {
		Company company = customer.getCompany();
		Manager manager = customer.getManager();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address = customer.getAddress();
		String companyName = company.getName();
		String userName = customer.getUserName();
		
		RegisterCustomer registerCustomer = new RegisterCustomer();
		registerCustomer.setErrorCode("0");
		registerCustomer.setErrorMessage("");
		registerCustomer.setCustomerCode(customerCode);
		registerCustomer.setUserName(userName);
		registerCustomer.setCompany(company);
		registerCustomer.setOrganizationGroupName(companyName);
		return registerCustomer;
		
	}
	
	private RegisterCustomer getRegisterCustomerError(String errorCode, String errorMessage, String userName) {
		log.debug(errorMessage);
		RegisterCustomer registerCustomerError = new RegisterCustomer();
		registerCustomerError.setErrorCode(errorCode);
		registerCustomerError.setErrorMessage(errorMessage);
		registerCustomerError.setUserName(userName);
		return registerCustomerError;	
	}
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}
	
}
