package com.softech.vu360.lms.webservice.endpoint.soap.lmsapi;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.service.lmsapi.validation.LmsApiAuthenticationService;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.AddUserGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.AddUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.DeleteUserGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.DeleteUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.GetUserGroupByIdRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.GetUserGroupByIdResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.GetUserGroupIdByNameRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.GetUserGroupIdByNameResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.UpdateUserGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.usergroup.UpdateUserGroupResponse;

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
public class UserGroupSoapEndpoint {

	private static final Logger log = Logger.getLogger(UserGroupSoapEndpoint.class.getName());
	
	/**
	 * This is the namespace we defined in our UserGroupServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	private static final String USER_GROUP_TARGET_NAMESPACE = "http://usergroup.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	private static final String ADD_USER_GROUP_LOCAL_PART = "AddUserGroupRequest";
	private static final String UPDATE_USER_GROUP_LOCAL_PART = "UpdateUserGroupRequest";
	private static final String DELETE_USER_GROUP_LOCAL_PART = "DeleteUserGroupRequest";
	private static final String GET_USER_GROUP_BY_ID_LOCAL_PART = "GetUserGroupByIdRequest";
	private static final String GET_USER_GROUP_ID_BY_NAME_LOCAL_PART = "GetUserGroupIdByNameRequest";
	
	@Autowired
	private LmsApiAuthenticationService lmsApiAuthenticationService;
	
	public LmsApiAuthenticationService getLmsApiAuthenticationService() {
		return lmsApiAuthenticationService;
	}

	public void setLmsApiAuthenticationService(LmsApiAuthenticationService lmsApiAuthenticationService) {
		this.lmsApiAuthenticationService = lmsApiAuthenticationService;
	}
	
	//@PayloadRoot(namespace = USER_GROUP_TARGET_NAMESPACE, localPart = ADD_USER_GROUP_LOCAL_PART)
	public AddUserGroupResponse addUserGroup(AddUserGroupRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for addUserGroup");
		
		AddUserGroupResponse response = new AddUserGroupResponse();
		return response;
		
	}
	
	//@PayloadRoot(namespace = USER_GROUP_TARGET_NAMESPACE, localPart = UPDATE_USER_GROUP_LOCAL_PART)
	public UpdateUserGroupResponse updateUserGroup(UpdateUserGroupRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for updateUserGroup");
		
		UpdateUserGroupResponse response = new UpdateUserGroupResponse();
		return response;
	}
	
	//@PayloadRoot(namespace = USER_GROUP_TARGET_NAMESPACE, localPart = DELETE_USER_GROUP_LOCAL_PART)
	public DeleteUserGroupResponse deleteUserGroup(DeleteUserGroupRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for deleteUserGroup");
		
		DeleteUserGroupResponse response = new DeleteUserGroupResponse();
		return response;
	}
	
	//@PayloadRoot(namespace = USER_GROUP_TARGET_NAMESPACE, localPart = GET_USER_GROUP_BY_ID_LOCAL_PART)
	public GetUserGroupByIdResponse getUserGroupById(GetUserGroupByIdRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for getUserGroupById");
		
		GetUserGroupByIdResponse response = new GetUserGroupByIdResponse();
		return response;
	}
	
	//@PayloadRoot(namespace = USER_GROUP_TARGET_NAMESPACE, localPart = GET_USER_GROUP_ID_BY_NAME_LOCAL_PART)
	public GetUserGroupIdByNameResponse getUserGroupIdByName(GetUserGroupIdByNameRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for getUserGroupIdByName");
		
		GetUserGroupIdByNameResponse response = new GetUserGroupIdByNameResponse();
		return response;
	}
	
}
