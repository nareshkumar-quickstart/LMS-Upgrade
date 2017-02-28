package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;

/**
 * 
 * 
 * @author haider
 *
 */
public class OrgGroupEntitlementRepositoryImpl implements OrgGroupEntitlementRepositoryCustom {
	
	private OrgGroupEntitlementRepository orgGroupEntitlementRespository;
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	@Deprecated
	public List<OrgGroupEntitlement> getOrgGroupEntitlemnetsForLearner(Long learnerId) {
		String queryString = "select oge.* from ORGANIZATIONALGROUPENTITLEMENT oge "
				+ " inner join LEARNER_ORGANIZATIONALGROUP lorg on lorg.ORGANIZATIONALGROUP_ID = oge.ORGANIZATIONALGROUP_ID "
				+ " where oge.ALLOWSELFENROLLMENTTF=1 and oge.SEATS>=0 and lorg.LEARNER_ID = "+learnerId;
		Query query = entityManager.createNativeQuery(queryString, OrgGroupEntitlement.class);
		List<OrgGroupEntitlement> listOrgGroupEntitlement = (List<OrgGroupEntitlement>) query.getResultList();
		return listOrgGroupEntitlement;
	}

	@Override
	public List<OrgGroupEntitlement> getAvailableOrgGroupEntitlementsOfLearner(Learner learner, List<OrganizationalGroup> orgGroups) {
		List<OrgGroupEntitlement> ls=null;
		if(orgGroups!=null && !orgGroups.isEmpty()){
			ls = orgGroupEntitlementRespository.findByMaxNumberSeatsGreaterThanEqualAndAllowSelfEnrollmentTrueAndOrganizationalGroupIdIn(0l, orgGroups);
		}else{
			ls = orgGroupEntitlementRespository.findByMaxNumberSeatsGreaterThanEqualAndAllowSelfEnrollmentTrue(0l); 
		}
		return ls;
	}
	
}
