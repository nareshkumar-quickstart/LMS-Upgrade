package com.softech.vu360.lms.repositories.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.repositories.TrainingPlanRepositoryCustom;

public class TrainingPlanRepositoryImpl implements TrainingPlanRepositoryCustom{
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<Map<Object, Object>> getTrainingPlansForCourseCatalog(long id, String search) {
		
		
		StringBuffer strBuffSQL= new StringBuffer();
		
		strBuffSQL.append("EXEC [SP_COURSECATALOGWITHTRAININGPLANS] ");
		strBuffSQL.append("[").append(id).append("]");
		strBuffSQL.append(", ");
		strBuffSQL.append("null");
		if (StringUtils.isNotBlank(search) && !search.equals("Search")) { 
			strBuffSQL.append(", ");
			strBuffSQL.append("[").append(search).append("]");
		}
		
		Query qry=entityManager.createNativeQuery(strBuffSQL.toString());
		List<Object[]> lst = qry.getResultList();
		List<Map<Object,Object>> listMap = new ArrayList();
		Map<Object,Object> map = new HashMap();
		for(Object[] obj : lst){
			map.put("CUSTOMERENTITLEMENT_ID", obj[0]);
			map.put("ENROLLMENT_ID", obj[1]);
			map.put("TRAININGPLAN_ID", obj[2]);
			map.put("TRAININGPLANNAME", obj[3]);
			map.put("COURSENAME", obj[4]);
			map.put("COURSECODE", obj[5]);
			map.put("LEARNER_ID", obj[6]);
			map.put("ENROLLMENTSTATUS", obj[7]);
			map.put("COURSE_ID", obj[8]);
			map.put("COURSETYPE", obj[9]);
			listMap.add(map);
			map = new HashMap();
		}
		
		return listMap;
	}
	
	public List<Object[]> countLearnerByTrainingPlan(Long [] trainingPlanIds){
		StringBuffer strBuffSQL= new StringBuffer();
		strBuffSQL.append("select tpa.TRAINIINGPLAN_ID, COUNT( DISTINCT le.LEARNER_ID) LearnerCount from TRAINIINGPLANASSIGNMENT tpa inner join TRAININGPLANASSIGNMENT_LEARNERENROLLMENT tpa_le on tpa_le.TRAININGPLANASSIGNMENT_ID = tpa.ID inner join LEARNERENROLLMENT le on le.id = tpa_le.LEARNERENROLLMENT_ID where tpa.TRAINIINGPLAN_ID in (:arrTrainId) group by tpa.TRAINIINGPLAN_ID  ");
		
		Query qry=entityManager.createNativeQuery(strBuffSQL.toString());
		qry.setParameter("arrTrainId", Arrays.asList(trainingPlanIds));
		return qry.getResultList();
	}
}
