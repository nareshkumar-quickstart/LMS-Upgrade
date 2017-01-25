package com.softech.vu360.lms.web.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.OrderCreatedEventService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.LmsSfOrderResultBuilder;
import com.softech.vu360.lms.web.controller.model.EasySignupPageForm;
import com.softech.vu360.lms.webservice.message.storefront.AuthenticationCredential;
import com.softech.vu360.lms.webservice.message.storefront.OrderLineItem;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.VU360Properties;


/**
 * AICC Controller that allows access to all content
 * via AICC adapter.
 * 
 * @author Jason Burns
 */
public class EasySignupPageController implements Controller {
	
	private static final Logger log = Logger.getLogger(EasySignupPageController.class.getName());
	private static final String ADEnabled = VU360Properties.getVU360Property("ad.integration.enabled");//Active Directory
	private static final String DISTRIBUTOR_CODE = VU360Properties.getVU360Property("lms.distributor.code.megasite");
	private VU360UserService vu360UserService=null;
	private ActiveDirectoryService activeDirectoryService = null;
	private OrderCreatedEventService orderCreatedEventService = null;
	private DistributorService distributorService = null;
	private String easySignupPage = null;
	private String easySignupSuccessPage = null;
	private PasswordEncoder passwordEncoder = null;
	private SaltSource saltSource = null;
	
	EasySignupPageForm easySignupForm = new EasySignupPageForm();
	
	
	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		easySignupForm = convertRequestToModel(request);
		
		// To Check if Course is not selected 
		if(easySignupForm==null || (easySignupForm!=null && easySignupForm.getCourseList()==null) || 
				(easySignupForm!=null && easySignupForm.getCourseList()!=null&& easySignupForm.getCourseList().size()<=0)){
			
			List<String> errorList = new ArrayList<String>();
			errorList.add("error.lms.easysignup.course.missing");
			
			if(errorList!=null && errorList.size()>0){
				Map<Object, Object> errorMap = new HashMap<Object, Object>();
				errorMap.put("error", true);
				errorMap.put("errorCodes", errorList);
				context.put("status", errorMap);
				return new ModelAndView(easySignupPage, "context", context);
			}
			
		}
		
		try{
			log.debug("~~~ Inside EasySignupPageController.handleRequest() ~~~");
			String action = request.getParameter("_frmAction");
			log.debug("~~~ Action = "+action+" ~~~");
			
			if(action!=null && action.equals("New")){
				Map<Object, Object> errorMap = this.validateNewSignupForm(null,this.easySignupForm);
				
				if(errorMap.get("error")==null) {
					
					// Adding User and enrolling into Course
					context = createUser(request, context); // This method will create user and enroll user inthe course
					context.put("freecourseslink", VU360Properties.getVU360Property("lms.easysignup.free.courses"));
					context.put("freecoursessoftware", VU360Properties.getVU360Property("lms.easysignup.free.courses_software"));
					return new ModelAndView(easySignupSuccessPage, "context", context);
				}
				else{
					context.put("status", errorMap);
					context.put("newUserForm", this.easySignupForm);
				}
				
			}
			else if(action!=null && action.equals("Return")){
				List<String> errorList = new ArrayList<String>();
				
				String username = request.getParameter("rUsername");
				String password = request.getParameter("rPassword");
				
				try{
					
					VU360User objUser = vu360UserService.findUserByUserName(username);
					com.softech.vu360.lms.vo.VU360User user = ProxyVOHelper.setUserProxy(objUser);
					
					if(user!=null && user.getLearner()!=null && user.getLearner().getCustomer()!=null){
						boolean isAuthenticated = false;
						if(ADEnabled.equals(Boolean.TRUE)){
							isAuthenticated = isValidADUserCredentials(username, password);
						}
						else{
							
							// if user's password exists in plan text in DB
				             if(user.getPassword().equalsIgnoreCase(password)){
				            	 isAuthenticated = true;
				             }
							
							Object salt = saltSource.getSalt(user);
							// if user's password exists encrypted in DB
				             if (passwordEncoder.isPasswordValid(user.getPassword(), password, salt)) {
				            	 isAuthenticated = true;
				             }
				             
				             
						}
						
						com.softech.vu360.lms.vo.Customer userCustomer = user.getLearner().getCustomer();
						if(isAuthenticated){
							context.put("username", username);
							context.put("password", password);
							
							//Checking if the user is a MegaSite user else new Registration message will be dispalyed
							if(userCustomer.getDistributor()!=null && userCustomer.getDistributor().getDistributorCode()!=null && 
									userCustomer.getDistributor().getDistributorCode().equals(DISTRIBUTOR_CODE)){
								
								// Enrolling User
								enrollExistingUser(objUser,request);
								
								//return new ModelAndView(easySignupSuccessPage, "context", context);
								return new ModelAndView("redirect:/j_spring_security_check?username="+username+"&password="+password+"&spring-security-redirect=/interceptor.do");
							}
							else {
								
								errorList.add("error.lms.easysignup.existinguser.non.mega.site");
							}
						}
						else{
							errorList.add("lms.login.loginInfo");
							
						}
					}
					else{
						errorList.add("lms.login.loginInfo");
						
					}
				}
				catch(Exception e){
					log.debug(e);
				}
				
				if(errorList!=null && errorList.size()>0){
					Map<Object, Object> errorMap = new HashMap<Object, Object>();
					errorMap.put("error", true);
					errorMap.put("errorCodes", errorList);
					
					
					context.put("status", errorMap);
					context.put("newUserForm", this.easySignupForm);
				}

			}
			else{
				//context.put("status", errorMap);
				
				context.put("newUserForm", this.easySignupForm);
			}
		}
		catch(Exception e){
			log.debug(e.getMessage());
		}
		
