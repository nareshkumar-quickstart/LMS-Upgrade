package com.softech.vu360.lms.web.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.manager.ApplicationContextProvider;

public class VU360UserAuthenticationDetails extends WebAuthenticationDetails{
	
	private static final long serialVersionUID = 1L;
	private AdminSearchType currentSearchType = AdminSearchType.NONE;
	private com.softech.vu360.lms.vo.Customer currentCustomerVO = null;
	private com.softech.vu360.lms.vo.Distributor currentDistributorVO  = null;
	private com.softech.vu360.lms.vo.Learner currentLearnerVO = null;
	private String switchBackUrl="";

	private Boolean inited = Boolean.FALSE;

	private List<VU360UserMode> availableModes = new ArrayList<VU360UserMode>();
	private VU360UserMode currentMode;

	private com.softech.vu360.lms.vo.VU360User originalPrincipal;

	private static CustomerService customerService = (CustomerService) ApplicationContextProvider.getApplicationContext().getBean("customerService");
	private static VU360UserService vu360UserService = (VU360UserService) ApplicationContextProvider.getApplicationContext().getBean("userDetailsService");
	private static DistributorService distributorService = (DistributorService) ApplicationContextProvider.getApplicationContext().getBean("distributorService");
	
	public VU360UserAuthenticationDetails(HttpServletRequest request) {
		super(request);
	}

	public void doInitializeUser(com.softech.vu360.lms.vo.VU360User originalPrincipal) {
		setAvailableModes(originalPrincipal);
		//vu360UserService.getEnabledFeatureGroups(originalPrincipal.getId());
	}

	private void setAvailableModes(com.softech.vu360.lms.vo.VU360User originalPrincipal) {
		this.originalPrincipal = originalPrincipal;
		originalPrincipal.setLogInAsManagerRole(null);// LMS-5867
		availableModes = new ArrayList<VU360UserMode>();

		if (originalPrincipal.isLearnerMode()) {
			availableModes.add(VU360UserMode.ROLE_LEARNER);
			currentMode = VU360UserMode.ROLE_LEARNER;
		}
		if (originalPrincipal.isManagerMode()) {
			availableModes.add(VU360UserMode.ROLE_TRAININGADMINISTRATOR);
			if (currentMode != null)
				currentMode = VU360UserMode.ROLE_TRAININGADMINISTRATOR;
		}

		if (originalPrincipal.isAdminMode()) {
			availableModes.add(VU360UserMode.ROLE_LMSADMINISTRATOR);
			if (currentMode != null)
				currentMode = VU360UserMode.ROLE_LMSADMINISTRATOR;
		}
		if (originalPrincipal.isAccreditationMode()) {
			availableModes.add(VU360UserMode.ROLE_REGULATORYANALYST);
			if (currentMode != null)
				currentMode = VU360UserMode.ROLE_TRAININGADMINISTRATOR;
		}
		if (originalPrincipal.isProctorMode()) {
			availableModes.add(VU360UserMode.ROLE_PROCTOR);
			if (currentMode != null)
				currentMode = VU360UserMode.ROLE_PROCTOR;
		}
		inited = Boolean.TRUE;
	}

