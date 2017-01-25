package com.softech.vu360.lms.webservice.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.webservice.Predict360SurveyWS;
import com.softech.vu360.lms.webservice.message.predict360.survey.UserAssignedSurveysStatisticsRequest;
import com.softech.vu360.lms.webservice.message.predict360.survey.UserAssignedSurveysStatisticsResponse;


@Endpoint
public class SurveyWSImpl implements Predict360SurveyWS {

	private SurveyService surveyService;
	private VU360UserService vu360UserService;
	private static final Logger log = Logger.getLogger(SurveyWSImpl.class.getName());
	
	private static final String SURVEY_EVENT = "UserAssignedSurveysStatisticsRequest";
	private static final String MESSAGES_NAMESPACE = "http://www.360training.com/vu360/schemas/lms/survey";

	@Override
	@PayloadRoot(localPart = SURVEY_EVENT, namespace = MESSAGES_NAMESPACE)
	public UserAssignedSurveysStatisticsResponse getAssignedSurveyByUserName(UserAssignedSurveysStatisticsRequest assignedSurveyRequest) {
		System.out.println("Survey >> User Assigned surveys");
		UserAssignedSurveysStatisticsResponse response = new UserAssignedSurveysStatisticsResponse();
		
		String userName = assignedSurveyRequest.getUsername();

		VU360User vu360User = vu360UserService.findUserByUserName(userName);
		if(vu360User == null){
			response.setCompletedSurvey(0);
			response.setTotalAssignedSurvey(0);
			response.setInprogressSurvey(0);
			response.setOverdueSurvey(0);
			response.setUsername("");
			return response;
		}
		
		List<Survey> dueSurveyList = surveyService.getDueSurveysByUser(vu360User);
		List<Survey> completedSurveyList = surveyService.getCompletedSurveysByUser(vu360User);
		List<Survey> overDueSurveys = surveyService.getOverDueSurveysByUser(vu360User);
		
		response.setCompletedSurvey(completedSurveyList.size());
		response.setTotalAssignedSurvey(dueSurveyList.size());
		response.setInprogressSurvey(0);
		response.setOverdueSurvey(overDueSurveys.size());
		response.setUsername(userName);
		return response;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}



}