		context.put("freecourseslink", VU360Properties.getVU360Property("lms.easysignup.free.courses"));
		context.put("freecoursessoftware", VU360Properties.getVU360Property("lms.easysignup.free.courses_software"));
		return new ModelAndView(easySignupPage, "context", context);
	}
	
	private boolean isValidADUserCredentials(String username, String password){
		boolean isAuthenticated =false;
		try{
			if(username!=null && password!=null){
				isAuthenticated = activeDirectoryService.authenticateADUser(username, password);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return isAuthenticated;
	}
	
	private void enrollExistingUser(VU360User userLoggedin, HttpServletRequest request){
		try{
			Customer userCustomer = userLoggedin.getLearner().getCustomer();
			
			// Customer AuthenticationCredential Object
			AuthenticationCredential authCredential = new AuthenticationCredential();
			authCredential.setUsername(userLoggedin.getUsername());
			authCredential.setPassword(this.easySignupForm.getrPassword());
			authCredential.setDomain(userLoggedin.getDomain());
			

			// Create Customer Primary address Object (Billing Address)
			com.softech.vu360.lms.webservice.message.storefront.Address oCustomerBillingAddress = new com.softech.vu360.lms.webservice.message.storefront.Address();
			oCustomerBillingAddress.setCity(userCustomer.getBillingAddress().getCity());
			oCustomerBillingAddress.setCountry(userCustomer.getBillingAddress().getCountry());
			oCustomerBillingAddress.setState(userCustomer.getBillingAddress().getState());
			oCustomerBillingAddress.setAddressLine1(userCustomer.getBillingAddress().getStreetAddress());
			oCustomerBillingAddress.setAddressLine2(userCustomer.getBillingAddress().getStreetAddress2());
			oCustomerBillingAddress.setAddressLine3(userCustomer.getBillingAddress().getStreetAddress3());
			oCustomerBillingAddress.setZipCode(userCustomer.getBillingAddress().getZipcode());
			
			
			// Create Customer Primary address Object (Shipping Address)
			com.softech.vu360.lms.webservice.message.storefront.Address oCustomerShippingAddress = new com.softech.vu360.lms.webservice.message.storefront.Address();
			oCustomerShippingAddress.setCity(userCustomer.getBillingAddress().getCity());
			oCustomerShippingAddress.setCountry(userCustomer.getBillingAddress().getCountry());
			oCustomerShippingAddress.setState(userCustomer.getBillingAddress().getState());
			oCustomerShippingAddress.setAddressLine1(userCustomer.getBillingAddress().getStreetAddress());
			oCustomerShippingAddress.setAddressLine2(userCustomer.getBillingAddress().getStreetAddress2());
			oCustomerShippingAddress.setAddressLine3(userCustomer.getBillingAddress().getStreetAddress3());
			oCustomerShippingAddress.setZipCode(userCustomer.getBillingAddress().getZipcode());
			
			
			// Create Customer Primary Contact Object
			com.softech.vu360.lms.webservice.message.storefront.Contact oCustomerContact = new com.softech.vu360.lms.webservice.message.storefront.Contact();
			oCustomerContact.setAuthenticationCredential(authCredential);
			oCustomerContact.setEmailAddress(userCustomer.getEmail());
			oCustomerContact.setFirstName(userCustomer.getFirstName());
			oCustomerContact.setLastName(userCustomer.getLastName());
			oCustomerContact.setShippingAddress(oCustomerBillingAddress);
			oCustomerContact.setBillingAddress(oCustomerShippingAddress);
			oCustomerContact.setBillingShippingAddressSame(Boolean.TRUE);
			oCustomerContact.setPrimaryPhone(userCustomer.getPhoneNumber());
			//oCustomerContact.setAlternatePhone(userCustomer.getp);
			
			// Create Customer Object
			com.softech.vu360.lms.webservice.message.storefront.Customer oCustomer = new com.softech.vu360.lms.webservice.message.storefront.Customer();
			oCustomer.setCustomerId(userCustomer.getCustomerGUID());
			oCustomer.setCustomerName(userCustomer.getName());
			oCustomer.setPrimaryContact(oCustomerContact);
			oCustomer.setPrimaryAddress(oCustomerContact.getBillingAddress());
			
			// Create OrderLine
			List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>();
			for (String lineItemCourse : this.easySignupForm.getCourseList()) {
				OrderLineItem lineItem = new OrderLineItem();
				lineItem.setGuid(lineItemCourse);
				lineItem.setLineitemguid("-1");
				lineItem.setQuantity(new BigInteger("1"));
				lineItem.setTermOfService(new BigInteger("0"));
				
				orderLineItems.add(lineItem);
			}
			// Create Order Object
			com.softech.vu360.lms.webservice.message.storefront.Order order = new com.softech.vu360.lms.webservice.message.storefront.Order();
			order.setOrderId(GUIDGeneratorUtil.generateGUID());
			order.setCustomer(oCustomer);
			order.setDistributorId(userCustomer.getDistributor().getDistributorCode());
			order.setLineItem(orderLineItems);
			
			// Create orderCreatedRequest Object
			com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest orderCreatedRequest = new com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest();
			orderCreatedRequest.setOrder(order);
			orderCreatedRequest.setTransactionGUID(GUIDGeneratorUtil.generateGUID());
			
			try{
				GregorianCalendar gcal = new GregorianCalendar();
			    XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
				orderCreatedRequest.setEventDate(xgcal);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			
			// Placing Order
			LmsSfOrderResultBuilder result = orderCreatedEventService.processStoreFrontOrder(orderCreatedRequest);
			
		}
		catch(Exception e){
			log.debug(e.getStackTrace());
		}
	}
	
	private Map<Object, Object> createUser(HttpServletRequest request, Map<Object, Object> context){
		String firstName = request.getParameter("nUserFirstName");
		String lastName = request.getParameter("nUserLastName");
		String emailAddress = request.getParameter("nUserEmailAddress");
		String userNameOption = request.getParameter("nUserNameOption");
		String username = (userNameOption==null || (userNameOption!=null && userNameOption.equals("0"))) ? request.getParameter("nUserName") : emailAddress;
		String password = request.getParameter("nPassword");
		//String courseGuids = "35d7652ac57d4fb08d569c43473d03d0";//request.getParameter("nCourse");
		
		
		// Create Customer AuthenticationCredential Object
		AuthenticationCredential authCredential = new AuthenticationCredential();
		authCredential.setUsername(username);
		authCredential.setPassword(password);
		//authCredential.setDomain(value);
		
		// Create Customer Primary address Object
		com.softech.vu360.lms.webservice.message.storefront.Address oCustomerAddress = new com.softech.vu360.lms.webservice.message.storefront.Address();
		oCustomerAddress.setAddressLine1("");
		oCustomerAddress.setAddressLine2("");
		oCustomerAddress.setCity("");
		oCustomerAddress.setCountry("");
		oCustomerAddress.setState("");
		oCustomerAddress.setZipCode("");
		
		
		// Create Customer Primary Contact Object
		com.softech.vu360.lms.webservice.message.storefront.Contact oCustomerContact = new com.softech.vu360.lms.webservice.message.storefront.Contact();
		oCustomerContact.setAuthenticationCredential(authCredential);
		oCustomerContact.setEmailAddress(emailAddress);
		oCustomerContact.setFirstName(firstName);
		oCustomerContact.setLastName(lastName);
		oCustomerContact.setShippingAddress(oCustomerAddress);
		oCustomerContact.setBillingAddress(oCustomerAddress);
		oCustomerContact.setBillingShippingAddressSame(Boolean.TRUE);
		oCustomerContact.setPrimaryPhone("");
		oCustomerContact.setAlternatePhone("");
		
		// Create Customer Object
		com.softech.vu360.lms.webservice.message.storefront.Customer oCustomer = new com.softech.vu360.lms.webservice.message.storefront.Customer();
		oCustomer.setCustomerId("-1");
		oCustomer.setCustomerName(firstName + " " +lastName);
		oCustomer.setPrimaryContact(oCustomerContact);
		oCustomer.setPrimaryAddress(oCustomerAddress);
		
		
		// Create OrderLine
		List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>();
		for (String lineItemCourse : this.easySignupForm.getCourseList()) {
			OrderLineItem lineItem = new OrderLineItem();
			lineItem.setGuid(lineItemCourse);
			lineItem.setLineitemguid("-1");
			lineItem.setQuantity(new BigInteger("1"));
			lineItem.setTermOfService(new BigInteger("0"));
			orderLineItems.add(lineItem);
		}		
		
		
		// Create Order Object
		com.softech.vu360.lms.webservice.message.storefront.Order order = new com.softech.vu360.lms.webservice.message.storefront.Order();
		order.setOrderId(GUIDGeneratorUtil.generateGUID());
		order.setCustomer(oCustomer);
		order.setDistributorId(DISTRIBUTOR_CODE);
		order.setLineItem(orderLineItems);
		
		// Create orderCreatedRequest Object
		com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest orderCreatedRequest = new com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest();
		orderCreatedRequest.setOrder(order);
		orderCreatedRequest.setTransactionGUID(GUIDGeneratorUtil.generateGUID());
		
		try{
			GregorianCalendar gcal = new GregorianCalendar();
		    XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			orderCreatedRequest.setEventDate(xgcal);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//Placing Order
		LmsSfOrderResultBuilder result = orderCreatedEventService.processStoreFrontOrder(orderCreatedRequest);
		
		context.put("username", username);
		context.put("password", password);
		
		return context;
	}

	public String getEasySignupPage() {
		return easySignupPage;
	}
	
	public void setEasySignupPage(String easySignupPage) {
		this.easySignupPage = easySignupPage;
	}


	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}


	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}

	public String getEasySignupSuccessPage() {
		return easySignupSuccessPage;
	}

	public void setEasySignupSuccessPage(String easySignupSuccessPage) {
		this.easySignupSuccessPage = easySignupSuccessPage;
	}

	public OrderCreatedEventService getOrderCreatedEventService() {
		return orderCreatedEventService;
	}

	public void setOrderCreatedEventService(
			OrderCreatedEventService orderCreatedEventService) {
		this.orderCreatedEventService = orderCreatedEventService;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}
	
	
	private Map<Object, Object> validateNewSignupForm(Map<Object, Object> status, EasySignupPageForm newUserForm){
		if(status==null)
			status = new HashMap<Object, Object>();

		List<String> errorList = new ArrayList<String>();
		
		if( newUserForm.getnFirstName()==null || (newUserForm.getnFirstName()!=null && newUserForm.getnFirstName().length()<=0) ||
			newUserForm.getnLastName()==null || (newUserForm.getnLastName()!=null && newUserForm.getnLastName().length()<=0) ||
			newUserForm.getnEmailAddress()==null || (newUserForm.getnEmailAddress()!=null && newUserForm.getnEmailAddress().length()<=0) ||
			newUserForm.getnEmailAddressConfirm()==null || (newUserForm.getnEmailAddressConfirm()!=null && newUserForm.getnEmailAddressConfirm().length()<=0) ||
			newUserForm.getnUserName()==null || (newUserForm.getnUserName()!=null && newUserForm.getnUserName().length()<=0) ||
			newUserForm.getnPassword()==null || (newUserForm.getnPassword()!=null && newUserForm.getnPassword().length()<=0) ||
			newUserForm.getnPasswordConfirm()==null || (newUserForm.getnPasswordConfirm()!=null && newUserForm.getnPasswordConfirm().length()<=0)
			){
			
			errorList.add("error.lms.easysignup.newuser.mandatoryfield.missing");
		}
		else{
			// Email
			if(!newUserForm.getnEmailAddress().equals(newUserForm.getnEmailAddressConfirm())){
				errorList.add("error.lms.easysignup.newuser.email.matching");
			}
			else if (!FieldsValidation.isEmailValid(newUserForm.getnEmailAddress())) {
				errorList.add("error.lms.easysignup.newuser.email.invalid");
			}
			
			
			// Password
			if(!newUserForm.getnPassword().equals(newUserForm.getnPasswordConfirm())){
				errorList.add("error.lms.easysignup.newuser.password.matching");
			}
			else if(!FieldsValidation.isPasswordCorrect(newUserForm.getnPassword())){
				errorList.add("error.lms.easysignup.newuser.password.length");
			}
			else if(newUserForm.getnPassword()==null || (newUserForm.getnPassword()!=null && newUserForm.getnPassword().length()<8)){
				errorList.add("error.lms.easysignup.newuser.password.length");
			}
			
			
			// Check if username already exists in DB
			VU360User user = vu360UserService.findUserByUserName(newUserForm.getnUserName());
			if(FieldsValidation.isInValidUsername(newUserForm.getnUserName())) {
				errorList.add("error.lms.easysignup.newuser.username.invalid");
			}
			else if(user!=null && user.getUsername()!=null){
				errorList.add("error.lms.easysignup.newuser.already.exist");
			}
			
			
		}
		
		if(errorList!=null && errorList.size()>0){
			status.put("error", true);
			status.put("errorCodes", errorList);
		}
		
        return status;
	}
	
	
	
	private EasySignupPageForm convertRequestToModel(HttpServletRequest request){
		EasySignupPageForm model = new EasySignupPageForm();
		try{
			model.setnFirstName(request.getParameter("nUserFirstName"));
			model.setnLastName(request.getParameter("nUserLastName"));
			model.setnEmailAddress(request.getParameter("nUserEmailAddress"));
			model.setnEmailAddressConfirm(request.getParameter("nUserEmailAddressConfirm"));
			model.setnUserNameOption(request.getParameter("nUserNameOption"));
			
			String username = (model.getnUserNameOption()==null || (model.getnUserNameOption()!=null && model.getnUserNameOption().equals("0"))) ? request.getParameter("nUserName") : model.getnEmailAddress();
			model.setnUserName(username);
			
			model.setnPassword(request.getParameter("nPassword"));
			model.setnPasswordConfirm(request.getParameter("nPasswordConfirm"));
			
			model.setrUserName(request.getParameter("rUsername"));
			model.setrPassword(request.getParameter("rPassword"));
			
			String[] courses = request.getParameterValues("COURSEGUID");
			List<String> courseList = new ArrayList<String>(Arrays.asList(courses));
			model.setCourseList(courseList);

		}
		catch(Exception e){
			log.debug(e.getStackTrace());
		}
		
		return model;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public SaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}
	
 
}