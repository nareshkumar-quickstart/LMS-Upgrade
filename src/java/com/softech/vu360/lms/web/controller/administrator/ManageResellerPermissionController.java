package com.softech.vu360.lms.web.controller.administrator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.CommonControllerActions;
import com.softech.vu360.lms.web.controller.model.LMSFeaturePermissionForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.RedirectUtils;

public class ManageResellerPermissionController extends MultiActionController{

	
	private static final String MANAGE_ROLE_CANCEL_ACTION = "Cancel";
	private static final String MANAGE_ROLE_SAVE_ACTION = "Save";
	private static final Logger log = Logger.getLogger(ManageResellerPermissionController.class.getName());
	private AccreditationService accreditationService = null;
	private SecurityAndRolesService securityAndRolesService=null;
	private VU360UserService vu360UserService=null;
	private LearnerService learnerService=null;
	private DistributorService distributorService = null; 
	private VelocityEngine velocityEngine;
	private String redirectTemplate = null;
	private String showResellerPermissions=null;
	private String editSecurityRoleTemplate;
	
	public static final String ROLE_LEARNER = "ROLE_LEARNER";
	public static final String ROLE_TRAININGMANAGER = "ROLE_TRAININGADMINISTRATOR";
	public static final String ROLE_LMSADMINISTRATOR = "ROLE_LMSADMINISTRATOR";
	public static final String ROLE_REGULATORYANALYST = "ROLE_REGULATORYANALYST";
	public static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
	public static final String ROLE_PROCTOR = "ROLE_PROCTOR";
	
	/**
	 * lms feature permission comparator
	 */
	private Comparator<LMSRoleLMSFeature> lmsFeaturePermissionComparator;

