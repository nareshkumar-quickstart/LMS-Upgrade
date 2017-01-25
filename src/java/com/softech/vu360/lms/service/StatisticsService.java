package com.softech.vu360.lms.service;

import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.AICCLearnerStatistics;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerSCOStatistics;
import com.softech.vu360.lms.model.LearningSession;

/**
 * StatsticsService defines the set of interfaces 
 * to control the interactions and business logic
 * of calculating learner statistics within the LMS.
 * 
 * @author jason
 *
 */
public interface StatisticsService {
		
	public LearnerCourseStatistics getLearnerCourseStatisticsForLearnerEnrollment(LearnerEnrollment learnerErnollment);
	public LearnerCourseStatistics getLearnerCourseStatisticsByLearnerEnrollmentId(Long id);
	public void aggregrateStatisticsForLearningSession(String learningSessionId,Boolean courseCompleted,String vuCertificateURL,Date eventDateTime);
	public void launchedCourse(LearnerEnrollment le);
	public LearningSession getLearningSessionByLearningSessionId(String learningSessionId);
	public void surveyFiredEvent(Learner learner,Course course);
	public LearnerCourseStatistics getLearnerCourseStatistics(Learner learner, Course course);
	public void saveLearnerCourseStatistics(LearnerCourseStatistics newCourseStats);
	public LearnerCourseStatistics getLearnerCourseStatisticsById(long id) ;
	public LearnerSCOStatistics getLearnerSCOStatisticsById(long scoStatsId);
	public LearnerSCOStatistics loadLearnerSCOStatisticsById(long scoStatsId);
	public List<LearnerSCOStatistics> getLearnerSCOStatistics(Long learnerID, long enrollmentId, String courseGUID);
	public LearnerSCOStatistics saveLearnerSCOStatistics(LearnerSCOStatistics scoStats);
	public List<LearnerCourseActivity> getCourseActivitiesFromCourseStatistics(long statId);
	public LearnerCourseStatistics loadForUpdateLearnerCourseStatistics(Long id);
	//public CourseConfiguration getCourseCompletionCriteriaForCourse(Course course);
	public LearnerCourseStatistics updateLearnerCourseStatistics(long courseStatsId, LearnerCourseStatistics newCourseStats);
	public Long getLearnerSelectedCourseApprovalByEnrollmentId(long learnerEnrollmentId);
	
	public void saveLearningSession(LearningSession learningSession);
	
	public AICCLearnerStatistics getAICCLearnerStatisticsByEnrollment(LearnerEnrollment learnerEnrollment);
	public AICCLearnerStatistics saveAICCLearnerStatistics(AICCLearnerStatistics aiccStats);
	public AICCLearnerStatistics loadForUpdateAICCLearnerStatistics(long id);
	
	public String getCurrentLearningSession(LearningSession ls);
	public List<LearningSession> getLearningSessionByCourseApproval(List<CourseApproval> lstCourseApproval);
	public LearningSession updateLearningSessionForSurvey(String redirectURL, Long learningSessionId); 
	public List<LearnerCourseStatistics> getLearnerCoursesbyEnrolmentId(Long enrollmentId);
	//public LearnerCourseStatistics getLearnerCoursebyEnrolmentId(Long enrollmentId);
}
