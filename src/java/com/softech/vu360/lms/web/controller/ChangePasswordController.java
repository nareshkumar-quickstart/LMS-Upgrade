/**
 * 
 */
package com.softech.vu360.lms.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.model.ChangePasswordForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

/**
 * // [9/24/2010] LMS-7219 :: Learner Mode > Login: Force User to Change Password on Next Login 
 * @author sana.majeed
 *
 */
public class ChangePasswordController extends SimpleFormController {
	
	private static final Logger log = Logger.getLogger(ChangePasswordController.class.getName());
	private VU360UserService vu360UserService = null;
	private LearnerService learnerService = null;
	private VelocityEngine velocityEngine = null;
	
	/**
	 * Validate before Submission
	 */
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		log.debug("In processFormSubmission");
		
		ChangePasswordForm form = (ChangePasswordForm) command;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//VU360User dbUser = VU360UserAuthenticationDetails.getCurrentUser();
		if( StringUtils.isNotBlank(form.getCurrentPassword()) ) {
			if (! this.learnerService.isCorrectPassword(user, form.getCurrentPassword()) ) {
				errors.rejectValue("currentPassword", "lms.learner.changePassword.errors.incorrectCurrentPassword");
			}			
		}
		
		if( StringUtils.isNotBlank(form.getNewPassword()) ) {
			if ( form.getCurrentPassword().equalsIgnoreCase(form.getNewPassword()) ) {
				errors.rejectValue("newPassword", "lms.learner.changePassword.errors.incorrectNewPassword");
			}			
		}
		
		return super.processFormSubmission(request, response, command, errors);
	}
	
	protected ModelAndView onSubmit (HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		log.debug("In onSubmit");
		ChangePasswordForm form = (ChangePasswordForm) command;
		
		VU360User vu360User = VU360UserAuthenticationDetails.getCurrentUser();
		
		// Update values to be saved to database
		vu360User.setPassWordChanged( true );
		vu360User.setPassword( form.getNewPassword() );
		vu360User.setChangePasswordOnLogin( false );
		
		// Setup email notification for password modification
		StringBuilder loginURL = new StringBuilder();
		loginURL.append(request.getScheme());
		loginURL.append("://");
		loginURL.append(request.getServerName());
		if ( request.getServerPort() != 80 ){
			loginURL.append(":");
			loginURL.append(request.getServerPort());
		}
		loginURL.append(request.getContextPath());
		
		Brander brander= VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());	
		
		vu360User = this.learnerService.updateUser(vu360User, true, loginURL.toString(), brander, this.velocityEngine);	
		vu360User.setLmsRoles(vu360User.getLmsRoles());
		com.softech.vu360.lms.vo.VU360User UserVO = ProxyVOHelper.setUserProxy(vu360User);
		// Here we need to update the Principal Object
		AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(UserVO, vu360User.getPassword(), vu360User.getAuthorities());
		authentication.setDetails(SecurityContextHolder.getContext().getAuthentication().getDetails());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		
		return super.onSubmit(request, response, command, errors);
	}
	
	
	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	/**
	 * @return the vu360UserService
	 */
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}
	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}
	/**
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	
	

}
