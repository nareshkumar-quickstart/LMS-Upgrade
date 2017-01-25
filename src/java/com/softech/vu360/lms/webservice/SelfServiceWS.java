package com.softech.vu360.lms.webservice;

import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedResponse;



public interface SelfServiceWS {
	public OrderCreatedResponse orderCreatedRequestCompleted(OrderCreatedRequest orderCreatedRequest)throws Exception;
	
}
