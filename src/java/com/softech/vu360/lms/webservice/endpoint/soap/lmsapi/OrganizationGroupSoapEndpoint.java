package com.softech.vu360.lms.webservice.endpoint.soap.lmsapi;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.service.lmsapi.validation.LmsApiAuthenticationService;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.AddOrgGroupByHierarchyRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.AddOrgGroupByHierarchyResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.AddOrgGroupByParentIdRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.AddOrgGroupByParentIdResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.DeleteOrganizationGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.DeleteOrganizationGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.GetOrgGroupByIdRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.GetOrgGroupByIdResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.GetOrgGroupIdByNameRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.GetOrgGroupIdByNameResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.UpdateOrganizationGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.orggroup.UpdateOrganizationGroupResponse;

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
public class OrganizationGroupSoapEndpoint {

	private static final Logger log = Logger.getLogger(OrganizationGroupSoapEndpoint.class.getName());
	
	/**
	 * This is the namespace we defined in our OrgGroupServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	private static final String ORG_GROUP_TARGET_NAMESPACE = "http://orggroup.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	private static final String ADD_ORG_GROUP_BY_HIERARCHY_LOCAL_PART = "AddOrgGroupByHierarchyRequest";
	private static final String ADD_ORG_GROUP_BY_PARENT_ID_LOCAL_PART = "AddOrgGroupByParentIdRequest";
	private static final String UPDATE_ORGANIZATION_GROUP_LOCAL_PART = "UpdateOrganizationGroupRequest";
	private static final String DELETE_ORGANIZATION_GROUP_LOCAL_PART = "DeleteOrganizationGroupRequest";
	private static final String GET_ORG_GROUP_BY_ID_LOCAL_PART = "GetOrgGroupByIdRequest";
	private static final String GET_ORG_GROUP_ID_BY_LOCAL_PARTT = "GetOrgGroupIdByNameRequest";
	
	@Autowired
	private LmsApiAuthenticationService lmsApiAuthenticationService;
	
	public LmsApiAuthenticationService getLmsApiAuthenticationService() {
		return lmsApiAuthenticationService;
	}

	public void setLmsApiAuthenticationService(LmsApiAuthenticationService lmsApiAuthenticationService) {
		this.lmsApiAuthenticationService = lmsApiAuthenticationService;
	}
	
	//@PayloadRoot(namespace = ORG_GROUP_TARGET_NAMESPACE, localPart = ADD_ORG_GROUP_BY_HIERARCHY_LOCAL_PART)
	public AddOrgGroupByHierarchyResponse addOrgGroupByHierarchy(AddOrgGroupByHierarchyRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for addOrgGroupByHierarchy");
		
		AddOrgGroupByHierarchyResponse response = new AddOrgGroupByHierarchyResponse();
		return response;
	}
	
	//@PayloadRoot(namespace = ORG_GROUP_TARGET_NAMESPACE, localPart = ADD_ORG_GROUP_BY_PARENT_ID_LOCAL_PART)
	public AddOrgGroupByParentIdResponse addOrgGroupByParentId(AddOrgGroupByParentIdRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for addOrgGroupByParentId");
		
		AddOrgGroupByParentIdResponse response = new AddOrgGroupByParentIdResponse();
		return response;
	}
	
	//@PayloadRoot(namespace = ORG_GROUP_TARGET_NAMESPACE, localPart = UPDATE_ORGANIZATION_GROUP_LOCAL_PART)
	public UpdateOrganizationGroupResponse updateOrganizationGroup(UpdateOrganizationGroupRequest request) {
	
		log.info("Request received at " + getClass().getName() + " for updateOrganizationGroup");
		
		UpdateOrganizationGroupResponse response = new UpdateOrganizationGroupResponse();
		return response;
	}
	
	//@PayloadRoot(namespace = ORG_GROUP_TARGET_NAMESPACE, localPart = DELETE_ORGANIZATION_GROUP_LOCAL_PART)
	public DeleteOrganizationGroupResponse deleteOrganizationGroup(DeleteOrganizationGroupRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for deleteOrganizationGroup");
		
		DeleteOrganizationGroupResponse response = new DeleteOrganizationGroupResponse();
		return response;
	}
	
	//@PayloadRoot(namespace = ORG_GROUP_TARGET_NAMESPACE, localPart = GET_ORG_GROUP_BY_ID_LOCAL_PART)
	public GetOrgGroupByIdResponse getOrgGroupById(GetOrgGroupByIdRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for getOrgGroupById");
		
		GetOrgGroupByIdResponse response = new GetOrgGroupByIdResponse();
		return response;
	}
	
	//@PayloadRoot(namespace = ORG_GROUP_TARGET_NAMESPACE, localPart = GET_ORG_GROUP_ID_BY_LOCAL_PARTT)
	public GetOrgGroupIdByNameResponse getOrgGroupIdByName(GetOrgGroupIdByNameRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for getOrgGroupIdByName");
		
		GetOrgGroupIdByNameResponse response = new GetOrgGroupIdByNameResponse();
		return response;
	}

	
}
