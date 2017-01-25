package com.softech.vu360.lms.service.impl;

import java.io.InputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.CCILead;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.CustomerOrder;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LineItem;
import com.softech.vu360.lms.model.LmsSfOrderAuditLog;
import com.softech.vu360.lms.model.OrderRequestSOAPEnvelop;
import com.softech.vu360.lms.model.SelfServiceProductDetails;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.CCILeadRepository;
import com.softech.vu360.lms.repositories.CustomerOrderRepository;
import com.softech.vu360.lms.repositories.LineItemRepository;
import com.softech.vu360.lms.repositories.LmsSfOrderAuditLogRepository;
import com.softech.vu360.lms.repositories.OrderRequestSOAPEnvelopRepository;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrderService;
import com.softech.vu360.lms.service.SelfServiceService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.ObjectToSOAPGenerator;
import com.softech.vu360.lms.vo.LmsSfOrderResultBuilder;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedResponse;
import com.softech.vu360.lms.webservice.message.selfservice.Product;
import com.softech.vu360.lms.webservice.message.storefront.Order;
import com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest;
import com.softech.vu360.lms.webservice.message.storefront.OrderLineItem;
import com.softech.vu360.lms.webservice.message.storefront.RefundRequest;
import com.softech.vu360.util.DateUtil;

public class OrderServiceImpl implements OrderService {
	
	private static final Logger log = Logger.getLogger(EnrollmentServiceImpl.class.getName());
	@Inject
	private CustomerOrderRepository customerOrderRepository;
	@Inject
	private LineItemRepository lineItemRepository;
	@Inject
	private OrderRequestSOAPEnvelopRepository orderRequestSOAPEnvelopRepository;
	@Inject
	private LmsSfOrderAuditLogRepository lmsSfOrderAuditLogRepository;
	@Inject
	private CCILeadRepository cCILeadRepository;
	private EntitlementService entitlementService=null;
	private CustomerService customerService=null;
	private EnrollmentService enrollmentService=null;
	private VU360UserService vu360UserService;
	private CourseAndCourseGroupService courseAndCourseGroupService=null;
	private static final int COURSE_GUID_SIZE=32;
	private SelfServiceService selfServiceService;
    public com.softech.vu360.lms.products.Product objPro;
   
    
    public enum ProductType {
    	CORE,    	EXTENSION,    	GAME,    	ACTIVITY
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

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	private LearnerService learnerService=null;

	@Override
	public CustomerOrder getOrderByGUID(String guid) {
		return customerOrderRepository.findByOrderGUID(guid);
	}

	@Override
	@Transactional
	public CustomerOrder saveOrder(CustomerOrder orderInfo) {
		return customerOrderRepository.save(orderInfo);
	}
	
	@Override
	@Transactional
	public CCILead saveCCILead(CCILead cciLead) {
		return cCILeadRepository.save(cciLead);
	}

