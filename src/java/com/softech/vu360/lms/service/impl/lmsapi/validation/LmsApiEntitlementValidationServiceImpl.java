package com.softech.vu360.lms.service.impl.lmsapi.validation;

import static com.softech.vu360.lms.webservice.validation.lmsapi.EntitlementPredicate.isCustomerEntitlementsExist;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.service.lmsapi.LmsApiEntitlementService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiEntitlementValidationService;

@Service
public class LmsApiEntitlementValidationServiceImpl implements LmsApiEntitlementValidationService {

	@Autowired
	private LmsApiEntitlementService lmsApiEntitlementService;
	
	public void setLmsApiEntitlementService(LmsApiEntitlementService lmsApiEntitlementService) {
		this.lmsApiEntitlementService = lmsApiEntitlementService;
	}

	@Override
	public boolean isValidCourseEntitlement(Customer customer, Long courseId, Date todayDate) {
		
		Map<Boolean, List<CustomerEntitlement>> seatsAvailableEntitlementsMap = lmsApiEntitlementService.getSeatAvailableEntitlementsMap(customer, courseId);
		return isValidCourseEntitlement(seatsAvailableEntitlementsMap, todayDate);
	}
	
	@Override
	public boolean isValidCourseEntitlement(Map<Boolean, List<CustomerEntitlement>> seatsAvailableEntitlementsMap, Date todayDate) {
		
		boolean validCourseEntitlement = false;
		if (!CollectionUtils.isEmpty(seatsAvailableEntitlementsMap)) {
			List<CustomerEntitlement> seatsAvailableCustomerEntitlements  = seatsAvailableEntitlementsMap.get(Boolean.TRUE);
			if (!CollectionUtils.isEmpty(seatsAvailableCustomerEntitlements)) {
				List<Date> entitlementStartDates = lmsApiEntitlementService.getEntitlementStartDates(seatsAvailableCustomerEntitlements, todayDate);
				List<Date> entitlementEndDates = lmsApiEntitlementService.getEntitlementEndDates(seatsAvailableCustomerEntitlements, todayDate);
				if (!CollectionUtils.isEmpty(entitlementStartDates) && !CollectionUtils.isEmpty(entitlementEndDates)) {
					validCourseEntitlement = true;
				}	
			}
		}
		
		return validCourseEntitlement;
	}
	
	@Override
	public String getEntitlementLevelErrorMessage(String customerCode, String courseGuid, List<CustomerEntitlement> customerEntitlements, Date todayDate) {
		
		String errorMessage = "";
		if (!isCustomerEntitlementsExist(customerEntitlements)) {
			errorMessage = "No entitlements found for course: " + courseGuid;
		} else {
			Map<Boolean, List<CustomerEntitlement>> seatsAvailableEntitlementsMap = lmsApiEntitlementService.getSeatAvailableEntitlementsMap(customerEntitlements);
			if (!CollectionUtils.isEmpty(seatsAvailableEntitlementsMap)) {
				List<CustomerEntitlement> seatsAvailableCustomerEntitlements  = seatsAvailableEntitlementsMap.get(Boolean.TRUE);
				if (!CollectionUtils.isEmpty(seatsAvailableCustomerEntitlements)) {
					List<Date> entitlementStartDates = lmsApiEntitlementService.getEntitlementStartDates(seatsAvailableCustomerEntitlements, todayDate);
					List<Date> entitlementEndDates = lmsApiEntitlementService.getEntitlementEndDates(seatsAvailableCustomerEntitlements, todayDate);
					if (CollectionUtils.isEmpty(entitlementStartDates)) {
						errorMessage = "No entitlement start dates found for course: " + courseGuid;
					} else if (CollectionUtils.isEmpty(entitlementEndDates)) {
						errorMessage = "No entitlement end dates found for course: " + courseGuid;
					}
				} else {
					errorMessage = "No seats available in any entitlement for customer: " + customerCode;
				}
			} else {
				errorMessage = "No seats available in any entitlement for customer: " + customerCode;
			}
		}
		return errorMessage;
	}
	
}
