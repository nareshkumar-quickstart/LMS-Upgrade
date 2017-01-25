package com.softech.vu360.lms.web.controller.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.SurveyResultAnswer;
import com.softech.vu360.lms.vo.SurveySectionVO;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SurveyAnalysis  implements ILMSBaseInterface, Serializable {
	
	private static final long serialVersionUID = -7369114558265425195L;
	
	private Survey analyzedSurvey = null;
	private Integer startedSurveyCount = 0;
	private Integer completedSurveyCount = 0;
	private List<SurveyQuestionAnalysis> questions = new ArrayList<SurveyQuestionAnalysis>();
	private List<SurveySectionVO> sectionVOs = new ArrayList<SurveySectionVO>();
	
	public SurveyAnalysis(Survey analyzedSurvey) {
		this.analyzedSurvey = analyzedSurvey;
	}
	
	public void initialize(){
		if(analyzedSurvey!=null){
			List<SurveyQuestion> surveyQuestion = analyzedSurvey.getQuestions();
			Collections.sort(surveyQuestion);
			for(SurveyQuestion question : surveyQuestion){
				SurveyQuestionAnalysis sqa = new SurveyQuestionAnalysis(question);
				sqa.initialize();
				questions.add(sqa);
			}
		}
	}

	public void analyze(SurveyResult result){
		if(result.getSurvey().getId().equals(analyzedSurvey.getId())){
			startedSurveyCount++;
			if(this.isSurveyCompleted(result))
				completedSurveyCount++;
			
			for(SurveyResultAnswer answer : result.getAnswers()){
				for(SurveyQuestionAnalysis sqa : questions){
					if(sqa.isResultForThisQuestion(answer)){
						sqa.analyze(answer);
						break;
					}
				}
			}
			for(SurveyQuestionAnalysis question:questions)
				question.summarize();
		}else{
			//a result not for this survey
		}
	}

	private boolean isSurveyCompleted(SurveyResult result){
		//the number of surveyresultanswers for this survey result
		//should be equal to the number of questions in the survey associated to this survey result
		if(result.getAnswers().size() == analyzedSurvey.getQuestions().size())
			return true;
		return false;
	}
	
	/**
	 * @return the analyzedSurvey
	 */
	public Survey getAnalyzedSurvey() {
		return analyzedSurvey;
	}

	/**
	 * @return the completedSurveyCount
	 */
	public Integer getCompletedSurveyCount() {
		return completedSurveyCount;
	}

	/**
	 * @return the questions
	 */
	public List<SurveyQuestionAnalysis> getQuestions() {
		return questions;
	}

	/**
	 * @return the startedSurveyCount
	 */
	public Integer getStartedSurveyCount() {
		return startedSurveyCount;
	}

	public List<SurveySectionVO> getSectionVOs() {
		return sectionVOs;
	}

	public void setSectionVOs(List<SurveySectionVO> sectionVOs) {
		this.sectionVOs = sectionVOs;
	}

	public void setStartedSurveyCount(Integer startedSurveyCount) {
		this.startedSurveyCount = startedSurveyCount;
	}

	public void setCompletedSurveyCount(Integer completedSurveyCount) {
		this.completedSurveyCount = completedSurveyCount;
	}
}
