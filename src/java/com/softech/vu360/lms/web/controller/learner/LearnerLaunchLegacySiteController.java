package com.softech.vu360.lms.web.controller.learner;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.util.Brander;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * 
 * @author haider.ali
 *
 */

public class LearnerLaunchLegacySiteController implements Controller{

	private static final String LEGACY_LAUNCH_SITE_LOC = VU360Properties.getVU360Property("lms.launch.legacySiteUrl");
	private static final short userGUID = 0;
	private static final short userName = 1;
	private static final short storeID = 2;
	private static final short transGUID = 3;
	
	
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		
		HashMap<String,Object> context = new HashMap<String,Object>();
        com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Brander brander = VU360Branding.getInstance().getBrander(
				(String) arg0.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());
		List<LabelBean> urlParams = brander.getBrandMapElements("lms.migration.legacysite.url");
		
		//user Guid
		LabelBean labelBean = urlParams.get(userGUID);
		context.put(labelBean.getLabel(),  user.getUserGUID());
		
		//user email
		LabelBean labelBean1 = urlParams.get(userName);
		context.put(labelBean1.getLabel(),  user.getUsername());

		//distributor code 
		LabelBean labelBean2 = urlParams.get(storeID);
		context.put(labelBean2.getLabel(), user.getLearner().getCustomer().getDistributor().getDistributorCode() );

		//transactionGUID  
		LabelBean labelBean3 = urlParams.get(transGUID);
		context.put(labelBean3.getLabel(), GUIDGeneratorUtil.generateGUID());
		
		
		//legacy URL
		context.put("siteLoc", LEGACY_LAUNCH_SITE_LOC);
		
		return new ModelAndView("learner/launchLegacySite", "context", context );
	}

}
