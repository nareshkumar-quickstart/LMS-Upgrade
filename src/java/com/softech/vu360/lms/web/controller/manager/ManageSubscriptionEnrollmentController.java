package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.EnrollmentSubscriptionView;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SubscriptionService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.EnrollmentSubscriptionDetailsForm;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.model.SubscriptionItemForm;
import com.softech.vu360.lms.web.controller.validator.AddSubscriptionEnrollmentValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.VU360Branding;

public class ManageSubscriptionEnrollmentController extends AbstractWizardFormController{
	
	private static final Logger log = Logger.getLogger(ManageSubscriptionEnrollmentController.class.getName());
	
	private VU360UserService vu360UserService;
    private LearnerService learnerService;
    private SubscriptionService subscriptionService;
    private EntitlementService entitlementService;
    private EnrollmentService enrollmentService;
    private SynchronousClassService synchronousClassService = null;
    private SurveyService surveyService;
    private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
    private CourseAndCourseGroupService courseCourseGrpService;
    private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
    private VelocityEngine velocityEngine;
    private LearnersToBeMailedService learnersToBeMailedService;
   
    private static final String MANAGE_USER_SORT_FIRSTNAME = "firstName";
    private static final String MANAGE_USER_SORT_LASTNAME = "lastName";
    private static final String MANAGE_USER_SORT_USERNAME = "emailAddress";
    private static final String MANAGE_USER_SORT_ACCOUNTLOCKED = "accountNonLocked";
    private static final String[] SORTABLE_COLUMNS = { MANAGE_USER_SORT_FIRSTNAME, MANAGE_USER_SORT_LASTNAME, MANAGE_USER_SORT_USERNAME,
	    MANAGE_USER_SORT_ACCOUNTLOCKED };
    private static final String ENROLLMENT_METHOD_LEARNER = "Learner";
    private static final int SEARCH_COURSE_PAGE_SIZE = 10;
    private static boolean ACCOUNT_NON_EXPIRED = true;
    private static boolean ACCOUNT_NON_LOCKED = true;
    private static boolean ENABLED = true;
    

    private String closeTemplate = null;
    public ManageSubscriptionEnrollmentController() {
    	super();
    	setCommandName("enrollmentForm");
    	setCommandClass(EnrollmentSubscriptionDetailsForm.class);
    	setSessionForm(true);
    	this.setBindOnNewForm(true);
    	setPages(new String[] { "manager/enrollments/assignSubscribtionEnrollments","manager/enrollments/enrollSelectSubscriptionLearners",
    			"","","manager/enrollments/enrollSelectSubscription","manager/enrollments/assignSubscriptionErollmentbyLearner"});
        }

    protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
    	EnrollmentSubscriptionDetailsForm form = (EnrollmentSubscriptionDetailsForm) command;
    	HttpSession session = request.getSession();
    	
    	int targetPage = super.getTargetPage(request, command, errors, currentPage);
    	
