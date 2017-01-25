/**
 * 
 */
package com.softech.vu360.lms.web.controller.instructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.LMSProductPurchaseService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.TimeZoneService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.ClassForm;
import com.softech.vu360.lms.web.controller.model.SessionForm;
import com.softech.vu360.lms.web.controller.validator.AddSynchronousClassScheduleValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Noman
 *
 */


public class AddSynchronousClassScheduleController extends AbstractWizardFormController{
	private static final Logger log = Logger.getLogger(AddSynchronousClassScheduleController.class.getName());
	
	private SynchronousClassService synchronousClassService;
	private CourseAndCourseGroupService courseAndCourseGroupService;
	private TimeZoneService timeZoneService = null;
	private VelocityEngine velocityEngine;
	private String closeTemplate = null;
//	private String skipValidation=null;
	private LMSProductPurchaseService lmsProductPurchaseService;
	
	public AddSynchronousClassScheduleController() {
		super();
		setCommandName("classForm");
		setCommandClass(ClassForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"instructor/manageSynchronousClass/addClass/Step1",
				"instructor/manageSynchronousClass/addClass/Step2", 
				"instructor/manageSynchronousClass/addClass/Step3",
				"instructor/manageSynchronousClass/addClass/addRecurrenceForSynClass_step1",
				"instructor/manageSynchronousClass/addClass/addRecurrenceForSynClass_step2"});
	}

	/**
	 * Step 1.
	 * We do not need to override this method now.
	 * This method basically lets us hook in to the point
	 * before the request data is bound into the form/command object
	 * This is called first when a new request is made and then on
	 * every subsequent request. However in our case, 
	 * since the bindOnNewForm is true this 
	 * will NOT be called on subsequent requests...
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		log.debug(request.getParameter("id"));
		log.debug("IN formBackingObject in addSynchrounous class ");
		Object command = super.formBackingObject(request);
		ClassForm classForm = (ClassForm)command;
		log.debug("formBackingObject className : " + classForm.getClassName());
		log.debug("formBackingObject meetinType : " + classForm.getMeetingType());
		log.debug("formBackingObject pattern : " + classForm.getPattern());

		return command;
	}

	/**
	 * Step 2.
	 * We do not need to override this method now.
	 * This method lets us hook in to the point
	 * before the request data is bound into the form/command object
	 * and just before the binder is initialized...
	 * We can have customized binders used here to interpret the request fields
	 * according to our requirements. It allows us to register 
	 * custom editors for certain fields.
	 * Called on the first request to this form wizard.
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		log.debug("IN initBinder");
		// TODO Auto-generated method stub
		super.initBinder(request, binder);
	}

	/**
	 * Step 3.
	 * We do not need to override this method now.
	 * Since we have bindOnNewForm property set to true in the constructor
	 * this method will be called when the first request is processed.
	 * We can add custom implentation here later to populate the command object
	 * accordingly.
	 * Called on the first request to this form wizard.
	 */
	protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException binder) throws Exception {
		log.debug("IN onBindOnNewForm");
		// TODO Auto-generated method stub
		
		super.onBindOnNewForm(request, command, binder);
	}

	/**
	 * Step 4.
	 * Shows the first form view.
	 * Called on the first request to this form wizard.
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
		log.debug("IN showForm");
		// check for customer selection
		
		ModelAndView modelNView = super.showForm(request, response, binder);
		String view = modelNView.getViewName();
		log.debug("OUT showForm for view = "+view);
		return modelNView;
	}

	/**
	 * Called by showForm and showPage ... get some standard data for this page
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		log.debug("IN referenceDat a");  
 		ClassForm classForm = (ClassForm)command;
 		if(classForm.getCourseType()==null || (classForm.getCourseType()!=null && classForm.getCourseType().isEmpty())){
 			classForm.setCourseType(this.findCourseType(classForm.getId()));
 			if(classForm.getCourseType()!=null && classForm.getCourseType().equals(WebinarCourse.COURSE_TYPE)){
 				if(classForm.getMeetingType()==null || (classForm.getMeetingType()!=null && classForm.getMeetingType().isEmpty())){
 					classForm.setMeetingType("Webinar"); //Setting default to Webinar
 				}
 			}
 		}
 		List<SynchronousSession> listSynchronousSession=classForm.getSynchronousSessionList();
 		try{
 			classForm.setTimeZone(timeZoneService.getTimeZoneById( classForm.getTimeZoneId() ));
 		}
 		catch(Exception e){
 			log.debug("Timezone Retrieval Error:\n"+e.getStackTrace());
 		}
		if(request.getSession().getAttribute("sync_CourseTitle")!=null)
			classForm.setCourseName(request.getSession().getAttribute("sync_CourseTitle").toString());
 		
		Map model = new HashMap();
		log.debug("courseType = "+request.getParameter("courseType"));
		model.put("courseType",request.getParameter("courseType"));
		switch( page){
		case 0:  
			model.put( "timeZoneList", timeZoneService.getAllTimeZone() );
			
			//Start: LMS-15941 - SRS: 2.3.2 > 2.3.2.1
			boolean showLS360Option = false;
			boolean showOtherProviderOption = false;
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				Customer currentCustomer=details.getCurrentCustomer();
				if(currentCustomer.getCustomerType().equalsIgnoreCase("B2B")&& currentCustomer.getContentOwner()!=null && 
						(currentCustomer.getContentOwner().getPlanTypeId()==null || 
						(currentCustomer.getContentOwner().getPlanTypeId()!=null && currentCustomer.getContentOwner().getPlanTypeId()==1))){
					showLS360Option = true;
					showOtherProviderOption = true;
					
				}
				/*long customerId = details.getCurrentCustomer().getId();
				List<LMSProductCourseType> allowedCourseTypes = lmsProductPurchaseService.getCustomerCourseTypes(customerId);
				if(allowedCourseTypes!=null && allowedCourseTypes.size()>0){
					for (LMSProductCourseType lmsProductCourseType : allowedCourseTypes) {
						if(lmsProductCourseType.getCourseType()!=null && lmsProductCourseType.getCourseType().equals(SynchronousCourse.COURSE_TYPE)){
							showOtherProviderOption = true;
							
						}
					}
				}*/
			}
			model.put("showLS360Option", showLS360Option);
			model.put("showOtherProviderOption", showOtherProviderOption);
			//End: LMS-15941 - SRS: 2.3.2 > 2.3.2.1
			
			break;
		case 1:
			classForm.getWeekDaysList().clear();
			if(!listSynchronousSession.isEmpty()) {//if schedule is added Manually 
				createListForVOAndModel(classForm, listSynchronousSession);
			}
			break;
		case 2:
			log.debug("page 1 referenceData className : " + classForm.getClassName());
			log.debug("page 1 referenceData meetinType : " + classForm.getMeetingType());
			log.debug("page 1 referenceData pattern : " + classForm.getPattern());
			
			if(listSynchronousSession.isEmpty()){
				classForm.initClassSessionList();
				classForm.generateSessions();
			}
			
			model.put("sessionList", classForm.getClassSessionList());
			model.put( "timeZone", timeZoneService.getTimeZoneById( classForm.getTimeZoneId() ).getFormattedTimeZone() );
			classForm.initWeekDaysList();
			break;
			
			/**
			 * Work to be done on processFinish
			 */
		case 4:
			SynchronousSession synchronousSession=new SynchronousSession();
			synchronousSession.setId(RandomUtils.nextLong());
			synchronousSession.setStartDateTime(classForm.getStartDateTime());
			synchronousSession.setEndDateTime(classForm.getEndDateTime());
			classForm.addSynchronousSessionList(synchronousSession);
