package com.softech.vu360.lms.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertQueue;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.web.controller.model.InterceptorAlertForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.AlertInterceotorSort;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * General purpose class to load a velocity template that
 * requires not business logic or processing other than just
 * presenting the page.  Best example is displaying the login page.
 * 
 * @author jason
 * @modified Praveen Gaurav (jan-15-2009, Added isLoginDone in session)
 *
 */
public class LoginInterceptorController implements Controller {

	private static final Logger log = Logger.getLogger(LoginInterceptorController.class.getName());
	private String defaultTemplate = null;
	private String adminTemplate = null;
	private String managerTemplate = null;
	private String regulatorTemplate = null;
	private String surveyTemplate = null;
	private String loginOrSurveyRequestFormTemplate = null; // [NOTE : To ask what to do  i.e. servay or login]
	private String loginOrAlertRequestFormTemplate = null;
	private String loginBrowserCheckTemplate = null;
	private String loginGuidedTourTemplate = null;
	private String loginLicenseAgreementTemplate = null;
	private SurveyService surveyService;	
	private VU360UserService vu360UserService=null;
	private HttpSession session = null;
	Map<Object, Object> map = new HashMap<Object, Object>();
	protected static String AUTH_TOEKN_HEADER = "360_AUTH_USER_TOKEN";

	/**
	 * Security and roles service
	 * @see {@link SecurityAndRolesService}
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	private SecurityAndRolesService securityAndRolesService;

	public final static double MILLISECONDS_IN_DAY = 1000*60*60*24;
	
	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) {
		String preauthToken = ServletRequestUtils.getStringParameter(request, AUTH_TOEKN_HEADER, "-1");
		boolean isPredictUserCookie = false;
		String redirectUrl = ServletRequestUtils.getStringParameter(request, "redirectUrl","");
		log.debug("Inside LoginInterceptorController Redicrt URL =================================>" + redirectUrl);
		
		if(preauthToken.compareTo("-1")!=0) {
			if(request.getParameter("redirectUrl")!=null){
				return new ModelAndView("redirect:" + request.getParameter("redirectUrl"));
			}
			log.debug("redirecting to interceptor to hide auth token");
			return new ModelAndView("redirect:/interceptor.do");
		}
		String templateToBeRedirected =  null;
		try {
			String userInput = request.getParameter("userInput");
			String action = request.getParameter("method");
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) auth.getPrincipal();
			//VU360User userObj = vu360UserService.loadForUpdateVU360User(user.getId());
//			VU360User userObj = null;
			VU360UserNew userObj = null;
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();
			
			if(isPredictUser())
			{
				Cookie cookies[]= request.getCookies();
				if (cookies!=null)
				{
					for (int i=0; i<cookies.length; i++)
					{
						if(cookies[i].getName().equalsIgnoreCase("isPredictUser"))
						{
							isPredictUserCookie = true;
							break;
						}
					}
				}
				
				if(!isPredictUserCookie)
				{
					Cookie cookiePredictUser = new Cookie("isPredictUser", "true");
					cookiePredictUser.setMaxAge(60*60*24); 
					response.addCookie(cookiePredictUser);
				}
			}
			//details.setOriginalPrincipal((VU360User)vu360UserService.loadUserByUsername(user.getUsername()));
			//details.doInitializeUser();
			//user = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			/* new code added for branding
			 * 21 july 2009 
			 * Faisal A. Siddiqui
			 */
			Brander brand=VU360Branding.getInstance().getBranderByUser(request,user);
			request.getSession().setAttribute(VU360Branding.BRAND,brand.getName());
			
			//OWS Code
			// setting in a cookie so that if user click on logout then
			// application may get it from cookie
			Cookie c = new Cookie(VU360Branding.BRAND, brand.toString());
			c.setMaxAge(86400);
			response.addCookie(c);
			//END OWS
			
			if(userInput == null){			 
				//Changes made by Marium Saud inorder to by pass all delete queries executed while user login
				user.setNumLogons(user.getNumLogons()+1);
				user.setLastLogonDate(new Date());
				userObj = vu360UserService.loadForUpdateVU360UserNew(user.getId());
				userObj.setNumLogons(user.getNumLogons());
				userObj.setLastLogonDate(user.getLastLogonDate());
				userObj = vu360UserService.updateNumLogons(userObj);
				
				/**
				 * LMS-8108 | S M Humayun | 12 Apr 2011
				 */
				this.setDisabledLmsFeatureCodesAndGroupsForUser(user, request.getSession());
			}
			

			
			// TODO:  use this as an opportunity to check for things like
			// if the user has accepted the EULA, must change their password etc.
			
