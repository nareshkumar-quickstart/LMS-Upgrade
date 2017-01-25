package com.softech.vu360.lms.service.lmsapi;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToLearnerResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.trainingplan.AssignTrainingPlanToUserGroupResponse;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.ResponseTrainingPlan;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlan;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanAssignResp;
import com.softech.vu360.lms.webservice.message.lmsapi.types.trainingplan.TrainingPlanAssignResponsed;

public interface TrainingPlanServiceLmsApi {
	
	public Map<String, Object> validateCreateTrainingPlanRequest(Customer customer, TrainingPlan trainingPlan) throws Exception;
	public List<ResponseTrainingPlan> processTrainingPlansMap(Customer customer, Map<TrainingPlan, Object> trainingPlansMap) throws Exception;
	
	//Assign training plan to learner
	public Map<String, Object> validateAssignTrainingPlanToLearnerRequest(BigInteger trainingPlanId, String startDate, String endDate, String customerCode, String customerGuid, boolean notifyLearnersByEmail) throws Exception;
	public AssignTrainingPlanToLearnerResponse getAssignTrainingPlanToLearnerResponse(Map<String, Object> trainingPlanValidationMap);
	public AssignTrainingPlanToLearnerResponse getAssignTrainingPlanToLearnerResponse(BigInteger trainingPlanId, Map<String, Object> learnersMap);
	public Map<com.softech.vu360.lms.model.Course, Object> getCourseEntitlementMap(com.softech.vu360.lms.model.TrainingPlan trainingPlan, 
			Date enrollmentStartDate, Date enrollmentEndDate) throws Exception;
	
	public void setTrainingPlanAssignment(com.softech.vu360.lms.model.TrainingPlan trainingPlan, List<LearnerEnrollment> learnerEnrollmentList);
	public List<TrainingPlanAssignResp> processLearnerCoursesEnrollmentResultMapForResponse(Map<String, Object> learnersMap, Map<Learner, Map<String, Object>> learnerCoursesEnrollmentResultMap);
	public Map<Learner, List<LearnerEnrollment>> getLearnerEnrollmentEmailMap(Map<Learner, Map<String, Object>> learnerCoursesEnrollmentResultMap);
	
	//Assign Training plan to User group
	public Map<String, Object> validateAssignTrainingPlanToUserGroupRequest(BigInteger trainingPlanId, String startDate, String endDate, String customerCode, String customerGuid, boolean notifyLearnersByEmail) throws Exception;
	public AssignTrainingPlanToUserGroupResponse getAssignTrainingPlanToUserGroupResponse(Map<String, Object> trainingPlanValidationMap) throws Exception;
	public AssignTrainingPlanToUserGroupResponse getAssignTrainingPlanToUserGroupResponse(BigInteger trainingPlanId, List<BigInteger> userGroupIdList, Map<String, Object> learnersMap) throws Exception;
	public AssignTrainingPlanToUserGroupResponse getAssignTrainingPlanToUserGroupResponse(BigInteger trainingPlanId, String errorMessage, List<BigInteger> userGroupIdList) throws Exception;
	public TrainingPlanAssignResponsed getTrainingPlanAssignResponsed(BigInteger trainingPlanId, String errorMessage, List<BigInteger> userGroupIdList) throws Exception;

}


