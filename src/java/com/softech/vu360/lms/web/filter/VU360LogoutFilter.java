package com.softech.vu360.lms.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.util.VU360Properties;

public class VU360LogoutFilter extends LogoutFilter {

	private static final Logger LOGGER = Logger.getLogger(VU360LogoutFilter.class);
	
	public VU360LogoutFilter(String logoutSuccessUrl, LogoutHandler[] handlers) {
		super(logoutSuccessUrl, handlers);
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String requestedURI = request.getRequestURI();
        StringBuffer requestedURL = request.getRequestURL();
        String requestedPath = request.getServletPath();
        
        //logger.debug("#### requestedURI:"+requestedURI+"\n#### requestedURL:"+requestedURL);
		if (requiresLogout(request, response)) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			LOGGER.debug("running custom logout filter");
			if(authentication!=null && authentication.getPrincipal()!=null) {
				com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) authentication.getPrincipal();
				if(vu360User.getLearner()!=null) {
					if(vu360User.getLearner().getCustomer()!=null) {
						if(vu360User.getLearner().getCustomer().getDistributor()!=null) {
							String distributorName = vu360User.getLearner().getCustomer().getDistributor().getName().trim();
							String strPredictResellers = VU360Properties.getVU360Property("predict.resellers");
							if(strPredictResellers!=null) {
								String[] predictResellers = strPredictResellers.split(",");
								for (String predictReseller : predictResellers) {
									if(predictReseller.trim().compareTo(distributorName)==0) {
										String predictServerLocation = VU360Properties.getVU360Property("lms.predict.location");
										String predictPreauthContextLocation = VU360Properties.getVU360Property("lms.predict.preauth.location");
										String url = predictServerLocation + predictPreauthContextLocation;
										if(!response.isCommitted()) {
											try {
												SecurityContextHolder.getContext().setAuthentication(null);
												request.getSession().invalidate();
												response.sendRedirect(url);
												return;
											} catch (IOException e) {
												LOGGER.error(e.getMessage(), e);
											}
										} else {
											LOGGER.debug("response is already committed");
										}
									}
								}
							} else {
								LOGGER.debug("predict.resellers property is not set");
							}
						} else {
							LOGGER.debug("distributor is null");
						}
					} else {
						LOGGER.debug("customer is null");
					}
				} else {
					LOGGER.debug("learner is null");
				}
			} else {
				LOGGER.debug("authentication is null");
			}
		} 
		else if(requestedPath.equals("/index.html") || requestedPath.equals("/login.do")){
			//try {
				SecurityContextHolder.getContext().setAuthentication(null);
				//request.getSession().invalidate();
				//response.sendRedirect("/lms/login.do");
				//return;
			//} catch (IOException e) {
			//	LOGGER.error(e.getMessage(), e);
			//}
		}
		else {
			LOGGER.debug("logout is not required");
		}
		
		super.doFilter(request, response, chain);
	}

}
