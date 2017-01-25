package com.softech.vu360.lms.service.lmsapi.validation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;

public interface LmsApiEntitlementValidationService {

	boolean isValidCourseEntitlement(Customer customer, Long courseId, Date todayDate);
	boolean isValidCourseEntitlement(Map<Boolean, List<CustomerEntitlement>> seatsAvailableEntitlementsMap, Date todayDate);
	String getEntitlementLevelErrorMessage(String customerCode, String courseGuid, List<CustomerEntitlement> customerEntitlements, Date todayDate);
	
}
