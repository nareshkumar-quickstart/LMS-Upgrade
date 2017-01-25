package com.softech.vu360.lms.service.impl.lmsapi.validation;

import java.math.BigInteger;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.model.lmsapi.LmsApiDistributor;
import com.softech.vu360.lms.repositories.LmsApiCustomerRepository;
import com.softech.vu360.lms.repositories.LmsApiDistributorRepository;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiAuthenticationService;

@Service
public class LmsApiAuthenticationServiceImpl implements LmsApiAuthenticationService {

	private static final Logger log = Logger.getLogger(LmsApiAuthenticationServiceImpl.class.getName());

	@Inject
	private LmsApiCustomerRepository lmsApiCustomerRepository;

	@Inject
	private LmsApiDistributorRepository lmsApiDistributorRepository;

	@Override
	public Customer authenticateByApiKeyAndCustomerCode(String apiKey, String customerCode) throws Exception {

		LmsApiCustomer lmsApiCustomer = lmsApiCustomerRepository.findByApiKey(apiKey);
		validateLmsApiCustomer(lmsApiCustomer, customerCode);
		Customer customer = lmsApiCustomer.getCustomer();
		return customer;

	}

	@Override
	public Distributor authenticateByApiKeyAndResellerId(String apiKey, BigInteger resellerId) throws Exception {

		LmsApiDistributor lmsApiDistributor = lmsApiDistributorRepository.findByApiKey(apiKey);
		validateLmsApiDistributor(lmsApiDistributor, resellerId.longValue());
		Distributor distributor = lmsApiDistributor.getDistributor();
		return distributor;
	}

	public boolean validateLmsApiCustomer(LmsApiCustomer lmsApiCustomer, String customerCode) throws Exception {

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

		if (!(customer.getCustomerCode().equals(customerCode))) {
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

	private boolean validateLmsApiDistributor(LmsApiDistributor lmsApiDistributor, Long resellerId) throws Exception {

		String errorMessage = null;

		if (lmsApiDistributor == null) {
			errorMessage = "No Api Key found. Unauthorized Access";
			throwException(errorMessage);
		}

		Distributor distributor = lmsApiDistributor.getDistributor();
		if (distributor == null) {
			errorMessage = "No Reseller found for reseller Id: " + resellerId;
			throwException(errorMessage);
		}

		if (!(distributor.getId().equals(resellerId))) {
			errorMessage = "Invalid reseller Id: " + resellerId;
			throwException(errorMessage);
		}

		boolean isApiEnabled = distributor.getLmsApiEnabledTF();
		if (!isApiEnabled) {
			errorMessage = "LMS API is not enable for resellerId: " + distributor.getId();
			log.debug(errorMessage);
			throwException(errorMessage);
		}

		return true;
	}

	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}

}
