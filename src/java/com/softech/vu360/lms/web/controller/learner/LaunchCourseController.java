package com.softech.vu360.lms.web.controller.learner;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.meetingservice.SynchronousMeetingService;
import com.softech.vu360.lms.meetingservice.SynchronousMeetingServiceFactory;
import com.softech.vu360.lms.model.AICCAssignableUnit;
import com.softech.vu360.lms.model.AICCCourse;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CoursePlayerType;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.HomeWorkAssignmentAsset;
import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.InstructorCourse;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmission;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.SCO;
import com.softech.vu360.lms.model.ScormCourse;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.AICCAssignableUnitService;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CoursePlayerTypeService;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LaunchCourseService;
import com.softech.vu360.lms.service.LearnerHomeworkAssignmentSubmissionService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.CourseApprovalVO;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.lms.web.controller.model.CreditReportingForm;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldBuilder;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.validator.CreditReportingFormValidator;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * The controller for the Launch a Course. /**
 * 
 * @author Ashis
 * 
 */
public class LaunchCourseController extends VU360BaseMultiActionController {// implements
    protected static final String VIEW_CONTEXT = "context";
	private static final String SUBSCRIPTION_VIEW = "learner/subscription";
	private static final String ENROLLMENTEXPIRED_VIEW = "learner/enrollmentExpired";
	private static final String INVALIDLOCATION_ACCESS_VIEW = "learner/myCourse/myInvalidAccessLocation";
	protected static final String BRAND_SUBSCRIPTION_ONHOLD = "lms.learner.enrollment.subscription.onhold";
	protected static final String BRAND_SESSION_EXPIRED = "lms.learner.session.expired";
	protected static final String SUBSCRIPTION_STATUS_ONHOLD = "ONHOLD";
	protected static final String COURSE_STARTED = "Course_Started";
	protected static final String COURSE_ENROLLED = "Course_Enrolled";
	protected static final String COURSE_COMPLETED = "Course_Completed";
	// Controller
    // {
    private static final Logger log = Logger.getLogger(LaunchCourseController.class.getName());
    private String launchTemplate = null;
    private String errorTemplate = null;
    private String learnerHomeworkAssignment = null;
    private String syncClassRoomErrorTemplate = null;
    private String jurisdictionSuggestTemplate = null;
    private String noAdditionCourseApprovalTemplate = null;
    private String learnerInstructorConnect = null;
    private SynchronousMeetingServiceFactory meetingServiceFactory;
    private LearnersToBeMailedService learnersToBeMailedService;
    private VelocityEngine velocityEngine;
    private EntitlementService entitlementService;
	private StatisticsService statsService = null;
    private LaunchCourseService launchCourseService = null;
    private CourseAndCourseGroupService courseAndCourseGroupService = null;
    private AccreditationService accreditationService = null;
    private LearnerHomeworkAssignmentSubmissionService learnerHomeworkAssignmentSubmissionService;
    private LearnerService learnerService = null;
    private CustomFieldService customFieldService;
    private VU360UserService userService;
    private EnrollmentService enrollmentService;
    private JavaMailSenderImpl mailSender;
    private CustomerService customerService = null;
    private DistributorService distributorService = null;
    private CoursePlayerTypeService coursePlayerTypeService=null;
    private AICCAssignableUnitService aiccAssignableUnitService=null;

    public LaunchCourseController() {
	super();

    }

    public LaunchCourseController(Object delegate) {
	super(delegate);
    }

  
    @Override
    protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
	
    	CreditReportingForm form = (CreditReportingForm) command;
    	List<CourseApprovalVO> courseApproval = new ArrayList<CourseApprovalVO>();
    	String learnerEnrollmentID = request.getParameter("learnerEnrollmentId");
    	HashMap<String, Integer> fileDetails = new HashMap<String, Integer>();
    	String filename = null;
    	String fileLocation = null;
    	String filePath = null;
    	
    	
    	if(methodName==null)
    		methodName = "";
    	
