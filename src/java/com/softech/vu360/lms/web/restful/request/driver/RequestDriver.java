package com.softech.vu360.lms.web.restful.request.driver;

import com.softech.vu360.lms.web.restful.request.AbstractRequest;
import com.softech.vu360.lms.web.restful.request.AbstractResponse;
import com.softech.vu360.lms.web.restful.request.responsible.AbstractRequestResponsible;

public interface RequestDriver {

	AbstractResponse driveRequest(AbstractRequest request) throws Exception;
	AbstractResponse driveRequest(AbstractRequest request, Exception e) throws Exception;
	void setRequestResponsible(AbstractRequestResponsible requestResponsible);
	
}
