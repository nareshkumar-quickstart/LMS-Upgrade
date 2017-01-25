package com.softech.vu360.lms.service.impl.lmsapi;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.lmsapi.LmsApiOrgGroupService;
import com.softech.vu360.lms.service.lmsapi.LmsApiUserService;
import com.softech.vu360.lms.service.lmsapi.LmsApiUserValidationService;
import com.softech.vu360.lms.service.lmsapi.response.LmsApiUserResponseService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiOrgGroupValidationService;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.RegisterOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.GUIDGeneratorUtil;

@Service
public class LmsApiUserServiceImpl implements LmsApiUserService {

	private static final Logger log = Logger.getLogger(LmsApiUserServiceImpl.class.getName());
	
	private static final String ERROR_CODE_ONE  = "1";
	private static final String ERROR_CODE_ZERO  = "0";
	
	@Autowired
	private LmsApiUserValidationService lmsApiUserValidationService;
	
	@Autowired
	private LmsApiOrgGroupValidationService lmsApiOrgGroupValidationService;
	
	@Autowired
	private LmsApiOrgGroupService lmsApiOrgGroupService;
	
	@Autowired
	private LearnerService learnerService;
	
	@Autowired
	private VU360UserService vu360UserService;
	
	@Autowired
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	
	@Autowired
	private LmsApiUserResponseService lmsApiUserResponseService;
	
	public void setLmsApiUserValidationService(LmsApiUserValidationService lmsApiUserValidationService) {
		this.lmsApiUserValidationService = lmsApiUserValidationService;
	}

	public void setLmsApiOrgGroupValidationService(LmsApiOrgGroupValidationService lmsApiOrgGroupValidationService) {
		this.lmsApiOrgGroupValidationService = lmsApiOrgGroupValidationService;
	}

