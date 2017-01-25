package com.softech.vu360.lms.web.restful.request.customer.contract.driver;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.softech.vu360.lms.web.restful.request.AbstractRequest;
import com.softech.vu360.lms.web.restful.request.AbstractResponse;
import com.softech.vu360.lms.web.restful.request.driver.RequestDriver;
import com.softech.vu360.lms.web.restful.request.responsible.AbstractRequestResponsible;

@Component(value="customerContractRequestDriver")
@Scope("request")
public class CustomerContractRequestDriver implements RequestDriver {

	protected AbstractRequestResponsible requestResponsible;

	@Override
	public void setRequestResponsible(AbstractRequestResponsible requestResponsible) {
		this.requestResponsible = requestResponsible;
	}

	@PostConstruct
	public void init() {
		System.out.println();
	}
	
	@Override
	public AbstractResponse driveRequest(AbstractRequest request) throws Exception {
		return requestResponsible.handleRequest(request);
	}
	
	@Override
	public AbstractResponse driveRequest(AbstractRequest request, Exception e) throws Exception {
		return requestResponsible.handleRequestError(request, e);
	}

}
