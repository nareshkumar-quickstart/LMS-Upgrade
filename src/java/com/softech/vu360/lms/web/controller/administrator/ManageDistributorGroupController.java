package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FieldsValidation;

/**
 * @author Tapas Mondal
 *
 */
public class ManageDistributorGroupController extends MultiActionController implements InitializingBean {

	private String displayEditDistributorGroupNameTemplate = null;
	private String displayEditDistributorGroupTemplate = null;
	DistributorService distributorService = null;
	private String manageDistributorGroupTemplate= null;
	private static final Logger log = Logger.getLogger(ManageDistributorGroupController.class.getName());

	
	private static final String MANAGE_DISTRIBUTORGROUP_SEARCH_ACTION = "Search";
	private static final String MANAGE_DISTRIBUTORGROUP_DEFAULT_ACTION = "Default";
	private static final String MANAGE_DISTRIBUTORGROUP_DELETE_ACTION = "Delete";
	private static final String MANAGE_DISTRIBUTORGROUP_ALLDISTRIBUTORS_ACTION = "ShowAll";
	//private static final String MANAGE_DISTRIBUTORGROUP_UPDATE_ACTION = "Update";

	public ModelAndView displayEditDistributorGroupName(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0027");
				
			log.debug(" displayEditDistributorGroupName ");
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//VU360UserAuthenticationDetails.getCurrentUser();
			Map<Object, Object> context = new HashMap<Object, Object>();
			String distributorGroupID=request.getParameter("distributorGroupId");
			String action = request.getParameter("action");
			DistributorGroup distributorGroup=null;
			if( StringUtils.isNotBlank(distributorGroupID) ){
				distributorGroup=distributorService.getDistributorGroupById(Long.valueOf(distributorGroupID));
				context.put("distributorGroup", distributorGroup);
			}
			else
				return new ModelAndView(displayEditDistributorGroupNameTemplate);			
			if( action!= null ){
					if(!this.validateData(request,context,loggedInUser)){
						context.put("vu360User", loggedInUser);
					return new ModelAndView(displayEditDistributorGroupNameTemplate, "context", context);
				}
				String distributorGroupName = request.getParameter("distributorGroupName");
				distributorGroup.setName(distributorGroupName);
				//distributorGroup.getDistributors().get(0)
				distributorGroup=distributorService.saveDistributorGroup(distributorGroup);
				//context.put("distributorGroup", distributorGroup);
				
				return new ModelAndView(manageDistributorGroupTemplate);
			}
			return new ModelAndView(displayEditDistributorGroupNameTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}		
		return new ModelAndView(displayEditDistributorGroupNameTemplate);
	}	
	