//			using map for searching and when deleting record for getting object to be deleted bc on delete
//			we will only get id's not objects
			classForm.addRecurrenceSessionsToDeleteMap(synchronousSession.getId().toString(),synchronousSession);
			// will not allow users to add any other Schedule if once Recurrence is added from manual
			classForm.setManualSession(true);
			
			model.put("synchSessions", classForm.getSynchronousSessionList());
			break;
			
		default:
			break;
		}
		 
		return model;
	}

	private void createListForVOAndModel(ClassForm classForm,
			List<SynchronousSession> listSynchronousSession) {
		String recurrenceSessionsToDelete=classForm.getRecurrenceSessionsToDelete();
		if(!StringUtils.isEmpty(recurrenceSessionsToDelete)) {
			String[] recurrenceSessionsToDeleteArray=recurrenceSessionsToDelete.split(",");
			
			for(String recurrenceSessionsID:recurrenceSessionsToDeleteArray){
				Map<String, SynchronousSession> mapSynchronousSession=classForm.getRecurrenceSessionsToDeleteMap();
				 if(mapSynchronousSession.containsKey(recurrenceSessionsID)){
					 SynchronousSession synchronousSession=mapSynchronousSession.get(recurrenceSessionsID);
					 mapSynchronousSession.remove(recurrenceSessionsID);
					 listSynchronousSession.remove(synchronousSession); 
				 }
			}
		}
		classForm.initClassSessionList();
		if(!listSynchronousSession.isEmpty()) {
			for(SynchronousSession synchronousSession:classForm.getSynchronousSessionList()){
				SessionForm sessionForm = new SessionForm();
				sessionForm.setId(synchronousSession.getId());
				sessionForm.setStartDateTime(synchronousSession.getStartDateTime());
				sessionForm.setEndDateTime(synchronousSession.getEndDateTime());
				sessionForm.setStartDate(classForm.getFormattedDate(synchronousSession.getStartDateTime(),classForm.getTimeZone().getHours()));
				sessionForm.setEndDate(classForm.getFormattedDate(synchronousSession.getEndDateTime(),classForm.getTimeZone().getHours()));
				classForm.addClassSessionList(sessionForm);
				}
		}else{
			classForm.setManualSession(false);
		}
	}

	/**
	 * The Validator's default validate method WILL NOT BE CALLED by a wizard form controller!
	 * We need to do implementation specific - page by page - validation
	 * by explicitly calling the validateXXX function in the validator
	 */
	protected void validatePage(Object command, Errors errors, int page) {
		log.debug("IN validatePage");
 
		ClassForm classForm = (ClassForm)command;
		AddSynchronousClassScheduleValidator addSynClassValidator = (AddSynchronousClassScheduleValidator) getValidator();
		
		errors.setNestedPath("");
		switch (page) { 

		case 0:
			addSynClassValidator.validatePage1(classForm, errors);
			break;
		case 1:
			if(classForm.getSynchronousSessionList().isEmpty()){
				addSynClassValidator.validatePage2(classForm, errors);
			}
			break;
		case 3:
			addSynClassValidator.validatePage3(classForm, errors);
			break;
		default:
			break;
		}
		errors.setNestedPath("");
		
	}

	/**
	 * we can do custom processing after binding and validation
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException error, int page) throws Exception {
		log.debug("IN onBindAndValidate");
		super.onBindAndValidate(request, command, error, page);
	}

	//16.01.2009
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		ClassForm classForm = (ClassForm)command;
		
		log.debug("onBind className : " + classForm.getClassName());
		log.debug("onBind meetingType : " + classForm.getMeetingType());
		log.debug("onBind pattern : " + classForm.getPattern());
		
		int Page=getCurrentPage(request);
		if (Page==0){
			// Sana Majeed | LMS-4180
			if (request.getParameter("onlineMeetingTF") == null){ // done for checkbox if unchecked
				classForm.setOnlineMeetingTF(false);
				classForm.setMeetingId("");
				classForm.setMeetingPasscode("");
				classForm.setMeetingURL("");
			}
			if (!StringUtils.isBlank( classForm.getEnrollmentCloseDateString() )){
				String strEnrolCloseDate=classForm.getEnrollmentCloseDateString()+" 11:59:59 PM";
				classForm.setEnrollmentCloseDate( FormUtil.getInstance().getDate(strEnrolCloseDate , "MM/dd/yyyy hh:mm:ss a"));
			} 
		}
		super.onBind(request, command, errors);

			
		if (Page==1){
			classForm.setTimeZone( timeZoneService.getTimeZoneById( classForm.getTimeZoneId() ) );
		}
		else if (Page==0){
			if (!StringUtils.isBlank( classForm.getEnrollmentCloseDateString() )){
					String strEnrolCloseDate=classForm.getEnrollmentCloseDateString()+" 11:59:59 PM";
					classForm.setEnrollmentCloseDate( FormUtil.getInstance().getDate(strEnrolCloseDate , "MM/dd/yyyy hh:mm:ss a"));
			} 
		}
		
		if(Page==4){
			classForm.setStartDateTime(null);
			classForm.setEndDateTime(null);
			classForm.setStartDate(null);
			classForm.setEndDate(null);
			classForm.setStartTime(null);
			classForm.setEndTime(null);
		}
		
		super.onBind(request, command, errors);
	}
	
	//16.01.2009
	@Override
	protected int getCurrentPage(HttpServletRequest request) {
		return super.getCurrentPage(request);
	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		return super.getTargetPage(request, command, errors, currentPage);
	}
	
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(closeTemplate);
	}
	
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {

		ClassForm classForm = (ClassForm)command;
		
//		if Schedule for class is manual then following code will run
		if(!classForm.getClassSessionList().isEmpty()){
			setStartAndEndDateForManualSchedule(classForm);
		} 
		
		classForm.setTimeZone( timeZoneService.getTimeZoneById( classForm.getTimeZoneId() ) );
		synchronousClassService.addSchedule(classForm);
		
		// if Synchronous Course is MyWebinarPlace
		if(classForm.isOnlineMeetingTF() && classForm.getMeetingType().equals(SynchronousClass.MEETINGTYPE_WEBINAR)) {
			SendWebinarURLEmailToPresenter(classForm);
		}
		
		return new ModelAndView("redirect:ins_manageSynchronousClass.do?id="+classForm.getId()+"&courseType="+request.getParameter("courseType"));
	}

	private void SendWebinarURLEmailToPresenter(ClassForm syncClass) {
		StringBuilder presenterURL = new StringBuilder(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL"));
		presenterURL.append("&");
		presenterURL.append(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.mt"));
		presenterURL.append("=");
		presenterURL.append(syncClass.getMeetingId());
		presenterURL.append("&");
		presenterURL.append(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.first_name"));
		presenterURL.append("=");
		presenterURL.append(syncClass.getPresenterFirstName());
		presenterURL.append("&");
		presenterURL.append(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.last_name"));
		presenterURL.append("=");
		presenterURL.append(syncClass.getPresenterLastName());
		presenterURL.append("&");
		presenterURL.append(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.presenter.parameters.email"));
		presenterURL.append("=");
		presenterURL.append(syncClass.getPresenterEmailAddress());			

		//Send email to presenter
		SendMailService.sendSMTPMessage(syncClass.getPresenterEmailAddress(),
				VU360Properties.getVU360Property("lms.instructor.mywebinarplace.email.from"),
				VU360Properties.getVU360Property("lms.instructor.mywebinarplace.email.from"),
				VU360Properties.getVU360Property("lms.instructor.mywebinarplace.email.subject"), 
				VU360Properties.getVU360Property("lms.instructor.mywebinarplace.email.body") + presenterURL.toString());
	}
	
	
	private void setStartAndEndDateForManualSchedule(ClassForm classForm) {
		List<Date> startDateList=new ArrayList<Date>();
		List<Date> endDateList=new ArrayList<Date>();
		
		for(SessionForm sessionForm:classForm.getClassSessionList()){
			startDateList.add(sessionForm.getStartDateTime());
			endDateList.add(sessionForm.getEndDateTime());
		}
		
		classForm.setStartDate(FormUtil.getInstance().getFormatedDateInfo(Collections.min(startDateList),"MM/dd/yyyy hh:mm:ss a" ) );
		classForm.setEndDate(FormUtil.getInstance().getFormatedDateInfo(Collections.max(endDateList),"MM/dd/yyyy hh:mm:ss a" ) ); // LMS-9274
	}
	
	private String findCourseType(Long courseId){
		
		String courseType = null;
		try{
			if(courseId>0){
				Course course = courseAndCourseGroupService.getCourseByIdWithNoCache(courseId);
				
				if(course instanceof WebinarCourse){
					courseType = WebinarCourse.COURSE_TYPE;
				}
				else{
					courseType = SynchronousCourse.COURSE_TYPE;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return courseType;
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

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}
	
	public TimeZoneService getTimeZoneService() {
		return timeZoneService;
	}

	public void setTimeZoneService(TimeZoneService timeZoneService) {
		this.timeZoneService = timeZoneService;
	}

	public LMSProductPurchaseService getLmsProductPurchaseService() {
		return lmsProductPurchaseService;
	}

	public void setLmsProductPurchaseService(
			LMSProductPurchaseService lmsProductPurchaseService) {
		this.lmsProductPurchaseService = lmsProductPurchaseService;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}
	
}