	public void doPopulateAdminSearchInformation(HttpServletRequest request) {
		/* SpringSecurityContext unable retain currentMode object LMS-LMS-13478	*/
		setCurrentModeClustered(request , this.getCurrentMode());
		setCurrentPrincipalClustered(request, getOriginalPrincipal());
		setCurrentInitedClustered(request);
		setCurrentAvaiableModesClustered(request);
		
		
		Object objSwitchType = request.getAttribute("SwitchType");
		if(objSwitchType instanceof AdminSearchType){
			AdminSearchType currentType = (AdminSearchType)objSwitchType;
			switch(currentType){
			case CUSTOMER:
				currentSearchType = AdminSearchType.CUSTOMER; 
				currentCustomerVO = (com.softech.vu360.lms.vo.Customer)request.getAttribute("SwitchCustomer");
				
			 	/* 
			 	   SpringSecurityContext unable retain Customer or Distributor object
				   LMS-13475 
				*/
				request.getSession(false).setAttribute("clusteredSearchType", currentSearchType);
				request.getSession(false).setAttribute("clusteredCustomer", currentCustomerVO);
				request.getSession(false).removeAttribute("clusteredDistributor");

				currentDistributorVO = null;
				break;
			case DISTRIBUTOR:
				currentSearchType = AdminSearchType.DISTRIBUTOR; 
				currentDistributorVO = (com.softech.vu360.lms.vo.Distributor)request.getAttribute("SwitchDistributor");
			 	/* 
			 	   SpringSecurityContext unable retain Customer or Distributor object
				   LMS-13475 
				*/
				request.getSession(false).setAttribute("clusteredSearchType", currentSearchType);
				request.getSession(false).setAttribute("clusteredDistributor", currentDistributorVO);
				request.getSession(false).removeAttribute("clusteredCustomer");

				currentLearnerVO = ((com.softech.vu360.lms.vo.Learner) request.getAttribute("SwitchLearner"));
				if(currentLearnerVO !=null ) 
				{
					/* SpringSecurityContext unable retain Customer or Distributor object LMS-13478	*/
					request.getSession(false).setAttribute("clusteredLearner", currentLearnerVO);
				} 
				else{ 
					/* SpringSecurityContext unable retain Customer or Distributor object LMS-13478	*/
					request.getSession(false).removeAttribute("clusteredLearner");
				}
				
				break;
			case LEARNER:
				currentSearchType = AdminSearchType.LEARNER; 
				currentLearnerVO = (com.softech.vu360.lms.vo.Learner)request.getAttribute("SwitchLearner");
				
				/* SpringSecurityContext unable retain Customer or Distributor object LMS-13478	*/
				request.getSession(false).setAttribute("clusteredSearchType", currentSearchType);
				request.getSession(false).setAttribute("clusteredLearner", currentLearnerVO);
				request.getSession(false).removeAttribute("clusteredCustomer");
				request.getSession(false).removeAttribute("clusteredDistributor");

				currentCustomerVO = null;
				currentDistributorVO = null;
				break;
			default:
				currentSearchType = AdminSearchType.NONE; 
				currentCustomerVO = null;
				currentDistributorVO = null;
				currentLearnerVO = null;
	
				/* SpringSecurityContext unable retain Customer or Distributor object LMS-13478	*/
				request.getSession(false).setAttribute("clusteredSearchType", currentSearchType);
				request.getSession(false).removeAttribute("clusteredDistributor");
				request.getSession(false).removeAttribute("clusteredCustomer");
				request.getSession(false).removeAttribute("clusteredLearner");
			}
		}
	}

	public void doAdminSwitchMode(HttpServletRequest request){
		Object objSwitchType = request.getAttribute("AdminSwitchMode");
		//TODO we can also check if the mode is valid for this call
		if(objSwitchType instanceof VU360UserMode){
			currentMode = (VU360UserMode)objSwitchType;
			setCurrentModeClustered(request, currentMode);
		}
	}

	public void doManagerSwitchMode(HttpServletRequest request){
		Object objSwitchType = request.getAttribute("ManagerSwitchMode");
		//TODO we can also check if the mode is valid for this call
		if(objSwitchType instanceof VU360UserMode){
			currentMode = (VU360UserMode)objSwitchType;
			setCurrentModeClustered(request, currentMode);
		}
	}
	
	public void doInstructorSwitchMode(HttpServletRequest request){
		Object objSwitchType = request.getAttribute("InstructorSwitchMode");
		//TODO we can also check if the mode is valid for this call
		if(objSwitchType instanceof VU360UserMode){
			currentMode = (VU360UserMode)objSwitchType;
			setCurrentModeClustered(request, currentMode);
		}
	}

	public void doAccreditationSwitchMode(HttpServletRequest request){
		Object objSwitchType = request.getAttribute("AccreditationSwitchMode");
		//TODO we can also check if the mode is valid for this call
		if(objSwitchType instanceof VU360UserMode){
			currentMode = (VU360UserMode)objSwitchType;
			setCurrentModeClustered(request, currentMode);
		}
	}
	