			// [9/27/2010] LMS-7219 :: Learner Mode > Login: Force User to Change Password on Next Login
			if (user.getChangePasswordOnLogin()) {
				// Redirect to force user to change Password on this Login
				return new ModelAndView("redirect:lrn_changePassword.do");				
			}
			
			// 05 May, 2012:: [LMS-12966] :: Proctor updates his password on first login and after every 30 days.
			
			/*---// disable password reset for Proctor on business request 14 Nov, 2012
			if (user.isProctor() && user.getProctor()!=null && user.getProctor().isFirstLogin()) {
				// Redirect to force user to change Password on First Login if User has Proctor Mode
				return new ModelAndView("redirect:proctor_changePassword.do");				
			}
			else if (user.isProctor() &&  user.getProctor()!=null && user.getProctor().getProctorStatusTimeStamp()!=null) {
				long dateDiff = DateUtil.getDateDifference(user.getProctor().getProctorStatusTimeStamp(), new Date());
				// Redirect to force user to change Password after 30 days [Proctor password]
				if(dateDiff <= -30)
					return new ModelAndView("redirect:proctor_changePassword.do?showThirtyDaysPassedPage=yes");				
			}
			*/
			// all okay let them proceed.
			//no longer required here
			//we are doing the following stuff in the Spring Security Filter - VU360UserModeSetupFilter
            /*		
			HttpSession session = request.getSession();
			GrantedAuthority[] authorities = SecurityContextHolder.getContext().getAuthentication()
			.getAuthorities();
			session.setAttribute("hasSwitchedAuth", new Boolean(false));
			
			for(GrantedAuthority auth : authorities){
				if(auth instanceof SwitchUserGrantedAuthority){
					log.debug("USER HAS SWITCHED AUTH TO SwitchUserGrantedAuthority");
					session.setAttribute("hasSwitchedAuth", new Boolean(true));
					break;
				}
			}
            */	
			//user = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//While login to learner and having survey causing detached entity error (UAT User:rwa160501Learner1@lms.com)
			//user = VU360UserAuthenticationDetails.getCurrentUser();
			if((brand.getBrandElement("lms.login.settings.browsercheck")!= null) && (brand.getBrandElement("lms.login.settings.browsercheck").equalsIgnoreCase("true"))){
				if(userInput == null && user.isNewUser()){
					return new ModelAndView(loginBrowserCheckTemplate);
				 }else if(userInput == null){
					 userInput= "gotoGuidedTourPage";
				 }
				
				if(userInput != null && userInput.equalsIgnoreCase("browserCheckScreenShown")){
					userInput= "gotoGuidedTourPage";
					if(userObj == null)
						userObj = vu360UserService.loadForUpdateVU360UserNew(user.getId());				 	
					user.setNewUser(false);
					userObj.setNewUser(false);
					userObj = vu360UserService.updateUser(userObj);	
				 } 
			}
			
			if((brand.getBrandElement("lms.login.settings.guidedtour")!= null) &&(brand.getBrandElement("lms.login.settings.guidedtour").equalsIgnoreCase("true"))){			
				if((userInput == null || (userInput != null && userInput.equalsIgnoreCase("gotoGuidedTourPage"))) && user.getShowGuidedTourScreenOnLogin()){
					//user = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Map<Object, Object> context = new HashMap<Object, Object>();
					context.put("user", user);
					return new ModelAndView(loginGuidedTourTemplate,"context",context);
				 }
				
				 if(userInput != null && userInput.equalsIgnoreCase("guidedTourScreenShown") && request.getParameter("cbDontShowAgain") != null){
					 if(userObj == null)
						 userObj = vu360UserService.loadForUpdateVU360UserNew(user.getId());
					 user.setShowGuidedTourScreenOnLogin(Boolean.FALSE);
					 userObj.setShowGuidedTourScreenOnLogin(Boolean.FALSE);
					 userObj= vu360UserService.updateUser(userObj);	
				 }
				
				 if(userInput != null && (userInput.equalsIgnoreCase("gotoGuidedTourPage") || userInput.equalsIgnoreCase("guidedTourScreenShown"))){
					 //user= vu360UserService.loadForUpdateVU360User(user.getId());
					 ModelAndView surveyView= getSurveyModelAndView(userInput, user); /*show survey if applicable*/
					 if(surveyView != null) return surveyView;		
				 } 
				 
				 if((userInput != null && (userInput.equalsIgnoreCase("gotoGuidedTourPage") || userInput.equalsIgnoreCase("guidedTourScreenShown") || userInput.equalsIgnoreCase("remindMeLaterforSurvey")))
				 || action != null && action.equalsIgnoreCase("sortAlert")		 
				 ){														
				    ModelAndView alertsView= getAlertsModelAndView(request,userInput,user); /*show alerts if applicable*/
				    if(alertsView != null) return alertsView;
				 }
				 
			}	
			
