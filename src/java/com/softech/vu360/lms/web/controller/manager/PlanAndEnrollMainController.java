package com.softech.vu360.lms.web.controller.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


public class PlanAndEnrollMainController implements Controller {

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());
		
	private String viewEnrollmentMainTemplate = null;
		
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		/**
		 * S M Humayun | 31 Mar 2011
		 */
		if(this.getViewEnrollmentMainTemplate().contains("/customer/"))
			request.getSession(true).setAttribute("feature", "LMS-ADM-0012");
		else if(this.getViewEnrollmentMainTemplate().contains("/distributor/"))
			request.getSession(true).setAttribute("feature", "LMS-ADM-0018");
		
		return new ModelAndView(viewEnrollmentMainTemplate);
	}
	
	public String getViewEnrollmentMainTemplate() {
		return viewEnrollmentMainTemplate;
	}

	public void setViewEnrollmentMainTemplate(String viewEnrollmentMainTemplate) {
		this.viewEnrollmentMainTemplate = viewEnrollmentMainTemplate;
	}
	
}