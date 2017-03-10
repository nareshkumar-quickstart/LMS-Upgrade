package com.softech.vu360.lms.service.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.CCILead;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerOrder;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LineItem;
import com.softech.vu360.lms.model.LmsSfOrderAuditLog;
import com.softech.vu360.lms.model.OrderRequestSOAPEnvelop;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.model.SubscriptionCustomerEntitlement;
import com.softech.vu360.lms.model.SubscriptionKit;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrderCreatedEventService;
import com.softech.vu360.lms.service.OrderService;
import com.softech.vu360.lms.service.SubscriptionService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.LmsSfOrderResultBuilder;
import com.softech.vu360.lms.web.controller.model.AddCustomerForm;
import com.softech.vu360.lms.webservice.message.storefront.Order;
import com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest;
import com.softech.vu360.lms.webservice.message.storefront.OrderLineItem;
import com.softech.vu360.lms.webservice.message.storefront.RefundRequest;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.VU360Properties;

public class OrderCreatedEventServiceImpl implements OrderCreatedEventService {

	public static final int DEFAULT_TOS_IN_DAYS = (Integer.parseInt(VU360Properties.getVU360Property("defaultTOS")));
	private static final Logger log = Logger.getLogger(OrderCreatedEventServiceImpl.class.getName());
	private CustomerService customerService = null;
	private DistributorService distributorService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private LearnerService learnerService = null;
	private EnrollmentService enrollmentService = null;
	private EntitlementService entitlementService = null;
	private OrderService orderService=null;
	private VU360UserService vu360UserService = null;
	private boolean isCorporateAuthorVar;
	private SubscriptionService subscriptionService = null;
	
	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}
	
	/**
	 * This method processes the OrderCreatedRequest object and returns true if every thing goes ok.
	 * @return boolean
	 * @author sm.humayun refactored on 2012-01-27 | LMS-12567
	 */
	public LmsSfOrderResultBuilder processStoreFrontOrder(OrderCreatedRequest orderCreatedRequest)
	{
		Customer customer = null;
		Distributor dist = null;
		VU360User vu360User = null;
		OrderRequestSOAPEnvelop orderEnvelop = null;
		LmsSfOrderResultBuilder lmsSfOrderResultBuilder = new LmsSfOrderResultBuilder();
		try 
		{
			log.info(" --------------- OrderCreatedEventServiceImpl.processStoreFrontOrder() - START ----------------- ");
		
			com.softech.vu360.lms.webservice.message.storefront.Order order = orderCreatedRequest.getOrder();
			com.softech.vu360.lms.webservice.message.storefront.Customer sfCustomer = order.getCustomer();
			com.softech.vu360.lms.webservice.message.storefront.Address sfAddress = sfCustomer.getPrimaryAddress();
			com.softech.vu360.lms.webservice.message.storefront.Contact sfContact = sfCustomer.getPrimaryContact();
			vu360User = vu360UserService.findUserByUserName(sfCustomer.getPrimaryContact().getAuthenticationCredential().getUsername());
			Learner learner = vu360User != null ? vu360User.getLearner() : null;
			Object[] customerAndLearner = null;
			
			// Create SOAP Envelop -- Start
			// LMS-13133
			log.info("SOAP Envelope Creation Start");
			try {
				orderEnvelop = orderService.addOrderRequestSOAPEnvelop(orderCreatedRequest);
			}
			catch (Exception e) {
				e.printStackTrace();
				log.error("Exception while creation SOAP Envelop for Order Id = "+orderCreatedRequest.getOrder().getOrderId()+" --- "+e.getMessage(),e);
				orderEnvelop.setOrderError(e.getMessage());
				orderService.updateOrderRequestSOAPEnvelop(orderEnvelop);
			}
			log.info("SOAP Envelope Creation End");
			// SOAP Envelop -- End
			
			dist = distributorService.findDistibutorByDistributorCode(order.getDistributorId());
			
			if(dist.isCorporateAuthorVar()){
								
				if(learner == null){
					customer = customerService.findDefaultCustomerByDistributor(dist);
					if(customer == null){
						throw new Exception("No default customer was found for the distributor(ID = " + dist.getId() + "). A distributor must have a \"Default Customer\" if that distributor is set to \"Corporate Author Var\".");
					}
					learner = learnerService.addLearnerForDefaultCustomer(customer, sfContact, sfAddress);
					vu360User = learner.getVu360User();
				}else{
					customer = validateDistributorForLearner(learner, dist);
					learner = learnerService.updateLearner(learner, customer, sfContact, sfAddress);
				}
			}else{	
				customer = customerService.findCustomerByCustomerGUID(sfCustomer.getCustomerId());
				if(sfCustomer.getCustomerId().equals("-1")){
					if(vu360User != null){
						customer = learner.getCustomer();
						if(!order.getDistributorId().equals(customer.getDistributor().getDistributorCode())){
							throw new IllegalStateException("Same Customer can not be enrolled via more than one Resellers - Store Id = " 
									+ order.getDistributorId() + " & Distributor Code = " + customer.getDistributor().getDistributorCode());
						}else{
							customerAndLearner = saveCustomer(customer, learner, sfContact, order);
							customer = (Customer) customerAndLearner[0];
							learner = (Learner) customerAndLearner[1];
						}
					}
					else
						customer = addCustomer(order);
				}
				else{
					customer = customerService.findCustomerByCustomerGUID(sfCustomer.getCustomerId());
					if ( customer== null ) 
						customer = addCustomer(order);
					else{
						customerAndLearner = saveCustomer(customer, learner, sfContact, order);
						customer = (Customer) customerAndLearner[0];
						learner = (Learner) customerAndLearner[1];
					}
				}
			}

			List<LineItem> lineItemList = new ArrayList<LineItem>();
			CustomerOrder customerOrder = new CustomerOrder();
		
			if ( !order.isIssubscription() ){
				createEntitlements(customer, learner, customerOrder, lineItemList, order,lmsSfOrderResultBuilder);
			}else{
				ProcessEntitlements(customer, learner, customerOrder, lineItemList, order,lmsSfOrderResultBuilder);
			}
			saveCustomerOrder(customerOrder, lineItemList, customer, order, orderCreatedRequest);
		}catch(Exception exception){
			log.error("Error Occured in orderCreatedEvent:  " + orderCreatedRequest.getOrder().getOrderId()+ " --- " + exception.getMessage(), exception);
			orderEnvelop.setOrderError(exception.getMessage());
			orderService.updateOrderRequestSOAPEnvelop(orderEnvelop);
		}finally{
			log.info(" --------------- OrderCreatedEventServiceImpl.processStoreFrontOrder() - END ----------------- ");
		}
		
		lmsSfOrderResultBuilder.setCustomer(customer);
		if(dist != null && dist.isCorporateAuthorVar()){
			lmsSfOrderResultBuilder.setVU360User(vu360User);
			this.setCorporateAuthorVar(dist.isCorporateAuthorVar());
		}else{
			this.setCorporateAuthorVar(false);
		}
		
		return lmsSfOrderResultBuilder;
	}
	
	private Customer validateDistributorForLearner(Learner learner, Distributor distributor) throws Exception{
		Customer returnedCustomer = null;
		Customer learnerCustomer = learner.getCustomer();
		Distributor customerDistributor = learnerCustomer.getDistributor(); 
		try {
			if(customerDistributor.getId() == distributor.getId()){
//				if(learnerCustomer.getId() == defaultCustomer.getId()){
//					returnedCustomer = defaultCustomer;
//				}else{
					returnedCustomer = learnerCustomer;
//				}
			}else{
				//throw different distributor exception
				throw new Exception("Customer's(ID = "+learnerCustomer.getId()+") Distributor(ID = " + distributor.getId() + ") in SOAP Envelop is different from its original Distributor(ID =" + customerDistributor.getId());
			}	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}		
		return returnedCustomer;
	}
	
	private void saveCustomerOrder (CustomerOrder customerOrder, List<LineItem> lineItemList, Customer customer
			, com.softech.vu360.lms.webservice.message.storefront.Order order, OrderCreatedRequest orderCreatedRequest)
	{
		try
		{
			log.debug("Saving/ Creating OrderInfo");								
			customerOrder.setLineItem(lineItemList);
			customerOrder.setCustomer(customer);
			customerOrder.setOrderAmount((float)0);
			customerOrder.setOrderGUID(order.getOrderId());
			customerOrder.setTransactionGUID(orderCreatedRequest.getTransactionGUID());
			customerOrder.setOrderDate(new Date());
			orderService.saveOrder(customerOrder);
			
			// CCILead Id
			String cciLeadId = orderCreatedRequest.getCCILeadId();
			
			try
			{
				// Only saving CCILeadId if present.
				if((cciLeadId!=null) && (!cciLeadId.equals("")))
				{
					//String cciLeadOrderId = orderCreatedRequest.getOrder().getOrderId();
					CCILead cciLead = new CCILead();
					cciLead.setCciLeadId(cciLeadId);
					//cciLead.setOrderId(Long.parseLong(cciLeadOrderId));
					cciLead.setCustomerOrder(customerOrder);
					orderService.saveCCILead(cciLead);
					log.debug("CCI Lead Id " + cciLead.getCciLeadId());	
					
				}
			}
			catch(Exception e)
			{
				log.debug("Exception saving CCILead: " + e, e);
			}		
			
			
			log.info("OrderInfo Saved");
			
		}
		catch(Exception ex){
			log.error("ERROR in Saving OrderInfo: for orderId: " + order.getOrderId() + " --- " + ex.getMessage(), ex);
		}
	}

	private void createEntitlements (Customer customer, Learner learner, CustomerOrder customerOrder, List<LineItem> lineItemList
			, com.softech.vu360.lms.webservice.message.storefront.Order order,LmsSfOrderResultBuilder lmsSfOrderResultBuilder) throws ParseException
	{
		for(com.softech.vu360.lms.webservice.message.storefront.OrderLineItem orderLineItem : order.getLineItem())
		{
			CourseCustomerEntitlement customerEntitlement = new CourseCustomerEntitlement();
			log.info("SIZE of LINEITEM: " + order.getLineItem().size());
			BigInteger qty = orderLineItem.getQuantity();
			String strCourseGUID = orderLineItem.getGuid();
			Course course = courseAndCourseGroupService.getCourseByGUIDRefreshCourse(strCourseGUID);
			CourseGroup courseGroup = courseAndCourseGroupService.getCourseGroupByguid(orderLineItem.getGroupguid());
			String courseGroupGuid = courseGroup.getGuid();
			if(course == null)
			{
				log.error("Course Not Found against GUID: "+strCourseGUID);
				//Adding audit line item here because of the the current code. Should have thrown exception and log audit from the calling code
				lmsSfOrderResultBuilder.getOrderAuditList().add(new LmsSfOrderAuditLog(order.getOrderId(),strCourseGUID,courseGroupGuid,"Invalid","Invalid course guid","OrderEvent",false));
				continue;
			}
			if(course.isRetired())
			{
				log.error("Course was retired against GUID: "+strCourseGUID);
				//Adding audit line item here because of the the current code. Should have thrown exception and log audit from the calling code
				lmsSfOrderResultBuilder.getOrderAuditList().add(new LmsSfOrderAuditLog(order.getOrderId(),strCourseGUID,courseGroupGuid,"Invalid","Invalid course status : retired","OrderEvent",false));
				continue;
			}
			log.debug("Found Course against GUID:" + course.getCourseGUID() );
			//Adding audit line item here because of the the current code. Should have thrown exception and log audit from the calling code
			//lmsSfOrderResultBuilder.getOrderAuditList().add(new LmsSfOrderAuditLog(order.getOrderId(),strCourseGUID,courseGroupGuid,"Valid","Valid course guid","OrderEvent",true));
			lmsSfOrderResultBuilder.setHasOneValidCourseGuid(true);
			log.error("Creating Entitlement");
			
			customerEntitlement.setCustomer(customer);
			customerEntitlement.setName(customer.getName());
			customerEntitlement.setMaxNumberSeats(qty.intValue());
			
			
			/* SF-902 Order may have TOS null or 0 and in that we should use DEFAULT_TOS_IN_DAYS */ 
			if(orderLineItem.getTermOfService()==null || orderLineItem.getTermOfService().intValue()<1){
				
				/* more detail are in LMS-13438 */
				Date maxEndDate= entitlementService.getMaxDistributorEntitlementEndDate(customer.getDistributor());
				customerEntitlement.setDefaultTermOfServiceInDays( getValidContractExpirationDate(maxEndDate) );
			}	
			else{
				customerEntitlement.setDefaultTermOfServiceInDays(orderLineItem.getTermOfService().intValue());
			}

			customerEntitlement.setAllowUnlimitedEnrollments(false);
			customerEntitlement.setAllowSelfEnrollment(false);
			
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date dt = Calendar.getInstance().getTime();
			customerEntitlement.setStartDate(formatter.parse(formatter.format(dt)));
			customerEntitlement.setContractCreationDate(new Date());

			CourseCustomerEntitlement custEntitlement = (CourseCustomerEntitlement) entitlementService.addCustomerEntitlement(customer, customerEntitlement);
			entitlementService.addEntitlementItem(custEntitlement, course, courseGroup);
			log.error("Saved Customer Entitlement");
		
			// if customer is a b2c customer add enrollments
			if ( customer.getCustomerType().equalsIgnoreCase(Customer.B2C) || customer.getDistributor().isCorporateAuthorVar()) 
			{
				if(learner == null)
					learner = learnerService.getLearnerForB2CCustomer(customer);
				enrollmentService.addEnrollmentsForCourseEntitlements(learner, custEntitlement, orderLineItem.getLineitemguid() == null ?"" : orderLineItem.getLineitemguid());
				//No need to update custEntitlement separately. it is being update with learner enrollment with 1 increment of NUMBERSEATSUSED field. so commenting out below line
				//entitlementService.updateSeatsUsed(custEntitlement, null, 1);
			}

			LineItem lineItem = new LineItem();
			lineItem.setItemGUID(orderLineItem.getGuid());
			lineItem.setQuantity(orderLineItem.getQuantity().intValue());
			lineItem.setCustomerEntitlement(customerEntitlement);
			lineItem.setOrderInfo(customerOrder);
			 
			lineItemList.add(lineItem);
		}
	}
	
	
	private void createNormalEntitlements (Customer customer, Learner learner, CustomerOrder customerOrder, List<LineItem> lineItemList,
			 com.softech.vu360.lms.webservice.message.storefront.Order order,LmsSfOrderResultBuilder lmsSfOrderResultBuilder,
			List<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem> orderNormalLineItems) throws ParseException
	{
		for(com.softech.vu360.lms.webservice.message.storefront.OrderLineItem orderLineItem : orderNormalLineItems)
		{
			CourseCustomerEntitlement customerEntitlement = new CourseCustomerEntitlement();
			log.info("SIZE of LINEITEM: " + order.getLineItem().size());
			BigInteger qty = orderLineItem.getQuantity();
			String strCourseGUID = orderLineItem.getGuid();
			Course course = courseAndCourseGroupService.getCourseByGUID(strCourseGUID);
			CourseGroup courseGroup = courseAndCourseGroupService.getCourseGroupByguid(orderLineItem.getGroupguid());
			String courseGroupGuid = courseGroup.getGuid();
			if(course == null)
			{
				log.error("Course Not Found against GUID: "+strCourseGUID);
				//Adding audit line item here because of the the current code. Should have thrown exception and log audit from the calling code
				lmsSfOrderResultBuilder.getOrderAuditList().add(new LmsSfOrderAuditLog(order.getOrderId(),strCourseGUID,courseGroupGuid,"Invalid","Invalid course guid","OrderEvent",false));
				continue;
			}
			log.debug("Found Course against GUID:" + course.getCourseGUID() );
			//Adding audit line item here because of the the current code. Should have thrown exception and log audit from the calling code
			//lmsSfOrderResultBuilder.getOrderAuditList().add(new LmsSfOrderAuditLog(order.getOrderId(),strCourseGUID,courseGroupGuid,"Valid","Valid course guid","OrderEvent",true));
			lmsSfOrderResultBuilder.setHasOneValidCourseGuid(true);
			log.error("Creating Entitlement");
			
			customerEntitlement.setCustomer(customer);
			customerEntitlement.setName(customer.getName());
			customerEntitlement.setMaxNumberSeats(qty.intValue());
			
			
			/* SF-902 Order may have TOS null or 0 and in that we should use DEFAULT_TOS_IN_DAYS */ 
			if(orderLineItem.getTermOfService()==null || orderLineItem.getTermOfService().intValue()<1){
				
				/* more detail are in LMS-13438 */
				Date maxEndDate= entitlementService.getMaxDistributorEntitlementEndDate(customer.getDistributor());
				customerEntitlement.setDefaultTermOfServiceInDays( getValidContractExpirationDate(maxEndDate) );
			}	
			else{
				customerEntitlement.setDefaultTermOfServiceInDays(orderLineItem.getTermOfService().intValue());
			}

			customerEntitlement.setAllowUnlimitedEnrollments(false);
			customerEntitlement.setAllowSelfEnrollment(false);
			
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date dt = Calendar.getInstance().getTime();
			customerEntitlement.setStartDate(formatter.parse(formatter.format(dt)));
			customerEntitlement.setContractCreationDate(new Date());

			CourseCustomerEntitlement custEntitlement = (CourseCustomerEntitlement) entitlementService.addCustomerEntitlement(customer, customerEntitlement);
			entitlementService.addEntitlementItem(custEntitlement, course, courseGroup);
			log.error("Saved Customer Entitlement");
		
			// if customer is a b2c customer add enrollments
			if ( customer.getCustomerType().equalsIgnoreCase(Customer.B2C) || customer.getDistributor().isCorporateAuthorVar()) 
			{
				if(learner == null)
					learner = learnerService.getLearnerForB2CCustomer(customer);
				enrollmentService.addEnrollmentsForCourseEntitlements(learner, custEntitlement, orderLineItem.getLineitemguid() == null ?"" : orderLineItem.getLineitemguid());
				entitlementService.updateSeatsUsed(custEntitlement, null, 1);
			}

			LineItem lineItem = new LineItem();
			lineItem.setItemGUID(orderLineItem.getGuid());
			lineItem.setQuantity(orderLineItem.getQuantity().intValue());
			lineItem.setCustomerEntitlement(customerEntitlement);
			lineItem.setOrderInfo(customerOrder);
			 
			lineItemList.add(lineItem);
		}
	}
	
	private void createSubsctiptionEntitlements (Customer customer, Learner learner, CustomerOrder customerOrder, List<LineItem> lineItemList
			,Order order, LmsSfOrderResultBuilder lmsSfOrderResultBuilder,	String subscriptionCode,List<OrderLineItem> orderSubscriptionLineItem) 
					throws ParseException{
		
		SubscriptionKit objSubkit = null;
		objSubkit = subscriptionService.getSubscriptionKitByGUID(orderSubscriptionLineItem.get(0).getGuid());
	
		if(objSubkit!=null)
			lmsSfOrderResultBuilder.setHasOneValidCourseGuid(true);
		else{
			log.error(" -------------------------------------------------------------------------------------------------------");
			log.error("......Subscription Guid <  " +orderSubscriptionLineItem.get(0).getGuid() + "  > is not Available in SUBSCRIPTION_KIT table...");
			log.error(" -------------------------------------------------------------------------------------------------------");
			return;
		}
		SubscriptionCustomerEntitlement customerEntitlement = new SubscriptionCustomerEntitlement();
		log.info("Creating Entitlement");
		
		BigInteger qty =  orderSubscriptionLineItem.get(0).getQuantity();
		//int defaultTermOfServiceInDays = (orderSubscriptionLineItem.get(0).getTermOfService() == null) ?  orderSubscriptionLineItem.get(0).getTermOfService().intValue();
		customerEntitlement.setCustomer(customer);
		customerEntitlement.setName(customer.getName());
		customerEntitlement.setMaxNumberSeats(qty.intValue());
		
//		SF-902 Order may have TOS null or 0 and in that we should use DEFAULT_TOS_IN_DAYS */ 
//		No End date for subscription type contract http://jira.360training.com/browse/LMS-16952 */
//		if( orderSubscriptionLineItem.get(0).getTermOfService()==null || orderSubscriptionLineItem.get(0).getTermOfService().intValue()<1){
//			/* more detail are in LMS-13438 */
//			Date maxEndDate= entitlementService.getMaxDistributorEntitlementEndDate(customer.getDistributor());
//			customerEntitlement.setDefaultTermOfServiceInDays( getValidContractExpirationDate(maxEndDate) );
//		}else{
//			customerEntitlement.setDefaultTermOfServiceInDays( orderSubscriptionLineItem.get(0).getTermOfService().intValue());
//		}
		
		/* AS per user story Contract TOS is 99 years */
		customerEntitlement.setDefaultTermOfServiceInDays( 99*365);
		customerEntitlement.setAllowUnlimitedEnrollments(false);
		customerEntitlement.setAllowSelfEnrollment(false);
		
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date dt = Calendar.getInstance().getTime();
		customerEntitlement.setStartDate(formatter.parse(formatter.format(dt)));
		customerEntitlement.setContractCreationDate(new Date());
		
		SubscriptionCustomerEntitlement custEntitlement = (SubscriptionCustomerEntitlement) entitlementService.addCustomerEntitlement(customer, customerEntitlement);
		// For Subscription order, order object holding only one item 
		//SubscriptionKit objSubkit = null;
		//try{
			objSubkit = subscriptionService.getSubscriptionKitByGUID(orderSubscriptionLineItem.get(0).getGuid());
		//}catch(NullPointerException npe){
			//throw new NullPointerException("......Subscription Guid <  " +orderSubscriptionLineItem.get(0).getGuid()  + "  > is not Available in SUBSCRIPTION_KIT table.......");
		//}
		//------entitlementService.addEntitlementItem(custEntitlement, course, courseGroup);
		log.error("Saved Customer Entitlement");
		
		String subscriptionName =  orderSubscriptionLineItem.get(0).getSubscriptionname();
		String subscriptionType =  orderSubscriptionLineItem.get(0).getSubscriptiontype();
		
		
		Subscription subscription = new Subscription();
		subscription.setCustomerEntitlement(custEntitlement);
		subscription.setSubscriptionCode(subscriptionCode);
		subscription.setSubscriptionName(subscriptionName);
		subscription.setSubscriptionType(subscriptionType);
		subscription.setSubscriptionQty(qty.intValue());
		subscription.setCreateDate(new Date());
		subscription.setSubscriptionStatus("Active");
		subscription.setCustomerType(customer.getCustomerType());
		subscription.setSubscriptionKit(objSubkit);
		subscription = subscriptionService.saveSubscription(subscription);
		
		log.info("SIZE of LINEITEM subscription : " + subscriptionCode + " : " +  orderSubscriptionLineItem.size() );
		
		//List<SubscriptionCourse> lstsubscriptionCourses = new ArrayList<SubscriptionCourse>();
		
		for(com.softech.vu360.lms.webservice.message.storefront.OrderLineItem orderLineItem : orderSubscriptionLineItem){
			int TOS=0 ;
			
			if(orderLineItem.getTermOfService()==null || orderLineItem.getTermOfService().intValue()<1){
				/* more detail are in LMS-13438 */
				Date maxEndDate= entitlementService.getMaxDistributorEntitlementEndDate(customer.getDistributor());
				//customerEntitlement.setDefaultTermOfServiceInDays( getValidContractExpirationDate(maxEndDate) );
				TOS = getValidContractExpirationDate(maxEndDate);
			}	
			else{
				customerEntitlement.setDefaultTermOfServiceInDays(orderLineItem.getTermOfService().intValue());
				TOS = orderLineItem.getTermOfService().intValue();
			}

			subscriptionService.saveSubscriptionCourses(subscription.getId(), objSubkit.getId());
			LineItem lineItem = new LineItem();
			lineItem.setItemGUID(orderLineItem.getGuid());
			lineItem.setQuantity(orderLineItem.getQuantity().intValue());
			lineItem.setCustomerEntitlement(customerEntitlement);
			lineItem.setOrderInfo(customerOrder);
			lineItemList.add(lineItem);
		}
		
		// if customer is a b2c customer add enrollments
		if ( customer.getCustomerType().equalsIgnoreCase(Customer.B2C) || customer.getDistributor().isCorporateAuthorVar()) 
		{
			if(learner == null)
				learner = learnerService.getLearnerForB2CCustomer(customer);
			enrollmentService.addPreEnrollmentsForSubscription(learner, subscription);
			//enrollmentService.addEnrollmentsForSubscription(learner, subscription,  subscriptionCode);
			//enrollmentService.addEnrollmentsForCourseEntitlements(learner, custEntitlement, orderLineItem.getLineitemguid() == null ?"" : orderLineItem.getLineitemguid());
			entitlementService.updateSeatsUsed(custEntitlement, null, 1);
		}
		
	}
	
	private void ProcessEntitlements(Customer customer, Learner learner, CustomerOrder customerOrder, List<LineItem> lineItemList
			, com.softech.vu360.lms.webservice.message.storefront.Order order,LmsSfOrderResultBuilder lmsSfOrderResultBuilder) throws ParseException
	{
		Set<String> uniqueSubscriptionCodes = getUniqueSubsctionCodeListInOrder(order);
 		HashMap<String,List<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem>> EntitlementWithLineItems = getDifferentEntitlementTypeWithLineItems (uniqueSubscriptionCodes,order);
				
		for(String uniqueSubscriptionCode : uniqueSubscriptionCodes){
			List<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem> ContractLineItems = EntitlementWithLineItems.get(uniqueSubscriptionCode);
			if(uniqueSubscriptionCode.equals("-1")){
				//Course CustomerEntitlement
				createNormalEntitlements(customer, learner, customerOrder,lineItemList, order, lmsSfOrderResultBuilder,ContractLineItems);
			}else{
				//Subscription CustomerEntitlement
			createSubsctiptionEntitlements(customer, learner, customerOrder, lineItemList, order, lmsSfOrderResultBuilder, uniqueSubscriptionCode,ContractLineItems);	
			}
		}
	}
	private Set<String> getUniqueSubsctionCodeListInOrder(com.softech.vu360.lms.webservice.message.storefront.Order order)
	{
		Set<String> uniqueSubscrionCode = new HashSet<String>();
		for(com.softech.vu360.lms.webservice.message.storefront.OrderLineItem orderLineItem : order.getLineItem()){
		//	if(!orderLineItem.equals("-1")){
				uniqueSubscrionCode.add(orderLineItem.getSubscriptioncode());
		//	}
		}
		return uniqueSubscrionCode;
	}
	private HashMap<String,List<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem>> getDifferentEntitlementTypeWithLineItems(Set<String> uniqueSubscrionCodes,com.softech.vu360.lms.webservice.message.storefront.Order order)
	{
		
		HashMap<String,List<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem>> contractMap = new HashMap<String,List<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem>>(); 
		
		for(String uniqueSubscrionCode : uniqueSubscrionCodes){
		
			List<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem> contractLineItems = new ArrayList<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem>();
			
			for(com.softech.vu360.lms.webservice.message.storefront.OrderLineItem orderLineItem : order.getLineItem()){
				if(orderLineItem.getSubscriptioncode().equals(uniqueSubscrionCode)){
					//uniqueSubscrionCode.add(orderLineItem.getSubscriptioncode());
					contractLineItems.add(orderLineItem);
				}
		contractMap.put(uniqueSubscrionCode, contractLineItems);
				  
		}
		}
		return contractMap;	
	}
	
	
	private Object[] saveCustomer (Customer customerToUpdate, Learner learner, com.softech.vu360.lms.webservice.message.storefront.Contact sfContact
			, com.softech.vu360.lms.webservice.message.storefront.Order order)
	{
		Customer customerFromDb = customerService.loadForUpdateCustomer(customerToUpdate.getId());
		log.info("Returning / Existing Customer Name from DB: " + customerFromDb.getFirstName());

        // LMS-13679
        // Customer profile get updates when the order
        // submitted using customer user credentials

        // retrieve customer's learner
        long customerLearnerId = learnerService.getLearnerForSelectedCustomer(customerToUpdate.getId());
        Learner customerLearner = learnerService.getLearnerByID(customerLearnerId);

        // The following check placed to update the customer's
        // profile if the customer login by itself. In such
        // case both its learner and customer's profile will
        // get update
        if(
                customerLearner == null
                        ||
                sfContact.getAuthenticationCredential().getUsername().equals(
                        customerLearner.getVu360User().getUsername())
                )
        {
		    fillCustomer(customerToUpdate, order);
        }
        else // Customer profile get updates of order and customer type
        {
            customerToUpdate.setActive(true);

            // And, when a customer purchases any course
            // with quantity more than 1 then consider
            // it now as a B2B customer instead of B2C
            if
                    (
                        customerToUpdate.getCustomerType().equals(Customer.B2C)
                        &&
                        isCustomerEntitledForManagerRole(order.getLineItem())
                    )
            {
                customerToUpdate.setCustomerType(Customer.B2B);
            }
        }
		log.info("Customer Type from DB / Updated Customer Type: " + customerFromDb.getCustomerType() + " --- " + customerToUpdate.getCustomerType());
		if(customerToUpdate.getCustomerType().equalsIgnoreCase(Customer.B2C))
			learner = updateLearner(learner, customerToUpdate, sfContact, false);
		else
			learner = updateLearner(learner, customerToUpdate, sfContact, true);
		return new Object[] {customerService.saveCustomer(customerToUpdate,false), learner};
	}
	
	private Learner updateLearner (Learner learner, Customer customerToUpdate
			, com.softech.vu360.lms.webservice.message.storefront.Contact sfContact, boolean assignManagerRole)
	{
		log.info("Updating Learner's User and Learner Profile Addresses...");		
		learner=learnerService.loadForUpdateLearner(learner.getId());
		VU360User user=learner.getVu360User();					
		user.setPassword(sfContact.getAuthenticationCredential().getPassword());
		user.setPassWordChanged(true);
		user.setFirstName(sfContact.getFirstName());
		user.setLastName(sfContact.getLastName());
		user.setLastUpdatedDate(Calendar.getInstance().getTime());
		Address billingAddress = learner.getLearnerProfile().getLearnerAddress();
		Address shippingaddress = learner.getLearnerProfile().getLearnerAddress2();
		if(billingAddress !=null)
			this.copyAddress(sfContact.getBillingAddress(), billingAddress);
		if(shippingaddress !=null)
			this.copyAddress(sfContact.getShippingAddress(), shippingaddress);
		learner.getLearnerProfile().setLearnerAddress(billingAddress);
		learner.getLearnerProfile().setLearnerAddress2(shippingaddress);
		learner.getLearnerProfile().setMobilePhone(sfContact.getPrimaryPhone());
		learner.getLearnerProfile().setOfficePhone(sfContact.getAlternatePhone());
		learner.setCustomer(customerToUpdate);
		try
		{
			if(assignManagerRole)
				assignManagerRole(customerToUpdate, user);
			learnerService.saveLearner(learner);
			log.info("Learner's User and Learner Profile Addresses are updated!");		
		}
		catch(Exception e)
		{
			log.error("Error occured while saving learner " + e.getMessage(), e);
		}
		return learner;
	}

	private void assignManagerRole (Customer customerToUpdate, VU360User user)
	{
		log.info("Assigning MANAGER role...");		
		LMSRole LMSRoleManager = new LMSRole();
		LMSRoleManager.setOwner(customerToUpdate);
		LMSRoleManager.setRoleName("MANAGER");
		LMSRoleManager.setSystemCreated(true);
		LMSRoleManager.setRoleType(LMSRole.ROLE_TRAININGMANAGER);
		List<LMSRoleLMSFeature>	lmsPermissions = customerService.getLMSPermissions(
				customerToUpdate.getDistributor(), LMSRole.ROLE_TRAININGMANAGER, LMSRoleManager);
		LMSRoleManager.setLmsPermissions(lmsPermissions);
		LMSRoleManager = learnerService.addRole(LMSRoleManager, customerToUpdate);
		user.addLmsRole(LMSRoleManager);
		learnerService.assignUserToRole(user, LMSRoleManager);
		log.debug("MANAGER role ADDED!");
	}
	
	private Customer addCustomer (com.softech.vu360.lms.webservice.message.storefront.Order order)
	{
		Customer customer = null;
		Distributor dist = distributorService.findDistibutorByDistributorCode(order.getDistributorId());
		if ( dist == null ) 
		{
			log.error("Distributor against Code: " + order.getDistributorId() + " not found!");
			throw new IllegalArgumentException("could not find the distributor with distributorCode "+order.getDistributorId());
		}
		AddCustomerForm customerForm = new AddCustomerForm();
		customerForm = fillCustomerForm(order);
		customer = customerService.addCustomer(false, null, dist, customerForm);
		log.info("Customer Added: " + customer.getId() + " - " + customer.getName());		
		return customer;
	}

	private void copyAddress (Address srcAddress, Address destAddress)
	{
		destAddress.setCity(srcAddress.getCity());
		destAddress.setCountry(srcAddress.getCountry());
		destAddress.setProvince(srcAddress.getProvince());
		destAddress.setState(srcAddress.getState());
		destAddress.setStreetAddress(srcAddress.getStreetAddress());
		destAddress.setStreetAddress2(srcAddress.getStreetAddress2());
		destAddress.setStreetAddress3(srcAddress.getStreetAddress3());
		destAddress.setZipcode(srcAddress.getZipcode());
	}

    private void copyAddress (com.softech.vu360.lms.webservice.message.storefront.Address srcAddress, Address destAddress)
    {
        destAddress.setCity(srcAddress.getCity());
        destAddress.setCountry(srcAddress.getCountry());
        //destAddress.setProvince(srcAddress.getProvince());
        destAddress.setState(srcAddress.getState());
        destAddress.setStreetAddress(srcAddress.getAddressLine1());
        destAddress.setStreetAddress2(srcAddress.getAddressLine2());
        destAddress.setStreetAddress3(srcAddress.getAddressLine3());
        destAddress.setZipcode(srcAddress.getZipCode());
    }

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	private AddCustomerForm fillCustomerForm(com.softech.vu360.lms.webservice.message.storefront.Order order){
		com.softech.vu360.lms.webservice.message.storefront.Customer sfCustomer=order.getCustomer();
		com.softech.vu360.lms.webservice.message.storefront.Address sfAddress=sfCustomer.getPrimaryAddress();
		com.softech.vu360.lms.webservice.message.storefront.Contact sfContact=sfCustomer.getPrimaryContact();
		com.softech.vu360.lms.webservice.message.storefront.AuthenticationCredential sfAuthCredentials=sfContact.getAuthenticationCredential();

		AddCustomerForm customerForm=new AddCustomerForm();

			customerForm.setAudio(true);
			customerForm.setAudioLocked(false);
			customerForm.setBandwidth(true);
			customerForm.setBandwidthLocked(false);
			customerForm.setCaptioning(true);
			customerForm.setCaptioningLocked(false);
			customerForm.setChangePassword(false);
			customerForm.setCity1(sfAddress.getCity());
			customerForm.setCity2("");
			customerForm.setCountry1(sfAddress.getCountry());
			customerForm.setCountry2("");
			
			if(!StringUtils.isBlank(sfCustomer.getCompanyName()))
					customerForm.setCustomerName(sfCustomer.getCompanyName()) ;
			else
				customerForm.setCustomerName(sfContact.getFirstName() + " " + sfContact.getLastName());
			
			 if(isCustomerEntitledForManagerRole(order.getLineItem()))
			 {
				 customerForm.setCustomerType(Customer.B2B);
			 }
			 else
			 {
				 customerForm.setCustomerType(Customer.B2C);
			 }
			
			customerForm.setDisabled(false);
			
			customerForm.setEnrollmentEmails(false);
			customerForm.setEnrollmentEmailsLocked(false);
			customerForm.setExpirationDate(null);		
			customerForm.setExpired(false);
			customerForm.setExt("");
			customerForm.setLocked(false);
			customerForm.setRegistrationEmails(false);
			customerForm.setReport(false);
			customerForm.setStatus(true);
			customerForm.setVideo(true);
			customerForm.setVideoLocked(false);
		
			Calendar expDate=Calendar.getInstance();
			log.debug(expDate.getTime());
			expDate.add(Calendar.DAY_OF_YEAR, DEFAULT_TOS_IN_DAYS );
			log.debug("EXPIRY DATE: " + expDate.getTime().toString());
			
			//customerForm.setExpirationDate(expDate.toString());
			customerForm.setExpirationDate("");
		
		customerForm.setEmailAdd(sfContact.getEmailAddress());
		customerForm.setLoginEmailID(sfContact.getAuthenticationCredential().getUsername());
		customerForm.setPassword(sfContact.getAuthenticationCredential().getPassword());	
				
		customerForm.setAddress1(sfContact.getBillingAddress().getAddressLine1() );
		if(!sfContact.getBillingAddress().getAddressLine2().equals("null"))
			customerForm.setAddress1a(sfContact.getBillingAddress().getAddressLine2());
		else
			customerForm.setAddress1a("");
		
		customerForm.setAddress2(sfContact.getShippingAddress().getAddressLine1());
		if(!sfContact.getShippingAddress().getAddressLine2().equals("null"))
			customerForm.setAddress2a(sfContact.getShippingAddress().getAddressLine2());
		else
			customerForm.setAddress2a("");
		
		customerForm.setCity1(sfContact.getBillingAddress().getCity());
		customerForm.setCity2(sfContact.getShippingAddress().getCity());
		customerForm.setCountry2(sfContact.getShippingAddress().getCountry());
		
		customerForm.setFirstName(sfContact.getFirstName());
		customerForm.setLastName(sfContact.getLastName());
		
		customerForm.setPhone(sfContact.getPrimaryPhone());
		if(customerForm.getPhone().trim().equals("")){
			customerForm.setPhone(sfContact.getAlternatePhone().trim());
		}
		
		customerForm.setState1(sfContact.getBillingAddress().getState());
		customerForm.setState2(sfContact.getShippingAddress().getState());
		customerForm.setZip1(sfContact.getBillingAddress().getZipCode());
		customerForm.setZip2(sfContact.getShippingAddress().getZipCode());		
		
		return customerForm;
	}
	
	private void fillCustomer(Customer customer, com.softech.vu360.lms.webservice.message.storefront.Order order)
	{	
		com.softech.vu360.lms.webservice.message.storefront.Customer sfCustomer=order.getCustomer();
		com.softech.vu360.lms.webservice.message.storefront.Address sfAddress=sfCustomer.getPrimaryAddress();
		com.softech.vu360.lms.webservice.message.storefront.Contact sfContact=sfCustomer.getPrimaryContact();
		com.softech.vu360.lms.webservice.message.storefront.AuthenticationCredential sfAuthCredentials=sfContact.getAuthenticationCredential();
		List<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem> sfLineItemList=order.getLineItem();

		customer.setFirstName(sfContact.getFirstName());
		customer.setLastName(sfContact.getLastName());

		if(!StringUtils.isBlank(sfCustomer.getCompanyName()))
			customer.setName(sfCustomer.getCompanyName()) ;
		else
			customer.setName(sfContact.getFirstName() + " " + sfContact.getLastName());

		customer.setEmail(sfAuthCredentials.getUsername());
		customer.setOrderId(order.getOrderId());
		customer.setActive(true);
		 createShippingAddress(sfContact.getShippingAddress(), customer);
		 createBillingAddress(sfContact.getBillingAddress(), customer);

		 customer.setPhoneNumber(sfContact.getPrimaryPhone());
		 if(customer.getPhoneNumber()!=null && customer.getPhoneNumber().trim().equals("")){
			 customer.setPhoneNumber(sfContact.getAlternatePhone());
		 }

		 if(isCustomerEntitledForManagerRole(sfLineItemList))
		 {
			 customer.setCustomerType(Customer.B2B);
		 }
		 else
		 {
			 customer.setCustomerType(Customer.B2C);
		 }
	}

	@SuppressWarnings("deprecation")
	private void createBillingAddress(com.softech.vu360.lms.webservice.message.storefront.Address sfAddress, Customer customer)
	{
		Address address = customer.getBillingAddress();
		address.setCity(sfAddress.getCity());
		address.setCountry(sfAddress.getCountry());
		address.setState(sfAddress.getState());
		address.setStreetAddress(sfAddress.getAddressLine1());
		if(!sfAddress.getAddressLine2().equals("null"))
			address.setStreetAddress2(sfAddress.getAddressLine2());
		else
			address.setStreetAddress2("");
		address.setStreetAddress3(sfAddress.getAddressLine3());
		address.setZipcode(sfAddress.getZipCode());
	}
	
	@SuppressWarnings("deprecation")
	private void createShippingAddress(com.softech.vu360.lms.webservice.message.storefront.Address sfAddress, Customer customer)
	{
		Address address = customer.getShippingAddress();
		address.setCity(sfAddress.getCity());
		address.setCountry(sfAddress.getCountry());
		address.setState(sfAddress.getState());
		address.setStreetAddress(sfAddress.getAddressLine1());
		if(!sfAddress.getAddressLine2().equals("null"))
			address.setStreetAddress2(sfAddress.getAddressLine2());
		else
			address.setStreetAddress2("");
		address.setStreetAddress3(sfAddress.getAddressLine3());
		address.setZipcode(sfAddress.getZipCode());
	}

	/*
	 * if order has got any order line item with the quantity greater than 1
	 * then (s)he would be entitled as Manager
	 * @author Faisal A. Siddiqui
	 * @param sfLineItemList	collection of orderline item
	 * @return true if customer is entitled for manager role and false if customer is not entitled for manager role. 
	 */
	private boolean isCustomerEntitledForManagerRole(List<com.softech.vu360.lms.webservice.message.storefront.OrderLineItem> sfLineItemList)
	{
		boolean isCustomerAManager=false;
		for(com.softech.vu360.lms.webservice.message.storefront.OrderLineItem lineItem:sfLineItemList)
		{
			BigInteger qty=lineItem.getQuantity();
			// if customer took any course with more than 1 qty then business says consider it Manager
			if(qty.longValue()>1)
			{
				isCustomerAManager=true;
				break;
			}
		}
		return isCustomerAManager;
	}	
	
	
	private static int getValidContractExpirationDate(Date maxEndDate){
		Date currentDate = new Date();
		Date customerDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, DEFAULT_TOS_IN_DAYS);
		customerDate = cal.getTime();
		
		if(customerDate.before(maxEndDate)|| DateUtil.getDatesDiffInDays(currentDate, maxEndDate) == 0){
			return DEFAULT_TOS_IN_DAYS;
		}else{
			return (int) DateUtil.getDatesDiffInDays(currentDate, maxEndDate);
		}
    }

	public void orderAuditLog(LmsSfOrderResultBuilder lmsSfOrderResultBuilder) throws Exception {
		
		orderService.orderAuditLog(lmsSfOrderResultBuilder);
	}
	
	public boolean implementRefund(RefundRequest refund) throws Exception{
		return orderService.implementRefund(refund);
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}
	
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public boolean isCorporateAuthorVar() {
		return isCorporateAuthorVar;
	}

	public void setCorporateAuthorVar(boolean isCorporateAuthorVar) {
		this.isCorporateAuthorVar = isCorporateAuthorVar;
	}
}