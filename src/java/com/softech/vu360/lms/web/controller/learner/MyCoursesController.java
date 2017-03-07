package com.softech.vu360.lms.web.controller.learner;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfiguration;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LaunchCourseService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SubscriptionService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.SubscriptionVO;
import com.softech.vu360.lms.web.controller.model.AvailableCoursesTree;
import com.softech.vu360.lms.web.controller.model.CourseView;
import com.softech.vu360.lms.web.controller.model.MyCoursesItem;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * The controller for the learner course display page.
 *
 * @author jason
 * @author Ashis Taru Mukherjee
 * @modified by Faisal A. Siddiqui
 * 
 */
public class MyCoursesController extends MultiActionController implements
InitializingBean {

	public static final String RECENT_MYCOURSES_VIEW = "recent";
	public static final String ENROLLED_MYCOURSES_VIEW = "enrolled";
	public static final String COURSECATALOG_MYCOURSES_VIEW = "coursecatalog";
	public static final String SURVEY_MYCOURSES_VIEW = "survey";
	public static final String COMPLETED_MYCOURSES_VIEW = "completedCourses";
	public static final String EXPIRED_MYCOURSES_VIEW = "expiredCourses";
	public static final String COURSES_ABOUT_TO_EXPIRED_MYCOURSES_VIEW = "coursesAboutToExpire";
	private static final String MANAGE_USER_PREVPAGE_DIRECTION = "prev";
	private static final String MANAGE_USER_NEXTPAGE_DIRECTION = "next";
	private static final String STR_SEARCH = "search";
	private static final String STR_CONTEXT = "context";
	
	private static final Logger log = Logger.getLogger(MyCoursesController.class.getName());
	private String myCoursesTemplate = null;
	private String courseDetailsTemplate = null;
	private String viewAssessmentTemplate = null;
	private String loginTemplate = null;
	private String courseCompletionReportTemplate = null;
	private EntitlementService entitlementService;
	private StatisticsService statsService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private TrainingPlanService trainingPlanService = null;
	private LaunchCourseService launchCourseService = null;
	private SynchronousClassService synchronousClassService;
	private SurveyService surveyService;
	private EnrollmentService enrollmentService;
	private AccreditationService accreditationService;

	private String courseDescriptionTemplate = null;
	private String launchTemplate=null;
	private String mySurveysTemplate=null;

	// templates to handle un-enrolled courses templates 
	private String catalogCourseDetailsTemplate = null;
	private String catalogCourseDescriptionTemplate = null;
	private String catalogCourseCompletionReportTemplate = null;
	private String viewScheduleTemplate=null;
	private String syncCourseDetailsTemplate=null;
	private String scheduleToEnrollTemplate=null;
	private String availableCoursesTemplate = null;
	private String completedCoursesTemplate = null;
	private String expiredCoursesTemplate = null;
	private String coursesAboutToExpireTemplate = null;
	private String courseDescriptionAjaxTemplate;
	private SubscriptionService subscriptionService = null;
	
	@Autowired
	private VU360UserService vu360UserService;
	
	public String getCompletedCoursesTemplate() {
		return completedCoursesTemplate;
	}

	public void setCompletedCoursesTemplate(String completedCoursesTemplate) {
		this.completedCoursesTemplate = completedCoursesTemplate;
	}

	public String getExpiredCoursesTemplate() {
		return expiredCoursesTemplate;
	}

	public void setExpiredCoursesTemplate(String expiredCoursesTemplate) {
		this.expiredCoursesTemplate = expiredCoursesTemplate;
	}

	public String getCoursesAboutToExpireTemplate() {
		return coursesAboutToExpireTemplate;
	}

	public void setCoursesAboutToExpireTemplate(String coursesAboutToExpireTemplate) {
		this.coursesAboutToExpireTemplate = coursesAboutToExpireTemplate;
	}

	public String getAvailableCoursesTemplate() {
		return availableCoursesTemplate;
	}

	public void setAvailableCoursesTemplate(String availableCoursesTemplate) {
		this.availableCoursesTemplate = availableCoursesTemplate;
	}

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	@SuppressWarnings("unchecked")
	public ModelAndView displayMyCourses(HttpServletRequest request, HttpServletResponse response) {
 
		/**
		 * This has been implemented for CAS
		 * If authenticated user is a Manager, and coming directly on this page
		 * system will switch him to Learner mode
		 */
		try {
			VU360UserAuthenticationDetails authUserdetails = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
			if(!authUserdetails.getCurrentMode().equals(VU360UserMode.ROLE_LEARNER)) {
				log.debug("Implemented for CAS, redirecting to Learner mode");
				return new ModelAndView("redirect:mgrSwitchLearnerMode.do?managerSwitchLearnerModeTargetURL=" + request.getRequestURI().replace("/lms", "") + "?" + request.getQueryString());
			}
		} catch(Exception ex) {
			log.error(ex.getMessage(),ex);
		}

		log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~ Start Time (displayMyCourses):"+new Timestamp(System.currentTimeMillis()));
		
		String strViewType = request.getParameter("show");
		String search= request.getParameter(STR_SEARCH);
		Boolean isFirstTimeView = Boolean.FALSE;
		
		if (StringUtils.isBlank(strViewType)) {
			strViewType = RECENT_MYCOURSES_VIEW; // the default view
			isFirstTimeView=Boolean.TRUE;
		}
		
		if (SURVEY_MYCOURSES_VIEW.equalsIgnoreCase(strViewType)) {
			return new ModelAndView(mySurveysTemplate);
		}
		
		com.softech.vu360.lms.vo.VU360User userVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (COURSECATALOG_MYCOURSES_VIEW.equals(strViewType)){
			Learner learner = new Learner();
			learner.setId(userVO.getLearner().getId());
			
			Map<Object, Object> context = new HashMap<>();

			// SWITCH BETWEEN THESE TWO FOR TEST VS LIVE DATA.
			AvailableCoursesTree myCoursesTree = enrollmentService.getAvailableCoursesTree(learner, search);
			myCoursesTree.build();
			JSONObject rootNodesJSON = myCoursesTree.generateAllNodesJSON();

			context.put("gJON", rootNodesJSON);
			context.put("viewType", COURSECATALOG_MYCOURSES_VIEW);
			log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~ Completion Time (Dispatching):"+new Timestamp(System.currentTimeMillis()));
			return new ModelAndView(myCoursesTemplate, STR_CONTEXT, context);
		}
		
		if ("subscription".equals(strViewType)){
			com.softech.vu360.lms.vo.Learner learner = userVO.getLearner();
			
			String direction = request.getParameter("direction");
			String pageIndex = request.getParameter("pageIndex");
			String subscriptionId = request.getParameter("cboSubscription");
			String coursesearch = request.getParameter("courseSearch");
			String selSubscription =  request.getParameter("selSubscription");
			int totalRecords;
			String seletctedSubId = null;
			boolean subUser = false;
			
			int iSearchStart;
			int iSearchEnd;
			
			HttpSession session = request.getSession();
			
			direction = ( direction == null ) ? MANAGE_USER_PREVPAGE_DIRECTION : direction;
			pageIndex = ( pageIndex == null ) ? "0" : pageIndex;
			
			Map<Object, Object> context = new HashMap<>();
			
			if(coursesearch== null ){
				coursesearch = request.getParameter("courseName");
			}
			log.debug("Searched Course:"+ coursesearch);
			
			if(StringUtils.isNotBlank(selSubscription))	{
				subscriptionId = selSubscription;
				session.setAttribute("subId", selSubscription);
			}else if(session.getAttribute("subId")!=null && session.getAttribute("subId").toString().length() > 0){
				subscriptionId = session.getAttribute("subId").toString();
			}
			
			List<SubscriptionVO> lstSubscriptionVO = new ArrayList<>();
			
			if(subscriptionId != null)
			{
				seletctedSubId =    subscriptionId;
				lstSubscriptionVO = subscriptionService.getUserSubscriptionCourses(learner.getId(), learner.getVu360User().getId(),Long.parseLong(subscriptionId),coursesearch);
				subUser = true;
			}else{
				if(subscriptionService.getUserSubscriptions(learner.getVu360User().getId()) != null 
						&& !subscriptionService.getUserSubscriptions(learner.getVu360User().getId()).isEmpty()){
					seletctedSubId = subscriptionService.getUserSubscriptions(learner.getVu360User().getId()).get(0).getSubscriptionId();
					lstSubscriptionVO = subscriptionService.getUserSubscriptionCourses(learner.getId(), learner.getVu360User().getId(),Long.parseLong(seletctedSubId),coursesearch);
					subUser = true;
				}
			}
	
			totalRecords =	lstSubscriptionVO.isEmpty()?-1: lstSubscriptionVO.size();
			int pageNo = 0;
			int recordShowing = 0;
			
			if( direction.equalsIgnoreCase(MANAGE_USER_PREVPAGE_DIRECTION) ) {
				pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
				pageNo = (pageNo <= 0)?0:pageNo-1;
				session.setAttribute("prevAction","paging");
			}else if( direction.equalsIgnoreCase(MANAGE_USER_NEXTPAGE_DIRECTION) ) {
				pageIndex = request.getParameter("pageIndex");
				pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
				pageNo = (pageNo<0)?0:pageNo+1;
				session.setAttribute("prevAction","paging");
			}
			
			if (lstSubscriptionVO.size() <= 10)
				iSearchEnd = lstSubscriptionVO.size() - 1;
			else
				iSearchEnd = 9;
			
        	iSearchStart= 0;
			session.setAttribute("pageNo", pageNo);
			session.setAttribute("direction", direction);
			session.setAttribute("selectedSubscription", direction);
			if(coursesearch != null)
				session.setAttribute("coursesearch", coursesearch);	
			
			context.put("viewType", strViewType);//"subscription"
			context.put("cbosubscription", subscriptionService.getUserSubscriptions(learner.getVu360User().getId()));
			context.put("isSubUser", subUser);
			context.put("lstSubscription", lstSubscriptionVO);
			context.put("totalRecord", totalRecords);
			context.put("recordShowing", recordShowing);
			context.put("pageNo", session.getAttribute("pageNo"));
			context.put("direction", direction);
			context.put("Search Start",iSearchStart);
			context.put("Search End", iSearchEnd);
			context.put("subscriptionId", seletctedSubId);
			context.put("coursesearch", session.getAttribute("coursesearch"));
			
			return new ModelAndView(myCoursesTemplate, STR_CONTEXT, context);
			
		}
		try {
			
			List<MyCoursesItem> filteredMyCourses = null;
			
			Learner learnerModel = new Learner();
			learnerModel.setId(userVO.getLearner().getId());
			
			LearnerProfile learnerProfileModel = new LearnerProfile();
			learnerProfileModel.setId(userVO.getLearner().getLearnerProfile().getId());
			
			if(userVO.getLearner().getLearnerProfile().getTimeZone() != null) {
				TimeZone learnerTimeZone = new TimeZone();
				learnerTimeZone.setId(userVO.getLearner().getLearnerProfile().getTimeZone().getId());
				learnerTimeZone.setCode(userVO.getLearner().getLearnerProfile().getTimeZone().getCode());
				learnerTimeZone.setHours(userVO.getLearner().getLearnerProfile().getTimeZone().getHours());
				learnerTimeZone.setMinutes(userVO.getLearner().getLearnerProfile().getTimeZone().getMinutes());
				learnerTimeZone.setZone(userVO.getLearner().getLearnerProfile().getTimeZone().getZone());
				learnerProfileModel.setTimeZone(learnerTimeZone);
			}
			learnerModel.setLearnerProfile(learnerProfileModel);
			
			VU360User vu360UserModel = new VU360User();
			vu360UserModel.setLearner(learnerModel);
			
			/*
			 * At this point we are adding a pre-display courses action to check and update status 
			 * of Synchronous course for a learner automatically (TPMO-960) 
			 */
			enrollmentService.updateAutoSynchronousCourseStatus(learnerModel);
			
			Brander brand = VU360Branding.getInstance().getBrander((String) request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			Map<Object, Object> context = enrollmentService.displayMyCourses(vu360UserModel, brand, new ArrayList<>(), filteredMyCourses, new ArrayList<>(), strViewType, search, isFirstTimeView);
			
			context.put("show", strViewType);
			context.put(STR_SEARCH, search);
			
			
			String lmsDomain=VU360Properties.getVU360Property("lms.domain");
			context.put("lmsDomain",lmsDomain);
			context.put("lguid", userVO.getUserGUID()); //LMS-19305
			log.debug("learner guid>>"+userVO.getUserGUID());
			return new ModelAndView(myCoursesTemplate, STR_CONTEXT, context);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return new ModelAndView(myCoursesTemplate);
		
	}

	
	/*
	 * Niles Testing
	 */
	public ModelAndView displayMyCoursesOverlay(HttpServletRequest request, HttpServletResponse response) {
		try {
			String courseId = request.getParameter("courseId");
			String courseGroupId = request.getParameter("courseGroupId"); 
			Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(courseId));
			String courseGUID = course.getCourseGUID();
			String courseIdStr=course.getBussinesskey(); //LMS-11993
			String courseTitle = course.getCourseTitle();
			String courseType = course.getCourseType();
			String courseCreditHours = course.getCredithour();
			String coursePath = null;
			if(courseGroupId != null && !courseGroupId.trim().equals("")){
				coursePath = entitlementService.getCoursePathToCourseGroup(Integer.valueOf(courseGroupId));
			}
			
			String courseDescription = course.getDescription();			
			String courseOverview = "";
			String courseGuide = course.getCourseGuide();
			String coursePrerequisites = course.getCoursePrereq();
			String courseLearningObjectives = course.getLearningObjectives();
			String courseQuizInfo = course.getQuizInfo();
			String courseFinalExamInfo = course.getFinalExamInfo();
			String courseEndOfCourseInstructions = course.getEndofCourseInstructions();
			String courseAdditionalDetails = "";
			
			//LMS-15747
			String courseDeliveryMethod="";
			
			try{
				if(course.getDeliveryMethodId()!=null){
					
					Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
					List<LabelBean> deliveryMethodTypes = brander.getBrandMapElements("lms.Instructor.SynchronousCourse.DeliveryMethod");
					
					for( LabelBean deliveryMethodType : deliveryMethodTypes ) {
						
						if(deliveryMethodType.getValue().equalsIgnoreCase(course.getDeliveryMethodId()+"")) {
							courseDeliveryMethod=deliveryMethodType.getLabel();
							break ;
						}
					}
				}	
			}catch(Exception exSub){
				log.debug("exception when set Delivery Method", exSub);
			}
						
			double courseApprovedCourseHours = course.getApprovedcoursehours();
			String courseApprovalNumber = course.getApprovalNumber();
			double courseDuration = 0.0;
			
			if(course.getCeus()!=null){
				courseDuration = course.getCeus();
			}
			log.debug("courseGUID " + courseGUID);
			
			JSONObject courseDetailsJSON = new JSONObject();
			courseDetailsJSON.put("courseGUID", courseGUID);
			courseDetailsJSON.put("courseId", courseIdStr);
			courseDetailsJSON.put("courseTitle", courseTitle);
			courseDetailsJSON.put("courseType", courseType);
			courseDetailsJSON.put("courseCreditHours", courseCreditHours);
			
			if(coursePath != null && !coursePath.trim().equals(""))
				courseDetailsJSON.put("coursePath", coursePath);
			
			courseDetailsJSON.put("courseDescription", courseDescription);			
			courseDetailsJSON.put("courseOverview", courseOverview);
			courseDetailsJSON.put("courseGuide", courseGuide);
			courseDetailsJSON.put("coursePrerequisites", coursePrerequisites);
			courseDetailsJSON.put("courseLearningObjectives", courseLearningObjectives);
			courseDetailsJSON.put("courseQuizInfo", courseQuizInfo);
			courseDetailsJSON.put("courseFinalExamInfo", courseFinalExamInfo);
			courseDetailsJSON.put("courseAdditionalDetails", courseAdditionalDetails);
			courseDetailsJSON.put("courseDeliveryMethod", courseDeliveryMethod);
			courseDetailsJSON.put("courseApprovedCourseHours", courseApprovedCourseHours);
			courseDetailsJSON.put("courseApprovalNumber", courseApprovalNumber);
			courseDetailsJSON.put("courseEndOfCourseInstructions", courseEndOfCourseInstructions);
			courseDetailsJSON.put("courseDuration", courseDuration);
			
			
			
			PrintWriter pw = response.getWriter();
			response.setHeader("Content-Type", "application/json");
			pw.write(courseDetailsJSON.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}  catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
		
	}

	public ModelAndView displayScheduleOverlay(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId="
				+ user.getLearner());
			Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
			
			String courseId = request.getParameter("courseId");
			String learnerEnrollmentId = request.getParameter("learnerEnrollmentId");
			
			log.debug(courseId);
			log.debug(learnerEnrollmentId);
			
			JSONObject scheduleOverlayJSON = new JSONObject();
                        
			Map<Object, Object> context = new HashMap<Object, Object>();
			try{
                            context = enrollmentService.displayViewSchedule(vu360UserService.getUserById(user.getId()), brand, courseId, learnerEnrollmentId);
                            Course course = (Course) context.get("course");
                            List<SynchronousClass> syncClasses = (List<SynchronousClass>) context.get("syncClasses");
                            JSONArray jsonArray = createJSONArray(syncClasses);
                            if( null != course && jsonArray.size()>0 ) {
                                scheduleOverlayJSON.put("syncClassesWithSessions", jsonArray);
                                scheduleOverlayJSON.put("courseTitle", course.getCourseTitle());
                            }
			}catch(Exception e){
                            logger.fatal("Exception occurred.", e);
                            scheduleOverlayJSON.put("error", e.toString());
			}
			
			PrintWriter pw = response.getWriter();
			response.setHeader("Content-Type", "application/json");
			pw.write(scheduleOverlayJSON.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			log.debug("IOException", e);
		}  catch (IllegalStateException e) {
			log.debug("IllegalStateException", e);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return null;
	}

	/**
	* // [12/15/2010] LMS-7942 :: Show Previous Enrollments detail for the same course
 	*/
	public ModelAndView displayCourseDetails(HttpServletRequest request, HttpServletResponse response) {
		
		log.debug("1- displayCourseDetailsPage freeMemory : "+Runtime.getRuntime().freeMemory());

		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId = "+ user.getLearner());
			Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
			
			// Get request parameters
			String learnerEnrollmentID = request.getParameter("learnerEnrollmentId");
			String viewType = request.getParameter("show");
			String activeTab = StringUtils.isBlank( request.getParameter("activeTab") ) ? "crnt" : request.getParameter("activeTab");
			String crntEnrollmentId = request.getParameter("crntEnrollmentId");
			String selEnrollmentPeriod = request.getParameter("selEnrollmentPeriod");
			
			log.debug(learnerEnrollmentID == null ? "learnerEnrollmentID null" : " learnerEnrollmentID=" + learnerEnrollmentID);
			
			Map<Object, Object> context = enrollmentService.displayCourseDetailsPage(learnerEnrollmentID, crntEnrollmentId, vu360UserService.getUserById(user.getId()), activeTab, viewType, selEnrollmentPeriod, brand);
		

					
					
			return new ModelAndView(courseDetailsTemplate, STR_CONTEXT, context);
				
			
		} 
		catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(loginTemplate);
	}
	



	public ModelAndView displayCourseCompletionReport(HttpServletRequest request, HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId="
				+ user.getLearner());
			String viewType = request.getParameter("show");
			String learnerEnrollmentID = request.getParameter("learnerEnrollmentId");
			String courseId = request.getParameter("courseId");

			Map<Object, Object> context = new HashMap<Object, Object>();

			LearnerEnrollment learnerEnrollment = null;
			Course course = null;
			LearnerCourseStatistics courseStatistics = null;
			CourseConfiguration courseCompletionCriteria = null;
			if (learnerEnrollmentID != null
					&& !learnerEnrollmentID.trim().equalsIgnoreCase("")) {
				learnerEnrollment = entitlementService
				.getLearnerEnrollmentById(Long
						.valueOf(learnerEnrollmentID));
				/*
				 * Check whether enrollment object which learner is looking for is enrolled with logged in learner
				 * To prevent changing from URL
				 */
				if(user.getLearner().getId().longValue()== learnerEnrollment.getLearner().getId().longValue()){
					course = learnerEnrollment.getCourse();
					courseStatistics = learnerEnrollment.getCourseStatistics();
					//courseCompletionCriteria = new CourseConfiguration();//courseAndCourseGroupService.findCourseCompletionCriteriaForCourse(user.getLearner(), course);


					if (courseCompletionCriteria.getCourseEvaluationSpecified()) {
						// [2/15/2011] LMS-8972 :: Course Evaluation Criteria is appearing incorrectly
						boolean courseEvaluationCompleted = this.surveyService.isCourseEvaluationCompleted( learnerEnrollment.getId() );
						context.put("courseEvaluationCompleted", courseEvaluationCompleted);
					}
				}

			} else {
				// no enrollment yet - just look up the course
				course = courseAndCourseGroupService.getCourseById(Long.valueOf(courseId));
				
				courseStatistics = null;
				learnerEnrollment = null;
				//courseCompletionCriteria = new CourseConfiguration();//courseAndCourseGroupService.findCourseCompletionCriteriaForCourse(user.getLearner(), course);
			}

			List<Survey> dueSurveys=surveyService.getDueSurveysByLearnerEnrollment(learnerEnrollment.getId());
			if(CollectionUtils.isEmpty(dueSurveys))
			{
				context.put("surveysRemaining", Boolean.TRUE);	
			}
			else
			{
				context.put("surveysRemaining", Boolean.FALSE);
			}

			context.put("learnerEnrollment", learnerEnrollment);
			context.put("course", course);
			context.put("viewType",viewType);
			context.put("courseStatistics", courseStatistics);
			context.put("courseCompletionCriteria", courseCompletionCriteria);
			context.put("userName", user.getFirstName() + " " + user.getLastName());

			return new ModelAndView(courseCompletionReportTemplate, STR_CONTEXT, context);

		}
		catch (Exception e) {
			log.debug(e.getMessage(), e);
		}

		return new ModelAndView(loginTemplate);
	}

	/**
	 * @author Adeel Hussain
	 * 
	 * Description : View Course completion report for un enrolled / Catalog courses  
	 */
	public ModelAndView displayCourseSampleCompletionReport(HttpServletRequest request, HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId="
				+ user.getLearner());

			//String learnerEnrollmentID = request.getParameter("learnerEnrollmentId");
			String courseId = request.getParameter("courseId");
			String viewType = request.getParameter("show");
			VU360User vu360UserModel = new VU360User();
			vu360UserModel.setFirstName(user.getFirstName());
			vu360UserModel.setLastName(user.getLastName());
			Map<Object, Object> context = enrollmentService.displayCourseSampleCompletionReport(vu360UserModel, courseId, viewType);

			return new ModelAndView( catalogCourseCompletionReportTemplate, STR_CONTEXT, context);

		}
		catch (Exception e) {
			log.debug(e.getMessage(), e);
		}

		return new ModelAndView(loginTemplate);
	}

	public ModelAndView testingMyCoursesController(HttpServletRequest request,HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId=" + user.getLearner());

			String courseId = request.getParameter("courseId");
			
			// -- start 
			//LMS-15922, LMS-15921 - get course approval by enrollment id
			long objEnrollmentId = 0;
			try{
				if(request.getParameter("learnerEnrollmentId")!=null && StringUtils.isNotBlank(request.getParameter("learnerEnrollmentId"))){
					objEnrollmentId = Long.parseLong(request.getParameter("learnerEnrollmentId"));
				}
			}catch(Exception subException){
				log.debug("exception", subException);
			}
			
			CourseApproval objCourseApproval = null;
			
			Long valApprovalId = statsService.getLearnerSelectedCourseApprovalByEnrollmentId(objEnrollmentId);
			if(valApprovalId!=null && valApprovalId>0){
				 objCourseApproval = accreditationService.getCourseApprovalById(valApprovalId);
			}		
			// -- end
			
			
			// construct a context for the velocity to use.

			/*
			 * Can a course description be available to a learner who is not entitled to get it?
			 */
			Map<Object, Object> context = new HashMap<Object, Object>();
			Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(courseId));

			context.put("course", course);
			context.put("userName", user.getFirstName() + " " + user.getLastName());

			// -- start 
			//LMS-15922, LMS-15921 - setting values in 'approval number' and in 'credit hour' as defined into given ticket number
			if(objCourseApproval!=null && objCourseApproval.getCourseApprovalNumber()!=null && StringUtils.isNotBlank(objCourseApproval.getCourseApprovalNumber()))
				context.put("courseApprovalNumber",  objCourseApproval.getCourseApprovalNumber());
			else
				context.put("courseApprovalNumber",  course.getApprovalNumber());
			 
			
			try{
				if(objCourseApproval!=null && objCourseApproval.getApprovedCreditHours()!=null && StringUtils.isNotBlank(objCourseApproval.getApprovedCreditHours())){
					
					if(Double.parseDouble(objCourseApproval.getApprovedCreditHours())>0)
						context.put("aprovedCreditHours",  objCourseApproval.getApprovedCreditHours());
					else if(course.getCredithour()!=null && StringUtils.isNotBlank(course.getCredithour()) && Double.parseDouble(course.getCredithour())>0) 
						context.put("aprovedCreditHours",  course.getCredithour());
					else
						context.put("aprovedCreditHours",  course.getCeus());
					
				}else{
					if(course.getCredithour()!=null && StringUtils.isNotBlank(course.getCredithour()) && Double.parseDouble(course.getCredithour())>0) 
						context.put("aprovedCreditHours",  course.getCredithour());
					else
						context.put("aprovedCreditHours",  course.getCeus());
				}
			}catch(Exception subException){
				log.debug("exception", subException);
			}
			//-- end
			return new ModelAndView(courseDescriptionTemplate, STR_CONTEXT,context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(courseDescriptionTemplate);
	}
	
	
	public ModelAndView displayCourseDescription(HttpServletRequest request,HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId=" + user.getLearner());

			String courseId = request.getParameter("courseId");
			/*
			 * Can a course description be available to a learner who is not entitled to get it?
			 */
			Map<Object, Object> context = new HashMap<>();
			Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(courseId));

			CourseApproval objCourseApproval = accreditationService.getCourseApprovalByCourse(course);
			
			if(objCourseApproval!=null && objCourseApproval.getApprovedCreditHours()!=null && StringUtils.isNotBlank(objCourseApproval.getApprovedCreditHours())
					&& Double.parseDouble(objCourseApproval.getApprovedCreditHours())>0){
				
				context.put("CourseApprovalCreditHours",  objCourseApproval.getApprovedCreditHours());
			}
			context.put("course", course);
			if (user != null){
				context.put("userName", user.getFirstName() + " " + user.getLastName());
			}
			

			return new ModelAndView(courseDescriptionTemplate, STR_CONTEXT,context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(courseDescriptionTemplate);
	}

	/**
	 * @author Adeel Hussain
	 * @Description : Simple method to show the description of non-enrolled courses 
	 */	
	public ModelAndView displayCourseSampleDescription(HttpServletRequest request,HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId="
				+ user.getLearner());

			//Learner learner = user.getLearner();
			String courseId = request.getParameter("courseId");
			String viewType = request.getParameter("show");
			// construct a context for the velocity to use.

			/*
			 * Can a course description be available to a learner who is not entitled to get it?
			 */
			Map<Object, Object> context = new HashMap<Object, Object>();
			Course course = courseAndCourseGroupService.getCourseById(Long
					.valueOf(courseId));


			context.put("course", course);
			context.put("viewType", viewType);
			context.put("userName", user.getFirstName() + " "
					+ user.getLastName());

			return new ModelAndView(catalogCourseDescriptionTemplate, STR_CONTEXT,
					context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(catalogCourseDescriptionTemplate);
	}

	/**
	 * @author Adeel Hussain
	 * @Description : Simple method to show course detail of non-enrolled courses from the course catalog 
	 */	
	public ModelAndView displayCourseSampleDetails(HttpServletRequest request,HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId="+ user.getLearner());

			String courseId = request.getParameter("courseId");
			String viewType=request.getParameter("show");
			VU360User vu360UserModel = new VU360User();
			vu360UserModel.setFirstName(user.getFirstName());
			vu360UserModel.setLastName(user.getLastName());
			Map<Object, Object> context = enrollmentService.displayCourseSampleDetails(vu360UserModel, courseId, viewType);

			return new ModelAndView(catalogCourseDetailsTemplate, STR_CONTEXT,context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(catalogCourseDetailsTemplate);
	}

	//by OWS to display Enrollment Page for Sync Courses
	public ModelAndView displayScheduleToEnroll(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
			log.debug(user == null ? "User null" : " learnerId="+ user.getLearner());
			String viewType=request.getParameter("show");
			Long courseId = Long.parseLong( request.getParameter("courseId") );
			
			Map<Object, Object> context = enrollmentService.displayScheduleToEnroll(vu360UserService.getUserById(user.getId()), brand, courseId, viewType);
			

			return new ModelAndView(scheduleToEnrollTemplate, STR_CONTEXT,
					context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(viewScheduleTemplate);
	}

	public ModelAndView displayAssessmentResults(HttpServletRequest request,
			HttpServletResponse response){
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			String enrollmentId=request.getParameter("learnerEnrollmentId");
			String viewAssessmentResultsURL = VU360Properties.getVU360Property("lcms.viewAssessmentResultsURL"); 
			
			LearnerEnrollment le = enrollmentService.loadForUpdateLearnerEnrollment(Long.valueOf(enrollmentId));
			Course course = courseAndCourseGroupService.getCourseById(le.getCourse().getId());
			
			context.put("enrollmentId", enrollmentId);
			context.put("viewType", "enrolled");
			context.put("learnerName", user.getFirstName() +" "+ user.getLastName());
			context.put("courseTitle", course.getCourseTitle());
			context.put("viewAssessmentResultsURL",viewAssessmentResultsURL+enrollmentId);
			
			
			return new ModelAndView(viewAssessmentTemplate, STR_CONTEXT, context);

		} catch (Exception e) {
			log.debug("exception", e);
		}

		return new ModelAndView(viewAssessmentTemplate);
	}
	
	public ModelAndView displaySyncCourseDetailsPage(HttpServletRequest request,
			HttpServletResponse response) {
		log.debug("1- displayCourseDetailsPage freeMemory:"+Runtime.getRuntime().freeMemory());

		try {

			return new ModelAndView(syncCourseDetailsTemplate, STR_CONTEXT, null);

		} catch (Exception e) {
			log.debug("exception", e);
		}

		return new ModelAndView(syncCourseDetailsTemplate);
	}

	//by OWS
	public ModelAndView displayViewSchedule(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId="
				+ user.getLearner());
			Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
			
			String courseId = request.getParameter("courseId");
			String learnerEnrollmentID = request.getParameter("learnerEnrollmentId");
			
			
			try{
				courseId=Long.parseLong(courseId)+"";

			}
			catch(Exception e){
				courseId=courseAndCourseGroupService.getCourseByGUID(courseId).getId().toString();
			}
			
			Map<Object, Object> context = enrollmentService.displayViewSchedule(vu360UserService.getUserById(user.getId()), brand, courseId, learnerEnrollmentID);
			
			String viewType = request.getParameter("show")!=null ? request.getParameter("show") : "";
			String search= request.getParameter(STR_SEARCH)!=null ? request.getParameter(STR_SEARCH) : "";
			
			context.put("show", viewType);
			context.put(STR_SEARCH, search);
			
			return new ModelAndView(viewScheduleTemplate, STR_CONTEXT,
					context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(viewScheduleTemplate);
	}
	
	
	public ModelAndView getCoursesByCourseGroups(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			log.debug(user == null ? "User null" : " learnerId="+ user.getLearner());
			
			String courseGroupId = request.getParameter("courseGroupId");
			String trainingPlan = request.getParameter("isTrainingPlan");
			String miscellaneous = request.getParameter("isMiscellaneous");
			log.debug(courseGroupId);
			log.debug(trainingPlan);
			log.debug(miscellaneous);
			
			boolean isTrainingPlan = false;
			if(Integer.valueOf(trainingPlan) == 1)
				isTrainingPlan = true;
			
			boolean isMiscellaneous = false;
			if(Integer.valueOf(miscellaneous) == 1)
				isMiscellaneous = true;
			
			JSONObject courseViewJSON = new JSONObject();

			Map<Object,Object> map = new HashMap<Object,Object>();
			List<CourseView> lstCourseViews = new ArrayList<CourseView>();
			JSONArray jsonArray = null;
			try{
				Learner learnerModel = new Learner();
				learnerModel.setId(user.getLearner().getId());
				lstCourseViews = courseAndCourseGroupService.getEntitlementsByCourseGroupId(learnerModel, Long.valueOf(courseGroupId), isTrainingPlan, isMiscellaneous);
				
	            jsonArray = createCourseJSONArray(lstCourseViews);
	            if( jsonArray.size()>0 ) {
	            	courseViewJSON.put("courseArray", jsonArray);
	            }
			}catch(Exception e){
                logger.fatal("Exception occurred.", e);
                courseViewJSON.put("error", e.toString());
			}
			
			PrintWriter pw = response.getWriter();
			response.setHeader("Content-Type", "application/json");
			pw.write(jsonArray.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			log.debug("IOException", e);
		}  catch (IllegalStateException e) {
			log.debug("IllegalStateException", e);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return null;
	}
	
	private JSONObject createCourseJSONObject(CourseView courseView) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("COURSEGROUPNAME", courseView.getGroupName());
        jsonObject.put("name", courseView.getName());
        jsonObject.put("COURSECODE", courseView.getCode());
        jsonObject.put("type", courseView.getType());
        if(courseView.getEnrollmentId()!=null && courseView.getEnrollmentId()>0)
        	jsonObject.put("enrollmentId", String.valueOf(courseView.getEnrollmentId()));
        else
        	jsonObject.put("enrollmentId", "0");
        jsonObject.put("entitlementId", courseView.getEntitlementId());
        jsonObject.put("courseId", courseView.getCourseId()); 
        jsonObject.put("EnrollmentStatus", courseView.getEnrollmentStatus());
        
        jsonObject.put("COURSE_STATUS", courseView.getCourseStatus()); 
        jsonObject.put("courseHours", courseView.getCourseHours());
        jsonObject.put("duration", courseView.getDuration());

        return jsonObject;
    }
    
    private JSONArray createCourseJSONArray(List<CourseView> lstCourseViews){
        JSONArray jsonArray = new JSONArray();
        for (CourseView courseView : lstCourseViews) {
            jsonArray.add(createCourseJSONObject(courseView));
        }
        return jsonArray;
    }
	

	/**
	 * @return the myCoursesTemplate
	 */
	public String getMyCoursesTemplate() {
		return myCoursesTemplate;
	}

	/**
	 * @param myCoursesTemplate
	 *            the myCoursesTemplate to set
	 */
	public void setMyCoursesTemplate(String myCoursesTemplate) {
		this.myCoursesTemplate = myCoursesTemplate;
	}

	/**
	 * @return the courseDetailsTemplate
	 */
	public String getCourseDetailsTemplate() {
		return courseDetailsTemplate;
	}

	/**
	 * @param courseDetailsTemplate
	 *            the courseDetailsTemplate to set
	 */
	public void setCourseDetailsTemplate(String courseDetailsTemplate) {
		this.courseDetailsTemplate = courseDetailsTemplate;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}

	/**
	 * @return the courseDescriptionTemplate
	 */
	public String getCourseDescriptionTemplate() {
		return courseDescriptionTemplate;
	}

	/**
	 * @param courseDescriptionTemplate
	 *            the courseDescriptionTemplate to set
	 */
	public void setCourseDescriptionTemplate(String courseDescriptionTemplate) {
		this.courseDescriptionTemplate = courseDescriptionTemplate;
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
	 * @return the courseService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	/**
	 * @param courseService the courseService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	/**
	 * @return the launchCourseService
	 */
	public LaunchCourseService getLaunchCourseService() {
		return launchCourseService;
	}

	/**
	 * 
	 * @return
	 */
	public String getLaunchTemplate() {
		return launchTemplate;
	}

	/**
	 * 
	 * @param launchTemplate
	 */
	public void setLaunchTemplate(String launchTemplate) {
		this.launchTemplate = launchTemplate;
	}

	/**
	 * @param launchCourseService the launchCourseService to set
	 */
	public void setLaunchCourseService(LaunchCourseService launchCourseService) {
		this.launchCourseService = launchCourseService;
	}

	public String getLoginTemplate() {
		return loginTemplate;
	}

	public void setLoginTemplate(String loginTemplate) {
		this.loginTemplate = loginTemplate;
	}

	/**
	 * @return the courseCompletionReportTemplate
	 */
	public String getCourseCompletionReportTemplate() {
		return courseCompletionReportTemplate;
	}

	/**
	 * @param courseCompletionReportTemplate the courseCompletionReportTemplate to set
	 */
	public void setCourseCompletionReportTemplate(
			String courseCompletionReportTemplate) {
		this.courseCompletionReportTemplate = courseCompletionReportTemplate;
	}

	/**
	 * @return the mySurveysTemplate
	 */
	public String getMySurveysTemplate() {
		return mySurveysTemplate;
	}

	/**
	 * @param mySurveysTemplate the mySurveysTemplate to set
	 */
	public void setMySurveysTemplate(String mySurveysTemplate) {
		this.mySurveysTemplate = mySurveysTemplate;
	}

	/**
	 * @return the catalogCourseDetailsTemplate
	 */
	public String getCatalogCourseDetailsTemplate() {
		return catalogCourseDetailsTemplate;
	}

	/**
	 * @param catalogCourseDetailsTemplate the catalogCourseDetailsTemplate to set
	 */
	public void setCatalogCourseDetailsTemplate(String catalogCourseDetailsTemplate) {
		this.catalogCourseDetailsTemplate = catalogCourseDetailsTemplate;
	}

	/**
	 * @return the catalogCourseDescriptionTemplate
	 */
	public String getCatalogCourseDescriptionTemplate() {
		return catalogCourseDescriptionTemplate;
	}

	/**
	 * @param catalogCourseDescriptionTemplate the catalogCourseDescriptionTemplate to set
	 */
	public void setCatalogCourseDescriptionTemplate(
			String catalogCourseDescriptionTemplate) {
		this.catalogCourseDescriptionTemplate = catalogCourseDescriptionTemplate;
	}

	/**
	 * @return the catalogCourseCompletionReportTemplate
	 */
	public String getCatalogCourseCompletionReportTemplate() {
		return catalogCourseCompletionReportTemplate;
	}

	/**
	 * @param catalogCourseCompletionReportTemplate the catalogCourseCompletionReportTemplate to set
	 */
	public void setCatalogCourseCompletionReportTemplate(
			String catalogCourseCompletionReportTemplate) {
		this.catalogCourseCompletionReportTemplate = catalogCourseCompletionReportTemplate;
	}
	public String getViewScheduleTemplate() {
		return viewScheduleTemplate;
	}

	public void setViewScheduleTemplate(String viewScheduleTemplate) {
		this.viewScheduleTemplate = viewScheduleTemplate;
	}
	public String getSyncCourseDetailsTemplate() {
		return syncCourseDetailsTemplate;
	}

	public void setSyncCourseDetailsTemplate(String syncCourseDetailsTemplate) {
		this.syncCourseDetailsTemplate = syncCourseDetailsTemplate;
	}
	public String getScheduleToEnrollTemplate() {
		return scheduleToEnrollTemplate;
	}

	public void setScheduleToEnrollTemplate(String scheduleToEnrollTemplate) {
		this.scheduleToEnrollTemplate = scheduleToEnrollTemplate;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	
	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}

	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}

	/**
	 * @param enrollmentService the enrollmentService to set
	 */
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	/**
	 * @return the enrollmentService
	 */
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

    private JSONObject createJSONObject(SynchronousClass syncClass) {
    	String strClassStatus="Active";
    	
    	if(!synchronousClassService.isSyncClassAssignable(syncClass, 1)){
    		strClassStatus="Inactive";
    	}
    	
    	Map<Object, Object> result = synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId(),0,10, "startDateTime", 0);
    	List<SynchronousSession> synchSessions = (List<SynchronousSession>) result.get("synchronousSessionList");
    	
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("courseTitle", syncClass.getCourse().getCourseTitle());
        jsonObject.put("courseStatus", strClassStatus);
        jsonObject.put("currentlyInSession", syncClass.isCurrentlyInSession());
        jsonObject.put("courseMeetingType", syncClass.getMeetingType());
        jsonObject.put("courseClassStartDate", syncClass.getClassStartDate());
        jsonObject.put("courseClassEndDate", syncClass.getClassEndDate());
        jsonObject.put("courseClassStartTime", synchSessions.get(0).getStartTime()); 
        jsonObject.put("courseClassEndTime", synchSessions.get(synchSessions.size()-1).getEndTime());

        return jsonObject;
    }
    
    private JSONArray createJSONArray(List<SynchronousClass> syncClasses){
        JSONArray jsonArray = new JSONArray();
        for (SynchronousClass syncClass : syncClasses) {
            jsonArray.add(createJSONObject(syncClass));
        }
        
        return jsonArray;
    }
    
    public ModelAndView getCourseDescription(HttpServletRequest request, HttpServletResponse response){   	
 	   Course course = courseAndCourseGroupService.getCourseByGUID(request.getParameter("courseId")); 	   
   	   return new ModelAndView(courseDescriptionAjaxTemplate, STR_CONTEXT, course.getDescription()); 
    }

 	public String getCourseDescriptionAjaxTemplate() {
 		return courseDescriptionAjaxTemplate;
 	}

 	public void setCourseDescriptionAjaxTemplate(
 			String courseDescriptionAjaxTemplate) {
 		this.courseDescriptionAjaxTemplate = courseDescriptionAjaxTemplate;
 	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	public String getViewAssessmentTemplate() {
		return viewAssessmentTemplate;
	}

	public void setViewAssessmentTemplate(String viewAssessmentTemplate) {
		this.viewAssessmentTemplate = viewAssessmentTemplate;
	}

}