package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

public class ManagerExternalServicesController extends
		VU360BaseMultiActionController {

	private String externalServicesTemplate = null;
	
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub

	}
	
	
	public ModelAndView displayExternalServicesLinks(HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> urlTitle = new HashMap<Object, Object>();
		ArrayList<Map<Object, Object>> externalUrlList = new ArrayList<Map<Object, Object>>();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		
		try{
			for(int iCount =1 ; iCount<=5 ; iCount++){
				String externalServicesLink = brander.getBrandElement("lms.manager.externalservices.link" + iCount);
				if (externalServicesLink != null){
					String[] arrExternalServicesLink =  externalServicesLink.split(";");
					if (arrExternalServicesLink.length >= 2 ){
						String title =  arrExternalServicesLink[0];
						String url =  arrExternalServicesLink[1];
						url = url + "?userguid=" + loggedInUser.getUserGUID();
						urlTitle.put("title", title);
						urlTitle.put("url", url);
						externalUrlList.add(urlTitle);
					}
				}
				
			}
		
		}catch(Exception ex){
			
			//String s = ex.getMessage();
		}
		context.put("externalUrlList", externalUrlList);
		return new ModelAndView(externalServicesTemplate, "context", context);
	}
	
	public String getExternalServicesTemplate() {
		return externalServicesTemplate;
	}

	public void setExternalServicesTemplate(String externalServicesTemplate) {
		this.externalServicesTemplate = externalServicesTemplate;
	}

}
