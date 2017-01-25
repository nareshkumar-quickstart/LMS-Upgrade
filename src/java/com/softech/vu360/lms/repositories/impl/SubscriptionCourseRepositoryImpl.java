package com.softech.vu360.lms.repositories.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import com.softech.vu360.lms.repositories.SubscriptionCourseRepositoryCustom;

public class SubscriptionCourseRepositoryImpl implements SubscriptionCourseRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	public boolean saveSubscriptionCourses(long subscriptionId, long subscriptionKitid) {
		StoredProcedureQuery query;
		int out = 0;
		Object resultSet;
		
		query = entityManager.createNamedStoredProcedureQuery("SurveyCourse.INSERT_SUBSCRIPTION_COURSE");
		query.setParameter("SUBSCRIPTION_ID", subscriptionId);
		query.setParameter("SUBSCRIPTION_KIT_ID", subscriptionKitid);
		query.execute();
		
		resultSet = query.getSingleResult();
		
		if(resultSet != null) {
			out = ((Integer)resultSet).intValue();
		}
		return out > 0 ? true : false;
	}

}
