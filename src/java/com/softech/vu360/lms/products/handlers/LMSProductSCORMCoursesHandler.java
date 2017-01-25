package com.softech.vu360.lms.products.handlers;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.LMSProducts;
import com.softech.vu360.lms.service.SelfServiceService;

/*
 * This class handler will use for following product
 * LMS – SCORM Courses (Up to 100GB)
 * Product Code = 1003
 */

public class LMSProductSCORMCoursesHandler implements Handler{

	private static final Logger LOGGER = Logger.getLogger(LMSProductSCORMCoursesHandler.class.getName());
	
	@Override
	public boolean init() throws Exception {
		return false;
	}

	@Override
	public boolean exec(Object dataBundle, Exception errors) throws Exception {
		try{
			Hashtable obj = (Hashtable<String, Object>) dataBundle;
			SelfServiceService  selfServiceService = (SelfServiceService) obj.get("selfServiceService");
			return selfServiceService.processLMSProductPurchase(obj, LMSProducts.SCORM_COURSES);
		}catch(Exception ex){
			LOGGER.info("Exception...LMSProductSCORMCoursesHandler.java ..... exec().."+ ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean exit(Object dataBundle, Exception errors) throws Exception {
		return false;
	}

}