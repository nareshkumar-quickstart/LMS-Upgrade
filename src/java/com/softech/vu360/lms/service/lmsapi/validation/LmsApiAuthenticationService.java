package com.softech.vu360.lms.service.lmsapi.validation;

import java.math.BigInteger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;

public interface LmsApiAuthenticationService {

	Customer authenticateByApiKeyAndCustomerCode(String apiKey, String customerCode) throws Exception;
	Distributor authenticateByApiKeyAndResellerId(String apiKey, BigInteger resellerId) throws Exception;
	
}
