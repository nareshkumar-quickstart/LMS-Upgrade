package com.softech.vu360.lms.web.velocity;

import java.util.Arrays;

/**
 * @modified Praveen Gaurav (jan-15-2009, Added userData in modelAndView Object)
 */

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.BrandDomain;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.BrandDomainService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.impl.VU360UserServiceImpl;
import com.softech.vu360.lms.util.NullTool;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.vo.VU360User;
import com.softech.vu360.lms.web.controller.MenuController;
import com.softech.vu360.lms.web.controller.ReportController;
import com.softech.vu360.lms.web.controller.administrator.AddNewCustomerWizardController;
import com.softech.vu360.lms.web.controller.administrator.EditCustomerController;
import com.softech.vu360.lms.web.controller.administrator.EditDistributorProfileController;
import com.softech.vu360.lms.web.controller.administrator.ManageCustomFieldsController;
import com.softech.vu360.lms.web.controller.administrator.ManageDistributorController;
import com.softech.vu360.lms.web.controller.administrator.ManageDistributorEntitlementController;
import com.softech.vu360.lms.web.controller.administrator.ManageDistributorGroupWizardController;
import com.softech.vu360.lms.web.controller.administrator.UpdateEntitlementDetailsController;
import com.softech.vu360.lms.web.controller.learner.ControllerUtils;
import com.softech.vu360.lms.web.controller.learner.LaunchCourseController;
import com.softech.vu360.lms.web.controller.learner.LearnerProfileController;
import com.softech.vu360.lms.web.controller.learner.LearnerWidgetDashboardController;
import com.softech.vu360.lms.web.controller.learner.MyCoursesController;
import com.softech.vu360.lms.web.controller.manager.AddCustomCoursesWizardController;
import com.softech.vu360.lms.web.controller.manager.AddLearnerController;
import com.softech.vu360.lms.web.controller.manager.AddLearnerGroupController;
import com.softech.vu360.lms.web.controller.manager.AddOrganizationGroupController;
import com.softech.vu360.lms.web.controller.manager.AddSecurityRoleController;
import com.softech.vu360.lms.web.controller.manager.AddSecurityRoleWizardController;
import com.softech.vu360.lms.web.controller.manager.AddSelfRegistrationInvitationWizardController;
import com.softech.vu360.lms.web.controller.manager.AddTrainingPlanControllerStep2;
import com.softech.vu360.lms.web.controller.manager.AddTrainingPlanWizardController;
import com.softech.vu360.lms.web.controller.manager.BatchImportLearnersController;
import com.softech.vu360.lms.web.controller.manager.ChangeMemberRoleController;
import com.softech.vu360.lms.web.controller.manager.EditLearnerPreferencesController;
import com.softech.vu360.lms.web.controller.manager.EditManagerProfileController;
import com.softech.vu360.lms.web.controller.manager.ManageAlertController;
import com.softech.vu360.lms.web.controller.manager.ManageCustomCoursesController;
import com.softech.vu360.lms.web.controller.manager.ManageEmailController;
import com.softech.vu360.lms.web.controller.manager.ManageEnrollmentController;
import com.softech.vu360.lms.web.controller.manager.ManageLearnerController;
import com.softech.vu360.lms.web.controller.manager.ManageLearnerGroupsController;
import com.softech.vu360.lms.web.controller.manager.ManageMyAlertController;
import com.softech.vu360.lms.web.controller.manager.ManageOraganizationGroupsController;
import com.softech.vu360.lms.web.controller.manager.ManageSecurityRolesController;
import com.softech.vu360.lms.web.controller.manager.ManageSurveyController;
import com.softech.vu360.lms.web.controller.manager.ManageSurveyWizardController;
import com.softech.vu360.lms.web.controller.manager.SearchAndManageTrainingPlanController;
import com.softech.vu360.lms.web.controller.manager.SelfRegistrationInvitationController;
import com.softech.vu360.lms.web.controller.manager.ViewAllEntitlementsController;
import com.softech.vu360.lms.web.controller.manager.ViewEntitlementDetailsController;
import com.softech.vu360.lms.web.controller.manager.ViewLearnerEnrollmentController;
import com.softech.vu360.lms.web.controller.model.Menu;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldForm;
import com.softech.vu360.lms.web.controller.proctor.ProctorCertificateCompletionController;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.CASStorefrontUtil;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;


