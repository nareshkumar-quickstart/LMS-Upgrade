package com.softech.vu360.lms.widget.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.vo.WidgetData;
import com.softech.vu360.lms.widget.WidgetLogic;

public class SurveyWidgetLogic implements WidgetLogic {

	protected static final Logger log = Logger.getLogger(SurveyWidgetLogic.class);
	
	protected SurveyService surveyService;
	
	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	@Override
	public Collection<WidgetData> getWidgetDataList(VU360User vu360User, Map<String, Object> params, HttpServletRequest request) {
		log.debug("in getWidgetDataList vu360User.id=" + vu360User.getId() + " vu360User.username=" + vu360User.getUsername() + " params=" + params);
		List<WidgetData> datas = new ArrayList<WidgetData>();
		String filter = (String) ((params.get("filter")!=null) ? params.get("filter") : "all");
		
		if(filter.compareTo("pending")==0 || filter.compareTo("all")==0) {
			List<Survey> surveys = surveyService.getDueSurveysByUser(vu360User);
			for(Survey survey : surveys) {
				WidgetData data = new WidgetData();
				data.setId((long)datas.size());
				Map<String, Object> dataMap = data.getDataMap();
				dataMap.put("name", survey.getName());
				dataMap.put("surveyId", survey.getId().toString());
				dataMap.put("filter", "pending");
				datas.add(data);
			}
			/*
			List<Learner> learners = new ArrayList<Learner>();
			learners.add(vu360User.getLearner());
			List<SurveyLearner> allSurveyLearners = surveyService.getSurveyAssignmentOfLearners(learners);
			
			for(SurveyLearner surveyLearner : allSurveyLearners) {
				for(Survey survey : surveys) {
					if(surveyLearner.getSurvey().getId().longValue()==survey.getId().longValue()) {
						WidgetData data = new WidgetData();
						data.setId((long)datas.size());
						Map<String, Object> dataMap = data.getDataMap();
						dataMap.put("name", surveyLearner.getSurvey().getName());
						dataMap.put("surveyId", surveyLearner.getSurvey().getId().toString());
						if(surveyLearner.getEndDate()!=null) {
							dataMap.put("enddate",  surveyLearner.getEndDate().getMonth()+"/"+surveyLearner.getEndDate().getDate()+"/"+(surveyLearner.getEndDate().getYear()+1900));
						} else {
							dataMap.put("enddate",  "");
						}
						dataMap.put("link", "lrn_takeSurvey.do?method=showSurveyView&launch=0&surveyId=" + surveyLearner.getSurvey().getId());
						dataMap.put("filter", "pending");
						datas.add(data);
						break;
					}
				}
			}
			*/
		}
		
		if(filter.compareTo("completed")==0 || filter.compareTo("all")==0) {
			List<Survey> surveys = surveyService.getCompletedSurveysByUser(vu360User);
			for(Survey survey : surveys) {
				WidgetData data = new WidgetData();
				data.setId((long)datas.size());
				Map<String, Object> dataMap = data.getDataMap();
				dataMap.put("name", survey.getName());
				dataMap.put("surveyId", survey.getId().toString());
				dataMap.put("filter", "pending");
				datas.add(data);
			}
			/*
			List<SurveyLearner> allSurveyLearners = surveyService.getLearnerSurveys(vu360User);
			for(SurveyLearner surveyLearner : allSurveyLearners) {
				for(Survey survey : surveys) {
					if(surveyLearner.getSurvey().getId().longValue()==survey.getId().longValue()) {
						WidgetData data = new WidgetData();
						data.setId((long) datas.size());
						Map<String, Object> dataMap = data.getDataMap();
						dataMap.put("name", surveyLearner.getSurvey().getName());
						dataMap.put("surveyId", surveyLearner.getSurvey().getId().toString());
						if(surveyLearner.getEndDate()!=null) {
							dataMap.put("enddate",  surveyLearner.getEndDate().getMonth()+"/"+surveyLearner.getEndDate().getDate()+"/"+(surveyLearner.getEndDate().getYear()+1900));
						} else {
							dataMap.put("enddate",  "");
						}
						dataMap.put("link", "lrn_takeSurvey.do?method=showSurveyView&launch=0&surveyId=" + surveyLearner.getSurvey().getId());
						dataMap.put("filter", "completed");
						datas.add(data);
						break;
					}
				}
			}
			*/
		}
		
		return datas;
	}
	
	
	
}
