package com.softech.vu360.lms.webservice.endpoint.soap.lmsapi;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.lmsapi.LmsApiUserValidationService;
import com.softech.vu360.lms.service.lmsapi.response.LmsApiUserResponseService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiAuthenticationService;
import com.softech.vu360.lms.service.lmsapi.LmsApiUserService;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserLmsOnlyRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserLmsOnlyResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.UpdateUserRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.UpdateUserResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUsers;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.Users;

/**
 * Endpoints are the key concept of Spring Web Service’s server support. Unlike controllers, whose handler methods are directly tied
 * to HTTP requests and responses, Spring Web Service SOAP endpoints can serve SOAP requests made via HTTP, XMPP, SMTP, JMS, and 
 * more. Just like @Controller marks a controller whose @RequestMapping methods should be scanned and mapped to requests, @Endpoint 
 * marks an endpoint whose @org.springframework.ws.server.endpoint.annotation.PayloadRoot methods, 
 * @org.springframework.ws.soap.server.endpoint.annotation.SoapAction methods, and/or 
 * @org.springframework.ws.soap.addressing.server.annotation.Action methods are handlers for incoming SOAP requests on any protocol. 
 * Endpoint methods’ parameters correspond to elements of the request, whereas return types indicate response contents.
 * 
 * @author basit.ahmed
 *
 */
@Endpoint
public class VU360UserSoapEndpoint {

	private static final Logger log = Logger.getLogger(VU360UserSoapEndpoint.class.getName());
	
	/**
	 * This is the namespace we defined in our UserServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	private static final String VU360USER_TARGET_NAMESPACE = "http://user.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	private static final String ADD_USER_LOCAL_PART = "AddUserRequest";
	private static final String ADD_USER_LMS_ONLY_LOCAL_PART= "AddUserLmsOnlyRequest";
	private static final String UPDATE_USER_LOCAL_PART = "UpdateUserRequest";
	
	@Autowired
	private LmsApiAuthenticationService lmsApiAuthenticationService;
	
	@Autowired
	private LmsApiUserService lmsApiUserService;
	
	@Autowired
	private LmsApiUserValidationService lmsApiUserValidationService;
	
	@Autowired
	private VU360UserService vu360UserService;
	
	@Autowired
	private LmsApiUserResponseService lmsApiUserResponseService;
	
	public void setLmsApiAuthenticationService(LmsApiAuthenticationService lmsApiAuthenticationService) {
		this.lmsApiAuthenticationService = lmsApiAuthenticationService;
	}
	
	public void setLmsApiUserService(LmsApiUserService lmsApiUserService) {
		this.lmsApiUserService = lmsApiUserService;
	}

	public void setLmsApiUserValidationService(LmsApiUserValidationService lmsApiUserValidationService) {
		this.lmsApiUserValidationService = lmsApiUserValidationService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	public void setLmsApiUserResponseService(LmsApiUserResponseService lmsApiUserResponseService) {
		this.lmsApiUserResponseService = lmsApiUserResponseService;
	}

	@PayloadRoot(namespace = VU360USER_TARGET_NAMESPACE, localPart = ADD_USER_LOCAL_PART)
	public AddUserResponse addUser(AddUserRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for addUser");
		
		AddUserResponse response = null;
		Users users = request.getUsers();
		List<User> userList = users.getUser();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {
			Customer customer = lmsApiAuthenticationService.authenticateByApiKeyAndCustomerCode(apiKey, customerCode);
			String customerGuid = customer.getCustomerGUID();
			VU360User manager = vu360UserService.getUserByGUID(customerGuid);
			Map<Boolean, List<User>> usersMap = lmsApiUserValidationService.getAddUserMap(userList, manager);
			List<RegisterUser> registerUsers = lmsApiUserService.addUsers(customer, manager, usersMap);
			response = lmsApiUserResponseService.getAddUserResponse(registerUsers);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			response = lmsApiUserResponseService.getAddUserResponse(errorMessage);
		}
		
		return response;
	}
	
	@PayloadRoot(namespace = VU360USER_TARGET_NAMESPACE, localPart = ADD_USER_LMS_ONLY_LOCAL_PART)
	public AddUserLmsOnlyResponse addUserLmsOnly(AddUserLmsOnlyRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for addUserLmsOnly");
		
		AddUserLmsOnlyResponse response = null;
		Users users = request.getUsers();
		List<User> userList = users.getUser();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {
			Customer customer = lmsApiAuthenticationService.authenticateByApiKeyAndCustomerCode(apiKey, customerCode);
			String customerGuid = customer.getCustomerGUID();
			VU360User manager = vu360UserService.getUserByGUID(customerGuid);
			Map<Boolean, List<User>> lmsOnlyUsersMap = lmsApiUserValidationService.getAddUserLmsOnlyMap(userList, manager);
			List<RegisterUser> registerUsers = lmsApiUserService.addUsersLmsOnly(customer, manager, lmsOnlyUsersMap);
			response = lmsApiUserResponseService.getAddUserLmsOnlyResponse(registerUsers);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			response = lmsApiUserResponseService.getAddUserLmsOnlyResponse(errorMessage);
		}
		
		return response;
	}
	
	@PayloadRoot(namespace = VU360USER_TARGET_NAMESPACE, localPart = UPDATE_USER_LOCAL_PART)
	public UpdateUserResponse updateUser(UpdateUserRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for updateUser");
		
		UpdateUserResponse response = null;
		UpdateableUsers users = request.getUsers();
		List<UpdateableUser> updateableUsersList = users.getUpdateableUser();
		String customerCode = request.getCustomerCode();
		String apiKey = request.getKey();
		
		try {
			Customer customer = lmsApiAuthenticationService.authenticateByApiKeyAndCustomerCode(apiKey, customerCode);
			String customerGuid = customer.getCustomerGUID();
			VU360User manager = vu360UserService.getUserByGUID(customerGuid);
			Map<Boolean, List<UpdateableUser>> updateableUsersMap = lmsApiUserValidationService.getUpdateableUserMap(updateableUsersList, manager);
			List<RegisterUser> registerUsers = lmsApiUserService.updateUsers(customer, manager, updateableUsersMap);
			response = lmsApiUserResponseService.getUpdateUserResponse(registerUsers);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			response = lmsApiUserResponseService.getUpdateUserResponse(errorMessage);
		}
		
		return response;
	}
	
}
