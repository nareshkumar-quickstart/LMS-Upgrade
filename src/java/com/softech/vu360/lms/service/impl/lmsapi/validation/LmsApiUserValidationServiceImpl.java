package com.softech.vu360.lms.service.impl.lmsapi.validation;

import static com.softech.vu360.lms.webservice.validation.lmsapi.LmsApiUserPredicate.*;
import static com.softech.vu360.lms.webservice.validation.lmsapi.LmsApiOrganizationalGroupsPredicate.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.lmsapi.LmsApiUserValidationService;
import com.softech.vu360.lms.webservice.message.lmsapi.types.address.Address;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;

@Service
public class LmsApiUserValidationServiceImpl implements LmsApiUserValidationService {

	@Autowired
	private VU360UserService vu360UserService;
	
	@Autowired
	private ActiveDirectoryService activeDirectoryService;
	
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public void setActiveDirectoryService(ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}
	
	@Override
	public Map<Boolean, List<User>> getAddUserMap(List<User> users, VU360User manager) {
		
		List<User> validUsers = new ArrayList<>();
		List<User> invalidUsers = new ArrayList<>();
		Map<Boolean, List<User>> userMap = new LinkedHashMap<>();
		
		for (User user: users) {
			boolean validUser = false;
			String userName = user.getUserName();
			OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
			boolean isValidUserCredentials = isValidUserCredentials(user);
			boolean isUserExistInLms = findUserInLms(userName);
			boolean isUserExistInAD = activeDirectoryService.findADUser(userName);
			if (isValidUserCredentials && !isUserExistInLms && !isUserExistInAD) {
				if (isValidLmsApiOrgGroup(organizationalGroups, manager)) {
					validUser = true;
				} 
			}
			
			if (validUser) {
				validUsers.add(user);
			} else {
				invalidUsers.add(user);
			}
			
		} //end of for
		
		if (!CollectionUtils.isEmpty(validUsers)) {
			userMap.put(Boolean.TRUE, validUsers);
		}
		
		if (!CollectionUtils.isEmpty(invalidUsers)) {
			userMap.put(Boolean.FALSE, invalidUsers);
		}
		
		return userMap;
	}
	
	@Override
	public Map<Boolean, List<User>> getAddUserLmsOnlyMap(List<User> users, VU360User manager) {
		
		List<User> validUsers = new ArrayList<>();
		List<User> invalidUsers = new ArrayList<>();
		Map<Boolean, List<User>> userMap = new LinkedHashMap<>();
		
		for (User user: users) {
			boolean validUser = false;
			String userName = user.getUserName();
			OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
			boolean isValidUserCredentials = isValidUserCredentials(user);
			boolean isUserExistInLms = findUserInLms(userName);
			if (isValidUserCredentials && !isUserExistInLms) {
				if (isValidLmsApiOrgGroup(organizationalGroups, manager)) {
					validUser = true;
				} 
			}
			
			if (validUser) {
				validUsers.add(user);
			} else {
				invalidUsers.add(user);
			}
			
		} // end of for
		
		if (!CollectionUtils.isEmpty(validUsers)) {
			userMap.put(Boolean.TRUE, validUsers);
		}
		
		if (!CollectionUtils.isEmpty(invalidUsers)) {
			userMap.put(Boolean.FALSE, invalidUsers);
		}
		
		return userMap;
		
	}
	
