package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSProducts;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LMSProductPurchaseService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageSecurityRoleForm;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;

/**
 * @author Dyutiman
 *
 */
public class ManageSecurityRolesController extends VU360BaseMultiActionController {

	private Logger log = Logger.getLogger(ManageSecurityRolesController.class.getName());

	private LearnerService learnerService = null;
	private ProctorService proctorService = null;
	private VU360UserService vu360UserService = null;
	private LMSProductPurchaseService lmsProductPurchaseService;
	private String failureTemplate = null;
	private String manageSecurityRolesTemplate = null;
	private String deleteSecurityRoleTemplate= null;
	private String proctorProfileTemplate= null;
	
	private String redirectTemplate = null;
	//TODO why is it here, can cause issue in concurrent users as we are using prototype injection in spring
	HttpSession session = null;

	public ManageSecurityRolesController() {
		super();
	}

	public ManageSecurityRolesController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod( NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		this.setFeatureInSession(request);
			
		//return editCustomerProfile( request, response );
		//return null;
		log.debug("IN handleNoSuchRequestHandlingMethod");
		request.setAttribute("newPage","true");
		return new ModelAndView(redirectTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {

		if( command instanceof ManageSecurityRoleForm ) {

			ManageSecurityRoleForm form = (ManageSecurityRoleForm)command;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = ((VU360UserAuthenticationDetails) auth.getDetails()).getCurrentCustomer();
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) auth.getPrincipal();
			List<LMSRole> allUserRoles = vu360UserService.getAllRoles(customer,user);

			form.setAllUserRoles(allUserRoles);
		}
	}

	/**
	 * Set feature based on mode of logged in user in session.
	 * @param session
	 * @author sm.humayun
	 * @since 4.14 {LMS-9971}
	 */
	private void setFeatureInSession (HttpSession session)
	{
		session.setAttribute("feature"
				, ((VU360UserAuthenticationDetails) SecurityContextHolder
						.getContext().getAuthentication().getDetails())
						.getCurrentMode().name().equals("ROLE_LMSADMINISTRATOR")  
				? "LMS-ADM-0011" : "LMS-MGR-0006");
	}

	/**
	 * Set feature based on mode of logged in user in session.
	 * @param request
	 * @author sm.humayun
	 * @since 4.14 {LMS-9971}
	 */
	private void setFeatureInSession (HttpServletRequest request)
	{
		this.setFeatureInSession(request.getSession(true));
	}

	@SuppressWarnings("unchecked")
	public ModelAndView showSecurityRoles( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {

			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			session = request.getSession(true);
			this.setFeatureInSession(session);

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
			VU360UserMode currentMode = details.getCurrentMode();
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			
			if(currentMode.toString().equalsIgnoreCase(com.softech.vu360.lms.web.filter.VU360UserMode.ROLE_LMSADMINISTRATOR.toString()))
			{
			  if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
					if( details.getCurrentCustomer() != null && ( details.getCurrentSearchType()==AdminSearchType.CUSTOMER || details.getCurrentSearchType()==AdminSearchType.DISTRIBUTOR )){
						session = request.getSession();	
					} else {
						return new ModelAndView(failureTemplate,"isRedirect","");
					}
					if( details.getCurrentDistributor() != null && ( details.getCurrentSearchType()==AdminSearchType.DISTRIBUTOR || details.getCurrentSearchType()==AdminSearchType.DISTRIBUTOR )){
						return new ModelAndView(failureTemplate,"isRedirect","c");
					}
				} else {
					// admin has not selected any customer
					return new ModelAndView(failureTemplate,"isRedirect","");
				}
			}
			String sortDirection = request.getParameter("sortDirection");
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			Map<Object, Object> context = new HashMap<Object, Object>();
			
			List<Map<String,Object>> members = new ArrayList <Map<String,Object>>();
			List<LMSRole> listOfRolls = new ArrayList <LMSRole>();
			String name = request.getParameter("name");
			context.put("name", name);
			
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";

			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
						
			listOfRolls = vu360UserService.findRolesByName(name, customer, loggedInUser);
			
			Long [] arrRoleIds = new Long[listOfRolls.size()];
			for(int i=0;i<listOfRolls.size(); i++){
					arrRoleIds[i] = listOfRolls.get(i).getId();
			}
				
			Map<String, String> lstLearnerCounted = learnerService.countLearnerByRoles(arrRoleIds);
			
			for(LMSRole userRole : listOfRolls){
				//adding this check, by OWS for LMS-4297 on 26th Jan 2010
				if(vu360UserService.hasAdministratorRole(loggedInUser) && ((VU360UserAuthenticationDetails)auth.getDetails()).getCurrentMode().toString().equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)){
				
					String count = "0";
					if(lstLearnerCounted.containsKey(userRole.getId().toString())){
						count = (String)lstLearnerCounted.get(userRole.getId().toString());
					}
					Map<String,Object> member = new HashMap <String,Object>();
					member.put("userRole", userRole);
					member.put("Count", count);
					//if(!userRole.getRoleType().equals(LMSRole.ROLE_PROCTOR))//TODO temporary code coz of ANSI improvement---making roles thru script
					//{
						members.add(member);
					//}
				}else if (userRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_PROCTOR)){
					String totalRecord = "0";
					
					Map<Object, Object> results = learnerService.getAllUsersInLmsRole(userRole,
							vu360UserService.hasAdministratorRole(loggedInUser), vu360UserService.hasTrainingAdministratorRole(loggedInUser), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), loggedInUser.getTrainingAdministrator().getManagedGroups(), 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
							-1, -1, "firstName", 0);
						
					if ( !results.isEmpty() ) {
						totalRecord = results.get("recordSize").toString();
					}
					Map<String,Object> member = new HashMap <String,Object>();
					
					member.put("userRole", userRole);
					member.put("Count", Integer.parseInt(totalRecord));
					members.add(member);
						
				}else{
					if(userRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER) 
							|| userRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER) 
							){
						String count = "0";
						if(lstLearnerCounted.containsKey(userRole.getId().toString())){
							count = (String)lstLearnerCounted.get(userRole.getId().toString());
						}
						
						Map<String,Object> member = new HashMap <String,Object>();
						member.put("userRole", userRole);
						member.put("Count", count);
						//if(!userRole.getRoleType().equals(LMSRole.ROLE_PROCTOR))//TODO temporary code coz of ANSI improvement---making roles thru script
						//{
							members.add(member);
						//}
					}
					//To show Instructor Role for Freemium ILT Upgrade User (LMS-16018)
					if(userRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_INSTRUCTOR)){
						if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
							Customer currentCustomer=details.getCurrentCustomer();
							if(currentCustomer.getCustomerType().equalsIgnoreCase("B2B")&& currentCustomer.getContentOwner()!=null && 
									(currentCustomer.getContentOwner().getPlanTypeId()==null || 
									(currentCustomer.getContentOwner().getPlanTypeId()!=null && currentCustomer.getContentOwner().getPlanTypeId()==2))){
								
								LMSProducts lmsProducts = lmsProductPurchaseService.findLMSProductsPurchasedByCustomer(LMSProducts.WEBINAR_AND_WEBLINK_COURSES, details.getCurrentCustomerId());
								if(lmsProducts!=null && lmsProducts.getId()!=null){
									int Count = (learnerService.getMembersByRole(userRole) == null) ? 0 : learnerService.getMembersByRole(userRole).size();
									Map<String,Object> member = new HashMap <String,Object>();
									member.put("userRole", userRole);
									member.put("Count", Count);
									members.add(member);
								}
							}
						}
					}
				
				}
			}
			context.put("showAll", showAll);
			context.put("sortDirection", 0);
			context.put("sortColumnIndex", 0);
			String pageIndex = request.getParameter("pageCurrIndex");
			if( pageIndex == null ) pageIndex = "0";
			
			Map PagerAttributeMap = new HashMap();
			PagerAttributeMap.put("pageIndex",pageIndex);
			if( listOfRolls.size() <= 10 ) {
				PagerAttributeMap.put("pageIndex", 0);
			}
			// manually sorting
			if( sortDirection != null && sortColumnIndex != null ) {
				// sorting against role name
				request.setAttribute("PagerAttributeMap", PagerAttributeMap);
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < members.size() ; i ++ ) {
							for( int j = 0 ; j < members.size() ; j ++ ) {
								if( i != j ) {
									LMSRole r1 =  (LMSRole) members.get(i).get("userRole");
									LMSRole r2 =  (LMSRole) members.get(j).get("userRole");
									if( r1.getRoleName().compareTo(r2.getRoleName()) > 0 ) {
										Map<String,Object> tempMap = members.get(i);
										members.set(i, members.get(j));
										members.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						for( int i = 0 ; i < members.size() ; i ++ ) {
							for( int j = 0 ; j < members.size() ; j ++ ) {
								if( i != j ) {
									LMSRole r1 =  (LMSRole) members.get(i).get("userRole");
									LMSRole r2 =  (LMSRole) members.get(j).get("userRole");
									if( r1.getRoleName().toUpperCase().compareTo(r2.getRoleName().toUpperCase()) < 0 ) {
										Map<String,Object> tempMap = members.get(i);
										members.set(i, members.get(j));
										members.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				// sorting against number of members
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < members.size() ; i ++ ) {
							for( int j = 0 ; j < members.size() ; j ++ ) {
								if( i != j ) {
									int count1 =  (Integer) members.get(i).get("Count");
									int count2 =  (Integer) members.get(j).get("Count");
									if( count1 > count2 ) {
										Map<String,Object> tempMap = members.get(i);
										members.set(i, members.get(j));
										members.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						for( int i = 0 ; i < members.size() ; i ++ ) {
							for( int j = 0 ; j < members.size() ; j ++ ) {
								if( i != j ) {
									int count1 =  (Integer) members.get(i).get("Count");
									int count2 =  (Integer) members.get(j).get("Count");
									if( count1 < count2 ) {
										Map<String,Object> tempMap = members.get(i);
										members.set(i, members.get(j));
										members.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				// sorting against role type	
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						for( int i = 0 ; i < members.size() ; i ++ ) {
							for( int j = 0 ; j < members.size() ; j ++ ) {
								if( i != j ) {
									LMSRole r1 =  (LMSRole) members.get(i).get("userRole");
									LMSRole r2 =  (LMSRole) members.get(j).get("userRole");
									if( r1.getRoleType().compareTo(r2.getRoleType()) < 0 ) {
										Map<String,Object> tempMap = members.get(i);
										members.set(i, members.get(j));
										members.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 2);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
					} else {
						for( int i = 0 ; i < members.size() ; i ++ ) {
							for( int j = 0 ; j < members.size() ; j ++ ) {
								if( i != j ) {
									LMSRole r1 =  (LMSRole) members.get(i).get("userRole");
									LMSRole r2 =  (LMSRole) members.get(j).get("userRole");
									if( r1.getRoleType().compareTo(r2.getRoleType()) > 0 ) {
										Map<String,Object> tempMap = members.get(i);
										members.set(i, members.get(j));
										members.set(j, tempMap);
									}
								}
							}
						}
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 2);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
					}
				}
			}else{
				request.setAttribute("newPage","true");
			}
			context.put("members", members);
			return new ModelAndView(manageSecurityRolesTemplate, "context", context);
			
		} catch (Exception e) {
			log.debug("exception", e);
		}		
		return new ModelAndView(manageSecurityRolesTemplate);
	}	
	
	public ModelAndView runDeletionWizard( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {

			com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			this.setFeatureInSession(request);
				
			ManageSecurityRoleForm form = (ManageSecurityRoleForm)command;			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentMode() == VU360UserMode.ROLE_LMSADMINISTRATOR ) {
					if( details.getCurrentSearchType() != AdminSearchType.CUSTOMER ) {
						return new ModelAndView(failureTemplate,"isRedirect","c");
					}
				}
			}
						
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			Map<Object, Object> context = new HashMap<Object, Object>();			
			/*If user is in administrator mode, run deletions directly and do not show wizard*/
			
			String[] selectedRolesValues = request.getParameterValues("rolescheck");
			Long roleIdArray[] = new Long[selectedRolesValues.length];			
			
			if( selectedRolesValues != null )
				for( int i = 0; i < selectedRolesValues.length ; i++ ) {
					String roleID = selectedRolesValues[i];
					if( roleID != null )
						roleIdArray[i]= Long.parseLong(selectedRolesValues[i]);						
				}									
			
			Map<String,Set<LMSRole>> learnerDataMap= this.getDataForRoleType(roleIdArray, LMSRole.ROLE_LEARNER, customer);
			Map<String,Set<LMSRole>> managerDataMap= this.getDataForRoleType(roleIdArray, LMSRole.ROLE_TRAININGMANAGER, customer);
			
			context.put("learnerRolesToBeDeleted",learnerDataMap.get("rolesToBeDeleted"));
			context.put("learnerRolesWithUsers"  ,learnerDataMap.get("rolesWithUsers"));
			context.put("alternateLearnerRoles"  ,learnerDataMap.get("alternateRoles"));
						
			context.put("managerRolesToBeDeleted",managerDataMap.get("rolesToBeDeleted"));
			context.put("managerRolesWithUsers"  ,managerDataMap.get("rolesWithUsers"));
			context.put("alternateManagerRoles"  ,managerDataMap.get("alternateRoles"));
			
			int userCount= learnerDataMap.get("rolesWithUsers").size() + managerDataMap.get("rolesWithUsers").size();
			
			if(loggedInUserVO.isAdminMode() && ((VU360UserAuthenticationDetails)auth.getDetails()).getCurrentMode().toString().equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)){
				Map<String,Set<LMSRole>> instructorDataMap= this.getDataForRoleType(roleIdArray, LMSRole.ROLE_INSTRUCTOR, customer);
				Map<String,Set<LMSRole>> adminDataMap= this.getDataForRoleType(roleIdArray, LMSRole.ROLE_LMSADMINISTRATOR, customer);
				Map<String,Set<LMSRole>> regulatoryDataMap= this.getDataForRoleType(roleIdArray, LMSRole.ROLE_REGULATORYANALYST, customer);
				
				context.put("instructorRolesToBeDeleted",instructorDataMap.get("rolesToBeDeleted"));
				context.put("instructorRolesWithUsers"  ,instructorDataMap.get("rolesWithUsers"));
				context.put("alternateInstructorRoles"  ,instructorDataMap.get("alternateRoles"));
				
				context.put("adminRolesToBeDeleted",adminDataMap.get("rolesToBeDeleted"));
				context.put("adminRolesWithUsers"  ,adminDataMap.get("rolesWithUsers"));
				context.put("alternateAdminRoles"  ,adminDataMap.get("alternateRoles"));
				
				context.put("regulatoryRolesToBeDeleted",regulatoryDataMap.get("rolesToBeDeleted"));
				context.put("regulatoryRolesWithUsers"  ,regulatoryDataMap.get("rolesWithUsers"));
				context.put("alternateRegulatoryRoles"  ,regulatoryDataMap.get("alternateRoles"));
				
				userCount+= instructorDataMap.get("rolesWithUsers").size() + adminDataMap.get("rolesWithUsers").size() +
				   regulatoryDataMap.get("rolesWithUsers").size(); 
			}
			
			  
						  
			
			/*don't show wizard if user count is zero*/
			if(userCount == 0){
				vu360UserService.deleteUserRole(roleIdArray);
								
				List<LMSRole> allRoles = vu360UserService.getRolesByRoleType(LMSRole.ROLE_LEARNER, customer.getId());
				allRoles.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_TRAININGMANAGER, customer.getId()));
				
				if(loggedInUserVO.isAdminMode() && ((VU360UserAuthenticationDetails)auth.getDetails()).getCurrentMode().toString().equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)){
					allRoles.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_INSTRUCTOR, customer.getId()));
					allRoles.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_LMSADMINISTRATOR, customer.getId()));
					allRoles.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_REGULATORYANALYST, customer.getId()));
				}
				
				List<Map<String,Object>> members = new ArrayList <Map<String,Object>>();
				form.setAllUserRoles(allRoles);
				
				Long [] arrRoleIds = new Long[allRoles.size()];
				for(int i=0;i<allRoles.size(); i++){
						arrRoleIds[i] = allRoles.get(i).getId();
				}
				Map<String, String> lstLearnerCounted = learnerService.countLearnerByRoles(arrRoleIds);
				
				for(LMSRole userRole : allRoles){
					String count = "0";
					if(lstLearnerCounted.containsKey(userRole.getId().toString())){
						count = (String)lstLearnerCounted.get(userRole.getId().toString());
					}
					Map<String,Object> member = new HashMap <String,Object>();
					member.put("userRole", userRole);
					member.put("Count", count);
					members.add(member);
				}
				context.put("members", members);
	
				return new ModelAndView(manageSecurityRolesTemplate, "context", context);				
			}			
			
						
			return new ModelAndView(deleteSecurityRoleTemplate, "context", context);
			
		} catch (Exception e) {
			log.debug("exception", e);
			e.printStackTrace();
		}		
		return new ModelAndView(manageSecurityRolesTemplate);
	 
	}
	
	public Map<String,Set<LMSRole>> getDataForRoleType(Long[] selectedRolesIdArray,String roleType,Customer customer){
		Map<String,Set<LMSRole>> returnMap= new HashMap <String,Set<LMSRole>>();
		List<LMSRole> allRoles = vu360UserService.getRolesByRoleType(roleType,customer.getId());
		Set<LMSRole> rolesToBeDeleted= new HashSet<LMSRole>();
		Set<LMSRole> rolesWithUsers= new HashSet<LMSRole>();
		//Set<Learner> learnerSet= new HashSet<Learner>();
		Set<LMSRole> alternateRoles=new HashSet<LMSRole>(allRoles);
		
		Long [] arrRoleIds = new Long[allRoles.size()];
		for(int i=0;i<allRoles.size(); i++){
				arrRoleIds[i] = allRoles.get(i).getId();
		}
			
		Map<String, String> lstLearnerCounted = learnerService.countLearnerByRoles(arrRoleIds);
		
		for(Long roleId:selectedRolesIdArray)
		 for(LMSRole lmsRole: allRoles){
			String count = "0";
			if(lstLearnerCounted.containsKey(lmsRole.getId().toString())){
				count = (String)lstLearnerCounted.get(lmsRole.getId().toString());
			}
				
		    if(roleId == lmsRole.getId() && roleType.equals(roleType)){ 
			  rolesToBeDeleted.add(lmsRole);				  
			  if(Long.valueOf(count) > 0)
			     rolesWithUsers.add(lmsRole);				     				    	 				     				     				  
			}			     			 			  
		 }
			 	
		alternateRoles.removeAll(rolesToBeDeleted);
		
		returnMap.put("rolesToBeDeleted", rolesToBeDeleted);
		returnMap.put("rolesWithUsers", rolesWithUsers);
		returnMap.put("alternateRoles", alternateRoles);
	    
		return returnMap;
	}
	
	
	
	public ModelAndView showProctorProfile( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
			String userId = request.getParameter("userId");
			Proctor prkoctor = proctorService.getProctorByUserId(Long.parseLong(userId));
			
			return new ModelAndView(proctorProfileTemplate, "proctor", prkoctor);
	  }	
	
	
	public ModelAndView deleteSecurityRoles( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {	

			com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			this.setFeatureInSession(request);
				
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();			
					
			int dropAllLearners= 1;			
			String[] learnerRolesToDeleteValues = new String[1];
			int learnersAlternativeRoleId= -1;									
			if(request.getParameter("radioDropLearner")!=null)dropAllLearners= Integer.parseInt(request.getParameter("radioDropLearner"));										
			if(request.getParameterValues("learnerRolesToDelete")!= null)learnerRolesToDeleteValues= request.getParameterValues("learnerRolesToDelete");						
			if(dropAllLearners == 0 && request.getParameter("learnerAlternativeRole")!=null)	learnersAlternativeRoleId= Integer.parseInt(request.getParameter("learnerAlternativeRole"));

			int dropAllManagers= 1;
			String[] managerRolesToDeleteValues = new String[1];
			int managerAlternativeRoleId= -1;
			if(request.getParameter("radioDropManager")!=null)dropAllManagers= Integer.parseInt(request.getParameter("radioDropManager"));									 
			if(request.getParameterValues("managerRolesToDelete")!=null) managerRolesToDeleteValues = request.getParameterValues("managerRolesToDelete");						
			if(dropAllManagers == 0 && request.getParameter("managerAlternativeRole")!=null) managerAlternativeRoleId= Integer.parseInt(request.getParameter("managerAlternativeRole"));
						
						
			this.transferUsersAndDeleteRole(LMSRole.ROLE_LEARNER,learnerRolesToDeleteValues,learnersAlternativeRoleId,customer);
			this.transferUsersAndDeleteRole(LMSRole.ROLE_TRAININGMANAGER,managerRolesToDeleteValues,managerAlternativeRoleId,customer);
			
			if(loggedInUserVO.isAdminMode() && ((VU360UserAuthenticationDetails)auth.getDetails()).getCurrentMode().toString().equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)){
				int dropAllInstructors= 1;
				String[] instructorRolesToDeleteValues = new String[1];
				int instructorAlternativeRoleId= -1;
				if(request.getParameter("radioDropInstructor")!=null)dropAllInstructors= Integer.parseInt(request.getParameter("radioDropInstructor"));									 
				if(request.getParameterValues("instructorRolesToDelete")!=null) instructorRolesToDeleteValues = request.getParameterValues("instructorRolesToDelete");						
				if(dropAllInstructors == 0 && request.getParameter("instructorAlternativeRole")!=null) instructorAlternativeRoleId= Integer.parseInt(request.getParameter("instructorAlternativeRole"));
				
				int dropAllAdmin= 1;
				String[] adminRolesToDeleteValues = new String[1];
				int adminAlternativeRoleId= -1;
				if(request.getParameter("radioDropAdmin")!=null)dropAllAdmin= Integer.parseInt(request.getParameter("radioDropAdmin"));									 
				if(request.getParameterValues("adminRolesToDelete")!=null) adminRolesToDeleteValues = request.getParameterValues("adminRolesToDelete");						
				if(dropAllAdmin == 0 && request.getParameter("adminAlternativeRole")!=null) adminAlternativeRoleId= Integer.parseInt(request.getParameter("adminAlternativeRole"));
				
				int dropAllRegulatory= 1;
				String[] regulatoryRolesToDeleteValues = new String[1];
				int regulatoryAlternativeRoleId= -1;
				if(request.getParameter("radioDropRegulatory")!=null)dropAllRegulatory= Integer.parseInt(request.getParameter("radioDropRegulatory"));									 
				if(request.getParameterValues("regulatoryRolesToDelete")!=null) regulatoryRolesToDeleteValues = request.getParameterValues("regulatoryRolesToDelete");						
				if(dropAllRegulatory == 0 && request.getParameter("regulatoryAlternativeRole")!=null) regulatoryAlternativeRoleId= Integer.parseInt(request.getParameter("regulatoryAlternativeRole"));
				
				this.transferUsersAndDeleteRole(LMSRole.ROLE_INSTRUCTOR,instructorRolesToDeleteValues,instructorAlternativeRoleId,customer);
				this.transferUsersAndDeleteRole(LMSRole.ROLE_LMSADMINISTRATOR,adminRolesToDeleteValues,adminAlternativeRoleId,customer);
				this.transferUsersAndDeleteRole(LMSRole.ROLE_REGULATORYANALYST,regulatoryRolesToDeleteValues,regulatoryAlternativeRoleId,customer);
			}
			
					ManageSecurityRoleForm form = (ManageSecurityRoleForm)command;							
					List<LMSRole> allRoles = vu360UserService.getRolesByRoleType(LMSRole.ROLE_LEARNER, customer.getId());
					allRoles.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_TRAININGMANAGER, customer.getId()));
					
					if(loggedInUserVO.isAdminMode() && ((VU360UserAuthenticationDetails)auth.getDetails()).getCurrentMode().toString().equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)){
						allRoles.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_INSTRUCTOR, customer.getId()));
						allRoles.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_LMSADMINISTRATOR, customer.getId()));
						allRoles.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_REGULATORYANALYST, customer.getId()));
					}
					List<Map<String,Object>> members = new ArrayList <Map<String,Object>>();
					Map<Object, Object> context = new HashMap<Object, Object>();										
					form.setAllUserRoles(allRoles);
					
					for(LMSRole userRole : allRoles){
						Long learnerCount = learnerService.countMembersByRole(userRole);
						Map<String,Object> member = new HashMap <String,Object>();
						member.put("userRole", userRole);
						member.put("Count", learnerCount);
						members.add(member);
					}
					context.put("members", members);
		
					return new ModelAndView(manageSecurityRolesTemplate, "context", context);
			
		} catch (Exception e) {
			log.debug("exception", e);
			e.printStackTrace();
		}		
		return new ModelAndView(manageSecurityRolesTemplate);
	  }	
	
	public void transferUsersAndDeleteRole(String roleType,String[] rolesToDeleteValues, long alternateRoleId,Customer customer){
		
		Long deleteRoleIdArray[] = new Long[rolesToDeleteValues.length];
		Set<LMSRole> rolesToBeDeleted= new HashSet<LMSRole>();
		Set<Learner> learnerSet= new HashSet<Learner>();
		
		if( rolesToDeleteValues != null )
			for( int i = 0; i < rolesToDeleteValues.length ; i++ ) {
				String roleID = rolesToDeleteValues[i];
				if( roleID != null ) 
					deleteRoleIdArray[i]= Long.parseLong(rolesToDeleteValues[i]);					
			}						
																	
			for(long id: deleteRoleIdArray)/*collect set of learners assigned to the roles to be deleted*/
			 for(LMSRole role: vu360UserService.getRolesByRoleType(roleType, customer.getId()))
			  if(role.getId() == id && role.getRoleType().equals(roleType)){
				  rolesToBeDeleted.add(role);				  
				  List <Learner> memberList= learnerService.getMembersByRole(role);
				  if(memberList!=null)
					  learnerSet.addAll(memberList);					  				  					 
			     }				  
			   		     
			/*get all usersIds from the set of collected learners*/
			long [] userIdArray = new long[learnerSet.size()];
			int count= 0;
			for(Learner learner:learnerSet){
				userIdArray[count++]= learner.getVu360User().getId();
			}
			
			for(LMSRole role:rolesToBeDeleted){
				learnerService.unAssignUserFromRole(userIdArray,role);	
			}
			vu360UserService.deleteUserRole(deleteRoleIdArray);
			if(alternateRoleId > 0){
		    	LMSRole alternativeRole= learnerService.getLMSRoleById(Long.valueOf(alternateRoleId));
				learnerService.assignUserToRole(userIdArray,alternativeRole);									
		    }
	}
		/*public ModelAndView directlyDeleteSecurityRoles( HttpServletRequest request, HttpServletResponse response,
				Object command, BindException errors ) throws Exception {
			try {
	
				ManageSecurityRoleForm form = (ManageSecurityRoleForm)command;
				
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
					VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
					if( details.getCurrentMode() == VU360UserMode.ROLE_LMSADMINISTRATOR ) {
						if( details.getCurrentSearchType() != AdminSearchType.CUSTOMER ) {
							return new ModelAndView(failureTemplate,"isRedirect","c");
						}
					}
				} 
							
				VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
						getAuthentication().getDetails()).getCurrentCustomer();
				Map<Object, Object> context = new HashMap<Object, Object>();
	
				List<Map<String,Object>> members = new ArrayList <Map<String,Object>>();
	
				String[] selectedRolesValues = request.getParameterValues("rolescheck");
				long roleIdArray[] = new long[selectedRolesValues.length];
				log.debug("RoletobeDeleted "+roleIdArray.length);
				
				if( selectedRolesValues != null ){
					for( int i = 0; i < selectedRolesValues.length ; i++ ) {
						String roleID = selectedRolesValues[i];
						if( roleID != null ) {
							roleIdArray[i]=Long.parseLong(selectedRolesValues[i]);
						}	
					}		
					vu360UserService.deleteUserRole(roleIdArray);
				}
				
				Set<LMSRole> selectedRoles= new HashSet<LMSRole>(); 			
				for(long id:roleIdArray)
				 for(LMSRole role: form.getAllUserRoles())
				  if(role.getId()==id)
				    selectedRoles.add(role);
									
				
				List<LMSRole> allRoles = vu360UserService.getRolesByRoleType(LMSRole.ROLE_LEARNER, customer);
				allRoles.addAll(vu360UserService.getRolesByRoleType(LMSRole.ROLE_TRAININGMANAGER, customer));
				//List<LMSRole> allUserRoles = vu360UserService.getAllRoles(customer,loggedInUser);			
				form.setAllUserRoles(allRoles);
				
				for(LMSRole userRole : allRoles){
					List <Learner> list = learnerService.getMembersByRole(userRole);
					int count= list==null?0:list.size();					
					Map<String,Object> member = new HashMap <String,Object>();
					member.put("userRole", userRole);
					member.put("Count", count);
					members.add(member);
				}
				context.put("members", members);
	
				return new ModelAndView(manageSecurityRolesTemplate, "context", context);
				
			} catch (Exception e) {
				log.debug("exception", e);
			}		
			return new ModelAndView(manageSecurityRolesTemplate);
		}	*/
	
		
	
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// Auto-generated method stub
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public String getFailureTemplate() {
		return failureTemplate;
	}

	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}

	public String getManageSecurityRolesTemplate() {
		return manageSecurityRolesTemplate;
	}

	public void setManageSecurityRolesTemplate(String manageSecurityRolesTemplate) {
		this.manageSecurityRolesTemplate = manageSecurityRolesTemplate;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getDeleteSecurityRoleTemplate() {
		return deleteSecurityRoleTemplate;
	}

	public void setDeleteSecurityRoleTemplate(String deleteSecurityRoleTemplate) {
		this.deleteSecurityRoleTemplate = deleteSecurityRoleTemplate;
	}

	public String getProctorProfileTemplate() {
		return proctorProfileTemplate;
	}

	public void setProctorProfileTemplate(String proctorProfileTemplate) {
		this.proctorProfileTemplate = proctorProfileTemplate;
	}

	public ProctorService getProctorService() {
		return proctorService;
	}

	public void setProctorService(ProctorService proctorService) {
		this.proctorService = proctorService;
	}

	public LMSProductPurchaseService getLmsProductPurchaseService() {
		return lmsProductPurchaseService;
	}

	public void setLmsProductPurchaseService(
			LMSProductPurchaseService lmsProductPurchaseService) {
		this.lmsProductPurchaseService = lmsProductPurchaseService;
	}
	
	
}