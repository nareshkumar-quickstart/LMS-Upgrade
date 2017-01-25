package com.softech.vu360.lms.web.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.aws.model.AffiliateResult;
import com.softech.vu360.lms.service.AmazonIntegrationService;

public class AmazonIntegrationController implements Controller{

	private AmazonIntegrationService amazonIntegrationService;
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String keyword = request.getParameter("keyword");
		String jsonCallback = request.getParameter("jsoncallback");
		
		if(keyword==null){
			keyword = "C#";
		}
		//if(keyword.equalsIgnoreCase("kindle")){
		//	keyword = "Kindle Fire";
		//}
		
		System.out.println(keyword);
		
		
		AffiliateResult aF = amazonIntegrationService.affiliateSearchResults(keyword);
		
		JSONArray aFArray = aF.toJSON();
		
		
		
		try {
			JSONObject returnJSON = new JSONObject();
			returnJSON.put("affiliateItems", aFArray);
			
			String jsonPValue = jsonCallback + "(" + returnJSON.toString() + ")";
			
			System.out.println(jsonPValue);
			
			PrintWriter pw = response.getWriter();
			response.setHeader("Content-Type", "application/json");
			pw.write(jsonPValue);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			//log.debug("IOException", e);
		} catch (IllegalStateException e) {
			//log.debug("IllegalStateException", e);
		} catch (Exception e) {
			//log.debug("exception", e);
		}
		return null;
	}

	
	
	
	
	
	
	/**
	 * @return the amazonIntegrationService
	 */
	public AmazonIntegrationService getAmazonIntegrationService() {
		return amazonIntegrationService;
	}

	/**
	 * @param amazonIntegrationService the amazonIntegrationService to set
	 */
	public void setAmazonIntegrationService(
			AmazonIntegrationService amazonIntegrationService) {
		this.amazonIntegrationService = amazonIntegrationService;
	}

	
	
	
}