	@Override
	public Map<Boolean, List<UpdateableUser>> getUpdateableUserMap(List<UpdateableUser> updateableUsers, VU360User manager) {
		
		List<UpdateableUser> validUpdateableUsers = new ArrayList<>();
		List<UpdateableUser> invalidUpdateableUsers = new ArrayList<>();
		Map<Boolean, List<UpdateableUser>> updateableUserMap = new LinkedHashMap<>();
		for (UpdateableUser updateableUser: updateableUsers) {
			boolean validUser = false;
			String userName = updateableUser.getUserName();
			OrganizationalGroups organizationalGroups = updateableUser.getOrganizationalGroups();
			boolean isValidUserCredentials = isValidUpdateableUserCredentials(updateableUser);
			boolean isUserExistInLms = findUserInLms(userName);
			if (isValidUserCredentials && isUserExistInLms) {
				if (isValidLmsApiOrgGroup(organizationalGroups, manager)) {
					validUser = true;
				} 
			}
			
			if (validUser) {
				validUpdateableUsers.add(updateableUser);
			} else {
				invalidUpdateableUsers.add(updateableUser);
			}
			
		}
		
		if (!CollectionUtils.isEmpty(validUpdateableUsers)) {
			updateableUserMap.put(Boolean.TRUE, validUpdateableUsers);
		}
		
		if (!CollectionUtils.isEmpty(invalidUpdateableUsers)) {
			updateableUserMap.put(Boolean.FALSE, invalidUpdateableUsers);
		}
		
		return updateableUserMap;
		
	}

	@Override
	public List<User> getValidUsers(Map<Boolean, List<User>> addUserMap) {
		return getUsers(addUserMap, Boolean.TRUE);
	}
	
	@Override
	public List<UpdateableUser> getValidUpdateableUsers(Map<Boolean, List<UpdateableUser>> updateableUserMap) {
		return getUpdateableUsers(updateableUserMap, Boolean.TRUE);
	}

	@Override
	public Map<User, String> getInvalidUsersForAddUser(String customerCode, VU360User manager, Map<Boolean, List<User>> addUserMap) {
		Map<User, String> invalidUsersMap = null;
		List<User> invalidUsers = getUsers(addUserMap, Boolean.FALSE);
		if (!CollectionUtils.isEmpty(invalidUsers)) {
			invalidUsersMap = new HashMap<>();
			for (User user : invalidUsers) {
				String errorMessage = getUserErrorMessage(customerCode, manager, user);
				if (StringUtils.isBlank(errorMessage)) {
					String userName = user.getUserName();
					boolean isUserExistInLms = findUserInLms(userName);
					if (isUserExistInLms) {
						errorMessage = "Unable to add. User Already Exist: " + userName;
					} else {
						boolean isUserExistInAD = activeDirectoryService.findADUser(userName);
						if (isUserExistInAD) {
							errorMessage = "Unable to add. User Already Exist in AD: " + userName;
						}	
					}
				}
				invalidUsersMap.put(user, errorMessage);
			} //end of for()
		}
		return invalidUsersMap;
	}
	
	@Override
	public Map<User, String> getInvalidUsersForAddUserLmsOnly(String customerCode, VU360User manager, Map<Boolean, List<User>> addUserMap) {
		Map<User, String> invalidUsersMap = null;
		List<User> invalidUsers = getUsers(addUserMap, Boolean.FALSE);
		if (!CollectionUtils.isEmpty(invalidUsers)) {
			invalidUsersMap = new HashMap<>();
			for (User user : invalidUsers) {
				String errorMessage = getUserErrorMessage(customerCode, manager, user);
				if (StringUtils.isBlank(errorMessage)) {
					String userName = user.getUserName();
					boolean isUserExistInLms = findUserInLms(userName);
					if (isUserExistInLms) {
						errorMessage = "Unable to add. User Already Exist: " + userName;
					} 
				}
				invalidUsersMap.put(user, errorMessage);
			} //end of for()
		}
		return invalidUsersMap;
	}