	private  List<LMSRoleLMSFeature> managePermissions(HttpServletRequest request,com.softech.vu360.lms.vo.VU360User loggedInUser,String roleType_p,List<LMSRoleLMSFeature> permissions){

		Enumeration<String> paramNames = request.getParameterNames();
		List<LMSRoleLMSFeature> permissionList=new ArrayList<LMSRoleLMSFeature>();
		//LMSRole objRole = new LMSRole();
		LMSFeature objFeature=null;

		while(paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if(paramName.startsWith("prd")){	//All Feature radio Buttons starts with 'prd' 				
				log.debug("paramName   " + paramName.substring(3));
				log.debug("paramValues   " + paramValues[0]);
				LMSRoleLMSFeature objPermission =  new LMSRoleLMSFeature();
				//objPermission.setLmsRole(objRole);
//				//objFeature.setFeatureCode(paramName.substring(3));
				//for(LMSRole role:loggedInUser.getLmsRoles()){

					for(LMSRoleLMSFeature per:permissions){
						if(per.getLmsFeature().getFeatureCode().equalsIgnoreCase(paramName.substring(3))){
							objFeature=	per.getLmsFeature();
							objPermission.setId(per.getId());
						}
					}

				//}
				
				//objFeature.setRoleType(roleType_p);
				//objFeature.setFeatureGroup(featureGroup);
				
				objPermission.setLmsFeature(objFeature);
				if((objFeature.getLmsMode().equalsIgnoreCase("learner"))
						&& (roleType_p.equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER))
						&& (roleType_p.equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR))){
					objPermission.setEnabled(true);
				}
				else if(paramValues[0].equalsIgnoreCase("false")){
					objPermission.setEnabled(false);
				}
				else if(paramValues[0].equalsIgnoreCase("true")){
					objPermission.setEnabled(true);

				}

				permissionList.add(objPermission);
			}
		}
		return permissionList;

	}
	


	@SuppressWarnings("deprecation")
	public ModelAndView showResellerPermissionForm(HttpServletRequest request, HttpServletResponse response) {


		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0021");
			boolean resellerFeaturesenabled = false;
				
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			String roleType=request.getParameter("roleType");
			
			if(roleType != null || !StringUtils.isBlank(roleType)){
				roleType=getRoleType(Integer.parseInt(roleType));
			}
			else
				roleType=LMSRole.ROLE_LEARNER;
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Distributor distributor = null;
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				
				if( details.getCurrentDistributor() != null ){
					distributor = details.getCurrentDistributor();
				}
			}
			if(distributor!=null){

				String action = request.getParameter("action");
				action = (action==null) ? CommonControllerActions.DEFAULT.toString() : action;	
				
				Map<Object, Object> context = new HashMap<Object, Object>();
				
				if(action.equalsIgnoreCase(CommonControllerActions.CANCEL.toString())) {
					context.put("distributorId", request.getParameter("distributorId"));
					return new ModelAndView(redirectTemplate);
				}

				List<LMSRoleLMSFeature> finalPermissions = new ArrayList<LMSRoleLMSFeature>();
				finalPermissions = securityAndRolesService.getPermissionByDistributor(distributor, roleType);
				if(finalPermissions==null || finalPermissions.size()==0)
					finalPermissions = securityAndRolesService.getDefaultPermissionByDistributor(distributor, roleType);
				
				if(action.equalsIgnoreCase(MANAGE_ROLE_SAVE_ACTION)){
					List<LMSRoleLMSFeature>lmsPermissions = managePermissions(request,loggedInUser,roleType,finalPermissions);
					
					log.debug(request.getParameter("roleType"));
					
					if(request.getParameter("roleType").equals("1"))
					{
						for(LMSRoleLMSFeature per:lmsPermissions){
							log.debug("Permission Enabled :"+ per.getEnabled().toString() + "Permission Name: " + per.getLmsFeature().getFeatureCode().toString());
							if(per.getEnabled())
							resellerFeaturesenabled = per.getEnabled();
							}
						if(resellerFeaturesenabled)
						{
						   distributorService.updateFeaturesForDistributor(distributor, lmsPermissions);
						   finalPermissions=lmsPermissions;
						}
					}
					else
					{
						distributorService.updateFeaturesForDistributor(distributor, lmsPermissions);
						finalPermissions=lmsPermissions;
					}

				}//End Save
				
				List<LMSRoleLMSFeature> finalPermissionsRestrictedPermissionLocked = new ArrayList<LMSRoleLMSFeature>();
				List<String> restrictedFeatures = distributorService.getRestrictedFeatures();
				for(LMSRoleLMSFeature lmsRoleLMSFeature : finalPermissions){
					if(restrictedFeatures.contains(lmsRoleLMSFeature.getLmsFeature().getFeatureCode())){
						lmsRoleLMSFeature.setLocked(true);
					}
					finalPermissionsRestrictedPermissionLocked.add(lmsRoleLMSFeature);
				}
					
				
				Collections.sort(finalPermissionsRestrictedPermissionLocked, this.getLmsFeaturePermissionComparator());
				if(!resellerFeaturesenabled && request.getParameter("roleType").equals("1") && action.equalsIgnoreCase(MANAGE_ROLE_SAVE_ACTION))
				context.put("validateTermsofServices","error.customerpermission.activepermission");
				context.put("finalPermissions",finalPermissionsRestrictedPermissionLocked);
				context.put("permisionForm", new LMSFeaturePermissionForm(finalPermissionsRestrictedPermissionLocked));
				context.put("roleType",request.getParameter("roleType"));
				return new ModelAndView(showResellerPermissions, "context", context);

			}else{
				throw new IllegalArgumentException("Distributor not found");
			}	
		} catch (Exception e) {
			log.debug("exception", e);
			throw new IllegalArgumentException("Distributor not found" , e);
		}
	}	

	
	private String getRoleType(int roleType) {
		
		switch(roleType){
			case 0 : return ROLE_LEARNER;
			case 1 : return ROLE_TRAININGMANAGER;
			case 2 : return ROLE_INSTRUCTOR;
			case 3 : return ROLE_REGULATORYANALYST;
			case 4 : return ROLE_LMSADMINISTRATOR;
			case 5 : return ROLE_PROCTOR;
			default: return ROLE_LEARNER;
		}
	}


	
	
	public DistributorService getDistributorService() {
		return distributorService;
	}
	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	public SecurityAndRolesService getSecurityAndRolesService() {
		return securityAndRolesService;
	}
	public void setSecurityAndRolesService(
			SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	public LearnerService getLearnerService() {
		return learnerService;
	}
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}



	public String getShowResellerPermissions() {
		return showResellerPermissions;
	}
	public void setShowResellerPermissions(String showResellerPermissions) {
		this.showResellerPermissions = showResellerPermissions;
	}

	
	
	
	public String getMode()
	{
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
		VU360UserMode currentMode = details.getCurrentMode();
		log.debug("currentMode="+currentMode.toString());
		return currentMode.toString();
	}
	
	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}
	
	/**
	 * exception handler method for this multiaction
	 * to be used if the distributor could not be resolved
	 */
	public void handleDistributorNotFoundException(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException ex ) throws IOException{
		String url = "/adm_searchMember.do?method=displaySearchMemberView&isRedirect=d";
		RedirectUtils.sendRedirect(request, response, url, false);
	}

	/**
	 * Set lmsFeaturePermissionComparator
	 * @return lmsFeaturePermissionComparator
	 */
	public Comparator<LMSRoleLMSFeature> getLmsFeaturePermissionComparator() 
	{
		return this.lmsFeaturePermissionComparator;
	}

	/**
	 * Set lmsFeaturePermissionComparator
	 * @param lmsFeaturePermissionComparator
	 */
	public void setLmsFeaturePermissionComparator(
			Comparator lmsFeaturePermissionComparator
		) 
	{
		this.lmsFeaturePermissionComparator = lmsFeaturePermissionComparator;
	}
}
