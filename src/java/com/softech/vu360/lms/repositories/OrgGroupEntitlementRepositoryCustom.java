package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;


/**
 * 
 * @author haider
 *
 */
public interface OrgGroupEntitlementRepositoryCustom {

	
	public List<OrgGroupEntitlement> getAvailableOrgGroupEntitlementsOfLearner(Learner learner,List<OrganizationalGroup> orgGroups);
	//public void deleteCustomerEntitlement( CustomerEntitlement objCustomerEntitlement);
	@Deprecated
	public List<OrgGroupEntitlement> getOrgGroupEntitlemnetsForLearner(Long learnerId);
}
