package com.softech.vu360.lms.web.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.util.Assert;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.model.Menu;
import com.softech.vu360.util.RedirectUtils;
/**
 * @author Somnath
 *
 */
public class VU360UserModeSetupFilter extends SwitchUserFilter implements InitializingBean,
ApplicationEventPublisherAware, MessageSourceAware {
	
	public static final int FILTER_CHAIN_FIRST = Ordered.HIGHEST_PRECEDENCE + 1000;
	private static final int INTERVAL = 100;

	private Logger log = Logger.getLogger(VU360UserModeSetupFilter.class.getName());
    private static final String SPRING_SECURITY_SWITCH_USERNAME_KEY = "username";
    private static final String ROLE_PREVIOUS_ADMINISTRATOR = "ROLE_PREVIOUS_ADMINISTRATOR";

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private ApplicationEventPublisher eventPublisher;
    //private AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private boolean useRelativeContext;

    //private String userEntryURL;
    //private String userEntryTargetURL;
    //private String userEntryFailureURL;

    //following urls for switching mode
    private String adminSwitchManagerModeURL;
    private String adminSwitchManagerModeTargetURL;
    private String adminSwitchManagerModeFailureURL;
    
    private String adminSwitchLearnerModeURL;
    private String adminSwitchLearnerModeTargetURL;
    private String adminSwitchLearnerModeFailureURL;
    
    private String adminSwitchBackURL;
    private String adminSwitchBackTargetURL;
    private String adminSwitchBackFailureURL;
    
    private String managerSwitchLearnerModeURL;
    private String managerSwitchLearnerModeTargetURL;
    private String managerSwitchLearnerModeFailureURL;
    
    private String managerSwitchBackFromLearnerModeURL;
    private String managerSwitchBackFromLearnerModeTargetURL;
    private String managerSwitchBackFromLearnerModeFailureURL;
    private String adminSwitchBackFromManagerURL;
    
    //following urls for switch from manager user to learner user
    //these will switch the principal in the security context
    private String managerSwitchURL;
    private String managerSwitchTargetURL;
    private String managerSwitchFailureURL;
    
    private String managerSwitchBackURL;
    private String managerSwitchBackTargetURL;
    private String managerSwitchBackFailureURL;
    
    // for instructor mode switching 
    private String switchInstructorModeURL;
    private String switchInstructorModeTargetURL;
    private String switchInstructorModeFailureURL;
    
    private String accreditationSwitchLearnerModeURL;
    private String accreditationSwitchLearnerModeTargetURL;
    private String accreditationSwitchBackFromLearnerModeURL;
    private String accreditationSwitchBackFromLearnerModeTargetURL;
    
    private String accreditationSwitchManagerModeURL;
    private String accreditationSwitchManagerModeTargetURL;
    private String accreditationSwitchBackFromManagerModeURL;
    private String accreditationSwitchBackFromManagerModeTargetURL;
    
    private String accreditationSwitchAdminModeURL;
    private String accreditationSwitchAdminModeTargetURL;
    private String accreditationSwitchBackFromAdminModeURL;
    private String accreditationSwitchBackFromAdminModeTargetURL;
    private String accreditationSwitchFailureURL;
    private String accreditationSwitchBackFromLearnerModeFailureURL;
    private String accreditationSwitchLearnerModeFailureURL;
    
    private String accreditationSwitchBackFromManagerModeFailureURL;
    private String accreditationSwitchManagerModeFailureURL;
    
    private String accreditationSwitchBackFromAdminModeFailureURL;
    private String accreditationSwitchAdminModeFailureURL;
    private String accreditationSwitchURL;
    private String accreditationSwitchTargetURL;
    
    
    //Proctor URLS
    private String switchProctorModeURL;
    private String switchProctorModeTargetURL;
    private String switchProctorModeFailureURL;
    
    private UserDetailsService userDetailsService;
    private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
    private LearnerService learnerService;
    private VU360UserService vu360UserService;
    private CustomerService customerService;

	private SecurityAndRolesService securityService = null;
	
	public void afterPropertiesSet()  {
		//Assert.hasLength(userEntryURL, "User Entry URL must be specified");
		//Assert.hasLength(userEntryTargetURL, "User Entry Target URL must be specified");
		
		Assert.hasLength(adminSwitchManagerModeURL, "Admin Switch to Manager Mode URL must be specified");
		Assert.hasLength(adminSwitchManagerModeTargetURL, "Admin Switch to Manager Mode Target URL must be specified");
		
		Assert.hasLength(adminSwitchLearnerModeURL, "Admin Switch to Learner Mode URL must be specified");
		Assert.hasLength(adminSwitchLearnerModeTargetURL, "Admin Switch to Learner Mode Target URL must be specified");

		Assert.hasLength(adminSwitchBackURL, "Admin Switch Back to OWN Mode URL must be specified");
		Assert.hasLength(adminSwitchBackTargetURL, "Admin Switch Back to OWN Mode Target URL must be specified");
		
		Assert.hasLength(managerSwitchLearnerModeURL, "Manager Switch to Learner Mode URL must be specified");
		Assert.hasLength(managerSwitchLearnerModeTargetURL, "Manager Switch to Learner Mode Target URL must be specified");
		
		Assert.hasLength(managerSwitchBackFromLearnerModeURL, "Manager Switch back from Learner Mode URL must be specified");
		Assert.hasLength(managerSwitchBackFromLearnerModeTargetURL, "Manager Switch back from Learner Mode Target URL must be specified");
		
		////////////////////
		Assert.hasLength(managerSwitchURL, "Manager Switch to Learner User URL must be specified");
		Assert.hasLength(managerSwitchTargetURL, "Manager Switch to Learner User Target URL must be specified");
		
		Assert.hasLength(managerSwitchBackURL, "Manager Switch Back to OWN User URL must be specified");
		Assert.hasLength(managerSwitchBackTargetURL, "Manager Switch Back to OWN User Target URL must be specified");
		
		Assert.hasLength(accreditationSwitchLearnerModeURL, "Accreditation Switch to Learner Mode URL must be specified");
		Assert.hasLength(accreditationSwitchLearnerModeTargetURL, "Accreditation Switch to Learner Mode Target URL must be specified");
		
		Assert.hasLength(accreditationSwitchBackFromLearnerModeURL, "Accreditation Switch back from Learner Mode URL must be specified");
		Assert.hasLength(accreditationSwitchBackFromLearnerModeTargetURL, "Accreditation Switch back from Learner Mode Target URL must be specified");
		
		Assert.hasLength(switchInstructorModeURL, "Switching to Instructor Mode must be specified");
		Assert.hasLength(switchInstructorModeTargetURL, "Switching to Instructor Mode must be specified");
		Assert.hasLength(switchInstructorModeFailureURL, "Switching to Instructor Mode must be specified");
		
		Assert.hasLength(switchProctorModeURL, "Switching to Proctor Mode must be specified");
		Assert.hasLength(switchProctorModeTargetURL, "Switching to Proctor Mode must be specified");
		Assert.hasLength(switchProctorModeFailureURL, "Switching to Proctor Mode must be specified");
		
		Assert.notNull(userDetailsService, "authenticationDao must be specified");
		Assert.notNull(messages, "A message source must be set");

	}

	/**
	 * The standard spring filters are ordered with a gap of 100 between each position
	 * So we can insert custom filters in these gaps
	 * @see org.springframework.core.Ordered#getOrder()
	 */
	public int getOrder() {
		//return FilterChainOrder.FILTER_SECURITY_INTERCEPTOR + 10; //just after the FILTER_SECURITY_INTERCEPTOR and before the SWITCH_USER_FILTER
		return FILTER_CHAIN_FIRST + INTERVAL * 13;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);		
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
	}
