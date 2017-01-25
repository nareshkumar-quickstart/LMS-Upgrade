package com.softech.vu360.lms.web.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.model.AICCAssignableUnit;
import com.softech.vu360.lms.model.AICCLearnerStatistics;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AICCAssignableUnitService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.ExternalStatisticsProcessor;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.util.FormUtil;

public class AICCCourseController implements Controller {

	private Logger log = Logger.getLogger(AICCCourseController.class.getName());
	private StatisticsService statisticsService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private String getParamTemplate = null;
	private ExternalStatisticsProcessor aiccHandler=null;
	private EnrollmentService enrollmentService;
	private AICCAssignableUnitService aiccAssignableUnitService=null;
	protected static final String COURSE_COMPLETED = "Course_Completed";
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	String command = request.getParameter("command");
		Map<Object, Object> context = new HashMap<Object, Object>();
		String lessonLocation = null;
		String lessonStatus = null;
		String score = null;
		String time = null;		
		double avg ;
		if(command.equalsIgnoreCase("getParam")) {
			try {
				log.info("AICC Course get param session");
				String learningSessionId = request.getParameter("session_id");
				LearningSession learningSession = statisticsService.getLearningSessionByLearningSessionId(learningSessionId);
				if(learningSession == null) {
					context.put("paramValue", "LearningSessionNull");
					return new ModelAndView(getParamTemplate, "context", context);
				} else {
					LearnerCourseStatistics courseStats = statisticsService.getLearnerCourseStatisticsByLearnerEnrollmentId(learningSession.getEnrollmentId());
					if(courseStats != null) {
						AICCLearnerStatistics aiccStats = statisticsService.getAICCLearnerStatisticsByEnrollment(courseStats.getLearnerEnrollment());
						
						if(aiccStats == null) {
							aiccStats = new AICCLearnerStatistics();
							aiccStats.setLearnerId(courseStats.getLearnerEnrollment().getLearner().getId());
							aiccStats.setLearnerEnrollmentId(courseStats.getLearnerEnrollment().getId());
							aiccStats.setCreateDate(new Date());
							aiccStats.setCredit("");
							aiccStats.setLastUpdateDate(new Date());
							aiccStats.setLessonLocation("");
							aiccStats.setStatus("I");
							aiccStats.setTotalTimeInSeconds(0);
							
							aiccStats = statisticsService.saveAICCLearnerStatistics(aiccStats);
						}
						
						VU360User user = courseStats.getLearnerEnrollment().getLearner().getVu360User();
							
						context.put("user", user);

						//Course/exam location get from DB to start course/exam from the last location learner saved and closed.
						context.put("Lesson_Location", aiccStats.getLessonLocation());
							
						context.put("isSuccessfullyCall", true);
							
						//credit hours set successfully will change when get requirement to save/get value from DB
						context.put("credit", aiccStats.getCredit());
						
						if(courseStats.getStatus().equalsIgnoreCase(LearnerCourseStatistics.NOT_STARTED)) {
							context.put("Lesson_Status", "N");
						} else if(courseStats.getStatus().equalsIgnoreCase(LearnerCourseStatistics.IN_PROGRESS)) {
							context.put("Lesson_Status", "I");
						} else {
							context.put("Lesson_Status", "C");
						}
						if(courseStats.getLastPostTestDate() != null) {
							double scoreParam = courseStats.getHighestPostTestScore();
							if(scoreParam < 1) {
								scoreParam *= 100;
							}
							context.put("score", new DecimalFormat("##.##").format(scoreParam));
						} else {
							context.put("score", new DecimalFormat("##.##").format(courseStats.getHighestPostTestScore()));
						}
						context.put("Time", FormUtil.getInstance().formatTimeVerbose(courseStats.getTotalTimeInSeconds() * 1000));
						context.put("version", 4.0);
						context.put("paramValue", "getParam");
					}
					
					return new ModelAndView(getParamTemplate, "context", context);
				}
			} catch(Exception ex) {
				log.error("Get Param Exception:" + ex);
			}	
		} else if(command.equalsIgnoreCase("putParam")) {
			try {
				log.info("AICC Course put param request");
				String learningSessionId = request.getParameter("session_id");
				if(learningSessionId == null || learningSessionId.isEmpty()) {
					context.put("error", "AICC Session ID is null");
					log.error("Learning Session ID is null");
					return new ModelAndView(getParamTemplate, "context", context);
				}
				String aiccData = request.getParameter("aicc_data");				
				if(aiccData == null || aiccData.isEmpty() || aiccData.equals("")) {
					log.error("Put Param AICC Data is null");
					context.put("error", "Put Param AICC Data is null");
					return new ModelAndView(getParamTemplate, "context", context);
				}
				StringTokenizer token = new StringTokenizer(aiccData, "\r\n");
				String[] requestParams = new String[token.countTokens()];
				
				int index = 0;
				while(token.hasMoreTokens()) {
					requestParams[index] = token.nextToken();
					index++;
				}
				
				for(int i = 0; i < requestParams.length; i++) {
					if(requestParams[i].indexOf("=") > 0) {
						if(requestParams[i].contains("lesson_location"))
							lessonLocation = requestParams[i].substring(requestParams[i].indexOf("=")+1);
						else if(requestParams[i].contains("lesson_status"))
							lessonStatus = requestParams[i].substring(requestParams[i].indexOf("=")+1);
						else if(requestParams[i].contains("score"))
							score = requestParams[i].substring(requestParams[i].indexOf("=")+1);
						else if(requestParams[i].contains("time"))
							time = requestParams[i].substring(requestParams[i].indexOf("=")+1);
					}
					
				}
				LearningSession learningSession = statisticsService.getLearningSessionByLearningSessionId(learningSessionId);
				if(learningSession == null) {
					context.put("paramValue", "LearningSessionNull");
					return new ModelAndView(getParamTemplate, "context", context);
				} else {
					LearnerCourseStatistics courseStatistics = statisticsService.getLearnerCourseStatisticsByLearnerEnrollmentId(learningSession.getEnrollmentId());
					LearnerCourseStatistics courseStats = statisticsService.loadForUpdateLearnerCourseStatistics(courseStatistics.getId());
					LearnerEnrollment le = enrollmentService.loadForUpdateLearnerEnrollment(learningSession.getEnrollmentId());
					LearnerEnrollment updateEnrollmented = enrollmentService.loadForUpdateLearnerEnrollment(le.getId());
					AICCLearnerStatistics aiccLearnerStats = statisticsService.getAICCLearnerStatisticsByEnrollment(courseStats.getLearnerEnrollment());
					AICCLearnerStatistics aiccStats = statisticsService.loadForUpdateAICCLearnerStatistics(aiccLearnerStats.getId());
					courseStats.setDebugInfo("lastdebugInfo:-" + new Date() + " " + aiccData);
					log.info(lessonStatus);
					
					if(!courseStats.getCompleted())
					{
						if(lessonStatus == null || lessonStatus.isEmpty() || lessonStatus.equals("")) {
							courseStats.setStatus("");
							aiccStats.setStatus("");
						}
						else {
							if(lessonStatus.equalsIgnoreCase("C") || lessonStatus.equalsIgnoreCase("complete")
									|| lessonStatus.equalsIgnoreCase("Pass") || lessonStatus.equalsIgnoreCase("P")
									|| lessonStatus.equalsIgnoreCase("completed")){
								
								courseStats.setCompleted(true);
								courseStats.setStatus(LearnerCourseStatistics.COMPLETED);
								if(!le.getMarketoCompletion()){
  								    log.debug("AICC Cuorse Marketo Status before Update" + le.getMarketoCompletion() );
  									enrollmentService.marketoPacket(le, COURSE_COMPLETED);
									LearnerEnrollment updateLearnerEnrollment = enrollmentService.loadForUpdateLearnerEnrollment(le.getId());
									updateLearnerEnrollment.setMarketoCompletion(Boolean.TRUE);
									enrollmentService.updateEnrollment(updateLearnerEnrollment);
  								}
								courseStats.setPercentComplete(100);
								if (courseStats.getCompletionDate() == null)
										courseStats.setCompletionDate(new Date());
								
							}else if(lessonStatus.equalsIgnoreCase("B") || lessonStatus.equalsIgnoreCase("browse")
									|| lessonStatus.equalsIgnoreCase("I") || lessonStatus.equalsIgnoreCase("incomplete")
									|| lessonStatus.equalsIgnoreCase("F") || lessonStatus.equalsIgnoreCase("Failed")) {							
								courseStats.setStatus(LearnerCourseStatistics.IN_PROGRESS);						
							}else if(lessonStatus.equalsIgnoreCase("N") || lessonStatus.equalsIgnoreCase("NA"))
								courseStats.setStatus(LearnerCourseStatistics.NOT_STARTED); 
							aiccStats.setStatus(lessonStatus);
						}
					}
					if( score != null){
						if ( ( score.isEmpty() || score.equals("") || score.trim().equals("0") ) && (courseStats.getHighestPostTestScore() <= 0) ) {
								courseStats.setHighestPostTestScore(0.0);
						} else {
							
							if( (courseStats.getHighestPostTestScore() < Double.parseDouble(score)) || (courseStats.getLowestPostTestScore() > Double.parseDouble(score)) ) {
								
								//Set Number of Post Tests given
								if(courseStats.getNumberPostTestsTaken() <= 0) {
									courseStats.setNumberPostTestsTaken(1);
								} else {
									courseStats.setNumberPostTestsTaken(courseStats.getNumberPostTestsTaken() + 1);
								}
								
								//Set Highest Score
								if(courseStats.getHighestPostTestScore() < Double.parseDouble(score)) {
									courseStats.setHighestPostTestScore(Double.parseDouble(score));
								}
								//Set Lowest Score
								if(courseStats.getLowestPostTestScore() == -1 || 
										courseStats.getLowestPostTestScore() > Double.parseDouble(score)) {
									courseStats.setLowestPostTestScore(Double.parseDouble(score));
								}	
							
								//Set Average Post test scores
								if (courseStats.getNumberPostTestsTaken()>1){
									avg = (courseStats.getLowestPostTestScore() + courseStats.getHighestPostTestScore()) / courseStats.getNumberPostTestsTaken();
									courseStats.setAveragePostTestScore(avg);
								}else{
									courseStats.setAveragePostTestScore(Double.parseDouble(score));
								}
								
								//Set last Post Test Date
								courseStats.setLastPostTestDate(new Date());
								
								//Set First Post Test Date
								if(courseStats.getFirstPostTestDate() == null) {
									courseStats.setFirstPostTestDate(new Date());
								}
							}
						}
					}
					
					if(time != null && !time.equals("")) {
						SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
						Date date = format.parse(time);
						int timeInSeconds = (date.getHours() * 3600) + (date.getMinutes() * 60) + (date.getSeconds());
						/**
						 * Modified By Marium Saud
						 * Removed mapping for AICCAssignableUnit from Course.java
						 * Loading AICCAssignableUnit using findOne
						 */
						AICCAssignableUnit assignableUnit = aiccAssignableUnitService.findAICCAssignableUnitByCourseId(courseStats.getLearnerEnrollment().getCourse().getId()); //courseStats.getLearnerEnrollment().getCourse().getAICCAssignableUnit();
						if(assignableUnit.getType().equals("lesson")) {
							courseStats.setTotalTimeInSeconds(timeInSeconds);
							aiccStats.setTotalTimeInSeconds(timeInSeconds);
						} else {
							courseStats.setTotalTimeInSeconds(courseStatistics.getTotalTimeInSeconds() + timeInSeconds);
							aiccStats.setTotalTimeInSeconds(courseStatistics.getTotalTimeInSeconds() + timeInSeconds);
						}
					}
					
					aiccStats.setLessonLocation(lessonLocation);
					aiccStats.setLastUpdateDate(new Date());
					
					statisticsService.saveAICCLearnerStatistics(aiccStats);
					statisticsService.saveLearnerCourseStatistics(courseStats);
					context.put("Lesson_Location", lessonLocation);
					context.put("Lesson_Status", lessonStatus);
					context.put("Score", score);
					context.put("Time", time);
					context.put("paramValue", "putParam");
					if ( learningSession.getSource() != null && 
							learningSession.getSource().trim().equalsIgnoreCase(LearningSession.SOURCE_AICC)) {
						/**
						 * Get current learning session for reverse AICC.
						 * When putParam call from external lms to save stats, it's given old learning session id
						 */
						
						String currentLearningSessionId = statisticsService.getCurrentLearningSession(learningSession);
						LearningSession currentLearningSession = statisticsService.getLearningSessionByLearningSessionId(currentLearningSessionId);	
						aiccHandler.handleLearingSessionCompleteEvent(currentLearningSession,"PutParamAICCNotify3rdPartyLMS");					
											
					}	
					return new ModelAndView(getParamTemplate, "context", context);

				}
				
			} catch(Exception ex) {
				log.error("Put Param Exception:" + ex);
			}
			
		} else if(command.equalsIgnoreCase("putComments")) {
			log.info("Put comments condition");
		} else if(command.equalsIgnoreCase("putObjectives")) {
			log.info("Put objective condition");
		} else if(command.equalsIgnoreCase("putPath")) {
			log.info("Put path condition");
		} else if(command.equalsIgnoreCase("putInteractions")) {
			log.info("Put interactions condition");
		} else if(command.equalsIgnoreCase("putPerformance")) {
			log.info("Put performance condition");
		} else if(command.equalsIgnoreCase("ExitAU")) {
			try {
				String learningSessionId = request.getParameter("session_id");
				LearningSession learningSession = statisticsService.getLearningSessionByLearningSessionId(learningSessionId);
				//LearnerCourseStatistics courseStatistics = statisticsService.getLearnerCourseStatisticsByLearnerEnrollmentId(learningSession.getEnrollmentId());
				//LearnerCourseStatistics courseStats = statisticsService.loadForUpdateLearnerCourseStatistics(courseStatistics.getId());
				
				
				/**
				 * Get current learning session for reverse AICC.
				 * When putParam call for stats save, external lms given first learning session id
				 */
				String currentLearningSessionId = statisticsService.getCurrentLearningSession(learningSession);
				LearningSession currentLearningSession = statisticsService.getLearningSessionByLearningSessionId(currentLearningSessionId);
				
				statisticsService.saveLearningSession(currentLearningSession);
				//statisticsService.saveLearnerCourseStatistics(courseStats);
				log.info("Exit AU successfully for AICC course");
				if ( learningSession.getSource() != null && 
						learningSession.getSource().trim().equalsIgnoreCase(LearningSession.SOURCE_AICC)) {	
						aiccHandler.handleLearingSessionCompleteEvent(currentLearningSession,"ExitAUAICCNotify3rdPartyLMS");					
										
				}
			} catch(Exception ex) {
				log.error("Exit AU Exception:" + ex);
			}
		} else if(!command.equalsIgnoreCase("getParam") 
				|| !command.equalsIgnoreCase("putParam") || !command.equalsIgnoreCase("ExitAU")){
			context.put("paramValue", "wrongParam");
			return new ModelAndView(getParamTemplate, "context", context);
		} else if(command.equalsIgnoreCase("close_window_on_exit")) {
			log.info("close windows on exit call");
		}
		return null;
	}
	
	/**
	 * @return the statisticsService
	 */
	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * @param statisticsService the statisticsService to set
	 */
	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	
	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	/**
	 * @return the getParamTemplate
	 */
	public String getGetParamTemplate() {
		return getParamTemplate;
	}

	/**
	 * @param getParamTemplate the getParamTemplate to set
	 */
	public void setGetParamTemplate(String getParamTemplate) {
		this.getParamTemplate = getParamTemplate;
	}
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

	public EnrollmentService getEnrollmentService() {
		  return enrollmentService;
	}
	
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	 }
}
