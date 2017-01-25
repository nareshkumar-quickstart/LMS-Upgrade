package com.softech.vu360.lms.webservice.endpoint.soap.lmsapi;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.service.lmsapi.validation.LmsApiAuthenticationService;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.securityroles.AssignSecurityRoleToUsersRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.securityroles.AssignSecurityRoleToUsersResponse;

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
public class SecurityRolesSoapEndpoint {

	private static final Logger log = Logger.getLogger(SecurityRolesSoapEndpoint.class.getName());
	
	/**
	 * This is the namespace we defined in our SecurityRoleServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	private static final String SECURITY_ROLES_TARGET_NAMESPACE = "securityroles.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	private static final String ASSIGN_SECURITY_ROLE_TO_LEARNERS_LOCAL_PART = "AssignSecurityRoleToLearnersRequest";
	
	@Autowired
	private LmsApiAuthenticationService lmsApiAuthenticationService;
	
	public LmsApiAuthenticationService getLmsApiAuthenticationService() {
		return lmsApiAuthenticationService;
	}

	public void setLmsApiAuthenticationService(LmsApiAuthenticationService lmsApiAuthenticationService) {
		this.lmsApiAuthenticationService = lmsApiAuthenticationService;
	}
	
	//@PayloadRoot(namespace = SECURITY_ROLES_TARGET_NAMESPACE, localPart = ASSIGN_SECURITY_ROLE_TO_LEARNERS_LOCAL_PART)
	public AssignSecurityRoleToUsersResponse assignSecurityRoleToLearners(AssignSecurityRoleToUsersRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for assignSecurityRoleToLearners");
		
		AssignSecurityRoleToUsersResponse response = new AssignSecurityRoleToUsersResponse();
		return response;
		
	}
	
	
	
}
