package com.softech.vu360.lms.products.handlers;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.LMSProducts;
import com.softech.vu360.lms.service.SelfServiceService;

/*
 * This class handler will use for following product
 * LMS – Instructors for Classroom Training
 * Product Code = 1002
 */

public class LMSProductInstructorsForClassroomTrainingHandler implements Handler{

	private static final Logger LOGGER = Logger.getLogger(LMSProductInstructorsForClassroomTrainingHandler.class.getName());
	
	@Override
	public boolean init() throws Exception {
		return false;
	}

	@Override
	public boolean exec(Object dataBundle, Exception errors) throws Exception {
		try{
			Hashtable obj = (Hashtable<String, Object>) dataBundle;
			SelfServiceService  selfServiceService = (SelfServiceService) obj.get("selfServiceService");
			return selfServiceService.processLMSProductPurchase(obj, LMSProducts.INSTRUCTORS_FOR_CLASSROOM_TRAINING);
		}
		catch(Exception e){
			LOGGER.debug(e);
			return false;
		}
	}

	@Override
	public boolean exit(Object dataBundle, Exception errors) throws Exception {
		return false;
	}


}