	public void setLmsApiOrgGroupService(LmsApiOrgGroupService lmsApiOrgGroupService) {
		this.lmsApiOrgGroupService = lmsApiOrgGroupService;
	}
	
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}
	
	public void setLmsApiUserResponseService(LmsApiUserResponseService lmsApiUserResponseService) {
		this.lmsApiUserResponseService = lmsApiUserResponseService;
	}

	@Override
	public List<RegisterUser> addUsers(Customer customer, VU360User manager, Map<Boolean, List<User>> usersMap) {
		
		String customerCode = customer.getCustomerCode();
		List<User> validUsers = lmsApiUserValidationService.getValidUsers(usersMap);
		Map<User, String> invalidUsers = lmsApiUserValidationService.getInvalidUsersForAddUser(customerCode, manager, usersMap);
		List<RegisterUser> registerUsers = processAddUsers(customer, customerCode, manager, validUsers, invalidUsers);
		return registerUsers;
	}
	
	@Override
	public List<RegisterUser> addUsersLmsOnly(Customer customer, VU360User manager, Map<Boolean, List<User>> lmsOnlyUsersMap) {
		
		String customerCode = customer.getCustomerCode();
		List<User> lmsOnlyValidUsers = lmsApiUserValidationService.getValidUsers(lmsOnlyUsersMap);
		Map<User, String> lmsOnlyInvalidUsers = lmsApiUserValidationService.getInvalidUsersForAddUserLmsOnly(customerCode, manager, lmsOnlyUsersMap);
		List<RegisterUser> registerUsers = processAddUsers(customer, customerCode, manager, lmsOnlyValidUsers, lmsOnlyInvalidUsers);
		return registerUsers;
	}
	
	@Override
	public List<RegisterUser> updateUsers(Customer customer, VU360User manager, Map<Boolean, List<UpdateableUser>> updateableUsersMap) {
		List<RegisterUser> registerUsers = new ArrayList<RegisterUser>();
		String customerCode = customer.getCustomerCode();
		List<UpdateableUser> validUpdateableUsers = lmsApiUserValidationService.getValidUpdateableUsers(updateableUsersMap);
		Map<UpdateableUser, String> invalidUpdateableUsers = lmsApiUserValidationService.getInvalidUpdateableUsers(customerCode, manager, updateableUsersMap);
		if (!CollectionUtils.isEmpty(validUpdateableUsers)) {
			for (UpdateableUser updateableUser : validUpdateableUsers) {
				try {
					RegisterUser registerUser = updateUser(customer, customerCode, manager, updateableUser);
					registerUsers.add(registerUser);
				} catch (Exception e) {
					if (invalidUpdateableUsers == null) {
						invalidUpdateableUsers = new HashMap<>();
					}
					invalidUpdateableUsers.put(updateableUser, e.getMessage());
				}
			}
		}
		
		if (!CollectionUtils.isEmpty(invalidUpdateableUsers)) {
			for (Map.Entry<UpdateableUser, String> entry : invalidUpdateableUsers.entrySet()) {
				UpdateableUser invalidUpdateableUser = entry.getKey();
				String errorMessage = entry.getValue();
				RegisterUser registerUser = lmsApiUserResponseService.getRegisterUser(ERROR_CODE_ONE, errorMessage, invalidUpdateableUser);
				registerUsers.add(registerUser);
			}
		}
		return registerUsers;
	}

	private List<RegisterUser> processAddUsers(Customer customer, String customerCode, VU360User manager, List<User> validUsers, Map<User, String> invalidUsers) {
		
		List<RegisterUser> registerUsers = new ArrayList<RegisterUser>();
		
		if (!CollectionUtils.isEmpty(validUsers)) {
			for (User user: validUsers) {
				try {
					RegisterUser registerUser = addUser(customer, customerCode, manager, user);
					registerUsers.add(registerUser);
				} catch (Exception e) {
					if (invalidUsers == null) {
						invalidUsers = new HashMap<>();
					}
					invalidUsers.put(user, e.getMessage());
				}	
			} //end of for
		} // end of if
		
		if (!CollectionUtils.isEmpty(invalidUsers)) {
			for (Map.Entry<User, String> entry : invalidUsers.entrySet()) {
				User invalidUser = entry.getKey();
				String errorMessage = entry.getValue();
				RegisterUser registerUser = lmsApiUserResponseService.getRegisterUser(ERROR_CODE_ONE, errorMessage, invalidUser);
				registerUsers.add(registerUser);
			}
		}
		return registerUsers;
	}
	
	private RegisterUser addUser(Customer customer, String customerCode, VU360User manager, User user) throws Exception {
		
		RegisterUser registerUser = null;
		OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
		Map<Boolean, List<String>> orgGroupHierarchyMap = lmsApiOrgGroupValidationService.getOrgGroupHierarchyMap(manager, organizationalGroups);
		List<String> validOrgGroupHierarchies = lmsApiOrgGroupValidationService.getValidOrgGroupHierarchies(orgGroupHierarchyMap);
		Map<String, String> invalidOrgGroupHierarchies = lmsApiOrgGroupValidationService.getInvalidOrgGroupHierarchies(manager, customerCode, orgGroupHierarchyMap);
		if (!CollectionUtils.isEmpty(validOrgGroupHierarchies)) {
			Map<String, OrganizationalGroup> orgGroups = lmsApiOrgGroupService.getOrgGroupsMap(customer, manager, validOrgGroupHierarchies);
			List<OrganizationalGroup> validOrgGroups = lmsApiOrgGroupValidationService.getValidOrgGroups(orgGroups);
			Map<String, String> invalidOrgGroups = lmsApiOrgGroupValidationService.getInvalidOrgGroups(customer, manager, orgGroups);
			if (!CollectionUtils.isEmpty(validOrgGroups)) {
				Learner learner = getNewLearner(customer, user);
				updateLearnerAssociationOfOrgGroups(learner, validOrgGroups);
				registerUser = lmsApiUserResponseService.getRegisterUser(ERROR_CODE_ZERO, "", user);
				validOrgGroupHierarchies.clear();
				for (OrganizationalGroup orgGroup : validOrgGroups) {
					String orgGroupHierarchy = orgGroup.toString();
					validOrgGroupHierarchies.add(orgGroupHierarchy);
				}
				
				if (!CollectionUtils.isEmpty(invalidOrgGroups)) {
					if (invalidOrgGroupHierarchies == null) {
						invalidOrgGroupHierarchies = new HashMap<>();
					}
					invalidOrgGroupHierarchies.putAll(invalidOrgGroups);
				}
				RegisterOrganizationalGroups registerOrganizationalGroups = lmsApiUserResponseService.getRegisterOrganizationalGroups(validOrgGroupHierarchies, invalidOrgGroupHierarchies);
				registerUser.setRegisterOrganizationalGroups(registerOrganizationalGroups);
			} else {
				String userName = user.getUserName();
				String errorMessage = "Unable to add user. No valid organizational group found for user: " + userName;
				if (!CollectionUtils.isEmpty(invalidOrgGroups)) {
					if (invalidOrgGroupHierarchies == null) {
						invalidOrgGroupHierarchies = new HashMap<>();
					}
					invalidOrgGroupHierarchies.putAll(invalidOrgGroups);
				}
				RegisterOrganizationalGroups registerOrganizationalGroups = lmsApiUserResponseService.getRegisterOrganizationalGroups(null, invalidOrgGroupHierarchies);
				registerUser = lmsApiUserResponseService.getRegisterUser(ERROR_CODE_ONE, errorMessage, user);
				registerUser.setRegisterOrganizationalGroups(registerOrganizationalGroups);
			}
			
		} else {
			String userName = user.getUserName();
			String errorMessage = "Unable to add user. No valid Org group hierarchy found for user: " + userName;
			RegisterOrganizationalGroups registerOrganizationalGroups = lmsApiUserResponseService.getRegisterOrganizationalGroups(null, invalidOrgGroupHierarchies);
			registerUser = lmsApiUserResponseService.getRegisterUser(ERROR_CODE_ONE, errorMessage, user);
			registerUser.setRegisterOrganizationalGroups(registerOrganizationalGroups);
		}
		
		return registerUser;
		
	}
	
	private RegisterUser updateUser(Customer customer, String customerCode, VU360User manager, UpdateableUser updateableUser) throws Exception {
		
		RegisterUser registerUser = null;
		OrganizationalGroups organizationalGroups = updateableUser.getOrganizationalGroups();
		Map<Boolean, List<String>> orgGroupHierarchyMap = lmsApiOrgGroupValidationService.getOrgGroupHierarchyMap(manager, organizationalGroups);
		List<String> validOrgGroupHierarchies = lmsApiOrgGroupValidationService.getValidOrgGroupHierarchies(orgGroupHierarchyMap);
		Map<String, String> invalidOrgGroupHierarchies = lmsApiOrgGroupValidationService.getInvalidOrgGroupHierarchies(manager, customerCode, orgGroupHierarchyMap);
		if (!CollectionUtils.isEmpty(validOrgGroupHierarchies)) {
			Map<String, OrganizationalGroup> orgGroups = lmsApiOrgGroupService.getOrgGroupsMap(customer, manager, validOrgGroupHierarchies);
			List<OrganizationalGroup> validOrgGroups = lmsApiOrgGroupValidationService.getValidOrgGroups(orgGroups);
			Map<String, String> invalidOrgGroups = lmsApiOrgGroupValidationService.getInvalidOrgGroups(customer, manager, orgGroups);
			if (!CollectionUtils.isEmpty(validOrgGroups)) {
				Learner learner = getUpdatedLearner(customer, updateableUser);
				updateLearnerAssociationOfOrgGroups(learner, validOrgGroups);
				registerUser = lmsApiUserResponseService.getRegisterUser(ERROR_CODE_ZERO, "", updateableUser);
				validOrgGroupHierarchies.clear();
				for (OrganizationalGroup orgGroup : validOrgGroups) {
					String orgGroupHierarchy = orgGroup.toString();
					validOrgGroupHierarchies.add(orgGroupHierarchy);
				}
				
				if (!CollectionUtils.isEmpty(invalidOrgGroups)) {
					if (invalidOrgGroupHierarchies == null) {
						invalidOrgGroupHierarchies = new HashMap<>();
					}
					invalidOrgGroupHierarchies.putAll(invalidOrgGroups);
				}
				RegisterOrganizationalGroups registerOrganizationalGroups = lmsApiUserResponseService.getRegisterOrganizationalGroups(validOrgGroupHierarchies, invalidOrgGroupHierarchies);
				registerUser.setRegisterOrganizationalGroups(registerOrganizationalGroups);
			} else {
				String userName = updateableUser.getUserName();
				String errorMessage = "Unable to update user. No valid organizational group found for user: " + userName;
				if (!CollectionUtils.isEmpty(invalidOrgGroups)) {
					if (invalidOrgGroupHierarchies == null) {
						invalidOrgGroupHierarchies = new HashMap<>();
					}
					invalidOrgGroupHierarchies.putAll(invalidOrgGroups);
				}
				RegisterOrganizationalGroups registerOrganizationalGroups = lmsApiUserResponseService.getRegisterOrganizationalGroups(null, invalidOrgGroupHierarchies);
				registerUser = lmsApiUserResponseService.getRegisterUser(ERROR_CODE_ONE, errorMessage, updateableUser);
				registerUser.setRegisterOrganizationalGroups(registerOrganizationalGroups);
			}
			
		} else {
			String userName = updateableUser.getUserName();
			String errorMessage = "Unable to update user. No valid Org group hierarchy found for user: " + userName;
			RegisterOrganizationalGroups registerOrganizationalGroups = lmsApiUserResponseService.getRegisterOrganizationalGroups(null, invalidOrgGroupHierarchies);
			registerUser = lmsApiUserResponseService.getRegisterUser(ERROR_CODE_ONE, errorMessage, updateableUser);
			registerUser.setRegisterOrganizationalGroups(registerOrganizationalGroups);
		}
		
		return registerUser;
		
	}
	
	private Learner getNewLearner(Customer customer, User user) throws Exception {
		Learner newLearner = createNewUser(customer, user);;
		return newLearner;
	}
	
	private Learner createNewUser(Customer customer, User user) throws Exception {
		
		VU360User newUser = getNewUser(customer, user);
		LearnerProfile newLearnerProfile = getNewLearnerProfile(user);
		Learner newLearner = new Learner();
		newLearner.setVu360User(newUser);
		
		newLearnerProfile.setLearner(newLearner);
		
		newLearner.setLearnerProfile(newLearnerProfile);
		newLearner.setCustomer(customer);
		
		newUser.setLearner(newLearner);
		
		log.debug("adding learner...");
		newLearner = learnerService.addLearner(newLearner);
		log.debug("learner added.");
		
		return newLearner;
		
	} //end of createNewUser()
	
	private VU360User getNewUser(Customer customer, User user) throws Exception {
		
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
		LMSRole systemRole = getDefaultSystemRole(customer);
		
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
	
	private LearnerProfile getNewLearnerProfile(User user) {
		
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = user.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = user.getAlternateAddress();
		//List<CustomFieldValue> myCustomFieldValues = new ArrayList<CustomFieldValue>();
		
		LearnerProfile newLearnerProfile = new LearnerProfile();
		newLearnerProfile.setMobilePhone(mobilePhone);
		newLearnerProfile.setOfficePhone(phone);
		newLearnerProfile.setOfficePhoneExtn(extension);
		newLearnerProfile.setCustomFieldValues(null);
		
		if (address1 != null) {
			Address newAddress = getNewAddress(address1);
			newLearnerProfile.setLearnerAddress(newAddress);
		} else {
			address1 = getEmptyAddress();
			Address newAddress = getNewAddress(address1);
			newLearnerProfile.setLearnerAddress(newAddress);
		}
		
		if (alternateAddress != null) {
			Address newAddress2 = getNewAddress(alternateAddress);
			newLearnerProfile.setLearnerAddress2(newAddress2);
		} else {
			alternateAddress = getEmptyAddress();
			Address newAddress2 = getNewAddress(alternateAddress);
			newLearnerProfile.setLearnerAddress2(newAddress2);
		}
		
		return newLearnerProfile;
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
	
	private Address getNewAddress(com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address) {
		
		String streetAddress1 = address.getStreetAddress1();
		String streetAddress2 = address.getStreetAddress2();
		String city = address.getCity();
		String country = address.getCountry();
		String state = address.getState();
		String zipCode = address.getZipCode();
		
		Address newAddress = new Address();
		newAddress.setStreetAddress(streetAddress1);
		newAddress.setStreetAddress2(streetAddress2);
		newAddress.setCity(city);
		newAddress.setState(state);
		newAddress.setZipcode(zipCode);
		newAddress.setCountry(country);
		
		return newAddress;
		
	}

	private LMSRole getDefaultSystemRole(Customer customer) {
	
		LMSRole systemRole = vu360UserService.getDefaultRole(customer);
		if(systemRole == null) {
			List<LMSRole> systemRoles=vu360UserService.getSystemRolesByCustomer(customer);
			for(LMSRole role:systemRoles){
	
				if(role.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER))
				{
					systemRole=role;
					break;
				}
			}
		}
		return systemRole;
	}
	
	private boolean updateLearnerAssociationOfOrgGroups (Learner learner, List<OrganizationalGroup> organizationalGroups) {
		
		boolean updated = false;
		
		if (!CollectionUtils.isEmpty(organizationalGroups)) {
			Set<OrganizationalGroup> setOfOrgGroups = new HashSet<>(organizationalGroups);
			log.debug("updating associations of Org groups...");
	        orgGroupLearnerGroupService.addRemoveOrgGroupsForLearner(learner, setOfOrgGroups);
	        updated = true;
		}
		
        return updated;
		
	}
	
	private Learner getUpdatedLearner(Customer customer, UpdateableUser updateableUser) throws Exception {
		
		String userName = updateableUser.getUserName();
		Learner updatedLearner = null;
		VU360User existingUser = vu360UserService.findUserByUserName(userName);
		if (existingUser == null) {
			String errorMessage = "No existing user found for: " + userName;
			throw new Exception(errorMessage);
		} 
		
		updatedLearner = updateUser(customer, existingUser, updateableUser);
		return updatedLearner;
		
	}
	
	private Learner updateUser(Customer customer, VU360User existingUser, UpdateableUser userToBeUpdate) throws Exception {
		
		VU360User updateableUser = vu360UserService.loadForUpdateVU360User(existingUser.getId());
        VU360User updatedUser = getUpdatedUser(userToBeUpdate, updateableUser);
        LearnerProfile updatedLearnerProfile = getUpdatedLearnerProfile(userToBeUpdate, updatedUser);
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

	private VU360User getUpdatedUser(UpdateableUser userToBeUpdate, VU360User existingUser) throws Exception {
		
		String middleName = null;
		
		Date updatedUserExpirationDate = null;
		String accountExpiryDate = null;
		String firstName = userToBeUpdate.getFirstName();
		middleName = userToBeUpdate.getMiddleName();
		if (middleName == null) {
			middleName = existingUser.getMiddleName();
		}
		String lastName = userToBeUpdate.getLastName();
		String emailAddress = userToBeUpdate.getEmailAddress();
		String userName = userToBeUpdate.getUserName();
		String password = userToBeUpdate.getPassword();
		boolean isAccountExpired = userToBeUpdate.isAccountExpired();
		boolean isAccountLocked = userToBeUpdate.isAccountLocked();
		boolean isChangePasswordOnNextLogin = userToBeUpdate.isChangePasswordOnNextLogin();
		boolean isAccountDisabled = userToBeUpdate.isAccountDisabled();
		boolean isVisibleOnReport = userToBeUpdate.isVisibleOnReport();
		
		existingUser.setFirstName(firstName);
		existingUser.setMiddleName(middleName);
		existingUser.setLastName(lastName);
		existingUser.setEmailAddress(emailAddress);
		
		if (password != null) {
			isCorrectPassword(password);
			existingUser.setPassWordChanged(true);
			existingUser.setPassword(password);
		} 
		
		existingUser.setUsername(userName);
		existingUser.setAccountNonExpired(!isAccountExpired);
		existingUser.setAccountNonLocked(!isAccountLocked);
		existingUser.setChangePasswordOnLogin(isChangePasswordOnNextLogin);
		existingUser.setVissibleOnReport(isVisibleOnReport);
		existingUser.setCredentialsNonExpired(!isAccountExpired);
		existingUser.setEnabled(true);
	   
		/**
		 * We have date in ("yyyy-MM-dd") format but in the it requires date in ("MM/dd/yyyy") format.
		 * Here we are making date in the desired format by splitting date string.
		 */
		XMLGregorianCalendar accountExpirationDate = userToBeUpdate.getExpirationDate();
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
		} else {
			updatedUserExpirationDate = existingUser.getExpirationDate();
		}
	
		existingUser.setExpirationDate(updatedUserExpirationDate);
		return existingUser;	
		
	}

	private LearnerProfile getUpdatedLearnerProfile(UpdateableUser userToBeUpdate, VU360User existingUser) {
		
		String phone = userToBeUpdate.getPhone() == null ? existingUser.getLearner().getLearnerProfile().getOfficePhone() : userToBeUpdate.getPhone();
		String mobilePhone = userToBeUpdate.getMobilePhone() == null ? existingUser.getLearner().getLearnerProfile().getMobilePhone() : userToBeUpdate.getMobilePhone();
		String extension = userToBeUpdate.getExtension() == null ? existingUser.getLearner().getLearnerProfile().getOfficePhoneExtn() : userToBeUpdate.getExtension();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address address1 = userToBeUpdate.getAddress();
		com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address alternateAddress = userToBeUpdate.getAlternateAddress();
		
		LearnerProfile updatedLearnerProfile = existingUser.getLearner().getLearnerProfile();
		updatedLearnerProfile = learnerService.loadForUpdateLearnerProfile(updatedLearnerProfile.getId());
		updatedLearnerProfile.setMobilePhone(mobilePhone);
		updatedLearnerProfile.setOfficePhone(phone);
		updatedLearnerProfile.setOfficePhoneExtn(extension);
		//updatedLearnerProfile.setCustomFieldValues(null);
		
		if (address1 != null) {
			Address newAddress = getNewAddress(address1);
			updatedLearnerProfile.setLearnerAddress(newAddress);
		} else {
			Address newAddress = existingUser.getLearner().getLearnerProfile().getLearnerAddress();
			updatedLearnerProfile.setLearnerAddress(newAddress);
		}
		
		if (alternateAddress != null) {
			Address newAddress2 = getNewAddress(alternateAddress);
			updatedLearnerProfile.setLearnerAddress2(newAddress2);
		} else {
			Address newAddress2 = existingUser.getLearner().getLearnerProfile().getLearnerAddress2();
			updatedLearnerProfile.setLearnerAddress2(newAddress2);
		}
		
		return updatedLearnerProfile;
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
		if(!CollectionUtils.isEmpty(roles2Remove)){
			roles.removeAll(roles2Remove);
		}
		user.setLmsRoles(roles);
	}
	
	private boolean isCorrectPassword(String password) throws Exception {
		
		if (password != null) {
			if ( !FieldsValidation.isPasswordCorrect(password) ) {
				String error = "Password must contain alphabets and numbers and must be at least 8 characters long";
				throw new Exception(error);
	        }
		}
		
		return true;
	}
		
}
