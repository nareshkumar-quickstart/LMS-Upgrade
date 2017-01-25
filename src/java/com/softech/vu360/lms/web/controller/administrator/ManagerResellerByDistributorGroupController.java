package com.softech.vu360.lms.web.controller.administrator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.LMSAdministratorAllowedDistributor;
import com.softech.vu360.lms.service.DistributorService;

public class ManagerResellerByDistributorGroupController extends MultiActionController {
	
	private String manageResellerByAdministratorTemplate = null;
	private String addAdministratorInResellerGroupTemplate=null;
	DistributorService distributorService = null;
	
	
	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response)	throws Exception {
			
			
			String action = request.getParameter("actionField");
			if(action!=null && action.equals("deleteDistributor"))
				return deleteDistributorByGroupIdAndAdministratorId(request, response);
			else
				return displayEditDistributorGroupName(request, response);
			
	}
	
	public ModelAndView displayEditDistributorGroupName(HttpServletRequest request, HttpServletResponse response) {
	
			HttpSession sess = request.getSession();
			
			if(request.getParameter("groupId")!=null)
				sess.setAttribute("groupId", request.getParameter("groupId"));
			if(request.getParameter("administratorId")!=null)
				sess.setAttribute("administratorId", request.getParameter("administratorId"));
			
			if(request.getParameter("administratorName")!=null)
				sess.setAttribute("administratorName", request.getParameter("administratorName"));
			
			Map<Object, Object> context = new HashMap<Object, Object>();	
			List<Distributor> distributorGroups=null;
			distributorGroups = distributorService.getAllowedDistributorByGroupId(sess.getAttribute("groupId").toString(),
																				  sess.getAttribute("administratorId").toString());
			
			context.put("distributorGroups", distributorGroups);
			return new ModelAndView(manageResellerByAdministratorTemplate, "context", context);
			
	}



	public ModelAndView addAdministratorInResellerGroup(HttpServletRequest request, HttpServletResponse response) {
		DistributorGroup distributorGroup_ = distributorService.getDistributorGroupById(Long.valueOf(request.getParameter("Id")));
		List<Distributor> groupDistributors = distributorGroup_.getDistributors();
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		//previous page param
		if(request.getParameterValues("selectReseller")!=null){
			String[] selectAdminstrator = request.getParameterValues("selectReseller");
			context.put("selectedLMSAdministrator", Arrays.asList(selectAdminstrator));
		}
		//context.put("firstname", request.getParameter("firstname"));
		//context.put("lastname", request.getParameter("lastname"));
		//context.put("emailaddress", request.getParameter("emailaddress"));

		//paging and sorting params
		////performSorting(groupDistributors, request);

		context.put("sortDirection", (request.getParameter("sortDirection")==null) ? 0 : request.getParameter("sortDirection") )   ;
		context.put("sortColumnIndex", (request.getParameter("sortColumnIndex")==null) ? 0 : request.getParameter("sortDirection")							);
		context.put("showAll", isShowAll(request));

		
		context.put("resellerOf", distributorGroup_.getName());
		context.put("listOfReseller", groupDistributors);
		context.put("distributorGroup", distributorGroup_);
		

		return new ModelAndView(addAdministratorInResellerGroupTemplate, "context", context);
	}
	
	

	public ModelAndView deleteDistributorByGroupIdAndAdministratorId(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession sess = request.getSession();
		
		if(request.getParameter("groupId")!=null)
			sess.setAttribute("groupId", request.getParameter("groupId"));
		if(request.getParameter("administratorId")!=null)
			sess.setAttribute("administratorId", request.getParameter("administratorId"));
		
		
		String[] selectedDistributorIds = request.getParameterValues("distributorIds");
		
		
		
	
		//long adminstratorIdArray[] = new long[selectedDistributorIds.length];
		if( selectedDistributorIds != null ){
			for( int i=0; i<selectedDistributorIds.length; i++ ) {
				String adminID = selectedDistributorIds[i];
				if( StringUtils.isNotBlank(adminID)) {
					//adminstratorIdArray[i]=Long.parseLong(selectedDistributorIds[i]);
					distributorService.deleteDistributorByGroupIdAndAdministratorId(sess.getAttribute("groupId").toString(),
														sess.getAttribute("administratorId").toString(),selectedDistributorIds[i]);
				}	
			}	
			//if (adminstratorIdArray.length>0)
			//	distributorService.deleteDistributorByGroupIdAndAdministratorId(sess.getAttribute("groupId").toString(),
			//																		sess.getAttribute("administratorId").toString(), 
			//																			adminstratorIdArray);
		}
		
		
		return displayEditDistributorGroupName(request, response);
		
	}

	
	

	public ModelAndView addDistributorWithGroupIdAndAdministratorId(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession sess = request.getSession();
		
		//sess.setAttribute("groupId", request.getParameter("groupId"));
		//sess.setAttribute("administratorId", request.getParameter("administratorId"));
		
		String[] selectResellers = request.getParameterValues("selectReseller");
		
		LMSAdministratorAllowedDistributor allowedDistributor = null;
		
		if( selectResellers != null ){
			for( int i=0; i<selectResellers.length; i++ ) {
				String adminID = selectResellers[i];
				if( StringUtils.isNotBlank(adminID)) {
					 allowedDistributor  = new LMSAdministratorAllowedDistributor();
					 allowedDistributor.setDistributorGroupId(Long.valueOf(sess.getAttribute("groupId").toString()));
					 allowedDistributor.setLmsAdministratorId(Long.valueOf(sess.getAttribute("administratorId").toString()));
					 allowedDistributor.setAllowedDistributorId(Long.valueOf(selectResellers[i]));
					 
					distributorService.addDistributorWithGroupIdAndAdministratorId(allowedDistributor);
				}	
			}	
		}
		
		
		return displayEditDistributorGroupName(request, response);
		
	}

	
	
	

	public String getManageResellerByAdministratorTemplate() {
		return manageResellerByAdministratorTemplate;
	}
	
	
	
	public void setManageResellerByAdministratorTemplate(
			String manageResellerByAdministratorTemplate) {
		this.manageResellerByAdministratorTemplate = manageResellerByAdministratorTemplate;
	}



	public DistributorService getDistributorService() {
		return distributorService;
	}
	
	
	
	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}	
		
	
	public String getAddAdministratorInResellerGroupTemplate() {
		return addAdministratorInResellerGroupTemplate;
	}

	public void setAddAdministratorInResellerGroupTemplate(
			String addAdministratorInResellerGroupTemplate) {
		this.addAdministratorInResellerGroupTemplate = addAdministratorInResellerGroupTemplate;
	}

	private static String isShowAll(HttpServletRequest request) {
	
		String sa = request.getParameter("showAll");
		if (StringUtils.isEmpty(sa) || sa.equalsIgnoreCase("false")) {
			return "false";
		} else {
			return "true";
		}
	}	
	
	
	
	
}