	@Override
	@Transactional
	public boolean implementRefund(RefundRequest refund) throws Exception {
		Order order=refund.getOrder();
		List<LineItem> lineItemList;
		CustomerEntitlement customerEntitlement=null;
		boolean result=true;
		List<LearnerEnrollment> learnerEnrollmenList=null;
		log.debug("INSIDE RefundRequest! for customer GUID: "+ order.getCustomer().getCustomerId() + " AND OrderGUID: " + order.getOrderId());
		CustomerOrder orderInfo=customerOrderRepository.findByOrderGUID(order.getOrderId());
		Customer customer=orderInfo.getCustomer();
		if(customer==null)
			throw new Exception("Customer Not Found against GUID: " + order.getCustomer().getCustomerId());
		if(orderInfo!=null){
			lineItemList=orderInfo.getLineItem();
			for(OrderLineItem orderLineItem:order.getLineItem()){
				String strCourseGUID="";
				//This is a patch to overcome GUID issue since SF stores CourseGUID+CourseGroupGUID
				//Hence we need to parse out the 1st 32 digits which are of courseGUID
				if(orderLineItem.getGuid().length()>=COURSE_GUID_SIZE){
					strCourseGUID=orderLineItem.getGuid().substring(0, COURSE_GUID_SIZE);
				}
				else{
					strCourseGUID=orderLineItem.getGuid();
				}
				LineItem lineItem=lineItemRepository.findByOrderInfoIdAndItemGUID(orderInfo.getId(), strCourseGUID);
				if(lineItem==null){
					throw new Exception("Line Item not Found against order guid: " + orderInfo.getOrderGUID() + " AND courseGUID: " + strCourseGUID );
				}
				customerEntitlement=lineItem.getCustomerEntitlement();
				if(customerEntitlement==null){
					throw new Exception("Customer Entitlement not present! ");
				}
				CustomerEntitlement customerEntitlementUpdate = entitlementService.loadForUpdateCustomerEntitlement(customerEntitlement.getId());
				customerEntitlementUpdate.setEndDate(new Date());
				customerEntitlementUpdate.setAllowUnlimitedEnrollments(false);
				customerEntitlementUpdate.setMaxNumberSeats(0);
				
				entitlementService.saveCustomerEntitlement(customerEntitlementUpdate, null);
				
				//Check if its a B2C Customer then drop his/her enrollments as well
				if(customer.getCustomerType().equals(Customer.B2C)){
					learnerEnrollmenList=entitlementService.getLearnerEnrollmentsAgainstCustomerEntitlement(customerEntitlement);
					LearnerEnrollment enrollmentToBeDropped = null;
					for(LearnerEnrollment lE:learnerEnrollmenList){
						enrollmentToBeDropped = enrollmentService.loadForUpdateLearnerEnrollment(lE.getId());
						enrollmentToBeDropped.setEnrollmentEndDate(new Date());
						enrollmentToBeDropped.setEnrollmentStatus(LearnerEnrollment.DROPPED);
						enrollmentService.updateEnrollment(enrollmentToBeDropped);
					}
					enrollmentService.addEnrollments(learnerEnrollmenList);
				}
				//update lineItem
				lineItem.setRefundQty(lineItem.getRefundQty()+orderLineItem.getQuantity().intValue());
				lineItemRepository.save(lineItem);
			}
		}
		return result;
	}
	
