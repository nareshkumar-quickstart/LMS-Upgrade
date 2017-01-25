package com.softech.vu360.lms.service.lmsapi;

import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;

public interface LmsApiCustomerService {

	public LmsApiCustomer findLmsApiByCustomerId(long distributorId) throws Exception;
	public LmsApiCustomer findApiKey(String key) throws Exception;
	public LmsApiCustomer addLmsApiCustomer(LmsApiCustomer lmsApiCustomer) throws Exception;
	public boolean customerApiValidation(LmsApiCustomer lmsApiCustomer, String customerCode) throws Exception;
	public boolean customerApiValidation(String apiKey, String customerCode) throws Exception;
	
}
