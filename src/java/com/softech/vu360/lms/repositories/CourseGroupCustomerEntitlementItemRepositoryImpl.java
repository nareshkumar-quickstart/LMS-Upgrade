package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.Distributor;

public class CourseGroupCustomerEntitlementItemRepositoryImpl implements
		CourseGroupCustomerEntitlementItemRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	private int batchSize=20;

	@Override
	public List<Map<Object, Object>> getCourseGroupIdsForDistributor(
			Distributor distributor) {
		StringBuilder sb = new StringBuilder("SELECT DISTINCT COURSEGROUP_ID");
		sb.append(" FROM LMS_CONTRACTSEARCH_VIEW ");
		sb.append(" WHERE DISTRIBUTOR_ID=" + distributor.getId());
		sb.append(" ORDER BY COURSEGROUP_ID");

		Query query = entityManager.createNativeQuery(sb.toString());
		query.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);

		List<Map<Object, Object>> lst = query.getResultList();

		return lst;
	}

	@Override
	@Transactional
	public List<CourseGroupCustomerEntitlementItem> bulkSave(List<CourseGroupCustomerEntitlementItem> entities) {
		final List<CourseGroupCustomerEntitlementItem> savedEntities = new ArrayList<CourseGroupCustomerEntitlementItem>(entities.size());
		int i = 0;
		for (CourseGroupCustomerEntitlementItem entity : entities) {
			savedEntities.add(entityManager.merge(entity));
			i++;
			if (i % batchSize == 0) {
				// Flush a batch of inserts and release memory.
				entityManager.flush();
				entityManager.clear();
			}
		}
		return savedEntities;
	}

	@Override
	@Transactional
	public CourseGroupCustomerEntitlementItem saveCGCEI(CourseGroupCustomerEntitlementItem CGCEI){
		entityManager.merge(CGCEI);
		return CGCEI;
	}
}
