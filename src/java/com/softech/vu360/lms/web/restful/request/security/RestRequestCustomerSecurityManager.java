package com.softech.vu360.lms.web.restful.request.security;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;
import com.softech.vu360.lms.service.lmsapi.LmsApiCustomerService;

@Component(value="restRequestCustomerSecurityManager")
@Scope("request")
public class RestRequestCustomerSecurityManager extends RestRequestSecurityManager {

	private static final Logger log = Logger.getLogger(RestRequestCustomerSecurityManager.class.getName());

	/**
	 * Instead of the @Autowired, we are using @Resource(name="messageProvider") to achieve the same result. The @Resource
	 * is one of the annotations in the JSR-250 standard that defines a common set of Java annotations for use on both JSE and JEE
	 * platforms. Different from @Autowired, the @Resource annotation supports the name parameter for more fine-grained DI 
	 * requirements.
	 */
	@Resource(name="lmsApiCustomerService")
	private LmsApiCustomerService lmsApiCustomerService;

	public void setLmsApiCustomerService(LmsApiCustomerService lmsApiCustomerService) {
		this.lmsApiCustomerService = lmsApiCustomerService;
	}
	
	@PostConstruct
	public void init() {
		System.out.println();
	}
	
	@Override
	public LmsApiDistributor validateRequest(String apiKey, Long resellerId)throws Exception {
		return null;
	}

	@Override
	public LmsApiCustomer validateRequest(String apiKey, String customerCode) throws Exception {
		
		LmsApiCustomer lmsApiCustomer = lmsApiCustomerService.findApiKey(apiKey);
		customerApiValidation(lmsApiCustomer, customerCode);
		return lmsApiCustomer;
		
	}
	
	private boolean customerApiValidation(LmsApiCustomer lmsApiCustomer, String customerCode) throws Exception {
		
		String errorMessage = null;
		
		if (lmsApiCustomer == null) {
			errorMessage = NO_API_KEY_FOUND_ERROR;
			throwException(errorMessage);
		}
		
		Customer customer = lmsApiCustomer.getCustomer();
		
		if (customer == null) {
			errorMessage = "No Customer found: " + customerCode;
			throwException(errorMessage);
		}
		
		if (!(customer.getCustomerCode().equals(customerCode))){
			errorMessage = "Invalid customer code: " + customerCode;
			throwException(errorMessage);
		}
		
		if (!customer.getLmsApiEnabledTF().booleanValue()) {
			errorMessage = "LMS API is not enable for customer: " + customerCode;
			log.debug(errorMessage);
			throwException(errorMessage);
		}
		
		return true;
	}
	
}
