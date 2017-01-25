package com.softech.vu360.lms.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.service.JwtService;
import com.softech.vu360.lms.service.LmsAuthenticationService;
import com.softech.vu360.lms.vo.VU360User;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

@Controller
public class LmsServicesController {

	private static final Logger log = Logger.getLogger(LmsServicesController.class.getName());
	
	@Inject
	private JwtService jwtService;
	
	@Inject
	private LmsAuthenticationService lmsAuthenticationService;

	@RequestMapping(value = "/course.launch", method = RequestMethod.GET)
	public ModelAndView launchCourse(
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "learnerEnrollmentId", required = true) String enrollmentId,
			@RequestParam(value = "courseId", required = true) String courseId,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {

		log.info("Request received at " + getClass().getName() + " for Launch Course");
		ModelAndView modelAndView = null;
		try {
			String userName = jwtService.validateToken(token);
			if (StringUtils.isNotBlank(userName)) {
				Authentication auth = lmsAuthenticationService.authenticateUser(userName);
				Object principal = auth.getPrincipal();
				if (principal instanceof VU360User) {
					Brander brander = VU360Branding.getInstance().getBranderByUser(httpRequest, (VU360User)principal);
					httpRequest.getSession().setAttribute(VU360Branding.BRAND, brander);
				}
				String url = String.format("redirect:/lrn_launchCourse.do?method=displayLearnerProfile&learnerEnrollmentId=%s&courseId=%s", enrollmentId, courseId);
				modelAndView = getModelAndView(httpRequest, auth, url);
			} 	
		} catch (Exception e) {
			modelAndView = getErrorModelAndView(e);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/certificate.launch", method = RequestMethod.GET)
	public ModelAndView launchCertificate(
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "learnerEnrollmentId", required = true) String enrollmentId,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		
		log.info("Request received at " + getClass().getName() + " for Launch Certificate");
		
		ModelAndView modelAndView = null;
		try {
			String userName = jwtService.validateToken(token);
			if (StringUtils.isNotBlank(userName)) {
				Authentication auth = lmsAuthenticationService.authenticateUser(userName);
				modelAndView = getModelAndView(httpRequest, auth, "forward:/completionCertificate.pdf");
			}
		} catch (Exception e) {
			modelAndView = getErrorModelAndView(e);
		}
		return modelAndView;
	}

	private ModelAndView getModelAndView(HttpServletRequest httpRequest, Authentication auth, String viewName) throws Exception {
		SecurityContextHolder.getContext().setAuthentication(auth);
		httpRequest.getSession(true);
		return new ModelAndView(viewName);
	}
	
	private ModelAndView getErrorModelAndView(Exception e) {
		
		Map<Object, Object> errorModel = new HashMap<Object, Object>();
		errorModel.put("error", e.getMessage());
		return new ModelAndView("lmsServicesControllerError", "context", errorModel);
		
	}
	
}
