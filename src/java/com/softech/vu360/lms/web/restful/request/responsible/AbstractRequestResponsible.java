package com.softech.vu360.lms.web.restful.request.responsible;

import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;
import com.softech.vu360.lms.web.restful.request.AbstractRequest;
import com.softech.vu360.lms.web.restful.request.AbstractResponse;

public abstract class AbstractRequestResponsible {

	protected LmsApiDistributor lmsApiDistributor;
	protected LmsApiCustomer lmsApiCustomer;
	
	public void setLmsApiDistributor(LmsApiDistributor lmsApiDistributor) {
		this.lmsApiDistributor = lmsApiDistributor;
	}

	public void setLmsApiCustomer(LmsApiCustomer lmsApiCustomer) {
		this.lmsApiCustomer = lmsApiCustomer;
	}
	
	public abstract AbstractResponse handleRequest(AbstractRequest request) throws Exception;
	public abstract AbstractResponse handleRequestError(AbstractRequest request, Exception e) throws Exception;
	
}
