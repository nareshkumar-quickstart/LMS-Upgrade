package com.softech.vu360.lms.products.handlers;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.service.SelfServiceService;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest;

/* This handler will use for following product
 * Author LCMS Software – Shelf 
 */
public class IndivisualProduct15COursePublishRightsPermissionLCMS implements Handler{

	private static final Logger LOGGER = Logger.getLogger(IndivisualProduct15COursePublishRightsPermissionLCMS.class.getName());
	
	@Override
	public boolean init() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exec(Object dataBundle, Exception errors) throws Exception {
		try{
			LOGGER.info("...IndivisualProduct15COursePublishRightsPermissionLCMS ..... exec().. Start");
			
			Hashtable obj = (Hashtable<String, Object>) dataBundle;
			SelfServiceService  selfServiceService = (SelfServiceService) obj.get("selfServiceService");
			selfServiceService.insertCourseRightsByFreemium(paramSetting(obj));
			
			LOGGER.info("...IndivisualProduct15COursePublishRightsPermissionLCMS ..... exec().. End");
			return true;
		}catch(Exception ex){
			LOGGER.info("...IndivisualProduct15COursePublishRightsPermissionLCMS.java ..... exec().."+ ex.getMessage());
			return false;
		}
		
	}

	@Override
	public boolean exit(Object dataBundle, Exception errors) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	
	public Hashtable paramSetting(Hashtable<String, Object> dataBundle){
		LOGGER.info("...IndivisualProduct15COursePublishRightsPermissionLCMS ..... paramSetting().. Start");
		
		OrderCreatedRequest orderCreatedRequest = (OrderCreatedRequest)dataBundle.get("orderCreatedRequest");
  		Hashtable<String, String> colFreemiumaccount = new Hashtable<String, String>();
		colFreemiumaccount.put("UserName", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getAuthenticationCredential().getUsername()) ;
		
		if(orderCreatedRequest.getOrder().getLineItem()!=null &&
			    orderCreatedRequest.getOrder().getLineItem().get(0)!=null &&
			    orderCreatedRequest.getOrder().getLineItem().get(0).getQuantity()!=null){
			   
			   colFreemiumaccount.put("proQuantity", orderCreatedRequest.getOrder().getLineItem().get(0).getQuantity().toString()) ;
			  
			  }else{
			   colFreemiumaccount.put("proQuantity", "-1") ;			  
			  }		
		
		LOGGER.info("...IndivisualProduct15COursePublishRightsPermissionLCMS ..... paramSetting().. End");
		return colFreemiumaccount;
	}
	
	
}
