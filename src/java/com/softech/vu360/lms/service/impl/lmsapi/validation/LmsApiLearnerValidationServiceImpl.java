package com.softech.vu360.lms.service.impl.lmsapi.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiLearnerValidationService;

@Service
public class LmsApiLearnerValidationServiceImpl implements LmsApiLearnerValidationService {

	private static final Logger log = Logger.getLogger(LmsApiLearnerValidationServiceImpl.class.getName());
	
	@Autowired
	private VU360UserService vu360UserService;
	
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	@Override
	public Learner getValidLearner(String userName, String customerCode) throws Exception {
		log.debug("getValidLearner() start");
		
		String errorMessage = null;
		
		if (StringUtils.isEmpty(userName) && StringUtils.isBlank(userName)) {
			errorMessage =  "UserId can not be empty or blank" ;
			throwException(errorMessage);
		}
				
		VU360User vu360User = vu360UserService.findUserByUserName(userName);
		if (vu360User == null) {
			errorMessage =  userName + " not found for customer: " + customerCode ;
			throwException(errorMessage);
		}
				
		
		if (!vu360User.getAccountNonExpired()) {
			errorMessage =  userName + " account is expired" ;
			throwException(errorMessage);
		}
		
		if (!vu360User.getAccountNonLocked()) {
			errorMessage =  userName + " account is locked";
			throwException(errorMessage);
		}
		
		if (!vu360User.getEnabled()) {
			errorMessage =  userName + " is not enabled";
			throwException(errorMessage);
		}
		
		Learner learner = vu360User.getLearner();
		String learnerCustomerCode = learner.getCustomer().getCustomerCode();
				
		// Check whether customer has these learners or not
		if (!customerCode.equalsIgnoreCase(learnerCustomerCode)) {
			errorMessage = "UserId: " + userName + " not found for customer: " + customerCode ;
			throwException(errorMessage);			
		}	 
		
		log.debug("getValidLearner() end");
		
	    return learner;	
	}
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}

}