	@Override
	public Map<UpdateableUser, String> getInvalidUpdateableUsers(String customerCode, VU360User manager, Map<Boolean, List<UpdateableUser>> updateableUserMap) {
		
		Map<UpdateableUser, String> invalidUsersMap = null;
		List<UpdateableUser> invalidUpdateableUsers = getUpdateableUsers(updateableUserMap, Boolean.FALSE);
		if (!CollectionUtils.isEmpty(invalidUpdateableUsers)) {
			invalidUsersMap = new HashMap<>();
			for (UpdateableUser user : invalidUpdateableUsers) {
				String accountExpiryDate = null;
				String firstName = user.getFirstName();
				String middleName = user.getMiddleName();
				String lastName = user.getLastName();
				String emailAddress = user.getEmailAddress();
				String phone = user.getPhone();
				String mobilePhone = user.getMobilePhone();
				String extension = user.getExtension();
				Address address1 = user.getAddress();
				Address alternateAddress = user.getAlternateAddress();
				String userName = user.getUserName();
				String password = user.getPassword();
				OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
				
				XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
				if (accountExpirationDate != null) {
					accountExpiryDate = accountExpirationDate.toString();
				}
				
				String errorMessage = getUserCredentialsErrorMessage(userName, emailAddress, firstName, middleName, lastName, phone, extension, mobilePhone, address1, alternateAddress, accountExpirationDate, accountExpiryDate);
				if (StringUtils.isBlank(errorMessage)) {
					if (password != null) {
						if (!isValidPasswordPattern(password)) {
							errorMessage = "Password must contain alphabets and numbers and must be at least 8 characters long";
						}
					} 
					
					if (StringUtils.isBlank(errorMessage)) {
						errorMessage = getOrgGroupErrorMessage(customerCode, manager, organizationalGroups);
					}
				}
				
				if (StringUtils.isBlank(errorMessage)) {
					boolean isUserExistInLms = findUserInLms(userName);
					if (!isUserExistInLms) {
						errorMessage = "Unable to Update. User doesn't exist. Invalid user name: " + userName;
					} 
				}
				invalidUsersMap.put(user, errorMessage);
			} //end of for()
		}
		return invalidUsersMap;
		
	}
	
	private String getUserErrorMessage(String customerCode, VU360User manager, User user) {
		
		String accountExpiryDate = null;
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		Address address1 = user.getAddress();
		Address alternateAddress = user.getAlternateAddress();
		String userName = user.getUserName();
		String password = user.getPassword();
		OrganizationalGroups organizationalGroups = user.getOrganizationalGroups();
		
		XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
		if (accountExpirationDate != null) {
			accountExpiryDate = accountExpirationDate.toString();
		}
		
		String errorMessage = getUserCredentialsErrorMessage(userName, emailAddress, firstName, middleName, lastName, phone, extension, mobilePhone, address1, alternateAddress, accountExpirationDate, accountExpiryDate);
		if (StringUtils.isBlank(errorMessage)) {
			if (!isValidPassword(password)) {
				errorMessage = "Password required";
			} else if (!isValidPasswordPattern(password)) {
				errorMessage = "Password must contain alphabets and numbers and must be at least 8 characters long";
			} else {
				errorMessage = getOrgGroupErrorMessage(customerCode, manager, organizationalGroups);
			}
		}
		return errorMessage;
	}
	
	private boolean isValidLmsApiOrgGroup(OrganizationalGroups organizationalGroups, VU360User manager) {
		
		boolean validUser = false;
		boolean isUserHasValidOrgGroupCredentials = isValidUserOrgGroupCredentials(organizationalGroups);
		if (isUserHasValidOrgGroupCredentials) {
			List<String> orgGroupHierarchies = organizationalGroups.getOrgGroupHierarchy();
			if (isManagerHasSecurityRightsForAtleastOneOrgGroupHierarchy(manager, orgGroupHierarchies)) {
				validUser = true;
			}
		}
		return validUser;
		
	}
	
