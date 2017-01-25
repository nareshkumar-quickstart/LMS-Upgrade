package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.CustomerEntitlement;

public interface TrainingPlanRepositoryCustom {
	
	List<Map<Object, Object>> getTrainingPlansForCourseCatalog(long id,String search);
	List<Object[]> countLearnerByTrainingPlan(Long [] trainingPlanIds);
}
