package com.softech.vu360.lms.webservice.endpoint.soap.lmsapi;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.service.lmsapi.validation.LmsApiAuthenticationService;
import com.softech.vu360.lms.webservice.impl.LMSAPIWSImpl;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.customer.AddCustomerRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.customer.AddCustomerResponse;

/**
 * Endpoints are the key concept of Spring Web Service’s server support. Unlike controllers, whose handler methods are directly tied
 * to HTTP requests and responses, Spring Web Service SOAP endpoints can serve SOAP requests made via HTTP, XMPP, SMTP, JMS, and 
 * more. Just like @Controller marks a controller whose @RequestMapping methods should be scanned and mapped to requests, @Endpoint 
 * marks an endpoint whose @org.springframework.ws.server.endpoint.annotation.PayloadRoot methods, 
 * @org.springframework.ws.soap.server.endpoint.annotation.SoapAction methods, and/or 
 * @org.springframework.ws.soap.addressing.server.annotation.Action methods are handlers for incoming SOAP requests on any protocol. 
 * Endpoint methods’ parameters correspond to elements of the request, whereas return types indicate response contents.
 * 
 * @author basit.ahmed
 *
 */
@Endpoint
public class CustomerSoapEndpoint {

	private static final Logger log = Logger.getLogger(CustomerSoapEndpoint.class.getName());
	
	/**
	 * This is the namespace we defined in our CustomerServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	private static final String CUSTOMER_TARGET_NAMESPACE = "http://customer.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	private static final String ADD_CUSTOMER_LOCAL_PART = "AddCustomerRequest";
	
	@Autowired
	private LmsApiAuthenticationService lmsApiAuthenticationService;
	
	public LmsApiAuthenticationService getLmsApiAuthenticationService() {
		return lmsApiAuthenticationService;
	}

	public void setLmsApiAuthenticationService(LmsApiAuthenticationService lmsApiAuthenticationService) {
		this.lmsApiAuthenticationService = lmsApiAuthenticationService;
	}
	
	//@PayloadRoot(namespace = CUSTOMER_TARGET_NAMESPACE, localPart = ADD_CUSTOMER_LOCAL_PART)
	public AddCustomerResponse addCustomer(AddCustomerRequest request) {
		
		log.info("Request received at " + getClass().getName() + " for addCustomer");
		
		AddCustomerResponse response = new AddCustomerResponse();
		return response;
		
	}
	
	
}
