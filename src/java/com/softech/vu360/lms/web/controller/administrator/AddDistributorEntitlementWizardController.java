package com.softech.vu360.lms.web.controller.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddDistributorEntitlementsForm;
import com.softech.vu360.lms.web.controller.model.DistributorEntitlementCourseGroup;
import com.softech.vu360.lms.web.controller.validator.AddDistributorEntitlementValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.TreeNode;

@SuppressWarnings("all")
public class AddDistributorEntitlementWizardController extends AbstractWizardFormController{

	private static final String MANAGE_ENTITLEMENT_SIMPLESEARCH_ACTION = "simpleSearch";
	private static final String MANAGE_ENTITLEMENT_ADVANCEDSEARCH_ACTION = "advancedSearch";
	private static final String MANAGE_ENTITLEMENT_ALLSEARCH_ACTION ="allsearch";

	private static final Logger log = Logger.getLogger(AddDistributorEntitlementWizardController.class.getName());
	private String cancelTemplate = null;
	private String viewDistributorEntitlementTemplate= null;


	private EntitlementService entitlementService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService=null;

	public AddDistributorEntitlementWizardController() {
		super();
		setCommandName("addDistributorEntitlementsForm");
		setCommandClass(AddDistributorEntitlementsForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"administrator/distributor/AddDistributorEntitlementDetails"
				, "administrator/distributor/AddDistributorEntitlementCourseGroup"
				, "administrator/distributor/AddDistributorEntitlementSummery"
		});
	}

	protected int getTargetPage(HttpServletRequest request, Object command,
			Errors errors, int currentPage) {
		AddDistributorEntitlementsForm form = (AddDistributorEntitlementsForm)command;
		//	HttpSession session = request.getSession();
		if ( currentPage == 1  ) {
			if(request.getParameter("action")!=null){
				if(request.getParameter("action").equals("search")){
					return 1;

				}
			}
		}
		return super.getTargetPage(request, command, errors, currentPage);
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		AddDistributorEntitlementsForm addDistributorEntitlementsForm = (AddDistributorEntitlementsForm)
			command;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Distributor distributor = null;
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			if( details.getCurrentDistributor() != null ){
				distributor=new Distributor();
				distributor = details.getCurrentDistributor();
				addDistributorEntitlementsForm.setDistributor(distributor);
			}else{
				// TODO ------return statement if distributor is not available  
			}
		} else {
			return new ModelAndView(cancelTemplate);
		}

		try{
			/*List<CourseGroup> courseGroupList = null;
			List<DistributorEntitlementCourseGroup> entilementcourseGroupList = 
			new ArrayList<DistributorEntitlementCourseGroup>();
			courseGroupList = entitlementService.findCourseGroups("");
			if(courseGroupList!=null){
				for(CourseGroup courseGroup : courseGroupList) {
					DistributorEntitlementCourseGroup distributorEntitlementCourseGroup = 
					new DistributorEntitlementCourseGroup();
					distributorEntitlementCourseGroup.setCourseGroup(courseGroup);
					distributorEntitlementCourseGroup.setSelected(false);
					entilementcourseGroupList.add(distributorEntitlementCourseGroup);
				}
			}
			addDistributorEntitlementsForm.setSelectedCourseGroups(entilementcourseGroupList);*/

		}catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}
	
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page)
		throws Exception {

		if( page == 1 ) {
			
			if(request.getParameter("action")!=null){
				if(request.getParameter("action").equals("search")){
					AddDistributorEntitlementsForm addDistributorEntitlementsForm = 
						(AddDistributorEntitlementsForm)command;
					List<CourseGroup> courseGroupList = null;
					List<DistributorEntitlementCourseGroup> entilementcourseGroupList = 
						new ArrayList<DistributorEntitlementCourseGroup>();
					String searchType = addDistributorEntitlementsForm.getCourseGroupSearchType();
					if (StringUtils.isNotBlank(searchType)) {
						if(searchType.equalsIgnoreCase(MANAGE_ENTITLEMENT_SIMPLESEARCH_ACTION)){
							courseGroupList = entitlementService.
							findCourseGroups(addDistributorEntitlementsForm.getCourseGroupSimpleSearchKey());
						} else if (searchType.equalsIgnoreCase(MANAGE_ENTITLEMENT_ALLSEARCH_ACTION)){
							courseGroupList = entitlementService.findCourseGroups("");
						}
						/*else if(searchType.equalsIgnoreCase("advancedsearch")){

						courseGroupList = courseAndCourseGroupServicegetCoursesByEntitlement(customer
								,manageSurveyForm.getSearchCourseName()
								,manageSurveyForm.getSearchCourseID()
								,manageSurveyForm.getSearchKeyword());
					}*/
						if(courseGroupList!=null){
							for(CourseGroup courseGroup : courseGroupList) {
								DistributorEntitlementCourseGroup distributorEntitlementCourseGroup = 
									new DistributorEntitlementCourseGroup();
								distributorEntitlementCourseGroup.setCourseGroup(courseGroup);
								distributorEntitlementCourseGroup.setSelected(false);
								entilementcourseGroupList.add(distributorEntitlementCourseGroup);
							}
						}
						addDistributorEntitlementsForm.setSelectedCourseGroups(entilementcourseGroupList);
					}
				}
			}
		}
		if ( page == 1 ) {
			if(this.getTargetPage(request, page) == 2 && (request.getParameter("action") == null || 
					request.getParameter("action").equalsIgnoreCase(""))){
				AddDistributorEntitlementsForm addDistributorEntitlementsFormDetails = 
					(AddDistributorEntitlementsForm)command;
				AddDistributorEntitlementValidator validator = (AddDistributorEntitlementValidator)
					getValidator();
				validator.validatePage2(addDistributorEntitlementsFormDetails, errors);
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int currentPage) throws Exception {
		super.onBindAndValidate(request, command, errors, currentPage);
	}

	protected ModelAndView cancelCancel(){
		return new ModelAndView(cancelTemplate);
	}


	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		return new ModelAndView(cancelTemplate);
	}


	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {

		//VU360User user = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		DistributorEntitlement distributorEntitlement = new DistributorEntitlement();
		AddDistributorEntitlementsForm addDistributorEntitlementsForm = (AddDistributorEntitlementsForm)
			command;

		distributorEntitlement.setName(addDistributorEntitlementsForm.getEntitlementName());
		distributorEntitlement.setAllowSelfEnrollment(addDistributorEntitlementsForm.isAllowSelfEnrollments());
		distributorEntitlement.setAllowUnlimitedEnrollments(addDistributorEntitlementsForm.isMaxEnrollments());
		/*if(!(addDistributorEntitlementsForm.isMaxEnrollments()))
			distributorEntitlement.setMaxNumberSeats(Integer.parseInt
			(addDistributorEntitlementsForm.getNoOfMaxEnrollments()));*/

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		if(addDistributorEntitlementsForm.getStartDate()!=null){
			Date myDate = formatter.parse(addDistributorEntitlementsForm.getStartDate());

			distributorEntitlement.setStartDate(myDate);
		}else{
			distributorEntitlement.setStartDate(null);
		}
		distributorEntitlement.setDistributor(addDistributorEntitlementsForm.getDistributor());


		if(addDistributorEntitlementsForm.isMaxEnrollments()==false){
			distributorEntitlement.setAllowUnlimitedEnrollments(new Boolean("false"));
			if(StringUtils.isNotBlank(addDistributorEntitlementsForm.getNoOfMaxEnrollments())){
				distributorEntitlement.setMaxNumberSeats(Integer.parseInt
						(addDistributorEntitlementsForm.getNoOfMaxEnrollments()));
			}
		}else if(addDistributorEntitlementsForm.isMaxEnrollments()==true){
			distributorEntitlement.setAllowUnlimitedEnrollments(new Boolean("true"));
			distributorEntitlement.setMaxNumberSeats(0);
		}

		if(addDistributorEntitlementsForm.isTermsOfService()== true){
			//	if(addDistributorEntitlementsForm.getDays()!=null){
			log.debug(addDistributorEntitlementsForm.getDays());
			distributorEntitlement.setEndDate(null);
			if(StringUtils.isNotBlank(addDistributorEntitlementsForm.getDays())){
				if(FieldsValidation.isNumeric(addDistributorEntitlementsForm.getDays())){
					distributorEntitlement.setDefaultTermOfServiceInDays(Integer.parseInt
							(addDistributorEntitlementsForm.getDays()));
				}
			}
			//	}
		}else if(addDistributorEntitlementsForm.isTermsOfService()== false){
			if (!addDistributorEntitlementsForm.getFiexedEndDate().isEmpty()){
				Date myDate = formatter.parse(addDistributorEntitlementsForm.getFiexedEndDate());
				distributorEntitlement.setEndDate(FormUtil.formatToDayEnd(myDate));
				distributorEntitlement.setDefaultTermOfServiceInDays(0);
			}else{
				distributorEntitlement.setEndDate(null);
				distributorEntitlement.setDefaultTermOfServiceInDays(0);
			}
		}

		Set<CourseGroup> courseGroups = new HashSet <CourseGroup>();
		List<DistributorEntitlementCourseGroup> distributorEntitlementCourseGroups = 
			addDistributorEntitlementsForm.getSelectedCourseGroups();
		for(DistributorEntitlementCourseGroup addDistributorEntitlementCourseGroup : 
			addDistributorEntitlementsForm.getSelectedCourseGroups()){
			if(addDistributorEntitlementCourseGroup.isSelected() ){
				courseGroups.add(addDistributorEntitlementCourseGroup.getCourseGroup());
				courseGroups.addAll(addDistributorEntitlementCourseGroup.getCourseGroup().getAllChildrenInHierarchy());

			}
		}
		
/*		if(addDistributorEntitlementsForm.getTransactionAmount() == null || addDistributorEntitlementsForm.getTransactionAmount().equals("")) {
			distributorEntitlement.setTransactionAmount(0.0);
		} else {
			distributorEntitlement.setTransactionAmount(Double.valueOf(addDistributorEntitlementsForm.getTransactionAmount()));
		}
*/		
		Double amt = Double.parseDouble(addDistributorEntitlementsForm.getTransactionAmount());
		distributorEntitlement.setTransactionAmount(amt);
        
                initializeCourseGroups(distributorEntitlement, addDistributorEntitlementsForm);
		entitlementService.saveDistributorEntitlement(distributorEntitlement);

		Map<Object, Object> context = new HashMap<Object, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Distributor distributor = null;
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			if( details.getCurrentDistributor() != null ) {
				distributor = details.getCurrentDistributor();
				List<DistributorEntitlement> listDistributorEntitlements = 
					entitlementService.getAllDistributorEntitlements(distributor);
				context.put("DistributorEntitlementsList", listDistributorEntitlements);
				context.put("selectedDistributor", distributor);
				//	session.setAttribute("selectedDistributor", distributor);
				return new ModelAndView(viewDistributorEntitlementTemplate, "context", context);
			} else {
				return new ModelAndView(cancelTemplate, "isRedirect", "d");
			}
		} else {
			// admin has not selected any distributor
			return new ModelAndView(cancelTemplate, "isRedirect", "d");
		}
		//return new ModelAndView (viewDistributorEntitlementTemplate,"context");
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController
	 * #referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, 
	 * org.springframework.validation.Errors, int)
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) 
		throws Exception {
		
		Map model = new HashMap();
		AddDistributorEntitlementsForm form = (AddDistributorEntitlementsForm)command;

		//VU360User user = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
		Distributor distributor = details.getCurrentDistributor();
		String ss="";
		if(!StringUtils.isBlank(request.getParameter("action"))){
			ss=request.getParameter("action");
		}
		try {
			switch(page){
			case 0:
				break;
			case 1:
				if ( ( !StringUtils.isBlank(request.getParameter("action") )
						&& !form.getCourseSearchType().isEmpty() ) ) {
					/*
					VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().
					getAuthentication().getPrincipal();
					Customer customer = null;
					if (loggedInUser.isLMSAdministrator()) {
						customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
								getAuthentication().getDetails()).getCurrentCustomer();
					}
					else {
						customer = loggedInUser.getLearner().getCustomer();
					}                                                    
					 */
					List<CourseGroup> selectedLearnerCourseGroups = new ArrayList<CourseGroup>();
					//List<DistributorEntitlement> distributorEntitlementsList = 
					//entitlementService.getAllDistributorEntitlements(distributor); 
					List<CourseGroup> courseGroupList = entitlementService.findCourseGroups("");
					Set<CourseGroup> courseGroupSet = new HashSet<CourseGroup>();
					Set<CourseGroup> rootCourseGroupSet = new HashSet<CourseGroup>();

					for (CourseGroup courseGrp : courseGroupList) {
						if(courseGrp.getCourses()!= null && courseGrp.getCourses().size()>0){
							rootCourseGroupSet.add(courseGrp.getRootCourseGroup());
							courseGroupSet.add(courseGrp);
						}
					}

					String[] courseGrp = form.getGroups();
					List<Long> cGroupIdList = new ArrayList<Long>();
					if(courseGrp!=null && courseGrp.length>0){
						for(String cGroupId : courseGrp){
							Long courseGroupId = Long.parseLong(cGroupId);
							cGroupIdList.add(courseGroupId);
						}
					}
					List<List<TreeNode>> courseGroupTreeAsList = new ArrayList<List<TreeNode>>();
					for (CourseGroup rootCourseGroup : rootCourseGroupSet) {
						TreeNode courseGroupRoot = getCourseGroupTree(null, rootCourseGroup,cGroupIdList);
						if ( courseGroupRoot != null ) {
							courseGroupTreeAsList.add(courseGroupRoot.bfs());
						}
					}
					model.put("distributorName", distributor.getName());
					model.put("courseGroupTreeAsList", courseGroupTreeAsList);
				}
				return model;
			case 2:
				if (form.getGroups() != null) {
					List<DistributorEntitlementCourseGroup> entitlementsCourseGroupList = 
						new ArrayList<DistributorEntitlementCourseGroup>();
					for (String id : form.getGroups()) {
						DistributorEntitlementCourseGroup entitlementsCourseGroup = 
							new DistributorEntitlementCourseGroup();
						CourseGroup cGrp = courseAndCourseGroupService.getCourseGroupById(Long.valueOf(id));
						entitlementsCourseGroup.setCourseGroup(cGrp);
						entitlementsCourseGroup.setSelected(true);
						entitlementsCourseGroupList.add(entitlementsCourseGroup);
					}
					form.setSelectedCourseGroups(entitlementsCourseGroupList);
				}

				break;
			default :
				break;
			}
		}
		catch(Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return super.referenceData(request, command, errors, page);
	}

	private TreeNode getCourseGroupTree(TreeNode parentNode, CourseGroup courseGroup, 
			List<Long> selectedCourseGroups){
		courseGroup = this.courseAndCourseGroupService.getCourseGroupById(courseGroup.getId());
		if( courseGroup != null ) { 
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

	protected void validatePage(Object command, Errors errors, int page) {

		AddDistributorEntitlementsForm addDistributorEntitlementsFormDetails = 
			(AddDistributorEntitlementsForm)command;
		AddDistributorEntitlementValidator validator = (AddDistributorEntitlementValidator) getValidator();
		errors.setNestedPath("");

		switch (page) {

		case 0:
			validator.validatePage1(addDistributorEntitlementsFormDetails, errors);
			break;
		case 1:
			// TODO
			// validator.validatePage2(addDistributorEntitlementsFormDetails, errors);
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}

	/*
	 *  getters & setters
	 */
	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
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

	public String getViewDistributorEntitlementTemplate() {
		return viewDistributorEntitlementTemplate;
	}

	public void setViewDistributorEntitlementTemplate(
			String viewDistributorEntitlementTemplate) {
		this.viewDistributorEntitlementTemplate = viewDistributorEntitlementTemplate;
	}

        /**
         * Initializes the distributor entitlement with course groups with course group ids
         * passed in the form.
         * 
         * @param distributorEntitlement
         * @param addDistributorEntitlementsForm 
         */
        private void initializeCourseGroups(DistributorEntitlement distributorEntitlement, 
                AddDistributorEntitlementsForm addDistributorEntitlementsForm) {
            String formInput = addDistributorEntitlementsForm.getCourseGroupIds();
            if( StringUtils.isNotEmpty(formInput)){
                String[] strCourseGroupIds = formInput.split(",");
                
                if(ArrayUtils.isEmpty(strCourseGroupIds) == false){
                    List<CourseGroup> courseGroups = courseAndCourseGroupService.
                        getCourseGroupsByBusinessKeys(strCourseGroupIds);
                    distributorEntitlement.setCourseGroups(courseGroups);
                }
            }
        }
}