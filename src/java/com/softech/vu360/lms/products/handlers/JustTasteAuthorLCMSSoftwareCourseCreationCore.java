package com.softech.vu360.lms.products.handlers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.service.SelfServiceService;
import com.softech.vu360.lms.webservice.message.selfservice.OrderCreatedRequest;

/**
 * This handler will use for product 'Author LCMS Software – Course Creation Core'
 * @author rehan.rana
 * @since 4.24.1
 */

public class JustTasteAuthorLCMSSoftwareCourseCreationCore implements Handler{

	private static final Logger LOGGER = Logger.getLogger(JustTasteAuthorLCMSSoftwareCourseCreationCore.class.getName());
	@Override
	public boolean init() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exec(Object dataBundle, Exception errors) throws Exception {
		try{
			LOGGER.info("...JustTasteAuthorLCMSSoftwareCourseCreationCore.java ..... exec().. Start..");
			Hashtable obj = (Hashtable<String, Object>) dataBundle;
			SelfServiceService  selfServiceService = (SelfServiceService) obj.get("selfServiceService");
			selfServiceService.createFreemiumAccount(makeGenForFreemiumAccount(obj));
			LOGGER.info("...JustTasteAuthorLCMSSoftwareCourseCreationCore.java ..... exec().. End..");
			return true;
		}catch(Exception ex){
			LOGGER.info("...JustTasteAuthorLCMSSoftwareCourseCreationCore.java ..... exec().."+ ex.getMessage());
			return false;
		}
		
	}

	@Override
	public boolean exit(Object dataBundle, Exception errors) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	
	public Hashtable makeGenForFreemiumAccount(Hashtable<String, Object> dataBundle){
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());  
		
		final String  MaxAuthorCount = "1";
		final String  MaxCourseCount  = "1";
		
		
		OrderCreatedRequest orderCreatedRequest = (OrderCreatedRequest)dataBundle.get("orderCreatedRequest");
  	
		Hashtable<String, String> colFreemiumaccount = new Hashtable<String, String>();
		//colFreemiumaccount.put("StoredProcedureName", "TEST_CREATE_FREEMIUM_ACCOUNT") ;
		colFreemiumaccount.put("ContentOwnerName", orderCreatedRequest.getOrder().getCustomer().getCustomerName()) ;
		colFreemiumaccount.put("UserName", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getAuthenticationCredential().getUsername()) ;
		colFreemiumaccount.put("EmailAddress", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getEmailAddress()) ;
		colFreemiumaccount.put("FirstName", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getFirstName()) ;
		colFreemiumaccount.put("LastName", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getLastName()) ;
		colFreemiumaccount.put("Password", orderCreatedRequest.getOrder().getCustomer().getPrimaryContact().getAuthenticationCredential().getPassword()) ;
		colFreemiumaccount.put("EffectiveFrom", format.format(calendar.getTime())) ;
		calendar.add(Calendar.YEAR, 1);
		colFreemiumaccount.put("EffectiveTo", format.format(calendar.getTime())) ;
		colFreemiumaccount.put("MaxAuthorCount", MaxAuthorCount) ;
		colFreemiumaccount.put("MaxCourseCount", MaxCourseCount ) ;
		
		
		return colFreemiumaccount;
	}
	
	
	
	
	
	
}
