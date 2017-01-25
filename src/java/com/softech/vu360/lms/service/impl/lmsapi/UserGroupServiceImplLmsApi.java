package com.softech.vu360.lms.service.impl.lmsapi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.lmsapi.OrgGroupServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.UserGroupServiceLmsApi;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.ResponseOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.NewUserGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.ResponseUserGroup;

public class UserGroupServiceImplLmsApi implements UserGroupServiceLmsApi {
	
	private static final Logger log = Logger.getLogger(UserGroupServiceImplLmsApi.class.getName());
	
	private OrgGroupServiceLmsApi orgGroupServiceLmsApi;
	private LearnerService learnerService;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;

	public void setOrgGroupServiceLmsApi(OrgGroupServiceLmsApi orgGroupServiceLmsApi) {
		this.orgGroupServiceLmsApi = orgGroupServiceLmsApi;
	}
	
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public boolean isValidOrgGroupHierarchy(Customer customer, String orgGroupHierarchy) throws Exception {
		return orgGroupServiceLmsApi.isValidParentOrgGroupHierarchy(customer, orgGroupHierarchy);
	}
	
	public Map<String, Object> isValidUserGroupNames(Customer customer, List<String> userGroupNames) throws Exception {
		
		List<String> validUserGroupNameList = new ArrayList<String>();
		Map<String, Object> userGroupMap = new LinkedHashMap<String, Object>();
		Map<String, String> userGroupErrorMap = new LinkedHashMap<String, String>();
		
		for(String userGroupName : userGroupNames) {
			try {
				isValidUserGroupName(customer, userGroupName);
				validUserGroupNameList.add(userGroupName);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				userGroupErrorMap.put(userGroupName, errorMessage);
			}
		}
		
		if (!validUserGroupNameList.isEmpty()) {
			userGroupMap.put("validUserGroupNameList", validUserGroupNameList);
		}
		
		if (!userGroupErrorMap.isEmpty()) {
			userGroupMap.put("userGroupErrorMap", userGroupErrorMap);
		}
		
		return userGroupMap;
		
	}
	
	private boolean isValidUserGroupName(Customer customer, String userGroupName) throws Exception {
		
		String errorMessage = null;
		
		if (StringUtils.isEmpty(userGroupName)) {
			errorMessage = "UserGroupName can not be empty or blanck";
			throwException(errorMessage);
		}
		
		LearnerGroup learnerGroup = findUserGroupByName(customer, userGroupName);
		if (learnerGroup != null){
			errorMessage = "UserGroupName already exist";
			throwException(errorMessage);
		}
		
		return true;
	}
	
	public LearnerGroup findUserGroupByName(Customer customer, String userGroupName) throws Exception {
		LearnerGroup userGroup = learnerService.getLearnerGroupByName(userGroupName, customer);
		return userGroup;
	}
	
	public Map<String, Object> processUserGroupMap(Customer customer, String orgGroupHierarchy, Map<String, Object> orgGroupMap) throws Exception {
		
		List<String> validUserGroupNameList = (List<String>)orgGroupMap.get("validUserGroupNameList");
		
		if (validUserGroupNameList != null) {
			List<LearnerGroup> newUserGroupList = createNewUserGroups(customer, orgGroupHierarchy, validUserGroupNameList);
			if (!newUserGroupList.isEmpty()) {
				orgGroupMap.put("newUserGroupList", newUserGroupList);
			}
		} 
		return orgGroupMap;
	}
	
	public NewUserGroups processUserGroupMapForResponse(String orgGroupHierarchy, Map<String, Object> userGroupMap) throws Exception {
		
		List<ResponseUserGroup> newResponseUserGroupList = new ArrayList<ResponseUserGroup>();
		
		NewUserGroups newUserGroups = new NewUserGroups();
		List<LearnerGroup> newUserGroupList = (List<LearnerGroup>)userGroupMap.get("newUserGroupList");
		Map<String, String> userGroupErrorMap = (Map<String, String>)userGroupMap.get("userGroupErrorMap");
		
		if (userGroupErrorMap != null) {
			List<ResponseUserGroup> errorUserGroupList = processUserGroupErrorMap(orgGroupHierarchy, userGroupErrorMap);
			newResponseUserGroupList.addAll(errorUserGroupList);
		} 
			
		if (newUserGroupList != null) {
			List<ResponseUserGroup> newRespUserGroupList = getNewUserGroupResponse(orgGroupHierarchy, newUserGroupList);
			newResponseUserGroupList.addAll(newRespUserGroupList);
		}
	
		newUserGroups.setNewUserGroup(newResponseUserGroupList);
		return newUserGroups;
	
	}
	
