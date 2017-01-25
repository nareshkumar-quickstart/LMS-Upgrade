package com.softech.vu360.lms.web.controller.administrator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerPreferences;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;

public class EditLearnerPreferencesController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(EditLearnerPreferencesController.class.getName());
	private static final String SAVE_LEARNER_PREFERENCES = "saveLearnerPreferences";
	private static final String CANCEL = "cancel";

	private String learnerPreferencesTemplate = null;
	private String redirectTemplate = null;

	private LearnerService learnerService;
	public VU360UserService vu360UserService;

	public ModelAndView displayLearnerPreferences(HttpServletRequest request, HttpServletResponse response) {
		try {
			String userId = request.getParameter("learnerId");
			Map<Object, Object> context = new HashMap<Object, Object>();
			VU360User vu360User = null;
			context.put("id", userId);
			if( StringUtils.isNotBlank(userId)) {
				vu360User = vu360UserService.loadForUpdateVU360User(Long.valueOf(userId));
			} else {
				vu360User = VU360UserAuthenticationDetails.getCurrentUser();
			}
			if ( vu360User == null ) {
				return new ModelAndView(redirectTemplate, "context", context);
			}
			context.put("vu360User", vu360User);
			String actionType = request.getParameter("action");
			if( StringUtils.isNotBlank(actionType) ) {
				if( actionType.equalsIgnoreCase(SAVE_LEARNER_PREFERENCES) ) {
					this.learnerPreferencesSave(request, vu360User);
				} else if( actionType.equalsIgnoreCase(CANCEL) ) {
					return new ModelAndView(redirectTemplate, "context", context);
				}
				return new ModelAndView(redirectTemplate, "context", context);
			}
			return new ModelAndView(learnerPreferencesTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(learnerPreferencesTemplate);
	}

	public void learnerPreferencesSave(HttpServletRequest request, VU360User vu360User) {

		Learner learner = vu360User.getLearner();

		try {
			LearnerPreferences currLearnerPreferences = null;
			if ( learner.getPreference() !=  null ) {
				currLearnerPreferences = learner.getPreference();
				currLearnerPreferences.setId(learner.getPreference().getId());
			} else {
				currLearnerPreferences = new LearnerPreferences();
			}

			if(request.getParameter("audio").equalsIgnoreCase("true")) {
				currLearnerPreferences.setAudioEnabled(true);
			} else {
				currLearnerPreferences.setAudioEnabled(false);
			}
			if(request.getParameter("volume").isEmpty()) {
				currLearnerPreferences.setVolume(0);
			} else {
				currLearnerPreferences.setVolume(Integer.parseInt(request.getParameter("volume")));
			}
			if(request.getParameter("captioning").equalsIgnoreCase("true")) {
				currLearnerPreferences.setCaptioningEnabled(true);
			} else {
				currLearnerPreferences.setCaptioningEnabled(false);
			}
			if(request.getParameter("bandwidth").equalsIgnoreCase("high")) {
				currLearnerPreferences.setBandwidth("high");
			} else {
				currLearnerPreferences.setBandwidth("low");
			}
			if(request.getParameter("video").equalsIgnoreCase("true")) {
				currLearnerPreferences.setVedioEnabled(true);
			} else {
				currLearnerPreferences.setVedioEnabled(false);
			}
			if(request.getParameter("RegistrationEmial").equalsIgnoreCase("true")) {
				currLearnerPreferences.setInRegistrationEmialEnabled(true);
			} else {
				currLearnerPreferences.setInRegistrationEmialEnabled(false);
			}
			if(request.getParameter("EnrollmentEmail").equalsIgnoreCase("true")) {
				currLearnerPreferences.setEnrollmentEmailEnabled(true);
			} else {
				currLearnerPreferences.setEnrollmentEmailEnabled(false);
			}
			if(request.getParameter("CertificateEmail").equalsIgnoreCase("true")) {
				currLearnerPreferences.setCourseCompletionCertificateEmailEnabled(true);
			} else {
				currLearnerPreferences.setCourseCompletionCertificateEmailEnabled(false);
			}
			currLearnerPreferences.setLearner(learner);
			learner.setPreference(currLearnerPreferences);
			vu360User.setLearner(learner);
			
			currLearnerPreferences = learnerService.saveLearnerPreferences(currLearnerPreferences);

		} catch (Exception e) {
			log.debug("exception ", e);
		}
	}	

	public void afterPropertiesSet() throws Exception {

	}

	public String getLearnerPreferencesTemplate() {
		return learnerPreferencesTemplate;
	}

	public void setLearnerPreferencesTemplate(String learnerPreferencesTemplate) {
		this.learnerPreferencesTemplate = learnerPreferencesTemplate;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

}