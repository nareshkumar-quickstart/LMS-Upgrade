package com.softech.vu360.lms.web.controller.model.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.MathTool;

import com.softech.vu360.lms.exception.TemplateServiceException;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.SurveyResultAnswer;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.service.impl.TemplateServiceImpl;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.Brander;

public class SurveyQuestionAnalysis  implements ILMSBaseInterface{
	private static Logger log = Logger.getLogger(SurveyQuestionAnalysis.class);
	private static final String MULTICHOICE_TEMPLATE="vm/manager/userGroups/survey/multichoice.vm";
	private static final String COMMENT_TEMPLATE="vm/manager/userGroups/survey/commentText.vm";
	private static final long serialVersionUID = 1L;
	private SurveyQuestion analyzedQuestion;
	private Integer answeredCount = 0;
	private Integer skippedCount = 0;
	private List<SurveyAnswerItemAnalysis> answerItems = new ArrayList<SurveyAnswerItemAnalysis>();
	
	private String templatePath;
	
	public SurveyQuestionAnalysis(SurveyQuestion analyzedQuestion) {
		super();
		this.analyzedQuestion = analyzedQuestion;
	}
	
	public void initialize(){
		if(this.analyzedQuestion instanceof TextBoxSurveyQuestion){
			//a comment survey question will not have any options
			SurveyAnswerItemAnalysis saia = new SurveyAnswerItemAnalysis(null);
			answerItems.add(saia);
			templatePath = COMMENT_TEMPLATE;
		}else{
			//A multiple choice question
			List<SurveyAnswerItem> surveyAnswerItems = analyzedQuestion.getSurveyAnswers();
			Collections.sort(surveyAnswerItems);
			for(SurveyAnswerItem answerItem : surveyAnswerItems ){
				SurveyAnswerItemAnalysis saia = new SurveyAnswerItemAnalysis(answerItem);
				saia.initialize();
				answerItems.add(saia);
			}
			templatePath = MULTICHOICE_TEMPLATE;
		}

	}
	
	public void analyze(SurveyResultAnswer answer){
		if(this.analyzedQuestion instanceof TextBoxSurveyQuestion){
			if(StringUtils.isNotBlank(answer.getSurveyAnswerText())){
				answeredCount++;
				//set up the corresponding SurveyAnswerItemAnalysis
				SurveyAnswerItemAnalysis answerItem = answerItems.get(0);
				answerItem.analyze(null, analyzedQuestion);
			}else{
				skippedCount++;
			}
			
		}else if(this.analyzedQuestion instanceof SingleSelectSurveyQuestion){
			List<SurveyAnswerItem> surveyAnswerItems = answer.getSurveyAnswerItems();
			if(surveyAnswerItems!=null && surveyAnswerItems.size()>0){
				Collections.sort(surveyAnswerItems);
				answeredCount++;
				//should have only one answer item - as it is single select
				SurveyAnswerItem answerItemSelected = surveyAnswerItems.get(0);
				for(SurveyAnswerItemAnalysis answerItem : answerItems){
					if(answerItem.isResultForThisAnswerItem(answerItemSelected)){
						answerItem.analyze(answerItemSelected, analyzedQuestion);
						break;
					}
				}
			}else{
				skippedCount++;
			}
		}else if(this.analyzedQuestion instanceof MultipleSelectSurveyQuestion){
			List<SurveyAnswerItem> surveyAnswerItems = answer.getSurveyAnswerItems();
			if(surveyAnswerItems!=null && surveyAnswerItems.size()>0){
				Collections.sort(surveyAnswerItems);
				answeredCount++;
				//can have multiple answer items - as it is multi select
				for(SurveyAnswerItem answerItemSelected : surveyAnswerItems){
					for(SurveyAnswerItemAnalysis answerItem : answerItems){
						if(answerItem.isResultForThisAnswerItem(answerItemSelected)){
							answerItem.analyze(answerItemSelected, analyzedQuestion);
							break;
						}
					}
				}
			}else{
				skippedCount++;
			}
		}else{
			
		}
	}

	public void summarize(){
		if(this.analyzedQuestion instanceof TextBoxSurveyQuestion){
			//do not calculate percentages
		}else{
			Integer totalCount = 0;
			for(SurveyAnswerItemAnalysis answer:answerItems){
				totalCount += answer.getResponseCount();
			}
			if(totalCount>0)
				for(SurveyAnswerItemAnalysis answer:answerItems){
					answer.summarize(totalCount);
				}
		}
	}
	
	public boolean isResultForThisQuestion(SurveyResultAnswer answer){
        return answer.getQuestion().getId().equals(this.analyzedQuestion.getId());
    }
	
	/**
	 * @return the analyzedQuestion
	 */
	public SurveyQuestion getAnalyzedQuestion() {
		return analyzedQuestion;
	}

	/**
	 * @return the answeredCount
	 */
	public Integer getAnsweredCount() {
		return answeredCount;
	}

	/**
	 * @return the skippedCount
	 */
	public Integer getSkippedCount() {
		return skippedCount;
	}
	
	public String renderQuestion(String prefix){
		return this.renderQuestion(prefix, null);
	}
	
	public String renderQuestion(String prefix, Brander brander){
		log.debug("SurveyQuestionAnalysis renderQuestion for question - "+this.analyzedQuestion.getText());
		TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
		HashMap<Object, Object> attrs = new HashMap<Object, Object>();
		attrs.put("prefix", prefix);
		attrs.put("question", this);
		attrs.put("math", new MathTool());//did not like the way I had to do this!!!!
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
	 * @return the answerItems
	 */
	public List<SurveyAnswerItemAnalysis> getAnswerItems() {
		return answerItems;
	}

}
