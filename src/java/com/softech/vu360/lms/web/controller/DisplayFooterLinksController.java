package com.softech.vu360.lms.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class DisplayFooterLinksController extends MultiActionController {
	private String aboutus;
	private String termsofuse;
	private String contactus;
	private String onlinePrivacy;

	public String getContactus() {
		return contactus;
	}

	public void setContactus(String contactus) {
		this.contactus = contactus;
	}

	public String getTermsofuse() {
		return termsofuse;
	}

	public void setTermsofuse(String termsofuse) {
		this.termsofuse = termsofuse;
	}

	public String getAboutus() {
		return aboutus;
	}

	public void setAboutus(String aboutus) {
		this.aboutus = aboutus;
	}

	

	public String getOnlinePrivacy() {
		return onlinePrivacy;
	}

	public void setOnlinePrivacy(String onlinePrivacy) {
		this.onlinePrivacy = onlinePrivacy;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
//		ModelAndView mNv=new ModelAndView(aboutus);
		
		
		//response.sendRedirect("http://localhost:8080/vu360-lms/aboutus.html");
		String strAction=request.getParameter("action");
		if(strAction.equalsIgnoreCase("aboutus")){
			return new ModelAndView(aboutus);
		}
		if(strAction.equalsIgnoreCase("termsofuse")){
			return new ModelAndView(termsofuse);
		}
		if(strAction.equalsIgnoreCase("contactus")){
			return new ModelAndView(contactus);
		}
		if(strAction.equalsIgnoreCase("onlineprivacy")){
			return new ModelAndView(onlinePrivacy);
		}
		return new ModelAndView("");
	}

	

}