/*
    public void setAuthenticationDetailsSource(AuthenticationDetailsSource authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }
*/
//	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
		  
		
		/*
		//if(matchesURI(request,this.getSuccessfulLoginTargetUrl(request))){ //user trying to login and setup the details object
		if(isFirstSuccessfulLoginEvent(request)){ //user trying to login and setup the details object
			try {
				log.debug("We are trying to setup the User Details");
				this.attemptSetupUser(request);
				//redirect to target url
                //sendRedirect(request, response, this.userEntryTargetURL);
				
				//initializing the switch user flag here
				//this was done previously in the login interceptor
				HttpSession session = request.getSession(false);
				session.setAttribute("hasSwitchedAuth", new Boolean(false));
			} catch (Exception e) {
				//this.redirectToFailureUrl(request, response, this.userEntryFailureURL, e);
				e.printStackTrace();//???
			}
			//return;
		}else 
		*/	
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

		if(currentAuth!=null){
			Object objDetails = currentAuth.getDetails();
			if(objDetails!=null && objDetails instanceof VU360UserAuthenticationDetails){
				VU360UserAuthenticationDetails userDetails = (VU360UserAuthenticationDetails)objDetails;

				//only called in clustered environment
				//setClusteredObjects(request, userDetails);
				
				if( !StringUtils.isBlank(userDetails.getSwitchBackUrl()) ){
					request.setAttribute("switchBackUrl", userDetails.getSwitchBackUrl());
				}
				
				String switchBackReuest=request.getParameter("switchBackReuest");
				if(switchBackReuest!=null && switchBackReuest.trim().length()>0){
					try {
						userDetails.setSwitchBackUrl(null);
						this.attemptAdminSwitchBack(request);
						((com.softech.vu360.lms.vo.VU360User)currentAuth.getPrincipal()).setLogInAsManagerRole(null);//LMS-4742
						HttpSession ssn=request.getSession(true);
						ssn.removeAttribute("loggedInAsManager");
						ssn.removeAttribute("distributorFeatureGroup");
						ssn.removeAttribute("distributorFeature");
						//ENGSUP-25905. Fix is for switch back issue when opening multiple tabs
						String switchBackUrl = (String)request.getAttribute("switchBackUrl");
						String adminSwitchBackTargetURLStr = (adminSwitchBackTargetURL != null ? (adminSwitchBackTargetURL.indexOf("?") < 0) ? 
																								adminSwitchBackTargetURL+"?method="+request.getParameter("method") : adminSwitchBackTargetURL+"&method="+request.getParameter("method") : "/");
						sendRedirect(request, response, switchBackUrl != null ? switchBackUrl : adminSwitchBackTargetURLStr);
					} catch (Exception e) {
						this.redirectToFailureUrl(request, response, this.adminSwitchBackFailureURL, e);
					}
				}
				
				Object objInitedClustered=request.getSession(false).getAttribute("initedClustered");
				// following check will be PASSED if one node down (because of any reason) and user switched to another node by balancer
				if(!userDetails.getInited() && objInitedClustered!=null)
				{
					try{
                        logger.info("\n\n......Setting setInited for UserDetails......objInitedClustered="+objInitedClustered.toString());
						userDetails.setInited(Boolean.valueOf((objInitedClustered.toString())));
					}catch(Exception subEx){
						log.error("....initedClustered casting error..." + subEx);
					}
				}
				
				if(!userDetails.getInited()){
                    logger.info("\n\n......Initializing user for Current Mode.......objInitedClustered="+objInitedClustered);
					userDetails.doInitializeUser((com.softech.vu360.lms.vo.VU360User)currentAuth.getPrincipal());//call only once
					HttpSession session = request.getSession(false);
					if(session!=null) {
						session.setAttribute("hasSwitchedAuth", Boolean.FALSE);
						userDetails.setCurrentInitedClustered(request);
					}
										
				}else{
					if(matchesURI(request,this.adminSwitchManagerModeURL)){ //admin trying to switch to manager and setup the details object
						try {
							this.attemptAdminSwitchToManager(request);
							
							//redirect to target url
							String switchBackUrl=request.getParameter("switchBackUrl");
							userDetails.setSwitchBackUrl(switchBackUrl);
							//LMS-4742
							com.softech.vu360.lms.vo.VU360User loggedInUser=(com.softech.vu360.lms.vo.VU360User)currentAuth.getPrincipal();
							Customer cust=userDetails.getCurrentCustomer();
							LMSRole loginAsManagerRole=securityService.getFicticousRoleForLoginAsManager(cust);
							loggedInUser.setLogInAsManagerRole(ProxyVOHelper.createLMSRoleVO(loginAsManagerRole));
//							HttpSession ssn=request.getSession(true);
//							Distributor dist=cust.getDistributor();
//							List<LMSFeature> features=dist.getFeatures();
//							List featureGroup=new ArrayList();
//							List featureList=new ArrayList();
//							for(LMSFeature feature:features){
//								if(!featureGroup.contains(feature.getFeatureGroup())){
//									featureGroup.add(feature.getFeatureGroup());
//								}
//								if(!featureList.contains(feature.getFeatureCode())){
//									featureList.add(feature.getFeatureCode());
//								}
//							}
//							List permission=new ArrayList();
//							ssn.setAttribute("distributorFeature",featureList);
//							ssn.setAttribute("distributorFeatureGroup",featureGroup);
							sendRedirect(request, response, this.adminSwitchManagerModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.adminSwitchManagerModeFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.adminSwitchLearnerModeURL)){ //admin trying to switch to learner and setup the details object
						try {
							
							
							//redirect to target url
							String switchBackUrl=request.getParameter("switchBackUrl");
							userDetails.setSwitchBackUrl(switchBackUrl);
							//TODO these line of codes not available in 4.5.2 branch
							HttpSession session=request.getSession(false);
							Object currentAuthDetails = currentAuth.getDetails();
					        session.removeAttribute("isAdminSwitch");
					        
							Menu menu=new Menu((com.softech.vu360.lms.vo.VU360User)currentAuth.getPrincipal(),request);
							if(menu.redirectingToAdmin())
							{
								log.debug("redirecting user to admin mode");
							}
							else if (menu.redirectingToManager())
							{
								this.attemptAdminSwitchToManager(request);
							}
							else if(menu.redirectingToLearner())
							{
								this.attemptAdminSwitchToLearner(request);	
							}
							
							String url=menu.getUrl();
							if(url.isEmpty())
							{
								//sendRedirect(request, response, "/j_spring_security_logout");
								sendRedirect(request, response, "/logout");
							}
							else
								sendRedirect(request, response, "/"+url);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.adminSwitchLearnerModeFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.adminSwitchBackURL)){ //admin trying to switch back to admin and setup the details object
						try {
							this.attemptAdminSwitchBack(request);
							//redirect to target url
							sendRedirect(request, response, this.adminSwitchBackTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.adminSwitchBackFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.managerSwitchLearnerModeURL)){//manager trying to switch to learner mode with own principal
						try {
							this.attemptManagerSwitchToLearnerMode(request);
							//redirect to target url
							String switchBackUrl=request.getParameter("switchBackUrl");
							userDetails.setSwitchBackUrl(switchBackUrl);
							if((request.getParameter("managerSwitchLearnerModeTargetURL") != null) && 
									(!request.getParameter("managerSwitchLearnerModeTargetURL").equals(StringUtils.EMPTY))) {
								this.managerSwitchLearnerModeTargetURL = URLDecoder.decode(request.getParameter("managerSwitchLearnerModeTargetURL"), "UTF-8");;
							}
							sendRedirect(request, response, this.managerSwitchLearnerModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.managerSwitchLearnerModeFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.managerSwitchBackFromLearnerModeURL)){ //manager trying to switch back from learner mode, principal remaining same
						try {
							this.attemptManagerSwitchBackFromLearnerMode(request);
							//redirect to target url
							HttpSession session=request.getSession(false);;
							Object currentAuthDetails = currentAuth.getDetails();
					        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
					        	VU360UserAuthenticationDetails managerDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
					        		session.setAttribute("isAdminSwitch", new Boolean(true));
					        }
							String redireURL = getManagerRedirectURL(request) ;
					        sendRedirect(request, response, redireURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.managerSwitchBackFromLearnerModeFailureURL, e);
						}
						return;

						//following block for switch user by manager
					}else if(matchesURI(request,this.managerSwitchURL)){//manager trying to switch to learner mode with learner principal
						try {
							
							HttpSession session = request.getSession(false);
							
							String switchBackUrl=request.getParameter("switchBackUrl");
							if(StringUtils.isBlank(switchBackUrl)){
								if(request.getAttribute("switchBackUrl")!=null){
									switchBackUrl=request.getAttribute("switchBackUrl").toString();
								}
							}
							userDetails.setSwitchBackUrl(switchBackUrl);
							
					        Object currentAuthDetails = currentAuth.getDetails();
					        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
					        	VU360UserAuthenticationDetails managerDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
					        	if(managerDetail.getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR)){
					        		session.setAttribute("isAdminSwitch", Boolean.TRUE);
					        	}
					        	else{
					        		session.setAttribute("isAdminSwitch", Boolean.FALSE);
					        	}
					        }
					        
							this.attemptManagerSwitchToLearnerUser(request);

							//setting the switch user flag to true here
							//this was done previously in the login interceptor
							session.setAttribute("hasSwitchedAuth", Boolean.TRUE);
			
							// LMS-3241 code added to remove the course catalog cache from session
							request.getSession().removeAttribute("_courseCatalog");

							//redirect to target url
							sendRedirect(request, response, this.managerSwitchTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.managerSwitchFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.managerSwitchBackURL)){ //manager trying to switch back from learner mode and regain own principal
						try {
							com.softech.vu360.lms.vo.VU360User currentLearnerUser = (com.softech.vu360.lms.vo.VU360User)currentAuth.getPrincipal();
							HttpSession session = request.getSession(false);
							this.attemptManagerSwitchBackFromLearnerUser(request);

							//setting the switch user flag to false here
							//this was done previously in the login interceptor
							session.setAttribute("hasSwitchedAuth", new Boolean(false));
							// LMS-3241 code added to remove the course catalog cache from session
							request.getSession().removeAttribute("_courseCatalog");
							//redirect to target url
							String finalTargetUrl = this.managerSwitchBackTargetURL+"?method=displayUser&userId="+currentLearnerUser.getId();
							if(session.getAttribute("isAdminSwitch")!=null){
								if((Boolean)session.getAttribute("isAdminSwitch")){
									this.attemptAdminSwitchBack(request);
									finalTargetUrl=userDetails.getSwitchBackUrl();
								}
							}
							sendRedirect(request, response, finalTargetUrl);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.managerSwitchBackFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.switchInstructorModeURL)){//instructor trying to switch to learner mode with learner principal
						try {
							log.debug("switchInstructorModeURL: " + this.switchInstructorModeURL);
							//this.attemptManagerSwitchBackFromLearnerMode(request);
							this.attemptInstructorSwitchBackFromLearnerMode(request);
							//redirect to target url
							log.debug("switchInstructorModeTargetURL: " + this.switchInstructorModeTargetURL);
							sendRedirect(request, response, this.switchInstructorModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.switchInstructorModeFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.switchProctorModeURL)){//instructor trying to switch to learner mode with learner principal
						try {
							log.debug("switchProctorModeURL: " + this.switchProctorModeURL);
							this.attemptProctorSwitchBackFromLearnerMode(request);
							//redirect to target url
							log.debug("switchProctorModeTargetURL: " + this.switchProctorModeTargetURL);
							sendRedirect(request, response, this.switchProctorModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.switchInstructorModeFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.accreditationSwitchURL)){//accreditation trying to switch to learner mode with learner principal
						try {
							this.attemptManagerSwitchToLearnerUser(request);

							//setting the switch user flag to true here
							//this was done previously in the login interceptor
							HttpSession session = request.getSession(false);
							session.setAttribute("hasSwitchedAuth", new Boolean(true));

							//redirect to target url
							sendRedirect(request, response, this.accreditationSwitchTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.accreditationSwitchFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.accreditationSwitchLearnerModeURL)){//accreditation trying to switch to learner mode with own principal
						try {
							this.attemptAccreditationSwitchToLearnerMode(request);
							//redirect to target url
							sendRedirect(request, response, this.accreditationSwitchLearnerModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.accreditationSwitchLearnerModeFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.accreditationSwitchBackFromLearnerModeURL)){ //accreditation trying to switch back from learner mode, principal remaining same
						try {
							this.attemptAccreditationSwitchBackFromLearnerMode(request);
							//redirect to target url
							sendRedirect(request, response, this.accreditationSwitchBackFromLearnerModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.accreditationSwitchBackFromLearnerModeFailureURL, e);
						}
						return;

						//following block for switch user by manager
					}else if(matchesURI(request,this.accreditationSwitchManagerModeURL)){//accreditation trying to switch to learner mode with own principal
						try {
							this.attemptAccreditationSwitchToLearnerMode(request);
							//redirect to target url
							sendRedirect(request, response, this.accreditationSwitchManagerModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.accreditationSwitchManagerModeFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.accreditationSwitchBackFromManagerModeURL)){ //accreditation trying to switch back from learner mode, principal remaining same
						try {
							this.attemptAccreditationSwitchBackFromLearnerMode(request);
							//redirect to target url
							sendRedirect(request, response, this.accreditationSwitchBackFromManagerModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.accreditationSwitchBackFromManagerModeFailureURL, e);
						}
						return;

						//following block for switch user by manager
					}else if(matchesURI(request,this.accreditationSwitchAdminModeURL)){//accreditation trying to switch to learner mode with own principal
						try {
							this.attemptAccreditationSwitchToLearnerMode(request);
							//redirect to target url
							sendRedirect(request, response, this.accreditationSwitchAdminModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.accreditationSwitchAdminModeFailureURL, e);
						}
						return;
					}else if(matchesURI(request,this.accreditationSwitchBackFromAdminModeURL)){ //accreditation trying to switch back from learner mode, principal remaining same
						try {
							this.attemptAccreditationSwitchBackFromAdminMode(request);
							//redirect to target url
							sendRedirect(request, response, this.accreditationSwitchBackFromAdminModeTargetURL);
						} catch (Exception e) {
							this.redirectToFailureUrl(request, response, this.accreditationSwitchBackFromAdminModeFailureURL, e);
						}
						return;

						//following block for switch user by manager
					}
				}
			}
		}
		chain.doFilter(request, response);
	}
/*
	private boolean isFirstSuccessfulLoginEvent(HttpServletRequest request){
		String hasNotYetLoggedInMarkerInSessionKey = "com.softech.vu360.lms.web.filter.VU360UserModeSetupFilter.PRINCIPALNOTAVAILABLE";
		HttpSession session = request.getSession(false);
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		if(session!=null){
			//trying to identify that the request for a secure resource is unauthenticated
			boolean hasPrincipalInSecurityContext =(currentAuth.getPrincipal()!=null && currentAuth.getPrincipal() instanceof VU360User );
			boolean hasNotYetLoggedInMarkerInSession = session.getAttribute(hasNotYetLoggedInMarkerInSessionKey)==null ? false : true;
		 
			//in case of first request for a secure resource when unauthenticated
			if(!hasNotYetLoggedInMarkerInSession && !hasPrincipalInSecurityContext){
				session.setAttribute(hasNotYetLoggedInMarkerInSessionKey, new Boolean(true));
				return false;
			}
			//in case of subsequent request for a secure resource when unauthenticated 
			if(hasNotYetLoggedInMarkerInSession && !hasPrincipalInSecurityContext){
				return false;
			}
			//in case of the request for a secure resource just authenticated - in this http request
			if(hasNotYetLoggedInMarkerInSession && hasPrincipalInSecurityContext){
				session.removeAttribute(hasNotYetLoggedInMarkerInSessionKey);
				return true;
			}
		}
		return false;
	}
	
	private String getSuccessfulLoginTargetUrl(HttpServletRequest request){
		String hasSavedRequestInSessionKey = "com.softech.vu360.lms.web.filter.VU360UserModeSetupFilter.HASSAVEDREQUEST";
		String savedUrl = AbstractProcessingFilter.obtainFullSavedRequestUrl(request);
		
		String requestedUrl = request.getRequestURI();
		String successLoginUrl = this.userEntryURL;
		log.debug("Saved Requested URL = "+savedUrl+" | Current Requested URL = "+requestedUrl);

		HttpSession session = request.getSession(false);
		if(session!=null){
			//trying to identify that the request for a secure resource is unauthenticated
			boolean haveSavedRequestFromAbstractProcessingFilter = savedUrl==null ? false : true;
			boolean haveSavedRequestMarker = session.getAttribute(hasSavedRequestInSessionKey)==null ? false : true;

			//in case of first request for a secure resource when unauthenticated
			if(!haveSavedRequestMarker && haveSavedRequestFromAbstractProcessingFilter){
				log.debug("0. We will save the Saved Request as = "+savedUrl);
				int idx = savedUrl.indexOf("?");
				if (idx > 0) 
					savedUrl = savedUrl.substring(0,idx);
				log.debug("1. We will save the Saved Request as = "+savedUrl);
				savedUrl = stripUri(savedUrl);
				log.debug("2. We will save the Saved Request as = "+savedUrl);
				idx = savedUrl.lastIndexOf(request.getContextPath());
				savedUrl = savedUrl.substring(idx);
				log.debug("3. We will save the Saved Request as = "+savedUrl);
				idx = savedUrl.lastIndexOf("/");
				savedUrl = savedUrl.substring(idx);
				log.debug("4. We will save the Saved Request as = "+savedUrl);
				
				session.setAttribute(hasSavedRequestInSessionKey, savedUrl);
				successLoginUrl = savedUrl;
				log.debug("We have Saved Request From AbstractProcessingFilter BUT DO NOT have Saved Request Marker");
			}
			//in case of subsequent request for a secure resource when unauthenticated 
			if(haveSavedRequestMarker && haveSavedRequestFromAbstractProcessingFilter){
				successLoginUrl = (String)session.getAttribute(hasSavedRequestInSessionKey);
				log.debug("We have Saved Request From AbstractProcessingFilter AND ALSO have Saved Request Marker");
			}
			//in case of the request for a secure resource just authenticated - in this http request
			if(haveSavedRequestMarker && !haveSavedRequestFromAbstractProcessingFilter){
				successLoginUrl = (String)session.getAttribute(hasSavedRequestInSessionKey);
				session.removeAttribute(hasSavedRequestInSessionKey);
				log.debug("We DO NOT have Saved Request From AbstractProcessingFilter BUT have Saved Request Marker");
			}
		}
		return successLoginUrl;
	}
	
	private void attemptSetupUser(HttpServletRequest request) throws Exception{
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		if(currentAuth.getPrincipal()!=null && currentAuth.getPrincipal() instanceof VU360User ){
			Object currentUser = currentAuth.getPrincipal();
			Object currentCredentials = currentAuth.getCredentials();
			Object currentAuthDetails = currentAuth.getDetails();
			GrantedAuthority[] currentAuths = currentAuth.getAuthorities();

			//UsernamePasswordAuthenticationToken targetUser;
			// create the new authentication token
			//targetUser = new UsernamePasswordAuthenticationToken(currentUser, currentCredentials, currentAuths);

			//This is the only place where we create the details object
			Object targetDetail = authenticationDetailsSource.buildDetails(request);//ONLY PLACE WE DO THIS!!!

			if(targetDetail !=null && targetDetail instanceof VU360UserAuthenticationDetails){
				VU360UserAuthenticationDetails userDetails = (VU360UserAuthenticationDetails)targetDetail;
				request.setAttribute("OriginalUser", currentUser);
				userDetails.doInitializeUser(request);
				targetUser.setDetails(userDetails); 
			}
			targetUser.setDetails(targetDetail);
			//update the current context to the new target user
//			SecurityContextHolder.getContext().setAuthentication(targetUser);
		}
	}
 */

	private void attemptAdminSwitchToManager(HttpServletRequest request) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Object currentAuthDetails = currentAuth.getDetails();
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("AdminSwitchMode", VU360UserMode.ROLE_TRAININGADMINISTRATOR);
        	VU360UserAuthenticationDetails adminDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	adminDetail.doAdminSwitchMode(request);
        }
	}

	private void attemptAdminSwitchToLearner(HttpServletRequest request) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Object currentAuthDetails = currentAuth.getDetails();
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("AdminSwitchMode", VU360UserMode.ROLE_LEARNER);
        	VU360UserAuthenticationDetails adminDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	adminDetail.doAdminSwitchMode(request);
        }
	}

	private void attemptAdminSwitchBack(HttpServletRequest request) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Object currentAuthDetails = currentAuth.getDetails();
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("AdminSwitchMode", VU360UserMode.ROLE_LMSADMINISTRATOR);
        	VU360UserAuthenticationDetails adminDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	adminDetail.doAdminSwitchMode(request);
        }
	}

	
	private void attemptInstructorSwitchBackFromLearnerMode(HttpServletRequest request) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Object currentAuthDetails = currentAuth.getDetails();
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("InstructorSwitchMode", VU360UserMode.ROLE_INSTRUCTOR);
        	VU360UserAuthenticationDetails instructorDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	instructorDetail.doInstructorSwitchMode(request);
        }
	}
	
	private void attemptProctorSwitchBackFromLearnerMode(HttpServletRequest request) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Object currentAuthDetails = currentAuth.getDetails();
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("ProctorSwitchMode", VU360UserMode.ROLE_PROCTOR);
        	VU360UserAuthenticationDetails proctorDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	proctorDetail.doProctorSwitchMode(request);
        }
	}
	
	

	private void attemptManagerSwitchBackFromLearnerMode(HttpServletRequest request) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Object currentAuthDetails = currentAuth.getDetails();
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("ManagerSwitchMode", VU360UserMode.ROLE_TRAININGADMINISTRATOR);
        	VU360UserAuthenticationDetails managerDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	managerDetail.doManagerSwitchMode(request);
        }
	}

	private void attemptManagerSwitchToLearnerMode(HttpServletRequest request) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        Object currentAuthDetails = currentAuth.getDetails();
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("ManagerSwitchMode", VU360UserMode.ROLE_LEARNER);
        	VU360UserAuthenticationDetails managerDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	managerDetail.doManagerSwitchMode(request);
        }
	}

	private void attemptManagerSwitchToLearnerUser(HttpServletRequest request) throws Exception{

        String username = request.getParameter(SPRING_SECURITY_SWITCH_USERNAME_KEY);
        
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
			
        if (StringUtils.isBlank(username)) {
            username = "";
        }

        log.debug("Attempt to switch to user [" + username + "]");

        UserDetails targetUser = userDetailsService.loadUserByUsername(username);
        //TODO check if targetuser is null
        
        userDetailsChecker.check(targetUser);

        //TODO additional checks if the customer for this manager and the learner is same
        //TODO else throw Exception
        
        // OK, create the switch user token
        
        // grant an additional authority that contains the original Authentication object
        // which will be used to 'exit' from the current switched user.
        GrantedAuthority switchAuthority = new SwitchUserGrantedAuthority(ROLE_PREVIOUS_ADMINISTRATOR, currentAuth);

        // get the original authorities
        //List orig = Arrays.asList(targetUser.getAuthorities());

        // add the new switch user authority
        //List newAuths = new ArrayList(orig);
       // newAuths.add(switchAuthority);

        //GrantedAuthority[] authorities = (GrantedAuthority[]) newAuths.toArray(new GrantedAuthority[newAuths.size()]);
        //Collection auth = Arrays.asList(authorities);

        List<GrantedAuthority> auth = (List<GrantedAuthority>) targetUser.getAuthorities();
        auth.add(switchAuthority);

        // create the new authentication token
        UsernamePasswordAuthenticationToken targetUserAuth = new UsernamePasswordAuthenticationToken(targetUser, targetUser.getPassword(), auth);

        //also switch mode for the current principal in the details object
        Object currentAuthDetails = currentAuth.getDetails();
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("ManagerSwitchMode", VU360UserMode.ROLE_LEARNER);
        	VU360UserAuthenticationDetails managerDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	managerDetail.doManagerSwitchMode(request);
        }

        //preserve the original auth details object
        targetUserAuth.setDetails(currentAuth.getDetails());
        
        
        log.debug("Switch User Token [" + targetUserAuth + "]");

        // publish event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(SecurityContextHolder.getContext().getAuthentication(), targetUser));
        }

        SecurityContextHolder.getContext().setAuthentication(targetUserAuth);
	}

	private void attemptManagerSwitchBackFromLearnerUser(HttpServletRequest request) throws Exception {
        // need to check to see if the current user has a SwitchUserGrantedAuthority
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        if (null == currentAuth) {
            throw new AuthenticationCredentialsNotFoundException(messages.getMessage("SwitchUserProcessingFilter.noCurrentUser", "No current user associated with this request"));
        }

        // check to see if the current user did actual switch to another user
        // if so, get the original source user so we can switch back
        Authentication originalAuth = null;
        
        //Spring 3.0 change
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) currentAuth.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
        	if (grantedAuthority instanceof SwitchUserGrantedAuthority) {
        		originalAuth = ((SwitchUserGrantedAuthority) grantedAuthority).getSource();
        		log.debug("Found original switch user granted authority [" + originalAuth + "]");
        	}
		}

        if (originalAuth == null) {
            log.error("Could not find original user Authentication object!");
            throw new AuthenticationCredentialsNotFoundException(messages.getMessage("SwitchUserProcessingFilter.noOriginalAuthentication", "Could not find original Authentication object"));
        }

        // get the source user details
        UserDetails originalUser = null;
        Object obj = originalAuth.getPrincipal();

        if ((obj != null) && obj instanceof UserDetails) {
            originalUser = (UserDetails) obj;
        }

        //also switch mode for the current principal in the details object        
        Object currentAuthDetails = originalAuth.getDetails();
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("ManagerSwitchMode", VU360UserMode.ROLE_TRAININGADMINISTRATOR);
        	VU360UserAuthenticationDetails managerDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	managerDetail.doManagerSwitchMode(request);
        }

        // publish event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(currentAuth, originalUser));
        }

        //update the current context back to the original user
        SecurityContextHolder.getContext().setAuthentication(originalAuth);
	}
