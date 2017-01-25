package com.softech.vu360.lms.webservice.lmsapi;

import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.securityroles.AssignSecurityRoleToUsersRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.securityroles.AssignSecurityRoleToUsersResponse;

public interface LmsApiSecurityRolesWS {

	/**
	 * This is the namespace we defined in our SecurityRoleServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	String SECURITY_ROLES_TARGET_NAMESPACE = "http://securityroles.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	String ASSIGN_SECURITY_ROLE_TO_LEARNERS_REQUEST = "AssignSecurityRoleToUsersRequest";
	
	AssignSecurityRoleToUsersResponse assignSecurityRoleToLearners(AssignSecurityRoleToUsersRequest assignSecurityRoleToLearnersRequest);
	
}
