package com.softech.vu360.lms.web.controller.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.AddCustomerEntitlementsForm;
import com.softech.vu360.lms.web.controller.validator.EditCustomerEntitlementValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.TreeNode;

/**
 * @author arijit dutta
 * modified by Dyutiman on 26-aug-09
 * 
 */
public class ViewEntitlementDetailsController extends VU360BaseMultiActionController {
	
	private static final int SUMMARY_TAB = 1;
	private static final int COURSES_TAB = 2;
	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());
	private EntitlementService entitlementService = null;
	private LearnerService learnerService = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private String courseDescriptionAjaxTemplate;
	
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	private String viewEntitlementDetailsTemplate = null;
	private String viewEntitlementCourseGroupsAndCourseTemplate = null;
	private String closeTemplate = null;
	private String viewSubscriptionEntitlementDetailsTemplate=null;
	private String viewSubscriptionEntitlementCoursesTemplate=null;

	public String getViewEntitlementCourseGroupsAndCourseTemplate() {
		return viewEntitlementCourseGroupsAndCourseTemplate;
	}

	public void setViewEntitlementCourseGroupsAndCourseTemplate(
			String viewEntitlementCourseGroupsAndCourseTemplate) {
		this.viewEntitlementCourseGroupsAndCourseTemplate = viewEntitlementCourseGroupsAndCourseTemplate;
	}

	public ViewEntitlementDetailsController() {
		super();
	}

	public ViewEntitlementDetailsController(Object delegate) {
		super(delegate);
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		if( command instanceof AddCustomerEntitlementsForm ) {
			AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;
			if( methodName.equals("viewEntitlementDetails") || methodName.equals("viewEntitlementCourses")) {
				Long entId = Long.parseLong(request.getParameter("Id"));
				CustomerEntitlement custEntitlement = entitlementService.getCustomerEntitlementById(entId);
				form.setCustomerEntitlement(custEntitlement);
				form.setEntId(entId);
			}
			else if ( methodName.equals("updateCustomerEntitlement") ) {
				CustomerEntitlement custEntitlement = null;
				if( form.getCustomerEntitlement() == null ) {
					custEntitlement = entitlementService.getCustomerEntitlementById(form.getEntId());
				} else {
					custEntitlement = form.getCustomerEntitlement();
				}
				form.setCustomerEntitlement(custEntitlement);
			}
			else if ( methodName.equals("viewSubscriptionEntitlementDetails") ) {
					Long entId = Long.parseLong(request.getParameter("Id"));
					form.setEntId(entId);
					CustomerEntitlement custEntitlement = entitlementService.getCustomerEntitlementById(form.getEntId());
					form.setCustomerEntitlement(custEntitlement);
			}
		}
	}
	
	/**
	 * @author muzammil.shaikh
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewEntitlementCourses(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;
		CustomerEntitlement customerEntitlement = form.getCustomerEntitlement();
		Map<String, Object> context = new HashMap<String, Object>();
		List<TreeNode> courseGroupTreeNodeList = new ArrayList<TreeNode>(); 
		Set<CourseGroup> contractCourseGroups = new HashSet<CourseGroup>();
		courseGroupTreeNodeList=entitlementService.getTreeForContract(customerEntitlement, contractCourseGroups);
		
		if(customerEntitlement instanceof CourseGroupCustomerEntitlement) {
			context.put("contractType", "courseGroup");
		}
		else if (customerEntitlement instanceof CourseCustomerEntitlement){
			context.put("contractType", "Course");
		}
		
		context.put("contractCourseGroups", contractCourseGroups);
		context.put("entId", form.getEntId());
		context.put("tab", COURSES_TAB);
		context.put("treeAsList", courseGroupTreeNodeList);
		return new ModelAndView(viewEntitlementCourseGroupsAndCourseTemplate, "context", context);
	}
	
	public ModelAndView viewEntitlementDetails(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		
		CustomerEntitlement custEntitlement = form.getCustomerEntitlement();
		form.setEntitlementName(custEntitlement.getName());
		form.setMaxEnrollments(custEntitlement.isAllowUnlimitedEnrollments());
		form.setNoOfMaxEnrollments(custEntitlement.getMaxNumberSeats()+"");
		form.setAllowSelfEnrollments(custEntitlement.isAllowSelfEnrollment());
		form.setGroups(null);
		form.setEntitlementType(false);
		
		String contractType = "Course";
		if (custEntitlement instanceof CourseGroupCustomerEntitlement) {
			contractType = "courseGroup";
			form.setEntitlementType(true);
		}
		
		form.setStartDate(formatter.format(custEntitlement.getStartDate()));
		if (custEntitlement.getEndDate() != null) {
			form.setTermsOfService(false);
			form.setFiexedEndDate(formatter.format(custEntitlement.getEndDate()));
		} else {
			form.setTermsOfService(true);
			form.setDays(custEntitlement.getDefaultTermOfServiceInDays()+"");
		}
		form.setSeatUsed(custEntitlement.getNumberSeatsUsed()+"");
		if (custEntitlement.isAllowUnlimitedEnrollments())
			form.setSeatRemaining("Unlimited");
		else {
			int remainingseat = custEntitlement.getMaxNumberSeats() - custEntitlement.getNumberSeatsUsed();
			form.setSeatRemaining(remainingseat+"");
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Long customerId = details.getCurrentCustomerId();

		OrganizationalGroup rootOrgGroup =  orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
		
		List<OrgGroupEntitlement> orgGroupEntitlements = entitlementService.getOrgGroupsEntilementsForCustomerEntitlement(custEntitlement);
		TreeNode orgGroupRoot = null;
		List<TreeNode> treeAsList = null;
		
		if(orgGroupEntitlements!=null && orgGroupEntitlements.size()>0) {//If org group entitlement exists then show the limited org groups hierarchy
			Set<OrganizationalGroup> og = new HashSet<>();
			og.add(rootOrgGroup);
			if(orgGroupEntitlements!=null && orgGroupEntitlements.size()>0){
				for(int loop3=0; loop3<orgGroupEntitlements.size(); loop3++){
					OrganizationalGroup orgGroup = orgGroupEntitlements.get(loop3).getOrganizationalGroup();
					
					OrganizationalGroup orgGroupTemp=orgGroup;
					while(orgGroupTemp.getParentOrgGroup()!=null){
						og.add(orgGroupTemp);
						orgGroupTemp = orgGroupTemp.getParentOrgGroup(); 
					}
					og.add(orgGroup);
					map1.put(orgGroup.getId(), new Integer(orgGroupEntitlements.get(loop3).getMaxNumberSeats()));
				}
				orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup, og);
			}
			treeAsList = orgGroupRoot!=null ? orgGroupRoot.bfs() : null;
		}
		else { // For existing data there will not be any mapping in OrgGroupEntitlement so as per requirement we will show all org group hierarchy
			orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup);
			treeAsList = orgGroupRoot.bfs();
		}
		
		context.put("orgGroupTreeAsList", treeAsList);
		context.put("countmap", map1);
		context.put("contractType", contractType);
		context.put("tab", SUMMARY_TAB);
		context.put("entId", form.getEntId());
		
		return new ModelAndView(viewEntitlementDetailsTemplate, "context", context);
	}

	public ModelAndView updateCustomerEntitlement(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {

		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;
		CustomerEntitlement customerEntitlement = form.getCustomerEntitlement();
		customerEntitlement=entitlementService.loadForUpdateCustomerEntitlement(customerEntitlement.getId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Long customerId = details.getCurrentCustomerId();
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
		TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup);
		List<TreeNode> treeAsList = orgGroupRoot.bfs();

		// all org groups...
		List<OrgGroupEntitlement> orgGroupEntitlements = 
			entitlementService.getOrgGroupsEntilementsForCustomerEntitlement(customerEntitlement);

		if( orgGroupEntitlements != null && !orgGroupEntitlements.isEmpty() ) {
			for(int loop3=0; loop3<orgGroupEntitlements.size(); loop3++){
				OrganizationalGroup orgGroup = orgGroupEntitlements.get(loop3).getOrganizationalGroup();
				map1.put(orgGroup.getId(), new Integer(orgGroupEntitlements.get(loop3).getMaxNumberSeats()));
			}
		}
		context.put("orgGroupTreeAsList", treeAsList);
		context.put("countmap", map1);

		if( errors.hasErrors() ) {
			return new ModelAndView(viewEntitlementDetailsTemplate, "context", context);
		}

		String[] selectedOrgGroupValues = form.getGroups();

		List<OrgGroupEntitlement> modOrgGroupEntitlements = new ArrayList<OrgGroupEntitlement>(); 

		List<Integer> maximumEnroll = new ArrayList <Integer>();
		boolean invalidMaxEnroll = false;
		if( selectedOrgGroupValues != null && selectedOrgGroupValues.length > 0 ) {
			log.debug("CHECKED - "+selectedOrgGroupValues.length);

			// checked org group ents... 
			for(int loop1 = 0 ; loop1 < selectedOrgGroupValues.length ; loop1++ ) {

				int flagOldOrgGroupEntitlement = 0;
				String orgGroupID = selectedOrgGroupValues[loop1];
				OrganizationalGroup orgGroup = null;
				if( StringUtils.isNotBlank(orgGroupID) ) {
					orgGroup = learnerService.getOrganizationalGroupById(Long.valueOf(orgGroupID));
				}
				// Edit existing org group ents, assume keeping no value means maximum value
				if( orgGroupEntitlements != null && !orgGroupEntitlements.isEmpty() ) {

					for( int loop2 = 0 ; loop2 < orgGroupEntitlements.size() ; loop2++ ) {

						OrgGroupEntitlement oge = entitlementService.getOrgGroupEntitlementById(
								orgGroupEntitlements.get(loop2).getId());
						// if checked group is previously saved...
						if( orgGroupEntitlements.get(loop2).getOrganizationalGroup().getId().longValue() == 
							orgGroup.getId().longValue() ) {

							if( request.getParameter(orgGroup.getName()).isEmpty() ){
								if(customerEntitlement.isAllowUnlimitedEnrollments())
									oge.setAllowUnlimitedEnrollments(true);
								else
									oge.setMaxNumberSeats(customerEntitlement.getMaxNumberSeats());
							} else {
								String str = request.getParameter(orgGroup.getName());
								if( str != null ) {
									if( StringUtils.isNumeric(str) ) {
										if (Integer.parseInt(request.getParameter(orgGroup.getName())) < oge.getNumberSeatsUsed()) {
											form.setGroups(null);
											errors.rejectValue("maxEnrollments", "error.custEntitlement.organisationalGroupEntitlementItems.maxEnroll");
											return new ModelAndView(viewEntitlementDetailsTemplate, "context", context);
										} else {
											oge.setMaxNumberSeats(Integer.parseInt(request.getParameter(orgGroup.getName())));
											maximumEnroll.add(oge.getMaxNumberSeats());
										}
									} else {
										invalidMaxEnroll = true;
										break;
									}
								}
							}
							//orgGroupEntitlements.get(loop2).setCustomerEntitlement(customerEntitlement);
							flagOldOrgGroupEntitlement = 1;
							modOrgGroupEntitlements.add(oge);
							log.debug("MODIFIED GROUP - "+oge.getOrganizationalGroup().getName());
						}
					}
				}
				//for new OrganizationalGroupEntitlement 
				if( flagOldOrgGroupEntitlement == 0 ) {

					OrgGroupEntitlement newOrgGroupEntitlement = new OrgGroupEntitlement();

					if(request.getParameter(orgGroup.getName()).isEmpty()){
						newOrgGroupEntitlement.setMaxNumberSeats(0);
					} else {
						if( request.getParameter(orgGroup.getName()) != null ) {
							if( StringUtils.isNumeric(request.getParameter(orgGroup.getName())) ) {
								newOrgGroupEntitlement.setMaxNumberSeats(Integer.parseInt(request.
										getParameter(orgGroup.getName())));
								maximumEnroll.add(newOrgGroupEntitlement.getMaxNumberSeats());
							} else {
								invalidMaxEnroll = true;
								break;
							}
						}
					}
					newOrgGroupEntitlement.setOrganizationalGroup(orgGroup);
					if(customerEntitlement.isAllowSelfEnrollment())
						newOrgGroupEntitlement.setAllowSelfEnrollment(true);
					else
						newOrgGroupEntitlement.setAllowSelfEnrollment(false);
					newOrgGroupEntitlement.setCustomerEntitlement(customerEntitlement);
					//orgGroupEntitlements.add(newOrgGroupEntitlement);
					modOrgGroupEntitlements.add(newOrgGroupEntitlement);
					log.debug("ADDED GROUP - "+newOrgGroupEntitlement.getOrganizationalGroup().getName());
				}
			}
		}
		int maxEnrollment = 0;
		if( !maximumEnroll.isEmpty() ) {
			maxEnrollment = Collections.max(maximumEnroll);
		}
		if( invalidMaxEnroll ) {
			form.setGroups(null);
			errors.rejectValue("maxEnrollments", "error.editCustEntitlement.invalidEnroll");
			return new ModelAndView(viewEntitlementDetailsTemplate, "context", context);
		}
		if( !form.isMaxEnrollments() && maxEnrollment > Integer.parseInt(form.getNoOfMaxEnrollments()) ) {
			form.setGroups(null);
			errors.rejectValue("maxEnrollments", "error.editCustEntitlement.enrollmentsExceeded");
			return new ModelAndView(viewEntitlementDetailsTemplate, "context", context);
		}
		if( !form.isMaxEnrollments() && customerEntitlement.getNumberSeatsUsed() > 
			Integer.parseInt(form.getNoOfMaxEnrollments()) ) {
			form.setGroups(null);
			errors.rejectValue("maxEnrollments", "error.editCustEntitlement.seatsExceeded");
			return new ModelAndView(viewEntitlementDetailsTemplate, "context", context);
		}
		//editing or setting of customerEntitlement object
		customerEntitlement.setAllowSelfEnrollment(form.isAllowSelfEnrollments());
		
		/* these parts are not edited here ---
		 *  
		customerEntitlement.setName(form.getEntitlementName());
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if (!form.getStartDate().isEmpty()){
			customerEntitlement.setStartDate(formatter.parse(form.getStartDate()));
		}else{
			customerEntitlement.setStartDate(null);
		}	
		if(form.isTermsOfService()){
			customerEntitlement.setEndDate(null);
			customerEntitlement.setDefaultTermOfServiceInDays(Integer.parseInt(form.getDays()));
		}else {
			customerEntitlement.setEndDate(formatter.parse(form.getFiexedEndDate()));
			customerEntitlement.setDefaultTermOfServiceInDays(0);
		}
		if(form.isMaxEnrollments()){
			customerEntitlement.setAllowUnlimitedEnrollments(form.isMaxEnrollments());
			customerEntitlement.setMaxNumberSeats(0);
		} else {
			customerEntitlement.setAllowUnlimitedEnrollments(form.isMaxEnrollments());
			customerEntitlement.setMaxNumberSeats(Integer.parseInt(form.getNoOfMaxEnrollments()));
		}*/
		customerEntitlement.setCustomer(customerEntitlement.getCustomer());//it is not shown in the form
		log.debug("ORG GROUM NUMBER - "+modOrgGroupEntitlements.size());

		// getting the OrgGroupEntitlement which are de-selected
		if( selectedOrgGroupValues != null && selectedOrgGroupValues.length > 0 ) {
			for(int loop1 = 0 ; loop1 < selectedOrgGroupValues.length ; loop1++ ) {
				String orgGroupID = selectedOrgGroupValues[loop1];
				OrganizationalGroup orgGroup = null;
				if( StringUtils.isNotBlank(orgGroupID ) ) {
					orgGroup = learnerService.getOrganizationalGroupById(Long.valueOf(orgGroupID));
				}
				if( orgGroupEntitlements != null && !orgGroupEntitlements.isEmpty() ) {
					for( int loop2 = 0 ; loop2 < orgGroupEntitlements.size() ; loop2++ ) {

						// if checked group is previously saved...
						if( orgGroupEntitlements.get(loop2).getOrganizationalGroup().getId().longValue() == 
							orgGroup.getId().longValue() ) {
							orgGroupEntitlements.remove(loop2);
						}
					}
				}	
			}
		}

		for( int i = 0 ; i < orgGroupEntitlements.size() ; i ++ ) {
			log.debug("DELETED GROUP - "+orgGroupEntitlements.get(i).getOrganizationalGroup().getName());
		}
		for( int i = 0 ; i < orgGroupEntitlements.size() ; i ++ ) {
			if(orgGroupEntitlements.get(i).getNumberSeatsUsed()>0) {
				form.setGroups(null);
				errors.rejectValue("seatUsed", "error.editCustEntitlement.enrollmentsUsed");
				return new ModelAndView(viewEntitlementDetailsTemplate, "context", context);
			}
		}
		entitlementService.deleteOrgGroupEntitlementInCustomerEntitlement(orgGroupEntitlements);
		entitlementService.saveCustomerEntitlement(customerEntitlement, modOrgGroupEntitlements);

		return new ModelAndView(closeTemplate);
	}

	public ModelAndView cancelCustomerEntitlement(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception {
		return new ModelAndView(closeTemplate);
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {

		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command; 
		EditCustomerEntitlementValidator validator = (EditCustomerEntitlementValidator)this.getValidator();

		if( methodName.equals("updateCustomerEntitlement")) {
			validator.validateDetailsPage(form, errors);
			validator.validateOrgGrpEntitlementPage(form, errors);
		}
	}
	
	/**
	 * method required to create the Organizational group tree
	 */
	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup ) {

		if( orgGroup != null ) {

			TreeNode node = new TreeNode(orgGroup);
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for( int i=0; i<childGroups.size(); i++ ){
				node = getOrgGroupTree(node, childGroups.get(i));
			}
			if( parentNode != null ) {
				parentNode.addChild(node);
				return parentNode;
			} else
				return node;
		}
		return null;
	}
	
	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup, Set<OrganizationalGroup> sOG ) {

		if( orgGroup != null && sOG.contains(orgGroup)) {

			TreeNode node = new TreeNode(orgGroup);
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for( int i=0; i<childGroups.size(); i++ ){
				OrganizationalGroup childOrgGroup = childGroups.get(i);
				if(sOG.contains(childOrgGroup))
				node = getOrgGroupTree(node, childOrgGroup, sOG);
			}
			if( parentNode != null ) {
				parentNode.addChild(node);
				return parentNode;
			} else
				return node;
		}
		return null;
	}
	
	public ModelAndView viewSubscriptionEntitlementDetails(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		
		CustomerEntitlement custEntitlement = form.getCustomerEntitlement();
		form.setEntitlementName(custEntitlement.getName());
		form.setMaxEnrollments(custEntitlement.isAllowUnlimitedEnrollments());
		form.setNoOfMaxEnrollments(custEntitlement.getMaxNumberSeats()+"");
		form.setAllowSelfEnrollments(custEntitlement.isAllowSelfEnrollment());
		form.setGroups(null);
		form.setEntitlementType(false);
		
		String contractType = "Subscription";
		
		form.setStartDate(formatter.format(custEntitlement.getStartDate()));
		if (custEntitlement.getEndDate() != null) {
			form.setTermsOfService(false);
			form.setFiexedEndDate(formatter.format(custEntitlement.getEndDate()));
		} else {
			form.setTermsOfService(true);
			form.setDays(custEntitlement.getDefaultTermOfServiceInDays()+"");
		}
		form.setSeatUsed(custEntitlement.getNumberSeatsUsed()+"");
		if (custEntitlement.isAllowUnlimitedEnrollments())
			form.setSeatRemaining("Unlimited");
		else {
			int remainingseat = custEntitlement.getMaxNumberSeats() - custEntitlement.getNumberSeatsUsed();
			form.setSeatRemaining(remainingseat+"");
		}

		context.put("contractType", contractType);
		context.put("tab", SUMMARY_TAB);
		context.put("entId", form.getEntId());
		
		return new ModelAndView(viewSubscriptionEntitlementDetailsTemplate, "context", context);
	}
	
	public ModelAndView viewSubscriptionEntitlementCourses(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;
		CustomerEntitlement customerEntitlement = form.getCustomerEntitlement();
		Map<String, Object> context = new HashMap<String, Object>();
		List<TreeNode> courseGroupTreeNodeList = new ArrayList<TreeNode>(); 
		Set<CourseGroup> contractCourseGroups = new HashSet<CourseGroup>();
		courseGroupTreeNodeList=entitlementService.getTreeForContract(customerEntitlement, contractCourseGroups);
		
		context.put("contractType", "Subscription");
		context.put("contractCourseGroups", contractCourseGroups);
		context.put("entId", form.getEntId());
		context.put("tab", COURSES_TAB);
		context.put("treeAsList", courseGroupTreeNodeList);
		return new ModelAndView(viewSubscriptionEntitlementCoursesTemplate, "context", context);
	}

	
	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}
	
	/*
	 *  getter & setters
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public String getViewEntitlementDetailsTemplate() {
		return viewEntitlementDetailsTemplate;
	}

	public void setViewEntitlementDetailsTemplate( String viewEntitlementDetailsTemplate ) {
		this.viewEntitlementDetailsTemplate = viewEntitlementDetailsTemplate;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}
	
	public ModelAndView getCourseDescription(HttpServletRequest request, HttpServletResponse response){   	
	   Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(request.getParameter("courseId")));
	   
   	   return new ModelAndView(""); 
    }

	public String getCourseDescriptionAjaxTemplate() {
		return courseDescriptionAjaxTemplate;
	}

	public void setCourseDescriptionAjaxTemplate(
			String courseDescriptionAjaxTemplate) {
		this.courseDescriptionAjaxTemplate = courseDescriptionAjaxTemplate;
	}

	public String getViewSubscriptionEntitlementDetailsTemplate() {
		return viewSubscriptionEntitlementDetailsTemplate;
	}

	public void setViewSubscriptionEntitlementDetailsTemplate(String viewSubscriptionEntitlementDetailsTemplate) {
		this.viewSubscriptionEntitlementDetailsTemplate = viewSubscriptionEntitlementDetailsTemplate;
	}

	public String getViewSubscriptionEntitlementCoursesTemplate() {
		return viewSubscriptionEntitlementCoursesTemplate;
	}

	public void setViewSubscriptionEntitlementCoursesTemplate(
			String viewSubscriptionEntitlementCoursesTemplate) {
		this.viewSubscriptionEntitlementCoursesTemplate = viewSubscriptionEntitlementCoursesTemplate;
	}
}