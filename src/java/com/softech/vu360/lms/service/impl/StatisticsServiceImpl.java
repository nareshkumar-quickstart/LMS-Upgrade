package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.AICCLearnerStatistics;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAssessmentResultStatistic;
import com.softech.vu360.lms.model.LearnerAttendanceSummaryStatistic;
import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerSCOObjectiveStatistics;
import com.softech.vu360.lms.model.LearnerSCOStatistics;
import com.softech.vu360.lms.model.LearnerTimeSpentStatistic;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.SCO;
import com.softech.vu360.lms.model.SCOObjective;
import com.softech.vu360.lms.model.ScormCourse;
import com.softech.vu360.lms.model.SelfPacedCourse;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.repositories.AICCLearnerStatisticsRepository;
import com.softech.vu360.lms.repositories.LearnerAssessmentResultStatisticRepository;
import com.softech.vu360.lms.repositories.LearnerAttendanceSummaryStatisticRepository;
import com.softech.vu360.lms.repositories.LearnerCourseActivityRepository;
import com.softech.vu360.lms.repositories.LearnerCourseStatisticsRepository;
import com.softech.vu360.lms.repositories.LearnerSCOStatisticsRepository;
import com.softech.vu360.lms.repositories.LearnerTimeSpentStatisticRepository;
import com.softech.vu360.lms.repositories.LearningSessionRepository;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.LearnersToBeMailedService;
//import com.softech.vu360.lms.model.CourseCompletionCriteria;

/**
 * @author jason
 * @modified by Faisal A. Siddiqui
 */
public class StatisticsServiceImpl implements StatisticsService {
	
	private static final Logger log = Logger.getLogger(StatisticsServiceImpl.class.getName());
	private EntitlementService entitlementService=null;
	private SurveyService surveyService=null;
	private LearnersToBeMailedService learnersToBeMailedService= null;
	
	@Inject 
	private LearnerCourseStatisticsRepository learnerCourseStatisticsRepository;
	
	@Inject
	private AICCLearnerStatisticsRepository aICCLearnerStatisticsRepository;
	
	@Inject
	private LearnerCourseActivityRepository learnerCourseActivityRepository;
	
	@Inject
	private LearningSessionRepository learningSessionRepository;
	
	@Inject
	private LearnerSCOStatisticsRepository learnerSCOStatisticsRepository;
	
	@Inject
	private LearnerAttendanceSummaryStatisticRepository learnerAttendanceSummaryStatisticsRepository;
	
	@Inject
	private LearnerTimeSpentStatisticRepository learnerTimeSpentStatisticsRepository;
	
	@Inject
	private LearnerAssessmentResultStatisticRepository learnerAssesmentResultStatisticsRepository;
	
	
	public LearnerCourseStatistics getLearnerCourseStatisticsForLearnerEnrollment(LearnerEnrollment learnerEnrollment) {
		log.debug("looking up courseStatistics for enrollment:"+learnerEnrollment.getKey());
		return this.getLearnerCourseStatisticsForLearnerEnrollment(learnerEnrollment.getId());
	}
	
	public LearnerCourseStatistics getLearnerCourseStatisticsByLearnerEnrollmentId(Long id) {
		return this.getLearnerCourseStatisticsForLearnerEnrollment(id);
	}

	public LearnerCourseStatistics getLearnerCourseStatisticsById(long id) {
		//return statisticsDAO.getLearnerCourseStatisticsById(id);
		return this.learnerCourseStatisticsRepository.findOne(id);
	}
	
	public LearnerCourseStatistics loadForUpdateLearnerCourseStatistics(Long id) {
		//return statisticsDAO.loadForUpdateLearnerCourseStatistics(id);
		return this.learnerCourseStatisticsRepository.findOne(id);
	}
	
	public LearningSession getLearningSessionByLearningSessionId(String learningSessionId){
		//return statisticsDAO.getLearningSessionByLearningSessionId(learningSessionId);
		return learningSessionRepository.findTop1ByLearningSessionID(learningSessionId);
	}
	
	/**
	 * This method is involved to save learning session.
	 */
	public void saveLearningSession(LearningSession learningSession) {
		learningSession.setSessionEndDateTime(new Date(System.currentTimeMillis()));
		learningSessionRepository.save(learningSession);
	}
	
	/**
	 * This method is invoked via a web service from LCMS once the player has detected
	 * that the user has abandoned the player session either by clicking the logout button
	 * or by a session expiration message in LCMS.
	 */
	public void aggregrateStatisticsForLearningSession(String learningSessionId,Boolean courseCompleted,String vucertificateURL,Date eventDateTime) {
		// lookup objects from db
		LearningSession lSession = this.getLearningSessionByLearningSessionId(learningSessionId);//statisticsDAO.getLearningSessionByLearningSessionId(learningSessionId);
		// end the learning session
		lSession.setSessionEndDateTime(new Date(System.currentTimeMillis()));
		lSession = learningSessionRepository.save(lSession);
		LearnerCourseStatistics courseStats = this.getLearnerCourseStatisticsForLearnerEnrollment(lSession.getEnrollmentId());
		
		List<LearnerAssessmentResultStatistic> assesmentStats =this.getLearnerAssessmentStatisticByLearningSession(learningSessionId,eventDateTime);
		List<LearnerTimeSpentStatistic> attendanceStats =this.getLearnerTimeSpentStatisticByLearningSession(learningSessionId,eventDateTime);
		List<LearnerAttendanceSummaryStatistic> attendanceSummaryStats =this.getLearnerAttendaceSummaryByLearningSession(learningSessionId,eventDateTime);

		
		LearnerCourseStatistics newCourseStats=courseStats.clone();
		newCourseStats.appendDebugInfo("{learningSession:"+learningSessionId+",timestamp:"+eventDateTime+",courseType="+courseStats.getLearnerEnrollment().getCourse().getCourseType());
		updateLearnerAssessmentStats(assesmentStats, courseStats, newCourseStats);
		log.debug("updating LearnerAttendaceStats...stats:"+attendanceSummaryStats);
		updateLearnerAttendanceStats(attendanceSummaryStats, courseStats, newCourseStats);
		log.debug("updated..");
		log.debug("updating LearnerTimeSpentStats..stats:"+attendanceStats);
		updateLearnerTimeSpentStatistic(attendanceStats, courseStats, newCourseStats);
		
		// last Access date would be the session end date
		newCourseStats.setLastAccessDate(Calendar.getInstance().getTime());
		// evaluate course completion
		log.debug("courseCompleted:"+/*courseStats.isCompleted()*/courseStats.isCourseCompleted());
		if ( /*!courseStats.isCompleted()*/ !courseStats.isCourseCompleted() ) {
			
			if(courseStats.getLearnerEnrollment().getCourse() instanceof SelfPacedCourse){
				boolean complete = evaluateCourseCompletion(newCourseStats,lSession.getEnrollmentId());
				if ( complete ) {
					newCourseStats.setCompleted(true);
					newCourseStats.setCompletionDate(new Date(System.currentTimeMillis()));
					newCourseStats.setStatus(LearnerCourseStatistics.COMPLETED);
					newCourseStats.setPercentComplete(100);
				}
			}else
			{
				log.debug("legacy  course completed flag="+courseCompleted);
				if ( courseCompleted!=null && courseCompleted.booleanValue()) {
					newCourseStats.setCompleted(true);
					newCourseStats.setCompletionDate(new Date(System.currentTimeMillis()));
					newCourseStats.setStatus(LearnerCourseStatistics.COMPLETED);
					newCourseStats.setPercentComplete(100);
				}
			}
			log.debug("vuCertificateURL:"+vucertificateURL);
			newCourseStats.setCertificateURL(vucertificateURL);
		}
		log.debug("persisting coursestats");
		newCourseStats.appendDebugInfo(", certificateURL:"+newCourseStats.getCertificateURL());
		newCourseStats.appendDebugInfo(", old_status="+courseStats.getStatus());
		newCourseStats.appendDebugInfo(", new_status="+newCourseStats.getStatus());
		newCourseStats.appendDebugInfo("}");
		courseStats = updateLearnerCourseStatistics(courseStats.getId(), newCourseStats);
		log.debug("courses persist & method ended..");
	}
	
