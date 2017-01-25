package com.softech.vu360.lms.web.controller.learner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softech.vu360.lms.vo.VU360User;


/**
 * 
 * @author haider.ali
 * @version 1.0
 */
public class ControllerUtils {

	
	 private ControllerUtils() {
		 
	 }

	public static Boolean isValidUserOrSession(HttpServletRequest request, HttpServletResponse response){
     	VU360User vu360User = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 	    String lguid = request.getParameter("lguid");
 	    return (!StringUtils.isEmpty(lguid) && !(vu360User.getUserGUID().equalsIgnoreCase(lguid))) ? true : false;
     }
}
