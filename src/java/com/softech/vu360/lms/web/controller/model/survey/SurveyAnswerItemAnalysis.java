package com.softech.vu360.lms.web.controller.model.survey;

import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class SurveyAnswerItemAnalysis  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private SurveyAnswerItem answerItem;
	
	private Integer responseCount = 0;
	private Float responsePercent = 0.0f;

	public SurveyAnswerItemAnalysis(SurveyAnswerItem answerItem) {
		super();
		this.answerItem = answerItem;
	}
	
	public void initialize(){
		
	}
	
	public void analyze(SurveyAnswerItem answerItem, SurveyQuestion question){
		responseCount++;
	}

	public boolean isResultForThisAnswerItem(SurveyAnswerItem answerItem){
		if(this.answerItem !=null && this.answerItem.getId().equals(answerItem.getId())){
			return true;
		}
		return false;
	}
	
	public void summarize(Integer total){
		responsePercent = ((float)responseCount)/total*100;
	}
	
	/**
	 * @return the answerItem
	 */
	public SurveyAnswerItem getAnswerItem() {
		return answerItem;
	}

	/**
	 * @return the responseCount
	 */
	public Integer getResponseCount() {
		return responseCount;
	}

	/**
	 * @return the responsePercent
	 */
	public Float getResponsePercent() {
		return responsePercent;
	}
	
}
