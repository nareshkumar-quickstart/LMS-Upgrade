package com.softech.vu360.lms.web.filter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/*
 * @Kaunain - VU360AuthenticationFailureHandler for catching exceptions related 
 * AuthenticationFailure and reporting back to the velocity template in order to 
 * produce appropriate error message validation error at front end 
 * 
 */


public class VU360AuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {

	//set if request needs to be forwarded to destination
	private boolean forwardToDestination = true;
	private String defaultFailureUrl = null;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	//Constructor which takes default URL as parameter
	public VU360AuthenticationFailureHandler(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;

	}
	
	//Override AuthenticationFailure to adjust own way as Spring4 has changed widely
	@Override
	public void onAuthenticationFailure(
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response,
			AuthenticationException exception) throws IOException,
			ServletException

	{

		if (defaultFailureUrl == null) {
			logger.debug("No failure URL set, sending 401 Unauthorized error");
			saveException(request,exception);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Authentication Failed: " + exception.getMessage());
		} else {
			if (forwardToDestination) {
				logger.debug("Forwarding to " + defaultFailureUrl);
				saveException(request,exception);
				request.setAttribute("SECURITY_EXCEPTION", exception);
				request.getRequestDispatcher(defaultFailureUrl).forward(request, response);//Forward as we do not want a new request
		        
				
				
			} else {
				logger.debug("Redirecting to " + defaultFailureUrl);
				saveException(request,exception);
				redirectStrategy.sendRedirect(request, response,
						defaultFailureUrl);// If no appropriate strategy, redirect to a fresh new request
				
			}
		}

	}

}
