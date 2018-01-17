package com.softech.vu360.lms.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softech.JWTValidation.JwtValidation;
import com.softech.JWTValidation.model.JwtPayload;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.rest.request.OrganizationRequest;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;

@RestController
@RequestMapping(value = "/customer")
public class OrganizationGroupRestEndPoint {
	    
    	@Autowired
		private CustomerService customerService = null;
		
        @Autowired
        private VU360UserService vu360UserService;
        
        @Autowired
        private OrgGroupLearnerGroupService groupService = null;
        
		@RequestMapping( value = "/organizationgroup", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public Map<String, Boolean> saveOrganizationgroup(@RequestBody OrganizationRequest OrganizationUpdateRequest, @RequestHeader(value = "token") String token) throws Exception{     
	        Map<String,Boolean> responseData = new HashMap<>();
	        JwtPayload payload = JwtValidation.validateJWTToken(token);
	           
	        VU360User vu360User = vu360UserService.findUserByUserName(payload.getUser_name());
	        Customer customer= vu360User.getLearner().getCustomer();
	        customer.setName(OrganizationUpdateRequest.getOrganizationName());
	        customerService.saveCustomer(customer);
	        
	        
	        OrganizationalGroup orgGroup = groupService.getRootOrgGroupForCustomer(customer.getId());
			orgGroup.setName(customer.getName());
			groupService.saveOrganizationalGroup(orgGroup);
	        
	        responseData.put("status", Boolean.TRUE);    
	        return responseData;
	    }
}
