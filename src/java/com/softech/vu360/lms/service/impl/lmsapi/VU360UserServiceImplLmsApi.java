package com.softech.vu360.lms.service.impl.lmsapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;

import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;

import com.softech.vu360.lms.service.lmsapi.LearnerServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.OrgGroupServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.VU360UserServiceLmsApi;

import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;

import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.RegisterOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.RegisterOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;

import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.VU360Branding;

public class VU360UserServiceImplLmsApi implements VU360UserServiceLmsApi {
	
	private static final Logger log = Logger.getLogger(VU360UserServiceImplLmsApi.class.getName());
	
	private static final String ERROR_CODE_ONE  = "1";
	private static final String ERROR_CODE_ZERO  = "0";
	
	private VU360UserService vu360UserService;
	private OrgGroupServiceLmsApi orgGroupServiceLmsApi;
	private LearnerServiceLmsApi learnerServiceLmsApi;
	private LearnerService learnerService;
	private ActiveDirectoryService activeDirectoryService;
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	public OrgGroupServiceLmsApi getOrgGroupServiceLmsApi() {
		return orgGroupServiceLmsApi;
	}

	public void setOrgGroupServiceLmsApi(OrgGroupServiceLmsApi orgGroupServiceLmsApi) {
		this.orgGroupServiceLmsApi = orgGroupServiceLmsApi;
	}
	
	public LearnerServiceLmsApi getLearnerServiceLmsApi() {
		return learnerServiceLmsApi;
	}

