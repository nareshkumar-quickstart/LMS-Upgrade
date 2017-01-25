package com.softech.vu360.lms.webservice.lmsapi;

import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToLearnerRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToLearnerResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToUserGroupRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.CreateTrainingPlanRequest;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.CreateTrainingPlanResponse;

public interface LmsApiTrainingPlanWS {

	/**
	 * This is the namespace we defined in our TrainingPlanServiceOperations.xsd . We use this in the endpoint class for 
	 * mapping request to specific methods for processing.
	 */
	String TRAINING_PLAN_TARGET_NAMESPACE = "http://trainingplan.serviceoperations.lmsapi.message.webservice.lms.vu360.softech.com";
	
	String CREATE_TRAININGPLAN_EVENT = "CreateTrainingPlanRequest";
	String ASSIGN_TRAININGPLAN_TO_LEARNER_EVENT = "AssignTrainingPlanToLearnerRequest";
	String ASSIGN_TRAININGPLAN_TO_USERGROUP_EVENT = "AssignTrainingPlanToUserGroupRequest";
	
	CreateTrainingPlanResponse createTrainingPlan(CreateTrainingPlanRequest createTrainingPlanRequest);
	AssignTrainingPlanToLearnerResponse assignTrainingPlantoLearner(AssignTrainingPlanToLearnerRequest assignTrainingPlantoLearnerRequest);
	AssignTrainingPlanToUserGroupResponse assignTrainingPlantoUserGroup(AssignTrainingPlanToUserGroupRequest assignTrainingPlantoUserGroupRequest);
	
}