	public ModelAndView displayEditDistributorGroup(HttpServletRequest request, HttpServletResponse response) {
	
		
		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0027");
			
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		Map<Object, Object> context = new HashMap<Object, Object>();
		try {
			log.debug(" displayEditDistributorGroup");
			String distributorName=request.getParameter("distributorName");
			String distributorGroupID=request.getParameter("distributorGroupId");
			if ( distributorGroupID == null )
				return new ModelAndView(displayEditDistributorGroupTemplate);
			String action = request.getParameter("action");
			DistributorGroup distributorGroup= null;
			List<Distributor> distributors=null;
			List<Distributor> previouslySelectedDistributors=new ArrayList();
			if( StringUtils.isNotBlank(distributorGroupID) ){
				distributorGroup=distributorService.getDistributorGroupById(Long.valueOf(distributorGroupID));
				context.put("distributorGroup", distributorGroup);
			}
			context.put("firstTime", true);
			action = (action == null)?MANAGE_DISTRIBUTORGROUP_DEFAULT_ACTION:action;
			distributorName = (distributorName == null)?"":distributorName;
			
			if (action.equalsIgnoreCase(MANAGE_DISTRIBUTORGROUP_SEARCH_ACTION)){
				
				distributors=distributorService.findDistributorsByName(distributorName, loggedInUser,false,0,-1,new ResultSet(),null,0);
				for(Distributor selectedDistributor : distributorGroup.getDistributors()) {
					for(Distributor searchedDistributor : distributors) {
						
						if(selectedDistributor.getId().equals(searchedDistributor.getId())) {
							previouslySelectedDistributors.add(selectedDistributor);
						}
					}
				}
				context.put("previouslySelectedDistributors", previouslySelectedDistributors);
				context.put("distributors", distributors);
				context.put("firstTime", false);
				return new ModelAndView(displayEditDistributorGroupTemplate,"context",context);
				
			}else if (action.equalsIgnoreCase(MANAGE_DISTRIBUTORGROUP_ALLDISTRIBUTORS_ACTION)){

				distributors=distributorService.findDistributorsByName("", loggedInUser,false,-1,-1,new ResultSet(),"name",0);
				for(Distributor selectedDistributor : distributorGroup.getDistributors()) {
					for(Distributor searchedDistributor : distributors) {
						
						if(selectedDistributor.getId() == searchedDistributor.getId()) {
							previouslySelectedDistributors.add(selectedDistributor);
						}
					}
				}
				context.put("previouslySelectedDistributors", previouslySelectedDistributors);
				context.put("distributors", distributors);
				context.put("firstTime", false);
				return new ModelAndView(displayEditDistributorGroupTemplate,"context",context);
				
			}else if (action.equalsIgnoreCase(MANAGE_DISTRIBUTORGROUP_DELETE_ACTION)){
				
				String[] selectedDistributors = request.getParameterValues("selectedDistributors");
				Long distributorIdArray[] = new Long[selectedDistributors.length];
				if( selectedDistributors != null ){
					for( int counter=0; counter<selectedDistributors.length; counter++ ) {
						String distributorID = selectedDistributors[counter];
						if( StringUtils.isNotBlank(distributorID) ) {
							distributorIdArray[counter]=Long.parseLong(distributorID);
						}	
					}
					distributorService.assignDistributorsInDistributorGroup(Long.parseLong(distributorGroupID), distributorIdArray);
					return new ModelAndView(manageDistributorGroupTemplate);
				}
				distributors=distributorService.findDistributorsByName("", loggedInUser,false,-1,-1,new ResultSet(),"name",0);
				for(int dist_no = 0; dist_no<distributorIdArray.length; dist_no++) {
					Distributor selectedDistributor = distributorService.getDistributorById(distributorIdArray[dist_no]);
					for(Distributor searchedDistributor : distributors) {
						
						if(selectedDistributor.getId() == searchedDistributor.getId()) {
							previouslySelectedDistributors.add(selectedDistributor);
						}
					}
				}
				context.put("previouslySelectedDistributors", previouslySelectedDistributors);
				context.put("distributors", distributors);
				context.put("firstTime", false);
				return new ModelAndView(displayEditDistributorGroupTemplate,"context",context);
				
			} else {
				context.put("distributors", distributorGroup.getDistributors());
			}
		} catch (Exception e) {
			log.debug("exception", e);
		}		
		return new ModelAndView(displayEditDistributorGroupTemplate,"context",context);
	}	
	
