package com.softech.vu360.lms.repositories.impl;

import java.util.List;

import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyOwner;

public interface SurveyRepositoryCustom {
	List<Survey> getCurrentSurveyListByCoursesForUser(Long learnerId, Long ownerId);
	Boolean isAlertQueueRequiredProctorApproval(Long tableNameId, String businessKeys);
	List<Survey> getSurveysByOwnerAndSurveyName(SurveyOwner surveyowner,String surveyName, String surveyStatus, String isLocked, String readonly,String event);
	List<Survey> findManualSurveys(String surveyName,String surveyStatus, String retiredSurvey, Long ownerIds[],int intLimit);
	List<Survey> getNonFinishedManualSurveyAssignmentOfLearners(Long learnerId, Long customerId, Long vu360UserId);
}
