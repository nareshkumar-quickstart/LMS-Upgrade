package com.softech.vu360.lms.repositories;

import java.util.Arrays;
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

	@Override
	public String getAnyEnabledFeatureCodeInDisplayOrderByRoleType(Long userId,String roleType, List<String> disabledFeatureCode) {
		String featureCode=null;
		StringBuilder sql = new StringBuilder("select top 1 f.featurecode from vu360user u, learner l, vu360user_role ur, lmsrole r, lmsrolelmsfeature rf, lmsfeature f");
		sql.append(" where u.id ="+userId+ " and l.VU360USER_ID = u.id and ur.USER_ID = u.ID");
		sql.append(" and (r.id = ur.role_id and r.customer_id = l.customer_id and r.ROLE_TYPE ='"+roleType+"')");
		sql.append(" and (rf.LMSROLE_ID = r.id and rf.ENABLEDTF = 1)");
		sql.append(" and (f.id = rf.LMSFEATURE_ID");
		if(!disabledFeatureCode.isEmpty()){
			
			StringBuilder subQueryBuilder = new StringBuilder();
			
			subQueryBuilder.append(" and f.FEATURECODE not in (");
			
			for (String code : disabledFeatureCode) {
				subQueryBuilder.append("'").append(code).append("'").append(",");
			}
			
			subQueryBuilder.deleteCharAt(subQueryBuilder.length() - 1);
			subQueryBuilder.append(")");
			sql.append(subQueryBuilder);
		}
		
		sql.append(" ) order by DISPLAYORDER");		
		
		Query query = entityManager.createNativeQuery(sql.toString());
		
		Object result = query.getSingleResult();
		
		if(result!=null){
			featureCode = result.toString();
		}
	
		return featureCode;
	}

		
}
