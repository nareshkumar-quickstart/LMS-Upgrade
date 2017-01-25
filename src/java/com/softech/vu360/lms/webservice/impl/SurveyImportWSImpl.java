package com.softech.vu360.lms.webservice.impl;

import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ClassUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.SurveyQuestionBank;
import com.softech.vu360.lms.model.SurveySection;
import com.softech.vu360.lms.model.SurveySectionSurveyQuestionBank;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.webservice.Predict360WS;
import com.softech.vu360.lms.webservice.message.predict360.SurveyFrameworkSoapVO;
import com.softech.vu360.lms.webservice.message.predict360.SurveyImportPredictRequest;
import com.softech.vu360.lms.webservice.message.predict360.SurveyImportPredictResponse;
import com.softech.vu360.lms.webservice.message.predict360.SurveyQuestionAnswerSoapVO;
import com.softech.vu360.lms.webservice.message.predict360.SurveyQuestionSoapVO;
import com.softech.vu360.lms.webservice.message.predict360.SurveySectionSoapVO;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.VU360Properties;

@Endpoint
public class SurveyImportWSImpl implements Predict360WS {

	private static final Logger log = Logger.getLogger(SurveyImportWSImpl.class.getName());
	
	private static final String SURVEY_IMPORT_EVENT = "SurveyImportPredictRequest";
	private static final String MESSAGES_NAMESPACE = "http://www.360training.com/vu360/schemas/lms/surveyimport";

	protected SurveyService surveyService;
	protected VU360UserService vu360UserService;
	
	@Override
	@PayloadRoot(localPart = SURVEY_IMPORT_EVENT, namespace = MESSAGES_NAMESPACE)
	public SurveyImportPredictResponse lmsImportEvent( SurveyImportPredictRequest surveyVO) {
		SurveyImportPredictResponse response = new SurveyImportPredictResponse();
		try {
			log.debug("received request data >> " + surveyVO);
			
			Survey survey = new Survey();
			survey.setName(surveyVO.getName());
			survey.setInspection((surveyVO.getIsInspection()!=null)?surveyVO.getIsInspection():false);
			// hardcoded values required by lms :(
			survey.setStatus(Survey.PUBLISHED);
			survey.setEvent(Survey.SURVEY_EVENT_MANUAL_CODE);
			survey.setIsShowAll(true);
			String username = surveyVO.getUsername();
			VU360User vu360User = vu360UserService.findUserByUserName(username);
			if(vu360User!=null && vu360User.getLearner()!=null && vu360User.getLearner().getCustomer()!=null) {
				Customer customer = vu360User.getLearner().getCustomer();
				survey.setOwner(customer);
			} else {
				throw new UsernameNotFoundException("FOUND NO SUCH USER NAME : " + username);
			}
			survey = surveyService.saveSurvey(survey);
			
			for (SurveySectionSoapVO sectionVO : surveyVO.getSections()) {
				SurveySection surveySection = getPopulatedSection(sectionVO, null, survey, surveyVO.getId());
				survey.getSections().add(surveySection);
			}
			survey = surveyService.saveSurvey(survey);
			response.setResultMessage("OK");
		} catch(Exception e) {
			log.error("problem in importing survey from predict", e);
			response.setResultMessage("ERROR [" + e.getMessage() + "]");
		}
		response.setEventDate(DateUtil.getCurrentDateTimeForXML());
		return response;
	}
	
