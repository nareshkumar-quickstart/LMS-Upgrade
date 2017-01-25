package com.softech.vu360.lms.web.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.model.ADLStore;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerNote;
import com.softech.vu360.lms.model.LearnerSCOAssessment;
import com.softech.vu360.lms.model.LearnerSCOObjectiveStatistics;
import com.softech.vu360.lms.model.LearnerSCOStatistics;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.SCOInteraction;
import com.softech.vu360.lms.repositories.LearningSessionRepository;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.ExternalStatisticsProcessor;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.VU360Branding;

import net.sf.json.JSONObject;

/**
 * Menu Controller class used to fix/determine the current
 * page's feature and feature group.
 * @author Jason Burns
 *
 */
public class ScormController implements Controller {
	
	private static final Logger log = Logger.getLogger(ScormController.class.getName());
	
	public static final String SCORM_GET_VALUE = "getValue";
	public static final String SCORM_SET_VALUE = "setValue";
	public static final String SCORM_TERMINATE = "terminate";
	public static final String SCORM_COMMIT = "commit";
	
	public static final String SCORM_STATUS_NOT_INITIALIZED = "not initialized";
	public static final String SCORM_STATUS_RUNNING = "running";
	public static final String SCORM_STATUS_TERMINATED = "terminated";
	protected static final String COURSE_COMPLETED = "Course_Completed";
	
