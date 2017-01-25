package com.softech.vu360.lms.service.impl.lmsapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;

import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;

import com.softech.vu360.lms.service.lmsapi.OrgGroupServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.SecurityAndRolesServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.VU360UserServiceLmsApi;

import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.AssignedOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.AssignedSecurityRoleUsers;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.ResponseUserSecurityRole;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.UnassignedOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.UnassignedSecurityRoleUsers;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.UserSecurityRole;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.ValueWithError;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.Vu360Users;

public class SecurityAndRolesServiceImplLmsApi implements SecurityAndRolesServiceLmsApi {
	
	private static final Logger log = Logger.getLogger(SecurityAndRolesServiceImplLmsApi.class.getName());
	private static final String ERROR_CODE_ONE  = "1";
	private static final String ERROR_CODE_ZERO  = "0";
	private VU360UserService vu360UserService;
	private VU360UserServiceLmsApi vu360UserServiceLmsApi;
	private OrgGroupServiceLmsApi orgGroupServiceLmsApi;
	private LearnerService learnerService;
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	public VU360UserServiceLmsApi getVu360UserServiceLmsApi() {
		return vu360UserServiceLmsApi;
	}

	public void setVu360UserServiceLmsApi(VU360UserServiceLmsApi vu360UserServiceLmsApi) {
		this.vu360UserServiceLmsApi = vu360UserServiceLmsApi;
	}
	
	public OrgGroupServiceLmsApi getOrgGroupServiceLmsApi() {
		return orgGroupServiceLmsApi;
	}

