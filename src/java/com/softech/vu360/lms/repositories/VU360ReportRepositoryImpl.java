package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

public class VU360ReportRepositoryImpl implements VU360ReportRepositoryCustom{
	
		@PersistenceContext
		protected EntityManager entityManager;
	
		public List<Map<Object, Object>> executeSqlString(String sqlString) {
			
			StringBuilder queryString = new StringBuilder(sqlString.toString());
			Query query = entityManager.createNativeQuery(queryString.toString());
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List rows = query.getResultList();  
			List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
			mapList.addAll(rows);
			return mapList;
	}
		
		
		public List<Map<Object, Object>> executeSqlString(String sqlString, List<Object> params) {
			StringBuilder queryString = new StringBuilder(sqlString.toString());
			Query query = entityManager.createNativeQuery(queryString.toString());
			int paramCounter=1;
			for(Object obj : params){
				
				query.setParameter(paramCounter++, obj);
				
			}
			//query.setpara
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List rows = query.getResultList();  
			List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
			mapList.addAll(rows);
			return mapList;
			
			// TopLink loves Vectors!!
		/*	Vector<Object> sqlParams = new Vector<Object>(params);
			Vector<Object> paramTypes = new Vector<Object>(params.size());
			for (int i=0; i<params.size(); i++) {
				//paramTypes.add(params.get(i).getClass());
				paramTypes.add(SQLCall.LITERAL);
			}

			SQLCall call = new SQLCall(sqlString.toString());
			call.setParameters(sqlParams);
			call.setParameterTypes(paramTypes);

			DataReadQuery drq = new DataReadQuery(call);
			drq.setShouldBindAllParameters(true);
			drq.setShouldPrepare(true);
			Session session = sessionFactory.createSession();
			List<Map<Object, Object>> results = (List<Map<Object, Object>>)session.executeQuery(drq);
			session.release();
	*/
			
		}	

}