	public ModelAndView manageDistributorGroups(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0027");
				
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//VU360UserAuthenticationDetails.getCurrentUser(); 
			
			Map<Object, Object> context = new HashMap<Object, Object>();
		
			String action = request.getParameter("action");
			List<DistributorGroup> distributorGroups=null;
			action = (action == null)?MANAGE_DISTRIBUTORGROUP_DEFAULT_ACTION:action;
		
			if (action.equalsIgnoreCase(MANAGE_DISTRIBUTORGROUP_SEARCH_ACTION)){
				
				String distributorGroupName = request.getParameter("distributorGroupName");
				distributorGroups=distributorService.findDistributorGroupsByName(distributorGroupName, loggedInUser, false);
				context.put("distributorGroups", distributorGroups);
				return new ModelAndView(manageDistributorGroupTemplate,"context",context);
				
				
			}else if (action.equalsIgnoreCase(MANAGE_DISTRIBUTORGROUP_ALLDISTRIBUTORS_ACTION)){
				
				distributorGroups=distributorService.findDistributorGroupsByName("", loggedInUser, false);
				context.put("distributorGroups", distributorGroups);
				return new ModelAndView(manageDistributorGroupTemplate,"context",context);
				
			}else if (action.equalsIgnoreCase(MANAGE_DISTRIBUTORGROUP_DELETE_ACTION)){
				
				String[] selectedDistributorGroups = request.getParameterValues("selectedDistributorGroups");
				Long distributorGroupIdArray[] = new Long[selectedDistributorGroups.length];
				if( selectedDistributorGroups != null ){
					for( int counter=0; counter<selectedDistributorGroups.length; counter++ ) {
						String distributorGroupID = selectedDistributorGroups[counter];
						if( StringUtils.isNotBlank(distributorGroupID)) {
							distributorGroupIdArray[counter]=Long.parseLong(distributorGroupID);
						}	
					}
					distributorService.deleteDistributorGroups(distributorGroupIdArray);
				}
				String distributorGroupName = request.getParameter("distributorGroupName");
				if( distributorGroupName == null )
					distributorGroupName = "";
				distributorGroups=distributorService.findDistributorGroupsByName(distributorGroupName, loggedInUser, false);
				context.put("distributorGroups", distributorGroups);
				return new ModelAndView(manageDistributorGroupTemplate,"context",context);
			} else {
				request.setAttribute("newPage","true");
			}
		} catch (Exception e) {
			log.debug("exception", e);
		}		
		return new ModelAndView(manageDistributorGroupTemplate);
	}	
private boolean validateData(HttpServletRequest request,Map context,com.softech.vu360.lms.vo.VU360User loggedInUser){
		
		
		if (StringUtils.isBlank(request.getParameter("distributorGroupName")))
		{
			//errors.rejectValue("emailAddress", "error.email.required");
			context.put("validatedistributorGroupName", "error.distributorGroupName.required");
			
			
			return false;
		}else if (FieldsValidation.isInValidGlobalName(request.getParameter("distributorGroupName"))){
		//	errors.rejectValue("distributorGroupName", "error.all.invalidText");
			context.put("validatedistributorGroupName", "error.all.invalidText");
			
			
			return false;
		}
		
		if (distributorService.findDistributorGroupsByName(request.getParameter("distributorGroupName"), loggedInUser,true).size()>0){
			 
			 context.put("validatedistributorGroupName", "error.distributorGroupName.existDistributorGroupName");
				
			 return false;
		 }

			
		
		return true;
	}



	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}
	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public String getDisplayEditDistributorGroupName() {
		return displayEditDistributorGroupNameTemplate;
	}

	public void setDisplayEditDistributorGroupName(
			String displayEditDistributorGroupName) {
		this.displayEditDistributorGroupNameTemplate = displayEditDistributorGroupName;
	}

	public String getDisplayEditDistributorGroupTemplate() {
		return displayEditDistributorGroupTemplate;
	}

	public void setDisplayEditDistributorGroupTemplate(
			String displayEditDistributorGroupTemplate) {
		this.displayEditDistributorGroupTemplate = displayEditDistributorGroupTemplate;
	}

	public String getManageDistributorGroupTemplate() {
		return manageDistributorGroupTemplate;
	}

	public void setManageDistributorGroupTemplate(
			String manageDistributorGroupTemplate) {
		this.manageDistributorGroupTemplate = manageDistributorGroupTemplate;
	}

	public String getDisplayEditDistributorGroupNameTemplate() {
		return displayEditDistributorGroupNameTemplate;
	}

	public void setDisplayEditDistributorGroupNameTemplate(
			String displayEditDistributorGroupNameTemplate) {
		this.displayEditDistributorGroupNameTemplate = displayEditDistributorGroupNameTemplate;
	}
}