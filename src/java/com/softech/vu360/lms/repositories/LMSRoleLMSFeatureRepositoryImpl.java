package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.LMSRoleLMSFeature;

/**
 * 
 * @author raja.ali
 * @create date 15-July-2016
 *  
 */
public class LMSRoleLMSFeatureRepositoryImpl implements LMSRoleLMSFeatureRepositoryCustom {

	
	@PersistenceContext
	protected EntityManager entityManager;


	@Override
	public List<LMSRoleLMSFeature> findLMSRoleLMSFeatureByUser(Long loggedInUserId, Long customerId, String roleType) {

		String queryString = "select lmsRoleFeature.* from LMSROLELMSFEATURE lmsRoleFeature "
				+ "inner join  VU360USER_ROLE userRole on userRole.role_id = lmsRoleFeature.lmsrole_id "
				+ "inner join  lmsrole role on role.id = userRole.role_id "
				+ "inner join  LMSFEATURE LMSFEATURE on LMSFEATURE.id = lmsRoleFeature.LMSFEATURE_id "
				+ "where userRole.user_id = "+loggedInUserId + " and role.role_type = '"+roleType+"' "
				+ "and role.customer_id="+customerId +" "
				+ "order by LMSFEATURE.featurecode ASC ";
		
		Query query = entityManager.createNativeQuery(queryString, LMSRoleLMSFeature.class);
		List<LMSRoleLMSFeature> lmsFeatureList = query.getResultList(); 
		return lmsFeatureList;
	}


	@Override
	public boolean isAllowedLearnerDashboard(Long userId) {
		
		try {
			String queryString = "select LRLF.* from LMSRoleLMSFeature LRLF "
					+ "inner join VU360USER_ROLE UR on UR.ROLE_ID = LRLF.LMSROLE_ID "
					+ "inner join LMSFEATURE LF on LF.ID = LRLF.LMSFEATURE_ID "
					+ "where UR.USER_ID ="+userId+" AND LF.FEATURECODE = 'LMS-LRN-0007' ";
			
			Query query = entityManager.createNativeQuery(queryString, LMSRoleLMSFeature.class);
			List<LMSRoleLMSFeature> lmsFeatureList = query.getResultList();
			
			if(lmsFeatureList!=null && lmsFeatureList.size()>0)
				return Boolean.TRUE;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

		
}
