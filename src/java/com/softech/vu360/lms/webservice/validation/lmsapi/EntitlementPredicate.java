package com.softech.vu360.lms.webservice.validation.lmsapi;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.CustomerEntitlement;

public class EntitlementPredicate {

	public static boolean isCustomerEntitlementsExist(List<CustomerEntitlement> customerEntitlementList) {
		return !CollectionUtils.isEmpty(customerEntitlementList);
	}
	
	public static boolean isSeatAvailable(CustomerEntitlement customerEntitlement) {
		boolean seatAvailable = false;
		if (!customerEntitlement.isAllowUnlimitedEnrollments()) {
			if (customerEntitlement.hasAvailableSeats(1)) {
				seatAvailable = true;		
			}
		} else {
			seatAvailable = true;
		}
		return seatAvailable;
	}
	
}
