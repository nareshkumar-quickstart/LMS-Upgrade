/**
 * 
 */
package com.softech.vu360.lms.web.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.lms.web.controller.model.ChangePasswordForm;

/**
 * // [9/24/2010] LMS-7219 :: Learner Mode > Login: Force User to Change Password on Next Login 
 * @author sana.majeed
 *
 */
public class ChangeProctorPasswordController extends SimpleFormController {
	
	private static final Logger log = Logger.getLogger(ChangeProctorPasswordController.class.getName());
	//private VU360UserService vu360UserService = null;
	//private LearnerService learnerService = null;
	private VelocityEngine velocityEngine = null;
	private ProctorService proctorService = null;
	/**
	 * Validate before Submission
	 */
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		log.debug("In processFormSubmission");
		
		ChangePasswordForm form = (ChangePasswordForm) command;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if( StringUtils.isNotBlank(form.getCurrentPassword())  &&   user.getProctor()!=null) {
			if (! user.getProctor().getPassword().equals(form.getCurrentPassword()) ) {
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
		
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Proctor proctor = proctorService.loadForUpdate(user.getProctor().getId());
		
		// Update values to be saved to database
		proctor.setFirstLogin( false );
		proctor.setPassword( form.getNewPassword() );
		proctor.setPasswordResetDateTime(new Date());
		proctor.setProctorStatusTimeStamp(new Date());
		proctor = this.proctorService.updateProctor(proctor);	
		
		return super.onSubmit(request, response, command, errors);
	}
	
	
	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	//public void setVu360UserService(VU360UserService vu360UserService) {
	//	this.vu360UserService = vu360UserService;
	//}
	/**
	 * @return the vu360UserService
	 */
	//public VU360UserService getVu360UserService() {
	//	return vu360UserService;
	//}
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		if(request.getParameter("showThirtyDaysPassedPage")!=null){
			HttpSession session = request.getSession();
			session.setAttribute("showThirtyDaysPassedPage", request.getParameter("showThirtyDaysPassedPage"));
		}
		return super.referenceData(request);
	}

	/**
	 * @param learnerService the learnerService to set
	 */
	//public void setLearnerService(LearnerService learnerService) {
	//	this.learnerService = learnerService;
	//}
	/**
	 * @return the learnerService
	 */
	//public LearnerService getLearnerService() {
	//	return learnerService;
	//}
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

	public ProctorService getProctorService() {
		return proctorService;
	}

	public void setProctorService(ProctorService proctorService) {
		this.proctorService = proctorService;
	}
	
	

}
