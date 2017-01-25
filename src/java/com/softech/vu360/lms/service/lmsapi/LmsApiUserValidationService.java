package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;

public interface LmsApiUserValidationService {

	public Map<Boolean, List<User>> getAddUserMap (List<User> userList, VU360User manager);
	Map<Boolean, List<User>> getAddUserLmsOnlyMap(List<User> users, VU360User manager);
	Map<Boolean, List<UpdateableUser>> getUpdateableUserMap(List<UpdateableUser> users, VU360User manager);
	List<User> getValidUsers(Map<Boolean, List<User>> addUserMap);
	List<UpdateableUser> getValidUpdateableUsers(Map<Boolean, List<UpdateableUser>> updateableUserMap);
	Map<User, String> getInvalidUsersForAddUser(String customerCode, VU360User manager, Map<Boolean, List<User>> addUserMap);
	Map<User, String> getInvalidUsersForAddUserLmsOnly(String customerCode, VU360User manager, Map<Boolean, List<User>> addUserMap);
	Map<UpdateableUser, String> getInvalidUpdateableUsers(String customerCode, VU360User manager, Map<Boolean, List<UpdateableUser>> updateableUserMap);
	
}