//////////////////////Accreditation Mode ///////
	private void attemptAccreditationSwitchBackFromLearnerMode(HttpServletRequest request) {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    Object currentAuthDetails = currentAuth.getDetails();
    if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
    	request.setAttribute("AccreditationSwitchMode", VU360UserMode.ROLE_REGULATORYANALYST);
    	VU360UserAuthenticationDetails accreditationDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
    	accreditationDetail.doAccreditationSwitchMode(request);
    }
}

private void attemptAccreditationSwitchToLearnerMode(HttpServletRequest request) {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    Object currentAuthDetails = currentAuth.getDetails();
    if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
    	request.setAttribute("AccreditationSwitchMode", VU360UserMode.ROLE_LEARNER);
    	VU360UserAuthenticationDetails accreditationDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
    	accreditationDetail.doAccreditationSwitchMode(request);
    }
}
private void attemptAccreditationSwitchBackFromManagerMode(HttpServletRequest request) {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    Object currentAuthDetails = currentAuth.getDetails();
    if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
    	request.setAttribute("AccreditationSwitchMode", VU360UserMode.ROLE_REGULATORYANALYST);
    	VU360UserAuthenticationDetails accreditationDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
    	accreditationDetail.doAccreditationSwitchMode(request);
    }
}

private void attemptAccreditationSwitchToManagerMode(HttpServletRequest request) {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    Object currentAuthDetails = currentAuth.getDetails();
    if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
    	request.setAttribute("AccreditationSwitchMode", VU360UserMode.ROLE_TRAININGADMINISTRATOR);
    	VU360UserAuthenticationDetails accreditationDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
    	accreditationDetail.doAccreditationSwitchMode(request);
    }
}
private void attemptAccreditationSwitchBackFromAdminMode(HttpServletRequest request) {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    Object currentAuthDetails = currentAuth.getDetails();
    if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
    	request.setAttribute("AccreditationSwitchMode", VU360UserMode.ROLE_REGULATORYANALYST);
    	VU360UserAuthenticationDetails accreditationDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
    	accreditationDetail.doAccreditationSwitchMode(request);
    }
}