	/**
	 * This method is called by the Survey sub-system when a survey has been completed
	 * by a learner to check if there are any course completion requirements.
	 * @param learner
	 * @param course
	 */
	public void surveyFiredEvent(Learner learner, Course course){
		log.debug("course="+course);
		log.debug("learner="+learner);
		LearnerEnrollment learnerEnrollment=entitlementService.getLearnerEnrollmentsForLearner(learner,course);
		log.debug("learnerEnrollment="+learnerEnrollment);
		LearnerCourseStatistics lcs=this.getLearnerCourseStatisticsForLearnerEnrollment(learnerEnrollment.getId());
		log.debug("learnerCourseStats="+lcs);
		if(lcs!=null){
			log.debug("completed="+/*lcs.isCompleted()*/lcs.isCourseCompleted());
			if (/*!lcs.isCompleted()*/!lcs.isCourseCompleted())	{
				boolean complete = evaluateCourseCompletion(lcs,learnerEnrollment.getId());
				if (complete){
				lcs.setCompleted(true);
				lcs.setCompletionDate(new Date(System.currentTimeMillis()));
				lcs.setStatus(LearnerCourseStatistics.COMPLETED);
				LearnerCourseStatistics updatedCourseStatistics = updateLearnerCourseStatistics(lcs.getId(), lcs);
			}
		}
		}
	}

	
	
