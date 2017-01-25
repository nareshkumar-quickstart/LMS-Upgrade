package com.softech.vu360.lms.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Author: Raja Wajahat Ali
 * This class is the implementation of Custom WebAuthenticationDetailsSource
 * Create Date:22-Aug-2015
 */
public class CustomWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

	@Override
	public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
		return new VU360UserAuthenticationDetails(request);
	}

}