	private String getUserCredentialsErrorMessage(String userName, String emailAddress, String firstName, String middleName, 
			String lastName, String phone, String extension, String mobilePhone, Address address1, Address alternateAddress, 
			XMLGregorianCalendar accountExpirationDate, String accountExpiryDate) {
		
		String errorMessage = "";
		
		if (!isValidUserName(userName)) {
			errorMessage = "User name required";
		} else if (isInValidGlobalName(userName)) {
			errorMessage = "Bad characters not allowed (Username)";
		} else if (!isValidEmailAddress(emailAddress)) {
			errorMessage = "Email address required";
		} else if (!isValidEmailAddressPattern(emailAddress)) {
			errorMessage = "Invalid email address";
		} else if (!isValidFirstName(firstName)) {
			errorMessage = "First Name required";
		} else if (!isValidMiddleName(middleName)) {
			errorMessage = "Bad characters not allowed (Middle Name)";
		} else if (!isValidLastName(lastName)) {
			errorMessage = "Last Name required";
		} else if (isInValidGlobalName(firstName)) {
			errorMessage = "Bad characters not allowed (First name)";
		} else if (isInValidGlobalName(lastName)) {
			errorMessage = "Bad characters not allowed (Last name)";
		} else if (!isValidPhone(phone)) {
			errorMessage = "Bad characters not allowed (Office Phone)";
		} else if (!isValidExtension(extension)) {
			errorMessage = "Bad characters not allowed (Phone Extension)";
		} else if (!isValidMobilePhone(mobilePhone)) {
			errorMessage = "Bad characters not allowed (Mobile Phone)";
		} else if (!isValidStreetAddress1(address1)) {
			errorMessage = "Bad characters not allowed (Street Address1)";
		} else if (!isValidStreetAddress2(address1)) {
			errorMessage = "Bad characters not allowed (Street Address2)";
		} else if (!isValidCity(address1)) {
			errorMessage = "Bad characters not allowed (City)";
		} else if (!isValidCountryAndZipCode(address1)) {
			errorMessage = "ZIP CODE FAILED";
		} else if (!isValidStreetAddress1(alternateAddress)) {
			errorMessage = "Bad characters not allowed (Street Address1)";
		} else if (!isValidStreetAddress2(alternateAddress)) {
			errorMessage = "Bad characters not allowed (Street Address2)";
		} else if (!isValidCity(alternateAddress)) {
			errorMessage = "Bad characters not allowed (City)";
		} else if (!isValidCountryAndZipCode(alternateAddress)) {
			errorMessage = "ZIP CODE FAILED";
		} else if (!isValidAccountExpirationDateFormat(accountExpiryDate)) {
			errorMessage = "Invalid Date format. Format should be yyyy-MM-dd";
		} else if (!isAccountExpirationDateBeforeTodayDate(accountExpiryDate)) {
			errorMessage = "Invalid Date. Account Expiration date is before today date";
		} 
		
		return errorMessage;
	}
	
	private String getOrgGroupErrorMessage(String customerCode, VU360User manager, OrganizationalGroups organizationalGroups) {
		String errorMessage = "";
		if (!isValidOrganizationalGroups(organizationalGroups)) {
			errorMessage = "OrganizationalGroups elemnt is required";
		} else if (!isValidOrgGroupHierarchy(organizationalGroups)) {
			errorMessage = "Atleast one OrgGroupHierarchy is required";
		} else {
			List<String> orgGroupHierarchies = organizationalGroups.getOrgGroupHierarchy();
			if (!isManagerHasSecurityRightsForAtleastOneOrgGroupHierarchy(manager, orgGroupHierarchies)) {
				errorMessage = "Manager for customer: " + customerCode + " do not have rigths to create organizational group: " + orgGroupHierarchies;
			}
		}
		return errorMessage;
	}
	
	private boolean isValidUserCredentials(User user) {
		
		String accountExpiryDate = null;
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		Address address1 = user.getAddress();
		Address alternateAddress = user.getAlternateAddress();
		String userName = user.getUserName();
		String password = user.getPassword();
		
		XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
		if (accountExpirationDate != null) {
			accountExpiryDate = accountExpirationDate.toString();
		}
		
		boolean validCredentials = isValidCredentials(userName, emailAddress, firstName, middleName, lastName, phone, extension, mobilePhone, address1, alternateAddress, accountExpirationDate, accountExpiryDate);
		if (validCredentials) {
			validCredentials = isValidPassword(password) && isValidPasswordPattern(password);
		}
		
		return validCredentials;
	}
	
