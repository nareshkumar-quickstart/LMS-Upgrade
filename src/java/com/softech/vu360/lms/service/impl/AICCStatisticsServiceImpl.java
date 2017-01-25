package com.softech.vu360.lms.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.repositories.LearningSessionRepository;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.ExternalStatisticsProcessor;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.VU360Branding;

/**
 * This is the default AICC handler for posting of statistics
 * back to a LMS.
 * 
 * @author jason
 */
public class AICCStatisticsServiceImpl implements ExternalStatisticsProcessor {
	
	private static final Logger log = Logger.getLogger(AICCStatisticsServiceImpl.class.getName());
	
	private StatisticsService statsService;
	private CourseAndCourseGroupService courseService;
	
	@Inject
	private LearningSessionRepository learningSessionRepository;
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.ExternalStatisticsProcessor#handleLearingSessionCompleteEvent(com.softech.vu360.lms.model.LearningSession)
	 */
	@Override
	public void handleLearingSessionCompleteEvent(LearningSession learningSession,String CallerFunction) {
		// TODO Auto-generated method stub
		String sessionId = learningSession.getExternalLMSSessionID();
		String aiccUrl = learningSession.getExternalLMSUrl();
		// retreive brandName for custom messaging
		String brandName = learningSession.getBrandName();
		com.softech.vu360.lms.vo.Language language = new com.softech.vu360.lms.vo.Language();
		language.setLanguage(Language.DEFAULT_LANG);
		Brander brander = VU360Branding.getInstance().getBrander(brandName, language);
		boolean IsExitAU = true; // Variable is introduced to stop calling AICC ExitAU method for legacy courses.
		
		LearnerCourseStatistics courseStats = statsService.getLearnerCourseStatisticsByLearnerEnrollmentId(learningSession.getEnrollmentId());
		Map<Object, Object> contextMap = new HashMap<Object, Object>();
		contextMap.put("courseStats", courseStats);
		
		double value = 0.0;
		NumberFormat formatter = new DecimalFormat("##.##");
		if ( courseStats.getLastPostTestDate() != null ) {
			value = courseStats.getHighestPostTestScore();
			if ( value < 1 ) {
				// convert fraction
				value *= 100;
			}
		}
		contextMap.put("score", formatter.format(value));
		
		Course course = courseService.getCourseByGUID(learningSession.getCourseId());		
		
		if (CallerFunction !="recordCourseProgress" && course.getCourseType().equalsIgnoreCase("Legacy Course")){
			IsExitAU = false;
		}
		if ( learningSession.getSessionStartDateTime() != null && learningSession.getSessionEndDateTime() != null ) {
			// this is a little more optimistic than reality, but close enough for this learning session.  Over time we may
			// notice a time floating issue where the LS 360 credits less time than this AICC adapter reports.
			long timeInMillis = learningSession.getSessionEndDateTime().getTime() - learningSession.getSessionStartDateTime().getTime();
			contextMap.put("timeSpent", FormUtil.getInstance().formatTimeVerbose(timeInMillis));
		}
		else{			
			if(course.getCourseType().equalsIgnoreCase("Legacy Course")){
				long timeCurrentTimeInMillis = Calendar.getInstance().getTimeInMillis() - learningSession.getSessionStartDateTime().getTime();
				long totalTimeSpentInSeconds = courseStats.getTotalTimeInSeconds()*1000;				
				long timeInMillis = totalTimeSpentInSeconds - (totalTimeSpentInSeconds - timeCurrentTimeInMillis);
				if (timeInMillis > 0){					
					contextMap.put("timeSpent", FormUtil.getInstance().formatTimeVerbose(timeInMillis));
				}
				try{
					if (timeInMillis > 0 && courseStats.getCompleted()==true ){					
						learningSession.setSessionEndDateTime(new Date(System.currentTimeMillis()));					
						learningSession = learningSessionRepository.save(learningSession);
					}					
				}
				catch(Exception e) {
					log.error("could not save LearningSession End Time", e);
				}	
			}
		}
		
		String aiccPutParamTemplate = brander.getBrandElement("aicc.templates.putParam");
		TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
		String putParamStr = null;
		HashMap<Object, Object> context = new HashMap<Object, Object>(contextMap);
		try {
			putParamStr = tmpSvc.renderTemplate(aiccPutParamTemplate,context);
			log.info("AICC PUT PARAM:"+putParamStr);
		}
		catch(Exception e) {
			log.error("could not render velocity template for aicc put param", e);
		}		
		
		StringBuffer getParamUrl = new StringBuffer(aiccUrl);
		HttpURLConnection connection = null;
		BufferedReader rd = null;
		StringBuilder result = null;
		String line = null;
		try {
			
			log.debug("getparam URL:"+getParamUrl.toString());

			URL serverAddress = null;

			serverAddress = new URL(getParamUrl.toString());
			// set up out communications stuff
			connection = null;
			
			StringBuffer content = new StringBuffer();

			content.append("command=putparam&version=");
			content.append(URLEncoder.encode("4.0", "UTF-8"));
			content.append("&session_id=");
			content.append(URLEncoder.encode(sessionId, "UTF-8"));
			content.append("&AICC_DATA=" + URLEncoder.encode (this.formatForNewLine(putParamStr), "UTF-8"));
			log.error("putparam call:"+content.toString());	


			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("CONTENT-TYPE", "application/x-www-form-urlencoded;charset=utf-8");
			connection.setRequestProperty("Content-Length", ""+content.toString().getBytes().length);
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			//connection.setDoInput(true);
			
			 // Send POST output.
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			wr.write(content.toString());
		    wr.flush();

		    connection.connect();

			// read the result from the server
			rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			result = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				result.append(line + '\n');
			}
		    wr.close();			
			log.error("message back from LMS putparam:\n\n"+result.toString());
			
			// TODO:  parse the response to see if there is an error
		}
		catch (Exception e) {
			log.error("error during putparam call:"+e.getMessage(), e);
		}
		finally {
			// close the connection, set all objects to null
			if ( connection != null ) {
				connection.disconnect();
			}
			rd = null;
			connection = null;
		}
		
