package com.softech.vu360.lms.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;
/**
 * ReportNotExecutableException class... enough said.
 *
 * @author  jason
 */
@SuppressWarnings("serial")
public class VU360LMSExceptionHandler implements HandlerExceptionResolver {
	
	private String errorView = null;


	/**
	 * @return the errorView
	 */
	public String getErrorView() {
		return errorView;
	}

	/**
	 * @param errorView the errorView to set
	 */
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) {

		Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		com.softech.vu360.lms.vo.VU360User user =  (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ModelAndView modelAndView = new ModelAndView(errorView);
		
		StringBuffer sf = new StringBuffer();
		if(brander != null){
			modelAndView.addObject("brander", brander);
		}
		
		if(user != null){
			modelAndView.addObject("userData", user);
			sf.append("User Name : "+user.getUsername()+"<br>");
			sf.append("User Email : "+user.getEmailAddress()+"<br>");			
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
			VU360UserMode currentMode = details.getCurrentMode();
			sf.append("Mode : "+currentMode+"<br>");
			sf.append("Local Address : " + request.getLocalAddr()+"<br>");
			sf.append("Context Path : " + request.getContextPath() +"<br>");
			sf.append("Request URI : " + request.getRequestURI().toString() +"<br>");
			sf.append("Request URL : " + request.getRequestURL().toString() +"<br>");
			sf.append("Server Name : " + request.getServerName() +"<br>");
			sf.append("Remote Address : " + request.getRemoteAddr() +"<br>");
			sf.append("Remote Host : " + request.getRemoteHost() +"<br>");
			sf.append("Remote Port : " + request.getRemotePort() +"<br>");
			sf.append("Exception occured Date : " + new Date().toString() +"<br>");
			sf.append("Host : " + request.getHeader("Host") +"<br>");
			modelAndView.addObject("currentMode", currentMode);
		}
		
		modelAndView.addObject("userPermissionChecker", new UserPermissionChecker());
		
		StringWriter errors = new StringWriter();
		arg3.printStackTrace(new PrintWriter(errors));
		modelAndView.addObject("ExceptionTrace", errors.toString());
		System.out.println(errors.toString());

		try{
			SendMailService.sendSMTPMessage(
					new String[]{VU360Properties.getVU360Property("lms.email.globalException.toAddress")},
					null,
					VU360Properties.getVU360Property("lms.email.globalException.fromAddress"),
					VU360Properties.getVU360Property("lms.email.globalException.title"), 
					VU360Properties.getVU360Property("lms.email.globalException.subject"), "<div>"+sf.toString()+"<br>"+errors.toString()+"</div>");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return modelAndView;
	}

}