private void attemptAccreditationSwitchToAdminMode(HttpServletRequest request) {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    Object currentAuthDetails = currentAuth.getDetails();
    if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
    	request.setAttribute("AccreditationSwitchMode", VU360UserMode.ROLE_LMSADMINISTRATOR);
    	VU360UserAuthenticationDetails accreditationDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
    	accreditationDetail.doAccreditationSwitchMode(request);
    }
}

/**
 * In clustering environment SpringSecurityContext unable to retain Distributor, Customer objects and currentMode
 * that is why we force fully set currentDistriutor or currentCustomer.
 * more detail can be found  LMS-13475 & LMS-13478 & LMS-13524
 * "remoteIPclustered" attribute is only for indication current node has down 
 * and an other node has started to full fill request, Now all 
 * VU360UserAuthentication need to be refresh .
 * @param request
 * @param userDetails
 * 
 */
/*
@SuppressWarnings("unchecked")
private void setClusteredObjects(HttpServletRequest request, VU360UserAuthenticationDetails userDetails){

	HttpSession session = request.getSession(false);
	String previousMachineLocalAddress = (String) session.getAttribute("remoteIPclustered");
	String currentMachineLocalAddress = request.getLocalAddr();
	
	//only execute in case of tomcat node changed 
	if (StringUtils.isEmpty(previousMachineLocalAddress) 
			|| !previousMachineLocalAddress.equalsIgnoreCase(currentMachineLocalAddress)) {
		
		session.setAttribute("remoteIPclustered", request.getLocalAddr());
		Distributor distributor = (Distributor) session.getAttribute("clusteredDistributor");
		Customer customer = (Customer) session.getAttribute("clusteredCustomer");
		Learner learner = (Learner) session.getAttribute("clusteredLearner");
		VU360UserMode vu360UserMode = (VU360UserMode) session.getAttribute("clusteredCurrentMode");
		AdminSearchType adminSearchType = (AdminSearchType) session.getAttribute("clusteredSearchType");
		Boolean initied = (Boolean) session.getAttribute("initedClustered");
		VU360User principal = (VU360User) session.getAttribute("Principalclustered");
		List<VU360UserMode> availableModes = (List<VU360UserMode>) session.getAttribute("avaiableModesclustered");
		
		
		if (adminSearchType != null) {
			userDetails.setClusteredCurrentSearchType(adminSearchType);
		}
		if (distributor != null) {
			userDetails.setClusteredDistributor(distributor);
		}
		if (customer != null) {
			customer = customerService.getCustomerById(customer.getId()); // LMS-13540
			userDetails.setClusteredCustomer(customer);
		}
		if (learner != null) {
			learner = learnerService.getLearnerByID(learner.getId()); // LMS-13478
			userDetails.setClusteredLearner(learner);
		}
		if (vu360UserMode != null) {
			userDetails.setClusteredCurrentMode(vu360UserMode);
		}
		//if (initied != null) {
		//	userDetails.setClusteredInited(initied);
		//}
		if (principal != null) {
			principal = vu360UserService.getUpdatedUserById(principal.getId());
			userDetails.setClusteredPrincipal(principal);
		}
		if (availableModes != null) {
			userDetails.setClusteredAvailableModes(availableModes);
		}
	}

}
*/
/*
private void attemptAccreditationSwitchToLearnerUser(HttpServletRequest request) throws Exception{

    String username = request.getParameter(SPRING_SECURITY_SWITCH_USERNAME_KEY);

    if (username == null) {
        username = "";
    }

    log.debug("Attempt to switch to user [" + username + "]");

    UserDetails targetUser = userDetailsService.loadUserByUsername(username);
    //TODO check if targetuser is null
    
    userDetailsChecker.check(targetUser);

    //TODO additional checks if the customer for this manager and the learner is same
    //TODO else throw Exception
    
    // OK, create the switch user token
    
    // grant an additional authority that contains the original Authentication object
    // which will be used to 'exit' from the current switched user.
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    GrantedAuthority switchAuthority = new SwitchUserGrantedAuthority(ROLE_PREVIOUS_ADMINISTRATOR, currentAuth);

    // get the original authorities
    List orig = Arrays.asList(targetUser.getAuthorities());

    // add the new switch user authority
    List newAuths = new ArrayList(orig);
    newAuths.add(switchAuthority);

    GrantedAuthority[] authorities = (GrantedAuthority[]) newAuths.toArray(new GrantedAuthority[newAuths.size()]);

    // create the new authentication token
    UsernamePasswordAuthenticationToken targetUserAuth = new UsernamePasswordAuthenticationToken(targetUser, targetUser.getPassword(), authorities);

    //also switch mode for the current principal in the details object
    Object currentAuthDetails = currentAuth.getDetails();
    if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
    	request.setAttribute("ManagerSwitchMode", VU360UserMode.ROLE_LEARNER);
    	VU360UserAuthenticationDetails managerDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
    	managerDetail.doManagerSwitchMode(request);
    }

    //preserve the original auth details object
    targetUserAuth.setDetails(currentAuth.getDetails());
    
    
    log.debug("Switch User Token [" + targetUserAuth + "]");

    // publish event
    if (this.eventPublisher != null) {
        eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(SecurityContextHolder.getContext().getAuthentication(), targetUser));
    }

    SecurityContextHolder.getContext().setAuthentication(targetUserAuth);
}
*/
private void attemptAccreditationSwitchBackFromLearnerUser(HttpServletRequest request) throws Exception {
    // need to check to see if the current user has a SwitchUserGrantedAuthority
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

    if (null == currentAuth) {
        throw new AuthenticationCredentialsNotFoundException(messages.getMessage("SwitchUserProcessingFilter.noCurrentUser", "No current user associated with this request"));
    }

    // check to see if the current user did actual switch to another user
    // if so, get the original source user so we can switch back
    Authentication originalAuth = null;
    
    //Spring 3.0 change
    Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) currentAuth.getAuthorities();
    for (GrantedAuthority grantedAuthority : authorities) {
    	if (grantedAuthority instanceof SwitchUserGrantedAuthority) {
    		originalAuth = ((SwitchUserGrantedAuthority) grantedAuthority).getSource();
    		log.debug("Found original switch user granted authority [" + originalAuth + "]");
    	}
	}

    if (originalAuth == null) {
        log.error("Could not find original user Authentication object!");
        throw new AuthenticationCredentialsNotFoundException(messages.getMessage("SwitchUserProcessingFilter.noOriginalAuthentication", "Could not find original Authentication object"));
    }

    // get the source user details
    UserDetails originalUser = null;
    Object obj = originalAuth.getPrincipal();

    if ((obj != null) && obj instanceof UserDetails) {
        originalUser = (UserDetails) obj;
    }

    //also switch mode for the current principal in the details object        
    Object currentAuthDetails = originalAuth.getDetails();
    if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
    	request.setAttribute("AccreditationSwitchMode", VU360UserMode.ROLE_REGULATORYANALYST);
    	VU360UserAuthenticationDetails accreditationDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
    	accreditationDetail.doAccreditationSwitchMode(request);
    }

    // publish event
    if (this.eventPublisher != null) {
        eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(currentAuth, originalUser));
    }

    //update the current context back to the original user
    SecurityContextHolder.getContext().setAuthentication(originalAuth);
}

