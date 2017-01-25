package com.softech.vu360.lms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LockedCourse;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.vo.EnrollmentVO;
import com.softech.vu360.lms.vo.SaveEnrollmentParam;
import com.softech.vu360.lms.web.controller.model.AvailableCoursesTree;
import com.softech.vu360.lms.web.controller.model.CourseGroupView;
import com.softech.vu360.lms.web.controller.model.MyCoursesItem;
import com.softech.vu360.util.Brander;

public interface EnrollmentService {

	public LearnerEnrollment addEnrollment(Learner learner, CustomerEntitlement custEnt, OrgGroupEntitlement orgGrpEntl, LearnerEnrollment le);
	public LearnerEnrollment addEnrollment(LearnerEnrollment learnerEnrollment, CustomerEntitlement customerContract,
		    OrgGroupEntitlement orgGroupContract);
	public List<LearnerEnrollment> addEnrollmentsForSubscription(Learner learner, Subscription subscription, String subItem);
	public List<LearnerEnrollment> getLearnerEnrollmentByCourseId(long courseid);
	public List<LearnerEnrollment> addEnrollments(List<LearnerEnrollment> enrollments);
	
	public Map<Object, Object>   getAllCoursesNotEnrolledByLearner(long courseIdArray[],Customer customer);
	public Map<Object, Object>   getAllCoursesNotEnrolledByLearner(long courseIdArray[],Customer customer,String courseName,String courseGUID,String keywords, int pageIndex,int pageSize,String sortBy,int sortDirection);
	
	// [02/09/2011] LMS-8577 :: For Swap Courses, Number seats used is not correctly incrementing
	public String swapCourse (Learner learner, Long swappedEnrolementId, Long courseId, Long customerContractId, SynchronousClass synchronousClass);
    public Map<Object, Object>   getAllCoursesNotEnrolledByLearner(long courseIdArray[],Customer customer,String searchCriteria, int pageIndex,int pageSize,String sortBy,int sortDirection);
	public void setEnrollmentStatus(long enrolmentIdArray[], List<String> extendedDateList,String status);
	public void setCourseStatus( Vector<Long> statusList ,String status); // to unLock locked courses
	
	public List<LearnerEnrollment> addEnrollmentsForCourseEntitlements(Learner learner, CourseCustomerEntitlement custEntitlement, String subItem);
	public Map<Object, Object>   getAllCoursesNotEnrolledByLearner(long courseIdArray[],Customer customer,String courseName,String courseGUID,String keywords,
			String sortBy,int sortDirection);
				
	public List<VU360User> getEnrolledLearnerInfoByClassId(long synchClassId);
	public List<LearnerEnrollment>  getNumberofEnrollmentsOfThisCourse(Long courseId);
	public List<LearnerEnrollment> dropEnrollments(List<LearnerEnrollment> enrollments);
//    public EnrollmentVO saveEnrollment(SaveEnrollmentParam saveEnrollmentParam) throws ParseException;
    
    // [2/8/2011] LMS-8769 :: Admin Mode > Manage Enrollments: System is showing incorrect Locked Course Status 
    public LockedCourse getLastLockedCourse(LearnerEnrollment le);
    //public void deleteLearnerEnrollment(Learner learner, Course course);
    public Map<Object, Object> selfEnrollTheLearner(VU360User user, String courseId, String synchronousClassId);
    public void enrollLearnersInLearnerGroupCourses(List<Learner> learners, List<LearnerGroupItem> items, Brander brand);
    public void enrollLearnerInLearnerGroupsCourses(VU360User user,Customer customer, Learner learner, List<LearnerGroup> learnerGroups, Brander brander) throws Exception;
    public void enrollLearnersInCourses(List<Learner> learners, Long[] learnerGroupCourseIds, VU360User user, 
    		Customer customer, Brander brander);
    
    public void enrollLearnersInCoursesViaUserGroup(List<Learner> learners, List<LearnerGroupItem> items, VU360User user, 
    		Customer customer, Brander brander);
    
    public LearnerEnrollment loadForUpdateLearnerEnrollment(long enrollmentId);

	public void updateEnrollment(LearnerEnrollment enrollment);
	//public String getNextSequenceNumberForCertificate();
	
	// [12/15/2010] LMS-7942 :: Show Previous Enrollments detail for the same course
	public List<LearnerEnrollment> getEnrollmentsByCourseID (Long learnerId, Long courseId, Date enrollmentEndDate);
	
	//public LearnerEnrollment getLearnerEnrollmentByLearner(Learner learner);
	public List<LearnerEnrollment> getAllLearnerEnrollmentsByLearner(Learner learner);
	public LearnerEnrollment getAICCLearnerEnrollment(String externalLMSSessionID,long learnerId,long courseId);
	LearnerEnrollment getAICCLearnerEnrollment(String externalLMSSessionID,long learnerId,long courseId, String courseGuid);
	public LearnerCourseStatistics getLearnerCourseStatisticsBylearnerEnrollment(LearnerEnrollment learnerEnrollment);

	
	
	public Map displayMyCourses(VU360User user, Brander brand,List<CourseGroupView> courseGroups,List<MyCoursesItem> filteredMyCourses, List sortedCourseGroups, String viewType, String search, Boolean isFirstTimeView);
	public HashMap displayMyAvailableCourses(Learner learner, List<CourseGroupView> courseGroups, String courseGroupId, String trainingPlanId,  HashMap<Object,Object> catalogMap, String search);
	public Map displayCourseDetailsPage(String learnerEnrollmentID, String crntEnrollmentId, VU360User user, String activeTab, String viewType, String selEnrollmentPeriod, Brander brand  );
	public Map displayCourseSampleCompletionReport(VU360User user, String courseId, String viewType);
	public Map displayCourseSampleDetails(VU360User user, String courseId, String viewType);
	public Map displayScheduleToEnroll(VU360User user, Brander brand, Long courseId, String viewType);
	public Map displayViewSchedule(VU360User user, Brander brand, String courseId, String learnerEnrollmentID);
	
	// [1/27/2011] LMS-7183 :: Retired Course Functionality II (to enroll learner in Learner Group from Self Registration Invitations
	public void enrollLearnerToLearnerGroupItems (VU360User vu360User, LearnerGroup learnerGroup);
	public AvailableCoursesTree getAvailableCoursesTree(Learner learner, String search);
	//public List<Subscription> getSubscriptionLearnerAssignments(long userId);
	
	public EnrollmentVO processEnrollments (SaveEnrollmentParam saveEnrollmentParam);
	
	public void enableCertificate(Long enrolmentIdArray[], boolean status);

	public List<LearnerEnrollment> getLearnerEnrollmentByEnrollmentIds(List<Long> lstEnrollmentIds);
	public List<LearnerEnrollment> getLearerEnrollmentsByCourses(List<Long> lstCourseIds);
	public List<LearnerEnrollment> getLearerEnrollmentsByCourse(Long courseId);
	
	public void updateAutoSynchronousCourseStatus(Learner learner);
	public List<LearnerEnrollment> getLearnerEnrollmentsBySubscription(Long subscriptionId);
	public Subscription addPreEnrollmentsForSubscription(Learner learner, Subscription subscription);
	public LearnerEnrollment addSelfEnrollmentsForSubscription(Learner learner, String subscriptionId, String courseId);
	public boolean getviewAssessmentResults(long learnerEnrollmentId);
	public void marketoPacket(LearnerEnrollment le, String eventName);
	
	public LearnerEnrollment addSubscriptionEnrollments(Learner learner, String subscriptionId, String courseId);
	
}