	public void doProctorSwitchMode(HttpServletRequest request) {
		Object objSwitchType = request.getAttribute("ProctorSwitchMode");
		//TODO we can also check if the mode is valid for this call
		if(objSwitchType instanceof VU360UserMode){
			currentMode = (VU360UserMode)objSwitchType;
			setCurrentModeClustered(request, currentMode);
		}
	}
	
	/**
	 * 
	 * SpringSecurityContext unable retain currentMode object LMS-13478 
	 * @param request
	 * @param vu360UserMode
	 */
	private void setCurrentModeClustered(HttpServletRequest request, VU360UserMode vu360UserMode) {
		request.getSession(false).setAttribute("clusteredCurrentMode", vu360UserMode);
	}

	/**
	 * 
	 * SpringSecurityContext unable retain currentMode object LMS-13478 
	 * @param request
	 * @param vu360UserMode
	 */
	private void setCurrentPrincipalClustered(HttpServletRequest request, com.softech.vu360.lms.vo.VU360User ovu360User) {
		request.getSession(false).setAttribute("Principalclustered", ovu360User);
	}

	/**
	 * 
	 * SpringSecurityContext unable retain currentMode object LMS-13478 
	 * @param request
	 * @param vu360UserMode
	 */
	public void setCurrentInitedClustered(HttpServletRequest request) {
		request.getSession(false).setAttribute("initedClustered", this.getInited());
	}

	/**
	 * 
	 * SpringSecurityContext unable retain currentMode object LMS-13478 
	 * @param request
	 * @param vu360UserMode
	 */
	private void setCurrentAvaiableModesClustered(HttpServletRequest request) {
		request.getSession(false).setAttribute("avaiableModesclustered", getAvailableModes());
	}
	
	/**
	 * @return the currentDistributor
	 */
	public com.softech.vu360.lms.vo.Distributor getProxyDistributor() {
		/*
		 * TODO: 03-09-2016
		 * if(currentDistributor!=null){
			Distributor objDis = distributorService.getDistributorById(currentDistributor.getId());
			return ProxyVOHelper.setDistributorProxy(objDis);
		}
		return ProxyVOHelper.setDistributorProxy(currentDistributor);*/
		
		return currentDistributorVO;
		
		
	}
	
	/**
	 * @return the corresponding Customer
	 */
	public com.softech.vu360.lms.vo.Customer getProxyCustomer() {
		Customer objCustomer = null;
		
		if(getOriginalPrincipal()!=null){
			if(originalPrincipal.isAdminMode()){
				//If the admin did not have a chance to select a customer 
				//then return the selected customer as his own learner's customer
				//NOT SURE ABOUT THIS LOGIC!!!
				if(currentCustomerVO == null){
					objCustomer = customerService.getCustomerById(originalPrincipal.getLearner().getCustomer().getId());
					return ProxyVOHelper.setCustomerProxy(objCustomer);
				}else{
					//@TODO
					
					/*
						(LMS-18319) The reason to add this "else" condition is, "currentCustomer" is unable to retrieve "customField" property (rising LazyException) 
						that is why we added "else" condition to refreshed customer from DB. it is temporary solution, will be changed soon.
						It compromises quality/performance  
					*/
					objCustomer =  customerService.getCustomerById(currentCustomerVO.getId());
					return ProxyVOHelper.setCustomerProxy(objCustomer);
				}

				//return currentCustomer;
			}else{
				objCustomer =  customerService.getCustomerById(originalPrincipal.getLearner().getCustomer().getId());
				return ProxyVOHelper.setCustomerProxy(objCustomer);
				//return originalPrincipal.getLearner().getCustomer();
			}
		}
		return null;
	}
	
	public static com.softech.vu360.lms.vo.VU360User getProxyUser() {
		/* Principal: 03-09-2016 */
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//loggedInUser = vu360UserService.getUserById(loggedInUser.getId());
		//return ProxyVOHelper.setUserProxy(loggedInUser);
		
		return loggedInUser;
	}