	public CustomerOrder getOrderById(Long id){
		return customerOrderRepository.findOne(id);
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	
	
	private boolean isRefundQtyValid(Long orderId, List<OrderLineItem> orderLineItemList){
		boolean result=true;
		for(OrderLineItem orderLineItem : orderLineItemList){
			LineItem lineItem=lineItemRepository.findByOrderInfoIdAndItemGUID(orderId, orderLineItem.getGuid());
			result = ((lineItem.getQuantity()-lineItem.getRefundQty())>=orderLineItem.getQuantity().intValue())?true:false;
			if(!result){
				break;
			}
		}
		return result;
	}

    public  List<CustomerOrder>  getOrderByCustomerId(Long customerId){
        return customerOrderRepository.findByCustomerId(customerId);
    }


    // LMS-13133
    // To generate SOAP Envelop from Object Graph
    @Override
    @Transactional
    public OrderRequestSOAPEnvelop addOrderRequestSOAPEnvelop(OrderCreatedRequest orderCreatedRequest)throws Exception{
    	String soapEnvelop = ObjectToSOAPGenerator.<OrderCreatedRequest>generateSOAPEnvelop(orderCreatedRequest);
		OrderRequestSOAPEnvelop orderRequestSOAPEnvelop = new OrderRequestSOAPEnvelop();
		orderRequestSOAPEnvelop.setCustomerName(orderCreatedRequest.getOrder().getCustomer().getCustomerName());
		orderRequestSOAPEnvelop.setOrderId(orderCreatedRequest.getOrder().getOrderId());
		orderRequestSOAPEnvelop.setTransactionGUID(orderCreatedRequest.getTransactionGUID());
		orderRequestSOAPEnvelop.setSoapEnvelop(soapEnvelop);
		orderRequestSOAPEnvelop.setEventDate(orderCreatedRequest.getEventDate().toGregorianCalendar().getTime());
    	return orderRequestSOAPEnvelopRepository.save(orderRequestSOAPEnvelop);
    }

    @Override
	public boolean saveAndEnrollCourseOnly(List<Product> lstProduct, com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest orderCreatedRequest, OrderCreatedResponse orderCreatedResponse) throws Exception {
    	VU360User vu360User=null;
		String userName = (String) orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getAuthenticationCredential().getUsername();
		if(StringUtils.isNotEmpty(userName))
			vu360User = vu360UserService.findUserByUserName(userName);
    	// if product is null and lineItem present then create contract of each line item.
    	if(vu360User != null ){
    		Customer customer = customerService.loadForUpdateCustomer( vu360User.getLearner().getCustomer().getId());
			List<LineItem> lineItemList = new ArrayList<LineItem>();
			CustomerOrder customerOrder = new CustomerOrder();
			LmsSfOrderResultBuilder lmsSfOrderResultBuilder = new LmsSfOrderResultBuilder();
			Learner learner = null;
			createEntitlementOfEachLineItem(customer, learner, customerOrder, lineItemList, orderCreatedRequest.getOrder(), lmsSfOrderResultBuilder);
			saveCustomerOrder(customerOrder, lineItemList, customer, orderCreatedRequest.getOrder(), orderCreatedRequest);
			orderCreatedResponse.setCustomerGUID(customer.getCustomerGUID());
			orderCreatedResponse.setTransactionResultMessage("Course entitlement created successfully.");
			orderCreatedResponse.setTransactionGUID(orderCreatedRequest.getTransactionGUID());
			orderCreatedResponse.setTransactionResult("SUCCESS");
    	}
    	return true;
    }
    
    
    @Override
    @Transactional
	public boolean saveProducts(List<Product> lstProduct, com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest orderCreatedRequest, OrderCreatedResponse orderCreatedResponse) throws Exception {
		log.info(":::::::::::OrderServiceImp.java::::::saveProducts()::::::Start");
    	Properties properties = new Properties();
    	InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("productHandlerConfig.properties");  
		properties.load(inputStream);
		String proExecuteHandlerFirst = properties.getProperty("ececuteHandlerFirst");
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date()); 
		calendar.add(Calendar.YEAR, 1);
		Date dtOneYearPlus = calendar.getTime();
    	objPro = new com.softech.vu360.lms.products.Product();
    	Hashtable<String, Object> dataBundle = new Hashtable<String, Object>();
    	dataBundle.put("orderCreatedRequest", orderCreatedRequest);
    	dataBundle.put("orderCreatedResponse", orderCreatedResponse);
    	dataBundle.put("selfServiceService", selfServiceService);
    	
    	log.info("......OrderServiceImp.java.......start loop for product handler having code '002'...............");
    	//save Products order as define in  ProductType Enum
    	//We have one Product that creates VU360user, Customer, entitlement. So, We call his Handler first.
		for(Product objProduct :lstProduct){
			   if(objProduct.getCode().equalsIgnoreCase(proExecuteHandlerFirst)){
	     		    // Calling Handler base on product code
				    // exec method of handler call functions from lms.jar for DB operations 
	     		    boolean handledProduct = objPro.callProductHandler(objProduct.getCode(), dataBundle);
	     		  
	     		    // populating object for inserting record on SELFSERVICEPRODUCTDETAILS table
		     		SelfServiceProductDetails selfserviceproductdetails = new SelfServiceProductDetails();
		     		selfserviceproductdetails.setProductCode(objProduct.getCode());
		     		selfserviceproductdetails.setProductType(objProduct.getType());
		     		selfserviceproductdetails.setProductCategory(objProduct.getCategory());
		     		selfserviceproductdetails.setPurchaseDate(new Date());
		     		selfserviceproductdetails.setEffectiveFrom(new Date());		
		     		selfserviceproductdetails.setEffectiveTo(dtOneYearPlus);				
		     		// inserting record in SELFSERVICEPRODUCTDETAILS table
		     		selfServiceService.saveSelfServiceProductDetails(selfserviceproductdetails);
	     	   }
	    }
		log.info("......OrderServiceImp.java.......start loop for calling product handler...............");
    	// loop execution order for Products [CORE, EXTENSION, GAME, ACTIVITY]
    	for(ProductType pt : ProductType.values()){
    		for(Product objProduct :lstProduct){
	     	   if(objProduct.getType().equalsIgnoreCase(pt.toString()) && !(objProduct.getCode().equalsIgnoreCase(proExecuteHandlerFirst))){
	     		  dataBundle.put("PRODUCTCODE", objProduct.getCode());
	     		    // call Handler base on product code
	     		    boolean handledProduct = objPro.callProductHandler(objProduct.getCode(), dataBundle);
		     		SelfServiceProductDetails selfserviceproductdetails = new SelfServiceProductDetails();
		     		selfserviceproductdetails.setProductCode(objProduct.getCode());
		     		selfserviceproductdetails.setProductType(objProduct.getType());
		     		selfserviceproductdetails.setProductCategory(objProduct.getCategory());
		     		selfserviceproductdetails.setPurchaseDate(new Date());
		     		selfserviceproductdetails.setEffectiveFrom(new Date());		
		     		selfserviceproductdetails.setEffectiveTo(dtOneYearPlus);				
		     		// inserting recods in SELFSERVICEPRODUCTDETAILS table
		     		selfServiceService.saveSelfServiceProductDetails(selfserviceproductdetails);
	     	   }
	        }
    	}
    	log.info("......OrderServiceImp.java......saveProducts()....End...");
		return true;
	}
    
