package com.softech.vu360.lms.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomFormLoginFilter extends UsernamePasswordAuthenticationFilter {
	private static final Logger log = Logger.getLogger(CustomFormLoginFilter.class.getName());
	private UserDetailsService userDetailsService;

	public CustomFormLoginFilter() {
		//setPostOnly(false);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		/*
		 * String username = request.getHeader(usernameHeader);
		 * String password = request.getHeader(passwordHeader);
		 * SignedUsernamePasswordAuthenticationToken authRequest = new SignedUsernamePasswordAuthenticationToken(username, password);
		 * return this.getAuthenticationManager().authenticate(authRequest); 
		*/
		
		return super.attemptAuthentication(request, response);
		
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		log.debug("!!!!!!!!!!!!!!!!!!!!!!!!obtainPassword!!!!!!!!!!!!!!!!!!!!!!!!");
		return super.obtainPassword(request);
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		log.debug("!!!!!!!!!!!!!obtainUsername!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		return super.obtainUsername(request);
	}

	@Override
	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		log.debug("!!!!!!!!!!!!!!!!!!!!!authRequest!!!!!!!!!!!!!!!!!!!!!!!!!!!"+authRequest);
		super.setDetails(request, authRequest);
	}

	@Override
	public void setUsernameParameter(String usernameParameter) {
		log.debug("!!!!!!!!!!!!!!!!!!!!!!!!usernameParameter!!!!!!!!!!!!!!!!!!!!!!!!"+usernameParameter);
		super.setUsernameParameter(usernameParameter);
	}

	@Override
	public void setPasswordParameter(String passwordParameter) {
		log.debug("!!!!!!!!!!!!!!!!!!!passwordParameter!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+passwordParameter);
		super.setPasswordParameter(passwordParameter);
	}

	@Override
	public void setPostOnly(boolean postOnly) {
		log.debug("!!!!!!!!!!!!!!!!!!!!!!!postOnly!!!!!!!!!!!!!!!!!!!!!!!!!"+postOnly);
		super.setPostOnly(postOnly);
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	
}