	/**
	 *
	 * This method will update all type of learner assessment stats.
	 * 	1- PRE-TEST Stats 2- QUIZ Stats  3- POST-TEST Stats
	 * @param assesmentStats
	 * @param courseStats
	 * @param newCourseStats
	 */
	private void updateLearnerAssessmentStats(List<LearnerAssessmentResultStatistic> assesmentStats, LearnerCourseStatistics courseStats, LearnerCourseStatistics newCourseStats)
	{
		
		log.debug("updateLearnerAssessmentStats is called :"+assesmentStats);
		
		//CourseConfiguration complCriteria = getCourseCompletionCriteriaForCourse(courseStats.getLearnerEnrollment().getCourse());
//		AssessmentConfiguration assessmentConfigPost =  null;
//		AssessmentConfiguration assessmentConfigPre =  null;
//		AssessmentConfiguration assessmentConfigQuiz =  null;
		
//		if(complCriteria!=null){
//			assessmentConfigPost = complCriteria.getPostAssessmentConfiguration();
//			assessmentConfigPre = complCriteria.getPreAssessmentConfiguration();
//			assessmentConfigQuiz = complCriteria.getQuizAssessmentConfiguration();
//		}
//		
//		if(assessmentConfigPost==null)
//			assessmentConfigPost= new AssessmentConfiguration();
//		if(assessmentConfigPre==null)
//			assessmentConfigPre= new AssessmentConfiguration();
//		if(assessmentConfigQuiz==null)
//			assessmentConfigQuiz= new AssessmentConfiguration();
//		
//		
//		if(assesmentStats!=null && assesmentStats.size()>0)
//		{
//			
//			//temp variable to update highest PRE-TEST Score and DATE
//			double highestPreTestScore=courseStats.getPretestScore();
//			newCourseStats.appendDebugInfo(",<previous highestpretestscore:"+highestPreTestScore);
//
//			int totalNumberOfQuizTaken=0;
//			double sumOfQuizScore=0.0;
//			double highestQuizScore=0.0;
//			double lowestQuizScore=0.0;
//			Date lastQuizDate = new Date();
//			Date firstQuizDate = new Date();
//			
//			//temp variable for QUIZ STATS
//			
//			if(!assessmentConfigQuiz.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT)){
//				totalNumberOfQuizTaken=(int)courseStats.getNumberQuizesTaken();
//				newCourseStats.appendDebugInfo(",totalquiztaken:"+totalNumberOfQuizTaken);
//				sumOfQuizScore=(courseStats.getAverageQuizScore()*totalNumberOfQuizTaken);
//				newCourseStats.appendDebugInfo(",totalquizscore:"+sumOfQuizScore);
//				log.debug("1- totalNumberOfQuizTaken:"+totalNumberOfQuizTaken+" sumOfQuizScore:"+sumOfQuizScore);
//						
//				highestQuizScore=courseStats.getHighestQuizScore();
//				newCourseStats.appendDebugInfo(",highestquizscore:"+highestQuizScore);
//				lowestQuizScore=courseStats.getLowestQuizScore();
//				newCourseStats.appendDebugInfo(",lowestquizscore:"+lowestQuizScore);
//			
//				lastQuizDate=courseStats.getLastQuizDate();
//				lastQuizDate=lastQuizDate==null?new Date(System.currentTimeMillis()):lastQuizDate;
//				newCourseStats.appendDebugInfo(",lastQuizDate:"+lastQuizDate);
//				firstQuizDate=courseStats.getFirstQuizDate();
//				firstQuizDate=firstQuizDate==null?new Date(System.currentTimeMillis()):firstQuizDate;
//				newCourseStats.appendDebugInfo(",firstQuizDate:"+firstQuizDate);
//			}
//			
//			//temp variable for Post-Test Stats
//			int totalNumberOfPostTestsTaken=0;
//			double sumOfPostTestsScore=0.0;
//			double highestPostTestScore=0.0;
//			double lowestPostTestScore=0.0;
//			Date lastPostTestDate = new Date();
//			Date firstPostTestDate = new Date();
//			
//			if(!assessmentConfigPre.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT)){
//				totalNumberOfPostTestsTaken=courseStats.getNumberPostTestsTaken();
//				newCourseStats.appendDebugInfo(",totalPostTests:"+totalNumberOfPostTestsTaken);
//				sumOfPostTestsScore=(courseStats.getAveragePostTestScore()*totalNumberOfPostTestsTaken);
//				newCourseStats.appendDebugInfo(",totalPostTestsScore:"+sumOfPostTestsScore);
//				log.debug("1-totalNumberOfPostTestsTaken:"+totalNumberOfPostTestsTaken+" sumOfPostTestScore:"+sumOfPostTestsScore);
//				
//				highestPostTestScore=courseStats.getHighestPostTestScore();
//				lowestPostTestScore=courseStats.getLowestPostTestScore();
//				newCourseStats.appendDebugInfo(",highestPostTestsScore:"+highestPostTestScore);
//				newCourseStats.appendDebugInfo(",lowestPostTestsScore:"+lowestPostTestScore);
//				lastPostTestDate=courseStats.getLastPostTestDate();
//				//lastPostTestDate=lastPostTestDate==null?new Date(System.currentTimeMillis()):lastPostTestDate;
//				firstPostTestDate=courseStats.getFirstPostTestDate();
//				//firstPostTestDate=firstPostTestDate==null?new Date(System.currentTimeMillis()):firstPostTestDate;
//				
//				newCourseStats.appendDebugInfo(",firstPostTestDate:"+firstPostTestDate);
//				newCourseStats.appendDebugInfo(",lastPostTestDate:"+lastPostTestDate);
//			
//			}
//			
//			for (LearnerAssessmentResultStatistic resultStat: assesmentStats)
//			{
//				log.debug(" ASSESSMENT_TYPE:"+resultStat.getAssessmentType());
//				if (resultStat.getAssessmentType() == LearnerAssessmentResultStatistic.ASSESSMENT_TYPE_PRE_TEST) 
//				{
//					
//					
//					if(!assessmentConfigPre.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT)){
//					//UPDATE THE PRE-TEST STATS IF SCORE IS GREATER THAN PREVIOUS SCORE
//					// this is the possibility if in a single learning session learner gets failed first time but passed in subsequent attempt.
//					log.debug("highestPreTestScore:"+highestPreTestScore+" resultStas.getRawScore:"+resultStat.getRawScore());
//					if(highestPreTestScore<resultStat.getRawScore())
//					{
//						highestPreTestScore=resultStat.getRawScore();
//						log.debug("updating highestPreTestScore:"+highestPreTestScore+" date:"+resultStat.getStatisticDate());
//						//1- update pre-test score
//						newCourseStats.setPretestScore(highestPreTestScore);
//						//2- update pre-test date of highest Pre-Test Score
//						newCourseStats.setPreTestDate(resultStat.getStatisticDate());
//
//					}
//					}
//				}
//				else if ( resultStat.getAssessmentType() == LearnerAssessmentResultStatistic.ASSESSMENT_TYPE_QUIZ )
//				{
//					if(!assessmentConfigQuiz.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT)){
//					log.debug("1- ASSESSMENT_TYPE_QUIZ..highest Quiz Score:"+highestQuizScore+" -- new Score:"+resultStat.getRawScore());
//					//1- update highest Quiz Score
//					if (highestQuizScore< resultStat.getRawScore() ) 
//					{
//						//update the variable to compare with subsequent Quiz in same learning session
//						highestQuizScore=resultStat.getRawScore();
//					}
//					log.debug("updating highest quiz score :"+highestQuizScore);
//					newCourseStats.setHighestQuizScore(highestQuizScore);
//
//					//2- update lowest quiz score
//					log.debug("2- lowestQuizScore:"+lowestQuizScore+" new QuizScore:"+resultStat.getRawScore());
//					//by OWS
//					if(lowestQuizScore==-1) //i.e. first time
//						lowestQuizScore= resultStat.getRawScore();
//					else{
//						if (lowestQuizScore > resultStat.getRawScore())
//						{
//							lowestQuizScore=resultStat.getRawScore();
//						}
//					}
//					log.debug("updating lowest Quiz Score:"+lowestQuizScore);
//					newCourseStats.setLowestQuizScore(lowestQuizScore);
//
//					//3- update last quiz date
//					log.debug("3- lastQuizDate:"+lastQuizDate+" new QuizDaate:"+resultStat.getStatisticDate());
//					if(lastQuizDate.before(resultStat.getStatisticDate()))
//					{
//						lastQuizDate=resultStat.getStatisticDate();
//					}
//					log.debug("updating lastQuizDate:"+lastQuizDate);
//					newCourseStats.setLastQuizDate(lastQuizDate);	
//
//					//4- update first quiz date
//					log.debug("4- firstQuizDate:"+firstQuizDate+" new QuizDate:"+resultStat.getStatisticDate());
//					if(firstQuizDate.after(resultStat.getStatisticDate()))
//					{
//						firstQuizDate=resultStat.getStatisticDate();
//					}
//					log.debug("updating.. first Quiz Date:"+firstQuizDate);
//					newCourseStats.setFirstQuizDate(firstQuizDate);
//
//					//5- update Number of Quiz
//					totalNumberOfQuizTaken++;
//					//6- update sum Of Quiz Score to calculate Average
//					sumOfQuizScore+=resultStat.getRawScore();
//					log.debug("5 & 6 totalNumberOfQuizes:"+totalNumberOfQuizTaken+" sumOfQuizScore:"+sumOfQuizScore);
//					}
//				}
//				else if ( resultStat.getAssessmentType() == LearnerAssessmentResultStatistic.ASSESSMENT_TYPE_POST_TEST )
//				{
//					if(!assessmentConfigPost.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT)){
//					log.debug("new Score:"+resultStat.getRawScore());
//					//1- highest post test score update
//					log.debug("1-  highestPostTestScore:"+highestPostTestScore +" -- new Score:"+resultStat.getRawScore());
//					if(highestPostTestScore < resultStat.getRawScore())
//					{
//						highestPostTestScore=resultStat.getRawScore();
//					}
//					log.debug("updating new Highest Post Test Score:"+highestPostTestScore);
//					newCourseStats.setHighestPostTestScore(highestPostTestScore);
//
//					//2- lowest post test score
//					log.debug("2-  lowestPostTestScore:"+lowestPostTestScore +" -- new Score:"+resultStat.getRawScore());
//					
//					//by OWS
//					if(lowestPostTestScore==-1) //i.e. first time
//						lowestPostTestScore= resultStat.getRawScore();
//					else{
//						
//						if (lowestPostTestScore > resultStat.getRawScore()) 
//						{
//							lowestPostTestScore=resultStat.getRawScore();
//						}
//					}
//					log.debug("updating new Lowest Post Test Score:"+lowestPostTestScore);
//					newCourseStats.setLowestPostTestScore(lowestPostTestScore);
//
//					
//					//3- last post test taken
//					// in 1 session there may be more than 1 post test can be taken and and order of post test date is not guaranteed 
//					// therefore we need to take care of it in our logic
//					log.debug("3- lastPostTestDate:"+lastPostTestDate+"... resultStats.StatsDate:"+resultStat.getStatisticDate());
//					if(lastPostTestDate==null || lastPostTestDate.before(resultStat.getStatisticDate()))
//					{
//						lastPostTestDate=resultStat.getStatisticDate();
//					}
//					log.debug("updating last Post Test Date.."+lastPostTestDate);
//					newCourseStats.setLastPostTestDate(lastPostTestDate);
//					
//					//4- first Post test taken
//					log.debug("4- firstPostTestDate:"+firstPostTestDate+"... resultStats.StatsDate:"+resultStat.getStatisticDate());
//					if ( firstPostTestDate == null || firstPostTestDate.after(resultStat.getStatisticDate())) 
//					{
//						firstPostTestDate=resultStat.getStatisticDate();
//					}
//					log.debug("updating first Post Test Date.."+firstPostTestDate);
//					newCourseStats.setFirstPostTestDate(firstPostTestDate);
//
//					//5- update number of post test taken
//					totalNumberOfPostTestsTaken++;
//					//6- update sum of post test score to calculate average
//					sumOfPostTestsScore+=resultStat.getRawScore();
//					log.debug(" 5 & 6 totalNumberOfPostTestsTaken:"+totalNumberOfPostTestsTaken+" -- sumOfPostTestsScore:"+sumOfPostTestsScore);
//					}
//				}
//				else
//				{
//					log.error("Unknown Assessment Type.."+resultStat.getAssessmentType());
//				}
//			}
//
//			//updating Average Quiz Stats 
//			log.debug(" updating Quiz Stats totalNumberOfQuizTaken:"+totalNumberOfQuizTaken+"  sumOfQuizScore:"+sumOfQuizScore);
//			{
//				newCourseStats.setNumberQuizesTaken(totalNumberOfQuizTaken);
//				double averageQuizScore=sumOfQuizScore/(totalNumberOfQuizTaken==0?1:totalNumberOfQuizTaken);
//				log.debug("averageQuizScore:"+averageQuizScore);
//				newCourseStats.setAverageQuizScore(averageQuizScore);
//
//				newCourseStats.appendDebugInfo("><new totalquiztaken:"+totalNumberOfQuizTaken);
//				newCourseStats.appendDebugInfo(",totalQuizScore:"+sumOfQuizScore);
//			}
//
//
//			//update post-test average stats
//			log.debug("updating PostTest Stats  totalNumberOfPostTestTaken:"+totalNumberOfPostTestsTaken+"  sumOfPostTestScore:"+sumOfPostTestsScore);
//			{
//				newCourseStats.setNumberPostTestsTaken(totalNumberOfPostTestsTaken);
//				double averagePostTestScore=sumOfPostTestsScore/(totalNumberOfPostTestsTaken==0?1:totalNumberOfPostTestsTaken);
//				log.debug("averagePostTestScore:"+averagePostTestScore);
//				newCourseStats.setAveragePostTestScore(averagePostTestScore);
//
//				newCourseStats.appendDebugInfo(",totalPostTests:"+totalNumberOfPostTestsTaken);
//				newCourseStats.appendDebugInfo(",totalPostTestsScore:"+sumOfPostTestsScore);
//			}
//		}
//		log.debug("End of Update Learner Assessment Stats..");
	}
	
