package com.softech.vu360.lms.web.controller.administrator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.DistributorSort;

public class EditDistributorViewAdministratorController extends MultiActionController implements
InitializingBean {

	private static final Logger log = Logger.getLogger(EditDistributorViewAdministratorController.class.getName());
	private String editDistributorViewAdministratorTemplate = null;
	private String editDistributorAddAdministratorTemplate = null;
	private String editDistributorAddAdministratorResellerTemplate = null;
	private String redirectTemplate = null;
	private String redirectViewTemplate = null;

	private DistributorService distributorService = null;
	private LearnerService learnerService= null;
	HttpSession session = null;

	private static final String EDITDISTRIBUTOR_VIEWADMINSTRATOR_DELETEADMINISTRATOR_ACTION = "delete";
	private static final String EDITDISTRIBUTOR_VIEWADMINSTRATOR_ADDADMINISTRATORBUTTON_ACTION="addAdministrator";
	private static final String EDITDISTRIBUTOR_VIEWADMINSTRATOR_SELECT_RESELLER_ACTION = "selectReseller";

	private static final String MANAGE_TRAININGPLAN_SEARCH_ACTION = "Search";
	private static final String MANAGE_TRAININGPLAN_ADVANCEDSEARCH_ACTION = "advanceSearch";
	private static final String MANAGE_TRAININGPLAN_SHOWALLPLANS_ACTION = "allsearch";
	private static final String EDITDISTRIBUTOR_VIEWADMINSTRATOR_ASSIGN_ADMINISTRATOR_ACTION = "save";

	public ModelAndView editDistributorViewAdministrator(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			session = request.getSession();
			DistributorGroup distributorGroup = distributorService.getDistributorGroupById(Long.valueOf(request.getParameter("Id")));
			String action = request.getParameter("action");
			if(StringUtils.isNotBlank(action)){
				if(action.equalsIgnoreCase(EDITDISTRIBUTOR_VIEWADMINSTRATOR_ADDADMINISTRATORBUTTON_ACTION)){
					context.put("distributorGroupId", Long.valueOf(request.getParameter("Id")));
					return new ModelAndView(redirectTemplate,"context",context);
				}else if(action.equalsIgnoreCase(EDITDISTRIBUTOR_VIEWADMINSTRATOR_DELETEADMINISTRATOR_ACTION)){
					String[] selectedAdministratorValues = request.getParameterValues("admins");
					Long adminstratorIdArray[] = new Long[selectedAdministratorValues.length];
					if( selectedAdministratorValues != null ){
						for( int i=0; i<selectedAdministratorValues.length; i++ ) {
							String adminID = selectedAdministratorValues[i];
							if( StringUtils.isNotBlank(adminID)) {
								adminstratorIdArray[i]=Long.parseLong(selectedAdministratorValues[i]);
							}	
						}	
						if (adminstratorIdArray.length>0)
							distributorService.unassignLMSAdministratorsToDistributorGroup(adminstratorIdArray, distributorGroup.getId().longValue());
					}
				}
			}
			List<LMSAdministrator> listOfLMSAdministrator = distributorService.getLMSAdministratorsByDistributorGroupId(Long.valueOf(request.getParameter("Id")));
			session.setAttribute("distributorGroupId", Long.valueOf(request.getParameter("Id")));// Require when saving Add Administrators on Save
			context.put("listOfLMSAdministrator", listOfLMSAdministrator);
			context.put("distributorGroup",distributorGroup);
			return new ModelAndView(editDistributorViewAdministratorTemplate,"context",context);
			
		}catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editDistributorViewAdministratorTemplate);
	}

	public ModelAndView editDistributorAddAdministrator(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			DistributorGroup distributorGroup = distributorService.getDistributorGroupById(Long.valueOf(request.getParameter("Id")));
			Map<Object, Object> context = new HashMap<Object, Object>();
			session = request.getSession();
			String action = request.getParameter("action");
			if(StringUtils.isNotBlank(action)){
				if(action.equalsIgnoreCase(EDITDISTRIBUTOR_VIEWADMINSTRATOR_ASSIGN_ADMINISTRATOR_ACTION)){
					String[] selectedAdminstratorValues = request.getParameterValues("selectAdminstrator");
					String[] selectReseller = request.getParameterValues("selectReseller");
					
					Long adminstratorIdArray[] = new Long[selectedAdminstratorValues.length];
					if( selectedAdminstratorValues != null ){
						for( int i=0; i<selectedAdminstratorValues.length; i++ ) {
							String adminstratorID = selectedAdminstratorValues[i];
							if( StringUtils.isNotBlank(adminstratorID) ) {
								
								//check already exist
								List<Distributor> alreadyExist = distributorService.getAllowedDistributorByGroupId(String.valueOf(distributorGroup.getId()) , selectedAdminstratorValues[i]);
								if(alreadyExist==null || alreadyExist.size()==0)
									adminstratorIdArray[i]=Long.parseLong(selectedAdminstratorValues[i]);

							}	
						}
						if (adminstratorIdArray.length>0)
							distributorService.assignLMSAdministratorsToDistributorGroup(adminstratorIdArray, distributorGroup.getId().longValue(), selectReseller);
					}
					List<LMSAdministrator> listOfLMSAdministrator = distributorService.getLMSAdministratorsByDistributorGroupId(Long.parseLong(session.getAttribute("distributorGroupId").toString()));
					context.put("listOfLMSAdministrator", listOfLMSAdministrator);
					context.put("distributorGroupId", Long.valueOf(distributorGroup.getId()));

					return new ModelAndView(redirectViewTemplate, "context", context);
					
				}else if(action.equalsIgnoreCase(MANAGE_TRAININGPLAN_SHOWALLPLANS_ACTION)){
					//List<LMSAdministrator> listOfSelectedLMSAdministrator = distributorService.getLMSAdministratorsByDistributorGroupId(Long.valueOf(request.getParameter("Id")));
					//List<LMSAdministrator> listOfLMSAdministrator = distributorService.findLMSAdministrators("", loggedInUser);
					
					String firstname=request.getParameter("firstname");
					String lastname=request.getParameter("lastname");
					String emailaddress=request.getParameter("emailaddress");
					context.put("firstname", firstname);
					context.put("lastname", lastname);
					context.put("emailaddress", emailaddress);

					List<LMSAdministrator> listOfLMSAdministrator = distributorService.findLMSAdministrators(firstname, lastname, emailaddress, loggedInUser);
					context.put("listOfLMSAdministrator", listOfLMSAdministrator);
					
				}else if(action.equalsIgnoreCase(MANAGE_TRAININGPLAN_SEARCH_ACTION)){
					context.put("searchType","simpleSearch");
					
					String searchStr=null;
					if(request.getParameter("simpleSearchCriteria") != null){   //simpleSearchCriteria=single text box id
//						session.setAttribute("simpleSearchCriteria",request.getParameter("simpleSearchCriteria"));
						searchStr = request.getParameter("simpleSearchCriteria");
					}
					context.put("simpleSearchcriteria", searchStr);
					//List<LMSAdministrator> listOfSelectedLMSAdministrator = distributorService.getLMSAdministratorsByDistributorGroupId(Long.valueOf(request.getParameter("Id")));
					//List<LMSAdministrator> listOfLMSAdministrator = distributorService.findLMSAdministrators(searchStr, loggedInUser);
					//context.put("listOfSelectedLMSAdministrator", listOfSelectedLMSAdministrator);

					String firstname=request.getParameter("firstname");
					String lastname=request.getParameter("lastname");
					String emailaddress=request.getParameter("emailaddress");
					context.put("firstname", firstname);
					context.put("lastname", lastname);
					context.put("emailaddress", emailaddress);

					List<LMSAdministrator> listOfLMSAdministrator = distributorService.findLMSAdministrators(firstname, lastname, emailaddress, loggedInUser);
					context.put("listOfLMSAdministrator", listOfLMSAdministrator);
					
				}else if(action.equalsIgnoreCase(MANAGE_TRAININGPLAN_ADVANCEDSEARCH_ACTION)){
					
					context.put("searchType","advanceSearch");
					String firstname=request.getParameter("firstname");
					String lastname=request.getParameter("lastname");
					String emailaddress=request.getParameter("emailaddress");
					context.put("firstname", firstname);
					context.put("lastname", lastname);
					context.put("emailaddress", emailaddress);
					List<LMSAdministrator> listOfLMSAdministrator = distributorService.findLMSAdministrators(firstname, lastname, emailaddress, loggedInUser);
					
					if(listOfLMSAdministrator!=null && listOfLMSAdministrator.size()>0)
						context.put("listOfLMSAdministrator", listOfLMSAdministrator);
					
					String strArry[] = request.getParameterValues("selectAdminstrator");
					if(strArry!=null && strArry.length>=0)		
						context.put("lasTimeselectedAdmin", Arrays.asList(strArry));
					
				}
				else if(action.equalsIgnoreCase(EDITDISTRIBUTOR_VIEWADMINSTRATOR_SELECT_RESELLER_ACTION)){
					
					DistributorGroup distributorGroup_ = distributorService.getDistributorGroupById(Long.valueOf(request.getParameter("Id")));
					List<Distributor> groupDistributors = distributorGroup_.getDistributors();
					
					//previous page param
					String[] selectAdminstrator = request.getParameterValues("selectAdminstrator");
					context.put("selectedLMSAdministrator", Arrays.asList(selectAdminstrator));
					context.put("firstname", request.getParameter("firstname"));
					context.put("lastname", request.getParameter("lastname"));
					context.put("emailaddress", request.getParameter("emailaddress"));

					//paging and sorting params
					performSorting(groupDistributors, request);

					context.put("sortDirection", (request.getParameter("sortDirection")==null) ? 0 : request.getParameter("sortDirection") )   ;
					context.put("sortColumnIndex", (request.getParameter("sortColumnIndex")==null) ? 0 : request.getParameter("sortDirection")							);
					context.put("showAll", isShowAll(request));

					
					context.put("resellerOf", distributorGroup_.getName());
					context.put("listOfReseller", groupDistributors);
					context.put("distributorGroup", distributorGroup_);
					

					return new ModelAndView(editDistributorAddAdministratorResellerTemplate, "context", context);
				}
				else if(action.equalsIgnoreCase("sortReseller")){

					DistributorGroup distributorGroup_ = distributorService.getDistributorGroupById(Long.valueOf(request.getParameter("Id")));
					List<Distributor> groupDistributors = distributorGroup_.getDistributors();
					
					performSorting(groupDistributors, request);

					context.put("sortDirection", getRequestParam("sortDirection", request));
					context.put("sortColumnIndex",
							getRequestParam("sortColumnIndex", request));

					context.put("showAll", isShowAll(request));
					context.put("listOfReseller", groupDistributors);
					
					return new ModelAndView(editDistributorAddAdministratorResellerTemplate, "context", context);


				}
				else if(action.equalsIgnoreCase("previous")){
					
				}
				else{
					request.setAttribute("newPage","true");
				}
//				context.put("searchType",session.getAttribute("searchType"));
			}else{
//				session.setAttribute("searchType", "simpleSearch");
				request.setAttribute("newPage","true");
			}
			context.put("distributorGroup",distributorGroup);
			return new ModelAndView(editDistributorAddAdministratorTemplate, "context", context);
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editDistributorAddAdministratorTemplate);
	}

	

	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}


	public String getEditDistributorViewAdministratorTemplate() {
		return editDistributorViewAdministratorTemplate;
	}


	public void setEditDistributorViewAdministratorTemplate(
			String editDistributorViewAdministratorTemplate) {
		this.editDistributorViewAdministratorTemplate = editDistributorViewAdministratorTemplate;
	}


	public DistributorService getDistributorService() {
		return distributorService;
	}


	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}


	public String getEditDistributorAddAdministratorTemplate() {
		return editDistributorAddAdministratorTemplate;
	}


	public void setEditDistributorAddAdministratorTemplate(
			String editDistributorAddAdministratorTemplate) {
		this.editDistributorAddAdministratorTemplate = editDistributorAddAdministratorTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getEditDistributorAddAdministratorResellerTemplate() {
		return editDistributorAddAdministratorResellerTemplate;
	}

	public void setEditDistributorAddAdministratorResellerTemplate(
			String editDistributorAddAdministratorResellerTemplate) {
		this.editDistributorAddAdministratorResellerTemplate = editDistributorAddAdministratorResellerTemplate;
	}

	
	
	public static void performSorting(List<Distributor> groupDistributors,  HttpServletRequest request){
		
		if (getRequestParam("sortColumnIndex", request) != null
				&& getRequestParam("sortColumnIndex", request).equals("0")) {
			
			DistributorSort compartor = new DistributorSort();
			compartor.setSortBy("name");
			if (getRequestParam("sortDirection", request) != null
					&& getRequestParam("sortDirection", request)
							.equals("1")) {
				compartor.setSortDirection(1);
			} else {
				compartor.setSortDirection(0);
			}

			Collections.sort(groupDistributors, compartor);

		}

	}
	
	public String getRedirectViewTemplate() {
		return redirectViewTemplate;
	}

	public void setRedirectViewTemplate(String redirectViewTemplate) {
		this.redirectViewTemplate = redirectViewTemplate;
	}

	private static String getRequestParam(String paramName,
			HttpServletRequest request) {
		String paramValue = request.getParameter(paramName);
		if (StringUtils.isNotBlank(paramValue)
				&& StringUtils.isNotEmpty(paramValue))
			return paramValue;
		return null;
	}
	
	private static String isShowAll(HttpServletRequest request) {

		String sa = request.getParameter("showAll");
		if (StringUtils.isEmpty(sa) || sa.equalsIgnoreCase("false")) {
			return "false";
		} else {
			return "true";
		}
	}
}