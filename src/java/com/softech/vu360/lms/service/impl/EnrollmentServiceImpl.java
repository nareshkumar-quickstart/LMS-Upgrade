package com.softech.vu360.lms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.AICCCourse;
import com.softech.vu360.lms.model.AssessmentConfiguration;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfiguration;
import com.softech.vu360.lms.model.CourseConfigurationTemplate;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAssignmentActivity;
import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerFinalCourseActivity;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmission;
import com.softech.vu360.lms.model.LearnerLectureCourseActivity;
import com.softech.vu360.lms.model.LearnerSelfStudyCourseActivity;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.LegacyCourse;
import com.softech.vu360.lms.model.LockedCourse;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.ScormCourse;
import com.softech.vu360.lms.model.SelfPacedCourse;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.model.SubscriptionCourse;
import com.softech.vu360.lms.model.SubscriptionCustomerEntitlement;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.repositories.CourseConfigurationTemplateRepository;
import com.softech.vu360.lms.repositories.LearnerCourseStatisticsRepository;
import com.softech.vu360.lms.repositories.LearnerEnrollmentRepository;
import com.softech.vu360.lms.repositories.LearnerGroupItemRepository;
import com.softech.vu360.lms.repositories.LearningSessionRepository;
import com.softech.vu360.lms.repositories.LockedCourseRepository;
import com.softech.vu360.lms.repositories.TimeZoneRepository;
import com.softech.vu360.lms.repositories.VU360UserRepository;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerHomeworkAssignmentSubmissionService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SubscriptionService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.ORMUtils;
import com.softech.vu360.lms.vo.EnrollmentVO;
import com.softech.vu360.lms.vo.MarketoPacketSerialized;
import com.softech.vu360.lms.vo.SaveEnrollmentParam;
import com.softech.vu360.lms.web.controller.model.AvailableCoursesTree;
import com.softech.vu360.lms.web.controller.model.CourseGroupView;
import com.softech.vu360.lms.web.controller.model.CourseView;
import com.softech.vu360.lms.web.controller.model.MyCoursesComparator;
import com.softech.vu360.lms.web.controller.model.MyCoursesCourseGroup;
import com.softech.vu360.lms.web.controller.model.MyCoursesItem;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.EnrollLearnerAsyncTask;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.VU360Properties;

import net.sf.json.JSONObject;

public class EnrollmentServiceImpl implements EnrollmentService {

    public static final String RECENT_MYCOURSES_VIEW = "recent";
    public static final String ENROLLED_MYCOURSES_VIEW = "enrolled";
    public static final String COURSECATALOG_MYCOURSES_VIEW = "coursecatalog";
    public static final String SURVEY_MYCOURSES_VIEW = "survey";
    public static final String COMPLETED_MYCOURSES_VIEW = "completedCourses";
    public static final String EXPIRED_MYCOURSES_VIEW = "expiredCourses";
    public static final String COURSES_ABOUT_TO_EXPIRED_MYCOURSES_VIEW = "coursesAboutToExpire";
    public static final String MISSING_REPORTING_FIELDS_MESSAGE = "Reporting field missing";
    public static final String COURSE_STARTED = "Course_Started";
    public static final String COURSE_ENROLLED = "Course_Enrolled";
    public static final String COURSE_COMPLETED = "Course_Completed";

    private static final Logger log = Logger.getLogger(EnrollmentServiceImpl.class.getName());

    @Inject
    private LearnerEnrollmentRepository learnerEnrollmentRepository;
	@Inject
    private LearningSessionRepository learningSessionRepository;
    @Inject
    private LearnerCourseStatisticsRepository learnerCourseStatisticsRepository;
    @Inject
	private LockedCourseRepository lockedCourseRepository;
	@Inject
	private VU360UserRepository vu360UserRepository;
	@Inject
	private CourseConfigurationTemplateRepository courseConfigurationTemplateRepository;
	@Inject
	private LearnerGroupItemRepository learnerGroupItemRepository;
	@Inject
	private TimeZoneRepository timeZoneRepository;

    private EntitlementService entitlementService;
    private LearnerService learnerService;
    private SynchronousClassService synchronousClassService;
    private CourseAndCourseGroupService courseAndCourseGroupService;
    private TrainingPlanService trainingPlanService;
    private LearnerHomeworkAssignmentSubmissionService learnerHomeworkAssignmentSubmissionService;
    private VelocityEngine velocityEngine;
    private LearnersToBeMailedService learnersToBeMailedService;
    private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
    private StatisticsService statsService;
    private AccreditationService accreditationService;
    private VU360UserService userService;
    private SubscriptionService subscriptionService;
    private JmsTemplate marketoJMSTemplate = null;
    private boolean isMarketoEnabled = false;
    
	public EnrollmentServiceImpl() {

		super();
		isMarketoEnabled = Boolean.parseBoolean((String) VU360Properties.getVU360Property("marketo.enabled"));

	}
    
    public List<LearnerEnrollment> addEnrollments(List<LearnerEnrollment> enrollments) {
		List<LearnerEnrollment> les = new ArrayList<LearnerEnrollment>();
		for (LearnerEnrollment learnerEnrollment : enrollments) {
		    les.add(addEnrollment(learnerEnrollment.getLearner(), learnerEnrollment.getCustomerEntitlement(),
			    learnerEnrollment.getOrgGroupEntitlement(), learnerEnrollment));
		}
		return les;
    }

    public List<LearnerEnrollment> addEnrollmentsForCourseEntitlements(Learner learner, CourseCustomerEntitlement custEntitlement, String subItem) {
		List<LearnerEnrollment> enrollments = new ArrayList<LearnerEnrollment>();
		// make no assumptions about where this entitlement came from
		CustomerEntitlement ce = entitlementService.getCustomerEntitlementById(custEntitlement.getId());
		if (ce.hasAvailableSeats(1)) {
		    // enroll them in anything we can find in the entitlement!
		    Set<Course> coursesToEnroll = entitlementService.getCoursesByCourseCustomerEntitlement(custEntitlement);// ce.getUniqueCourses();
		    // LMS-6747
		    // now iterate over all courses and generate a learner enrollment
		    // for it and enroll the learner
		    Date today = new Date(System.currentTimeMillis());
		    for (Course c : coursesToEnroll) {
		    	LearnerEnrollment le = new LearnerEnrollment();
				le.setCourse(c);
				le.setEnrollmentStartDate(today);
				le.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
				if (ce.getEndDate() == null) {
				    le.setEnrollmentEndDate(getEndDateOffset(today, ce.getDefaultTermOfServiceInDays()));
				} else {
				    le.setEnrollmentEndDate(ce.getEndDate());
				}
				le.setCustomerEntitlement(ce);
				le.setLearner(learner);
		
				// For Sync Course
				if ((c.getCourseType().equals(SynchronousCourse.COURSE_TYPE) || c.getCourseType().equals(WebinarCourse.COURSE_TYPE)) 
						&& !subItem.isEmpty()) {
					log.info("This is Synchronous Course");
					SynchronousClass syncClass = null;
					if(subItem.equals("-1")) {
						log.info("LineItemGuid is -1, which means enroll user into very first class created for this course");
						List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassByCourseId(c.getId());
						if(syncClasses != null && syncClasses.size()>0) {
							syncClass = syncClasses.get(0);
							log.info("enrolled into:" + syncClass.getSectionName() + "("+ syncClass.getId() +")");
						}
					} else {
						syncClass = synchronousClassService.getSynchronousClassByGUID(subItem);
					}
				    if (syncClass != null) {
				    	le.setSynchronousClass(syncClass);
				    }
				}
				if (c.getCourseType().equals(InstructorConnectCourse.COURSE_TYPE) && !subItem.equals("")) {
				    SynchronousClass syncClass = synchronousClassService.getSynchronousClassByGUID(subItem);
				    if (syncClass != null) {
				    	le.setSynchronousClass(syncClass);
				    }
				}
				// add it to the db
				le = addEnrollment(learner, ce, null, le);
				enrollments.add(le);
		    }
		}
		return enrollments;
    }

