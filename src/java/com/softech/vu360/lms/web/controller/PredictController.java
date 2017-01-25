package com.softech.vu360.lms.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.util.XorEncoding;
import com.softech.vu360.util.VU360Properties;

/**
 * Controller for handling the integration with Predict
 * @author muhammad.javed
 *
 */
public class PredictController implements Controller {

	protected static final Logger log = Logger.getLogger(PredictController.class);
	protected static final String PREDICT_LOCATION = VU360Properties.getVU360Property("lms.predict.location");
	protected static final String PREDICT_PREAUTH_LOCATION = VU360Properties.getVU360Property("lms.predict.preauth.location");
	protected static String AUTH_TOEKN_HEADER = "360_AUTH_USER_TOKEN";
	
	/**
	 * Main controller method <br/>
	 * 
	 * Does the preauth stuff and invalidates current session
	 * 
	 * @author muhammad.javed
	 */
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		log.debug(">> in PredictController");
		com.softech.vu360.lms.vo.VU360User vu360User;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		vu360User = (com.softech.vu360.lms.vo.VU360User) auth.getPrincipal();
		
		try {
			if( doPredictLogin(request, response, vu360User) ) {
				HttpSession session = request.getSession(false);
				if(session!=null) {
					SecurityContextHolder.getContext().setAuthentication(null);
				}
				return null;
			}
		} catch (ClientProtocolException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		log.warn("i guess there was some problem redirecting to home page");
		return new ModelAndView("redirect:/");
	}
	
	/**
	 *
	 * Prepares the URL for pre auth process of LMS and sends the redirect by encoding the username.
	 * 
	 * @param {@link javax.servlet.http.HttpServletRequest}
	 * @param {@link javax.servlet.http.HttpServletResponse}
	 * @param {@link VU360User}
	 * @return returns true if there is no exception
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @author muhammad.javed
	 */
	protected boolean doPredictLogin(HttpServletRequest request, HttpServletResponse response, com.softech.vu360.lms.vo.VU360User vu360User) throws ClientProtocolException, IOException  {
		log.debug(">> doing PREDICT preauth on " + PREDICT_LOCATION + PREDICT_PREAUTH_LOCATION);
		
		String username = vu360User.getUsername();
		String encoded =  new XorEncoding().encode(username);
		log.debug("username = " + username + " encoded = " + encoded);
		log.debug("redirectUrl ======================================" + request.getParameter("redirectUrl"));
		
		String strRedirect = PREDICT_LOCATION + PREDICT_PREAUTH_LOCATION + "?" + AUTH_TOEKN_HEADER + "=" + encoded;
		log.debug("sending redirect to " + strRedirect);
		
		response.addHeader(AUTH_TOEKN_HEADER, encoded);
		response.sendRedirect(strRedirect);
		
		return true;
	}

}
