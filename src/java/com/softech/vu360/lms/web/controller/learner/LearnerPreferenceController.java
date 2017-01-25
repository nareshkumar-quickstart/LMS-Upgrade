package com.softech.vu360.lms.web.controller.learner;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.LearnerPreferences;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * The controller for the learner profile display and modify .
 *
 * @author Bishwajit Maitra
 * @date 16-12-08
 *
 */
public class LearnerPreferenceController extends MultiActionController {

	private String learnerPreferencesTemplate = null;
	private LearnerService learnerService;
	public VU360UserService vu360UserService;
		
	private static final String AVATAR_URL = VU360Properties.getVU360Property("avatar.URL");
	private static final String AVATAR_UPLOAD_DIRECTORY = VU360Properties.getVU360Property("document.avatar.upload.location");
	private static final String AVATAR_DAFAULT_DIRECTORY = VU360Properties.getVU360Property("document.avatar.default.location");

	public ModelAndView displayLearnerPreferences(HttpServletRequest request, HttpServletResponse response) {
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360User vu360User = vu360UserService.loadForUpdateVU360User(user.getId());
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("vu360User", vu360User);
		
		// [6/8/2010] For Avatar VCS Integration		
		context.put("avatar", this.getLearnerAvatar(request, vu360User.getLearner().getPreference()));

		return new ModelAndView(learnerPreferencesTemplate, "context", context);
	}

	/**
	 * @author Bishwajit Maitra
	 * @date 24-02-09
	 * this method save learner Preference to database
	 * @param request
	 * 
	 */	
	public ModelAndView updatePreferences(HttpServletRequest request, HttpServletResponse response){
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360User vu360User = vu360UserService.loadForUpdateVU360User(user.getId());
		LearnerPreferences currLearnerPreferences = vu360User.getLearner().getPreference();
		if ( currLearnerPreferences == null ) {
			currLearnerPreferences = new LearnerPreferences();
			currLearnerPreferences.setLearner(vu360User.getLearner());
			vu360User.getLearner().setPreference(currLearnerPreferences);
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
		}else {
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
		currLearnerPreferences.setLearner(vu360User.getLearner());
		vu360User.getLearner().setPreference(currLearnerPreferences);
		currLearnerPreferences = learnerService.saveLearnerPreferences(currLearnerPreferences);
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("vu360User", vu360User);
		
		// [6/8/2010] For Avatar VCS Integration		
		context.put("avatar", this.getLearnerAvatar(request, vu360User.getLearner().getPreference()));
		
		return new ModelAndView(learnerPreferencesTemplate, "context", context);
	}	
	
	/**
	 * [6/8/2010] Get Avatar information
	 * @param learnerPreferences
	 * @return
	 */
	protected String getLearnerAvatar (HttpServletRequest request, LearnerPreferences learnerPreferences) {
			
		// Return User Avatar, if exists
		if ( learnerPreferences != null && learnerPreferences.getAvatar() != null ) {			
			if ( StringUtils.isNotBlank( learnerPreferences.getAvatar() ) ) {
				return AVATAR_URL + AVATAR_UPLOAD_DIRECTORY + learnerPreferences.getAvatar();				
			}			
		}
		
		// Return default Avatar, otherwise
		Brander brander = VU360Branding.getInstance().getBrander(
				(String) request.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());
		String[] avatarList = brander.getBrandElements("lms.learner.default.avatars");
		return AVATAR_URL + AVATAR_DAFAULT_DIRECTORY + avatarList[0];
	}

	/**
	 * @return the learnerPreferencesTemplate
	 */
	public String getLearnerPreferencesTemplate() {
		return learnerPreferencesTemplate;
	}

	/**
	 * @param learnerPreferencesTemplate the learnerPreferencesTemplate to set
	 */
	public void setLearnerPreferencesTemplate(String learnerPreferencesTemplate) {
		this.learnerPreferencesTemplate = learnerPreferencesTemplate;
	}

	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}

	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	/**
	 * @return the vu360UserService
	 */
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
 
}