	public void setLearnerServiceLmsApi(LearnerServiceLmsApi learnerServiceLmsApi) {
		this.learnerServiceLmsApi = learnerServiceLmsApi;
	}
	
	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}

	@Override
	public VU360User getValidUser(String userName, String customerCode) throws Exception {
		log.debug("getValidUser() start");
		String errorMessage = null;
		if (StringUtils.isEmpty(userName) && StringUtils.isBlank(userName)) {
			errorMessage =  "UserId can not be empty or blank" ;
			throwException(errorMessage);
		}
				
		VU360User vu360User = vu360UserService.findUserByUserName(userName);
		if (vu360User == null) {
			errorMessage =  userName + " not found for customer: " + customerCode ;
			throwException(errorMessage);
		}
				
		Learner vu360Learner = vu360User.getLearner();
		String learnerCustomerCode = vu360Learner.getCustomer().getCustomerCode();
				
		// Check whether customer has these learners or not
		if (!customerCode.equalsIgnoreCase(learnerCustomerCode)) {
			errorMessage = userName + " does not belong to customer: " + customerCode ;
			throwException(errorMessage);			
		}	 
		
		log.debug("getValidUser() end");
	    return vu360User;
	}
	
	public VU360User getVU360UserByCustomerGUID(Customer customer) {
		
		if (customer == null) {
			return null;
		}
		String customerGUID = customer.getCustomerGUID();
		return getVU360UserByCustomerGUID(customerGUID);
	}
	
	public VU360User getVU360UserByCustomerGUID(String customerGUID) {
		VU360User manager = vu360UserService.getUserByGUID(customerGUID);
		return manager;
	}
	
	public Map<String, Object> validateAddUserRequest(List<User> userList) throws Exception {
		
		Map<String, Object> usersMap = new LinkedHashMap<String, Object>();
		Map<User, String> invalidUsersMap = new LinkedHashMap<User, String>();
		List<User> validUserList = new ArrayList<User>();
		for (User user: userList) {
			try {
				String userName = user.getUserName();
				if (isUserExist(userName)) {
					String errorMessage = "Unable to add. User Already Exist: " + userName;
					throwException(errorMessage);
				}
				validateUser(user);
				validUserList.add(user);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				invalidUsersMap.put(user, errorMessage);
			}
		}
		
		if (!validUserList.isEmpty()) {
			usersMap.put("validUserList", validUserList); 
		}
		
		if (!invalidUsersMap.isEmpty()) {
			usersMap.put("invalidUsersMap", invalidUsersMap);
		}
		
		return usersMap;	
	}
	
	public Map<String, Object> validateUpdateUserRequest(List<User> userList) throws Exception {
		
		Map<String, Object> usersMap = new LinkedHashMap<String, Object>();
		Map<User, String> invalidUsersMap = new LinkedHashMap<User, String>();
		List<User> validUserList = new ArrayList<User>();
		for (User user: userList) {
			try {
				String userName = user.getUserName();
				if (!isUserExist(userName)) {
					String errorMessage = "Unable to Update. User not found: " + userName;
					throwException(errorMessage);
				}
				validateUser(user);
				validUserList.add(user);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				invalidUsersMap.put(user, errorMessage);
			}
		}
		
		if (!validUserList.isEmpty()) {
			usersMap.put("validUserList", validUserList);
		}
		
		if (!invalidUsersMap.isEmpty()) {
			usersMap.put("invalidUsersMap", invalidUsersMap);
		}
		
		return usersMap;	
	}
	
	public boolean validateUser(User user) throws Exception {
		
		userValidation(user);
		OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
		orgGroupServiceLmsApi.organizationalGroupsValidation(organizationalGroups);
		
		return true;	
	}
	
	public boolean userValidation(User user) throws Exception {
		
		String error = null;
		String accountExpiryDate = null;
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = user.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = user.getAlternateAddress();
		String userName = user.getUserName();
		String password = user.getPassword();
		
		XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
		if (accountExpirationDate != null) {
			accountExpiryDate = accountExpirationDate.toString();
		}
		
		userNameValidation(userName);
		passwordValidation(password);
		emailValidation(emailAddress);
		nameValidation(firstName, middleName, lastName);
		
		if (StringUtils.isNotEmpty(phone) || StringUtils.isNotBlank(phone)){
			if (FieldsValidation.isInValidOffPhone((phone))){
				error = "Bad characters not allowed (Office Phone)";
				throwException(error);
			}
		}
		
		extensionValidation(extension);
		
		if (StringUtils.isNotEmpty(mobilePhone) || StringUtils.isNotBlank(mobilePhone)){
			if (FieldsValidation.isInValidMobPhone((mobilePhone))){
				error = "Bad characters not allowed (Mobile Phone)";
				throwException(error);
			}
		}
		
		addressValidation(address1);
		addressValidation(alternateAddress);
		accountExpirationDateValidation(accountExpiryDate);
		
		return true;
		
	} //end of addUserValidation()
	
	private boolean userNameValidation(String userName) throws Exception {
		
		String error = null;
		if (StringUtils.isEmpty(userName) || StringUtils.isBlank(userName)) {
			error = "User name required";
			throwException(error);
		} else if (FieldsValidation.isInValidGlobalName(userName)){
			error = "Bad characters not allowed (Username)";
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
	
	private static Brander getBrander(String brandName, com.softech.vu360.lms.vo.Language language ) throws Exception {
		
		if (StringUtils.isEmpty(brandName) && StringUtils.isBlank(brandName)) {
			brandName = "default";
		}
		
		if (language == null) {
			language = new com.softech.vu360.lms.vo.Language();
			language.setLanguage(Language.DEFAULT_LANG);
		}
		Brander brander = VU360Branding.getInstance().getBrander(brandName, new com.softech.vu360.lms.vo.Language());
		return brander;
	}
	
	public VU360User getNewUser(Customer customer, User user) throws Exception {
		
		Date newUserExpirationDate = null;
		String accountExpiryDate = null;
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		String userName = user.getUserName();
		String password = user.getPassword();
		boolean isAccountExpired = user.isAccountExpired();
		boolean isAccountLocked = user.isAccountLocked();
		boolean isChangePasswordOnNextLogin = user.isChangePasswordOnNextLogin();
		boolean isAccountDisabled = user.isAccountDisabled();
		boolean isVisibleOnReport = user.isVisibleOnReport();
		
		Calendar calender=Calendar.getInstance();
		Date createdDate=calender.getTime();
		LMSRole systemRole = vu360UserService.getDefaultSystemRole(customer);
		
		VU360User newUser = new VU360User();
		newUser.setFirstName(firstName);
		newUser.setMiddleName(middleName);
		newUser.setLastName(lastName);
		newUser.setEmailAddress(emailAddress);
		newUser.setPassword(password);
		newUser.setUsername(userName);
		newUser.setAccountNonExpired(!isAccountExpired);
		newUser.setAccountNonLocked(!isAccountLocked);
		newUser.setChangePasswordOnLogin(isChangePasswordOnNextLogin);
		newUser.setVissibleOnReport(isVisibleOnReport);
		newUser.setAcceptedEULA(false);
		newUser.setNewUser(true);
		newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);
        newUser.setUserGUID(GUIDGeneratorUtil.generateGUID());
		newUser.setCreatedDate(createdDate);
		newUser.addLmsRole(systemRole);
	
		/**
		 * We have date in ("yyyy-MM-dd") format but in the it requires date in ("MM/dd/yyyy") format.
		 * Here we are making date in the desired format by splitting date string.
		 */
		XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
		if (accountExpirationDate != null) {
			String expiryDate = accountExpirationDate.toString();
			if (expiryDate.indexOf("-") != -1) {
				String[] dateArray = expiryDate.split("-");
				String year = dateArray[0];
				String month = dateArray[1];
				String day = dateArray[2];
				accountExpiryDate = month + "/" + day + "/" + year;
				
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				newUserExpirationDate = formatter.parse(accountExpiryDate);
			}
		}

		newUser.setExpirationDate(newUserExpirationDate);
		return newUser;	
	}
	
	public Learner updateUser(Customer customer, VU360User existingUser, User user) throws Exception {
		
		VU360User updateableUser = vu360UserService.loadForUpdateVU360User(existingUser.getId());
        VU360User updatedUser = getUpdatedUser(user, updateableUser);
        LearnerProfile updatedLearnerProfile = learnerServiceLmsApi.getUpdatedLearnerProfile(user, updatedUser);
        updatedUser.getLearner().setLearnerProfile(updatedLearnerProfile);
        
        //Update Profile before Remove Training Manger since it generate exception after removal. 
        learnerService.updateLearnerProfile(updatedUser.getLearner().getLearnerProfile());
        
        if(updatedUser.getTrainingAdministrator()!= null){
    		// this means.. this is existing learner and has got administrator .. really need demotion :)
    		removeTrainingManager(updatedUser);
    	}
        
        log.debug("updating learner having id = " + updatedUser.getLearner().getId() +" profile id = " +
        		updatedUser.getLearner().getLearnerProfile().getId() +" address id = " +
        		updatedUser.getLearner().getLearnerProfile().getLearnerAddress().getId() +" user id = " + updatedUser.getId());
        
        VU360User newUpdatedUser = learnerService.updateUserFromBatchFile(updatedUser);
        Learner newUpdatedLearner = newUpdatedUser.getLearner(); 
        log.debug("learner updated.");
       
        return updatedUser.getLearner();
           
	}

	private VU360User getUpdatedUser(User user, VU360User updateableUser) throws Exception {
	
		Date updatedUserExpirationDate = null;
		String accountExpiryDate = null;
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		String userName = user.getUserName();
		String password = user.getPassword();
		boolean isAccountExpired = user.isAccountExpired();
		boolean isAccountLocked = user.isAccountLocked();
		boolean isChangePasswordOnNextLogin = user.isChangePasswordOnNextLogin();
		boolean isAccountDisabled = user.isAccountDisabled();
		boolean isVisibleOnReport = user.isVisibleOnReport();
	
		updateableUser.setFirstName(firstName);
		updateableUser.setMiddleName(middleName);
		updateableUser.setLastName(lastName);
		updateableUser.setEmailAddress(emailAddress);
		updateableUser.setPassWordChanged(true);
		updateableUser.setPassword(password);
		updateableUser.setUsername(userName);
		updateableUser.setAccountNonExpired(!isAccountExpired);
		updateableUser.setAccountNonLocked(!isAccountLocked);
		updateableUser.setChangePasswordOnLogin(isChangePasswordOnNextLogin);
		updateableUser.setVissibleOnReport(isVisibleOnReport);
		updateableUser.setCredentialsNonExpired(!isAccountExpired);
		updateableUser.setEnabled(true);
   
		/**
		 * We have date in ("yyyy-MM-dd") format but in the it requires date in ("MM/dd/yyyy") format.
		 * Here we are making date in the desired format by splitting date string.
		 */
		XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
		if (accountExpirationDate != null) {
			String expiryDate = accountExpirationDate.toString();
			if (expiryDate.indexOf("-") != -1) {
				String[] dateArray = expiryDate.split("-");
				String year = dateArray[0];
				String month = dateArray[1];
				String day = dateArray[2];
				accountExpiryDate = month + "/" + day + "/" + year;
			
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				updatedUserExpirationDate = formatter.parse(accountExpiryDate);
			}
		}

		updateableUser.setExpirationDate(updatedUserExpirationDate);
		return updateableUser;	
	
	}
	
	private void removeTrainingManager(VU360User user){
		TrainingAdministrator trainingAdministrator = user.getTrainingAdministrator();
		trainingAdministrator.setVu360User(null);
		trainingAdministrator.setCustomer(null);
		vu360UserService.deleteLMSTrainingAdministrator(trainingAdministrator);
		
		// set training manager null
		user.setTrainingAdministrator(null);

		//delete manager role from this user
		Set<LMSRole> roles = user.getLmsRoles();
    	Set<LMSRole> roles2Remove = new HashSet<LMSRole>();
    	for(LMSRole role : roles){
    		if(role.getRoleType().equals(LMSRole.ROLE_TRAININGMANAGER)){
    			roles2Remove.add(role);
    		}
    	}
    	if(CollectionUtils.isNotEmpty(roles2Remove)){
    		roles.removeAll(roles2Remove);
    	}
    	user.setLmsRoles(roles);
    }
	
	private boolean isUserExist(String userName){
		boolean userExist = false;
		VU360User vu360User = vu360UserService.findUserByUserName(userName); 
		if (vu360User != null) {
			userExist = true;
		}
		
		return userExist;
	}
	
	private boolean findUserInAD(String userName) {
		return activeDirectoryService.findADUser(userName);	
	}
	
	public Map<String, Object> processUsersMap(Customer customer, Map<String, Object> usersMap) throws Exception {
		
		Object learners = usersMap.get("validUserList");
		Object invalidLearners = usersMap.get("invalidUsersMap");
		
		if (learners != null) {
			List<User> validUserList = (List<User>) learners;
			Map<User, Map<String, Object>> usersResultMap = processValidUserList(customer, validUserList);
			usersMap.put("usersResultMap", usersResultMap);
		}
		
		if (invalidLearners != null) {
			Map<User, String> invalidUsersMap = (Map<User, String>) invalidLearners;
			
		}
		
		return usersMap;
		
	}
	
	private Map<User, Map<String, Object>> processValidUserList(Customer customer, List<User> userList) throws Exception {
		
		
		Map<User, Map<String, Object>> usersResultMap = new LinkedHashMap<User, Map<String, Object>>();
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		
		for (User user : userList) {
			
			Map<String, Object> orgGroupResultMap = new LinkedHashMap<String, Object>();
			
			List<String> validOrgGroupHierarchyList = null;
			Map<String, String> inValidOrgGroupHierarchyMap = null;
			List<OrganizationalGroup> validOrganizationalGroupList = null;
			List<String> orgGroupHierarchyListForResponse = null;
			Map<String, String> organizationalGroupErrorMap = null;
			
			OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
			List<String> orgGroupHierarchyList = organizationalGroups.getOrgGroupHierarchy();
			Map<String, Object> OrgGroupHierarchyMap = orgGroupServiceLmsApi.getOrgGroupHierarchyMap(customer, orgGroupHierarchyList);
			Object validOrgGroupHierarchy = OrgGroupHierarchyMap.get("validOrgGroupHierarchyList");
			if (validOrgGroupHierarchy != null) {
				validOrgGroupHierarchyList = (List<String>)validOrgGroupHierarchy;
			} 
			
			Object invalidOrgGroupHierarchy = OrgGroupHierarchyMap.get("inValidOrgGroupHierarchyMap");
			if (invalidOrgGroupHierarchy != null) {
				inValidOrgGroupHierarchyMap = (Map<String, String>) invalidOrgGroupHierarchy;
				orgGroupResultMap.put("inValidOrgGroupHierarchyMap", inValidOrgGroupHierarchyMap);
			}
			
			Map<String, Object> organizationalGroupMap = orgGroupServiceLmsApi.getOrganizationalGroupMap(customer, validOrgGroupHierarchyList);
			Object organizationalGroupList = organizationalGroupMap.get("validOrganizationalGroupList");
			if (organizationalGroupList != null) {
				validOrganizationalGroupList = (List<OrganizationalGroup>) organizationalGroupList;
				orgGroupResultMap.put("validOrganizationalGroupList", validOrganizationalGroupList);
			}
			
			Object orgGroupHierarchyForResponse = organizationalGroupMap.get("orgGroupHierarchyListForResponse");
			if (orgGroupHierarchyForResponse != null) {
				orgGroupHierarchyListForResponse = (List<String>) orgGroupHierarchyForResponse;
				orgGroupResultMap.put("orgGroupHierarchyListForResponse", orgGroupHierarchyListForResponse);
			}
			
			Object organizationalGroupErrors = organizationalGroupMap.get("organizationalGroupErrorMap");
			if (organizationalGroupErrors != null) {
				organizationalGroupErrorMap = (Map<String, String>) organizationalGroupErrors;
				orgGroupResultMap.put("organizationalGroupErrorMap", organizationalGroupErrorMap);
			}
			
			usersResultMap.put(user, orgGroupResultMap);
			
		}
		
		return usersResultMap;
		
	}
	
	public List<RegisterUser> getRegisterUserList(Customer customer, Map<String, Object> processedUsersMap, boolean addUser) throws Exception {
		
		List<RegisterUser> registerUserList = new ArrayList<RegisterUser>();
		Map<User, Map<String, Object>> usersResultMap = null;
		Map<User, String> invalidUsersMap = null;
		
		Object invalidLearners = processedUsersMap.get("invalidUsersMap");
		if (invalidLearners != null) {
			invalidUsersMap = (Map<User, String>) invalidLearners;
			List<RegisterUser> registerInvalidUserList = processInvalidUsersMap(invalidUsersMap);
			registerUserList.addAll(registerInvalidUserList);
		}
		
		Object usersResult = processedUsersMap.get("usersResultMap");
		if (usersResult != null) {
			usersResultMap = (Map<User, Map<String, Object>>) usersResult;
			for (Map.Entry<User, Map<String, Object>> usersResultMapEntry : usersResultMap.entrySet()) {
				
				RegisterUser registerUser = null;
				
				User user = usersResultMapEntry.getKey();
				Map<String, Object> orgGroupResultMap = usersResultMapEntry.getValue();
				
				List<RegisterOrganizationalGroup> registerOrganizationalGroupList = new ArrayList<RegisterOrganizationalGroup>();
				
				Object invalidOrgGroupHierarchy = orgGroupResultMap.get("inValidOrgGroupHierarchyMap");
				if (invalidOrgGroupHierarchy != null) {
					Map<String, String> inValidOrgGroupHierarchyMap = (Map<String, String>) invalidOrgGroupHierarchy;
					List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorListForOrgGroupHierarchy = processOrgGroupErrorsMap(inValidOrgGroupHierarchyMap);
					registerOrganizationalGroupList.addAll(registerOrganizationalGroupErrorListForOrgGroupHierarchy);
				}
				
				Object organizationalGroupErrors = orgGroupResultMap.get("organizationalGroupErrorMap");
				if (organizationalGroupErrors != null) {
					Map<String, String> organizationalGroupErrorMap = (Map<String, String>) organizationalGroupErrors;
					List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorListForOrgGroup  = processOrgGroupErrorsMap(organizationalGroupErrorMap);
					registerOrganizationalGroupList.addAll(registerOrganizationalGroupErrorListForOrgGroup);
					
				}
				
				Object organizationalGroupList = orgGroupResultMap.get("validOrganizationalGroupList");
				if (organizationalGroupList != null) {
					List<OrganizationalGroup> validOrganizationalGroupList = (List<OrganizationalGroup>) organizationalGroupList;
					
					Learner learner = null;
					if (addUser) {
						learner = learnerServiceLmsApi.getNewLearner(customer, user);
					} else {
						learner = learnerServiceLmsApi.getUpdatedLearner(customer, user);
					}
					
					learnerServiceLmsApi.updateLearnerAssociationOfOrgGroups(learner, validOrganizationalGroupList);
					
					Object orgGroupHierarchyForResponse = orgGroupResultMap.get("orgGroupHierarchyListForResponse");
					if (orgGroupHierarchyForResponse != null) {
						List<String> orgGroupHierarchyListForResponse = (List<String>) orgGroupHierarchyForResponse;
						
						for (String orgGroupHierarchy :  orgGroupHierarchyListForResponse) {
							RegisterOrganizationalGroup registerOrgGroup = getRegisterOrganizationalGroup(ERROR_CODE_ZERO, "", orgGroupHierarchy);
							registerOrganizationalGroupList.add(registerOrgGroup);
						}
						
					}
					registerUser = getResponseRegisterUser(user, registerOrganizationalGroupList, ERROR_CODE_ZERO, "");
				} else {
					
					String errorMessage = null;
					if (addUser) {
						errorMessage = "Unable to add user";
					} else {
						errorMessage = "Unable to update user";
					}
					registerUser = getResponseRegisterUser(user, registerOrganizationalGroupList, ERROR_CODE_ONE, errorMessage);
				}
				registerUserList.add(registerUser);
			}	
		}
		return registerUserList;
	}
	
	private List<RegisterUser> processInvalidUsersMap(Map<User, String> invalidUsersMap) {
		
		List<RegisterUser> registerInvalidUserList = new ArrayList<RegisterUser>();
		
		for (Map.Entry<User, String> invalidUsersMapEntry : invalidUsersMap.entrySet()) {
			
			User user = invalidUsersMapEntry.getKey();
			String errorMessage = invalidUsersMapEntry.getValue();
			RegisterUser registerUserError = getRegisterUser(ERROR_CODE_ONE, errorMessage, user);
			registerInvalidUserList.add(registerUserError);
		}
		
		return registerInvalidUserList;
		
	}
	
	private List<RegisterOrganizationalGroup> processOrgGroupErrorsMap(Map<String, String> OrgGroupErrorsMap) {
		
		List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorList = new ArrayList<RegisterOrganizationalGroup>();
		
		for (Map.Entry<String, String> OrgGroupErrorsMapEntry : OrgGroupErrorsMap.entrySet()) {
			
			String orgGroupHierarchy = OrgGroupErrorsMapEntry.getKey();
			String errorMessage = OrgGroupErrorsMapEntry.getValue();
			RegisterOrganizationalGroup registerOrgGroupError = getRegisterOrganizationalGroup(ERROR_CODE_ONE, errorMessage, orgGroupHierarchy);
			registerOrganizationalGroupErrorList.add(registerOrgGroupError);
		}
		
		return registerOrganizationalGroupErrorList;
		
	}
	
	private RegisterUser getRegisterUser(String errorCode, String errorMessage, User user) {
		log.debug(errorMessage);
		
		String userName = user.getUserName();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		
		RegisterUser registerUser = new RegisterUser();
		registerUser.setErrorCode(errorCode);
		registerUser.setErrorMessage(errorMessage);
		registerUser.setUserName(userName);
		registerUser.setFirstName(firstName);
		registerUser.setLastName(lastName);
		registerUser.setEmailAddress(emailAddress);
		return registerUser;
	}
	
	private RegisterOrganizationalGroup getRegisterOrganizationalGroup(String errorCode, String errorMessage, String orgGroupHierarchy) {
		RegisterOrganizationalGroup registerOrgGroup = new RegisterOrganizationalGroup();
		registerOrgGroup.setErrorCode(errorCode);
		registerOrgGroup.setErrorMessage(errorMessage);
		registerOrgGroup.setOrgGroupHierarchy(orgGroupHierarchy);
		return registerOrgGroup;
	}
	
	private RegisterUser getResponseRegisterUser(User user, List<RegisterOrganizationalGroup> registerOrganizationalGroupList, String errorCode, String errorMessage) {
		
		RegisterOrganizationalGroups registerOrganizationalGroups = new RegisterOrganizationalGroups();
		registerOrganizationalGroups.setRegisterOrganizationalGroup(registerOrganizationalGroupList);
		
		RegisterUser registerUser = getRegisterUser(errorCode, errorMessage, user);
		registerUser.setRegisterOrganizationalGroups(registerOrganizationalGroups);
		
		return registerUser;
	}
	

	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}

}