    	if(request.getParameter("courseId")!=null && learnerEnrollmentID!=null 
    			&& request.getParameter("isAjaxCall")==null &&
    			  !methodName.equals("saveMissingCustomFieldsAndCreditReportingFields")){
    	    		
    		/* reset reporting field list */
    		form.setCreditReportingFields(new ArrayList<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField>());
    		com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		/* reset custom field list */
    		form.setMissingCustomFields(new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>());
    		
    		setCustomField(vu360User, form);
    		
    		String courseId = request.getParameter("courseId");
    		Course objCourse = null;
    		objCourse = courseAndCourseGroupService.getCourseByGUID(courseId);
    		if (objCourse == null) {
    			objCourse = courseAndCourseGroupService.getCourseById(new Long(courseId));
    		}
    		
    		if(objCourse != null)
    		{
    			if(objCourse instanceof InstructorConnectCourse)
    			{
	    			InstructorConnectCourse connectCourse = (InstructorConnectCourse) objCourse;
	    		    //form.setLearnerEmailAddress(connectCourse.getEmailAddress());
	    			
	    			if (connectCourse.getInstructorType().equals("Email Online")) {
	    			
	    			form.setLearnerEmailAddress(vu360User.getEmailAddress());
	    		    String instructorEmailAddresses = "";
	    		    log.debug("connectCourse : " + connectCourse.getId());
	    		    List<InstructorCourse> instructorCourses = connectCourse.getInstructorCourses();
	    		    log.debug("instructorCourses : " + instructorCourses.size());
	    		    Iterator<InstructorCourse> iterator = instructorCourses.iterator();
	    		    while (iterator.hasNext()) {
	    			InstructorCourse instructorCourse = iterator.next();
	    			if (StringUtils.isBlank(instructorEmailAddresses)) {
	    			    instructorEmailAddresses = instructorCourse.getInstructor().getUser().getEmailAddress();
	    			} else {
	    			    instructorEmailAddresses += "," + instructorCourse.getInstructor().getUser().getEmailAddress();
	    			}
	
	    		    }
	
	    		    form.setInstructorEmailAddresses(instructorEmailAddresses);
    			}
    		  }
    		}
    		
    		String homeWorkAssignmentdocid = request.getParameter("downloadfilename");
    		if (homeWorkAssignmentdocid != null)
    		{
    		   form.setHomeWorkAssignmentdocid(homeWorkAssignmentdocid);
    		}
    		    		
    		LearnerEnrollment learnerEnrollment = null;
    		if(learnerEnrollmentID != null)
    		{
    		  learnerEnrollment = entitlementService.getLearnerEnrollmentById(Long.valueOf(learnerEnrollmentID));
    		    		
    		    		
    		  if(learnerEnrollment.getCourse().getCourseType().equals("Learner Assignment"))
    		    	{
    		    		Course course = learnerEnrollment.getCourse();
    		    		List<HomeWorkAssignmentAsset> lsthomeWorkAssignmentAsset = learnerHomeworkAssignmentSubmissionService.getUploadedHomeWorkDocuments(course.getId());
    		 			List<Document> lstDoc = new ArrayList<Document>(); 
    		    		if(lsthomeWorkAssignmentAsset != null)
    		   			 {
    		    				if(lsthomeWorkAssignmentAsset.size() > 0)
    		    				{
    		    					for(HomeWorkAssignmentAsset homeWorkAssignmentAsset : lsthomeWorkAssignmentAsset)
    		   					    {
    		    						Document doc = new Document();
    		    						double bytes,kilobytes = 0;
    		    						int imgsize = 0;
    		    						doc = (Document)homeWorkAssignmentAsset.getAsset();
    		    						lstDoc.add(doc);
    		    						filename = doc.getFileName();
    		    						fileLocation = VU360Properties.getVU360Property("hwassignment.saveLocation");
    		    						filePath = fileLocation + File.separator + filename;
    		    						log.debug(filePath);
    		    						File file = new File(filePath);
    		    						if(file.exists())
    		    						{
    		    							bytes = file.length();
    		    							kilobytes = (bytes / 1024);
    		    							imgsize = (int)kilobytes;
    		    						}
    		    						
    		    						fileDetails.put(filename,imgsize);
    		    						
    		    					  }
    		    					form.setLstHomeWorkAssignment(lstDoc);
    		    					form.setFileDetails(fileDetails);
    		    					
    		    				}
    		    			}
    		    			
    		    			
    		   		  }
    		    }
    		
    		
    		//boolean isCASelected = accreditationService.isCourseApprovalSelected(Long.valueOf(learnerEnrollmentID));
    		boolean isCASelected = accreditationService.isCourseApprovalSelected(Long.valueOf(learnerEnrollmentID),  Long.valueOf(learnerEnrollment.getLearner().getId()), learnerEnrollment.getCourse().getCourseGUID());
    		form.setSuggestedCourseApprovalFound("");
    		/**
    		 * LMS-15942
    		 * Jurisdiction Suggest 
    		 * Only show course approval those match to their jurisdiction.  
    		 */
    		
	    	if(!isCASelected && objCourse != null){

	    		if(StringUtils.isNotEmpty(learnerEnrollmentID)){
	    			courseApproval=accreditationService.getCourseApprovalByCourse_Jurisdiction(objCourse.getCourseGUID(), learnerEnrollmentID);
	    			if(courseApproval.size()>0)
	    				form.setSuggestedCourseApprovalFound("true");
	    		}
	    		
	    		//if not match result found then get all regulators.
	    		String ShowAllRegulatorsBecouseNoMatchRegulatorFound="-1";
	    		if(courseApproval.size()<=0 || "Y".equals(request.getParameter("showAllRegulators"))){
	    			courseApproval=accreditationService.getCourseApprovalByCourse_Jurisdiction(objCourse.getCourseGUID(), ShowAllRegulatorsBecouseNoMatchRegulatorFound);
	    			form.setShowAllRegulators("true");
	    			
	    			if(courseApproval!=null && courseApproval.size()>0)
	    				form.setNoneSuggestedCourseApprovalFound("true");
	    		}
	    		else {
	    			form.setShowAllRegulators("false");
	    			
	    			//check any additional Regulater found except SuggestedOne which has retived before.
	    			//do not show option 3 in case NO addition regulator found.
	    			//Addition Regulator/CourseApproval which fall in same course group (Course group attached to CourseApproval)
	    			List<CourseApprovalVO> additionCourseApproval = new ArrayList<CourseApprovalVO>();
	    			additionCourseApproval=accreditationService.getCourseApprovalByCourse_Jurisdiction(objCourse.getCourseGUID(), ShowAllRegulatorsBecouseNoMatchRegulatorFound);
	    			
	    			if("true".equalsIgnoreCase(form.getSuggestedCourseApprovalFound())){
	    				if(!isAdditionRegulatorFound(courseApproval, additionCourseApproval))
	    					form.setNoneSuggestedCourseApprovalFound("true");
	    				else 
	    					form.setNoneSuggestedCourseApprovalFound("false");
	    			}

	    		}
	    		
	    		form.setCourseApproval(courseApproval); 
	    	}
	    	else
	    	{
	    		form.setCourseApproval(courseApproval); 	    		
	    	}
	    	
	    	//this check is help full for showing new added CreditReporting field during any course (inProgress course status)
	    	//do not set any CourseApproval in form in case CourseApproval already selected.  
	    	Long alreadySelectedApproval_id=null;
	    	if(isCASelected ){
	    		//alreadySelectedApproval_id = accreditationService.getCourseApprovalSelected(  Long.valueOf(request.getParameter("learnerEnrollmentId")) );  		
	    		alreadySelectedApproval_id = accreditationService.getCourseApprovalSelected(  Long.valueOf(request.getParameter("learnerEnrollmentId")), Long.valueOf(learnerEnrollment.getLearner().getId()), learnerEnrollment.getCourse().getCourseGUID());
	    		form.setJurisdictionSuggestionYN("");
	    	}
	    	


	    	
	    	// Set CourseApprovalId to get first CourseApproval id from list in case of user launch the course  
	    	if(courseApproval!=null && courseApproval.size()>0)
	    		form.setCourseApprovalId(courseApproval.get(0).getCourseApprovalId());
	    	else if(alreadySelectedApproval_id != null){
	    		form.setCourseApprovalId(String.valueOf(alreadySelectedApproval_id));
	    	}
	    	else{
	    		form.setCourseApproval(new ArrayList<CourseApprovalVO>()) ;
	    		form.setCourseApprovalId("0");
	    		form.setJurisdictionSuggestionYN("");
	    	}
	    	
	    	/*
  		    	if(isCASelected){
		    		form.setCourseApproval(new ArrayList<CourseApprovalVO>()) ;
		    		form.setCourseApprovalId("0");
	    		}
	    	*/	    
	    	
	    	if(objCourse != null)
	    		form.setCourseName(objCourse.getCourseTitle());
	    	else{
	    		form.setCourseApproval(new ArrayList<CourseApprovalVO>()) ;
	    		form.setCourseApprovalId("0");
	    	}
	    	
	    	String externalLMSSessionId =request.getParameter("externallmssessionid");
	    	String LaunchSource =request.getParameter("source");
	    	String LaunchExternalLmsUrl= request.getParameter("externallmsurl");
	    	
	    	if(externalLMSSessionId!=null && externalLMSSessionId.length()>0 && (form.getexternallmssessionid() ==null || (form.getexternallmssessionid() !=null && form.getexternallmssessionid().length() == 0)))
	    	{
	    		form.setexternallmssessionid(externalLMSSessionId);
	    	}
	    	
	    	if(LaunchSource!=null && LaunchSource.length()>0 && (form.getSource() ==null || (form.getSource() !=null && form.getSource().length() == 0)))
	    	{
	    		form.setSource(LaunchSource);
	    	}
	    	
	    	if(LaunchExternalLmsUrl!=null && LaunchExternalLmsUrl.length()>0 && (form.getexternallmsurl() ==null || (form.getexternallmsurl() !=null && form.getexternallmsurl().length() == 0)))
	    	{
	    		form.setexternallmsurl(LaunchExternalLmsUrl);
	    	}
	    	
	    	//LMS-15975
	    	String textOfdailog=objCourse.getInstructionForLearnerFromRegulator();
	    	if(objCourse.getEnableCoursApprovalInfo()!=null && objCourse.getEnableCoursApprovalInfo()==true){
	    		Brander brander = VU360Branding.getInstance().getBranderByUser(request, vu360User);
	    		form.setFindMoreInfoLink(brander.getBrandElement("lms.launchCourse.courseAapproval.add.helpText"));
	    		if(! StringUtils.isEmpty(textOfdailog))
	    			form.setFindMoreInfoTxt(textOfdailog.replaceAll("\\<[^>]*>",""));
	    		else
	    			form.setFindMoreInfoTxt(" ");
	    	}
    	}
    }

    private Boolean isAdditionRegulatorFound(
			List<CourseApprovalVO> courseApproval,
			List<CourseApprovalVO> additionCourseApproval) {
    		
    	List<CourseApprovalVO> addList = new ArrayList<CourseApprovalVO>();
    			
    	for (Iterator<CourseApprovalVO> iterator = additionCourseApproval.iterator(); iterator.hasNext();) {
			CourseApprovalVO additionalAourseApprovalVO = (CourseApprovalVO) iterator.next();
			
			for (Iterator<CourseApprovalVO> iterator2 = courseApproval.iterator(); iterator2.hasNext();) {
				CourseApprovalVO courseApprovalVO = (CourseApprovalVO) iterator2.next();
				
				if(Long.parseLong(additionalAourseApprovalVO.getCourseApprovalId())== Long.parseLong(courseApprovalVO.getCourseApprovalId()))
					addList.add(additionalAourseApprovalVO);
			}
		}
    	
    	
		additionCourseApproval.removeAll(addList);
		return additionCourseApproval.isEmpty();
		
	}


	///--
    public ModelAndView displayLearnerProfile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
    throws Exception {
    	CreditReportingForm form = (CreditReportingForm) command;
		com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/* reset custom field list */
		form.setMissingCustomFields(new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>());
		
		setCustomField(vu360User, form);
		setCreditReportingField( vu360User, form ) ;

		// LMS-15942 Suggest jurisdiction 
		LearnerEnrollment le = enrollmentService.loadForUpdateLearnerEnrollment(Long.valueOf(form.getLearnerEnrollmentId()));
		
		
		//LMS-20480
		if(new Date().after(le.getEnrollmentEndDate()) ){
		    Map<Object, Object> context = new HashMap<Object, Object>();
		   	context.put("message", "Enrollment has expired");
		   	return new ModelAndView(ENROLLMENTEXPIRED_VIEW, VIEW_CONTEXT, context);
		}
		
		Brander brand = VU360Branding.getInstance().getBrander(
				(String) request.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());
		
		if(isSubscriptionCourse(le) && 
    		le.getSubscription().getSubscriptionStatus().equalsIgnoreCase(SUBSCRIPTION_STATUS_ONHOLD) &&
    		(le.getCourseStatistics().getStatus().equals(LearnerCourseStatistics.IN_COMPLETE) ||
    		le.getCourseStatistics().getStatus().equals(LearnerCourseStatistics.IN_PROGRESS) || 
    		le.getCourseStatistics().getStatus().equals(LearnerCourseStatistics.NOT_STARTED))) {
				log.debug("Inside displayLearnerProfile: isSubscriptionCourse()~~~");
			    Map<Object, Object> context = new HashMap<Object, Object>();
			    String message = "";
		    	message = brand.getBrandElement(BRAND_SUBSCRIPTION_ONHOLD);
		    	context.put("message", message);
		    	return new ModelAndView(SUBSCRIPTION_VIEW, VIEW_CONTEXT, context);
		    	
	    } 
		
		if (StringUtils.isNotEmpty(form.getJurisdictionSuggestionYN())) {
			if ("Y".equals(form.getJurisdictionSuggestionYN())){
				log.debug("1-Inside displayLearnerProfile: getJurisdictionSuggestionYN... if Y ");
				form.setJurisdictionSuggestionYN(null);
    			le.setSkippedCourseApproval(new Boolean(false));
    			enrollmentService.updateEnrollment(le);	

				return new ModelAndView(jurisdictionSuggestTemplate);
			}
			if ("N".equals(form.getJurisdictionSuggestionYN())){
				log.debug("2-Inside displayLearnerProfile: getJurisdictionSuggestionYN... if N ");
				form.setJurisdictionSuggestionYN(null);
				
				//reset any approval in NO Button pressed LMS-15996
				form.setCourseApproval(new ArrayList<CourseApprovalVO>()) ;
				form.setCourseApprovalId("0");
    			le.setSkippedCourseApproval(new Boolean(true));
    			enrollmentService.updateEnrollment(le);	

				return this.launchCourse(request, response, command);
			}
		}
		
		
		
		if (form.getCourseApproval().size() > 0){
			//return new ModelAndView(errorTemplate);// now suggest jurisdiction page should be redirect.
			if(le.getSkippedCourseApproval()!=null && le.getSkippedCourseApproval() == true)
			{
				form.setCourseApprovalId("0");
				return this.launchCourse(request, response, command);
			}
			else
				return new ModelAndView(jurisdictionSuggestTemplate);
			
		}else if (form.getMissingCustomFields().size() > 0) {
		    return new ModelAndView(errorTemplate);
		}else if (form.getMissingCreditReprotingFields().size() > 0) {
		    //return new ModelAndView(errorTemplate);
			if(le.getSkippedCourseApproval()!=null && le.getSkippedCourseApproval() == true)
				return this.launchCourse(request, response, command);
			else
				return new ModelAndView(errorTemplate);

		}else {
		    return this.launchCourse(request, response, command);
		}

    }

    /**
     * 
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return
     */
    public ModelAndView ShowAllRegulators(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){

		CreditReportingForm form = (CreditReportingForm) command;
		com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		form.setMissingCustomFields(new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>());
		setCustomField(vu360User, form);
		setCreditReportingField( vu360User, form ) ;
		Map<String, Object> context =  new HashMap<String, Object>();
		form.setShowAllRegulators("true");
	
		if (form.getCourseApproval().size() > 0){
			//this check has useless because we do not show error message page (noAddtionalCoruseApprovalFound.vm) when no addition regulator not exist.
			//this check hide bottom "Show All regulator" is not additions regulator exist.
			//LMS-16001
			if (StringUtils.isNotEmpty(form.getSuggestedCourseApprovalFound()) 
					&& "true".equals(form.getSuggestedCourseApprovalFound()) 
					&& form.getCourseApproval().size() == 1 
					) {
				
				return new ModelAndView(noAdditionCourseApprovalTemplate, VIEW_CONTEXT, context);
			}
			
			return new ModelAndView(errorTemplate,VIEW_CONTEXT, context);
		}else if (form.getMissingCustomFields().size() > 0) {
		    return new ModelAndView(errorTemplate);
		}else if (form.getMissingCreditReprotingFields().size() > 0) {
		    return new ModelAndView(errorTemplate);
		}else {
		    return this.launchCourse(request, response, command);
		}
	}
    
    public ModelAndView ShowSuggestedCA(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){

    	CreditReportingForm form = (CreditReportingForm) command;
		//VU360User vu360User = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		form.setJurisdictionSuggestionYN(null);
		return new ModelAndView(errorTemplate);

    }
    /**
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return
     * @throws Exception
     */
    public ModelAndView displayCourseCustomFields(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    	String genHTML="";
    	log.debug("onBind");
    	CreditReportingForm form = null;
    	log.debug("command : " + command.getClass());
    	
    	long courseApprovalId=0;
    	if(request.getParameter("courseApprovalId")!=null){
    		try{
    			courseApprovalId = Integer.valueOf(request.getParameter("courseApprovalId").toString());
    		}catch(Exception nfe){
    			courseApprovalId=0;
    		}
    	}
    	if (command instanceof CreditReportingForm) {
    	    form = (CreditReportingForm) command;
    	    com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	    VU360User vu360UserModel = userService.getUserById(vu360User.getId());
    	    /* reset reporting and custom field list */
    		form.setCreditReportingFields(new ArrayList<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField>());
    		//form.setMissingCustomFields(new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>());

    		form.setVu360User(vu360UserModel);
    		
    		CreditReportingFieldBuilder fieldBuilder = new CreditReportingFieldBuilder();
    		String courseId = request.getParameter("courseId");
    		Course course = null;
    		course = courseAndCourseGroupService.getCourseByGUID(courseId);
    		if (course == null) {
    		    course = courseAndCourseGroupService.getCourseById(new Long(courseId));
    		}

    		if (course == null) {
    		    log.error("course with id of:" + courseId + " was not found as either a GUID or a primary key.");
    		    // TODO: should return to a nice error page
    		}

    		List<CreditReportingField> customFieldList= new ArrayList<CreditReportingField>(accreditationService.getCreditReportingFieldsByCourseApproval(courseApprovalId));
    						
    		if (customFieldList.size()>0) {
    		    //List<CreditReportingField> customFieldList = credentialCategory.getCreditReportingFieldList();// accreditationService.getCreditReportingFieldByCourseApproval(courseApproval);
    		    List<CreditReportingFieldValue> customFieldValueList = learnerService.getCreditReportingFieldValues(vu360UserModel.getLearner());
    		    List<CreditReportingField> reportingFieldsToRender = new ArrayList<CreditReportingField>();

    		    boolean valueFound = false;
    		    for (CreditReportingField field : customFieldList) {
    		    	//System.out.println("Field:::::::"+field.getFieldLabel() +".. id="+field.getId()+"...type="+field.getFieldType());
	    			valueFound = false;
	    			//if (field.isFieldRequired()) {
	    			    for (CreditReportingFieldValue value : customFieldValueList) {
		    				if (value.getReportingCustomField().getId().longValue() == field.getId().longValue()) {
		    					valueFound = true;
		    					/*if(value.getValue()!=null)
		    						*/
		    				    //
		    				}
	    			    }
	    			    
	    			    /* check for static field values */
	    			    if("STATICCREDITREPORTINGFIELD".equals(field.getFieldType())) {
	    			    	valueFound = false;
	    			    	if (userService.getValueForStaticReportingField(vu360UserModel, field.getFieldLabel()) != null )
	    			    		valueFound = true;
	    			    }
	
	    			    if (!valueFound) {
	    				reportingFieldsToRender.add(field);
	    				// break;
	    			    }
	    			//}
	    		    }

    		    Map<Long, List<CreditReportingFieldValueChoice>> existingCreditReportingFieldValueChoiceMap = new HashMap<Long, List<CreditReportingFieldValueChoice>>();

    		    // for(CreditReportingField creditReportingField :
    		    // customFieldList){
    		    for (CreditReportingField creditReportingField : reportingFieldsToRender) {
    			if (creditReportingField instanceof SingleSelectCreditReportingField
    				|| creditReportingField instanceof MultiSelectCreditReportingField) {

    			    List<CreditReportingFieldValueChoice> creditReportingFieldValueOptionList = this.getLearnerService()
    				    .getChoicesByCreditReportingField(creditReportingField);
    			    fieldBuilder
    				    .buildCreditReportingField(creditReportingField, 0, customFieldValueList, creditReportingFieldValueOptionList);

    			    if (creditReportingField instanceof MultiSelectCreditReportingField) {
    				CreditReportingFieldValue creditReportingFieldValue = this.getCreditReportingFieldValueByCreditReportingField(
    					creditReportingField, customFieldValueList);
    				existingCreditReportingFieldValueChoiceMap.put(creditReportingField.getId(),
    					creditReportingFieldValue.getCreditReportingValueChoices());
    			    }

    			} else {
    			    fieldBuilder.buildCreditReportingField(creditReportingField, 0, customFieldValueList);
    			}
    		    }

    		    List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields = fieldBuilder
    			    .getCreditReportingFieldList();

    		    for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : creditReportingFields) {
    			if (field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField) {
    			    List<CreditReportingFieldValueChoice> existingChoices = existingCreditReportingFieldValueChoiceMap.get(field
    				    .getCreditReportingFieldRef().getId());
    			    Map<Long, CreditReportingFieldValueChoice> existingChoicesMap = new HashMap<Long, CreditReportingFieldValueChoice>();

    			    for (CreditReportingFieldValueChoice choice : existingChoices) {
    				existingChoicesMap.put(choice.getId(), choice);
    			    }

    			    for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice tempChoice : field
    				    .getCreditReportingFieldValueChoices()) {
    				if (existingChoicesMap.containsKey(tempChoice.getCreditReportingFieldValueChoiceRef().getId())) {
    				    tempChoice.setSelected(true);
    				}
    			    }
    			}
    		    }
    		    
    		    form.setCreditReportingFields(creditReportingFields);
    		    Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new com.softech.vu360.lms.vo.Language());
    		    
    		    for(int count=0;count<form.getCreditReportingFields().size();count++){
    		    	com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField cr = form.getCreditReportingFields().get(count);
    		    	genHTML = genHTML + cr.renderCustomField("customField"+count,
 							 "creditReportingFields["+count+"].creditReportingFieldValueRef",
 							 "creditReportingFields["+count+"].creditReportingFieldValueRef.id",
 							 "creditReportingFields["+count+"]", brand);
    		    }
    		  
    		    
    		    customFieldService.createValueRecordForStaticReportingField(vu360UserModel, customFieldList, customFieldValueList);
    		}

    		
    		//setCustomField(vu360User, form);
    	    

    	    log.debug("onBind middle");

    	    String learnerEnrollmentID = request.getParameter("learnerEnrollmentId");
    	    log.debug("onBind request learnerEnrollmentID = " + learnerEnrollmentID);
    	    
    	    log.debug("onBind request courseId = " + courseId);
    	    String source = request.getParameter("source");
    	    String externalLMSSessionID = request.getParameter("externallmssessionid");
    	    String externalLMSUrl = request.getParameter("externallmsurl");
    	    String lmsProviderStr = request.getParameter("lmsprovider");
    	    int lmsProvider = LearningSession.LMS_LS360;
			Brander brand = VU360Branding.getInstance().getBrander(
					(String) request.getSession().getAttribute(VU360Branding.BRAND),
					new com.softech.vu360.lms.vo.Language());

    	    try {
    		if (lmsProviderStr != null && !lmsProviderStr.trim().isEmpty()) {
    		    lmsProvider = Integer.valueOf(lmsProviderStr).intValue();
    		} else {
    		    lmsProvider = LearningSession.LMS_LS360;
    		}
    	    } catch (Exception e) {
    		lmsProvider = LearningSession.LMS_LS360;
    	    }

    	    if (source == null || source.trim().isEmpty()) {
    		source = LearningSession.SOURCE_VU360_LMS;
    		externalLMSSessionID = null;
    		externalLMSUrl = null;
    		lmsProvider = LearningSession.LMS_LS360;
    	    }

    	    if (form != null) {

    		/*Course course = null;
    		course = courseAndCourseGroupService.getCourseByGUID(courseId);
    		if (course == null) {
    		    course = courseAndCourseGroupService.getCourseById(new Long(courseId));
    		}*/
    		if (course instanceof HomeworkAssignmentCourse) {
    		    form.setFileName(((HomeworkAssignmentCourse) course).getHwAssignmentInstruction().getFileName());
    		    Date date = ((HomeworkAssignmentCourse) course).getAssignmentDueDate();
    		    if (date != null) {
    			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    			form.setAssignmentDueDate(formatter.format(date));
    		    }

    		}

    		if (course instanceof InstructorConnectCourse) {
    		    InstructorConnectCourse connectCourse = (InstructorConnectCourse) course;
    		    form.setLearnerEmailAddress(connectCourse.getEmailAddress());
    		    String instructorEmailAddresses = "";
    		    log.debug("connectCourse : " + connectCourse.getId());
    		    List<InstructorCourse> instructorCourses = connectCourse.getInstructorCourses();
    		    log.debug("instructorCourses : " + instructorCourses.size());
    		    Iterator<InstructorCourse> iterator = instructorCourses.iterator();
    		    while (iterator.hasNext()) {
    			InstructorCourse instructorCourse = iterator.next();
    			if (StringUtils.isBlank(instructorEmailAddresses)) {
    			    instructorEmailAddresses = instructorCourse.getInstructor().getUser().getEmailAddress();
    			} else {
    			    instructorEmailAddresses += "," + instructorCourse.getInstructor().getUser().getEmailAddress();
    			}

    		    }

    		    form.setInstructorEmailAddresses(instructorEmailAddresses);

    		}

    		form.setNoOfHomeworkAssignmentuploaded(brand.getBrandElement("lms.hwassignmentUpload.noOfFiles"));
    		form.setCourseId(courseId);
    		log.debug("onBind form.setCourseId(courseId) = " + form.getCourseId());
    		form.setLearnerEnrollmentId(learnerEnrollmentID);
    		log.debug("onBind form.setLearnerEnrollmentId(learnerEnrollmentID) = " + form.getLearnerEnrollmentId());
    		form.setSource(source);
    		form.setexternallmssessionid(externalLMSSessionID);
    		form.setexternallmsurl(externalLMSUrl);
    		form.setLmsProvider(lmsProvider);
    	    }
    	}
    	
    	
    		/* Creating JSONArray and put JSONObject into it.
    		 * We will return null because require data is set into response Header.
    		 */
	    	JSONArray jsonArray = new JSONArray();
		    JSONObject scheduleOverlayJSON = new JSONObject();
		    JSONObject CourseApprovalDetailsJSON = new JSONObject();
		    genHTML = genHTML.replace("customField", "creditReportingField");
		    genHTML = genHTML.replace("25%", "20% ");
		    CourseApprovalDetailsJSON.put("field", genHTML);
			jsonArray.add(CourseApprovalDetailsJSON);
			
			scheduleOverlayJSON.put("courseApprovalUsingAjaxCall", jsonArray);
			PrintWriter pw = response.getWriter();
			response.setHeader("Content-Type", "application/json");
			
			pw.write(scheduleOverlayJSON.toString());
			pw.flush();
			pw.close();
			
	    	return null;
    	
		
	}
    
    
    void setCustomField(com.softech.vu360.lms.vo.VU360User vu360User, CreditReportingForm form){
    	// For Mandatory Custom Fields
		List<CustomField> customFields = getCustomFieldsForLearner(vu360User);
		List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
		;// customer.getCustomFields();
		List<CustomField> missingCustomFields = new ArrayList<CustomField>();
		CustomFieldBuilder customFieldBuilder = new CustomFieldBuilder();

		//customFieldValues = vu360User.getLearner().getLearnerProfile().getCustomFieldValues();
		Learner learner = learnerService.getLearnerByID(vu360User.getLearner().getId());
		customFieldValues = learner.getLearnerProfile().getCustomFieldValues();

		boolean valueFound = false;
		for (CustomField field : customFields) {
		    valueFound = false;
		    if (field.getFieldRequired()) {
			for (CustomFieldValue value : customFieldValues) {
			    if (field.equals(value.getCustomField())) {
				valueFound = true;
				break;
			    }
			}
			if (!valueFound) {
			    missingCustomFields.add(field);
			}
		    }
		}

		if (customFieldValues!=null && customFieldValues.size() == 0 && form.getCustomFieldValueList().size() > 0) {
		    customFieldValues.addAll(form.getCustomFieldValueList());
		}

		Map<Long, List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long, List<CustomFieldValueChoice>>();

		for (CustomField customField : missingCustomFields) {
		    if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField) {
			List<CustomFieldValueChoice> customFieldValueChoices = this.getCustomFieldService().getOptionsByCustomField(customField);
			customFieldBuilder.buildCustomField(customField, 0, customFieldValues, customFieldValueChoices);

			if (customField instanceof MultiSelectCustomField) {
			    CustomFieldValue customFieldValue = this.getCustomFieldValueByCustomField(customField, customFieldValues);
			    existingCustomFieldValueChoiceMap.put(customField.getId(), customFieldValue.getValueItems());
			}

		    } else {
			customFieldBuilder.buildCustomField(customField, 0, customFieldValues);
		    }
		}

		List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> missingCustomFieldList2 = customFieldBuilder
			.getCustomFieldList();

		for (com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : missingCustomFieldList2) {
		    if (customField.getCustomFieldRef() instanceof MultiSelectCustomField) {
			List<CustomFieldValueChoice> existingCustomFieldValueChoiceList = existingCustomFieldValueChoiceMap.get(customField
				.getCustomFieldRef().getId());
			Map<Long, CustomFieldValueChoice> tempChoiceMap = new HashMap<Long, CustomFieldValueChoice>();

			for (CustomFieldValueChoice customFieldValueChoice : existingCustomFieldValueChoiceList) {
			    tempChoiceMap.put(customFieldValueChoice.getId(), customFieldValueChoice);
			}

			for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField
				.getCustomFieldValueChoices()) {
			    if (tempChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())) {
				customFieldValueChoice.setSelected(true);
			    }
			}
		    }
		}

		form.setMissingCustomFields(missingCustomFieldList2);
    }
   
    public ModelAndView launchCourse(HttpServletRequest request, HttpServletResponse response, Object command) {

	try {
	    com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String learnerEnrollmentID = request.getParameter("learnerEnrollmentId");
	    String courseId = request.getParameter("courseId");
	    String source = request.getParameter("source");
	    String externalLMSSessionID = request.getParameter("externallmssessionid");
	    String externalLMSUrl = request.getParameter("externallmsurl");
	    String lmsProviderStr = request.getParameter("lmsprovider");
	    //String strenrollId = request.getParameter("enrollmentId");
	    int lmsProvider = LearningSession.LMS_LS360;
	    String learningSessionId = null;
	    //Brander brand = (Brander) request.getSession().getAttribute(VU360Branding.BRAND);
	    Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new com.softech.vu360.lms.vo.Language());
	    String learnerAssignmentStatus = null;
	    
	    response.setHeader("cache-control", "no-cache, no-store, must-revalidate");
		response.setHeader("pragma", "no-cache");
		response.setHeader("expires", "0");
		response.setHeader("Last-Modified", (new Date()).toString());
		response.setDateHeader("Expires", 0); 

		log.info("Request.Header" + request.getHeader("Host"));
		log.info("Propperty SCORM" + VU360Properties.getVU360Property("lms.SCORM"));
		log.info("Propperty DOMAIN" + VU360Properties.getVU360Property("lms.domain"));
		
		//log.info("string EnrollmentId" + request.getParameter("enrollmentId"));
	    try {
			if (lmsProviderStr != null && !lmsProviderStr.trim().isEmpty()) {
			    lmsProvider = Integer.valueOf(lmsProviderStr).intValue();
			} else {
			    lmsProvider = LearningSession.LMS_LS360;
			}
	    } catch (Exception e) {
	    	lmsProvider = LearningSession.LMS_LS360;
	    }

	    if (source == null || source.trim().isEmpty()) {
			source = LearningSession.SOURCE_VU360_LMS;
			externalLMSSessionID = null;
			externalLMSUrl = null;
			lmsProvider = LearningSession.LMS_LS360;
	    }

	    // if(request.getParameter("action")!=null &&
	    // request.getParameter("action").equals("saveReportingFields")){
	    // saveCreditReportingFields(request, response);
	    // }

	    // //for Credit Reporting Reqd. Fields
	    // Map<Object, Object> reportingContext =
	    // renderRequiredReportingFieldsUpdated(user, courseId);
	    // if( reportingContext!=null ) {
	    // //Redner the reporting fields and get the input
	    //
	    //
	    // reportingContext.put("reportingFieldError",
	    // "lms.customField.error.description");
	    // reportingContext.put("learnerEnrollmentId", learnerEnrollmentID);
	    // reportingContext.put("courseId", courseId);
	    // reportingContext.put("source", source);
	    // reportingContext.put("externallmssessionid",externalLMSSessionID);
	    // reportingContext.put("externallmsurl",externalLMSUrl);
	    // reportingContext.put("reportingFieldError",
	    // "lms.customField.error.description");
	    // return new
	    // ModelAndView(errorTemplate,"context",reportingContext);
	    // }

	    /*
	     * if( !isRequiredFieldsUpdated(user) ) { //send it to a nice UI
	     * with branded message (Please Update your profile before launching
	     * course)
	     * 
	     * Map<Object, Object> context = new HashMap<Object, Object>();
	     * context.put("customFieldError",
	     * "lms.customField.error.description"); return new
	     * ModelAndView(errorTemplate,"context",context); }
	     */
	    // now call the launch course service
	    // TODO: update to not hard code the language.
	    // Language lang = user.getLanguage();//user should have language
	    // option to define
//	    Language lang = new Language();
//	    lang.setId(new Long(1));
//	    lang.setCountry("US");
//	    lang.setVariant(null);
//	    lang.setLanguage("en");
	    Course course = null;
	    Language lang = null;
	    LearnerEnrollment learnerEnrollment = null;
	    if (learnerEnrollmentID != null) {
			learnerEnrollment = entitlementService.getLearnerEnrollmentById(Long.valueOf(learnerEnrollmentID));
			course = learnerEnrollment.getCourse();
			
						LearnerCourseStatistics learnerCoursestatistics = statsService.getLearnerCourseStatisticsByLearnerEnrollmentId(learnerEnrollment.getId());
			 
			 			if(learnerCoursestatistics != null){
			 				if(learnerEnrollment.getCourseStatistics().getStatus().equals(LearnerCourseStatistics.NOT_STARTED)){
			 					enrollmentService.marketoPacket(learnerEnrollment, COURSE_STARTED);
			 	}
			 }
			
			lang =  course.getLanguage();
			com.softech.vu360.lms.vo.Learner learner = user.getLearner();
			
			log.debug("LearnerEnrollment Not Null and Course Type: "+course.getCourseType());
			
			CreditReportingForm form = (CreditReportingForm) command;
			int varCourseApprovalId=0;
			
			try{
				if(form.getCourseApprovalId()!=null)
					varCourseApprovalId= Integer.valueOf(form.getCourseApprovalId().toString());
			}catch(Exception subE){	
				log.error("Error in LaunchCourseController. Because of getting Course Approval Id for save data into learningSession");
			}
			
			if( launchCourseService.isValidOSHACourseJurisdication(learnerEnrollment.getCourse().getId()))
			{
                   
				  Map<Object, Object> geoLocationcontext = new HashMap<Object, Object>();
				  SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
			      Date now = new Date();
			      String strLaunchDate = sdfDate.format(now);
			      
				  String[] bcc = new String[1];
				  String[] to = new String[1];
				  
				  log.info("Learner User Name: " + learnerEnrollment.getLearner().getVu360User().getUsername());
				  
				  if(!isValidOSHARequest(request)){
	                    String invalidLocationsender = VU360Properties.getVU360Property("lms.secureIp.email.sender");
						String invalidLocationsubject = VU360Properties.getVU360Property("lms.secureIp.email.subject");
						String invalidLocationbcc = VU360Properties.getVU360Property("lms.secureIp.email.bcc");
						String invalidLocationemailmessage = brand.getBrandElement("lms.secureIp.email.message");
						String invalidLocationpopupmessage = brand.getBrandElement("lms.secureIp.popup.message");
						geoLocationcontext.put("message", invalidLocationpopupmessage);
						geoLocationcontext.put("geolocation", 0);
						bcc[0]=invalidLocationbcc;
						to[0]=learnerEnrollment.getLearner().getVu360User().getEmailAddress();
						learnersToBeMailedService.SendMailToLearnersForLaunchingInvalidIp(learnerEnrollment.getLearner(), to, bcc, invalidLocationsender, invalidLocationsubject, invalidLocationemailmessage, brand, strLaunchDate);
						return new ModelAndView(INVALIDLOCATION_ACCESS_VIEW, "locationContext", geoLocationcontext); 
                   }
			}
			
			/*if(learner.getId().longValue() != learnerEnrollment.getLearner().getId().longValue())
			{
				Map<Object, Object> context = new HashMap<Object, Object>();
				String message = brand.getBrandElement(BRAND_SESSION_EXPIRED);
				context.put("message", message);
				context.put("sesstiontimout", 1);
				
				return new ModelAndView(RECENT_VIEW, VIEW_CONTEXT, context); 
				
			}*/
			log.debug("B4 launchCourseService.launchCourse()"); 
			Learner learnerModel = new Learner();
			learnerModel.setVu360User(new VU360User());
			learnerModel.getVu360User().setUserGUID(user.getUserGUID());
			learningSessionId = launchCourseService.launchCourse(learnerModel, learnerEnrollment, brand.getName(), lang, source, externalLMSSessionID,
				externalLMSUrl, lmsProvider, varCourseApprovalId);
			log.debug("After launchCourseService.launchCourse()");
			
			if(learningSessionId!=null){
				statsService.launchedCourse(learnerEnrollment);
			}
			
			//--Start- resetting of course Approval and IdIs after save into learningSession
			form.setCourseApproval(new ArrayList<CourseApprovalVO>()) ;
			form.setCourseApprovalId("0");
			//- END -----------------------------------------------------------------
			
	    } else if (courseId != null) {

			Course courseTmp = courseAndCourseGroupService.getCourseByGUID(courseId);
			if (courseTmp == null) {
			    courseTmp = courseAndCourseGroupService.getCourseById(new Long(courseId));
			    course.setCourseId(courseTmp.getCourseGUID());
			    course.setLanguage(courseTmp.getLanguage());
			} else {
			    course.setCourseId(courseTmp.getCourseGUID());
			    course.setLanguage(courseTmp.getLanguage());
			}
	
			log.debug("LearnerEnrollment Not Null and Course Type: "+course.getCourseType());
		
		learningSessionId = launchCourseService.launchCourse(user.getUserGUID(), course.getCourseId(), null, course.getLanguage(), source,
			externalLMSSessionID, externalLMSUrl, lmsProvider);
		
		if(learningSessionId!=null){
			statsService.launchedCourse(learnerEnrollment);
		}
	    }

	    if(isSubscriptionCourse(learnerEnrollment) && 
	    		learnerEnrollment.getSubscription().getSubscriptionStatus().equalsIgnoreCase(SUBSCRIPTION_STATUS_ONHOLD) &&
	    		(learnerEnrollment.getCourseStatistics().getStatus().equals(LearnerCourseStatistics.IN_COMPLETE) ||
	    		learnerEnrollment.getCourseStatistics().getStatus().equals(LearnerCourseStatistics.IN_PROGRESS) || 
	    		learnerEnrollment.getCourseStatistics().getStatus().equals(LearnerCourseStatistics.NOT_STARTED))) {
				    Map<Object, Object> context = new HashMap<Object, Object>();
				    String message = "";
			    	message = brand.getBrandElement(BRAND_SUBSCRIPTION_ONHOLD);
			    	context.put("message", message);
			    	log.debug("isSubscriptionCourse");
			    	return new ModelAndView(SUBSCRIPTION_VIEW, VIEW_CONTEXT, context);
		    } 
	    
	    
	    StringBuffer launchURL = new StringBuffer();
	    // now every launch course will be forwarded to LCMS
	    // TODO: update LCMS to use the following logic for launching SCORM
	    // Courses:
	    if (course instanceof ScormCourse) {
			List<SCO> scos = ((ScormCourse) course).getScos();
			if (scos == null || scos.isEmpty()) {
			    // return error
			    log.error("no scos to launch");
			    throw new RuntimeException("SCORM course:" + course.getCourseGUID() + " is mis configured, no SCOS");
			}
			//launchURL.append(VU360Properties.getVU360Property("lms.domain"));
	
			log.info("Request.Header SORM " + request.getHeader("Host"));
			log.info("Propperty SCORM " + VU360Properties.getVU360Property("lms.SCORM"));
			log.info("Propperty DOMAIN " + VU360Properties.getVU360Property("lms.domain"));
			
			
			//TODO temperary fix for new nodes setting applied
//			if (request.getHeader("Host").equalsIgnoreCase(VU360Properties.getVU360Property("lms.SCORM")))
//				launchURL.append(VU360Properties.getVU360Property("lms.domain"));		
//			else
//				launchURL.append("http://"+ request.getHeader("Host"));
			
			launchURL.append(VU360Properties.getVU360Property("lms.domain"));	
			
			if (scos.size() == 1) {
			    launchURL.append(scos.get(0).getLaunchURI());
			    //launchURL.append("?session=");
			} else {
			    launchURL.append(scos.get(0).getLaunchURI());
			    //launchURL.append("?session=");
			}
			
			if (launchURL.indexOf("?") == -1) {
				launchURL.append("?session=");
			} else {
				launchURL.append("&session=");
			}
			
			launchURL.append(learningSessionId);
			launchURL.append("&ts=");
			launchURL.append(System.currentTimeMillis());
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			log.debug("SCORM: launchURL: "+launchURL.toString());
			 context.put("url", launchURL.toString());
			 context.put("courseName", course.getName());
			 context.put("courseDesc", course.getDescription());
			 
			 request.getSession().setAttribute("learningSessionId", learningSessionId);
	 
			 Brander brander = VU360Branding.getInstance().getBrander((String) request.getSession().getAttribute(VU360Branding.BRAND), new com.softech.vu360.lms.vo.Language());			 
             String enabledDistributorCodes = brander.getBrandElement("lms.newscormShell.enabledDistributorCodes");
             String currentDistCode = learnerEnrollment.getLearner().getCustomer().getDistributor().getDistributorCode();
  
             String view = "learner/learnerScormShellNormal";

             String[] IDs = enabledDistributorCodes.split(",");
             for (String str : IDs) {
                 if(str.equals(currentDistCode))
                 {
                     view = "learner/learnerScormShell";
                     break;
                 }           
             }
             
			return new ModelAndView(view, VIEW_CONTEXT, context);

	    } else if (course instanceof SynchronousCourse || course instanceof WebinarCourse ) {
		    	
			// call meeting service factory and get Desired Meeting Service
			// Object
			// call getJoinMeetingURL method against SynchronousClass
			// redirectUser to joinMeeting URL
			SynchronousClass synchClass = learnerEnrollment.getSynchronousClass();
			
			if(synchClass!=null && synchClass.getMeetingID()==null){
				//classroom location information.
				//classroom schedule information.
				
				//TimeZone timeZoneModel = learnerService.getTimeZoneByLearnerProfileId(user.getLearner().getLearnerProfile().getId());
				TimeZone timeZoneModel = new TimeZone();
				if(user.getLearner().getLearnerProfile().getTimeZone()!=null){
					timeZoneModel.setCode(user.getLearner().getLearnerProfile().getTimeZone().getCode());
					timeZoneModel.setHours(user.getLearner().getLearnerProfile().getTimeZone().getHours());
					timeZoneModel.setZone(user.getLearner().getLearnerProfile().getTimeZone().getZone());
					timeZoneModel.setMinutes(user.getLearner().getLearnerProfile().getTimeZone().getMinutes());
					timeZoneModel.setId(user.getLearner().getLearnerProfile().getTimeZone().getId());
				}
				
				VU360User vu360UserModel = new VU360User();
				vu360UserModel.setFirstName(user.getFirstName());
				vu360UserModel.setLastName(user.getLastName());
				vu360UserModel.setLearner(new Learner());
				vu360UserModel.getLearner().setLearnerProfile(new LearnerProfile());
				vu360UserModel.getLearner().getLearnerProfile().setTimeZone(timeZoneModel);
				
				Map<Object, Object> context = enrollmentService.displayViewSchedule(vu360UserModel, brand, course.getId().toString(), learnerEnrollmentID);
				
	    		return new ModelAndView(syncClassRoomErrorTemplate, VIEW_CONTEXT, context);
	    	}
			
			if(synchClass.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_OTHERS)) {
				launchURL.append(synchClass.getMeetingURL());
			}
			else if(synchClass.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_WEBINAR)){
				//Get Attendee meeting URL
				log.debug("Inside condition (synchClass.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_WEBINAR))");
				VU360User vu360UserModel = new VU360User();
				vu360UserModel.setFirstName(user.getFirstName());
				vu360UserModel.setLastName(user.getLastName());
				vu360UserModel.setEmailAddress(user.getEmailAddress());
				String meetingURL = makeMeetingURL(vu360UserModel,brand,synchClass);
				log.debug("launchCourse > meetingURL: "+meetingURL);
				launchURL.append(meetingURL);
				log.debug("launchCourse > meetingURL > launchURL: "+launchURL);
				
			}
			else	{
				String meetingID = null;
				if (synchClass.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_DIMDIM))
				    meetingID = synchClass.getGuid(); // in case of dimdim
				// meeting id is host
				// account id which is
				// class guid in dimdim
				else
				    meetingID = synchClass.getMeetingID();
				String meetingPassCode = synchClass.getMeetingPassCode();
				SynchronousMeetingService meetingService = meetingServiceFactory.getInstance(synchClass.getMeetingType());
				String joinMeetingURL = meetingService.getJoinMeetingURL(brand, meetingID, learnerEnrollment.getLearner().getVu360User()
					.getFirstName()
					+ " " + learnerEnrollment.getLearner().getVu360User().getLastName(), learnerEnrollment.getLearner().getVu360User()
					.getEmailAddress(), meetingPassCode);
				log.debug("meetingURL:" + joinMeetingURL);
				launchURL.append(joinMeetingURL);
			}
		} else if (course instanceof DiscussionForumCourse) {
			
			VU360User vu360UserModel = new VU360User();
			vu360UserModel.setUsername(user.getUsername());
			vu360UserModel.setUserGUID(user.getUserGUID());
			vu360UserModel.setPassword(user.getPassword());
			vu360UserModel.setEmailAddress(user.getEmailAddress());

			Learner learnerModel = new Learner();
			learnerModel.setVu360User(vu360UserModel);
			
			launchURL.append(this.learnerService.registerLearnerToVCS((DiscussionForumCourse) course, learnerModel));
	    } else if (course instanceof HomeworkAssignmentCourse) {
			// Register user to phpBB Forum and redirect to returned URL
			HomeworkAssignmentCourse hwAssign = (HomeworkAssignmentCourse) course;
			
			 Map<Object, Object> context = new HashMap<Object, Object>();
			 
			 HomeworkAssignmentCourse hw = ((HomeworkAssignmentCourse) course);
		     LearnerHomeworkAssignmentSubmission learnerhomeworksubmission =  learnerHomeworkAssignmentSubmissionService.getLearnerHomeworkAssignmentSubmission(learnerEnrollment);
			
		     
		     if(learnerhomeworksubmission != null)
		    	{
				     if(hwAssign.getGradingMethod().equals("Scored"))
				     {
				    	 double courseMasterScore;
				    	 int learnerScore;
				    	 
				    	 if(hw.getGradingMethod() != null && learnerhomeworksubmission.getPercentScore() != null)
				    		{
				    		  courseMasterScore = hw.getMasteryScore();
				    		  if(!learnerhomeworksubmission.getPercentScore().equals("Incomplete"))
				    		    {
						    		learnerScore = Integer.parseInt(learnerhomeworksubmission.getPercentScore());
						    		if(courseMasterScore == learnerScore || learnerScore > courseMasterScore)
						    		{
						    			learnerAssignmentStatus = "Pass";
						    		}
						    		
					    		}
				    		}
				     }
				     else if(hwAssign.getGradingMethod().equals("Simple"))
				     {
				    	 if(learnerhomeworksubmission.getPercentScore() != null)
					    	 {
					    	 if(learnerhomeworksubmission.getPercentScore().equals("Pass"))
							    {
								  learnerAssignmentStatus = "Pass";
								}
					    	 }
				     }
				     
			 
				  
				}
				
				
			/*
			 * context.put("learnerEnrollmentId", learnerEnrollmentID);
			 * context.put("courseId", courseId); 
			 */
			/*
			 * return new ModelAndView(learnerHomeworkAssignment, "context",
			 * context);
			 */
			 context.put("homeworkAssignment", learnerAssignmentStatus);
			 context.put("courseTitle", hwAssign.getCourseTitle());
			 return new ModelAndView(learnerHomeworkAssignment, VIEW_CONTEXT, context);
	    } else if (course instanceof InstructorConnectCourse) {
			// Register user to phpBB Forum and redirect to returned URL
			InstructorConnectCourse instructorConnectCourse = (InstructorConnectCourse) course;
			/*
			 * Map<Object, Object> context = new HashMap<Object, Object>();
			 * context.put("learnerEnrollmentId", learnerEnrollmentID);
			 * context.put("courseId", courseId); context.put("fileName",
			 * hwAssign.getHwAssignmentInstruction() .getFileName());
			 */
			/*
			 * return new ModelAndView(learnerHomeworkAssignment, "context",
			 * context);
			 */
	
			if (instructorConnectCourse.getInstructorType().equals("Webinar")) {
	
				SynchronousClass synchClass = learnerEnrollment.getSynchronousClass();
				VU360User vu360UserModel = new VU360User();
				vu360UserModel.setFirstName(user.getFirstName());
				vu360UserModel.setLastName(user.getLastName());
				vu360UserModel.setEmailAddress(user.getEmailAddress());
				String meetingURL = makeMeetingURL(vu360UserModel,brand,synchClass);
				launchURL.append(meetingURL);
				
			}
			else {
				return new ModelAndView(learnerInstructorConnect);
			}
				/*
				if(synchClass.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_WEBINAR)){
					//Get Attendee meeting URL
					String meetingURL = makeMeetingURL(user,brand,synchClass);
					launchURL.append(meetingURL);
				}
				else	
				{
					String meetingID = null;
					if (synchClass.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_DIMDIM))
					    meetingID = synchClass.getGuid(); // in case of dimdim
					// meeting id is host
					// account id which is
					// class guid in dimdim
					else
					    meetingID = synchClass.getMeetingID();
					String meetingPassCode = synchClass.getMeetingPassCode();
					SynchronousMeetingService meetingService = meetingServiceFactory.getInstance(synchClass.getMeetingType());
					String joinMeetingURL = meetingService.getJoinMeetingURL(brand, meetingID, learnerEnrollment.getLearner().getVu360User()
						.getFirstName()
						+ " " + learnerEnrollment.getLearner().getVu360User().getLastName(), learnerEnrollment.getLearner().getVu360User()
						.getEmailAddress(), meetingPassCode);
					log.debug("meetingURL:" + joinMeetingURL);
					launchURL.append(joinMeetingURL);
				}
				*/
				// SynchronousClass synchClass =
			    // learnerEnrollment.getSynchronousClass();
			    //String meetingID = null;
			    // if
			    // (synchClass.getMeetingType().equalsIgnoreCase(SynchronousClass.MEETINGTYPE_DIMDIM))
			    // meetingID = synchClass.getGuid(); // in case of dimdim
			    // meeting id is host
			    // account id which is
			    // class guid in dimdim
			    // else
			    
			    
			    /*
			    meetingID = instructorConnectCourse.getMeetingId();
			    String meetingPassCode = instructorConnectCourse.getMeetingPasscode();
			    SynchronousMeetingService meetingService = meetingServiceFactory.getInstance(SynchronousClass.MEETINGTYPE_WEBEX);
			    String joinMeetingURL = meetingService.getJoinMeetingURL(brand, meetingID, learnerEnrollment.getLearner().getVu360User()
				    .getFirstName()
				    + " " + learnerEnrollment.getLearner().getVu360User().getLastName(), learnerEnrollment.getLearner().getVu360User()
				    .getEmailAddress(), meetingPassCode);
			    log.debug("meetingURL:" + joinMeetingURL);
			    launchURL.append(joinMeetingURL);
			    */
	
		} else if(course instanceof AICCCourse) {
	    	log.debug("AICC Course condition working fine...");
	    	/**
			 * Modified By Marium Saud
			 * Removed mapping for AICCAssignableUnit from Course.java
			 * Loading AICCAssignableUnit using findOne
			 */
	    	AICCAssignableUnit courseAssignableUnit = aiccAssignableUnitService.findAICCAssignableUnitByCourseId(course.getId());//course.getAICCAssignableUnit();
	    	if(courseAssignableUnit != null) {
	    		com.softech.vu360.lms.vo.Customer customer = user.getLearner().getCustomer();
		    	log.info("customer code:" + customer.getCustomerCode());
		    	//response.setContentType("application/x-www-form-urlencoded");
		    	launchURL.append(courseAssignableUnit.getFileName() + "?" + courseAssignableUnit.getWebLaunch());
		    	launchURL.append("&AICC_SID=" + URLEncoder.encode(learningSessionId, "UTF-8"));
		    	launchURL.append("&AICC_URL=" + URLEncoder.encode(VU360Properties.getVU360Property("aicc.call.back.url") + "aiccCourse.do", "UTF-8"));
		    	log.info("Launching URL:" + launchURL.toString());
	    	}
	    	
	    } else {
	    	/**
			 * Modified By Marium Saud
			 * Removed mapping for CoursePlayerType from Course.java
			 * Loading CoursePlayerType using findOne
			 */
	    	CoursePlayerType playerType = coursePlayerTypeService.findCoursePlayerTypeByCourseId(course.getId());  //course.getCoursePlayerType()

    		if(learnerEnrollment.isLaunchInN3()) {
    			launchURL.append(VU360Properties.getVU360Property("lcms.newLaunchURL"));
	    		launchURL.append(learningSessionId);
	    		log.debug("launchURL:" + launchURL.toString());
    		}
    		else if(playerType!=null && playerType.getPlayerVersion().equals("1.0")
	    			&& course.getId() == learnerEnrollment.getCourse().getId()
	    			&& !learnerEnrollment.isLaunchInN3() &&	
	    			learnerEnrollment.getCourseStatistics().getStatus().equals(LearnerCourseStatistics.NOT_STARTED)) {		
    
	    			launchURL.append(VU360Properties.getVU360Property("lcms.newLaunchURL"));
	    			launchURL.append(learningSessionId);
	    			log.debug("launchURL:" + launchURL.toString());
	    			LearnerEnrollment le = enrollmentService.loadForUpdateLearnerEnrollment(learnerEnrollment.getId());
	    			le.setLaunchInN3(true);
	    			enrollmentService.updateEnrollment(le);
	    	} 
    		else {
    			String brandLaunchURL = brand.getBrandElement("lcms.launchURL");
    			if (StringUtils.isBlank(brandLaunchURL)) {
    			   // use the system default
    			   launchURL.append(VU360Properties.getVU360Property("lcms.launchURL"));
    		    } else {
    			  // use the one from the brand properties
    			  launchURL.append(brandLaunchURL);
    			}
    			launchURL.append(learningSessionId);
    			launchURL.append("&ts=");
    			launchURL.append(System.currentTimeMillis());
    			log.debug("launchURL:" + launchURL.toString());
			}
		}
	  
	    // leave this for all requests so that we may do server-side
	    // validation
	    request.getSession().setAttribute("learningSessionId", learningSessionId);
	    // send redirect to user's browser
	    
		log.debug("Redirect: launchURL: "+launchURL.toString());

	    //response.sendRedirect(launchURL.toString());
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("redirectURL", launchURL.toString());
		return new ModelAndView("learner/redirectLaunchCourse", VIEW_CONTEXT, context);

	} catch (Exception e) {
	    log.error("exception", e);
	}
	return null;
    }

	/**
	 * @param learnerEnrollment
	 */
	private boolean isSubscriptionCourse(LearnerEnrollment learnerEnrollment) {
		if(learnerEnrollment.getSubscription() != null && 
	    		learnerEnrollment.getSubscription().getSubscriptionStatus() != null) {
		    return true;
	    }
	    return false;
	}
	
    /**
     * 
     */
    private String makeMeetingURL(VU360User user, Brander brand, SynchronousClass synchClass) {
		String meetingURL = null;
		
		StringBuilder myWebinarMeetingURL = new StringBuilder(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL"));
		myWebinarMeetingURL.append("&" + VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.attendee.parameters.mt_number") + "=" + synchClass.getMeetingID());
		myWebinarMeetingURL.append("&" + VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.attendee.parameters.first_name") + "=" + user.getFirstName());
		myWebinarMeetingURL.append("&" + VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.attendee.parameters.last_name") + "=" + user.getLastName());
		myWebinarMeetingURL.append("&" + VU360Properties.getVU360Property("lms.instructor.mywebinarplace.meetingURL.attendee.parameters.email") + "=" + user.getEmailAddress());
		
		meetingURL = myWebinarMeetingURL.toString();
		
		return meetingURL;
	}

	private CreditReportingFieldValue getCreditReportingFieldValueByCreditReportingField(
	    com.softech.vu360.lms.model.CreditReportingField creditReportingField, List<CreditReportingFieldValue> creditReportingFieldValues) {
	if (creditReportingFieldValues != null) {
	    for (CreditReportingFieldValue creditReportingFieldValue : creditReportingFieldValues) {
		if (creditReportingFieldValue.getReportingCustomField() != null) {
		    if (creditReportingFieldValue.getReportingCustomField().getId().compareTo(creditReportingField.getId()) == 0) {
			return creditReportingFieldValue;
		    }
		}
	    }
	}
	return new CreditReportingFieldValue();
    }

    /**
     * @return the launchTemplate
     */
    public String getLaunchTemplate() {
	return launchTemplate;
    }

    /**
     * @param launchTemplate
     *            the launchTemplate to set
     */
    public void setLaunchTemplate(String launchTemplate) {
	this.launchTemplate = launchTemplate;
    }

    /**
     * @return the entitlementService
     */
    public EntitlementService getEntitlementService() {
	return entitlementService;
    }

    /**
     * @param entitlementService
     *            the entitlementService to set
     */
    public void setEntitlementService(EntitlementService entitlementService) {
	this.entitlementService = entitlementService;
    }

    /**
     * @return the launchCourseService
     */
    public LaunchCourseService getLaunchCourseService() {
	return launchCourseService;
    }

    /**
     * @param launchCourseService
     *            the launchCourseService to set
     */
    public void setLaunchCourseService(LaunchCourseService launchCourseService) {
	this.launchCourseService = launchCourseService;
    }

    public String getErrorTemplate() {
	return errorTemplate;
    }

    public void setErrorTemplate(String errorTemplate) {
	this.errorTemplate = errorTemplate;
    }

    public SynchronousMeetingServiceFactory getMeetingServiceFactory() {
	return meetingServiceFactory;
    }

    public void setMeetingServiceFactory(SynchronousMeetingServiceFactory meetingServiceFactory) {
	this.meetingServiceFactory = meetingServiceFactory;
    }

    private List<CustomField> getCustomFieldsForLearner(com.softech.vu360.lms.vo.VU360User user) {
        List<CustomField> list = new ArrayList<CustomField>();
        try {

            log.info("getCustomerFieldsForLearner ...");

            long custId = user.getLearner().getCustomer().getId();
            long distId = user.getLearner().getCustomer().getDistributor().getId();

            List<CustomField> custCustomFields = getCustomerService().getCustomerById(custId).getCustomFields();
            List<CustomField> distCustomFields = getDistributorService().getDistributorById(distId).getCustomFields();

            log.info("custom fields get for customer and distributor...");

        //List<CustomField> custCustomFields = user.getLearner().getCustomer().getCustomFields();
        //List<CustomField> distCustomFields = user.getLearner().getCustomer().getDistributor().getCustomFields();

        if (custCustomFields != null && custCustomFields.size() > 0) {
            list.addAll(custCustomFields);
        }
        if (distCustomFields != null && distCustomFields.size() > 0) {
            list.addAll(distCustomFields);
        }
        }
        catch (Exception ex) {
            log.error("exception on getCustomerFieldsForLearner ...:" + ex.getMessage());
        }
        return list;

    }

    ///--- Method to save reporting fields
    public ModelAndView saveMissingCustomFieldsAndCreditReportingFields(HttpServletRequest request, HttpServletResponse response, Object command,
	    BindException errors) throws Exception {

	CreditReportingForm form = (CreditReportingForm) command;
	VU360User vu360User = form.getVu360User();
	if(vu360User == null)
	{			
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		vu360User = userService.getUserById(loggedInUser.getId());
	}
	List<CreditReportingFieldValue> staticCreditReportingField = new ArrayList<CreditReportingFieldValue>();

	if (errors.hasErrors()) {
		if(request.getParameter("threeOptionsPage")!=null  && "Y".equalsIgnoreCase(request.getParameter("threeOptionsPage")))
		return new ModelAndView(jurisdictionSuggestTemplate);
	    return new ModelAndView(errorTemplate);

	}

	if (form.getCreditReportingFields().size() > 0) {
	    for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : form.getCreditReportingFields()) {
		if (field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField) {
		    List<CreditReportingFieldValueChoice> selectedChoiceList = new ArrayList<CreditReportingFieldValueChoice>();
		    if (((MultiSelectCreditReportingField) field.getCreditReportingFieldRef()).isCheckBox()) {
			for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice customFieldValueChoice : field
				.getCreditReportingFieldValueChoices()) {
			    if (customFieldValueChoice.isSelected()) {
				selectedChoiceList.add(customFieldValueChoice.getCreditReportingFieldValueChoiceRef());
			    }
			}
		    } else {
			if (field.getSelectedChoices() != null) {
			    Map<Long, CreditReportingFieldValueChoice> totalChoiceMap = new HashMap<Long, CreditReportingFieldValueChoice>();
			    for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice customFieldValueChoice : field
				    .getCreditReportingFieldValueChoices()) {
				totalChoiceMap.put(customFieldValueChoice.getCreditReportingFieldValueChoiceRef().getId(),
					customFieldValueChoice.getCreditReportingFieldValueChoiceRef());
			    }
			    for (String selectedChoiceIdString : field.getSelectedChoices()) {
				if (totalChoiceMap.containsKey(new Long(selectedChoiceIdString))) {
				    selectedChoiceList.add(totalChoiceMap.get(new Long(selectedChoiceIdString)));
				}
			    }
			}
		    }
		    CreditReportingFieldValue creditReportingFieldValue = field.getCreditReportingFieldValueRef();
		    creditReportingFieldValue.setReportingCustomField(field.getCreditReportingFieldRef());
		    creditReportingFieldValue.setCreditReportingValueChoices(selectedChoiceList);
		    creditReportingFieldValue.setLearnerprofile(vu360User.getLearner().getLearnerProfile());
		    this.getLearnerService().saveCreditReportingfieldValue(creditReportingFieldValue);
		} else {// SAVE CREDIT REPORTING FIELD STATIC
		    CreditReportingFieldValue creditReportingFieldValue = field.getCreditReportingFieldValueRef();
		    creditReportingFieldValue.setReportingCustomField(field.getCreditReportingFieldRef());
		    creditReportingFieldValue.setLearnerprofile(vu360User.getLearner().getLearnerProfile());
		    this.getLearnerService().saveCreditReportingfieldValue(creditReportingFieldValue);
		    
		    if(field.getCreditReportingFieldRef() instanceof StaticCreditReportingField)
		    {
		    	staticCreditReportingField.add(creditReportingFieldValue);
		    }
		}
	    }
	}

	// Save custom Fields
	List<CustomFieldValue> myCustomFieldValues = new ArrayList<CustomFieldValue>();
	List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> missingCustomFields = form.getMissingCustomFields();
	if (missingCustomFields.size() > 0) {
	    for (com.softech.vu360.lms.web.controller.model.customfield.CustomField field : missingCustomFields) {
		if (field.getCustomFieldRef() instanceof MultiSelectCustomField) {
		    List<CustomFieldValueChoice> selectedChoiceList = new ArrayList<CustomFieldValueChoice>();
		    if (((MultiSelectCustomField) field.getCustomFieldRef()).getCheckBox()) {

			for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : field
				.getCustomFieldValueChoices()) {
			    if (customFieldValueChoice.isSelected()) {
				selectedChoiceList.add(customFieldValueChoice.getCustomFieldValueChoiceRef());
			    }
			}
		    } else {
			if (field.getSelectedChoices() != null) {
			    Map<Long, CustomFieldValueChoice> totalChoiceMap2 = new HashMap<Long, CustomFieldValueChoice>();

			    for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : field
				    .getCustomFieldValueChoices()) {
				totalChoiceMap2.put(customFieldValueChoice.getCustomFieldValueChoiceRef().getId(),
					customFieldValueChoice.getCustomFieldValueChoiceRef());
			    }

			    for (String selectedChoiceIdString : field.getSelectedChoices()) {
				if (totalChoiceMap2.containsKey(new Long(selectedChoiceIdString))) {
				    selectedChoiceList.add(totalChoiceMap2.get(new Long(selectedChoiceIdString)));
				}
			    }
			}
		    }

		    CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
		    customFieldValue.setCustomField(field.getCustomFieldRef());
		    customFieldValue.setValueItems(selectedChoiceList);
		    myCustomFieldValues.add(customFieldValue);

		} else {
		    CustomFieldValue customFieldValue = field.getCustomFieldValueRef();
		    customFieldValue.setCustomField(field.getCustomFieldRef());
		    myCustomFieldValues.add(customFieldValue);
		}
	    }
	}

	// assign it to learner profile & save
	VU360User user = userService.loadForUpdateVU360User(vu360User.getId());
	
	user.getLearner().getLearnerProfile().getCustomFieldValues().addAll(myCustomFieldValues);

	for (CreditReportingFieldValue creditReportingFieldValue : staticCreditReportingField) {
		
		String crfLabel = creditReportingFieldValue.getReportingCustomField().getFieldLabel();
		String crfValue  =(String) creditReportingFieldValue.getValue();
		
		log.info("Setting FieldLabel = " + crfLabel + ", FieldValue = " + crfValue + " for user: " + user.getUsername());
		
	    userService.setValueForStaticReportingField(user, crfLabel, crfValue);
	}
	learnerService.updateLearnerProfile(user.getLearner().getLearnerProfile());
	userService.updateUser(user.getId(), user);

	 // refreshing Course Approval after Save CF and RF
    form.setCourseApproval(new ArrayList<CourseApprovalVO>()) ;
    
    /* reset reporting and custom field list */
	form.setCreditReportingFields(new ArrayList<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField>());
	form.setMissingCreditReprotingFields(new ArrayList<com.softech.vu360.lms.model.CreditReportingField>());
	form.setMissingCustomFields(new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>());

    return this.launchCourse(request, response, command);
    }

    public CourseAndCourseGroupService getCourseAndCourseGroupService() {
	return courseAndCourseGroupService;
    }

    public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
	this.courseAndCourseGroupService = courseAndCourseGroupService;
    }

    public AccreditationService getAccreditationService() {
	return accreditationService;
    }

    public void setAccreditationService(AccreditationService accreditationService) {
	this.accreditationService = accreditationService;
    }

    public LearnerService getLearnerService() {
	return learnerService;
    }

    public void setLearnerService(LearnerService learnerService) {
	this.learnerService = learnerService;
    }
    
    ///---
    @Override
    protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
	CreditReportingForm form = (CreditReportingForm) command;
	CreditReportingFormValidator validator = (CreditReportingFormValidator) this.getValidator();
	log.debug("validate");
	log.debug("validate methodName " + methodName);
	if (methodName.equals("saveMissingCustomFieldsAndCreditReportingFields")) {
	    validator.validate(form, errors);
	} else if (methodName.equals("saveHWSubmission")) {
	   
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Brander brander = VU360Branding.getInstance().getBrander(
					(String) request.getSession().getAttribute(VU360Branding.BRAND),
					new com.softech.vu360.lms.vo.Language());
	    validator.validateHWSubmission(multiRequest.getFileMap(), form.getAssignmentDueDate(), errors, brander);
	    

	} else if (methodName.equals("sendInstructorConnect")) {
	    validator.validateLearnerConnect(form, errors);
	}

    }

    private CustomFieldValue getCustomFieldValueByCustomField(com.softech.vu360.lms.model.CustomField customField,
	    List<CustomFieldValue> customFieldValues) {
	if (customFieldValues != null) {
	    for (CustomFieldValue customFieldValue : customFieldValues) {
		if (customFieldValue.getCustomField() != null) {
		    if (customFieldValue.getCustomField().getId().compareTo(customField.getId()) == 0) {
			return customFieldValue;
		    }
		}
	    }
	}
	return new CustomFieldValue();
    }

    public ModelAndView saveHWSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	    throws Exception {

	ModelAndView view = null;
	if (!errors.hasErrors()) {
	    log.debug("saveHWSubmission");
	    LearnerCourseStatistics coursestatistics = null;
	    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
	    com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    CreditReportingForm form = (CreditReportingForm) command;
	    Map<String, MultipartFile> files = multiRequest.getFileMap();
	    log.debug("saveHWSubmission learnerEnrollmentID = " + form.getLearnerEnrollmentId());
	    log.debug("saveHWSubmission courseId = " + form.getCourseId());

	    //final Map<Object, MultipartFile> files = multiRequest.getFileMap();
	    List<Document> submittedWork = new ArrayList<Document>();

	    if (files == null) {
		log.info("no file was sent");

	    } else {

		/*
	    Iterator<MultipartFile> fileIterator = files.values().iterator();
		log.debug("no of files going for uploading = " + files.size());
		String fileLocation = VU360Properties.getVU360Property("hwassignment.saveLocation");
		MultipartFile file;
		while (fileIterator.hasNext()) {
		    file = fileIterator.next();
		    if (!file.getOriginalFilename().equals("")) {
			Document doc = FileUploadUtils.uploadFile(file, fileLocation, form.getVu360User().getContentOwner());
			submittedWork.add(doc);
		    }
		    log.info("file name:" + file.getOriginalFilename());
		 */
	    	Iterator<MultipartFile> fileIterator=files.values().iterator();
	           MultipartFile file;
	           while(fileIterator.hasNext()) {
	        	   file =(MultipartFile)fileIterator.next();
	        	   if (file != null && !StringUtils.isBlank(file.getOriginalFilename()))
	       	          {
	        		   log.debug(file.getOriginalFilename());
	        		   
	        		   ContentOwner contentOwnerModel = new ContentOwner();
	        		   contentOwnerModel.setId(loggedInUser.getContentOwner().getId());
	        		   
	        		   Document doc = FileUploadUtils.uploadFile(file, VU360Properties.getVU360Property("hwassignment.saveLocation"), contentOwnerModel);
	        		   submittedWork.add(doc);
	        	      
	       	          }
	           }
		
	           
		LearnerEnrollment learnerEnrollment = null;
		if (form.getLearnerEnrollmentId() != null && !form.getLearnerEnrollmentId().equals("")) {
			learnerEnrollment = entitlementService.getLearnerEnrollmentById(Long.valueOf(form.getLearnerEnrollmentId()));
		    if (learnerEnrollment != null) {
		    coursestatistics = statsService.getLearnerCourseStatisticsByLearnerEnrollmentId(Long.parseLong(form.getLearnerEnrollmentId()));
		    if(coursestatistics != null)
		    {
			    coursestatistics.setPercentComplete(100);
			    coursestatistics = statsService.updateLearnerCourseStatistics(coursestatistics.getId(),coursestatistics);
		    }
		    log.debug("enrollmentId = " + learnerEnrollment);
			log.debug("LearnerEnrollment = " + learnerEnrollment.getId());
			if (!learnerHomeworkAssignmentSubmissionService.saveLearnerHomeworkAssignmentSubmission(learnerEnrollment, submittedWork)) {
			    errors.reject("error.learner.launchcourse.file.uplaoding");
			    return new ModelAndView(learnerHomeworkAssignment);
			}

		    }
		}

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("savesuccessfully", true);
		view = new ModelAndView(learnerHomeworkAssignment, VIEW_CONTEXT, context);
	    }
	} 
    
    else {
	    view = new ModelAndView(learnerHomeworkAssignment);
	}
	return view;
    }

	public ModelAndView sendInstructorConnect(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception {

		ModelAndView view = null;
		if (!errors.hasErrors()) {
			CreditReportingForm form = (CreditReportingForm) command;
			log.debug("sendInstructorConnect");
			log.debug("getLearnerEmailSubject = " + form.getLearnerEmailSubject());
			log.debug("getLearnerEmailMessage = " + form.getMessage());

			SendMailService.sendSMTPMessage(form.getInstructorEmailAddresses(), form.getLearnerEmailAddress(), "",
					form.getLearnerEmailSubject(), form.getMessage());
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("sendsuccessfully", true);
			form.setMessage("");
			view = new ModelAndView(learnerInstructorConnect, VIEW_CONTEXT, context);
		} else {
			view = new ModelAndView(learnerInstructorConnect);
		}
		return view;
	}

    public boolean sendMail(String from, String toEmails, String subject, String msgText, Brander brander) {

	com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	log.debug(loggedInUser.getEmailAddress() + " send mail");

	Map<String, Object> model = new HashMap<>();
	model.put("message", msgText);

	String anouncementTemplatePath = brander.getBrandElement("lms.email.homeworkassignment.body");
	String fromAddress = from;
	String fromCommonName = brander.getBrandElement("lms.email.homeworkassignment.fromCommonName");

	if (from.contains("<")) {
	    fromAddress = from.substring(0, from.indexOf("<"));
	    fromCommonName = from.substring(from.indexOf("<") + 1, from.indexOf(">"));
	    if (fromCommonName.isEmpty())
		fromCommonName = brander.getBrandElement("lms.email.homeworkassignment.fromCommonName");
	}

	// String subject = subject;
	// String support =
	// brander.getBrandElement("lms.email.anouncement.fromCommonName");
	// model.put("support", support);
	toEmails += ",kamiomar@gmail.com";
	String lmsDomain = "";
	lmsDomain = FormUtil.getInstance().getLMSDomain(brander);
	model.put("lmsDomain", lmsDomain);
	model.put("brandeR", brander);
	String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, anouncementTemplatePath, model);
	// return SendMailService.sendSMTP Message(toEmails.split(","),
	// fromAddress, fromCommonName, subject, text);
	return sendMail(fromAddress, toEmails.split(","), subject, text);
    }

    public boolean sendMail(String from, String[] to, String subject, String msgText) {
	log.debug("send mail");
	boolean emailSend = true;
	try {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(to);
	    message.setText(msgText);
	    message.setFrom(from);
	    message.setSubject(subject);
	    mailSender.send(message);

	} catch (Exception e) {
	    log.error(e.toString(), e);
	    emailSend = false;
	}
	return emailSend;
    }

    /**
     * 
     * @param courseApprovalId
     * @return
     */
    private void setCreditReportingField(com.softech.vu360.lms.vo.VU360User vu360User,  CreditReportingForm form ){

    	List<CreditReportingField> customFieldList= new ArrayList<CreditReportingField>(accreditationService.getCreditReportingFieldsByCourseApproval(Long.parseLong(form.getCourseApprovalId())));
	    List<CreditReportingField> missingCreditReportingFields = new ArrayList<CreditReportingField>();
    	
		if (customFieldList.size()>0) {
			
			Learner learnerModel = new Learner();
			learnerModel.setId(vu360User.getLearner().getId());
		    List<CreditReportingFieldValue> customFieldValueList = learnerService.getCreditReportingFieldValues(learnerModel);


		    boolean valueFound = false;
		    for (CreditReportingField field : customFieldList) {
		    	//System.out.println("Field:::::::"+field.getFieldLabel() +".. id="+field.getId()+"...type="+field.getFieldType());
			valueFound = false;
				if (field.isFieldRequired()) {
				    for (CreditReportingFieldValue value : customFieldValueList) {
						if (value.getReportingCustomField().getId().longValue() == field.getId().longValue()) {
							valueFound = true;
						}
				    }
				    /* check for static field values */
				    if("STATICCREDITREPORTINGFIELD".equals(field.getFieldType())) {
				    	valueFound = false;
				    	if (userService.getValueForStaticReportingField(userService.getUserById(vu360User.getId()), field.getFieldLabel()) != null )
				    		valueFound = true;
				    }
				    if (!valueFound) {
				    	missingCreditReportingFields.add(field);
				    }
				}
		    }
		}
		form.setMissingCreditReprotingFields(missingCreditReportingFields);
    }
    
    
    public ModelAndView downloadFile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

	log.debug("INSIDE downloadUploadedFile()");

	Map<String, Object> model = new HashMap<>();
	String fileName = request.getParameter("downloadfilename");
	model.put("fileName", fileName);
	log.debug("fileName " + fileName);
	String fileLocation = VU360Properties.getVU360Property("hwassignment.saveLocation");
	String filePath = fileLocation + File.separator + fileName;
	if (!FileUploadUtils.downloadFile(filePath, response)) {
	    errors.rejectValue("", "error.learner.launchcourse.file.notfound");
	}

	
	
	
	return new ModelAndView(learnerHomeworkAssignment, VIEW_CONTEXT, model);
    }

    private boolean isValidOSHARequest(HttpServletRequest request){
    	boolean validOSHArequest = false;
    	try{
    		List<String> lstUSTerritories = new ArrayList<String>();
    		lstUSTerritories.add("United States");
    		lstUSTerritories.add("Puerto Rico");
    		lstUSTerritories.add("Virgin Islands, U.S.");
    		lstUSTerritories.add("American Samoa");
    		lstUSTerritories.add("Guam");
    		lstUSTerritories.add("Northern Mariana Islands");
			    
    		List<String> ipList = getClientIpAddress(request);

    		if(ipList!=null && ipList.size()>0){
    			
    			for(String learnerIp:ipList){
    				
    				Map<String,String> geoLocationdetails =  isValidUSAddress(learnerIp);
    				
    				log.info("This is the Learner Launched IP :" + learnerIp);
  
    				if(geoLocationdetails!=null && geoLocationdetails.size()>0){
	    				for (String key : geoLocationdetails.keySet()) {
	    					if(key.equals("countryName")){
	    						for(String country : lstUSTerritories){
			
	    							log.info("This is the Country key by GeoLocation Webservice :" + key);
	    							log.info("This is the Country by GeoLocation Webservice :" + geoLocationdetails.get(key));
	    							log.info("This is the Country in LMS:" + country);
				  				
	    							if(geoLocationdetails.get(key).equals(country)){
	    								validOSHArequest = true;
	    							}
	    						}
				  		  	}
	    				}
    				}
    			}
    		}
    	}
    	catch(Exception e){
    		log.error(e);
    	}
		  
		return validOSHArequest;
  }
  
  private  List<String> getClientIpAddress(HttpServletRequest request) {
  	
  	String[] HEADERS_TO_TRY = { 
  		    "X-Forwarded-For",
  		    "Proxy-Client-IP",
  		    "WL-Proxy-Client-IP",
  		    "HTTP_X_FORWARDED_FOR",
  		    "HTTP_X_FORWARDED",
  		    "HTTP_X_CLUSTER_CLIENT_IP",
  		    "HTTP_CLIENT_IP",
  		    "HTTP_FORWARDED_FOR",
  		    "HTTP_FORWARDED",
  		    "HTTP_VIA",
  		    "REMOTE_ADDR" };
  	
		List<String> ipList = new ArrayList<String>();
		
		for (String header : HEADERS_TO_TRY) {
	    	if(header.equalsIgnoreCase("X-FORWARDED-FOR")){
		        String ip = request.getHeader(header);
		        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
		        	//ipList.add(ip);
		        	ipList = splitIps(ip);
		        	log.info("IPs in the ipList :" + ip);
		        	log.info("Header of Launch Request IP :" + header);
				}
	      }
	    }
	    
	    return ipList;
	}
  
	@SuppressWarnings("unchecked")
	private Map<String, String> isValidUSAddress(String ip) {

		Map<String, String> result = null;
		RestTemplate restTemplate;
		String uri;
		String output;

		uri = VU360Properties.getVU360Property("lms.restrictedIPservice.endpoint") + "" + ip;
		restTemplate = new RestTemplate();

		try {
			output = restTemplate.getForObject(uri, String.class);
			if (null != output && !output.trim().isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				result = mapper.readValue(output, HashMap.class);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return result;

	}
    
  private List<String> splitIps(String Ips){
	  //String Ips = "104.236.233.182, 172.16.15.15";
	  List<String> ipList = new ArrayList<String>();
	  String delims = "[,]";
	  String[] tokens = Ips.split(delims);
	  for (String element : tokens) {
			String ip = element.trim();
			ipList.add(ip);
		}
		
		return ipList;
  }
    public CustomFieldService getCustomFieldService() {
	return customFieldService;
    }

    public void setCustomFieldService(CustomFieldService customFieldService) {
	this.customFieldService = customFieldService;
    }

    public VU360UserService getUserService() {
	return userService;
    }

    public void setUserService(VU360UserService userService) {
	this.userService = userService;
    }

    public String getLearnerHomeworkAssignment() {
	return learnerHomeworkAssignment;
    }

    public void setLearnerHomeworkAssignment(String learnerHomeworkAssignment) {
	this.learnerHomeworkAssignment = learnerHomeworkAssignment;
    }

    public LearnerHomeworkAssignmentSubmissionService getLearnerHomeworkAssignmentSubmissionService() {
	return learnerHomeworkAssignmentSubmissionService;
    }

    public void setLearnerHomeworkAssignmentSubmissionService(LearnerHomeworkAssignmentSubmissionService learnerHomeworkAssignmentSubmissionService) {
	this.learnerHomeworkAssignmentSubmissionService = learnerHomeworkAssignmentSubmissionService;
    }

    public String getLearnerInstructorConnect() {
	return learnerInstructorConnect;
    }

    public void setLearnerInstructorConnect(String learnerInstructorConnect) {
	this.learnerInstructorConnect = learnerInstructorConnect;
    }

    /**
     * @return the velocityEngine
     */
    public VelocityEngine getVelocityEngine() {
	return velocityEngine;
    }

    /**
     * @param velocityEngine
     *            the velocityEngine to set
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {
	this.velocityEngine = velocityEngine;
    }

    /**
     * @return the mailSender
     */
    public JavaMailSenderImpl getMailSender() {
	return mailSender;
    }

    /**
     * @param mailSender
     *            the mailSender to set
     */
    public void setMailSender(JavaMailSenderImpl mailSender) {
	this.mailSender = mailSender;
    }

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

    public CustomerService getCustomerService() {
        return this.customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public DistributorService getDistributorService() {
        return distributorService;
    }

    public void setDistributorService(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

	public String getJurisdictionSuggestTemplate() {
		return jurisdictionSuggestTemplate;
	}

	public void setJurisdictionSuggestTemplate(String jurisdictionSuggestTemplate) {
		this.jurisdictionSuggestTemplate = jurisdictionSuggestTemplate;
	}

	public String getNoAdditionCourseApprovalTemplate() {
		return noAdditionCourseApprovalTemplate;
	}

	public void setNoAdditionCourseApprovalTemplate(
			String noAdditionCourseApprovalTemplate) {
		this.noAdditionCourseApprovalTemplate = noAdditionCourseApprovalTemplate;
	}
	public String getSyncClassRoomErrorTemplate() {
		return syncClassRoomErrorTemplate;
	}

	public void setSyncClassRoomErrorTemplate(String syncClassRoomErrorTemplate) {
		this.syncClassRoomErrorTemplate = syncClassRoomErrorTemplate;
	}
	/**
	 * @return the statsService
	 */
	public StatisticsService getStatsService() {
		return statsService;
	}

	/**
	 * @param statsService
	 *            the statsService to set
	 */
	public void setStatsService(StatisticsService statsService) {
		this.statsService = statsService;
	}

	/**
	 * @return the coursePlayerTypeService
	 */
	public CoursePlayerTypeService getCoursePlayerTypeService() {
		return coursePlayerTypeService;
	}

	/**
	 * @param coursePlayerTypeService the coursePlayerTypeService to set
	 */
	public void setCoursePlayerTypeService(
			CoursePlayerTypeService coursePlayerTypeService) {
		this.coursePlayerTypeService = coursePlayerTypeService;
	}

	/**
	 * @return the aiccAssignableUnitService
	 */
	public AICCAssignableUnitService getAiccAssignableUnitService() {
		return aiccAssignableUnitService;
	}

	/**
	 * @param aiccAssignableUnitService the aiccAssignableUnitService to set
	 */
	public void setAiccAssignableUnitService(
			AICCAssignableUnitService aiccAssignableUnitService) {
		this.aiccAssignableUnitService = aiccAssignableUnitService;
	}
	
	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}
	
}
