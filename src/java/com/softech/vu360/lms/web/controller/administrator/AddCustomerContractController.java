package com.softech.vu360.lms.web.controller.administrator;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.impl.VU360UserServiceImpl;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.AddCustomerContractForm;
import com.softech.vu360.lms.web.controller.model.OrganisationalGroupEntitlementItem;
import com.softech.vu360.lms.web.controller.validator.AddCustomerContractValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.TreeNode;

@SuppressWarnings("all")
public class AddCustomerContractController extends VU360BaseMultiActionController 
{
	private static final Logger log = Logger.getLogger(VU360UserServiceImpl.class.getName());
	private EntitlementService entitlementService = null;
	private VelocityEngine velocityEngine;
	private OrgGroupLearnerGroupService groupService = null;
	private LearnerService learnerService = null;
	private String TARGET_CANCEL="cancelAddCustomerContract";
	 private String SAVE_METHOD = "saveCustomerContract";
    private String successView;
    private String formView;
    private static final String SHOW_ADD_FORM = "showForm"; 
    private static final String SHOW_EDIT_FORM = "editForm"; 
    private static final String VIEW_ENT_DETAILS ="viewCustEntitlementDetails";
    private String closeTemplate = null;
    private String viewCustomerEntitlementTemplate = null;
    private OrgGroupLearnerGroupService orgGroupLearnerGroupService = null;
    
    
    
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public String getViewCustomerEntitlementTemplate() {
		return viewCustomerEntitlementTemplate;
	}

