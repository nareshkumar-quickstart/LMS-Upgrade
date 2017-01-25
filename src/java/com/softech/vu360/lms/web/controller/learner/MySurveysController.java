package com.softech.vu360.lms.web.controller.learner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyLearner;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.MySurveyCourse;
import com.softech.vu360.lms.web.controller.model.MySurveysForm;

/**
 * 
 * @author Saptarshi
 *
 */

public class MySurveysController extends VU360BaseMultiActionController {

	private LearnerService learnerService;
	private EntitlementService entitlementService;
	private SurveyService surveyService;
	
	@Autowired
	private VU360UserService vu360UserService;
	
	private String defaultSurveysThanksPageTemplate ;
	private String defaultMySurveysTemplate;
	private String returnMyCoursesTemplate;
	
	private static final Logger log = Logger.getLogger(MySurveysController.class.getName());
	public MySurveysController() {
		super();
	}

	public MySurveysController(Object delegate) {
		super(delegate);
	}


	/**
	 * Callback method for doing extra binding related stuff.
	 */
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if(command instanceof MySurveysForm){
			MySurveysForm form = (MySurveysForm)command;
			if(methodName.equals("displaySurveys")) {

			}
		}
	}

	/**
	 * Callback method for doing validations before the processing method is executed.
	 * Be sure to check if there are any errors before doing anything
	 * in the processing method
	 */
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		// TODO implement the validations based on the method name
	}
	public ModelAndView displaySurveyThanksPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		 displaySurveys(request,  response,  command,  errors);
		 return new ModelAndView(defaultSurveysThanksPageTemplate);
	}
	public ModelAndView displaySurveys(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		MySurveysForm form = (MySurveysForm)command;
		String search= request.getParameter("search");
		log.debug("The search parameter value is : "+search);
				
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		VU360User vu360UserModel = vu360UserService.getUserById(user.getId());
		
		List<LearnerEnrollment> enrollments = entitlementService.getLearnerEnrollmentsByLearner(vu360UserModel.getLearner(), null);
		Set<Course> courses = new HashSet<Course>();
		List<Survey> dueSurveys = surveyService.getDueSurveysByUser(vu360UserModel);
	
		for (Survey survey : dueSurveys) {
			for (Course course : survey.getCourses()) {
				boolean found = false;
				for(LearnerEnrollment lenrollment:enrollments){
					if(course.getId().compareTo(lenrollment.getCourse().getId())==0){
						found = true;
						break;
					}
				}
				if(found)
					courses.add(course);
			}
		}
		List<MySurveyCourse> mySurveyCourseList = new ArrayList<MySurveyCourse>();
		
		
		List<Survey> courseSurveyList = null;
		MySurveyCourse mySurveyCourse = null;
			for(Course c : courses) {
				mySurveyCourse = new MySurveyCourse();
				courseSurveyList = new ArrayList<Survey>();
				for(Survey s : dueSurveys){
					if(s.getCourses().contains(c) && !courseSurveyList.contains(s)){
						courseSurveyList.add(s);
					}
				}
				if(!courseSurveyList.isEmpty()){
					mySurveyCourse.setCourse(c);
					mySurveyCourse.setSurveys(courseSurveyList);
					mySurveyCourseList.add(mySurveyCourse);
				}
			}
		
		

		
		
		
		
		
		/* **************************** send back surveys start ****************************** */
		
		
			
		
		
			//LMS: 7617
			List<SurveyResult> sendBackSurveyResults = surveyService.getSendBackSurveyResultsByLoggedInUser(vu360UserModel);
			for(int i=0;i<sendBackSurveyResults.size();i++){				
				for(int j=0;j<mySurveyCourseList.size();j++){
					if(mySurveyCourseList.get(j).getCourse() != null && sendBackSurveyResults.get(i).getCourse() != null){
						if(mySurveyCourseList.get(j).getCourse().getId() == sendBackSurveyResults.get(i).getCourse().getId()){
							mySurveyCourseList.get(j).getSurveys().add(sendBackSurveyResults.get(i).getSurvey());
							break;
						}
					}
				}
				
				
				
				
			}
		
			/* **************************** send back surveys ends ****************************** */
		
		
		List<SurveyLearner> surveyLearnerList = surveyService.getLearnerSurveys(vu360UserModel, search);
		List<Survey> surveyList = new ArrayList<Survey>();
		surveyList = getUnFinishedSurveys(surveyLearnerList , vu360UserModel, search);
		
		form.setMySurveyCourseList(mySurveyCourseList);
		Collections.sort(surveyList);
		form.setSurveyList(surveyList);
		
		
		
		return new ModelAndView(defaultMySurveysTemplate);
	}
	
	List<Survey> getUnFinishedSurveys(List<SurveyLearner> surveyLearnerList ,VU360User user, String search){
		List<Survey> surveyList = new ArrayList<Survey>();
		for( SurveyLearner surveyLearner  : surveyLearnerList){
			if(surveyService.getSurveyResultByLearnerAndSurvey(user,   surveyLearner.getSurvey(), search)==null){
				surveyList.add( surveyLearner.getSurvey() );
			}
		}
		return surveyList ;
	}

	public ModelAndView changeOption(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(returnMyCoursesTemplate);
	}

	/**
	 * @return the defaultMySurveysTemplate
	 */
	public String getDefaultMySurveysTemplate() {
		return defaultMySurveysTemplate;
	}

	/**
	 * @param defaultMySurveysTemplate the defaultMySurveysTemplate to set
	 */
	public void setDefaultMySurveysTemplate(String defaultMySurveysTemplate) {
		this.defaultMySurveysTemplate = defaultMySurveysTemplate;
	}

	/**
	 * @return the returnMyCoursesTemplate
	 */
	public String getReturnMyCoursesTemplate() {
		return returnMyCoursesTemplate;
	}

	/**
	 * @param returnMyCoursesTemplate the returnMyCoursesTemplate to set
	 */
	public void setReturnMyCoursesTemplate(String returnMyCoursesTemplate) {
		this.returnMyCoursesTemplate = returnMyCoursesTemplate;
	}

	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}

	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
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
	 * @param defaultSurveysThanksPageTemplate the defaultSurveysThanksPageTemplate to set
	 */
	public void setDefaultSurveysThanksPageTemplate(
			String defaultSurveysThanksPageTemplate) {
		this.defaultSurveysThanksPageTemplate = defaultSurveysThanksPageTemplate;
	}

	/**
	 * @return the defaultSurveysThanksPageTemplate
	 */
	public String getDefaultSurveysThanksPageTemplate() {
		return defaultSurveysThanksPageTemplate;
	}
}