/*
 * @Wajahat:
 * Spring MVC allow you to intercept web request through handler interceptors. The handler interceptor have to implement the HandlerInterceptor interface, 
 * which contains three methods :
 * 1- preHandle() – Called before the handler execution, returns a boolean value, “true” : continue the handler execution chain; “false”, 
 * 					stop the execution chain and return it.
 * 2- postHandle() – Called after the handler execution, allow manipulate the ModelAndView object before render it to view page.
 * 3- afterCompletion() – Called after the complete request has finished. Seldom use, cant find any use case.
 * 
 * Modified by Marium Saud
 * Replace details.getCurrentLearner() with 
 * VU360User dbCurrentUser = details.getCurrentUser();
 * dbCurrentUser.getLearner();
 * Reason for replacement is object 'adminSelectedLearner' is used in many .VM files which is crashing the page as learner object failed to lazily initialize VU360User object. 
 *  
 * */

public class AcegiVelocityContextInterceptor extends HandlerInterceptorAdapter {
	BrandDomainService brandDomainService = null;
	VU360UserServiceImpl vu360UserService = null;
	AuthorService authorService = null;
	LearnerService learnerService=null;
	
	private static final Logger log = Logger.getLogger(AcegiVelocityContextInterceptor.class.getName());
	private static UserPermissionChecker userPermissionChecker = new UserPermissionChecker();
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		log.debug("Inside AcegiVelocityContextInterceptor.afterCompletion()");
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.debug("Inside AcegiVelocityContextInterceptor.postHandle()");
		HttpSession menuSession = request.getSession(true);
		if(request.getRequestURI().indexOf("login.do") > -1){
			request.getSession().setAttribute("initedClustered", null);
		}
		if (modelAndView != null) {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			if (auth!=null && auth.getPrincipal() != null
					&& auth.getPrincipal() instanceof com.softech.vu360.lms.vo.VU360User) {
				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) auth.getPrincipal();

				if(modelAndView.getViewName().compareTo("json")!=0) {
					modelAndView.addObject("userData", loggedInUser);
					modelAndView.addObject("userPermissionChecker", userPermissionChecker);
				}
				if (handler instanceof MenuController) {

				} else {
					boolean pageAuth = handleMenuAuth(loggedInUser, handler, request);
					if (pageAuth == false) {
						Menu menu = new Menu(loggedInUser,request);
						if (menu.getUrl() != null){
							//request.getRequestDispatcher(request.getContextPath().toString()+"/"+ menu.getUrl()).forward(request, response);
							if(!response.isCommitted()){
							response.sendRedirect(request.getContextPath().toString()+"/"+ menu.getUrl());
							}
						}
						else
							//response.sendRedirect("j_spring_security_logout");
							response.sendRedirect("logout");

						// return false;

					}

				}

			}

			// Jason Burns - 03/05/2009 - why is this here? This does not appear
			// to be required for
			// EVERY request in the system?
			if (auth!=null && auth.getDetails() != null
					&& auth.getDetails() instanceof VU360UserAuthenticationDetails) {
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth
						.getDetails();
				if (details.getCurrentSearchType() != AdminSearchType.NONE) {
					modelAndView.addObject("adminSelectedEntityType", details.getCurrentSearchType());
					
					modelAndView.addObject("adminSelectedCustomer", details.getCurrentCustomer());
					if(details.getCurrentSearchType().equals(AdminSearchType.CUSTOMER)) {
						menuSession.setAttribute("adminSelectedCustomer", ProxyVOHelper.setCustomerProxy(details.getCurrentCustomer()));
						menuSession.setAttribute("adminSelectedDistributor", null);
					} else if(details.getCurrentSearchType().equals(AdminSearchType.DISTRIBUTOR)) {
						menuSession.setAttribute("adminSelectedDistributor", details.getProxyDistributor());
						menuSession.setAttribute("adminSelectedCustomer", null);
					} else {
						menuSession.setAttribute("adminSelectedCustomer", null);
						menuSession.setAttribute("adminSelectedDistributor", null);
					}
					
					modelAndView.addObject("customerUserName", details.getOriginalPrincipal().getUsername());
					//LMS-3147
					if(details.getCurrentCustomer()!=null)
					{
						modelAndView.addObject("adminSelectedCustomer_Reseller", details
							.getCurrentCustomer().getDistributor());
					}
				 	 // end LMS-3147
						modelAndView.addObject("adminSelectedDistributor", details
							.getProxyDistributor());
					
						modelAndView.addObject("adminSelectedLearner", details.getProxyLearner());

					// 	 LMS-3147
					if(details.getProxyLearner()!=null)
					{
						modelAndView.addObject("adminSelectedLearner_Customer", details.getProxyLearner().getCustomer());
						modelAndView.addObject("adminSelectedLearner_Reseller", details.getProxyLearner().getCustomer().getDistributor());
					}
					// 	 end LMS-3147
				
				} else {
					modelAndView.addObject("adminSelectedEntityType",
							AdminSearchType.NONE);
				}
				
				//LMS-19454 - user is switched to manager mode from Admin when Tomcat node switched
				HttpSession session = request.getSession(false);
				if (session != null && request.getSession(false).getAttribute("clusteredCurrentMode") !=null) {
					modelAndView.addObject("adminCurrentMode", request.getSession(false).getAttribute("clusteredCurrentMode"));
				}else{
					modelAndView.addObject("adminCurrentMode", details.getCurrentMode());
				}
				
			} else {
				modelAndView.addObject("adminSelectedEntityType",
						AdminSearchType.NONE);
			}
			

