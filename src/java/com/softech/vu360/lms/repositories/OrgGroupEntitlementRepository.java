package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;


/**
 * 
 * @author haider.ali
 *
 */
public interface OrgGroupEntitlementRepository extends CrudRepository<OrgGroupEntitlement, Long>, OrgGroupEntitlementRepositoryCustom {
	
	public List<OrgGroupEntitlement> findByIdAndMaxNumberSeatsGreaterThan(Customer customer, Long maxSeats);
	public OrgGroupEntitlement findById(CustomerEntitlement customerEntitlement);
	public OrgGroupEntitlement findById(Long Id);
	//public List<OrgGroupEntitlement> findAll(List<Long> ids);
	public List<OrgGroupEntitlement> findByMaxNumberSeatsGreaterThanAndAllowSelfEnrollmentTrue(Long maxNumberSeatsGreaterThan);
	public List<OrgGroupEntitlement> findByMaxNumberSeatsGreaterThanAndAllowSelfEnrollmentTrueAndOrganizationalGroupIdIn(Long maxNumberSeatsGreaterThan,  List<OrganizationalGroup> orgGroupIds);
	public List<OrgGroupEntitlement> findByCustomerEntitlementId(Long id);
	public List<OrgGroupEntitlement> findByCustomerEntitlementIn(List<OrgGroupEntitlement> orgGroupEntitlementList);
	public OrgGroupEntitlement findFirstByOrganizationalGroupId(Long id);
}
