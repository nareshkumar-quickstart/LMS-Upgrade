package com.softech.vu360.lms.web.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.component.bean.ProxyHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.util.WebUtils;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.BrandDomain;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.BrandDomainService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.util.CASStorefrontUtil;
import com.softech.vu360.util.Cas;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class LoginPageController implements Controller {
	
	private static final Logger log = Logger.getLogger(LoginPageController.class.getName());
	
	private String loginPage = null;
	private BrandDomainService brandDomainService = null;
	private VU360UserService vu360UserService=null;
	protected static String PREDICT_LOCATION = VU360Properties.getVU360Property("lms.predict.location");
	protected static String PREDICT_PREAUTH_LOCATION = VU360Properties.getVU360Property("lms.predict.preauth.location");
	protected static String AUTH_TOEKN_HEADER = "360_AUTH_USER_TOKEN";
	protected static String LMS_LOGIN_URL = VU360Properties.getVU360Property("lms.login.url");
	protected static final String CAS_LOGOUT_URL = VU360Properties.getVU360Property("lms.cas.logout.url");
	protected static final String LMS_DOMAIN = VU360Properties.getVU360Property("lms.domain");
	protected static final String STOREFRONT_LOGIN_URL = VU360Properties.getVU360Property("lms.storefront.login.url");
	
	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		/**
		 * CAS Authentication
		 * For CAS Authentication system needs
		 * ticket & service parameters
		 */
		String ticket, to, predictUrl;
		
		
		Cookie cookies[]= request.getCookies();
		if (cookies!=null)
		{
			for (int i=0; i<cookies.length; i++)
			{
				if(cookies[i].getName().equalsIgnoreCase("isPredictUser"))
				{
					//isPredictUserCookie = true;
					cookies[i].setMaxAge(0);
					cookies[i].setValue("");
					response.addCookie(cookies[i]);
					predictUrl = PREDICT_LOCATION + PREDICT_PREAUTH_LOCATION;
					response.sendRedirect(predictUrl);
					
					break;
				}
			}
		}
		
		// get required parameters
		ticket = request.getParameter("ticket");
		
		// redirect to, after signed in
		to = request.getParameter("to");
		
		// CAS Authentication
		if(ticket != null && !ticket.equals(StringUtils.EMPTY)) {
		
			StringBuilder serviceUrlBuilder;
			String serviceUrl;
			serviceUrlBuilder = new StringBuilder();

			// prepare Service URL for CAS Authentication
			serviceUrlBuilder
			// on request.getRequestURL(), it returns
			// route URL incremented with port (8080)
			// and non HTTPS scheme. We need exact
			// browser URL
//			.append(request.getRequestURL().toString())
			.append(LMS_DOMAIN)
			.append(request.getRequestURI());
			if((request.getQueryString() != null) && !(request.getQueryString().toString().equals(StringUtils.EMPTY))) {
				serviceUrlBuilder.append("?");
				serviceUrlBuilder.append(request.getQueryString());
			}
			
			// remove unwanted parameter from Service URL
			serviceUrl = serviceUrlBuilder.toString();
			
			serviceUrl = serviceUrl.replace("&ticket=".concat(ticket), StringUtils.EMPTY);
			serviceUrl = serviceUrl.replace("?ticket=".concat(ticket), StringUtils.EMPTY);
			serviceUrl = URLDecoder.decode(serviceUrl, "UTF-8");
			
			log.info(serviceUrl);
			
			// if all required parameters for 
			// CAS Authentication are provided,
			// proceed CAS Ticket Authentication
			if((ticket != null && !ticket.equals(StringUtils.EMPTY)) && 
					(serviceUrl != null && !serviceUrl.equals(StringUtils.EMPTY))) {
				try {
					Map<String, Object> casModelMap = authenticateCAS(ticket, serviceUrl);
					if(casModelMap != null && casModelMap.size() == 2) {
						// redirect to specific module, if 'to'
						// is not given, land user to default module
						if((to != null) && (!to.equals(StringUtils.EMPTY))) {
							casModelMap.put("domain", LMS_DOMAIN);
							casModelMap.put("to", request.getParameter("to"));
						}
						/**
						 * Once user login through CAS,
						 * and his/her session timeout
						 * it should be redirected to 
						 * Storefront login page 
						 */
						Cookie cookieCAS = new Cookie("isCASAuthenticated", "true");
						cookieCAS.setMaxAge(60*60*24); 
						response.addCookie(cookieCAS);
						
						VU360User vu360User = vu360UserService.getUserByUsernameAndDomain(casModelMap.get("userName").toString(), StringUtils.EMPTY);
						Cookie cookieLogoutSuccessUrl = new Cookie("logoutSuccessUrl", CASStorefrontUtil.getLogOutUrl(ProxyVOHelper.setUserProxy(vu360User)));
						cookieLogoutSuccessUrl.setMaxAge(60*60*24);
						response.addCookie(cookieLogoutSuccessUrl);
						
						return new ModelAndView("seamlessLogin", casModelMap);
					}
				} catch(Exception ex) {
					/**
					 * Log Exception 
					 * If the provided parameters (ticket and service)
					 * invalid/expired/incorrect then log the error
					 */
					log.error(ex.getMessage());
				}
			}
		}
		
		/**
		 * Once user login through CAS,
		 * and his/her session timed out
		 * it should be redirected to 
		 * Storefront login page 
		 */
		Cookie casCookie = WebUtils.getCookie(request, "isCASAuthenticated");
		Cookie cookieLogoutSuccessUrl = WebUtils.getCookie(request, "logoutSuccessUrl");
		if (casCookie != null && cookieLogoutSuccessUrl != null) {
		   casCookie.setMaxAge(0);
		   cookieLogoutSuccessUrl.setMaxAge(0);
		   response.addCookie(casCookie);
		   response.addCookie(cookieLogoutSuccessUrl);
		   //response.sendRedirect("redirect:/logout?logoutSuccessUrl=" + cookieLogoutSuccessUrl.getValue());
		}
		
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		
		/* Now determine which environment we are in.
		 * This is used to toggle the Google Analytics code on the
		 * login page on/off.  GA code is on for PROD environment only
		 * so that we are getting an accurate picture of what the end-users'
		 * systems look like.
		 */
		String runningEnvironment = VU360Properties.getVU360Property("running.environment");
		if ( runningEnvironment == null || runningEnvironment.trim().isEmpty() ) {
			runningEnvironment = "DEV";
		}
		context.put("runningEnvironment", runningEnvironment);
		
		// now set put the brander in the session to be used throughout
		
		//else if lms user
		return new ModelAndView(loginPage, "context", context);

	}
	
	protected boolean predictLoginPage(HttpServletRequest request, HttpServletResponse response) throws ClientProtocolException, IOException {
		
		String url = PREDICT_LOCATION + PREDICT_PREAUTH_LOCATION;
		response.sendRedirect(url);
		
		return true;
	}

	/**
	 * Validate CAS Ticket
	 * @param serviceUrl
	 * @param ticket
	 * @return String
	 * @throws Exception
	 */
	private String validateTicket(String serviceUrl, String ticket) throws Exception {
		return Cas.validate(VU360Properties.getVU360Property("lms.cas.validation.url"), ticket, serviceUrl);
	}
	
	/**
	 * Authenticate CAS Users
	 * @param ticket
	 * @param serviceUrl
	 * @return Map<String, String>
	 * @throws Exception
	 */
	private Map<String, Object> authenticateCAS(String ticket, String serviceUrl) throws Exception {
		String casUserName, password;
		Map<String, Object> modelMap = null;
		casUserName = validateTicket(serviceUrl, ticket);
		if(!casUserName.equals(StringUtils.EMPTY)) {
			
			VU360User vu360User = vu360UserService.getUserByUsernameAndDomain(casUserName, StringUtils.EMPTY);
			if(vu360User != null) {
				password = vu360User.getPassword();
	
				modelMap = new HashMap<String, Object>();
				modelMap.put("userName", casUserName);
				modelMap.put("password", password);
				
			}
		}
		return modelMap;
	}
	
	/**
	 * @return the brandDomainService
	 */
	public BrandDomainService getBrandDomainService() {
		return brandDomainService;
	}

	/**
	 * @param brandDomainService the brandDomainService to set
	 */
	public void setBrandDomainService(BrandDomainService brandDomainService) {
		this.brandDomainService = brandDomainService;
	}

	/**
	 * @return the loginPage
	 */
	public String getLoginPage() {
		return loginPage;
	}

	/**
	 * @param loginPage the loginPage to set
	 */
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
}