	@Transactional
	public void launchedCourse(LearnerEnrollment le) {
		// update the last access date and access counts
		boolean sendCourseCompletionEmail= false;
		LearnerCourseStatistics courseStats = this.getLearnerCourseStatisticsForLearnerEnrollment(le.getId());
		if ( courseStats.getFirstAccessDate() == null ) {
			
			courseStats.setFirstAccessDate(new Date(System.currentTimeMillis()));
			courseStats.setLastAccessDate(courseStats.getFirstAccessDate());
			courseStats.setLaunchesAccrued(1);
			//code added September 03 2009
			if(!(courseStats.getLearnerEnrollment().getCourse() instanceof SynchronousCourse || courseStats.getLearnerEnrollment().getCourse() instanceof WebinarCourse) && !(courseStats.getLearnerEnrollment().getCourse() instanceof WebLinkCourse) && !(courseStats.getLearnerEnrollment().getCourse() instanceof DiscussionForumCourse)  ){
				courseStats.setStatus(LearnerCourseStatistics.IN_PROGRESS);
			}
			
		}
		else {
			courseStats.setLastAccessDate(new Date(System.currentTimeMillis()));
			courseStats.setLaunchesAccrued(courseStats.getLaunchesAccrued()+1);
		}
			
		if(courseStats.getLearnerEnrollment().getCourse() instanceof SynchronousCourse || courseStats.getLearnerEnrollment().getCourse() instanceof WebinarCourse ){
			SynchronousClass enrolledClass = le.getSynchronousClass();
			if(enrolledClass!=null){					
				Date currentDateTime = DateUtil.getCurrentServerTimeGMT();
				Date enrollmentStartDate = enrolledClass.getClassStartDate();
				Date enrollmentEndDate = enrolledClass.getClassEndDate();
				if(currentDateTime.compareTo(DateUtil.getGMTDateForTimezoneHours(enrollmentStartDate,enrolledClass.getTimeZone().getHours()))>=0 &&
					courseStats.getStatus().equals(LearnerCourseStatistics.NOT_STARTED)){//Set to inProgress
					
					courseStats.setStatus(LearnerCourseStatistics.IN_PROGRESS);
					
				}
				if(currentDateTime.compareTo(DateUtil.getGMTDateForTimezoneHours(enrollmentEndDate,enrolledClass.getTimeZone().getHours()))>=0 && 
					!courseStats.getStatus().equals(LearnerCourseStatistics.COMPLETED)){//Set to Completed
					
					if(enrolledClass.isAutomatic()){
						courseStats.setStatus(LearnerCourseStatistics.COMPLETED);
						courseStats.setCompleted(true);
						courseStats.setCompletionDate(enrolledClass.getClassEndDate());
						courseStats.setPercentComplete(100);
						
						sendCourseCompletionEmail= true;
					}
				}
					
			}
		}
			
			
		
		// this check is placed here to cater fallback cases in which some how status couldn't be updated
		if((courseStats.getLearnerEnrollment().getCourse() instanceof WebLinkCourse) || (courseStats.getLearnerEnrollment().getCourse() instanceof DiscussionForumCourse)){
			if(/*!courseStats.isCompleted()*/ !courseStats.isCourseCompleted()){
				courseStats.setCompleted(Boolean.TRUE);
				courseStats.setStatus(LearnerCourseStatistics.COMPLETED);
				courseStats.setCompletionDate(new Date(System.currentTimeMillis()));
				courseStats.setPercentComplete(100);
				if(courseStats.getLearnerEnrollment().getCourse() instanceof WebLinkCourse){
					sendCourseCompletionEmail= true;
				}
			}			
		}
		else if ( courseStats.getLearnerEnrollment().getCourse() instanceof ScormCourse ) {
			// ensure that each SCO has a learnerSCOStatistics...
			List<LearnerSCOStatistics> scoStats = learnerSCOStatisticsRepository.findByLearnerIdAndLearnerEnrollmentId(le.getLearner().getId(), le.getId());
			if ( scoStats == null || scoStats.isEmpty() ) {
				log.debug("creating new SCO Stats for learner, first launch");
				List<SCO> scos = ((ScormCourse)courseStats.getLearnerEnrollment().getCourse()).getScos();
				LearnerSCOStatistics scoStat = new LearnerSCOStatistics();
				log.debug("found:"+scos.size()+" scos for scorm course.");
				for (SCO sco : scos) {
					scoStat = new LearnerSCOStatistics();
					le.getLearner().getId();
					scoStat.setLearnerId(le.getLearner().getId());
					scoStat.setLearnerEnrollmentId(le.getId());
					scoStat.setSco(sco);
					List<SCOObjective> scoObjectives = sco.getScoObjectives();
					for (SCOObjective objective : scoObjectives) {
						LearnerSCOObjectiveStatistics scoObjStat = new LearnerSCOObjectiveStatistics();
						scoObjStat.setScoObjective(objective);
						scoStat.addLearnerScoObjectiveStatistics(scoObjStat);
					}
					//statisticsDAO.saveLearnerSCOStatistics(scoStat);
					this.learnerSCOStatisticsRepository.save(scoStat);
				}
			}
		}
		this.saveLearnerCourseStatistics(courseStats);  //[1/17/2011] LMS-8645 :: Used LoadForUpdate Call to update the stats properly
		
		/*Emails certificate if course complete*/
		if(sendCourseCompletionEmail){
		   learnersToBeMailedService.emailCourseCompletionCertificate(le.getId());
		}
		
	}
	
//	public CourseConfiguration getCourseCompletionCriteriaForCourse(Course course) {
//		return statisticsDAO.getCourseCompletionCriteriaForCourse(course.getId());
//	}
	
