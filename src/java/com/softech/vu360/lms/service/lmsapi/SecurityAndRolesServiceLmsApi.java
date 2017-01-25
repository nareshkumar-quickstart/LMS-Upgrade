package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.ResponseUserSecurityRole;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.UserSecurityRole;

public interface SecurityAndRolesServiceLmsApi {
	
	public boolean securityRoleRequestValidation(UserSecurityRole securityRole, Customer customer, LMSRole lmsRole) throws Exception;
	public Map<Object, Object> getSecurityRoleMap(UserSecurityRole securityRole, Customer customer, LMSRole lmsRole)throws Exception;
	public Map<UserSecurityRole, Object> processSeurityRoles(Map<UserSecurityRole, Object> securityRolesMap) throws Exception;
	public List<ResponseUserSecurityRole> getAssignSeurityRoleToUsersResponse(Map<UserSecurityRole, Object> processedSeurityRoles);

}
