package com.softech.vu360.lms.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Menu Controller class used to fix/determine the current
 * page's feature and feature group.
 * @author Praveen Gaurav
 *
 */
public class MenuController implements Controller {
	
	private String defaultTemplate = null;
	private static final Logger log = Logger.getLogger(MenuController.class.getName());

	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) {
		
		try {
			
			String featureGroup = request.getParameter("featureGroup");
			String feature = request.getParameter("feature");
			String actionUrl = request.getParameter("actionUrl");
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("actionUrl", actionUrl);
			HttpSession ssn = request.getSession(true);
            //TODO refactoring Required should not be in session, (use cache-OS) 
			if( ( StringUtils.isNotBlank(featureGroup) ) && ( StringUtils.isNotBlank(feature) ) ) {
				ssn.setAttribute("featureGroup",featureGroup);
				ssn.setAttribute("feature",feature);
			}
			if ( !StringUtils.isBlank(actionUrl) ) {
				//RequestDispatcher rd = request.getRequestDispatcher(actionUrl.trim());
				//rd.forward(request, response);
				return new ModelAndView(defaultTemplate, "context", context);
			}
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView("");
	}

	/**
	 * @return the defaultTemplate
	 */
	public String getDefaultTemplate() {
		return defaultTemplate;
	}

	/**
	 * @param defaultTemplate the defaultTemplate to set
	 */
	public void setDefaultTemplate(String defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}
	
}