package com.softech.vu360.lms.service.lmsapi;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;

public interface LmsApiEntitlementService {

	Map<Boolean, List<CustomerEntitlement>> getSeatAvailableEntitlementsMap(Customer customer, Long courseId);
	Map<Boolean, List<CustomerEntitlement>> getSeatAvailableEntitlementsMap(List<CustomerEntitlement> customerEntitlementList);
	Date getEntitlementStartDate(CustomerEntitlement ent, Date enrollmentStartDate);
	Date getEntitlementEndDate(CustomerEntitlement ent, Date enrollmentStartDate);
	List<Date> getEntitlementStartDates(List<CustomerEntitlement> customerEntitlements, Date enrollmentStartDate);
	List<Date> getEntitlementEndDates(List<CustomerEntitlement> customerEntitlements, Date enrollmentStartDate);
	CustomerEntitlement getCustEntitlementWithMaximumEntitlementEndDate(Customer customer, Long courseId);
	
}
