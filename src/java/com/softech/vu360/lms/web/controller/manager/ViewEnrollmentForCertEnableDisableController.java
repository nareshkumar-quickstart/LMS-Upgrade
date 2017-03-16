package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.AdminLearnerEnrollmentSearch;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEnrollmentForm;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEntitlementItem;
import com.softech.vu360.util.AdminLearnerEnrollmentSort;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Mohammed Rehan Rana
 * 14 Feb, 2012
 */
public class ViewEnrollmentForCertEnableDisableController extends VU360BaseMultiActionController {
	
	private static final Logger log = Logger.getLogger(ViewEnrollmentForCertEnableDisableController.class.getName());

	private String viewEnrollmentForCertEnableDisableTemplate = null;
	private String viewEnrollmentDetailForCertEnableDisableTemplate = null;
	
	private LearnerService learnerService;
	private VU360UserService vu360UserService;
	private EntitlementService entitlementService;
	private EnrollmentService enrollmentService;
	
	HttpSession session = null;
	
	public ViewEnrollmentForCertEnableDisableController() {
		super();
	}
	
	public ViewEnrollmentForCertEnableDisableController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		log.debug(" IN handleNoSuch Method ");
		return new ModelAndView(viewEnrollmentForCertEnableDisableTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof ViewLearnerEnrollmentForm ){
			ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
			if( methodName.equals("showEnrollments")) {
				VU360User user = vu360UserService.getUserById(form.getId());
				Learner learner = user.getLearner();
				List<LearnerEnrollment> enrollments =entitlementService.getLearnerEnrollmentsForLearner(learner);// learner.getEnrollments();
				form.setViewLearnerEntitlementItems(filterEnrollmets(enrollments));
			} 
		}
	}
	
	private List<ViewLearnerEntitlementItem> filterEnrollmets(List<LearnerEnrollment> learnerEnrollments){
		Map <String,CustomerEntitlement> customerEntitlement = new LinkedHashMap<String,CustomerEntitlement>();
		for(LearnerEnrollment learnerEnrollment: learnerEnrollments){
			if(learnerEnrollment.getCustomerEntitlement()!=null)
			customerEntitlement.put(learnerEnrollment.getCustomerEntitlement().getName(),learnerEnrollment.getCustomerEntitlement());
		}
		List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems= new ArrayList<ViewLearnerEntitlementItem>();
		for(String key:customerEntitlement.keySet()){
			ViewLearnerEntitlementItem viewLearnerEntitlementItem =new ViewLearnerEntitlementItem();
			viewLearnerEntitlementItem.setEntitlement(customerEntitlement.get(key));

			for(LearnerEnrollment learnerEnrollment: learnerEnrollments){
				if(learnerEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.ACTIVE)
						|| learnerEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED))
					
					if(learnerEnrollment.getCustomerEntitlement()!=null && !learnerEnrollment.isEnableCertificate()){
						ViewLearnerEnrollmentItem viewLearnerEnrollmentItem = new ViewLearnerEnrollmentItem();
						if(customerEntitlement.get(key).getName().contains(learnerEnrollment.getCustomerEntitlement().getName())){
							learnerEnrollment.setLastLockedCourse( this.enrollmentService.getLastLockedCourse(learnerEnrollment) ); // [2/8/2011] LMS-8769 :: Admin Mode > Manage Enrollments: System is showing incorrect Locked Course Status 
							viewLearnerEnrollmentItem.setLearnerEnrollment(learnerEnrollment);
							viewLearnerEnrollmentItem.setSelected(false);
							viewLearnerEnrollmentItem.setReady(false);
							viewLearnerEntitlementItem.addViewLearnerEnrollmentItem(viewLearnerEnrollmentItem);
						}
					}
			} 
			if(viewLearnerEntitlementItem.getViewLearnerEnrollmentItems() !=null)
				viewLearnerEntitlementItems.add(viewLearnerEntitlementItem);
		}
		return viewLearnerEntitlementItems;
	}
	
	protected void validate(HttpServletRequest request, Object command,	BindException errors, String methodName) throws Exception {
		
	}
	
	public ModelAndView enableCertificate(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {
		
		if( errors.hasErrors() ) {
			return new ModelAndView(viewEnrollmentDetailForCertEnableDisableTemplate);
		}
		
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		try {
			log.debug("IN enableCertificate METHOD");
			List<ViewLearnerEnrollmentItem> selectedViewLearnerEnrollmentItems= new ArrayList<ViewLearnerEnrollmentItem>();
			List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems=form.getViewLearnerEntitlementItems();
			for (ViewLearnerEntitlementItem viewLearnerEntitlementItem:viewLearnerEntitlementItems){
				for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
					if(viewLearnerEnrollmentItem.isSelected()){
						selectedViewLearnerEnrollmentItems.add(viewLearnerEnrollmentItem);
					}
				}
			}

			Long enrolmentIdArray[] = new Long[selectedViewLearnerEnrollmentItems.size()];
			int count=0;
			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:selectedViewLearnerEnrollmentItems){
				enrolmentIdArray[count]=viewLearnerEnrollmentItem.getLearnerEnrollment().getId();
				count++;
			}
			enrollmentService.enableCertificate(enrolmentIdArray, true);
			onBind(request,command,"showEnrollments");

		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(viewEnrollmentDetailForCertEnableDisableTemplate);
	}
	
	public ModelAndView showSearchLearnerPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		request.getSession(true).setAttribute("feature", "LMS-ADM-0002");

		ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
		enrollForm.reset();
		return new ModelAndView(viewEnrollmentForCertEnableDisableTemplate);
	}
	
	public ModelAndView searchLearner(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		try {
			ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
			String searchType = enrollForm.getSearchType();
			session = request.getSession();
			String sortDirection = request.getParameter("sortDirection");
			if( StringUtils.isNotBlank(sortDirection)&& session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			if( StringUtils.isNotBlank(sortColumnIndex) && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);
			//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if(searchType.equalsIgnoreCase("simplesearch")){

			}else if(searchType.equalsIgnoreCase("advancedsearch")){
				List<VU360User> searchedUsers = new ArrayList<VU360User>();					
 
				// to stop empty searches 
				if( enrollForm.getFirstName().trim().length() == 0 &&  enrollForm.getLastName().trim().length() == 0 && enrollForm.getEmailAddress().trim().length() ==0 )
					context.put("empty_search_exception", "Exception2");
				else{
					Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
					String showAllLimit = brander.getBrandElement("lms.resultSet.showAll.Limit") ;
					int intShowAllLimit = Integer.parseInt(showAllLimit.trim());
					List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
					searchedUsers = learnerService.findAllLearner(StringUtils.isBlank(enrollForm.getFirstName())?"":enrollForm.getFirstName(), StringUtils.isBlank(enrollForm.getLastName())?"":enrollForm.getLastName() ,  StringUtils.isBlank(enrollForm.getEmailAddress())?"":enrollForm.getEmailAddress() ,
							loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
							0, VelocityPagerTool.DEFAULT_PAGE_SIZE, "", 0 , intShowAllLimit );
				}
				List<AdminLearnerEnrollmentSearch> adminLearnerEnrollmentSearchList = new ArrayList<AdminLearnerEnrollmentSearch>();
				
				for (VU360User vu360User : searchedUsers) {
					AdminLearnerEnrollmentSearch adminSearchMember = new AdminLearnerEnrollmentSearch();
					if(vu360User.getLearner()==null)
						continue;
					adminSearchMember.setId(vu360User.getId());
					adminSearchMember.setFirstName(vu360User.getFirstName());
					adminSearchMember.setLastName(vu360User.getLastName());
					adminSearchMember.setEMail(vu360User.getUsername());
					adminLearnerEnrollmentSearchList.add(adminSearchMember);
				}
				enrollForm.setAdminSearchMemberList(adminLearnerEnrollmentSearchList);
				String pageIndex = request.getParameter("pageCurrIndex");
				if( pageIndex == null ) pageIndex = "0";
				log.debug("pageIndex = " + pageIndex);
				Map<String, String> PagerAttributeMap = new HashMap <String, String>();
				PagerAttributeMap.put("pageIndex", pageIndex);

				// manually sorting
				if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {
					request.setAttribute("PagerAttributeMap", PagerAttributeMap);
					// sorting against customer name
					request.setAttribute("PagerAttributeMap", PagerAttributeMap);
					if( sortColumnIndex.equalsIgnoreCase("0") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {
							AdminLearnerEnrollmentSort adminLearnerEnrollmentSort=new AdminLearnerEnrollmentSort();
							adminLearnerEnrollmentSort.setSortBy("firstName");
							adminLearnerEnrollmentSort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(adminLearnerEnrollmentSearchList,adminLearnerEnrollmentSort);

							context.put("sortDirection", 0);
							context.put("sortColumnIndex", 0);
						} else {
							AdminLearnerEnrollmentSort adminLearnerEnrollmentSort=new AdminLearnerEnrollmentSort();
							adminLearnerEnrollmentSort.setSortBy("firstName");

							adminLearnerEnrollmentSort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(adminLearnerEnrollmentSearchList,adminLearnerEnrollmentSort);

							context.put("sortDirection", 1);
							context.put("sortColumnIndex", 0);
						}
						// sorting against e-mail address
					} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {
							// list of in-active members 
							AdminLearnerEnrollmentSort adminLearnerEnrollmentSort = new AdminLearnerEnrollmentSort();
							adminLearnerEnrollmentSort.setSortBy("email");
							adminLearnerEnrollmentSort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(adminLearnerEnrollmentSearchList,adminLearnerEnrollmentSort);
							context.put("sortDirection", 0);
							context.put("sortColumnIndex", 2);
						} else {
							// list of active members 
							AdminLearnerEnrollmentSort adminLearnerEnrollmentSort=new AdminLearnerEnrollmentSort();
							adminLearnerEnrollmentSort.setSortBy("email");

							adminLearnerEnrollmentSort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(adminLearnerEnrollmentSearchList,adminLearnerEnrollmentSort);

							context.put("sortDirection", 1);
							context.put("sortColumnIndex", 2);
						}
					}else if( sortColumnIndex.equalsIgnoreCase("1") ) // for last name sort 
					{
						if( sortDirection.equalsIgnoreCase("0") ) {
							// list of in-active members 
							AdminLearnerEnrollmentSort adminLearnerEnrollmentSort=new AdminLearnerEnrollmentSort();
							adminLearnerEnrollmentSort.setSortBy("lastName");

							adminLearnerEnrollmentSort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(adminLearnerEnrollmentSearchList,adminLearnerEnrollmentSort);
							context.put("sortDirection", 0);
							context.put("sortColumnIndex", 1);
						} else {
							// list of active members 
							AdminLearnerEnrollmentSort adminLearnerEnrollmentSort=new AdminLearnerEnrollmentSort();
							adminLearnerEnrollmentSort.setSortBy("lastName");

							adminLearnerEnrollmentSort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(adminLearnerEnrollmentSearchList,adminLearnerEnrollmentSort);

							context.put("sortDirection", 1);
							context.put("sortColumnIndex", 1);
						}
					}
				}					
				enrollForm.setAdminSearchMemberList(adminLearnerEnrollmentSearchList);		
			}
			
		} catch (Exception e) {
			log.error("exception", e);
			context.put("exception", "Exception");
			return new ModelAndView(viewEnrollmentForCertEnableDisableTemplate, "context", context);
		}
		return new ModelAndView(viewEnrollmentForCertEnableDisableTemplate, "context", context);
	}

	
	public ModelAndView searchEnrollment( HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			Map<Object, Object> context = new HashMap<Object, Object>();
			return new ModelAndView(viewEnrollmentForCertEnableDisableTemplate,"context",context);
		}catch(Exception e){
			log.debug(e);
		}
		return new ModelAndView(viewEnrollmentForCertEnableDisableTemplate);
	}
	
	public ModelAndView showEnrollments(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {

		ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
		if(errors.hasErrors()){
			return new ModelAndView(viewEnrollmentForCertEnableDisableTemplate);
		}
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			String userId = request.getParameter("id");
			VU360User user = vu360UserService.getUserById(Long.parseLong(userId));
			enrollForm.setUserName(user.getName());
			context.put("userName",user.getFirstName() + " "+ user.getLastName());
			return new ModelAndView(viewEnrollmentDetailForCertEnableDisableTemplate, "context", context);

		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(viewEnrollmentDetailForCertEnableDisableTemplate);
	}

	public String getViewEnrollmentForCertEnableDisableTemplate() {
		return viewEnrollmentForCertEnableDisableTemplate;
	}

	public void setViewEnrollmentForCertEnableDisableTemplate(
			String viewEnrollmentForCertEnableDisableTemplate) {
		this.viewEnrollmentForCertEnableDisableTemplate = viewEnrollmentForCertEnableDisableTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getViewEnrollmentDetailForCertEnableDisableTemplate() {
		return viewEnrollmentDetailForCertEnableDisableTemplate;
	}

	public void setViewEnrollmentDetailForCertEnableDisableTemplate(
			String viewEnrollmentDetailForCertEnableDisableTemplate) {
		this.viewEnrollmentDetailForCertEnableDisableTemplate = viewEnrollmentDetailForCertEnableDisableTemplate;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}
	
}
