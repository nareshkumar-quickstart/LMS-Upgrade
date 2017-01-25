package com.softech.vu360.lms.web.controller.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SurveyLink;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.controller.SimpleFormController;
import com.softech.vu360.lms.web.controller.model.ManageSurveyForm;
import com.softech.vu360.lms.web.controller.model.SurveyCourse;
import com.softech.vu360.lms.web.controller.validator.ManageSurveyValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AddNewSurveyController extends SimpleFormController {
	public AddNewSurveyController() {
		super();
		setCommandClass(ManageSurveyForm.class);
		setCommandName("manageSurveyForm");
	}

	private static final Logger log = Logger.getLogger(AddNewSurveyController.class.getName());
	private EntitlementService entitlementService = null;
	private String TARGET_CANCEL="cancelSurveyInfoAndDisplaySurveys";
	private SurveyService surveyService = null;
	private VelocityEngine velocityEngine;
	private String UPDATE_SAVE_METHOD = "saveSurveyInfoAndDisplaySurveys";
	
	/*
	 * Call to formBackingObject()  which by default, returns an instance of the commandClass that has been configured (see the properties the superclass exposes), 
	 * but can also be overridden to e.g. retrieve an object from the database (that needs to be modified using the form).
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	public Object formBackingObject(HttpServletRequest request) throws ServletException { 
		log.debug("getting   object in 'formBackingObject' method " );
		String sid = request.getParameter("sid");
		ManageSurveyForm form = new ManageSurveyForm();
		if( sid != null && !sid.equals("") )
		{
			form.reset();

			//load the survey data from the command.getSid() into the command object using service
			Survey survey = surveyService.loadForUpdateSurvey(Long.parseLong(sid));
			List<SurveyLink> surveyLinks=surveyService.getSurveyLinksById(Long.parseLong(sid));

			form.setSurveyName(survey.getName());
			form.setSid(survey.getId());
			List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
			Collections.sort(surveyQuestionList);
			form.setSurveyQuestions(surveyQuestionList);
			int length = surveyQuestionList.size();
			if(length>0) {
				SurveyQuestion surveyQuestion = surveyQuestionList.get(length-1);
				int displayOrder = surveyQuestion.getDisplayOrder();
				form.setDisplayQuestionOrder(displayOrder+1);
			}else {
				form.setDisplayQuestionOrder(0);
			}
			form.setSurveyEvent(survey.getEvent());
			form.setPublished(survey.getStatus().equalsIgnoreCase(Survey.PUBLISHED));
			form.setLocked(survey.getIsLocked());
			if (survey.getIsShowAll() == Boolean.FALSE) {
				form.setAllQuestionPerPage(false);
				form.setQuestionsPerPage(survey.getQuestionsPerPage()+"");
			}
			List<SurveyCourse> selectedCourses = new ArrayList<SurveyCourse>();
			for(Course course:survey.getCourses()){
				SurveyCourse surveyCourse = new SurveyCourse();
				surveyCourse.setSelected(true);
				surveyCourse.setCourse(course);
				selectedCourses.add(surveyCourse);
			}
			form.setSurveyCourses(selectedCourses);

			if(surveyLinks != null && surveyLinks.size()>0){
				String linkvalues="";
				for(SurveyLink surveyLink:surveyLinks){
					linkvalues=linkvalues+ surveyLink.getUrlName() + "\n";
				}
				form.setLinksValue(linkvalues);
			}
			form.setAllowAnonymousResponse(survey.isAllowAnonymousResponse()==null ? false: survey.isAllowAnonymousResponse());
			form.setElectronicSignature(survey.isElectronicSignatureRequire());
			form.setLinks(survey.isLinkSelected());
			form.setRememberPriorResponse(survey.isRememberPriorResponse());
			form.setElectronicSignatureValue(survey.getElectronicSignature());
			form.setReadOnly(survey.isReadonly());
		}

		return form;
	}	

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,	HttpServletResponse response, BindException errors,
			Map controlModel) throws Exception {
		controlModel = new HashMap<Object,Object>();
		String sid = request.getParameter("sid");
		if (sid != null) 
			controlModel.put("isEdit", false);
		else
			controlModel.put("isEdit", true);	
		
		// Auto-generated method stub
		return super.showForm(request, response, errors, controlModel);
	}


	protected void validate( Object command, BindException errors) throws Exception {
		ManageSurveyForm form = (ManageSurveyForm)command; 
		ManageSurveyValidator validator = (ManageSurveyValidator)this.getValidator();
		validator.validate(form, errors);			
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		log.debug("inside reference data" + command);
		Map<String, Object> model = new HashMap <String, Object>();
		model.put("surveyEvents", Survey.SURVEY_EVENTS);
		System.out.println("inside reference data" + Survey.SURVEY_EVENTS);
		return model;			
	} 

	@Override
	protected ModelAndView processFormSubmission( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		
		String methodName = request.getParameter("method");
		System.out.println("MethodNAme:: "+methodName);
		ManageSurveyForm form = (ManageSurveyForm)command;
		
		if ( !methodName.equals(TARGET_CANCEL) ) {	    	

			if( !errors.hasErrors() ) {
				if( methodName != null && methodName.equals(UPDATE_SAVE_METHOD) ) {
					try {
						saveSurveyInfoAndDisplaySurveys(form);
					} catch( Exception e1 ) {
						e1.printStackTrace();
						return new ModelAndView(getFormView(),"manageSurveyForm",form);
					}
				} else {
					try {
						saveSurveyInfo(form);
					} catch( Exception e2 ) {
						e2.printStackTrace();
						return new ModelAndView(getFormView(),"manageSurveyForm",form);
					}
				}
			}
		} else {
			return new ModelAndView(getSuccessView());
		}
		return super.processFormSubmission(request, response, command, errors);
	}
	
	public void saveSurveyInfoAndDisplaySurveys( ManageSurveyForm manageSurveyForm) throws Exception {
		
		//ManageSurveyForm manageSurveyForm = (ManageSurveyForm)command;
		Survey survey = surveyService.loadForUpdateSurvey(manageSurveyForm.getSid());
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		Distributor distributor = customer.getDistributor();
		
		if(distributor==null && survey.getOwner() instanceof Distributor){
			Survey newSurvey = new Survey();
			customer.initializeOwnerParams();
			newSurvey.setOwner(customer);
			
			newSurvey.setName(manageSurveyForm.getSurveyName());
			newSurvey.setEvent(manageSurveyForm.getSurveyEvent());
			if(manageSurveyForm.isPublished()){
				newSurvey.setStatus(Survey.PUBLISHED);
			}else{
				newSurvey.setStatus(Survey.NOTPUBLISHED);
			}
			
			if(!manageSurveyForm.isAllQuestionPerPage()){
				newSurvey.setIsShowAll(false);
				newSurvey.setQuestionsPerPage(Integer.parseInt(manageSurveyForm.getQuestionsPerPage()));
			}else {
				newSurvey.setIsShowAll(true);
			}
			
			newSurvey.setRememberPriorResponse(manageSurveyForm.isRememberPriorResponse());
			newSurvey.setAllowAnonymousResponse(manageSurveyForm.isAllowAnonymousResponse());
			if(manageSurveyForm.isElectronicSignature()) {
				newSurvey.setElectronicSignatureRequire(manageSurveyForm.isElectronicSignature());
				newSurvey.setElectronicSignature(manageSurveyForm.getElectronicSignatureValue());
			}
			newSurvey.setLinkSelected(manageSurveyForm.isLinks());
			surveyService.addSurvey(newSurvey); //!!!the service function name should be save survey
			
			List<SurveyLink> surveyLinks = null;
			if(manageSurveyForm.isLinks()) {
				String links = manageSurveyForm.getLinksValue();
				surveyLinks = this.readSurveyLinks(links, newSurvey);
				surveyService.saveSurveyLinkList(surveyLinks);
			}
			
		}else{
			survey.setName(manageSurveyForm.getSurveyName());
			survey.setEvent(manageSurveyForm.getSurveyEvent());
			if(manageSurveyForm.isPublished()){
				survey.setStatus(Survey.PUBLISHED);
			}else{
				survey.setStatus(Survey.NOTPUBLISHED);
			}

			if(!manageSurveyForm.isAllQuestionPerPage()){
				survey.setIsShowAll(false);
				survey.setQuestionsPerPage(Integer.parseInt(manageSurveyForm.getQuestionsPerPage()));
			}else {
				survey.setIsShowAll(true);
			}
			
			survey.setRememberPriorResponse(manageSurveyForm.isRememberPriorResponse());
			survey.setAllowAnonymousResponse(manageSurveyForm.isAllowAnonymousResponse());
			survey.setElectronicSignatureRequire(manageSurveyForm.isElectronicSignature());
			if(manageSurveyForm.isElectronicSignature()) {
				survey.setElectronicSignature(manageSurveyForm.getElectronicSignatureValue());
			}
			
			survey.setLinkSelected(manageSurveyForm.isLinks());
			
			survey=	surveyService.addSurvey(survey); //!!!the service function name should be save survey
			List<SurveyLink> surveyLinks = null;
			if(manageSurveyForm.isLinks()) {
				String links = manageSurveyForm.getLinksValue();
				surveyLinks = this.readSurveyLinks(links, survey);
				surveyService.saveSurveyLinkList(surveyLinks);
			}
		}
	}
	
	public void saveSurveyInfo(Object command) throws Exception {

		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		Survey newSurvey = new Survey();

		ManageSurveyForm manageSurveyForm = (ManageSurveyForm)command;
		newSurvey.setName(manageSurveyForm.getSurveyName());
		newSurvey.setEvent(manageSurveyForm.getSurveyEvent());
		if( manageSurveyForm.isPublished() ) {
			newSurvey.setStatus(Survey.PUBLISHED);
		}else{
			newSurvey.setStatus(Survey.NOTPUBLISHED);
		}
		if( !manageSurveyForm.isAllQuestionPerPage() ) {
			newSurvey.setIsShowAll(false);
			newSurvey.setQuestionsPerPage(Integer.parseInt(manageSurveyForm.getQuestionsPerPage()));
		}
		newSurvey.setRememberPriorResponse(manageSurveyForm.isRememberPriorResponse());
		newSurvey.setAllowAnonymousResponse(manageSurveyForm.isAllowAnonymousResponse());
		if( manageSurveyForm.isElectronicSignature() ) {
			newSurvey.setElectronicSignatureRequire(manageSurveyForm.isElectronicSignature());
			newSurvey.setElectronicSignature(manageSurveyForm.getElectronicSignatureValue());
		}
		
		newSurvey.setLinkSelected(manageSurveyForm.isLinks());
		
		if( distributor == null ) {
			customer.initializeOwnerParams();
			newSurvey.setOwner(customer);
		}else{
			distributor.initializeOwnerParams();
			newSurvey.setOwner(distributor);
		}
		//surveyService.addSurvey(newSurvey,surveyLink);
		newSurvey = surveyService.addSurvey(newSurvey);
		List<SurveyLink> surveyLinks = null;
		if( manageSurveyForm.isLinks() ) {
			String links = manageSurveyForm.getLinksValue();
			surveyLinks = this.readSurveyLinks(links, newSurvey);
			surveyService.saveSurveyLinkList(surveyLinks);
		}
	}

	private List<SurveyLink> readSurveyLinks( String links, Survey survey) {

		String str;
		int i = 0;
		BufferedReader reader = new BufferedReader(new StringReader(links));
		try {
			List<SurveyLink> surveyLinkList = new ArrayList<SurveyLink>();
			while ((str = reader.readLine()) != null) {
				if (str.length() > 0){
					if(!StringUtils.isBlank(str)) {
						SurveyLink link = new SurveyLink();
						link.setUrlName(str);
						link.setDisplayOrder(i++);
						link.setSurvey(survey);
						surveyLinkList.add(link);
					}
				}
			}
			return surveyLinkList;

		} catch(IOException e) {
			e.printStackTrace();
		}		
		return null;
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

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
}