	private SurveySection getPopulatedSection(SurveySectionSoapVO sectionVO, SurveySection parentSection, Survey survey, Long predictSurveyId) throws Exception {
		PredictSurveyQuestionMapping predictSurveyQuestionMapping = new PredictSurveyQuestionMapping();
		predictSurveyQuestionMapping.setSurveyId(predictSurveyId);
		log.debug("Received Survey:" + survey);
		predictSurveyQuestionMapping.setLmsSurveyId(survey.getId());
		
		SurveySection section = new SurveySection();
		log.debug("Received survey section soap:" + sectionVO);
		section.setName(sectionVO.getName());
		section.setDescription(sectionVO.getDescription());
		log.debug("Is parent section soap is null = " + parentSection);
		section.setParent(parentSection);
		section.setSurvey(survey);
		section = surveyService.saveSurveySection(section);
		log.debug("Survey section saved successfully. Section name :" + section.getName());
		
		log.debug("Survey section vo id exist.  " + sectionVO.getId());
		predictSurveyQuestionMapping.setSurveySectionId(sectionVO.getId().toString());
		predictSurveyQuestionMapping.setLmsSectionId(section.getId());
		surveyService.savePredictSurveyQuestionMapping(predictSurveyQuestionMapping);
		log.debug("PredictSurveyQuestionMapping is saved.");
		
		if(sectionVO.getChildren()!=null && sectionVO.getChildren().size()>0) {
			for (SurveySectionSoapVO childSectionVO : sectionVO.getChildren()) {
				if(childSectionVO.getId()!=null){
					section.getChildren().add(getPopulatedSection(childSectionVO, section, survey, predictSurveyId));
				}
			}
		}
		section = surveyService.saveSurveySection(section);
		log.debug("Survey section again saved successfully. Section name :" + section.getName());
		for(SurveyFrameworkSoapVO frameworkVO : sectionVO.getFrameworks()) {
			SurveyQuestionBank questionBank = new SurveyQuestionBank();
			log.debug("Framework :" + frameworkVO + " "  + frameworkVO.getName());
			questionBank.setName(frameworkVO.getName());
			questionBank.setDescription(frameworkVO.getDescription());
			questionBank = surveyService.saveSurveyQuestionBank(questionBank);
			log.debug("Survey question bank saved sucessfully.");
			
			predictSurveyQuestionMapping = new PredictSurveyQuestionMapping();
			predictSurveyQuestionMapping.setSurveyId(predictSurveyId);
			predictSurveyQuestionMapping.setLmsSurveyId(survey.getId());
			predictSurveyQuestionMapping.setFrameworkId(frameworkVO.getId());
			predictSurveyQuestionMapping.setLmsFrameworkId(questionBank.getId());
			predictSurveyQuestionMapping.setSurveySectionId(sectionVO.getId().toString());
			predictSurveyQuestionMapping.setLmsSectionId(section.getId());
			surveyService.savePredictSurveyQuestionMapping(predictSurveyQuestionMapping);
			log.debug("PredictSurveyQuestionMapping saved sucessfully.");
			
			SurveySectionSurveyQuestionBank sectionFrameworkMapping = new SurveySectionSurveyQuestionBank();
			sectionFrameworkMapping.setSurveyQuestionBank(questionBank);
			sectionFrameworkMapping.setSurveySection(section);
			sectionFrameworkMapping = surveyService.saveSurveySectionSurveyQuestionBank(sectionFrameworkMapping);
			log.debug("sectionFrameworkMapping saved sucessfully.");
			
			section.getSurveySectionSurveyQuestionBanks().add(sectionFrameworkMapping);
			
			int questionDisplayOrder = 0;
			log.debug("frameworkVO.getQuestions loop started.");
			for(SurveyQuestionSoapVO questionVO : frameworkVO.getQuestions()) {
				String questionKey = questionVO.getType();
				String questionType = VU360Properties.getVU360Property(questionKey);
				if(questionType==null) {
					throw new Exception("No question mapping found for " + questionKey);
				}
				
				Class<?> questionClass = ClassUtils.forName(questionType,Class.class.getClassLoader());
				SurveyQuestion question = (SurveyQuestion) questionClass.newInstance();
				question.setText(questionVO.getText());
				question.setNotes(questionVO.getNotes());
				if(questionVO.getCanHaveFile()!=null && questionVO.getCanHaveFile().booleanValue()) {
					question.setCanHaveFile(questionVO.getCanHaveFile());
				} else {
					question.setCanHaveFile(Boolean.FALSE);
				}
				if(questionVO.getFileRequired()!=null && questionVO.getFileRequired().booleanValue()) {
					question.setFileRequired(questionVO.getFileRequired());
				} else {
					question.setFileRequired(Boolean.FALSE);
				}
				question.setDisplayOrder(questionDisplayOrder++);
				question.setSurvey(survey);
				//question.setSurveyQuestionBank(questionBank);
				
				questionBank.getQuestions().add(question);
				questionBank = surveyService.saveSurveyQuestionBank(questionBank);
				log.debug("QuestionBank added ");
				
				question = surveyService.addSurveyQuestion(question);
				log.debug("Question added ");

				int answerDisplayOrder = 0;
				for(SurveyQuestionAnswerSoapVO answerVO : questionVO.getAnswers()) {
					SurveyAnswerItem answerItem = new SurveyAnswerItem();
					answerItem.setLabel(answerVO.getLabel());
					answerItem.setDisplayOrder(answerDisplayOrder++);
					answerItem.setSurveyQuestion(question);
					answerItem = surveyService.saveSurveyAnswerItem(answerItem);
					log.debug("SurveyAnswerItem saved Successfully.");
					
					question.getSurveyAnswers().add(answerItem);
				}
				
				predictSurveyQuestionMapping = new PredictSurveyQuestionMapping();
				predictSurveyQuestionMapping.setSurveyId(predictSurveyId);
				predictSurveyQuestionMapping.setLmsSurveyId(survey.getId());
				predictSurveyQuestionMapping.setFrameworkId(frameworkVO.getId());
				predictSurveyQuestionMapping.setLmsFrameworkId(questionBank.getId());
				predictSurveyQuestionMapping.setSurveySectionId(sectionVO.getId().toString());
				predictSurveyQuestionMapping.setLmsSectionId(section.getId());
				predictSurveyQuestionMapping.setQuestionId(questionVO.getId());
				predictSurveyQuestionMapping.setLmsQuestionId(question.getId());
				surveyService.savePredictSurveyQuestionMapping(predictSurveyQuestionMapping);
				log.debug("PredictSurveyQuestionMapping saved successfully." + predictSurveyQuestionMapping.getId());
			}
		}
		return section;
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
