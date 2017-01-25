package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.MultipleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SingleSelectSurveyQuestion;
import com.softech.vu360.lms.model.SuggestedTraining;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddCourseInSuggestedTrainingForm;
import com.softech.vu360.lms.web.controller.model.CourseItem;
import com.softech.vu360.lms.web.controller.model.SurveyQuestionBuilder;
import com.softech.vu360.lms.web.controller.validator.AddCourseInSuggestionValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author Saptarshi
 * created on 2nd Aug 2010
 *
 */
public class AddSuggestedTrainingController extends AbstractWizardFormController {

	private String finishTemplate = null;
	private String cancelTemplate = null;

	private SurveyService surveyService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService ;
	private EntitlementService entitlementService = null;

	public AddSuggestedTrainingController() {
		super();
		setCommandName("addCoursesInSuggestedTraining");
		setCommandClass(AddCourseInSuggestedTrainingForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"administrator/survey/Suggested Training/addCourseToSuggestedTraining"
				//, "administrator/survey/Suggested Training/addSuggestedTrainingStep2"   Note for LMS 7612 [survey module>System starts crashing when selecting Assign Multiple Courses button on Edit Suggested Training screen. ]
		});
	}
	
	

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Object command = super.formBackingObject(request);
		AddCourseInSuggestedTrainingForm form = (AddCourseInSuggestedTrainingForm)command;
		String surveyId = request.getParameter("sid");
		if (surveyId != null) {
			form.setSid(Long.parseLong(surveyId));
			Survey survey = surveyService.getSurveyByID(form.getSid());
			form.setSurvey(survey);
		}
		return command;
	}



	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {

		AddCourseInSuggestedTrainingForm form = (AddCourseInSuggestedTrainingForm)command;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = null;
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ) {
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			customer = details.getCurrentCustomer();
		}
		
		if( page == 0 && !StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("search") ) {
			ArrayList<CourseItem> courses = new ArrayList<CourseItem>();
			//List<WebLinkCourse> suggestedCourses = courseAndCourseGroupService.findCustomCourseByName(form.getCourseName(),customer);
			List<Course> suggestedCourses = entitlementService.getCoursesByEntitlement(customer, form.getCourseName(), form.getCourseId(), form.getKeywords());
			if (suggestedCourses != null && suggestedCourses.size() > 0) {
			for( Course c : suggestedCourses ) {
				CourseItem item = new CourseItem();
				item.setCourse(c);
				item.setSelected(false);
				courses.add(item);
			}
			form.setCourses(courses);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors, int page) throws Exception {
		AddCourseInSuggestedTrainingForm form = (AddCourseInSuggestedTrainingForm)command;
		
		switch(page) {
		
		case 0:
			if(form.getCourses().size() > 0) {
				List<Course> courses = new ArrayList<Course>();
				for(CourseItem corseItem : form.getCourses()) {
					if (corseItem.getSelected() == true) {
						courses.add(corseItem.getCourse());
					}
				}
				form.setCourseList(courses);
			}
			
			if (form.getSurvey() != null) {
				List<SurveyQuestion> surveyQuns = form.getSurvey().getQuestions();
				if (surveyQuns != null) {
					SurveyQuestionBuilder builder = new SurveyQuestionBuilder(form.getSurvey()); 
					Collections.sort(surveyQuns);
					for( SurveyQuestion question : surveyQuns) {
						/*if (question instanceof AggregateSurveyQuestion) {
							AggregateSurveyQuestion aggrtQuestion = (AggregateSurveyQuestion)question;
							List<AggregateSurveyQuestionItem> aggregateSurveyQuestionItems = surveyService.getAggregateSurveyQuestionItemsByQuestionId(aggrtQuestion.getId());
							if (aggregateSurveyQuestionItems != null) {
									builder.buildAggregateSurveyQuestion(aggrtQuestion, aggregateSurveyQuestionItems,null);
							}
								
						}*/ 
						if (question instanceof MultipleSelectSurveyQuestion || question instanceof SingleSelectSurveyQuestion) {
							builder.buildQuestion(question,null);
						}
					}
					com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = builder.getSurveyView();
					//set it up in the form object
					form.setSurveyView(surveyView);
				}
			}
			break;
		default:
			break;
		}
		
		return super.referenceData(request, command, errors, page);
	}

	protected void validatePage( Object command, Errors errors, int page ) {

		AddCourseInSuggestedTrainingForm form = (AddCourseInSuggestedTrainingForm)command;
		AddCourseInSuggestionValidator validator = (AddCourseInSuggestionValidator)getValidator();
		errors.setNestedPath("");
		switch (page) {

		case 0:
			if( form.getAction().isEmpty() ) {
				validator.validateFirstPage(form, errors);
			}
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		
		AddCourseInSuggestedTrainingForm form = (AddCourseInSuggestedTrainingForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("sid", form.getSid());
		return new ModelAndView(cancelTemplate, "context", context);
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		AddCourseInSuggestedTrainingForm form = (AddCourseInSuggestedTrainingForm)command;
		List<SuggestedTraining> suggestedTrainings = surveyService.getSuggestedTrainingsBySurveyID(form.getSid());
		
		
		
		//surveyService.addSuggestedTraining(form);  Note for LMS 7612 [survey module>System starts crashing when selecting Assign Multiple Courses button on Edit Suggested Training screen. ]
		// Note there will be only one suggested training under survey
		
		if(suggestedTrainings.size() > 0){
			
			
			
			if(form.getCourses().size() > 0) {
				List<Course> courses = null;
				if(suggestedTrainings.get(0).getCourses() != null){
					courses = suggestedTrainings.get(0).getCourses();
				}else{
					courses = new ArrayList<Course>();
				}
				
				for(CourseItem corseItem : form.getCourses()) {
					if (corseItem.getSelected() == true) {
						courses.add(corseItem.getCourse());
					}
				}
				form.setCourseList(courses);
			}
			
			
			SuggestedTraining suggestedTraining = surveyService.loadSuggestedTrainingForUpdate(suggestedTrainings.get(0).getId());
			form.setSuggTraining(suggestedTraining);
			surveyService.addSuggestedTraining(form);
			Map<Object, Object> context = new HashMap<Object, Object>();			
			context.put("sid", form.getSid());			
			return new ModelAndView("redirect:adm_suggestedTraining.do?sid="+form.getSid());
		}
		
		if(form.getCourses().size() > 0) {
			List<Course> courses = new ArrayList<Course>();
			for(CourseItem corseItem : form.getCourses()) {
				if (corseItem.getSelected() == true) {
					courses.add(corseItem.getCourse());
				}
			}
			form.setCourseList(courses);
		}
		SuggestedTraining suggestedTraining = new SuggestedTraining();
		suggestedTraining.setSurvey(form.getSurvey());
		form.setSuggTraining(suggestedTraining);
		surveyService.addSuggestedTraining(form);
		Map<Object, Object> context = new HashMap<Object, Object>();
		//context.put("methodName", "searchCourses?");
		context.put("sid", form.getSid());
		// TODO Finish service here...
		//return new ModelAndView(finishTemplate, "context", context);
		return new ModelAndView("redirect:adm_suggestedTraining.do?sid="+form.getSid());
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
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
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}



	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	

}
