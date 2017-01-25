package com.softech.vu360.lms.web.controller.learner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.vo.VU360User;
import com.softech.vu360.util.CASStorefrontUtil;
import com.softech.vu360.util.VU360Properties;

public class LS360Dashboard implements Controller {

	protected final String LS360DashboardUrl = VU360Properties.getVU360Property("lms.storefront.ls360dashboard.url");
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws Exception {
		VU360User loggedInUser = null;
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth.getPrincipal() != null
				&& auth.getPrincipal() instanceof VU360User) {
			loggedInUser = (VU360User) auth.getPrincipal();
		}
		
		return new ModelAndView("redirect:" + CASStorefrontUtil.getLS360DashboardUrl(loggedInUser));
	}

}