	public LearnerGroup validateUpdateUserGroupRequest(Customer customer, long learnerGroupId, long orgGroupId, String newUserGroupName) throws Exception {
		
		String errorMessage = null;
		
		LearnerGroup userGroup = findUserGroupById(learnerGroupId);
		
		if(!(userGroup.getCustomer().getId().equals(customer.getId()))){
			errorMessage = "LearnerGroup's customer id does not match with Customer Id";
			throwException(errorMessage);
		}
		
		LearnerGroup newUserGroup = learnerService.getLearnerGroupByName(userGroup.getName(), customer);
		if (newUserGroup !=null){
			if(newUserGroup.getId().longValue() != userGroup.getId().longValue()){
				errorMessage = "User Group Name already exists";
				throwException(errorMessage);
			}
		}
		
		OrganizationalGroup OrganizationGroup = learnerService.loadForUpdateOrganizationalGroup(orgGroupId);
		if (OrganizationGroup == null) {
			errorMessage = "Invalid OrganizationGroupId";
			throwException(errorMessage);
		}
		
		if (StringUtils.isEmpty(newUserGroupName)) {
			errorMessage = "NewUserGroupName can not be empty or blank";
			throwException(errorMessage);
		}
		
		return userGroup;
	}
	
	public LearnerGroup updateUserGroup(LearnerGroup learnerGroup, String newUserGroupName, long orgGroupId) throws Exception {
		
		OrganizationalGroup OrganizationGroup = learnerService.loadForUpdateOrganizationalGroup(orgGroupId);
		if (OrganizationGroup == null) {
			String errorMessage = "Invalid OrganizationGroupId";
			throwException(errorMessage);
		}
		
		LearnerGroup updatedLearnerGroup = learnerGroup;
		//long orgGroupId = updatedLearnerGroup.getOrganizationalGroup().getId();
		
		updatedLearnerGroup.setOrganizationalGroup(OrganizationGroup);
		updatedLearnerGroup.setName(newUserGroupName);
		orgGroupLearnerGroupService.saveLearnerGroup(updatedLearnerGroup);
		
		return updatedLearnerGroup;
		
	}
	
	public ResponseUserGroup getUpdatedOrganizationGroupResponse(LearnerGroup updatedLearnerGroup) {
		
		Long userGroupId = updatedLearnerGroup.getId();
		
		// Convert Long to String.
		String stringId = userGroupId.toString();
		BigInteger bigIntId = new BigInteger( stringId );
		String updatedName = updatedLearnerGroup.getName();
		
		return getResponseUserGroup(bigIntId, updatedName, null, null, null, null, "0", "");
		
	}
	
	public boolean deleteUserGroups(String[] userGroupIds) throws Exception {
		
		if( userGroupIds != null ){
			long userGroupIdArray[] = new long[userGroupIds.length];
			for( int i=0; i<userGroupIds.length; i++ ) {
				String learnerGroupID = userGroupIds[i];
				if( StringUtils.isNotBlank(learnerGroupID) ) {
					userGroupIdArray[i]=Long.parseLong(userGroupIds[i]);
				}	
			}	
			orgGroupLearnerGroupService.deleteLearnerGroups(userGroupIdArray);
		}
		return true;
	}
	
	public LearnerGroup findUserGroupById(long learnerGroupId) throws Exception {
		
		LearnerGroup learnerGroup = learnerService.loadForUpdateLearnerGroup(learnerGroupId);
		if (learnerGroup == null) {
			String errorMessage = "Invalid Id: " + learnerGroupId ;
			throwException(errorMessage);
		}
		
		return learnerGroup;
	}
	
	private List<ResponseUserGroup> processUserGroupErrorMap(String orgGroupHierarchy, Map<String, String> userGroupErrorMap) throws Exception {
		
		List<ResponseUserGroup> newResponseUserGroupList = new ArrayList<ResponseUserGroup>();
		
		for (Map.Entry<String, String> userGroupErrorMapEntry : userGroupErrorMap.entrySet()) {
			String userGroupName = userGroupErrorMapEntry.getKey();
			String errorMessage = userGroupErrorMapEntry.getValue();
			
			ResponseUserGroup responseUserGroup = getResponseUserGroup(null, userGroupName, null, orgGroupHierarchy, null, null, "1", errorMessage);
			newResponseUserGroupList.add(responseUserGroup);
		}
		
		return newResponseUserGroupList;
	}
	
