package com.softech.vu360.lms.web.controller.administrator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class ManageDistributorController extends MultiActionController implements InitializingBean {
	
	private static final Logger log = Logger.getLogger(ManageDistributorController.class.getName());
	private static final String MANAGE_DISTRIBUTOR_SEARCH_ACTION = "Search";
	private static final String MANAGE_DISTRIBUTOR_DELETE_ACTION = "Delete";
	private static final String MANAGE_DISTRIBUTOR_ALLDISTRIBUTORS_ACTION = "ShowAll";


	private DistributorService distributorService = null;
	private String displayDistributorTemplate = null;



	public ModelAndView displaySearchDistributor(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Map<Object, Object> context = new HashMap<Object, Object>();
			String action = request.getParameter("action");
			if(StringUtils.isNotBlank(action)){
				if(action.equalsIgnoreCase(MANAGE_DISTRIBUTOR_SEARCH_ACTION)){
					String distributorName = request.getParameter("distributorName");
					List<Distributor> listOfDistributors = distributorService.findDistributorsByName(distributorName, loggedInUser,false,0,-1,new ResultSet(),null,0);
					context.put("listOfDistributors", listOfDistributors);
					return new ModelAndView(displayDistributorTemplate, "context", context);
				}else if(action.equalsIgnoreCase(MANAGE_DISTRIBUTOR_DELETE_ACTION)){
					String[] selectedDistributorValues = request.getParameterValues("selectDistributor");
					long distributorIdArray[] = new long[selectedDistributorValues.length];
					if( selectedDistributorValues != null ){
						for( int i=0; i<selectedDistributorValues.length; i++ ) {
							String distributorID = selectedDistributorValues[i];
							if( StringUtils.isNotBlank(distributorID) ) {
								distributorIdArray[i]=Long.parseLong(selectedDistributorValues[i]);
							}	
						}		
						//distributorService.deleteDistributors(distributorIdArray);
					}
					List<Distributor> listOfDistributors = distributorService.findDistributorsByName("", loggedInUser,false,-1,-1,new ResultSet(),"name",0);
					context.put("listOfDistributors", listOfDistributors);
					return new ModelAndView(displayDistributorTemplate, "context", context);
				}else if(action.equalsIgnoreCase(MANAGE_DISTRIBUTOR_ALLDISTRIBUTORS_ACTION)){
					List<Distributor> listOfDistributors = distributorService.findDistributorsByName("", loggedInUser,false,-1,-1,new ResultSet(),"name",0);
					context.put("listOfDistributors", listOfDistributors);
					return new ModelAndView(displayDistributorTemplate, "context", context);
				}
			}
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(displayDistributorTemplate);
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}


	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public String getDisplayDistributorTemplate() {
		return displayDistributorTemplate;
	}

	public void setDisplayDistributorTemplate(String displayDistributorTemplate) {
		this.displayDistributorTemplate = displayDistributorTemplate;
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub

	}
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub

	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

}