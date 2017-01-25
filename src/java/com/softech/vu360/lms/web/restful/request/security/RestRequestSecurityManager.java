package com.softech.vu360.lms.web.restful.request.security;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;

public abstract class RestRequestSecurityManager {

	private static final Logger log = Logger.getLogger(RestRequestSecurityManager.class.getName());
	
	protected static final String NO_API_KEY_FOUND_ERROR = "No Api Key found. Unauthorized Access";
	
	protected void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}
	
	public abstract LmsApiDistributor validateRequest(String apiKey, Long resellerId) throws Exception;
	public abstract LmsApiCustomer validateRequest(String apiKey, String customerCode) throws Exception;
		
}