			Brander brander = null;
			brander = getBrander(request, response, auth);

			modelAndView.addObject("formUtil", FormUtil.getInstance());
			modelAndView.addObject("brander", brander);
			modelAndView.addObject("lang", new Language());
			modelAndView.addObject("brand", brander.getName());
			modelAndView
					.addObject("userAgent", request.getHeader("User-Agent"));
			modelAndView.addObject("currentView", modelAndView.getViewName());
			modelAndView.addObject("locale", request.getLocale());
			modelAndView.addObject("request",request);
			modelAndView.addObject("nullTool",new NullTool());
			
			Cookie casCookie = WebUtils.getCookie(request, "isCASAuthenticated");
			boolean isCASAuthenticated = false; 
			if (casCookie != null) {
				isCASAuthenticated = true;
			}
			
			if(isCASAuthenticated && SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null && (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof VU360User))
				modelAndView.addObject("logoutSuccessUrl", CASStorefrontUtil.getLogOutUrl((VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
		}
		super.postHandle(request, response, handler, modelAndView);
	}

	private Brander getBrander(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
		Brander brander = null;
		com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
		BrandDomain brandDomain;
		String paramLanguage = request.getParameter("lang");
		String paramBrand = request.getParameter("brand");

		// case of authenticated user
		if (auth != null && auth.getPrincipal() != null
				&& auth.getPrincipal() instanceof com.softech.vu360.lms.vo.VU360User) {

			com.softech.vu360.lms.vo.VU360User vu360UserVO = (com.softech.vu360.lms.vo.VU360User) auth
					.getPrincipal();
			
			//VU360User loggedInUser = vu360UserService.getUserById(vu360UserVO.getId());
			
			brander = VU360Branding.getInstance().getBranderByUser(request, vu360UserVO);
			request.getSession().setAttribute(VU360Branding.BRAND, brander.getName());

		}
		// perhaps, not an authenticated user or perhaps logged out user
		else if ((paramBrand != null && paramBrand.trim().length() > 0)
				|| (paramLanguage != null && paramLanguage.trim().length() > 0)) {
			
			// for an unauthenticated (guest or once logged in user),
			// the preference of brands will always be from query string

			paramLanguage = (paramLanguage == null || paramLanguage.trim().length() == 0) ? Language.DEFAULT_LANG
					: paramLanguage.trim();

			lang.setLanguage(paramLanguage);

			paramBrand = (paramBrand == null || paramBrand.trim().length() == 0) ? VU360Branding.DEFAULT_BRAND
					: paramBrand.trim();

			brander = (Brander) VU360Branding.getInstance().getBrander(paramBrand, lang);

			Cookie c = new Cookie(VU360Branding.BRAND, brander.toString());

			c.setMaxAge(86400);
			response.addCookie(c);

			request.getSession().setAttribute(VU360Branding.BRAND, paramBrand);

		// and, none of above cases are valid then get the brand by
		// domain
		} else if ((brandDomain = getDomainBrand(request)) != null) {
			
			paramBrand = (brandDomain.getBrand() == null || brandDomain.getBrand().trim().length() == 0)
					? paramBrand : brandDomain.getBrand().trim();

			paramLanguage = (brandDomain.getLanguage() == null || brandDomain.getLanguage().trim().length() == 0)
					? Language.DEFAULT_LANG : brandDomain.getLanguage().trim();

			lang.setLanguage(paramLanguage);

			brander = (Brander) VU360Branding.getInstance().getBrander(paramBrand, lang);
			request.getSession().setAttribute(VU360Branding.BRAND, brander.getName());
			
		// still no luck, try to get it from cookies
		} else if (hasBrandCookie(request, VU360Branding.BRAND) ) {

			brander = getBrandNameFromCookie(request, brander, lang);
			request.getSession().setAttribute(VU360Branding.BRAND, brander.getName());

		// finally, get the default brand
		} else {

			paramBrand = VU360Branding.DEFAULT_BRAND;
			paramLanguage = Language.DEFAULT_LANG;
			lang.setLanguage(paramLanguage);
			brander = (Brander) VU360Branding.getInstance().getBrander(paramBrand, lang);
			request.getSession().setAttribute(VU360Branding.BRAND, brander.getName());

		}
		
		return brander;
	}

	private BrandDomain getDomainBrand(HttpServletRequest request) {
		
		String scheme = request.getScheme();
		String serverName = request.getServerName();

		StringBuffer domain = new StringBuffer();
		domain.append(scheme);
		domain.append("://");
		domain.append(serverName);
		BrandDomain brandDomain = brandDomainService.findBrandByDomain(domain.toString());
		
		return brandDomain;
	}
	
	private Boolean hasBrandCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return false;
		return Arrays.asList(cookies).stream().anyMatch((cookie) -> cookie.getName().equals(VU360Branding.BRAND));		
	}
	
