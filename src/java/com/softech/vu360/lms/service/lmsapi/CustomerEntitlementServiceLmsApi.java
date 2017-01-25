package com.softech.vu360.lms.service.lmsapi;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;

public interface CustomerEntitlementServiceLmsApi {

	public Map<String, Object> getCustomerEntitlementHavingMaximumEntitlementEndDate(
			List<CustomerEntitlement> customerEntitlementList, Date enrollmentStartDate, Date enrollmentEndDate) throws Exception;
	
	public List<CustomerEntitlement> getValidCustomerEntitlementList(Customer customer, com.softech.vu360.lms.model.Course course, 
			Date enrollmentStartDate, Date enrollmentEndDate) throws Exception;
	
	public List<CustomerEntitlement> getCustomerEntitlementListForCourse(Customer customer, com.softech.vu360.lms.model.Course course);
			
}