	private boolean evaluateCourseCompletion(LearnerCourseStatistics stats,long learnerEnrollmentId) {
		boolean complete = false;
		return complete;
//		log.debug("evaluating course completion for learner enrollment="+learnerEnrollmentId);
//		//CourseConfiguration complCriteria = getCourseCompletionCriteriaForCourse(stats.getLearnerEnrollment().getCourse());
//		
////		AssessmentConfiguration assessmentConfigPost = complCriteria.getPostAssessmentConfiguration();
////		AssessmentConfiguration assessmentConfigPre = complCriteria.getPreAssessmentConfiguration();
////		AssessmentConfiguration assessmentConfigQuiz = complCriteria.getQuizAssessmentConfiguration();
//		
//
//		
//		
////		if(complCriteria!=null)
//		{
////			log.debug("isCompleteAfterUniqueNumberVisits:"+complCriteria.isCanOnlyBeCompleteAfterNumberOfCourseLaunches());
////			stats.appendDebugInfo(",<isCompleteAfterUniqueNumberVisits:"+complCriteria.isCanOnlyBeCompleteAfterNumberOfCourseLaunches());
////			if ( complCriteria.isCanOnlyBeCompleteAfterNumberOfCourseLaunches() ) {
//				//TODO: FaisalSiddiqui; shouldn't we call complCriteria.meetsPlayEverySceneRequirement() ? 
////				log.debug("LaunchesAccrued:"+stats.getLaunchesAccrued()+" -- NumberUniqueVisitsRequired:"+complCriteria.getNumberOfCoursesLaunch());
////				stats.appendDebugInfo(",LaunchesAccrued:"+stats.getLaunchesAccrued()+" , NumberUniqueVisitsRequired:"+complCriteria.getNumberOfCoursesLaunch());
////				complete = stats.getLaunchesAccrued() >= complCriteria.getNumberOfCoursesLaunch();
//				log.debug("evaluationResult:"+complete);
//				// as this is a criteria and we have not met it we can stop
//				if ( !complete ) {
//					return false;
//				}
//			}
//	
//			log.debug("isVisitEverySceneInCourse:"+complCriteria.isMustViewEverySceneInTheCourse());
//			stats.appendDebugInfo(">,<isVisitEverySceneInCourse:"+complCriteria.isMustViewEverySceneInTheCourse());
//			if ( complCriteria.isMustViewEverySceneInTheCourse() ) { 
//				log.debug("course progress:"+stats.getPercentComplete());
//				stats.appendDebugInfo(",percentComplete:"+stats.getPercentComplete());
//				complete = stats.getPercentComplete() >= 100;
//				log.debug("evaluationResult:"+complete);
//				// as this is a criteria and we have not met it we can stop
//				if ( !complete ) {
//					return false;
//				}
//			}
//			
//			
//			log.debug("isPostAssessmentAttempted:"+complCriteria.isMustAttemptPostAssessment());
//			stats.appendDebugInfo(",isPostAssessmentAttempted:"+complCriteria.isMustAttemptPostAssessment());
//			if ( complCriteria.isMustAttemptPostAssessment() ) {
//				//TODO: FaisalSiddiqui: shouldn't we evaluate this criteria on number of post test taken
//				// why this criteria is not available on course completion report?
//				log.debug("lastPostTestDate:"+stats.getLastPostTestDate());
//				stats.appendDebugInfo(",lastPostTestDate:"+stats.getLastPostTestDate());
//				complete = (stats.getLastPostTestDate() != null);
//				log.debug("evaluationResult:"+complete);
//				// as this is a criteria and we have not met it we can stop
//				if ( !complete ) {
//					return false;
//				}
//			}
//			
//			
//
//				log.debug("isPostAssessmentMastery:"+complCriteria.isMustDemonstratePostAssessmentMastery());
//				stats.appendDebugInfo(",isPostAssessmentMastery:"+complCriteria.isMustDemonstratePostAssessmentMastery());
//				if ( complCriteria.isMustDemonstratePostAssessmentMastery() ) {
//					if(assessmentConfigPost!=null && assessmentConfigPost.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT)){
//						return false;
//					}
//					//TODO: FaisalSiddiqui: shouldn't we call complCriteria.meetsPostAssessmentScoreRequirement(lcs)? 
//					log.debug("HighestPostTestScore:"+stats.getHighestPostTestScore()+" -- PostAssessmentMasteryScoreRequired:"+complCriteria.getPostassessmentMasteryscore());
//					stats.appendDebugInfo(",HighestPostTestScore:"+stats.getHighestPostTestScore()+" -- PostAssessmentMasteryScoreRequired:"+complCriteria.getPostassessmentMasteryscore());
//					if(stats.getLastPostTestDate() != null){
//						complete = stats.getHighestPostTestScore() >= complCriteria.getPostassessmentMasteryscore();
//					}
//					else{
//						complete = false;
//					}
//					log.debug("evaluationResult:"+complete);
//					// as this is a criteria and we have not met it we can stop
//					if ( !complete ) {
//						return false;
//					}
//				}
//
//			
//			
//			
//				log.debug("isPreAssessmentMastery:"+complCriteria.isMustDemonstratePreAssessmentMastery());
//				stats.appendDebugInfo(",isPreAssessmentMastery:"+complCriteria.isMustDemonstratePreAssessmentMastery());
//				if ( complCriteria.isMustDemonstratePreAssessmentMastery() ) {
//					if(assessmentConfigPre!=null && assessmentConfigPre.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT)){
//						return false;
//					}
//					
//					//TODO: FaisalSiddiqui: shouldn't we call complCriteria.meetsPreAssessmentScoreRequirement(lcs)? 
//					log.debug("PretestScore:"+stats.getPretestScore()+" -- PreAssessmentMasteryScoreRequired:"+complCriteria.getPreassessmentMasteryscore());
//					stats.appendDebugInfo(",PretestScore:"+stats.getPretestScore()+" -- PreAssessmentMasteryScoreRequired:"+complCriteria.getPreassessmentMasteryscore());
//					complete = stats.getPretestScore() >= complCriteria.getPreassessmentMasteryscore();
//					log.debug("evaluationResult:"+complete);
//					// as this is a criteria and we have not met it we can stop
//					if ( !complete ) {
//						return false;
//					}
//				}
//				
//			
//			
//				log.debug("isQuizAssessmentMastery:"+complCriteria.isMustDemonstrateQuizMastery());
//				stats.appendDebugInfo(",isQuizAssessmentMastery:"+complCriteria.isMustDemonstrateQuizMastery());
//				if ( complCriteria.isMustDemonstrateQuizMastery() ) {
//					
//					if(assessmentConfigQuiz!=null && assessmentConfigQuiz.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT)){
//						return false;
//					}
//					
//					//TODO: FaisalSiddiqui: shouldn't we call complCriteria.meetsQuizAssessmentScoreRequirement(lcs)?
//					// TODO:  iterate over all quizes from db and check each one....
//					// as this is a criteria and we have not met it we can stop
//					log.debug("HighestQuizScore:"+stats.getHighestQuizScore()+" -- QuizAssessmentMasteryScoreRequired:"+complCriteria.getQuizMasteryscore());
//					stats.appendDebugInfo(",HighestQuizScore:"+stats.getHighestQuizScore()+" -- QuizAssessmentMasteryScoreRequired:"+complCriteria.getQuizMasteryscore());
//					complete=stats.getHighestQuizScore()>=complCriteria.getQuizMasteryscore();
//					if ( !complete ) {
//						return false;
//					}
//				}
//			
//			log.debug("isCompletedWithinDaysOfRegistration:"+complCriteria.isCompleteWithinDaysOfRegistration());
//			stats.appendDebugInfo(",isCompletedWithinDaysOfRegistration:"+complCriteria.isCompleteWithinDaysOfRegistration());
//			if ( complCriteria.isCompleteWithinDaysOfRegistration() ) {
//				//TODO: FaisalSiddiqui: shouldn't we call complCriteria.meetsTimeInDaysFromRegistrationRequirement(lcs)?
//				Calendar cal = new GregorianCalendar();
//				cal.setTimeInMillis(stats.getLearnerEnrollment().getEnrollmentStartDate().getTime());
//				log.debug("LearnerEnrollment Time:"+stats.getLearnerEnrollment().getEnrollmentStartDate());
//				cal.add(Calendar.DAY_OF_YEAR, complCriteria.getDaysOfRegistraion());
//				/*Start:LMS-8749:Allow student the entire last day*/
//				cal.set(Calendar.HOUR_OF_DAY,23);
//				cal.set(Calendar.MINUTE,59);
//				cal.set(Calendar.SECOND,59);
//				/*End:LMS-8749:Allow student the entire last day*/
//				stats.appendDebugInfo(",timeToCompleteDaysFromRegistration:"+cal.getTime());
//				stats.appendDebugInfo(",LearnerEnrollment Time:"+cal.getTime());
//				Date now = new Date(System.currentTimeMillis());
//				log.debug("Current Time:"+now);
//				log.debug("TimeToCompleteDaysFromRegistrationInDays:"+complCriteria.getDaysOfRegistraion());
//				stats.appendDebugInfo(",currentTime:"+now);
//				complete = (now.before(cal.getTime()) || now.equals(cal.getTime()));
//				log.debug("evaluationResult:"+complete);
//				// as this is a criteria and we have not met it we can stop
//				if ( !complete ) {
//					return false;
//				}
//			}
//			log.debug("isCompleteWithinTimePeriod:"+complCriteria.isCompleteWithinTimePeriod());
//			stats.appendDebugInfo(",isCompleteWithinTimePeriod:"+complCriteria.isCompleteWithinTimePeriod());
//			if ( complCriteria.isCompleteWithinTimePeriod() ) {
//
//				Calendar maxLimit = Calendar.getInstance();
//				maxLimit.setTimeInMillis(stats.getFirstAccessDate().getTime());
//				log.debug("FirstAccessDate:"+stats.getFirstAccessDate());
//				
//				if(complCriteria.getUnitOfTimeToComplete().equalsIgnoreCase("DAYS"))
//					maxLimit.add(Calendar.DATE, complCriteria.getMinutesAfterFirstCourseLaunch());
//				if(complCriteria.getUnitOfTimeToComplete().equalsIgnoreCase("Months"))
//					maxLimit.add(Calendar.MONTH, complCriteria.getMinutesAfterFirstCourseLaunch());
//				
//				if(complCriteria.getUnitOfTimeToComplete().equalsIgnoreCase("minutes"))
//					maxLimit.add(Calendar.MINUTE, complCriteria.getMinutesAfterFirstCourseLaunch());
//				
//				
//				
//				log.debug("TimeToCompleteInMinutes:"+complCriteria.getMinutesAfterFirstCourseLaunch());
//				stats.appendDebugInfo(",TimeToCompleteInMinutes:"+complCriteria.getMinutesAfterFirstCourseLaunch());
//				/*Start:LMS-8749:Allow student the entire last day*/
//				if(complCriteria.getUnitOfTimeToComplete().equalsIgnoreCase(CourseConfiguration.UNITS_MONTH) ||
//						complCriteria.getUnitOfTimeToComplete().equalsIgnoreCase(CourseConfiguration.UNITS_DAYS)){
//					/*Allow student the entire last day*/
//					maxLimit.set(Calendar.HOUR_OF_DAY,23);
//					maxLimit.set(Calendar.MINUTE,59);
//					maxLimit.set(Calendar.SECOND,59);
//				}
//				/*End:LMS-8749:Allow student the entire last day*/
//				Date now=Calendar.getInstance().getTime();
//				log.debug("current time:"+now);
//				stats.appendDebugInfo(",currenttime:"+now);
//				complete = (now.before(maxLimit.getTime()) || now.equals(maxLimit.getTime()));
//				log.debug("evaluationResult:"+complete);
//				if(!complete){
//					log.warn("Help! Primary logic of complete with in specified time gets failed, rescue my stats,[enrollment_id="+stats.getLearnerEnrollment().getId()+"]");
//					stats.appendDebugInfo(",Help!");
//					if(stats.getLastPostTestDate()!=null){
//						now.setTime(stats.getLastPostTestDate().getTime());
//						complete = (now.before(maxLimit.getTime()) || now.equals(maxLimit.getTime()));
//					}
//					else{
//						stats.appendDebugInfo(",lastPostTestDate=null,");
//					}
//					log.warn("Completion status after rescue:"+complete);
//					stats.appendDebugInfo(",completion status after rescue:"+complete+",");
//				}
//				// as this is a criteria and we have not met it we can stop
//				if ( !complete ) {
//					return false;
//				}
//			}
//
//			stats.appendDebugInfo(",isCompleteSurvey:"+complCriteria.isCompleteWithinTimePeriod());			
//			if ( complCriteria.isMustCompleteAnySurveys() ) {
//				// check survey service for any surveys for this course and learner combination
//				//complete = surveyService.getSurveysForCourseLearner(learner, course);
//				// as this is a criteria and we have not met it we can stop
//				List<Survey> dueSurveys=surveyService.getDueSurveysByLearnerEnrollment(learnerEnrollmentId);
//				log.debug("dueSurveys.size=>"+dueSurveys.size());
//				stats.appendDebugInfo(",dueSurveys.size=>"+dueSurveys.size());
//				complete=(dueSurveys.size()>0)?false:true;
//				if ( !complete ) 
//				{
//					return false;
//				}
//			}
//
//			stats.appendDebugInfo(",isCourseEvaluationSpecified:"+complCriteria.isCourseEvaluationSpecified());
//			if(complCriteria.isCourseEvaluationSpecified()){
//				// [2/15/2011] LMS-8972 :: Course Evaluation Criteria is appearing incorrectly
//				boolean surveysCompleted = this.surveyService.isCourseEvaluationCompleted(stats.getLearnerEnrollment().getId());
//				stats.appendDebugInfo(",surveyComplete:"+surveysCompleted);
//				return surveysCompleted;
//			}
//		}
//		//FaisalSiddiqui: if all criteria's are met and all conditions are passed then we should return true?
//		//return false;
//		return true;
	}
	
