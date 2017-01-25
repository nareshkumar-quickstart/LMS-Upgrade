package com.softech.vu360.lms.web.controller.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.AICCCourse;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.ExternalCourse;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.LegacyCourse;
import com.softech.vu360.lms.model.ScormCourse;
import com.softech.vu360.lms.model.SelfPacedCourse;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.instructor.CourseDetails;
import com.softech.vu360.lms.web.controller.validator.AddCourseValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class ManagerEditCourseController extends
VU360BaseMultiActionController {
	private static final String[] BUSINESS_UNITS = {"All", "Business Development", "EMEA", "Ethics and Complaince", 
		"Financial Services", "Health Care", "Higher Education", "Occupational Training", "Public Safety", "Real State"};
	private static final String[] LANGUAGES = {"English"};
	private static final String COURSE_STATUS_PUBLISHED = "published";
	private static final String[] REGULATORY_REQUIREMENT= {"None","Percentage","Flat Dollar"};
	private static final Logger log = Logger.getLogger(ManagerEditCourseController.class.getName());

	private String summaryTemplate=null;
	private String closeTemplate=null;
	private String courseOverViewTemplate=null;
	private String courseExamOverviewTemplate=null;
	private String courseAdditionalDetailsTemplate=null;
	private String scheduleTemplate = null;	
	private CourseAndCourseGroupService courseAndCourseGroupService;	
	private SynchronousClassService synchronousClassService = null;
	private VelocityEngine velocityEngine;

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
	
		if(methodName.equals("viewCourseSummary") || methodName.equals("viewCourseOverView") || methodName.equals("viewCourseExamOverView")
				|| methodName.equals("viewCourseAdditionalDetails") ){
	
	
	if(!StringUtils.isBlank(request.getParameter("start"))){
		long cid = Long.parseLong(request.getParameter("id"));

		
		Course course = (Course)courseAndCourseGroupService.getCourseById(cid);


		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.debug(user == null ? "User null" : " learnerId="
			+ user.getLearner());
		//Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
		//Learner learner = user.getLearner();


		CourseDetails form = (CourseDetails)command;

		//form.setLearnerEnrollmentId(course.);
		Course dbcourse = getCourseType(course);
		form.setId(cid);
		form.setCourseType(dbcourse.getCourseType());
		form.setCourseName(dbcourse.getCourseTitle());
		form.setCourseID(dbcourse.getCourseId());
		form.setKeywords(dbcourse.getKeywords());
		form.setCreditHours(dbcourse.getCredithour());
		form.setVersion(dbcourse.getVersion());
		form.setBusinessUnit(dbcourse.getBussinesskey());
		Language lang = dbcourse.getLanguage();

		if(lang!=null){
			form.setLanguage(lang.getId()+"");
		}
		if(course.getCourseType().equals(WebLinkCourse.COURSE_TYPE))
		{
			form.setLink(((WebLinkCourse)course).getLink());
		}

		form.setDescription(dbcourse.getDescription());
		
		//Setting Up Course  Over View
		form.setCourseGuide(course.getCourseGuide());
		form.setPreRequisites(course.getCoursePrereq());
		
		//Setting Up Course Additional Info View
		try{
			if(course.getDeliveryMethodId()!=null)
				form.setDeliveryMethodId(course.getDeliveryMethodId().toString());
			else
				form.setDeliveryMethodId("1");
		}catch(Exception sube){
			form.setDeliveryMethodId("1");
		}
		
		form.setMsrp(""+course.getMsrp());
		form.setCourseCode(course.getCode());
		form.setApprovedCourseHours(""+course.getApprovedcoursehours());
		form.setApprovalNumber(course.getApprovalNumber());
		form.setCurrency(course.getCurrency());
		form.setProductPrice(""+course.getProductprice());
		if(course.getWholeSalePrice()!=null)
		{
			form.setWholeSalePrice(""+course.getWholeSalePrice());
		}
		form.setRoyaltyPartner(course.getRoyaltyPartner());
		form.setRoyaltyType(course.getRoyaltyType());
		
		form.setRegulatoryRequirement(course.getStateRegistartionRequired());
		form.setTermsOfService(""+course.getTos());
		
		
		//Setting Up EXAM Over View
		
		form.setLearningObjectives(course.getLearningObjectives());
		form.setQuizInformation(course.getQuizInfo());
		form.setFinalExamInformation(course.getFinalExamInfo());
		form.setEndOfCourseInstructions(course.getEndofCourseInstructions());
	}
	
}


	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		log.debug("inside validate .:::::::::");
		
		
		
		if(StringUtils.isBlank(request.getParameter("start"))){
			CourseDetails form = (CourseDetails)command;
			AddCourseValidator validator = (AddCourseValidator)this.getValidator();
		
		
			validator.validate(form, errors);
		
		}
		
//		CourseDetails form = (CourseDetails)command;
//		AddCourseValidator validator = (AddCourseValidator)this.getValidator();	
//		if(methodName.equals("saveUpdateCourseSummary")){
//			validator.validatePage1(form, errors);
//		}
//
//		if(methodName.equals("saveUpdateCourseAdditionalDetails")){
//			validator.validateCourseAdditionalDetails(form, errors);
//		}
//
//		/*else if(methodName.equals("updateSurveyCoursesAndDisplaySurveys")){
//			validator.validateFinishPage(form, errors);
//		}else if(methodName.equals("saveQuestionAndDisplaySurveys")){
//			validator.validateSecondPage(form, errors);
//		}*/

	}

	@SuppressWarnings("unchecked")
	public ModelAndView viewCourseSummary( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		log.debug("INSIDE viewCourseSummary()");

		Map model = new HashMap();		

		model.put("businessUnits", BUSINESS_UNITS);			
		model.put("languages", LANGUAGES);		
		return new ModelAndView(summaryTemplate,"context",model);
	}
	public ModelAndView saveUpdateCourseOverview( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		log.debug("inside saveUpdateCourseOverview method");
		CourseDetails form = (CourseDetails)command;
		Course course = getCourseType(courseAndCourseGroupService.getCourseById(form.getId()));	

		course.setCourseGuide(form.getCourseGuide());
		course.setCoursePrereq(form.getPreRequisites());

		courseAndCourseGroupService.saveCourse(course);

		return new ModelAndView(closeTemplate);
	}

	public ModelAndView viewCourseOverView( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		log.debug("inside viewCourseOverView()");
		

		return new ModelAndView(courseOverViewTemplate);

	}
	public ModelAndView saveUpdateCourseExamOverView( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		log.debug("inside saveUpdateCourseExamOverView method");
		CourseDetails form = (CourseDetails)command;
		Course course = getCourseType(courseAndCourseGroupService.getCourseById(form.getId()));	

		course.setLearningObjectives(form.getLearningObjectives());
		course.setQuizInfo(form.getQuizInformation());
		course.setFinalExamInfo(form.getFinalExamInformation());
		course.setEndofCourseInstructions(form.getEndOfCourseInstructions());

		courseAndCourseGroupService.saveCourse(course);

		return new ModelAndView(closeTemplate);
	}

	public ModelAndView viewCourseExamOverView( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		log.debug("inside viewCourseExamOverView()");

		return new ModelAndView(courseExamOverviewTemplate);
	}

	public ModelAndView saveUpdateCourseAdditionalDetails( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		if(errors.hasErrors())
		{
			Map model = new HashMap();		
			model.put("regulatoryRequirements", REGULATORY_REQUIREMENT);
			return new ModelAndView(courseAdditionalDetailsTemplate,"context",model);
		}
		if(!errors.hasErrors())
		{
			log.debug("inside saveUpdateCourseExamOverView method");
			CourseDetails form = (CourseDetails)command;
			Course course = getCourseType(courseAndCourseGroupService.getCourseById(form.getId()));	

			try{
		    	course.setDeliveryMethodId(Long.parseLong(form.getDeliveryMethodId()));
			}catch(Exception sube){
				course.setDeliveryMethodId(1l);
			}
			
			if(form.getMsrp()!=null && !form.getMsrp().equals(""))
			{
				course.setMsrp(Double.parseDouble(form.getMsrp()));
			}
			else
				course.setMsrp(0.0);
			course.setCode(form.getCourseCode());
			if(form.getApprovedCourseHours()!=null && !form.getApprovedCourseHours().equals(""))
			{
				course.setApprovedcoursehours(Double.parseDouble(form.getApprovedCourseHours()));
			}
			else
				course.setApprovedcoursehours(0.0);
			course.setApprovalNumber(form.getApprovalNumber());
			course.setCurrency(form.getCurrency());
			if(form.getProductPrice()!=null && !form.getProductPrice().equals(""))
			{
				course.setProductprice(Double.parseDouble(form.getProductPrice()));
			}
			else
				course.setProductprice(0.0);
			if(form.getWholeSalePrice()!=null && !form.getWholeSalePrice().equals(""))
			{
				course.setWholeSalePrice(Double.parseDouble(form.getWholeSalePrice()));
			}
			else
				course.setWholeSalePrice(0.0);
			course.setRoyaltyPartner(form.getRoyaltyPartner());
			course.setRoyaltyType(form.getRoyaltyType());
			course.setStateRegistartionRequired(form.getRegulatoryRequirement());
			if(form.getTermsOfService()!=null && !form.getTermsOfService().equals(""))
			{
				course.setTos(Integer.parseInt(form.getTermsOfService()));
			}
			else
				course.setTos(0);

			course.setLearningObjectives(form.getLearningObjectives());

			courseAndCourseGroupService.saveCourse(course);

			return new ModelAndView(closeTemplate);
		}
		else
			return new ModelAndView(courseAdditionalDetailsTemplate);

	}
	public ModelAndView viewCourseAdditionalDetails( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		log.debug("inside viewCourseAdditionalDetails()");
		Map model = new HashMap();		

		model.put("regulatoryRequirements", REGULATORY_REQUIREMENT);


		return new ModelAndView(courseAdditionalDetailsTemplate,"context",model);
	}
	@SuppressWarnings("unchecked")
	public ModelAndView saveUpdateCourseSummary( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		log.debug("inside saveUpdateCourseSummary .:::::::::");
		if(errors.hasErrors())
		{
			Map model = new HashMap();		
			model.put("businessUnits", BUSINESS_UNITS);			
			model.put("languages", LANGUAGES);		
			return new ModelAndView(summaryTemplate,"context",model);
		}
		CourseDetails form = (CourseDetails)command;
		Course course = getCourseType(courseAndCourseGroupService.getCourseById(form.getId()));		

		course.setCourseTitle(form.getCourseName());
		course.setCourseId(form.getCourseID());
		course.setKeywords(form.getKeywords());
		course.setCredithour(form.getCreditHours());
		course.setVersion(form.getVersion());//should i save it or not. as its already present in course.java
		course.setBussinesskey(form.getBusinessUnit());
		course.setCourseStatus(COURSE_STATUS_PUBLISHED);

		// [4/22/2010] VCS-264 :: Content Owner code was missing; required for Discussion Forum during call to Web Service
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		if(loggedInUser.getInstructor()!=null){
			ContentOwner contentOwner = loggedInUser.getContentOwner();
			if(contentOwner!=null)
				course.setContentOwner(contentOwner);
		}

		if(form.getCourseType().equals(WebLinkCourse.COURSE_TYPE))
		{
			((WebLinkCourse)course).setLink(form.getLink());
		}
		log.debug("language:::: >>>> "+form.getLanguage());
		Language language=new Language();
		try{
			language.setId(1l);
			language.setLanguage("en");
			language.setCountry("US");
			language.setVariant("En-US");			
		}catch(Exception pe)
		{
			pe.printStackTrace();
		}
		course.setLanguage(language);//its hard coded... need to have a service class for  accessing languages,
		course.setDescription(form.getDescription());	

		// [4/22/2010] VCS-264 :: Error handling in case of failure		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("errorMessage", this.saveCourse(course,errors));		

		return new ModelAndView(closeTemplate, "context", context);
	}

	// [4/22/2010] VCS-264 :: Wrapper method exclusively due to Discussion Forum type courses along with other types
	private String saveCourse(Course course, BindException error){	

		String errorCode = "";
		if(course instanceof DiscussionForumCourse){
			Course retCourse = courseAndCourseGroupService.updateDiscussionForumCourse((DiscussionForumCourse)course);
			if (retCourse == null){
				errorCode = "error.instructor.editCourse.dfcCourse.save.failed";
			}
		}
		else{
			courseAndCourseGroupService.saveCourse(course);
		}
		return errorCode;
	}



	public ModelAndView viewDFCourseCourses( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		// DFC course, course tab functionality goes here........

		return new ModelAndView(courseAdditionalDetailsTemplate);
	}
	@SuppressWarnings("unchecked")
	public ModelAndView saveUpdateDFCourseCourses( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{


		return new ModelAndView(closeTemplate);

	}
	@SuppressWarnings("unchecked")
	public ModelAndView viewCourseSchedule( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		Long instructorId;
		Map<Object, Object> context = new HashMap<Object, Object>();

		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		instructorId = user.getInstructor().getId();
		List<SynchronousClass> synchClassesList = synchronousClassService.getAllSynchronousClasses(instructorId);

		context.put("mySynchClassList", synchClassesList);

		return new ModelAndView(scheduleTemplate, "context", context);
	}
	@SuppressWarnings("unchecked")
	public ModelAndView saveUpdateCourseSchedule( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		// DFC course, course tab functionality goes here........
		return new ModelAndView(closeTemplate);

	}
	public ModelAndView cancelEditCourse( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{		
		return new ModelAndView(closeTemplate);	
	}
	public Course getCourseType(Course course)
	{

		if(course instanceof DiscussionForumCourse)
		{
			log.debug("course instanceof DiscussionForumCourse ::::: >"+(course instanceof DiscussionForumCourse));
			DiscussionForumCourse dbcourse=(DiscussionForumCourse)course;
			return dbcourse;
		}
		if(course instanceof ScormCourse)
		{
			log.debug("course instanceof ScormCourse ::::: >"+(course instanceof ScormCourse));
			ScormCourse dbcourse=(ScormCourse)course;
			return dbcourse;
		}
		if(course instanceof WebLinkCourse)
		{
			log.debug("course instanceof WebLinkCourse ::::: >"+(course instanceof WebLinkCourse));
			WebLinkCourse dbcourse=(WebLinkCourse)course;
			return dbcourse;
		}
		if(course instanceof SynchronousCourse)
		{
			log.debug("course instanceof SynchronousCourse ::::: >"+(course instanceof SynchronousCourse));
			SynchronousCourse dbcourse=(SynchronousCourse)course;
			return dbcourse;
		}
		if(course instanceof WebinarCourse)
		{
			log.debug("course instanceof WebinarCourse ::::: >"+(course instanceof WebinarCourse));
			WebinarCourse dbcourse=(WebinarCourse)course;
			return dbcourse;
		}
		if(course instanceof ExternalCourse)
		{
			log.debug("course instanceof ExternalCourse ::::: >"+(course instanceof ExternalCourse));
			ExternalCourse dbcourse=(ExternalCourse)course;
			return dbcourse;
		}
		if(course instanceof LegacyCourse)
		{
			log.debug("course instanceof LegacyCourse ::::: >"+(course instanceof LegacyCourse));
			LegacyCourse dbcourse=(LegacyCourse)course;
			return dbcourse;
		}
		if(course instanceof SelfPacedCourse)
		{
			log.debug("course instanceof SelfPacedCourse ::::: >"+(course instanceof SelfPacedCourse));
			SelfPacedCourse dbcourse=(SelfPacedCourse)course;
			return dbcourse;
		}
		if(course instanceof AICCCourse) {
			log.debug("Course instanceof AIC Course ::::: >"+(course instanceof AICCCourse));
			AICCCourse dbcourse = (AICCCourse) course;
			return dbcourse;
		}
		return new Course();

	}
	/*
	 * Getter Setter Area
	 */
	public String getSummaryTemplate() {
		return summaryTemplate;
	}

	public void setSummaryTemplate(String summaryTemplate) {
		this.summaryTemplate = summaryTemplate;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	public String getCourseOverViewTemplate() {
		return courseOverViewTemplate;
	}

	public void setCourseOverViewTemplate(String courseOverViewTemplate) {
		this.courseOverViewTemplate = courseOverViewTemplate;
	}

	public String getCourseExamOverviewTemplate() {
		return courseExamOverviewTemplate;
	}

	public void setCourseExamOverviewTemplate(String courseExamOverviewTemplate) {
		this.courseExamOverviewTemplate = courseExamOverviewTemplate;
	}

	public String getCourseAdditionalDetailsTemplate() {
		return courseAdditionalDetailsTemplate;
	}

	public void setCourseAdditionalDetailsTemplate(String courseAdditionalDetailsTemplate) {
		this.courseAdditionalDetailsTemplate = courseAdditionalDetailsTemplate;
	}
	public String getScheduleTemplate() {
		return scheduleTemplate;
	}

	public void setScheduleTemplate(String scheduleTemplate) {
		this.scheduleTemplate = scheduleTemplate;
	}

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}


	public ModelAndView saveAllInfo( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		
		log.debug("inside saveAllInfo method");
		CourseDetails form = (CourseDetails)command;
		Course course = getCourseType(courseAndCourseGroupService.loadForUpdateCourse(form.getId()));
		//Course Over View
		course.setCourseGuide(form.getCourseGuide());
		course.setCoursePrereq(form.getPreRequisites());
		
		//Course Exam Over View
		
		course.setLearningObjectives(form.getLearningObjectives());
		course.setQuizInfo(form.getQuizInformation());
		course.setFinalExamInfo(form.getFinalExamInformation());
		course.setEndofCourseInstructions(form.getEndOfCourseInstructions());
		
		//Additional Details
		if(errors.hasErrors())
		{
			Map model = new HashMap();		
			model.put("regulatoryRequirements", REGULATORY_REQUIREMENT);
			
			if(!StringUtils.isBlank(request.getParameter("pageView")))
				return new ModelAndView(summaryTemplate,"context",model);
			else	
				return new ModelAndView(courseAdditionalDetailsTemplate,"context",model);
			 
		}
		if(!errors.hasErrors())
		{
			log.debug("inside saveUpdateCourseExamOverView method");
		

			try{
		    	 course.setDeliveryMethodId(Long.parseLong(form.getDeliveryMethodId()));
			}catch(Exception sube){
				course.setDeliveryMethodId(1l);
			}
			
			if(form.getMsrp()!=null && !form.getMsrp().equals(""))
			{
				course.setMsrp(Double.parseDouble(form.getMsrp()));
			}
			else
				course.setMsrp(0.0);
			course.setCode(form.getCourseCode());
			if(form.getApprovedCourseHours()!=null && !form.getApprovedCourseHours().equals(""))
			{
				course.setApprovedcoursehours(Double.parseDouble(form.getApprovedCourseHours()));
			}
			else
				course.setApprovedcoursehours(0.0);
			course.setApprovalNumber(form.getApprovalNumber());
			course.setCurrency(form.getCurrency());
			if(form.getProductPrice()!=null && !form.getProductPrice().equals(""))
			{
				course.setProductprice(Double.parseDouble(form.getProductPrice()));
			}
			else
				course.setProductprice(0.0);
			if(form.getWholeSalePrice()!=null && !form.getWholeSalePrice().equals(""))
			{
				course.setWholeSalePrice(Double.parseDouble(form.getWholeSalePrice()));
			}
			else
				course.setWholeSalePrice(0.0);
			course.setRoyaltyPartner(form.getRoyaltyPartner());
			course.setRoyaltyType(form.getRoyaltyType());
			course.setStateRegistartionRequired(form.getRegulatoryRequirement());
			if(form.getTermsOfService()!=null && !form.getTermsOfService().equals(""))
			{
				course.setTos(Integer.parseInt(form.getTermsOfService()));
			}
			else
				course.setTos(0);

			course.setLearningObjectives(form.getLearningObjectives());

		}
		else{
			return new ModelAndView(courseAdditionalDetailsTemplate);
		}

		
		//Save Summary
		
		course.setCourseTitle(form.getCourseName());
		course.setCourseId(form.getCourseID());
		course.setKeywords(form.getKeywords());
		course.setCredithour(form.getCreditHours());
		course.setVersion(form.getVersion());//should i save it or not. as its already present in course.java
		course.setBussinesskey(form.getBusinessUnit());
		course.setCourseStatus(COURSE_STATUS_PUBLISHED);

		// [4/22/2010] VCS-264 :: Content Owner code was missing; required for Discussion Forum during call to Web Service
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		ContentOwner contentOwner = loggedInUser.getLearner().getCustomer().getContentOwner();
		course.setContentOwner(contentOwner);

		if(form.getCourseType().equals(WebLinkCourse.COURSE_TYPE))
		{
			((WebLinkCourse)course).setLink(form.getLink());
		}
		log.debug("language:::: >>>> "+form.getLanguage());
		Language language=new Language();
		try{
			language.setId(1l);
			language.setLanguage("en");
			language.setCountry("US");
			language.setVariant("En-US");			
		}catch(Exception pe)
		{
			pe.printStackTrace();
		}
		course.setLanguage(language);//its hard coded... need to have a service class for  accessing languages,
		course.setDescription(form.getDescription());	

		// [4/22/2010] VCS-264 :: Error handling in case of failure		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("errorMessage", this.saveCourse(course,errors));	

		courseAndCourseGroupService.saveCourse(course);
		return new ModelAndView(closeTemplate, "context", context);
	}



}
