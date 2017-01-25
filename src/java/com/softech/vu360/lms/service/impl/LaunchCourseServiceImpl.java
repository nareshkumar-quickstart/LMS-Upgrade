package com.softech.vu360.lms.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.repositories.CourseRestrictedIPRepository;
import com.softech.vu360.lms.repositories.LearningSessionRepository;
import com.softech.vu360.lms.service.LaunchCourseService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.VU360Properties;

/**
 * @author jason
 *
 */
public class LaunchCourseServiceImpl implements LaunchCourseService {
	
	private static final Logger log = Logger.getLogger(LaunchCourseServiceImpl.class.getName());
	private StatisticsService statsService = null;
	private SurveyService surveyService = null;
	@Inject
	private LearningSessionRepository learningSessionRepository;
	@Inject
	private CourseRestrictedIPRepository courseRestrictedIPRepository;
	
	public String launchCourse(Learner learner, LearnerEnrollment learnerEnrollment, String brandName, Language language, String source, String externalLMSSessionID, String externalLMSUrl, int lmsProvider, int courseApprovalId) {
		try {
			log.debug("----BEGIN LAUNCH COURSE SERVICE---");
		
			log.debug("Before saveLearningSession");
			LearningSession learningSession = saveLearningSession(learner,learnerEnrollment,brandName,language,source,externalLMSSessionID,externalLMSUrl,lmsProvider,courseApprovalId);
			log.debug("After saveLearningSession");
			
			//@TODO why are we calling saveLearningSession twice..?
			log.debug("B4 surveyService.getDueSurveysByLearningSession(");
			Boolean dueSurveyExists = getDueSurveysByLearningSession(learningSession);
			
			if(dueSurveyExists){
				updateLearningSessionIfDueSurveysExists(learningSession);
			}
			log.debug("After surveyService.getDueSurveysByLearningSession(");
			log.debug("returning getLearningSessionID"+learningSession.getLearningSessionID());
			return learningSession.getLearningSessionID();
		}
		catch(Exception ex) {
			log.error("Error during launch:"+ex.getMessage(), ex);
			return null;
		}
	}
	
	@Transactional
	private LearningSession saveLearningSession(Learner learner, LearnerEnrollment learnerEnrollment, String brandName, Language language, String source, String externalLMSSessionID, String externalLMSUrl,int lmsProvider, int courseApprovalId){
		
		LearningSession learningSession = new LearningSession();
		learningSession.setCourseId(learnerEnrollment.getCourse().getCourseGUID());
		learningSession.setEnrollmentId(learnerEnrollment.getId());
		//learningSession.setLearnerId(learner.getId());
		learningSession.setLearnerId(learnerEnrollment.getLearner().getId());
		
		String learningSessionId = GUIDGeneratorUtil.generateGUID();
		learningSession.setLearningSessionID(learningSessionId);
		learningSession.setSessionStartDateTime(new Date(System.currentTimeMillis()));
		learningSession.setUniqueUserGUID(learner.getVu360User().getUserGUID());
		learningSession.setBrandName(brandName);
		learningSession.setLanguage(language);
		learningSession.setSource(source);
		learningSession.setExternalLMSSessionID(externalLMSSessionID);
		learningSession.setExternalLMSUrl(externalLMSUrl);
		learningSession.setLmsProvider(lmsProvider);
		learningSession.setCourseApprovalId(new Long(courseApprovalId));
		learningSession = learningSessionRepository.save(learningSession);
		return learningSession;
		
	}
	
	private Boolean getDueSurveysByLearningSession(LearningSession learningSession){
		Boolean dueSurveyExists= Boolean.FALSE;
		List<Survey> availableSurvey = surveyService.getDueSurveysByLearningSession(learningSession);
		if(availableSurvey != null && availableSurvey.size()>0){
			dueSurveyExists=Boolean.TRUE;
		}
		return dueSurveyExists;
		}

	@Transactional
	private void updateLearningSessionIfDueSurveysExists(LearningSession learningSession){
		StringBuffer redirectURL = new StringBuffer();
		redirectURL.append(VU360Properties.getVU360Property("lcms.redirectURL"));
		redirectURL.append(learningSession.getLearningSessionID());
		learningSession.setRedirectURI(redirectURL.toString());
		learningSession = learningSessionRepository.save(learningSession);
	}
	
	@Transactional
	public String launchCourse(String userGUID, String courseID, String brandName, Language language, String source, String externalLMSSessionID, String externalLMSUrl, int lmsProvider) {
		try {
			LearningSession learningSession = new LearningSession();
			learningSession.setCourseId(courseID);
			learningSession.setLearningSessionID(GUIDGeneratorUtil.generateGUID());
			learningSession.setSessionStartDateTime(new Date(System.currentTimeMillis()));
			learningSession.setUniqueUserGUID(userGUID);
			learningSession.setBrandName(brandName);
			learningSession.setLanguage(language);
			learningSession.setSource(source);
			learningSession.setExternalLMSSessionID(externalLMSSessionID);
			learningSession.setExternalLMSUrl(externalLMSUrl);
			learningSession.setLmsProvider(lmsProvider);
			//TODO we need to set RedirectURL in learning session if this is 
			learningSessionRepository.save(learningSession);
			return learningSession.getLearningSessionID();
		}
		catch(Exception ex) {
			log.error("Error during launch:"+ex.getMessage(), ex);
			return null;
		}
	}
	
	/**
	 * @return the statsService
	 */
	public StatisticsService getStatsService() {
		return statsService;
	}

	/**
	 * @param statsService the statsService to set
	 */
	public void setStatsService(StatisticsService statsService) {
		this.statsService = statsService;
	}

	/**
	 * @return the surveyService
	 */
	public SurveyService getSurveyService() {
		return surveyService;
	}

	/**
	 * @param surveyService the surveyService to set
	 */
	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
	
	@Override
	public boolean isValidOSHACourseJurisdication(Long courseId){
		//return statisticsDAO.isValidOSHACourseJurisdication(courseId);
		return courseRestrictedIPRepository.isValidOSHACourseJurisdication(courseId);
	}
		
}