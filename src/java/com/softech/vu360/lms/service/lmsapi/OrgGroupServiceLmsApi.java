package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.AddOrgGroupByParentIdOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.NewOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.ResponseOrganizationalGroup;

public interface OrgGroupServiceLmsApi {
	
	Boolean organizationalGroupsValidation(OrganizationalGroups organizationalGroups) throws Exception;

	boolean isOrganizationalGroupExist(Customer customer, String orgGroupHierarchy) throws Exception;
	boolean isRootOrganizationalGroupExist(Customer customer, String orgGroupHierarchy) throws Exception;
	Boolean hasManagerSecurityRightsForOrganization(Customer customer,	String orgGroupHierarchy) throws Exception;
	boolean hasPermissionToCreateOrgGroup(Customer customer, OrganizationalGroup organizationalGroup) throws Exception;
	
	String getRootOrgGroupName(String orgGroupHierarchy) throws Exception;
	String getOrgGrouptHierarchy(String orgGroupHierarchy) throws Exception;
	
	OrganizationalGroup getExistingOrganizationalGroup(Customer customer, String orgGroupHierarchy) throws Exception;
	OrganizationalGroup getRootOrganizationalGroup(Customer customer) throws Exception;
	OrganizationalGroup getOrganizationalGroupFromAllOrganizationalGroups(Customer customer, String orgGroupHierarchy) throws Exception;
	
	OrganizationalGroup createMissingOrgGroup(Customer customer, OrganizationalGroup organizationalGroup, String orgGroupHierarchy) throws Exception;
	OrganizationalGroup createNewOrgGroup(Customer customer, VU360User manager, String orgGroupName, OrganizationalGroup organizationalGroup) throws Exception;
	OrganizationalGroup createNewOrgGroup(Customer customer, String orgGroupName, String orgGroupHierarchy) throws Exception;
	OrganizationalGroup createNewOrganizationalGroup(Customer customer, String orgGroupName, OrganizationalGroup organizationalGroup) throws Exception;
	OrganizationalGroup createNewOrganizationalGroup(Customer customer, String orgGroupName, String orgGroupHierarchy) throws Exception;
	
	Map<String, OrganizationalGroup> getAllOrganizationalGroups(Customer customer) throws Exception;
	Map<String, Object> getOrgGroupHierarchyMap(Customer customer,List<String> orgGroupHierarchyList) throws Exception;
	Map<String, Object> getOrganizationalGroupMap(Customer customer, List<String> validOrgGroupHierarchyList) throws Exception;
	Map<String, Object> getExistingOrganizationalGroupMap(Customer customer, List<String> validOrgGroupHierarchyList) throws Exception;
	
	boolean isValidParentOrgGroupHierarchy(Customer customer, String parentOrgGroupHierarchy) throws Exception;
	Map<String, Object> isValidOrgGroupNames(Customer customer, List<String> orgGroupNames) throws Exception;
	Map<String, Object> processOrgGroupMap(Customer customer, String parentOrgGroupHierarchy, Map<String, Object> orgGroupMap) throws Exception;
	NewOrganizationalGroups processOrgGroupMapForResponse(String parentOrgGroupHierarchy, Map<String, Object> orgGroupMap) throws Exception;
	
	
	Map<String, Object> validateAddOrgGroupByParentIdRequest(Customer customer, List<AddOrgGroupByParentIdOrganizationalGroup>  organizationGroupsList) throws Exception;
	Map<String, Object> processOrgGroupMap(Customer customer, Map<String, Object> orgGroupMap) throws Exception;
	NewOrganizationalGroups processOrgGroupMapForResponse(Map<String, Object> orgGroupMap) throws Exception;
	
	OrganizationalGroup validateUpdateOrganizationRequest(Customer customer, long orgGroupId, String newOrgGroupName) throws Exception;
	OrganizationalGroup updateOrganizationGroup(OrganizationalGroup organizationalGroup, String newOrgGroupName);
	ResponseOrganizationalGroup getUpdatedOrganizationGroupResponse(OrganizationalGroup updatedOrganizationalGroup);
	
	Map<String, Object> validateDeleteOrganizationGroupRequest(String orgGroupIdArray[], Customer customer) throws Exception;
	Map<String, Object> processDeleteIdsMap(Map<String, Object> deleteIdsMap) throws Exception;
	Map<String, Object> processDeleteIdsMapForResponse(Map<String, Object> deleteIdsMap) throws Exception;
	
	OrganizationalGroup findOrganizationalGroupById(long orgGroupId) throws Exception;
	ResponseOrganizationalGroup getOrgGroupByIdResponse(OrganizationalGroup organizationalGroup) throws Exception;
	
	boolean validateGetOrgGroupIdByNameRequest(Customer customer, String parentOrgGroupHierarchy, String orgGroupName) throws Exception;
	OrganizationalGroup createNewOrganizationGroup(Customer customer, String parentOrgGroupHierarchy, String orgGroupName) throws Exception;
	ResponseOrganizationalGroup getOrgGroupIdByNameResponse(OrganizationalGroup organizationalGroup) throws Exception;
	
}
