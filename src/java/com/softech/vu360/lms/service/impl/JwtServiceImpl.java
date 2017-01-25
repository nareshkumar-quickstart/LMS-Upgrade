package com.softech.vu360.lms.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.softech.JWTValidation.JwtValidation;
import com.softech.JWTValidation.model.JwtPayload;
import com.softech.vu360.lms.service.JwtService;

@Service
public class JwtServiceImpl implements JwtService {

	@Override
	public String validateToken(String jwtValidationUrl, String token) throws Exception {

		String response = null;
		
		URL url = new URL(jwtValidationUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.connect();

		int status = conn.getResponseCode();
		switch (status) {
		    case HttpURLConnection.HTTP_OK:
            case HttpURLConnection.HTTP_CREATED:
            	response = getResponseAsString(conn.getInputStream());
            	conn.disconnect();
            	break;
            case HttpURLConnection.HTTP_BAD_REQUEST:
            case HttpURLConnection.HTTP_UNAUTHORIZED:
            case HttpURLConnection.HTTP_FORBIDDEN:
            case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
            	response = getResponseAsString(conn.getErrorStream());
            	conn.disconnect();
            	throw new Exception(response);	
		}
		return response;
	}
	
	@Override
	public String validateToken(String token) throws Exception {
	    String userName = null;
		JwtPayload jwtPayload = JwtValidation.validateJWTToken(token);
		if (jwtPayload == null) {
			throw new Exception("Invalid Token");	
		} 
		userName = jwtPayload.getUser_name();
		return userName;
		
	}

	private String getResponseAsString(InputStream connInputStream) throws Exception {

		String response = null;
		try (Reader in = new BufferedReader(new InputStreamReader(connInputStream));) {
			StringBuilder sb = new StringBuilder();
			for (int c; (c = in.read()) >= 0;) {
				sb.append((char) c);
			}
			response = sb.toString();
		}
		return response;
	}
	
	private String getUserName(String apiResponse) throws Exception {
		
		String userName = null;
		if (StringUtils.isNotBlank(apiResponse)) {
			ObjectMapper objectMapper = new ObjectMapper();
			JwtValidationResponse jwtValidationResponse = objectMapper.readValue(apiResponse, JwtValidationResponse.class);
			userName = jwtValidationResponse.getUserName();
		}
		return userName;
	}
	
	private static class JwtValidationResponse {

		private String userName;

		public String getUserName() {
			return userName;
		}

	}

}