	private void updateLearnerAttendanceStats(List<LearnerAttendanceSummaryStatistic> stats, LearnerCourseStatistics oldStats, LearnerCourseStatistics newStats) {
		log.debug("updateLearnerAttendanceStats is called :"+stats);
		newStats.appendDebugInfo(",<previous courseCompleted:"+newStats.getPercentComplete());
		if(stats!=null && stats.size()>0)
		{
			for(LearnerAttendanceSummaryStatistic stat:stats)
			{
				log.debug("updateLearnerAttendanceStats is called.. new:"+stat.getMaxPercentCourseAttended()+" old:"+oldStats.getPercentComplete());
				if ( stat.getMaxPercentCourseAttended() > oldStats.getPercentComplete() ) 
				{
					newStats.setPercentComplete(stat.getMaxPercentCourseAttended());
				}
			}
		}
		newStats.appendDebugInfo(">,<new courseCompleted:"+newStats.getPercentComplete()+">");

	}
	
	
	/*
	 * This method will update the totalTimeSpent in course by Iterating the LearnerTimeSpentStatistic collection  
	 */
	private void updateLearnerTimeSpentStatistic(List<LearnerTimeSpentStatistic> stats, LearnerCourseStatistics oldStats, LearnerCourseStatistics newStats) {

		log.debug("updateLearnerTimeSpentStatistic OldTotalTime:"+newStats.getTotalTimeInSeconds()+" stats:"+stats);
		newStats.appendDebugInfo(",<previous timespent:"+newStats.getTotalTimeInSeconds());
		long totalTimeSpentForThisCall=0;
		if(stats!=null && stats.size()>0)
		{
			for(LearnerTimeSpentStatistic stat:stats)
			{
				log.debug("-previousTotal:"+newStats.getTotalTimeInSeconds()+"  new time:"+stat.getTimeAttendedInSeconds());
				newStats.addTimeSpentInSeconds(stat.getTimeAttendedInSeconds());
				totalTimeSpentForThisCall+=stat.getTimeAttendedInSeconds();
			}
			log.debug("New TotalTime:"+newStats.getTotalTimeInSeconds());
		}
		newStats.appendDebugInfo(">,<TimeSpentByThisCall="+totalTimeSpentForThisCall+"><new totaltimespent:"+newStats.getTotalTimeInSeconds()+">");

	}
	
