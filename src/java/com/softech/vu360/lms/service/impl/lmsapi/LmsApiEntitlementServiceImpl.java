package com.softech.vu360.lms.service.impl.lmsapi;

import static com.softech.vu360.lms.webservice.validation.lmsapi.EntitlementPredicate.isCustomerEntitlementsExist;
import static com.softech.vu360.lms.webservice.validation.lmsapi.EntitlementPredicate.isSeatAvailable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.lmsapi.LmsApiEntitlementService;

@Service
public class LmsApiEntitlementServiceImpl implements LmsApiEntitlementService {

	@Autowired
	private EntitlementService entitlementService;
	
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	
	@Override
	public Map<Boolean, List<CustomerEntitlement>> getSeatAvailableEntitlementsMap(Customer customer, Long courseId) {
		List<CustomerEntitlement> customerEntitlementList = entitlementService.getCustomerEntitlementsByCourseId(customer, courseId);
		return getSeatAvailableEntitlementsMap(customerEntitlementList);
	}

	@Override
	public Map<Boolean, List<CustomerEntitlement>> getSeatAvailableEntitlementsMap(List<CustomerEntitlement> customerEntitlementList) {
		Map<Boolean, List<CustomerEntitlement>> seatsAvailableEntitlementsMap = null;
		List<CustomerEntitlement> seatsAvailableCustomerEntitlements = new ArrayList<>();
		List<CustomerEntitlement> seatsNotAvailableCustomerEntitlements = new ArrayList<>();
		if (isCustomerEntitlementsExist(customerEntitlementList)) {
			seatsAvailableEntitlementsMap = new HashMap<>();
			for (CustomerEntitlement ent : customerEntitlementList ) {	
				boolean seatAvailable = isSeatAvailable(ent);
				if (seatAvailable) {
					seatsAvailableCustomerEntitlements.add(ent);
				} else {
					seatsNotAvailableCustomerEntitlements.add(ent);
				}
			} //end of for()
			
			
			if (!CollectionUtils.isEmpty(seatsAvailableCustomerEntitlements)) {
				seatsAvailableEntitlementsMap.put(Boolean.TRUE, seatsAvailableCustomerEntitlements);
			}
			
			if (!CollectionUtils.isEmpty(seatsNotAvailableCustomerEntitlements)) {
				seatsAvailableEntitlementsMap.put(Boolean.FALSE, seatsNotAvailableCustomerEntitlements);
			}
		} //end of if
		
		return seatsAvailableEntitlementsMap;
	}

	@Override
	public Date getEntitlementStartDate(CustomerEntitlement ent, Date enrollmentStartDate) {
		Date entitlementStartDate = null;
		if (ent.getEndDate() != null && !ent.getEndDate().before(enrollmentStartDate)) {
			entitlementStartDate = ent.getStartDate();
		} else {
			Date entEndDate = getEntitlementEndDate(ent);
			if(!entEndDate.before(enrollmentStartDate)){ 
				entitlementStartDate = ent.getStartDate();
			}
		}
		return entitlementStartDate;
	}

	@Override
	public Date getEntitlementEndDate(CustomerEntitlement ent, Date enrollmentStartDate) {
		Date entitlementEndDate = null;
		if (ent.getEndDate() != null && !ent.getEndDate().before(enrollmentStartDate)) {
			entitlementEndDate = ent.getEndDate();
		} else {
			Date entEndDate = getEntitlementEndDate(ent);
			if(!entEndDate.before(enrollmentStartDate)){ 
				entitlementEndDate = entEndDate;
			}
		}
		return entitlementEndDate;
	}

	@Override
	public List<Date> getEntitlementStartDates(List<CustomerEntitlement> customerEntitlements, Date enrollmentStartDate) {
		List<Date> entitlementStartDates = null;
		if (!CollectionUtils.isEmpty(customerEntitlements)) {
			entitlementStartDates = new ArrayList<>();
			for (CustomerEntitlement ent : customerEntitlements ) {
				Date entitlementStartDate = getEntitlementStartDate(ent, enrollmentStartDate);
				if (entitlementStartDate != null) {
					entitlementStartDates.add(entitlementStartDate);
				}
			}
		}
		return entitlementStartDates;
	}

	@Override
	public List<Date> getEntitlementEndDates(List<CustomerEntitlement> customerEntitlements, Date enrollmentStartDate) {
		List<Date> entitlementEndDates = null;
		if (!CollectionUtils.isEmpty(customerEntitlements)) {
			entitlementEndDates = new ArrayList<>();
			for (CustomerEntitlement ent : customerEntitlements ) {
				Date entitlementEndDate = getEntitlementEndDate(ent, enrollmentStartDate);
				if (entitlementEndDate != null) {
					entitlementEndDates.add(entitlementEndDate);
				}
			}
		}
		return entitlementEndDates;
	}
	
	@Override
	public CustomerEntitlement getCustEntitlementWithMaximumEntitlementEndDate(Customer customer, Long courseId) {
		CustomerEntitlement custEntitlementWithMaximumEntitlementEndDate = null;
		Map<Boolean, List<CustomerEntitlement>> seatAvailableEntitlementsMap = getSeatAvailableEntitlementsMap(customer, courseId);
		if (!CollectionUtils.isEmpty(seatAvailableEntitlementsMap)) {
			List<CustomerEntitlement> validSeatAvailableCustomerEntitlements  = seatAvailableEntitlementsMap.get(Boolean.TRUE);
			if (!CollectionUtils.isEmpty(validSeatAvailableCustomerEntitlements)) {
				Date maxEntitlementEndDate = null;
				//Get that entitlement that has maximum entitlement end date
				for( CustomerEntitlement ent : validSeatAvailableCustomerEntitlements ) {
					if (maxEntitlementEndDate == null) {
					    maxEntitlementEndDate = ent.getEntitlementEndDate();
					    custEntitlementWithMaximumEntitlementEndDate = ent;
					} else {
						if (ent.getEntitlementEndDate().after(maxEntitlementEndDate)) {
							maxEntitlementEndDate = ent.getEntitlementEndDate();
							custEntitlementWithMaximumEntitlementEndDate = ent;
						}
					}		
				} //end of for()
			}
		}
		return custEntitlementWithMaximumEntitlementEndDate;
	}

	private Date getEntitlementEndDate(CustomerEntitlement ent) {
		Calendar c = Calendar.getInstance();
		c.setTime(ent.getStartDate()); // Now use entitlement start date.
		c.add(Calendar.DATE, ent.getDefaultTermOfServiceInDays()); // Adding days
		Date entEndDate = c.getTime();
		return entEndDate;
	}

}
