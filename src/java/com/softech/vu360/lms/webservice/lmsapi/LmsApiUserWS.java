package com.softech.vu360.lms.webservice.lmsapi;

import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserLmsOnlyRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserLmsOnlyResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.UpdateUserRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.UpdateUserResponse;

public interface LmsApiUserWS {

	/**
	 * This is the namespace we defined in our UserServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	String USER_TARGET_NAMESPACE = "http://user.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	String ADD_USER_EVENT = "AddUserRequest";
	String ADD_USER_LMS_ONLY_EVENT = "AddUserLmsOnlyRequest";
	String UPDATE_USER_EVENT = "UpdateUserRequest";
	
	AddUserResponse addUser(AddUserRequest addUserRequest);
	AddUserLmsOnlyResponse addUserLmsOnly(AddUserLmsOnlyRequest addUserRequest);
	UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest);
	
}
