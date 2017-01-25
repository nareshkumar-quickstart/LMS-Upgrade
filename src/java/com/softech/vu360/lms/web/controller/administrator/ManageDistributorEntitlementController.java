package com.softech.vu360.lms.web.controller.administrator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.FormUtil;

/**
 * @author Dyutiman
 *
 */

@SuppressWarnings("all")
public class ManageDistributorEntitlementController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(UpdateEntitlementDetailsController.class.getName());
	private String viewDistributorEntitlementTemplate = null;
	private String addNewEntitlementTemplate = null;
	private String updateEntitlementTemplate = null;
	private String searchCourseGroupTemplate = null;
	private String failureTemplate = null;

	private static final String MANAGE_ENTITLEMENT_SAVE_ACTION = "Save";
	private static final String MANAGE_ENTITLEMENT_REMOVE_ACTION = "removeEntitlements";
        

	private LearnerService learnerService = null;
	private EntitlementService entitlementService = null;
	private CustomerService customerService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public ModelAndView showEntitlements(HttpServletRequest request, HttpServletResponse response) {

		try {
                        //For some reason the request could not get routed to removeEntitlements request handler.
                        String action = request.getParameter("action");
                        if( StringUtils.isNotEmpty(action))
                        {
                            if( MANAGE_ENTITLEMENT_REMOVE_ACTION.equalsIgnoreCase(action)){
                                String distributorEntitlementIdStr = request.getParameter("distributorEntitlementId");
                                log.debug("distributorEntitlementId: " + distributorEntitlementIdStr);
                                if( NumberUtils.isNumber(distributorEntitlementIdStr)){
                                    Long distributorEntitlementId = Long.valueOf(distributorEntitlementIdStr);
                                    removeEntitlement(distributorEntitlementId);
                                }else{
                                    throw new IllegalArgumentException("Incorrect distributorEntitlementId" + distributorEntitlementIdStr);
                                }
                            }
                        }
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0022");
				
			Map<Object, Object> context = new HashMap<Object, Object>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Distributor distributor = null;
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				distributor = details.getCurrentDistributor();
				
				if( distributor != null ) {
					List<DistributorEntitlement> listDistributorEntitlements =
						entitlementService.getAllDistributorEntitlements(distributor);
					context.put("DistributorEntitlementsList", listDistributorEntitlements);
					context.put("selectedDistributor", distributor);
					return new ModelAndView(viewDistributorEntitlementTemplate, "context", context);
				} else {
					return new ModelAndView(failureTemplate, "isRedirect", "d");
				}
			} else {
				// admin has not selected any distributor
				return new ModelAndView(failureTemplate, "isRedirect", "d");
			}
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(viewDistributorEntitlementTemplate);
	}
        
        /**
         * Removes an entitlement from the distributor.
         * @param entitlementId 
         */
        private void removeEntitlement(Long entitlementId){
            log.debug("Removing entitlement with id: " + entitlementId);
            DistributorEntitlement distributorEntitlement = entitlementService.
                getDistributorEntitlementById(entitlementId);
            log.debug("Distributor entitlement name: "+ distributorEntitlement.getName());
            entitlementService.removeDistributorEntitlement(distributorEntitlement);
        }

        /**
         * Removes the contracts from the distributor.
         * @param request 
         * @param response 
         */
        public ModelAndView removeEntitlements(HttpServletRequest request, HttpServletResponse response) {
            log.debug("removeEntitlements begin.");
            
            String distributorEntitlementIdStr = request.getParameter("distributorEntitlementId");
            log.debug("distributorEntitlementId: " + distributorEntitlementIdStr);
            if( NumberUtils.isNumber(distributorEntitlementIdStr)){
                Long distributorEntitlementId = Long.valueOf(distributorEntitlementIdStr);
                removeEntitlement(distributorEntitlementId);
            }else{
                throw new IllegalArgumentException("Incorrect distributorEntitlementId" + distributorEntitlementIdStr);
            }
            
            return showEntitlements(request, response);
        }

	/**
	 *  method used to edit a existing distributor entitlement
	 * 
	 */
	public ModelAndView updateDistributorEntitlement(HttpServletRequest request, HttpServletResponse response) {
            
		try {
			
			/**
			 * LMS-9512 | S M Humayun | 28 Mar 2011
			 */
			request.getSession(true).setAttribute("feature", "LMS-ADM-0022");
				
			Map<Object, Object> context = new HashMap<Object, Object>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Distributor distributor = null;
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				if( details.getCurrentDistributor() != null ) {
					distributor = details.getCurrentDistributor();
					List<DistributorEntitlement> listDistributorEntitlements = 
						entitlementService.getAllDistributorEntitlements(distributor);
					context.put("selectedDistributor", distributor);
				} else {
					return new ModelAndView(failureTemplate, "isRedirect", "d");
				}
			} else {
				// admin has not selected any distributor
				return new ModelAndView(failureTemplate, "isRedirect", "d");
			}
			String action = request.getParameter("action");
			if( StringUtils.isNotBlank(action) ) {
				DistributorEntitlement distributorEntitlement = entitlementService.loadForUpdateDistributorEntitlement(Long.valueOf((String)request.getParameter("distributorEntitlementId")));
				if( action.equalsIgnoreCase(MANAGE_ENTITLEMENT_SAVE_ACTION) ) {
					if( !validate(request, context) ) {
						int sumMaxEnrollments = 0;
						//editing or setting of customerEntitlement object
						distributorEntitlement.setName(request.getParameter("entitlementName"));
						distributorEntitlement.setAllowSelfEnrollment(new Boolean(request.getParameter("allowSelfEnrollment")));
						SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
						if (!request.getParameter("startDate").isEmpty()){
							Date myDate = formatter.parse(request.getParameter("startDate"));
							distributorEntitlement.setStartDate(myDate);
						}else{
							distributorEntitlement.setStartDate(null);
						}	
						if(request.getParameter("termsOfServicesRadio") != null){
							if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("days")){
								distributorEntitlement.setEndDate(null);
								distributorEntitlement.setDefaultTermOfServiceInDays(Integer.parseInt(request.getParameter("termsOfServices")));
							}else if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("date")){
								if (!request.getParameter("endDate").isEmpty()){
									Date myDate = formatter.parse(request.getParameter("endDate"));
									distributorEntitlement.setEndDate(FormUtil.formatToDayEnd(myDate));
									distributorEntitlement.setDefaultTermOfServiceInDays(0);
								}else{
									distributorEntitlement.setEndDate(null);
									distributorEntitlement.setDefaultTermOfServiceInDays(0);
								}
							}
						}
						/*if(request.getParameter("maximumEnrollmentsRadio").equalsIgnoreCase("notUnlimited")){
							distributorEntitlement.setAllowUnlimitedEnrollments(new Boolean("false"));
							distributorEntitlement.setMaxNumberSeats(Integer.parseInt(request.getParameter("maximumEnrollments")));
						}else if(request.getParameter("maximumEnrollmentsRadio").equalsIgnoreCase("unlimited")){
							distributorEntitlement.setAllowUnlimitedEnrollments(new Boolean("true"));
							distributorEntitlement.setMaxNumberSeats(0);
						}*/
						distributorEntitlement.setDistributor(distributorEntitlement.getDistributor());
/*						if(request.getParameter("transactionAmount") == null || request.getParameter("transactionAmount").equals("")) {
							distributorEntitlement.setTransactionAmount(0.0);
						} else {
							distributorEntitlement.setTransactionAmount(Double.valueOf(request.getParameter("transactionAmount")));
						}

*/
						Double amt = null;
						if(StringUtils.isNotEmpty(request.getParameter("transactionAmount"))){
							amt = Double.parseDouble(request.getParameter("transactionAmount"));
							distributorEntitlement.setTransactionAmount(amt);
						}else{
							amt = Double.parseDouble("0.0");
							distributorEntitlement.setTransactionAmount(amt);
						}
						
						if(distributorEntitlement.isAllowUnlimitedEnrollments()){
							entitlementService.saveDistributorEntitlement(distributorEntitlement);
						}else{
							if(distributorEntitlement.getMaxNumberSeats() < sumMaxEnrollments){
								context.put("errorMsg", new Boolean(true));
							}else{
								context.put("errorMsg", new Boolean(false));
								entitlementService.saveDistributorEntitlement(distributorEntitlement);
							}
						}
					} else {
						Map<Object, Object> map1 = new HashMap<Object, Object>();
						String distributorEntitlementId = request.getParameter("distributorEntitlementId");
						distributorEntitlement= entitlementService.getDistributorEntitlementById(Long.valueOf(distributorEntitlementId));
						String startDateString = null;
						String endDateString = null;
						SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
						if(distributorEntitlement.getEndDate() != null) {
							endDateString = formatter.format(distributorEntitlement.getEndDate());
						}
						if(distributorEntitlement.getStartDate() != null){
							startDateString = formatter.format(distributorEntitlement.getStartDate());
						}
						
						context.put("distributorEntitlement",distributorEntitlement);
                                                context.put("distributorEntitlementId",distributorEntitlement.getId());
						context.put("endDate",endDateString);
						context.put("startDate",startDateString);
						context.put("countmap", map1);
						return new ModelAndView(updateEntitlementTemplate, "context", context);
					}
				}
				List<DistributorEntitlement> listDistributorEntitlements = entitlementService.getAllDistributorEntitlements(distributor);
				context.put("DistributorEntitlementsList", listDistributorEntitlements);
				return new ModelAndView(viewDistributorEntitlementTemplate, "context", context);
			}
			Map<Object, Object> map1 = new HashMap<Object, Object>();
			String distributorEntitlementId = request.getParameter("distributorEntitlementId");
			DistributorEntitlement distributorEntitlement= entitlementService.getDistributorEntitlementById(Long.valueOf(distributorEntitlementId));
			String startDateString = null;
			String endDateString = null;
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			
                        if(distributorEntitlement.getEndDate() != null) {
				endDateString = formatter.format(distributorEntitlement.getEndDate());
			}
			if(distributorEntitlement.getStartDate() != null){
				startDateString = formatter.format(distributorEntitlement.getStartDate());
			}
			if(distributorEntitlement.getTransactionAmount()==null){
				distributorEntitlement.setTransactionAmount(0.0);
			}
					
					
			context.put("distributorEntitlement",distributorEntitlement);
                        context.put("distributorEntitlementId",distributorEntitlement.getId());
			context.put("endDate",endDateString);
			context.put("startDate",startDateString);
			context.put("countmap", map1);
			return new ModelAndView(updateEntitlementTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(updateEntitlementTemplate);
	}


	@SuppressWarnings("unchecked")
	private boolean validate(HttpServletRequest request,Map context){
		boolean check = false;
		long diffInMilliseconds =(24 * 60 * 60 * 1000);
		if(StringUtils.isBlank(request.getParameter("entitlementName"))){
			context.put("validateName","error.customerentitlement.validateNameBlank");
			check = true;
		}
		/*if(request.getParameter("maximumEnrollmentsRadio").equalsIgnoreCase("notUnlimited") 
				&& StringUtils.isBlank(request.getParameter("maximumEnrollments"))){
			context.put("validateMaximumEnrollments","error.customerentitlement.maximumEnrollment");
			check = true;
		}*/
		if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("days") 
				&& StringUtils.isBlank(request.getParameter("termsOfServices"))){
			context.put("validateTermsofServices","error.customerentitlement.enterdays");
			check = true;
		}
		if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("date") && StringUtils.isBlank(request.getParameter("endDate"))){
			context.put("validateTermsofServicesInDate","error.customerentitlement.enterdate");
			check = true;
		}
		if(request.getParameter("termsOfServicesRadio").equalsIgnoreCase("days") && !request.getParameter("termsOfServices").isEmpty() 
			&& !FieldsValidation.isNumeric(request.getParameter("termsOfServices"))){
			context.put("validateTermsofServicesNumericField","error.customerentitlement.InvalidInput");
			check = true;
		}
		/*if(request.getParameter("maximumEnrollmentsRadio").equalsIgnoreCase("notUnlimited") 
				&& request.getParameter("maximumEnrollments") != "" &&!FieldsValidation.isNumeric(request.getParameter("maximumEnrollments"))){
			context.put("validateMaximumEnrollmentsNumericField","error.customerentitlement.InvalidInput");
			check = true;
		}*/
		DistributorEntitlement distributorEntitlement = null;
		String entitlementName = request.getParameter("entitlementName");
		long distributorEntitlementId=Long.valueOf((String)request.getParameter("distributorEntitlementId"));
		distributorEntitlement=entitlementService.getDistributorEntitlementByName(entitlementName,
				entitlementService.getDistributorEntitlementById(distributorEntitlementId).getDistributor().getId());
		//long distributorEntitlementId=Long.valueOf((String)request.getParameter("distributorEntitlementId"));
		
		if (distributorEntitlement != null){
			if (distributorEntitlement.getId() !=distributorEntitlementId){
				context.put("validateDuplicateName", "error.roleName.exists");
				
				
				check = true;
			}
		}
		if(StringUtils.isBlank(request.getParameter("startDate"))){
			context.put("validateStartDate", "error.customerentitlement.invalidStartDate");
			check = true;
			
		}else{
			if(!FieldsValidation.isValidDate(request.getParameter("startDate"))){
				context.put("validateStartDate", "error.customerentitlement.DuplicateName");
				check = true;
	
			}else{
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date newEnrollmentDate = null;
				try{
					newEnrollmentDate=formatter.parse(request.getParameter("startDate"));
					Calendar calender=Calendar.getInstance();
					Date date=calender.getTime();
					
					
					/*if( ( newEnrollmentDate.getTime()-date.getTime()  )/ diffInMilliseconds < 0){
						context.put("startDate", "error.customerentitlement.invalidStartDate");
					}*/
				}
				catch (ParseException e) {
					context.put("startDate", "error.addNewLearner.expDate.invalidDate");
					//errors.rejectValue("startDate", "error.addNewLearner.expDate.invalidDate");
					e.printStackTrace();
					check = true;
				}
				
			}
		}
		if(!StringUtils.isBlank(request.getParameter("endDate"))){
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date newEnrollmentDate = null;
			try{
				newEnrollmentDate=formatter.parse(request.getParameter("endDate"));
			}
			catch (ParseException e) {
				context.put("startDate", "error.addNewLearner.expDate.invalidDate");
				//errors.rejectValue("startDate", "error.addNewLearner.expDate.invalidDate");
				e.printStackTrace();
				check = true;
			}
			
			Date startDate = null;
			try{
				startDate=formatter.parse(request.getParameter("startDate"));
				Calendar calender=Calendar.getInstance();
				Date date=calender.getTime();
				if( (  newEnrollmentDate.getTime()-startDate.getTime() )/ diffInMilliseconds < 0){
					context.put("validateTermsofServicesInDate", "error.customerentitlement.invalidEndDate");
					check = true;
				}
			}
			catch (ParseException e) {
				context.put("startDate", "error.addNewLearner.expDate.invalidDate");
				//errors.rejectValue("startDate", "error.addNewLearner.expDate.invalidDate");
				e.printStackTrace();
				check = true;
			}
			
			
			
		}
		/*String value = request.getParameter("transactionAmount");*/
		/*if(!StringUtils.isBlank(request.getParameter("transactionAmount")) && !FieldsValidation.isNumeric(request.getParameter("transactionAmount"))) {
			context.put("validateTransactionAmount", "error.customerentitlement.invalidTransactionAmount");
			check = true;
		}*/

		String amt = request.getParameter("transactionAmount");
		try {
			if ( StringUtils.isNotEmpty(amt) ){
				if(StringUtils.isNumeric(Math.round(Double.parseDouble(amt))+"")){
					//Max DB length: 18 digit including 2 precision
					Double val = Double.parseDouble(amt);
					if ( String.valueOf(val.longValue()).length() > 16) {
						context.put("validateTransactionAmount", "error.customerentitlement.invalidTransactionAmount");
						check = true;
					}
				}
			}
		} catch (java.lang.NumberFormatException e) {
			context.put("validateTransactionAmount", "error.customerentitlement.invalidTransactionAmount");
			check=true;
		}
		
		
		/*for(char c : value.toCharArray()) {
			if(!Character.isDigit(c) && c != '.') {
				context.put("validateTransactionAmount", "error.customerentitlement.invalidTransactionAmount");
				check = true;
				break;
			}
		}*/
		return check;
	}
        
        /*
	 *  getter & setters
	 */
	public String getViewDistributorEntitlementTemplate() {
		return viewDistributorEntitlementTemplate;
	}

	public void setViewDistributorEntitlementTemplate(
			String viewDistributorEntitlementTemplate) {
		this.viewDistributorEntitlementTemplate = viewDistributorEntitlementTemplate;
	}

	public String getFailureTemplate() {
		return failureTemplate;
	}

	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public String getAddNewEntitlementTemplate() {
		return addNewEntitlementTemplate;
	}

	public void setAddNewEntitlementTemplate(String addNewEntitlementTemplate) {
		this.addNewEntitlementTemplate = addNewEntitlementTemplate;
	}

	public String getUpdateEntitlementTemplate() {
		return updateEntitlementTemplate;
	}

	public void setUpdateEntitlementTemplate(String updateEntitlementTemplate) {
		this.updateEntitlementTemplate = updateEntitlementTemplate;
	}

	public String getSearchCourseGroupTemplate() {
		return searchCourseGroupTemplate;
	}

	public void setSearchCourseGroupTemplate(String searchCourseGroupTemplate) {
		this.searchCourseGroupTemplate = searchCourseGroupTemplate;
	}

    

}