package com.softech.vu360.lms.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * General purpose class to load a velocity template that
 * requires not business logic or processing other than just
 * presenting the page.  Best example is displaying the login page.
 * 
 * @author jason
 *
 */
public class DisplayVelocityTemplateController implements Controller {

	private static final Logger log = Logger.getLogger(DisplayVelocityTemplateController.class.getName());
	private String defaultTemplate = null;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String templateName = request.getParameter("vm");
		Map<Object, Object> context = new HashMap<Object, Object>();

		if(templateName == "keepalive")
		{
			response.setHeader("cache-control", "no-cache");
			response.setHeader("pragma", "no-cache");
			response.setHeader("expires", "0");
		}
		
		if( !StringUtils.isBlank(request.getParameter("message")) && request.getParameter("message").equalsIgnoreCase("true"))
			context.put("message", 1);
		else
			context.put("message", 0);
		try {
			if ( templateName == null || templateName.equalsIgnoreCase("") ) {
				templateName = defaultTemplate;
			}
			log.debug("forwarding to velocity template:"+templateName);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(templateName,"context",context);
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