    @Override
    @Transactional
    public boolean submitOrder(com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest orderCreatedRequest, OrderCreatedResponse orderCreatedResponse) throws Exception {
    	log.info(" OrderServiceImp.java - submitOrder() - Start - ");
    	OrderRequestSOAPEnvelop soapOrderRequest = new OrderRequestSOAPEnvelop();
        soapOrderRequest.setCustomerName(orderCreatedRequest.getOrder().getCustomer().getCustomerName());
        soapOrderRequest.setEventDate(orderCreatedRequest.getEventDate().toGregorianCalendar().getTime());
        soapOrderRequest.setOrderId(orderCreatedRequest.getOrder().getOrderId());
        soapOrderRequest.setTransactionGUID(orderCreatedRequest.getTransactionGUID());
        String soapEnvelop = ObjectToSOAPGenerator.generateSOAPEnvelop(orderCreatedRequest);
        soapOrderRequest.setSoapEnvelop(soapEnvelop);
        orderRequestSOAPEnvelopRepository.save(soapOrderRequest);
        // this chcek identify the existing user buying only 360 Courses not Product
    	saveAndEnrollCourseOnly(orderCreatedRequest.getProducts(), orderCreatedRequest, orderCreatedResponse);
    	saveProducts(orderCreatedRequest.getProducts(), orderCreatedRequest, orderCreatedResponse);
        log.info(" OrderServiceImp.java - submitOrder() - End - ");
        return true;
    }

