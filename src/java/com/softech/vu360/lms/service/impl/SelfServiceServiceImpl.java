package com.softech.vu360.lms.service.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.CCILead;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerOrder;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSProductPurchase;
import com.softech.vu360.lms.model.LMSProducts;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LineItem;
import com.softech.vu360.lms.model.LmsSfOrderAuditLog;
import com.softech.vu360.lms.model.SelfServiceProductDetails;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.SelfServiceProductDetailsRepository;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LMSProductPurchaseService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrderService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.SelfServiceService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.impl.lmsapi.LearnerServiceImplLmsApi;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.vo.LmsSfOrderResultBuilder;
import com.softech.vu360.lms.web.controller.model.AddDistributorForm;
import com.softech.vu360.lms.web.controller.model.UserItemForm;
import com.softech.vu360.lms.webservice.message.selfservice.Order;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedResponse;
import com.softech.vu360.util.DateUtil;

/**
 * @author rehan.rana
 * 
 */
public class SelfServiceServiceImpl implements SelfServiceService{
	
	private static final Logger log = Logger.getLogger(SelfServiceServiceImpl.class.getName());
	private CustomerService customerService = null;
	private DistributorService distributorService = null;
	private VU360UserService vu360UserService = null;
	public static final int DEFAULT_TOS_IN_DAYS = 180;
	public static final int DEFAULT_BRAND_ID=1;
	private AuthorService authorService;
	private LearnerService learnerService;
	private LMSProductPurchaseService productPurchaseService;
	private CourseAndCourseGroupService courseAndCourseGroupService;
	private EntitlementService entitlementService;
	private EnrollmentService enrollmentService; 
	private OrderService orderService;
	private SecurityAndRolesService securityAndRolesService=null;
	private LearnerServiceImplLmsApi learnerServiceImplLmsApi;
	
	@Inject
	private SelfServiceProductDetailsRepository selfServiceRepository;
	
	@Override
	public SelfServiceProductDetails saveSelfServiceProductDetails(
			SelfServiceProductDetails productDetail) {
		return selfServiceRepository.save (productDetail);
	}
	
	@Override
	public boolean createFreemiumAccount(Hashtable<String, Object> accountdetail){
		int i= selfServiceRepository.createFreemiumAccount(
				accountdetail.get("ContentOwnerName"),
				accountdetail.get("UserName"),
				accountdetail.get("EmailAddress"),
				accountdetail.get("FirstName"),
				accountdetail.get("LastName"),
				accountdetail.get("Password"),
				accountdetail.get("EffectiveFrom"),
				accountdetail.get("EffectiveTo"),
				accountdetail.get("MaxAuthorCount"),
				accountdetail.get("MaxCourseCount"));
		if(i==1){return true;}else return false;
		
	}
	
