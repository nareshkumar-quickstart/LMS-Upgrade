package com.softech.vu360.lms.webservice;

import com.softech.vu360.lms.webservice.message.predict360.survey.UserAssignedSurveysStatisticsRequest;
import com.softech.vu360.lms.webservice.message.predict360.survey.UserAssignedSurveysStatisticsResponse;

public interface Predict360SurveyWS extends AbstractWS {
	UserAssignedSurveysStatisticsResponse getAssignedSurveyByUserName(UserAssignedSurveysStatisticsRequest surveyImportRequest);
}
