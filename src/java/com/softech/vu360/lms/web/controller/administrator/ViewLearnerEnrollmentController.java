package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LockedCourse;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.SynchrounousSessionVO;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.AdminLearnerEnrollmentSearch;
import com.softech.vu360.lms.web.controller.model.CourseItem;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEnrollmentForm;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEntitlementItem;
import com.softech.vu360.lms.web.controller.validator.EditEnrollmentValidator;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.AdminLearnerEnrollmentSort;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;


/**
 * @author Parthasarathi
 * @modified by Dyutiman
 */
public class ViewLearnerEnrollmentController extends VU360BaseMultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(ViewLearnerEnrollmentController.class.getName());
	private static final String MANAGE_ENROLLMENT_DEFAULT_ACTION = "default";
	private static final String MANAGE_ENROLLMENT_SIMPLE_SEARCH_ACTION = "simpleSearch";
	private static final String MANAGE_ENROLLMENT_ALL_SEARCH_ACTION = "allSearch";
	private static final String MANAGE_ENROLLMENT_PREVPAGE_DIRECTION = "prev";
	private static final String MANAGE_ENROLLMENT_NEXTPAGE_DIRECTION = "next";
	private static final int MANAGE_ENROLLMENT_PAGE_SIZE = 5;
	private static final String MANAGE_ENROLLMENT_ADVANCED_SEARCH_ACTION = "advanceSearch";
	private static final String MANAGE_ENROLLMENT_SORT_LEARNER_ACTION = "sort";
	private static final int SEARCH_RESULT_PAGE_SIZE = 10;
	private static final String MANAGE_ENROLLMENT_SEARCH_ACTION_NONE = "showNone";

	private String manageLearnersTemplate = null;
	private String showEnrollmentTemplate = null;
	private String showExtendTemplate = null;
	private String showSwapTemplate = null;
	private String redirectTemplate = null;
	private String showSynchronousCourseDetailsTemplate = null;	
	private EnrollmentService enrollmentService= null;
	private VU360UserService vu360UserService;
	private LearnerService learnerService;
	private EntitlementService entitlementService; 
	private CourseAndCourseGroupService courseAndCourseGroupService ;
	private SynchronousClassService synchronousClassService ;
	private StatisticsService statService ;
	@Inject
	private CustomerService customerService;
	
	HttpSession session = null;
	
	public ViewLearnerEnrollmentController() {
		super();
        log.debug("In Contructor");
	}

	public ViewLearnerEnrollmentController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod( NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("NO SUCH REQUEST HANDLING METHOD");
		request.setAttribute("newPage","true");
		return new ModelAndView(redirectTemplate);
	}
	
	public ModelAndView cancelEnrollment(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		return new ModelAndView(redirectTemplate);
	}
	
	public ModelAndView showSearchLearnerPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
         *
		 */
        Map<Object, Object> context = new HashMap<Object, Object>();
		boolean isBlankSearch = false;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Customer customer = null;
        if (user.isAdminMode()) {
            customer = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
        } else {
            customer = customerService.getCustomerById(user.getLearner().getCustomer().getId());
        }
        if(customer == null)
        {
            isBlankSearch = false;
        }
        else
        {
            isBlankSearch = customer.getCustomerPreferences().isBlankSearchEnabled();
        }
        context.put("blankSearch",isBlankSearch);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			
			if(details.getCurrentMode()==VU360UserMode.ROLE_LMSADMINISTRATOR){
				request.getSession(true).setAttribute("feature", "LMS-ADM-0002");
			} else {
				request.getSession(true).setAttribute("feature", "LMS-MGR-0029");
			}
        }

        ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
		enrollForm.reset();

        return new ModelAndView(manageLearnersTemplate, "context", context);
	}

	private boolean isCustomerSet(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
			if(details.getCurrentMode()==VU360UserMode.ROLE_LMSADMINISTRATOR){
				if(details.getCurrentSearchType()!=AdminSearchType.CUSTOMER){
					return false;
				}
				else {
					return true;
				}	
			} else if(details.getCurrentMode() == VU360UserMode.ROLE_TRAININGADMINISTRATOR) {
				return true;
			}
		}
		return false ; 
	}

    private boolean isCustomerBlankSearchEnabled(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
            VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();

            if(details.getCurrentMode()==VU360UserMode.ROLE_LMSADMINISTRATOR){
                if(details.getCurrentSearchType() != AdminSearchType.CUSTOMER){
                    return false;
                }
                else {
                    if(details.getCurrentCustomer().getCustomerPreferences().isBlankSearchEnabled())
                    	return true;
                    else
                        return false;
                }
            } else if(details.getCurrentMode() == VU360UserMode.ROLE_TRAININGADMINISTRATOR) {
                if(details.getCurrentCustomer().getCustomerPreferences().isBlankSearchEnabled())
                    return true;
                else
                    return false;
            }
        }
        return false;
    }
	
	public ModelAndView searchLearner(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {
		Map<Object, Object> context = new HashMap<Object, Object>();

        boolean isBlankSearch = false;

		try {
			// check if customer is selected
			if( isCustomerSet() == false ) {
				context.put("customerSelection", "NOT SET");
				context.put("showAll",false);
				return new ModelAndView(manageLearnersTemplate, "context", context);
			}
            
			//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            com.softech.vu360.lms.vo.Customer customer = null;
            if (loggedInUser.isAdminMode()) {
                customer = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getProxyCustomer();
            } else {
                customer = loggedInUser.getLearner().getCustomer();
            }
            if(customer == null)
            {
                isBlankSearch = false;
            }
            else
            {
                isBlankSearch = customer.getCustomerPreferences().isBlankSearchEnabled();
            }

            context.put("blankSearch",isBlankSearch);

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
			/*
			 * Get logged in user
			 */
			if(searchType.equalsIgnoreCase("simplesearch")){
			}else if(searchType.equalsIgnoreCase("advancedsearch")){
				
				List<VU360User> searchedUsers = new ArrayList<VU360User>();// learnerService.findAllLearner(StringUtils.isBlank(enrollForm.getFirstName())?"":enrollForm.getFirstName(), StringUtils.isBlank(enrollForm.getLastName())?"":enrollForm.getLastName() ,  StringUtils.isBlank(enrollForm.getEmailAddress())?"":enrollForm.getEmailAddress() ,loggedInUser, 0, VelocityPagerTool.DEFAULT_PAGE_SIZE, "", 0);					
				List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				// to stop empty searches 

                if( enrollForm.getFirstName().trim().length() == 0 &&  enrollForm.getLastName().trim().length() == 0 && enrollForm.getEmailAddress().trim().length() ==0 )
					context.put("empty_search_exception", "Exception2");
				else{
					//commented by Faisal A. Siddiqui July 21 09 for new branding
					Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
					
					String showAllLimit = brander.getBrandElement("lms.resultSet.showAll.Limit") ;
					int intShowAllLimit = Integer.parseInt(showAllLimit.trim());
					searchedUsers = learnerService.findAllLearner(StringUtils.isBlank(enrollForm.getFirstName())?"":enrollForm.getFirstName(), StringUtils.isBlank(enrollForm.getLastName())?"":enrollForm.getLastName() ,  StringUtils.isBlank(enrollForm.getEmailAddress())?"":enrollForm.getEmailAddress() ,
							loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
							loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
							loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
							0, VelocityPagerTool.DEFAULT_PAGE_SIZE, "", 0 , intShowAllLimit );
				}

                log.debug("In Search Learner");
                Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());

                String showAllLimit = brander.getBrandElement("lms.resultSet.showAll.Limit") ;
                int intShowAllLimit = Integer.parseInt(showAllLimit.trim());
                searchedUsers = learnerService.findAllLearner(StringUtils.isBlank(enrollForm.getFirstName())?"":enrollForm.getFirstName(), StringUtils.isBlank(enrollForm.getLastName())?"":enrollForm.getLastName() ,  StringUtils.isBlank(enrollForm.getEmailAddress())?"":enrollForm.getEmailAddress() ,
                		loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
						0, VelocityPagerTool.DEFAULT_PAGE_SIZE, "", 0 , intShowAllLimit );
				
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
			return new ModelAndView(manageLearnersTemplate, "context", context);
		}
		
		return new ModelAndView(manageLearnersTemplate, "context", context);
	}

	public ModelAndView showAllApplicationLearners(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		context.put("showAll", showAll);

		// check if customer is selected
		if( isCustomerSet() == false ) {
			context.put("customerSelection", "NOT SET");
			context.put("showAll",false);
			return new ModelAndView(manageLearnersTemplate, "context", context);
		}

		if( session.getAttribute("sortDirection") != null && session.getAttribute("sortColumnIndex") != null ) {
			if (session.getAttribute("sortDirection") instanceof Integer &&  session.getAttribute("sortColumnIndex") instanceof Integer)
			{ 
				int dir = (Integer) session.getAttribute("sortDirection");
				int col = (Integer) session.getAttribute("sortColumnIndex");
				context.put("sortDirection", dir);
				context.put("sortColumnIndex", col);
			}else
			{
				context.put("sortDirection", 0);
				context.put("sortColumnIndex", 0);
			}	
		}

		return new ModelAndView(manageLearnersTemplate, "context", context);		
	}

	public ModelAndView showAllLearners(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {

		try {
			ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
			Map <Object, Object> userMap = new HashMap<Object,Object>();
			Map<Object,Object> results = new HashMap<Object,Object>();
			List<VU360User> userList = new ArrayList<VU360User>();
			List<Map> list = new ArrayList <Map>();
			//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
			Map<Object, Object> context = new HashMap<Object, Object>();
			int sortDirection = enrollForm.getSortDirection()<0 ? 0:enrollForm.getSortDirection(); 
			String sortBy = StringUtils.isBlank(enrollForm.getSortField())? "firstName":enrollForm.getSortField();

			results = learnerService.findAllLearners("",
					loggedInUser.isAdminMode(), loggedInUser.isManagerMode(), loggedInUser.getTrainingAdministrator().getId(), 
					loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
					loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(),
					sortBy,sortDirection);

			userList = (List<VU360User>)results.get("list");
			String totalRecord = (results.get("recordSize")==null)?"0":results.get("recordSize").toString();
			if ( !results.isEmpty() ) {

				for(int userNumber=0; userNumber<userList.size(); userNumber++) {

					userMap = new HashMap<Object, Object>();
					int activeEnrollments = 0;
					VU360User vu360user = userList.get(userNumber);
					
					List<LearnerEnrollment> enrollments = entitlementService.getLearnerEnrollmentsForLearner(vu360user.getLearner());//vu360user.getLearner().getEnrollments();

					for(int enrollmentNum=0; enrollmentNum<enrollments.size(); enrollmentNum++) {
						LearnerEnrollment enrollment = enrollments.get(enrollmentNum);
						if(enrollment.getEnrollmentStatus().equalsIgnoreCase("ACTIVE")) {
							activeEnrollments++;
						}
					}
					userMap.put("user", vu360user);
					userMap.put("activeEnrollments", activeEnrollments);
					list.add(userMap);

				}
			}
			context.put("totalRecord", Integer.parseInt(totalRecord));
			context.put("members", list);
			enrollForm.setLearnerItems(list);
			return new ModelAndView(manageLearnersTemplate, "context", context);
		} catch (Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(manageLearnersTemplate);
	}

	public ModelAndView resetSearch(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {

		try {
			ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
			enrollForm.setEmailAddress("");
			enrollForm.setFirstName("");
			enrollForm.setLastName("");
			enrollForm.setSimpleSearchKey("");
			enrollForm.setPageIndex(0);

		} catch (Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(manageLearnersTemplate);
	}
	
	/* to handle course unlock functionality */
	public ModelAndView courseUnlock(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors)
	{ 
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		try {
			
			if(!errors.hasErrors()){
			
				log.debug("IN expireEnrollment METHOD");
	
				List<ViewLearnerEnrollmentItem> selectedViewLearnerEnrollmentItems= new ArrayList<ViewLearnerEnrollmentItem>();
				List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems=form.getViewLearnerEntitlementItems();
				for (ViewLearnerEntitlementItem viewLearnerEntitlementItem:viewLearnerEntitlementItems){
	
					for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
						if(viewLearnerEnrollmentItem.isSelected()){
							selectedViewLearnerEnrollmentItems.add(viewLearnerEnrollmentItem);
						}
					}
				}
	
				Vector lockedEnrolmentIdArray = new Vector();
				LockedCourse lockedCourse = null ; 
				for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:selectedViewLearnerEnrollmentItems){
					lockedCourse = viewLearnerEnrollmentItem.getLearnerEnrollment().getLastLockedCourse(); // get the last added locked course entry if any
					if(lockedCourse != null) // see if it is null
					{ // if not null
						if( lockedCourse.isCourselocked() ) // is if it is locked 
						{
							lockedEnrolmentIdArray.add(lockedCourse.getId());
						}
					}	
				}
	
				if(lockedEnrolmentIdArray.size() > 0 )
					enrollmentService.setCourseStatus(lockedEnrolmentIdArray,  LearnerEnrollment.UNLOCK);
			
			}
			onBind(request,command,"showEnrollments");
		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView("redirect:/mgr_learnerEnrollments.do?method=showEnrollments&id=" + form.getId());		
	}
	
	public ModelAndView showEnrollments(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {

		ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
		if(errors.hasErrors()){
			return new ModelAndView(showEnrollmentTemplate);
		}
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			String userId = request.getParameter("id");
			VU360User user = vu360UserService.getUserById(Long.parseLong(userId));
			enrollForm.setUserName(user.getName());
			context.put("userName",user.getFirstName() + " "+ user.getLastName());
			return new ModelAndView(showEnrollmentTemplate, "context", context);

		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(showEnrollmentTemplate);
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		
		log.debug("In validate: " + methodName);

		EditEnrollmentValidator validator = (EditEnrollmentValidator)this.getValidator();
		if( methodName.equals("showExtendEnrollment")) {
			validator.validateSelectEnrollment(form, errors);
			checkIfAnySelectionCoursesAreCompleted(form, errors,request,"extends");

		} else if( methodName.equals("dropEnrollment")) {
			validator.validateSelectEnrollment(form, errors);
			validator.validateDropEnrollments(form, errors);
			checkIfAnySelectionCoursesAreCompleted(form, errors,request,"drop");

		} else if( methodName.equals("expireEnrollment")) {
			validator.validateSelectEnrollment(form, errors);
			checkIfAnySelectionCoursesAreCompleted(form, errors,request,"expire");
			checkIfAnySelectionCoursesAreExpired(form, errors, request);
			
			
		} else if( methodName.equals("saveExtendEnrollment")) {
			validator.validateSelectEnrollment(form, errors);
			boolean check = validator.validateEnrollmentCourses(form, errors);
			if( check )
				validator.validateExtendEnrollments(form, errors);
			
		} else if( methodName.equals("swapEnrollment")) {
			validator.validateSwapEnrollment(form, errors);
		} else if(methodName.equals("courseUnlock")) {
			validator.validateUnlockEntollment(form, errors);
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
					
					if(learnerEnrollment.getCustomerEntitlement()!=null){

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

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
        log.debug("onBind");
		if( command instanceof ViewLearnerEnrollmentForm ){
			ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
			if( methodName.equals("showEnrollments")) {
				VU360User user = vu360UserService.getUserById(form.getId());
				Learner learner = user.getLearner();
		
				//Need to enhanch dao
				List<LearnerEnrollment> enrollments =entitlementService.getFreshLearnerEnrollmentsForLearner(learner);// learner.getEnrollments();
		
				form.setViewLearnerEntitlementItems(filterEnrollmets(enrollments));
			}
            if(methodName.equals("showSearchLearnerPage"))
            {
               log.debug("showSearchLearnerPage Method");
               log.debug("CustomerBlankSearchEnabled" + isCustomerBlankSearchEnabled());
            }
		}
	}

	public ModelAndView showExtendEnrollment(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {

		Map<Object, Object> context = new HashMap<Object, Object>();
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		if( errors.hasErrors() ) {
			return new ModelAndView(showEnrollmentTemplate);
		}
		try {
			List<ViewLearnerEntitlementItem> selectedViewLearnerEntitlementItems= new ArrayList<ViewLearnerEntitlementItem>();
			List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems=form.getViewLearnerEntitlementItems();
			for (ViewLearnerEntitlementItem viewLearnerEntitlementItem:viewLearnerEntitlementItems){

				ViewLearnerEntitlementItem selectedViewLearnerEntitlementItem =new ViewLearnerEntitlementItem();

				for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
					if(viewLearnerEnrollmentItem.isSelected()){
						selectedViewLearnerEntitlementItem.addViewLearnerEnrollmentItem(viewLearnerEnrollmentItem);
					}
				}
				selectedViewLearnerEntitlementItems.add(selectedViewLearnerEntitlementItem);
			}
			return new ModelAndView(showExtendTemplate, "context", context);

		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(showExtendTemplate);
	}

	public ModelAndView expireEnrollment(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {
		
		if( errors.hasErrors() ) {
			return new ModelAndView(showEnrollmentTemplate);
		}
		
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		try {
			log.debug("IN expireEnrollment METHOD");
			List<ViewLearnerEnrollmentItem> selectedViewLearnerEnrollmentItems= new ArrayList<ViewLearnerEnrollmentItem>();
			List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems=form.getViewLearnerEntitlementItems();
			for (ViewLearnerEntitlementItem viewLearnerEntitlementItem:viewLearnerEntitlementItems){
				for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
					if(viewLearnerEnrollmentItem.isSelected()){
						selectedViewLearnerEnrollmentItems.add(viewLearnerEnrollmentItem);
					}
				}
			}

			long enrolmentIdArray[] = new long[selectedViewLearnerEnrollmentItems.size()];
			int count=0;
			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:selectedViewLearnerEnrollmentItems){
				enrolmentIdArray[count]=viewLearnerEnrollmentItem.getLearnerEnrollment().getId();
				count++;
			}

			enrollmentService.setEnrollmentStatus(enrolmentIdArray, null, LearnerEnrollment.EXPIRED);
			
			onBind(request,command,"showEnrollments");

		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView("redirect:/mgr_learnerEnrollments.do?method=showEnrollments&id=" + form.getId());
	}

	public ModelAndView dropEnrollment(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {
		if( errors.hasErrors() ) {
			return new ModelAndView(showEnrollmentTemplate);
		}
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		try {
			log.debug("IN dropEnrollment METHOD");
			List<ViewLearnerEnrollmentItem> selectedViewLearnerEnrollmentItems= new ArrayList<ViewLearnerEnrollmentItem>();
			List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems=form.getViewLearnerEntitlementItems();
			for (ViewLearnerEntitlementItem viewLearnerEntitlementItem:viewLearnerEntitlementItems){

				for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
					if(viewLearnerEnrollmentItem.isSelected()){
						selectedViewLearnerEnrollmentItems.add(viewLearnerEnrollmentItem);
					}
				}
			}
			long enrolmentIdArray[] = new long[selectedViewLearnerEnrollmentItems.size()];
			int count=0;
			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:selectedViewLearnerEnrollmentItems){

				enrolmentIdArray[count]=viewLearnerEnrollmentItem.getLearnerEnrollment().getId();
				count++;
			}
			enrollmentService.setEnrollmentStatus(enrolmentIdArray, null, LearnerEnrollment.DROPPED);
			onBind(request,command,"showEnrollments");
		} catch(Exception e) {
			log.error("exception", e);
		}
		 return new ModelAndView("redirect:/mgr_learnerEnrollments.do?method=showEnrollments&id=" + form.getId());
	}

	public ModelAndView saveExtendEnrollment(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		List<String> extendedDateList=new ArrayList<String>();
		if( errors.hasErrors() ) {
			return new ModelAndView(showExtendTemplate);
		}
		try {
			log.debug("IN saveExtendEnrollment METHOD");
			List<ViewLearnerEnrollmentItem> selectedViewLearnerEnrollmentItems= new ArrayList<ViewLearnerEnrollmentItem>();
			List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems=form.getViewLearnerEntitlementItems();
			for (ViewLearnerEntitlementItem viewLearnerEntitlementItem:viewLearnerEntitlementItems){

				for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
					if(viewLearnerEnrollmentItem.isSelected()){
						selectedViewLearnerEnrollmentItems.add(viewLearnerEnrollmentItem);
					}
				}
			}
			long enrolmentIdArray[] = new long[selectedViewLearnerEnrollmentItems.size()];
			int count=0;
			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:selectedViewLearnerEnrollmentItems){

				enrolmentIdArray[count]=viewLearnerEnrollmentItem.getLearnerEnrollment().getId();
				extendedDateList.add(viewLearnerEnrollmentItem.getNewEnrollmentEndDate());
				count++;
			}
			enrollmentService.setEnrollmentStatus(enrolmentIdArray, extendedDateList, LearnerEnrollment.ACTIVE);
			onBind(request,command,"showEnrollments");
			return new ModelAndView("redirect:/mgr_learnerEnrollments.do?method=showEnrollments&id=" + form.getId());

		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(showExtendTemplate);
	}

	// [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing courses irrespective of contract and enrollments availability
	@SuppressWarnings("unchecked")
	public ModelAndView swapEnrollment(HttpServletRequest request,HttpServletResponse response, Object command, BindException errors) {
		log.debug("In swapEnrollment");
		Map<Object, Object> context = new HashMap<Object, Object>();
		int totalRecords = -1, recordShowing = 0, pageIndex = 0, pageSize = SEARCH_RESULT_PAGE_SIZE;
		
		if( errors.hasErrors() ) {
			return new ModelAndView(showEnrollmentTemplate);
		}
		
		try {
			ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
			VU360User user = this.vu360UserService.getUserById(Long.valueOf(form.getId()));
			Customer customer = user.getLearner().getCustomer();
			String swapEnrollmentId = "0";
			
			/* LMS-13940 - 
			 * We can swap only one course at a time. So, I didn't use array for swapEnrollmentId.
			 */
			List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems=form.getViewLearnerEntitlementItems();
			for (ViewLearnerEntitlementItem viewLearnerEntitlementItem:viewLearnerEntitlementItems){

				for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem:viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
					if(viewLearnerEnrollmentItem!=null && viewLearnerEnrollmentItem.isSelected()){
						if(viewLearnerEnrollmentItem.getLearnerEnrollment()!=null)
							swapEnrollmentId = viewLearnerEnrollmentItem.getLearnerEnrollment().getId().toString();
					}
				}
			}
			

			String action = request.getParameter("action");
			String searchCourseName = request.getParameter("searchCourseName");			
			String searchContractName = request.getParameter("searchContractName");
			String searchCourseCode = request.getParameter("searchCourseId");
			pageIndex = StringUtils.isBlank( request.getParameter("pageIndex") ) ? 0 : Integer.valueOf(request.getParameter("pageIndex")) ;	
			pageIndex = (pageIndex <= 0) ? 0 : pageIndex;
			
			if (action.equalsIgnoreCase(MANAGE_ENROLLMENT_SEARCH_ACTION_NONE)) {
				request.setAttribute("newPage", true);
			}
			else {				
				if ( action.equalsIgnoreCase(MANAGE_ENROLLMENT_ALL_SEARCH_ACTION) ) {					
					pageSize = -1;
					pageIndex = 0;				
				}
				else if ( action.equalsIgnoreCase(MANAGE_ENROLLMENT_PREVPAGE_DIRECTION) ) {
					pageIndex = (pageIndex <= 0) ? 0 : (pageIndex - 1);				
				}
				else if ( action.equals(MANAGE_ENROLLMENT_NEXTPAGE_DIRECTION) ) {
					pageIndex = pageIndex < 0 ? 0 : (pageIndex + 1);			
				}
				
				Map<Object, Object> resultSet = this.entitlementService.getCoursesForEnrollment(customer.getId(), searchCourseName, searchCourseCode, searchContractName, pageIndex, pageSize);
				List<EnrollmentCourseView> enrollmentList = (List<EnrollmentCourseView>) resultSet.get("enrollmentList");
				totalRecords = Integer.valueOf( resultSet.get("totalRecords").toString() );
				pageSize = (pageSize == -1) ? totalRecords : pageSize;
				
				if ( action.equalsIgnoreCase(MANAGE_ENROLLMENT_ALL_SEARCH_ACTION) ) {
					recordShowing = enrollmentList.size();
				}
				else {
					recordShowing = enrollmentList.size() < SEARCH_RESULT_PAGE_SIZE ? totalRecords : (pageIndex + 1) * SEARCH_RESULT_PAGE_SIZE;
				}
				form.setEnrollmentList(enrollmentList);
			}			
			
			context.put("swapEnrollmentId", swapEnrollmentId);
			context.put("searchCourseName", searchCourseName);
			context.put("searchContractName", searchContractName);
			context.put("searchCourseCode", searchCourseCode);			
		}
		catch (Exception e) {
			log.debug(e);
		}
		
		context.put("totalRecords", totalRecords);
		context.put("recordShowing", recordShowing);
		context.put("pageIndex", pageIndex);
		
		return new ModelAndView(showSwapTemplate, "context", context);
	}
	
	private List<Map<Object,Object>> findSynchronousCourses(  List<CourseItem> list) {
		Map<Object ,Object> synchronousClassDetails = new HashMap<Object ,Object>();
		Map<Object ,Object> synchClassSessionList = new HashMap<Object ,Object>();
		Map<Object ,Object> synchSessionDateList = new HashMap<Object ,Object>();
		List<Map<Object ,Object>> synchClassList = new ArrayList<Map<Object ,Object>>();
		List<SynchronousClass> classList = null;
		String courseTitle = null;
		
		for ( CourseItem courseItem : list) {
			classList = null;
			classList = synchronousClassService.getSynchronousClassByCourseId(courseItem.getCourse().getId());
			if( classList.size() > 0 ) {
				synchronousClassDetails = new HashMap<Object ,Object>();
				courseTitle = courseItem.getCourse().getCourseTitle();
				synchClassSessionList = new HashMap<Object ,Object>();
				SynchrounousSessionVO synchSessionVO = null;
				for( SynchronousClass sc : classList  ) {
					synchSessionDateList = new HashMap<Object ,Object>();
					synchSessionVO = synchronousClassService.getMinMaxScheduleDateForSynchronousClass(sc.getId()) ;
					Date dt1 = synchSessionVO.getMinStartDateTime();
					Date dt2 = synchSessionVO.getMaxEndDateTime();
					synchSessionDateList.put("minStartScheduleDate", dt1);
					synchSessionDateList.put("maxEndScheduleDate", dt2);
					synchClassSessionList.put(sc.getId(), synchSessionDateList);
					log.debug("synch end date "+ synchSessionVO.getMaxEndDateTime());
					synchSessionVO = null;
					synchSessionDateList =null;
					
				} 
				synchronousClassDetails.put("timings", synchClassSessionList);
				synchronousClassDetails.put("list", classList);
				synchronousClassDetails.put("courseTitle", courseTitle);
				synchClassList.add(synchronousClassDetails);
			}	
		}
		// sending course id of courses typr synchronous course
		
		return synchClassList ;
	}
	public ModelAndView swapSearchCourse(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		
		try {
			String searchType = form.getCourseSearchType();
			Long userId = form.getId();
			VU360User user = vu360UserService.getUserById(userId);
			Learner learner = user.getLearner();
			List<LearnerEnrollment> learnerEnrollmentsForThisLearner = entitlementService.getLearnerEnrollmentsForLearner(learner);
			
			List<LearnerEnrollment> learnerEnrollments =new ArrayList<LearnerEnrollment>();
			for(LearnerEnrollment learnerEnrollment: learnerEnrollmentsForThisLearner){
				if(learnerEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.ACTIVE)
						|| learnerEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.DROPPED)){
					learnerEnrollments.add(learnerEnrollment);
				}
			}
			
			long courseIdArray[]=null;
			if(learnerEnrollments.size()>0)
				courseIdArray= new long[learnerEnrollments.size()];
			else{
				courseIdArray=new long[1];
				courseIdArray[0]=0;
			}
			List<CourseItem> list = new ArrayList <CourseItem>();
			List<Course> courseList = new ArrayList <Course>();
			
			/*
			 * Get logged in user
			 */
			for (int count=0;count<learnerEnrollments.size();count++) {
				if(learnerEnrollments.get(count).getCourse() != null) {
					courseIdArray[count]=(learnerEnrollments.get(count).getCourse().getId());
				}
			}
			String courseName = form.getCourseName()== null ? "" : form.getCourseName().trim();
			String courseId = form.getCourseId()== null ? "" : form.getCourseId().trim();
			String courseKeyword = form.getCourseKeyword()== null ? "" : form.getCourseKeyword().trim();
			String courseSearchKey = form.getCourseSearchKey()== null ? "" : form.getCourseSearchKey().trim();
			if(searchType.equalsIgnoreCase("simplesearch")){
				courseList = entitlementService.getAllCoursesByEntitlement(learner.getCustomer().getId());
				long[] l_courseIdArray = new long[courseList.size()]; 
				int count=0;
				for( Course course : courseList ) {
					l_courseIdArray[count] = course.getId();
					count = count + 1;
				}
				courseList = entitlementService.findCoursesByCustomer(l_courseIdArray
						,courseName,courseId,courseKeyword,courseSearchKey);

			}else if(searchType.equalsIgnoreCase("advancedsearch")){

				courseList = entitlementService.getAllCoursesByEntitlement(learner.getCustomer().getId());
				
				long[] l_courseIdArray = new long[courseList.size()]; 
				int count=0;
				for( Course course : courseList ) {
					l_courseIdArray[count] = course.getId();
					count = count + 1;
				}
				courseList = entitlementService.findCoursesByCustomer(l_courseIdArray
						,courseName,courseId,courseKeyword,courseSearchKey);
			}
				for(Course c : courseList) {
					CourseItem item = new CourseItem();
					if(searchType.equalsIgnoreCase("simplesearch")){
						if(c.getCourseTitle().toLowerCase().contains(form.getCourseSearchKey().trim().toLowerCase())){
							if(request.getSession().getAttribute("selectedCourseId")!=null){
								if(!request.getSession().getAttribute("selectedCourseId").equals(c.getId().toString())){
								item.setCourse(c);
								item.setSelected(false);
								list.add(item);
								}
							}
						}
					}else if(searchType.equalsIgnoreCase("advancedsearch")){
						List<String> searchKeys=new ArrayList<String>();
						if(!StringUtils.isBlank(courseName)){
							searchKeys.add(courseName);
						}
						if(!StringUtils.isBlank(courseId)){
							searchKeys.add(courseId);
						}
						if(!StringUtils.isBlank(courseKeyword)){
							searchKeys.add(courseKeyword);
						}
						if (searchKeys.size()>0){
							for(String searchKey:searchKeys){
								if(c.getCourseTitle().toLowerCase().contains(searchKey.toLowerCase())){
									if(request.getSession().getAttribute("selectedCourseId")!=null){
										if(!request.getSession().getAttribute("selectedCourseId").toString().equals(c.getId().toString())){
											item.setCourse(c);
											item.setSelected(false);
											list.add(item);
											break;
										}
									}
								}
							}
							
						}else{
							if(request.getSession().getAttribute("selectedCourseId")!=null){
								if(!request.getSession().getAttribute("selectedCourseId").toString().equals(c.getId().toString())){
									item.setCourse(c);
									item.setSelected(false);
									list.add(item);
								}
							}
						}
						
					}
				}
			
			String totalRecord = list.size()+"";
			
			Map<String,String> pagerAttributeMap = new HashMap<String,String>();
			pagerAttributeMap.put("pageIndex", Integer.toString(form.getCoursePageIndex()));
			pagerAttributeMap.put("totalCount", totalRecord.toString());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			
			context.put("totalRecord", Integer.parseInt(totalRecord));
			context.put("members", list);
			form.setCourseItems(list);
			List<Map<Object,Object>> synchronousCourses = new ArrayList<Map<Object,Object>>();
			
			synchronousCourses = findSynchronousCourses(  list ) ;
			
			context.put( "synchronousCourses", synchronousCourses);
			return new ModelAndView(showSwapTemplate, "context", context);

		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(showSwapTemplate);
	}

	public ModelAndView showAllCourses(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		Long userId = form.getId();
		VU360User user = vu360UserService.getUserById(userId);
		Learner learner = user.getLearner();
		List<LearnerEnrollment> learnerEnrollmentsForThisLearner = entitlementService.getLearnerEnrollmentsForLearner(learner);
		List<LearnerEnrollment> learnerEnrollments =new ArrayList<LearnerEnrollment>();
		for(LearnerEnrollment learnerEnrollment: learnerEnrollmentsForThisLearner){
			if(learnerEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.ACTIVE)
					|| learnerEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.DROPPED)){
				learnerEnrollments.add(learnerEnrollment);
			}
		}
		long courseIdArray[]= new long[learnerEnrollments.size()];
		List<CourseItem> list = new ArrayList <CourseItem>();
		List<Course> courseList = new ArrayList <Course>();
		for (int count=0;count<learnerEnrollments.size();count++) {
			if(learnerEnrollments.get(count).getCourse() != null) {
				courseIdArray[count]=(learnerEnrollments.get(count).getCourse().getId());
			}
		}

		try {
			courseList = entitlementService.getAllCoursesByEntitlement(learner.getCustomer().getId());
				for(Course c : courseList) {
					CourseItem item = new CourseItem();
					if(request.getSession().getAttribute("selectedCourseId")!=null){
						if(!request.getSession().getAttribute("selectedCourseId").toString().equals(c.getId().toString())){
								item.setCourse(c);
								item.setSelected(false);
								list.add(item);
							}
						}
				}
			
			String totalRecord = list.size()+"";
			
			context.put("totalRecord", Integer.parseInt(totalRecord));
			context.put("members", list);
			form.setCourseItems(list);
			
			List<Map<Object,Object>> synchronousCourses = new ArrayList<Map<Object,Object>>();
			
			synchronousCourses = findSynchronousCourses(  list ) ;
			
			context.put( "synchronousCourses", synchronousCourses);
			return new ModelAndView(showSwapTemplate, "context", context);
			
		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(showSwapTemplate);
	}
	
	public ModelAndView resetCourseSearch(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {

		try {
			ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
			enrollForm.setCourseId("");
			enrollForm.setCourseKeyword("");
			enrollForm.setCourseName("");
			enrollForm.setCourseSearchKey("");
			enrollForm.setCourseSearchType("");
			enrollForm.setCoursePageIndex(0);

		} catch (Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(manageLearnersTemplate);
	}
	
	// [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing courses irrespective of contract and enrollments availability
	public ModelAndView swapCourse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) {
		log.debug("In swapCourse");
		if( errors.hasErrors() ) {
			return new ModelAndView(showEnrollmentTemplate);
		}
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		try {
			VU360User user = this.vu360UserService.getUserById(Long.valueOf(form.getId()));

			Long swapEnrollmentId = Long.valueOf( request.getParameter("swapEnrollmentId") );
			Long courseId = null;
			Long customerContractId = null;			
			SynchronousClass synchronousClass = null;
			
			for (EnrollmentCourseView enrollment : form.getEnrollmentList()) {
				if (enrollment.getSelected()) {
					
					courseId = Long.valueOf(enrollment.getCourseId());
					customerContractId = Long.valueOf(enrollment.getEntitlementId());
					
					if (enrollment.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE)
							|| enrollment.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
						Long selClassId = StringUtils.isNotBlank( request.getParameter("selectedClass") ) ? Long.valueOf(request.getParameter("selectedClass")) : 0L;
						
						for (SynchronousClass curClass : enrollment.getSyncClasses()) {
							if (curClass.getId().equals(selClassId)) {
								synchronousClass = curClass;
								break;
							}
						}
					}					
					break;
				}
			}
			
			String error = this.enrollmentService.swapCourse(user.getLearner(), swapEnrollmentId, courseId, customerContractId, synchronousClass);
			if (StringUtils.isNotBlank(error)) {
				errors.rejectValue("courseItems", error);			
			}
			
			onBind(request,command,"showEnrollments");
		}	
		catch (Exception e) {
			log.debug(e);
		}
		
		return new ModelAndView("redirect:/mgr_learnerEnrollments.do?method=showEnrollments&id=" + form.getId());
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView showSwapEnrollment(HttpServletRequest request,
			HttpServletResponse response) {

		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> results = new HashMap<Object, Object>();
		String action = request.getParameter("action");
		action = (action==null)?MANAGE_ENROLLMENT_DEFAULT_ACTION:action;
		String userId = request.getParameter("Id");
		String swappedId = request.getParameter("swappedId");

		String courseName = request.getParameter("courseName");
		String courseKeyword = request.getParameter("courseKeyword");
		String courseId =request.getParameter("courseId");
		String searchKey = request.getParameter("searchkey");
		String direction=request.getParameter("direction");
		String pageIndex=request.getParameter("pageIndex");
		String sortDirection=request.getParameter("sortDirection");

		int pageNo=0;
		int recordShowing=0;

		try {		
			VU360User user = vu360UserService.getUserById(Long.valueOf(userId));
			Learner learner =user.getLearner();// learnerService.getLearnerByID(user.getLearner().getId());

			//////////////////////////////////////////////////////////////
			action = (action==null)?MANAGE_ENROLLMENT_DEFAULT_ACTION:action;
			courseName = (courseName  == null)?"":courseName;
			courseKeyword = (courseKeyword == null)?"":courseKeyword;
			courseId = (courseId == null)?"":courseId;
			searchKey = (searchKey==null)?"":searchKey;
			HttpSession session = request.getSession();

			if (sortDirection == null){

				session.setAttribute("searchedCourseName", courseName);
				session.setAttribute("searchedCourseKeyword", courseKeyword);
				session.setAttribute("searchedCourseId", courseId);
				session.setAttribute("searchedSearchKey", searchKey);
				session.setAttribute("pageNo",pageNo);

			}
			direction = (direction==null)?MANAGE_ENROLLMENT_PREVPAGE_DIRECTION:direction;
			pageIndex = (pageIndex==null)?"0":pageIndex;
			sortDirection = (sortDirection==null)?"0":sortDirection;
			List<LearnerEnrollment> learnerEnrollmentsForThisLearner = entitlementService.getLearnerEnrollmentsForLearner(learner);
			long courseIdArray[]=new long[learnerEnrollmentsForThisLearner.size()];		


			for (int count=0;count<learnerEnrollmentsForThisLearner.size();count++)
			{
				courseIdArray[count]=(Long.valueOf(learnerEnrollmentsForThisLearner.get(count).getCourse().getId()));
			}

			if(action.equalsIgnoreCase(MANAGE_ENROLLMENT_SIMPLE_SEARCH_ACTION)){

				log.debug("IN SIMPLE SEARCH");
				session.setAttribute("searchType", MANAGE_ENROLLMENT_SIMPLE_SEARCH_ACTION);

				if(direction.equalsIgnoreCase(MANAGE_ENROLLMENT_PREVPAGE_DIRECTION)){

					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;
					log.debug("pageNo = " + pageNo);

				}else if(direction.equalsIgnoreCase(MANAGE_ENROLLMENT_NEXTPAGE_DIRECTION)){

					pageIndex=request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
					log.debug("page no " + pageNo);

				}
				session.setAttribute("pageNo",pageNo);
				results=enrollmentService.getAllCoursesNotEnrolledByLearner(courseIdArray,learner.getCustomer(),session.getAttribute("searchedSearchKey").toString(),
						pageNo, MANAGE_ENROLLMENT_PAGE_SIZE, "", Integer.parseInt(sortDirection));

			}else if(action.equalsIgnoreCase(MANAGE_ENROLLMENT_ADVANCED_SEARCH_ACTION)){

				session.setAttribute("searchType", MANAGE_ENROLLMENT_ADVANCED_SEARCH_ACTION);
				if(direction.equalsIgnoreCase(MANAGE_ENROLLMENT_PREVPAGE_DIRECTION)){

					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo <= 0)?0:pageNo-1;
					log.debug("pageNo = " + pageNo);

				}else if(direction.equalsIgnoreCase(MANAGE_ENROLLMENT_NEXTPAGE_DIRECTION)){

					pageIndex=request.getParameter("pageIndex");
					pageNo = (pageIndex.isEmpty())?0:Integer.parseInt(pageIndex);
					pageNo = (pageNo<0)?0:pageNo+1;
					log.debug("page no " + pageNo);

				}
				session.setAttribute("pageNo",pageNo);	
				results=enrollmentService.getAllCoursesNotEnrolledByLearner(courseIdArray,learner.getCustomer(), session.getAttribute("searchedCourseName").toString(),
						session.getAttribute("searchedCourseId").toString(),session.getAttribute("searchedCourseKeyword").toString(),
						pageNo, MANAGE_ENROLLMENT_PAGE_SIZE, "", Integer.parseInt(sortDirection));

			}else if(action.equalsIgnoreCase(MANAGE_ENROLLMENT_ALL_SEARCH_ACTION)){
				session.setAttribute("searchType", MANAGE_ENROLLMENT_ALL_SEARCH_ACTION);
				log.debug("searchType " + session.getAttribute("searchType" ));
				pageNo=0;
				session.setAttribute("pageNo",pageNo);	
				results=enrollmentService.getAllCoursesNotEnrolledByLearner(courseIdArray,learner.getCustomer());

			}else if(action.equalsIgnoreCase(MANAGE_ENROLLMENT_SORT_LEARNER_ACTION)){
				if (session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_ENROLLMENT_ADVANCED_SEARCH_ACTION)){
					results=enrollmentService.getAllCoursesNotEnrolledByLearner(courseIdArray,learner.getCustomer() ,session.getAttribute("searchedCourseName").toString(),
							session.getAttribute("searchedCourseId").toString(),session.getAttribute("searchedCourseKeyword").toString(),
							Integer.parseInt(session.getAttribute("pageNo").toString()), MANAGE_ENROLLMENT_PAGE_SIZE, "", Integer.parseInt(sortDirection));
				}
				if (session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_ENROLLMENT_ALL_SEARCH_ACTION)){

					results=enrollmentService.getAllCoursesNotEnrolledByLearner(courseIdArray,learner.getCustomer());

				} else {

					results=enrollmentService.getAllCoursesNotEnrolledByLearner(courseIdArray,learner.getCustomer(),session.getAttribute("searchedSearchKey").toString(),
							Integer.parseInt(session.getAttribute("pageNo").toString()), MANAGE_ENROLLMENT_PAGE_SIZE, "", Integer.parseInt(sortDirection));

				}
			}
			else{
				session.setAttribute("searchType","");
				sortDirection="0";
			}
			List<CustomerEntitlement> customerEntitlementList=null;

			if (!results.isEmpty())
			{
				customerEntitlementList=(List<CustomerEntitlement> )results.get("list");
				recordShowing = ((Integer)customerEntitlementList.size()<MANAGE_ENROLLMENT_PAGE_SIZE)
				?Integer.parseInt(results.get("recordSize").toString())
						:(Integer.parseInt(session.getAttribute("pageNo").toString())+1)*MANAGE_ENROLLMENT_PAGE_SIZE;	
			}
			if (sortDirection.equals("0"))
				sortDirection = "1";
			else
				sortDirection="0";
			if (session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_ENROLLMENT_ALL_SEARCH_ACTION))
				recordShowing=customerEntitlementList.size();

			String totalRecord=	(results.get("recordSize")==null)?"0":results.get("recordSize").toString();
			context.put("courseName", session.getAttribute("searchedCourseName"));
			context.put("courseId", session.getAttribute("searchedCourseId"));
			context.put("courseKeyword", session.getAttribute("searchedCourseKeyword"));
			context.put("searchKey", session.getAttribute("searchedSearchKey"));
			context.put("userId", userId);

			context.put("customerEntitlements", customerEntitlementList);
			context.put("totalRecord", Integer.parseInt(totalRecord));
			context.put("recordShowing", recordShowing);
			context.put("pageNo", session.getAttribute("pageNo"));
			context.put("sortDirection", sortDirection );
			context.put("searchType", session.getAttribute("searchType"));
			context.put("direction", direction);
			context.put("swappedId",swappedId);
			return new ModelAndView(showSwapTemplate, "context", context);

		} catch(Exception e) {

			log.error("exception", e);
		}
		return new ModelAndView(showSwapTemplate);
	}
	
	public ModelAndView showSynchronousClassDetails( HttpServletRequest request , HttpServletResponse response){
		String courseId = request.getParameter("id");
		Long intCourseId = 0L ;
		if (StringUtils.isNumeric(courseId.trim() ) ) {
			try{
				intCourseId = Long.valueOf( courseId );
			}catch(Exception e ) {
				log.debug("error while parsing course id :" + courseId);
			}
		}
		List<SynchronousClass> synchClassList = synchronousClassService.getSynchronousClassByCourseId(intCourseId) ;
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		if(synchClassList == null) {
			return null;
		}
		
		List<SynchronousSession> synchronousSessionList = null ;
		Map<Object , Object> classDetails = null ;
		
		List<Map<Object , Object>> classDetailsList = new ArrayList<Map<Object , Object>>();
		
		for( SynchronousClass synchClass : synchClassList  ) {
			synchronousSessionList = null ;
			classDetails = null;
			classDetails = new HashMap<Object , Object>() ;
			synchronousSessionList = synchronousClassService.getSynchronousSessionsByClassId( synchClass.getId() );
			
			classDetails.put("section", synchClass);
			classDetails.put("object", synchronousSessionList);
			
			classDetailsList.add(classDetails);
		}
		
		context.put("classDetailsList", classDetailsList);
		return new ModelAndView(showSynchronousCourseDetailsTemplate, "context", context); 
		
	}
		
	/* ========================================================================== */
	public void checkIfAnySelectionCoursesAreCompleted(ViewLearnerEnrollmentForm viewLearnerEnrollmentForm,Errors errors ,HttpServletRequest request,String compareReason){

		//boolean anySelected = false;
		//errors.rejectValue("courseItems", "for test only","" );
		List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems = viewLearnerEnrollmentForm.getViewLearnerEntitlementItems();
		for (ViewLearnerEntitlementItem viewLearnerEntitlementItem : viewLearnerEntitlementItems){

			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem : viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
				if(viewLearnerEnrollmentItem.isSelected() ){
					LearnerCourseStatistics lcs = statService.getLearnerCourseStatisticsForLearnerEnrollment(viewLearnerEnrollmentItem.getLearnerEnrollment());
					if( lcs != null ){
						if( lcs.isCourseCompleted() ){ 
						//anySelected = true;
							Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
							if( viewLearnerEnrollmentItem.getLearnerEnrollment().getEnrollmentStatus().equalsIgnoreCase(viewLearnerEnrollmentItem.getLearnerEnrollment().EXPIRED))
								errors.rejectValue("courseItems", viewLearnerEnrollmentItem.getLearnerEnrollment().getCourse().getCourseTitle() + brander.getBrandElement("lms.enrollment.showEnrollments."+lcs.getStatus().toLowerCase()+"CourseCannotExpire.error") );
							else if(compareReason.equalsIgnoreCase("drop"))
								errors.rejectValue("courseItems", viewLearnerEnrollmentItem.getLearnerEnrollment().getCourse().getCourseTitle() + brander.getBrandElement("lms.enrollment.showEnrollments."+lcs.getStatus().toLowerCase()+"CourseCannotDrop.error") );
							else if(compareReason.equalsIgnoreCase("completed"))
								errors.rejectValue("courseItems", viewLearnerEnrollmentItem.getLearnerEnrollment().getCourse().getCourseTitle() + brander.getBrandElement("lms.enrollment.showEnrollments."+lcs.getStatus().toLowerCase()+"CourseCannotExtends.error") );
						}	
					}
				}
			}
		}
	}		
