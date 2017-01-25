package com.softech.vu360.lms.webservice.impl;

import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.service.OrderService;
import com.softech.vu360.lms.webservice.SelfServiceWS;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedResponse;
import com.softech.vu360.util.DateUtil;

/**
 * @author ramiz.uddin
 * @since 0.1
 */
@Endpoint
public class SelfServiceWSImpl implements SelfServiceWS {

	private static final Logger log = Logger.getLogger(SelfServiceWSImpl.class
			.getName());
	private static final Logger LOGGER = Logger.getLogger(SelfServiceWSImpl.class.getName());
	private static final String TARGET_NAMESPACE = "http://com/softech/vu360/lms/selfservice/webservices/orderservice";
	private static final String ORDER_CREATED_EVENT = "OrderCreatedRequest";

	private OrderService orderService;

	@PayloadRoot(localPart = ORDER_CREATED_EVENT, namespace = TARGET_NAMESPACE)
	public OrderCreatedResponse orderCreatedRequestCompleted(OrderCreatedRequest orderCreatedRequest) throws Exception {
		
		OrderCreatedResponse orderCreatedResponse = new OrderCreatedResponse();
		orderCreatedResponse.setCustomerGUID("[empty]");
		orderCreatedResponse.setTransactionResult("[empty]");
		orderCreatedResponse.setTransactionResultMessage("[empty]");
		orderCreatedResponse.setEventDate(DateUtil.getCurrentDateTimeForXML());
		orderCreatedResponse.setTransactionGUID("-1");
		
		log.debug("--------------orderCreatedRequestCompleted----------1------" + orderCreatedRequest.getTransactionGUID());
		log.info("---------------orderCreatedRequestCompleted----------2-----");
		// create new storeCreation() function because of LMS-15514
		if(orderCreatedRequest.getOrder()!=null && orderCreatedRequest.getOrder().getOrderId()!=null && orderCreatedRequest.getOrder().getOrderId().equals("storecreation")){
			orderService.storeCreation(orderCreatedRequest);
		}else
			orderService.submitOrder(orderCreatedRequest, orderCreatedResponse);
		log.debug("--------------orderCreatedRequestCompleted----------3------" + orderCreatedRequest.getTransactionGUID());
		
		log.debug("   ******* Response of Request *******" );
		log.debug("   ******* Response Status *******" + orderCreatedResponse.getTransactionResult());
		log.debug("   ******* Response Customer GUID *******" + orderCreatedResponse.getCustomerGUID());
		log.debug("   ******* Response Customer Message *******" + orderCreatedResponse.getTransactionResultMessage());
		
		
		log.info("---------------orderCreatedRequestCompleted----------4-----");
		
		return orderCreatedResponse;
	}


	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
}
