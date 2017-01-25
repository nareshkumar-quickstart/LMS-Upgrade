package com.softech.vu360.lms.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.AssessmentConfiguration;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.LearnerAttendanceSummaryStatistic;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerTimeSpentStatistic;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CertificateService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.ExternalStatisticsProcessor;
import com.softech.vu360.lms.service.LearningSessionService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.webservice.message.lcms.LearnerCourseProgressRequest;
import com.softech.vu360.lms.webservice.message.lcms.LearnerStatsTransferRequest;
import com.softech.vu360.lms.webservice.message.lcms.LearningSessionCompleteRequest;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.VU360Properties;

public class LearningSessionServiceImpl implements LearningSessionService{

	private static final Logger log = Logger.getLogger(LearningSessionServiceImpl.class.getName());
	private StatisticsService statisticsService;
	private LearnersToBeMailedService learnersToBeMailedService= null;
	private ExternalStatisticsProcessor aiccHandler = null;
	private EntitlementService entitlementService= null;
	private AccreditationService accreditationService=null;
	private CertificateService certificateService = null;
	private SurveyService surveyService = null;
	
	private EnrollmentService enrollmentService = null;
	 
	protected static final String COURSE_COMPLETED = "Course_Completed";
	
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
		}
	
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
      }
	
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public CertificateService getCertificateService() {
		return certificateService;
	}

	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	@Override
	public boolean processLearningSessionEnded(LearningSessionCompleteRequest learningSessionCompleteRequest) {
		log.debug("learningSessionCompleted was called.");
		boolean result=true;
		try {
			String learningSessionId=learningSessionCompleteRequest.getLearningSessionId();
			Boolean courseCompleted=learningSessionCompleteRequest.isCourseCompleted();
			String certificateURL=learningSessionCompleteRequest.getCertificateURL();
			
			Date eventDateTime = null;
			if(learningSessionCompleteRequest.getEventDate()!=null){
				eventDateTime = DateUtils.truncate(learningSessionCompleteRequest.getEventDate().toGregorianCalendar().getTime(),Calendar.SECOND); 
			}
			Long learnerEnrollmentId= statisticsService.getLearningSessionByLearningSessionId(learningSessionId).getEnrollmentId();
			LearnerEnrollment le = entitlementService.getLearnerEnrollmentById(learnerEnrollmentId);
			LearnerCourseStatistics lcsDB=statisticsService.loadForUpdateLearnerCourseStatistics(le.getCourseStatistics().getId());
			boolean sendMail= false;
			if(/*!lcsDB.isCompleted()*/ !lcsDB.isCourseCompleted()){/*Fetch db status to ensure completion certificate mail is sent only once*/
				sendMail= true;
			}
			statisticsService.aggregrateStatisticsForLearningSession(learningSessionId,courseCompleted,certificateURL,eventDateTime);
			notifyExternalSystems(learningSessionId,"processLearningSessionEnded");


			Long courseApprovalId = statisticsService.getLearnerSelectedCourseApprovalByEnrollmentId(le.getId());
			CourseApproval courseApproval = null;
			if(courseApprovalId != null && courseApprovalId.longValue() > 0){
				courseApproval=accreditationService.loadForUpdateCourseApproval(courseApprovalId);	
			}
			 
			if(sendMail){
				if(courseApproval!=null && !courseApproval.getSelfReported())
					learnersToBeMailedService.emailCourseCompletionCertificate(learnerEnrollmentId, false);
				else
					learnersToBeMailedService.emailCourseCompletionCertificate(learnerEnrollmentId, true);
			}
			
		}
		catch(Exception e) {
			log.error("Error Occured in processLearningSessionEnded..["+e.getMessage()+"]", e);	
			result=false;
		}
		return result;
	}
	
	private void notifyExternalSystems(String learningSessionId,String CallerFunction) {
		LearningSession ls = statisticsService.getLearningSessionByLearningSessionId(learningSessionId);
		if ( ls.getSource() != null ) {
			String source = ls.getSource().trim();
			
			// AICC
			if ( source.equalsIgnoreCase(LearningSession.SOURCE_AICC) ) {
				log.error("CallerFunction:- " + CallerFunction);
				aiccHandler.handleLearingSessionCompleteEvent(ls,CallerFunction);
			}
			// add others here
		}
	}
	
	public StatisticsService getStatisticsService() {
		return statisticsService;
	}
	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	/**
	 * @return the aiccHandler
	 */
	public ExternalStatisticsProcessor getAiccHandler() {
		return aiccHandler;
	}

	/**
	 * @param aiccHandler the aiccHandler to set
	 */
	public void setAiccHandler(ExternalStatisticsProcessor aiccHandler) {
		this.aiccHandler = aiccHandler;
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

	@Override
	public Map recordCourseProgress(LearnerCourseProgressRequest req) {
		
		log.debug("recordCourseProgress was called.");
		Map resultMap = new HashMap();
		boolean result=true;
		try {
			CourseApproval courseApproval = null;
			Long courseApprovalId = null;
			String learningSessionId=req.getLearningSessionId();
			Boolean courseCompleted=req.getCourseCompleted();
			/*String certificateURL=req.getCertificateURL();
			
			Date eventDateTime = null;
			if(req.getEventDate()!=null){
				eventDateTime = DateUtils.truncate(req.getEventDate().toGregorianCalendar().getTime(),Calendar.SECOND); 
			}*/
			LearningSession learningSession = statisticsService.getLearningSessionByLearningSessionId(learningSessionId);
			Long learnerEnrollmentId= learningSession.getEnrollmentId();
			LearnerEnrollment le = entitlementService.getLearnerEnrollmentById(learnerEnrollmentId);
			LearnerEnrollment updateEnrollment = enrollmentService.loadForUpdateLearnerEnrollment(learnerEnrollmentId);
			LearnerCourseStatistics courseStats=statisticsService.loadForUpdateLearnerCourseStatistics(le.getCourseStatistics().getId());
			
			if(courseCompleted){
				 		if(!updateEnrollment.getMarketoCompletion()){
				 				enrollmentService.marketoPacket(le, COURSE_COMPLETED);
				 				updateEnrollment.setMarketoCompletion(true);
				 				enrollmentService.updateEnrollment(updateEnrollment);
				 	    }
				 	}
			
			//LearnerCourseStatistics newCourseStats=courseStats.clone();
			
			boolean sendMail= false;
			if(courseCompleted && courseStats.getCertificateIssueDate() == null){/*to ensure emails are sent only once per completion*/
				sendMail= true;
			}
			// LMS-13726
			if(courseCompleted && (StringUtils.isBlank(courseStats.getCertificateNumber())|| courseStats.getCertificateIssueDate() == null)) {
					courseApprovalId = statisticsService.getLearnerSelectedCourseApprovalByEnrollmentId(le.getId());
					
					if(courseApprovalId != null && courseApprovalId.longValue() > 0){
						courseApproval=accreditationService.loadForUpdateCourseApproval(courseApprovalId);	
					}
					if(courseApproval != null && courseApproval.getSelfReported()) {
						if(StringUtils.isBlank(courseStats.getCertificateNumber())) {
							certificateService.assignCompletionCertificateNo(courseStats,courseApproval);
						}
						if (courseStats.getCertificateIssueDate() == null){
							courseStats.setCertificateIssueDate( new Date() );
							this.statisticsService.saveLearnerCourseStatistics(courseStats);			
						}
					}	
				
			} 
			//End:LMS-13726
			//Save Progress
			
					
			/*log.debug("updateCourseProgress is called.. new:"+req.getCourseProgress()+" old:"+courseStats.getPercentComplete());
			if(req.getCourseProgress()>0)
				newCourseStats.setPercentComplete(req.getCourseProgress());
			
			
				
			newCourseStats.addTimeSpentInSeconds(req.getTimeInLearningSession());
			
			
			if(courseCompleted){
				if(!newCourseStats.isCompleted()) //if it was false only then update the completion date
					newCourseStats.setCompletionDate(eventDateTime);
				newCourseStats.setStatus(LearnerCourseStatistics.COMPLETED);
				
				
			}
			newCourseStats.setCompleted(courseCompleted);
			
			
				
				
			statisticsService.updateLearnerCourseStatistics(courseStats.getId(), newCourseStats);*/
			
			
			
			
			
			notifyExternalSystems(learningSessionId,"recordCourseProgress");
			
			if(courseApprovalId==null || courseApprovalId<=0)
				courseApprovalId = statisticsService.getLearnerSelectedCourseApprovalByEnrollmentId(le.getId());
			
			
			if(courseApproval==null && courseApprovalId > 0){
				courseApproval=accreditationService.loadForUpdateCourseApproval(courseApprovalId);	
			}
			
			if(sendMail){
				if(courseApproval!=null && !courseApproval.getSelfReported())
					learnersToBeMailedService.emailCourseCompletionCertificate(learnerEnrollmentId, false);
				else
					learnersToBeMailedService.emailCourseCompletionCertificate(learnerEnrollmentId, true);
			}
			
			
			
			/* LMS-16379
			 * Update Learningsession for survey
			 */
			log.debug("~~~learningSessionId:"+learningSessionId+" and CourseCompleted:"+req.getCourseCompleted());
			if(learningSessionId!=null && req.getCourseCompleted()){
				log.debug("Inside block: learningSessionId and CourseCompleted condition true");
				List<Survey> availableSurvey = null;
				try{
					availableSurvey = surveyService.getDueSurveysByLearningSession(learningSessionId);
					enrollmentService.marketoPacket(le, COURSE_COMPLETED);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				log.debug("~~~availableSurvey Size:"+(availableSurvey!=null?availableSurvey.size():"0"));
				if(availableSurvey != null && availableSurvey.size()>0){
					StringBuffer redirectURL = new StringBuffer();
					redirectURL.append(VU360Properties.getVU360Property("lcms.redirectURL"));
					redirectURL.append(learningSessionId);
					
					log.debug("~~~Start updating Redirect URL:"+redirectURL.toString());
					resultMap.put("SURVEY_URL", redirectURL.toString());
					learningSession = statisticsService.updateLearningSessionForSurvey(redirectURL.toString(), learningSession.getId());
					log.debug("~~~Finish updating Redirect URL:"+redirectURL.toString());
				}
			}

			
		}
		catch(Exception e) {
			log.error("Error Occured in processLearningSessionEnded..["+e.getMessage()+"]", e);	
			result=false;
		}
		resultMap.put("RESULT", result) ;
		
		return resultMap;
	}

	@Override
	public boolean recordLearnerStats(LearnerStatsTransferRequest req) {
		log.debug("recordLearnerStats was called.");
		boolean result=true;
		try {
			String learningSessionId=req.getLearningSessionId();
			//Boolean courseCompleted=req.getCourseCompleted();
			String certificateURL=req.getCertificateURL();
			
			Date eventDateTime = null;
			if(req.getEventDate()!=null){
				eventDateTime = DateUtils.truncate(req.getEventDate().toGregorianCalendar().getTime(),Calendar.SECOND); 
			}
			LearningSession learningSession = statisticsService.getLearningSessionByLearningSessionId(learningSessionId);
			Long learnerEnrollmentId= learningSession.getEnrollmentId();
			LearnerEnrollment le = entitlementService.getLearnerEnrollmentById(learnerEnrollmentId);
			//LearnerCourseStatistics courseStats=statisticsService.loadForUpdateLearnerCourseStatistics(le.getCourseStatistics().getId());
			
			
			//LearnerCourseStatistics newCourseStats=courseStats.clone();
			
			
			
//			boolean sendMail= false;
//			if(courseCompleted && !courseStats.isCompleted()){/*Compare new Status & LCS db status to ensure completion certificate mail is sent only once*/
//				sendMail= true;
//			}
			
			//Save Stats
			
			//this.updateLearnerAssessmentStats(req, courseStats, newCourseStats);
			
			
//			if(courseCompleted){
//				if(!newCourseStats.isCompleted()) //if it was false only then update the completion date
//					newCourseStats.setCompletionDate(eventDateTime);
//				newCourseStats.setStatus(LearnerCourseStatistics.COMPLETED);
//				
//				
//			}
//			newCourseStats.setCompleted(courseCompleted);
			
			
			//statisticsService.updateLearnerCourseStatistics(courseStats.getId(), newCourseStats);
			
			
			
			
			
			notifyExternalSystems(learningSessionId,"recordLearnerStats");
//			if(sendMail)
//				learnersToBeMailedService.emailCourseCompletionCertificate(learnerEnrollmentId);
			
			/* LMS-16379
			 * Update Learningsession for survey
			 */
			log.debug("~~~learningSessionId:"+learningSessionId+" and CourseCompleted:"+req.getCourseCompleted());
			if(learningSessionId!=null && req.getCourseCompleted()){
				log.debug("Inside block: learningSessionId and CourseCompleted condition true");
				List<Survey> availableSurvey = null;
				try{
					availableSurvey = surveyService.getDueSurveysByLearningSession(learningSessionId);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				log.debug("~~~availableSurvey Size:"+(availableSurvey!=null?availableSurvey.size():"0"));
				if(availableSurvey != null && availableSurvey.size()>0){
					StringBuffer redirectURL = new StringBuffer();
					redirectURL.append(VU360Properties.getVU360Property("lcms.redirectURL"));
					redirectURL.append(learningSessionId);
					
					log.debug("~~~Start updating Redirect URL:"+redirectURL.toString());
					learningSession = statisticsService.updateLearningSessionForSurvey(redirectURL.toString(), learningSession.getId());
					log.debug("~~~Finish updating Redirect URL:"+redirectURL.toString());
				}
			}
			
			
		}
		catch(Exception e) {
			log.error("Error Occured in updateLearnerAssessmentStats..["+e.getMessage()+"]", e);	
			result=false;
		}
		return result;
	}
	
	
	
	
	
	private void updateLearnerAssessmentStats(LearnerStatsTransferRequest req, LearnerCourseStatistics courseStats, LearnerCourseStatistics newCourseStats){

		log.debug("updateLearnerAssessmentStats is called");
		Date eventDate  = new Date(req.getEventDate().getYear(),req.getEventDate().getMonth(),req.getEventDate().getDay(),req.getEventDate().getHour(),req.getEventDate().getMinute());
		
		
		
		//CourseConfiguration complCriteria = new CourseConfiguration();//statisticsService.getCourseCompletionCriteriaForCourse(courseStats.getLearnerEnrollment().getCourse());
		
//		AssessmentConfiguration assessmentConfigPost =  null;
//		AssessmentConfiguration assessmentConfigPre =  null;
//		AssessmentConfiguration assessmentConfigQuiz =  null;
//		
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
		
			
			//temp variable to update highest PRE-TEST Score and DATE
			double highestPreTestScore=courseStats.getPretestScore();
			newCourseStats.appendDebugInfo(",<previous highestpretestscore:"+highestPreTestScore);

			int totalNumberOfQuizTaken=0;
			double sumOfQuizScore=0.0;
			double highestQuizScore=0.0;
			double lowestQuizScore=0.0;
			Date lastQuizDate = new Date();
			Date firstQuizDate = new Date();
			
			//temp variable for QUIZ STATS
			
			//if(!assessmentConfigQuiz.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT))
			{
				totalNumberOfQuizTaken=(int)courseStats.getNumberQuizesTaken();
				newCourseStats.appendDebugInfo(",totalquiztaken:"+totalNumberOfQuizTaken);
				sumOfQuizScore=(courseStats.getAverageQuizScore()*totalNumberOfQuizTaken);
				newCourseStats.appendDebugInfo(",totalquizscore:"+sumOfQuizScore);
				log.debug("1- totalNumberOfQuizTaken:"+totalNumberOfQuizTaken+" sumOfQuizScore:"+sumOfQuizScore);
						
				highestQuizScore=courseStats.getHighestQuizScore();
				newCourseStats.appendDebugInfo(",highestquizscore:"+highestQuizScore);
				lowestQuizScore=courseStats.getLowestQuizScore();
				newCourseStats.appendDebugInfo(",lowestquizscore:"+lowestQuizScore);
			
				lastQuizDate=courseStats.getLastQuizDate();
				lastQuizDate=lastQuizDate==null?new Date(System.currentTimeMillis()):lastQuizDate;
				newCourseStats.appendDebugInfo(",lastQuizDate:"+lastQuizDate);
				firstQuizDate=courseStats.getFirstQuizDate();
				firstQuizDate=firstQuizDate==null?new Date(System.currentTimeMillis()):firstQuizDate;
				newCourseStats.appendDebugInfo(",firstQuizDate:"+firstQuizDate);
			}
			
			//temp variable for Post-Test Stats
			int totalNumberOfPostTestsTaken=0;
			double sumOfPostTestsScore=0.0;
			double highestPostTestScore=0.0;
			double lowestPostTestScore=0.0;
			Date lastPostTestDate = new Date();
			Date firstPostTestDate = new Date();
			
//			if(!assessmentConfigPre.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT))
			{
				totalNumberOfPostTestsTaken=courseStats.getNumberPostTestsTaken();
				newCourseStats.appendDebugInfo(",totalPostTests:"+totalNumberOfPostTestsTaken);
				sumOfPostTestsScore=(courseStats.getAveragePostTestScore()*totalNumberOfPostTestsTaken);
				newCourseStats.appendDebugInfo(",totalPostTestsScore:"+sumOfPostTestsScore);
				log.debug("1-totalNumberOfPostTestsTaken:"+totalNumberOfPostTestsTaken+" sumOfPostTestScore:"+sumOfPostTestsScore);
				
				highestPostTestScore=courseStats.getHighestPostTestScore();
				lowestPostTestScore=courseStats.getLowestPostTestScore();
				newCourseStats.appendDebugInfo(",highestPostTestsScore:"+highestPostTestScore);
				newCourseStats.appendDebugInfo(",lowestPostTestsScore:"+lowestPostTestScore);
				lastPostTestDate=courseStats.getLastPostTestDate();
				//lastPostTestDate=lastPostTestDate==null?new Date(System.currentTimeMillis()):lastPostTestDate;
				firstPostTestDate=courseStats.getFirstPostTestDate();
				//firstPostTestDate=firstPostTestDate==null?new Date(System.currentTimeMillis()):firstPostTestDate;
				
				newCourseStats.appendDebugInfo(",firstPostTestDate:"+firstPostTestDate);
				newCourseStats.appendDebugInfo(",lastPostTestDate:"+lastPostTestDate);
			
			}
			
			
				log.debug(" ASSESSMENT_TYPE:"+req.getAssessmentType());
				if (req.getAssessmentType().equalsIgnoreCase(AssessmentConfiguration.STATS_PREASSESSMENT)) 
				{
					
					
//					if(!assessmentConfigPre.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT))
					{
					//UPDATE THE PRE-TEST STATS IF SCORE IS GREATER THAN PREVIOUS SCORE
					// this is the possibility if in a single learning session learner gets failed first time but passed in subsequent attempt.
					log.debug("highestPreTestScore:"+highestPreTestScore+" resultStas.getRawScore:"+req.getAssessmentScore());
					if(highestPreTestScore<req.getAssessmentScore())
					{
						highestPreTestScore=req.getAssessmentScore();
						log.debug("updating highestPreTestScore:"+highestPreTestScore+" date:"+req.getEventDate());
						//1- update pre-test score
						newCourseStats.setPretestScore(highestPreTestScore);
						//2- update pre-test date of highest Pre-Test Score
						newCourseStats.setPreTestDate(eventDate);

					}
					}
				}
				else if ( req.getAssessmentType().equalsIgnoreCase(AssessmentConfiguration.STATS_QUIZ))
				{
//					if(!assessmentConfigQuiz.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT))
					{
					log.debug("1- ASSESSMENT_TYPE_QUIZ..highest Quiz Score:"+highestQuizScore+" -- new Score:"+req.getAssessmentScore());
					//1- update highest Quiz Score
					if (highestQuizScore< req.getAssessmentScore() ) 
					{
						//update the variable to compare with subsequent Quiz in same learning session
						highestQuizScore=req.getAssessmentScore();
					}
					log.debug("updating highest quiz score :"+highestQuizScore);
					newCourseStats.setHighestQuizScore(highestQuizScore);

					//2- update lowest quiz score
					log.debug("2- lowestQuizScore:"+lowestQuizScore+" new QuizScore:"+req.getAssessmentScore());
					//by OWS
					if(lowestQuizScore==-1) //i.e. first time
						lowestQuizScore= req.getAssessmentScore();
					else{
						if (lowestQuizScore > req.getAssessmentScore())
						{
							lowestQuizScore=req.getAssessmentScore();
						}
					}
					log.debug("updating lowest Quiz Score:"+lowestQuizScore);
					newCourseStats.setLowestQuizScore(lowestQuizScore);

					//3- update last quiz date
					log.debug("3- lastQuizDate:"+lastQuizDate+" new QuizDaate:"+req.getEventDate());
					if(lastQuizDate.before(eventDate))
					{
						lastQuizDate=eventDate;
					}
					log.debug("updating lastQuizDate:"+lastQuizDate);
					newCourseStats.setLastQuizDate(lastQuizDate);	

					//4- update first quiz date
					log.debug("4- firstQuizDate:"+firstQuizDate+" new QuizDate:"+req.getEventDate());
					if(firstQuizDate.after(eventDate))
					{
						firstQuizDate=eventDate;
					}
					log.debug("updating.. first Quiz Date:"+firstQuizDate);
					newCourseStats.setFirstQuizDate(firstQuizDate);

					//5- update Number of Quiz
					totalNumberOfQuizTaken++;
					//6- update sum Of Quiz Score to calculate Average
					sumOfQuizScore+=req.getAssessmentScore();
					log.debug("5 & 6 totalNumberOfQuizes:"+totalNumberOfQuizTaken+" sumOfQuizScore:"+sumOfQuizScore);
					}
				}
				else if ( req.getAssessmentType().equalsIgnoreCase(AssessmentConfiguration.STATS_POSTASSESSMENT))
				{
//					if(!assessmentConfigPost.getScoringType().equals(AssessmentConfiguration.SCORETYPE_NORESULT))
					{
					log.debug("new Score:"+req.getAssessmentScore());
					//1- highest post test score update
					log.debug("1-  highestPostTestScore:"+highestPostTestScore +" -- new Score:"+req.getAssessmentScore());
					if(highestPostTestScore < req.getAssessmentScore())
					{
						highestPostTestScore=req.getAssessmentScore();
					}
					log.debug("updating new Highest Post Test Score:"+highestPostTestScore);
					newCourseStats.setHighestPostTestScore(highestPostTestScore);

					//2- lowest post test score
					log.debug("2-  lowestPostTestScore:"+lowestPostTestScore +" -- new Score:"+req.getAssessmentScore());
					
					//by OWS
					if(lowestPostTestScore==-1) //i.e. first time
						lowestPostTestScore= req.getAssessmentScore();
					else{
						
						if (lowestPostTestScore > req.getAssessmentScore()) 
						{
							lowestPostTestScore=req.getAssessmentScore();
						}
					}
					log.debug("updating new Lowest Post Test Score:"+lowestPostTestScore);
					newCourseStats.setLowestPostTestScore(lowestPostTestScore);

					
					//3- last post test taken
					// in 1 session there may be more than 1 post test can be taken and and order of post test date is not guaranteed 
					// therefore we need to take care of it in our logic
					log.debug("3- lastPostTestDate:"+lastPostTestDate+"... resultStats.StatsDate:"+eventDate);
					if(lastPostTestDate==null || lastPostTestDate.before(eventDate))
					{
						lastPostTestDate=eventDate;
					}
					log.debug("updating last Post Test Date.."+lastPostTestDate);
					newCourseStats.setLastPostTestDate(lastPostTestDate);
					
					//4- first Post test taken
					log.debug("4- firstPostTestDate:"+firstPostTestDate+"... resultStats.StatsDate:"+eventDate);
					if ( firstPostTestDate == null || firstPostTestDate.after(eventDate)) 
					{
						firstPostTestDate=eventDate;
					}
					log.debug("updating first Post Test Date.."+firstPostTestDate);
					newCourseStats.setFirstPostTestDate(firstPostTestDate);

					//5- update number of post test taken
					totalNumberOfPostTestsTaken++;
					//6- update sum of post test score to calculate average
					sumOfPostTestsScore+=req.getAssessmentScore();
					log.debug(" 5 & 6 totalNumberOfPostTestsTaken:"+totalNumberOfPostTestsTaken+" -- sumOfPostTestsScore:"+sumOfPostTestsScore);
					}
				}
				else
				{
					log.error("Unknown Assessment Type.."+req.getAssessmentType());
				}
			

			//updating Average Quiz Stats 
			log.debug(" updating Quiz Stats totalNumberOfQuizTaken:"+totalNumberOfQuizTaken+"  sumOfQuizScore:"+sumOfQuizScore);
			{
				newCourseStats.setNumberQuizesTaken(totalNumberOfQuizTaken);
				double averageQuizScore=sumOfQuizScore/(totalNumberOfQuizTaken==0?1:totalNumberOfQuizTaken);
				log.debug("averageQuizScore:"+averageQuizScore);
				newCourseStats.setAverageQuizScore(averageQuizScore);

				newCourseStats.appendDebugInfo("><new totalquiztaken:"+totalNumberOfQuizTaken);
				newCourseStats.appendDebugInfo(",totalQuizScore:"+sumOfQuizScore);
			}


			//update post-test average stats
			log.debug("updating PostTest Stats  totalNumberOfPostTestTaken:"+totalNumberOfPostTestsTaken+"  sumOfPostTestScore:"+sumOfPostTestsScore);
			{
				newCourseStats.setNumberPostTestsTaken(totalNumberOfPostTestsTaken);
				double averagePostTestScore=sumOfPostTestsScore/(totalNumberOfPostTestsTaken==0?1:totalNumberOfPostTestsTaken);
				log.debug("averagePostTestScore:"+averagePostTestScore);
				newCourseStats.setAveragePostTestScore(averagePostTestScore);

				newCourseStats.appendDebugInfo(",totalPostTests:"+totalNumberOfPostTestsTaken);
				newCourseStats.appendDebugInfo(",totalPostTestsScore:"+sumOfPostTestsScore);
			}
		
		log.debug("End of Update Learner Assessment Stats..");
		
		
		
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
	public List<LearningSession> getLearningSessionByCourseApproval(List<CourseApproval> lstCourseApproval) {
		
		return statisticsService.getLearningSessionByCourseApproval(lstCourseApproval);
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}	
	
}
