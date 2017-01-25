package com.softech.vu360.lms.web.restful.request.validator;

import com.softech.vu360.lms.web.restful.request.AbstractRequest;
import com.softech.vu360.lms.web.restful.request.AbstractResponse;
import com.softech.vu360.lms.web.restful.request.handler.AbstractRequestHandler;
import com.softech.vu360.lms.web.restful.request.responsible.AbstractRequestResponsible;
import com.softech.vu360.lms.web.restful.request.security.RestRequestSecurityManager;

public abstract class AbstractRequestValidator {

	protected AbstractRequestHandler restRequestHandler;
	protected RestRequestSecurityManager restRequestSecurityManager;
	protected AbstractRequestResponsible requestResponsible;

	public void setRestRequestSecurityManager(RestRequestSecurityManager restRequestSecurityManager) {
		this.restRequestSecurityManager = restRequestSecurityManager;
	}
	
	public void setRestRequestHandler(AbstractRequestHandler restRequestHandler) {
		this.restRequestHandler = restRequestHandler;
	}
	
	public void setRequestResponsible(AbstractRequestResponsible requestResponsible) {
		this.requestResponsible = requestResponsible;
	}

	public abstract AbstractResponse validateRequest(AbstractRequest request, String apiKey, Long resellerId) throws Exception;
	public abstract AbstractResponse validateRequest(AbstractRequest request, String apiKey, String customerCode) throws Exception;
	
}
