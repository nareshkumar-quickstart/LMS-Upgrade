package com.softech.vu360.lms.service.impl.lmsapi;


import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.repositories.LmsApiCustomerRepository;
import com.softech.vu360.lms.service.lmsapi.LmsApiCustomerService;

public class LmsApiCustomerServiceImpl implements LmsApiCustomerService {
	
	private static final Logger log = Logger.getLogger(LmsApiCustomerServiceImpl.class.getName());
	
	@Inject
	private LmsApiCustomerRepository lmsApiCustomerRepository ;
	//private LmsApiCustomerService lmsApiCustomerService;
	
	//public void setLmsApiCustomerRepository(LmsApiCustomerRepository lmsApiCustomerRepository) {
		//this.lmsApiCustomerRepository = lmsApiCustomerRepository;
	//}

	//public LmsApiCustomerService getLmsApiCustomerService() {
		//return lmsApiCustomerService;
	//}

	//public void setLmsApiCustomerService(LmsApiCustomerService lmsApiCustomerService) {
		//this.lmsApiCustomerService = lmsApiCustomerService;
	//}

	@Override
	public LmsApiCustomer findLmsApiByCustomerId(long customerId) throws Exception {
		LmsApiCustomer lmsApiCustomer = lmsApiCustomerRepository.findLmsApiByCustomerId(customerId);
		return lmsApiCustomer;
	}

	@Override
	public LmsApiCustomer findApiKey(String key) throws Exception {
		LmsApiCustomer lmsApiCustomer = lmsApiCustomerRepository.findByApiKey(key);
		return lmsApiCustomer;
	}

	@Override
	public LmsApiCustomer addLmsApiCustomer(LmsApiCustomer lmsApiCustomer) throws Exception {
		LmsApiCustomer newLmsApiCustomer = lmsApiCustomerRepository.save(lmsApiCustomer);
		return newLmsApiCustomer;
	}

	@Override
	public boolean customerApiValidation(LmsApiCustomer lmsApiCustomer,String customerCode) throws Exception {
		
		String errorMessage = null;
		if (lmsApiCustomer == null) {
			errorMessage = "No Api Key found. Unauthorized Access";
			throwException(errorMessage);
		}
		
		Customer customer = lmsApiCustomer.getCustomer();
		
		if (customer == null) {
			errorMessage = "No Customer found: " + customerCode;
			throwException(errorMessage);
		}
		
		if (!(customer.getCustomerCode().equals(customerCode))){
			errorMessage = "Invalid customer code: " + customerCode;
			throwException(errorMessage);
		}
		
		boolean isApiEnabled = customer.getLmsApiEnabledTF();
		if (!isApiEnabled) {
			errorMessage = "LMS API is not enable for customer: " + customerCode;
			log.debug(errorMessage);
			throwException(errorMessage);
		}
		
		return true;
	}

	@Override
	public boolean customerApiValidation(String apiKey, String customerCode)throws Exception {
		
		LmsApiCustomer lmsApiCustomer = findApiKey(apiKey);
		return customerApiValidation(lmsApiCustomer, customerCode);
			
	}
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}
	
	

}
