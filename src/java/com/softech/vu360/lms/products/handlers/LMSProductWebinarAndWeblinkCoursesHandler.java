package com.softech.vu360.lms.products.handlers;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.LMSProducts;
import com.softech.vu360.lms.service.SelfServiceService;

/*
 * This class handler will use for following product
 * LMS – Webinar and Weblink Courses
 * Product Code = 1001
 */

public class LMSProductWebinarAndWeblinkCoursesHandler implements Handler{

	private static final Logger LOGGER = Logger.getLogger(LMSProductWebinarAndWeblinkCoursesHandler.class.getName());
	
	@Override
	public boolean init() throws Exception {
		return false;
	}

	@Override
	public boolean exec(Object dataBundle, Exception errors) throws Exception {
		try{
			Hashtable obj = (Hashtable<String, Object>) dataBundle;
			SelfServiceService  selfServiceService = (SelfServiceService) obj.get("selfServiceService");
			return selfServiceService.processLMSProductPurchase(obj, LMSProducts.WEBINAR_AND_WEBLINK_COURSES);
		}catch(Exception ex){
			LOGGER.info("Exception...LMSProductWebinarAndWeblinkCoursesHandler.java ..... exec().."+ ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean exit(Object dataBundle, Exception errors) throws Exception {
		return false;
	}

}