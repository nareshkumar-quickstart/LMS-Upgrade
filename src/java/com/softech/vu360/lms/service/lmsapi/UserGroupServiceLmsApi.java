package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.NewUserGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup.ResponseUserGroup;

public interface UserGroupServiceLmsApi {
	
	boolean isValidOrgGroupHierarchy(Customer customer, String parentOrgGroupHierarchy) throws Exception;
	Map<String, Object> isValidUserGroupNames(Customer customer, List<String> userGroupNames) throws Exception;
	Map<String, Object> processUserGroupMap(Customer customer, String orgGroupHierarchy, Map<String, Object> orgGroupMap) throws Exception;
	NewUserGroups processUserGroupMapForResponse(String orgGroupHierarchy, Map<String, Object> userGroupMap) throws Exception;
	
	LearnerGroup validateUpdateUserGroupRequest(Customer customer, long learnerGroupId, long orgGroupId, String newUserGroupName) throws Exception;
	LearnerGroup updateUserGroup(LearnerGroup learnerGroup, String newUserGroupName, long orgGroupId) throws Exception;
	ResponseUserGroup getUpdatedOrganizationGroupResponse(LearnerGroup updatedLearnerGroup) throws Exception;
	
	boolean deleteUserGroups(String[] userGroupIds) throws Exception;
	LearnerGroup findUserGroupById(long learnerGroupId) throws Exception;
	ResponseUserGroup getUserGroupByIdResponse(LearnerGroup userGroup) throws Exception;
	
	LearnerGroup findUserGroupByName(Customer customer, String userGroupName) throws Exception;
	ResponseUserGroup getUserGroupIdByNameResponse(LearnerGroup userGroup) throws Exception;

}