    public LearnerEnrollment addEnrollment(Learner learner, CustomerEntitlement origCustEntitlment, OrgGroupEntitlement orgGrpEntl,
	    LearnerEnrollment le) {

		try {
		    Date today = new Date(System.currentTimeMillis());
		    // business logic of adding an enrollment to the system
		    // CustomerEntitlement origCustEntitlment =
		    // entitlementService.getCustomerEntitlementById(custEnt.getId());
		    Learner origLearner = learnerService.getLearnerByID(learner.getId());
		    LearnerCourseStatistics courseStats = new LearnerCourseStatistics();
		    courseStats.setStatus(LearnerCourseStatistics.NOT_STARTED);
		    le.setLearner(origLearner);
		    origCustEntitlment.setNumberSeatsUsed(origCustEntitlment.getNumberSeatsUsed() + 1);
		    le.setCustomerEntitlement(origCustEntitlment);
		    le.setCourseStatistics(courseStats);
		    courseStats.setLearnerEnrollment(le);
		    le.setEnrollmentDate(today);
	
		    // [10/13/2010] LMS-7196 :: Update Org. Group Contract Seats Used on
		    // Learner Enrollment
		    if (orgGrpEntl != null) {
		    	le.setOrgGroupEntitlement(orgGrpEntl);
		    }
	
		    LearnerEnrollment learnerEnrollment = learnerEnrollmentRepository.save(le);
	
		    // now update the customer entitlement object
		    // origCustEntitlment.setNumberSeatsUsed(origCustEntitlment.getNumberSeatsUsed()+1);
		     entitlementService.saveCustomerEntitlement(origCustEntitlment,null);
	
		    // if ( orgGrpEntl != null ) {
		    // OrgGroupEntitlement origOrgGroupEntitlement =
		    // entitlementService.getOrgGroupEntitlementById(orgGrpEntl.getId());
		    // origOrgGroupEntitlement.setNumberSeatsUsed(origOrgGroupEntitlement.getNumberSeatsUsed()+1);
		    // entitlementService.saveOrgGroupEntitlement(origOrgGroupEntitlement);
		    // }
	
		     //marketoPacket(learnerEnrollment,COURSE_ENROLLED);
		    return learnerEnrollment;
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return null;
    }

    
    public LearnerEnrollment addSelfEnrollmentForSubscription(Learner learner, CustomerEntitlement origCustEntitlment, LearnerEnrollment le) {

    		try {
    		    Date today = new Date(System.currentTimeMillis());
    		    LearnerCourseStatistics courseStats = new LearnerCourseStatistics();
    		    courseStats.setStatus(LearnerCourseStatistics.NOT_STARTED);
    		    le.setLearner(learner);
    		    le.setCustomerEntitlement(origCustEntitlment);
    		    le.setCourseStatistics(courseStats);
    		    courseStats.setLearnerEnrollment(le);
    		    le.setEnrollmentDate(today);
    		    LearnerEnrollment learnerEnrollment = learnerEnrollmentRepository.save(le);
    		    return learnerEnrollment;
    		} catch (Exception ex) {
    		    log.error(ex.getMessage(), ex);
    		}
    		return null;
        }
    //Function calling is commented as this function breaks on Toplink because of some property that doesnot exists in entity
    public Map<Object, Object> getAllCoursesNotEnrolledByLearner(long courseIdArray[], Customer customer) {

	Map<Object, Object> results = new HashMap<Object, Object>();

	//results = enrollmentDAO.getAllCoursesNotEnrolledByLearner(courseIdArray, customer);

	return results;

    }

  //Function calling is commented as this function breaks on Toplink because of some property that doesnot exists in entity
    public Map<Object, Object> getAllCoursesNotEnrolledByLearner(long courseIdArray[], Customer customer, String courseName, String courseGUID,
	    String keywords, int pageIndex, int pageSize, String sortBy, int sortDirection) {

	Map<Object, Object> results = new HashMap<Object, Object>();

//	results = enrollmentDAO.getAllCoursesNotEnrolledByLearner(courseIdArray, customer, courseName, courseGUID, keywords, pageIndex, pageSize,
//		sortBy, sortDirection);

	return results;

    }

    /**
     * [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing courses
     * irrespective of contract and enrollments availability [02/09/2011]
     * LMS-8577 :: For Swap Courses, Number seats used is not correctly
     * incrementing
     */
    @Override
    public String swapCourse(Learner learner, Long swappedEnrolementId, Long courseId, Long customerContractId, SynchronousClass synchronousClass) {
		try {
		    boolean swapEnrollment = true;
		    // Get Existing Enrollment and update its status as swapped.
		    LearnerEnrollment swappedEnrollment = this.learnerEnrollmentRepository.findOne(swappedEnrolementId); //this.enrollmentDAO.loadForUpdateLearnerEnrollment(swappedEnrolementId);
	
		    if (swappedEnrollment.getCustomerEntitlement().getId().equals(customerContractId)
			    && swappedEnrollment.getCourse().getId().equals(courseId)) {
				swapEnrollment = false;
				// Check for Synchronous Course, classes could be different.
				if (synchronousClass != null && swappedEnrollment.getSynchronousClass() != null) {
				    swapEnrollment = true;
				    if (swappedEnrollment.getSynchronousClass().equals(synchronousClass)) {
				    	swapEnrollment = false;
				    }
				}
		    }
	
		    if (swapEnrollment) {
				CustomerEntitlement contract = this.entitlementService.getCustomerEntitlementById(customerContractId);
				OrgGroupEntitlement orgGroupContract = this.entitlementService.getMaxAvaiableOrgGroupEntitlementByLearner(learner, contract.getId());
				if (orgGroupContract != null) {
				    if (!orgGroupContract.hasAvailableSeats(1)) {
					orgGroupContract = null;
				    }
				}
	
				// Create New Enrollment
				LearnerEnrollment newEnrollment = new LearnerEnrollment();
				newEnrollment.setLearner(learner);
				newEnrollment.setEnrollmentDate(Calendar.getInstance().getTime());
				newEnrollment.setEnrollmentStartDate(FormUtil.formatToDayStart(newEnrollment.getEnrollmentDate())); // [2/22/2011]
				// LMS-9042 Enrollment Start and End dates should have proper time
				newEnrollment.setEnrollmentEndDate(swappedEnrollment.getEnrollmentEndDate());
				newEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
				newEnrollment.setCourse(this.courseAndCourseGroupService.getCourseById(courseId));
				if (synchronousClass != null) {
				    newEnrollment.setSynchronousClass(synchronousClass);
				}
		
				if (!this.isValidEnrollment(newEnrollment)) {
				    return newEnrollment.getCourse().getCourseTitle() + " - Active enrollment already exists, cannot swap.";
				}
	
				// Create New Enrollment
				newEnrollment = this.addEnrollment(newEnrollment, contract, orgGroupContract);
		
				// Update Swapped Enrollment
				swappedEnrollment.setEnrollmentStatus(LearnerEnrollment.SWAPPED);
				//this.enrollmentDAO.updateLearnerEnrollment(swappedEnrollment);
				this.learnerEnrollmentRepository.save(swappedEnrollment);
		
				// Decrement seats used for previous contract.
				this.entitlementService.updateSeatsUsed(swappedEnrollment.getCustomerEntitlement(), swappedEnrollment.getOrgGroupEntitlement(), -1);
		    }
		} catch (Exception e) {
		    log.error(e);
		    return "Error occurred while swapping the course. Please try again later.";
		}
		return null;
    }

    /**
     * Method to Validate Enrollment specially to check if any Active Enrollment
     * exists for this Learner on this Course [02/09/2011] LMS-8577 :: For Swap
     * Courses, Number seats used is not correctly incrementing
     */
    public boolean isValidEnrollment(LearnerEnrollment learnerEnrollment) {
    	List<LearnerEnrollment> duplicateEnrollmentList = this.getActiveDuplicateEnrollments(learnerEnrollment.getLearner().getId(),
		learnerEnrollment.getCourse().getId(), learnerEnrollment.getEnrollmentStartDate());
		return ((duplicateEnrollmentList != null && duplicateEnrollmentList.size() > 0) ? false : true);
    }

    private List<LearnerEnrollment> getActiveDuplicateEnrollments(Long learnerId, Long courseId, Date newEnrollmentStartDate) {
		List<LearnerEnrollment> duplicateEnrollmentList = new ArrayList<LearnerEnrollment>();
		List<LearnerEnrollment> enrollmentList = this.learnerEnrollmentRepository.findByLearnerIdAndCourseIdAndEnrollmentStatusAndEnrollmentEndDateNotNullAndEnrollmentEndDateAfter(learnerId, courseId,LearnerEnrollment.ACTIVE,newEnrollmentStartDate);
		if (enrollmentList != null && enrollmentList.size() > 0) {
			duplicateEnrollmentList = enrollmentList.stream().filter(p -> p.getCourseStatistics().isCourseCompleted().equals(Boolean.FALSE)).collect(Collectors.toList());
//		    for (LearnerEnrollment enrollment : enrollmentList) {
//				//if (enrollment.getEnrollmentEndDate()!=null && enrollment.getEnrollmentEndDate().compareTo(newEnrollmentStartDate) > 0) {
//				    LearnerCourseStatistics courseStatistics = this.getLearnerCourseStatisticsBylearnerEnrollment(enrollment);
//				    if (/*!courseStatistics.isCompleted()*/ !courseStatistics.isCourseCompleted()) {
//					duplicateEnrollmentList.add(enrollment);
//				    }
//				//}
//		    }
		}
		return duplicateEnrollmentList;
    }

    /**
     * Create Complete Enrollment for a Learner and update Contract Seats
     * [02/09/2011] LMS-8577 :: For Swap Courses, Number seats used is not
     * correctly incrementing
     */
    public LearnerEnrollment addEnrollment(LearnerEnrollment learnerEnrollment, CustomerEntitlement customerContract,
	    OrgGroupEntitlement orgGroupContract) {

		try {
		    learnerEnrollment.setEnrollmentDate(new Date(System.currentTimeMillis()));
		    LearnerCourseStatistics courseStats = new LearnerCourseStatistics();
		    courseStats.setStatus(LearnerCourseStatistics.NOT_STARTED);
		    learnerEnrollment.setCourseStatistics(courseStats);
		    courseStats.setLearnerEnrollment(learnerEnrollment);
		    learnerEnrollment.setCustomerEntitlement(customerContract);
		    if (orgGroupContract != null) {
		    	learnerEnrollment.setOrgGroupEntitlement(orgGroupContract);
		    }
		    learnerEnrollment = learnerEnrollmentRepository.save(learnerEnrollment);
		    this.entitlementService.updateSeatsUsed(customerContract, orgGroupContract, 1);
		    marketoPacket(learnerEnrollment,COURSE_ENROLLED);
		    return learnerEnrollment;
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return null;
    }

  //Function calling is commented as this function breaks on Toplink because of some property that doesnot exists in entity
    public Map<Object, Object> getAllCoursesNotEnrolledByLearner(long courseIdArray[], Customer customer, String searchCriteria, int pageIndex,
	    int pageSize, String sortBy, int sortDirection) {

	Map<Object, Object> results = new HashMap<Object, Object>();

//	results = enrollmentDAO
//		.getAllCoursesNotEnrolledByLearner(courseIdArray, customer, searchCriteria, pageIndex, pageSize, sortBy, sortDirection);

	return results;

    }

  //Function calling is commented as this function breaks on Toplink because of some property that doesnot exists in entity
    public Map<Object, Object> getAllCoursesNotEnrolledByLearner(long courseIdArray[], Customer customer, String courseName, String courseGUID,
	    String keywords, String sortBy, int sortDirection) {
    	//enrollmentDAO.getAllCoursesNotEnrolledByLearner(courseIdArray, customer, courseName, courseGUID, keywords, sortBy, sortDirection);
	return null; 
    }

    public void setEnrollmentStatus(long enrolmentIdArray[], List<String> extendedDateList, String status) {
		//enrollmentDAO.setEnrollmentStatus(enrolmentIdArray, extendedDateList, status);
		List<LearnerEnrollment> learnerEnrollmentClones = this.learnerEnrollmentRepository.findByIdIn( ArrayUtils.toObject(enrolmentIdArray));
		Calendar calender=Calendar.getInstance();
		Date date=calender.getTime();
		
		for (int counter=0;counter<learnerEnrollmentClones.size(); counter++){
			if (status.equalsIgnoreCase(LearnerEnrollment.ACTIVE)){
				learnerEnrollmentClones.get(counter).setEnrollmentEndDate(FormUtil.formatToDayEnd(extendedDateList.get(counter)));
				learnerEnrollmentClones.get(counter).setEnrollmentStatus(LearnerEnrollment.ACTIVE);
			}else if (status.equalsIgnoreCase(LearnerEnrollment.EXPIRED)){
				learnerEnrollmentClones.get(counter).setEnrollmentEndDate(date);
				learnerEnrollmentClones.get(counter).setEnrollmentStatus(LearnerEnrollment.EXPIRED);
			}else if (status.equalsIgnoreCase(LearnerEnrollment.DROPPED)){
				if (learnerEnrollmentClones.get(counter).getOrgGroupEntitlement()!=null)
				{
					learnerEnrollmentClones.get(counter).getOrgGroupEntitlement()
						.setNumberSeatsUsed(learnerEnrollmentClones.get(counter).getOrgGroupEntitlement().getNumberSeatsUsed()-1);
					learnerEnrollmentClones.get(counter).getCustomerEntitlement()
					.setNumberSeatsUsed(learnerEnrollmentClones.get(counter).getCustomerEntitlement().getNumberSeatsUsed()-1);
					learnerEnrollmentClones.get(counter).setEnrollmentStatus(LearnerEnrollment.DROPPED);
				}else{
					learnerEnrollmentClones.get(counter).getCustomerEntitlement()
					.setNumberSeatsUsed(learnerEnrollmentClones.get(counter).getCustomerEntitlement().getNumberSeatsUsed()-1);
					learnerEnrollmentClones.get(counter).setEnrollmentStatus(LearnerEnrollment.DROPPED);
				}
			}
		}
		for (LearnerEnrollment le : learnerEnrollmentClones)
		{
			this.learnerEnrollmentRepository.save(le);
		}
	}


    // to unlock already Locked courses
    @Transactional
	public void setCourseStatus(Vector<Long> statusList, String status) {
		List<LockedCourse> lockedCourses = lockedCourseRepository.findByIdIn(statusList);
		for (LockedCourse lckCrs : lockedCourses) {
			lckCrs.setCourselocked(false);
		}
		lockedCourseRepository.save(lockedCourses);
	}

    public List<VU360User> getEnrolledLearnerInfoByClassId(long synchClassId) {
		List<LearnerEnrollment> learnerEnrollment= new ArrayList<LearnerEnrollment>();
		List<VU360User> synchClassLearnerEnrollment= new ArrayList<VU360User>();

		try {
			learnerEnrollment = learnerEnrollmentRepository.findBySynchronousClassId(synchClassId);
			Long learnerIdArray[]=new Long[learnerEnrollment.size()];
			for (int count=0;count<learnerEnrollment.size();count++)
			{
				learnerIdArray[count]=(Long.valueOf(learnerEnrollment.get(count).getLearner().getId()));
			}

			if (learnerEnrollment.size() > 0) {
				synchClassLearnerEnrollment = vu360UserRepository.findByLearnerIdIn(learnerIdArray);
			}
		} catch (ObjectRetrievalFailureException e){
				if (log.isDebugEnabled()) {
					log.debug("Learner enrolled not found");
				}
			}
		return synchClassLearnerEnrollment;
	}

    // helper method to get the end date for enrollments with a term of service days.
    private Date getEndDateOffset(Date startDate, int daysOffset) {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, daysOffset);
		return cal.getTime();
    }

    public List<LearnerEnrollment> getNumberofEnrollmentsOfThisCourse(Long courseId) {
		List<LearnerEnrollment> numberOfEnrollments = null;
		try {		 
			numberOfEnrollments = learnerEnrollmentRepository.findByCourseId(courseId);
			return numberOfEnrollments ;
		}catch(ObjectRetrievalFailureException e){
			log.error(e.getMessage(), e);
		}	
		return numberOfEnrollments;
	}

    public List<LearnerEnrollment> dropEnrollments(List<LearnerEnrollment> enrollments) {
		LearnerEnrollment updateableVersion = null;
		List<LearnerEnrollment> droped = new ArrayList<LearnerEnrollment>();
		for (LearnerEnrollment le : enrollments) {
		    updateableVersion =  this.learnerEnrollmentRepository.findOne(le.getId()); // enrollmentDAO.loadForUpdateLearnerEnrollment(le.getId());
		    updateableVersion.setEnrollmentStatus(LearnerEnrollment.DROPPED);
		    updateableVersion =  this.learnerEnrollmentRepository.save(updateableVersion); //enrollmentDAO.updateLearnerEnrollment(updateableVersion);
		    droped.add(updateableVersion);
		}
		return droped;
    }

    @Override
    public synchronized EnrollmentVO processEnrollments(SaveEnrollmentParam saveEnrollmentParam) {
		EnrollmentVO enrollmentVO = new EnrollmentVO();
		try {
		    // Get learners to be enrolled
		    List<Learner> learnerList = saveEnrollmentParam.getLearnersToBeEnrolled();
		    // Get Courses for enrollment
		    List<EnrollmentCourseView> courseEntitlementList = this.getSelectedCourses(saveEnrollmentParam);
		    Date enrollmentDate = Calendar.getInstance().getTime();
		    Date enrollmentStartDate = FormUtil.formatToDayStart(new Date());
		    Date enrollmentEndDate = FormUtil.formatToDayEnd(new Date());
		    if (saveEnrollmentParam.isModifyAllEntitlements()) {
				if (StringUtils.isNotBlank(saveEnrollmentParam.getStartDate())) {
				    enrollmentStartDate = FormUtil.formatToDayStart(saveEnrollmentParam.getStartDate());
				}
				if (enrollmentDate.after(enrollmentStartDate)) {
				    enrollmentStartDate = enrollmentDate;
				}
				if (StringUtils.isNotBlank(saveEnrollmentParam.getEndDate())) {
				    enrollmentEndDate = FormUtil.formatToDayEnd(saveEnrollmentParam.getEndDate());
				}
		    }
	
		    int newEnrollmentCount = 0;
		    for (EnrollmentCourseView courseEntitlement : courseEntitlementList) {
			CustomerEntitlement customerContract = this.entitlementService.getCustomerEntitlementById(courseEntitlement.getEntitlementId());
			Course course = this.courseAndCourseGroupService.getCourseById(courseEntitlement.getCourseId());
			SynchronousClass synchronousClass = null;
			if (course.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || course.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
			    synchronousClass = this.getSynchronousClass(courseEntitlement);
			    // LMS-15854 - Set the enrollment start date for SynchronousCourse to current date because ClassStartDate was allocating in start date, 
			    // because of this, courses with future start dates were not been visible to the enrolled user in available course in 
			    // learner mode and in manager mode, go to Users & Groups>Manage Enrollments.
			    enrollmentStartDate = new Date();
			    //enrollmentEndDate = synchronousClass.getClassEndDate();
			    if(customerContract.getEndDate()==null){
			    	//le.setEnrollmentEndDate(getEndDateOffset(today, ce.getDefaultTermOfServiceInDays()));
			    	enrollmentEndDate = getEndDateOffset(new Date(System.currentTimeMillis()), customerContract.getDefaultTermOfServiceInDays());
			    }else{
			    	enrollmentEndDate = customerContract.getEndDate();//LMS-15957
			    }
			} else if (course.getCourseType().equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
			    if(course instanceof InstructorConnectCourse)
			    {
			    	InstructorConnectCourse dbcourse = (InstructorConnectCourse) course;
	    			if(!dbcourse.getInstructorType().equals("Email Online"))
	    			{
			    	    synchronousClass = this.getSynchronousClass(courseEntitlement);
					    enrollmentStartDate = synchronousClass.getClassStartDate();
					    enrollmentEndDate = synchronousClass.getClassEndDate();
	    			}
			    }
			} else if (!saveEnrollmentParam.isModifyAllEntitlements()) {
			    if (StringUtils.isNotBlank(courseEntitlement.getEnrollmentStartDate())) {
			    	enrollmentStartDate = FormUtil.formatToDayStart(courseEntitlement.getEnrollmentStartDate());
			    }
			    if (enrollmentDate.after(enrollmentStartDate)) {
			    	enrollmentStartDate = enrollmentDate;
			    }
			    if (StringUtils.isNotBlank(courseEntitlement.getEnrollmentEndDate())) {
			    	enrollmentEndDate = FormUtil.formatToDayEnd(courseEntitlement.getEnrollmentEndDate());
			    } else if (courseEntitlement.getEntitlementEndDate() != null) {
			    	enrollmentEndDate = FormUtil.formatToDayEnd(courseEntitlement.getEntitlementEndDate());
			    } else if (courseEntitlement.getExpirationDate() != null) {
			    	enrollmentEndDate = FormUtil.formatToDayEnd(courseEntitlement.getExpirationDate());
			    }
			}
			for (Learner learner : learnerList) {
			    boolean isValidEnrollment = true;
			    // Validate if Sync. Class is assignable for each learner (as it includes class size validation)
			    if (course.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE)
			    		||course.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
			    	isValidEnrollment = this.synchronousClassService.isSyncClassAssignable(synchronousClass, 1);
			    } else if (course.getCourseType().equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
			    	 if(course instanceof InstructorConnectCourse){
				    	InstructorConnectCourse dbcourse = (InstructorConnectCourse) course;
		    			if(!dbcourse.getInstructorType().equals("Email Online")){
			               isValidEnrollment = this.synchronousClassService.isSyncClassAssignable(synchronousClass, 1);
		    			}else{
		    				isValidEnrollment = true;
			    		}
				    }
			    }
			    if (isValidEnrollment) {
				// Validate Enrollment if any Incomplete Active
				// Enrollment exists for same course
				List<LearnerEnrollment> duplicateEnrollments = this.getActiveDuplicateEnrollments(learner.getId(), course.getId(),
					enrollmentStartDate);
				if (duplicateEnrollments != null && duplicateEnrollments.size() > 0) { 
					// Work out as per Duplicate Enrollment option (Ignore/Update)
				    if (!saveEnrollmentParam.isDuplicates()) {
						// Update existing learner enrollment for same
						// course (Enrollment Start Date, Enrollment End
						// Date, Sync. Class ID)
						for (LearnerEnrollment enrollment : duplicateEnrollments) {
						    LearnerEnrollment learnerEnrollment = this.learnerEnrollmentRepository.findOne(enrollment.getId()); //this.enrollmentDAO.loadForUpdateLearnerEnrollment(enrollment.getId());
						    learnerEnrollment.setEnrollmentStartDate(enrollmentStartDate);
						    learnerEnrollment.setEnrollmentEndDate(enrollmentEndDate);
						    if (synchronousClass != null) {
						    	learnerEnrollment.setSynchronousClass(synchronousClass);
						    }
						    enrollment =  this.learnerEnrollmentRepository.save(learnerEnrollment);  //this.enrollmentDAO.updateLearnerEnrollment(learnerEnrollment);
						}
						enrollmentVO.getEnrollmentsToBeModified().addAll(duplicateEnrollments);
				    }
				} else { // Create New Enrollment
					 // Validate Customer Contract Seats for New Enrollment
					customerContract = this.entitlementService.getCustomerEntitlementById(customerContract.getId());
				    isValidEnrollment = customerContract.hasAvailableSeats(1);
				    if (isValidEnrollment) {
				    	
				    	// LMS-22023 : Validations will be proceed as follows:
					    // 1. If Manager level Permission "Enforce Org. Group Enrollment Restriction" is set to 'Disable' then learner enrolllment will be proceed with no organization Group Entitlement.
					    // 2. If Manager level Permission "Enforce Org. Group Enrollment Restriction" is set to 'Enable' then learner enrollment will be proceed with org grp Entitlement, with further validation for
					    // learner's org grp matches contract's org group.
					    	
				    	boolean isEnabled = this.entitlementService.isEnforceOrgGroupEnrollmentRestrictionEnable(learner.getCustomer());
					    if(isEnabled){
							OrgGroupEntitlement orgGroupContract = this.entitlementService.getMaxAvaiableOrgGroupEntitlementByLearner(learner,
									customerContract.getId());
							
								if(orgGroupContract!=null){
									// Create New Enrollment
									LearnerEnrollment learnerEnrollment = new LearnerEnrollment();
									learnerEnrollment.setLearner(learner);
									learnerEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);

									learnerEnrollment.setEnrollmentDate(enrollmentDate);
									learnerEnrollment.setEnrollmentStartDate(enrollmentStartDate);
									learnerEnrollment.setEnrollmentEndDate(enrollmentEndDate);

									learnerEnrollment.setCourse(course);
									if (synchronousClass != null) {
									    learnerEnrollment.setSynchronousClass(synchronousClass);
									}
									
									
										learnerEnrollment = this.addEnrollment(learnerEnrollment, customerContract, orgGroupContract);
										courseEntitlement.setEnrollmentId(learnerEnrollment.getId());
										if (learnerEnrollment != null && learnerEnrollment.getId() > 0) {
										    newEnrollmentCount++;
										    enrollmentVO.getLearnerEnrollments().add(learnerEnrollment);
										    enrollmentVO.getUniqueLearners().add(learner);
										    enrollmentVO.getUniqueCourses().add(course);
										} else {
										    isValidEnrollment = false;
										}
								} 
								else{
									isValidEnrollment = false;
								}
					    }
					    else{
							// Create New Enrollment
							LearnerEnrollment learnerEnrollment = new LearnerEnrollment();
							learnerEnrollment.setLearner(learner);
							learnerEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);

							learnerEnrollment.setEnrollmentDate(enrollmentDate);
							learnerEnrollment.setEnrollmentStartDate(enrollmentStartDate);
							learnerEnrollment.setEnrollmentEndDate(enrollmentEndDate);

							learnerEnrollment.setCourse(course);
							if (synchronousClass != null) {
							    learnerEnrollment.setSynchronousClass(synchronousClass);
							}
							
							
								learnerEnrollment = this.addEnrollment(learnerEnrollment, customerContract, null);
								courseEntitlement.setEnrollmentId(learnerEnrollment.getId());
								if (learnerEnrollment != null && learnerEnrollment.getId() > 0) {
								    newEnrollmentCount++;
								    enrollmentVO.getLearnerEnrollments().add(learnerEnrollment);
								    enrollmentVO.getUniqueLearners().add(learner);
								    enrollmentVO.getUniqueCourses().add(course);
								} else {
								    isValidEnrollment = false;
								}
					    }

					}
				}
			    }
	
			    if (!isValidEnrollment) {
			    	enrollmentVO.getLearnersFailedToEnroll().add(learner);
			    }
			}
		    }
		    enrollmentVO.setEnrollmentsCreated(newEnrollmentCount);
		} catch (Exception e) {
		    log.error(e);
		}
		return enrollmentVO;
    }

    private SynchronousClass getSynchronousClass(EnrollmentCourseView courseView) {
		List<SynchronousClass> classes = courseView.getSyncClasses();
		SynchronousClass selectedSyncClass = null;
		for (SynchronousClass syncClass : classes) {
		    if (syncClass.getSelected()) {
				selectedSyncClass = syncClass;
				break;
		    }
		}
		return selectedSyncClass;
    }

    private List<EnrollmentCourseView> getSelectedCourses(SaveEnrollmentParam saveEnrollmentParam) {
		List<EnrollmentCourseView> selectedEntitlementItems = new ArrayList<EnrollmentCourseView>();
		for (EnrollmentCourseView course : saveEnrollmentParam.getCourseEntItems()) {
		    if (course.getSelected()) {
		    	selectedEntitlementItems.add(course);
		    }
		}
		return selectedEntitlementItems;
    }

    // [2/8/2011] LMS-8769 :: Admin Mode > Manage Enrollments: System is showing
    // incorrect Locked Course Status
    @Override
    public LockedCourse getLastLockedCourse(LearnerEnrollment le) {
		try {
			LockedCourse lockedCourses = lockedCourseRepository.findFirstByEnrollmentIdAndCourselockedTrueOrderByIdDesc(le.getId());
			return lockedCourses;
		}
		catch (Exception e) {
			log.error(e);
		}
		return null;
	}

    public EntitlementService getEntitlementService() {
    	return entitlementService;
    }

    public void setEntitlementService(EntitlementService entitlementService) {
    	this.entitlementService = entitlementService;
    }

    public LearnerService getLearnerService() {
    	return learnerService;
    }

    public void setLearnerService(LearnerService learnerService) {
    	this.learnerService = learnerService;
    }

    public SynchronousClassService getSynchronousClassService() {
    	return synchronousClassService;
    }

    public void setSynchronousClassService(SynchronousClassService synchronousClassService) {
    	this.synchronousClassService = synchronousClassService;
    }

    public LearnerEnrollment getActiveLearnerEnrollment(Long learnerId, Long courseId) {
		List<LearnerEnrollment> enrollments = learnerEnrollmentRepository.findByLearnerIdAndCourseIdAndEnrollmentStatus(learnerId, courseId,LearnerEnrollment.ACTIVE);
		LearnerEnrollment le = null;
		if (CollectionUtils.isNotEmpty(enrollments)) {
		    le = enrollments.get(enrollments.size() - 1);
		}
		return le;
    }

    
    /**
     * the following cases are handled against ENGSUP-28431 ticket
		case#	enrollment expired	status									behavior
		------------------------------------------------------------------------------------------
		1			0				completed								do enrollment
		2			0				inprogress/notstarted					don't do enrollment
		3			1				inprogress/notstarted					do enrollment
		4			1				completed								do enrollment
		5			0				locked									don't do enrollment
		6			1				locked									do enrollment
		7			0 / 1 / null	expired/swapped							do enrollment
		8		expired by date		completed/inprogress/notstarted/		do enrollment
     */
    @Override
    public Map<Object, Object> selfEnrollTheLearner(VU360User user, String courseId, String synchronousClassId) {
	boolean isSyncCourse = false;
	Map<Object, Object> returnVal = new HashMap<Object, Object>();
	//it only get active enrollment, No dropped, swapped, locked, 
	LearnerEnrollment le = this.getActiveLearnerEnrollment(user.getLearner().getId(), Long.valueOf(courseId));
	Date currentDate = new Date();
	boolean isEnrollDateNotExpired = false;
	boolean isCompleted = false;
	boolean isNotStartedOrInprogress = false;

	if(le!=null){
		isEnrollDateNotExpired = (le.getEnrollmentEndDate().after(currentDate) || le.getEnrollmentEndDate().equals(currentDate));
		isCompleted = le.getCourseStatistics().getCompleted();
		isNotStartedOrInprogress = (
				LearnerCourseStatistics.NOT_STARTED.equals(le.getCourseStatistics().getStatus()) || 
				LearnerCourseStatistics.IN_PROGRESS.equals(le.getCourseStatistics().getStatus())
				);
	}
	//in case of expired enrollment, allow to make new one.
	if (le!=null && !isEnrollDateNotExpired) 
		le=null;
	
	if (le==null || isCompleted || !isNotStartedOrInprogress  ) {
			// new enrollment start here..
			Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(courseId));
		    if( (course instanceof SynchronousCourse || course instanceof WebinarCourse) && !synchronousClassService.isSyncCourseAssignable(Long.valueOf(courseId))){
		    	returnVal.put("error", "lms.learnerenrollment.error.courseNotAssignable");
				return returnVal;
		    }
		    List<CustomerEntitlement> availableEntitlementsForCustomer = entitlementService.getActiveCustomerContractByCourseId(user.getLearner()
			    .getCustomer(), Long.valueOf(courseId));
		    LearnerEnrollment newEnrollment;
		    CustomerEntitlement entitlement = null;
		    if (CollectionUtils.isNotEmpty(availableEntitlementsForCustomer))
		    	entitlement = availableEntitlementsForCustomer.get(0);
		    
		    if (entitlement == null) {
				returnVal.put("error", "lms.learnerenrollment.error.seatsNotAvailable");
				return returnVal;
		    } else {
				OrgGroupEntitlement oge = entitlementService.getMaxAvaiableOrgGroupEntitlementByLearner(user.getLearner(), entitlement.getId());
				if (oge != null && !oge.hasAvailableSeats(1)) {
				    returnVal.put("error", "lms.learnerenrollment.error.seatsNotAvailable");
				    return returnVal;
				}
				LearnerEnrollment learnerEnrollment = new LearnerEnrollment();
				learnerEnrollment.setCourse(course);
				learnerEnrollment.setCustomerEntitlement(entitlement);
				learnerEnrollment.setLearner(user.getLearner());
				learnerEnrollment.setEnrollmentStartDate(FormUtil.formatToDayStart(new Date())); 
				// [2/22/2011] LMS-9042 :: Enrollment Start and End dates should have proper time
				learnerEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
				learnerEnrollment.setOrgGroupEntitlement(oge);
	
				// for Sync Class Association
				try {
				    if (course.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) 
				    		|| course.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
						if (synchronousClassId != null) {
						    learnerEnrollment.setSynchronousClass(synchronousClassService.getSynchronousClassById(Long.valueOf(synchronousClassId)));
						}
						else{//LMS-15743- Enrolled in first class
							List<SynchronousClass> syncClasses = synchronousClassService.getAllSynchClassesOfCourse(Long.valueOf(courseId));
							learnerEnrollment.setSynchronousClass(syncClasses.get(0));
						}
						isSyncCourse = true;
				    } else if (course.getCourseType().equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
						if (synchronousClassId != null) {
						    learnerEnrollment.setSynchronousClass(synchronousClassService.getSynchronousClassById(Long.valueOf(synchronousClassId)));
						}
						isSyncCourse = true;
				    }
				} catch (Exception e) {
				    log.error("Error in Fetching Synchronous Class",e);
				}
	
				// end Sync Class association
	
				TrainingPlan trainingPlan = courseAndCourseGroupService.getTrainingPlanByCourse(course, user.getLearner().getCustomer());
				// LMS-4150; // [2/22/2011] LMS-9042 :: Enrollment Start and End
				// dates should have proper time
				if (trainingPlan != null && trainingPlan.getEnddate() != null) {
				    learnerEnrollment.setEnrollmentEndDate(FormUtil.formatToDayEnd(trainingPlan.getEnddate()));
				} else {
				    learnerEnrollment.setEnrollmentEndDate(FormUtil.formatToDayEnd(entitlement.getEntitlementEndDate()));
				}
	
				newEnrollment = this.addEnrollment(learnerEnrollment, availableEntitlementsForCustomer.get(0), oge);
	
				if (isSyncCourse)
				    returnVal.put("syncCourseEnrollment", newEnrollment);
				else
				    returnVal.put("courseEnrollment", newEnrollment);
				// training plan is not null then assign training plan to
				// learner so that this enrollment appear under training plan in
				// enrolled course
				if (trainingPlan != null) {
				    List<LearnerEnrollment> enrollments = new ArrayList<LearnerEnrollment>();
				    TrainingPlanAssignment tpa = new TrainingPlanAssignment();
				    tpa.setTrainingPlan(trainingPlan);
				    enrollments.add(newEnrollment);
				    tpa.setLearnerEnrollments(enrollments);
				    trainingPlanService.addTrainingPlanAssignments(tpa);
				}
		    }
		}
		else{
		    if (le.getCourse() instanceof SynchronousCourse || le.getCourse() instanceof WebinarCourse) {
		    	returnVal.put("syncCourseEnrollment", le);
		    } else {
		    	returnVal.put("courseEnrollment", le);
		    }
		}// end of else for new enrollment
	
		return returnVal;
    }

    public void enrollLearnersInLearnerGroupCourses(List<Learner> learners, List<LearnerGroupItem> items, Brander brand) {
		if (learners != null && !learners.isEmpty() && items != null && !items.isEmpty()) {
			log.debug("IN process enrollLearnersInLearnerGroupCourses end *4 ");
			VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
		    Customer customer = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		    log.debug("IN process enrollLearnersInCoursesViaUserGroup start *5 ");
		    if(items != null) {
		    	enrollLearnersInCoursesViaUserGroup(learners, items, user, customer, brand);
		    }
		    log.debug("IN process enrollLearnersInCoursesViaUserGroup end *5 ");
		    log.debug("IN process enrollLearnersInLearnerGroupCourses end *4 ");
		}
    }
    
    @Transactional
    public void enrollLearnerInLearnerGroupsCourses(VU360User user, Customer customer, Learner learner, List<LearnerGroup> learnerGroups,
	    Brander brander) throws Exception {
		if (learner != null) {
			List<Learner> learners = new ArrayList<Learner>();
			learners.add(learner);
			/*List<LearnerGroupItem> items = new ArrayList<LearnerGroupItem>();
				for (LearnerGroupItem item : learnerGroup.getLearnerGroupItems()) {
					items.add(item);
				}
			}*/
			List<LearnerGroupItem> items = new ArrayList<LearnerGroupItem>();
			for (LearnerGroup learnerGroup : learnerGroups) {
				List<LearnerGroupItem> listItems = learnerGroupItemRepository.findByLearnerGroupId(learnerGroup.getId());
				if(listItems!=null && listItems.size()>0)
					items.addAll(listItems);
			}
			
			if(items.size() > 0) {
				enrollLearnersInCoursesViaUserGroup(learners, items, user, customer, brander);
			}
		}
    }

    public void enrollLearnersInCourses(List<Learner> learners, Long[] learnerGroupCourseIds, VU360User user, Customer customer, Brander brander) {
		List<CustomerEntitlement> custEntList = entitlementService.getActiveCustomerEntitlementsForCustomer(customer);
		List<EnrollmentCourseView> enrollmentCourseViewList = entitlementService.getEnrollmentCourseViewsByCustomerAndCourseIds(custEntList,
		learnerGroupCourseIds);

		for (EnrollmentCourseView enrollmentCourseView : enrollmentCourseViewList) {
		    enrollmentCourseView.setSelected(true);
		}
		SaveEnrollmentParam saveEnrollmentParam = new SaveEnrollmentParam(enrollmentCourseViewList, learners, false, null, null, true);
	
		saveEnrollmentParam.setBrander(brander);
		saveEnrollmentParam.setUser(user);
		saveEnrollmentParam.setNotifyLearner(customer.getCustomerPreferences().isEnableEnrollmentEmailsForNewCustomers());
		saveEnrollmentParam.setEnableEnrollmentEmailsForNewCustomers(customer.getDistributor().getDistributorPreferences()
			.isEnableRegistrationEmailsForNewCustomers() ? customer.getCustomerPreferences().isEnableEnrollmentEmailsForNewCustomers() ? true
			: false : false);
		saveEnrollmentParam.setNotifyManagerOnConfirmation(true);
		saveEnrollmentParam.setVelocityEngine(velocityEngine);
		saveEnrollmentParam.setEmailService(learnersToBeMailedService);
	
		EnrollLearnerAsyncTask task = new EnrollLearnerAsyncTask();
		task.setSaveEnrollmentParam(saveEnrollmentParam);
		task.setEnrollmentService(this);
		asyncTaskExecutorWrapper.execute(task);
    }

    public void enrollLearnersInCoursesViaUserGroup(List<Learner> learners, List<LearnerGroupItem> items, VU360User user, Customer customer, Brander brander) {
    	Set<Long> courseIds = new HashSet<Long>();
    	for (LearnerGroupItem item : items) {
    		Course course = item.getCourse();
    		if(course.isActive())
    		{
    			log.debug("IN process enrollLearnersInCoursesViaUserGroup start *6 CoruseTitle:"+course.getCourseTitle()+ ",    CourseID."+course.getId() );
    			List<CustomerEntitlement> custEntList = entitlementService.getActiveCustomerEntitlementsForCustomer(customer);
    			List<EnrollmentCourseView> enrollmentCourseViewList = null;
		    	if(item.getCourseGroup()!=null && item.getCourseGroup().getId()>0)
		    	{    	
		    		enrollmentCourseViewList = entitlementService.getEnrollmentCourseViewsByCustomerCourseAndCourseGroupIds(custEntList,
		    			course.getId(), item.getCourseGroup().getId());
		    	}
	    	
		    	if(enrollmentCourseViewList !=null && enrollmentCourseViewList.size() >0)
		    	{
			    	for (EnrollmentCourseView enrollmentCourseView : enrollmentCourseViewList) {
			    		log.debug("IN process enrollmentCourseView start *7 "+enrollmentCourseView.getEntitlementId());
			    	    enrollmentCourseView.setSelected(true);
			    	}
			    	SaveEnrollmentParam saveEnrollmentParam = new SaveEnrollmentParam(enrollmentCourseViewList, learners, false, null, null, true);
			    	log.debug("IN process enrollLearnersInCoursesViaUserGroup start *8 ");
			    	saveEnrollmentParam.setBrander(brander);
			    	saveEnrollmentParam.setUser(user);
			    	saveEnrollmentParam.setNotifyLearner(customer.getCustomerPreferences().isEnableEnrollmentEmailsForNewCustomers());
			    	saveEnrollmentParam.setEnableEnrollmentEmailsForNewCustomers(customer.getDistributor().getDistributorPreferences()
			    		.isEnableRegistrationEmailsForNewCustomers() ? customer.getCustomerPreferences().isEnableEnrollmentEmailsForNewCustomers() ? true
			    		: false : false);
			    	saveEnrollmentParam.setNotifyManagerOnConfirmation(true);
			    	saveEnrollmentParam.setVelocityEngine(velocityEngine);
			    	saveEnrollmentParam.setEmailService(learnersToBeMailedService);
			
			    	log.debug("IN process EnrollLearnerAsyncTask start *9 created. ");
			    	EnrollLearnerAsyncTask task = new EnrollLearnerAsyncTask();
			    	task.setSaveEnrollmentParam(saveEnrollmentParam);
			    	task.setEnrollmentService(this);
			    	log.debug("IN process EnrollLearnerAsyncTask start *10 ");
			    	asyncTaskExecutorWrapper.execute(task);
		    	}
		    	else
		    	{
		    		enrollmentCourseViewList = null;
		    		courseIds.add(course.getId());    		
		    	}
		    	log.debug("IN process enrollLearnersInCoursesViaUserGroup end *6 "+course.getName());
    		}
    	}
    	 if (!courseIds.isEmpty()) {
    			enrollLearnersInCourses(learners, courseIds.toArray(new Long[courseIds.size()]), user, customer, brander);
    	 }
    }
    
    public CourseAndCourseGroupService getCourseAndCourseGroupService() {
    	return courseAndCourseGroupService;
    }

    public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
    	this.courseAndCourseGroupService = courseAndCourseGroupService;
    }

    public TrainingPlanService getTrainingPlanService() {
    	return trainingPlanService;
    }

    public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
    	this.trainingPlanService = trainingPlanService;
    }

    public VelocityEngine getVelocityEngine() {
    	return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
    	this.velocityEngine = velocityEngine;
    }

    public LearnersToBeMailedService getLearnersToBeMailedService() {
    	return learnersToBeMailedService;
    }

    public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
    	this.learnersToBeMailedService = learnersToBeMailedService;
    }

    public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
    	return asyncTaskExecutorWrapper;
    }

    public void setAsyncTaskExecutorWrapper(AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
    	this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
    }

    public LearnerEnrollment loadForUpdateLearnerEnrollment(long enrollmentId) {
    	return this.learnerEnrollmentRepository.findOne(new Long(enrollmentId));	
    }

    public void updateEnrollment(LearnerEnrollment enrollment) {
    	//enrollmentDAO.updateLearnerEnrollment(enrollment);
    	this.learnerEnrollmentRepository.save(enrollment);
    }
   
    /**
     * // [12/15/2010] LMS-7942 :: Show Previous Enrollments detail for the same
     * course
     */
    @Override
    public List<LearnerEnrollment> getEnrollmentsByCourseID(Long learnerId,
			Long courseId, Date enrollmentEndDate) {

		List<LearnerEnrollment> learnerEnrollmentList = new ArrayList<LearnerEnrollment>();
		try {			
			learnerEnrollmentList = learnerEnrollmentRepository.findByLearnerIdAndCourseIdAndEnrollmentEndDateLessThan(learnerId, courseId, enrollmentEndDate);			
		}
		catch (Exception e) {
			log.debug(e);
		}
		
		return learnerEnrollmentList;
	}

	public List<LearnerEnrollment> getAllLearnerEnrollmentsByLearner(Learner learner) {
		//return this.enrollmentDAO.getAllLearnerEnrollmentsByLearner(learner);
		return learnerEnrollmentRepository.findByLearnerId(learner.getId());
		
	}
    
	public List<LearnerEnrollment> getLearnerEnrollmentByCourseId(long courseid)
	{
		return this.learnerEnrollmentRepository.findByCourseIdAndEnrollmentStatus(courseid,LearnerEnrollment.ACTIVE);
	}
    
    /**
     * Gets learner enrollment using externallmssessionId.If no session has been created yet then gets first active enrollment.
     */
    public LearnerEnrollment getAICCLearnerEnrollment(String externalLMSSessionID,long learnerId,long courseId) {    	
    	
    	LearnerEnrollment learnerEnrollment = null;	
    	try{
    			LearningSession learningSession = learningSessionRepository.findFirstByExternalLMSSessionIDAndSource(externalLMSSessionID, "AICC");
    
			    if (learningSession != null){
			    	 learnerEnrollment  = this.learnerEnrollmentRepository.findByIdAndEnrollmentStatus(learningSession.getEnrollmentId(), LearnerEnrollment.ACTIVE);
			    }
			  //If no session has been created yet then gets first active enrollment
				if(learnerEnrollment == null){
					List<LearnerEnrollment> leList= learnerEnrollmentRepository.findByLearnerIdAndCourseIdAndEnrollmentStatus(new Long(learnerId), new Long(courseId),LearnerEnrollment.ACTIVE);
					for(LearnerEnrollment le:leList){
						LearnerCourseStatistics courseStatistics =  this.learnerCourseStatisticsRepository.findByLearnerEnrollment(le); //this.enrollmentDAO.getLearnerCourseStatisticsBylearnerEnrollment(le);
						if(!courseStatistics.isCourseCompleted())
							return le;    			
					}   
					
				}    	
    	}
		catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("LearnerEnrollment with external LMS Session ID: " + externalLMSSessionID + "is not found");
			}
		}
	
	return learnerEnrollment;   
 }
    
    @Override
    public LearnerEnrollment getAICCLearnerEnrollment(String externalLMSSessionID,long learnerId,long courseId, String courseGuid) {    	
    	
    	LearnerEnrollment learnerEnrollment = null;	
    	try{
    			LearningSession learningSession = learningSessionRepository.findFirstByExternalLMSSessionIDAndSourceAndCourseId(externalLMSSessionID, "AICC",courseGuid);
    
			    if (learningSession != null){
			    	 learnerEnrollment  = this.learnerEnrollmentRepository.findByIdAndEnrollmentStatus(learningSession.getEnrollmentId(), LearnerEnrollment.ACTIVE);
			    }
			  //If no session has been created yet then gets first active enrollment
				if(learnerEnrollment == null){
					List<LearnerEnrollment> leList= learnerEnrollmentRepository.findByLearnerIdAndCourseIdAndEnrollmentStatus(new Long(learnerId), new Long(courseId),LearnerEnrollment.ACTIVE);
					for(LearnerEnrollment le:leList){
						LearnerCourseStatistics courseStatistics =  this.learnerCourseStatisticsRepository.findByLearnerEnrollment(le); //this.enrollmentDAO.getLearnerCourseStatisticsBylearnerEnrollment(le);
						if(!courseStatistics.isCourseCompleted())
							return le;    			
					}   
					
				}    	
    	}
		catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("LearnerEnrollment with external LMS Session ID: " + externalLMSSessionID + "is not found");
			}
		}
	
	return learnerEnrollment;   
    	
    		    	
    }

    public LearnerCourseStatistics getLearnerCourseStatisticsBylearnerEnrollment(
			LearnerEnrollment learnerEnrollment) {
		LearnerCourseStatistics learnerCourseStatistics = new LearnerCourseStatistics();
		try {
			learnerCourseStatistics = learnerCourseStatisticsRepository.findByLearnerEnrollment(learnerEnrollment);
		}
		catch (ObjectRetrievalFailureException e) {
			if (log.isDebugEnabled()) {
				log.debug("LearnerCourseStatistics with LearnerEnrollment id: " + learnerEnrollment.getId() + "is not found");
			}
		}
		return learnerCourseStatistics;
	}

    public AvailableCoursesTree getAvailableCoursesTree(Learner learner, String search) {
		List<CourseGroupView> cgViewList = this.courseGroupAndCoursesForView(search, learner);

		Map<Long, CourseGroupView> cgViewMap = transformListIntoMap(cgViewList);
		
		/*
		 * This block is replaced by the below 2 lines of code due to the performance optimization.
		 * In this block of code, if there is n coursegroups then it querys DB n times for the course groups. 
		 * @Wajahat
		 * LMS-16326
		 */
			
		/*for (CourseGroupView cgView : cgViewList) {
		    parentCourseGroupId = cgView.getParentCourseGroupId();
		    if (cgViewMap.get(parentCourseGroupId) == null) {
			// this means we haven't got parent in collection and have to
			// fetch it from database
			CourseGroup cg = courseAndCourseGroupService.getCourseGroupById(parentCourseGroupId);
			while (cg != null) {
				if(cgViewMap.get(cg.getId())==null){
				    CourseGroupView view = new CourseGroupView();
				    view.setGroupName(cg.getName());
				    view.setId(cg.getId());
				    cgViewMap.put(view.getId(), view);
		
				    if (cg.getParentCourseGroup() != null) {
				    	view.setParentCourseGroupId(cg.getParentCourseGroup().getId());
				    }
				    cg = cg.getParentCourseGroup();
				}
				else
					cg=null;
			}
		    }
		}
		*/
		
		/*
		 * @Wajahat:
		 * The below 2 lines of code retrieving the same result as the above block does but it is optimized and there is only 1 query in DB.
		 * ENGSUP-34002
		 * LMS-16326
		 */
		try{
			Map<Long,CourseGroupView> mapCourseGroupView = courseAndCourseGroupService.getAllParentCourseGroups(cgViewList, cgViewMap);
			cgViewMap.putAll(mapCourseGroupView);
		}
		catch(Exception e){
			log.error("Error during retrieve course groups:"+e.getMessage(), e);
		}
		
		return new AvailableCoursesTree(cgViewMap);
    }

    // Refactor this method to reduce its Cognitive Complexity from 371 to the 340.
    @Override
    public Map displayMyCourses(VU360User user, Brander brand, List<CourseGroupView> courseGroups, List<MyCoursesItem> filteredMyCourses,
	    List sortedCourseGroups, String viewType, String search, Boolean isFirstTimeView) {

		Map<Object, Object> context = new HashMap<>();
		final long currentTimeInMillis = System.currentTimeMillis();
		final int totMillisInDay=1000 * 60 * 60 * 24;
		Learner learner = user.getLearner();
	
		log.debug("Searched Content :: " + search);
	
		List<MyCoursesCourseGroup> myCoursesCourseGroups = new ArrayList<>();
	
		if (viewType.equalsIgnoreCase(COURSECATALOG_MYCOURSES_VIEW)) {
		    context.put("courseGroupList", courseGroupAndCoursesForView(search, learner));
		} else {// runs on null view Type
		    List<LearnerEnrollment> enrollments;
		    if (viewType.equalsIgnoreCase(EXPIRED_MYCOURSES_VIEW)) {
		    	enrollments = entitlementService.getExpiredLearnerEnrollmentsByLearner(learner, search);
		    } else if (viewType.equalsIgnoreCase(COMPLETED_MYCOURSES_VIEW)) {
		    	enrollments = this.getCompletedLearnerCourses(learner, search);
		    } else { // runs on null view Type
		    	enrollments = entitlementService.getLearnerEnrollmentsByLearner(learner, search);
		    }
		    
		    if (viewType.equalsIgnoreCase(COURSES_ABOUT_TO_EXPIRED_MYCOURSES_VIEW)) {
		    	List<LearnerEnrollment> selectedEnrollments = new ArrayList<>();
				for (LearnerEnrollment enrollment : enrollments) {
				    Date enrlEndDate = enrollment.getEnrollmentEndDate();
				    if (enrlEndDate != null) {
						long diff = enrlEndDate.getTime() - currentTimeInMillis;
						long exprDate = diff / totMillisInDay;
			
						if (! enrollment.getCourseStatistics().getStatus().equalsIgnoreCase(LearnerCourseStatistics.REPORTING_PENDING) && exprDate >= 0
							&& exprDate <= Integer.parseInt(brand.getBrandElement("lms.learner.mycourses.caption.coursesAboutToExpired.timeline"))) {
						    selectedEnrollments.add(enrollment);
						}		
				    }
				}
	
				myCoursesCourseGroups = generateMyCoursesViewForLearnerEnrollments(selectedEnrollments);
		    } else {// runs on null view Type
		    	if(viewType.equalsIgnoreCase(COMPLETED_MYCOURSES_VIEW))
		    		myCoursesCourseGroups = generateMyCoursesViewForLearnerEnrollmentsForView(enrollments);
		    	else
		    		myCoursesCourseGroups = generateMyCoursesViewForLearnerEnrollments(enrollments);
		    }
	
		}
	
		// apply the filter to the alMyCourseItems for the filtered view that the user has asked for.
		Set<MyCoursesCourseGroup> filteredCourseGroups = null;
	
		if (viewType.equalsIgnoreCase(RECENT_MYCOURSES_VIEW)) {// runs on null
								       // view Type
		    filteredCourseGroups = MyCoursesCourseGroup.filterCoursesForMyCourses(MyCoursesCourseGroup.FILTER_RECENT, myCoursesCourseGroups);
		    filteredMyCourses = MyCoursesCourseGroup.getAllCurrentCourses(filteredCourseGroups);
		    Collections.sort(filteredMyCourses, new MyCoursesComparator(RECENT_MYCOURSES_VIEW));
	
		    if (isFirstTimeView && filteredCourseGroups.isEmpty() && filteredMyCourses.isEmpty()) {
				// if no recently accessed courses yet, show enrolled courses
			    filteredCourseGroups = MyCoursesCourseGroup.filterCoursesForMyCourses(MyCoursesCourseGroup.FILTER_ENROLLED, myCoursesCourseGroups);
			    viewType = ENROLLED_MYCOURSES_VIEW;
		    }
	
		} else if (viewType.equalsIgnoreCase(ENROLLED_MYCOURSES_VIEW)) {
		    filteredCourseGroups = MyCoursesCourseGroup.filterCoursesForMyCourses(MyCoursesCourseGroup.FILTER_ENROLLED, myCoursesCourseGroups);
	
		} else if (viewType.equalsIgnoreCase(COURSECATALOG_MYCOURSES_VIEW)) {
	
		} else if (viewType.equalsIgnoreCase(COMPLETED_MYCOURSES_VIEW)) {
		    filteredCourseGroups = MyCoursesCourseGroup.filterCoursesForMyCourses(MyCoursesCourseGroup.FILTER_COMPLETED, myCoursesCourseGroups);
		} else if (viewType.equalsIgnoreCase(EXPIRED_MYCOURSES_VIEW)) {
		    filteredCourseGroups = MyCoursesCourseGroup.filterCoursesForMyCourses(MyCoursesCourseGroup.FILTER_EXPIRED, myCoursesCourseGroups);
		} else if (viewType.equalsIgnoreCase(COURSES_ABOUT_TO_EXPIRED_MYCOURSES_VIEW)) {
		    filteredCourseGroups = MyCoursesCourseGroup.filterCoursesForMyCourses(MyCoursesCourseGroup.FILTER_ABOUT_TO_EXPIRE, myCoursesCourseGroups);
		} else {
		    filteredCourseGroups = MyCoursesCourseGroup.filterCoursesForMyCourses(MyCoursesCourseGroup.FILTER_RECENT, myCoursesCourseGroups);
	
		}
	
		checkCurrentlyInSession(user, brand, filteredMyCourses);
	
		if (filteredCourseGroups != null)
		    sortedCourseGroups.addAll(filteredCourseGroups);
		if (viewType.equalsIgnoreCase(RECENT_MYCOURSES_VIEW) || viewType.equalsIgnoreCase(ENROLLED_MYCOURSES_VIEW)
			|| viewType.equalsIgnoreCase(EXPIRED_MYCOURSES_VIEW) || viewType.equalsIgnoreCase(COURSES_ABOUT_TO_EXPIRED_MYCOURSES_VIEW)
			|| viewType.equalsIgnoreCase(COMPLETED_MYCOURSES_VIEW)) {
		    if (filteredCourseGroups != null) {
			for (MyCoursesCourseGroup d : filteredCourseGroups) {
			    for (MyCoursesItem mci : d.getCurrentMyCoursesItems("enrolled")) {
			    	// Checking Missing Reporting Field - Start
			    	try{
			    		Long courseApprovalId = accreditationService.getCourseApprovalSelected(mci.getEnrollment().getId(), mci.getEnrollment().getLearner().getId(), mci.getEnrollment().getCourse().getCourseGUID());
		    			if(courseApprovalId !=null && courseApprovalId >0)
		    			{
		    				List<CreditReportingField> creditReportingFieldList= new ArrayList<CreditReportingField>(accreditationService.getCreditReportingFieldsByCourseApproval(courseApprovalId));
		    				List<CreditReportingFieldValue> creditReportingFieldValueList =learnerService.getCreditReportingFieldValues(learner);
				    	
				    		boolean valueFound = true;
				    	
					    	if(creditReportingFieldList!=null && !creditReportingFieldList.isEmpty()){
					    		CHK_ALL_REPORTING_FIELDS_LOOP:for (CreditReportingField field : creditReportingFieldList) {
					    			valueFound = false;
					    			if (field.isFieldRequired()) {
										if(creditReportingFieldValueList!=null && !creditReportingFieldValueList.isEmpty()){
										    for (CreditReportingFieldValue value : creditReportingFieldValueList) {
											    valueFound = value.getReportingCustomField().getId().longValue() == field.getId().longValue();
											    continue CHK_ALL_REPORTING_FIELDS_LOOP;
										    }
										    /* check for static field values */ 
										    if("STATICCREDITREPORTINGFIELD".equals(field.getFieldType())) {
										    	valueFound = false;
										    	if (userService.getValueForStaticReportingField(user, field.getFieldLabel()) != null )
										    		valueFound = true;
										    }
										    
										}
										
										if(!valueFound){
											mci.setReportingFieldMissing(MISSING_REPORTING_FIELDS_MESSAGE);
											break CHK_ALL_REPORTING_FIELDS_LOOP;
										}
									}
							    }
					    	}
		    			}
			    	}catch(Exception e){
			    		log.error(e.getMessage(),e);
			    	}
			    	// Checking Missing Reporting Field - End
			    	
					String courseStatus = (String) mci.get("courseStatus");
					String courseCategoryType = (String) mci.get("courseCategoryType");
					LearnerEnrollment learnerEnrollment = (LearnerEnrollment) mci.get("enrollment");
					if(learnerEnrollment.getCourse().getCourseConfigTemplate() !=null){
					CourseConfiguration courseconfig = accreditationService.getCourseConfigurationByTemplateId(learnerEnrollment.getCourse().getCourseConfigTemplate().getId(),true);
					
					if(courseconfig != null && courseconfig.getAssessmentConfigurations() != null)	{
						for(AssessmentConfiguration assesmentConfiguration:courseconfig.getAssessmentConfigurations()){
							if(assesmentConfiguration.getAssessmentType().equals("PostAssessment")){
								if(assesmentConfiguration.getViewassessmentresultsEnabled()!=null && assesmentConfiguration.getViewassessmentresultsEnabled()){
									if(getviewAssessmentResults(mci.getEnrollment().getId())){
										mci.put("viewAssessmentResults", 1);
										mci.setViewAssessmentResults(1);											
									}
									else{
										mci.put("viewAssessmentResults", 0);
										mci.setViewAssessmentResults(0);										
									}
								}else{
									mci.put("viewAssessmentResults", 0);
									mci.setViewAssessmentResults(0);									
								}
							}
						}
					}
					
					CourseApproval courseApproval = accreditationService.getCourseApprovalByCourse(learnerEnrollment.getCourse());
					if(courseApproval!=null){
						CourseConfigurationTemplate courseConfigurationTemplate = courseApproval.getTemplate();
						  if(courseConfigurationTemplate!=null){
							  CourseConfiguration courseapproval_courseconfig = accreditationService.getCourseConfigurationByTemplateId(courseConfigurationTemplate.getId(), true);
							  if(courseapproval_courseconfig!=null){
								  if(courseapproval_courseconfig.getAssessmentConfigurations() != null)
									{
										for(AssessmentConfiguration courseapproval_assesmentConfiguration:courseapproval_courseconfig.getAssessmentConfigurations()){
											if(courseapproval_assesmentConfiguration.getAssessmentType().equals("PostAssessment")){
												if(courseapproval_assesmentConfiguration.getViewassessmentresultsEnabled()!=null && courseapproval_assesmentConfiguration.getViewassessmentresultsEnabled()){
													if(getviewAssessmentResults(mci.getEnrollment().getId())){
														  mci.put("viewAssessmentResults", 1);
														  mci.setViewAssessmentResults(1);											
													}
													else{
														  mci.put("viewAssessmentResults", 0);
														  mci.setViewAssessmentResults(0);										
													}
					
												}
												else{
													  mci.put("viewAssessmentResults", 0);
													  mci.setViewAssessmentResults(0);									
												}
											}
											  
										}
											
									}
							  }
						  }
					}
					
					// if course is	already	completed
					if (courseStatus.equals("completed") || courseStatus.equals("userdeclinedaffidavit") ) { 
						if(learnerEnrollment.getCourse().getCourseConfigTemplate() != null && courseconfig.getAllowFaceook()){
							  mci.put("allowFacebook", 1);
							  mci.setAllowFacebook(1);
						}
						if (courseCategoryType.equalsIgnoreCase("Weblink")) {
							// if it is a weblink course
							log.debug("Weblink course found - courseCategoryType --> " + courseCategoryType);
							mci.put("courseCategoryType", "Weblink");
					    }
					}
				}
				// for Sync Course Session In Progress Check
				try {
				    LearnerEnrollment le = (LearnerEnrollment) mci.get("enrollment");
				    if (le != null) {
					boolean currentlyInSession = false;
					if (le.getSynchronousClass() != null && user.getLearner().getLearnerProfile().getTimeZone() != null) {
					    currentlyInSession = synchronousClassService.checkIfSessionInProgress(synchronousClassService
						    .getSynchronousSessionsByClassId(le.getSynchronousClass().getId()), le.getSynchronousClass()
						    .getTimeZone(), user.getLearner().getLearnerProfile().getTimeZone(), brand);
					}
	
					mci.put("syncInProgress", currentlyInSession);
					mci.put("courseIconToShow",
						getCourseIconToShow(le, currentlyInSession, mci.get("courseIdKey").toString(), mci.get("courseCategoryType").toString()));
	
					// Code by OWS for Stats regarding synchronous courses
	
	//              Ticket: LMS-14013 - Synchronous Courses should not be marked Complete at Class End Date or Enrollment End Date automatically
	//              Change: Commented the below sync. course condition
					if (courseCategoryType.equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || courseCategoryType.equalsIgnoreCase(WebinarCourse.COURSE_TYPE) ) {
					    LearnerCourseStatistics newCourseStats = statsService.getLearnerCourseStatisticsForLearnerEnrollment(le);
					    newCourseStats.setPercentComplete(this.getPercentCompleteForSyncCourse(le));
					    if (newCourseStats.getPercentComplete() == 100 && !newCourseStats.isCourseCompleted()) {
						/**
						 * added by muhammad akif for -- LMS-16163
						 * for course completion automatic/manual
						 */
						    if (le.getSynchronousClass() != null && le.getSynchronousClass().isAutomatic()!=null && le.getSynchronousClass().isAutomatic()){
								mci.put("courseStatus", LearnerCourseStatistics.COMPLETED);
								mci.put("completionPercent", newCourseStats.getPercentComplete());
						    }
					    
					    }
					} else if (courseCategoryType.equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
	
					    LearnerCourseStatistics newCourseStats = statsService.getLearnerCourseStatisticsForLearnerEnrollment(le);
					    newCourseStats.setPercentComplete(this.getPercentCompleteForSyncCourse(le));
					    boolean sendCompletionMail = false;
					    if (newCourseStats.getPercentComplete() == 100 && !newCourseStats.isCourseCompleted()) {
							newCourseStats.setCompleted(true);
							newCourseStats.setStatus(LearnerCourseStatistics.COMPLETED);
		
							mci.put("courseStatus", LearnerCourseStatistics.COMPLETED);
							sendCompletionMail = true;
							mci.put("completionPercent", newCourseStats.getPercentComplete());
					    }
					    statsService.saveLearnerCourseStatistics(newCourseStats);
					    if (sendCompletionMail) {
					    	learnersToBeMailedService.emailCourseCompletionCertificate(le.getId());
					    }
	
					}
	
				    }
				} catch (Exception e) {
				    log.error("Exception in saving Sync Course Stats: " + e.getMessage(),e);
				}
			    }
			}
		    }
		}
		if (sortedCourseGroups != null)
		    Collections.sort(sortedCourseGroups);
	
		context.put("userName", user.getFirstName() + " " + user.getLastName());
		context.put("myCoursesCourseGroups", sortedCourseGroups);
		context.put("myCourseItems", filteredMyCourses);
		context.put("viewType", viewType);
		context.put("search", search);
		context.put("courseGroupList", courseGroups);
		log.debug("displaying view:" + viewType);
	
		return context;
    }

    //Added By Marium Saud : Moved From TopLinkEnrollmentDAO
  	public List<LearnerEnrollment> getCompletedLearnerCourses(
  			Learner learner, String search) {
  		List<LearnerEnrollment> result;
  		
  		if(!StringUtils.isBlank(search) && !search.equals("Search"))
  			result = learnerEnrollmentRepository.findByLearnerIdAndCourseStatusWithTitleDescKeywords(learner.getId(), search, search,search);
  		else
  			result = learnerEnrollmentRepository.findByLearnerIdAndCourseStatus(learner.getId());
  			 
  		return result;
  	}
  	
  	
    public HashMap displayMyAvailableCourses(Learner learner, List<CourseGroupView> courseGroups, String courseGroupId, String trainingPlanId,
	    HashMap<Object, Object> catalogMap, String search) {

	List<CustomerEntitlement> customerEntitlements = entitlementService.getCustomerEntitlementsForLearner(learner);
	if (courseGroupId != null && !courseGroupId.isEmpty()) {
	    catalogMap = (HashMap<Object, Object>) MyCoursesCourseGroup.generateCourseCatalogView(learner.getId(), customerEntitlements,
		    courseAndCourseGroupService, search, "CG", Long.parseLong(courseGroupId));
	    CourseGroup courseGroup = courseAndCourseGroupService.getCourseGroupById(Long.parseLong(courseGroupId));
	    StringBuffer youAreHere = new StringBuffer();
	    youAreHere.append(courseGroup.getName());
	    while (courseGroup.getParentCourseGroup() != null) {
		youAreHere.insert(0, courseGroup.getParentCourseGroup().getName() + " > ");
		courseGroup = courseGroup.getParentCourseGroup();
	    }
	    log.debug("You Are HERE :: " + youAreHere);
	    catalogMap.put("youAreHere", youAreHere);
	    List<CourseGroupView> courses = (ArrayList<CourseGroupView>) catalogMap.get("courses");
	    for (CourseGroupView grpView : courses) {
		for (CourseView courseView : grpView.getCourses()) {
		    courseView.setCourseIconToShow(this.getCourseIconToShow(null, false, courseView.getCourseId().toString(), courseView.getType()));
		    LearnerEnrollment lE = entitlementService.getLearnerEnrollmentsForLearner(learner, courseView.getCourseId());
		    if (lE != null)
			courseView.setEnrollmentId(lE.getId());
		}
	    }
	    catalogMap.put("courses", courses);
	} else if (trainingPlanId != null && !trainingPlanId.isEmpty()) {
	    catalogMap = (HashMap<Object, Object>) MyCoursesCourseGroup.generateCourseCatalogView(learner.getId(), customerEntitlements,
		    courseAndCourseGroupService, search, "TP", Long.parseLong(trainingPlanId));
	    TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanByID(Long.parseLong(trainingPlanId));
	    StringBuffer youAreHere = new StringBuffer();
	    youAreHere.append("Training Plans > " + trainingPlan.getName());
	    log.debug("You Are HERE :: " + youAreHere);
	    catalogMap.put("youAreHere", youAreHere);
	    List<CourseGroupView> courses = (ArrayList<CourseGroupView>) catalogMap.get("courses");
	    for (CourseGroupView grpView : courses) {
		for (CourseView courseView : grpView.getCourses()) {
		    courseView.setCourseIconToShow(this.getCourseIconToShow(null, false, courseView.getCourseId().toString(), courseView.getType()));
		}
	    }
	    catalogMap.put("courses", courses);
	} else {
	    catalogMap = (HashMap<Object, Object>) MyCoursesCourseGroup.generateCourseCatalogView(learner.getId(), customerEntitlements,
		    courseAndCourseGroupService, search, "nothing", 0l);
	}

	log.debug("catalogMap.size() :: " + catalogMap.size());

	for (CourseGroupView grpView : courseGroups) {
	    for (CourseView courseView : grpView.getCourses()) {
		courseView.setCourseIconToShow(this.getCourseIconToShow(null, false, courseView.getCourseId().toString(), courseView.getType()));
	    }
	}
	return catalogMap;
    }

    public Map displayCourseDetailsPage(String learnerEnrollmentID, String crntEnrollmentId, com.softech.vu360.lms.vo.VU360User user, String activeTab, String viewType,
	    String selEnrollmentPeriod, Brander brand) {

	LearnerEnrollment learnerEnrollment = null;
	Course course = null;
	String hwcourseassignmentcoursestatus = null;
	String hwcourseassignmentscoringtype = null;
	boolean hwcourseassignmentcoursestatuscmpleted = false;
	boolean hwPass = false;
	boolean hwFail = false;
	boolean hwIncomplete = false;
	double courseMasterScore;
	int learnerScore;
	long hwfailid = 0;
	long hwincompleteid = 0;
	int hwassignmentcourseHighestMasteryScore = 0 ;
	int hwassignmentcourseLowestMasteryScore = 0 ;
	int hwassignmentcourseAverageMasteryScore = 0;
	LearnerCourseStatistics courseStatistics = null;
	List<LearnerCourseActivity> courseActivities = new ArrayList<LearnerCourseActivity>();
	List<Map<String, Object>> activitiyResults = new ArrayList<Map<String, Object>>();
	List<LearnerHomeworkAssignmentSubmission> lstLearnerHomeworkAssignmentSubmission = null;
	Map<Object, Object> context = new HashMap<Object, Object>();
	String instructorCoursetype = null;

	if (StringUtils.isNotEmpty(learnerEnrollmentID)) {

	    // Save current enrollment Id to context before proceeding further
	    // to exclude and preserver enrollment listed under CURRENT tab.
	    crntEnrollmentId = StringUtils.isNotBlank(crntEnrollmentId) ? crntEnrollmentId : learnerEnrollmentID;
	    learnerEnrollment = entitlementService.getLearnerEnrollmentById(Long.valueOf(crntEnrollmentId));

	    /*
	     * Check whether enrollment object which learner is looking for is
	     * enrolled with logged in learner To prevent changing from URL
	     */
	    if (user.getLearner().getId().longValue() == learnerEnrollment.getLearner().getId().longValue()) {

			List<LearnerEnrollment> similarEnrollments = this.getEnrollmentsByCourse(learnerEnrollment);
	
			if (activeTab.equalsIgnoreCase("prev")) {
			    // If PREVIOUS tab is selected, fetch the data for selected
			    // enrollment period or last enrollment by default
			    if (CollectionUtils.isNotEmpty(similarEnrollments)) {
			    	learnerEnrollmentID = StringUtils.isNotBlank(selEnrollmentPeriod) ? selEnrollmentPeriod : similarEnrollments
			    			.get(similarEnrollments.size() - 1).getId().toString();
			    }
			} else {
			    // For CURRENT tab, fetch the data for current enrollment
			    learnerEnrollmentID = crntEnrollmentId;
			}
			// Finally fetch the data to display enrollment details
			learnerEnrollment = entitlementService.getLearnerEnrollmentById(Long.valueOf(learnerEnrollmentID));
	
			course = learnerEnrollment.getCourse();
			if(course instanceof InstructorConnectCourse){
				if (((InstructorConnectCourse) course).getInstructorType().equals("Email Online")){
					instructorCoursetype = "Email Online";
				}
			}
			
			context.put("similarEnrollments", similarEnrollments);
			context.put("formatter", new SimpleDateFormat("MMM yy"));
			context.put("activeTab", activeTab);
			context.put("crntEnrollmentId", crntEnrollmentId);
			context.put("viewType", viewType);
			context.put("instructorCoursetype", instructorCoursetype);
			/*Reset Learner Course Statistics object for Learner Enrollment as it was coming from cache.*/
			learnerEnrollment.setCourseStatistics(statsService.getLearnerCourseStatisticsById(learnerEnrollment.getCourseStatistics().getId()));
			context.put("courseCompleted", learnerEnrollment.getCourseStatistics().isCourseCompleted());
	
			course = learnerEnrollment.getCourse();
	
			// Code by OWS for Stats regarding synchronous courses
			if (course.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || course.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
				//Not required to update any status at this stage for a synchronous type course
			}else if (course.getCourseType().equalsIgnoreCase(HomeworkAssignmentCourse.COURSE_TYPE)) {
			    if(course instanceof HomeworkAssignmentCourse ){
			    	HomeworkAssignmentCourse hw = (HomeworkAssignmentCourse) course;
			    	hwcourseassignmentscoringtype = hw.getGradingMethod();
			    	LearnerHomeworkAssignmentSubmission learnerhomeworksubmission =  learnerHomeworkAssignmentSubmissionService.getLearnerHomeworkAssignmentSubmission(learnerEnrollment);
			    	lstLearnerHomeworkAssignmentSubmission =  learnerHomeworkAssignmentSubmissionService.getLearnerHomeworkAssignmentSubmissionMastery(learnerEnrollment);
			    	
			    	if(learnerhomeworksubmission != null){
			    		if(lstLearnerHomeworkAssignmentSubmission != null){
					    	if(hw.getGradingMethod().equals("Simple")){
					    		for (LearnerHomeworkAssignmentSubmission lhwAS : lstLearnerHomeworkAssignmentSubmission){
					    			if(lhwAS.getPercentScore() != null){
					    				if(lhwAS.getPercentScore().equals("Fail")){
							    			hwFail = true;
							    			hwfailid = lhwAS.getId();
					    				}
					    				if(lhwAS.getPercentScore().equals("Pass")){
							    			hwPass = true;
							    		}
					    				if(lhwAS.getPercentScore().equals("Incomplete")){
							    			hwIncomplete = true;
							    			hwincompleteid = lhwAS.getId();
							    		}
					    			}
					    		}
					    		
					    		if(hwPass){
					    			hwcourseassignmentcoursestatus = "Passed";
					    			hwcourseassignmentcoursestatuscmpleted = true;
					    		}else if(!hwPass){
					    			if(hwFail && !hwIncomplete){
				    					hwcourseassignmentcoursestatus = "Failed";
						    			hwcourseassignmentcoursestatuscmpleted = true;
					    			}else if(!hwFail && hwIncomplete){
					    				hwcourseassignmentcoursestatus = "Incomplete";
						    			hwcourseassignmentcoursestatuscmpleted = false;
					    			}else if(hwFail && hwIncomplete){
					    				if(hwfailid > hwincompleteid){
					    					hwcourseassignmentcoursestatus = "Failed";
							    			hwcourseassignmentcoursestatuscmpleted = true;
					    				}else if(hwfailid < hwincompleteid){
							    			hwcourseassignmentcoursestatus = "Incomplete";
							    			hwcourseassignmentcoursestatuscmpleted = false;
					    				}
					    			}
					    		}
					    		
					    	  }else if(hw.getGradingMethod().equals("Scored")){
					    		for (LearnerHomeworkAssignmentSubmission lhwAS : lstLearnerHomeworkAssignmentSubmission){
						    		hwassignmentcourseHighestMasteryScore = highestPostMasteryScore(lstLearnerHomeworkAssignmentSubmission);
						    		hwassignmentcourseLowestMasteryScore = lowestPostMasteryScore(lstLearnerHomeworkAssignmentSubmission);
						    		hwassignmentcourseAverageMasteryScore = averagePostMasteryScore(lstLearnerHomeworkAssignmentSubmission);
		
					    			if(lhwAS.getPercentScore() != null){
					    				courseMasterScore = hw.getMasteryScore();
					    				if(!lhwAS.getPercentScore().equals("Incomplete")){
								    		learnerScore = Integer.parseInt(lhwAS.getPercentScore());
								    		if(courseMasterScore == learnerScore || learnerScore > courseMasterScore){
								    			hwPass = true;
								    		}else if(learnerScore < courseMasterScore){
								    			hwFail = true;
								    		}
							    		}else{
						    			  hwIncomplete = true;
						    		  }
					    			}
					    		}
					    		
					    		if(hwPass){
					    			hwcourseassignmentcoursestatus = "Passed";
					    			hwcourseassignmentcoursestatuscmpleted = true;
					    		}else if(!hwPass){
					    			if(hwFail && !hwIncomplete){
				    					hwcourseassignmentcoursestatus = "Failed";
						    			hwcourseassignmentcoursestatuscmpleted = true;
					    			}else if(!hwFail && hwIncomplete){
					    				hwcourseassignmentcoursestatus = "Incomplete";
						    			hwcourseassignmentcoursestatuscmpleted = false;
					    			}
					    		}
					    	}
			    		}
			    	}
			    }
			}else if (course.getCourseType().equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
			    LearnerCourseStatistics newCourseStats = statsService.getLearnerCourseStatisticsForLearnerEnrollment(learnerEnrollment);
			    newCourseStats.setPercentComplete(this.getPercentCompleteForSyncCourse(learnerEnrollment));
			    boolean sendCompletionMail = false;
			    if (newCourseStats.getPercentComplete() == 100 && !newCourseStats.isCourseCompleted()) {
					newCourseStats.setCompleted(true);
					newCourseStats.setStatus(LearnerCourseStatistics.COMPLETED);
					sendCompletionMail = true;
			    }
			    statsService.saveLearnerCourseStatistics(newCourseStats);
			    if (sendCompletionMail) {
			    	learnersToBeMailedService.emailCourseCompletionCertificate(learnerEnrollment.getId());
			    }
			} else if(course.getCourseType().equalsIgnoreCase(AICCCourse.COURSE_TYPE)) {
				log.info("AICC Course stats setting for more details");
				LearnerCourseStatistics newCourseStats = statsService.getLearnerCourseStatisticsForLearnerEnrollment(learnerEnrollment);
				boolean sendCompletionMail = false;
				if(newCourseStats.getPercentComplete() == 100 && !newCourseStats.isCourseCompleted()) {
					newCourseStats.setCompleted(true);
					newCourseStats.setStatus(LearnerCourseStatistics.COMPLETED);
					sendCompletionMail = true;
				}
				if(sendCompletionMail) {
					learnersToBeMailedService.emailCourseCompletionCertificate(learnerEnrollment.getId());
				}
			}
			courseStatistics = statsService.getLearnerCourseStatisticsForLearnerEnrollment(learnerEnrollment);
			// end by OWS
	
			/*
			 * Added by Dyutiman :: getting activity details for the
			 * particular learner.
			 */
			courseActivities = statsService.getCourseActivitiesFromCourseStatistics(courseStatistics.getId());
			for (LearnerCourseActivity lca : courseActivities) {
			    Map<String, Object> result = new HashMap<>();
			    if (lca.getCourseActivity() != null) {
			    	result.put("actName", lca.getCourseActivity().getActivityName());
			    }
			    if (lca instanceof LearnerLectureCourseActivity) {
					if (((LearnerLectureCourseActivity) lca).isAttended()){
					    result.put("actScore", "Attented");
					}else{
					    result.put("actScore", "Not-Attented");
					}
			    } else if (lca instanceof LearnerSelfStudyCourseActivity) {
			    	result.put("actScore", ((LearnerSelfStudyCourseActivity) lca).getOverrideScore());
			    } else if (lca instanceof LearnerAssignmentActivity) {
			    	result.put("actScore", ((LearnerAssignmentActivity) lca).getPercentScore());
			    } else if (lca instanceof LearnerFinalCourseActivity) {
			    	result.put("actScore", ((LearnerFinalCourseActivity) lca).getFinalPercentScore());
			    }
			    	activitiyResults.add(result);
				}
	
			/*
			 * checking for weblink course implementing LMS-3056
			 */
			Course c = learnerEnrollment.getCourse();
			log.debug("courseType=>" + c.getCourseType());
			log.debug("course class=>" + learnerEnrollment.getCourse().getClass().getName());
			context.put("hwcourseassignmentcoursestatus", hwcourseassignmentcoursestatus);
			context.put("hwcourseassignmentcoursestatuscmpleted", hwcourseassignmentcoursestatuscmpleted);
			context.put("hwcourseassignmenthighestscore", hwassignmentcourseHighestMasteryScore);
			context.put("hwcourseassignmentlowestscore", hwassignmentcourseLowestMasteryScore);
			context.put("hwcourseassignmentscoringtype", hwcourseassignmentscoringtype);
			context.put("hwassignmentcourseAverageMasteryScore", hwassignmentcourseAverageMasteryScore);
			context.put("courseType", c.getCourseType());
			context.put("courseIconToShow", this.getCourseIconToShow(null, false, c.getId().toString(), c.getCourseType()));
	
			context.put("learnerEnrollment", learnerEnrollment);
			context.put("course", course);
			context.put("courseStatistics", courseStatistics);
			context.put("userName", user.getFirstName() + " " + user.getLastName());
			context.put("activities", activitiyResults);
	
			// By OWS on 11/09/2009 for Schedule of Course in case its a
			// synchronous course
			try {
			    SynchronousClass synchronousClass = learnerEnrollment.getSynchronousClass();
			    if(synchronousClass != null){
			    	List<SynchronousSession> synchronousSessions = synchronousClassService.getSynchronousSessionsByClassId(synchronousClass.getId());
				    TimeZone learnerTimeZone=null;
				    com.softech.vu360.lms.vo.TimeZone timeZoneVO= user.getLearner().getLearnerProfile().getTimeZone();
		
				    if(timeZoneVO!=null){
				    	learnerTimeZone=timeZoneRepository.findOne(timeZoneVO.getId());
				    }
				    synchronousClass.setCurrentlyInSession(synchronousClassService.checkIfSessionInProgress(synchronousSessions,synchronousClass.getTimeZone(), learnerTimeZone, brand));
		
				    context.put("syncSessions", synchronousSessions);
				    context.put("syncClass", synchronousClass);
			    }
	
			}catch (Exception e) {
			    log.error("Exception while getting Schedule for Sync Course", e);
			}
	    }
	}

	return context;
    }

    @Override
    public Map<Object, Object> displayCourseSampleCompletionReport(VU360User user, String courseId, String viewType) {
		Map<Object, Object> context = new HashMap<>();
	
		LearnerEnrollment learnerEnrollment = null;
		LearnerCourseStatistics courseStatistics = null;
		CourseConfiguration courseCompletionCriteria = null;
	
		// no enrollment yet - just look up the course
		Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(courseId));
		courseStatistics = null;
		learnerEnrollment = null;
		// figure out if they have any surveys left to take for this course
	
		context.put("surveysRemaining", Boolean.TRUE);
		context.put("learnerEnrollment", learnerEnrollment);
		context.put("course", course);
		context.put("viewType", viewType);
		context.put("courseStatistics", courseStatistics);
		context.put("courseCompletionCriteria", courseCompletionCriteria);
		context.put("userName", user.getFirstName() + " " + user.getLastName());
	
		return context;
    }

    public Integer averagePostMasteryScore (List<LearnerHomeworkAssignmentSubmission> lsthomeworkAssignmentSubmission){
    	int learnerScore;
    	int scorecounter = 0;
    	int learneraveragescore = 0;
    	int tmplearneraveragescore = 0;
    	for (LearnerHomeworkAssignmentSubmission lhas : lsthomeworkAssignmentSubmission) {
    	
    		if(lhas.getPercentScore() != null)
    		{
    			if(!lhas.getPercentScore().equals("Incomplete"))
    			{
		    		  learnerScore = Integer.parseInt(lhas.getPercentScore());
		    		  if(scorecounter == 0)
		    		  {
		    			  tmplearneraveragescore = learnerScore;
		    			  learneraveragescore = learnerScore;
		    			  scorecounter = scorecounter + 1;
		    			  
		    		  }
		    		  else if(scorecounter > 0)
		    		  {
		    			  tmplearneraveragescore = tmplearneraveragescore + learnerScore;
		    			  scorecounter = scorecounter + 1;
		    		  }
    			 }
    		 }
    	  }
    		
    	if(!lsthomeworkAssignmentSubmission.isEmpty()){
    		if(scorecounter != 0)
    			learneraveragescore = tmplearneraveragescore/scorecounter;
    	}
    	return learneraveragescore;
    }
    
    private Integer highestPostMasteryScore (List<LearnerHomeworkAssignmentSubmission> lsthomeworkAssignmentSubmission){
    	int learnerScore;
    	int scorecounter = 0;
    	int tmplearnerhigestscore = 0;
    	for (LearnerHomeworkAssignmentSubmission lhas : lsthomeworkAssignmentSubmission) {
    		if(lhas.getPercentScore() != null && !lhas.getPercentScore().equals("Incomplete")){
			  learnerScore = Integer.parseInt(lhas.getPercentScore());
			  if(scorecounter == 0){
				  tmplearnerhigestscore = learnerScore;
				  scorecounter = scorecounter + 1;
			  }else if(scorecounter > 0){
				  if(tmplearnerhigestscore > learnerScore){
					  scorecounter = scorecounter + 1;
				  }else if(tmplearnerhigestscore < learnerScore){
					  tmplearnerhigestscore = learnerScore;
					  scorecounter = scorecounter + 1;
				  }
			  }
    		}
    	}
    	return tmplearnerhigestscore;
    }
    
    public Integer lowestPostMasteryScore (List<LearnerHomeworkAssignmentSubmission> lsthomeworkAssignmentSubmission)
    {
    	int learnerScore;
    	int scorecounter = 0;
    	int tmplearnerlowestscore = 0;
    	for (LearnerHomeworkAssignmentSubmission lhas : lsthomeworkAssignmentSubmission) {
    		if(lhas.getPercentScore() != null && !lhas.getPercentScore().equals("Incomplete")){
    		  learnerScore = Integer.parseInt(lhas.getPercentScore());
    		  if(scorecounter == 0){
    			  tmplearnerlowestscore = learnerScore;
    			  scorecounter = scorecounter + 1;
    		  }else if(scorecounter > 0){
    			  if(tmplearnerlowestscore < learnerScore){
    				  scorecounter = scorecounter + 1;
    			  }else if(tmplearnerlowestscore > learnerScore){
    				  tmplearnerlowestscore = learnerScore;
    				  scorecounter = scorecounter + 1;
    			  }
    		  }
    		     	  
    		}
    	}
    	return tmplearnerlowestscore;
    }
    
    @Override
    public Map<Object, Object> displayCourseSampleDetails(VU360User user, String courseId, String viewType) {

		Map<Object, Object> context = new HashMap<>();
		Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(courseId));
		/*
		 * checking for weblink course implementing LMS-3056
		 */
		log.debug("courseType=>" + course.getCourseType());
		log.debug("course class=>" + course.getClass().getName());
		context.put("courseType", course.getCourseType());
		context.put("courseIconToShow", this.getCourseIconToShow(null, false, course.getId().toString(), course.getCourseType()));
		context.put("viewType", viewType);
		context.put("course", course);
		context.put("userName", user.getFirstName() + " " + user.getLastName());
	
		return context;
    }

    @Override
    public Map<Object, Object> displayScheduleToEnroll(VU360User user, Brander brand, Long courseId, String viewType) {
		// [09/07/2010] LMS-6858 :: Get Valid Classes for Learner Enrollment by
		// Synchronous Course Id.
		List<SynchronousClass> synchronousClasses = null;
		synchronousClasses = synchronousClassService.getSynchronousClassesForEnrollment(courseId, 1);
	
		if (synchronousClasses != null) {
		    for (SynchronousClass synchronousClass : synchronousClasses) {
	
			synchronousClass.setSynchronousSessions(synchronousClassService.getSynchronousSessionsByClassId(synchronousClass.getId()));
			synchronousClassService.checkIfSessionInProgress(synchronousClass.getSynchronousSessions(), synchronousClass.getTimeZone(), user
				.getLearner().getLearnerProfile().getTimeZone(), brand);
		    }
		}
	
		Map<Object, Object> context = new HashMap<>();
		Course course = courseAndCourseGroupService.getCourseById(courseId);
		boolean alreadyEnrolled = false;
	
		LearnerEnrollment existingEnrollment = entitlementService.getLearnerEnrollmentsForLearner(user.getLearner(), course);
		if (existingEnrollment != null && existingEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.ACTIVE)) {
		    alreadyEnrolled = true;
		}
	
		context.put("course", course);
		context.put("courseIconToShow", this.getCourseIconToShow(null, false, course.getId().toString(), course.getCourseType()));
		context.put("syncClasses", synchronousClasses);
		context.put("viewType", viewType);
		context.put("alreadyEnrolled", alreadyEnrolled);
		context.put("userName", user.getFirstName() + " " + user.getLastName());
	
		return context;
    }

    @Override
	public Map<Object, Object> displayViewSchedule(VU360User user, Brander brand, String courseId, String strLearnerEnrollmentId) {
        List<SynchronousClass> syncClasses = synchronousClassService.getAllSynchClassesOfCourse(Long.valueOf(courseId));        
        List<SynchronousSession> syncSchedules = new  ArrayList<>();
        long learnerEnrollmentId= strLearnerEnrollmentId==null?0L:Long.parseLong(strLearnerEnrollmentId);
        LearnerEnrollment learnerEnrollment = entitlementService.getLearnerEnrollmentById(learnerEnrollmentId);
        Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(courseId));
        
        if((course instanceof SynchronousCourse || course instanceof WebinarCourse) && 
        		(learnerEnrollmentId<=0)){//RWA: If Learner is not yet enrolled in the course, show all schedules
            for (SynchronousClass syncClass : syncClasses) {
                List<SynchronousSession> syncSessions = synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId());
                boolean sessionInProgress = synchronousClassService.checkIfSessionInProgress(syncSessions,
                   syncClass.getTimeZone(),user.getLearner().getLearnerProfile().getTimeZone(),brand);
                syncClass.setCurrentlyInSession(sessionInProgress);
                
                // Rehan Rana
                // LMS-10358 - Creating this collection because of collecting all schedules of the given courseId
                for(SynchronousSession SyncSess : syncSessions){
                	syncSchedules.add(SyncSess);
                }
            }
        }
        if(course instanceof InstructorConnectCourse && learnerEnrollmentId<=0){//RWA: If Learner is not yet enrolled in the course, show all schedules
            for (SynchronousClass syncClass : syncClasses) {
                List<SynchronousSession> syncSessions = synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId());
                boolean sessionInProgress = synchronousClassService.checkIfSessionInProgress(syncSessions,
                   syncClass.getTimeZone(),user.getLearner().getLearnerProfile().getTimeZone(),brand);
                syncClass.setCurrentlyInSession(sessionInProgress);
                
                // Rehan Rana
                // LMS-10358 - Creating this collection because of collecting all schedules of the given courseId
                for(SynchronousSession SyncSess : syncSessions){
                	syncSchedules.add(SyncSess);
                }
            }
        }else{
            for (SynchronousClass syncClass : syncClasses) {
                List<SynchronousSession> syncSessions = synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId());
                boolean sessionInProgress = synchronousClassService.checkIfSessionInProgress(syncSessions,
                   syncClass.getTimeZone(),user.getLearner().getLearnerProfile().getTimeZone(),brand);
                syncClass.setCurrentlyInSession(sessionInProgress);
            }

            /*
        	 * RWA: LMS-15663
        	 */
            if(learnerEnrollment!=null){
	        	SynchronousClass synchronousClass = learnerEnrollment.getSynchronousClass();
	    	    if(synchronousClass!=null){
	    	    	syncSchedules = synchronousClassService.getSynchronousSessionsByClassId(synchronousClass.getId());
	    	    	synchronousClass.setCurrentlyInSession(synchronousClassService.checkIfSessionInProgress(syncSchedules,
	    				    synchronousClass.getTimeZone(), user.getLearner().getLearnerProfile().getTimeZone(), brand));
	    	    }
            }
        }
		Map<Object, Object> context = new HashMap<>();
		context.put("syncSessions", syncSchedules);
		context.put("course", course);
		context.put("syncClasses", syncClasses);
		context.put("courseType", course.getCourseType());
		context.put("userName", user.getFirstName() + " " + user.getLastName());
		return context;
    }
	
    private List<CourseGroupView> courseGroupAndCoursesForView(String search, Learner learner) {
		List<CustomerEntitlement> customerEntitlements = new ArrayList<>();
		List<CourseGroupView> courseGroups = MyCoursesCourseGroup.generateCourseCatalogView(learner.getId(), customerEntitlements, courseAndCourseGroupService, search);
		for (CourseGroupView grpView : courseGroups) {
		    for (CourseView courseView : grpView.getCourses()) {
		    	courseView.setCourseIconToShow(this.getCourseIconToShow(null, false, courseView.getCourseId().toString(), courseView.getType()));
		    }
		}
		return courseGroups;
    }

    private void checkCurrentlyInSession(VU360User user, Brander brand, List<MyCoursesItem> filteredMyCourses) {
		if (filteredMyCourses != null) { // not null only for recent courses
		    for (MyCoursesItem mci : filteredMyCourses) {
				String courseStatus = (String) mci.get("courseStatus");
				String courseCategoryType = (String) mci.get("courseCategoryType");
				if (courseStatus.equals("completed")) { // if course is already completed
				    if (courseCategoryType.equalsIgnoreCase("Weblink")) {
					// if it is a weblink course
					log.debug("Weblink course found - courseCategoryType --> " + courseCategoryType);
					mci.put("courseCategoryType", "Weblink");
				    }
				    try {
						if (courseCategoryType.equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) 
								|| courseCategoryType.equalsIgnoreCase(WebinarCourse.COURSE_TYPE)
								|| courseCategoryType.equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
						    LearnerEnrollment le = (LearnerEnrollment) mci.get("enrollment");
						    if (le != null) {
							boolean currentlyInSession = false;
							if (le.getSynchronousClass() != null)
							    currentlyInSession = synchronousClassService.checkIfSessionInProgress(synchronousClassService
								    .getSynchronousSessionsByClassId(le.getSynchronousClass().getId()), le.getSynchronousClass()
								    .getTimeZone(), user.getLearner().getLearnerProfile().getTimeZone(), brand);
			
							mci.put("syncInProgress", currentlyInSession);
							mci.put("courseIconToShow",
								getCourseIconToShow(le, currentlyInSession, mci.get("courseIdKey").toString(), mci.get("courseCategoryType")
									.toString()));
						    }
						}
				    } catch (Exception e) {
				    	log.error("Exception checking in progress: " + e.getMessage(),e);
				    }
				}
		    }
		}
    }

    // Cyclomatic Complexity of this method decreased from 49 to 42
    private String getCourseIconToShow(LearnerEnrollment le, boolean currentlyInSession, String strCourseId, String courseType) {
		if (le != null) {
		    if (le.getSynchronousClass() != null) {
				if (le.getSynchronousClass().getMeetingType() != null) {
				    if (currentlyInSession && !le.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED))
				    	return "lms.pixel.iconOnlineAnimatedSync";
				    else
				    	return "lms.pixel.iconOnlineSync";// virtual classroom icon
				} else {
				    if (le.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED))
				    	return "lms.pixel.dimIconOffineSync";// classroom icon
		
				    return "lms.pixel.iconOffineSync";
				}
	
		    } else if (courseType.equalsIgnoreCase(DiscussionForumCourse.COURSE_TYPE)) {
				if (le.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED))
				    return "lms.pixel.dimIconDFC";
		
				return "lms.pixel.iconDFC";
		    } else if (courseType.equalsIgnoreCase(WebLinkCourse.COURSE_TYPE)) {
				if (le.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED))
				    return "lms.pixel.dimIconWeblink";
		
				return "lms.pixel.iconWeblink";
		    } else if (courseType.equalsIgnoreCase(SelfPacedCourse.COURSE_TYPE) || courseType.equalsIgnoreCase(ScormCourse.COURSE_TYPE)) {
				if (le.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED))
		    		return "lms.pixel.dimIconSelfPaced";
		
				return "lms.pixel.iconSelfPaced";
		    } else if (courseType.equalsIgnoreCase(LegacyCourse.COURSE_TYPE)) {
		    	if (le.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED))
		    		return "lms.pixel.dimIconSelfPacedLegacy";
	
		    	return "lms.pixel.iconSelfPaced";
		    } else if (courseType.equalsIgnoreCase(HomeworkAssignmentCourse.COURSE_TYPE)) {
		    	if (le.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED))
		    		return "lms.pixel.dimHomeworkAssignment";
	
		    	return "lms.pixel.homeworkAssignment";
		    } else if (courseType.equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
		    	if (le.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED))
		    		return "lms.pixel.dimInstructorConnect";
	
		    	return "lms.pixel.instructorConnect";
		    }
		    // LMS-11223 -- Changed by - Rehan Rana ----   Start   -----
		    // set the correct Icons for Online and offline Synchronous Courses  
		    else if (courseType.equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || courseType.equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
				boolean isOnline = false;
				List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassByCourseId(Long.parseLong(strCourseId));
				if (syncClasses == null || syncClasses.isEmpty())
				    return "lms.pixel.iconOnlineGif";
	
				for (SynchronousClass syncClass : syncClasses) {
				    if (syncClass.getMeetingType() != null) {// i.e. this is online
				    	isOnline = true;
				    	break;
				    }
				}
				
				return isOnline? "lms.pixel.iconOnlineSync" : "lms.pixel.iconOffineSync";
			}
		    // LMS-11223 -------   END   ------------
		    else{ // i.e. its a non sync course
		    	return "lms.pixel.iconOnlineGif";// this is true ultimately.
		    }
		} else {// i.e. this may be for catalog courses since no enrollment
		    // Now check if this course has any sync class associated with it
		    if (courseType == null) {
		    	return "lms.pixel.iconOnlineGif";
		    } else if (courseType.equalsIgnoreCase(DiscussionForumCourse.COURSE_TYPE)) {
		    	return "lms.pixel.iconDFC";
		    } else if (courseType.equalsIgnoreCase(HomeworkAssignmentCourse.COURSE_TYPE)) {
		    	return "lms.pixel.homeworkAssignment";
		    } else if (courseType.equalsIgnoreCase(WebLinkCourse.COURSE_TYPE)) {
		    	return "lms.pixel.iconWeblink";
		    } else if (courseType.equalsIgnoreCase(SelfPacedCourse.COURSE_TYPE) 
		    		|| courseType.equalsIgnoreCase(ScormCourse.COURSE_TYPE) || courseType.equalsIgnoreCase(LegacyCourse.COURSE_TYPE)) {
		    	return "lms.pixel.iconSelfPaced";
		    } else if (courseType.equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || courseType.equalsIgnoreCase(WebinarCourse.COURSE_TYPE) 
		    		|| courseType.equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
				boolean isOnline = false;
				List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassByCourseId(Long.parseLong(strCourseId));
				if (syncClasses == null || syncClasses.isEmpty())
				    return "lms.pixel.iconOnlineGif";
		
				for (SynchronousClass syncClass : syncClasses) {
				    if (syncClass.getMeetingType() != null) {// i.e. this is online
						isOnline = true;
						break;
				    }
				}
				if (isOnline)
				    return "lms.pixel.iconOnlineSync";
				else
				    return "lms.pixel.iconOffineSync";
		    }
		    return "lms.pixel.iconOnlineGif";
		}
    }

    private double getPercentCompleteForSyncCourse(LearnerEnrollment learnerEnrollment) {
		SynchronousClass syncClass = learnerEnrollment.getSynchronousClass();
		int totalSessions = 0;
		int totalCompleted = 0;
		if (syncClass != null) {
			List<SynchronousSession> sessions = synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId());
		    totalSessions = sessions.size();
		    for (SynchronousSession session : sessions) {
				if (session.getStatus().equalsIgnoreCase(SynchronousSession.COMPLETED)) {
				    totalCompleted++;
				}
		    }
	
		}
	
		return (totalCompleted / (totalSessions == 0 ? 1 : totalSessions)) * 100;
    }

    public StatisticsService getStatsService() {
    	return statsService;
    }

    public void setStatsService(StatisticsService statsService) {
    	this.statsService = statsService;
    }

    private List<LearnerEnrollment> getEnrollmentsByCourse(LearnerEnrollment learnerEnrollment) {
		// Fetch enrollments whose end date is less than start date of currently selected enrollment
		List<LearnerEnrollment> learnerEnrollmentList = this.getEnrollmentsByCourseID(learnerEnrollment.getLearner().getId(), learnerEnrollment
			.getCourse().getId(), learnerEnrollment.getEnrollmentStartDate());
		if (!learnerEnrollmentList.isEmpty() && learnerEnrollmentList.size() > 0) {
		    for (int index = 0; index < learnerEnrollmentList.size(); index++) {
				// Filter out Current ID enrollment
				if (learnerEnrollmentList.get(index).getId().equals(learnerEnrollment.getId())) {
				    learnerEnrollmentList.remove(index);
				    break;
				}
		    }
		}
		return learnerEnrollmentList;
    }

    // END MyCoursesController Logic Refactoring

    /**
     * // [1/27/2011] LMS-7183 :: Retired Course Functionality II (to enroll
     * learner in Learner Group from Self Registration Invitations
     */
    @Override
    public void enrollLearnerToLearnerGroupItems(VU360User vu360User, LearnerGroup learnerGroup) {
		if (CollectionUtils.isNotEmpty(learnerGroup.getLearnerGroupItems())) {
		    for (LearnerGroupItem item : learnerGroup.getLearnerGroupItems()) {
				Course course = item.getCourse();
				if (course.isActive()) {
				    String synClassId = null;
				    boolean doEnrollment = true;
				    if (course instanceof SynchronousCourse || course instanceof WebinarCourse
				    		|| course instanceof InstructorConnectCourse) {
						List<SynchronousClass> synClasses = this.synchronousClassService.getSynchronousClassesForEnrollment(course.getId(), 1, false);
						if (CollectionUtils.isNotEmpty(synClasses)) {
						    synClassId = synClasses.get(0).getId().toString();
						} else {
						    doEnrollment = false;
						}
				    }
				    if (doEnrollment) {
				    	this.selfEnrollTheLearner(vu360User, course.getId().toString(), synClassId);
				    }
				}
		    }
		}
    }

    public Map<Long, CourseGroupView> transformListIntoMap(List<CourseGroupView> list) {
		Map<Long, CourseGroupView> map = new HashMap<>(list.size());
		Long id;
		for (CourseGroupView view : list) {
		    id = view.getId();
		    if (map.get(id) == null) {
		    	map.put(id, view);
		    }
		}
		return map;
    }
    
    @Override
	public void updateAutoSynchronousCourseStatus(Learner learner) {
		try{
			List<LearnerEnrollment> learnerEnrollments = learnerEnrollmentRepository.findByLearnerIdAndCourseType(learner.getId());
			for (LearnerEnrollment learnerEnrollment : learnerEnrollments) {
				SynchronousClass enrolledClass = learnerEnrollment.getSynchronousClass();
				if(enrolledClass!=null){					
					LearnerCourseStatistics learnerStat = learnerEnrollment.getCourseStatistics();
					Date currentDateTime = DateUtil.getCurrentServerTimeGMT();
					Date enrollmentStartDate = enrolledClass.getClassStartDate();
					Date enrollmentEndDate = enrolledClass.getClassEndDate();
					if(enrolledClass.getTimeZone()!=null && currentDateTime.compareTo(DateUtil.getGMTDateForTimezoneHours(enrollmentStartDate,enrolledClass.getTimeZone().getHours()))>=0 &&
						learnerStat.getStatus().equals(LearnerCourseStatistics.NOT_STARTED)){//Set to inProgress
						learnerStat.setStatus(LearnerCourseStatistics.IN_PROGRESS);
						statsService.updateLearnerCourseStatistics(learnerStat.getId(),learnerStat);
					}
					if(enrolledClass.getTimeZone()!=null && currentDateTime.compareTo(DateUtil.getGMTDateForTimezoneHours(enrollmentEndDate,enrolledClass.getTimeZone().getHours()))>=0 && 
						!learnerStat.getStatus().equals(LearnerCourseStatistics.COMPLETED)
						/**
						 * added by muhammad akif for -- LMS-16163
						 * for course completion automatic/manual
						 */
						&& enrolledClass.isAutomatic()){
						learnerStat.setStatus(LearnerCourseStatistics.COMPLETED);
						learnerStat.setCompleted(true);
						learnerStat.setCompletionDate(enrolledClass.getClassEndDate());
						learnerStat.setPercentComplete(100);
						
						statsService.updateLearnerCourseStatistics(learnerStat.getId(),learnerStat);
						learnersToBeMailedService.emailCourseCompletionCertificate(learnerEnrollment.getId());
					}
				}
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
	}
    
    @Override
	public Subscription addPreEnrollmentsForSubscription(Learner learner, Subscription subscription){
		Set<VU360User> lstvu360User = new HashSet<>();
		lstvu360User.add(learner.getVu360User());
    	subscription.setVu360User(lstvu360User);
    	return subscriptionService.updateSubscription(subscription);
	}
    
    @Override
	public LearnerEnrollment addSelfEnrollmentsForSubscription(Learner learner, String subscriptionId, String courseId) {
		LearnerEnrollment le = this.getActiveLearnerEnrollment(learner.getId(), Long.valueOf(courseId));
		if (le == null) {
			Subscription subscription = subscriptionService.findSubscriptionById(Long.parseLong(subscriptionId));
			Course course = courseAndCourseGroupService.getCourseById(Long.parseLong(courseId));
			CustomerEntitlement ce = subscription.getCustomerEntitlement();
			Date today = new Date(System.currentTimeMillis());
			le = new LearnerEnrollment();
			le.setCourse(course);
			le.setEnrollmentStartDate(today);
			le.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
			le.setEnrollmentEndDate(getEndDateOffset(today, (99 * 365))); // As per story ticket, enrollment should be valid till 99 years
			le.setCustomerEntitlement(ce);
			le.setLearner(learner);
			le.setSubscription(subscription);

			if (course.getCourseType().equals(SynchronousCourse.COURSE_TYPE)
					|| course.getCourseType().equals(WebinarCourse.COURSE_TYPE)) {
				log.info("This is Synchronous Course");
				List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassByCourseId(course.getId());
				if (CollectionUtils.isNotEmpty(syncClasses)) {
					SynchronousClass syncClass = syncClasses.get(0);
					if (syncClass != null) {
						le.setSynchronousClass(syncClass);
						log.info("enrolled into:" + syncClass.getSectionName() + "(" + syncClass.getId() + ")");
					}
				}
				
			}
			le = addSelfEnrollmentForSubscription(learner, ce, le);
			return le;
		}
		return le;
	}
	
	public List<LearnerEnrollment> addEnrollmentsForSubscription(Learner learner, Subscription subscription, String subItem) {
    	List<LearnerEnrollment> enrollments = new ArrayList<LearnerEnrollment>();
    	// make no assumptions about where this entitlement came from
    	subscription = subscriptionService.findSubscriptionById(subscription.getId());
    	CustomerEntitlement ce = subscription.getCustomerEntitlement();
    	if (ce.hasAvailableSeats(1)) {
    		// enroll them in anything we can find in the entitlement!
    		List<SubscriptionCourse> subscriptionCourses = subscription.getSubscriptionCourses();
		    Date today = new Date(System.currentTimeMillis());
		    for (SubscriptionCourse sc : subscriptionCourses) {
			    LearnerEnrollment le = new LearnerEnrollment();
		    	Course c = sc.getCourse();
		    	le.setCourse(c);
		    	le.setEnrollmentStartDate(today);
		    	le.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
		    	le.setEnrollmentEndDate(getEndDateOffset(today, (99*365))); // As per story ticket, enrollment should be valid till 99 years
				le.setCustomerEntitlement(ce);
				le.setLearner(learner);
				le.setSubscription(subscription);
				
				// For Sync Course
				if ((c.getCourseType().equals(SynchronousCourse.COURSE_TYPE) || c.getCourseType().equals(WebinarCourse.COURSE_TYPE)) 
						&& !subItem.equals("")) {
					log.info("This is Synchronous Course");
					SynchronousClass syncClass = null;
					if(subItem.equals("-1")) {
						log.info("LineItemGuid is -1, which means enroll user into very first class created for this course");
						List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassByCourseId(c.getId());
						if(syncClasses != null && syncClasses.size()>0) {
							syncClass = syncClasses.get(0);
							log.info("enrolled into:" + syncClass.getSectionName() + "("+ syncClass.getId() +")");
						}
					} else {
						syncClass = synchronousClassService.getSynchronousClassByGUID(subItem);
					}
				    if (syncClass != null) {
				    	le.setSynchronousClass(syncClass);
				    }
		
				}

				if (c.getCourseType().equals(InstructorConnectCourse.COURSE_TYPE) && !subItem.equals("")) {
				    SynchronousClass syncClass = synchronousClassService.getSynchronousClassByGUID(subItem);
				    if (syncClass != null) {
				    	le.setSynchronousClass(syncClass);
				    }
				}

				// add it to the db
				le = addEnrollment(learner, ce, null, le);
				enrollments.add(le);
		    }
    	}
    	
    	return enrollments;
    }
    
	@Override
	public boolean getviewAssessmentResults(long learnerEnrollmentId){
		return courseConfigurationTemplateRepository.findviewAssessmentResults(learnerEnrollmentId);
	}
	
	@Override
    public List<LearnerEnrollment> getLearnerEnrollmentsBySubscription(
			Long subscriptionId) {
		List<LearnerEnrollment> learnerList = learnerEnrollmentRepository.findBySubscriptionId(subscriptionId);
		return learnerList;
	}
    
    @Override
    public void marketoPacket(LearnerEnrollment le, String eventName){
    	/*
    	if(isMarketoEnabled){
	    	try {
				final String firstName = le.getLearner().getVu360User().getFirstName();
				final String lastName = le.getLearner().getVu360User().getLastName();
				final String emailAddress = le.getLearner().getVu360User().getEmailAddress();
				final String company = le.getLearner().getCustomer().getName();
				final String courseName = le.getCourse().getCourseTitle();
				final String storeName = le.getLearner().getCustomer().getDistributor().getName();
				final String event = eventName;
				final String customerType = le.getLearner().getCustomer().getCustomerType();
				String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				final String eventDate = simpleDateFormat.format(new Date());
				
		   		if((emailAddress!=null) && (emailAddress.length() > 0) && (FieldsValidation.isEmailValid(emailAddress))){
		   		  marketoJMSTemplate.send(new MessageCreator() {
		                 public TextMessage createMessage(Session session) throws JMSException {
		              	  TextMessage message = session.createTextMessage();
		               	  
		               	  JSONObject jsonObj = JSONObject.fromObject(new MarketoPacketSerialized(firstName,lastName,emailAddress,company,courseName,storeName,event,eventDate,customerType));
		               	  message.setText(jsonObj.toString());
		                     return message;
		                 }
		             });
		   		  
		   		}
		   		else if((emailAddress!=null) && (emailAddress.length() > 0)){
				  log.debug("Invalid Marketo Email address" + emailAddress);
				}
				
			} catch (Exception e) {
				log.error(e);
			}
    	}
    	*/
     }
	
	@Override
	public void enableCertificate(Long enrolmentIdArray[], boolean status){
    	List<LearnerEnrollment> le = learnerEnrollmentRepository.findByIdIn(enrolmentIdArray);
    	for (int i = 0; i < le.size(); i++)
			le.get(i).setEnableCertificate(status);
		learnerEnrollmentRepository.save(le);	
    }
    
	@Override
	public List<LearnerEnrollment> getLearnerEnrollmentByEnrollmentIds(List<Long> lstEnrollmentIds) {
		return learnerEnrollmentRepository.findByIdInAndEnrollmentStatus(lstEnrollmentIds, LearnerEnrollment.ACTIVE);
	}
	
	@Override
	public List<LearnerEnrollment> getLearerEnrollmentsByCourses(List<Long> lstCourseIds) {
		return learnerEnrollmentRepository.findByCourseIdInAndEnrollmentStatus(lstCourseIds, LearnerEnrollment.ACTIVE);
	}
	
	@Override
	public List<LearnerEnrollment> getLearerEnrollmentsByCourse(Long courseId) {
		return learnerEnrollmentRepository.findByCourseIdAndEnrollmentStatus(courseId, LearnerEnrollment.ACTIVE);
	}

	private List<MyCoursesCourseGroup> generateMyCoursesViewForLearnerEnrollmentsForView(List<LearnerEnrollment> learnerEnrollments) {
		long startTime=System.currentTimeMillis();
		
		List<MyCoursesCourseGroup> results = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(learnerEnrollments)) {
			List<TrainingPlanAssignment> trainingPlanAssignment = courseAndCourseGroupService.getTrainingPlanByEnrollments(learnerEnrollments);	
							
			HashMap<String,MyCoursesCourseGroup> cgMap= new HashMap<>();
			for (LearnerEnrollment learnerEnrollment : learnerEnrollments) {
				MyCoursesCourseGroup mcg;
				// [07/22/2010] LMS-6388 :: Hide retired courses from CourseGroup Contracts only
				CourseGroup leCourseGroup = getCourseGroupByEnrollment(learnerEnrollment, trainingPlanAssignment);
				mcg = cgMap.get(leCourseGroup.getName()); 
				if(mcg==null){
					mcg = new MyCoursesCourseGroup(leCourseGroup);
				}
				cgMap.put(leCourseGroup.getName(), mcg);
				MyCoursesItem mci = new MyCoursesItem(learnerEnrollment); 
				mcg.addCourse(mci);
				results.add(mcg);
			}			
		}
		
		long endTime=System.currentTimeMillis();
		log.debug("totalTimeTaken MyCoursesCourseGroup.generateMyCoursesView["+(endTime-startTime)+"]");
		log.debug("results.size:"+results.size());
		// sort the results
		Collections.sort(results);
		return results;
	}

	/**
	 * @author muzammil.shaikh
	 * Returns list of Course Groups and Training Plans mapped to unique courses with no courses repeating under any of the Course Group
	 * and Training Plan. 
	 * @param learnerEnrollments	List of Learner Enrollments
	 * @param courseAndCourseGroupService	Service object used for getting training plans for the enrollments
	 * @return	List<MyCoursesCourseGroup>	List of Course Groups and Training Plans with courses. 
	 */
	private List<MyCoursesCourseGroup> generateMyCoursesViewForLearnerEnrollments(List<LearnerEnrollment> learnerEnrollments) {
		long startTime=System.currentTimeMillis();
		
		List<MyCoursesCourseGroup> results = new ArrayList<MyCoursesCourseGroup>();
		/*
		 * Just to double check whether duplicate course is there for a learner enrollments
		 */
		if( CollectionUtils.isNotEmpty(learnerEnrollments)) {
			ArrayList<Long> toIgnoreEnrollmentIds = new ArrayList<>();
			
			Iterator<LearnerEnrollment> learnerEnrollmentsIterator = learnerEnrollments.iterator();
			while (learnerEnrollmentsIterator.hasNext()) {
				LearnerEnrollment enrollment = learnerEnrollmentsIterator.next();
				Course course = enrollment.getCourse();
				if(enrollment.getCourseStatistics()==null 
						|| enrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.SWAPPED)
						|| enrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.DROPPED)
							|| StringUtils.isBlank(course.getCourseStatus()) || !course.getCourseStatus().equalsIgnoreCase(Course.PUBLISHED)) {
					
					toIgnoreEnrollmentIds.add(enrollment.getId());
				}
			}
			List<TrainingPlanAssignment> trainingPlanAssignment = courseAndCourseGroupService.getTrainingPlanByEnrollments(learnerEnrollments);	
			HashMap<String,MyCoursesCourseGroup> cgMap= new HashMap<>();

			for (LearnerEnrollment learnerEnrollment : learnerEnrollments) {
				if (!toIgnoreEnrollmentIds.contains(learnerEnrollment.getId())) {
					MyCoursesCourseGroup mcg;
					// [07/22/2010] LMS-6388 :: Hide retired courses from CourseGroup Contracts only
					CourseGroup leCourseGroup = getCourseGroupByEnrollment(learnerEnrollment, trainingPlanAssignment);
					mcg = cgMap.get(leCourseGroup.getName()); 
					if(mcg==null){
						mcg = new MyCoursesCourseGroup(leCourseGroup);
					}
					cgMap.put(leCourseGroup.getName(), mcg);
					MyCoursesItem mci = new MyCoursesItem(learnerEnrollment); 
					
					mcg.addCourse(mci);
					results.add(mcg);
				}
			}			
		}
		
		long endTime=System.currentTimeMillis();
		log.debug("totalTimeTaken MyCoursesCourseGroup.generateMyCoursesView["+(endTime-startTime)+"]");
		log.debug("results.size:"+results.size());
		// sort the results
		Collections.sort(results);
		return results;
	}
	
	