	private boolean isValidUpdateableUserCredentials(UpdateableUser user) {
		
		String accountExpiryDate = null;
		String firstName = user.getFirstName();
		String middleName = user.getMiddleName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		String phone = user.getPhone();
		String mobilePhone = user.getMobilePhone();
		String extension = user.getExtension();
		Address address1 = user.getAddress();
		Address alternateAddress = user.getAlternateAddress();
		String userName = user.getUserName();
		String password = user.getPassword();
		
		XMLGregorianCalendar accountExpirationDate = user.getExpirationDate();
		if (accountExpirationDate != null) {
			accountExpiryDate = accountExpirationDate.toString();
		}
		
		boolean validCredentials = isValidCredentials(userName, emailAddress, firstName, middleName, lastName, phone, extension, mobilePhone, address1, alternateAddress, accountExpirationDate, accountExpiryDate);
		
		if (validCredentials) {
			if (password != null) {
				validCredentials = 	isValidPasswordPattern(password);
			} 
		}
		
		return validCredentials;
	}
	
	/**
	 * If values provided then all provided values should be valid
	 */
	private boolean isValidCredentials(String userName, String emailAddress, String firstName, String middleName, 
			String lastName, String phone, String extension, String mobilePhone, Address address1, Address alternateAddress, 
			XMLGregorianCalendar accountExpirationDate, String accountExpiryDate) {
		
		return isValidUserName(userName) && 
		!isInValidGlobalName(userName) &&
		isValidEmailAddress(emailAddress) &&
		isValidEmailAddressPattern(emailAddress) &&
		isValidFirstName(firstName) &&
		isValidMiddleName(middleName) &&
		isValidLastName(lastName) &&
		!isInValidGlobalName(firstName) &&
		!isInValidGlobalName(lastName) &&
		isValidPhone(phone) &&
		isValidExtension(extension) &&
		isValidMobilePhone(mobilePhone) &&
		isValidStreetAddress1(address1) &&
		isValidStreetAddress2(address1) &&
		isValidCity(address1) &&
		isValidCountryAndZipCode(address1) &&
		isValidStreetAddress1(alternateAddress) &&
		isValidStreetAddress2(alternateAddress) &&
		isValidCity(alternateAddress) &&
		isValidCountryAndZipCode(alternateAddress) &&
		isValidAccountExpirationDateFormat(accountExpiryDate) &&
		isAccountExpirationDateBeforeTodayDate(accountExpiryDate);
	}
	
	private boolean isValidUserOrgGroupCredentials(OrganizationalGroups organizationalGroups) {
		return isValidOrganizationalGroups(organizationalGroups) && isValidOrgGroupHierarchy(organizationalGroups);
	}
	
	private boolean findUserInLms(String userName){
		boolean userExist = false;
		VU360User vu360User = vu360UserService.findUserByUserName(userName); 
		if (vu360User != null) {
			userExist = true;
		}
		
		return userExist;
	}
	
	private List<User> getUsers(Map<Boolean, List<User>> addUserMap, Boolean key) {
		
		List<User> studentsList = null;
		if (!CollectionUtils.isEmpty(addUserMap)) {
			studentsList = addUserMap.get(key);
		}
	
		return studentsList;
		
	}
	
	private List<UpdateableUser> getUpdateableUsers(Map<Boolean, List<UpdateableUser>> updateableUserMap, Boolean key) {
		
		List<UpdateableUser> studentsList = null;
		if (!CollectionUtils.isEmpty(updateableUserMap)) {
			studentsList = updateableUserMap.get(key);
		}
	
		return studentsList;
		
	}
	
}
