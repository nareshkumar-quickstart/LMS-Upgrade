package com.softech.vu360.lms.web.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class VU360LogoutHandler implements LogoutHandler {

	private static final Logger LOGGER = Logger.getLogger(VU360LogoutHandler.class);
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		LOGGER.debug("dummy logout handler");
		HttpSession session = request.getSession(false);
		if (session != null) {
	            session.invalidate();
        }
		
		
		String logoutSuccessUrl = request.getParameter("logoutSuccessUrl");
		System.out.println("VU360LogoutHandler->logoutSuccessUrl::"+logoutSuccessUrl);

		if(logoutSuccessUrl!=null){
			try {
				response.sendRedirect(logoutSuccessUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