			if((brand.getBrandElement("lms.login.settings.eula")!= null) && (brand.getBrandElement("lms.login.settings.eula").equalsIgnoreCase("true"))){
				 if(userInput != null && userInput.equalsIgnoreCase("licenseAgreementAccepted")){
					 if(userObj == null)
						 userObj = vu360UserService.loadForUpdateVU360UserNew(user.getId());				 	
					 userObj.setAcceptedEULA(Boolean.TRUE);
					 user.setAcceptedEULA(Boolean.TRUE);
					 vu360UserService.updateUser( userObj);
				 } 
				 
				 if(!user.isAcceptedEULA() && userInput != null && (userInput.equalsIgnoreCase("gotoGuidedTourPage") || userInput.equalsIgnoreCase("guidedTourScreenShown") || userInput.equalsIgnoreCase("remindMeLaterforSurvey") || userInput.equalsIgnoreCase("continueFromAlertRequestPage"))){																 
					return new ModelAndView(loginLicenseAgreementTemplate);
				 }				
			}
			
			if(userInput!=null && userInput.equalsIgnoreCase("takeSurveyNow")){ /*TAKE SURVEY NOW*/
				return new  ModelAndView(surveyTemplate);
				 
			} 	
			
			//user = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			
			

			
			if(user.isAdminMode()){				    
					request.setAttribute("AdminSwitchMode",VU360UserMode.ROLE_LMSADMINISTRATOR);
					details.doAdminSwitchMode(request);
					return new ModelAndView("redirect:/adm_searchMember.do");//Jump right to admin mode if thats the highest mode									
			}	 
			if(user.isAccreditationMode()){				 
					request.setAttribute("AccreditationSwitchMode",VU360UserMode.ROLE_REGULATORYANALYST);
					details.doAccreditationSwitchMode(request);
					return new ModelAndView("redirect:/acc_search.do");//Jump right to accreditation mode if thats the highest mode				
			}
			
