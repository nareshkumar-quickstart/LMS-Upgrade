package com.softech.vu360.lms.web.controller.learner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.AggregateSurveyQuestion;
import com.softech.vu360.lms.model.AggregateSurveyQuestionItem;
import com.softech.vu360.lms.model.CommentSurveyQuestion;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.PersonalInformationSurveyQuestion;
import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.SurveyQuestionBank;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.SurveyResultAnswer;
import com.softech.vu360.lms.model.SurveyResultAnswerFile;
import com.softech.vu360.lms.model.SurveySection;
import com.softech.vu360.lms.model.SurveySectionSurveyQuestionBank;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.predict.PredictSurveySeciton;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.SurveyPreviewVO;
import com.softech.vu360.lms.vo.SurveyQuestionBankVO;
import com.softech.vu360.lms.vo.SurveySectionVO;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.SurveyQuestionBuilder;
import com.softech.vu360.lms.web.controller.model.survey.TakeSurveyForm;
import com.softech.vu360.lms.webservice.client.impl.RestClient;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class TakeSurveyController extends VU360BaseMultiActionController{

	private SurveyService surveyService = null;
	
	@Autowired
	private VU360UserService vu360UserService;
	
	private VelocityEngine velocityEngine;
	private String defaultTakeSurveyTemplate;
	private String defaultTakeSurveySectionTemplate;
	private String closeTemplate;
	private static final Logger log = Logger.getLogger(TakeSurveyController.class.getName());
	private static final String SURVEY_QUESTION_FILE_UPLOAD_DIR = VU360Properties.getVU360Property("predict.survey.file.upload.path");

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		TakeSurveyForm form = (TakeSurveyForm)command;
		log.debug("inside on bind message " + form);
	}

	protected void validateSectionSurvey(HttpServletRequest request, TakeSurveyForm form, BindException errors, String methodName) throws Exception {
		log.debug("in validateSectionSurvey");
		String [] questionsArray = request.getParameterValues("questionsArray");
		String [] sectionsArray = request.getParameterValues("sectionsArray");
		String [] questionBanksArray = request.getParameterValues("questionBanksArray");
		if(questionsArray!=null && sectionsArray!=null && questionBanksArray!=null) {
			log.debug("sectionsArray="+sectionsArray.length + " questionBanksArray=" + questionBanksArray.length + " questionsArray=" + questionsArray.length);
		}
	}
	
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		log.debug("in validate");
		TakeSurveyForm form = (TakeSurveyForm)command;
		if(form.getSurvey()!=null && form.getSurvey().getSurveyRef()!=null && form.getSurvey().getSurveyRef().getSections()!=null && form.getSurvey().getSurveyRef().getSections().size()>0) {
			validateSectionSurvey(request, form, errors, methodName);
		} else {
			String [] questionsArray = request.getParameterValues("questionsArray");		
			if ( questionsArray != null ) {
				boolean selected =false ; 
				log.debug(questionsArray.length);
				// get all survey questions
	
				for(String questionIdString : questionsArray){
					long questionId =0 ;
					try{
						questionId = Long.parseLong(questionIdString);
	
					}catch(NumberFormatException e){
						e.printStackTrace();
						continue;
					}
					int listCount=0;
					
					for(int i=0;i<form.getSurvey().getQuestionList().size(); i++){
						if( questionId == form.getSurvey().getQuestionList().get(i).getSurveyQuestionRef().getId().longValue()  ) { // if question is found in list
	
	
							com.softech.vu360.lms.model.SurveyQuestion surveyQuestion = form.getSurvey().getQuestionList().get(i).getSurveyQuestionRef();
	
							if(surveyQuestion instanceof CommentSurveyQuestion){
	
								if( StringUtils.isBlank( form.getSurvey().getQuestionList().get(listCount).getAnswerText() ) ) {
									errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
								}
	
							}else if(surveyQuestion instanceof MultipleSelectSurveyQuestion){
								
								if(form.getSurvey().getQuestionList().get(listCount).getSurveyQuestionRef().getRequired().booleanValue()){
									selected = false;
									for ( int k=0 ; k<form.getSurvey().getQuestionList().get(listCount).getAnswerItems().size() ; k++) {								
										if (form.getSurvey().getQuestionList().get(listCount).getAnswerItems().get(k).isSelected()) {
											selected = true ;
										}
									}
									if( selected == false ){ // if selected is still set to false
										errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
									}
								}
	
								
								if(i==form.getSurvey().getQuestionList().size()){
									return;
								}
							}else if(surveyQuestion instanceof SingleSelectSurveyQuestion){
	
								/*if(form.getSurvey().getQuestionList().get(listCount).getSingleSelectAnswerId() ==null){
	
									errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
								}*/	
								
								if(form.getSurvey().getQuestionList().get(listCount).getSurveyQuestionRef().getRequired().booleanValue()){
									selected = false;																
									if (form.getSurvey().getQuestionList().get(listCount).getSingleSelectAnswerId() != null) {
										selected = true ;
									}								
									if( selected == false ){ // if selected is still set to false
										errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
									}
								}							
								if(i==form.getSurvey().getQuestionList().size()){
									return;
								}
	
							} else if(surveyQuestion instanceof PersonalInformationSurveyQuestion){
								List<com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield> personalInfoItems = form.getSurvey().getQuestionList().get(listCount).getPersonalInfoItems();
								boolean flag = false;
								if(!surveyQuestion.getRequired().booleanValue()){ // if all answer required is not selected
									for (com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield aPIF : personalInfoItems) {
										if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("FIRSTNAME")) {
											if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
												flag = true;
												errors.rejectValue("survey.questionList["+listCount+"]", "First Name is required");
												
											}
										} else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("LASTNAME")) {
											if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
												flag = true;
												errors.rejectValue("survey.questionList["+listCount+"]", "Last Name is required");
												
											}
										} else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("EMAILADDRESS")) {
											if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
												errors.rejectValue("survey.questionList["+listCount+"]", "Email Address is required");
												flag = true;
												
											}
											if ( !FieldsValidation.isEmailValid(aPIF.getAnswerText()) && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
												errors.rejectValue("survey.questionList["+listCount+"]", "Email Address is required");
												flag = true;
												
											}
										}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("MIDDLENAME")) {
											if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
												errors.rejectValue("survey.questionList["+listCount+"]", "Middle Name is required");
												flag = true;
												
											}
										}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("STREETADDRESS")) {
											if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
												errors.rejectValue("survey.questionList["+listCount+"]", "Street Address is required");
												flag = true;
												
											}
										}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("STREETADDRESS2")) {
											if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
												errors.rejectValue("survey.questionList["+listCount+"]", "Street Address2 is required");
												flag = true;
												
											}
										
										}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("OFFICEPHONE")) {
											if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
												errors.rejectValue("survey.questionList["+listCount+"]", "Office Phone is required");
												flag = true;
												
											}
										
										}else if (aPIF.getAvailablePersonalInformationfieldRef().getAvailablePersonalInformationfieldItem().getDbColumnName().equalsIgnoreCase("ZONE")) {
											if (aPIF.getAnswerText().isEmpty() && aPIF.getAvailablePersonalInformationfieldRef().isFieldsRequired()) {
												errors.rejectValue("survey.questionList["+listCount+"]", "Prefferd Zone is required");
												flag = true;
												
											}
										}
									}
									if( flag ){		
										errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
									}
								}else{		// if all answer required is selected
									for (com.softech.vu360.lms.web.controller.model.survey.AvailablePersonalInformationfield aPIF : personalInfoItems) {									
										if (aPIF.getAnswerText().isEmpty()) {										
											errors.rejectValue("survey.questionList["+listCount+"]", "First Name is required");											
										}
									}
								}
							}else if(surveyQuestion instanceof TextBoxSurveyQuestion){
								if(form.getSurvey().getQuestionList().get(listCount).getSurveyQuestionRef().getRequired()){
									selected = false;																
									if (!StringUtils.isBlank(form.getSurvey().getQuestionList().get(listCount).getAnswerText())) {
										selected = true ;
									}								
									if( selected == false ){ // if selected is still set to false
										errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
									}
								}							
								if(i==form.getSurvey().getQuestionList().size()){
									return;
								}
							}
	
	
						}
						listCount++;
					}
	
				}	
			}
		}
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.handleNoSuchRequestHandlingMethod(ex, request, response);
	}

	private boolean checkForFurtherProceesingRequirment(Map<Object, Object> context, BindException errors, HttpServletRequest request){
		if(  errors.getErrorCount() > 0 ){
			int nextPageIndex = Integer.parseInt(request.getParameter("nextPageIndex"));
			int questionNo = Integer.parseInt(request.getParameter("questionNo"));
			int totalQuestions =  Integer.parseInt(request.getParameter("totalQuestions"));
			log.debug("error count" + errors.getErrorCount());

			context.put("nextPageIndex", nextPageIndex);
			context.put("questionNo", questionNo);
			context.put("totalQuestions", totalQuestions);
			return true;
		}

		return false;
	}

	public ModelAndView showSurveyView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("in showSurveyView");
		TakeSurveyForm form = (TakeSurveyForm)command;
		int nextPageIndex = 0;
		int questionIndex = 0;
		int questionNo = 0;
		int totalQuestions = 0;
		int index = 0;
		int i;
		Long surveyId = form.getSurveyId();
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360User user = vu360UserService.getUserById(vu360User.getId());
		Learner learner = user.getLearner();
		form.setLearner(learner);
		com.softech.vu360.lms.model.Survey survey = surveyService.loadForUpdateSurvey(surveyId); //get the Survey object somehow????

		if(survey.getSections()!=null && !survey.getSections().isEmpty()) {
			log.debug("survey with a section found");
			return showSurveySectionView(survey, request, form);
		}
		
		SurveyResult surveyResult = surveyService.getSurveyResultByLearnerAndSurvey(user, survey);
		List<SurveyResultAnswer> surveyResultAnswers = null;
		if(surveyResult != null) {
			surveyResultAnswers = surveyResult.getAnswers();
		}

		if( checkForFurtherProceesingRequirment(context, errors,request)){

			return new ModelAndView(defaultTakeSurveyTemplate, "context",context);
		}

		if (request.getParameter("nextPageIndex") != null && !survey.getIsShowAll()) {
			index = Integer.parseInt(request.getParameter("nextPageIndex"));
			questionIndex = index*survey.getQuestionsPerPage();
			questionNo = index*survey.getQuestionsPerPage();;
		}

		SurveyQuestionBuilder builder = new SurveyQuestionBuilder(survey); 		
		List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
		Collections.sort(surveyQuestionList);
		List<Long> sqIndex = new ArrayList<Long>();
		for(i=questionIndex; i<surveyQuestionList.size(); i++){
			totalQuestions++;
			
			com.softech.vu360.lms.model.SurveyQuestion question = surveyQuestionList.get(i);
			if (survey.isRememberPriorResponse() && surveyResultAnswers != null && surveyResultAnswers.size() > 0) {
				for(SurveyResultAnswer surveyResultAnswer : surveyResultAnswers) {
					if (surveyResultAnswer.getQuestion().getId().compareTo(question.getId()) == 0) {
						if (question instanceof AggregateSurveyQuestion) {
							
							AggregateSurveyQuestion aggrtQuestion = (AggregateSurveyQuestion)question;
							List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems = surveyService.getAggregateSurveyQuestionItemsByQuestionId(aggrtQuestion.getId());
							if (aggregateSurveyQuestionItems != null) {
								builder.buildAggregateSurveyQuestion(aggrtQuestion, aggregateSurveyQuestionItems,surveyResultAnswer);
							}

						} else if (question instanceof PersonalInformationSurveyQuestion) {
							builder.buildPersonalInformationQuestion(question, surveyResultAnswer, learner);
						} else {
							builder.buildQuestion(question,surveyResultAnswer);
						}
					} 
				}
			} else {
				if (question instanceof AggregateSurveyQuestion) {
					
					AggregateSurveyQuestion aggrtQuestion = (AggregateSurveyQuestion)question;
					List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems = surveyService.getAggregateSurveyQuestionItemsByQuestionId(aggrtQuestion.getId());
					if (aggregateSurveyQuestionItems != null) {
						builder.buildAggregateSurveyQuestion(aggrtQuestion, aggregateSurveyQuestionItems,null);
					}

				} else if (question instanceof PersonalInformationSurveyQuestion) {
					builder.buildPersonalInformationQuestion(question, null, null);
				} else {
					builder.buildQuestion(question,null);
				}
			}
			if (!survey.getIsShowAll() && totalQuestions == survey.getQuestionsPerPage() ) {
				i++;
				break;
			}
		}
		com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = builder.getSurveyView();
		//set it up in the form object
		if( form.getSurvey() == null   || request.getParameter("launch") != null )
			form.setSurvey(surveyView);
		else
			form.getSurvey().getQuestionList().addAll(surveyView.getQuestionList());

		Set<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> set = new HashSet<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion>(form.getSurvey().getQuestionList());
		ArrayList<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> newUniqueQuestionList = new ArrayList<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion>(set);
		//Added to save the custom question responses to the db
		Collections.sort(newUniqueQuestionList);
		for(int j=0;j<newUniqueQuestionList.size();j++){
			if(newUniqueQuestionList.get(j).getSurveyQuestionRef() instanceof AggregateSurveyQuestion){
				sqIndex.add((long) j);
			}
		}
		int z=0;
		for(int k=0;k<newUniqueQuestionList.size();k++){
			com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion squestion =newUniqueQuestionList.get(k);
				if(squestion.getSurveyQuestionRef() instanceof AggregateSurveyQuestion){
					newUniqueQuestionList.get(k).setIndex(sqIndex.get(z));
					z++;
				}
		}
		
		form.getSurvey().setQuestionList(newUniqueQuestionList);

		if (i == surveyQuestionList.size() || survey.getIsShowAll()) 
			nextPageIndex = 0;
		else
			nextPageIndex = index+1;
		boolean showTerms = surveyView.getSurveyRef().isElectronicSignatureRequire();
        if( showTerms )
            context.put("showTerms", surveyView.getSurveyRef().getElectronicSignature());
        else
            context.put("showTerms", "false");

        boolean showLinks = surveyView.getSurveyRef().isLinkSelected();
        if( showLinks )
            context.put("showLinks", surveyService.getSurveyLinksById(surveyId));
        else
            context.put("showLinks", "false");

		context.put("totalQuestions", totalQuestions);
		context.put("nextPageIndex", nextPageIndex);
		context.put("questionNo", questionNo);


		return new ModelAndView(defaultTakeSurveyTemplate, "context",context);
	}

	protected SurveySectionVO populateSectionVO(SurveySection section, com.softech.vu360.lms.web.controller.model.survey.Survey surveyVO) {
		SurveySectionVO vo = new SurveySectionVO();
		vo.setId(section.getId());
		vo.setName(section.getName());
		vo.setDescription(section.getDescription());
		
		for(SurveySectionSurveyQuestionBank frameworkMapping : section.getSurveySectionSurveyQuestionBanks()) {
			SurveyQuestionBank framework = frameworkMapping.getSurveyQuestionBank();
			SurveyQuestionBankVO surveyQuestionBankVO = new SurveyQuestionBankVO();
			surveyQuestionBankVO.setId(framework.getId());
			surveyQuestionBankVO.setName(framework.getName());
			surveyQuestionBankVO.setDescription(framework.getDescription());
			vo.getSurveyQuestionBanks().add(surveyQuestionBankVO);
			
			for(SurveyQuestion surveyQuestion : framework.getQuestions()) {
				com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion questionVO = new com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion(surveyQuestion);
				String questionClassName = questionVO.getSurveyQuestionRef().getClass().getName();
				if( questionClassName.equalsIgnoreCase(MultipleSelectSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.MULTICHOICE_MULTISELECT_TEMPLATE);
				} else if ( questionClassName.equalsIgnoreCase(SingleSelectSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.MULTICHOICE_SINGLESELECT_TEMPLATE);
				} else if ( questionClassName.equalsIgnoreCase(TextBoxSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.COMMENT_UNLIMITEDTEXT_TEMPLATE);
				} else if ( questionClassName.equalsIgnoreCase(PersonalInformationSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.PERSONAL_INFORMATION_TEMPLATE);
				} else if ( questionClassName.equalsIgnoreCase(AggregateSurveyQuestion.class.getName()) ) {
					questionVO.setTemplatePath(SurveyQuestionBuilder.CUSTOM_MULTICHOICE_MULTISELECT_TEMPLATE);
				}
				questionVO.setCanHaveFile(surveyQuestion.isCanHaveFile());
				questionVO.setFileRequired(surveyQuestion.isFileRequired());
				
				for(SurveyAnswerItem answerItem : surveyQuestion.getSurveyAnswers()) {
					com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerVO = new com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem(answerItem);
					questionVO.getAnswerItems().add(answerVO);
				}
				
				surveyQuestionBankVO.getSurveyQuestions().add(questionVO);
				surveyVO.getQuestionList().add(questionVO);
			}
		}
		
		if(section.getChildren()!=null && section.getChildren().size()>0) {
			for(SurveySection childSection : section.getChildren()) {
				vo.getChildren().add( populateSectionVO(childSection, surveyVO) );
			}
		}
		
		return vo;
	}
	
	protected Map<String, com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> getSurveyQuesitonMap(List<SurveySectionVO> surveySectionVOs) {
		log.debug("in getSurveyQuesitonMap");
		Map<String, com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> questionMapping = new LinkedHashMap<String, com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion>();
		for(SurveySectionVO surveySectionVO : surveySectionVOs) {
			Long sectionId = surveySectionVO.getId();
			for(SurveyQuestionBankVO surveyQuestionBankVO : surveySectionVO.getSurveyQuestionBanks()) {
				Long frameworkId = surveyQuestionBankVO.getId();
				for(com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion quesitonVO : surveyQuestionBankVO.getSurveyQuestions()) {
					Long questionId = quesitonVO.getSurveyQuestionRef().getId();
					String key = sectionId + "-" + frameworkId + "-" + questionId;
					questionMapping.put(key, quesitonVO);
				}
			}
			
			if(surveySectionVO.getChildren()!=null && surveySectionVO.getChildren().size()>0) {
				Map<String, com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> childrenQuestionMapping = getSurveyQuesitonMap(surveySectionVO.getChildren());
				questionMapping.putAll(childrenQuestionMapping);
			}
		}
		
		log.debug("returning questionMapping with size " + questionMapping.size());
		return questionMapping;
	}
	
	protected void repopulateAnswers(Map<String, com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> questionMap, HttpServletRequest request) {
		log.debug("in repopulateAnswers");
		String [] questionsArray = request.getParameterValues("questionsArray");
		String [] sectionsArray = request.getParameterValues("sectionsArray");
		String [] questionBanksArray = request.getParameterValues("questionBanksArray");
		if(questionsArray!=null && sectionsArray!=null && questionBanksArray!=null) {
			for(int idx=0; idx<questionsArray.length; ++idx) {
				Long sectionId = Long.parseLong(sectionsArray[idx]);
				Long frameworkId = Long.parseLong(questionBanksArray[idx]);
				Long questionId = Long.parseLong(questionsArray[idx]);
				String key = sectionId + "-" + frameworkId + "-" + questionId;
				com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion questionVO = questionMap.get(key);
				log.debug("key=" + key + " => " + questionVO);
				
				if(questionVO!=null) {
					SurveyQuestion surveyQuestion = questionVO.getSurveyQuestionRef();
					if(surveyQuestion instanceof CommentSurveyQuestion || surveyQuestion instanceof TextBoxSurveyQuestion) {
						String answerText = request.getParameter("survey.questionList[" + idx + "].answerText");
						questionVO.setAnswerText(answerText);
					} else if(surveyQuestion instanceof MultipleSelectSurveyQuestion) {
						for(int answerIdx=0; answerIdx<questionVO.getAnswerItems().size(); ++answerIdx) {
							com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItemVO = questionVO.getAnswerItems().get(answerIdx);
							String selectedAnswer = request.getParameter("survey.questionList[" + idx + "].answerItems[" + answerIdx + "].selected");
							if(selectedAnswer!=null && (Boolean.TRUE.toString().compareToIgnoreCase(selectedAnswer)==0)) {
								answerItemVO.setSelected(true);
							}
						}
					} else if(surveyQuestion instanceof SingleSelectSurveyQuestion) {
						String selectedAnswerId = request.getParameter("survey.questionList[" + idx + "].singleSelectAnswerId");
						if(selectedAnswerId!=null) {
							questionVO.setSingleSelectAnswerId(Long.parseLong(selectedAnswerId));
						}
					}
				}
				
			}
		}
		log.debug("populated answers");
	}
	
	public ModelAndView showSurveySectionView(com.softech.vu360.lms.model.Survey survey, HttpServletRequest request, TakeSurveyForm form) {
		log.debug("in showSurveySectionView");
		com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360User user = vu360UserService.getUserById(vu360User.getId());
		Learner learner = user.getLearner();
		form.setLearner(learner);
		form.setSurveyee(user);
		com.softech.vu360.lms.web.controller.model.survey.Survey surveyVO = new com.softech.vu360.lms.web.controller.model.survey.Survey(survey);
		form.setSurvey(surveyVO);
		Map<Object, Object> context = new HashMap<Object, Object>();
		SurveyPreviewVO surveyPreviewVO = new SurveyPreviewVO();
		surveyPreviewVO.setSurvey(survey);

		List<SurveySection> surveySections = surveyService.getRootSurveySections(survey);
		for(SurveySection surveySection : surveySections) {
			SurveySectionVO surveySectionVO = populateSectionVO(surveySection, surveyVO);
			surveyPreviewVO.getSurveySectionVO().add(surveySectionVO);
		}
		
		Map<String, com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> questionMap = getSurveyQuesitonMap(surveyPreviewVO.getSurveySectionVO());
		repopulateAnswers(questionMap, request);
		
		context.put("survey", surveyPreviewVO);
		return new ModelAndView(defaultTakeSurveySectionTemplate, "context", context);
	}
	
	protected SurveySection createClone(SurveySection surveySection, SurveySection parentSection, PredictSurveySeciton predictSurveySeciton) {
		SurveySection surveySectionClone = new SurveySection();
		
		surveySectionClone.setName(surveySection.getName());
		surveySectionClone.setDescription(surveySection.getDescription());
		surveySectionClone.setSurvey(surveySection.getSurvey());
		surveySectionClone.setParent(parentSection);
		surveySectionClone = surveyService.saveSurveySection(surveySectionClone);
		PredictSurveyQuestionMapping tmpMapping = surveyService.getPredictSurveyQuestionMappingBySectionId(surveySection.getId());
		
		PredictSurveyQuestionMapping sectionMapping = new PredictSurveyQuestionMapping();
		sectionMapping.setSurveyId(tmpMapping.getSurveyId());
		sectionMapping.setLmsSurveyId(surveySection.getSurvey().getId());
		sectionMapping.setSurveySectionId(predictSurveySeciton.getId().toString());
		sectionMapping.setLmsSectionId(surveySectionClone.getId());
		sectionMapping = surveyService.savePredictSurveyQuestionMapping(sectionMapping);
		
		log.debug("sectionId " + surveySection.getId() + " ==>> cloned sectionId=" + surveySectionClone.getId());
		
		if(surveySection.getChildren()!=null && surveySection.getChildren().size()>0) {
			for(int idx=0; idx<surveySection.getChildren().size(); ++idx) {
				SurveySection childSurveySection = surveySection.getChildren().get(idx);
				PredictSurveySeciton childPredictSurveySeciton = predictSurveySeciton.getChildren().get(idx);
				SurveySection clonedChildSection = createClone(childSurveySection, surveySectionClone, childPredictSurveySeciton);
				surveySectionClone.getChildren().add(clonedChildSection);
			}
		}
		
		for(SurveySectionSurveyQuestionBank frameworkMapping : surveySection.getSurveySectionSurveyQuestionBanks()) {
			SurveySectionSurveyQuestionBank clonedMapping = new SurveySectionSurveyQuestionBank();
			clonedMapping.setSurveyQuestionBank(frameworkMapping.getSurveyQuestionBank());
			clonedMapping.setSurveySection(surveySectionClone);
			clonedMapping = surveyService.saveSurveySectionSurveyQuestionBank(clonedMapping);
			
			Integer cloneNumber = null;
			for(SurveyQuestion surveyQuestion : clonedMapping.getSurveyQuestionBank().getQuestions()) {
				PredictSurveyQuestionMapping oldMapping = surveyService.getPredictSurveyQuestionMapping(surveySection.getSurvey().getId(), surveySection.getId().toString(), clonedMapping.getSurveyQuestionBank().getId(), surveyQuestion.getId());
				if(cloneNumber==null) {
					cloneNumber = surveyService.getNextSectionCloneNumber(oldMapping.getSurveyId(), oldMapping.getLmsSurveyId(), oldMapping.getSurveySectionId());
				}
				
				PredictSurveyQuestionMapping predictMapping = new PredictSurveyQuestionMapping();
				predictMapping.setSurveyId(oldMapping.getSurveyId());
				predictMapping.setLmsSurveyId(surveySectionClone.getSurvey().getId());
				predictMapping.setSurveySectionId(predictSurveySeciton.getId().toString());
				predictMapping.setLmsSectionId(surveySectionClone.getId());
				predictMapping.setFrameworkId(oldMapping.getFrameworkId());
				predictMapping.setLmsFrameworkId(oldMapping.getLmsFrameworkId());
				predictMapping.setQuestionId(oldMapping.getQuestionId());
				predictMapping.setLmsQuestionId(oldMapping.getLmsQuestionId());
				predictMapping.setClones(cloneNumber);
				predictMapping = surveyService.savePredictSurveyQuestionMapping(predictMapping);
				log.debug("PredictSurveyQuestionMapping id=" + predictMapping.getId() + " surveyId=" + predictMapping.getLmsSurveyId() + " sectionId=" + predictMapping.getLmsSectionId() + " frameworkId=" + predictMapping.getLmsFrameworkId() + " questionId=" + predictMapping.getLmsQuestionId() + " clones=" + predictMapping.getClones());
			}
		}
		
		surveySectionClone = surveyService.saveSurveySection(surveySectionClone);
		return surveySectionClone;
	}

	public ModelAndView removeUploadedFile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) {
		log.debug("in removeUploadedFile");
		ModelAndView modelAndView = new ModelAndView("json");
		modelAndView.addObject("result", false);
		TakeSurveyForm form = (TakeSurveyForm) command;
		String strFileId = ServletRequestUtils.getStringParameter(request, "fileId", "N");
		log.debug("got fileId=" + strFileId);
		if(strFileId.compareToIgnoreCase("N")!=0) {
			String strIds[] = strFileId.split("~");
			String strKey = strIds[0];
			String strFileIdx = strIds[1];
			int fileIdx = Integer.parseInt(strFileIdx);
			List<SurveyResultAnswerFile> files = form.getAnswerFilesMap().get(strKey);
			if(files!=null && files.size()>0 && fileIdx<=files.size()) {
				SurveyResultAnswerFile surveyResultAnswerFile = files.remove(fileIdx);
				String filePath = surveyResultAnswerFile.getFilePath();
				File file = new File(filePath);
				FileUtils.deleteQuietly(file);
				modelAndView.addObject("result", true);
			}
		}
		return modelAndView;
	}
	
	public ModelAndView cloneSection(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("in cloneSection");
		TakeSurveyForm form = (TakeSurveyForm) command;
		Long surveyId = ServletRequestUtils.getLongParameter(request, "surveyId", 0);
		Long surveySectiondId = ServletRequestUtils.getLongParameter(request, "surveySectiondId", 0);
		logger.debug("surveyId="+surveyId+" surveySectiondId="+surveySectiondId);
		Survey survey = surveyService.loadForUpdateSurvey(surveyId);
		
		if(surveyId.longValue()>0L && surveySectiondId.longValue()>0L && survey.isInspection()) {
			PredictSurveyQuestionMapping psqMapping = surveyService.getPredictSurveyQuestionMappingBySectionId(surveySectiondId);
			
			String path = VU360Properties.getVU360Property("lms.predict.ws.endpoint.host") + "/predict360/ws/restapi/surveyservice/clone/";
	        int timeout = 30*60*1000;
			String callResponse = new RestClient().get(path+psqMapping.getSurveySectionId(), timeout);
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(callResponse);
			logger.debug(jsonObject);
			JSONArray output = (JSONArray) jsonObject.get("output");
			jsonObject = (JSONObject) output.get(0);
			PredictSurveySeciton predictSurveySeciton = jsonToSectionBean(jsonObject);
			
			SurveySection surveySection = surveyService.getSurveySectionByID(surveySectiondId);
			createClone(surveySection, surveySection.getParent(), predictSurveySeciton);
			/*
			 * TSM - Technology Stack Migration
			 * 
			 * Following code piece was written to enforce
			 * cache update and such calls are now unnecessary.  
			 * Cache must not be enforced in such manner, therefore
			 * this code has been taken off
			 * 
			 * */
			//surveyService.refreshSurveys(surveyId);
			
			Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				fileMap = multipartRequest.getFileMap();
				String [] questionsArray = request.getParameterValues("questionsArray");
				String [] sectionsArray = request.getParameterValues("sectionsArray");
				String [] questionBanksArray = request.getParameterValues("questionBanksArray");
				
				for(int idx=0; idx<questionsArray.length; ++idx) {
					long sectionId = Long.parseLong(sectionsArray[idx]);
					long frameworkId = Long.parseLong(questionBanksArray[idx]);
					long questionId = Long.parseLong(questionsArray[idx]);
					surveySection = surveyService.getSurveySectionByID(sectionId);
					SurveyQuestionBank surveyQuestionBank = surveyService.getSurveyQuestionBankById(frameworkId);
					com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion questionVO = form.getSurvey().getQuestionList().get(idx);
					if(questionVO.getSurveyQuestionRef().getId().longValue()!=questionId) { // sanity checking for safe side
						logger.error("was expection questionId=" + questionId + " but got " + questionVO.getSurveyQuestionRef().getId());
					}
					SurveyQuestion surveyQuestion = questionVO.getSurveyQuestionRef();
					
					int fileIdx = 0;
					String key = surveySection.getId() + "-" + surveyQuestionBank.getId() + "-" + surveyQuestion.getId();
					MultipartFile file = fileMap.get("file-" + key + "[" + fileIdx + "]");
					while(file!=null && file.getSize()>0) {
						String folderPath = SURVEY_QUESTION_FILE_UPLOAD_DIR + File.separatorChar + surveySection.getSurvey().getId() + File.separatorChar + form.getSurveyee().getId();
						File directory = new File(folderPath);
						directory.mkdirs();
						String fileName = file.getOriginalFilename();
						String filePath = folderPath + File.separatorChar + System.nanoTime() + "_" + fileName;
						File uploadedFile = new File(filePath);
						file.transferTo(uploadedFile);
						filePath = uploadedFile.getAbsolutePath();
						SurveyResultAnswerFile resultAnswerFile = new SurveyResultAnswerFile();
						resultAnswerFile.setFilePath(filePath);
						resultAnswerFile.setFileName(fileName);
						
						List<SurveyResultAnswerFile> files = form.getAnswerFilesMap().get(key);
						if(files==null) {
							files = new LinkedList<SurveyResultAnswerFile>();
							form.getAnswerFilesMap().put(key, files);
						}
						
						files.add(resultAnswerFile);
						++fileIdx;
						file = fileMap.get("file-" + key + "[" + fileIdx + "]");
					}
				}
			}
		}
		
		return showSurveySectionView(survey, request, form);
	}
	
	protected PredictSurveySeciton jsonToSectionBean(JSONObject jsonObject) {
		PredictSurveySeciton predictSurveySeciton = new PredictSurveySeciton();
		predictSurveySeciton.setId(Long.parseLong(jsonObject.get("id").toString()));
		JSONArray children = (JSONArray) jsonObject.get("children");
		for (Object childObj : children) {
			JSONObject childJsonObject = (JSONObject) childObj; 
			PredictSurveySeciton child = jsonToSectionBean(childJsonObject);
			predictSurveySeciton.getChildren().add(child);
		}
		return predictSurveySeciton;
	}
	
	public ModelAndView cancelDisplaySurvey(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(closeTemplate);
	}

	public ModelAndView finishSectionSurvey(HttpServletRequest request, HttpServletResponse response, TakeSurveyForm form, BindException errors) throws Exception {
		log.debug("in finishSectionSurvey");
		String [] questionsArray = request.getParameterValues("questionsArray");
		String [] sectionsArray = request.getParameterValues("sectionsArray");
		String [] questionBanksArray = request.getParameterValues("questionBanksArray");
		if(questionsArray!=null && sectionsArray!=null && questionBanksArray!=null) {
			log.debug("sectionsArray="+sectionsArray.length + " questionBanksArray=" + questionBanksArray.length + " questionsArray=" + questionsArray.length);
			
			Map<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();
			
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				fileMap = multipartRequest.getFileMap();
			}
			
			SurveyResult surveyResult = new SurveyResult();
			surveyResult.setSurvey(form.getSurvey().getSurveyRef());
			surveyResult.setSurveyee(form.getSurveyee());
			surveyResult.setResponseDate(new Date());
			List<SurveyResultAnswer> surveyResultAnswers = surveyResult.getAnswers();
			for(int idx=0; idx<questionsArray.length; ++idx) {
				long sectionId = Long.parseLong(sectionsArray[idx]);
				long frameworkId = Long.parseLong(questionBanksArray[idx]);
				long questionId = Long.parseLong(questionsArray[idx]);
				
				com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion questionVO = form.getSurvey().getQuestionList().get(idx);
				if(questionVO.getSurveyQuestionRef().getId().longValue()!=questionId) { // sanity checking for safe side
					logger.error("was expection questionId=" + questionId + " but got " + questionVO.getSurveyQuestionRef().getId());
				}
				
				SurveyQuestion surveyQuestion = questionVO.getSurveyQuestionRef();
				SurveySection surveySection = surveyService.getSurveySectionByID(sectionId);
				SurveyQuestionBank surveyQuestionBank = surveyService.getSurveyQuestionBankById(frameworkId);
				
				SurveyResultAnswer surveyResultAnswer = new SurveyResultAnswer();
				surveyResultAnswer.setSurveyResult(surveyResult);
				surveyResultAnswer.setSurveySection(surveySection);
				surveyResultAnswer.setSurveyQuestionBank(surveyQuestionBank);
				surveyResultAnswer.setQuestion(surveyQuestion);
				
				List<SurveyResultAnswerFile> answerFiles = surveyResultAnswer.getSurveyResultAnswerFiles();
				String folderPath = SURVEY_QUESTION_FILE_UPLOAD_DIR + File.separatorChar + surveySection.getSurvey().getId() + File.separatorChar + form.getSurveyee().getId();
				File directory = new File(folderPath);
				directory.mkdirs();
				int fileIdx = 0;
				String key =  surveySection.getId() + "-" + surveyQuestionBank.getId() + "-" + surveyQuestion.getId();
				MultipartFile file = fileMap.get("file-" + key + "[" + fileIdx + "]");
				log.debug("file for =" + "file-" + key + "[" + fileIdx + "] is " + file);
				while(file!=null && file.getSize()>0) {
					String fileName = file.getOriginalFilename();
					String filePath = folderPath + File.separatorChar + System.nanoTime() + "_" + fileName;
					File uploadedFile = new File(filePath);
					file.transferTo(uploadedFile);
					filePath = uploadedFile.getAbsolutePath();
					SurveyResultAnswerFile resultAnswerFile = new SurveyResultAnswerFile();
					resultAnswerFile.setSurveyResultAnswer(surveyResultAnswer);
					answerFiles.add(resultAnswerFile);
					resultAnswerFile.setFilePath(filePath);
					resultAnswerFile.setFileName(fileName);
					
					++fileIdx;
					file = fileMap.get("file-" + key + "[" + fileIdx + "]");
					log.debug("file for =" + "file-" + key + "[" + fileIdx + "] is " + file);
				}
				List<SurveyResultAnswerFile> otherFiles = form.getAnswerFilesMap().get(key);
				if(otherFiles!=null) {
					for (SurveyResultAnswerFile surveyResultAnswerFile : otherFiles) {
						surveyResultAnswerFile.setSurveyResultAnswer(surveyResultAnswer);
						answerFiles.add(surveyResultAnswerFile);
					}
				}
				
				if(surveyQuestion instanceof CommentSurveyQuestion || surveyQuestion instanceof TextBoxSurveyQuestion) {
					surveyResultAnswer.setSurveyAnswerText(questionVO.getAnswerText());
				} else if(surveyQuestion instanceof MultipleSelectSurveyQuestion) {
					for(com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItemVO : questionVO.getAnswerItems()) {
						if(answerItemVO.isSelected()) {
							SurveyAnswerItem answerItem = answerItemVO.getSurveyAnswerItemRef();
							surveyResultAnswer.getSurveyAnswerItems().add(answerItem);
						}
					}
				} else if(surveyQuestion instanceof SingleSelectSurveyQuestion) {
					Long answerId = questionVO.getSingleSelectAnswerId();
					if(answerId!=null) {
						SurveyAnswerItem answerItem = surveyService.getAnswerItemById(answerId);
						surveyResultAnswer.getSurveyAnswerItems().add(answerItem);
					}
				}
				
				// add only if there was an answer otherwise don't 
				if((surveyResultAnswer.getSurveyAnswerText()!=null && !surveyResultAnswer.getSurveyAnswerText().isEmpty()) || (surveyResultAnswer.getSurveyAnswerItems().size()>0)) {
					surveyResultAnswers.add(surveyResultAnswer);
				}
			}
			surveyResult = surveyService.saveSurveyResult(surveyResult);
		}
		return new ModelAndView(closeTemplate);
	}

	public ModelAndView finishSurvey(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("in finishSurvey");
		TakeSurveyForm form = (TakeSurveyForm)command;
		if(form.getSurvey().getSurveyRef().getSections()!=null && form.getSurvey().getSurveyRef().getSections().size()>0) {
			return finishSectionSurvey(request, response, form, errors);
		} else {
			Map<Object, Object> context = new HashMap<Object, Object>();
			if( checkForFurtherProceesingRequirment(context, errors,request)){
	
				return new ModelAndView(defaultTakeSurveyTemplate, "context",context);
			}	 
	
			com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VU360User user = vu360UserService.getUserById(vu360User.getId());
			surveyService.addSurveyResult(user,form);
			Map<String,Object> questionAnswerMap = surveyService.getSurveyQASummary( user, form);
			if( form.getSurvey().getSurveyRef().getEvent().equalsIgnoreCase("survey.event.manual")) // if manual survey
				sendEmailToManager(user, form.getSurvey().getSurveyRef(),questionAnswerMap ,request) ;
			return new ModelAndView(closeTemplate);
		}
	}

	private void sendEmailToManager(VU360User loggedInUser, Survey survey , Map<String,Object> questionAnswerMap, HttpServletRequest request ){
		Map<String,Object> model = new  HashMap<String,Object>();
		Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		String surveyAssignmentManagerTemplatePath =  brander.getBrandElement("lms.email.managerSurveyAssignment.surveyCompletion.body");
		String fromAddress =  brander.getBrandElement("lms.email.managerSurveyAssignment.fromAddress");
		String fromCommonName =  brander.getBrandElement("lms.email.managerSurveyAssignment.fromCommonName");
		String subject =  loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + " completed survey " + survey.getName();
		String learner =  loggedInUser.getFirstName() + " " + loggedInUser.getLastName()  ;
		model.put("title", subject);
		model.put("learner", learner);
		model.put("questionAnswerMap", questionAnswerMap);
		
		String lmsDomain="";
		lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
		model.put("lmsDomain",lmsDomain);
		model.put("brander",brander);
		
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, surveyAssignmentManagerTemplatePath, model);
		VU360User manager = surveyService.getManagerOfSurveyLearner( loggedInUser, survey);
		SendMailService.sendSMTPMessage(manager.getEmailAddress(),	fromAddress, fromCommonName, subject, text);
	}

	public String getDefaultTakeSurveySectionTemplate() {
		return defaultTakeSurveySectionTemplate;
	}

	public void setDefaultTakeSurveySectionTemplate(
			String defaultTakeSurveySectionTemplate) {
		this.defaultTakeSurveySectionTemplate = defaultTakeSurveySectionTemplate;
	}

	public String getDefaultTakeSurveyTemplate() {
		return defaultTakeSurveyTemplate;
	}

	public void setDefaultTakeSurveyTemplate(String defaultTakeSurveyTemplate) {
		this.defaultTakeSurveyTemplate = defaultTakeSurveyTemplate;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

}
