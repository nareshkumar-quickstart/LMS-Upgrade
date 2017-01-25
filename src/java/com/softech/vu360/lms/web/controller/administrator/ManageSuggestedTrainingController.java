package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyAnswerItem;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.SuggestedTrainingForm;
import com.softech.vu360.lms.web.controller.model.SurveyResponseBuilder;
import com.softech.vu360.lms.web.controller.validator.SuggestedTrainingValidator;

/**
 * @author Saptarshi
 * created on 2nd Aug 2010
 *
 */
public class ManageSuggestedTrainingController extends VU360BaseMultiActionController {

	private String searchSuggestedCoursesTemplate = null;
	private String editSuggestedTrainingTemplate = null;
	private String redirectTemplate = null;

	private HttpSession session;

	private CourseAndCourseGroupService courseAndCourseGroupService ;
	private SurveyService surveyService ;

	protected void onBind( HttpServletRequest request, Object command, String methodName ) throws Exception {
		if( command instanceof SuggestedTrainingForm ) {

		}
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod( NoSuchRequestHandlingMethodException ex, HttpServletRequest request, 
			HttpServletResponse response ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		session = request.getSession();
		String surveyId = request.getParameter("sid");
		session.setAttribute("sid", surveyId);
		context.put("methodName", "searchCourses");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	public ModelAndView searchCourses( HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors ) throws Exception {
		SuggestedTrainingForm form = (SuggestedTrainingForm)command;
		session = request.getSession();
		form.setSid(Long.parseLong((String)session.getAttribute("sid")));
		List<SuggestedTraining> suggestedTrainings = surveyService.getSuggestedTrainingsBySurveyID(form.getSid());
		List<Course> courses = new ArrayList<Course>();
		if(suggestedTrainings.size()>0){
			
			SuggestedTraining suggestedTraining = surveyService.loadForUpdateSuggestedTraining(suggestedTrainings.get(0).getId());   // Note assuming there should be a only one SuggestedTraining under a survey
			form.setSuggTraining(suggestedTraining);
			 courses = suggestedTraining.getCourses();
			form.setCourses(courses);
		}
		
		//List<Course> courses = surveyService.getCoursesUnderSuggestedTrainingBySurveyId(form.getSid());
		Map<Object, Object> context = new HashMap<Object, Object>();

		/*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ) {
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			if( details.getCurrentCustomer() != null ){
				customer = details.getCurrentCustomer();
			}
		}
		List<WebLinkCourse> suggestedCourses = courseAndCourseGroupService.findCustomCourseByName("",customer);
		context.put("listOfCourse", suggestedCourses);*/
		context.put("eventType", surveyService.getSurveyByID(form.getSid()).getEvent());

		context.put("courses", courses);
		return new ModelAndView(searchSuggestedCoursesTemplate, "context", context);
	}

	
	public ModelAndView deleteCourse( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {
		SuggestedTrainingForm form = (SuggestedTrainingForm)command;
		
		String[] selectedCourses= request.getParameterValues("row") ;
		if (selectedCourses !=null && selectedCourses.length>0)
		{
			
			long[] selectedCourseIds = new long [selectedCourses.length];
			int count=0;
			for(String selectedCourse : selectedCourses){
				selectedCourseIds[count]= Long.parseLong(selectedCourse);
				count++;
			}
			
			for(long Id:selectedCourseIds){

				for(int j=0;j<form.getCourses().size();j++){

					if(form.getCourses().get(j).getId()==Id){

						form.getSuggTraining().getCourses().remove(j);
						//surveyService.deleteSuggestedTraining(form.getCourses().get(j));
						
						

					}

				}
				
			}
			
			SuggestedTraining suggestedTraining = surveyService.loadForUpdateSuggestedTraining(form.getSuggTraining().getId());
			
			suggestedTraining.setCourses(form.getSuggTraining().getCourses());			
			
			surveyService.saveSuggestedTraining(suggestedTraining);
			
		}
		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "displayAfterDelete");
		
		return searchCourses(request,  response,  command,  errors );
	}
	public ModelAndView editSuggestedTraining( HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors ) throws Exception {

		SuggestedTrainingForm form = (SuggestedTrainingForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		String stId = request.getParameter("id");
		//Course course = courseAndCourseGroupService.getCourseByCourseId(courseId);
		if(!(form.getCourses().size()>0)){
			SuggestedTrainingValidator suggestedTrainingValidator=new SuggestedTrainingValidator();
			suggestedTrainingValidator.ValidateAssignMultiple(form, errors);
			return new ModelAndView(searchSuggestedCoursesTemplate, "context", context);
		}
		SuggestedTraining suggTraining = surveyService.loadForUpdateSuggestedTraining(Long.parseLong(stId));
		form.setSuggTraining(suggTraining);
		Set<SurveyQuestion> qnsSet = new HashSet<SurveyQuestion>();

		for (SurveyAnswerItem item : suggTraining.getResponses() ) {
			qnsSet.add(item.getSurveyQuestion());
		}

		if (qnsSet.size() > 0) {
			Iterator<SurveyQuestion> itr = qnsSet.iterator(); 
			List<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();
			while(itr.hasNext()) {
				questions.add(itr.next()); 
			} 
			
			Survey survey = surveyService.loadForUpdateSurvey(form.getSid());
			
			SurveyResponseBuilder builder = new SurveyResponseBuilder(survey); 
			Collections.sort(questions);
			for( SurveyQuestion question : questions) {
				/*if (question instanceof AggregateSurveyQuestion) {
					AggregateSurveyQuestion aggrtQuestion = (AggregateSurveyQuestion)question;
					List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems = surveyService.getAggregateSurveyQuestionItemsByQuestionId(aggrtQuestion.getId());
					if (aggregateSurveyQuestionItems != null) {
							builder.buildAggregateSurveyQuestion(aggrtQuestion, aggregateSurveyQuestionItems);
					}
						
				} else {*/
				if (question instanceof MultipleSelectSurveyQuestion || question instanceof SingleSelectSurveyQuestion) { 
					builder.buildSuggestedTrainingQuestion(question, suggTraining.getResponses() );
				}
			}
			com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = builder.getSurveyView();
			//set it up in the form object
			form.setSurveyView(surveyView);
		}

		/*if( course != null ) {
			context.put("courseName", course.getCourseTitle());
		}*/
		return new ModelAndView(editSuggestedTrainingTemplate, "context", context);
	}
	
	public ModelAndView saveSuggestedTraining( HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors ) throws Exception {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		SuggestedTrainingForm form = (SuggestedTrainingForm)command;
		surveyService.editSuggestedTraining(form);
		
		context.put("methodName", "searchCourses");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	protected void validate(HttpServletRequest request, Object command,	BindException errors, String methodName) throws Exception {
		//
	}

	public String getSearchSuggestedCoursesTemplate() {
		return searchSuggestedCoursesTemplate;
	}

	public void setSearchSuggestedCoursesTemplate(
			String searchSuggestedCoursesTemplate) {
		this.searchSuggestedCoursesTemplate = searchSuggestedCoursesTemplate;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getEditSuggestedTrainingTemplate() {
		return editSuggestedTrainingTemplate;
	}

	public void setEditSuggestedTrainingTemplate(
			String editSuggestedTrainingTemplate) {
		this.editSuggestedTrainingTemplate = editSuggestedTrainingTemplate;
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

}
