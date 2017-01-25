package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.CourseItem;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEnrollmentForm;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEnrollmentItem;
import com.softech.vu360.lms.web.controller.model.ViewLearnerEntitlementItem;
import com.softech.vu360.lms.web.controller.validator.EditEnrollmentValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author Parthasarathi
 * @modified by Dyutiman
 */
public class ViewLearnerEnrollmentController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageLearnerController.class.getName());
	private static final String MANAGE_ENROLLMENT_DEFAULT_ACTION = "default";
	private static final String MANAGE_ENROLLMENT_SIMPLE_SEARCH_ACTION = "simpleSearch";
	private static final String MANAGE_ENROLLMENT_ALL_SEARCH_ACTION = "allSearch";
	private static final String MANAGE_ENROLLMENT_PREVPAGE_DIRECTION = "prev";
	private static final String MANAGE_ENROLLMENT_NEXTPAGE_DIRECTION = "next";
	private static final int MANAGE_ENROLLMENT_PAGE_SIZE = 5;
	private static final String MANAGE_ENROLLMENT_ADVANCED_SEARCH_ACTION = "advanceSearch";
	private static final String MANAGE_ENROLLMENT_SORT_LEARNER_ACTION = "sort";

	private String manageLearnersTemplate = null;
	private String showEnrollmentTemplate = null;
	private String showExtendTemplate = null;
	private String showSwapTemplate = null;
	private String redirectTemplate = null;
	private EnrollmentService enrollmentService= null;
	private VU360UserService vu360UserService;
	private LearnerService learnerService;
	private EntitlementService entitlementService; 
	private CourseAndCourseGroupService courseAndCourseGroupService;

	public ViewLearnerEnrollmentController() {
		super();
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
		ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
		enrollForm.reset();
		return new ModelAndView(manageLearnersTemplate);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView searchLearner(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {

		try {
			ViewLearnerEnrollmentForm enrollForm = (ViewLearnerEnrollmentForm)command;
			Map <Object, Object> userMap = new HashMap<Object,Object>();
			Map<Object,Object> results = new HashMap<Object,Object>();
			List<VU360User> userList = new ArrayList<VU360User>();
			List<Map> list = new ArrayList <Map>();
			String searchType = enrollForm.getSearchType();

			/*
			 * Get logged in user
			 */
			//VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			Map<Object, Object> context = new HashMap<Object, Object>();
			int pageNo = enrollForm.getPageIndex()<0 ? 0 : enrollForm.getPageIndex()/VelocityPagerTool.DEFAULT_PAGE_SIZE;
			int sortDirection = enrollForm.getSortDirection()<0 ? 0:enrollForm.getSortDirection(); 
			String sortBy = StringUtils.isBlank(enrollForm.getSortField())? "firstName":enrollForm.getSortField();
			
			if(searchType.equalsIgnoreCase("simplesearch")){
				List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				
				results = learnerService.findLearner1(enrollForm.getSimpleSearchKey(),
						loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
						pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);

				userList = (List<VU360User>)results.get("list");
				Integer totalResults = (Integer)results.get("recordSize");
				Map<String,String> pagerAttributeMap = new HashMap<String,String>();
				pagerAttributeMap.put("pageIndex", Integer.toString(enrollForm.getPageIndex()));
				pagerAttributeMap.put("totalCount", totalResults.toString());
				request.setAttribute("PagerAttributeMap", pagerAttributeMap);

			}else if(searchType.equalsIgnoreCase("advancedsearch")){
				List<OrganizationalGroup> tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
				
				results = learnerService.findLearner1(enrollForm.getFirstName(), enrollForm.getLastName(), enrollForm.getEmailAddress(),
						loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
						loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups(), tempManagedGroups, 
						loggedInUser.getLearner().getCustomer().getId(), loggedInUser.getId(), 
						pageNo, VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
				userList = (List<VU360User>)results.get("list");
				Integer totalResults = (Integer)results.get("recordSize");
				Map<String,String> pagerAttributeMap = new HashMap<String,String>();
				pagerAttributeMap.put("pageIndex", Integer.toString(enrollForm.getPageIndex()));
				pagerAttributeMap.put("totalCount", totalResults.toString());
				request.setAttribute("PagerAttributeMap", pagerAttributeMap);

			}
			String totalRecord = (results.get("recordSize")==null)?"0":results.get("recordSize").toString();

			for( int userNumber = 0 ; userNumber < userList.size() ; userNumber ++ ) {

				userMap = new HashMap<Object, Object>();
				int activeEnrollments = 0;
				VU360User vu360user = userList.get(userNumber);
				List<LearnerEnrollment> enrollments = entitlementService.getLearnerEnrollmentsForLearner(vu360user.getLearner());//vu360user.getLearner().getEnrollments();

				for(int enrollmentNum = 0 ; enrollmentNum<enrollments.size() ; enrollmentNum++ ) {
					LearnerEnrollment enrollment = enrollments.get(enrollmentNum);
					if(enrollment.getEnrollmentStatus().equalsIgnoreCase("ACTIVE")) {
						activeEnrollments++;
					}
				}
				userMap.put("user", vu360user);
				userMap.put("activeEnrollments", activeEnrollments);
				list.add(userMap);
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
					loggedInUser.isLMSAdministrator(), loggedInUser.isTrainingAdministrator(), loggedInUser.getTrainingAdministrator().getId(), 
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

		EditEnrollmentValidator validator = (EditEnrollmentValidator)this.getValidator();
		if( methodName.equals("showExtendEnrollment")) {
			validator.validateSelectEnrollment(form, errors);

		} else if( methodName.equals("dropEnrollment")) {
			validator.validateSelectEnrollment(form, errors);

		} else if( methodName.equals("expireEnrollment")) {
			validator.validateSelectEnrollment(form, errors);

		} else if( methodName.equals("saveExtendEnrollment")) {
			validator.validateSelectEnrollment(form, errors);
			boolean check = validator.validateEnrollmentCourses(form, errors);
			if( check )
				validator.validateExtendEnrollments(form, errors);
			
		} else if( methodName.equals("swapEnrollment")) {
			validator.validateSwapEnrollment(form, errors);

		} else if( methodName.equals("swapCourse")) {
			validator.validateCourseSwapEnrollment(form, errors);

		}
	}

	private List<ViewLearnerEntitlementItem> filterEnrollmets(List<LearnerEnrollment> learnerEnrollments){
		Map <String,CustomerEntitlement> customerEntitlement = new LinkedHashMap<String,CustomerEntitlement>();
		boolean found=false;
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

		if( command instanceof ViewLearnerEnrollmentForm ){
			ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
			if( methodName.equals("showEnrollments")) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				VU360User user = vu360UserService.getUserById(form.getId());
				Learner learner = user.getLearner();
				//Need to enhanch dao
				List<LearnerEnrollment> enrollments =entitlementService.getLearnerEnrollmentsForLearner(learner);// learner.getEnrollments();
				form.setViewLearnerEntitlementItems(filterEnrollmets(enrollments));
			} 
		}
	}

	@SuppressWarnings("unchecked")
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
		Map<Object, Object> context = new HashMap<Object, Object>();
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
		return new ModelAndView(showEnrollmentTemplate);
	}

	public ModelAndView dropEnrollment(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {
		Map<Object, Object> context = new HashMap<Object, Object>();
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
		return new ModelAndView(showEnrollmentTemplate);
	}

	public ModelAndView saveExtendEnrollment(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {
		Map<Object, Object> context = new HashMap<Object, Object>();
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
			return new ModelAndView(showEnrollmentTemplate, "context", context);

		} catch(Exception e) {
			log.error("exception", e);
		}
		return new ModelAndView(showExtendTemplate);
	}
	public ModelAndView showAllCourses(HttpServletRequest request,HttpServletResponse response,
			Object command, BindException errors) {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		ViewLearnerEnrollmentForm form = (ViewLearnerEnrollmentForm)command;
		Map<Object,Object> results = new HashMap<Object,Object>();
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
		List<CustomerEntitlement> customerEntitlementList = null;
		List<CourseItem> list = new ArrayList <CourseItem>();
		
		for (int count=0;count<learnerEnrollments.size();count++) {
			if(learnerEnrollments.get(count).getCourse() != null) {
				courseIdArray[count]=(learnerEnrollments.get(count).getCourse().getId());
			}
		}

		try {
			results = enrollmentService.getAllCoursesNotEnrolledByLearner(courseIdArray,learner.getCustomer());
			customerEntitlementList = (List<CustomerEntitlement>)results.get("list");
			
			for( CustomerEntitlement ent : customerEntitlementList ) {
				log.debug("CE in ShowAll: " + ent.getName()+ " - " + ent.getId());
				Set<Course> courses = entitlementService.getUniqueCourses(ent);//ent.getUniqueCourses();
				for(Course c : courses) {
					CourseItem item = new CourseItem();
					if(request.getSession().getAttribute("selectedCourseId")!=null){
						if(!request.getSession().getAttribute("selectedCourseId").toString().equals(c.getId().toString())){
								item.setCourse(c);
								item.setSelected(false);
								list.add(item);
							}
						}
				}
			}
			
			String totalRecord = list.size()+"";
			
			context.put("totalRecord", Integer.parseInt(totalRecord));
			context.put("members", list);
			form.setCourseItems(list);
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


			//if(action.equalsIgnoreCase(MANAGE_ENROLLMENT_SIMPLE_SEARCH_ACTION)){
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
			//sortDirection = (sortDirection =="0")?"1":"0";
			if (sortDirection == "0")
				sortDirection = "1";
			else
				sortDirection="0";
			if (session.getAttribute("searchType").toString().equalsIgnoreCase(MANAGE_ENROLLMENT_ALL_SEARCH_ACTION))
				recordShowing=customerEntitlementList.size();

			String totalRecord=	(results.get("recordSize")==null)?"0":results.get("recordSize").toString();


			/*	results=learnerService.getAllCoursesNotNotEnrolledByLearner(courseIdArray, session.getAttribute("searchedCourseName").toString(),
						session.getAttribute("searchedCourseId").toString(),session.getAttribute("searchedCourseKeyword").toString(),
						Integer.parseInt(session.getAttribute("pageNo").toString()), MANAGE_ENROLLMENT_PAGE_SIZE, "", Integer.parseInt(sortDirection));
			 */	
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

			//context.put("totalRecord", Integer.parseInt(totalRecord));

		} catch(Exception e) {

			log.error("exception", e);
		}
		return new ModelAndView(showSwapTemplate);
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

}