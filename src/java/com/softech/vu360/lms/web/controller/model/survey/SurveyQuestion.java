package com.softech.vu360.lms.web.controller.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.exception.TemplateServiceException;
import com.softech.vu360.lms.service.impl.TemplateServiceImpl;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.Brander;


public class SurveyQuestion  implements ILMSBaseInterface, Comparable<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion>, Serializable {
	
	private static final long serialVersionUID = -938239831062762177L;
	
	private static Logger log = Logger.getLogger(SurveyQuestion.class);

	private com.softech.vu360.lms.model.SurveyQuestion surveyQuestionRef;

	public SurveyQuestion(com.softech.vu360.lms.model.SurveyQuestion surveyQuestionRef) {
		super();
		this.surveyQuestionRef = surveyQuestionRef;
	}

	private String templatePath;
	private List<com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem> answerItems = new ArrayList<com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem>();
	private List<com.softech.vu360.lms.web.controller.model.survey.AggregateSurveyQuestionItem> aggregateAnswerItems = new ArrayList<com.softech.vu360.lms.web.controller.model.survey.AggregateSurveyQuestionItem>();
	private List<com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield> personalInfoItems = new ArrayList<com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield>();
	private String answerText;
	private Long singleSelectAnswerId;
	private String comment;
	private com.softech.vu360.lms.web.controller.model.survey.Survey surveyView;
	private Long index;
	private Integer skippedQuestion = 0;
	private Integer totalCompletedQuestion = 0;
	private Integer sumResponseCount = 0;
	private boolean canHaveFile;
	private boolean fileRequired;
	private List<SurveyResultAnswerFile> answerFiles = new LinkedList<SurveyResultAnswerFile>();
	/**
	 * @return the answerItems
	 */
	public List<com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem> getAnswerItems() {
		return answerItems;
	}
	/**
	 * @param answerItems the answerItems to set
	 */
	public void setAnswerItems(
			List<com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem> answerItems) {
		this.answerItems = answerItems;
	}

	public void addAnswerItem(com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem){
		if(this.answerItems==null)
			answerItems = new ArrayList<com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem>();
		this.answerItems.add(answerItem);
	}

	/**
	 * @return the answerText
	 */
	public String getAnswerText() {
		return answerText;
	}
	/**
	 * @param answerText the answerText to set
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	/**
	 * @return the surveyQuestionRef
	 */
	public com.softech.vu360.lms.model.SurveyQuestion getSurveyQuestionRef() {
		return surveyQuestionRef;
	}
	/**
	 * @param surveyQuestionRef the surveyQuestionRef to set
	 */
	public void setSurveyQuestionRef(
			com.softech.vu360.lms.model.SurveyQuestion surveyQuestionRef) {
		this.surveyQuestionRef = surveyQuestionRef;
	}

	/**
	 * @return the templatePath
	 */
	public String getTemplatePath() {
		return templatePath;
	}
	/**
	 * @param templatePath the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String renderQuestion(String prefix){
		return this.renderQuestion(prefix, null);
	}

	public String renderQuestion(String prefix, Brander brander){
		TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
		HashMap<Object, Object> attrs = new HashMap<Object, Object>();
		attrs.put("prefix", prefix);
		attrs.put("question", this);
		if(brander!=null)
			attrs.put("brander", brander);
		try {
			return tmpSvc.renderTemplate(this.templatePath, attrs);
		} catch (TemplateServiceException e) {
			log.error(e);
			return "";
		}
	}
	/**
	 * @return the singleSelectAnswerId
	 */
	public Long getSingleSelectAnswerId() {
		return singleSelectAnswerId;
	}
	/**
	 * @param singleSelectAnswerId the singleSelectAnswerId to set
	 */
	public void setSingleSelectAnswerId(Long singleSelectAnswerId) {
		this.singleSelectAnswerId = singleSelectAnswerId;
	}

	public boolean equals(Object obj){
		com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion surveyQuestion = (com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion) obj ;
		if(surveyQuestion.getSurveyQuestionRef().getId().longValue() == this.getSurveyQuestionRef().getId().longValue() ){
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {

		return this.getSurveyQuestionRef().getId().intValue();
	}
	@Override
	public int compareTo(SurveyQuestion arg0) {
		Integer i1 = new Integer (  this.getSurveyQuestionRef().getDisplayOrder()) ; 
		Integer i2 = new Integer (  arg0.getSurveyQuestionRef().getDisplayOrder()  ) ; 
		return i1.compareTo(i2);
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the aggregateAnswerItems
	 */
	public List<com.softech.vu360.lms.web.controller.model.survey.AggregateSurveyQuestionItem> getAggregateAnswerItems() {
		return aggregateAnswerItems;
	}
	/**
	 * @param aggregateAnswerItems the aggregateAnswerItems to set
	 */
	public void setAggregateAnswerItems(
			List<com.softech.vu360.lms.web.controller.model.survey.AggregateSurveyQuestionItem> aggregateAnswerItems) {
		this.aggregateAnswerItems = aggregateAnswerItems;
	}
	/**
	 * @return the surveyView
	 */
	public com.softech.vu360.lms.web.controller.model.survey.Survey getSurveyView() {
		return surveyView;
	}
	/**
	 * @param surveyView the surveyView to set
	 */
	public void setSurveyView(
			com.softech.vu360.lms.web.controller.model.survey.Survey surveyView) {
		this.surveyView = surveyView;
	}
	/**
	 * @return the personalInfoItems
	 */
	public List<com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield> getPersonalInfoItems() {
		return personalInfoItems;
	}
	/**
	 * @param personalInfoItems the personalInfoItems to set
	 */
	public void setPersonalInfoItems(
			List<com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield> personalInfoItems) {
		this.personalInfoItems = personalInfoItems;
	}
	
	public void addPersonalInfoItem(com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield answerItem){
		if(this.personalInfoItems==null)
			personalInfoItems = new ArrayList<com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield>();
		this.personalInfoItems.add(answerItem);
	}
	public Long getIndex() {
		return index;
	}
	public void setIndex(Long index) {
		this.index = index;
	}
	public Integer getSkippedQuestion() {
		return skippedQuestion;
	}
	public void setSkippedQuestion(Integer skippedQuestion) {
		this.skippedQuestion = skippedQuestion;
	}
	public Integer getTotalCompletedQuestion() {
		return totalCompletedQuestion;
	}
	public void setTotalCompletedQuestion(Integer totalCompletedQuestion) {
		this.totalCompletedQuestion = totalCompletedQuestion;
	}
	public Integer getSumResponseCount() {
		return sumResponseCount;
	}
	public void setSumResponseCount(Integer sumResponseCount) {
		this.sumResponseCount = sumResponseCount;
	}
	public boolean isCanHaveFile() {
		return canHaveFile;
	}
	public void setCanHaveFile(boolean canHaveFile) {
		this.canHaveFile = canHaveFile;
	}
	public boolean isFileRequired() {
		return fileRequired;
	}
	public void setFileRequired(boolean fileRequired) {
		this.fileRequired = fileRequired;
	}
	public List<SurveyResultAnswerFile> getAnswerFiles() {
		return answerFiles;
	}
	public void setAnswerFiles(List<SurveyResultAnswerFile> answerFiles) {
		this.answerFiles = answerFiles;
	}
}