	public void setOrgGroupServiceLmsApi(OrgGroupServiceLmsApi orgGroupServiceLmsApi) {
		this.orgGroupServiceLmsApi = orgGroupServiceLmsApi;
	}
	
	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public boolean securityRoleRequestValidation(UserSecurityRole userSecurityRole, Customer customer, LMSRole lmsRole) throws Exception {
		
		String errorMessage = null;
		if (userSecurityRole == null) {
			errorMessage = "User security role element is mandotory. Atleat one SecurityRole element is required";
			throwException(errorMessage);
		}
		
		String securityRoleName = userSecurityRole.getSecurityRoleName();
			
		if (lmsRole == null) {
			errorMessage = "Security Role name not found: " + securityRoleName;
			throwException(errorMessage);
		}
		
		if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)) {
			OrganizationalGroups organizationalGroups = userSecurityRole.getOrganizationalGroups();
			if (organizationalGroups == null) {
				errorMessage = "Organizational Group is mandotory for manager role.";
				throwException(errorMessage);
			}
				
			List<String> organizationalGroupList = organizationalGroups.getOrgGroupHierarchy();
			if (organizationalGroupList == null ||organizationalGroupList.isEmpty()) {
				errorMessage = "Organizational Group is mandotory for manager role.";
				throwException(errorMessage);
			}
		}
				
		Vu360Users users = userSecurityRole.getUsers();
		if (users == null) {
			errorMessage = "User name is mandotory. Atleat one User name is required.";
			throwException(errorMessage);
		}
			
		List<String> userNameList = users.getUserName();
		if (userNameList == null || userNameList.isEmpty()) {
			errorMessage = "User name is mandotory. Atleat one User name is required.";
			throwException(errorMessage);
		}
			
		return true;
	}
	
	@Override
	public Map<Object, Object> getSecurityRoleMap(UserSecurityRole securityRole, Customer customer, LMSRole lmsRole)throws Exception {
		
		Map<Object, Object> securityRoleMap = new HashMap<Object, Object>();
		List<VU360User> validUserList = new ArrayList<VU360User>();
		Map<String, String> invalidUserMap = new HashMap<String, String>();
		List<OrganizationalGroup> validOrgGroupList = new ArrayList<OrganizationalGroup>();
		Map<String, String> invalidOrgGroupMap = new HashMap<String, String>();
		
		String customerCode = customer.getCustomerCode();
		Vu360Users users = securityRole.getUsers();
		List<String> userNameList = users.getUserName();
		for (String userName : userNameList) {
			try {
				VU360User validUser = vu360UserServiceLmsApi.getValidUser(userName, customerCode);
				//validUserMap.put(roleName, validUser);
				validUserList.add(validUser);
				
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				invalidUserMap.put(userName, errorMessage);
			}
		}
		
		if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)) {
			OrganizationalGroups organizationalGroups = securityRole.getOrganizationalGroups();
			List<String> organizationalGroupList = organizationalGroups.getOrgGroupHierarchy();
			for (String orgGroupHierarchy : organizationalGroupList) {
				try {
					OrganizationalGroup organizationalGroup = orgGroupServiceLmsApi.getExistingOrganizationalGroup(customer, orgGroupHierarchy);
		    		if (organizationalGroup == null) {
		    			log.debug("orgGroup not found");
		    			String errorMessage = "Organizational group does not exist";
		    			invalidOrgGroupMap.put(orgGroupHierarchy, errorMessage);
		    		} else {
		    			//validOrgGroupMap.put(orgGroupHierarchy, organizationalGroup);
		    			validOrgGroupList.add(organizationalGroup);
		    		}
				} catch (Exception e) {
					String errorMessage = e.getMessage();
					invalidOrgGroupMap.put(orgGroupHierarchy, errorMessage);
				}
			}
			
			securityRoleMap.put("validOrgGroupList", validOrgGroupList);
			securityRoleMap.put("invalidOrgGroupMap", invalidOrgGroupMap);
			
		}
		
		securityRoleMap.put("securityRole", lmsRole);
		securityRoleMap.put("validUserList", validUserList);
		securityRoleMap.put("invalidUserMap", invalidUserMap);
		
		return securityRoleMap;
	}
	
	@Override
	public Map<UserSecurityRole, Object> processSeurityRoles(Map<UserSecurityRole, Object> securityRolesMap) throws Exception {
		
		for (Map.Entry<UserSecurityRole, Object> securityRolesMapEntry : securityRolesMap.entrySet()) {
			//SecurityRole securityRole = entry.getKey();
			Object value = securityRolesMapEntry.getValue();
			if (value instanceof Map<?, ?>) {
				Map<Object, Object> securityRoleMap = (Map<Object, Object>) value;
				try {
					LMSRole lmsRole = (LMSRole)securityRoleMap.get("securityRole");
					List<VU360User> validUserList = (ArrayList<VU360User>)securityRoleMap.get("validUserList");
					if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)) {
						List<OrganizationalGroup> validOrgGroupList = (ArrayList<OrganizationalGroup>)securityRoleMap.get("validOrgGroupList");
						assignSeurityRoleToUser(validUserList, validOrgGroupList, lmsRole);
					} else {
						assignSeurityRoleToUser(validUserList, null, lmsRole);
					}
				} catch(Exception e) {
					String errorMessage = e.getMessage();
					securityRoleMap.put("errorMessage", errorMessage);
				}
			}
		}
		
		return securityRolesMap;
	}
	
	public void assignSeurityRoleToUser(List<VU360User> userList, List<OrganizationalGroup> orgGroupList,  LMSRole lmsRole) {
		
		String[] selectedUserValues = new String[userList.size()];
		long userIdArray[] = new long[userList.size()];
		//List<OrganizationalGroup> orgGroupsList = new ArrayList<OrganizationalGroup>();
		
		for (int i=0; i<userList.size(); i++) {
			VU360User vu360User = userList.get(i);
			selectedUserValues[i] = vu360User.getId().toString();
			userIdArray[i] = (vu360User.getId());	
		}
		
		learnerService.unAssignUsersFromAllRolesOfType(selectedUserValues, lmsRole.getRoleType());
		learnerService.assignUserToRole(userIdArray, lmsRole, "false", orgGroupList);
	
	}
	
	public List<ResponseUserSecurityRole> getAssignSeurityRoleToUsersResponse(Map<UserSecurityRole, Object> processedSeurityRoles) {
		
		List<ResponseUserSecurityRole> responseSecurityRoleList = new ArrayList<ResponseUserSecurityRole>();
		for (Map.Entry<UserSecurityRole, Object> processedSeurityRolesMapEntry : processedSeurityRoles.entrySet()) {
			
			Object value = processedSeurityRolesMapEntry.getValue();
			if (value instanceof String) {
				
				String errorMessage = (String)value;
				UserSecurityRole userSecurityRole = (UserSecurityRole)processedSeurityRolesMapEntry.getKey();
				String securityRoleName = userSecurityRole.getSecurityRoleName();
				
				ResponseUserSecurityRole userSecurityRoleResponse = getUserSecurityRoleResponse(ERROR_CODE_ONE, errorMessage);
				userSecurityRoleResponse.setSecurityRoleName(securityRoleName);
				responseSecurityRoleList.add(userSecurityRoleResponse);
				
			} else if (value instanceof Map<?, ?>) {
				Map<Object, Object> securityRoleMap = (Map<Object, Object>) value;
				LMSRole lmsRole = (LMSRole)securityRoleMap.get("securityRole");
				List<VU360User> validUserList = (ArrayList<VU360User>)securityRoleMap.get("validUserList");
				Map<String, String> invalidUserMap = (Map<String, String>)securityRoleMap.get("invalidUserMap");
				
				AssignedSecurityRoleUsers assignedSecurityRoleUsers = getAssignedSecurityRoleUsersResponse(validUserList);
				UnassignedSecurityRoleUsers unassignedSecurityRoleUsers = getUnassignedSecurityRoleUsersResponse(invalidUserMap);
				
				ResponseUserSecurityRole userSecurityRoleResponse = getUserSecurityRoleResponse(ERROR_CODE_ZERO, "");
				userSecurityRoleResponse.setSecurityRoleName(lmsRole.getRoleName());
				userSecurityRoleResponse.setAssignedSecurityRoleUsers(assignedSecurityRoleUsers);
				userSecurityRoleResponse.setUnassignedSecurityRoleUsers(unassignedSecurityRoleUsers);
				
				if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)) {
					
					List<OrganizationalGroup> validOrgGroupList = (ArrayList<OrganizationalGroup>)securityRoleMap.get("validOrgGroupList");
					Map<String, String> invalidOrgGroupMap = (Map<String, String>)securityRoleMap.get("invalidOrgGroupMap");
					
					AssignedOrganizationalGroups assignedOrganizationalGroups = getAssignedOrganizationalGroupsResponse(validOrgGroupList);
					UnassignedOrganizationalGroups unassignedOrganizationalGroups = getUnassignedOrganizationalGroupsResponse(invalidOrgGroupMap);
					
					userSecurityRoleResponse.setAssignedOrganizationalGroups(assignedOrganizationalGroups);
					userSecurityRoleResponse.setUnassignedOrganizationalGroups(unassignedOrganizationalGroups);
					
				}
				
				responseSecurityRoleList.add(userSecurityRoleResponse);
			}
		}
		
		return responseSecurityRoleList;
	
	}
	
	private ResponseUserSecurityRole getUserSecurityRoleResponse(String errorCode, String errorMessage) {
		ResponseUserSecurityRole userSecurityRoleResponse = new ResponseUserSecurityRole();
		userSecurityRoleResponse.setErrorCode(errorCode);
		userSecurityRoleResponse.setErrorMessage(errorMessage);
		return userSecurityRoleResponse;
	}
	
	private AssignedSecurityRoleUsers getAssignedSecurityRoleUsersResponse(List<VU360User> validUserList) {
		
		if (validUserList == null || validUserList.isEmpty()) {
			return null;
		}
		
		List<String> assignedSecurityRoleUsersList = new ArrayList<String>();
		AssignedSecurityRoleUsers assignedSecurityRoleUsers = new AssignedSecurityRoleUsers();
	
		for (VU360User vu360User : validUserList) {
			String userName = vu360User.getUsername();
			assignedSecurityRoleUsersList.add(userName);
		}
		
		assignedSecurityRoleUsers.setUserName(assignedSecurityRoleUsersList);
		return assignedSecurityRoleUsers;
	}
	
	private UnassignedSecurityRoleUsers getUnassignedSecurityRoleUsersResponse(Map<String, String> invalidUserMap) {
		
		if (invalidUserMap == null || invalidUserMap.isEmpty()) {
			return null;
		}
		
		List<ValueWithError> unassignedUsersList = new ArrayList<ValueWithError>();
		UnassignedSecurityRoleUsers unassignedSecurityRoleUsers = new UnassignedSecurityRoleUsers();
		
		for (Map.Entry<String, String> invalidUserMapEntry : invalidUserMap.entrySet()) {
			
			String userName = (String)invalidUserMapEntry.getKey();
			String errorMessage = (String)invalidUserMapEntry.getValue();
			
			ValueWithError unassignedUser = new ValueWithError();
			unassignedUser.setValue(userName);
			unassignedUser.setErrorCode(ERROR_CODE_ONE);
			unassignedUser.setErrorMessage(errorMessage);
			
			unassignedUsersList.add(unassignedUser);
			
		}
		
		unassignedSecurityRoleUsers.setUserName(unassignedUsersList);
		return unassignedSecurityRoleUsers;
		
	}
	
	private AssignedOrganizationalGroups getAssignedOrganizationalGroupsResponse(List<OrganizationalGroup> validOrgGroupList) {
		
		if (validOrgGroupList == null || validOrgGroupList.isEmpty()) {
			return null;
		}
		
		List<String> assignedOrganizationalGroupsList = new ArrayList<String>();
		AssignedOrganizationalGroups assignedOrganizationalGroups = new AssignedOrganizationalGroups();
		
		for (OrganizationalGroup organizationalGroup : validOrgGroupList) {
			String organizationalGroupName = organizationalGroup.getName();
			assignedOrganizationalGroupsList.add(organizationalGroupName);
		}
		
		assignedOrganizationalGroups.setOrgGroupHierarchy(assignedOrganizationalGroupsList);
		return assignedOrganizationalGroups;
	}
	
	private UnassignedOrganizationalGroups getUnassignedOrganizationalGroupsResponse(Map<String, String> invalidOrgGroupMap) {
		
		if (invalidOrgGroupMap == null || invalidOrgGroupMap.isEmpty()) {
			return null;
		}
		
		List<com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.ValueWithError> unassignedOrganizationalGroupsList = new ArrayList<com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.ValueWithError>();
		UnassignedOrganizationalGroups unassignedOrganizationalGroups = new UnassignedOrganizationalGroups();
		
		for (Map.Entry<String, String> invalidOrgGroupMapEntry : invalidOrgGroupMap.entrySet()) {
			
			String orgGroupHierarchy = (String)invalidOrgGroupMapEntry.getKey();
			String errorMessage = (String)invalidOrgGroupMapEntry.getValue();
			
			com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.ValueWithError unassignedOrganizationalGroup = new com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.ValueWithError();
			unassignedOrganizationalGroup.setValue(orgGroupHierarchy);
			unassignedOrganizationalGroup.setErrorCode(ERROR_CODE_ONE);
			unassignedOrganizationalGroup.setErrorMessage(errorMessage);
			
			unassignedOrganizationalGroupsList.add(unassignedOrganizationalGroup);
			
		}
		
		unassignedOrganizationalGroups.setInvalidOrganizationalGroup(unassignedOrganizationalGroupsList);
		return unassignedOrganizationalGroups;
		
	}
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}
	
}
