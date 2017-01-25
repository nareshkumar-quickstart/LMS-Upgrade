package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.lmsapi.LmsApiCustomer;
import com.softech.vu360.lms.webservice.message.lmsapi.types.customer.RegisterCustomer;

public interface CustomerServiceLmsApi {
	
	public LmsApiCustomer findLmsApiCustomerByKey(String apiKey) throws Exception;
	public boolean customerApiValidation(LmsApiCustomer lmsApiCustomer, String customerCode) throws Exception;
	public boolean customerValidation(com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer customer) throws Exception;
	public Map<String, Object> validateAddCustomerRequest(List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer> customerList) throws Exception;
	public Map<String, Object> processCustomersCreation(Distributor distributor, List<com.softech.vu360.lms.webservice.message.lmsapi.types.customer.Customer> customerList) throws Exception;
	public Map<String, Object> processCustomersMap(Distributor distributor, Map<String, Object> customersMap) throws Exception;
	public List<RegisterCustomer> getRegisterCustomerList(Map<String, Object> customersResultMap) throws Exception;

}