	@Override
	public List<LearnerCourseStatistics> getLearnerCoursesbyEnrolmentId(Long enrollmentId){
		
		List<LearnerCourseStatistics> statsList=learnerCourseStatisticsRepository.findByLearnerEnrollmentIdEqualsAndStatusEqualsOrStatusEquals(enrollmentId, LearnerCourseStatistics.NOT_STARTED, LearnerCourseStatistics.IN_PROGRESS);
		return statsList;
	}
	
	/*
	 * This method will query the latest LearnerCourseStatistics Object 
	 * Update the old values with new 1 pass as parameter
	 * Call StatsDao to persist updated values
	 */
	public LearnerCourseStatistics updateLearnerCourseStatistics(long courseStatsId, LearnerCourseStatistics newCourseStats) {
		
		
		
		
		
		//LearnerCourseStatistics courseStatistics = statisticsDAO.getLearnerCourseStatisticsById(courseStatsId);
		log.debug("fetching learnercoursestats by id:"+courseStatsId);
		//LearnerCourseStatistics courseStatistics = statisticsDAO.loadForUpdateLearnerCourseStatistics(courseStatsId);
		LearnerCourseStatistics courseStatistics = this.learnerCourseStatisticsRepository.findOne(courseStatsId);
		log.debug("learnercoursestats retrieved:"+courseStatistics);
		// copy over new items - grab code from LearnerCourseStatistics.clone()
		//clone.setId(this.getId());
		//UPDATE PRE-TEST stats
		courseStatistics.setPreTestDate(newCourseStats.getPreTestDate());
		courseStatistics.setPretestScore(newCourseStats.getPretestScore());
		//UPDATE QUIZ STATS 
		courseStatistics.setFirstQuizDate(newCourseStats.getFirstQuizDate());
		courseStatistics.setLastQuizDate(newCourseStats.getLastQuizDate());
		courseStatistics.setHighestQuizScore(newCourseStats.getHighestQuizScore());
		courseStatistics.setLowestQuizScore(newCourseStats.getLowestQuizScore());
		courseStatistics.setAverageQuizScore(newCourseStats.getAverageQuizScore());
		courseStatistics.setNumberQuizesTaken(newCourseStats.getNumberQuizesTaken());
		
		//UPDATE POST-TEST STATS
		courseStatistics.setFirstPostTestDate(newCourseStats.getFirstPostTestDate());
		courseStatistics.setLastPostTestDate(newCourseStats.getLastPostTestDate());
		courseStatistics.setHighestPostTestScore(newCourseStats.getHighestPostTestScore());
		courseStatistics.setLowestPostTestScore(newCourseStats.getLowestPostTestScore());
		courseStatistics.setAveragePostTestScore(newCourseStats.getAveragePostTestScore());
		courseStatistics.setNumberPostTestsTaken(newCourseStats.getNumberPostTestsTaken());

		//UPDATE COMPLETION STATS
		courseStatistics.setCompleted(newCourseStats.getCompleted());
		courseStatistics.setCompletionDate(newCourseStats.getCompletionDate());
		courseStatistics.setPercentComplete(newCourseStats.getPercentComplete()>=100?100:newCourseStats.getPercentComplete());
		courseStatistics.setStatus(newCourseStats.getStatus());
		courseStatistics.setTotalTimeInSeconds(newCourseStats.getTotalTimeInSeconds());

		//FAISALSIDDIQUI: these 3 statistics are updated at the time of launchCourse method. 
		//courseStatistics.setFirstAccessDate(newCourseStats.getFirstAccessDate());
		//courseStatistics.setLastAccessDate(newCourseStats.getLastAccessDate()); LMS-6424
		//courseStatistics.setLaunchesAccrued(newCourseStats.getLaunchesAccrued());
		
		// UPDATE REFERENCES
		courseStatistics.setLearnerEnrollment(newCourseStats.getLearnerEnrollment());
		
		// UPDATE REPORT FIELDS
		courseStatistics.setReported(newCourseStats.getReported());
		courseStatistics.setReportedDate(newCourseStats.getReportedDate());
		
		//CERTIFICATE URL
		courseStatistics.setCertificateURL(newCourseStats.getCertificateURL());

		// DEBUG INFO
		courseStatistics.setDebugInfo(newCourseStats.getDebugInfo());
		log.debug("saving learnerCourseStats..");
		return this.learnerCourseStatisticsRepository.save(courseStatistics);
		//return statisticsDAO.saveLearnerCourseStatistics(courseStatistics);
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
	
	public LearnerSCOStatistics getLearnerSCOStatisticsById(long scoStatsId) {
		//return statisticsDAO.getLearnerSCOStatisticsById(scoStatsId);
		return learnerSCOStatisticsRepository.findOne(new Long(scoStatsId));
	}
	public LearnerSCOStatistics loadLearnerSCOStatisticsById(long scoStatsId) {
		//return statisticsDAO.loadLearnerSCOStatisticsById(scoStatsId);
		return learnerSCOStatisticsRepository.findOne(new Long(scoStatsId));
	}
	public List<LearnerSCOStatistics> getLearnerSCOStatistics(Long learnerID, long enrollmentId, String courseGUID) {
		return learnerSCOStatisticsRepository.findByLearnerIdAndLearnerEnrollmentId(learnerID,enrollmentId);
	}
	public LearnerSCOStatistics saveLearnerSCOStatistics(LearnerSCOStatistics scoStats) {
		//return statisticsDAO.saveLearnerSCOStatistics(scoStats);
		return this.learnerSCOStatisticsRepository.save(scoStats);
	}


	/**
	 * added by Dyutiman
	 *  method to save a Learner Course Statistics
	 *  [1/17/2011] LMS-8645 :: Used LoadForUpdate Call to update the stats properly
	 */
	public void saveLearnerCourseStatistics(LearnerCourseStatistics newCourseStats) {
		
		LearnerCourseStatistics learnerCourseStatistics = this.loadForUpdateLearnerCourseStatistics(newCourseStats.getId());
		learnerCourseStatistics = newCourseStats.clone();
		
		//newCourseStats = statisticsDAO.saveLearnerCourseStatistics(learnerCourseStatistics);
		newCourseStats = learnerCourseStatisticsRepository.save(learnerCourseStatistics);
		
	}

	public LearnerCourseStatistics getLearnerCourseStatistics(Learner learner, Course course){
		LearnerCourseStatistics learnerCourseStatistics;
		learnerCourseStatistics = learnerCourseStatisticsRepository.findFirstByLearnerEnrollmentLearnerIdAndLearnerEnrollmentCourseId(learner.getId(),course.getId());
		if(learnerCourseStatistics!=null){
			return learnerCourseStatistics;
		}
		return null;
	}

	public List<LearnerCourseActivity> getCourseActivitiesFromCourseStatistics(long statId) {
		return learnerCourseActivityRepository.findByLearnerCourseStatisticsId(statId);
	}

	/**
	 * @return the learnersToBeMailedService
	 */
	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	/**
	 * @param learnersToBeMailedService the learnersToBeMailedService to set
	 */
	public void setLearnersToBeMailedService(
			LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}
	
	public Long getLearnerSelectedCourseApprovalByEnrollmentId(long learnerEnrollmentId){
		LearningSession learningSession=learningSessionRepository.findFirstByEnrollmentIdAndCourseApprovalIdNotNullAndCourseApprovalIdGreaterThanOrderByIdDesc(learnerEnrollmentId, new Long(0));
		if(learningSession!=null){
			Number number = (Number)learningSession.getCourseApprovalId();
			return number.longValue();
		}else{
			return (long)0;
		}
	}

	@Override
	public AICCLearnerStatistics getAICCLearnerStatisticsByEnrollment(
			LearnerEnrollment learnerEnrollment) {
		//AICCLearnerStatistics aiccStats = statisticsDAO.getAICCLearnerStatisticsByEnrollmentBy(learnerEnrollment);
		AICCLearnerStatistics aiccStats = this.aICCLearnerStatisticsRepository.findTop1ByLearnerIdAndLearnerEnrollmentId(learnerEnrollment.getLearner().getId(),learnerEnrollment.getId());
		
		return aiccStats;
	}
	
	@Override
	public AICCLearnerStatistics loadForUpdateAICCLearnerStatistics(long id) {
		//return statisticsDAO.loadForUpdateAICCLearnerStatistics(id);
		return this.aICCLearnerStatisticsRepository.findOne(id);
	}
	
	@Override
	public AICCLearnerStatistics saveAICCLearnerStatistics(AICCLearnerStatistics aiccStats) {
		//return statisticsDAO.saveAICCLearnerStatistics(aiccStats);
		return aICCLearnerStatisticsRepository.save(aiccStats);
		
	}

	@Override
	public String getCurrentLearningSession(LearningSession ls) {
		LearningSession learningSession=learningSessionRepository.findFirstByEnrollmentIdAndLearnerIdAndCourseIdOrderByIdDesc(ls.getEnrollmentId(), ls.getLearnerId(),ls.getCourseId());

		if(learningSession!=null) {
			String learningSessionId = learningSession.getLearningSessionID().toString();
			return learningSessionId;
		} else {
			return null;
		}

	}

	@Override
	public List<LearningSession> getLearningSessionByCourseApproval(List<CourseApproval> lstCourseApproval) {
		List<LearningSession> lstlearningSession = new ArrayList<LearningSession>();
		List<Long> approvalIdLst = new ArrayList<Long>();
		
		for(CourseApproval approval : lstCourseApproval){
			approvalIdLst.add(approval.getId());
		}
		
		if(lstCourseApproval!=null && lstCourseApproval.size()>0){
			lstlearningSession=learningSessionRepository.findByCourseApprovalIdInAndCourseApprovalIdGreaterThan(approvalIdLst, new Long(0));
		}
		return lstlearningSession;
	}

	@Transactional
	@Override
	public LearningSession updateLearningSessionForSurvey(String redirectURL, Long learningSessionId) {
		LearningSession learningSession = null;
		try{
			learningSession = learningSessionRepository.findOne(learningSessionId);
			learningSession.setRedirectURI(redirectURL);
			learningSessionRepository.save(learningSession);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return learningSession;
	}
	
	//Added By Marium Saud
	//Method moved from TopLinkStatisticsDAO
	public List<LearnerAttendanceSummaryStatistic> getLearnerAttendaceSummaryByLearningSession(String learningSessionID,Date eventDateTime)
	{
		List<LearnerAttendanceSummaryStatistic> list=learnerAttendanceSummaryStatisticsRepository.findByLearningSessionLearningSessionID(learningSessionID); 
		
		List<LearnerAttendanceSummaryStatistic> filteredList=new ArrayList<LearnerAttendanceSummaryStatistic>();
    	if(eventDateTime!=null)
    	{
    		for(LearnerAttendanceSummaryStatistic stat:list)
    		{
    			if(stat.getStatisticDate().compareTo(eventDateTime)>=0 ) 
    			{	
    				filteredList.add(stat);
    			}
    		}
    	}
    	else
    	{
    		filteredList=list;
    	}
    	return filteredList;
	}
	
	
	public List<LearnerTimeSpentStatistic> getLearnerTimeSpentStatisticByLearningSession(String learningSessionID,Date eventDateTime) 
	{
		List<LearnerTimeSpentStatistic> list=learnerTimeSpentStatisticsRepository.findByLearningSessionLearningSessionID(learningSessionID);
		List<LearnerTimeSpentStatistic> filteredList=new ArrayList<LearnerTimeSpentStatistic>();
    	if(eventDateTime!=null)
    	{
    		for(LearnerTimeSpentStatistic stat:list)
    		{
    			if(stat.getStatisticDate().compareTo(eventDateTime)>=0 )
    			{
    				filteredList.add(stat);
    			}
    		}
    	}
    	else
    	{
    		filteredList=list;
    	}
    	return filteredList;
	}
	
	public List<LearnerAssessmentResultStatistic> getLearnerAssessmentStatisticByLearningSession(String learningSessionID,Date eventDateTime) 
	{


		List<LearnerAssessmentResultStatistic> list=learnerAssesmentResultStatisticsRepository.findByLearningSessionLearningSessionID(learningSessionID);
    	List<LearnerAssessmentResultStatistic> filteredList=new ArrayList<LearnerAssessmentResultStatistic>(); 
    	if(eventDateTime!=null)
    	{
    		for(LearnerAssessmentResultStatistic stat:list)
    		{
    			if(stat.getStatisticDate().compareTo(eventDateTime)>=0 )
    			{
    				filteredList.add(stat);
    			}	
    		}
    	}
    	else
    	{
    		filteredList=list;
    	}
    	return filteredList;
	}

	public LearnerCourseStatistics getLearnerCourseStatisticsForLearnerEnrollment(long learnerEnrollmentId) {
	 	LearnerCourseStatistics results =learnerCourseStatisticsRepository.findFirstByLearnerEnrollmentId(learnerEnrollmentId);
		if ( results!=null ) {
			return results;
		}
		return null;
	}

}