package com.softech.vu360.lms.service.lmsapi.validation;

import com.softech.vu360.lms.model.Learner;

public interface LmsApiLearnerValidationService {

	Learner getValidLearner(String userName, String customerCode) throws Exception;
	
}
