package com.softech.vu360.lms.web.restful.request.handler;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softech.vu360.lms.web.restful.request.AbstractRequest;
import com.softech.vu360.lms.web.restful.request.AbstractResponse;

@Component(value="restRequestHandler")
@Scope("request")
public class RequestHandler extends AbstractRequestHandler {

	@PostConstruct
	public void init() {
		System.out.println();
	}
	
	@Override
	public AbstractResponse handleRequest(AbstractRequest request) throws Exception {
		try {
			return requestResponsible.handleRequest(request);
		} catch (Exception e) {
			return handleRequest(request, e);
		}
	}

	@Override
	public AbstractResponse handleRequest(AbstractRequest request, Exception error)throws Exception {
		return requestResponsible.handleRequestError(request, error);
	}
	
}