// [07/22/2010] LMS-6388 :: Hide retired courses from CourseGroup Contracts only
	private CourseGroup getCourseGroupByEnrollment(LearnerEnrollment le, List<TrainingPlanAssignment> trainingPlanAssignment) {
		CourseGroup courseGroup=null;

		outer :	for(TrainingPlanAssignment assignment : trainingPlanAssignment){
			if(assignment.getLearnerEnrollments().contains(le)){
				String courseGroupName = "Training Plan: " + assignment.getTrainingPlan().getName();	
				courseGroup = new CourseGroup();
				courseGroup.setName(courseGroupName);
				courseGroup.setDescription(assignment.getTrainingPlan().getDescription());
				courseGroup.addCourse(le.getCourse());
				break outer;
			
			}
		}
			
		if(courseGroup!=null)
			return courseGroup;
		
		CustomerEntitlement objCustomerEntitlement = ORMUtils.initializeAndUnproxy(le.getCustomerEntitlement());
		
		if(objCustomerEntitlement instanceof CourseCustomerEntitlement){
			List<CourseCustomerEntitlementItem> items = entitlementService.findEntitlementItemsByCourse((CourseCustomerEntitlement)objCustomerEntitlement, le.getCourse().getId());
			if(CollectionUtils.isNotEmpty(items)){
				courseGroup = items.get(0).getCourseGroup();
			}
		}
		else if (objCustomerEntitlement instanceof CourseGroupCustomerEntitlement){
			//[8/31/2010] LMS-6931 :: Code refactored to get Course Group for Enrolled Courses
			List<CourseGroupCustomerEntitlementItem> items = entitlementService.findCourseGroupEntitlementItems((CourseGroupCustomerEntitlement) objCustomerEntitlement, le.getCourse().getId() );
			if(CollectionUtils.isNotEmpty(items)){
				courseGroup = items.get(0).getCourseGroup();
			}
		}else if (objCustomerEntitlement instanceof SubscriptionCustomerEntitlement){
			if(le.getSubscription()!=null){
				//[8/31/2010] LMS-6931 :: Code refactored to get Course Group for Enrolled Courses
				List<SubscriptionCourse> items = le.getSubscription().getSubscriptionCourses();
				if(CollectionUtils.isNotEmpty(items)){
					courseGroup = items.get(0).getCourseGroup();
				}
			}
		}
		
		if(courseGroup==null){
			log.debug("CREATING MISC COURSE GROUP");
			courseGroup = new CourseGroup();
			courseGroup.setName("Miscellaneous");
			courseGroup.setDescription("Miscellaneous Course Group");
			courseGroup.addCourse(le.getCourse());
		}
		return courseGroup;
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public VU360UserService getUserService() {
		return userService;
	}

	public void setUserService(VU360UserService userService) {
		this.userService = userService;
	}

	public LearnerHomeworkAssignmentSubmissionService getLearnerHomeworkAssignmentSubmissionService() {
		return learnerHomeworkAssignmentSubmissionService;
	}

	public void setLearnerHomeworkAssignmentSubmissionService(
			LearnerHomeworkAssignmentSubmissionService learnerHomeworkAssignmentSubmissionService) {
		this.learnerHomeworkAssignmentSubmissionService = learnerHomeworkAssignmentSubmissionService;
	}

	public SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}
	public JmsTemplate getMarketoJMSTemplate() {
	    return marketoJMSTemplate;
	}
	
	public void setMarketoJMSTemplate(JmsTemplate marketoJMSTemplate) {
		this.marketoJMSTemplate = marketoJMSTemplate;
	}
	
	public LearnerEnrollment addSubscriptionEnrollments(Learner learner, String subscriptionId, String courseId) {
    	List<LearnerEnrollment> enrollments = new ArrayList<LearnerEnrollment>();
    	Map<Object, Object> returnVal = new HashMap<Object, Object>();
    	LearnerEnrollment le = this.getActiveLearnerEnrollment(learner.getId(), Long.valueOf(courseId));
    	// make no assumptions about where this entitlement came from
    	if(le == null){
	    	Subscription subscription = subscriptionService.findSubscriptionById(Long.parseLong(subscriptionId));
	    	Course course = courseAndCourseGroupService.getCourseById(Long.parseLong(courseId));
	    	CustomerEntitlement ce = subscription.getCustomerEntitlement();
		    Date today = new Date(System.currentTimeMillis());
		    le = new LearnerEnrollment();
	        le.setCourse(course);
	    	le.setEnrollmentStartDate(today);
	    	le.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
	    	le.setEnrollmentEndDate(getEndDateOffset(today, (99*365))); // As per story ticket, enrollment should be valid till 99 years
			le.setCustomerEntitlement(ce);
			le.setLearner(learner);
			le.setSubscription(subscription);
			
			// For Sync Course
			if ((course.getCourseType().equals(SynchronousCourse.COURSE_TYPE) || course.getCourseType().equals(WebinarCourse.COURSE_TYPE))) 
			{
				log.info("This is Synchronous Course");
				SynchronousClass syncClass = null;
				List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassByCourseId(course.getId());
				if(syncClasses != null && syncClasses.size()>0) {
						syncClass = syncClasses.get(0);
						log.info("enrolled into:" + syncClass.getSectionName() + "("+ syncClass.getId() +")");
					}
				if (syncClass != null) {
			    	le.setSynchronousClass(syncClass);
			    }
			} 
			// add it to the db
			le = addSubscriptionEnrollment(learner, ce, null, le);
	        return le; 
    	}
    	if(le != null)
		    return le;
    	
    	return null;
	}
	
	//addSubscriptionEnrollment
	
	public LearnerEnrollment addSubscriptionEnrollment(Learner learner, CustomerEntitlement origCustEntitlment, OrgGroupEntitlement orgGrpEntl,
		    LearnerEnrollment le) {

			try {
			    Date today = new Date(System.currentTimeMillis());
			    // business logic of adding an enrollment to the system
			    // CustomerEntitlement origCustEntitlment =
			    // entitlementService.getCustomerEntitlementById(custEnt.getId());
			    Learner origLearner = learnerService.getLearnerByID(learner.getId());
			    LearnerCourseStatistics courseStats = new LearnerCourseStatistics();
			    courseStats.setStatus(LearnerCourseStatistics.NOT_STARTED);
			    le.setLearner(origLearner);
			    //origCustEntitlment.setNumberSeatsUsed(origCustEntitlment.getNumberSeatsUsed() + 1);
			    le.setCustomerEntitlement(origCustEntitlment);
			    le.setCourseStatistics(courseStats);
			    courseStats.setLearnerEnrollment(le);
			    le.setEnrollmentDate(today);
		
			    // [10/13/2010] LMS-7196 :: Update Org. Group Contract Seats Used on
			    // Learner Enrollment
			    if (orgGrpEntl != null) {
			    	le.setOrgGroupEntitlement(orgGrpEntl);
			    }
		
			    LearnerEnrollment learnerEnrollment = learnerEnrollmentRepository.save(le);
		
			    // now update the customer entitlement object
			    // origCustEntitlment.setNumberSeatsUsed(origCustEntitlment.getNumberSeatsUsed()+1);
			     entitlementService.saveCustomerEntitlement(origCustEntitlment,null);
		
			    // if ( orgGrpEntl != null ) {
			    // OrgGroupEntitlement origOrgGroupEntitlement =
			    // entitlementService.getOrgGroupEntitlementById(orgGrpEntl.getId());
			    // origOrgGroupEntitlement.setNumberSeatsUsed(origOrgGroupEntitlement.getNumberSeatsUsed()+1);
			    // entitlementService.saveOrgGroupEntitlement(origOrgGroupEntitlement);
			    // }
		
			    return learnerEnrollment;
			} catch (Exception ex) {
			    log.error(ex.getMessage(), ex);
			}
			return null;
	    }

	@Override
	public List<LearnerEnrollment> findByLearnerIdAndEnrollmentStatusAndCourseStatisticsStatusIn(long learnerId,
			String status, Set<String> courseStatisticsStatuses) {
		return learnerEnrollmentRepository.findByLearnerIdAndEnrollmentStatusAndCourseStatisticsStatusIn(learnerId, status, courseStatisticsStatuses);
	}

}
