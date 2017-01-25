package com.softech.vu360.lms.webservice.lmsapi;

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

public interface LmsApiOrgGroupWS {

	/**
	 * This is the namespace we defined in our OrgGroupServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	String ORG_GROUP_TARGET_NAMESPACE = "http://orggroup.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	String ADD_ORG_GROUP_BY_HIERARCHY_EVENT = "AddOrgGroupByHierarchyRequest";
	String ADD_ORG_GROUP_BY_PARENT_ID_EVENT = "AddOrgGroupByParentIdRequest";
	String UPDATE_ORGANIZATION_GROUP_EVENT = "UpdateOrganizationGroupRequest";
	String DELETE_ORGANIZATION_GROUP_EVENT = "DeleteOrganizationGroupRequest";
	String GET_ORG_GROUP_BY_ID_EVENT = "GetOrgGroupByIdRequest";
	String GET_ORG_GROUP_ID_BY_NAME_EVENT = "GetOrgGroupIdByNameRequest";
	
	AddOrgGroupByHierarchyResponse addOrgGroupByHierarchy(AddOrgGroupByHierarchyRequest addOrgByHierarchyRequest);
	AddOrgGroupByParentIdResponse addOrgGroupByParentId(AddOrgGroupByParentIdRequest addOrgGroupByParentIdRequest);
	UpdateOrganizationGroupResponse updateOrganizationGroup(UpdateOrganizationGroupRequest updateOrganizationGroupRequest);
	DeleteOrganizationGroupResponse deleteOrganizationGroup(DeleteOrganizationGroupRequest deleteOrganizationGroupRequest);
	GetOrgGroupByIdResponse getOrgGroupById(GetOrgGroupByIdRequest getOrgGroupByIdRequest);
	GetOrgGroupIdByNameResponse getOrgGroupIdByName(GetOrgGroupIdByNameRequest getOrgGroupIdByNameRequest);
	
}
