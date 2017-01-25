package com.softech.vu360.lms.service;

import org.springframework.security.core.Authentication;

public interface LmsAuthenticationService {

	Authentication authenticateUser(String userName);
	
}
