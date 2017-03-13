package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.VU360User;


/**
 * 
 * @author haider
 *
 */
public interface CustomerEntitlementRepositoryCustom {

	
	public boolean isCustomerEntitlementExistByName(String customerEntitlementName) ;
	public void deleteCustomerEntitlement(CustomerEntitlement objCustomerEntitlement);
	public List<CustomerEntitlement> findCustomerIdAndAllowSelfEnrollmentTrue(Learner learner);
	public List<CustomerEntitlement> getSimilarCustomerEntitlements(Long customerEntitlementId, Long totalEnrollments) ;
	public void removeEntitlementsWithEnrollments(Customer customer, CustomerEntitlement customerEntitlement) ;

	public List<CustomerEntitlement> getCustomerEntitlementsByCutomer(Customer customer);

}
