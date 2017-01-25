/**
 * 
 */
package com.softech.vu360.lms.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.util.Assert;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.util.RedirectUtils;

/**
 * @author Somnath
 *
 */
public class VU360AdminProxySetupFilter extends SwitchUserFilter implements InitializingBean,
	ApplicationEventPublisherAware, MessageSourceAware {
    
	public static final int FILTER_CHAIN_FIRST = Ordered.HIGHEST_PRECEDENCE + 1000;
	private static final int INTERVAL = 100;

	public static final String VU360_ADMIN_CHANGE_CUSTOMER_KEY = "customer";
	public static final String VU360_ADMIN_CHANGE_DISTRIBUTOR_KEY = "distributor";
	public static final String VU360_ADMIN_CHANGE_LEARNER_KEY = "learner";
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private ApplicationEventPublisher eventPublisher;
    //private AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();

    private String adminSearchEntryUrl; 
    
    private String customerProxyEntryUrl; 
    private String customerProxyExitUrl;
    private String customerProxyTargetUrl;
    private String customerProxyFailureUrl;
    
    private String distributorProxyEntryUrl; 
    private String distributorProxyExitUrl;
    private String distributorProxyTargetUrl;
    private String distributorProxyFailureUrl;
    
    private String learnerProxyEntryUrl; 
    private String learnerProxyExitUrl;
    private String learnerProxyTargetUrl;
    private String learnerProxyFailureUrl;
    
    private boolean useRelativeContext;

	private CustomerService customerService;
	private DistributorService distributorService;
	private LearnerService learnerService;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.hasLength(adminSearchEntryUrl,"Admin Search URL must be specified");
		
		Assert.hasLength(customerProxyEntryUrl,"Customer Entry URL must be specified");
		Assert.hasLength(customerProxyExitUrl, "Customer Exit URL must be specified");
		Assert.hasLength(customerProxyTargetUrl, "Customer Entry Target URL must be specified");
		
		Assert.hasLength(distributorProxyEntryUrl,"Distributor Entry URL must be specified");
		Assert.hasLength(distributorProxyExitUrl, "Distributor Exit URL must be specified");
		Assert.hasLength(distributorProxyTargetUrl, "Distributor Entry Target URL must be specified");
		
		Assert.hasLength(learnerProxyEntryUrl,"Learner Entry URL must be specified");
		Assert.hasLength(learnerProxyExitUrl, "Learner Exit URL must be specified");
		Assert.hasLength(learnerProxyTargetUrl, "Learner Entry Target URL must be specified");
		
		Assert.notNull(messages, "A message source must be set");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.ui.SpringSecurityFilter#doFilterHttp(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	//TODO static resource should be skipped from here. 
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		System.out.println("........................VU360AdminProxySetupFilter.doFilter.........................");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		if(matchesURI(request,this.customerProxyEntryUrl)){ //trying to change the selected customer
			try {
				this.attemptSetupCustomer(request);
				//redirect to target url
                sendRedirect(request, response, this.customerProxyTargetUrl);
			} catch (Exception e) {
				this.redirectToFailureUrl(request, response, this.customerProxyFailureUrl, e);
			}
			return;
		}else if(matchesURI(request,this.customerProxyExitUrl)){ //trying to return from the selected customer
			try {
				this.attemptTeardownSwitchedEntity(request);
			}finally{
				//redirect to search url
				sendRedirect(request, response, this.adminSearchEntryUrl);
			}
			return;
			
		}else if(matchesURI(request,this.distributorProxyEntryUrl)){ //trying to change the selected distributor
			try {
				this.attemptSetupDistributor(request);
				//redirect to target url
                sendRedirect(request, response, this.distributorProxyTargetUrl);
			} catch (Exception e) {
				this.redirectToFailureUrl(request, response, this.distributorProxyFailureUrl, e);
			}
			return;
			
		}else if(matchesURI(request,this.distributorProxyExitUrl)){ //trying to return from the selected distributor
			try {
				this.attemptTeardownSwitchedEntity(request);
			}finally{
					//redirect to search url
					sendRedirect(request, response, this.adminSearchEntryUrl);
			}
			return;
			
		}else if(matchesURI(request,this.learnerProxyEntryUrl)){ //trying to change the selected learner
			try {
				this.attemptSetupLearner(request);
				//redirect to target url
				sendRedirect(request, response, this.learnerProxyTargetUrl);
			} catch (Exception e) {
				this.redirectToFailureUrl(request, response, this.learnerProxyFailureUrl, e);
			}
			return;
			
		}else if(matchesURI(request,this.learnerProxyExitUrl)){ //trying to return from the selected learner
			try {
				this.attemptTeardownSwitchedEntity(request);
			}finally{
				//redirect to search url
				sendRedirect(request, response, this.adminSearchEntryUrl);
			}
			return;
			
		}
		
		chain.doFilter(request, response);
	}

	private void attemptSetupCustomer(HttpServletRequest request) throws Exception{
		String strCustomerId = request.getParameter(VU360_ADMIN_CHANGE_CUSTOMER_KEY);
		if(!StringUtils.isBlank(strCustomerId)){
			try {
				Long customerId = Long.parseLong(strCustomerId);
		        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		        //Object currentUser = currentAuth.getPrincipal();
		        //Object currentCredentials = currentAuth.getCredentials();
		        Object currentAuthDetails = currentAuth.getDetails();
		        //GrantedAuthority[] currentAuths = currentAuth.getAuthorities();
		        
		        //UsernamePasswordAuthenticationToken targetUser;
		        // create the new authentication token
		        //targetUser = new UsernamePasswordAuthenticationToken(currentUser, currentCredentials, currentAuths);
		        
		        //Get hold of the customer from the selected customerid
		        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
		        	request.setAttribute("SwitchType", AdminSearchType.CUSTOMER);
		        	Customer customer = customerService.getCustomerById(customerId);
		        	request.setAttribute("SwitchCustomer", ProxyVOHelper.setCustomerProxy(customer));
		        	VU360UserAuthenticationDetails adminDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
		        	adminDetail.doPopulateAdminSearchInformation(request);
		        }
		        //targetUser.setDetails(currentAuthDetails);
		        
		        //update the current context to the new target user
                //SecurityContextHolder.getContext().setAuthentication(targetUser);
		        
			} catch (NumberFormatException e) {
				throw new Exception("Invalid Customer reference to change in URL",e);
			}			
		}else{
			throw new Exception("Customer reference to change to not found in URL");
		}
	}

	private void attemptTeardownSwitchedEntity(HttpServletRequest request) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        //Object currentUser = currentAuth.getPrincipal();
        //Object currentCredentials = currentAuth.getCredentials();
        Object currentAuthDetails = currentAuth.getDetails();
        //GrantedAuthority[] currentAuths = currentAuth.getAuthorities();
        
        //UsernamePasswordAuthenticationToken targetUser;
        // create the new authentication token
        //targetUser = new UsernamePasswordAuthenticationToken(currentUser, currentCredentials, currentAuths);
        
        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
        	request.setAttribute("SwitchType", AdminSearchType.NONE);
        	VU360UserAuthenticationDetails adminDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
        	adminDetail.doPopulateAdminSearchInformation(request);
        }
        //targetUser.setDetails(currentAuthDetails);
        
        //update the current context to the new target user
        //SecurityContextHolder.getContext().setAuthentication(targetUser);

	}
	
	private void attemptSetupDistributor(HttpServletRequest request) throws Exception{
		String strDistributorId = request.getParameter(VU360_ADMIN_CHANGE_DISTRIBUTOR_KEY);
		if(!StringUtils.isBlank(strDistributorId)){
			try {
				Long distributorId = Long.parseLong(strDistributorId);
		        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		        Object currentAuthDetails = currentAuth.getDetails();

		        //Get hold of the distributor from the selected distributorId
		        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
		        	request.setAttribute("SwitchType", AdminSearchType.DISTRIBUTOR);
		        	Distributor distributor = distributorService.getDistributorById(distributorId);
		        	request.setAttribute("SwitchDistributor", ProxyVOHelper.setDistributorProxy(distributor));
		        	VU360UserAuthenticationDetails adminDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
		        	adminDetail.doPopulateAdminSearchInformation(request);
		        }
		        
			} catch (NumberFormatException e) {
				throw new Exception("Invalid Distributor reference to change in URL",e);
			}			
		}else{
			throw new Exception("Distributor reference to change to not found in URL");
		}

	}
	
	private void attemptSetupLearner(HttpServletRequest request) throws Exception{
		String strLearnerId = request.getParameter(VU360_ADMIN_CHANGE_LEARNER_KEY);
		if(!StringUtils.isBlank(strLearnerId)){
			try {
				Long learnerId = Long.parseLong(strLearnerId);
		        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		        Object currentAuthDetails = currentAuth.getDetails();
		        
		        //Get hold of the distributor from the selected distributorId
		        if(currentAuthDetails instanceof VU360UserAuthenticationDetails){
		        	request.setAttribute("SwitchType", AdminSearchType.LEARNER);
		        	Learner learner = learnerService.getLearnerByID(learnerId);
		        	request.setAttribute("SwitchLearner", ProxyVOHelper.setLearnerProxy( learner));
		        	VU360UserAuthenticationDetails adminDetail = (VU360UserAuthenticationDetails)currentAuthDetails; 
		        	adminDetail.doPopulateAdminSearchInformation(request);
		        }
		        
			} catch (NumberFormatException e) {
				throw new Exception("Invalid Learner reference to change in URL",e);
			}			
		}else{
			throw new Exception("Learner reference to change to not found in URL");
		}

	}


	protected boolean matchesURI(HttpServletRequest request, String pattern){
        String uri = stripUri(request);
        return uri.endsWith(request.getContextPath() + pattern);
	}
	
    protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
            throws IOException {

        RedirectUtils.sendRedirect(request, response, url, useRelativeContext);
    }
	
    private void redirectToFailureUrl(HttpServletRequest request, HttpServletResponse response,String failureUrl, Exception failed) throws IOException {
        logger.error("Switch Customer/Distributor/Learner by Admin failed", failed);

        if (failureUrl != null) {
        	sendRedirect(request, response, failureUrl);
        } else {
            response.getWriter().print("Switch Customer/Distributor/Learner by Admin failed: " + failed.getMessage());
            response.flushBuffer();
        }
    }
    
	/* (non-Javadoc)
	 * @see org.springframework.context.MessageSourceAware#setMessageSource(org.springframework.context.MessageSource)
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) throws BeansException {
        this.eventPublisher = eventPublisher;
		
	}

//    public void setAuthenticationDetailsSource(AuthenticationDetailsSource authenticationDetailsSource) {
//        Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
//        this.authenticationDetailsSource = authenticationDetailsSource;
//    }


	/**
	 * The standard spring filters are ordered with a gap of 100 between each position
	 * So we can insert custom filters in these gaps
	 * @see org.springframework.core.Ordered#getOrder()
	 */
	public int getOrder() {
		return  FILTER_CHAIN_FIRST + INTERVAL * 14;
		//return FilterChainOrder.SWITCH_USER_FILTER - 10; //just before the switch user filter
	}


	/**
	 * @return the adminSearchEntryUrl
	 */
	public String getAdminSearchEntryUrl() {
		return adminSearchEntryUrl;
	}

	/**
	 * @param adminSearchEntryUrl the adminSearchEntryUrl to set
	 */
	public void setAdminSearchEntryUrl(String adminSearchEntryUrl) {
		this.adminSearchEntryUrl = adminSearchEntryUrl;
	}

	/**
	 * @return the customerProxyEntryUrl
	 */
	public String getCustomerProxyEntryUrl() {
		return customerProxyEntryUrl;
	}

	/**
	 * @param customerProxyEntryUrl the customerProxyEntryUrl to set
	 */
	public void setCustomerProxyEntryUrl(String customerProxyEntryUrl) {
		this.customerProxyEntryUrl = customerProxyEntryUrl;
	}

	/**
	 * @return the customerProxyExitUrl
	 */
	public String getCustomerProxyExitUrl() {
		return customerProxyExitUrl;
	}

	/**
	 * @param customerProxyExitUrl the customerProxyExitUrl to set
	 */
	public void setCustomerProxyExitUrl(String customerProxyExitUrl) {
		this.customerProxyExitUrl = customerProxyExitUrl;
	}

	/**
	 * @return the customerProxyFailureUrl
	 */
	public String getCustomerProxyFailureUrl() {
		return customerProxyFailureUrl;
	}

	/**
	 * @param customerProxyFailureUrl the customerProxyFailureUrl to set
	 */
	public void setCustomerProxyFailureUrl(String customerProxyFailureUrl) {
		this.customerProxyFailureUrl = customerProxyFailureUrl;
	}

	/**
	 * @return the customerProxyTargetUrl
	 */
	public String getCustomerProxyTargetUrl() {
		return customerProxyTargetUrl;
	}

	/**
	 * @param customerProxyTargetUrl the customerProxyTargetUrl to set
	 */
	public void setCustomerProxyTargetUrl(String customerProxyTargetUrl) {
		this.customerProxyTargetUrl = customerProxyTargetUrl;
	}

	
	/**
	 * @return the distributorProxyEntryUrl
	 */
	public String getDistributorProxyEntryUrl() {
		return distributorProxyEntryUrl;
	}

	/**
	 * @param distributorProxyEntryUrl the distributorProxyEntryUrl to set
	 */
	public void setDistributorProxyEntryUrl(String distributorProxyEntryUrl) {
		this.distributorProxyEntryUrl = distributorProxyEntryUrl;
	}

	/**
	 * @return the distributorProxyExitUrl
	 */
	public String getDistributorProxyExitUrl() {
		return distributorProxyExitUrl;
	}

	/**
	 * @param distributorProxyExitUrl the distributorProxyExitUrl to set
	 */
	public void setDistributorProxyExitUrl(String distributorProxyExitUrl) {
		this.distributorProxyExitUrl = distributorProxyExitUrl;
	}

	/**
	 * @return the distributorProxyFailureUrl
	 */
	public String getDistributorProxyFailureUrl() {
		return distributorProxyFailureUrl;
	}

	/**
	 * @param distributorProxyFailureUrl the distributorProxyFailureUrl to set
	 */
	public void setDistributorProxyFailureUrl(String distributorProxyFailureUrl) {
		this.distributorProxyFailureUrl = distributorProxyFailureUrl;
	}

	/**
	 * @return the distributorProxyTargetUrl
	 */
	public String getDistributorProxyTargetUrl() {
		return distributorProxyTargetUrl;
	}

	/**
	 * @param distributorProxyTargetUrl the distributorProxyTargetUrl to set
	 */
	public void setDistributorProxyTargetUrl(String distributorProxyTargetUrl) {
		this.distributorProxyTargetUrl = distributorProxyTargetUrl;
	}

	/**
	 * @return the useRelativeContext
	 */
	public boolean isUseRelativeContext() {
		return useRelativeContext;
	}

	/**
	 * @param useRelativeContext the useRelativeContext to set
	 */
	public void setUseRelativeContext(boolean useRelativeContext) {
		this.useRelativeContext = useRelativeContext;
	}

    /**
     * Strips any content after the ';' in the request URI
     *
     * @param request The http request
     *
     * @return The stripped uri
     */
    private static String stripUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        int idx = uri.indexOf(';');

        if (idx > 0) {
            uri = uri.substring(0, idx);
        }

        return uri;
    }

	/**
	 * @return the customerService
	 */
	public CustomerService getCustomerService() {
		return customerService;
	}

	/**
	 * @param customerService the customerService to set
	 */
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * @return the distributorService
	 */
	public DistributorService getDistributorService() {
		return distributorService;
	}

	/**
	 * @param distributorService the distributorService to set
	 */
	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	/**
	 * @return the learnerProxyEntryUrl
	 */
	public String getLearnerProxyEntryUrl() {
		return learnerProxyEntryUrl;
	}

	/**
	 * @param learnerProxyEntryUrl the learnerProxyEntryUrl to set
	 */
	public void setLearnerProxyEntryUrl(String learnerProxyEntryUrl) {
		this.learnerProxyEntryUrl = learnerProxyEntryUrl;
	}

	/**
	 * @return the learnerProxyExitUrl
	 */
	public String getLearnerProxyExitUrl() {
		return learnerProxyExitUrl;
	}

	/**
	 * @param learnerProxyExitUrl the learnerProxyExitUrl to set
	 */
	public void setLearnerProxyExitUrl(String learnerProxyExitUrl) {
		this.learnerProxyExitUrl = learnerProxyExitUrl;
	}

	/**
	 * @return the learnerProxyFailureUrl
	 */
	public String getLearnerProxyFailureUrl() {
		return learnerProxyFailureUrl;
	}

	/**
	 * @param learnerProxyFailureUrl the learnerProxyFailureUrl to set
	 */
	public void setLearnerProxyFailureUrl(String learnerProxyFailureUrl) {
		this.learnerProxyFailureUrl = learnerProxyFailureUrl;
	}

	/**
	 * @return the learnerProxyTargetUrl
	 */
	public String getLearnerProxyTargetUrl() {
		return learnerProxyTargetUrl;
	}

	/**
	 * @param learnerProxyTargetUrl the learnerProxyTargetUrl to set
	 */
	public void setLearnerProxyTargetUrl(String learnerProxyTargetUrl) {
		this.learnerProxyTargetUrl = learnerProxyTargetUrl;
	}

	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}

	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
}
