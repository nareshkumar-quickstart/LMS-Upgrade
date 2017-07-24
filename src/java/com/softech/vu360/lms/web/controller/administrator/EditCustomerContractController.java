package com.softech.vu360.lms.web.controller.administrator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

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
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.AddCustomerContractForm;
import com.softech.vu360.lms.web.controller.model.OrganisationalGroupEntitlementItem;
import com.softech.vu360.lms.web.controller.validator.AddCustomerContractValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeCourseGroupTree;
import com.softech.vu360.util.CourseSort;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.TreeNode;

public class EditCustomerContractController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(EditCustomerContractController.class.getName());
	private EntitlementService entitlementService;
	private VelocityEngine velocityEngine;
	private OrgGroupLearnerGroupService groupService;
	private LearnerService learnerService;
	private String successView;
	private String formView;
	private static final String UPDATE_ENT_DETAILS ="updateCustomerEntitlement";
	private String closeTemplate;
	private String viewCustomerEntitlementTemplate;
	private String viewSubscriptionCustomerEntitlementTemplate;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	CourseAndCourseGroupService courseAndCourseGroupService;
	private EnrollmentService enrollmentService;
	private String addCourseTemplate;
	private String addCourseGroupTemplate =null;
	private String showContractItemTemplate;
	private String showSubscriptionContractItemTemplate;


	public String getShowContractItemTemplate() {
		return showContractItemTemplate;
	}

	public void setShowContractItemTemplate(String showContractItemTemplate) {
		this.showContractItemTemplate = showContractItemTemplate;
	}

	public Object formBackingObject(HttpServletRequest request) throws ServletException
	{ 
		log.debug("formBackingObject method of AddSummaryController class " );
		AddCustomerContractForm form=new AddCustomerContractForm();
		return form; 
	}

	public EditCustomerContractController() {
		super();
	}

	public EditCustomerContractController(Object delegate) {
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

		if(command instanceof AddCustomerContractForm){
			AddCustomerContractForm form = (AddCustomerContractForm)command;
			if(methodName.equals("viewCustEntitlementDetails")){
				CustomerEntitlement custEntitlement = entitlementService.loadForUpdateCustomerEntitlement(Long.parseLong(request.getParameter("contractId")));

				form.setCustomerEntitlement(custEntitlement);
				form.setEnrollmentType(custEntitlement.getEnrollmentType());
				form.setAllowSelfEnrollment(custEntitlement.isAllowSelfEnrollment());
				form.setContractName(custEntitlement.getName());
				form.setMaximumEnrollmentsLimitedValue(custEntitlement.getMaxNumberSeats()+"");
				form.setEntId(custEntitlement.getId());
				form.setMaximumEnrollmentsUnLimited(custEntitlement.isAllowUnlimitedEnrollments());
				form.setTermsOfServicesValue(custEntitlement.getDefaultTermOfServiceInDays()+"");
				form.setTransactionAmount(String.valueOf(custEntitlement.getTransactionAmount()));

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				Customer customer = details.getCurrentCustomer();
				OrganizationalGroup rootOrgGroup = null;
				rootOrgGroup =  groupService.getRootOrgGroupForCustomer(customer.getId());
				//Creating OrgGroups list
				List<OrganisationalGroupEntitlementItem> orgGroup = form.getOrganisationalGroupEntitlementItems();
				List<OrganisationalGroupEntitlementItem> selectedOrgGroupList = new ArrayList<OrganisationalGroupEntitlementItem>();

				if (orgGroup != null) {
					for (OrganisationalGroupEntitlementItem entitlementItem : (List<OrganisationalGroupEntitlementItem>)form.getOrganisationalGroupEntitlementItems()) {
						if (entitlementItem.isSelected()) {
							OrganizationalGroup organizationalGroup = learnerService.getOrganizationalGroupById(entitlementItem.getOrganisationalGroupId());
							entitlementItem.setOrganisationalGroupName(organizationalGroup.getName());
							selectedOrgGroupList.add(entitlementItem);
						}
					}
				}

				TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup,selectedOrgGroupList);
				List<TreeNode> treeAsList = orgGroupRoot.bfs();

				form.setTreeAsList(treeAsList);
				List<OrganizationalGroup> organizationalGroups=	groupService.getAllOrganizationalGroups(customer.getId());
				form.setOrganizationGroups(organizationalGroups);
				List<OrganisationalGroupEntitlementItem> organisationalGroupEntitlementItems = new ArrayList<OrganisationalGroupEntitlementItem>();
				for(OrganizationalGroup organizationalGroup:organizationalGroups) {
					OrganisationalGroupEntitlementItem organisationalGroupEntitlementItem=new OrganisationalGroupEntitlementItem();
					organisationalGroupEntitlementItem.setMaxEnrollments("");
					organisationalGroupEntitlementItem.setOrganisationalGroupId(organizationalGroup.getId());
					organisationalGroupEntitlementItem.setOrganisationalGroupName(organizationalGroup.getName());
					organisationalGroupEntitlementItem.setSelected(false);
					organisationalGroupEntitlementItems.add(organisationalGroupEntitlementItem);
				}

				form.setOrganisationalGroupEntitlementItems(organisationalGroupEntitlementItems);
			}
			if(methodName.equals("viewSubscriptionCustEntitlementDetails")){

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

				CustomerEntitlement custEntitlement = entitlementService.loadForUpdateCustomerEntitlement(Long.parseLong(request.getParameter("contractId")));
				form.setCustomerEntitlement(custEntitlement);
				form.setEnrollmentType(custEntitlement.getEnrollmentType());
				form.setAllowSelfEnrollment(custEntitlement.isAllowSelfEnrollment());
				form.setContractName(custEntitlement.getName());
				form.setMaximumEnrollmentsLimitedValue(custEntitlement.getMaxNumberSeats()+"");
				form.setEntId(custEntitlement.getId());
				form.setMaximumEnrollmentsUnLimited(custEntitlement.isAllowUnlimitedEnrollments());
				form.setRemainingSeats(custEntitlement.getMaxNumberSeats() - custEntitlement.getNumberSeatsUsed()+"");
				form.setSeatUsed(String.valueOf(custEntitlement.getNumberSeatsUsed()));
				form.setStartDate(formatter.format(custEntitlement.getStartDate()));
			}
		}
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {

		if(methodName.equals(UPDATE_ENT_DETAILS)) 
		{
			AddCustomerContractForm form = (AddCustomerContractForm)command; 
			AddCustomerContractValidator validator = (AddCustomerContractValidator)this.getValidator();
			validator.validate(form, errors);
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			Customer customer = details.getCurrentCustomer();
			List<CustomerEntitlement> entitlements = entitlementService.getActiveCustomerEntitlementsForCustomer(customer);
			for(CustomerEntitlement ent : entitlements){
				if(ent.getName().equalsIgnoreCase(form.getContractName()) && !ent.getId().equals(form.getCustomerEntitlement().getId())){
					errors.rejectValue("duplicateContractName", "error.addCustomerContract.duplicatecontractname");
					break;
				}
			}
			int maxEnrollment=0;
			List<OrganisationalGroupEntitlementItem> orgGroupEntitlementItem = form.getOrganisationalGroupEntitlementItems();
			for (OrganisationalGroupEntitlementItem orgGroupEnt : orgGroupEntitlementItem) {
				if (orgGroupEnt.isSelected()) { // not clear about this selection
					maxEnrollment+= Integer.parseInt(orgGroupEnt.getMaxEnrollments());
					if(maxEnrollment > 8) {
						errors.rejectValue("maximumEnrollmentsLimitedValue", "error.editCustEntitlement.invalidNumber");
						break;
					}
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
		if(expirationDate.after(maxEndDate)){
			return false;
		}
		
		return true;
    }
	
	public ModelAndView showForm( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{ 

		return new ModelAndView(formView);
	}
	public void cancelCustomerContract( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		response.sendRedirect("adm_SearchEntitlements.do");
	}

	public void cancelAddCustomerContract( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		response.sendRedirect("adm_SearchEntitlements.do");
	}

	public ModelAndView viewSubscriptionCustEntitlementDetails( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		
		Map<Object, Object> context = new HashMap<Object, Object>();

		AddCustomerContractForm form = (AddCustomerContractForm)command;
		CustomerEntitlement custEntitlement = form.getCustomerEntitlement();
		form.setContractName(custEntitlement.getName());
		form.setMaximumEnrollmentsUnLimited(custEntitlement.isAllowUnlimitedEnrollments());
		form.setMaximumEnrollmentsLimitedValue(custEntitlement.getMaxNumberSeats()+"");
		form.setAllowSelfEnrollment(custEntitlement.isAllowSelfEnrollment());
		context.put("tab", 1);
		return new ModelAndView(viewSubscriptionCustomerEntitlementTemplate, "context", context);

	}
	
	public ModelAndView viewCustEntitlementDetails( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		AddCustomerContractForm form = (AddCustomerContractForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		CustomerEntitlement custEntitlement = form.getCustomerEntitlement();
		form.setContractName(custEntitlement.getName());
		form.setMaximumEnrollmentsUnLimited(custEntitlement.isAllowUnlimitedEnrollments());
		form.setMaximumEnrollmentsLimitedValue(custEntitlement.getMaxNumberSeats()+"");
		form.setAllowSelfEnrollment(custEntitlement.isAllowSelfEnrollment());

		String entitlementType = custEntitlement.getEnrollmentType();

		form.setEnrollmentType(entitlementType); 

		form.setStartDate(formatter.format(custEntitlement.getStartDate()));
		if (custEntitlement.getEndDate() != null) {
			form.setTermsOfServices(false);
			form.setFixedEndDate(formatter.format(custEntitlement.getEndDate()));
		} else {
			form.setTermsOfServices(true);
			form.setFixedEndDate(custEntitlement.getDefaultTermOfServiceInDays()+"");
		}
		form.setSeatUsed(custEntitlement.getNumberSeatsUsed()+"");
		if (custEntitlement.isAllowUnlimitedEnrollments())
			form.setSeatRemaining("Unlimited");
		else {
			int remainingseat = custEntitlement.getMaxNumberSeats() - custEntitlement.getNumberSeatsUsed();
			form.setSeatRemaining(remainingseat+"");
		}

		Double amt = custEntitlement.getTransactionAmount();
		if(amt != null && StringUtils.isNumeric( Math.round(amt)+"") ){
			BigDecimal bd = new BigDecimal(amt);
			NumberFormat fmt = new DecimalFormat("#0.00");
			form.setTransactionAmount(fmt.format(bd));
		} else {
			form.setTransactionAmount("");
		}
		

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Long customerId = details.getCurrentCustomerId();

		OrganizationalGroup rootOrgGroup =  orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
		TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup);
		List<TreeNode> treeAsList = orgGroupRoot.bfs();
		List<OrgGroupEntitlement> orgGroupEntitlements = entitlementService.
		getOrgGroupsEntilementsForCustomerEntitlement(custEntitlement);
		for(int loop3=0; loop3<orgGroupEntitlements.size(); loop3++){
			OrganizationalGroup orgGroup = orgGroupEntitlements.get(loop3).getOrganizationalGroup();
			map1.put(orgGroup.getId(), new Integer(orgGroupEntitlements.get(loop3).getMaxNumberSeats()));
		}
		context.put("orgGroupTreeAsList", treeAsList);
		context.put("countmap", map1);
		return new ModelAndView(viewCustomerEntitlementTemplate, "context", context);
	}

	public ModelAndView updateCustomerEntitlement(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		System.out.println("updateCustomerEntitlement() called");
		AddCustomerContractForm form = (AddCustomerContractForm)command;

		CustomerEntitlement customerEntitlement = form.getCustomerEntitlement();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Long customerId = details.getCurrentCustomerId();
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
		TreeNode orgGroupRoot =getOrgGroupTree(null, rootOrgGroup);
		List<TreeNode> treeAsList = orgGroupRoot.bfs();

		// all org groups...
		List<OrgGroupEntitlement> orgGroupEntitlements = entitlementService.getOrgGroupsEntilementsForCustomerEntitlement(customerEntitlement);
		
		if( orgGroupEntitlements != null && !orgGroupEntitlements.isEmpty() ) {
			for(int loop3=0; loop3<orgGroupEntitlements.size(); loop3++){
				OrganizationalGroup orgGroup = orgGroupEntitlements.get(loop3).getOrganizationalGroup();
				map1.put(orgGroup.getId(), new Integer(orgGroupEntitlements.get(loop3).getMaxNumberSeats()));
			}
		}
		context.put("orgGroupTreeAsList", treeAsList);
		context.put("countmap", map1);

		if( errors.hasErrors() ) {
			return new ModelAndView(viewCustomerEntitlementTemplate, "context", context);
		}

		String orgGroupEntitlementArray = form.getOrgGroupEntitlementArray();
		StringTokenizer tokens=new StringTokenizer(orgGroupEntitlementArray,",");  
		 
		List<OrgGroupEntitlement> modOrgGroupEntitlements = new ArrayList<OrgGroupEntitlement>(); 

		List<Integer> maximumEnroll = new ArrayList <Integer>();
		boolean invalidMaxEnroll = false;
		if( tokens.countTokens() > 0 ) {

			// checked org group ents... 
			while(tokens.hasMoreElements() ) {

				int flagOldOrgGroupEntitlement = 0;
				String orgGroupID = tokens.nextToken();
				OrganizationalGroup orgGroup = null;
				if( !StringUtils.isEmpty(orgGroupID) ) {
					orgGroup = learnerService.getOrganizationalGroupById(Long.valueOf(orgGroupID));
				}
				// Edit existing org group ents, assume keeping no value means maximum value
				if( orgGroupEntitlements != null && !orgGroupEntitlements.isEmpty() ) {

				for( int loop2 = 0 ; loop2 < orgGroupEntitlements.size() ; loop2++ ) {

						OrgGroupEntitlement oge = entitlementService.loadForUpdateOrgGroupEntitlement(orgGroupEntitlements.get(loop2).getId());
						
						// if checked group is previously saved...
						if( orgGroupEntitlements.get(loop2).getOrganizationalGroup().getId().longValue() == 
							orgGroup.getId().longValue() ) {

							if( StringUtils.isBlank(request.getParameter(orgGroup.getName()))  ){
								if(customerEntitlement.isAllowUnlimitedEnrollments())
									oge.setAllowUnlimitedEnrollments(true);
								else
									oge.setMaxNumberSeats(customerEntitlement.getMaxNumberSeats());
							} else {
								String str = request.getParameter(orgGroup.getName());
								if( StringUtils.isNotBlank(str)) {
									if( StringUtils.isNumeric(str) ) {
										if(str.length() > 8) {
											errors.rejectValue("maximumEnrollmentsLimitedValue", "error.editCustEntitlement.invalidNumber");
											return new ModelAndView(viewCustomerEntitlementTemplate, "context", context);
										}
										// @MariumSaud : LMS-20646 -- Change if clause to return 'true' , to escape this condition everytime
										//if (Integer.parseInt(request.getParameter(orgGroup.getName())) < oge.getNumberSeatsUsed()) {
										if (Integer.parseInt(request.getParameter(orgGroup.getName())) + oge.getNumberSeatsUsed() < oge.getNumberSeatsUsed()) {
											errors.rejectValue("maxEnrollments", "error.custEntitlement.organisationalGroupEntitlementItems.maxEnroll");
											return new ModelAndView(viewCustomerEntitlementTemplate, "context", context);
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
							flagOldOrgGroupEntitlement = 1;
							modOrgGroupEntitlements.add(oge);
							log.debug("MODIFIED GROUP - "+oge.getOrganizationalGroup().getName());
						}
					}
				}
				//for new OrganizationalGroupEntitlement 
				if( flagOldOrgGroupEntitlement == 0 ) {
					OrgGroupEntitlement newOrgGroupEntitlement = new OrgGroupEntitlement();
					// @MariumSaud : LMS-20646 : For new OrgGrp Entitlements MaxEnrollment will always be 0
					String maxNumberSeats=request.getParameter(orgGroup.getName())==null?"0":request.getParameter(orgGroup.getName());
						if( !StringUtils.isEmpty(maxNumberSeats) ) {
							if( StringUtils.isNumeric(maxNumberSeats) ) {
								// @MariumSaud : LMS-20646 : For new OrgGrp Entitlements MaxEnrollment will always be 0
							    //if( StringUtils.isNumeric(request.getParameter(orgGroup.getName())) ) {
								//newOrgGroupEntitlement.setMaxNumberSeats(Integer.parseInt(request.getParameter(orgGroup.getName())));
								newOrgGroupEntitlement.setMaxNumberSeats(Integer.parseInt(maxNumberSeats));
								maximumEnroll.add(newOrgGroupEntitlement.getMaxNumberSeats());
							} else {
								invalidMaxEnroll = true;
								break;
							}
						newOrgGroupEntitlement.setOrganizationalGroup(orgGroup);
						if(customerEntitlement.isAllowSelfEnrollment())
							newOrgGroupEntitlement.setAllowSelfEnrollment(Boolean.TRUE);
						else
							newOrgGroupEntitlement.setAllowSelfEnrollment(Boolean.FALSE);
						newOrgGroupEntitlement.setCustomerEntitlement(customerEntitlement);
						modOrgGroupEntitlements.add(newOrgGroupEntitlement);
						log.debug("ADDED GROUP - "+newOrgGroupEntitlement.getOrganizationalGroup().getName());
					}
				}
			}
		}

		int maxEnrollment = 0;
		if(!maximumEnroll.isEmpty()){
			maxEnrollment = Collections.max(maximumEnroll);
		}
		if( invalidMaxEnroll ) {
			errors.rejectValue("maximumEnrollmentsLimitedValue", "error.editCustEntitlement.invalidEnroll");
			return new ModelAndView(viewCustomerEntitlementTemplate, "context", context);
		}
		if( !form.isMaximumEnrollmentsUnLimited() && maxEnrollment > Integer.parseInt(form.getMaximumEnrollmentsLimitedValue()) ) {
			errors.rejectValue("maximumEnrollmentsLimitedValue", "error.editCustEntitlement.enrollmentsExceeded");
			return new ModelAndView(viewCustomerEntitlementTemplate, "context", context);
		}
		if( !form.isMaximumEnrollmentsUnLimited() && customerEntitlement.getNumberSeatsUsed() > 
		Integer.parseInt(form.getMaximumEnrollmentsLimitedValue()) ) {
			errors.rejectValue("maximumEnrollmentsLimitedValue", "error.editCustEntitlement.seatsExceeded");
			return new ModelAndView(viewCustomerEntitlementTemplate, "context", context);
		}
		//editing or setting of customerEntitlement object
		customerEntitlement.setName(form.getContractName());
		customerEntitlement.setAllowSelfEnrollment(form.isAllowSelfEnrollment());
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if (!form.getStartDate().isEmpty()){
			try {
				customerEntitlement.setStartDate(formatter.parse(form.getStartDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			try {
				customerEntitlement.setStartDate(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		if(form.isTermsOfServices()){
			try {
				customerEntitlement.setEndDate(null);
				customerEntitlement.setDefaultTermOfServiceInDays(Integer.parseInt(form.getTermsOfServicesValue()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			try {
				customerEntitlement.setEndDate(FormUtil.formatToDayEnd(formatter.parse(form.getFixedEndDate())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			customerEntitlement.setDefaultTermOfServiceInDays(0);
		}
		if(form.isMaximumEnrollmentsUnLimited()){
			customerEntitlement.setAllowUnlimitedEnrollments(form.isMaximumEnrollmentsUnLimited());
			customerEntitlement.setMaxNumberSeats(0);
		} else {
			customerEntitlement.setAllowUnlimitedEnrollments(form.isMaximumEnrollmentsUnLimited());
			try {
				customerEntitlement.setMaxNumberSeats(Integer.parseInt(form.getMaximumEnrollmentsLimitedValue()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		customerEntitlement.setCustomer(customerEntitlement.getCustomer());//it is not shown in the form
		
		Double amt = Double.parseDouble(form.getTransactionAmount());
		customerEntitlement.setTransactionAmount(amt);
		
		log.debug("ORG GROUP SIZE = "+modOrgGroupEntitlements.size());

		// getting the OrgGroupEntitlement which are de-selected
		StringTokenizer orgEntitlementsToBeDeleted=new StringTokenizer(orgGroupEntitlementArray,",");  
		if( orgEntitlementsToBeDeleted.countTokens() > 0 ) {
			while(orgEntitlementsToBeDeleted.hasMoreElements() ) {
				String orgGroupID = orgEntitlementsToBeDeleted.nextToken();
				OrganizationalGroup orgGroup = null;
				if( StringUtils.isNotBlank(orgGroupID) ) {
					orgGroup = learnerService.getOrganizationalGroupById(Long.valueOf(orgGroupID));
				}
				if( orgGroupEntitlements != null && !orgGroupEntitlements.isEmpty() ) {
					for( int loop2 = 0 ; loop2 < orgGroupEntitlements.size() ; loop2++ ) {
						// if checked group is previously saved...
						if( orgGroupEntitlements.get(loop2).getOrganizationalGroup().getId().longValue() == orgGroup.getId().longValue() ) {
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
				errors.rejectValue("seatUsed", "error.editCustEntitlement.enrollmentsUsed");
				return new ModelAndView(viewCustomerEntitlementTemplate, "context", context);
			}
		}
		
		if(orgGroupEntitlements!=null && orgGroupEntitlements.size()>0){
			entitlementService.deleteOrgGroupEntitlementInCustomerEntitlement(orgGroupEntitlements);
		}
		entitlementService.saveCustomerEntitlement(customerEntitlement, modOrgGroupEntitlements);
		
		return new ModelAndView(closeTemplate);
	}

	public ModelAndView showCoursesToAdd( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		AddCustomerContractForm form = (AddCustomerContractForm) command;
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("contractType", form.getEnrollmentType());
		context.put("contractId", form.getCustomerEntitlement().getId());
		context.put("pageNo",0);
		context.put("totalRecord", 0);
		context.put("recordShowing", 0);
		return new ModelAndView(addCourseTemplate, "context", context);
	}

	public ModelAndView showCourseGroupsToAdd( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		return new ModelAndView(addCourseGroupTemplate);

	}

	public ModelAndView searchCourses( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{
		return new ModelAndView(addCourseGroupTemplate);
	}

	public ModelAndView searchCourseGroups( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		AddCustomerContractForm form = (AddCustomerContractForm) command;
		String keywords=request.getParameter("keywords");
		String title=request.getParameter("title");
		//entityId can be courseId or coursegroupId which are strings found in the course and coursegroup tables respectively
		String entityId= null;		
		if(request.getParameter("courseId") != null){
			entityId= request.getParameter("courseId");
		}else if(request.getParameter("courseGroupID")!=null){
			entityId= request.getParameter("courseGroupID");
		}
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		Map<String, Object> context = new HashMap<String, Object>();
		List<TreeNode> courseGroupTree = entitlementService.getEntitlementCourseGroupTreeNode(null, title.trim(), entityId, keywords.trim(), form.getEnrollmentType(), context, customer);
                Long[] resellerCourseGroupIDs = entitlementService.getCourseGroupIDArrayForDistributor(
                        customer.getDistributor());
                List<Long> resellerCourseGroupIds = Arrays.asList(resellerCourseGroupIDs);
                context.put("resellerCourseGroupIds", resellerCourseGroupIds);
		context.put("courseGroupTree", courseGroupTree);
		if (courseGroupTree == null) {
			String[] error = {"error.admin.customerEnt.course.errorMsg1"
					, "error.admin.customerEnt.course.errorMsg2"
					, "error.admin.customerEnt.course.errorMsg3"};
			context.put("error", error);
		}
		context.put("contractType", form.getEnrollmentType());
		context.put("contractId", form.getCustomerEntitlement().getId());
		context.put("pageNo",0);
		context.put("totalRecord", courseGroupTree.size());
		context.put("recordShowing", courseGroupTree.size());
		return new ModelAndView(addCourseTemplate, "context", context);
	}

	public ModelAndView addCourseGroups( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		AddCustomerContractForm form= (AddCustomerContractForm) command;

		if(form.getCustomerEntitlement() instanceof CourseGroupCustomerEntitlement){
			CourseGroupCustomerEntitlement ent=(CourseGroupCustomerEntitlement)form.getCustomerEntitlement();

			String []strCourseGroupIds = request.getParameterValues("courseGroups");
			long[] cgIds = new long[strCourseGroupIds.length];
			for(int i=0;i<strCourseGroupIds.length;i++){
				cgIds[i] = Long.valueOf(strCourseGroupIds[i]);
			}
			List<CourseGroup> groups = courseAndCourseGroupService.getCourseGroupsByIds(cgIds);
			List<CourseGroupCustomerEntitlementItem> items = new ArrayList<CourseGroupCustomerEntitlementItem>();
			for(CourseGroup cg:groups){
				 CourseGroupCustomerEntitlementItem item = new CourseGroupCustomerEntitlementItem();
				 item.setCourseGroup(cg);
				 item.setCourseGroupCustomerEntitlement(ent);
				 items.add(item);
			 }
			form.setCourseGroupItems(items);
			form.setEntitlementItems(null);  // [10/22/2010] LMS-7511 :: Deleted Contracts were re-inserted on Adding Contract Items
		}
		else{
			CourseCustomerEntitlement ent=(CourseCustomerEntitlement)form.getCustomerEntitlement();

			String []strCourseIds = request.getParameterValues("courses");
			List<CourseCustomerEntitlementItem> entitlementItems = new ArrayList<CourseCustomerEntitlementItem>();
			
			if(strCourseIds!=null){
				for(int i=0;i<strCourseIds.length;i++){

					String []strArray=strCourseIds[i].split(":");

					CourseGroup courseGroup = courseAndCourseGroupService.getCourseGroupById(Long.valueOf(strArray[0]));
					Course course=courseAndCourseGroupService.getCourseById(Long.valueOf(strArray[1]));
					
					 CourseCustomerEntitlementItem item = new CourseCustomerEntitlementItem();
					 item.setCustomerEntitlement(ent);
					 item.setCourse(course);
					 item.setCourseGroup(courseGroup);
					 entitlementItems.add(item);
				}
			}
			
			form.setEntitlementItems(entitlementItems);
			form.setCourseGroupItems(null); // [10/22/2010] LMS-7511 :: Deleted Contracts were re-inserted on Adding Contract Items
			//	entitlementService.saveCustomerEntitlement(ent,null);
		}

		Map<Object, Object> context = new HashMap<Object, Object>();
		List<TreeNode> treeAsList = null;
		CustomerEntitlement contract=form.getCustomerEntitlement();
		
		//  [10/15/2010] LMS-7332 :: Courses are not appearing under Course Groups
		Set<CourseGroup> contractCourseGroups = new HashSet<CourseGroup>();
		treeAsList=this.getTreeForContract(contract, form, contractCourseGroups);		
		context.put("contractCourseGroups", contractCourseGroups);
		
		context.put("coursesTreeAsList", treeAsList);
		context.put("contractType", contract.getEnrollmentType());
		context.put("contractId", contract.getId());

		return new ModelAndView(showContractItemTemplate, "context", context);
	}

	/*
	 * TODO review required
	 */
	public ModelAndView removeCourseGroup( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		AddCustomerContractForm form= (AddCustomerContractForm) command;
		if(form.getCustomerEntitlement() instanceof CourseGroupCustomerEntitlement){
			CourseGroupCustomerEntitlement ent=(CourseGroupCustomerEntitlement)form.getCustomerEntitlement();

			String []strCourseGroupIds = request.getParameterValues("courseGroups");
			long[] cgIds = new long[strCourseGroupIds.length];
			for(int i =0;i<strCourseGroupIds.length;i++){
				cgIds[i] = Long.valueOf(strCourseGroupIds[i]);
			}
			
			for(Long courseGroupId:cgIds){
				Iterator<CourseGroupCustomerEntitlementItem> iterCourseGroupEntitlementItem=form.getCourseGroupItems().iterator();
				while(iterCourseGroupEntitlementItem.hasNext()) {
					CourseGroupCustomerEntitlementItem courseGroupCustomerEntitlementItem=iterCourseGroupEntitlementItem.next();
					if(courseGroupCustomerEntitlementItem.getCourseGroup().getId().equals(courseGroupId) ){
						iterCourseGroupEntitlementItem.remove();
					}
				}
			}
			entitlementService.deleteEntitlementItems(ent,cgIds);
		}
		else{
			
			CourseCustomerEntitlement ent=(CourseCustomerEntitlement)form.getCustomerEntitlement();
			String []strCourseIds = request.getParameterValues("courses");
			List<CourseCustomerEntitlementItem> items = entitlementService.getItemsByEntitlement(ent);//ent.getEntitlementItems();
			List<CourseCustomerEntitlementItem> itemsToBeRemoved=new ArrayList<CourseCustomerEntitlementItem>();
			CourseCustomerEntitlementItem item = null;
			String []strArray = null;
			
			long course = -1;
			long courseGroup = -1;
			
			for(int i=0;i<strCourseIds.length;i++){
				
				strArray = strCourseIds[i].split(":");

				if(strArray.length == 2) {
					courseGroup = Long.valueOf(strArray[0]);
					course = Long.valueOf(strArray[1]);
				}
				
				List<CourseCustomerEntitlementItem> resultItems = entitlementService.findEntitlementItemsByCourse(ent, course);
				
				if(resultItems!=null && resultItems.size()>0) item = resultItems.get(0);
				if(item!=null && items.contains(item)){
					items.remove(item);
					itemsToBeRemoved.add(item);
				}
				
				if (form.getEntitlementItems() != null) {
					Iterator<CourseCustomerEntitlementItem> iterCourseCustomerEntitlementItem=form.getEntitlementItems().iterator();
					while(iterCourseCustomerEntitlementItem.hasNext()) {
						CourseCustomerEntitlementItem courseCustomerEntitlementItem=iterCourseCustomerEntitlementItem.next();
						if (courseCustomerEntitlementItem.getCourse().getId().equals(course)
								&& courseCustomerEntitlementItem.getCourseGroup().getId().equals(courseGroup)) {
							iterCourseCustomerEntitlementItem.remove();
						}
					}
				}
			}
			// because some courses or courses groups may be only in session so for that reason
			if(!itemsToBeRemoved.isEmpty()){
				entitlementService.deleteCourseEntitlementItems(itemsToBeRemoved);
			}
			this.removeEntitlements(itemsToBeRemoved, ent);
		}

		Map<Object, Object> context = new HashMap<Object, Object>();
		List<TreeNode> treeAsList = null;
		CustomerEntitlement contract=form.getCustomerEntitlement();
		treeAsList=this.getTreeForContract(contract,form);
		context.put("coursesTreeAsList", treeAsList);
		context.put("contractType", contract.getEnrollmentType());
		context.put("contractId", contract.getId());
		return new ModelAndView(showContractItemTemplate, "context", context);
	}

	public ModelAndView saveContract( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception{

		AddCustomerContractForm form= (AddCustomerContractForm) command;

		if ( form.getEntitlementItems() != null && !form.getEntitlementItems().isEmpty()){
			entitlementService.addCourseEntitlementItems(form.getEntitlementItems());
		}
		
		if( form.getCourseGroupItems() != null &&  !form.getCourseGroupItems().isEmpty()) {
			entitlementService.addCourseGroupEntitlementItem(form.getCourseGroupItems());
		}
		
		entitlementService.saveCustomerEntitlement(form.getCustomerEntitlement(),null);

		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Long customerId = details.getCurrentCustomerId();

		OrganizationalGroup rootOrgGroup =  orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customerId);
		TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup);
		List<TreeNode> treeAsList = orgGroupRoot.bfs();
		List<OrgGroupEntitlement> orgGroupEntitlements = entitlementService.getOrgGroupsEntilementsForCustomerEntitlement(form.getCustomerEntitlement());
		for(int loop3=0; loop3<orgGroupEntitlements.size(); loop3++){
			OrganizationalGroup orgGroup = orgGroupEntitlements.get(loop3).getOrganizationalGroup();
			map1.put(orgGroup.getId(), new Integer(orgGroupEntitlements.get(loop3).getMaxNumberSeats()));
		}
		context.put("orgGroupTreeAsList", treeAsList);
		context.put("countmap", map1);
		return new ModelAndView(viewCustomerEntitlementTemplate,"context",context);
	}

	public String getAddCourseTemplate() {
		return addCourseTemplate;
	}

	public void setAddCourseTemplate(String addCourseTemplate) {
		this.addCourseTemplate = addCourseTemplate;
	}

	public String getAddCourseGroupTemplate() {
		return addCourseGroupTemplate;
	}

	public void setAddCourseGroupTemplate(String addCourseGroupTemplate) {
		this.addCourseGroupTemplate = addCourseGroupTemplate;
	}

	public ModelAndView showContractItems(HttpServletRequest request, 
			HttpServletResponse response,Object command, BindException errors) throws Exception{ 			

		try {
			AddCustomerContractForm form = (AddCustomerContractForm) command;
			Map<Object, Object> context = new HashMap<Object, Object>();
			List<TreeNode> treeAsList = null;//getEntitlementCourseGroupTreeNode(null);
			String contractId = request.getParameter("contractId") != null && !request.getParameter("contractId").trim().equals("") 
			? request.getParameter("contractId").trim() : "";

			CustomerEntitlement contract=form.getCustomerEntitlement();//entitlementService.getCustomerEntitlementById(new Long(contractId));
			//CustomerEntitlement contract=entitlementService.getCustomerEntitlementById(new Long(contractId));

			if(contract==null){
				contract = entitlementService.getCustomerEntitlementById(
						Long.parseLong(contractId));

				form.setCustomerEntitlement(contract);
				if(contract instanceof CourseCustomerEntitlement){
					form.getEntitlementItems().addAll(entitlementService.getItemsByEntitlement((CourseCustomerEntitlement)contract));
				}
				else if (contract instanceof CourseGroupCustomerEntitlement){
					form.getCourseGroupItems().addAll( entitlementService.getItemsByEntitlement((CourseGroupCustomerEntitlement)contract));
				}
				form.setEnrollmentType(contract.getEnrollmentType());
				form.setAllowSelfEnrollment(contract.isAllowSelfEnrollment());
				form.setContractName(contract.getName());
				form.setMaximumEnrollmentsLimitedValue(contract.getMaxNumberSeats()+"");
				form.setEntId(contract.getId());
				form.setMaximumEnrollmentsUnLimited(contract.isAllowUnlimitedEnrollments());
				form.setTermsOfServicesValue(contract.getDefaultTermOfServiceInDays()+"");
			}

			Set<CourseGroup> contractCourseGroups = new HashSet<CourseGroup>();
			treeAsList = entitlementService.getTreeForContract(contract, contractCourseGroups);

			context.put("contractCourseGroups", contractCourseGroups);
			context.put("coursesTreeAsList", treeAsList);
			context.put("contractType", contract.getEnrollmentType());
			context.put("contractId", contractId);

			return new ModelAndView(showContractItemTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(showContractItemTemplate);
	}
	
	public ModelAndView showSubscriptionContractItems(HttpServletRequest request, 
			HttpServletResponse response,Object command, BindException errors) throws Exception{ 			

		try {
			AddCustomerContractForm form = (AddCustomerContractForm) command;
			Map<Object, Object> context = new HashMap<Object, Object>();
			List<TreeNode> treeAsList = null;//getEntitlementCourseGroupTreeNode(null);
			String contractId = request.getParameter("contractId") != null && !request.getParameter("contractId").trim().equals("") 
			? request.getParameter("contractId").trim() : "";

			CustomerEntitlement contract=form.getCustomerEntitlement();//entitlementService.getCustomerEntitlementById(new Long(contractId));
			//CustomerEntitlement contract=entitlementService.getCustomerEntitlementById(new Long(contractId));

			if(contract==null){
				contract = entitlementService.getCustomerEntitlementById(
						Long.parseLong(contractId));

				form.setCustomerEntitlement(contract);
				if(contract instanceof CourseCustomerEntitlement){
					form.getEntitlementItems().addAll(entitlementService.getItemsByEntitlement((CourseCustomerEntitlement)contract));
				}
				else if (contract instanceof CourseGroupCustomerEntitlement){
					form.getCourseGroupItems().addAll( entitlementService.getItemsByEntitlement((CourseGroupCustomerEntitlement)contract));
				}
				form.setEnrollmentType(contract.getEnrollmentType());
				form.setAllowSelfEnrollment(contract.isAllowSelfEnrollment());
				form.setContractName(contract.getName());
				form.setMaximumEnrollmentsLimitedValue(contract.getMaxNumberSeats()+"");
				form.setEntId(contract.getId());
				form.setMaximumEnrollmentsUnLimited(contract.isAllowUnlimitedEnrollments());
				form.setTermsOfServicesValue(contract.getDefaultTermOfServiceInDays()+"");
			}

			//treeAsList=this.getTreeForContract(contract);
			Set<CourseGroup> contractCourseGroups = new HashSet<CourseGroup>();
			treeAsList = entitlementService.getTreeForContract(contract, contractCourseGroups);
			context.put("contractCourseGroups", contractCourseGroups);
			context.put("coursesTreeAsList", treeAsList);
			context.put("contractType", contract.getEnrollmentType());
			context.put("contractId", contractId);
			context.put("tab", 2);

			return new ModelAndView(showSubscriptionContractItemTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(showSubscriptionContractItemTemplate);
	}
		
	
	/**
	 * //  [10/15/2010] LMS-7332
	 * Overloaded method to show Courses under Course Groups while adding Course Groups to the Customer Contract 
	 * @param entitlement
	 * @param form
	 * @param contractCourseGroups
	 * @return
	 */
	private List<TreeNode> getTreeForContract(CustomerEntitlement entitlement, AddCustomerContractForm form, Set<CourseGroup> contractCourseGroups) {
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		
		if (contractCourseGroups == null) {
			contractCourseGroups = new HashSet<CourseGroup>();
		}
		
		if(entitlement instanceof CourseGroupCustomerEntitlement){				
			List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
			TreeNode courseGroupCourseTreeNode = null;
			List<CourseGroupCustomerEntitlementItem> items = entitlementService.getItemsByEntitlement((CourseGroupCustomerEntitlement)entitlement);
			
			if(!form.getCourseGroupItems().isEmpty())
				items.addAll(form.getCourseGroupItems());
			
			for (CourseGroupCustomerEntitlementItem item : items) {
				boolean courseGroupAdded = false;
				CourseSort courseSort = new CourseSort();
				
				contractCourseGroups.add( item.getCourseGroup() );
				if ( (item.getCourseGroup().getChildrenCourseGroups() != null) && (! item.getCourseGroup().getChildrenCourseGroups().isEmpty()) ) {
					contractCourseGroups.addAll( item.getCourseGroup().getChildrenCourseGroups() );
				}
				
				List<Course> cgCourses = item.getCourseGroup().getActiveCourses(); // [07/22/2010] LMS-6388:: Hide retired courses from all queries and views
				if (cgCourses != null && cgCourses.size() > 0) {
					Collections.sort(cgCourses, courseSort);
					item.getCourseGroup().setCourses(cgCourses);
				}
				for (TreeNode rootTreeNode : rootNodesReferences) {
					List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
					if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, item.getCourseGroup(), childCourseGroups)) {
						courseGroupAdded = true;
						break;
					}

					CourseGroup currentCourseGroup = item.getCourseGroup();
					while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
						CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
						childCourseGroups.add(currentCourseGroup);
						if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
							courseGroupAdded = true;
							break;
						}
						currentCourseGroup = parentCourseGroup;
					}

					if (courseGroupAdded)
						break;
				}
				if (!courseGroupAdded) {
					courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(item.getCourseGroup(), null);
					rootNodesReferences.add(courseGroupCourseTreeNode);
				}						
			}

			for (TreeNode rootTreeNode : rootNodesReferences) {
				treeNodesList.addAll(rootTreeNode.bfs());
			}

		}
		else {
			CourseCustomerEntitlement courseCustomerEntitlement = (CourseCustomerEntitlement)entitlement;
			List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
			TreeNode courseGroupCourseTreeNode = null;
			List<CourseCustomerEntitlementItem> items = entitlementService.getItemsByEntitlement(courseCustomerEntitlement);
			
			if(!form.getEntitlementItems().isEmpty())
				items.addAll(form.getEntitlementItems());
			
			for(CourseCustomerEntitlementItem courseCustomerEntitlementItem : items) {
				Course course = courseCustomerEntitlementItem.getCourse();
				if (course.isActive()) { // [1/27/2011] LMS-7183 :: Retired Course Functionality II
					CourseGroup courseGroup = courseCustomerEntitlementItem.getCourseGroup();
					if(courseGroup==null){
						courseGroup = new CourseGroup();
						java.util.Date date = new java.util.Date();
						courseGroup.setId(new Long((date.getHours()+ date.getMinutes() + date.getSeconds())*100));
						courseGroup.setName("Miscellaneous");
					}
	
					boolean courseAdded = false;					
					for (TreeNode rootTreeNode : rootNodesReferences) {
						List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
						if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, courseGroup, course, childCourseGroups)) {
							courseAdded = true;
							break;
						}
	
						CourseGroup currentCourseGroup = courseGroup;
						while (!courseAdded && currentCourseGroup.getParentCourseGroup() != null) {
							CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
							childCourseGroups.add(currentCourseGroup);
							if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, parentCourseGroup, course, childCourseGroups)) {
								courseAdded = true;
								break;
							}
							currentCourseGroup = parentCourseGroup;
						}
	
						if (courseAdded)
							break;
					}
					if (!courseAdded) {
						courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, course);
						rootNodesReferences.add(courseGroupCourseTreeNode);
					}
				}
			}

			for (TreeNode rootTreeNode : rootNodesReferences) {
				treeNodesList.addAll(rootTreeNode.bfs());
			}
		}

		return treeNodesList;
	}


	private List<TreeNode> getTreeForContract(CustomerEntitlement entitlement,AddCustomerContractForm form) {
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();


		if(entitlement instanceof CourseGroupCustomerEntitlement){				
			List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
			TreeNode courseGroupCourseTreeNode = null;
			List<CourseGroupCustomerEntitlementItem> items = entitlementService.getItemsByEntitlement((CourseGroupCustomerEntitlement)entitlement);
			
			if(!form.getCourseGroupItems().isEmpty())
				items.addAll(form.getCourseGroupItems());
			
			for (CourseGroupCustomerEntitlementItem item : items) {
				boolean courseGroupAdded = false;
				CourseSort courseSort = new CourseSort();
				List<Course> cgCourses = item.getCourseGroup().getActiveCourses(); // [07/22/2010] LMS-6388:: Hide retired courses from all queries and views
				if (cgCourses != null && cgCourses.size() > 0) {
					Collections.sort(cgCourses, courseSort);
					item.getCourseGroup().setCourses(cgCourses);
				}
				for (TreeNode rootTreeNode : rootNodesReferences) {
					List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
					if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, item.getCourseGroup(), childCourseGroups)) {
						courseGroupAdded = true;
						break;
					}

					CourseGroup currentCourseGroup = item.getCourseGroup();
					while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
						CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
						childCourseGroups.add(currentCourseGroup);
						if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
							courseGroupAdded = true;
							break;
						}
						currentCourseGroup = parentCourseGroup;
					}

					if (courseGroupAdded)
						break;
				}
				if (!courseGroupAdded) {
					courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(item.getCourseGroup(), null);
					rootNodesReferences.add(courseGroupCourseTreeNode);
				}						
			}

			for (TreeNode rootTreeNode : rootNodesReferences) {
				treeNodesList.addAll(rootTreeNode.bfs());
			}

		}
		else {
			CourseCustomerEntitlement courseCustomerEntitlement = (CourseCustomerEntitlement)entitlement;
			List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
			TreeNode courseGroupCourseTreeNode = null;
			List<CourseCustomerEntitlementItem> items = entitlementService.getItemsByEntitlement(courseCustomerEntitlement);
			
			if(!form.getEntitlementItems().isEmpty())
				items.addAll(form.getEntitlementItems());
			
			for(CourseCustomerEntitlementItem courseCustomerEntitlementItem : items) {
				Course course = courseCustomerEntitlementItem.getCourse();
				if (course.isActive()) { // [1/27/2011] LMS-7183 :: Retired Course Functionality II
					CourseGroup courseGroup = courseCustomerEntitlementItem.getCourseGroup();
					if(courseGroup==null){
						courseGroup = new CourseGroup();
						java.util.Date date = new java.util.Date();
						courseGroup.setId(new Long((date.getHours()+ date.getMinutes() + date.getSeconds())*100));
						courseGroup.setName("Miscellaneous");
					}
	
					boolean courseAdded = false;					
					for (TreeNode rootTreeNode : rootNodesReferences) {
						List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
						if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, courseGroup, course, childCourseGroups)) {
							courseAdded = true;
							break;
						}
	
						CourseGroup currentCourseGroup = courseGroup;
						while (!courseAdded && currentCourseGroup.getParentCourseGroup() != null) {
							CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
							childCourseGroups.add(currentCourseGroup);
							if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, parentCourseGroup, course, childCourseGroups)) {
								courseAdded = true;
								break;
							}
							currentCourseGroup = parentCourseGroup;
						}
	
						if (courseAdded)
							break;
					}
					if (!courseAdded) {
						courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, course);
						rootNodesReferences.add(courseGroupCourseTreeNode);
					}
				}
			}
			for (TreeNode rootTreeNode : rootNodesReferences) {
				treeNodesList.addAll(rootTreeNode.bfs());
			}
		}

		return treeNodesList;
	}

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

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}


	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	private void removeEntitlements(List<CourseCustomerEntitlementItem> itemsToBeRemoved, CourseCustomerEntitlement ent) {

//		Comment the code due to LMS-7293 
		
//		List<LearnerEnrollment> learnerEnrollments=entitlementService.getLearnerEnrollmentsAgainstCustomerEntitlement(ent);
//
//		for(LearnerEnrollment learnerEnrollment : learnerEnrollments){
//			for(CourseCustomerEntitlementItem item : itemsToBeRemoved){
//				if(learnerEnrollment.getCourse().equals(item.getCourse())){
//					learnerEnrollment.setEnrollmentEndDate(new java.util.Date());
//					learnerEnrollment.setEnrollmentStatus(LearnerEnrollment.DROPPED);
//					if(ent.getNumberSeatsUsed()>0)
//						ent.setNumberSeatsUsed(ent.getNumberSeatsUsed() - 1);
//				}
//			}					
//		}
//
//		enrollmentService.addEnrollments(learnerEnrollments);
		entitlementService.saveCustomerEntitlement(ent, null);

	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}
	
	public String getViewSubscriptionCustomerEntitlementTemplate() {
		return viewSubscriptionCustomerEntitlementTemplate;
	}

	public void setViewSubscriptionCustomerEntitlementTemplate(
			String viewSubscriptionCustomerEntitlementTemplate) {
		this.viewSubscriptionCustomerEntitlementTemplate = viewSubscriptionCustomerEntitlementTemplate;
	}

	public String getShowSubscriptionContractItemTemplate() {
		return showSubscriptionContractItemTemplate;
	}

	public void setShowSubscriptionContractItemTemplate(
			String showSubscriptionContractItemTemplate) {
		this.showSubscriptionContractItemTemplate = showSubscriptionContractItemTemplate;
	}
}