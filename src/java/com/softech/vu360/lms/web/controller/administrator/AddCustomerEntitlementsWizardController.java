package com.softech.vu360.lms.web.controller.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddCustomerEntitlementsForm;
import com.softech.vu360.lms.web.controller.model.CustomerEntitlementsCourseGroup;
import com.softech.vu360.lms.web.controller.model.OrganisationalGroupEntitlementItem;
import com.softech.vu360.lms.web.controller.model.SurveyCourse;
import com.softech.vu360.lms.web.controller.validator.AddCustomerEntitlementsValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.TreeNodeCourseGroup;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddCustomerEntitlementsWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddCustomerEntitlementsWizardController.class.getName());

	private static final String MANAGE_ENTITLEMENT_SIMPLESEARCH_ACTION = "simpleSearch";
	private static final String MANAGE_ENTITLEMENT_ADVANCEDSEARCH_ACTION = "advancedSearch";
	private static final String MANAGE_ENTITLEMENT_ALLSEARCH_ACTION ="allsearch";

	private String closeTemplate = null;

	private LearnerService learnerService = null;
	private EntitlementService entitlementService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private OrgGroupLearnerGroupService groupService = null;

	public AddCustomerEntitlementsWizardController() {
		super();
		setCommandName("addCustomerEntitlementsForm");
		setCommandClass(AddCustomerEntitlementsForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"administrator/customerEntitlements/addCustomerEntitlementDetails"
				, "administrator/customerEntitlements/addCustomerEntitlementOrgGroup"
				, "administrator/customerEntitlements/addCustomerEntitlementCourse"
				, "administrator/customerEntitlements/addCustomerEntitlementCourseGroup"
				, "administrator/customerEntitlements/addCustomerEntitlementSummary"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			if( details.getCurrentCustomer() == null ){
				return new ModelAndView(closeTemplate);
			}
		}
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#onBind(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;
		int Page=getCurrentPage(request);

		if (Page==2){
			if (request.getParameter("courseGroups")==null){
				if (form.getCourseGroups()!=null){
					form.setCourseGroups(null);
					form.setSelectedCourses(null);
				}		
			}
		}

		if (Page==3){
			if (request.getParameter("groups")==null){
				if (form.getGroups()!=null){
					form.setGroups(null);
					form.setSelectedCourseGroups(null);
				}		
			}
		}
		super.onBind(request, command, errors);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;

		if ( currentPage == 1 && this.getTargetPage(request, currentPage) != 0 ) {
			if (form.isEntitlementType()) {
				return 3;
			} else {
				return 2;
			} 
		} else if ( currentPage == 4 ) {
			if (form.isEntitlementType()) {
				form.setPageIndex(3);
			} else {
				form.setPageIndex(2);
			} 
		} else if(currentPage == 2 ) {
			if (request.getParameter("action") != null)
				if (request.getParameter("action").equals("search"))
					return 2;
		} else if(currentPage == 3 ) {
			if (request.getParameter("action") != null)
				if (request.getParameter("action").equals("search"))
					return 3;
		}
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		// TODO Auto-generated method stub
		super.onBindAndValidate(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int currentPage) throws Exception {
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;
		AddCustomerEntitlementsValidator validator = (AddCustomerEntitlementsValidator)this.getValidator();
		if ( currentPage == 2 ) {
			if(request.getParameter("action")!=null){
				if(request.getParameter("action").equals("search")){
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
					HashMap<Long,Course> courseList = null;
					String searchType = form.getCourseSearchType();
					if( searchType== null){
						// Course Tree
					}else {
						 /* -- commenting following function call because findCoursesByDistributor() is in used for older forms and which are not in use
							if (searchType.equalsIgnoreCase(MANAGE_ENTITLEMENT_SIMPLESEARCH_ACTION)) {
								courseList=entitlementService.findCoursesByDistributor(customer.getDistributor(), "", "", "", form.getCourseSimpleSearchKey());
							} else if (searchType.equalsIgnoreCase(MANAGE_ENTITLEMENT_ADVANCEDSEARCH_ACTION)) {
								courseList=entitlementService.findCoursesByDistributor(customer.getDistributor(), form.getSearchCourseName(), form.getSearchCourseID(), form.getSearchCourseKeyword(), "");
							} else if (searchType.equalsIgnoreCase(MANAGE_ENTITLEMENT_ALLSEARCH_ACTION)) {
								courseList=entitlementService.findCoursesByDistributor(customer.getDistributor(), "", "", "", form.getCourseSimpleSearchKey());
							}
						
						*/
						//log.debug("-----JASON:courses:"+courseList.size());
						List<SurveyCourse> selectedCourses = new ArrayList<SurveyCourse>();
						if(courseList !=null){
							for (Map.Entry<Long, Course>entry : courseList.entrySet()) {
								SurveyCourse entitlementCourse = new SurveyCourse();
								log.debug("courseId="+entry.getKey());
								entitlementCourse.setCourse(entry.getValue());
								entitlementCourse.setSelected(false);
								selectedCourses.add(entitlementCourse);
							}
						}
						
						form.setSelectedCourses(selectedCourses);

					}
				}
			}
		} else if (currentPage == 3) {
			if(request.getParameter("action")!=null){
				if(request.getParameter("action").equals("search")){
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
					Customer customer = details.getCurrentCustomer();

					List<CourseGroup> courseGroupList = null;
					String searchType = form.getCourseGroupSearchType();
					if(searchType == null){
						// CourseGroup Tree
					}else {
						List<DistributorEntitlement> distributorEntitlementsList = entitlementService.getAllDistributorEntitlements(customer.getDistributor());
						Set<Long> courseGroupIdSet = new HashSet<Long>();
						for(int loop1=0;loop1<distributorEntitlementsList.size();loop1++){
							DistributorEntitlement distributorEntitlement = distributorEntitlementsList.get(loop1);
							Date date = distributorEntitlement.getStartDate();
							Date endDate = distributorEntitlement.getEndDate();
							if( endDate == null || date.before(endDate)) {
								for(int loop2=0;loop2<distributorEntitlement.getCourseGroups().size();loop2++){
									courseGroupIdSet = this.arrangecourseObj(distributorEntitlement.getCourseGroups().get(loop2),courseGroupIdSet);
								}
							}
						}	
						long[] courseGroupIdArray = new long[courseGroupIdSet.size()];
						Iterator<Long> iter = courseGroupIdSet.iterator();
						int i=0;
						while (iter.hasNext()) {
							courseGroupIdArray[i++] = (Long) iter.next();
						}

						if(searchType.equalsIgnoreCase(MANAGE_ENTITLEMENT_SIMPLESEARCH_ACTION)){
							courseGroupList = entitlementService.findCourseGroupsByDistributor(courseGroupIdArray, form.getCourseGroupSimpleSearchKey());
						} else if (searchType.equalsIgnoreCase(MANAGE_ENTITLEMENT_ALLSEARCH_ACTION)){
							courseGroupList = entitlementService.findCourseGroupsByDistributor(courseGroupIdArray, "");
						}
						
						log.debug("-----JASON:courseGroups:"+courseGroupList.size());

						List<CustomerEntitlementsCourseGroup> selectedCourseGroup = new ArrayList<CustomerEntitlementsCourseGroup>();
						for (CourseGroup courseGrp : courseGroupList) {
							CustomerEntitlementsCourseGroup entitlementCourseGrp = new CustomerEntitlementsCourseGroup();
							entitlementCourseGrp.setCourseGroup(courseGrp);
							entitlementCourseGrp.setSelected(false);
							selectedCourseGroup.add(entitlementCourseGrp);
						}
						form.setSelectedCourseGroups(selectedCourseGroup);
					}
				}
			}
		} 

		if (currentPage == 2) {
			if(this.getTargetPage(request, currentPage) == 4 && (request.getParameter("action") == null || request.getParameter("action").equalsIgnoreCase(""))){
				validator.validateCoursePage(form, errors);
			}
		}

		if (currentPage == 3) {
			if(this.getTargetPage(request, currentPage) == 4 && (request.getParameter("action") == null || request.getParameter("action").equalsIgnoreCase(""))){
				validator.validateCourseGroupPage(form, errors);
			}
		}
		super.postProcessPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		return new ModelAndView(closeTemplate);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Customer customer = details.getCurrentCustomer();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		CustomerEntitlement customerEntitlement = new CustomerEntitlement();
		if (form.isEntitlementType()) {
			List<CourseGroup> selectedCourseGroups = new ArrayList<CourseGroup>();
			for(CustomerEntitlementsCourseGroup courseGroup : form.getSelectedCourseGroups()){
				if(courseGroup.isSelected()){
					selectedCourseGroups.add(courseGroup.getCourseGroup());
				}
			}
			customerEntitlement = new CourseGroupCustomerEntitlement();
		} else {
			List<Course> selectedCourses = new ArrayList<Course>();
			for(SurveyCourse surveyCourse : form.getSelectedCourses()){
				if(surveyCourse.isSelected()){
					selectedCourses.add(surveyCourse.getCourse());
				}
			}
			// TODO LMS-4445 needs to revamp this functionality
			//((CourseCustomerEntitlement)customerEntitlement).setCourses(selectedCourses);
		}
		customerEntitlement.setName(form.getEntitlementName());
		List<OrgGroupEntitlement> orgGroupEntitlementList = new ArrayList<OrgGroupEntitlement>();
		List<OrganisationalGroupEntitlementItem> OrgGroupEntitlementItem = form.getOrganisationalGroupEntitlementItems();
		customerEntitlement.setCustomer(customer);
		
		customerEntitlement.setStartDate(formatter.parse(form.getStartDate()));
		if (form.isTermsOfService())
			customerEntitlement.setDefaultTermOfServiceInDays(Integer.parseInt(form.getDays()));
		else
			customerEntitlement.setEndDate(formatter.parse(form.getFiexedEndDate()));
		if (!form.isMaxEnrollments()) {
			customerEntitlement.setMaxNumberSeats(Integer.parseInt(form.getNoOfMaxEnrollments()));
		} else {
			customerEntitlement.setAllowUnlimitedEnrollments(true);
		}
		if (form.isAllowSelfEnrollments())
			customerEntitlement.setAllowSelfEnrollment(true);

		/*
		 * Let me check whether I am createdfor a specific org group
		 * LMS-2851 If org group selected is blank then system should allow any user to access 
		 * all the courses present in entitlement in course catalog. 
		 * However if the entitlement is created for a specific org group 
		 * then system must display those courses in course catalog for the learner 
		 * in which entitlement is created for that org group
		 */
		boolean isCreatedForSpecificOrgGroup = false;
		for (OrganisationalGroupEntitlementItem OrgGroupEnt : OrgGroupEntitlementItem) {
			if( !StringUtils.isBlank(OrgGroupEnt.getMaxEnrollments()) ){
				isCreatedForSpecificOrgGroup = true;
				break;
			}
		}
		
		for (OrganisationalGroupEntitlementItem OrgGroupEnt : OrgGroupEntitlementItem) {
			
			/*
			 * Org group may be kept blank or be provided some value. User may check the box or may
			 * not check. As per Shuja
			 * 1)Customer Entitlemnet is unlimited. Org group entitlements will be unlimited if it kept blank.
			 *    if ORG group entitlement is provided it will be set as max org group entitlement.
			 * 
			 * 2) Customer entitlement is limited.  Org group entitlements will be maximum if it kept blank.
			 */
			if (OrgGroupEnt.isSelected()) { // not clear about this selection
				OrgGroupEntitlement orgGroupEntitlement = new OrgGroupEntitlement();
				//orgGroupEntitlement.setOrganizationalGroup(learnerService.getOrganizationalGroupById(OrgGroupEnt.getOrganisationalGroupId()));
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
				if (form.isTermsOfService())
					orgGroupEntitlement.setDefaultTermOfServiceInDays(Integer.parseInt(form.getDays()));
				if (form.isAllowSelfEnrollments())
					orgGroupEntitlement.setAllowSelfEnrollment(true);
				orgGroupEntitlement.setStartDate(formatter.parse(form.getStartDate()));
				if (!form.isTermsOfService())
					orgGroupEntitlement.setEndDate(formatter.parse(form.getFiexedEndDate()));
				/*if (form.isMaxEnrollments())
					orgGroupEntitlement.setAllowUnlimitedEnrollments(true);*/
				if (form.isAllowSelfEnrollments())
					orgGroupEntitlement.setAllowSelfEnrollment(true);

				orgGroupEntitlementList.add(orgGroupEntitlement);
			}
		}
		//customerEntitlement.setOrgGroupEntitlements(orgGroupEntitlementList);
	//	customerEntitlement.setDistributor(customer.getDistributor());

		entitlementService.saveCustomerEntitlement(customerEntitlement,orgGroupEntitlementList);

		return new ModelAndView(closeTemplate);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		Map model = new HashMap();
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;

		//VU360User user = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Customer customer = details.getCurrentCustomer();

		switch(page){
		case 0:
			break;
		case 1:	
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
			model.put("orgGroupTreeAsList", treeAsList);
			return model;
		case 2:
			if ( ( StringUtils.isBlank(request.getParameter("action")) )
					&& form.getCourseSearchType().isEmpty() ) {
				// grab all possible entitlements so we can generate a course listing
				List<DistributorEntitlement> distributorEntitlementsList = entitlementService.getAllDistributorEntitlements(customer.getDistributor());
				Set<CourseGroup> courseGroupSet = new HashSet<CourseGroup>();
				Date startDate = null;
				Date endDate = null;
				Date today = new Date(System.currentTimeMillis());
				log.debug("----JASON:distEntl"+distributorEntitlementsList.size());
				for (DistributorEntitlement distEnt : distributorEntitlementsList) {
					startDate = distEnt.getStartDate();
					endDate = distEnt.getEndDate();
					log.debug("---jason:startDATE:"+startDate);
					log.debug("---jason:endDATE:"+endDate);
					log.debug("---jason:today:"+today);
					if ( startDate.before(today) ) {
						if( endDate == null || endDate.after(today) ) {
							log.debug("---JASON at least got into stack start!");
							// grab all coursegroups and add them to the stack
							Stack<CourseGroup> stack = new Stack<CourseGroup>();
							List<CourseGroup> cGroups = distEnt.getCourseGroups();
							log.debug("---JASON:stackCourseGrps:"+cGroups.size());
							/*
							 * LMS-2532: added course from LCMS is not showing in coursegroup.
							 * refreshing each coursegroup object.
							 */
							//for(CourseGroup cachedCourseGroup : cGroups){
								//stack.add(courseAndCourseGroupService.getCourseGroupById(cachedCourseGroup.getId()));
								//stack.add(courseAndCourseGroupService.getCourseGroupById(cachedCourseGroup);
						//	}
							stack.addAll(cGroups);
							CourseGroup courseGrp;
							while ( !stack.isEmpty() ) {
								courseGrp = stack.pop();
								if ( courseGrp.getRootCourseGroup() != null ) {
									courseGroupSet.add(courseGrp.getRootCourseGroup());
								}
								if ( courseGrp.getChildrenCourseGroups() != null && courseGrp.getChildrenCourseGroups().size() > 0 ) {
									
									stack.addAll(courseGrp.getChildrenCourseGroups());
								}
							}
						}
					}
				}
				log.debug("found :"+courseGroupSet.size()+" root course groups.");
				String[] courseID = form.getCourseGroups();
				List<Long> cGroupIdList = new ArrayList<Long>();
				if(courseID!=null && courseID.length>0){
					for(String cId : courseID){
						Long courseId = Long.parseLong(cId);
						cGroupIdList.add(courseId);
					}
				}
				List<TreeNodeCourseGroup> courseTreeAsList = new ArrayList<TreeNodeCourseGroup>();
				for (CourseGroup rootCourseGroup : courseGroupSet) {
					TreeNodeCourseGroup courseGroupRoot = getCourseTree(null, rootCourseGroup,cGroupIdList);
					courseTreeAsList.add(courseGroupRoot);
				}

				model.put("distributorName", customer.getDistributor().getName());
				model.put("courseTreeAsList", courseTreeAsList);
			}
			return model;
		case 3:
			if ( (StringUtils.isBlank(request.getParameter("action")) )
					&& form.getCourseSearchType().isEmpty() ) {
				List<DistributorEntitlement> distributorEntitlementsList = entitlementService.getAllDistributorEntitlements(customer.getDistributor());
				Set<CourseGroup> courseGroupSet = new HashSet<CourseGroup>();
				Date startDate = null;
				Date endDate = null;
				Date today = new Date(System.currentTimeMillis());
				log.debug("----JASON:distEntl"+distributorEntitlementsList.size());
				for (DistributorEntitlement distEnt : distributorEntitlementsList) {
					startDate = distEnt.getStartDate();
					endDate = distEnt.getEndDate();
					log.debug("---jason:startDATE:"+startDate);
					log.debug("---jason:endDATE:"+endDate);
					log.debug("---jason:today:"+today);
					if ( startDate.before(today) ) {
						if( endDate == null || endDate.after(today) ) {
							log.debug("---JASON at least got into stack start!");
							// grab all coursegroups and add them to the stack
							Stack<CourseGroup> stack = new Stack<CourseGroup>();
							List<CourseGroup> cGroups = distEnt.getCourseGroups();
							log.debug("---JASON:stackCourseGrps:"+cGroups.size());
							stack.addAll(cGroups);
							CourseGroup courseGrp;
							
							while ( !stack.isEmpty() ) {
								courseGrp = stack.pop();
								if ( courseGrp.getRootCourseGroup() != null ) {
									courseGroupSet.add(courseGrp.getRootCourseGroup());
								}
								if ( courseGrp.getChildrenCourseGroups() != null && courseGrp.getChildrenCourseGroups().size() > 0 ) {
									stack.addAll(courseGrp.getChildrenCourseGroups());
								}
								
								
							}
						}
					}
				}
				log.debug("found :"+courseGroupSet.size()+" root course groups.");
				
				String[] courseGrp = form.getGroups();
				List<Long> cGroupIdList = new ArrayList<Long>();
				if(courseGrp!=null && courseGrp.length>0){
					for(String cGroupId : courseGrp){
						Long courseGroupId = Long.parseLong(cGroupId);
						cGroupIdList.add(courseGroupId);
					}
				}
				List<List<TreeNode>> courseGroupTreeAsList = new ArrayList<List<TreeNode>>();
				for (CourseGroup rootCourseGroup : courseGroupSet) {
					TreeNode courseGroupRoot = getCourseGroupTree(null, rootCourseGroup,cGroupIdList);
					courseGroupTreeAsList.add(courseGroupRoot.bfs());
				}
				model.put("distributorName", customer.getDistributor().getName());
				model.put("courseGroupTreeAsList", courseGroupTreeAsList);
			}
			return model;
		case 4:
			for (OrganisationalGroupEntitlementItem entitlementItem : (List<OrganisationalGroupEntitlementItem>)form.getOrganisationalGroupEntitlementItems()) {
				if (entitlementItem.isSelected()) {
					entitlementItem.setOrganisationalGroupName(entitlementItem.getOrganisationalGroupName());
				}
			}

			if (form.isEntitlementType() == true) {
				if (form.getGroups() != null) {
					List<CustomerEntitlementsCourseGroup> entitlementsCourseGroupList = new ArrayList<CustomerEntitlementsCourseGroup>();
					for (String id : form.getGroups()) {
						CustomerEntitlementsCourseGroup entitlementsCourseGroup = new CustomerEntitlementsCourseGroup();
						CourseGroup cGrp = courseAndCourseGroupService.getCourseGroupById(Long.valueOf(id));
						entitlementsCourseGroup.setCourseGroup(cGrp);
						entitlementsCourseGroup.setSelected(true);
						entitlementsCourseGroupList.add(entitlementsCourseGroup);
					}
					form.setSelectedCourseGroups(entitlementsCourseGroupList);
				}
			} else {
				if (form.getCourseGroups() != null) {
					List<SurveyCourse> entitlementsCourseList = new ArrayList<SurveyCourse>();
					for (String id : form.getCourseGroups()) {
						SurveyCourse entitlementsCourse = new SurveyCourse();
						Course course = courseAndCourseGroupService.getCourseById(Long.valueOf(id));
						entitlementsCourse.setCourse(course);
						entitlementsCourse.setSelected(true);
						entitlementsCourseList.add(entitlementsCourse);
					}
					form.setSelectedCourses(entitlementsCourseList);
				}
			}

			break;
		default :
			break;
		}	
		return super.referenceData(request, command, errors, page);
	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		AddCustomerEntitlementsValidator validator = (AddCustomerEntitlementsValidator)this.getValidator();
		AddCustomerEntitlementsForm form = (AddCustomerEntitlementsForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			validator.validateDetailsPage(form, errors);
			break;
		case 1:
			validator.validateOrgGrpEntitlementPage(form, errors);
			break;
		case 2:
			break;
		case 3:
			break;
		}
		super.validatePage(command, errors, page, finish);
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


	private TreeNode getCourseGroupTree(TreeNode parentNode, CourseGroup courseGroup, List<Long> selectedCourseGroups){
		if(courseGroup!=null){ 
			TreeNode node = new TreeNode(courseGroup);
			for(Long selectedId : selectedCourseGroups){
				if(courseGroup.getId().longValue() == selectedId.longValue()){
					node.setSelected(true);
					break;
				}
			}

			List<CourseGroup> childGroups = courseGroup.getChildrenCourseGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getCourseGroupTree(node, childGroups.get(i),selectedCourseGroups);
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

	private TreeNodeCourseGroup getCourseTree(TreeNodeCourseGroup parentNode, CourseGroup courseGroup, List<Long> selectedCoursesIds){
		if(courseGroup!=null){

			List<Course> courseList = courseGroup.getCourses();
			List<Course> uniqueCourseList = new ArrayList<Course> ();
			boolean present = false;
			
			for(Course course : courseList) {
				for(Course uniqueCourse : uniqueCourseList) {
					if( course.getId().compareTo(uniqueCourse.getId()) == 0 ) {
						present = true;
						break;
					}
				}
				if( !present ){
					if(StringUtils.isNotBlank(course.getCourseStatus()) && course.getCourseStatus().equalsIgnoreCase(Course.PUBLISHED))
						uniqueCourseList.add(course);
				}
				present = false;
			}
			List<SurveyCourse> courseObjList = new ArrayList<SurveyCourse>();

			for(Course course : uniqueCourseList) {
				SurveyCourse courseObj = new SurveyCourse();
				courseObj.setCourse(course);
				courseObj.setEnabled(true);
				Long id = course.getId();
				for(Long selectedId : selectedCoursesIds){
					if( id.compareTo(selectedId) == 0 ) {
						courseObj.setSelected(true);
						break;
					} else {
						courseObj.setSelected(false);
					}
				}
				courseObjList.add(courseObj);
			}

			TreeNodeCourseGroup node = new TreeNodeCourseGroup(courseGroup);
			node.setCourses(courseObjList);

			List<CourseGroup> childGroups = courseGroup.getChildrenCourseGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getCourseTree(node, childGroups.get(i),selectedCoursesIds);
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
	
	private Set<Long> arrangecourseObj(CourseGroup courseGroup, Set<Long> courseGroupIdSet ){


		courseGroupIdSet.add(courseGroup.getId().longValue());
		if (courseGroup.getChildrenCourseGroups() != null 
				&& courseGroup.getChildrenCourseGroups().size() > 0) {
			List<CourseGroup> childCourseGroups = courseGroup.getChildrenCourseGroups();
			for (int childCount = 0; childCount< childCourseGroups.size();childCount++ ) {
				courseGroupIdSet = this.arrangecourseObj(childCourseGroups.get(childCount),courseGroupIdSet);
			}
		}
		return courseGroupIdSet;
	}

	/*
	 *  getters & setters
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
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

}