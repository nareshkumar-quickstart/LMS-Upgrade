package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.RegulatorCategory;

public class RegulatorCategoryRepositoryImpl implements RegulatorCategoryRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;



	@Override
	public List<RegulatorCategory> findByCriteria(String categoryType, String categoryName, Long regulatorId) {
		StringBuilder queryString = new StringBuilder("SELECT c FROM RegulatorCategory c WHERE c.displayName LIKE :displayName ");
		
		if (categoryType!=null && categoryType.trim().length() > 0){
			queryString.append(" AND c.categoryType LIKE :categoryType ");
		}
		if(regulatorId!=null && regulatorId>0){
			queryString.append(" AND c.regulator.id =:regulatorId ");
		}
			
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("displayName", "%" + categoryName + "%");
		
		if (categoryType!=null && categoryType.trim().length() > 0){
			query.setParameter("categoryType", categoryType);
		}
		
		if(regulatorId!=null && regulatorId>0){
			query.setParameter("regulatorId", regulatorId);
		}
		
		List<RegulatorCategory> listRegulatorCategory = query.getResultList(); 
		
		return listRegulatorCategory;
	}

	

	@Override
	public List<RegulatorCategory> findByCategoryTypeCategoryNameRegulatorId(String categoryType, String categoryName, Long regulatorId) {
		StringBuilder queryString = new StringBuilder("SELECT c FROM RegulatorCategory c WHERE c.regulator.id =:regulatorId ");
		
		if (!StringUtils.isBlank(categoryName)){
			queryString.append(" AND c.displayName LIKE :displayName ");
		}
		if(!StringUtils.isBlank(categoryType)){
			queryString.append(" AND c.categoryType LIKE :categoryType ");
		}
			
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("regulatorId", regulatorId);
		
		if (!StringUtils.isBlank(categoryType)){
			query.setParameter("categoryType", "%" + categoryType + "%");
		}
		
		if(!StringUtils.isBlank(categoryName)){
			query.setParameter("displayName", "%" + categoryName + "%");
		}
		
		List<RegulatorCategory> listRegulatorCategory = query.getResultList(); 
		
		return listRegulatorCategory;
	}

	
	/**
	 * SELECT COUNT(t0.ID) FROM REGULATORCATEGORY t0, REGULATOR t1 WHERE ((((t0.CATEGORYTYPE = ?) AND (t1.ID = ?)) AND (t0.ID <> ?)) AND (t1.ID = t0.REGULATOR_ID))
	 */
	@Override
	public boolean isRegulatorCategoryTypeAlreadyAssociatedWithRegulator(Long regulatorId, String regulatorCategoryType, Long excludeCategoryId) {

		StringBuilder queryString = new StringBuilder("SELECT count(c.id) FROM RegulatorCategory c WHERE c.categoryType=:categoryType AND c.regulator.id =:regulatorId ");
	
		
		if(excludeCategoryId > 0){
			queryString.append(" AND c.id <>:excludeCategoryId ");
		}
			
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("categoryType", regulatorCategoryType);
		query.setParameter("regulatorId", regulatorId);
		
		if (excludeCategoryId > 0){
			query.setParameter("excludeCategoryId", excludeCategoryId);
		}
		
		Long count = (Long)query.getSingleResult();
		
		return count.longValue()>0;
		
	}

	

	

}