			if(user.isManagerMode()){				 
					request.setAttribute("ManagerSwitchMode",VU360UserMode.ROLE_TRAININGADMINISTRATOR);
					details.doManagerSwitchMode(request);
					String redirectURL = null;
					String roleType = "ROLE_TRAININGADMINISTRATOR";
					List<LMSFeature> lstcustomerLMSFeatures =  securityAndRolesService.findAllActiveLMSFeaturesByUser(user.getId(), user.getLearner().getCustomer().getId(), roleType);
					//List<LMSRoleLMSFeature> lstcustomerLMSFeatures =  securityAndRolesService.findLMSRoleLMSFeatureByUser(user.getId(), user.getLearner().getCustomer().getId(), roleType);
					//List<CustomerLMSFeature> lstcustomerLMSFeatures = securityAndRolesService.getCustomerLMSFeatures (user.getLearner().getCustomer(), roleType);
					for(LMSFeature userLMSFeature : lstcustomerLMSFeatures)
					{
						if(UserPermissionChecker.hasAccessToFeature(userLMSFeature.getFeatureCode(), user, request.getSession()))
						{
							log.debug(userLMSFeature.getFeatureName());
							if(userLMSFeature.getFeatureCode().equals("AssignTrainingPlans"))
							 {
							   redirectURL = "redirect:/mgr_assignTraningPlan.do";
							   break;
							 }  
							
							else if( userLMSFeature.getFeatureCode().equals("LMS-MGR-0001"))
							 {
							   redirectURL = "redirect:/mgr_manageLearners.do";
							   break;
							 } 
							
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0002"))
							 {
							   redirectURL = "redirect:/mgr_batchImportLearners.do";
							   break;
							 }
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0003"))
							 {
							   redirectURL = "redirect:/mgr_regInvitation-1.do";
							   break;
							 } 
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0004"))
							 {
							   redirectURL = "redirect:/mgr_manageOrganizationGroup.do";
							   break;
							 } 
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0005"))
							 {
							   redirectURL = "redirect:/mgr_manageLearnerGroups.do";
							   break;
							 }
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0006"))
							 {
							   redirectURL = "redirect:/mgr_manageSecurityRoles.do?method=showSecurityRoles";
							   break;
							 } 
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0007"))
							 {
							   redirectURL = "redirect:/mgr_viewAssignSecurityRoleMain.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0029"))
							 {
							   redirectURL = "redirect:/mgr_learnerEnrollments.do?method=showSearchLearnerPage";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0008"))
							 {
							   redirectURL = "redirect:/mgr_viewPlanAndEnroll.do";
							   break;
							 }
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0009"))
							 {
							   redirectURL = "redirect:/mgr_searchTrainingPlans.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0010"))
							 {
							   redirectURL = "redirect:/mgr_manageSynchronousCourse.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0011"))
							 {
							   redirectURL = "redirect:/mgr_viewAllEntitlements.do";
							   break;
							 }
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0012"))
							 {
							   redirectURL = "redirect:/mgr_ManageReports.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0012"))
							 {
								   redirectURL = "redirect:/mgr_ManageReports.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0013"))
							 {
								   redirectURL = "redirect:/mgr_ManageReports.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0014"))
							 {
								   redirectURL = "redirect:/mgr_ManageReports.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0015"))
							 {
								   redirectURL = "redirect:/mgr_ManageReports.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0032"))
							 {
							   redirectURL = "redirect:/mgr_ManageReports.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0017"))
							 {
							   redirectURL = "redirect:/mgr_alertCourse.do";
							   break;
							 }
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0018"))
							 {
							   redirectURL = "redirect:/mgr_sendMailToLearners.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0019"))
							 {
							   redirectURL = "redirect:/mgr_viewAssignSurveyMain.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0020"))
							 {
							   redirectURL = "redirect:/mgr_manageSurveys.do";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0023"))
							 {
							   redirectURL = "redirect:/mgr_editCustomer.do?method=editCustomerProfile";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0024"))
							 {
							   redirectURL = "redirect:/mgr_editCustomer.do?method=editCustomerPreferences";
							   break;
							 }  
							 else if(userLMSFeature.getFeatureCode().equals("LMS-MGR-0025"))
							 {
							   redirectURL = "redirect:/mgr_ViewEnrollmentForCertEnableDisable.do?method=searchLearner";
							   break;
							 }  

						}
					}
					//return new ModelAndView("redirect:/mgr_manageLearners.do");//Jump right to manager mode if thats the highest mode
					return new ModelAndView(redirectURL);//Jump right to manager mode if thats the highest mode
			}
			
			if(user.isProctorMode()){				 
				request.setAttribute("ProctorSwitchMode",VU360UserMode.ROLE_PROCTOR);
				details.doProctorSwitchMode(request);
				return new ModelAndView("redirect:/proctorCompletionCertificate.do?method=displayProctorLearners");//Jump right to Proctor mode if thats the highest mode				
			}
			
			if(user.isInstructorMode()){				
					request.setAttribute("InstructorSwitchMode",VU360UserMode.ROLE_INSTRUCTOR);
					details.doInstructorSwitchMode(request);
					return new ModelAndView("redirect:/ins_synchronousClasses.do");//Jump right to Instructor mode if thats the highest mode			
			}
			if(user.isInLearnerRole()){
				return new ModelAndView(defaultTemplate);
			}
			/*redirect to login page if user dosen't have any rights*/
			return new ModelAndView("redirect:/login.do");
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(templateToBeRedirected , "context" , map);
	}

	/**
	 * Set disabled lms feature codes and groups for this user, either disabled 
	 * at distributor or customer level 
	 * @param user
	 * @param session
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	private void setDisabledLmsFeatureCodesAndGroupsForUser (com.softech.vu360.lms.vo.VU360User user, HttpSession session)
	{
		try
		{
			log.info(" ---------- START - setDisabledLmsFeatureCodesAndGroupsForUser : " + this.getClass().getName() + " ---------- ");

			Set<String> disabledFeatureCodes = new HashSet<String>();
			Set<String> disabledFeatureGroups = new HashSet<String>();
			List<String> featureGroupSet = securityAndRolesService.findDistinctEnabledFeatureFeatureGroupsForDistributorAndCustomer(
					user.getLearner().getCustomer().getDistributor().getId(), user.getLearner().getCustomer().getId());
			
			log.info("fetching disabled lms feaure codes and groups defined at distributor level...");
			List<DistributorLMSFeature> disabledDistributorLMSFeatures = securityAndRolesService.getDisabledDistributorLMSFeatures(
					user.getLearner().getCustomer().getDistributor().getId());
			log.info("disabledDistributorLMSFeatures " + (disabledDistributorLMSFeatures == null ? "is null" : "size = " 
				+ disabledDistributorLMSFeatures.size()));
			
			for(DistributorLMSFeature distributorLMSFeature : disabledDistributorLMSFeatures)
			{
				log.info("distributorLMSFeature - " + distributorLMSFeature.getLmsFeature().getFeatureCode());
				if(distributorLMSFeature.getLmsFeature().getFeatureName().equals(distributorLMSFeature.getLmsFeature().getFeatureGroup()) // OMG What is this :(
						&& !featureGroupSet.contains((String)distributorLMSFeature.getLmsFeature().getFeatureGroup()))
				{
					log.info(" => group");
					disabledFeatureGroups.add(distributorLMSFeature.getLmsFeature().getFeatureName());
				}
				else
					disabledFeatureCodes.add(distributorLMSFeature.getLmsFeature().getFeatureCode());
			}
			
			log.info("fetching disabled lms feaures defined at customer level...");
			List<CustomerLMSFeature> disabledCustomerLMSFeatures = securityAndRolesService.getDisabledCustomerLMSFeatures(user.getLearner().getCustomer().getId());
			log.info("disabledCustomerLMSFeatures " + (disabledCustomerLMSFeatures == null ? "is null" : "size = " 
				+ disabledCustomerLMSFeatures.size()));
			for(CustomerLMSFeature customerLMSFeature : disabledCustomerLMSFeatures)
			{
				log.info("customerLMSFeature - " + customerLMSFeature.getLmsFeature().getFeatureCode());
				if(customerLMSFeature.getLmsFeature().getFeatureName().equals(customerLMSFeature.getLmsFeature().getFeatureGroup())
						&& !featureGroupSet.contains((String)customerLMSFeature.getLmsFeature().getFeatureGroup()))
				{
					log.info(" => group");
					disabledFeatureGroups.add(customerLMSFeature.getLmsFeature().getFeatureName());
				}
				else
					disabledFeatureCodes.add(customerLMSFeature.getLmsFeature().getFeatureCode());
			}
			session.setAttribute(UserPermissionChecker.DISABLED_FEATURE_GROUPS, disabledFeatureGroups);
			session.setAttribute(UserPermissionChecker.DISABLED_FEATURE_CODES, disabledFeatureCodes);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			log.info(" ---------- END - setDisabledLmsFeaturesForUser : " + this.getClass().getName() + " ---------- ");
		}
	}
			
	/**
	 * The method returns Survey View (if applicable)
	 * @param userInput
	 * @param user
	 * @return
	 */
	private ModelAndView getSurveyModelAndView(String userInput,com.softech.vu360.lms.vo.VU360User user){
		ModelAndView mv = null;		
		try{
			if(userInput!=null && userInput.equalsIgnoreCase("remindMeLaterforSurvey")){ /*REMIND ME LATER*/ 
				return mv; 
			}
			if(userInput!=null && userInput.equalsIgnoreCase("takeSurveyNow")){ /*TAKE SURVEY NOW*/
				mv = new ModelAndView(surveyTemplate);
				return mv; 
			} 
			List<Survey> surveyList = surveyService.getDueSurveysByUser(user);
			if(surveyList!=null && surveyList.size() > 0 && user.isInLearnerRole()){ 				
				if( surveyList.size() > 0 ){		// IF DUE SURVEY EXISTS
					mv = new ModelAndView(loginOrSurveyRequestFormTemplate);
					mv.addObject("dueSurvey" , surveyList.size());					
					mv.addObject("dueSurveyList" , surveyList);
					return mv;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mv;
	
	}
	
	/**
	 * The method returns Alerts View (if applicable)
	 * @param user
	 * @return
	 */
	private ModelAndView getAlertsModelAndView(HttpServletRequest request,String userInput,com.softech.vu360.lms.vo.VU360User user ){		
		ModelAndView mv =null;		
			//List<Alert> alerts = surveyService.getAllAlertByCreatedUserId(user.getId());

			List<Alert> alertsList = new ArrayList<Alert>();	
			List<AlertQueue> alertQueues = new ArrayList<AlertQueue>();	
			alertQueues = surveyService.getAlertQueueByUserId(user.getId());
			
			if(alertQueues!=null && alertQueues.size() > 0){
				for(int findAlert=0;findAlert<alertQueues.size();findAlert++){
					long alertId=alertQueues.get(findAlert).getAlert_Id();
					
					if(alertQueues.get(findAlert)!=null && 
							alertQueues.get(findAlert).getEventDueDate() !=null &&
							isDueDateToday(alertQueues.get(findAlert).getEventDueDate()) == true){
						
						alertsList.add(surveyService.getAlertByID(alertId));
						
					}
				}
			}
			HashSet<Alert> hashSet = new HashSet<Alert>(alertsList);
			List<Alert> alerts = new ArrayList<Alert>(hashSet) ;
			
			
			
			List<InterceptorAlertForm> interceptorAlertForms = new ArrayList<InterceptorAlertForm>();
			if((alerts != null && alerts.size() > 0) ){ //if alert exists
				mv = new ModelAndView(loginOrAlertRequestFormTemplate);	
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");			
				for(int i=0;i<alerts.size();i++){	
					InterceptorAlertForm interceptorAlertForm = new InterceptorAlertForm();
					if(alerts.get(i).getCreatedDate() != null){
						interceptorAlertForm.setCreatedDate(df.format(alerts.get(i).getCreatedDate()));
					}
					interceptorAlertForm.setAlertName(alerts.get(i).getAlertName());
					interceptorAlertForm.setId(alerts.get(i).getId());
					interceptorAlertForm.setAlertMessageBody(alerts.get(i).getAlertMessageBody());
					interceptorAlertForms.add(interceptorAlertForm);
				}
				List<InterceptorAlertForm> sortedInterceptorAlertForms = sortAlert(interceptorAlertForms , request);
				mv.addObject("alertList" , sortedInterceptorAlertForms);
				mv.addObject("context" , map);
				
			 }
		
		return mv;
	}
	
	public List<InterceptorAlertForm> sortAlert(List<InterceptorAlertForm> interceptorAlertForms,HttpServletRequest request){
		
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		session = request.getSession();
		
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) {
			if(session.getAttribute("pageCurrIndex")==null)pageIndex="0";
			else pageIndex = session.getAttribute("pageCurrIndex").toString();
		}

		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AlertInterceotorSort sort = new AlertInterceotorSort();
					sort.setSortBy("date");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(interceptorAlertForms,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					AlertInterceotorSort sort = new AlertInterceotorSort();
					sort.setSortBy("date");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(interceptorAlertForms,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AlertInterceotorSort sort = new AlertInterceotorSort();
					sort.setSortBy("date");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(interceptorAlertForms,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					AlertInterceotorSort sort = new AlertInterceotorSort();
					sort.setSortBy("date");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(interceptorAlertForms,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			}
		}
		
		map = context;
		
		return interceptorAlertForms;
		
	}
	
	public boolean isPredictUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isPredictuser = false; 
		if(authentication!=null && authentication.getPrincipal()!=null) {
			com.softech.vu360.lms.vo.VU360User vu360User = (com.softech.vu360.lms.vo.VU360User) authentication.getPrincipal();
			if(vu360User.getLearner()!=null) {
				if(vu360User.getLearner().getCustomer()!=null) {
					if(vu360User.getLearner().getCustomer().getDistributor()!=null) {
						String distributorName = vu360User.getLearner().getCustomer().getDistributor().getName().trim();
						String strPredictResellers = VU360Properties.getVU360Property("predict.resellers");
						if(strPredictResellers!=null) {
							String[] predictResellers = strPredictResellers.split(",");
							for (String predictReseller : predictResellers) {
								if(predictReseller.trim().compareTo(distributorName)==0) {
									isPredictuser = true;
									}
								}
							}
						}
					}
				}
			}
		return isPredictuser;	
	}
		
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
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

	/**
	 * @return the managerTemplate
	 */
	public String getManagerTemplate() {
		return managerTemplate;
	}

	/**
	 * @param managerTemplate the managerTemplate to set
	 */
	public void setManagerTemplate(String managerTemplate) {
		this.managerTemplate = managerTemplate;
	}

	/**
	 * @return the adminTemplate
	 */
	public String getAdminTemplate() {
		return adminTemplate;
	}

	/**
	 * @param adminTemplate the adminTemplate to set
	 */
	public void setAdminTemplate(String adminTemplate) {
		this.adminTemplate = adminTemplate;
	}

	/**
	 * @return the regulatorTemplate
	 */
	public String getRegulatorTemplate() {
		return regulatorTemplate;
	}

	/**
	 * @param regulatorTemplate the regulatorTemplate to set
	 */
	public void setRegulatorTemplate(String regulatorTemplate) {
		this.regulatorTemplate = regulatorTemplate;
	}

	

	public String getSurveyTemplate() {
		return surveyTemplate;
	}

	public void setSurveyTemplate(String surveyTemplate) {
		this.surveyTemplate = surveyTemplate;
	}

	public String getLoginOrSurveyRequestFormTemplate() {
		return loginOrSurveyRequestFormTemplate;
	}

	public void setLoginOrSurveyRequestFormTemplate(
			String loginOrSurveyRequestFormTemplate) {
		this.loginOrSurveyRequestFormTemplate = loginOrSurveyRequestFormTemplate;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public String getLoginOrAlertRequestFormTemplate() {
		return loginOrAlertRequestFormTemplate;
	}

	public void setLoginOrAlertRequestFormTemplate(
			String loginOrAlertRequestFormTemplate) {
		this.loginOrAlertRequestFormTemplate = loginOrAlertRequestFormTemplate;
	}

	/**
	 * @return the loginBrowserCheckTemplate
	 */
	public String getLoginBrowserCheckTemplate() {
		return loginBrowserCheckTemplate;
	}

	/**
	 * @param loginBrowserCheckTemplate the loginBrowserCheckTemplate to set
	 */
	public void setLoginBrowserCheckTemplate(String loginBrowserCheckTemplate) {
		this.loginBrowserCheckTemplate = loginBrowserCheckTemplate;
	}

	/**
	 * @return the loginGuidedTourTemplate
	 */
	public String getLoginGuidedTourTemplate() {
		return loginGuidedTourTemplate;
	}

	/**
	 * @param loginGuidedTourTemplate the loginGuidedTourTemplate to set
	 */
	public void setLoginGuidedTourTemplate(String loginGuidedTourTemplate) {
		this.loginGuidedTourTemplate = loginGuidedTourTemplate;
	}
	
	
	/**
	 * @return the loginLicenseAgreementTemplate
	 */
	public String getLoginLicenseAgreementTemplate() {
		return loginLicenseAgreementTemplate;
	}

	/**
	 * @param loginLicenseAgreementTemplate the loginLicenseAgreementTemplate to set
	 */
	public void setLoginLicenseAgreementTemplate(
			String loginLicenseAgreementTemplate) {
		this.loginLicenseAgreementTemplate = loginLicenseAgreementTemplate;
	}

	public boolean isDueDateToday(Date dueDate) { //returns true if duedate is today
		Date todaysDate = new Date();
		double diff = Math.abs(dueDate.getTime() - todaysDate.getTime());

		double days = diff / MILLISECONDS_IN_DAY;


		if((int)days == 0){
			return true;
		}else{
			return false;
		}

	}
	
	/**
	 * Return securityAndRoleService
	 * @return securityAndRoleServic
	 * @see {@link SecurityAndRolesService}
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public SecurityAndRolesService getSecurityAndRolesService() {
		return securityAndRolesService;
	}
	
	/**
	 * Set securityAndRoleService
	 * @param securityAndRolesService
	 * @see {@link SecurityAndRolesService}
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public void setSecurityAndRolesService(
			SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}


}