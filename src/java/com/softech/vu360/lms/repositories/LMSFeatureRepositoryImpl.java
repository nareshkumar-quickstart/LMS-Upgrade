package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.LMSFeature;

/**
 * 
 * @author raja.ali
 * @create date 15-July-2016
 *  
 */
public class LMSFeatureRepositoryImpl implements LMSFeatureRepositoryCustom {

	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<LMSFeature> findAllActiveLMSFeaturesByUser(Long loggedInUserId, Long customerId, String roleType) {
		
		String queryString = "select LMSFEATURE.* from LMSROLELMSFEATURE lmsRoleFeature "
				+ "inner join  VU360USER_ROLE userRole on userRole.role_id = lmsRoleFeature.lmsrole_id "
				+ "inner join  lmsrole role on role.id = userRole.role_id "
				+ "inner join  LMSFEATURE LMSFEATURE on LMSFEATURE.id = lmsRoleFeature.LMSFEATURE_id "
				+ "where lmsRoleFeature.ENABLEDTF = 1 and userRole.user_id = "+loggedInUserId + " and role.role_type = '"+roleType+"' "
				+ "and role.customer_id="+customerId +" "
				+ "order by LMSFEATURE.featurecode ASC ";
		
		Query query = entityManager.createNativeQuery(queryString, LMSFeature.class);
		
		List<LMSFeature> lmsFeatureList = query.getResultList(); 
		
		return lmsFeatureList;
	}

		
}
