package com.softech.vu360.lms.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyLearner;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.TextBoxSurveyQuestion;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.model.SurveyQuestionBuilder;
import com.softech.vu360.lms.web.controller.model.survey.TakeSurveyForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author Dyutiman
 * created on 21st June 2010.
 *
 */
public class AnonymousSurveyController extends VU360BaseMultiActionController {

	private String defaultTemplate;
	private String defaultTakeSurveyTemplate;
	private String redirectTemplate;
	private String closeTemplate;
	private VelocityEngine velocityEngine;

	private EntitlementService entitlementService;
	private SurveyService surveyService;

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		TakeSurveyForm form = (TakeSurveyForm)command;
		/*String surveyId = request.getParameter("surveyId");
		if( StringUtils.isBlank(surveyId) ) {
			surveyId = "4052";
		}
		form.setSurveyId(Long.parseLong(surveyId));*/
		

		String surveyId = request.getParameter("surveyId");
		if( surveyId!=null ) {
			form.setSurveyId(Long.parseLong(surveyId));
		}
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		Map<String, String> context = new HashMap<String, String>();
		context.put("target", "displayBase");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	public ModelAndView displayBase(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(defaultTemplate);
	}
	
	public ModelAndView showSurveyView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		TakeSurveyForm form = (TakeSurveyForm)command;
		int nextPageIndex = 0;
		int questionIndex = 0;
		int questionNo = 0;
		int totalQuestions = 0;
		int index = 0;
		int i;
		Long surveyId = form.getSurveyId();
		if( surveyId == null ) {
			return new ModelAndView(defaultTemplate);
		}
		Map<Object, Object> context = new HashMap<Object, Object>();
		com.softech.vu360.lms.model.Survey survey = surveyService.getSurveyByID(surveyId);
		
		if (!survey.isAllowAnonymousResponse()) {
			context.put("msgText", "This is not an AllowAnonymous Survey");
			return new ModelAndView(defaultTemplate, "context",context);
		} else if (survey.getStatus().equalsIgnoreCase("Unpublished")) {
			context.put("msgText", "This Survey is not Published");
			return new ModelAndView(defaultTemplate, "context",context);
		}

		if( checkForFurtherProceesingRequirment(context, errors,request) ) {
			return new ModelAndView(defaultTakeSurveyTemplate, "context",context);
		}
		if( request.getParameter("nextPageIndex") != null && !survey.getIsShowAll() ) {
			index = Integer.parseInt(request.getParameter("nextPageIndex"));
			questionIndex = index*survey.getQuestionsPerPage();
			questionNo = index*survey.getQuestionsPerPage();
		}
		SurveyQuestionBuilder builder = new SurveyQuestionBuilder(survey); 		
		List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
		Collections.sort(surveyQuestionList);

		for( i = questionIndex ; i < surveyQuestionList.size() ; i++ ) {
			totalQuestions++;
			com.softech.vu360.lms.model.SurveyQuestion question = surveyQuestionList.get(i);
			builder.buildQuestion(question,null);
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
		Collections.sort(newUniqueQuestionList);
		form.getSurvey().setQuestionList(newUniqueQuestionList);

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

		context.put("totalQuestions", totalQuestions);
		context.put("nextPageIndex", nextPageIndex);
		context.put("questionNo", questionNo);

		return new ModelAndView(defaultTakeSurveyTemplate, "context",context);
	}

	public ModelAndView finishSurvey(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		 
		Map<Object, Object> context = new HashMap<Object, Object>();
		VU360User surveyee = VU360UserAuthenticationDetails.getCurrentUser();
		if( checkForFurtherProceesingRequirment(context, errors,request) ) {
			return new ModelAndView(defaultTakeSurveyTemplate, "context",context);
		}	 
		TakeSurveyForm form = (TakeSurveyForm)command;
		form.setSurveyee(surveyee);
		surveyService.addAnonymousSurveyResult(form);
		//Map<String,Object> questionAnswerMap = surveyService.getSurveyQASummary( user, form);
		//if( form.getSurvey().getSurveyRef().getEvent().equalsIgnoreCase("survey.event.manual")) // if manual survey
			//sendEmailToManager(user, form.getSurvey().getSurveyRef(),questionAnswerMap ,request) ;
		return new ModelAndView(closeTemplate);
	}
	
	private boolean checkForFurtherProceesingRequirment(Map<Object, Object> context, BindException errors, HttpServletRequest request){
		if(  errors.getErrorCount() > 0 ) {
			int nextPageIndex = Integer.parseInt(request.getParameter("nextPageIndex"));
			int questionNo = Integer.parseInt(request.getParameter("questionNo"));
			int totalQuestions =  Integer.parseInt(request.getParameter("totalQuestions"));

			context.put("nextPageIndex", nextPageIndex);
			context.put("questionNo", questionNo);
			context.put("totalQuestions", totalQuestions);
			return true;
		}
		return false;
	}
	
	List<Survey> getUnFinishedSurveys(List<SurveyLearner> surveyLearnerList ,VU360User user){
		List<Survey> surveyList = new ArrayList<Survey>();
		for( SurveyLearner surveyLearner  : surveyLearnerList ) {
			if( surveyService.getSurveyResultByLearnerAndSurvey(user, surveyLearner.getSurvey()) == null ) {
				surveyList.add( surveyLearner.getSurvey() );
			}
		}
		return surveyList ;
	}

	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		
		String [] questionsArray = request.getParameterValues("questionsArray");		
		if ( questionsArray != null ) {
			TakeSurveyForm form = (TakeSurveyForm)command;
			// get all survey questions
			for( String questionIdString : questionsArray ) {
				long questionId = 0 ;
				try {
					questionId = Long.parseLong(questionIdString);
				} catch ( NumberFormatException e ) {
					e.printStackTrace();
					continue;
				}
				int listCount = 0;
				for( com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion sq : form.getSurvey().getQuestionList() ) {
					
					if( questionId == sq.getSurveyQuestionRef().getId().longValue() && sq.getSurveyQuestionRef().getRequired().booleanValue() ) { // if question is found in list
						com.softech.vu360.lms.model.SurveyQuestion surveyQuestion = sq.getSurveyQuestionRef();
						
						if( surveyQuestion instanceof TextBoxSurveyQuestion ) {
							if( StringUtils.isBlank( form.getSurvey().getQuestionList().get(listCount).getAnswerText() ) ) {
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							}
						}else if( surveyQuestion instanceof MultipleSelectSurveyQuestion ) {
							
							boolean selected = false; 
							for( com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(listCount).getAnswerItems() ) {
								if( answerItem.isSelected() ) {
									selected = true ;
									break;
								}
							}
							if( selected == false ) { // if selected is still set to false
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							}
						}else if( surveyQuestion instanceof SingleSelectSurveyQuestion ) {
							if( form.getSurvey().getQuestionList().get(listCount).getSingleSelectAnswerId() == null ) {
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							}	
						}
					}
					listCount++;
				}
			}	
		}
	}

	public ModelAndView cancelDisplaySurvey(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) 
	throws Exception {
		return new ModelAndView(closeTemplate);
	}
	
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getDefaultTakeSurveyTemplate() {
		return defaultTakeSurveyTemplate;
	}

	public void setDefaultTakeSurveyTemplate(String defaultTakeSurveyTemplate) {
		this.defaultTakeSurveyTemplate = defaultTakeSurveyTemplate;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public String getDefaultTemplate() {
		return defaultTemplate;
	}

	public void setDefaultTemplate(String defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}

}