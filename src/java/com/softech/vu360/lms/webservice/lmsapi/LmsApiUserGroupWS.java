package com.softech.vu360.lms.webservice.lmsapi;

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

public interface LmsApiUserGroupWS {

	/**
	 * This is the namespace we defined in our UserGroupServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	String USER_GROUP_TARGET_NAMESPACE = "http://usergroup.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	String ADD_USER_GROUP_EVENT = "AddUserGroupRequest";
	String UPDATE_USER_GROUP_EVENT = "UpdateUserGroupRequest";
	String DELETE_USER_GROUP_EVENT = "DeleteUserGroupRequest";
	String GET_USER_GROUP_BY_ID_EVENT = "GetUserGroupByIdRequest";
	String GET_USER_GROUP_ID_BY_NAME_EVENT = "GetUserGroupIdByNameRequest";
	
	AddUserGroupResponse addUserGroup(AddUserGroupRequest addUserGroupRequest);
	UpdateUserGroupResponse updateUserGroup(UpdateUserGroupRequest updateUserGroupRequest);
	DeleteUserGroupResponse deleteUserGroup(DeleteUserGroupRequest deleteUserGroupRequest);
	GetUserGroupByIdResponse getUserGroupById(GetUserGroupByIdRequest getUserGroupByIdRequest);
	GetUserGroupIdByNameResponse getUserGroupIdByName(GetUserGroupIdByNameRequest getUserGroupIdByNameRequest);
	
}
