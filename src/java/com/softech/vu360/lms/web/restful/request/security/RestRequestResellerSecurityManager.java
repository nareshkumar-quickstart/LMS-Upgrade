package com.softech.vu360.lms.web.restful.request.security;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;
import com.softech.vu360.lms.service.lmsapi.LmsApiDistributorService;

@Component(value="restRequestResellerSecurityManager")
@Scope("request")
public class RestRequestResellerSecurityManager extends RestRequestSecurityManager {

	private static final Logger log = Logger.getLogger(RestRequestResellerSecurityManager.class.getName());
	
	/**
	 * Instead of the @Autowired, we are using @Resource(name="messageProvider") to achieve the same result. The @Resource
	 * is one of the annotations in the JSR-250 standard that defines a common set of Java annotations for use on both JSE and JEE
	 * platforms. Different from @Autowired, the @Resource annotation supports the name parameter for more fine-grained DI 
	 * requirements.
	 */
	@Resource(name="lmsApiDistributorService")
	private LmsApiDistributorService lmsApiDistributorService;

	public void setLmsApiDistributorService(LmsApiDistributorService lmsApiDistributorService) {
		this.lmsApiDistributorService = lmsApiDistributorService;
	}
	
	@PostConstruct
	public void init() {
		System.out.println();
	}
	
	@Override
	public LmsApiCustomer validateRequest(String apiKey, String customerCode)throws Exception {
		return null;
	}
	
	@Override
	public LmsApiDistributor validateRequest(String apiKey, Long resellerId) throws Exception {
		
		LmsApiDistributor lmsApiDistributor = lmsApiDistributorService.findApiKey(apiKey);
		resellerApiValidation(lmsApiDistributor, resellerId);
		return lmsApiDistributor;
		
	}
	
	private boolean resellerApiValidation(LmsApiDistributor lmsApiDistributor, Long resellerId) throws Exception {
		
		String errorMessage = null;
		
		if (lmsApiDistributor == null) {
			errorMessage = NO_API_KEY_FOUND_ERROR;
			throwException(errorMessage);
		}
		
		Distributor distributor = lmsApiDistributor.getDistributor();
		if (distributor == null) {
			errorMessage = "No Reseller found for reseller Id: " + resellerId;
			throwException(errorMessage);
		}
		
		if (!(distributor.getId().equals(resellerId))){
			errorMessage = "Invalid reseller Id: " + resellerId;
			throwException(errorMessage);
		}
		
		boolean isApiEnabled = distributor.getLmsApiEnabledTF();
		if (!isApiEnabled) {
			errorMessage = "LMS API is not enable for resellerId: " + distributor.getId();
			log.debug(errorMessage);
			throwException(errorMessage);
		}
		
		return true;
	}
	
}
