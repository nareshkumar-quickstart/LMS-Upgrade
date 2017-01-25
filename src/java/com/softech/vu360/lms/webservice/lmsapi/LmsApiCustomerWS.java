package com.softech.vu360.lms.webservice.lmsapi;

import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.customer.AddCustomerRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.customer.AddCustomerResponse;

public interface LmsApiCustomerWS {

	/**
	 * This is the namespace we defined in our CustomerServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	String CUSTOMER_TARGET_NAMESPACE = "http://customer.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	String ADD_CUSTOMER_EVENT = "AddCustomerRequest";
	
	//Customer
	AddCustomerResponse addCustomer(AddCustomerRequest addCustomerRequest);
	
}
