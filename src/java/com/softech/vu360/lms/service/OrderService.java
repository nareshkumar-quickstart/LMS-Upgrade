package com.softech.vu360.lms.service;

import java.util.List;

import com.softech.vu360.lms.model.CCILead;
import com.softech.vu360.lms.model.CustomerOrder;
import com.softech.vu360.lms.model.OrderRequestSOAPEnvelop;
import com.softech.vu360.lms.vo.LmsSfOrderResultBuilder;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedResponse;
import com.softech.vu360.lms.webservice.message.selfservice.Product;
import com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest;
import com.softech.vu360.lms.webservice.message.storefront.RefundRequest;

public interface OrderService {

	public CustomerOrder getOrderByGUID(String guid);
	public CustomerOrder getOrderById(Long id);
	
	public CustomerOrder saveOrder(CustomerOrder orderInfo);
	public boolean implementRefund(RefundRequest refund) throws Exception;
	
    public  List<CustomerOrder>  getOrderByCustomerId(Long customerId);
    
    
    public CCILead saveCCILead(CCILead cciLeadId);
    
    public OrderRequestSOAPEnvelop addOrderRequestSOAPEnvelop(OrderCreatedRequest orderCreatedRequest)throws Exception;
    public boolean updateOrderRequestSOAPEnvelop(OrderRequestSOAPEnvelop orderCreatedRequest);

    public boolean saveProducts(List<Product> lstProduct, com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest orderCreatedRequest, OrderCreatedResponse orderCreatedResponse)throws Exception;
    public boolean saveAndEnrollCourseOnly(List<Product> lstProduct, com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest orderCreatedRequest, OrderCreatedResponse orderCreatedResponse) throws Exception;
    public boolean submitOrder(com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest orderRequest, OrderCreatedResponse orderCreatedResponse) throws Exception;
    public void orderAuditLog(LmsSfOrderResultBuilder lmsSfOrderResultBuilder) throws Exception;
	// LMS-15514 - Updating DistributorCode in Distributor table. This function call when SF create Store on their end. Store Id is same as DistributorCode in LMS.
    public boolean storeCreation(com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest orderRequest) throws Exception;
    
}
