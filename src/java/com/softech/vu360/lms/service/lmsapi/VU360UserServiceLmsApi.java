package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.RegisterUser;
import com.softech.vu360.lms.webservice.message.lmsapi.types.user.User;

public interface VU360UserServiceLmsApi {
	
	public VU360User getValidUser(String userName, String customerCode) throws Exception;
	public VU360User getVU360UserByCustomerGUID(Customer customer) throws Exception;
	public VU360User getVU360UserByCustomerGUID(String customerGUID) throws Exception;
	public boolean userValidation(User user) throws Exception;
	public VU360User getNewUser(Customer customer, User user) throws Exception;
	
	public Map<String, Object> validateAddUserRequest(List<User> userList) throws Exception;
	public Map<String, Object> validateUpdateUserRequest(List<User> userList) throws Exception;
	public Map<String, Object> processUsersMap(Customer customer, Map<String, Object> usersMap) throws Exception;
	
	public Learner updateUser(Customer customer, VU360User existingUser, User user) throws Exception;
	
	public List<RegisterUser> getRegisterUserList(Customer customer, Map<String, Object> processedUsersMap, boolean add) throws Exception;
	
}
