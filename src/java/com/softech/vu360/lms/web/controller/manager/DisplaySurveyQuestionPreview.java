package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.AggregateSurveyQuestion;
import com.softech.vu360.lms.model.AggregateSurveyQuestionItem;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.PersonalInformationSurveyQuestion;
import com.softech.vu360.lms.model.PredictSurveyQuestionMapping;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.SurveyQuestionBank;
import com.softech.vu360.lms.model.SurveySection;
import com.softech.vu360.lms.model.SurveySectionSurveyQuestionBank;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.model.predict.PredictSurveySeciton;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.vo.FrameWorkRequirementVO;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.SurveyPreviewVO;
import com.softech.vu360.lms.vo.SurveyQuestionBankVO;
import com.softech.vu360.lms.vo.SurveySectionVO;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.SurveyQuestionBuilder;
import com.softech.vu360.lms.web.controller.model.survey.TakeSurveyForm;
import com.softech.vu360.lms.webservice.client.impl.RestClient;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class DisplaySurveyQuestionPreview extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(DisplaySurveyQuestionPreview.class.getName());

	private SurveyService surveyService = null;
	private VelocityEngine velocityEngine;

	private String defaultTakeSurveyTemplate;
	private String defaultSurveyPrintTemplate;
	private String defaultSectionTakeSurveyTemplate;
	private String surveySectionTemplate;
	private String closeTemplate;
	private String frameworkRequirements;

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		// Auto-generated method stub
	}

	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		// Auto-generated method stub
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		// Auto-generated method stub
		return super.handleNoSuchRequestHandlingMethod(ex, request, response);
	}

	public ModelAndView showSurveyView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) 
	throws Exception {
		log.debug("in showSurveyView");
		int i;
		int j = 0;
		int index = 0;
		int questionNo = 0;
		int nextPageIndex = 0;
		int questionIndex = 0;
		TakeSurveyForm form = (TakeSurveyForm)command;
		List<Long> sqIndex = new ArrayList<Long>();
		Long surveyId = form.getSurveyId();
		Map<Object, Object> context = new HashMap<Object, Object>();
		com.softech.vu360.lms.model.Survey survey = surveyService.getSurveyByID(surveyId); //get the Survey object somehow????
		String printPreview = request.getParameter("printPreview");
		if(printPreview != null && printPreview.trim().equals("true")){
			survey.setIsShowAll(true);
		}
		if(survey.getSections()!= null && !survey.getSections().isEmpty()) {
			log.debug("Hurray!!! survey section found");
			return displaySurveySection(survey, request, form);
			
		} else {
			if( request.getParameter("nextPageIndex") != null && !survey.getIsShowAll() ) {
				index = Integer.parseInt(request.getParameter("nextPageIndex"));
				questionIndex = index*survey.getQuestionsPerPage();
				questionNo = index*survey.getQuestionsPerPage();;
			}

			SurveyQuestionBuilder builder = new SurveyQuestionBuilder(survey); 

			List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
			Collections.sort(surveyQuestionList);

			for( i = questionIndex ; i < surveyQuestionList.size() ; i++ ) {
				j++;
				com.softech.vu360.lms.model.SurveyQuestion question = surveyQuestionList.get(i);
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
				if( !survey.getIsShowAll() && j == survey.getQuestionsPerPage() && printPreview ==null ) {
					i++;
					break;
				}
			}
			com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = builder.getSurveyView();
			for(int z=0;z<surveyView.getQuestionList().size();z++){
				if(surveyView.getQuestionList().get(z).getSurveyQuestionRef() instanceof AggregateSurveyQuestion){
					sqIndex.add((long) z);
				}
			}
			
			String defaultTemplate = "";
			if(request.getParameter("printPreview") == null){
				int z=0;
				for(int k=0;k<surveyView.getQuestionList().size();k++){
					com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion squestion =surveyView.getQuestionList().get(k);
						if(squestion.getSurveyQuestionRef() instanceof AggregateSurveyQuestion){
							surveyView.getQuestionList().get(k).setIndex(sqIndex.get(z));
							z++;
						}
				}
				//set it up in the form object
				form.setSurvey(surveyView);
				if( i == surveyQuestionList.size() || survey.getIsShowAll() ) 
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

				context.put("nextPageIndex", nextPageIndex);
				context.put("questionNo", questionNo);
				defaultTemplate = defaultTakeSurveyTemplate;
			}else{
				form.setSurvey(surveyView);
				defaultTemplate = defaultSurveyPrintTemplate;
			}
			context.put("nextPageIndex", nextPageIndex);
			context.put("questionNo", questionNo);
			
			return new ModelAndView(defaultTemplate, "context",context);
		}
		
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
	
	public ModelAndView displaySurveySection(Survey survey, HttpServletRequest request, TakeSurveyForm form) {
		log.debug("in displaySurveySection");
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
		
		context.put("survey", surveyPreviewVO);
		
		return new ModelAndView(defaultSectionTakeSurveyTemplate, "context", context);
	}

	public ModelAndView sendMail(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		log.debug("IN SEND MAIL METHOD");
		TakeSurveyForm form = (TakeSurveyForm)command;
		Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		Map<Object, Object> model = new HashMap <Object, Object>();
		if( ! StringUtils.isBlank(request.getParameter("mailAddress")) && form.getSurvey() != null ) {
			
			model.put("surveyName", form.getSurvey().getSurveyRef().getName());
			//String enrolmentTemplatePath = brander.getBrandElement("lms.email.surveyLaunch.body");
			String documentPath = VU360Properties.getVU360Property("lms.loginURL");
			String fromAddress = brander.getBrandElement("lms.email.managerenrollment.fromAddress");
			String fromCommonName = "";
			String subject = "";
			String text = documentPath+"/anonymousSurvey.do?method=showSurveyView&surveyId="+form.getSurveyId();
			log.debug("MAIL -  "+request.getParameter("mailAddress"));
			SendMailService.sendSMTPMessage( request.getParameter("mailAddress"), fromAddress, fromCommonName, subject, text );
		}
		return this.backDisplaySurvey(request, response, command, errors);
	}

	public ModelAndView backDisplaySurvey(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(closeTemplate);
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
	
	public ModelAndView cloneSection(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("in cloneSection");
		Long surveyId = ServletRequestUtils.getLongParameter(request, "surveyId", 0);
		Long surveySectiondId = ServletRequestUtils.getLongParameter(request, "surveySectiondId", 0);
		logger.debug("surveyId="+surveyId+" surveySectiondId="+surveySectiondId);
		
		if(surveyId.longValue()>0L && surveySectiondId.longValue()>0L) {
			
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
		}
		return new ModelAndView("redirect:mgr_SurveyQuestionPreview.do?method=showSurveyView&surveyId="+surveyId);
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
	
	public ModelAndView frameworkRequirements(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		Long frameworkId = Long.valueOf(request.getParameter("frameworkId"));
		if(frameworkId.longValue()>0L) {
			PredictSurveyQuestionMapping predictSurveyQuestionMapping = surveyService.getPredictSurveyQuestionMappingByFrameworkId(frameworkId);
			String path = VU360Properties.getVU360Property("lms.predict.ws.endpoint.host") + "/predict360/ws/restapi/requirementservice/frameworkrequirement/";
	        int timeout = 30*60*1000;
	        JSONObject jsonObject = new RestClient().getForObject(path+predictSurveyQuestionMapping.getFrameworkId(), timeout);
	        logger.debug(jsonObject);
	        JSONArray jsonArray = jsonObject.getJSONArray("output");
	        Collection fwReqCol = (Collection)jsonArray.toCollection(jsonArray, FrameWorkRequirementVO.class);
	        context.put("requirements", new ArrayList<FrameWorkRequirementVO>(fwReqCol));
		}
		
		return new ModelAndView(frameworkRequirements, "context",context);
	}


	/**
	 * @return the surveyService
	 */
	public SurveyService getSurveyService() {
		return surveyService;
	}

	/**
	 * @param surveyService the surveyService to set
	 */
	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	/**
	 * @return the defaultTakeSurveyTemplate
	 */
	public String getDefaultTakeSurveyTemplate() {
		return defaultTakeSurveyTemplate;
	}

	/**
	 * @param defaultTakeSurveyTemplate the defaultTakeSurveyTemplate to set
	 */
	public void setDefaultTakeSurveyTemplate(String defaultTakeSurveyTemplate) {
		this.defaultTakeSurveyTemplate = defaultTakeSurveyTemplate;  
	}
	
	public String getDefaultSurveyPrintTemplate() {
		return defaultSurveyPrintTemplate;
	}

	public void setDefaultSurveyPrintTemplate(String defaultSurveyPrintTemplate) {
		this.defaultSurveyPrintTemplate = defaultSurveyPrintTemplate;
	}


	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public String getSurveySectionTemplate() {
		return surveySectionTemplate;
	}

	public void setSurveySectionTemplate(String surveySectionTemplate) {
		this.surveySectionTemplate = surveySectionTemplate;
	}

	public String getDefaultSectionTakeSurveyTemplate() {
		return defaultSectionTakeSurveyTemplate;
	}

	public void setDefaultSectionTakeSurveyTemplate(
			String defaultSectionTakeSurveyTemplate) {
		this.defaultSectionTakeSurveyTemplate = defaultSectionTakeSurveyTemplate;
	}
	
	public String getFrameworkRequirements() {
		return frameworkRequirements;
	}

	public void setFrameworkRequirements(String frameworkRequirements) {
		this.frameworkRequirements = frameworkRequirements;
	}

}