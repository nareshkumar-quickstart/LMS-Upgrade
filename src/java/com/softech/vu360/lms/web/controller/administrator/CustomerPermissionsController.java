package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.web.controller.CommonControllerActions;
import com.softech.vu360.lms.web.controller.model.LMSFeaturePermissionForm;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * Customer Permissions Controller
 * @author sm.humayun
 * @since 4.13 {LMS-8108}
 */
public class CustomerPermissionsController extends MultiActionController 
{

	/**
	 * logger
	 * @see {@link Logger}
	 */
	private static final Logger log = Logger.getLogger(CustomerPermissionsController.class.getName());

	/**
	 * Security and roles service
	 * @see {@link SecurityAndRolesService}
	 */
	private SecurityAndRolesService securityAndRolesService;
	
	/**
	 * Customer service
	 * @see {@link CustomerService}
	 */
	private CustomerService customerService;
	
	/**
	 * lms feature permission comparator
	 */
	private Comparator lmsFeaturePermissionComparator;

	/**
	 * redirect to search view
	 */
	private String redirectToSearchView;
	
	/**
	 * edit customer permissions view
	 */
	private String editCustomerPermissionsView;
	
	/**
	 * Extract permission changes made by user from {@link HttpServletRequest} and update <code>customerLMSFeatures</code>
	 * @param request
	 * @param roleType
	 * @param customerLMSFeatures
	 */
	private void updatePermissionChangesFromRequest (HttpServletRequest request, String roleType, List<CustomerLMSFeature> customerLMSFeatures) 
	{
		try
		{
			log.info("\t ---------- START - updatePermissionChangesFromRequest : " + this.getClass().getName() + " ---------- ");
			Enumeration paramNames = request.getParameterNames();
			while(paramNames.hasMoreElements()) 
			{
				String paramName = (String) paramNames.nextElement();
				String[] paramValues = request.getParameterValues(paramName);
				if(paramName.startsWith("prd")) 
				{	//All Feature radio Buttons starts with 'prd' 				
					log.debug("paramName = " + paramName.substring(3) + ", paramValues = " + paramValues[0]);
					for(CustomerLMSFeature customerLMSFeature : customerLMSFeatures)
						if(customerLMSFeature.getLmsFeature().getFeatureCode().equalsIgnoreCase(paramName.substring(3)))
						{
							if((customerLMSFeature.getLmsFeature().getLmsMode().equalsIgnoreCase("learner")) 
									&& (roleType.equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)) 
									&& (roleType.equalsIgnoreCase(LMSRole.ROLE_LMSADMINISTRATOR)))
								customerLMSFeature.setEnabled(true);
							else if(paramValues[0].equalsIgnoreCase("false"))
								customerLMSFeature.setEnabled(false);
							else if(paramValues[0].equalsIgnoreCase("true"))
								customerLMSFeature.setEnabled(true);
						}
				}
			}
		}
		finally
		{
			log.info("\t ---------- END - updatePermissionChangesFromRequest : " + this.getClass().getName() + " ---------- ");
		}
	}

	/**
	 * Manage customer permissions
	 * @param request
	 * @param response
	 * @return model and view
	 */
	public ModelAndView manage (HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			log.info("\t ---------- START - manage : " + this.getClass().getName() + " ---------- ");

			//procced only if customer has been selected
			boolean customerFeaturesenabled = false;
			boolean customerLMSFeaturesdisable = true;
			boolean customerFeaturesdisable = false;
			Customer customer = null;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
			{
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentCustomer() != null && details.getCurrentSearchType()==AdminSearchType.CUSTOMER)
				{
					customer = details.getCurrentCustomer();
				}
				else
				{
					log.error("Customer not selected, throwing exception...");
					return new ModelAndView(redirectToSearchView,"isRedirect","c");
				}
			}
			else
				return new ModelAndView(redirectToSearchView,"isRedirect","c");

			//set feature to be selected on left nav
			request.getSession(true).setAttribute("feature", "LMS-ADM-0016");
			
			//determine action from request
			String action = request.getParameter("action");
			log.info("action from request = " + action);
			action = (action == null) ? CommonControllerActions.DEFAULT.toString() : action;
			log.info("action = " + action);
			//and redirect to search if action is cancel
			Map<Object, Object> context = new HashMap<Object, Object>();
			if(action.equalsIgnoreCase(CommonControllerActions.CANCEL.toString())) 
			{
				context.put("customerId", request.getParameter("customerId"));
				log.info("redirecting to search view...");
				return new ModelAndView(redirectToSearchView);
			}

			//determine role from request
			String intRoleType = request.getParameter("roleType");
			log.info("intRoleType from request = " + intRoleType);
			if(StringUtils.isBlank(intRoleType) || !StringUtils.isNumeric(intRoleType))
				intRoleType = "0";
			String roleType = LMSRole.getRoleType(Integer.parseInt(intRoleType));
			log.info("roleType = " + roleType);
			
			log.info("fetching lms feaures defined at distributor level...");
			List<DistributorLMSFeature> distributorLMSFeatures = securityAndRolesService.getDistributorLMSFeatures(customer.getDistributor(), roleType);
			log.info("distributorLMSFeatures " + (distributorLMSFeatures == null ? "is null" : "size = " + distributorLMSFeatures.size()));
			
			log.info("fetching lms feaures defined at customer level...");
			List<CustomerLMSFeature> customerLMSFeatures = securityAndRolesService.getCustomerLMSFeatures (customer, roleType);
			log.info("customerLMSFeatures " + (customerLMSFeatures == null ? "is null" : "size = " + customerLMSFeatures.size()));
			
			this.disableAndLockCustomerFeaturesOptimized(customerLMSFeatures, distributorLMSFeatures);
			this.addMissingFeatures(customer, customerLMSFeatures, distributorLMSFeatures);
			
			Enumeration paramNames = request.getParameterNames();

			while(paramNames.hasMoreElements() && request.getParameter("roleType") != null) 
			{
				String paramName = (String) paramNames.nextElement();
				String[] paramValues = request.getParameterValues(paramName);
				if(paramName.startsWith("prd") && request.getParameter("roleType").equals("1")) 
				  {
			        for(CustomerLMSFeature customerLMSFeature : customerLMSFeatures)
					  {
						if(customerLMSFeature.getLmsFeature().getFeatureCode().equalsIgnoreCase(paramName.substring(3)))
									{
									   if(paramValues[0].equalsIgnoreCase("true"))
										   customerFeaturesenabled = true;
									   else if(paramValues[0].equalsIgnoreCase("false"))
									   customerFeaturesdisable = true;
									   else
										   customerLMSFeaturesdisable = false;
					                }
					  }			
				  }		
			}
			
			if(request.getParameter("roleType") != null && action.equalsIgnoreCase(CommonControllerActions.SAVE.toString()))
			{
				if(request.getParameter("roleType").equals("1"))
				{
					if(customerFeaturesenabled && customerLMSFeaturesdisable)
					{
						this.saveFeatures(request, roleType, customerLMSFeatures);
					}
					else if(customerFeaturesenabled && customerFeaturesdisable)
					{
						this.saveFeatures(request, roleType, customerLMSFeatures);
					}
				}
				else
					this.saveFeatures(request, roleType, customerLMSFeatures);
			}
			
			else
			{
			  if(action.equalsIgnoreCase(CommonControllerActions.SAVE.toString()))
				this.saveFeatures(request, roleType, customerLMSFeatures);
			}		

			//sort before proceeding to display
			Collections.sort(customerLMSFeatures, this.getLmsFeaturePermissionComparator());
			
			if(!customerFeaturesenabled && customerFeaturesdisable)
			context.put("validateTermsofServices","error.customerpermission.activepermission");
			context.put("lmsFeaturePermissions", customerLMSFeatures);
			context.put("lmsFeaturePermisionForm", new LMSFeaturePermissionForm(customerLMSFeatures));
			context.put("roleType", intRoleType);
	
			log.info("forwarding to editCustomerPermissionView...");
			return new ModelAndView(editCustomerPermissionsView, "context", context);
		}
		finally
		{
			log.info("\t ---------- END - manage : " + this.getClass().getName() + " ---------- ");
		}
	}
	
	/**
	 * Save features such that:
	 *  - Update permission changes from request in 
	 *  <code>customerLMSFeatures</code>
	 *  - Update <code>customerLMSFeatures</code> in db
	 * @param request
	 * @param roleType
	 * @param customerLMSFeatures
	 * @param removeCustomerLMSFeatures
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	private void saveFeatures (HttpServletRequest request, String roleType, List<CustomerLMSFeature> customerLMSFeatures)
	{
		this.updatePermissionChangesFromRequest(request, roleType, customerLMSFeatures);
		log.info("saving updated customerLMSFeatures in db...");
		customerService.updateLMSFeatures(customerLMSFeatures);
		log.info("customerLMSFeatures saved");
	}
	
	/**
	 * Disable and Lock any additional lms features defined at customer level and not defined or disabled at distributor level
	 * @param customerLMSFeatures
	 * @param distributorLMSFeatures
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	private boolean disableAndLockCustomerFeatures (List<CustomerLMSFeature> customerLMSFeatures, List<DistributorLMSFeature> distributorLMSFeatures)
	{
		log.info("Disabling and Locking any additional lms features defined at customer level and not defined or disabled at distributor level..."); 
		boolean matched, enabled, customerfeature, distributorfeature;
		
		distributorfeature = customerfeature = false;
		
		int count = 0;
		for(CustomerLMSFeature customerLMSFeature : customerLMSFeatures)
		{
			log.info("\t (" + ++count + "). customerLMSFeature - " + customerLMSFeature.getLmsFeature().getId() + " - " 
					+ customerLMSFeature.getLmsFeature().getFeatureCode());
			matched = enabled = false;
			for(DistributorLMSFeature distributorLMSFeature : distributorLMSFeatures)
			{
				matched = customerLMSFeature.getLmsFeature().getId().longValue() == distributorLMSFeature.getLmsFeature().getId().longValue();
				if(matched)
				{
					enabled = distributorLMSFeature.getEnabled();
					break;
				}
				if(distributorLMSFeature.getEnabled())
				{
					distributorfeature = Boolean.TRUE;
				}
				
			}
			
			if(customerLMSFeature.getEnabled())
			{
				customerfeature = Boolean.TRUE;
			}
			
			if(customerLMSFeature.getLmsFeature().getFeatureCode().equals("LMS-MGR-0033"))
			{
				if(customerLMSFeature.getLmsFeature().getFeatureCode().equals("LMS-MGR-0033")){
					if(customerLMSFeature.getEnabled())
			           customerfeature = Boolean.TRUE;
				    else if (!customerLMSFeature.getEnabled())
				       customerfeature = Boolean.FALSE;
				}
			}
			
			log.info("\t\t distributor - defined = " + matched + " and enabled = " + enabled);
			if(!(matched && enabled))
			{
				customerLMSFeature.setEnabled(false);
				customerLMSFeature.setLocked(true);
				log.info("\t\t => disabled and locked at customer level");
			}
		}
		
		log.info("Distributor feature = " + distributorfeature + " Customer feature = " + customerfeature);
		
		return customerfeature;
	}
	
	/**
	 * 
	 * @param customerLMSFeaturesList
	 * @param distributorLMSFeaturesList
	 * @return
	 */
	private boolean disableAndLockCustomerFeaturesOptimized (List<CustomerLMSFeature> customerLMSFeaturesList, List<DistributorLMSFeature> distributorLMSFeaturesList)
	{
		log.info("Disabling and Locking any additional lms features defined at customer level and not defined or disabled at distributor level..."); 
		boolean matched, enabled, customerfeature, distributorfeature;
		distributorfeature = customerfeature = false;
		int count = 0;

		for(CustomerLMSFeature customerLMSFeature : customerLMSFeaturesList)
		{
			matched = enabled = false;
			DistributorLMSFeature distributorLMSFeature=null;
			try {
				distributorLMSFeature = distributorLMSFeaturesList.stream().filter(e -> e.getLmsFeature().getId() == customerLMSFeature.getLmsFeature().getId()).findFirst().get();
				matched=true;
				
			} catch (java.util.NoSuchElementException e2) {
				log.debug("java.util.NoSuchElementException: no Customer and Distributor feature matched." );
			}

			if(distributorLMSFeature!=null){
				enabled = distributorLMSFeature.getEnabled();
				distributorfeature = Boolean.TRUE;
			}
			
			if(customerLMSFeature.getEnabled()){
				customerfeature = Boolean.TRUE;
			}
			
			log.info("\t\t distributor - defined = " + matched + " and enabled = " + enabled);
			if(!(matched && enabled))
			{
				customerLMSFeature.setEnabled(false);
				customerLMSFeature.setLocked(true);
				log.info("\t\t => disabled and locked at customer level");
			}
			
			
			
			/*matched = enabled = false;
			for(DistributorLMSFeature distributorLMSFeature : distributorLMSFeaturesList)
			{
				matched = customerLMSFeature.getLmsFeature().getId().longValue() == distributorLMSFeature.getLmsFeature().getId().longValue();
				if(matched)
				{
					enabled = distributorLMSFeature.getEnabled();
					break;
				}
			}
			
			if(customerLMSFeature.getEnabled())
			{
				customerfeature = Boolean.TRUE;
			}
			
			log.info("\t\t distributor - defined = " + matched + " and enabled = " + enabled);
			if(!(matched && enabled))
			{
				customerLMSFeature.setEnabled(false);
				customerLMSFeature.setLocked(true);
				log.info("\t\t => disabled and locked at customer level");
			}*/
		}
		
		log.info("Distributor feature = " + distributorfeature + " Customer feature = " + customerfeature);
		
		return customerfeature;
	}
	
	
	/**
	 * Add any missing lms features not defined at customer level and available and enabled at distributor level
	 * @param customer
	 * @param customerLMSFeatures
	 * @param distributorLMSFeatures
	 * @author sm.humayun
	 * @since 4.13
	 */
	private void addMissingFeatures (Customer customer, List<CustomerLMSFeature> customerLMSFeatures
			, List<DistributorLMSFeature> distributorLMSFeatures)
	{
		log.info("Adding any missing lms features not defined at customer level and available and enabled at distributor level...");
		CustomerLMSFeature missingCustomerLMSFeature;
		int count = 0;
		boolean matched = false;
		for(DistributorLMSFeature distributorLMSFeature : distributorLMSFeatures)
		{
			matched = false;
			log.info("\t (" + ++count + "). distributorLMSFeature - " + distributorLMSFeature.getLmsFeature().getId() + " - " 
					+ distributorLMSFeature.getLmsFeature().getFeatureCode() + " - " + (distributorLMSFeature.getEnabled() ? "ENABLED" : "DISABLED"));
			if(distributorLMSFeature.getEnabled())
			{
				for(CustomerLMSFeature customerLMSFeature : customerLMSFeatures)
				{
					matched = customerLMSFeature.getLmsFeature().getId().longValue() == distributorLMSFeature.getLmsFeature().getId().longValue();
					if(matched)
						break;
				}
				log.info("\t\t customer - defined = " + matched);
				if(!matched)
				{
					missingCustomerLMSFeature = new CustomerLMSFeature();
					missingCustomerLMSFeature.setLmsFeature(distributorLMSFeature.getLmsFeature());
					missingCustomerLMSFeature.setCustomer(customer);
					missingCustomerLMSFeature.setEnabled(true);
					customerLMSFeatures.add(missingCustomerLMSFeature);
					log.info("\t\t => added at customer level");
				}
			}
		}
	}

	/**
	 * Return customerService
	 * @return customerService
	 * @see {@link CustomerService}
	 */
	public CustomerService getCustomerService() {
		return customerService;
	}
	
	/**
	 * Set customerService
	 * @param customerService
	 * @see {@link CustomerService}
	 */
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * Return securityAndRoleService
	 * @return securityAndRoleServic
	 * @see {@link SecurityAndRolesService}
	 */
	public SecurityAndRolesService getSecurityAndRolesService() {
		return securityAndRolesService;
	}
	
	/**
	 * Set securityAndRoleService
	 * @param securityAndRolesService
	 * @see {@link SecurityAndRolesService}
	 */
	public void setSecurityAndRolesService(SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}

	/**
	 * Return editCustomerPermissionsView
	 * @return editCustomerPermissionsView
	 */
	public String getEditCustomerPermissionsView() {
		return editCustomerPermissionsView;
	}

	/**
	 * Set editCustomerPermissionsView
	 * @param editCustomerPermissionsView
	 */
	public void setEditCustomerPermissionsView (String editCustomerPermissionsView)	{
		this.editCustomerPermissionsView = editCustomerPermissionsView;
	}

	/**
	 * Return redirectToSearchView
	 * @return redirectToSearchView
	 */
	public String getRedirectToSearchView() {
		return redirectToSearchView;
	}

	/**
	 * Set redirectToSearchView
	 * @param redirectToSearchView
	 */
	public void setRedirectToSearchView(String redirectToSearchView) {
		this.redirectToSearchView = redirectToSearchView;
	}

	/**
	 * Set lmsFeaturePermissionComparator
	 * @return lmsFeaturePermissionComparator
	 */
	public Comparator getLmsFeaturePermissionComparator() {
		return this.lmsFeaturePermissionComparator;
	}

	/**
	 * Set lmsFeaturePermissionComparator
	 * @param lmsFeaturePermissionComparator
	 */
	public void setLmsFeaturePermissionComparator(Comparator lmsFeaturePermissionComparator) {
		this.lmsFeaturePermissionComparator = lmsFeaturePermissionComparator;
	}
}
