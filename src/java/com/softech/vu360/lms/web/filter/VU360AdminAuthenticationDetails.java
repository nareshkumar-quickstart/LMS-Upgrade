package com.softech.vu360.lms.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class VU360AdminAuthenticationDetails extends WebAuthenticationDetails{

	private AdminSearchType currentSearchType = AdminSearchType.NONE;
	private com.softech.vu360.lms.vo.Customer currentCustomerVO = null;
	
	public VU360AdminAuthenticationDetails(HttpServletRequest request) {
		super(request);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.ui.WebAuthenticationDetails#doPopulateAdditionalInformation(javax.servlet.http.HttpServletRequest)
	 */
	public void doPopulateAdminSearchInformation(HttpServletRequest request) {
		Object objSwitchType = request.getAttribute("SwitchType");
		if(objSwitchType instanceof AdminSearchType){
			AdminSearchType currentType = (AdminSearchType)objSwitchType;
			switch(currentType){
			case CUSTOMER:
				currentSearchType = AdminSearchType.CUSTOMER; 
				currentCustomerVO = (com.softech.vu360.lms.vo.Customer)request.getAttribute("SwitchCustomer");
				break;
			case DISTRIBUTOR:
				currentSearchType = AdminSearchType.DISTRIBUTOR; 
				break;
			case LEARNER:
				currentSearchType = AdminSearchType.LEARNER; 
				break;
			default:
				currentSearchType = AdminSearchType.NONE; 
				break;
			
			}
		}
	}

	/**
	 * @return the currentSearchType
	 */
	public AdminSearchType getCurrentSearchType() {
		return currentSearchType;
	}


	public com.softech.vu360.lms.vo.Customer getCurrentCustomer() {
		return currentCustomerVO;
	}

	
}