/////////////////////////////////////
	
	

	protected boolean matchesURI(HttpServletRequest request, String pattern){
		String uri = request.getRequestURI();
        uri = stripUri(uri);
        return uri.endsWith(request.getContextPath() + pattern);
	}
	
    private static String stripUri(String uri) {
        //String uri = request.getRequestURI();
        int idx = uri.indexOf(';');

        if (idx > 0) {
            uri = uri.substring(0, idx);
        }

        return uri;
    }

	
    protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
            throws IOException {

        RedirectUtils.sendRedirect(request, response, url, useRelativeContext);
    }
	
    private void redirectToFailureUrl(HttpServletRequest request, HttpServletResponse response,String failureUrl, Exception failed) throws IOException {
        logger.error("User Mode Setup Failed", failed);

        if (failureUrl != null) {
        	sendRedirect(request, response, failureUrl);
        } else {
            response.getWriter().print("User Mode Setup Failed: " + failed.getMessage());
            response.flushBuffer();
        }
    }

    //The URL strings Getters and Setters
	
	/**
	 * @return the userEntryURL
	 */
//	public String getUserEntryURL() {
//		return userEntryURL;
//	}

	/**
	 * @param userEntryURL the userEntryURL to set
	 */
//	public void setUserEntryURL(String userEntryURL) {
//		this.userEntryURL = userEntryURL;
//	}

	/**
	 * @return the userEntryTargetURL
	 */
