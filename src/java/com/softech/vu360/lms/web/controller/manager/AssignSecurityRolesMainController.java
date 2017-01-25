package com.softech.vu360.lms.web.controller.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class AssignSecurityRolesMainController implements Controller{

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());
		
	private String viewAssignSecurityTemplate = null;
		
	/**
	 * Set feature based on mode of logged in user in session.
	 * @param session
	 * @author sm.humayun
	 * @since 4.14 {LMS-9971}
	 */
	private void setFeatureInSession (HttpSession session)
	{
		session.setAttribute("feature"
				, ((VU360UserAuthenticationDetails) SecurityContextHolder
						.getContext().getAuthentication().getDetails())
						.getCurrentMode().name().equals("ROLE_LMSADMINISTRATOR")  
				? "LMS-ADM-0007" : "LMS-MGR-0007");
	}

	/**
	 * Set feature based on mode of logged in user in session.
	 * @param request
	 * @author sm.humayun
	 * @since 4.14 {LMS-9971}
	 */
	private void setFeatureInSession (HttpServletRequest request)
	{
		this.setFeatureInSession(request.getSession(true));
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		this.setFeatureInSession(request);
			
		return new ModelAndView(viewAssignSecurityTemplate);
	}
	
	public String getViewAssignSecurityTemplate() {
		return viewAssignSecurityTemplate;
	}

	public void setViewAssignSecurityTemplate(String viewAssignSecurityTemplate) {
		this.viewAssignSecurityTemplate = viewAssignSecurityTemplate;
	}
	
}