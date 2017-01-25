package com.softech.vu360.lms.service.lmsapi.response;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserLmsOnlyResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.UpdateUserResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.RegisterOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;

public interface LmsApiUserResponseService {

	AddUserResponse getAddUserResponse(String message);
	AddUserResponse getAddUserResponse(List<RegisterUser> registerUsers);
	AddUserLmsOnlyResponse getAddUserLmsOnlyResponse(String message);
	AddUserLmsOnlyResponse getAddUserLmsOnlyResponse(List<RegisterUser> registerUsers);
	UpdateUserResponse getUpdateUserResponse(String message);
	UpdateUserResponse getUpdateUserResponse(List<RegisterUser> registerUsers);
	RegisterOrganizationalGroups getRegisterOrganizationalGroups(List<String> validOrgGroupHierarchies, Map<String, String> invalidOrgGroupHierarchies);
	RegisterUser getRegisterUser(String errorCode, String errorMessage, User user);
	RegisterUser getRegisterUser(String errorCode, String errorMessage, UpdateableUser user);
	
}