/* ============================================================================= */
	
	@SuppressWarnings("static-access")
	public void checkIfAnySelectionCoursesAreExpired(ViewLearnerEnrollmentForm viewLearnerEnrollmentForm, Errors errors, HttpServletRequest request) {
		//errors.rejectValue("courseItems", "for test only","" );
		List<ViewLearnerEntitlementItem> viewLearnerEntitlementItems = viewLearnerEnrollmentForm.getViewLearnerEntitlementItems();
		for (ViewLearnerEntitlementItem viewLearnerEntitlementItem : viewLearnerEntitlementItems){

			for (ViewLearnerEnrollmentItem viewLearnerEnrollmentItem : viewLearnerEntitlementItem.getViewLearnerEnrollmentItems()){
				if(viewLearnerEnrollmentItem.isSelected() ){
					if( viewLearnerEnrollmentItem.getLearnerEnrollment().getEnrollmentStatus().equalsIgnoreCase(viewLearnerEnrollmentItem.getLearnerEnrollment().EXPIRED) ) 
							errors.rejectValue("courseItems", "Course(s) Already Expired");
					else if(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().getStatus().equalsIgnoreCase(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().REPORTED))
						errors.rejectValue("courseItems", "Reported Course(s) Cannot Expire");
					else if(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().getStatus().equalsIgnoreCase(viewLearnerEnrollmentItem.getLearnerEnrollment().getCourseStatistics().COMPLETED))
						errors.rejectValue("courseItems", "Completed Course(s) Cannot Expire");
				}
			}
		}
	}
	
	public String getManageLearnersTemplate() {
		return manageLearnersTemplate;
	}

	public void setManageLearnersTemplate(String manageLearnersTemplate) {
		this.manageLearnersTemplate = manageLearnersTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public String getShowEnrollmentTemplate() {
		return showEnrollmentTemplate;
	}

	public void setShowEnrollmentTemplate(String showEnrollmentTemplate) {
		this.showEnrollmentTemplate = showEnrollmentTemplate;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public String getShowExtendTemplate() {
		return showExtendTemplate;
	}

	public void setShowExtendTemplate(String showExtendTemplate) {
		this.showExtendTemplate = showExtendTemplate;
	}

	public String getShowSwapTemplate() {
		return showSwapTemplate;
	}

	public void setShowSwapTemplate(String showSwapTemplate) {
		this.showSwapTemplate = showSwapTemplate;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
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

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	public String getShowSynchronousCourseDetailsTemplate() {
		return showSynchronousCourseDetailsTemplate;
	}

	public void setShowSynchronousCourseDetailsTemplate(String showSynchronousCourseDetailsTemplate) {
		this.showSynchronousCourseDetailsTemplate = showSynchronousCourseDetailsTemplate;
	}

	public StatisticsService getStatService() {
		return statService;
	}

	public void setStatService(StatisticsService statService) {
		this.statService = statService;
	}

}