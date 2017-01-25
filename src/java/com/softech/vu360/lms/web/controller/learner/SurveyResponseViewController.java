package com.softech.vu360.lms.web.controller.learner;

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
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CommentSurveyQuestion;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.SurveyQuestionBuilder;
import com.softech.vu360.lms.web.controller.model.survey.TakeSurveyForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;

public class SurveyResponseViewController extends VU360BaseMultiActionController {


	private SurveyService surveyService = null;
	private VelocityEngine velocityEngine;
	private StatisticsService statisticsService =null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	
	private VU360UserService vu360UserService;
	
	private String defaultSurveyResponseTemplate;
	private String nextSurveyResponseTemplate;
	private String closeTemplate;
	
	private static final Logger log = Logger.getLogger(TakeSurveyController.class.getName());

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.handleRequestInternal(request, response);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		TakeSurveyForm form = (TakeSurveyForm)command;
		if(methodName.equals("showSurveyResponseView")) {
			String learningSessionId = form.getLearningSessionId();
			LearningSession learningSession =  statisticsService.getLearningSessionByLearningSessionId(learningSessionId);
			List<Survey> surveyList = surveyService.getDueSurveysByLearningSession(learningSessionId);
			Course course = courseAndCourseGroupService.getCourseByGUID(learningSession.getCourseId());
			form.setCourseId(course.getId());
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Learner learnerModel = new Learner();
			learnerModel.setId(user.getLearner().getId());
			form.setLearner(learnerModel);
			form.setSurveys(surveyList);
		}
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#validate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, java.lang.String)
	 */
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {

		String [] questionsArray = request.getParameterValues("questionsArray");		
		if ( questionsArray != null ) {
			log.debug(questionsArray.length);
			TakeSurveyForm form = (TakeSurveyForm)command;
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
				for(com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion sq : form.getSurvey().getQuestionList() ){
					
					if( questionId == sq.getSurveyQuestionRef().getId().longValue() && sq.getSurveyQuestionRef().getRequired().booleanValue() ) { // if question is found in list
						com.softech.vu360.lms.model.SurveyQuestion surveyQuestion = sq.getSurveyQuestionRef();
						
						if(surveyQuestion instanceof CommentSurveyQuestion){
							
							if( StringUtils.isBlank( form.getSurvey().getQuestionList().get(listCount).getAnswerText() ) ) {
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							}
 
						}else if(surveyQuestion instanceof MultipleSelectSurveyQuestion){
							boolean selected =false ; 

							for (com.softech.vu360.lms.web.controller.model.survey.SurveyAnswerItem answerItem : form.getSurvey().getQuestionList().get(listCount).getAnswerItems()) {
								if (answerItem.isSelected()) {
									selected = true ;
									break;
								}
							}
							
							if( selected == false ){ // if selected is still set to false
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							}
						}else if(surveyQuestion instanceof SingleSelectSurveyQuestion){
							 
							if(form.getSurvey().getQuestionList().get(listCount).getSingleSelectAnswerId() ==null){
							
								errors.rejectValue("survey.questionList["+listCount+"]", "error" ,"errorDetails");
							 
							}	
							
						}

						
					}
					listCount++;
				}
				
			}	
		}


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
	
	public ModelAndView showSurveyResponseView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		TakeSurveyForm form = (TakeSurveyForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		int nextPageIndex = 0;
		int questionIndex = 0;
		int questionNo = 0;
		int totalQuestions = 0;
		int index = 0;
		int i;
		
		if( checkForFurtherProceesingRequirment(context, errors,request)){
			
			return new ModelAndView(defaultSurveyResponseTemplate, "context",context);
		}
		
		List<com.softech.vu360.lms.model.Survey> surveyList = form.getSurveys();
		if (surveyList != null && surveyList.size() > 0) {
			
			com.softech.vu360.lms.model.Survey survey = surveyList.get(0);
			
			if (request.getParameter("nextPageIndex") != null && !survey.getIsShowAll()) {
				index = Integer.parseInt(request.getParameter("nextPageIndex"));
				questionIndex = index*survey.getQuestionsPerPage();
				questionNo = index*survey.getQuestionsPerPage();;
			}
			
			form.setSurveyId(survey.getId());
			SurveyQuestionBuilder builder = new SurveyQuestionBuilder(survey);
			List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
			Collections.sort(surveyQuestionList);
			
			for(i=questionIndex; i<surveyQuestionList.size(); i++){
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
			
			if (i == surveyQuestionList.size() || survey.getIsShowAll()) {
				surveyList.remove(0);
				form.setSurveys(surveyList);
				nextPageIndex = 0;
			} else
				nextPageIndex = index+1;
			
			context.put("totalQuestions", totalQuestions);
			context.put("nextPageIndex", nextPageIndex);
			context.put("questionNo", questionNo);
		} else {
			return new ModelAndView(closeTemplate);
		}
		return new ModelAndView(defaultSurveyResponseTemplate, "context",context);
	}

	public ModelAndView finishSurveyResponse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		if( checkForFurtherProceesingRequirment(context, errors,request)){		
			return new ModelAndView(defaultSurveyResponseTemplate, "context",context);
		}
		
		TakeSurveyForm form = (TakeSurveyForm)command;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360User vu360UserModel = vu360UserService.getUserById(user.getId());
		surveyService.addSurveyResult(vu360UserModel,form);
		Map<String,Object> questionAnswerMap = surveyService.getSurveyQASummary( vu360UserModel, form);
		if( form.getSurvey().getSurveyRef().getEvent().equalsIgnoreCase("survey.event.manual")) // if manual survey
			sendEmailToManager(vu360UserModel, form.getSurvey().getSurveyRef(),questionAnswerMap ,request) ;
		return new ModelAndView(nextSurveyResponseTemplate);

	}

	public ModelAndView cancelDisplaySurvey(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(closeTemplate);
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

	/**
	 * @return the defaultSurveyResponseTemplate
	 */
	public String getDefaultSurveyResponseTemplate() {
		return defaultSurveyResponseTemplate;
	}

	/**
	 * @param defaultSurveyResponseTemplate the defaultSurveyResponseTemplate to set
	 */
	public void setDefaultSurveyResponseTemplate(
			String defaultSurveyResponseTemplate) {
		this.defaultSurveyResponseTemplate = defaultSurveyResponseTemplate;
	}

	/**
	 * @return the nextSurveyResponseTemplate
	 */
	public String getNextSurveyResponseTemplate() {
		return nextSurveyResponseTemplate;
	}

	/**
	 * @param nextSurveyResponseTemplate the nextSurveyResponseTemplate to set
	 */
	public void setNextSurveyResponseTemplate(String nextSurveyResponseTemplate) {
		this.nextSurveyResponseTemplate = nextSurveyResponseTemplate;
	}

	/**
	 * @return the statisticsService
	 */
	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * @param statisticsService the statisticsService to set
	 */
	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
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
		model.put("brander", brander);
		
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, surveyAssignmentManagerTemplatePath, model);
		VU360User manager = surveyService.getManagerOfSurveyLearner( loggedInUser, survey);
		SendMailService.sendSMTPMessage(manager.getEmailAddress(),	fromAddress, fromCommonName, subject, text);
	}

	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
}