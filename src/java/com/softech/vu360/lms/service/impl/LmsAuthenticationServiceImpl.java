package com.softech.vu360.lms.service.impl;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.softech.vu360.lms.service.LmsAuthenticationService;

@Service
public class LmsAuthenticationServiceImpl implements LmsAuthenticationService {

	@Inject
	private UserDetailsService userDetailsService;

	@Inject
	@Qualifier("authenticationManagerAlias")
	private AuthenticationManager authManager;
	
	@Override
	public Authentication authenticateUser(String userName) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword()));
		return auth;
	}

}