	public void setViewCustomerEntitlementTemplate(
			String viewCustomerEntitlementTemplate) {
		this.viewCustomerEntitlementTemplate = viewCustomerEntitlementTemplate;
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public String getFormView() {
		return formView;
	}

	public void setFormView(String formView) {
		this.formView = formView;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public OrgGroupLearnerGroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(OrgGroupLearnerGroupService groupService) {
		this.groupService = groupService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public Object formBackingObject(HttpServletRequest request) throws ServletException
	{ 
		log.debug("formBackingObject method of AddSummaryController class " );
		AddCustomerContractForm form =new  AddCustomerContractForm();
		return form; 
	}
	
	
	public AddCustomerContractController() {
		super();
	}

	public AddCustomerContractController(Object delegate) {
		super(delegate);
	}
	
	private TreeNode getOrgGroupTree(TreeNode parentNode, OrganizationalGroup orgGroup, List<OrganisationalGroupEntitlementItem> selectedOrgGroup){
		if(orgGroup!=null){
			OrganisationalGroupEntitlementItem entitlementItem = new OrganisationalGroupEntitlementItem(orgGroup.getId(),orgGroup.getName());
			TreeNode node = new TreeNode(entitlementItem);
			node.setEnabled(true);

			for(OrganisationalGroupEntitlementItem selectedOrgGrp : selectedOrgGroup){
				if(orgGroup.getId().longValue() == selectedOrgGrp.getOrganisationalGroupId().longValue()){
					entitlementItem.setMaxEnrollments(selectedOrgGrp.getMaxEnrollments());
					entitlementItem.setSelected(true);
					node.setSelected(true);
					break;
				}
			}

			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i),selectedOrgGroup);
			}
			node.setEnabled(true);

			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}
	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup){

		if( orgGroup != null ) {

			TreeNode node = new TreeNode(orgGroup);
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for( int i=0; i<childGroups.size(); i++ ){
				node = getOrgGroupTree(node, childGroups.get(i));
			}

			if( parentNode != null ) {
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		
		if(methodName.equals(SHOW_ADD_FORM) ){
			Map model = new HashMap();
			AddCustomerContractForm form = (AddCustomerContractForm)command;
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			Long customerId = details.getCurrentCustomerId();
			form.setContractName("");
			form.setMaximumEnrollmentsLimitedValue("");
			form.setStartDate("");
			form.setTermsOfServicesValue("");
			form.setFixedEndDate("");
			form.setAllowSelfEnrollment(true);
			form.setEnrollmentType("CourseGroup");
			form.setMaximumEnrollmentsUnLimited(true);
			form.setTermsOfServices(true);
			form.setCourseIds("");
			form.setTransactionAmount("0.0");

			OrganizationalGroup rootOrgGroup = null;
			rootOrgGroup =  groupService.getRootOrgGroupForCustomer(customerId);
			
			//Creating OrgGroups list
			List<OrganisationalGroupEntitlementItem> orgGroup = form.getOrganisationalGroupEntitlementItems();
			List<OrganisationalGroupEntitlementItem> selectedOrgGroupList = new ArrayList<OrganisationalGroupEntitlementItem>();
			TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup,selectedOrgGroupList);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();

			form.setTreeAsList(treeAsList);
		
			List<OrganizationalGroup> organizationalGroups=	groupService.getAllOrganizationalGroups(customerId);
			
			List<OrganisationalGroupEntitlementItem> organisationalGroupEntitlementItems = new ArrayList<OrganisationalGroupEntitlementItem>();
			for(OrganizationalGroup organizationalGroup:organizationalGroups) {
				OrganisationalGroupEntitlementItem organisationalGroupEntitlementItem=new OrganisationalGroupEntitlementItem();
				organisationalGroupEntitlementItem.setMaxEnrollments("0");
				organisationalGroupEntitlementItem.setOrganisationalGroupId(organizationalGroup.getId());
				organisationalGroupEntitlementItem.setOrganisationalGroupName(organizationalGroup.getName());
				organisationalGroupEntitlementItem.setSelected(false);
				organisationalGroupEntitlementItems.add(organisationalGroupEntitlementItem);
			}
			
			form.setOrganisationalGroupEntitlementItems(organisationalGroupEntitlementItems);
		}
			
		
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		
		if(methodName.equals(SAVE_METHOD)){
			AddCustomerContractForm form = (AddCustomerContractForm)command; 
			AddCustomerContractValidator validator = (AddCustomerContractValidator)this.getValidator();
			
			validator.validate(form, errors);
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			Customer customer = details.getCurrentCustomer();
			List<CustomerEntitlement> entitlements = entitlementService.getActiveCustomerEntitlementsForCustomer(customer);
			
			for(CustomerEntitlement ent : entitlements){
				if(ent.getName().equalsIgnoreCase(form.getContractName())){
					errors.rejectValue("duplicateContractName", "error.addCustomerContract.duplicatecontractname");
					break;
				}
			}
			
			long maxEnrollment=0;
			List<OrganisationalGroupEntitlementItem> orgGroupEntitlementItem = form.getOrganisationalGroupEntitlementItems();
			
			for (OrganisationalGroupEntitlementItem orgGroupEnt : orgGroupEntitlementItem) {
				if (orgGroupEnt.isSelected()) { // not clear about this selection
					if(StringUtils.isBlank(orgGroupEnt.getMaxEnrollments()) || !StringUtils.isNumeric(orgGroupEnt.getMaxEnrollments())){						
						errors.rejectValue("maximumEnrollmentsLimitedValue", "error.editCustEntitlement.invalidNumber");
						break;
					} else if(orgGroupEnt.getMaxEnrollments().length() > 8) {
						errors.rejectValue("maximumEnrollmentsLimitedValue", "error.editCustEntitlement.maxOutOfLimit");
						break;
					}
					maxEnrollment+= Long.valueOf(orgGroupEnt.getMaxEnrollments());//Integer.parseInt(orgGroupEnt.getMaxEnrollments());
				}
			}
			if( !form.isMaximumEnrollmentsUnLimited() && (StringUtils.isBlank(form.getMaximumEnrollmentsLimitedValue()) || !StringUtils.isNumeric(form.getMaximumEnrollmentsLimitedValue()))) {
				errors.rejectValue("maximumEnrollmentsLimitedValue", "error.editCustEntitlement.invalidMaxEnrollmentNumber");				
		
			}else if( !form.isMaximumEnrollmentsUnLimited() && maxEnrollment > Integer.parseInt(form.getMaximumEnrollmentsLimitedValue()) ) {
				errors.rejectValue("maximumEnrollmentsLimitedValue", "error.editCustEntitlement.enrollmentsExceeded");
		
			}			
						
			if(this.isExpirationDateValid(form,customer.getDistributor()) == false){
				errors.rejectValue("fixedEndDate", "error.editCustEntitlement.maxEndDateExceeded");
			}
			
			String amt = form.getTransactionAmount();
			try {
				if ( StringUtils.isNotEmpty(amt) ){
					if(StringUtils.isNumeric(Math.round(Double.parseDouble(amt))+"")){
						//Max DB length: 18 digit including 2 precision
						Double val = Double.parseDouble(form.getTransactionAmount());
						if ( String.valueOf(val.longValue()).length() > 16) {
							errors.rejectValue("transactionAmount","error.customerentitlement.invalidTransactionAmount");
						}
					}
				}else{
					form.setTransactionAmount("0.0");
				} 
			} catch (Exception e) {
				log.debug("exception", e);
			}

		}
						
	}
    
	private boolean isExpirationDateValid(AddCustomerContractForm form,Distributor distributor){
		/*get max distributor end date*/		
		Date maxEndDate= entitlementService.getMaxDistributorEntitlementEndDate(distributor);
		/*get customer contract end date*/
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date expirationDate= null;
		Date startDate= null;
		int days=0;
		try{expirationDate= formatter.parse(form.getFixedEndDate());}	catch (ParseException e) {	expirationDate= null;}
		try{ startDate= formatter.parse(form.getStartDate());	} catch (ParseException e) {	startDate= null;} 
		try{days= Integer.parseInt(form.getTermsOfServicesValue());	}catch(Exception e) {days=0;	} 
		
		if(startDate!=null && expirationDate == null){			
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.DATE, days);
			expirationDate = cal.getTime();
		}
		//compare
		if(expirationDate !=null && expirationDate.after(maxEndDate)){
			return false;
		}
		
		return true;
    }
	public ModelAndView showForm( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{ 
		
		return new ModelAndView(formView);
	}
	//merged from LMS 4.7.2 branch
	public void cancelAddCustomerContract( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		response.sendRedirect("adm_SearchEntitlements.do");
//		Map context = new HashMap();
//		context.put("target", "adm_SearchEntitlements.do");
//		return new ModelAndView(successView,"context",context);
	}
	
	public ModelAndView saveCustomerContract( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		 		
		AddCustomerContractForm form = (AddCustomerContractForm)command;
		CustomerEntitlement customerEntitlement ;//= new CustomerEntitlement();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Customer customer = details.getCurrentCustomer();
	    	//save logic
		if(!errors.hasErrors())
		{
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			
			if(form.getEnrollmentType().equals(AddCustomerContractForm.COURSEGROUPTYPE))
				customerEntitlement=new CourseGroupCustomerEntitlement();
			else
				customerEntitlement=new CourseCustomerEntitlement();
			
			customerEntitlement.setName(form.getContractName());

			List<OrgGroupEntitlement> orgGroupEntitlementList = new ArrayList<OrgGroupEntitlement>();
			List<OrganisationalGroupEntitlementItem> OrgGroupEntitlementItem = form.getOrganisationalGroupEntitlementItems();
			customerEntitlement.setCustomer(customer);
			try
			{
				customerEntitlement.setStartDate(formatter.parse(form.getStartDate()));
			}
			catch (ParseException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (form.isTermsOfServices())
				customerEntitlement.setDefaultTermOfServiceInDays(Integer.parseInt(form.getTermsOfServicesValue()));
			else
				try
			{
				customerEntitlement.setEndDate(FormUtil.formatToDayEnd(formatter.parse(form.getFixedEndDate())));
			}
			catch (ParseException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (!form.isMaximumEnrollmentsUnLimited()) {
				customerEntitlement.setMaxNumberSeats(Integer.parseInt(form.getMaximumEnrollmentsLimitedValue()));
			} else {
				customerEntitlement.setAllowUnlimitedEnrollments(true);
			}
			if (form.isAllowSelfEnrollment())
				customerEntitlement.setAllowSelfEnrollment(true);

			boolean isCreatedForSpecificOrgGroup = false;
 			for (OrganisationalGroupEntitlementItem OrgGroupEnt : OrgGroupEntitlementItem) {
				if( !StringUtils.isBlank(OrgGroupEnt.getMaxEnrollments()) ){
					isCreatedForSpecificOrgGroup = true;
					break;
				}
			}

 			Double amt = Double.parseDouble(form.getTransactionAmount());
 			customerEntitlement.setTransactionAmount(amt);			
			
			for (OrganisationalGroupEntitlementItem OrgGroupEnt : OrgGroupEntitlementItem) {
				
				if (OrgGroupEnt.isSelected()) { // not clear about this selection
					OrgGroupEntitlement orgGroupEntitlement = new OrgGroupEntitlement();
					OrganizationalGroup orgGroup=new OrganizationalGroup();
					orgGroup.setId(OrgGroupEnt.getOrganisationalGroupId());
					orgGroupEntitlement.setOrganizationalGroup(orgGroup);
					if ( !StringUtils.isBlank(OrgGroupEnt.getMaxEnrollments()) ){
						orgGroupEntitlement.setMaxNumberSeats(Integer.parseInt(OrgGroupEnt.getMaxEnrollments()));
					}else{
						if(isCreatedForSpecificOrgGroup){
							orgGroupEntitlement.setAllowUnlimitedEnrollments(false);
							orgGroupEntitlement.setMaxNumberSeats(0);
						}else{
							
							if(customerEntitlement.isAllowUnlimitedEnrollments()){
								orgGroupEntitlement.setAllowUnlimitedEnrollments(true);
							}else{
								orgGroupEntitlement.setAllowUnlimitedEnrollments(false);
								orgGroupEntitlement.setMaxNumberSeats(customerEntitlement.getMaxNumberSeats());
							}
						}
					}
					orgGroupEntitlement.setCustomerEntitlement(customerEntitlement);
					try {
						if (form.isTermsOfServices())
							orgGroupEntitlement.setDefaultTermOfServiceInDays(Integer.parseInt(form.getTermsOfServicesValue()));
					}
					catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					try {
						if (form.isAllowSelfEnrollment())
							orgGroupEntitlement.setAllowSelfEnrollment(true);
						orgGroupEntitlement.setStartDate(formatter.parse(form.getStartDate()));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						if (!form.isTermsOfServices())
							orgGroupEntitlement.setEndDate(formatter.parse(form.getFixedEndDate()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*if (form.isMaxEnrollments())
						orgGroupEntitlement.setAllowUnlimitedEnrollments(true);*/
					if (form.isAllowSelfEnrollment())
						orgGroupEntitlement.setAllowSelfEnrollment(true);

					orgGroupEntitlementList.add(orgGroupEntitlement);
				}
			}
			
			customerEntitlement.setContractCreationDate(new Date());
			CustomerEntitlement updatedEntitlement = entitlementService.saveCustomerEntitlement(customerEntitlement,orgGroupEntitlementList);
			
			//Add course/ course groups in it if user has entered businessKeys
			if(form.getCourseIds().trim().length()>0)
				addItemsInContract(form,updatedEntitlement,customer);
			
			return new ModelAndView(successView);	
		}
			return new ModelAndView(formView);		 
		}
	private void addItemsInContract(AddCustomerContractForm form,
			CustomerEntitlement updatedEntitlement, Customer customer) {
		if(updatedEntitlement instanceof CourseGroupCustomerEntitlement){
			CourseGroupCustomerEntitlement ent=(CourseGroupCustomerEntitlement)updatedEntitlement;

			String []strCourseGroupIds = form.getCourseIds().split(",");
			
			List<CourseGroup> courseGroups=new ArrayList<CourseGroup>();
			HashMap<CourseGroup, List<Course>> result = entitlementService.findAvailableCourseGroups(customer.getDistributor(), "",  form.getCourseIds(), "");

			for (CourseGroup courseGroup : result.keySet()){
				courseGroups.add(courseGroup);
				courseGroups.addAll(courseGroup.getAllChildrenInHierarchy());
			}
			
			entitlementService.addEntitlementItems(ent, courseGroups);
			 
		}
		else{
			CourseCustomerEntitlement ent=(CourseCustomerEntitlement)updatedEntitlement;

			HashMap<CourseGroup, List<Course>> result = entitlementService.findAvailableCourses(customer.getDistributor(), "",  form.getCourseIds(), "");
				for (CourseGroup courseGroup : result.keySet()){
					List<Course> courseList = result.get(courseGroup);
					for (Course course : courseList) {
						entitlementService.addEntitlementItem(ent,course, courseGroup);
					}
				}
			
		}
		
	}

	public ModelAndView cancelCustomerEntitlement(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception {
		return new ModelAndView(closeTemplate);
	}
}