	private List<ResponseUserGroup> getNewUserGroupResponse(String orgGroupHierarchy, List<LearnerGroup> newUserGroupList) {
		
		List<ResponseUserGroup> newResponseUserGroupList = new ArrayList<ResponseUserGroup>();
		
		for (LearnerGroup newUserGroup : newUserGroupList) {
			Long id = newUserGroup.getId();
			
			// Convert Long to String.
			String stringId = id.toString();
			BigInteger bigIntId = new BigInteger( stringId );
			String userGroupName = newUserGroup.getName();
			
			ResponseUserGroup responseUserGroup = getResponseUserGroup(bigIntId, userGroupName, null, orgGroupHierarchy, null, null, "0", "");
			newResponseUserGroupList.add(responseUserGroup);
		}
		
		return newResponseUserGroupList;
		
	}
	
	private List<LearnerGroup> createNewUserGroups(Customer customer, String orgGroupHierarchy, List<String> validUserGroupNameList) throws Exception {
		
		OrganizationalGroup organizationalGroup = orgGroupServiceLmsApi.getOrganizationalGroupFromAllOrganizationalGroups(customer, orgGroupHierarchy);
		
		List<LearnerGroup> newUserGroupList = new ArrayList<LearnerGroup>();
		
		for (String userGroupName : validUserGroupNameList) {
			LearnerGroup newUserGroup = createNewUserGroup(customer, userGroupName, organizationalGroup);
			newUserGroupList.add(newUserGroup);
		}
		
		return newUserGroupList;
		
	}
	
	private LearnerGroup createNewUserGroup(Customer customer, String userGroupName, OrganizationalGroup organizationalGroup) throws Exception {
		
		LearnerGroup learnerGroup = new LearnerGroup();
		learnerGroup.setName(userGroupName);
		learnerGroup.setOrganizationalGroup(organizationalGroup);
		learnerGroup.setCustomer(customer);
	
		orgGroupLearnerGroupService.saveLearnerGroup(learnerGroup);
		
		return learnerGroup;
	
	}
	
	public LearnerGroup createNewUserGroup(Customer customer, String orgGroupHierarchy, String userGroupName) throws Exception {
		OrganizationalGroup organizationalGroup = orgGroupServiceLmsApi.getOrganizationalGroupFromAllOrganizationalGroups(customer, orgGroupHierarchy);
		return createNewUserGroup(customer, userGroupName, organizationalGroup);
	}
	
	public ResponseUserGroup getUserGroupByIdResponse(LearnerGroup userGroup) throws Exception {
		
		Long orgGroupId = userGroup.getId();
		
		// Convert Long to String.
		String stringId = orgGroupId.toString();
		BigInteger bigIntId = new BigInteger( stringId );
		
		String userGroupName = userGroup.getName();
		OrganizationalGroup  organizationgroup = userGroup.getOrganizationalGroup();
		OrganizationalGroup  parentOrganizationgroup = userGroup.getOrganizationalGroup().getParentOrgGroup();
		//String parentOrgGroup = organizationalGroup.getParentOrgGroup().getName();
		//Long parentId = organizationalGroup.getParentOrgGroup().getId();
		
		// Convert Long to String.
		//String stringParentId = parentId.toString();
		//BigInteger bigIntParentId = new BigInteger( stringParentId );
		
		return getResponseUserGroup(bigIntId, userGroupName, null, null, null, null, "0", "");
		
	}
	
	public ResponseUserGroup getUserGroupIdByNameResponse(LearnerGroup userGroup) throws Exception {
		return getUserGroupByIdResponse(userGroup);
	}
	
	private ResponseUserGroup getResponseUserGroup(BigInteger id, String userGroupName, String orgGroupName,
			String orgGroupHierarchy, String parentOrgGroupHierarchy, BigInteger parentId, String errorCode, String errorMessage) {
		
		ResponseUserGroup responseUserGroup = new ResponseUserGroup();
		responseUserGroup.setId(id);
		responseUserGroup.setName(userGroupName);
		responseUserGroup.setOrgGroupName(orgGroupName);
		responseUserGroup.setOrgGroupHierarchy(orgGroupHierarchy);
		responseUserGroup.setParentOrgGroupHierarchy(parentOrgGroupHierarchy);
		responseUserGroup.setParentId(parentId);
		responseUserGroup.setErrorCode(errorCode);
		responseUserGroup.setErrorMessage(errorMessage);
		
		return responseUserGroup;
		
	}
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}
	
	

}
