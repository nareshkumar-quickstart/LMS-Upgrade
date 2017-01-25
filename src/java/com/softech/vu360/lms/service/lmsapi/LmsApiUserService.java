package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.UpdateableUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;

public interface LmsApiUserService {

	List<RegisterUser> addUsers(Customer customer, VU360User manager, Map<Boolean, List<User>> usersMap);
	List<RegisterUser> addUsersLmsOnly(Customer customer, VU360User manager, Map<Boolean, List<User>> lmsOnlyUsersMap);
	List<RegisterUser> updateUsers(Customer customer, VU360User manager, Map<Boolean, List<UpdateableUser>> updateableUsersMap);
		
	

}