	@Override
	public boolean processSelfServiceOrder(Hashtable<String, Object> accountdetail){

		Customer customer = null;
		VU360User vu360User=null;
		ContentOwner contentowner=null;
		
		log.info("***** SelfService Order processing start...... ");
		log.info("***** SelfServiceServiceImpl.processSelfServiceOrder(Hashtable<String, Object> accountdetail){ ");

		OrderCreatedRequest requestOrder = (OrderCreatedRequest) accountdetail.get("orderCreatedRequest");
		OrderCreatedResponse responseOrder = (OrderCreatedResponse) accountdetail.get("orderCreatedResponse");
		String userName = (String) accountdetail.get("UserName");

		try{
			
			if(StringUtils.isNotEmpty(userName))
				vu360User = vu360UserService.findUserByUserName(userName);
			
			if(vu360User == null)
			{

				/**
				 * Check lineItem: if the line items equal to 1 and only, then coming user will be created as LEARNER and will be fall on Default Customer
				 * otherwise the Customer.
				 * 	
				 */
				//if(requestOrder.getOrder() !=null && requestOrder.getOrder().getLineItem().size() > 1 ){
					
					//creating fresh and empty VU360User object and passing it to addDistributor() function because this function is in used 
					//in different areas in project thats why we cannot change it.
					
					log.info(" ************** More then 1 Line item Received in SelfService Order *********************** ");
					log.info(" ************** Creating vu360user/distributor/customer agists the User Name : " + userName);
					String sortBy = "firstName";
					VU360User defaultUserForSelfservice = new VU360User();
					Distributor distributor = addDistributor(accountdetail, defaultUserForSelfservice.getId());
					List<VU360User> lstUsr = distributorService.getLearnersByDistributor("", "", "", "", distributor.getId(),-1,-1,new ResultSet(), sortBy, "0",0);
					
					if(lstUsr.size()>0){ // No Null Checking
						
						Learner objLeaner = learnerService.getLearnerByVU360UserId(lstUsr.get(0));
						VU360User objUser = lstUsr.get(0);
						/* 
						 *  LMS-20103
						 *  Although VU360User should have learner info by default but I found here learner is null in it and causing 
						 *  NullPointerException in createAuthorForUsers(). so that I manually pick learner and set it into vu360user
						 */
						 
						objUser.setLearner(objLeaner);
						contentowner = authorService.addContentOwnerForSelfServiceUser(distributor.getMyCustomer(), objUser);
						
						customer = customerService.loadForUpdateCustomer(distributor.getMyCustomer().getId());
						customer.setContentOwner(contentowner);
						customerService.updateCustomer(customer);
						
						UserItemForm usrItemform = new UserItemForm();
						usrItemform.setUser(objUser);
						usrItemform.setSelected(true);
						List lstusr = new ArrayList();
						lstusr.add(usrItemform);
						Map<String, List<VU360User>> result = authorService.createAuthorForUsers(lstusr, objUser);
	
						List<LineItem> lineItemList = new ArrayList<LineItem>();
						CustomerOrder customerOrder = new CustomerOrder();
						LmsSfOrderResultBuilder lmsSfOrderResultBuilder = new LmsSfOrderResultBuilder();
						Learner learner = null;
					
						createEntitlementOfEachLineItem(customer, learner, customerOrder, lineItemList, requestOrder.getOrder(), lmsSfOrderResultBuilder);
						saveCustomerOrder(customerOrder, lineItemList, customer, requestOrder.getOrder(), requestOrder);
						responseOrder.setCustomerGUID(customer.getCustomerGUID());
						responseOrder.setTransactionResult("SUCCESS");
						//responseOrder.setResponseMessage("New Customer Created");
						
					}else{
						log.info("----- TopLinkSelfServiceDAO.processSelfServiceOrder() --- Error!....VU360User not found!");
					}
					lstUsr=null; contentowner=null;	customer=null;
				
				/*
				}
				
				
				else if(requestOrder.getOrder() !=null && requestOrder.getOrder().getLineItem().size() == 1 ){
					
					
					log.info(" ************** ONLY 1 Line item Received in SelfService Order *********************** ");
					log.info(" ************** Creating Learner Only in Default Customer, against User Name : " + userName);

					// Create new learner and in  default customer
					Customer  defaultCustomer = customerService.getCustomerById(54805L); // Assume it is Default Customer.
					List<LineItem> lineItemList = new ArrayList<LineItem>();
					CustomerOrder customerOrder = new CustomerOrder();
					LmsSfOrderResultBuilder lmsSfOrderResultBuilder = new LmsSfOrderResultBuilder();
					Learner learner = null;

					//create entitle of each LineItem.
					createEntitlementOfEachLineItem(defaultCustomer, learner, customerOrder, lineItemList, requestOrder.getOrder(), lmsSfOrderResultBuilder);
					
					//Create Learner in default Customer.
					//Learner newlyCreatedLearner = learnerService.addNewLearnerGivenCustomer(defaultCustomer, accountdetail);
					
					if(lineItemList.size() > 0){
						
						//Create Learner in default Customer.
						Learner newlyCreatedLearner = learnerService.addNewLearnerGivenCustomer(defaultCustomer, accountdetail);
						
						LineItem lineItem = lineItemList.get(0);
						enrollmentService.addEnrollmentsForCourseEntitlements(newlyCreatedLearner, (CourseCustomerEntitlement) lineItem.getCustomerEntitlement(), lineItem.getItemGUID());
						log.info("new Learne created in default Customer having user name :"+newlyCreatedLearner.getVu360User().getUsername()+"/"+newlyCreatedLearner.getVu360User().getPassword());
						log.info("----- Single LineItem received. end");
					}
					
					responseOrder.setCustomerGUID(defaultCustomer.getCustomerGUID());
					responseOrder.setTransactionResult("SUCCESS");
					//responseOrder.setResponseMessage("New Customer Created");

				}
				*/
			}
			
			if(vu360User !=null ){	

				log.info(" ************** Author Found this product has already purchased.. *********************** ");
				log.info(" ************** Author Found this product has already purchased.. *********************** ");
				log.info(" ************** Author Found this product has already purchased.. *********************** ");
				log.info(" ************** Author Found this product has already purchased.. *********************** ");
				
				/*
				log.info(" ************** Author Found and No Line items Received in SelfService Order *********************** ");
				log.info(" ************** Creating Customer Contract against User Name : " + userName);

				//customer = customerService.loadForUpdateCustomer(Long.parseLong(CustomerID) );
				customer = customerService.loadForUpdateCustomer( vu360User.getLearner().getCustomer().getId());

				List<LineItem> lineItemList = new ArrayList<LineItem>();
				CustomerOrder customerOrder = new CustomerOrder();
				LmsSfOrderResultBuilder lmsSfOrderResultBuilder = new LmsSfOrderResultBuilder();
				Learner learner = null;
			
				createEntitlementOfEachLineItem(customer, learner, customerOrder, lineItemList, requestOrder.getOrder(), lmsSfOrderResultBuilder);
				saveCustomerOrder(customerOrder, lineItemList, customer, requestOrder.getOrder(), requestOrder);
				
				responseOrder.setCustomerGUID(customer.getCustomerGUID());
				responseOrder.setTransactionResult("SUCCESS");
				//responseOrder.setResponseMessage("New Customer Created");
				*/
			}
			
		}catch(Exception exception){
			
			log.error("Error Occured in TopLinkSelfServiceDAO.processSelfServiceOrder:  --- " + exception.getMessage(), exception);
			responseOrder.setCustomerGUID("-1");
			responseOrder.setTransactionResult("FAIL");
			responseOrder.setTransactionResultMessage(exception.toString());
			
		}
		finally{
			
			log.info(" --------------- TopLinkSelfServiceDAO.processSelfServiceOrder() - END ----------------- ");
		}

		log.info("***** SelfService Order processing end...... ");

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
	
	/**
	 * 
	 * @param customerOrder
	 * @param lineItemList
	 * @param customer
	 * @param order
	 * @param orderCreatedRequest
	 */
	private void saveCustomerOrder (CustomerOrder customerOrder, List<LineItem> lineItemList, Customer customer, Order order, OrderCreatedRequest orderCreatedRequest)
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
			
			orderService.saveOrder(customerOrder);
			log.info("OrderInfo Saved");
			
		}
		catch(Exception ex){
			log.error("ERROR in Saving OrderInfo: for orderId: " + order.getOrderId() + " --- " + ex.getMessage(), ex);
		}
	}
	
	private Distributor addDistributor (Hashtable<String, Object> accountdetail, Long defaultUserForSelfserviceId)throws Exception{
		log.info("----- TopLinkSelfServiceDAO.addDistributor() --- Adding Distributor-----2");
		AddDistributorForm distributorForm = fillDistributorForm(accountdetail);
		
		
		Distributor distributor = distributorService.addDistributorForSelfService(defaultUserForSelfserviceId, distributorForm);
		
		log.info("----- TopLinkSelfServiceDAO.fillDistributorForm() --- Added Distributor-----5");
		return distributor;
	}	
	
	
	
	
	private AddDistributorForm fillDistributorForm(Hashtable<String, Object> accountdetail){
		AddDistributorForm addDistributorForm = new AddDistributorForm();
		
		log.info("----- TopLinkSelfServiceDAO.fillDistributorForm() --- Adding Distributor-----3");
		
		if(accountdetail.get("CompanyName")!=null)						
			addDistributorForm.setDistributorName(accountdetail.get("CompanyName").toString()) ;
		else
			addDistributorForm.setDistributorName(accountdetail.get("FirstName").toString() + " " + accountdetail.get("LastName").toString());
		
		
		//if(accountdetail.get("distributorId")!=null)						
			//addDistributorForm.setDistributorCode(accountdetail.get("distributorId").toString()) ;

		
		addDistributorForm.setFirstName(setEmptyIfNull(accountdetail.get("FirstName")));
		addDistributorForm.setLastName(setEmptyIfNull(accountdetail.get("LastName")));
		addDistributorForm.setWesiteURL("");
		addDistributorForm.setEmailAdd(setEmptyIfNull(accountdetail.get("EmailAddress")));
		addDistributorForm.setSelfAuthor(false);
		
		addDistributorForm.setPhone(setEmptyIfNull(accountdetail.get("Phone")));
		
		addDistributorForm.setExt("");
		addDistributorForm.setAccountStatus(true);			
		addDistributorForm.setBrandId(Integer.parseInt(accountdetail.get("defaultBrandSelfService").toString()));
		
		addDistributorForm.isSelfReporting(); 
		addDistributorForm.setCallLoggingEnabled(false);
		addDistributorForm.setLoginEmailID(accountdetail.get("UserName").toString());
		addDistributorForm.setPassword(accountdetail.get("Password").toString());
		
		addDistributorForm.setMarkedPrivate(true);
		addDistributorForm.setType("Direct Portal");

		addDistributorForm.setAddress1Line1(setEmptyIfNull(accountdetail.get("Address1Line1")));
		addDistributorForm.setAddress1Line2(setEmptyIfNull(accountdetail.get("Address1Line2")));
		addDistributorForm.setCity1(setEmptyIfNull(accountdetail.get("City1")));
		addDistributorForm.setCountry1(setEmptyIfNull(accountdetail.get("Country1")));
		addDistributorForm.setState1(setEmptyIfNull(accountdetail.get("State1")));
		addDistributorForm.setZip1(setEmptyIfNull(accountdetail.get("Zip1")));
		

		addDistributorForm.setAddress2Line1(setEmptyIfNull(accountdetail.get("Address2Line1")));
		addDistributorForm.setAddress2Line2(setEmptyIfNull(accountdetail.get("Address2Line2")));
		addDistributorForm.setCity2(setEmptyIfNull(accountdetail.get("City2")));
		addDistributorForm.setCountry2(setEmptyIfNull(accountdetail.get("Country2")));
		addDistributorForm.setState2(setEmptyIfNull(accountdetail.get("State2")));
		addDistributorForm.setZip2(setEmptyIfNull(accountdetail.get("Zip2")));
		
		
		addDistributorForm.setAudio(false);
		addDistributorForm.setAudioLocked(false);
		addDistributorForm.setVolume(1);
		addDistributorForm.setVolumeLocked(false);
		addDistributorForm.setCaptioning(false);
		addDistributorForm.setCaptioningLocked(true);
		addDistributorForm.setBandwidth(true);
		addDistributorForm.setBandwidthLocked(true);
		addDistributorForm.setVideo(false);
		addDistributorForm.setVideoLocked(false);
		addDistributorForm.setRegistrationEmails(true);
		addDistributorForm.setEnrollmentEmails(true);
		
		
		addDistributorForm.setCourseCompCertificateEmails(true);
		addDistributorForm.setCourseCompCertificateEmailsLocked(true);

		log.info("----- TopLinkSelfServiceDAO.fillDistributorForm() --- Adding Distributor-----4");
		return addDistributorForm;
		
	}
	
	public String setEmptyIfNull(Object funVal){
		
		if(funVal	!=	null)
			return funVal.toString();
		else
			return "";
	}
	
	// LMS-15514 - Update DistributorCode in Distributor table. This function call when SF create Store.
	@Override
	public boolean storeCreation(Hashtable<String, Object> accountdetail){
		try{
			log.info(" --------------- TopLinkSelfServiceDAO.storeCreation() - Start---------------- ");
			String distributorCode = accountdetail.get("storeCreationdistributorId").toString();
			
			
			VU360User vu360User = vu360UserService.findUserByUserName(accountdetail.get("storeCreationUserId").toString());
			
			if(vu360User!=null){
				Distributor objDistributor = distributorService.loadForUpdateDistributor(vu360User.getLearner().getCustomer().getDistributor().getId());
				objDistributor.setDistributorCode(distributorCode);
				distributorService.updateDistributor(objDistributor);
			}else{
				log.error("Error Occured in TopLinkSelfServiceDAO.storeCreation():  " + new Date()+ "--- User not Found");	
			}
		}catch(Exception exception){
			log.error("Error Occured in TopLinkSelfServiceDAO.storeCreation():  --- " + exception.getMessage(), exception);			
		}
		finally{
			log.info(" --------------- TopLinkSelfServiceDAO.storeCreation() - END ----------------- ");
		}
		
		return true;
	}
	
	
	@Override
	public boolean insertCourseLicenseByFreemium(Hashtable<String, Object> accountdetail){
		int i= selfServiceRepository.insertCourseLicenseByFreemium( accountdetail.get("UserName"));
		if(i==1)return true;else return false;
	}
	@Override
	public boolean insertAuthorRightsByFreemium(Hashtable<String, Object> accountdetail){
		return selfServiceRepository.insertAuthorRightsByFreemium(accountdetail.get("UserName"),
				accountdetail.get("proQuantity"));
	}
	@Override
	public boolean insertCourseRightsByFreemium(Hashtable<String, Object> accountdetail){
		int i=selfServiceRepository.insertCoursePublishRightsByFreemium(
				accountdetail.get("UserName"),accountdetail.get("proQuantity"));
		if(i==1)return true;else return false;
	}	
	@Override
	public boolean insertSceneTemplateByFreemium(Hashtable<String, Object> accountdetail){
		int i= selfServiceRepository.insertSceneTemplateByFreemium 
				(accountdetail.get("UserName"),
				accountdetail.get("SceneTemplateName"),
				accountdetail.get("EffectiveFrom"),
				accountdetail.get("EffectiveTo"));
		if(i==1)return true;else return false;
	}
	
	@Override
	public boolean insertCourseRightsByFreemiumV2(Hashtable<String, Object> accountdetail){
		int i= selfServiceRepository.insertSceneTemplateByFreemiumV2 
				(accountdetail.get("UserName"),
				accountdetail.get("PRODUCTCODE"),
				accountdetail.get("EffectiveFrom"),
				accountdetail.get("EffectiveTo"));
		if(i==1)return true;else return false;
	}
	
	@Override
	public boolean processLMSProductPurchase(Hashtable<String, Object> dataBundle, long lmsProductCode) {
		VU360User vu360User=null;
		
		
		try{
			OrderCreatedRequest orderCreatedRequest = (OrderCreatedRequest)dataBundle.get("orderCreatedRequest");
			String userNameWS = orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getAuthenticationCredential().getUsername();
			
			if(userNameWS!=null)
				vu360User = vu360UserService.findUserByUserName(userNameWS);
			
			if(vu360User != null){
				// Step#1: Insert in LMSPRODUCT_PURCHASE
				saveProductPurchase(vu360User, lmsProductCode);
				
				// Step#2: Assign Instructor Role
				if(lmsProductCode == LMSProducts.INSTRUCTORS_FOR_CLASSROOM_TRAINING || lmsProductCode == LMSProducts.WEBINAR_AND_WEBLINK_COURSES){
					saveAssignInstructorRole(vu360User);
				}
				
				// Step#3-A: Permission of Manage Custom Resources in Manager Mode for Distributor
				// Step#3-B: Permission of Manage Custom Resources in Manager Mode for Customer
				if(lmsProductCode == LMSProducts.SCORM_COURSES){
					updateAssignDistributor(vu360User);
					updateAssignManagerRole(vu360User);
				}
			}
			
			return true;
		}catch(Exception ex){
			log.info("Exception...LMSProductInstructorsForClassroomTrainingHandler.java ..... exec().."+ ex.getMessage());
			return false;
		}
	}

	private void saveProductPurchase(VU360User vu360User, long lmsProductCode){
		try{
			Customer customer = vu360User.getLearner().getCustomer();
			
			LMSProductPurchase lmsProductPurchase = new LMSProductPurchase();
			lmsProductPurchase.setCustomer(customer);
			LMSProducts lmsProduct = productPurchaseService.getlmsProduct(lmsProductCode);
			lmsProductPurchase.setLmsProduct(lmsProduct);
			productPurchaseService.savePurchaseProduct(lmsProductPurchase);
		}
		catch(Exception e){
			log.debug(e);
		}
	}
	
	private void updateAssignManagerRole(VU360User vu360User){
		try{
			List<LMSRoleLMSFeature> permissionListCustomer = securityAndRolesService.getPermissionByCustomer(vu360User.getLearner().getCustomer(), LMSRole.ROLE_TRAININGMANAGER);
			List<LMSRoleLMSFeature> finalPermissionsCustomer=new ArrayList<LMSRoleLMSFeature>();
			
			for (LMSRoleLMSFeature lmsRoleLMSFeature : permissionListCustomer) {
				
				LMSFeature lmsFeature = lmsRoleLMSFeature.getLmsFeature();
				if(lmsFeature.getFeatureName()!=null && lmsFeature.getFeatureName().equalsIgnoreCase("Manage Custom Resources")){
					lmsRoleLMSFeature.setEnabled(true);
					
					//LMSRoleLMSFeature lmsRoleLMSFeatureToUpdate = securityAndRolesService.loadForUpdateLMSRoleLMSFeature(lmsRoleLMSFeature.getId());
					//lmsRoleLMSFeatureToUpdate.setEnabled(true);
					securityAndRolesService.saveCustomerLMSFeature(lmsRoleLMSFeature);
					
				}
				finalPermissionsCustomer.add(lmsRoleLMSFeature);
			}
			customerService.updateFeaturesForCustomer(vu360User.getLearner().getCustomer(), finalPermissionsCustomer);
			
		}
		catch(Exception e){
			log.debug(e);
		}
	}
	
	private void updateAssignDistributor(VU360User vu360User){
		try{
			Distributor distributor = vu360User.getLearner().getCustomer().getDistributor();
			List<LMSRoleLMSFeature> permissionList = securityAndRolesService.getPermissionByDistributor(distributor, "ROLE_TRAININGADMINISTRATOR");
			List<LMSRoleLMSFeature> finalPermissions=new ArrayList<LMSRoleLMSFeature>();
			
			for (LMSRoleLMSFeature lmsRoleLMSFeature : permissionList) {
				
				LMSFeature lmsFeature = lmsRoleLMSFeature.getLmsFeature();
				if(lmsFeature.getFeatureName()!=null && lmsFeature.getFeatureName().equalsIgnoreCase("Manage Custom Resources")){
					lmsRoleLMSFeature.setEnabled(true);
				}
				finalPermissions.add(lmsRoleLMSFeature);
			}
			distributorService.updateFeaturesForDistributor(distributor, finalPermissions);
		}
		catch(Exception e){
			log.debug(e);
		}
	}
	
	private void saveAssignInstructorRole(VU360User vu360User){
		try{
			final String INSTRUCTOR_ROLE_TYPE = "ROLE_INSTRUCTOR";
			final String INSTRUCTOR_ROLE_NAME = "INSTRUCTOR";
			
			Customer customer = vu360User.getLearner().getCustomer();
			ContentOwner contentowner = vu360User.getContentOwner();
			
			List<LMSRoleLMSFeature> permissionList=null;
			LMSRole objRole = new LMSRole();
			List<LMSFeature> featureList = vu360UserService.getFeaturesByRoleType(INSTRUCTOR_ROLE_TYPE);
			
			if(featureList!=null){
				permissionList = new ArrayList<LMSRoleLMSFeature>();
				for(LMSFeature feature:featureList){
					LMSRoleLMSFeature objPermission =  new LMSRoleLMSFeature();
					objPermission.setLmsRole(objRole);
					objPermission.setLmsFeature(feature);
					objPermission.setEnabled(true);
					
					permissionList.add(objPermission);
				}
			}
			
			objRole.setRoleName(INSTRUCTOR_ROLE_NAME);
			objRole.setRoleType(INSTRUCTOR_ROLE_TYPE);
			objRole.setLmsPermissions(permissionList);
			objRole.setOwner(customer);
			objRole.setDefaultForRegistration(true);
			
			objRole = learnerService.addRole(objRole,customer);

			Instructor instructor = new Instructor();
			instructor.setUser(vu360User);
			instructor.setContentOwner(contentowner);
			vu360User.setInstructor(instructor); 				
			vu360User.addLmsRole(objRole);
			vu360User = vu360UserService.updateUser(vu360User);
			
			long[] l = new long[1];
			l[0] = vu360User.getId();
			learnerService.assignUserToRole(l, objRole);
		}
		catch(Exception e){
			log.debug(e);
		}
		
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

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public AuthorService getAuthorService() {
		return authorService;
	}

	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public LMSProductPurchaseService getProductPurchaseService() {
		return productPurchaseService;
	}

	public void setProductPurchaseService(
			LMSProductPurchaseService productPurchaseService) {
		this.productPurchaseService = productPurchaseService;
	}

	public SecurityAndRolesService getSecurityAndRolesService() {
		return securityAndRolesService;
	}

	public void setSecurityAndRolesService(
			SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public LearnerServiceImplLmsApi getLearnerServiceImplLmsApi() {
		return learnerServiceImplLmsApi;
	}

	public void setLearnerServiceImplLmsApi(
			LearnerServiceImplLmsApi learnerServiceImplLmsApi) {
		this.learnerServiceImplLmsApi = learnerServiceImplLmsApi;
	}


	
}
