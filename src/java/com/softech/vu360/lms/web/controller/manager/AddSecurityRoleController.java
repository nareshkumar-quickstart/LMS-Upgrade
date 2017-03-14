
package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSProducts;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LMSProductPurchaseService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.CustomerUtil;
import com.softech.vu360.lms.web.controller.model.LMSFeaturePermissionForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;

/**
 * The controller for the manage security role display and edit .
 *
 * @author Parthasarathi
 * @date 13-01-09
 *
 */

public class AddSecurityRoleController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(AddSecurityRoleController.class.getName());
	private LearnerService learnerService;
	private static final String MANAGE_ROLE_CANCEL_ACTION = "cancel";
	private static final String MANAGE_ROLE_ONCHANGE_ACTION = "onChange";
	private VU360UserService vu360UserService;
	private SecurityAndRolesService securityAndRolesService;
	private String addSecurityRoleTemplate = null;
	private String editSecurityRoleTemplate = null;
	private String manageSecurityRolesTemplate = null;
	private String manageSecurityRoleRedirectTemplate = null;
	private LMSProductPurchaseService lmsProductPurchaseService;

	private Comparator<LMSRoleLMSFeature> lmsFeaturePermissionComparator;

	/**
	 * added by Parthasarathi
	 * this method show Add Security Role and add new Role data to database
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */			

	public ModelAndView displayAddSecurityRole(HttpServletRequest request, HttpServletResponse response) {
		try {
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			Customer customer = null;

			if( vu360UserService.hasAdministratorRole(loggedInUser) ) {
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
						getAuthentication().getDetails()).getCurrentCustomer();
			} else {
				customer = loggedInUser.getLearner().getCustomer();
			}
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<Object,Object> roles=new HashMap<Object,Object>();
			Map<Object,Object> member=new HashMap<Object,Object>();
			Map<String,String> roleTypeForCombo=getListOfAvailableRoles(customer);
			String selectedType=getDefaultSelection(roleTypeForCombo);
			
			String action = request.getParameter("action");
			String roleName = request.getParameter("txtRoleName");
			String roleType_p = request.getParameter("rdRoleType");
			String defaultReg = request.getParameter("rdDefaultReg");

			action = (action==null)?MANAGE_ROLE_ONCHANGE_ACTION:action;
			roleName = (roleName==null)?"":roleName;
			roleType_p = (roleType_p==null)?selectedType:roleType_p;
			defaultReg = (defaultReg==null)?"false":defaultReg;

			List<LMSRoleLMSFeature> permissionList=null;
			LMSRole objRole = new LMSRole();
			List<LMSRole> roleList=new ArrayList<LMSRole>();
			log.debug("   action  " + action);

			if(action.equalsIgnoreCase("Save")){		
				if(!this.validateData(roleName,context,customer,Boolean.parseBoolean(defaultReg),roleType_p)){
					context.put("vu360User", loggedInUser);
					roleList.clear();
					roleList=filterRoles(roleType_p,customer);
					List<LMSRoleLMSFeature> permissions=roleList.get(0).getLmsPermissions();//getLMSPermissions(roleType_p,objRole);
					permissionList = managePermissions(request,loggedInUser,roleType_p,objRole,permissions);
					Collections.sort(permissionList, lmsFeaturePermissionComparator);
					roleList.get(0).setRoleType(roleType_p);
					roleList.get(0).setLmsPermissions(permissionList);
					roleList.get(0).setDefaultForRegistration(Boolean.parseBoolean(defaultReg));
					roleList.get(0).setRoleName(roleName);
					selectedType=roleType_p;
					context.put("roles", roleList);
					context.put("roleTypeForCombo", roleTypeForCombo);
					context.put("selectedType", selectedType);
					context.put("permisionForm", new LMSFeaturePermissionForm(roleList.get(0).getLmsPermissions()));
					return new ModelAndView(addSecurityRoleTemplate, "context", context);
				}

				roleList=filterRoles(roleType_p, customer);
				List<LMSRoleLMSFeature> permissions=roleList.get(0).getLmsPermissions();//getLMSPermissions(roleType_p,objRole);
				permissionList = managePermissions(request,loggedInUser,roleType_p,objRole,permissions);
				Collections.sort(permissionList, lmsFeaturePermissionComparator);
				log.info("permissionList.size = " + permissionList.size());
				objRole.setRoleName(roleName);
				objRole.setRoleType(roleType_p);
				objRole.setLmsPermissions(permissionList);
				objRole.setOwner(customer);

				if(defaultReg.equalsIgnoreCase("false")){
					objRole.setDefaultForRegistration(false);
				}
				else if(defaultReg.equalsIgnoreCase("true")){
					objRole.setDefaultForRegistration(true);
				}
				log.debug("Role Name  " + objRole.getRoleName());
				log.debug("Role ID  " + objRole.getId());
				objRole=	learnerService.addRole(objRole,customer);

				roleList=filterRoles(roleType_p,customer); 
				selectedType=roleType_p;
				return new ModelAndView(manageSecurityRoleRedirectTemplate);
			}else if(action.equalsIgnoreCase(MANAGE_ROLE_CANCEL_ACTION)){
				com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				// Start - following code is not being called from any where from the application
				// Mention it that following lines of code generates lot of extra/irrelevant queries as well
				List<LMSRole> allUserRoles = vu360UserService.getAllRoles(customer,user);
				for(LMSRole userRole :allUserRoles){
					int Count = (learnerService.getMembersByRole(userRole) == null) ? 0:learnerService.getMembersByRole(userRole).size();
					roles.put(userRole,userRole);
					member.put(userRole, Count);
				}
				context.put("roles", roles);
				context.put("member", member);
				// End ---------------------------------------------------------------------------------
				return new ModelAndView(manageSecurityRolesTemplate, "context", context);

			}
			else if(action.equalsIgnoreCase(MANAGE_ROLE_ONCHANGE_ACTION)){
				roleList.clear();
				roleList=filterRoles(roleType_p,customer); 
				selectedType=roleType_p;
			}
			context.put("roleName", roleName);
			context.put("roles", roleList);
			context.put("roleTypeForCombo", roleTypeForCombo);
			context.put("selectedType", selectedType);
			List<LMSRoleLMSFeature> lmsRoleLMSFeatures;
			if(roleList.size() <= 0)
				lmsRoleLMSFeatures = new ArrayList<LMSRoleLMSFeature>(0);
			else
				lmsRoleLMSFeatures = roleList.get(0).getLmsPermissions();
			context.put("permisionForm", new LMSFeaturePermissionForm(lmsRoleLMSFeatures));
			return new ModelAndView(addSecurityRoleTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}		
		return new ModelAndView(addSecurityRoleTemplate);
	}
	
	private String getDefaultSelection(Map<String,String> roleTypes){
		Iterator<String> itr=roleTypes.values().iterator();
		String roleType=null;
		while(itr.hasNext()){
			roleType=itr.next();
			if(!StringUtils.isBlank(roleType))
				break;
		}
		return roleType;
	}
	
	private HashMap<String,String> getListOfAvailableRoles(Customer customer)
	{

		HashMap<String,String> roleTypeForCombo = new HashMap<String,String>();
		if(getMode().equalsIgnoreCase(VU360UserMode.ROLE_LMSADMINISTRATOR.toString())){
			HashSet<String> roleTypes=securityAndRolesService.getTemplateTypesByDistributor(customer.getDistributor());
			Iterator<String> itr = roleTypes.iterator();
			String roleType=null;
			while(itr.hasNext())
			{
				roleType=itr.next();
				if(!roleType.equals(LMSRole.ROLE_PROCTOR))// TODO adding check temporary.
					roleTypeForCombo.put(roleType, roleType);
			}
		}else{
			List<LMSRole> availableRoles=securityAndRolesService.getUniqueRolesByCustomer(customer);
			com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			for(LMSRole userRole :availableRoles){
				if(loggedInUserVO.isAdminMode() && ((VU360UserAuthenticationDetails)auth.getDetails()).getCurrentMode().toString().equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)){
					if (userRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_INSTRUCTOR)) {
						roleTypeForCombo.put(userRole.getKey(), userRole.getRoleType());
					}
				}
				else if(!CustomerUtil.checkB2BCustomer(auth) && userRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_INSTRUCTOR)){
					VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
					LMSProducts lmsProducts = lmsProductPurchaseService.findLMSProductsPurchasedByCustomer(LMSProducts.WEBINAR_AND_WEBLINK_COURSES, details.getCurrentCustomerId());
					if(lmsProducts!=null && lmsProducts.getId()!=null){
							roleTypeForCombo.put(userRole.getKey(), userRole.getRoleType());
					}
				}
				
				if(userRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)) {  
					roleTypeForCombo.put(userRole.getKey(), userRole.ROLE_LEARNER);
				}else if(userRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)){
					roleTypeForCombo.put(userRole.getKey(), userRole.getRoleType());
				}
			}
		}
		return roleTypeForCombo;
	}
	
	private  List<LMSRoleLMSFeature> managePermissions(HttpServletRequest request,VU360User loggedInUser,String roleType_p,LMSRole objRole,List<LMSRoleLMSFeature> permissions){
		Enumeration<String> paramNames = request.getParameterNames();
		List<LMSRoleLMSFeature> permissionList=new ArrayList<LMSRoleLMSFeature>();
		LMSFeature objFeature=null;
		while(paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if(paramName.startsWith("prd")){	//All Feature radio Buttons starts with 'prd' 				
				log.debug("paramName   " + paramName.substring(3));
				log.debug("paramValues   " + paramValues[0]);
				LMSRoleLMSFeature objPermission =  new LMSRoleLMSFeature();
				objPermission.setLmsRole(objRole);
				for(LMSRoleLMSFeature per:permissions){
					if(per.getLmsFeature().getFeatureCode().equalsIgnoreCase(paramName.substring(3))){
						objFeature=	per.getLmsFeature();
						break;
					}
				}

				objPermission.setLmsFeature(objFeature);
				if((objFeature.getLmsMode().equalsIgnoreCase("learner"))
						&& (roleType_p.equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER))
						&& (roleType_p.equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR))){
					objPermission.setEnabled(Boolean.TRUE);
				}
				else if(paramValues[0].equalsIgnoreCase("false")){
					objPermission.setEnabled(Boolean.FALSE);
				}
				else if(paramValues[0].equalsIgnoreCase("true")){
					objPermission.setEnabled(Boolean.TRUE);

				}
				permissionList.add(objPermission);
			}
			
			
			//Disable check boxes are not pulling, thats why used hidden field for Disable check boxes		
			if(paramName.startsWith("hid_prd")){			
				if(paramValues[0].equalsIgnoreCase("true")){
					LMSRoleLMSFeature objPermission =  new LMSRoleLMSFeature();
					objPermission.setLmsRole(objRole);
					objPermission.setEnabled(Boolean.FALSE);

					for(LMSRoleLMSFeature per:permissions){
						if(per.getLmsFeature().getFeatureCode().equalsIgnoreCase(paramName.substring(7))){
							objFeature=	per.getLmsFeature();
							break;
						}
					}
					if(objFeature!=null){
						objPermission.setLmsFeature(objFeature);
						permissionList.add(objPermission);
					}
				}
			}
		}
		return permissionList;
	}
	
	private List<LMSRole> filterRoles(String roleType_p,Customer customer){
		List<LMSRole> roleList = new ArrayList<LMSRole>();
		if(roleType_p.equalsIgnoreCase(LMSRole.ROLE_LEARNER)==true){
			LMSRole newLMSRole= new LMSRole();
			newLMSRole.setOwner(customer);
			newLMSRole.setRoleName("LEARNER");
			newLMSRole.setDefaultForRegistration(true);
			newLMSRole.setRoleType(LMSRole.ROLE_LEARNER);
			List<LMSRoleLMSFeature>lmsPermissions = getLMSPermissions(LMSRole.ROLE_LEARNER,newLMSRole,customer);
			Collections.sort(lmsPermissions,lmsFeaturePermissionComparator);
			newLMSRole.setLmsPermissions(lmsPermissions);
			roleList.add(newLMSRole);
		}else if(roleType_p.equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)==true){
			LMSRole newLMSRole= new LMSRole();
			newLMSRole.setOwner(customer);
			newLMSRole.setRoleName("MANAGER");
			newLMSRole.setDefaultForRegistration(true);
			newLMSRole.setRoleType(LMSRole.ROLE_TRAININGMANAGER);
			List<LMSRoleLMSFeature> lmsPermissions = getLMSPermissions(LMSRole.ROLE_TRAININGMANAGER,newLMSRole,customer);
			Collections.sort(lmsPermissions,lmsFeaturePermissionComparator);
			newLMSRole.setLmsPermissions(lmsPermissions);
			roleList.add(newLMSRole);
		}else if(roleType_p.equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)==true){
			LMSRole newLMSRole= new LMSRole();
			newLMSRole.setOwner(customer);
			newLMSRole.setRoleName("ADMIN");
			newLMSRole.setDefaultForRegistration(true);
			newLMSRole.setRoleType(LMSRole.ROLE_LMSADMINISTRATOR);
			List<LMSRoleLMSFeature>lmsPermissions = getLMSPermissions(LMSRole.ROLE_LMSADMINISTRATOR,newLMSRole,customer);
			Collections.sort(lmsPermissions,lmsFeaturePermissionComparator);
			newLMSRole.setLmsPermissions(lmsPermissions);
			roleList.add(newLMSRole);
		}else if(roleType_p.equalsIgnoreCase(LMSRole.ROLE_REGULATORYANALYST)==true){
			LMSRole newLMSRole= new LMSRole();
			newLMSRole.setOwner(customer);
			newLMSRole.setRoleName("ACCREDITATION");
			newLMSRole.setDefaultForRegistration(true);
			newLMSRole.setRoleType(LMSRole.ROLE_REGULATORYANALYST);
			List<LMSRoleLMSFeature>lmsPermissions = getLMSPermissions(LMSRole.ROLE_REGULATORYANALYST,newLMSRole,customer);
			Collections.sort(lmsPermissions,lmsFeaturePermissionComparator);
			newLMSRole.setLmsPermissions(lmsPermissions);
			roleList.add(newLMSRole);
		}else if(roleType_p.equalsIgnoreCase(LMSRole.ROLE_INSTRUCTOR)==true){
			LMSRole newLMSRole= new LMSRole();
			newLMSRole.setOwner(customer);
			newLMSRole.setRoleName("INSTRUCTOR");
			newLMSRole.setDefaultForRegistration(true);
			newLMSRole.setRoleType(LMSRole.ROLE_INSTRUCTOR);
			List<LMSRoleLMSFeature>lmsPermissions = getLMSPermissions(LMSRole.ROLE_INSTRUCTOR,newLMSRole,customer);
			Collections.sort(lmsPermissions,lmsFeaturePermissionComparator);
			newLMSRole.setLmsPermissions(lmsPermissions);
			roleList.add(newLMSRole);
		}
		else if(roleType_p.equalsIgnoreCase(LMSRole.ROLE_PROCTOR)==true){
			LMSRole newLMSRole= new LMSRole();
			newLMSRole.setOwner(customer);
			newLMSRole.setRoleName("PROCTOR");
			newLMSRole.setDefaultForRegistration(true);
			newLMSRole.setRoleType(LMSRole.ROLE_PROCTOR);
			List<LMSRoleLMSFeature>lmsPermissions = getLMSPermissions(LMSRole.ROLE_PROCTOR,newLMSRole,customer);
			Collections.sort(lmsPermissions,lmsFeaturePermissionComparator);
			newLMSRole.setLmsPermissions(lmsPermissions);
			roleList.add(newLMSRole);
		}
		return roleList;
	}
	
	private List<LMSRoleLMSFeature> getLMSPermissions(String roleType,LMSRole lmsRole, Customer customer){
		log.info("roleType = " + roleType);
		log.info("role (id:name) = " + lmsRole.getId() + " : " + lmsRole.getRoleName());
		log.info("customer (id:name) = " + customer.getId() + " : " + customer.getName());
		List<LMSFeature> lmsFeatures = vu360UserService.getFeaturesByRoleType(roleType);
		List<LMSRoleLMSFeature> lmsPermissions = new ArrayList<LMSRoleLMSFeature>();
		List<CustomerLMSFeature> disabledCustomerFeatures = securityAndRolesService.getDisabledCustomerLMSFeatures(customer.getId());
		List<DistributorLMSFeature> disabledDistributorFeatures = securityAndRolesService.getDisabledDistributorLMSFeatures(customer.getDistributor().getId());
		// LMS-15470 - if some permissions are not available in 'CUSTOMERLMSFEATURE' table for customer, so, if they show in 'Manage Security Roles', they must be disable and read-only.
		List<LMSFeature> objMissingLMSFeatures = securityAndRolesService.getMissingCustomerLMSFeatures(customer, roleType);
		Set<Long> disabledFeatureIds = new HashSet<Long>();
		if(disabledDistributorFeatures != null && disabledDistributorFeatures.size() > 0){
			log.info("disabled distributor features = " + disabledCustomerFeatures.size());
			for(DistributorLMSFeature distFeature : disabledDistributorFeatures){
				log.info("\t" + distFeature.getLmsFeature().getFeatureCode() + " : " + distFeature.getLmsFeature().getFeatureName());
				disabledFeatureIds.add(distFeature.getLmsFeature().getId());
			}
		}else{
			log.info("disabled distributor features = 0");
		}
		
		if(disabledCustomerFeatures != null && disabledCustomerFeatures.size() > 0){
			log.info("disabled customer features = " + disabledCustomerFeatures.size());
			for(CustomerLMSFeature custFeature : disabledCustomerFeatures){
				log.info("\t" + custFeature.getLmsFeature().getFeatureCode() + " : " + custFeature.getLmsFeature().getFeatureName());
				disabledFeatureIds.add(custFeature.getLmsFeature().getId());
			}
		}else{
			log.info("disabled customer features = 0");
		}
		
		if(objMissingLMSFeatures != null && objMissingLMSFeatures.size() > 0){
			log.info("Missing customer features = " + objMissingLMSFeatures.size());
			for(LMSFeature missingFeature : objMissingLMSFeatures){
				log.info("\t" + missingFeature.getFeatureCode() + " : " + missingFeature.getFeatureName());
				disabledFeatureIds.add(missingFeature.getId());
			}
		}else{
			log.info("Missing customer features = 0");
		}
		
		log.info("features....");
		for (LMSFeature lmsFeature: lmsFeatures){		
			LMSRoleLMSFeature lmsPermission = new LMSRoleLMSFeature();
			lmsPermission.setLmsFeature(lmsFeature);
			lmsPermission.setLmsRole(lmsRole);
			lmsPermissions.add(lmsPermission);
			if(disabledFeatureIds.contains(lmsFeature.getId())){
				log.info("\t Disabling feature>>" + lmsFeature.getFeatureName());
				lmsPermission.setEnabled(Boolean.FALSE);
				lmsPermission.setLocked(Boolean.TRUE);
			}else{
				log.info("\t" + lmsFeature.getFeatureName());
				lmsPermission.setEnabled(Boolean.TRUE);
			}
		}
		return lmsPermissions;
	}

	private boolean validateData(String roleName,Map<Object, Object> context,Customer customer,boolean isDefaultForRegistration,String roleType_p ){
		if (StringUtils.isBlank(roleName))
		{
			context.put("validateRoleName", "error.roleName.required");
			return false;
		}
		LMSRole lmsRole=vu360UserService.getRoleByName(roleName,customer);
		if (lmsRole !=null){
			context.put("validateRoleName", "error.roleName.exists");
			return false;
		}
		return true;
	}
	
	private boolean validateDataForEdit(HttpServletRequest request ,List<LMSRoleLMSFeature> lmspermissions,String roleName,Map<Object, Object> context,Customer customer,long id,boolean isDefaultForRegistration,String roleType_p){
		if (StringUtils.isBlank(roleName)){
			context.put("validateRoleName", "error.roleName.required");
			return false;
		}
		LMSRole lmsRole=vu360UserService.getRoleByName(roleName,customer);
		if (lmsRole != null){
			if (lmsRole.getId() !=id){
				context.put("validateRoleName", "error.roleName.exists");
				return false;
			}
		}
		return true;
	}

	/**
	 * added by Parthasarathi Adhikary
	 * this method show role details and permission from db by specifying a roleID
	 * @param request
	 * @param response
	 * @return ModelAndView object 
	 */	
	public ModelAndView displayEditSecurityRole(HttpServletRequest request, HttpServletResponse response) {
		try {
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();

			Customer customer = null;
			if( vu360UserService.hasAdministratorRole(loggedInUser) ) {
				customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			} else {
				customer = loggedInUser.getLearner().getCustomer();
			}
			String selectedType=LMSRole.ROLE_LEARNER;
			String action = request.getParameter("action");
			String modifiedRoleID = request.getParameter("roleID");
			String roleName = request.getParameter("txtRoleName");
			String roleType_p = request.getParameter("rdRoleType");
			String defaultReg = request.getParameter("rdDefaultReg");

			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String,String> roleType=new HashMap<String,String>();
			List<LMSRoleLMSFeature> rolePermissions = new ArrayList<LMSRoleLMSFeature>();

			action = (action==null)?"Default":action;			
			modifiedRoleID = (modifiedRoleID==null)?"0":modifiedRoleID;
			roleName = (roleName==null)?"":roleName;
			roleType_p = (roleType_p==null)?LMSRole.ROLE_LEARNER:roleType_p;
			defaultReg = (defaultReg==null)?"false":defaultReg;
			log.debug("modifiedRoleID:"+modifiedRoleID);
	
			LMSRole modifiedRole=learnerService.loadForUpdateLMSRole(Long.valueOf(modifiedRoleID));
			roleType.put(modifiedRole.getKey(), modifiedRole.getRoleType());
			//system created role should not editable
			//System Create role should now be editable : LMS-10107
			//if(modifiedRole.isSystemCreated())
				//return new ModelAndView(manageSecurityRoleRedirectTemplate);
			
			if (modifiedRole !=null){				
				List<LMSRoleLMSFeature> masterFeatureList = getLMSPermissions(modifiedRole.getRoleType(), modifiedRole, customer);
				rolePermissions = modifiedRole.getLmsPermissions();
				Set<Long> masterFeatureSet = new HashSet<Long>();
				for(LMSRoleLMSFeature lmsRoleLMSFeature : masterFeatureList){
					if(!lmsRoleLMSFeature.isLocked()){
						masterFeatureSet.add(lmsRoleLMSFeature.getLmsFeature().getId());
					}
				}
				
				for(LMSRoleLMSFeature lmsRoleLMSFeature : rolePermissions){
					if(!masterFeatureSet.contains(lmsRoleLMSFeature.getLmsFeature().getId())){
						log.info("\t" + lmsRoleLMSFeature.getLmsFeature().getFeatureName());
						lmsRoleLMSFeature.setLocked(Boolean.TRUE);
					}
				}
				log.info("unlocking...");
				Set<Long> roleFeatureSet = new HashSet<Long>();
				for(LMSRoleLMSFeature lmsRoleLMSFeature : rolePermissions){
					roleFeatureSet.add(lmsRoleLMSFeature.getLmsFeature().getId());
				}
				
				for(LMSRoleLMSFeature lmsRoleLMSFeature : masterFeatureList){
					if(!roleFeatureSet.contains(lmsRoleLMSFeature.getLmsFeature().getId()))
					{
						if(masterFeatureSet.contains(lmsRoleLMSFeature.getLmsFeature().getId()))
						{
							log.info("\t" + lmsRoleLMSFeature.getLmsFeature().getFeatureName());
							lmsRoleLMSFeature.setEnabled(true);
							lmsRoleLMSFeature.setLocked(false);
						}
						rolePermissions.add(lmsRoleLMSFeature);
					}
				}
				Collections.sort(rolePermissions, lmsFeaturePermissionComparator);
			}
			
			List<LMSRoleLMSFeature> finalPermissions= new ArrayList<LMSRoleLMSFeature>();
			Map<Object,Object> roles=new HashMap<Object,Object>();
			Map<Object,Object> member=new HashMap<Object,Object>();

			if(action.equalsIgnoreCase("Default")){
				finalPermissions=rolePermissions;
				roleType_p=modifiedRole.getRoleType();
				selectedType=roleType_p;
				log.debug(modifiedRole.getRoleName());
			}else if(action.equalsIgnoreCase("Save")){
				if(!this.validateDataForEdit(request, rolePermissions,roleName,context,customer,modifiedRole.getId(),Boolean.parseBoolean(defaultReg),modifiedRole.getRoleType())){
					context.put("vu360User", loggedInUser);

					finalPermissions=rolePermissions;
					modifiedRole.setLmsPermissions(finalPermissions);

					rolePermissions = managePermissions(request,loggedInUser,roleType_p,modifiedRole,finalPermissions);
					Collections.sort(rolePermissions,lmsFeaturePermissionComparator);

					modifiedRole.setDefaultForRegistration(Boolean.parseBoolean(defaultReg));
					modifiedRole.setRoleName(roleName);

					modifiedRole.setRoleType(modifiedRole.getRoleType());
					context.put("roleToEdit", modifiedRole);
					context.put("roleType", modifiedRole.getRoleType());
					context.put("selectedType", modifiedRole.getRoleType());
					context.put("finalPermissions",finalPermissions);
					context.put("permisionForm", new LMSFeaturePermissionForm(modifiedRole.getLmsPermissions()));

					return new ModelAndView(editSecurityRoleTemplate, "context", context);
				}
				
				modifiedRole.setRoleName(roleName);
				
				if(defaultReg.equalsIgnoreCase("false")){
					modifiedRole.setDefaultForRegistration(Boolean.FALSE);
				}
				else if(defaultReg.equalsIgnoreCase("true")){
					modifiedRole.setDefaultForRegistration(Boolean.TRUE);
				}

				finalPermissions=rolePermissions;
				List<LMSRoleLMSFeature> lmsPermissions = managePermissions(request,loggedInUser,roleType_p,modifiedRole,finalPermissions);
				Collections.sort(lmsPermissions, lmsFeaturePermissionComparator);
				modifiedRole.getLmsPermissions().clear();//Wajahat: Why is this required ??
				modifiedRole.getLmsPermissions().addAll(lmsPermissions);//Wajahat: Why is this required ??

				modifiedRole=learnerService.updateRole(modifiedRole,customer);///delete from LMSROLELMSFEATURE where id=?     insert into LMSROLELMSFEATURE (ENABLEDTF, LMSFEATURE_ID, LMSROLE_ID, id) values (?, ?, ?, ?)
//				List<LMSRole> allUserRoles = vu360UserService.getAllRoles(customer,loggedInUser);
//				for(LMSRole userRole :allUserRoles){
//					int Count = (learnerService.getMembersByRole(userRole) == null) ? 0:learnerService.getMembersByRole(userRole).size();
//					roles.put(userRole,userRole);
//					member.put(userRole, Count);
//				}
//				context.put("roles", roles);
//				context.put("member", member);
				return new ModelAndView(manageSecurityRoleRedirectTemplate);


			}//End Save
			else if(action.equalsIgnoreCase(MANAGE_ROLE_CANCEL_ACTION)){
				com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				// Start - following code is not being called from any where from the application
				// Mention it that following lines of code generates lot of extra/irrelevant queries as well
				List<LMSRole> allUserRoles = vu360UserService.getAllRoles(customer,user);
				for(LMSRole userRole :allUserRoles){
					int Count = (learnerService.getMembersByRole(userRole) == null) ? 0:learnerService.getMembersByRole(userRole).size();
					roles.put(userRole,userRole);
					member.put(userRole, Count);
				}
				context.put("roles", roles);
				context.put("member", member);
				// ----- end
				return new ModelAndView(manageSecurityRolesTemplate, "context", context);

			}else if(action.equalsIgnoreCase(MANAGE_ROLE_ONCHANGE_ACTION)){
				List<LMSRole> roleList=new ArrayList<LMSRole>();
				roleList.clear();
				roleList=filterRoles(roleType_p,customer);
				selectedType=roleType_p;
				rolePermissions = roleList.get(0).getLmsPermissions();
				Collections.sort(rolePermissions,lmsFeaturePermissionComparator);
				modifiedRole.setRoleType(selectedType);
			}
			context.put("selectedType", selectedType);
			context.put("roleToEdit", modifiedRole);
			context.put("roleType", roleType);
			context.put("finalPermissions",finalPermissions);
			context.put("permisionForm", new LMSFeaturePermissionForm(modifiedRole.getLmsPermissions()));
			return new ModelAndView(editSecurityRoleTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}		
		return new ModelAndView(editSecurityRoleTemplate);
	}	

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public void setAddSecurityRoleTemplate(String addSecurityRoleTemplate) {
		this.addSecurityRoleTemplate = addSecurityRoleTemplate;
	}

	public void setEditSecurityRoleTemplate(String editSecurityRoleTemplate) {
		this.editSecurityRoleTemplate = editSecurityRoleTemplate;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public void setManageSecurityRolesTemplate(String manageSecurityRolesTemplate) {
		this.manageSecurityRolesTemplate = manageSecurityRolesTemplate;
	}
	
	public void setManageSecurityRoleRedirectTemplate(
			String manageSecurityRoleRedirectTemplate) {
		this.manageSecurityRoleRedirectTemplate = manageSecurityRoleRedirectTemplate;
	}

	public void setSecurityAndRolesService(
			SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}
	
	public String getMode(){
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
		VU360UserMode currentMode = details.getCurrentMode();
		log.debug("currentMode="+currentMode.toString());
		return currentMode.toString();
	}

	public void setLmsFeaturePermissionComparator(Comparator<LMSRoleLMSFeature> lmsFeaturePermissionComparator){
		this.lmsFeaturePermissionComparator = lmsFeaturePermissionComparator;
	}

	public void setLmsProductPurchaseService(
			LMSProductPurchaseService lmsProductPurchaseService) {
		this.lmsProductPurchaseService = lmsProductPurchaseService;
	}
}
