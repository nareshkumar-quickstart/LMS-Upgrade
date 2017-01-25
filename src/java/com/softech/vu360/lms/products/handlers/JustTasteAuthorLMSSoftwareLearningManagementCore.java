package com.softech.vu360.lms.products.handlers;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.service.SelfServiceService;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest;

//this class handler will use for following product
//'Author LMS Software – Learning Management Core'
public class JustTasteAuthorLMSSoftwareLearningManagementCore implements Handler{

	private static final Logger LOGGER = Logger.getLogger(JustTasteAuthorLMSSoftwareLearningManagementCore.class.getName());
	
	@Override
	public boolean init() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exec(Object dataBundle, Exception errors) throws Exception {
		try{
			LOGGER.info("...JustTasteAuthorLMSSoftwareLearningManagementCore.java ..... exec().. Start..");
			Hashtable obj = (Hashtable<String, Object>) dataBundle;
			SelfServiceService  selfServiceService = (SelfServiceService) obj.get("selfServiceService");
			selfServiceService.processSelfServiceOrder(makeGenForFreemiumAccount(obj));
			LOGGER.info("...JustTasteAuthorLCMSSoftwareCourseCreationCore.java ..... exec().. End..");
			return true;
		}catch(Exception ex){
			LOGGER.info("...JustTasteAuthorLMSSoftwareLearningManagementCore.java ..... exec().."+ ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean exit(Object dataBundle, Exception errors) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	public Hashtable makeGenForFreemiumAccount(Hashtable<String, Object> dataBundle) throws Exception{
		LOGGER.info("...JustTasteAuthorLMSSoftwareLearningManagementCore.java ..... makeGenForFreemiumAccount().. Start..");
		Properties properties = new Properties();
		//List<LineItem> lstLMSLineItem = new ArrayList<LineItem>();
		
    	InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("selfServiceSetupEntries.properties");  
		properties.load(inputStream);
		//String varDefaultCourseGuid = properties.getProperty("defaultCourseGuid");
		//String varDefaultCourseGroupGuid = properties.getProperty("defaultCourseGroupGuid");
		String varDefaultBrandSelfService = properties.getProperty("defaultBrandSelfService");
		
		OrderCreatedRequest orderCreatedRequest = (OrderCreatedRequest) dataBundle.get("orderCreatedRequest");
		Hashtable<String, Object> colFreemiumaccount = new Hashtable<String, Object>();
		colFreemiumaccount.put("orderCreatedRequest", orderCreatedRequest) ;
		colFreemiumaccount.put("orderCreatedResponse", dataBundle.get("orderCreatedResponse")) ;
		colFreemiumaccount.put("distributorId", orderCreatedRequest.getOrder().getDistributorId()) ;
		colFreemiumaccount.put("customerId", orderCreatedRequest.getOrder().getCustomer().getCustomerId()) ;
		colFreemiumaccount.put("UserName", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getAuthenticationCredential().getUsername()) ;
		colFreemiumaccount.put("EmailAddress", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getEmailAddress()) ;
		colFreemiumaccount.put("FirstName", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getFirstName()) ;
		colFreemiumaccount.put("LastName", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getLastName()) ;
		colFreemiumaccount.put("Password", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getAuthenticationCredential().getPassword()) ;
		
		if(orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getPrimaryPhone()!=null)
			colFreemiumaccount.put("Phone", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getPrimaryPhone());
		
		if(orderCreatedRequest.getOrder().getCustomer().getPrimaryAddress()!=null){
			colFreemiumaccount.put("Country1", orderCreatedRequest.getOrder().getCustomer().getPrimaryAddress().getCountry()) ;
			colFreemiumaccount.put("City1", orderCreatedRequest.getOrder().getCustomer().getPrimaryAddress().getCity());
			
			if(orderCreatedRequest.getOrder().getCustomer().getCompanyName()!=null && !orderCreatedRequest.getOrder().getCustomer().getCompanyName().equals(""))
				colFreemiumaccount.put("CompanyName", orderCreatedRequest.getOrder().getCustomer().getCompanyName()); 
			else
				colFreemiumaccount.put("CompanyName", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getFirstName()  + " " + 
						orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getLastName());
			
			colFreemiumaccount.put("Address1Line1", orderCreatedRequest.getOrder().getCustomer().getPrimaryAddress().getAddressLine1());
			colFreemiumaccount.put("Address1Line2", orderCreatedRequest.getOrder().getCustomer().getPrimaryAddress().getAddressLine2());
			colFreemiumaccount.put("State1", orderCreatedRequest.getOrder().getCustomer().getPrimaryAddress().getState());
			colFreemiumaccount.put("Zip1", orderCreatedRequest.getOrder().getCustomer().getPrimaryAddress().getZipCode());
		
			
			if(orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getBillingAddress()!=null){
				colFreemiumaccount.put("Address2Line1", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getBillingAddress().getAddressLine1());
				colFreemiumaccount.put("Address2Line2", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getBillingAddress().getAddressLine2());
				colFreemiumaccount.put("City2", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getBillingAddress().getCity());
				colFreemiumaccount.put("State2", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getBillingAddress().getState());
				colFreemiumaccount.put("Zip2", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getBillingAddress().getZipCode());
				colFreemiumaccount.put("Country2", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getBillingAddress().getCountry());
			}
		}
		
			
		
		colFreemiumaccount.put("OrderId", orderCreatedRequest.getOrder().getOrderId());
		//colFreemiumaccount.put("defaultCourseGuid", varDefaultCourseGuid);
		//colFreemiumaccount.put("defaultCourseGroupGuid", varDefaultCourseGroupGuid);
		colFreemiumaccount.put("defaultBrandSelfService", varDefaultBrandSelfService);
		colFreemiumaccount.put("TransactionGUID", orderCreatedRequest.getTransactionGUID());
		
		// Creating collection of type com.softech.vu360.lms.model.LineItem and sending it to lms.jar to save in ORDERINFO and OrderLineItem table
		/*if(orderCreatedRequest.getOrder()!=null){
			for(OrderLineItem selfServiceOrderItem : orderCreatedRequest.getOrder().getLineItem()){
				LineItem lmsLineItem = new LineItem();
				lmsLineItem.setQuantity(selfServiceOrderItem.getQuantity().intValue());
				lmsLineItem.setItemGUID(selfServiceOrderItem.getGuid());
				lmsLineItem.setRefundQty(0);
				lstLMSLineItem.add(lmsLineItem);
			}
		}
		
		colFreemiumaccount.put("lstLMSLineItem", lstLMSLineItem);*/
		LOGGER.info("...JustTasteAuthorLMSSoftwareLearningManagementCore.java ..... makeGenForFreemiumAccount().. End..");
		
		return colFreemiumaccount;
	}
}