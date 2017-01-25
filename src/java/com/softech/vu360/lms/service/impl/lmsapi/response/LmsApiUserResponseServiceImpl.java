package com.softech.vu360.lms.service.impl.lmsapi.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.service.lmsapi.response.LmsApiUserResponseService;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserLmsOnlyResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.UpdateUserResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.RegisterOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.RegisterOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.transactionresult.TransactionResultType;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUsers;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;

@Service
public class LmsApiUserResponseServiceImpl implements LmsApiUserResponseService {

	private static final String ERROR_CODE_ZERO  = "0";
	private static final String ERROR_CODE_ONE  = "1";
	
	@Override
	public AddUserResponse getAddUserResponse(String message) {
		
		AddUserResponse response = new AddUserResponse();
		response.setTransactionResult(TransactionResultType.FAILURE);
		response.setTransactionResultMessage(message);
		return response;
	}

	@Override
	public AddUserResponse getAddUserResponse(List<RegisterUser> registerUsers) {
		
		RegisterUsers lmsApiregisterUsers = new RegisterUsers();
		lmsApiregisterUsers.setRegisterUser(registerUsers);
		
		AddUserResponse response = new AddUserResponse();
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage("Success");
		response.setRegisterUsers(lmsApiregisterUsers);
		
		return response;
	}
	
	@Override
	public AddUserLmsOnlyResponse getAddUserLmsOnlyResponse(String message) {
		
		AddUserLmsOnlyResponse response = new AddUserLmsOnlyResponse();
		response.setTransactionResult(TransactionResultType.FAILURE);
		response.setTransactionResultMessage(message);
		return response;
	}
	
	@Override
	public AddUserLmsOnlyResponse getAddUserLmsOnlyResponse(List<RegisterUser> registerUsers) {
		
		RegisterUsers lmsApiregisterUsers = new RegisterUsers();
		lmsApiregisterUsers.setRegisterUser(registerUsers);
		
		AddUserLmsOnlyResponse response = new AddUserLmsOnlyResponse();
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage("Success");
		response.setRegisterUsers(lmsApiregisterUsers);
		
		return response;
	}
	
	@Override
	public UpdateUserResponse getUpdateUserResponse(String message) {
		UpdateUserResponse response = new UpdateUserResponse();
		response.setTransactionResult(TransactionResultType.FAILURE);
		response.setTransactionResultMessage(message);
		return response;
	}
	
	@Override
	public UpdateUserResponse getUpdateUserResponse(List<RegisterUser> registerUsers) {
		
		RegisterUsers lmsApiregisterUsers = new RegisterUsers();
		lmsApiregisterUsers.setRegisterUser(registerUsers);
		
		UpdateUserResponse response = new UpdateUserResponse();
		response.setTransactionResult(TransactionResultType.SUCCESS);
		response.setTransactionResultMessage("Success");
		response.setRegisterUsers(lmsApiregisterUsers);
		
		return response;
		
	}

	@Override
	public RegisterOrganizationalGroups getRegisterOrganizationalGroups(List<String> validOrgGroupHierarchies, Map<String, String> invalidOrgGroupHierarchies) {
		List<RegisterOrganizationalGroup> registerOrganizationalGroupErrors = getResponseRegisterOrganizationalGroups(validOrgGroupHierarchies, invalidOrgGroupHierarchies);
		RegisterOrganizationalGroups registerOrganizationalGroups = new RegisterOrganizationalGroups();
		registerOrganizationalGroups.setRegisterOrganizationalGroup(registerOrganizationalGroupErrors);
		return registerOrganizationalGroups;
	}
	
	@Override
	public RegisterUser getRegisterUser(String errorCode, String errorMessage, User user) {
		
		String userName = user.getUserName();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		
		return getRegisterUser(userName, firstName, lastName, emailAddress, errorCode, errorMessage);
	}
	
	@Override
	public RegisterUser getRegisterUser(String errorCode, String errorMessage, UpdateableUser user) {
		
		String userName = user.getUserName();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String emailAddress = user.getEmailAddress();
		
		return getRegisterUser(userName, firstName, lastName, emailAddress, errorCode, errorMessage);
	}
	
	private RegisterUser getRegisterUser(String userName, String firstName, String lastName, String emailAddress, String errorCode, String errorMessage) {
		
		RegisterUser registerUser = new RegisterUser();
		registerUser.setErrorCode(errorCode);
		registerUser.setErrorMessage(errorMessage);
		registerUser.setUserName(userName);
		registerUser.setFirstName(firstName);
		registerUser.setLastName(lastName);
		registerUser.setEmailAddress(emailAddress);
		return registerUser;
	}
	
	private List<RegisterOrganizationalGroup> getResponseRegisterOrganizationalGroups(List<String> validOrgGroupHierarchies, Map<String, String> invalidOrgGroupHierarchies) {
		
		List<RegisterOrganizationalGroup> registerOrganizationalGroups = new ArrayList<>();
		
		if (!CollectionUtils.isEmpty(validOrgGroupHierarchies)) {
			for (String orgGroupHierarchy : validOrgGroupHierarchies) {
				RegisterOrganizationalGroup registerOrgGroup = getRegisterOrganizationalGroup(ERROR_CODE_ZERO, "", orgGroupHierarchy);
				registerOrganizationalGroups.add(registerOrgGroup);
			}
		}
		
		if (!CollectionUtils.isEmpty(invalidOrgGroupHierarchies)) {
			for (Map.Entry<String, String> entry : invalidOrgGroupHierarchies.entrySet()) {
				String orgGroupHierarchy = entry.getKey();
				String errorMessage = entry.getValue();
				RegisterOrganizationalGroup registerOrgGroupError = getRegisterOrganizationalGroup(ERROR_CODE_ONE, errorMessage, orgGroupHierarchy);
				registerOrganizationalGroups.add(registerOrgGroupError);
			}
		}
		
		return registerOrganizationalGroups;
	}
	
	private RegisterOrganizationalGroup getRegisterOrganizationalGroup(String errorCode, String errorMessage, String orgGroupHierarchy) {
		RegisterOrganizationalGroup registerOrgGroup = new RegisterOrganizationalGroup();
		registerOrgGroup.setErrorCode(errorCode);
		registerOrgGroup.setErrorMessage(errorMessage);
		registerOrgGroup.setOrgGroupHierarchy(orgGroupHierarchy);
		return registerOrgGroup;
	}
	
}
