/**
 * 
 */
package com.softech.vu360.lms.web.controller.proctor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ProctorProfileForm;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author syed.mahmood
 *
 */
public class ProctorProfileController extends VU360BaseMultiActionController {
	
	private static final Logger log = Logger.getLogger(ProctorProfileController.class.getName());
	
	//Templates
	private String showProctorProfilePage = null;
	
	//Services
	private VU360UserService vu360UserService = null;
	private LearnerService learnerService = null;
	private ProctorService proctorService = null;
	private VelocityEngine velocityEngine = null;
	
	public ProctorProfileController() {
		super();
	}

	public ProctorProfileController(Object delegate) {
		super(delegate);
	}
	
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		
		
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		
		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView showProctorProfileForm(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		ProctorProfileForm proctorProfileForm = (ProctorProfileForm)command;
		Proctor proctor = null;
		
		//get LoggedIn User from session
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//get Proctor Profile details to set in form bean--
		proctor = proctorService.getProctorByUserId(loggedInUser.getId());
		
		//Populate form bean with proctor details
		proctorProfileForm.setProctor(proctor);
		proctorProfileForm.setId(proctor.getId());
		proctorProfileForm.setNewPassword(proctor.getPassword());
		proctorProfileForm.setProctorId(proctor.getUsername());
		
		return new ModelAndView(showProctorProfilePage);
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateProctorProfile(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		ProctorProfileForm proctorProfileForm = (ProctorProfileForm)command;
		Proctor proctor = new Proctor();
		
//		//Validate form
		
		if(StringUtils.isBlank(proctorProfileForm.getNewPassword())){
			errors.rejectValue("currentPassword", "error.learnerRegistrationPassword.required","");
		}
		if(StringUtils.isNotBlank(proctorProfileForm.getNewPassword())){
			if(!FieldsValidation.isPasswordCorrect(proctorProfileForm.getNewPassword())){
				errors.rejectValue("currentPassword", "error.editLearnerPassword.invalidlength","");
			}else if(!StringUtils.equals(proctorProfileForm.getNewPassword(),proctorProfileForm.getConfirmPassword())){
				errors.rejectValue("currentPassword", "error.password.matchPassword","");
			}
		}			
		if( errors.hasErrors())
			return new ModelAndView(showProctorProfilePage);
		
		//Populate form bean with proctor details
		proctor = proctorProfileForm.getProctor();
		proctor.setPassword(proctorProfileForm.getNewPassword());
		proctor.setPasswordResetDateTime(new Date());
		//proctor.setProctorStatusTimeStamp(new Date());
		//Updating proctor
		proctorService.saveProctor(proctor);
		proctorProfileForm.setSaved("true");
		return new ModelAndView(showProctorProfilePage);
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

	/**
	 * @return the proctorService
	 */
	public ProctorService getProctorService() {
		return proctorService;
	}

	/**
	 * @param proctorService the proctorService to set
	 */
	public void setProctorService(ProctorService proctorService) {
		this.proctorService = proctorService;
	}


	/**
	 * @return the showProctorProfilePage
	 */
	public String getShowProctorProfilePage() {
		return showProctorProfilePage;
	}


	/**
	 * @param showProctorProfilePage the showProctorProfilePage to set
	 */
	public void setShowProctorProfilePage(String showProctorProfilePage) {
		this.showProctorProfilePage = showProctorProfilePage;
	}

}
