package com.softech.vu360.lms.web.controller.learner;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.vo.VU360User;


public class LearnerWidgetDashboardController implements Controller{

	protected static final Logger log = Logger.getLogger(LearnerWidgetDashboardController.class);

	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		/** Return ModelAndView */
		//return new ModelAndView("learner/widgetDashboard");
		
		//LMS-19305 - Invalid User GUID in LearningSession Table
		Map<String, String> o = new HashMap<String,String>();
		VU360User u = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		o.put("lguid",  u.getUserGUID());
		
		ModelAndView m = new ModelAndView("learner/widgetDashboard", "context", o);
		return m;
	}
}
