package com.softech.vu360.lms.service;

public interface JwtService {

	String validateToken(String jwtValidationUrl , String token) throws Exception;
	String validateToken(String token) throws Exception;
	
}