//	public String getUserEntryTargetURL() {
//		return userEntryTargetURL;
//	}

	/**
	 * @param userEntryTargetURL the userEntryTargetURL to set
	 */
//	public void setUserEntryTargetURL(String userEntryTargetURL) {
//		this.userEntryTargetURL = userEntryTargetURL;
//	}

	/**
	 * @return the userEntryFailureURL
	 */
//	public String getUserEntryFailureURL() {
//		return userEntryFailureURL;
//	}

	/**
	 * @param userEntryFailureURL the userEntryFailureURL to set
	 */
//	public void setUserEntryFailureURL(String userEntryFailureURL) {
//		this.userEntryFailureURL = userEntryFailureURL;
//	}

	/**
	 * @return the adminSwitchManagerModeFailureURL
	 */
	public String getAdminSwitchManagerModeFailureURL() {
		return adminSwitchManagerModeFailureURL;
	}

	/**
	 * @param adminSwitchManagerModeFailureURL the adminSwitchManagerModeFailureURL to set
	 */
	public void setAdminSwitchManagerModeFailureURL(
			String adminSwitchManagerModeFailureURL) {
		this.adminSwitchManagerModeFailureURL = adminSwitchManagerModeFailureURL;
	}

	/**
	 * @return the adminSwitchManagerModeTargetURL
	 */
	public String getAdminSwitchManagerModeTargetURL() {
		return adminSwitchManagerModeTargetURL;
	}

	/**
	 * @param adminSwitchManagerModeTargetURL the adminSwitchManagerModeTargetURL to set
	 */
	public void setAdminSwitchManagerModeTargetURL(
			String adminSwitchManagerModeTargetURL) {
		this.adminSwitchManagerModeTargetURL = adminSwitchManagerModeTargetURL;
	}

	/**
	 * @return the adminSwitchManagerModeURL
	 */
	public String getAdminSwitchManagerModeURL() {
		return adminSwitchManagerModeURL;
	}

	/**
	 * @param adminSwitchManagerModeURL the adminSwitchManagerModeURL to set
	 */
	public void setAdminSwitchManagerModeURL(String adminSwitchManagerModeURL) {
		this.adminSwitchManagerModeURL = adminSwitchManagerModeURL;
	}

	/**
	 * @return the adminSwitchBackURL
	 */
	public String getAdminSwitchBackURL() {
		return adminSwitchBackURL;
	}

	/**
	 * @param adminSwitchBackURL the adminSwitchBackURL to set
	 */
	public void setAdminSwitchBackURL(String adminSwitchBackURL) {
		this.adminSwitchBackURL = adminSwitchBackURL;
	}

	/**
	 * @return the adminSwitchLearnerModeFailureURL
	 */
	public String getAdminSwitchLearnerModeFailureURL() {
		return adminSwitchLearnerModeFailureURL;
	}

	/**
	 * @param adminSwitchLearnerModeFailureURL the adminSwitchLearnerModeFailureURL to set
	 */
	public void setAdminSwitchLearnerModeFailureURL(
			String adminSwitchLearnerModeFailureURL) {
		this.adminSwitchLearnerModeFailureURL = adminSwitchLearnerModeFailureURL;
	}

	/**
	 * @return the adminSwitchLearnerModeTargetURL
	 */
	public String getAdminSwitchLearnerModeTargetURL() {
		return adminSwitchLearnerModeTargetURL;
	}

	/**
	 * @param adminSwitchLearnerModeTargetURL the adminSwitchLearnerModeTargetURL to set
	 */
	public void setAdminSwitchLearnerModeTargetURL(
			String adminSwitchLearnerModeTargetURL) {
		this.adminSwitchLearnerModeTargetURL = adminSwitchLearnerModeTargetURL;
	}

	/**
	 * @return the adminSwitchLearnerModeURL
	 */
	public String getAdminSwitchLearnerModeURL() {
		return adminSwitchLearnerModeURL;
	}

	/**
	 * @param adminSwitchLearnerModeURL the adminSwitchLearnerModeURL to set
	 */
	public void setAdminSwitchLearnerModeURL(String adminSwitchLearnerModeURL) {
		this.adminSwitchLearnerModeURL = adminSwitchLearnerModeURL;
	}

	
	/**
	 * @return the managerSwitchFailureURL
	 */
	public String getManagerSwitchFailureURL() {
		return managerSwitchFailureURL;
	}

	/**
	 * @param managerSwitchFailureURL the managerSwitchFailureURL to set
	 */
	public void setManagerSwitchFailureURL(String managerSwitchFailureURL) {
		this.managerSwitchFailureURL = managerSwitchFailureURL;
	}

	/**
	 * @return the managerSwitchTargetURL
	 */
	public String getManagerSwitchTargetURL() {
		return managerSwitchTargetURL;
	}

	/**
	 * @param managerSwitchTargetURL the managerSwitchTargetURL to set
	 */
	public void setManagerSwitchTargetURL(String managerSwitchTargetURL) {
		this.managerSwitchTargetURL = managerSwitchTargetURL;
	}

	/**
	 * @return the managerSwitchURL
	 */
	public String getManagerSwitchURL() {
		return managerSwitchURL;
	}

	/**
	 * @param managerSwitchURL the managerSwitchURL to set
	 */
	public void setManagerSwitchURL(String managerSwitchURL) {
		this.managerSwitchURL = managerSwitchURL;
	}

	/**
	 * @return the managerSwitchBackURL
	 */
	public String getManagerSwitchBackURL() {
		return managerSwitchBackURL;
	}

	/**
	 * @param managerSwitchBackURL the managerSwitchBackURL to set
	 */
	public void setManagerSwitchBackURL(String managerSwitchBackURL) {
		this.managerSwitchBackURL = managerSwitchBackURL;
	}

	/**
	 * @return the managerSwitchLearnerModeFailureURL
	 */
	public String getManagerSwitchLearnerModeFailureURL() {
		return managerSwitchLearnerModeFailureURL;
	}

	/**
	 * @param managerSwitchLearnerModeFailureURL the managerSwitchLearnerModeFailureURL to set
	 */
	public void setManagerSwitchLearnerModeFailureURL(
			String managerSwitchLearnerModeFailureURL) {
		this.managerSwitchLearnerModeFailureURL = managerSwitchLearnerModeFailureURL;
	}

	/**
	 * @return the managerSwitchBackFromLearnerModeFailureURL
	 */
	public String getManagerSwitchBackFromLearnerModeFailureURL() {
		return managerSwitchBackFromLearnerModeFailureURL;
	}

	/**
	 * @param managerSwitchBackFromLearnerModeFailureURL the managerSwitchBackFromLearnerModeFailureURL to set
	 */
	public void setManagerSwitchBackFromLearnerModeFailureURL(
			String managerSwitchBackFromLearnerModeFailureURL) {
		this.managerSwitchBackFromLearnerModeFailureURL = managerSwitchBackFromLearnerModeFailureURL;
	}

	/**
	 * @return the managerSwitchLearnerModeTargetURL
	 */
	public String getManagerSwitchLearnerModeTargetURL() {
		return managerSwitchLearnerModeTargetURL;
	}

	/**
	 * @param managerSwitchLearnerModeTargetURL the managerSwitchLearnerModeTargetURL to set
	 */
	public void setManagerSwitchLearnerModeTargetURL(
			String managerSwitchLearnerModeTargetURL) {
		this.managerSwitchLearnerModeTargetURL = managerSwitchLearnerModeTargetURL;
	}

	
	/**
	 * @return the managerSwitchBackFromLearnerModeTargetURL
	 */
	public String getManagerSwitchBackFromLearnerModeTargetURL() {
		return managerSwitchBackFromLearnerModeTargetURL;
	}

	/**
	 * @param managerSwitchBackFromLearnerModeTargetURL the managerSwitchBackFromLearnerModeTargetURL to set
	 */
	public void setManagerSwitchBackFromLearnerModeTargetURL(
			String managerSwitchBackFromLearnerModeTargetURL) {
		this.managerSwitchBackFromLearnerModeTargetURL = managerSwitchBackFromLearnerModeTargetURL;
	}

	/**
	 * @return the managerSwitchLearnerModeURL
	 */
	public String getManagerSwitchLearnerModeURL() {
		return managerSwitchLearnerModeURL;
	}

	/**
	 * @return the managerSwitchBackFromLearnerModeURL
	 */
	public String getManagerSwitchBackFromLearnerModeURL() {
		return managerSwitchBackFromLearnerModeURL;
	}

	/**
	 * @param managerSwitchBackFromLearnerModeURL the managerSwitchBackFromLearnerModeURL to set
	 */
	public void setManagerSwitchBackFromLearnerModeURL(
			String managerSwitchBackFromLearnerModeURL) {
		this.managerSwitchBackFromLearnerModeURL = managerSwitchBackFromLearnerModeURL;
	}

	/**
	 * @param managerSwitchLearnerModeURL the managerSwitchLearnerModeURL to set
	 */
	public void setManagerSwitchLearnerModeURL(String managerSwitchLearnerModeURL) {
		this.managerSwitchLearnerModeURL = managerSwitchLearnerModeURL;
	}

	/**
	 * @return the adminSwitchBackFailureURL
	 */
	public String getAdminSwitchBackFailureURL() {
		return adminSwitchBackFailureURL;
	}

	/**
	 * @param adminSwitchBackFailureURL the adminSwitchBackFailureURL to set
	 */
	public void setAdminSwitchBackFailureURL(String adminSwitchBackFailureURL) {
		this.adminSwitchBackFailureURL = adminSwitchBackFailureURL;
	}

	/**
	 * @return the adminSwitchBackTargetURL
	 */
	public String getAdminSwitchBackTargetURL() {
		return adminSwitchBackTargetURL;
	}

	/**
	 * @param adminSwitchBackTargetURL the adminSwitchBackTargetURL to set
	 */
	public void setAdminSwitchBackTargetURL(String adminSwitchBackTargetURL) {
		this.adminSwitchBackTargetURL = adminSwitchBackTargetURL;
	}

	/**
	 * @return the managerSwitchBackFailureURL
	 */
	public String getManagerSwitchBackFailureURL() {
		return managerSwitchBackFailureURL;
	}

	/**
	 * @param managerSwitchBackFailureURL the managerSwitchBackFailureURL to set
	 */
	public void setManagerSwitchBackFailureURL(String managerSwitchBackFailureURL) {
		this.managerSwitchBackFailureURL = managerSwitchBackFailureURL;
	}

	/**
	 * @return the managerSwitchBackTargetURL
	 */
	public String getManagerSwitchBackTargetURL() {
		return managerSwitchBackTargetURL;
	}

	/**
	 * @param managerSwitchBackTargetURL the managerSwitchBackTargetURL to set
	 */
	public void setManagerSwitchBackTargetURL(String managerSwitchBackTargetURL) {
		this.managerSwitchBackTargetURL = managerSwitchBackTargetURL;
	}

	
	/**
	 * @return the userDetailsService
	 */
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	/**
	 * @param userDetailsService the userDetailsService to set
	 */
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * @param userDetailsChecker the userDetailsChecker to set
	 */
	public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
	}

	/**
	 * @return the accreditationSwitchLearnerModeURL
	 */
	public String getAccreditationSwitchLearnerModeURL() {
		return accreditationSwitchLearnerModeURL;
	}

	/**
	 * @param accreditationSwitchLearnerModeURL the accreditationSwitchLearnerModeURL to set
	 */
	public void setAccreditationSwitchLearnerModeURL(
			String accreditationSwitchLearnerModeURL) {
		this.accreditationSwitchLearnerModeURL = accreditationSwitchLearnerModeURL;
	}

	/**
	 * @return the accreditationSwitchLearnerModeTargetURL
	 */
	public String getAccreditationSwitchLearnerModeTargetURL() {
		return accreditationSwitchLearnerModeTargetURL;
	}

	/**
	 * @param accreditationSwitchLearnerModeTargetURL the accreditationSwitchLearnerModeTargetURL to set
	 */
	public void setAccreditationSwitchLearnerModeTargetURL(
			String accreditationSwitchLearnerModeTargetURL) {
		this.accreditationSwitchLearnerModeTargetURL = accreditationSwitchLearnerModeTargetURL;
	}

	/**
	 * @return the accreditationSwitchBackFromLearnerModeURL
	 */
	public String getAccreditationSwitchBackFromLearnerModeURL() {
		return accreditationSwitchBackFromLearnerModeURL;
	}

	/**
	 * @param accreditationSwitchBackFromLearnerModeURL the accreditationSwitchBackFromLearnerModeURL to set
	 */
	public void setAccreditationSwitchBackFromLearnerModeURL(
			String accreditationSwitchBackFromLearnerModeURL) {
		this.accreditationSwitchBackFromLearnerModeURL = accreditationSwitchBackFromLearnerModeURL;
	}

	/**
	 * @return the accreditationSwitchBackFromLearnerModeTargetURL
	 */
	public String getAccreditationSwitchBackFromLearnerModeTargetURL() {
		return accreditationSwitchBackFromLearnerModeTargetURL;
	}

	/**
	 * @param accreditationSwitchBackFromLearnerModeTargetURL the accreditationSwitchBackFromLearnerModeTargetURL to set
	 */
	public void setAccreditationSwitchBackFromLearnerModeTargetURL(
			String accreditationSwitchBackFromLearnerModeTargetURL) {
		this.accreditationSwitchBackFromLearnerModeTargetURL = accreditationSwitchBackFromLearnerModeTargetURL;
	}

	/**
	 * @return the accreditationSwitchFailureURL
	 */
	public String getAccreditationSwitchFailureURL() {
		return accreditationSwitchFailureURL;
	}

	/**
	 * @param accreditationSwitchFailureURL the accreditationSwitchFailureURL to set
	 */
	public void setAccreditationSwitchFailureURL(
			String accreditationSwitchFailureURL) {
		this.accreditationSwitchFailureURL = accreditationSwitchFailureURL;
	}

	/**
	 * @return the accreditationSwitchBackFromLearnerModeFailureURL
	 */
	public String getAccreditationSwitchBackFromLearnerModeFailureURL() {
		return accreditationSwitchBackFromLearnerModeFailureURL;
	}

	/**
	 * @param accreditationSwitchBackFromLearnerModeFailureURL the accreditationSwitchBackFromLearnerModeFailureURL to set
	 */
	public void setAccreditationSwitchBackFromLearnerModeFailureURL(
			String accreditationSwitchBackFromLearnerModeFailureURL) {
		this.accreditationSwitchBackFromLearnerModeFailureURL = accreditationSwitchBackFromLearnerModeFailureURL;
	}

	/**
	 * @return the accreditationSwitchLearnerModeFailureURL
	 */
	public String getAccreditationSwitchLearnerModeFailureURL() {
		return accreditationSwitchLearnerModeFailureURL;
	}

	/**
	 * @param accreditationSwitchLearnerModeFailureURL the accreditationSwitchLearnerModeFailureURL to set
	 */
	public void setAccreditationSwitchLearnerModeFailureURL(
			String accreditationSwitchLearnerModeFailureURL) {
		this.accreditationSwitchLearnerModeFailureURL = accreditationSwitchLearnerModeFailureURL;
	}

	/**
	 * @return the accreditationSwitchURL
	 */
	public String getAccreditationSwitchURL() {
		return accreditationSwitchURL;
	}

	/**
	 * @param accreditationSwitchURL the accreditationSwitchURL to set
	 */
	public void setAccreditationSwitchURL(String accreditationSwitchURL) {
		this.accreditationSwitchURL = accreditationSwitchURL;
	}

	/**
	 * @return the accreditationSwitchTargetURL
	 */
	public String getAccreditationSwitchTargetURL() {
		return accreditationSwitchTargetURL;
	}

	/**
	 * @param accreditationSwitchTargetURL the accreditationSwitchTargetURL to set
	 */
	public void setAccreditationSwitchTargetURL(String accreditationSwitchTargetURL) {
		this.accreditationSwitchTargetURL = accreditationSwitchTargetURL;
	}

	public String getSwitchInstructorModeURL() {
		return switchInstructorModeURL;
	}

	public void setSwitchInstructorModeURL(String switchInstructorModeURL) {
		this.switchInstructorModeURL = switchInstructorModeURL;
	}

	public String getSwitchInstructorModeTargetURL() {
		return switchInstructorModeTargetURL;
	}

	public void setSwitchInstructorModeTargetURL(
			String switchInstructorModeTargetURL) {
		this.switchInstructorModeTargetURL = switchInstructorModeTargetURL;
	}

	public String getSwitchInstructorModeFailureURL() {
		return switchInstructorModeFailureURL;
	}

	public void setSwitchInstructorModeFailureURL(
			String switchInstructorModeFailureURL) {
		this.switchInstructorModeFailureURL = switchInstructorModeFailureURL;
	}

	public String getAdminSwitchBackFromManagerURL() {
		return adminSwitchBackFromManagerURL;
	}

	public void setAdminSwitchBackFromManagerURL(
			String adminSwitchBackFromManagerURL) {
		this.adminSwitchBackFromManagerURL = adminSwitchBackFromManagerURL;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	

	public SecurityAndRolesService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityAndRolesService securityService) {
		this.securityService = securityService;
	}
	/**
	 * @return the vu360UserService
	 */
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	/**
	 * @return the switchProctorModeURL
	 */
	public String getSwitchProctorModeURL() {
		return switchProctorModeURL;
	}

	/**
	 * @param switchProctorModeURL the switchProctorModeURL to set
	 */
	public void setSwitchProctorModeURL(String switchProctorModeURL) {
		this.switchProctorModeURL = switchProctorModeURL;
	}

	/**
	 * @return the switchProctorModeTargetURL
	 */
	public String getSwitchProctorModeTargetURL() {
		return switchProctorModeTargetURL;
	}

	/**
	 * @param switchProctorModeTargetURL the switchProctorModeTargetURL to set
	 */
	public void setSwitchProctorModeTargetURL(String switchProctorModeTargetURL) {
		this.switchProctorModeTargetURL = switchProctorModeTargetURL;
	}

	/**
	 * @return the switchProctorModeFailureURL
	 */
	public String getSwitchProctorModeFailureURL() {
		return switchProctorModeFailureURL;
	}

	/**
	 * @param switchProctorModeFailureURL the switchProctorModeFailureURL to set
	 */
	public void setSwitchProctorModeFailureURL(String switchProctorModeFailureURL) {
		this.switchProctorModeFailureURL = switchProctorModeFailureURL;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public String getManagerRedirectURL(HttpServletRequest request)
	{
		String redirectURL = null;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<String> disabledFeatureCodes = new ArrayList<>();
		disabledFeatureCodes.addAll((Set<String>)request.getSession().getAttribute("DISABLED_FEATURE_CODES"));

		String enabledFeatureCode = securityService.getAnyEnabledFeatureCodeInDisplayOrderByRoleType(user.getId(), LMSRole.ROLE_TRAININGMANAGER, disabledFeatureCodes);
		switch(enabledFeatureCode) {
			case "AssignTrainingPlans":
				redirectURL = "/mgr_assignTraningPlan.do";
				break;
			case "LMS-MGR-0029":
				redirectURL = "/mgr_learnerEnrollments.do?method=showSearchLearnerPage";
				break;
			case "LMS-MGR-0001":
				redirectURL = "/mgr_manageLearners.do";
				break;
			case "LMS-MGR-0002":
				redirectURL = "/mgr_batchImportLearners.do";
				break;
			case "LMS-MGR-0003":
				redirectURL = "/mgr_regInvitation-1.do";
				break;
			case "LMS-MGR-0004":
				redirectURL = "/mgr_manageOrganizationGroup.do";
				break;
			case "LMS-MGR-0005":
				redirectURL = "/mgr_manageLearnerGroups.do";
				break;
			case "LMS-MGR-0006":
				redirectURL = "/mgr_manageSecurityRoles.do?method=showSecurityRoles";
				break;
			case "LMS-MGR-0007":
				redirectURL = "/mgr_viewAssignSecurityRoleMain.do";
				break;
			case "LMS-MGR-0008":
				redirectURL = "/mgr_viewPlanAndEnroll.do";
				break;
			case "LMS-MGR-0009":
				redirectURL = "/mgr_searchTrainingPlans.do";
				break;
			case "LMS-MGR-0010":
				redirectURL = "/mgr_manageSynchronousCourse.do";
				break;
			case "LMS-MGR-0011":
				redirectURL = "/mgr_viewAllEntitlements.do";
				break;
			case "LMS-MGR-0012":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0013":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0014":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0015":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0032":
				redirectURL = "/mgr_ManageReports.do";
				break;
			case "LMS-MGR-0017":
				redirectURL = "/mgr_alertCourse.do";
				break;
			case "LMS-MGR-0018":
				redirectURL = "/mgr_sendMailToLearners.do";
				break;
			case "LMS-MGR-0019":
				redirectURL = "/mgr_viewAssignSurveyMain.do";
				break;
			case "LMS-MGR-0020":
				redirectURL = "/mgr_manageSurveys.do";
				break;
			case "LMS-MGR-0023":
				redirectURL = "/mgr_editCustomer.do?method=editCustomerProfile";
				break;
			case "LMS-MGR-0024":
				redirectURL = "/mgr_editCustomer.do?method=editCustomerPreferences";
				break;
			case "LMS-MGR-0025":
				redirectURL = "/mgr_ViewEnrollmentForCertEnableDisable.do?method=searchLearner";
				break;
		}
		return redirectURL;
	}

}