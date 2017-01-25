package com.softech.vu360.lms.web.restful.request.handler;

import com.softech.vu360.lms.web.restful.request.AbstractRequest;
import com.softech.vu360.lms.web.restful.request.AbstractResponse;
import com.softech.vu360.lms.web.restful.request.responsible.AbstractRequestResponsible;

public abstract class AbstractRequestHandler {

	
	protected AbstractRequestResponsible requestResponsible;
	
	public void setRequestResponsible(AbstractRequestResponsible requestResponsible) {
		this.requestResponsible = requestResponsible;
	}
	
	public abstract AbstractResponse handleRequest(AbstractRequest request) throws Exception;
	public abstract AbstractResponse handleRequest(AbstractRequest request, Exception error) throws Exception;
	
}
