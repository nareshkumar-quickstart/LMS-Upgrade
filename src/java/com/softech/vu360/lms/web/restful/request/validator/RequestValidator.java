package com.softech.vu360.lms.web.restful.request.validator;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;
import com.softech.vu360.lms.web.restful.request.AbstractRequest;
import com.softech.vu360.lms.web.restful.request.AbstractResponse;

@Component(value="restRequestValidator")
@Scope("request")
public class RequestValidator extends AbstractRequestValidator {

	@PostConstruct
	public void init() {
		System.out.println();
	}
	
	@Override
	public AbstractResponse validateRequest(AbstractRequest request, String apiKey, Long resellerId)throws Exception {
		
		AbstractResponse response = null;
		try {
			LmsApiDistributor lmsApiDistributor = restRequestSecurityManager.validateRequest(apiKey, resellerId);
			requestResponsible.setLmsApiDistributor(lmsApiDistributor);
		} catch (Exception e) {
			response = restRequestHandler.handleRequest(request, e);
		}
		return response;
	}

	@Override
	public AbstractResponse validateRequest(AbstractRequest request, String apiKey, String customerCode) throws Exception {
		
		AbstractResponse response = null;
		try {
			LmsApiCustomer lmsApiCustomer = restRequestSecurityManager.validateRequest(apiKey, customerCode);
			requestResponsible.setLmsApiCustomer(lmsApiCustomer);
		} catch (Exception e) {
			response = restRequestHandler.handleRequest(request, e);
		}
		return response;
	}

}
