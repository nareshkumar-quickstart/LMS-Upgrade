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
		
		
		StringBuilder strBuildSQL= new StringBuilder();
		
		strBuildSQL.append("EXEC [SP_COURSECATALOGWITHTRAININGPLANS] ");
		strBuildSQL.append("[").append(id).append("]");
		strBuildSQL.append(", ");
		strBuildSQL.append("null");
		if (StringUtils.isNotBlank(search) && !search.equals("Search")) { 
			strBuildSQL.append(", ");
			strBuildSQL.append("[").append(search).append("]");
		}
		
		Query qry=entityManager.createNativeQuery(strBuildSQL.toString());
		List<Object[]> lst = qry.getResultList();
		List<Map<Object,Object>> listMap = new ArrayList<>();
		Map<Object,Object> map;
		for(Object[] obj : lst){
			map = new HashMap<>();
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
		}
		
		return listMap;
	}
	
	@Override
	public List<Object[]> countLearnerByTrainingPlan(Long [] trainingPlanIds){
		String strSql="select tpa.TRAINIINGPLAN_ID, COUNT( DISTINCT le.LEARNER_ID) LearnerCount from TRAINIINGPLANASSIGNMENT tpa inner join TRAININGPLANASSIGNMENT_LEARNERENROLLMENT tpa_le on tpa_le.TRAININGPLANASSIGNMENT_ID = tpa.ID inner join LEARNERENROLLMENT le on le.id = tpa_le.LEARNERENROLLMENT_ID where tpa.TRAINIINGPLAN_ID in (:arrTrainId) group by tpa.TRAINIINGPLAN_ID";
		
		Query qry=entityManager.createNativeQuery(strSql);
		qry.setParameter("arrTrainId", Arrays.asList(trainingPlanIds));
		return qry.getResultList();
	}
}
