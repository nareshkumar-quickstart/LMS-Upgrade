package com.softech.vu360.lms.web.controller.administrator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class ManageCustomerController extends MultiActionController implements InitializingBean {
	
	private static final Logger log = Logger.getLogger(ManageCustomerController.class.getName());
	private String searchCustomerTemplate = null;
	private static final String MANAGE_CUSTOMER_DELETE_ACTION = "Delete";
	private CustomerService customerService = null;

	public ModelAndView displaySearchCustomer(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			HttpSession session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			String searchType = request.getParameter("searchType");
			String action = request.getParameter("action");
			if(StringUtils.isNotBlank(action)){
				if(action.equalsIgnoreCase(MANAGE_CUSTOMER_DELETE_ACTION)){
					String[] selectedCustomerValues = request.getParameterValues("selectCustomer");
					long customerIdArray[] = new long[selectedCustomerValues.length];
					if( selectedCustomerValues != null ){
						for( int i=0; i<selectedCustomerValues.length; i++ ) {
							String customerID = selectedCustomerValues[i];
							if( StringUtils.isNotBlank(customerID)) {
								customerIdArray[i]=Long.parseLong(selectedCustomerValues[i]);
							}	
						}		
						customerService.deleteCustomers(customerIdArray);
					}
					List<Customer> listOfCustomers = customerService.findCustomersByName("","", loggedInUser, false);
					context.put("listOfCustomers", listOfCustomers);
					context.put("typeOfSearch", searchType);
					return new ModelAndView(searchCustomerTemplate, "context", context);
				}
			}	
			if(searchType == null){
				context.put("listOfCustomers", null);
				context.put("typeOfSearch","simpleSearch");
				return new ModelAndView(searchCustomerTemplate,"context",context);
			}else if(StringUtils.isNotBlank(searchType)){
				searchType = request.getParameter("searchType");
				if(searchType.equalsIgnoreCase("simplesearch")){
					String searchStr=null;
					if(request.getParameter("simpleSearchCriteria") != null){
						session.setAttribute("simpleSearchCriteria",request.getParameter("simpleSearchCriteria"));
						searchStr = request.getParameter("simpleSearchCriteria");
					}else{
						searchStr = (String)session.getAttribute("simpleSearchCriteria");
					}
					List<Customer> listOfCustomers = customerService.findCustomersByName(searchStr,"", loggedInUser, false);
					context.put("listOfCustomers", listOfCustomers);
					context.put("simpleSearchcriteria", request.getParameter("simpleSearchCriteria"));
					context.put("typeOfSearch","simpleSearch");
					session.setAttribute("typeOfSearch", "simpleSearch");
				}else if(searchType.equalsIgnoreCase("advancedsearch")){
					List<Customer> listOfCustomers = customerService.findCustomersByName(request.getParameter("customerName"),request.getParameter("orderId"),loggedInUser, false);
					context.put("listOfCustomers", listOfCustomers);
					context.put("typeOfSearch","advancedSearch");
					session.setAttribute("typeOfSearch", "advancedSearch");
				}else if(searchType.equalsIgnoreCase("allsearch")){
					List<Customer> listOfCustomers = customerService.findCustomersByName("", "",loggedInUser, false);
					context.put("listOfCustomers", listOfCustomers);
					context.put("typeOfSearch",(String)session.getAttribute("typeOfSearch"));
				}
				return new ModelAndView(searchCustomerTemplate,"context",context);
			}
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchCustomerTemplate);
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public String getSearchCustomerTemplate() {
		return searchCustomerTemplate;
	}

	public void setSearchCustomerTemplate(String searchCustomerTemplate) {
		this.searchCustomerTemplate = searchCustomerTemplate;
	}

}