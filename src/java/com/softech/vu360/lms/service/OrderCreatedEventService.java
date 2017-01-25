package com.softech.vu360.lms.service;

import com.softech.vu360.lms.vo.LmsSfOrderResultBuilder;
import com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest;
import com.softech.vu360.lms.webservice.message.storefront.RefundRequest;

public interface OrderCreatedEventService {

	public  LmsSfOrderResultBuilder processStoreFrontOrder(OrderCreatedRequest req);
	public boolean implementRefund(RefundRequest refund) throws Exception;
	
	public void orderAuditLog(LmsSfOrderResultBuilder lmsSfOrderResultBuilder)throws Exception;
	
}