	/**
	 * 
	 * @param customerOrder
	 * @param lineItemList
	 * @param customer
	 * @param order
	 * @param orderCreatedRequest
	 */
	private void saveCustomerOrder (CustomerOrder customerOrder, List<LineItem> lineItemList, Customer customer, 
			com.softech.vu360.lms.webservice.message.selfservice.Order order, com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest orderCreatedRequest)
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
			// CCILead Id
			String cciLeadId = orderCreatedRequest.getCCILeadId();
			try
			{
				// Only saving CCILeadId if present.
				if((cciLeadId!=null) && (!cciLeadId.equals("")))
				{
					CCILead cciLead = new CCILead();
					cciLead.setCciLeadId(cciLeadId);
					cciLead.setCustomerOrder(customerOrder);
					saveCCILead(cciLead);
					log.debug("CCI Lead Id " + cciLead.getCciLeadId());	
				}
			}
			catch(Exception e)
			{
				log.debug("Exception saving CCILead: " + e, e);
			}		
			saveOrder(customerOrder);
			log.info("OrderInfo Saved");
		}
		catch(Exception ex){
			log.error("ERROR in Saving OrderInfo: for orderId: " + order.getOrderId() + " --- " + ex.getMessage(), ex);
		}
	}
    
	@Transactional
    public void orderAuditLog(LmsSfOrderResultBuilder lmsSfOrderResultBuilder) throws Exception {
		if(lmsSfOrderResultBuilder != null & lmsSfOrderResultBuilder.getOrderAuditList() != null && lmsSfOrderResultBuilder.getOrderAuditList().size() > 0)
		{
			lmsSfOrderAuditLogRepository.save(lmsSfOrderResultBuilder.getOrderAuditList());
		}
	}
    
    // LMS-15514 - Updating DistributorCode in Distributor table. This function call when SF create Store on their end. Store Id is same as DistributorCode in LMS.
    @Override
    @Transactional
    public boolean storeCreation(com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest orderCreatedRequest) throws Exception {
    	log.info(" OrderServiceImp.java - storeCreation() - Start - ");
    	OrderRequestSOAPEnvelop soapOrderRequest = new OrderRequestSOAPEnvelop();
        soapOrderRequest.setCustomerName(orderCreatedRequest.getOrder().getCustomer().getCustomerName());
        soapOrderRequest.setEventDate(orderCreatedRequest.getEventDate().toGregorianCalendar().getTime());
        soapOrderRequest.setOrderId(orderCreatedRequest.getOrder().getOrderId());
        soapOrderRequest.setTransactionGUID(orderCreatedRequest.getTransactionGUID());
        String soapEnvelop = ObjectToSOAPGenerator.generateSOAPEnvelop(orderCreatedRequest);
        soapOrderRequest.setSoapEnvelop(soapEnvelop);
        orderRequestSOAPEnvelopRepository.save(soapOrderRequest);
        log.info("Soap Envelop---- : " + soapEnvelop);
        log.info(" OrderServiceImp.java - storeCreation() - End - ");
        log.info("------Start---------Set Distributor Code in distributor table against user coming in [orderCreatedRequest.getOrder().getCustomer().getCustomerId ] ----");
        if(orderCreatedRequest.getOrder()!=null && orderCreatedRequest.getOrder().getCustomer()!=null 
        			&& orderCreatedRequest.getOrder().getCustomer().getCustomerId()!=null 
        			&& orderCreatedRequest.getOrder().getDistributorId()!=null){
	        
        	Hashtable<String, Object> colStoreCreation = new Hashtable<String, Object>();
	        colStoreCreation.put("storeCreationUserId", orderCreatedRequest.getOrder().getCustomer().getCustomerId()) ;
	        colStoreCreation.put("storeCreationdistributorId", orderCreatedRequest.getOrder().getDistributorId()) ;
	    	// Updating DistributorCode in Distributor table. 
	        // This function call when SF create Store on their end. Store Id is same as DistributorCode in LMS.
	        selfServiceService.storeCreation(colStoreCreation);
	        colStoreCreation = null;
        }else{
        	log.info("OrderServiceImp.java - storeCreation() - Error - " + new Date() + "SF send null data in orderCreatedRequest.getOrder().getDistributorId() or in orderCreatedRequest.getOrder().getCustomer().getCustomerId()");
        }
		log.info("OrderServiceImp.java - storeCreation() - End -");
        return true;
    }

	/**
	 * 
	 * @param customer
	 * @param learner
	 * @param customerOrder
	 * @param lineItemList
	 * @param order
	 * @param lmsSfOrderResultBuilder
	 * @throws ParseException
	 */
	@SuppressWarnings("unused")
	private void createEntitlementOfEachLineItem (Customer customer, Learner learner, CustomerOrder customerOrder, List<LineItem> lineItemList
			, com.softech.vu360.lms.webservice.message.selfservice.Order order, LmsSfOrderResultBuilder lmsSfOrderResultBuilder) throws ParseException
	{
		String unProcessableLineItem  = "-1";
		for(com.softech.vu360.lms.webservice.message.selfservice.OrderLineItem orderLineItem : order.getLineItem()){
			CourseCustomerEntitlement courseCustomerEntitlement = new CourseCustomerEntitlement();
			BigInteger qty = orderLineItem.getQuantity();
			String strCourseGUID = orderLineItem.getGuid();
			String strCourseGroupGUID = orderLineItem.getGroupguid();
			if(StringUtils.isEmpty(strCourseGroupGUID) || unProcessableLineItem.equals(strCourseGroupGUID) 
					|| StringUtils.isEmpty(strCourseGUID) || unProcessableLineItem.equals(strCourseGUID) ) 
				continue;
			
			Course course = courseAndCourseGroupService.getCourseByGUID(strCourseGUID);
			CourseGroup courseGroup = courseAndCourseGroupService.getCourseGroupByguid(orderLineItem.getGroupguid());
			String courseGroupGuid = courseGroup.getGuid();
			if(course == null){
				log.error("Course Not Found against GUID: "+strCourseGUID);
				//Adding audit line item here because of the the current code. Should have thrown exception and log audit from the calling code
				lmsSfOrderResultBuilder.getOrderAuditList().add(new LmsSfOrderAuditLog(order.getOrderId(),strCourseGUID,courseGroupGuid,"Invalid","Invalid course guid","OrderEvent",false));
				continue;
			}
			//Adding audit line item here because of the the current code. Should have thrown exception and log audit from the calling code
			//lmsSfOrderResultBuilder.getOrderAuditList().add(new LmsSfOrderAuditLog(order.getOrderId(),strCourseGUID,courseGroupGuid,"Valid","Valid course guid","OrderEvent",true));
			lmsSfOrderResultBuilder.setHasOneValidCourseGuid(true);
			log.error("Creating Entitlement");
			courseCustomerEntitlement.setCustomer(customer);
			courseCustomerEntitlement.setName(customer.getName());
			courseCustomerEntitlement.setMaxNumberSeats(qty.intValue());
			/* SF-902 Order may have TOS null or 0 and in that we should use DEFAULT_TOS_IN_DAYS */ 
			if(orderLineItem.getTermOfService()==null || orderLineItem.getTermOfService().intValue()<1){
				/* more detail are in LMS-13438 */
				Date maxEndDate= entitlementService.getMaxDistributorEntitlementEndDate(customer.getDistributor());
				courseCustomerEntitlement.setDefaultTermOfServiceInDays( getValidContractExpirationDate(maxEndDate) );
			}	
			else{
				courseCustomerEntitlement.setDefaultTermOfServiceInDays(orderLineItem.getTermOfService().intValue());
			}
			courseCustomerEntitlement.setAllowUnlimitedEnrollments(false);
			courseCustomerEntitlement.setAllowSelfEnrollment(false);
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date dt = Calendar.getInstance().getTime();
			courseCustomerEntitlement.setStartDate(formatter.parse(formatter.format(dt)));
			courseCustomerEntitlement.setContractCreationDate(new Date());
			CourseCustomerEntitlement courseCustEntitlement_ = (CourseCustomerEntitlement) entitlementService.addCustomerEntitlement(customer, courseCustomerEntitlement);
			entitlementService.addEntitlementItem(courseCustEntitlement_, course, courseGroup);
			log.error("Saved Customer Entitlement");
			// if customer is a b2c customer add enrollments
			if ( customer.getCustomerType().equalsIgnoreCase(Customer.B2C) ) 
			{
				if(learner == null)
					learner = learnerService.getLearnerForB2CCustomer(customer);
				enrollmentService.addEnrollmentsForCourseEntitlements(learner, courseCustEntitlement_, orderLineItem.getLineitemguid() == null ? "" : orderLineItem.getLineitemguid());
			}

			LineItem lineItem = new LineItem();
			lineItem.setItemGUID(orderLineItem.getGuid());
			lineItem.setQuantity(orderLineItem.getQuantity().intValue());
			lineItem.setCustomerEntitlement(courseCustomerEntitlement);
			lineItem.setOrderInfo(customerOrder);
			 
			lineItemList.add(lineItem);
		}
	}

	private static int getValidContractExpirationDate(Date maxEndDate){
		Date currentDate = new Date();
		Date customerDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, SelfServiceServiceImpl.DEFAULT_TOS_IN_DAYS);
		customerDate = cal.getTime();
		
		if(customerDate.before(maxEndDate)|| DateUtil.getDatesDiffInDays(currentDate, maxEndDate) == 0){
			return SelfServiceServiceImpl.DEFAULT_TOS_IN_DAYS;
		}else{
			return (int) DateUtil.getDatesDiffInDays(currentDate, maxEndDate);
		}
    }

	public SelfServiceService getSelfServiceService() {
		return selfServiceService;
	}

	public void setSelfServiceService(SelfServiceService selfServiceService) {
		this.selfServiceService = selfServiceService;
	}


	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	@Override
	@Transactional
	public boolean updateOrderRequestSOAPEnvelop(OrderRequestSOAPEnvelop orderRequestSOAPEnvelop){
		boolean isUpdated = false;
		try {
			orderRequestSOAPEnvelopRepository.save(orderRequestSOAPEnvelop);
			isUpdated = true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    	return isUpdated;
	}

}