    	return targetPage;
    }
     
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

    	log.debug("IN formBackingObject");
    	
    	Object command = super.formBackingObject(request);
    	
    	return command;
    }
    
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
	log.debug("IN initBinder");
	super.initBinder(request, binder);
    }
    
    protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException binder) throws Exception {
	log.debug("IN onBindOnNewForm");
	super.onBindOnNewForm(request, command, binder);
    }
    
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
	log.debug("IN showForm");
	ModelAndView modelNView = super.showForm(request, response, binder);
	String view = modelNView.getViewName();
	log.debug("OUT showOForm for view = " + view);
	return modelNView;
    }
    
    protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

    	log.debug("IN referenceData");
    	Map<Object, Object> model = new HashMap<Object, Object>();
    	
    	switch (page) {
    	case 0:

    	    Map<Object, Object> enrollmentMethodMap = new LinkedHashMap<Object, Object>();
    	    enrollmentMethodMap.put(ENROLLMENT_METHOD_LEARNER, ENROLLMENT_METHOD_LEARNER);
            model.put("enrollmentMethods", enrollmentMethodMap);
    	    return model;
    	    
    	case 1:

    	    break;
    	    
    	case 2:

    	    break; 
    	    
    	case 3:

    	    break;

    	case 4:

    	    break;
    	    
    	default:

    	    break;
    	}

    	return super.referenceData(request, page);    	    
    	
    }
    
    protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
    	
    	EnrollmentSubscriptionDetailsForm form = (EnrollmentSubscriptionDetailsForm) command;
    	
    	 int sortColumnIndex = form.getSortColumnIndex();
		 String sortBy = (sortColumnIndex >= 0 && sortColumnIndex < SORTABLE_COLUMNS.length) ? SORTABLE_COLUMNS[sortColumnIndex]: MANAGE_USER_SORT_FIRSTNAME;
		 int sortDirection = form.getSortDirection() < 0 ? 0 : (form.getSortDirection() > 1 ? 1 : form.getSortDirection());
		 
		 
    	if ((page == 1 && ((!StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("search") )))  )
    	 {
    		 if (!StringUtils.isBlank(form.getSearchType())) {
    			 
    			 Map<Object, Object> results = new HashMap<Object, Object>();
    			 //VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    			 //VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
    			 com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    			 List<VU360User> userList = null;
    			 List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				 
    			 if (form.getSearchType().equalsIgnoreCase("advancesearch")) {
    				 Integer totalResults = 0;
    				 int pageNo = form.getPageIndex() < 0 ? 0 : form.getPageIndex() / VelocityPagerTool.DEFAULT_PAGE_SIZE;
    				
    				    if (!loggedInUser.isLMSAdministrator() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
    					    if (tempManagedGroups!=null && tempManagedGroups.size() > 0) {

    					         results = learnerService.findActiveLearners(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
    								    loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
    									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
    									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
    									ACCOUNT_NON_EXPIRED , ACCOUNT_NON_LOCKED, ENABLED, pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);

    					    totalResults = (Integer) results.get("recordSize");
    					    userList = (List<VU360User>) results.get("list");
    					     }
    					    
                        } 
    				    else {

    					    results = learnerService.findActiveLearners(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
    								    loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
    									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
    									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
    									ACCOUNT_NON_EXPIRED , ACCOUNT_NON_LOCKED, ENABLED, pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
                            totalResults = (Integer) results.get("recordSize");
    						userList = (List<VU360User>) results.get("list");
    					    }	
    				    Map<String, String> pagerAttributeMap = new HashMap<String, String>();
					    pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					    pagerAttributeMap.put("totalCount", totalResults.toString());
					    request.setAttribute("PagerAttributeMap", pagerAttributeMap);
    			 } else if (form.getSearchType().equalsIgnoreCase("allsearch")) {
    				    // userList = learnerService.findLearner("", loggedInUser);
    				    if (!loggedInUser.isLMSAdministrator() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
    					if (tempManagedGroups!=null && tempManagedGroups.size() > 0) {

    						results = learnerService.findActiveLearners(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
								    loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									ACCOUNT_NON_EXPIRED , ACCOUNT_NON_LOCKED, ENABLED, -1, -1, sortBy, sortDirection);
                       
    					    userList = (List<VU360User>) results.get("list");
    					}

    				    } else {

    				    	results = learnerService.findActiveLearners(form.getSearchFirstName().trim(), form.getSearchLastName().trim(), form.getSearchEmailAddress().trim(), 
								    loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
									loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
									loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
									ACCOUNT_NON_EXPIRED , ACCOUNT_NON_LOCKED, ENABLED, -1, -1, sortBy, sortDirection);
                       userList = (List<VU360User>) results.get("list");
    				    }

    				}
    			 if (userList != null && userList.size() > 0) {
    				    List<LearnerItemForm> learnerList = new ArrayList<LearnerItemForm>();
    				    for (VU360User auser : userList) {
    					LearnerItemForm learnerItem = new LearnerItemForm();
    					learnerItem.setSelected(false);
    					learnerItem.setUser(auser);
    					learnerList.add(learnerItem);
    				    }
    				    for (LearnerItemForm learnerItem : learnerList) {
    					for (LearnerItemForm preSelectedItem : form.getSelectedLearners()) {
    					    if (learnerItem.compareTo(preSelectedItem) == 0) {
    						learnerItem.setSelected(true);
    						break;
    					    }
    					}
    				    }
    				    form.setLearners(learnerList);
    		    }
    		 
    		 
    	   }
    	
    	 }
    	
    	else if (((page == 1)) && (!form.getAction().equalsIgnoreCase("search"))) {
    	    request.setAttribute("newPage", "true");
    	}
    	
    	else if (page == 4   )
    	{
    		if(form.getSubscriptionSearchType().equalsIgnoreCase("advanceSubscriptionSearch"))
				form.setSearchType("advanceSubscriptionSearch");
			else if (form.getSubscriptionSearchType().equalsIgnoreCase("showAll"))
				form.setSearchType("showAll");
    		
    		if(!form.getSearchType().equalsIgnoreCase("allsearch")){
    			Customer customer = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
    			subscriptionSearch(form, customer, request);
    			subscriptionsSearchMove(form);
    			 form.setCourseSearchShowAll("");
    			 
    		}
    		else if (form.getSearchType().equalsIgnoreCase("allsearch")){
    			form.setSubscriptionSearchEnd(form.getEnrollmentSubscriptionViewList().size() - 1);
    	  	    form.setSubscriptionSearchStart(0);
    	  	    form.setCourseSearchShowAll("showAll");
    	  	  
    	  	   // form.setCourseSearchPageNumber(0);
    		}
    		
    	}
    	super.postProcessPage(request, command, errors, page);
    }
    
    void subscriptionsSearchMove(EnrollmentSubscriptionDetailsForm form) {
    	// show all courses is requested
    	if (form.getSubscriptionSearchType().trim().equalsIgnoreCase("move")) {
    	    int total = form.getEnrollmentSubscriptionViewList().size();

    	    if (form.getSubscriptionSearchDirection().trim().equalsIgnoreCase("next")) {
    		int newPageNumber = form.getSubscriptionSearchPageNumber() + 1;
    		if ((newPageNumber * SEARCH_COURSE_PAGE_SIZE) > total) {
    		    log.debug(newPageNumber + " greater than  " + total);
    		    // nochange
    		} else {
    		    int lastPageLimit = (form.getSubscriptionSearchPageNumber() + 2) * SEARCH_COURSE_PAGE_SIZE;
    		    form.setSubscriptionSearchStart(form.getSubscriptionSearchEnd() + 1);
    		    form.setSubscriptionSearchPageNumber(form.getSubscriptionSearchPageNumber() + 1);
    		    if (total >= lastPageLimit) {

    			form.setSubscriptionSearchEnd((lastPageLimit - 1));
    		    } else

    			form.setSubscriptionSearchEnd((total - 1));

    		}

    	    } else if (form.getSubscriptionSearchDirection().trim().equalsIgnoreCase("prev")) {
    		int newPageNumber = form.getSubscriptionSearchPageNumber() - 1;
    		if ((newPageNumber * SEARCH_COURSE_PAGE_SIZE) < 0) {
    		    log.debug(newPageNumber + " greater than  " + total);
    		    // nochange
    		} else {
    		    int lastPageLimit = (form.getSubscriptionSearchPageNumber() - 1) * SEARCH_COURSE_PAGE_SIZE;

    		    if (newPageNumber == 0) {
    			form.setSubscriptionSearchStart(0);
    		    } else if (newPageNumber > 0) {
    			form.setSubscriptionSearchStart(((newPageNumber * SEARCH_COURSE_PAGE_SIZE)));
    		    }

    		    else {
    			form.setSubscriptionSearchStart(0);
    		    }

    		    form.setSubscriptionSearchEnd((((newPageNumber * SEARCH_COURSE_PAGE_SIZE) + (SEARCH_COURSE_PAGE_SIZE - 1))));
    		    form.setSubscriptionSearchPageNumber(form.getSubscriptionSearchPageNumber() - 1);
    		}
    	    }

    	}

    	form.setCourseSearchShowAll("");
    	// form.setCourseSearchType("");
        }
    
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {

    	EnrollmentSubscriptionDetailsForm form = (EnrollmentSubscriptionDetailsForm) command;
    	//AddSubscriptionEnrollmentValidator Subscriptionvalidator = (AddSubscriptionEnrollmentValidator) this.getValidator();
	    
	    int targetPage = this.getTargetPage(request, command, errors, page);

	    if ((page == 1 && !StringUtils.isBlank(form.getAction()) && form.getAction().equalsIgnoreCase("search"))
	    		|| (this.getTargetPage(request, command, errors, 1) == 4) || (this.getTargetPage(request, command, errors, 1) == 0)) {

	    	    List<LearnerItemForm> learnerList = form.getLearners();
	    	    Collections.sort(learnerList);

	    	    List<LearnerItemForm> selectedLearnerList = form.getSelectedLearners();
	    	    Collections.sort(selectedLearnerList);

	    	    List<LearnerItemForm> mergedLearnerList = new ArrayList<LearnerItemForm>();
	    	    int i = 0, j = 0;
	    	    LearnerItemForm item, selecteditem;
	    	    for (; i < learnerList.size() && j < selectedLearnerList.size();) {
	    		item = learnerList.get(i);
	    		selecteditem = selectedLearnerList.get(j);
	    		if (item.compareTo(selecteditem) < 0) {
	    		    i++;
	    		    if (item.isSelected())
	    			mergedLearnerList.add(item);
	    		} else if (item.compareTo(selecteditem) > 0) {
	    		    j++;
	    		    mergedLearnerList.add(selecteditem);
	    		} else {
	    		    if (item.isSelected())
	    			mergedLearnerList.add(item);
	    		    i++;
	    		    j++;
	    		}
	    	    }
	    	    for (; i < learnerList.size(); i++) {
	    		item = learnerList.get(i);
	    		if (item.isSelected())
	    		    mergedLearnerList.add(item);
	    	    }
	    	    for (; j < selectedLearnerList.size(); j++) {
	    		selecteditem = selectedLearnerList.get(j);
	    		mergedLearnerList.add(selecteditem);
	    	    }
	    	    
	    	    
	    	    
	    	    form.setSelectedLearners(mergedLearnerList);
	    	    form.setNumberofLearnerenrolled(mergedLearnerList.size());
	    	}
	    
	    if (targetPage == 5) {
		if(form.getEnrollmentSubscriptionViewList() != null && form.getEnrollmentSubscriptionViewList().size() >0 )
		{
			List<SubscriptionItemForm> subscriptionList = new ArrayList<SubscriptionItemForm>();
			
			for (EnrollmentSubscriptionView enrollsubscriptionview : form.getEnrollmentSubscriptionViewList()) {
				
				SubscriptionItemForm subscriptionItemForm = new SubscriptionItemForm();
				if(enrollsubscriptionview.isSelected())
				{
					subscriptionItemForm.setSubscription(enrollsubscriptionview.getSubscription());
					subscriptionItemForm.setSelected(true);
					subscriptionItemForm.setSeatavailable(subscriptionService.hasseatsavailable(form.getSelectedLearners().size(), enrollsubscriptionview.getSubscription().getCustomerEntitlement().getId(), enrollsubscriptionview.getSubscription().getId()));
					subscriptionList.add(subscriptionItemForm);
				}
				
				form.setSelectedSubscriptions(subscriptionList);
				form.setNumberofSubscriptionsenrolledin(subscriptionList.size());
				if(form.getSubscriptionSearchType().equalsIgnoreCase("advanceSubscriptionSearch"))
					form.setSearchType("advanceSubscriptionSearch");
				else if (form.getSubscriptionSearchType().equalsIgnoreCase("showAll"))
					form.setSearchType("showAll");
			
		   }
	    }
	    
	  }
    }
    
    
    protected void validatePage(Object command, Errors errors, int page, boolean finish) {
    	AddSubscriptionEnrollmentValidator Subscriptionvalidator = (AddSubscriptionEnrollmentValidator) this.getValidator();
    	EnrollmentSubscriptionDetailsForm form = (EnrollmentSubscriptionDetailsForm) command;
    	errors.setNestedPath("");
    	
    	switch (page) {

    	case 0:
    	    break;
    	case 1:
    	    if (!form.getAction().equalsIgnoreCase("search"))
    	    	Subscriptionvalidator.validateSecondPage(form, errors);
    	    break;
    	case 4:
    		if (!form.getAction().equalsIgnoreCase("search"))
    			Subscriptionvalidator.validateThirdPage(form, errors);
    	    break;
    	 
    	default:
    	    break;
    	}
    		errors.setNestedPath("");
    }
    
    
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error)
    	    throws Exception {
    	log.debug("IN processCancel");
    	command = null;
    	return new ModelAndView("redirect:mgr_viewPlanAndEnroll.do");
        }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error)
            throws Exception {
        log.debug("IN processFinish");
        String url = null;
        boolean isenrolled = false;
        List<Learner> lstlearner = new ArrayList<Learner>();
        List <Map<Object, Object>> lstlearnersubscriptionmap = new ArrayList<Map<Object, Object>>();
        //Map<Object, Object> learnersubscriptionmap = new HashMap<Object, Object>();
        List<LearnerEnrollment> lstlearnerenrollment = null;
        List<LearnerEnrollment> lstsubscriptionlearnerenrollment = new ArrayList<LearnerEnrollment>();
        
        int Page = getCurrentPage(request);
        List<Learner> lstmailLearner = new ArrayList<Learner>();
        Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
        com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Map<Object, Object>> lstMap = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> learnersubscriptionmap = new HashMap<Object, Object>();

        if (Page == 7) {
         log.debug("getting page 7");
         
         EnrollmentSubscriptionDetailsForm form = (EnrollmentSubscriptionDetailsForm) command;
         int seatsEnrolled = 0;
         
            for(SubscriptionItemForm  subscriptionItemForm :form.getSelectedSubscriptions())
            {
             // Assign Subscription to VU360User
             seatsEnrolled = 0;
             // Subscription that's been selected
             Subscription selectedSubscription = subscriptionItemForm.getSubscription();
            
             // Learner's that are selected to assign the Subscription
             Set<VU360User> lstvu360User = new HashSet<VU360User>();
             
             for(LearnerItemForm learnerItemForm : form.getSelectedLearners()) {
            	 lstvu360User.add(learnerItemForm.getUser());
            	 seatsEnrolled += 1;
             }
             // Assign Subscription to Learners
             selectedSubscription = subscriptionService.findSubscriptionById(selectedSubscription.getId());
             selectedSubscription.getVu360User().addAll(lstvu360User);
             Subscription subscription = subscriptionService.assignToUsers(selectedSubscription);
             // Update the remaining count of Subscription
             entitlementService.updateSeatsUsed(subscription.getCustomerEntitlement(), null, seatsEnrolled);
            }
            
            for(LearnerItemForm learneritemform : form.getSelectedLearners())
         {
       if(!isalreadypresent(lstmailLearner,learneritemform.getUser().getLearner().getId())) {
        lstmailLearner.add(learneritemform.getUser().getLearner());
       }
            }
             
            for(Learner learner: lstmailLearner)
            {
              log.debug("Leaener Email list Count " + lstmailLearner.size());
              log.debug("Leaener Email " + learner.getVu360User().getFirstName());
             learnersToBeMailedService.SendMailToLearnersForSubscription(learner, form.getSelectedSubscriptions() ,lstlearnerenrollment, url, brander);
             
            }
             
            
         
         
        }
        return new ModelAndView(closeTemplate);
           }
    
    
    private boolean isalreadypresent (List<Learner> lstlearner, Long id)
    {
    	for(Learner learner: lstlearner)
    	{
    		if(learner.getId().equals(id))
    			return true;
    	}
    	return false;
    }
    
    private void subscriptionSearch(EnrollmentSubscriptionDetailsForm form, Customer customer, HttpServletRequest request) {
        	// if course search is requested
        	if (form.getSearchType().trim().equalsIgnoreCase("advanceSubscriptionSearch")) {

        	    int intLimit = 100;
        	    Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
        	    String limit = brander.getBrandElement("lms.resultSet.showAll.Limit");

        	    if (StringUtils.isNumeric(limit.trim()))
        		intLimit = Integer.parseInt(limit);

        	    
        	    String strEntDate = form.getSearchDateLimit();
        	    Date entitlementDate = null;
        	    if (!StringUtils.isBlank(strEntDate))
        		entitlementDate = DateUtil.getDateObject(strEntDate);
        	    
        	    List<EnrollmentSubscriptionView> enrollmentSubscriptionViewList = new ArrayList<EnrollmentSubscriptionView>();
        	    
        	    List<Subscription> lstSubscription = new ArrayList<Subscription>();
        	    
        	    lstSubscription = subscriptionService.searchSubscriptions(form.getSearchSubscriptionName(),customer.getId());
        	   
        	    for(Subscription subscription : lstSubscription)
        	    {
        	    	EnrollmentSubscriptionView enrollmentSubscriptionView = new EnrollmentSubscriptionView();
        	    	enrollmentSubscriptionView.setSubscription(subscription);
        	    	enrollmentSubscriptionView.setSubscriptionName(subscription.getSubscriptionName());
        	    	enrollmentSubscriptionView.setEntitlementId(subscription.getCustomerEntitlement().getId());
        	    	enrollmentSubscriptionView.setEntitlementName(subscription.getSubscriptionName());
        	    	enrollmentSubscriptionView.setSeatsUsed(subscription.getCustomerEntitlement().getNumberSeatsUsed());
        	    	enrollmentSubscriptionView.setMaxseats(subscription.getCustomerEntitlement().getMaxNumberSeats());
        	    	enrollmentSubscriptionView.setSeatsRemaining(subscription.getCustomerEntitlement().getMaxNumberSeats());
        	    	enrollmentSubscriptionViewList.add(enrollmentSubscriptionView);
        	    }

        	    form.setSubscriptionList(lstSubscription);
        	    form.setEnrollmentSubscriptionViewList(enrollmentSubscriptionViewList);
        	    
        	    
        	    
        	    if (form.getEnrollmentSubscriptionViewList().size() <= SEARCH_COURSE_PAGE_SIZE)
        		form.setSubscriptionSearchEnd(form.getEnrollmentSubscriptionViewList().size() - 1);
        	    else
        		form.setSubscriptionSearchEnd((SEARCH_COURSE_PAGE_SIZE - 1));
        	    form.setSubscriptionSearchStart(0);
        	    
        	/*    if (form.getSearchType().equalsIgnoreCase("allsearch"))
        	    {
        	    	form.setCourseSearchEnd(form.getEnrollmentSubscriptionViewList().size() - 1);
        	  	    form.setCourseSearchStart(0);
        	  	    form.setCourseSearchShowAll("showAll");
        	  	   // form.setCourseSearchPageNumber(0);
        	    }
        	 */   
        	    //form.setCourseSearchPageNumber(0);
        	    //form.setCourseSearchShowAll("");
        	    

        	}

            }        
        
    public VU360UserService getVu360UserService() {
	return vu360UserService;
    }

    public void setVu360UserService(VU360UserService vu360UserService) {
	this.vu360UserService = vu360UserService;
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
   
   public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	    }

  public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	    }
  
  public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	    }

 public void setSynchronousClassService(SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	    }
  public LearnersToBeMailedService getLearnersToBeMailedService() {
	return learnersToBeMailedService;
    }

    /**
     * @param learnersToBeMailedService
     *            the learnersToBeMailedService to set
     */
 public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
	this.learnersToBeMailedService = learnersToBeMailedService;
    }
 
 public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	    }

 public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	    }
 
 public void setAsyncTaskExecutorWrapper(AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
	this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
    }
 
 public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
	return asyncTaskExecutorWrapper;
   }
 
 public CourseAndCourseGroupService getCourseCourseGrpService() {
	return courseCourseGrpService;
    }

    public void setCourseCourseGrpService(CourseAndCourseGroupService courseCourseGrpService) {
	this.courseCourseGrpService = courseCourseGrpService;
    }
    
    public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
    	return orgGroupLearnerGroupService;
        }

   public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
    	this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
        }

   public SurveyService getSurveyService() {
	return surveyService;
    }

   public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	    }
   
   public void setCloseTemplate(String closeTemplate) {
	this.closeTemplate = closeTemplate;
   }
   
   public String getCloseTemplate() {
	return closeTemplate;
   }

public SubscriptionService getSubscriptionService() {
	return subscriptionService;
}

public void setSubscriptionService(SubscriptionService subscriptionService) {
	this.subscriptionService = subscriptionService;
}
}
