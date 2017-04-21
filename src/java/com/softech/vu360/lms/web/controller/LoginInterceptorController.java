package com.softech.vu360.lms.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

import com.softech.vu360.lms.helpers.ManagerHelper;
import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertQueue;
import com.softech.vu360.lms.model.Survey;
import com.softech.vu360.lms.model.VU360User;
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
			VU360User userObj = null;
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
				userObj = vu360UserService.loadForUpdateVU360User(user.getId());
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
						userObj = vu360UserService.loadForUpdateVU360User(user.getId());				 	
					user.setNewUser(false);
					userObj.setNewUser(false);
					userObj = vu360UserService.updateUser(user.getId(), userObj);	
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
						 userObj = vu360UserService.loadForUpdateVU360User(user.getId());
					 user.setShowGuidedTourScreenOnLogin(Boolean.FALSE);
					 userObj.setShowGuidedTourScreenOnLogin(Boolean.FALSE);
					 userObj= vu360UserService.updateUser(user.getId(), userObj);	
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
						 userObj = vu360UserService.loadForUpdateVU360User(user.getId());				 	
					 userObj.setAcceptedEULA(Boolean.TRUE);
					 user.setAcceptedEULA(Boolean.TRUE);
					 vu360UserService.updateUser(user.getId(), userObj);
				 } 
				 
				 if(!user.isAcceptedEULA() && userInput != null && (userInput.equalsIgnoreCase("gotoGuidedTourPage") || userInput.equalsIgnoreCase("guidedTourScreenShown") || userInput.equalsIgnoreCase("remindMeLaterforSurvey") || userInput.equalsIgnoreCase("continueFromAlertRequestPage"))){																 
					return new ModelAndView(loginLicenseAgreementTemplate);
				 }				
			}
			
			if(userInput!=null && userInput.equalsIgnoreCase("takeSurveyNow")){ /*TAKE SURVEY NOW*/
				return new  ModelAndView(surveyTemplate);
				 
			} 	
			
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
					return new ModelAndView((new StringBuilder().append("redirect:").append(ManagerHelper.getManagerURL(request))).toString());
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
			if(user.isLearnerMode()){
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
	 * Set disabled feature codes and groups for logged in user
	 * 
	 *  
	 * User having multiple roles, like Administrator, Manager & Learner
	 * and those roles have a feature group with the same name. For instance, 
	 * 'Tools' feature group exists in both Administrator and Manager. If
	 * all features of Tools disabled on Administrator-level, 'Tools' 
	 * remain accessible from Manager mode.
	 * 
	 * Due to which feature groups are now kept along with role type. 
	 * For example, if a user has Administrator, Manager & Learner 
	 * roles (modes), when the user logs in all its disabled feature groups
	 * are being kept in session and to distinguish which feature group
	 * belongs to which role type each feature group is now being concatenate 
	 * with relevant role. 
	 * 
	 * If you debug and inspect session.getAttribute(DISABLED_FEATURE_CODES)
	 * for disabled feature groups, you will find feature group concatenated
	 * with role type, for example ROLE_LMSADMINISTRATOR:Tools, 
	 * ROLE_TRAININGADMINISTRATOR:Tools. This means the user has Tools 
	 * disabled on both Administrator and Manager mode.
	 *   
	 * @param user
	 * @param session
	 * @author ramiz.uddin
	 * @since 4/19/2017
	 */
	
	private void setDisabledLmsFeatureCodesAndGroupsForUser (com.softech.vu360.lms.vo.VU360User user, HttpSession session)
	{
		try {
			log.info(" ---------- START - setDisabledLmsFeatureCodesAndGroupsForUser : " + this.getClass().getName()
					+ " ---------- ");

			String[] featureCodes, featureGroups;
			
			featureCodes = new String[] {};
			featureGroups = new String[] {};
			
			String[] disabledFeatures = securityAndRolesService
					.findDistinctEnabledFeatureFeatureGroupsForDistributorAndCustomer(
							user.getLearner().getCustomer().getDistributor().getId(),
							user.getLearner().getCustomer().getId());

			if(disabledFeatures != null && disabledFeatures.length == 2) {
				
				featureCodes = disabledFeatures[0].split(",");
				featureGroups = disabledFeatures[1].split(",");
			}
			
			session.setAttribute(UserPermissionChecker.DISABLED_FEATURE_GROUPS, new HashSet<>(Arrays.asList(featureGroups)));
			session.setAttribute(UserPermissionChecker.DISABLED_FEATURE_CODES, new HashSet<>(Arrays.asList(featureCodes)));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
			if(surveyList!=null && surveyList.size() > 0 && user.isLearnerMode()){ 				
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