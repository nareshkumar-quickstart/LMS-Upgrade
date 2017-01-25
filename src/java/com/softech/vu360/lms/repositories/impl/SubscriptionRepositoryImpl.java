package com.softech.vu360.lms.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.softech.vu360.lms.repositories.SubscriptionRepositoryCustom;

public class SubscriptionRepositoryImpl implements SubscriptionRepositoryCustom{
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	public List<Map<Object, Object>> findByIdByLearnerIdByUserIdByCourseName(Long learnerId, Long userId, Long subscriptionId,String coursesearch){
		
		String sqlQuery = null;
		
		if(coursesearch!= null){
		sqlQuery = "SELECT S.SUBSCRIPTION_NAME, S.ID, SC.COURSE_ID , " +
                          "C.NAME AS 'COURSENAME' , C.COURSETYPE , "+
                          "(SELECT TOP 1 LE.ID FROM LEARNERENROLLMENT LE WHERE LE.COURSE_ID = SC.COURSE_ID AND LE.LEARNER_ID = :learnerId ) AS 'ENROLLMENTID'"+
                          " FROM SUBSCRIPTION S, SUBSCRIPTION_COURSE SC, SUBSCRIPTION_USER SU, COURSE C"+
                          " WHERE S.ID = SC.SUBSCRIPTION_ID AND S.ID = SU.SUBSCRIPTION_ID AND SC.SUBSCRIPTION_ID = SU.SUBSCRIPTION_ID AND SC.COURSE_ID = C.ID AND C.NAME LIKE :CourseName AND S.ID = :subscriptionId  AND SU.VU360USER_ID = :userId ";
		}else{
		sqlQuery = "SELECT S.SUBSCRIPTION_NAME, S.ID, SC.COURSE_ID , " +
                "C.NAME AS 'COURSENAME' , C.COURSETYPE , "+
                "(SELECT TOP 1 LE.ID FROM LEARNERENROLLMENT LE WHERE LE.COURSE_ID = SC.COURSE_ID AND LE.LEARNER_ID = :learnerId) AS 'ENROLLMENTID'"+
                " FROM SUBSCRIPTION S, SUBSCRIPTION_COURSE SC, SUBSCRIPTION_USER SU, COURSE C"+
                " WHERE S.ID = SC.SUBSCRIPTION_ID AND S.ID = SU.SUBSCRIPTION_ID AND SC.SUBSCRIPTION_ID = SU.SUBSCRIPTION_ID AND SC.COURSE_ID = C.ID AND S.ID = :subscriptionId AND SU.VU360USER_ID = :userId ";
		}

		Query query = entityManager.createNativeQuery(sqlQuery.toString());
		
		query.setParameter("learnerId", learnerId);
		query.setParameter("subscriptionId", subscriptionId);
		query.setParameter("userId", userId);
		
		if(coursesearch != null)
			query.setParameter("CourseName", "%"+ coursesearch.trim().replace("'","''") +"%" );
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();  
		List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		mapList.addAll(rows);
 		return mapList;
	}
	
	
	@Override
	public List<Map<Object, Object>> findByUserId(Long userId){
		StringBuilder SQL = new StringBuilder( "SELECT S.ID, S.SUBSCRIPTION_NAME FROM SUBSCRIPTION_USER SU, SUBSCRIPTION S WHERE "); 
		SQL.append(" UPPER(S.SUBSCRIPTION_STATUS) IN ('ACTIVE', 'ONHOLD') AND S.ID = SU.SUBSCRIPTION_ID ") ;
		SQL.append(" AND SU.VU360USER_ID = :userId  GROUP BY S.ID, S.SUBSCRIPTION_NAME ORDER BY S.SUBSCRIPTION_NAME ASC" );
		
		Query query = entityManager.createNativeQuery(SQL.toString());
		query.setParameter("userId", userId);
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();  
		List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		mapList.addAll(rows);
 		return mapList;
	}
}