	private Brander getBrandNameFromCookie(HttpServletRequest request, Brander brander, com.softech.vu360.lms.vo.Language lang) {
		String paramLanguage;
		String paramBrand;
		Cookie cookies[] = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals(
						VU360Branding.BRAND)) {
					int lastIndex = cookie.getValue()
							.lastIndexOf(".");
					paramLanguage = cookie.getValue().substring(
							lastIndex + 1);
					paramBrand = cookie.getValue()
							.substring(0, lastIndex);
					lang.setLanguage(paramLanguage);
					brander = (Brander) VU360Branding
							.getInstance().getBrander(paramBrand, lang);
					break;
				}
			}
		}
		return brander;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		log.debug("Inside AcegiVelocityContextInterceptor.preHandle()");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		if (auth!=null && auth.getPrincipal() != null
				&& auth.getPrincipal() instanceof com.softech.vu360.lms.vo.VU360User) {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) auth.getPrincipal();
			
//			if(((VU360User)auth.getPrincipal()).getLearnerProxy() == null){
//				UsernamePasswordAuthenticationToken auths = new UsernamePasswordAuthenticationToken(loggedInUser, loggedInUser.getPassword(), loggedInUser.getAuthorities());
//				auths.setDetails(loggedInUser);
//				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		         SecurityContext context = SecurityContextHolder.getContext();
//		         context.setAuthentication(authentication);
//		         SecurityContextHolder.setContext(context);
//			}
		        
						
			if (handler instanceof ManageSurveyController) {

				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth
						.getDetails();
				if (loggedInUser.isLMSAdministrator()
						&& details.getCurrentCustomer() == null) {
					response.sendRedirect("/lms/adm_searchMember.do");
					return false;

				}
			} else if (handler instanceof ManageSecurityRolesController) {

				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth
						.getDetails();
				if (loggedInUser.isLMSAdministrator()
						&& details.getCurrentCustomer() == null) {
					response.sendRedirect("/lms/adm_searchMember.do");
					return false;

				}

			} else if (handler instanceof AddSecurityRoleController) {
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth
						.getDetails();
				if (loggedInUser.isLMSAdministrator()
						&& details.getCurrentCustomer() == null) {
					response.sendRedirect("/lms/adm_searchMember.do");
					return false;

				}

			}
			//LMS-19305 - Invalid User GUID in LearningSession Table
			else if (handler instanceof LaunchCourseController) {
		    	if(ControllerUtils.isValidUserOrSession(request,response)){
		    		new SecurityContextLogoutHandler().logout(request, response, null);
		    		response.sendRedirect("/lms/login.do");
		    		return false;
		    	}
		}
		}

		return super.preHandle(request, response, handler);

	}

	// this method will be used for highlight featue and feature group.
	// we have to re-think for better alternative

	private boolean handleMenuAuth(com.softech.vu360.lms.vo.VU360User loggedInUser, Object handler,
			HttpServletRequest request) throws Exception {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();
		if (handler instanceof LearnerWidgetDashboardController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "My Dashboard");
			session.setAttribute("feature", "LMS-LRN-0007");
			return true;
		} else
		if (handler instanceof MyCoursesController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "My Courses");
			session.setAttribute("feature", "LMS-LRN-0001");
			return true;

		} else if (handler instanceof LearnerProfileController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "My Profile");
			String contextPath = request.getRequestURI();
			if (contextPath
					.equalsIgnoreCase("/lms/lrn_learnerProfile.do")) {
				session.setAttribute("feature", "LMS-LRN-0003");
			} else if (contextPath
					.equalsIgnoreCase("/lms/lrn_learnerPreferences.do")) {
				session.setAttribute("feature", "LMS-LRN-0004");
			}

			if (UserPermissionChecker.hasAccessToFeature("LMS-LRN-0003", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isTrainingAdministrator() == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof ManageLearnerController
				|| handler instanceof AddLearnerController
				|| handler instanceof EditLearnerPreferencesController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Users & Groups");
			session.setAttribute("feature", "LMS-MGR-0001");
			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0001", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof BatchImportLearnersController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Users & Groups");
			session.setAttribute("feature", "LMS-MGR-0002");

			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0002", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof AddSelfRegistrationInvitationWizardController
				|| handler instanceof SelfRegistrationInvitationController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Users & Groups");
			session.setAttribute("feature", "LMS-MGR-0003");

			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0003", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof ManageOraganizationGroupsController
				|| handler instanceof AddOrganizationGroupController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Users & Groups");
			session.setAttribute("feature", "LMS-MGR-0004");
			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0004", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof ManageLearnerGroupsController
				|| handler instanceof AddLearnerGroupController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Users & Groups");
			session.setAttribute("feature", "LMS-MGR-0005");
			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0005", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof AddSecurityRoleController
				|| handler instanceof ChangeMemberRoleController) {
			HttpSession session = request.getSession();

			if (details.getCurrentMode().compareTo(
					VU360UserMode.ROLE_LMSADMINISTRATOR) == 0) {
				session.setAttribute("featureGroup", "Security");
				session.setAttribute("feature", "LMS-ADM-0011");
			} else {
				session.setAttribute("featureGroup", "Users & Groups");
				session.setAttribute("feature", "LMS-MGR-0006");
			}

			if (UserPermissionChecker.hasAccessToFeature("LMS-ADM-0011", loggedInUser, request.getSession(true)) == false 
					&& UserPermissionChecker.hasAccessToFeature("LMS-MGR-0006", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} 
		else if (handler instanceof AddSecurityRoleWizardController) 
		{
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Users & Groups");
			session.setAttribute("feature", "LMS-MGR-0007");
			if (!UserPermissionChecker.hasAccessToFeature("LMS-MGR-0007", loggedInUser, request.getSession(true)))
				return false;
		} 
		else if (handler instanceof ViewAllEntitlementsController || handler instanceof ViewEntitlementDetailsController) 
		{
			if (!UserPermissionChecker.hasAccessToFeature("LMS-MGR-0011", loggedInUser, request.getSession(true)))
				return false;
		} 
		else if (handler instanceof ViewLearnerEnrollmentController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Plan & Enroll");
			session.setAttribute("feature", "ManageEnrollments");
			if (UserPermissionChecker.hasAccessToFeature("ManageEnrollments", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof ManageEnrollmentController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Plan & Enroll");
			session.setAttribute("feature", "LMS-MGR-0008");
			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0008", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof ManageCustomCoursesController
				|| handler instanceof AddCustomCoursesWizardController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Plan & Enroll");
			session.setAttribute("feature", "LMS-MGR-0010");

			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0010", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof SearchAndManageTrainingPlanController
				|| handler instanceof AddTrainingPlanWizardController) {

/*			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Plan & Enroll");
			session.setAttribute("feature", "LMS-MGR-0009");
*/			
			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0009", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof AddTrainingPlanControllerStep2) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Plan & Enroll");
			session.setAttribute("feature", "LMS-MGR-0009");  //Rehan Rana - LMS-11137 - Set the menu option

			if (!UserPermissionChecker.hasAccessToFeature("LMS-MGR-0011", loggedInUser, request.getSession(true)))
				return false;
			
		} else if (handler instanceof ManageSurveyController
				|| handler instanceof ManageSurveyWizardController) {
			HttpSession session = request.getSession();
			
			if(details.getCurrentMode().compareTo(VU360UserMode.ROLE_LMSADMINISTRATOR)==0) {
				if( session.getAttribute("feature").toString().endsWith("LearnerSurveyResponse") ) {					
					session.setAttribute("featureGroup","Tools");
					session.setAttribute("feature","LearnerSurveyResponse");
				}
				else {
					session.setAttribute("featureGroup","Tools");
					session.setAttribute("feature","LMS-ADM-0008");
				}
			}else {
				if( session.getAttribute("feature").toString().endsWith("LearnerSurveyResponse") ) {					
					session.setAttribute("featureGroup","Tools");
					session.setAttribute("feature","LearnerSurveyResponse");
				}
				else {
					session.setAttribute("featureGroup","Tools");
					session.setAttribute("feature","LMS-MGR-0020");					
				}
			
			}
			if(UserPermissionChecker.hasAccessToFeature("LMS-MGR-0020", loggedInUser, request.getSession(true))== false && loggedInUser.isLMSAdministrator()==false){
				
				return false;

			}
		} else if (handler instanceof ReportController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Reports");
			session.setAttribute("feature", "Reports");

			if (UserPermissionChecker.hasAccessToFeature("Reports", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof EditManagerProfileController) {
			String methodname = request.getParameter("method");

			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Profile");
			if (methodname.equalsIgnoreCase("editCustomerProfile")) {
				session.setAttribute("feature", "LMS-MGR-0023");
				if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0023", loggedInUser, request.getSession(true)) == false
						&& loggedInUser.isLMSAdministrator() == false) {

					return false;

				}
			} else {
				session.setAttribute("feature", "LMS-MGR-0024");
				if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0024", loggedInUser, request.getSession(true)) == false
						&& loggedInUser.isLMSAdministrator() == false) {

					return false;

				}
			}

		} 
		else if (handler instanceof ManageEmailController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Tools");
			session.setAttribute("feature", "");

			/*
			 * LMS-10740 | S M Humayun | 30 Jun 2011
			 */
			if (!UserPermissionChecker.hasAccessToFeature("LMS-ADM-0006", loggedInUser, request.getSession(true)) && !UserPermissionChecker.hasAccessToFeature("LMS-MGR-0018", loggedInUser, request.getSession(true)))
				return false;
		}
		
		// //////////////////////////////////////
		else if (handler instanceof EditCustomerController) {
			HttpSession session = request.getSession();
			String methodname = request.getParameter("method");
			session.setAttribute("featureGroup", "Customers");
			if ( methodname.equalsIgnoreCase("editCustomerProfile") || 
					methodname.equalsIgnoreCase("saveCustomerProfile")) {
				session.setAttribute("feature", "LMS-ADM-0013");
				if (UserPermissionChecker.hasAccessToFeature("LMS-ADM-0013", loggedInUser, request.getSession(true)) == false) {

					return false;

				}
			} else {
				session.setAttribute("feature", "LMS-ADM-0014");
				if (UserPermissionChecker.hasAccessToFeature("LMS-ADM-0014", loggedInUser, request.getSession(true)) == false) {

					return false;

				}
			}
			// loggedInUser.

		} else if (handler instanceof AddNewCustomerWizardController) {
			HttpSession session = request.getSession();

			session.setAttribute("featureGroup", "Customers");
			session.setAttribute("feature", "LMS-ADM-0012");

			// loggedInUser.
			if (UserPermissionChecker.hasAccessToFeature("LMS-ADM-0012", loggedInUser, request.getSession(true)) == false) {

				return false;

			}
		} else if (handler instanceof UpdateEntitlementDetailsController) {
			HttpSession session = request.getSession();

			session.setAttribute("featureGroup", "Customers");
			session.setAttribute("feature", "LMS-ADM-0015");

			// loggedInUser.
			if (UserPermissionChecker.hasAccessToFeature("LMS-ADM-0015", loggedInUser, request.getSession(true)) == false) {

				return false;

			}
		} else if (handler instanceof EditDistributorProfileController) {

/*			HttpSession session = request.getSession();
			String contextPath = request.getRequestURI();
			session.setAttribute("featureGroup", "Resellers");
			if (contextPath
					.equalsIgnoreCase("/lms/adm_editDistributorPreferences.do")) {
				session.setAttribute("feature", "LMS-ADM-0020");
			} else if (contextPath
					.equalsIgnoreCase("/lms/adm_editDistributorProfile.do")) {
				session.setAttribute("feature", "LMS-ADM-0019");
			}
*/
			// loggedInUser.
			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0020", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof ManageDistributorController) {
			HttpSession session = request.getSession();

			session.setAttribute("featureGroup", "Resellers");
			session.setAttribute("feature", "ManageDistributors");

			// loggedInUser.
			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0020", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof ManageDistributorGroupWizardController) {
			HttpSession session = request.getSession();

			session.setAttribute("featureGroup", "Resellers");
			session.setAttribute("feature", "LMS-ADM-0027");

			// loggedInUser.
			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0020", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		} else if (handler instanceof ManageDistributorEntitlementController) {
/*			HttpSession session = request.getSession();

			session.setAttribute("featureGroup", "Resellers");
			session.setAttribute("feature", "LMS-ADM-0022");
*/
			// loggedInUser.
			if (UserPermissionChecker.hasAccessToFeature("LMS-MGR-0020", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		}
		else if (handler instanceof ManageCustomFieldsController) {
			HttpSession session = request.getSession();
			String entity = request.getParameter("entity") ;
			String type = "";
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				 details = (VU360UserAuthenticationDetails)auth.getDetails();		
				type = details.getCurrentSearchType().toString();
			} 
			if(     type.equalsIgnoreCase(CustomFieldForm.CUSTOMER) ||  type.equalsIgnoreCase("distributor")  ) {
				
				if(    type.equalsIgnoreCase(CustomFieldForm.CUSTOMER)){ // if entity belongs to customer
					session.setAttribute("featureGroup", "Customers");
					session.setAttribute("feature", "LMS-ADM-0017");
				}
				else if(   type.equalsIgnoreCase("distributor")){ // if entity belongs to reseller
					session.setAttribute("featureGroup", "Resellers");
					session.setAttribute("feature", "LMS-ADM-0029");
				}

			}
			

			//if(request.getParameter("").equalsIgnoreCase("customer"))

			// loggedInUser.
			if (UserPermissionChecker.hasAccessToFeature("LMS-ADM-0029", loggedInUser, request.getSession(true)) == false
					&& loggedInUser.isLMSAdministrator() == false) {

				return false;

			}
		}
		else if (handler instanceof ManageAlertController) {
			HttpSession session = request.getSession();
			
			if(details.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)) {
				session.setAttribute("feature", "LMS-ADM-0005");
			} else if(details.getCurrentMode().equals(VU360UserMode.ROLE_TRAININGADMINISTRATOR)){			
				session.setAttribute("feature", "LMS-MGR-0017");
			}
			
		}
		else if (handler instanceof ManageMyAlertController) {
			HttpSession session = request.getSession();
			
				session.setAttribute("feature", "myAlert");
			
		} else if(handler instanceof ProctorCertificateCompletionController) {
			HttpSession session = request.getSession();
			session.setAttribute("featureGroup", "Completions");
		}
		// /////////////////////////////////////

		return true;

	}

	public BrandDomainService getBrandDomainService() {
		return brandDomainService;
	}

	public void setBrandDomainService(BrandDomainService brandDomainService) {
		this.brandDomainService = brandDomainService;
	}

	/**
	 * @return the vu360UserService
	 */
	public VU360UserServiceImpl getVu360UserService() {
		return vu360UserService;
	}

	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserServiceImpl vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public AuthorService getAuthorService() {
		return authorService;
	}

	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

}
