package com.softech.vu360.lms.web.controller.learner;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.lms.webservice.message.storefront.tester.ShowUserProfileDisplayResponse;
import com.softech.vu360.util.GUIDGeneratorUtil;


/**
 * QuickStart would like to use learner referrals (via e-mail, social media, and similar channels) 
 * to promote the QuickStart web stores and their product, in order to increase sales
 * @date 9-25-2013
 * @author haider.ali
 *
 *http://jira.360training.com/browse/LMS-14265
 *
 */
public class LearnerMyRewardsController extends AbstractController{

	
	private String defaultTemplate = null ;
	
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request,HttpServletResponse response) throws Exception {
		 
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		//call webservice ,
		// User GUID
		com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// Reseller ID
		String resellerCode = vu360User.getLearner().getCustomer().getDistributor().getDistributorCode();
		// Generated transaction GUID 		
		String transactionGUID = GUIDGeneratorUtil.generateGUID();
		
		StorefrontClientWSImpl wsClient= new StorefrontClientWSImpl();
		//calling the web service
		ShowUserProfileDisplayResponse wsResponse = wsClient.getUserRewardsInfo(vu360User.getUserGUID(), transactionGUID, resellerCode);
			
		if( wsResponse == null){
			context.put("responseCode", "null");
		} else {
			context.put("responseCode", wsResponse.getResponseCode());
			context.put("storeFrontURL", wsResponse.getUserProfileDisplayURL());
		}
		
		return new ModelAndView(defaultTemplate , "context" , context);
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