		// now send exit au command
		getParamUrl = new StringBuffer(aiccUrl);
		StringBuffer postData = new StringBuffer();
		connection = null;
		rd = null;
		result = null;
		line = null;
		if (learningSession.getSessionEndDateTime() != null && IsExitAU){
			try {				
				if ( aiccUrl.toString().indexOf("?") > 0 ) {
					getParamUrl.append("&");
				}
				else {
					getParamUrl.append("?");
				}
				
				getParamUrl.append("command=ExitAU&version=");
				getParamUrl.append(URLEncoder.encode("4.0", "UTF-8"));
				getParamUrl.append("&session_id=");
				getParamUrl.append(URLEncoder.encode(sessionId, "UTF-8"));
				
				// for those looking in the body
				postData.append("command=ExitAU&version=");
				postData.append(URLEncoder.encode("4.0", "UTF-8"));
				postData.append("&session_id=");
				postData.append(URLEncoder.encode(sessionId, "UTF-8"));

				
				log.error("getparam URL:"+getParamUrl.toString());

				URL serverAddress = null;

				serverAddress = new URL(getParamUrl.toString());
				// set up out communications stuff
				connection = null;

				// Set up the initial connection
				connection = (HttpURLConnection) serverAddress.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("CONTENT-TYPE", "application/x-www-form-urlencoded;charset=utf-8");
				connection.setRequestProperty("Content-Length", String.valueOf(postData.toString().getBytes().length));
				connection.setDoOutput(true);
				connection.setReadTimeout(10000);
				
				// write to the body for the POST
				OutputStream out = connection.getOutputStream();
				Writer outWriter = new OutputStreamWriter(out, "UTF-8");
				outWriter.write(postData.toString());
				outWriter.flush();
				outWriter.close();

				connection.connect();

				// read the result from the server
				rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				result = new StringBuilder();

				while ((line = rd.readLine()) != null) {
					result.append(line + '\n');
				}
				
				log.info("message back from LMS from exitAU:\n\n"+result.toString());
				
				// TODO:  parse the response to see if there is an error
			}
			catch (Exception e) {
				log.error("error during putparam call:"+e.getMessage(), e);
			}
			finally {
				// close the connection, set all objects to null
				if ( connection != null ) {
					connection.disconnect();
				}
				rd = null;
				connection = null;
			}
		}
	}

	/**
	 * This method replaces all CR with CRLF
	 * @param inputString
	 * @return
	 */
	private String formatForNewLine(String inputString){	
		String outputStr= inputString.replaceAll("\n", "\r\n");
		log.debug(outputStr);
		return outputStr;
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
	 * @return the courseService
	 */
	public CourseAndCourseGroupService getCourseService() {
		return courseService;
	}

	/**
	 * @param courseService the courseService to set
	 */
	public void setCourseService(CourseAndCourseGroupService courseService) {
		this.courseService = courseService;
	}

	public LearningSession getLearningSessionByLearningSessionId(String learningSessionId){
		//return statisticsDAO.getLearningSessionByLearningSessionId(learningSessionId);
		return learningSessionRepository.findTop1ByLearningSessionID(learningSessionId);
		
	}
}