	/**
	 * @return the corresponding Customer
	 */
	public Customer getCurrentCustomer() {
		if(getOriginalPrincipal()!=null){
			if(originalPrincipal.isAdminMode()){
				//If the admin did not have a chance to select a customer 
				//then return the selected customer as his own learner's customer
				//NOT SURE ABOUT THIS LOGIC!!!
				if(currentCustomerVO == null){
					return customerService.getCustomerById(originalPrincipal.getLearner().getCustomer().getId());
				}else{
					//@TODO
					
					/*
						(LMS-18319) The reason to add this "else" condition is, "currentCustomer" is unable to retrieve "customField" property (rising LazyException) 
						that is why we added "else" condition to refreshed customer from DB. it is temporary solution, will be changed soon.
						It compromises quality/performance  
					*/
					return customerService.getCustomerById(currentCustomerVO.getId());
				}

				//return currentCustomer;
			}else{
				return customerService.getCustomerById(originalPrincipal.getLearner().getCustomer().getId());
			}
		}
		return null;
	}
	
	
	public com.softech.vu360.lms.vo.Customer getCurrentVOCustomer() {
		if(getOriginalPrincipal()!=null){
			if(originalPrincipal.isAdminMode()){
				if(currentCustomerVO == null){
					return originalPrincipal.getLearner().getCustomer();
				}else{
					return currentCustomerVO;
				}
			}else{
				return originalPrincipal.getLearner().getCustomer();
			}
		}
		return null;
	}
	
	public Long getCurrentCustomerId() {
		if(getOriginalPrincipal()!=null){
			if(originalPrincipal.isAdminMode()){
				if(currentCustomerVO == null){
					return originalPrincipal.getLearner().getCustomer().getId();
				}else{
					return currentCustomerVO.getId();
				}
			}else{
				return originalPrincipal.getLearner().getCustomer().getId();
			}
		}
		return null;
	}

	public static VU360User getCurrentUser() {
		com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360User loggedInUser = vu360UserService.getUserById(loggedInUserVO.getId());
		return loggedInUser;
	}
	
	public static VU360UserNew getCurrentSimpleUser() {
		com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360UserNew loggedInUser = vu360UserService.getSimpleUserById(loggedInUserVO.getId());
		return loggedInUser;
	}


	public Distributor getCurrentDistributor() {
		 if(currentDistributorVO!=null)
			 return distributorService.getDistributorById(currentDistributorVO.getId());
		 else
			 return null;
	}

	public AdminSearchType getCurrentSearchType() {
		return currentSearchType;
	}

	public List<VU360UserMode> getAvailableModes() {
		return availableModes;
	}

	public VU360UserMode getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(VU360UserMode vu360UserMode) {
		this.currentMode = vu360UserMode;
	}

	public com.softech.vu360.lms.vo.VU360User getOriginalPrincipal() {
		if(originalPrincipal==null){
			originalPrincipal = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		return originalPrincipal;
	}

	public com.softech.vu360.lms.vo.Learner getProxyLearner() {
		return currentLearnerVO;
	}

	public Boolean getInited() {
		return inited;
	}

	public void setInited(Boolean inited) {
		this.inited = inited;
	}

	public String getSwitchBackUrl() {
		return switchBackUrl;
	}

	public void setSwitchBackUrl(String switchBackUrl) {
		this.switchBackUrl = switchBackUrl;
	}
	
	// not in use 
	public void setClusteredDistributor(com.softech.vu360.lms.vo.Distributor distributorVO) {
		this.currentDistributorVO = distributorVO;
	}

	public void setClusteredCustomer(com.softech.vu360.lms.vo.Customer customerVO) {
		this.currentCustomerVO = customerVO;
	}

	public void setClusteredCurrentSearchType(AdminSearchType adminSearchType) {
		this.currentSearchType = adminSearchType;
	}
	
	public void setClusteredLearner(com.softech.vu360.lms.vo.Learner learner) {
		this.currentLearnerVO = learner;
	}

	public void setClusteredCurrentMode(VU360UserMode vu360UserMode){
		this.currentMode = vu360UserMode;
	}

	public void setClusteredPrincipal(com.softech.vu360.lms.vo.VU360User vu360User){
		this.originalPrincipal = vu360User;
	}

	public void setClusteredAvailableModes(List<VU360UserMode> avList){
		this.availableModes = avList;
	}

}