	private StatisticsService statsService;
	private EntitlementService entitlementService;
	private ExternalStatisticsProcessor aiccHandler=null;
	//private StatisticsDAO statisticsDAO;
	private LearnersToBeMailedService learnersToBeMailedService;
	@Inject
	private LearningSessionRepository learningSessionRepository;
	private EnrollmentService enrollmentService;

	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) {
		try {
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// have we already been through this once for this learner?
			LearnerSCOStatistics scoStats = (LearnerSCOStatistics)request.getSession().getAttribute("scoStats");
			String learningSessionId = (String)request.getSession().getAttribute("learningSessionId");
			Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			
			response.setHeader("cache-control", "no-cache");
			response.setHeader("pragma", "no-cache");
			response.setHeader("expires", "0");
			
			if ( scoStats == null ) {				
				LearningSession learningSession = statsService.getLearningSessionByLearningSessionId(learningSessionId);
				long enrollmentId = learningSession.getEnrollmentId();
				String courseGUID = learningSession.getCourseId();
				List<LearnerSCOStatistics> lrnScoStats = statsService.getLearnerSCOStatistics(user.getLearner().getId(), enrollmentId, courseGUID);
				
				// need to identify which sco was launched in the case of a multi-sco SCORM course wich we do not currenlty support, so
				// for now simply pull the first one and throw an exception if there is more than one...
				if ( lrnScoStats == null || lrnScoStats.isEmpty() ) {
					log.info("no SCOStats for course in learningSession"+learningSession.getLearningSessionID());
					JSONObject jsObj = new JSONObject();
					jsObj.put("errorCode", 200);
					response.setHeader("content-type", "application/json");
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bos.write(jsObj.toString().getBytes());
					response.setContentLength(bos.size());
					bos.writeTo(response.getOutputStream());
					bos.flush();
					bos.close();
					return null;
				}
				else if ( lrnScoStats.size() > 1 ) {
					log.info("too many SCOStats for course in learningSession"+learningSession.getLearningSessionID());
					JSONObject jsObj = new JSONObject();
					jsObj.put("errorCode", 200);
					response.setHeader("content-type", "application/json");
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bos.write(jsObj.toString().getBytes());
					response.setContentLength(bos.size());
					bos.writeTo(response.getOutputStream());
					bos.flush();
					bos.close();
					return null;
				}
				scoStats = lrnScoStats.get(0);
				scoStats = statsService.loadLearnerSCOStatisticsById(scoStats.getId());
				request.getSession().setAttribute("scoStats", scoStats);
			}
			
			
			String method = request.getParameter("method");
			String key = request.getParameter("key");
			String value = request.getParameter("value");
			String parameter = request.getParameter("parameter");

			log.info("-------------------------------------------------");
			log.info("RECEIVED SCORM REQUEST");
			log.info("-------------------------------------------------");
			log.info("\tmethod:"+method);
			log.info("\tkey:"+key);
			log.info("\tvalue:"+value);
			log.info("\tparameter:"+parameter);
			log.info("\tlearningSessionId: "+learningSessionId);

			
			
			
			if ( method.equalsIgnoreCase(SCORM_GET_VALUE) ) {
				String resultStr = getValue(user, scoStats, key.trim(), brander);
				if ( resultStr == null || resultStr.trim().equalsIgnoreCase("") ) {
					resultStr = "";
				}
				log.info("\tsending value:"+resultStr);
				// send response
				JSONObject jsObj = new JSONObject();
				jsObj.put("result", resultStr);
				jsObj.put("errorCode", 0);
				response.setHeader("content-type", "application/json");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bos.write(jsObj.toString().getBytes());
				response.setContentLength(bos.size());
				bos.writeTo(response.getOutputStream());
				bos.flush();
				bos.close();
			}
			else if ( method.equalsIgnoreCase(SCORM_SET_VALUE) ) {
				scoStats = setValue(user, scoStats, key.trim(), value);
				request.getSession().setAttribute("scoStats", scoStats);
				// send response
				JSONObject jsObj = new JSONObject();
				jsObj.put("errorCode", 0);
				response.setHeader("content-type", "application/json");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bos.write(jsObj.toString().getBytes());
				response.setContentLength(bos.size());
				bos.writeTo(response.getOutputStream());
				bos.flush();
				bos.close();
			}
			else if ( method.equalsIgnoreCase(SCORM_COMMIT) ) {
				// send response
				JSONObject jsObj = new JSONObject();
				jsObj.put("errorCode", 0);

				response.setHeader("content-type", "application/json");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bos.write(jsObj.toString().getBytes());
				response.setContentLength(bos.size());
				bos.writeTo(response.getOutputStream());
				bos.flush();
				bos.close();
			}
			else if ( method.equalsIgnoreCase(SCORM_TERMINATE) ) {
				LearnerEnrollment le = entitlementService.getLearnerEnrollmentById(scoStats.getLearnerEnrollmentId());
				LearnerCourseStatistics learnerCourseStatistics = statsService.getLearnerCourseStatisticsForLearnerEnrollment(le);
				boolean result = terminate(user, scoStats, learnerCourseStatistics,learningSessionId);
				request.getSession().removeAttribute("scoStats");

				// send response
				JSONObject jsObj = new JSONObject();
				if ( result ) {
					jsObj.put("errorCode", 200);
				}
				else {
					jsObj.put("errorCode", 0);
				}
				response.setHeader("content-type", "application/json");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bos.write(jsObj.toString().getBytes());
				response.setContentLength(bos.size());
				bos.writeTo(response.getOutputStream());
				bos.flush();
				bos.close();
			}
			else {
				// send response
				JSONObject jsObj = new JSONObject();
				jsObj.put("errorCode", 200);
				response.setHeader("content-type", "application/json");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bos.write(jsObj.toString().getBytes());
				response.setContentLength(bos.size());
				bos.writeTo(response.getOutputStream());
				bos.flush();
				bos.close();
				return null;
			}
			
			log.info("-------------------------------------------------");
		} catch (Exception e) {
			log.error("exception", e);
		}
		return null;
	}
	
	private String getValue(com.softech.vu360.lms.vo.VU360User user, LearnerSCOStatistics scoStats, String key, Brander brander) {
		if ( key == null ) {
			return null;
		}

		if ( key.equalsIgnoreCase("cmi.progress_measure") ) {
			return String.valueOf(scoStats.getProgressMeasure());
		}
		if ( key.equalsIgnoreCase("cmi.completion_status") ) {
			return scoStats.getCompletionStatus();
		}
		if ( key.equalsIgnoreCase("cmi.completion_threshold")) {
			return scoStats.getSco().getCompletionThreshold().toString();
		}
		
		if(key.equalsIgnoreCase("cmi.student_data.mastery_score") || key.equalsIgnoreCase("cmi.scaled_passing_score"))
		{
			return String.valueOf(scoStats.getSco().getScaledPassingScore());
		}
		
		if ( key.equalsIgnoreCase("cmi.credit") ) {
			return scoStats.getCredit();
		}
		if ( key.equalsIgnoreCase("cmi.entry") ) {
			return scoStats.getEntry();
		}
		if ( key.equalsIgnoreCase("cmi.exit") ) {
			return scoStats.getExit();
		}
		
		/*if ( key.equalsIgnoreCase("cmi.scaled_passing_score") ) {
			return String.valueOf(scoStats.getSco().getScaledPassingScore());
		}
		*/
		if ( key.equalsIgnoreCase("cmi.score.scaled") ) {
			if ( scoStats != null && scoStats.getCurrentAssessmentAttempt() != null ) {
				return String.valueOf(scoStats.getCurrentAssessmentAttempt().getScaledScore());
			}
			return null;
		}
		if ( key.equalsIgnoreCase("cmi.score.raw") || key.equalsIgnoreCase("cmi.core.score.raw")) {
			if ( scoStats != null && scoStats.getCurrentAssessmentAttempt() != null ) {
				return String.valueOf(scoStats.getCurrentAssessmentAttempt().getRawScore());
			}
			return null;
		}
		if ( key.equalsIgnoreCase("cmi.score.max") || key.equalsIgnoreCase("cmi.core.score.max") ) {
			if ( scoStats != null && scoStats.getCurrentAssessmentAttempt() != null ) {
				return String.valueOf(scoStats.getCurrentAssessmentAttempt().getMaxScore());
			}
			return null;
		}
		if ( key.equalsIgnoreCase("cmi.score.min") || key.equalsIgnoreCase("cmi.core.score.min") ) {
			if ( scoStats != null && scoStats.getCurrentAssessmentAttempt() != null ) {
				return String.valueOf(scoStats.getCurrentAssessmentAttempt().getMinScore());
			}
			return null;
		}
		if ( key.equalsIgnoreCase("cmi.core.lesson_location") ||  key.equalsIgnoreCase("cmi.location")) {
			return scoStats.getScoLocation();
		}
		if ( key.equalsIgnoreCase("cmi.success_status") ) {
			return scoStats.getCompletionStatus();
		}
		if ( key.equalsIgnoreCase("cmi.time_limit_action") ||  key.equalsIgnoreCase("cmi.student_data.time_limit_action")) {
			return scoStats.getSco().getTimeLimitAction();
		}
		if ( key.equalsIgnoreCase("cmi.total_time") ) {
			String timeStr = null;
			try {
				// format of outgoing time:  P[yY][mM][dD][T[hH][mM][s[.s]S]]
				long totalTimeSeconds = scoStats.getTotalTimeInSeconds();
				if ( totalTimeSeconds == 0 ) {
					// simple just return no time
					return "PT0S";
				}
				// we are only supporting the time component so we are ignoring the years, months and days and sending 
				// everything back in hours, minutes and seconds as:  PT5H3M15S for 5 hours, 3 minutes and 15 seconds.
				StringBuffer sb = new StringBuffer();
				sb.append("PT");
				// get it back in a familiar hh:mm:ss format
				timeStr = FormUtil.getInstance().formatTimeVerbose(totalTimeSeconds*1000L);
				String[] timeArray = timeStr.split(":");
				if ( Long.valueOf(timeArray[0]) > 0 ) {
					sb.append(timeArray[0]);
					sb.append("H");
				}
				if ( Long.valueOf(timeArray[1]) > 0 ) {
					sb.append(timeArray[1]);
					sb.append("M");
				}
				if ( Long.valueOf(timeArray[2]) > 0 ) {
					sb.append(timeArray[2]);
					sb.append("S");
				}
				return sb.toString();
			}
			catch (NumberFormatException nfe ) {
				log.error("could not parse out total time:"+timeStr, nfe);
			}
			return "";
		}
		
		if ( key.equalsIgnoreCase("cmi.max_time_allowed") ) {
			String timeStr = null;
			try {
				// format of outgoing time:  P[yY][mM][dD][T[hH][mM][s[.s]S]]
				long totalTimeSeconds = scoStats.getSco().getMaximumTimeAllowedSeconds();
				if ( totalTimeSeconds == 0 ) {
					// simple just return no time
					return "PT0S";
				}
				// we are only supporting the time component so we are ignoring the years, months and days and sending 
				// everything back in hours, minutes and seconds as:  PT5H3M15S for 5 hours, 3 minutes and 15 seconds.
				StringBuffer sb = new StringBuffer();
				sb.append("PT");
				// get it back in a familiar hh:mm:ss format
				timeStr = FormUtil.getInstance().formatTimeVerbose(totalTimeSeconds*1000L);
				String[] timeArray = timeStr.split(":");
				if ( Long.valueOf(timeArray[0]) > 0 ) {
					sb.append(timeArray[0]);
					sb.append("H");
				}
				if ( Long.valueOf(timeArray[1]) > 0 ) {
					sb.append(timeArray[1]);
					sb.append("M");
				}
				if ( Long.valueOf(timeArray[2]) > 0 ) {
					sb.append(timeArray[2]);
					sb.append("S");
				}
				return sb.toString();
			}
			catch (NumberFormatException nfe ) {
				log.error("could not parse out total time:"+timeStr, nfe);
			}
			return "";
		}
		
		if ( key.equalsIgnoreCase("cmi.suspend_data") ) {
			return scoStats.getSupsendData();
		}
		if ( key.equalsIgnoreCase("cmi.interactions._count") ) {
			if ( scoStats != null && scoStats.getCurrentAssessmentAttempt() != null &&
					scoStats.getCurrentAssessmentAttempt().getScoInteractions() != null ) {
				return String.valueOf(scoStats.getCurrentAssessmentAttempt().getScoInteractions().size());
				
			}
			return null;
		}
		if ( key.equalsIgnoreCase("cmi.core.lesson_mode") ) {
			return "normal";
		}
		if ( key.equalsIgnoreCase("cmi.core.lesson_status") ) {
			return scoStats.getCompletionStatus();
		}
		if ( key.equalsIgnoreCase("cmi.core.credit") ) {
			return scoStats.getCredit();
		}
		if ( key.equalsIgnoreCase("cmi.core.entry") ) {
			return scoStats.getEntry();
		}
		if ( key.equalsIgnoreCase("cmi.core.student_id") || key.equalsIgnoreCase("cmi.learner_id") ) {
			//return user.getUserGUID();
			return String.valueOf(user.getId());
		}
		if ( key.equalsIgnoreCase("cmi.core.student_name") || key.equalsIgnoreCase("cmi.core.learner_name") ) {
			return user.getName();
		}
		if ( key.equalsIgnoreCase("cmi.objectives._count") ) {
			return String.valueOf(scoStats.getSco().getScoObjectives().size());
		}
		if ( key.equalsIgnoreCase("cmi.launch_data") ) {
			return scoStats.getSco().getLaunchData();
		}
		//if ( key.equalsIgnoreCase("cmi.student_data.mastery_score") ) {
		//	return String.valueOf(scoStats.getSco().getScaledPassingScore());
		//}
		if ( key.equalsIgnoreCase("cmi.student_data.max_time_allowed") ) {
			//return String.valueOf(scoStats.getSco().getMaximumTimeAllowedSeconds());
			long maxAllowedTime = scoStats.getSco().getMaximumTimeAllowedSeconds();
			String formattedTimeSpan = FormUtil.getInstance().formatTimeVerbose(maxAllowedTime*1000L);
			if(formattedTimeSpan !=null && formattedTimeSpan.length() >0)
				formattedTimeSpan = formattedTimeSpan+".00";
			return formattedTimeSpan;
		}
		/*if ( key.equalsIgnoreCase("cmi.student_data.time_limit_action") ) {
			return String.valueOf(scoStats.getSco().getTimeLimitAction());
		}*/
		if ( key.equalsIgnoreCase("cmi.core.total_time") ) {
			// should be in format 00:00:00.0
			long totalTimeSeconds = scoStats.getTotalTimeInSeconds();
			return FormUtil.getInstance().formatTimeVerbose(totalTimeSeconds*1000L);
		}
		if ( key.equalsIgnoreCase("cmi.core._children") ) {
			return "student_id,student_name,lesson_location,credit,lesson_status,entry,score,total_time,exit,session_time";
		}
		if ( key.equalsIgnoreCase("cmi.student_data._children") ) {
			return "mastery_score, max_time_allowed, time_limit_action";
		}
		if ( key.equalsIgnoreCase("cmi.student_preference.audio") ) {
			if ( user.getLearner().getPreference() != null && user.getLearner().getPreference().isAudioEnabled()) {
				return "on";
			}
			return "off";
		}
		if ( key.equalsIgnoreCase("cmi.learner_preference.audio_level") ) {
			//TODO:  not supported
			return "1";
		}
		if ( key.equalsIgnoreCase("cmi.student_preference.audio_captioning") ) {
			if ( user.getLearner().getPreference().isCaptioningEnabled() ) {
				return "1";
			}
			else {
				return "-1";
			}
		}
		if ( key.equalsIgnoreCase("cmi.student_preference.language") ) {
			//TODO:  language setup for SCORM 
			//return scoStats.getLearner().getPreference().getLanguage();
			return "en";
		}
		if ( key.equalsIgnoreCase("cmi.student_preference.speed") ) {
			// TODO: speed not supported
			return "1";
		}
		if ( key.equalsIgnoreCase("cmi.student_preference.text") )  {
			// TODO: not supported
			return null;
		}
		if ( key.equalsIgnoreCase("cmi.comments") ) {
			// TODO: supported but not implemented yet
			return null;
		}
		if ( key.equalsIgnoreCase("cmi.comments_from_lms") ) {
			// TODO: supported but not implemented yet
			return null;
		}
		if ( key.equalsIgnoreCase("cmi.comments_from_learner._children") ) {
			return "comment,location,timestamp";
		}
		if ( key.equalsIgnoreCase("cmi.comments_from_lms._children") ) {
			return "comment,location,timestamp";
		}
		if ( key.equalsIgnoreCase("cmi.comments_from_lms._count") ) {
			// TODO:  supported but not implemented yet
			return "0";
		}
		if (key.equalsIgnoreCase("cmi.comments_from_learner._count") ) {
			// TODO:  supported but not implemented yet
			return "0";
		}
		if ( key.equalsIgnoreCase("cmi._version") ) {
			return "1.0";
		}
		if ( key.equalsIgnoreCase("adl.data._children") ) {
			return "id,store";
		}
		if ( key.equalsIgnoreCase("adl.data._count") ) {
			return String.valueOf(scoStats.getAdlStores().size());
		}
		if ( key.equalsIgnoreCase("cmi.core.score._children") ) {
			return "scaled,raw,min,max";
		}
		if ( key.equalsIgnoreCase("cmi.interactions._children") ) {
			return "id,type,objectives,timestamp,correct_responses,weighting,learner_response,result,latency,description";
		}
		if ( key.equalsIgnoreCase("cmi.objectives._children") ) {
			return "id,score,success_status,completion_status,progress_measure,description";
		}
		if ( key.equalsIgnoreCase("cmi.student_preference._children") ) {
			return "audio_level,language,delivery_speed,audio_captioning";
		}
		
		//  COMPLEX REQUESTS //
		if ( key.startsWith("cmi.comments_from_lms") ) {
			// need to find the interaction we are speaking of
			int index = Integer.valueOf(key.substring(17, 18));
			log.info("processing comments_from_learner."+index);
			String[] keys = key.split("\\.");
			log.info("keys.length:"+keys.length);
			String subKey = keys[keys.length-1];
			log.info("subkey:"+subKey);
			
			LearnerNote note = null;
			
			// server call to get learner notes
			List<LearnerNote> notes = new ArrayList<LearnerNote>();
	
			if ( index > notes.size() ) {
				return null;
			}
			note = notes.get(index);
			
			if ( subKey.equalsIgnoreCase("comment") ) {
				return note.getComment();
			}
			if ( subKey.equalsIgnoreCase("location") ) {
				return note.getLocation();
			}
			if ( subKey.equalsIgnoreCase("timestamp") ) {
				// TODO:  fix formatting
				return note.getTimestamp().toString();
			}
			
		}

		if ( key.startsWith("adl.data") ) {
			// need to find the interaction we are speaking of
			int index = Integer.valueOf(key.substring(17, 18));
			log.info("processing adl store."+index);
			String[] keys = key.split("\\.");
			log.info("keys.length:"+keys.length);
			log.info("keys:"+keys.toString());
			String subKey = keys[keys.length-1];
			ADLStore adlStore = null;
	
			if ( index > scoStats.getAdlStores().size() ) {
				return null;
			}
			adlStore = scoStats.getAdlStores().get(index);
			
			if ( subKey.equalsIgnoreCase("id") ) {
				return adlStore.getAdlStoreId();
			}
			if ( subKey.equalsIgnoreCase("store") ) {
				return adlStore.getStore();
			}			
		}
		if ( key.startsWith("cmi.objectives") ) {
			// need to find the interaction we are speaking of
			int index = Integer.valueOf(key.substring(17, 18));
			log.info("processing objectives."+index);
			String[] keys = key.split("\\.");
			log.info("keys.length:"+keys.length);
			log.info("keys:"+keys.toString());
			String subKey = keys[keys.length-1];
			LearnerSCOObjectiveStatistics scoObjStat = null;
			if ( index > scoStats.getLearnerScoObjectiveStatistics().size() ) {
				return null;
			}
			scoObjStat = scoStats.getLearnerScoObjectiveStatistics().get(index);
			
			if ( subKey.equalsIgnoreCase("score._children") ) {
				return "scaled,raw,min,max";
			}
			if ( subKey.equalsIgnoreCase("score.scaled") ) {
				return String.valueOf(scoObjStat.getScaledScore());
			}
			if ( subKey.equalsIgnoreCase("score.raw") ) {
				return String.valueOf(scoObjStat.getRawScore());
			}
			if ( subKey.equalsIgnoreCase("score.min") ) {
				return String.valueOf(scoObjStat.getMinScore());
			}
			if ( subKey.equalsIgnoreCase("score.max") ) {
				return String.valueOf(scoObjStat.getMaxScore());
			}
			if ( subKey.equalsIgnoreCase("success_status") ) {
				return scoObjStat.getSuccessStatus();
			}
			if ( subKey.equalsIgnoreCase("completion_status") ) {
				return scoObjStat.getCompletionStatus();
			}
			if ( subKey.equalsIgnoreCase("progress_measure") ) {
				return String.valueOf(scoObjStat.getProgressMeasure());
			}
			if ( subKey.equalsIgnoreCase("description") ) {
				return scoObjStat.getScoObjective().getDescription();
			}
		}
		if ( key.startsWith("cmi.interactions.") ) {
			// need to find the interaction we are speaking of
			int index = Integer.valueOf(key.substring(17, 18));
			log.info("processing interactions."+index);
			String[] keys = key.split("\\.");
			log.info("keys.length:"+keys.length);
			String subKey = keys[keys.length-1];
			log.info("subkey:"+subKey);
			SCOInteraction scoInteraction = null;
			
			if ( index > scoStats.getCurrentAssessmentAttempt().getScoInteractions().size() ) {
				return null;
			}
			scoInteraction = scoStats.getCurrentAssessmentAttempt().getScoInteractions().get(index);
			
			if ( subKey.equalsIgnoreCase("id") ) {
				return scoInteraction.getInteractionId();
			}
			if ( subKey.equalsIgnoreCase("weighting") ) {
				return String.valueOf(scoInteraction.getWeighting());
			}
			if ( subKey.equalsIgnoreCase("type") ) {
				return scoInteraction.getType();
			}
			if ( subKey.equalsIgnoreCase("latency") ) {
				scoInteraction.getLatency();
			}
			if ( subKey.equalsIgnoreCase("student_response") ) {
				scoInteraction.getLearnerResponse();
			}
			if ( subKey.equalsIgnoreCase("time") || subKey.equalsIgnoreCase("timestamp") ) {
				scoInteraction.getTime();
			}
			if ( subKey.equalsIgnoreCase("result") ) {
				scoInteraction.getResult();
			}
			if ( subKey.equalsIgnoreCase("pattern") ) {
				// TODO:  need to reparse the key as it is of the form:
				// cmi.interactions.0.correct_responses.0.pattern
			}
			if ( subKey.equalsIgnoreCase("objectives._count") ) {
				return String.valueOf(scoInteraction.getInteractionObjectives().size());
			}
			if ( subKey.equalsIgnoreCase("correct_responses._count") ) {
				return String.valueOf(scoInteraction.getInteractionResponses().size());
			}
		}
		
		// all else failed so check to see if it might be a brand element:
		String tmp = brander.getBrandElement(key);
		if ( tmp != null && !tmp.isEmpty() ) {
			// it magically matched, return the brand element instead
			return tmp;
		}
		
		log.info("------------------------------------------------------------------------");
		log.info("content asked for an unsupported key:"+ key);
		log.info("------------------------------------------------------------------------");
		
		return null;
	}
	
	private LearnerSCOStatistics SetSCOAssessmentInDB(LearnerSCOStatistics scoStats, String key)
	{		
		try {
			//BEGIN ENGSUP-28828
			//ENGSUP-28828: Commit assessment details in DB once SET
			//ENGSUP-29507: Condition added
			//((scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_COMPLETED) ||
			//	scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_PASSED)) 
			// && scoStats.getCurrentAssessmentAttempt().getRawScore() >=0)
			
			if ( 
				(
				scoStats.isPostAssessmentTaken() && scoStats.getCurrentAssessmentAttempt() != null &&
				scoStats.getCurrentAssessmentAttempt().getRawScore() >=0 && 	
				(
				(scoStats.getCurrentAssessmentAttempt().getMaxScore() >=0 && scoStats.getCurrentAssessmentAttempt().getMinScore() >=0) ||
				(scoStats.getCurrentAssessmentAttempt().getScaledScore() >=0) /*||
				(
						(   	scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_COMPLETED) ||
								scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_PASSED) ||
							    scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_FAILED)
						) && (key.equalsIgnoreCase("cmi.core.lesson_status") || key.equalsIgnoreCase("cmi.completion_status"))
				)*/
				)
				
				) ||
				(
					(
						scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_COMPLETED) ||
						scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_PASSED) ||
					    scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_FAILED)
					) && (key.equalsIgnoreCase("cmi.core.lesson_status") || key.equalsIgnoreCase("cmi.completion_status"))
				)
				
				) {
				
				LearnerEnrollment le = entitlementService.getLearnerEnrollmentById(scoStats.getLearnerEnrollmentId());
				LearnerCourseStatistics courseStats = statsService.getLearnerCourseStatisticsForLearnerEnrollment(le);
				//courseStats = statsService.loadForUpdateLearnerCourseStatistics(courseStats.getId());
				LearnerSCOStatistics scoStatsToUpdate = statsService.loadLearnerSCOStatisticsById(scoStats.getId());
				
				
				
				if(scoStats.isPostAssessmentTaken() && scoStats.getCurrentAssessmentAttempt() != null)
				{
					
					log.info("-------- START PROCESSING ASSESSMENT SCORE WITHIN SET CLAUSE -----------");
					
					double minScore = scoStats.getCurrentAssessmentAttempt().getMinScore();
					double maxScore = scoStats.getCurrentAssessmentAttempt().getMaxScore();
					double rawScore = scoStats.getCurrentAssessmentAttempt().getRawScore();
					double percentScore = 0.0;
					
					if ( scoStats.getCurrentAssessmentAttempt().getScaledScore() > 0 ) {
						percentScore = scoStats.getCurrentAssessmentAttempt().getScaledScore() * 100;
					}
					else if ( minScore >= 0 && maxScore >= 1 ) {
						// if the content sent both a min and a max range for the raw score then use it to calculate 
						// the scaled score.  Many LMS providers assume that the min is always 0 and the max is always 100.
						// don't assume that the minScore is 0 it could be something wacky like 10 to 20.
						percentScore = (rawScore / (maxScore - minScore)) * 100;
					}
					else {
						// just use the raw score as the percent score
						percentScore = rawScore;
					}
					
					

					
					if ( courseStats.getNumberPostTestsTaken() < 1 ) {
						courseStats.setHighestPostTestScore(percentScore);
						courseStats.setAveragePostTestScore(percentScore);
						courseStats.setLowestPostTestScore(percentScore);
						courseStats.setNumberPostTestsTaken(1);
						courseStats.setLastPostTestDate(new Date());
						courseStats.setFirstPostTestDate(new Date());
					}
					else {
						if ( courseStats != null ) {
							if ( courseStats.getHighestPostTestScore() < percentScore ) {
								courseStats.setHighestPostTestScore(percentScore);
							}
							if ( courseStats.getLowestPostTestScore() > percentScore ) {
								courseStats.setLowestPostTestScore(percentScore);
							}
			
							// update Average
							double currAverage = courseStats.getAveragePostTestScore();
							int currNumPostTests = courseStats.getNumberPostTestsTaken();
							double totalSumScore = currAverage * currNumPostTests;
							double currTotalSumScore = totalSumScore + percentScore;
							++currNumPostTests;
							double newAverageScore = currTotalSumScore/currNumPostTests;
							courseStats.setAveragePostTestScore(newAverageScore);
							courseStats.setNumberPostTestsTaken(currNumPostTests);
							
							// set last test date
							courseStats.setLastPostTestDate(new Date());
						}
					}
					
					// move the current attempt to the permanent archive
					
					LearnerSCOAssessment scoAssessment = scoStats.getCurrentAssessmentAttempt();
					LearnerSCOAssessment newScoAssessment = new LearnerSCOAssessment();
					newScoAssessment.setAttemptDate(scoAssessment.getAttemptDate());
					newScoAssessment.setMasteryAcheived(scoAssessment.isMasteryAcheived());
					newScoAssessment.setMaxScore(scoAssessment.getMaxScore());
					newScoAssessment.setMinScore(scoAssessment.getMinScore());
					newScoAssessment.setRawScore(scoAssessment.getRawScore());
					newScoAssessment.setScaledScore(scoAssessment.getScaledScore());
					newScoAssessment.setLearnerScoStatistic(scoStatsToUpdate);	
					
					if(scoAssessment!=null && scoAssessment.getScoInteractions()!=null && scoAssessment.getScoInteractions().size() > 0)
					{
						for (int i=0; i<scoAssessment.getScoInteractions().size(); i++)
						{
							scoAssessment.getScoInteractions().get(i).setLearnerSCOAssessment(newScoAssessment);						
						}
					}
					newScoAssessment.setScoInteractions(scoAssessment.getScoInteractions());	
					scoStatsToUpdate.addLearnerSCOAssessment(newScoAssessment);
					
					
					scoStatsToUpdate.setCurrentAssessmentAttempt(null);
					scoStatsToUpdate.setPostAssessmentTaken(false);					
					
					
					
					if ( scoStats.isPostAssessmentTaken() && scoStats.getCurrentAssessmentAttempt() != null ) {
						String debugInfoVal = "; SRawScore: "+ scoStats.getCurrentAssessmentAttempt().getRawScore()+"; SattemptDate: "+newScoAssessment.getAttemptDate().toString();
									
						courseStats.setDebugInfo(courseStats.getDebugInfo()+debugInfoVal);
						}
					
				}
				
				
		///////////scoStatsToUpdate.setTotalTimeInSeconds(Long.valueOf(scoStatsToUpdate.getTotalTimeInSeconds() + scoStats.getSessionTimeInSeconds()).intValue());
				// reset the session time metric
		///////////log.info("******* SETTING SESSION TIME TO ZERO ***********");
		///////////scoStatsToUpdate.setSessionTimeInSeconds(0);
				
				
				scoStatsToUpdate.setSupsendData(scoStats.getSupsendData());
				scoStatsToUpdate.setScoLocation(scoStats.getScoLocation());
				scoStatsToUpdate.setCompletionStatus(scoStats.getCompletionStatus());
				scoStatsToUpdate.setLastUpdatedDate(new Date());
				
				// update total time in course stats
				///////////courseStats.addTimeSpentInSeconds(Long.valueOf(scoStats.getSessionTimeInSeconds()).intValue());
				// update percent completed
				scoStatsToUpdate.setProgressMeasure(scoStats.getProgressMeasure());
				
				statsService.saveLearnerSCOStatistics(scoStatsToUpdate);
				
				
				boolean sendEmail=false;
				
				if (!courseStats.isCourseCompleted() ) {
					if ( scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_COMPLETED) ||
						 scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_PASSED)) {
						courseStats.setStatus(LearnerCourseStatistics.COMPLETED);
						courseStats.setCompleted(true);
						if(!le.getMarketoCompletion()){
  						    log.debug("Scorm Cuorse Marketo Status before Update" + le.getMarketoCompletion() );
  							enrollmentService.marketoPacket(le, COURSE_COMPLETED);
 							LearnerEnrollment updateLearnerEnrollment = enrollmentService.loadForUpdateLearnerEnrollment(le.getId());
 							updateLearnerEnrollment.setMarketoCompletion(Boolean.TRUE);
 							enrollmentService.updateEnrollment(updateLearnerEnrollment);
  						}
						courseStats.setCompletionDate(new Date());
						courseStats.setPercentComplete(100);
						sendEmail=true;
					}
					else {
						courseStats.setStatus(LearnerCourseStatistics.IN_PROGRESS);
					}
				}
				
				// progress measure is always a value between 0..1
				if ( courseStats.getPercentComplete() < (scoStats.getProgressMeasure()*100) ) {
					courseStats.setPercentComplete(scoStats.getProgressMeasure()*100);
				}
				
				// update our master LMS learner course statistics table for the LMS UI and reporting				
				statsService.saveLearnerCourseStatistics(courseStats);
				
				scoStats.setCurrentAssessmentAttempt(null);
				scoStats.setPostAssessmentTaken(false);	
				
				
				if(sendEmail)
					learnersToBeMailedService.emailCourseCompletionCertificate(scoStats.getLearnerEnrollmentId());
				
				log.info("--------END PROCESSING ASSESSMENT SCORE WITHIN SET CLAUSE -----------");
			}
			
			//END ENGSUP-28828
			
		}catch(Throwable th) {
			log.error("error during scorm SET Value:"+th.getMessage(), th);			
		}
		
		return scoStats;
		
	}
	
	
	private LearnerSCOStatistics setValue(com.softech.vu360.lms.vo.VU360User user, LearnerSCOStatistics scoStats, String key, String value) {
		boolean isError = true;
		// in testing we found that the LS 360 player calls setValue methods in rapid succession
		// and through the asynchronous nature of the LMS API implementation, it is normal to 
		// have multiple updates to the scoStats object occurring at the same time
		synchronized ( user ) {
			
			try {
								
				if ( key.equalsIgnoreCase("cmi.progress_measure") ) {
					// do not respect going backwards in course progress
					try {
						double d = Double.valueOf(value);
						if ( d > scoStats.getProgressMeasure() ) {
							scoStats.setProgressMeasure(d);
						}
					}
					catch (NumberFormatException nfe) {
						log.info("could not parse "+value+" into a double from content.", nfe);
					}
					isError = false;
				}
				if ( key.equalsIgnoreCase("cmi.session_time") ) {
				    // format of incoming time:  P[yY][mM][dD][T[hH][mM][s[.s]S]]
					int timeIndex = value.indexOf('T');
					if ( timeIndex >= 0 ) {
						int startIndex = 1;
						String hours = null;
						String minutes = null;
						String seconds = null;
						// parse time
						String timeStr = value.substring(timeIndex, value.length());
						// the time portion is broken into: T[hH][mM][s[.s]S]
						int hoursIndex = timeStr.indexOf('H');
						if ( hoursIndex >= 0 ) {
							// parse out hours
							hours = timeStr.substring(startIndex, hoursIndex);
							startIndex = hoursIndex+1;
						}
						
						int minutesIndex = timeStr.indexOf('M');
						if ( minutesIndex >= 0 ) {
							minutes = timeStr.substring(startIndex, minutesIndex);
							startIndex = minutesIndex + 1;
						}
						
						int secondsIndex = timeStr.indexOf('S');
						if ( secondsIndex >= 0 ) {
							seconds = timeStr.substring(startIndex, secondsIndex);
						}
						
						// construct the time in seconds
						long timeInSeconds = 0;
						if ( seconds != null ) {
							try {
								timeInSeconds += Double.parseDouble(seconds);
							}
							catch (NumberFormatException nfe) {
								log.info("invalid time from content for seconds:"+ seconds);
							}
						}
						
						if ( minutes != null ) {
							try {
								timeInSeconds = timeInSeconds + (Long.parseLong(minutes)*60);
							}
							catch (NumberFormatException nfe) {
								log.info("invalid time from content for minutes:"+ minutes);
							}						
						}
						
						if ( hours != null ) {
							try {
								timeInSeconds = timeInSeconds + (Long.parseLong(hours)*60*60);
							}
							catch (NumberFormatException nfe) {
								log.info("invalid time from content for hours:"+ hours);
							}						
						}
						log.info("time_spent_seconds->"+timeInSeconds);
						scoStats.setSessionTimeInSeconds(timeInSeconds);
					}
					isError = false;
				}
				
				if ( key.equalsIgnoreCase("cmi.suspend_data") ) {
					log.debug("SETTING CMI.SUSPEND.DATA:"+value);
					scoStats.setSupsendData(value);
					isError = false;
				}
				if ( key.equalsIgnoreCase("cmi.core.lesson_location") ||  key.equalsIgnoreCase("cmi.location")) {
					scoStats.setScoLocation(value);
					isError = false;
				}
				
				
				if ( key.equalsIgnoreCase("cmi.core.session_time") ) {
					// parse format 0000:08:37.36
					String[] parts = value.split(":");
					double hours = Double.parseDouble(parts[0]);
					double minutes = Double.parseDouble(parts[1]);
					double seconds = Double.parseDouble(parts[2]);
					scoStats.setSessionTimeInSeconds(Double.valueOf((hours*60*60)+(minutes*60)+seconds).longValue());
					isError = false;
				}
				if ( key.equalsIgnoreCase("cmi.core.lesson_status") || key.equalsIgnoreCase("cmi.completion_status") ) {
					// check to ensure that we are not marking something as incomplete if it is already completed
					//if ( !(scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_COMPLETED) ||
					//	   scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_PASSED)) ) {
						scoStats.setCompletionStatus(value);
					//}
					isError = false;
				}
				if ( key.equalsIgnoreCase("cmi.core.score.raw") || key.equalsIgnoreCase("cmi.score.raw") ) {
					if ( scoStats.getCurrentAssessmentAttempt() == null ) {
						scoStats.createNewLearnerSCOAssessmentAttempt();
					}
					scoStats.getCurrentAssessmentAttempt().setRawScore(Double.valueOf(value));
					scoStats.setPostAssessmentTaken(true);
					isError = false;
					
					//BEGIN ENGSUP-28828
					LearnerEnrollment le = entitlementService.getLearnerEnrollmentById(scoStats.getLearnerEnrollmentId());
					LearnerCourseStatistics courseStats = statsService.getLearnerCourseStatisticsForLearnerEnrollment(le);
					courseStats = statsService.loadForUpdateLearnerCourseStatistics(courseStats.getId());
					
					if ( scoStats.isPostAssessmentTaken() && scoStats.getCurrentAssessmentAttempt() != null ) {
						String debugInfoVal = "; RRawScore: "+ scoStats.getCurrentAssessmentAttempt().getRawScore()+"; RattemptDate: "+(new Date()).toString();
									
						courseStats.setDebugInfo(courseStats.getDebugInfo()+debugInfoVal);
						}
					
					statsService.saveLearnerCourseStatistics(courseStats);
					//END ENGSUP-28828
				}
				if ( key.equalsIgnoreCase("cmi.score.scaled") ) {
					if ( scoStats.getCurrentAssessmentAttempt() == null ) {
						scoStats.createNewLearnerSCOAssessmentAttempt();
					}
					scoStats.getCurrentAssessmentAttempt().setScaledScore(Double.valueOf(value));
					scoStats.setPostAssessmentTaken(true);
					isError = false;
				}
		
				if ( key.equalsIgnoreCase("cmi.core.exit") || key.equalsIgnoreCase("cmi.exit")) {
					scoStats.setExit(value);
					isError = false;
				}
				if ( key.equalsIgnoreCase("cmi.core.score.max") || key.equalsIgnoreCase("cmi.score.max") ) {
					if ( scoStats.getCurrentAssessmentAttempt() == null ) {
						scoStats.createNewLearnerSCOAssessmentAttempt();
					}
					scoStats.getCurrentAssessmentAttempt().setMaxScore(Double.valueOf(value));
					scoStats.setPostAssessmentTaken(true);
					isError = false;
				}
				if ( key.equalsIgnoreCase("cmi.core.score.min") || key.equalsIgnoreCase("cmi.score.min")) {
					if ( scoStats.getCurrentAssessmentAttempt() == null ) {
						scoStats.createNewLearnerSCOAssessmentAttempt();
					}
					scoStats.getCurrentAssessmentAttempt().setMinScore(Double.valueOf(value));
					scoStats.setPostAssessmentTaken(true);
					isError = false;
				}
				if ( key.startsWith("cmi.interactions.") ) {
					// need to find the interaction we are speaking of
					int index = Integer.valueOf(key.substring(17, 18));
					log.info("processing interactions."+index);
					String[] keys = key.split("\\.");
					log.info("keys.length:"+keys.length);
					String subKey = keys[keys.length-1];
					log.info("subKey:"+subKey);
					log.info("value:"+value);
					SCOInteraction scoInteraction = null;
					if ( scoStats.getCurrentAssessmentAttempt() == null ) {
						scoStats.createNewLearnerSCOAssessmentAttempt();
					}
					if ( index < scoStats.getCurrentAssessmentAttempt().getScoInteractions().size() ) {
						log.info("existing interaction");
						scoInteraction = scoStats.getCurrentAssessmentAttempt().getScoInteractions().get(index);
					}
					else {
						log.info("new interaction");
						scoInteraction = new SCOInteraction();
						scoStats.getCurrentAssessmentAttempt().addScoInteraction(scoInteraction);
						scoInteraction.setLearnerSCOAssessment(scoStats.getCurrentAssessmentAttempt());
					}
					
					if ( subKey.equalsIgnoreCase("id") ) {
						scoInteraction.setInteractionId(value);
					}
					if ( subKey.equalsIgnoreCase("weighting") ) {
						scoInteraction.setWeighting(Double.valueOf(value));
					}
					if ( subKey.equalsIgnoreCase("type") ) {
						scoInteraction.setType(value);
					}
					if ( subKey.equalsIgnoreCase("latency") ) {
						scoInteraction.setLatency(value);
					}
					if ( subKey.equalsIgnoreCase("student_response") ) {
						scoInteraction.setLearnerResponse(value);
					}
					if ( subKey.equalsIgnoreCase("time") ) {
						scoInteraction.setTime(value);
					}
					if ( subKey.equalsIgnoreCase("result") ) {
						scoInteraction.setResult(value);
					}
					if ( subKey.equalsIgnoreCase("pattern") ) {
						// TODO:  need to reparse the key as it is of the form:
						// cmi.interactions.0.correct_responses.0.pattern
					}
					isError = false;
				}
				
				//BEGIN ENGSUP-28828				
					scoStats = SetSCOAssessmentInDB(scoStats,key);
				//END ENGSUP-28828
				
			}
			catch(Throwable th) {
				log.error("error during scorm SET Value:"+th.getMessage(), th);
				isError = true;
			}
			
			if ( isError ) {
				log.error("------------------------------------------------------------------------");
				log.error("content set an unsupported key:"+ key);
				log.error("------------------------------------------------------------------------");
			}
		}
		return scoStats;
	}
	
	private boolean terminate(com.softech.vu360.lms.vo.VU360User user, LearnerSCOStatistics scoStats, LearnerCourseStatistics incomingCourseStats, String learningSessionId) {
		boolean isError = false;
		synchronized ( user ) {	
			try {
				
				LearnerCourseStatistics courseStats = statsService.loadForUpdateLearnerCourseStatistics(incomingCourseStats.getId());
				
				// aggregate the stats for this SCO and post to the LearnerCourseStatistics Object in LMS
				log.info("------------------------------------------------------------------------");
				log.info("terminate called");
				log.info("------------------------------------------------------------------------");
				log.info("suspend data:"+scoStats.getSupsendData());
				log.info("sessionTime:"+scoStats.getSessionTimeInSeconds());
				log.info("completionStatus/lessonStatus:"+scoStats.getCompletionStatus());
				if ( scoStats.getCurrentAssessmentAttempt() != null ) {
					log.info("assessment attempted:"+scoStats.isPostAssessmentTaken());
					log.info("raw score:"+scoStats.getCurrentAssessmentAttempt().getRawScore());
					log.info("scaled score:"+scoStats.getCurrentAssessmentAttempt().getScaledScore());
				}
				else {
					log.info("no assessment attempted");
				}
				log.info("progress measure:"+scoStats.getProgressMeasure());
				boolean sendEmail=false;
				
				if ( /*!courseStats.isCompleted()*/!courseStats.isCourseCompleted() ) {
					if ( scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_COMPLETED) ||
						 scoStats.getCompletionStatus().equalsIgnoreCase(LearnerSCOStatistics.COMPLETION_STATUS_PASSED)) {
						courseStats.setStatus(LearnerCourseStatistics.COMPLETED);
						courseStats.setCompleted(true);
						courseStats.setCompletionDate(new Date());
						courseStats.setPercentComplete(100);
						sendEmail=true;
					}
					else {
						courseStats.setStatus(LearnerCourseStatistics.IN_PROGRESS);
					}
				}
	
				// progress measure is always a value between 0..1
				if ( courseStats.getPercentComplete() < (scoStats.getProgressMeasure()*100) ) {
					courseStats.setPercentComplete(scoStats.getProgressMeasure()*100);
				}			
				
				
				if ( scoStats.isPostAssessmentTaken() && scoStats.getCurrentAssessmentAttempt() != null ) {
					log.info("--------TERMINATE: PROCESSING ASSESSMENT SCORE---------------");
					double minScore = scoStats.getCurrentAssessmentAttempt().getMinScore();
					double maxScore = scoStats.getCurrentAssessmentAttempt().getMaxScore();
					double rawScore = scoStats.getCurrentAssessmentAttempt().getRawScore();
					double percentScore = 0.0;
					// if we have a scaled score (SCORM 2004 only) then just use it as it provides everything
					// we need.
					if ( scoStats.getCurrentAssessmentAttempt().getScaledScore() > 0 ) {
						percentScore = scoStats.getCurrentAssessmentAttempt().getScaledScore() * 100;
					}
					else if ( minScore >= 0 && maxScore >= 1 ) {
						// if the content sent both a min and a max range for the raw score then use it to calculate 
						// the scaled score.  Many LMS providers assume that the min is always 0 and the max is always 100.
						// don't assume that the minScore is 0 it could be something wacky like 10 to 20.
						percentScore = (rawScore / (maxScore - minScore)) * 100;
					}
					else {
						// just use the raw score as the percent score
						percentScore = rawScore;
					}
					log.debug("percentScore:"+percentScore);
					if ( courseStats.getNumberPostTestsTaken() < 1 ) {
						courseStats.setHighestPostTestScore(percentScore);
						courseStats.setAveragePostTestScore(percentScore);
						courseStats.setLowestPostTestScore(percentScore);
						courseStats.setNumberPostTestsTaken(1);
						courseStats.setLastPostTestDate(new Date());
						courseStats.setFirstPostTestDate(new Date());
					}
					else {
						if ( courseStats != null ) {
							if ( courseStats.getHighestPostTestScore() < percentScore ) {
								courseStats.setHighestPostTestScore(percentScore);
							}
							if ( courseStats.getLowestPostTestScore() > percentScore ) {
								courseStats.setLowestPostTestScore(percentScore);
							}
			
							// update Average
							double currAverage = courseStats.getAveragePostTestScore();
							int currNumPostTests = courseStats.getNumberPostTestsTaken();
							double totalSumScore = currAverage * currNumPostTests;
							double currTotalSumScore = totalSumScore + percentScore;
							++currNumPostTests;
							double newAverageScore = currTotalSumScore/currNumPostTests;
							courseStats.setAveragePostTestScore(newAverageScore);
							courseStats.setNumberPostTestsTaken(currNumPostTests);
							
							// set last test date
							courseStats.setLastPostTestDate(new Date());
						}
					}
					
					// move the current attempt to the permanent archive
					LearnerSCOStatistics scoStatsToUpdate = statsService.loadLearnerSCOStatisticsById(scoStats.getId());
					LearnerSCOAssessment scoAssessment = scoStats.getCurrentAssessmentAttempt();
					LearnerSCOAssessment newScoAssessment = new LearnerSCOAssessment();
					newScoAssessment.setAttemptDate(scoAssessment.getAttemptDate());
					newScoAssessment.setMasteryAcheived(scoAssessment.isMasteryAcheived());
					newScoAssessment.setMaxScore(scoAssessment.getMaxScore());
					newScoAssessment.setMinScore(scoAssessment.getMinScore());
					newScoAssessment.setRawScore(scoAssessment.getRawScore());
					newScoAssessment.setScaledScore(scoAssessment.getScaledScore());
					newScoAssessment.setLearnerScoStatistic(scoStatsToUpdate);	
					
					if(scoAssessment!=null && scoAssessment.getScoInteractions()!=null && scoAssessment.getScoInteractions().size() > 0)
					{
						for (int i=0; i<scoAssessment.getScoInteractions().size(); i++)
						{
							scoAssessment.getScoInteractions().get(i).setLearnerSCOAssessment(newScoAssessment);						
						}
					}
					newScoAssessment.setScoInteractions(scoAssessment.getScoInteractions());	
					scoStatsToUpdate.addLearnerSCOAssessment(newScoAssessment);
					scoStatsToUpdate.setCurrentAssessmentAttempt(null);
					scoStatsToUpdate.setPostAssessmentTaken(false);					
					statsService.saveLearnerSCOStatistics(scoStatsToUpdate);
					log.info("--------TERMINATE: END PROCESSING ASSESSMENT SCORE-----------");
				}
				
				
				LearnerSCOStatistics scoStatsToUpdate = statsService.loadLearnerSCOStatisticsById(scoStats.getId());
				scoStatsToUpdate.setTotalTimeInSeconds(Long.valueOf(scoStatsToUpdate.getTotalTimeInSeconds() + scoStats.getSessionTimeInSeconds()).intValue());
				// reset the session time metric
				log.info("******* SETTING SESSION TIME TO ZERO ***********");
				scoStatsToUpdate.setSessionTimeInSeconds(0);
				
				
				scoStatsToUpdate.setSupsendData(scoStats.getSupsendData());
				scoStatsToUpdate.setScoLocation(scoStats.getScoLocation());
				scoStatsToUpdate.setCompletionStatus(scoStats.getCompletionStatus());
				scoStatsToUpdate.setLastUpdatedDate(new Date());
				
				// update total time in course stats
				courseStats.addTimeSpentInSeconds(Long.valueOf(scoStats.getSessionTimeInSeconds()).intValue());
				// update percent completed
				scoStatsToUpdate.setProgressMeasure(scoStats.getProgressMeasure());
				// ensure cleanup
				scoStatsToUpdate.setCurrentAssessmentAttempt(null);
				scoStatsToUpdate.setPostAssessmentTaken(false);
	
				statsService.saveLearnerSCOStatistics(scoStatsToUpdate);
				
				if ( scoStats.isPostAssessmentTaken() && scoStats.getCurrentAssessmentAttempt() != null ) {
					String debugInfoVal = "; TRawScore: "+ scoStats.getCurrentAssessmentAttempt().getRawScore()+"; TattemptDate: "+scoStats.getCurrentAssessmentAttempt().getAttemptDate().toString();
								
					courseStats.setDebugInfo(courseStats.getDebugInfo()+debugInfoVal);
					}
				
				// update our master LMS learner course statistics table for the LMS UI and reporting
				statsService.saveLearnerCourseStatistics(courseStats);
				/* adding code to send data to AICC clients*/							
				
				//LearningSession ls = statisticsDAO.getLearningSessionByLearningSessionId(learningSessionId);
				LearningSession ls = learningSessionRepository.findTop1ByLearningSessionID(learningSessionId);
				ls.setSessionEndDateTime(new Date(System.currentTimeMillis()));						
				ls = learningSessionRepository.save(ls);
				//email logic added for scorm courses			
				if(sendEmail)
					learnersToBeMailedService.emailCourseCompletionCertificate(scoStats.getLearnerEnrollmentId());
				if ( ls.getSource() != null ) {
					String source = ls.getSource().trim();
					if ( source.equalsIgnoreCase(LearningSession.SOURCE_AICC) ) {			
						aiccHandler.handleLearingSessionCompleteEvent(ls,"SCORM_terminate");						
					}					
				}				
				/* adding code to send data to AICC clients*/
			}
			catch(Exception ex) {
				log.error("error during SCORM terminate:"+ex.getMessage(), ex);
				isError = true;
			}
		}
		
		return isError;
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
	
	public ExternalStatisticsProcessor getAiccHandler() {
		return aiccHandler;
	}

	/**
	 * @param aiccHandler the aiccHandler to set
	 */
	public void setAiccHandler(ExternalStatisticsProcessor aiccHandler) {
		this.aiccHandler = aiccHandler;
	}
	
/*	public void setStatisticsDAO(StatisticsDAO statisticsDAO) {
		this.statisticsDAO = statisticsDAO;
	}*/
	public LearningSession getLearningSessionByLearningSessionId(String learningSessionId){
		//return statisticsDAO.getLearningSessionByLearningSessionId(learningSessionId);
		return learningSessionRepository.findTop1ByLearningSessionID(learningSessionId);
	}
 	
	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}
	
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}
		 
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		 this.enrollmentService = enrollmentService;
		 	}
}