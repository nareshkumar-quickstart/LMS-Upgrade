package com.softech.vu360.lms.products.handlers;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.service.SelfServiceService;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest;

/* This handler will use for following product
 * Author LCMS Software � Shelf 
 */
public class IndivisualProduct15AuthorRightsPermissionLCMS implements Handler{

	private static final Logger LOGGER = Logger.getLogger(IndivisualProduct15AuthorRightsPermissionLCMS.class.getName());
	
	@Override
	public boolean init() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exec(Object dataBundle, Exception errors) throws Exception {
		try{
			LOGGER.info("...OffGreatStart4AuthorLCMSSoftwareShelf ..... exec().. Start");
			
			Hashtable obj = (Hashtable<String, Object>) dataBundle;
			SelfServiceService  selfServiceService = (SelfServiceService) obj.get("selfServiceService");
			selfServiceService.insertAuthorRightsByFreemium(paramSetting(obj));
			
			LOGGER.info("...IndivisualProduct15AuthorRightsPermissionLCMS ..... exec().. End");
			return true;
		}catch(Exception ex){
			LOGGER.info("...IndivisualProduct15AuthorRightsPermissionLCMS.java ..... exec().."+ ex.getMessage());
			return false;
		}
		
	}

	@Override
	public boolean exit(Object dataBundle, Exception errors) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	
	public Hashtable paramSetting(Hashtable<String, Object> dataBundle){
		LOGGER.info("...IndivisualProduct15AuthorRightsPermissionLCMS ..... paramSetting().. Start");
		
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
		
		LOGGER.info("...IndivisualProduct15AuthorRightsPermissionLCMS ..... paramSetting().. End");
		return colFreemiumaccount;
	}
	
	
}
