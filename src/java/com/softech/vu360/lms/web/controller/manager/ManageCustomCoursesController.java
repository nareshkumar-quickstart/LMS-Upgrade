package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class ManageCustomCoursesController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(ManageCustomCoursesController.class.getName());
	
	private String searchCustomCoursesTemplate = null;
	private String customCoursesDeletionResultTemplate = null;
	

	private static final String CUSTOM_COURSE_DELETION_RESULT_SUCCESSFUL = "Deleted";
	private static final String CUSTOM_COURSE_DELETION_RESULT_UNSUCCESSFUL = "Not Deleted";
	
	private static final String MANAGE_CUSTOM_COURSES_SEARCH_ACTION = "Search";
	private static final String MANAGE_CUSTOM_COURSES_DELETE_ACTION = "Delete";
	private static final String MANAGE_CUSTOM_COURSES_ALLCUSTOMCOURSES_ACTION = "allsearch";
	private CourseAndCourseGroupService courseAndCourseGroupService ;
	private LearnerService learnerService = null;
	private EnrollmentService enrollmentService = null;
	private EntitlementService entitlementService = null;
	HttpSession session = null;

	@SuppressWarnings("unchecked")
	public ModelAndView displaySearchCustomCourses(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			session = request.getSession();
			String sortDirection = request.getParameter("sortDirection");
			if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String pageIndex = request.getParameter("pageCurrIndex");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";

			if( pageIndex == null ) pageIndex = "0";
			log.debug("pageIndex = " + pageIndex);
			Map PagerAttributeMap = new HashMap();
			PagerAttributeMap.put("pageIndex",pageIndex);

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = null;
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				customer = details.getCurrentCustomer();
			}
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("showAll", showAll);
			String action = request.getParameter("action");
			if( StringUtils.isNotBlank(action)) {

				List<WebLinkCourse> listOfCustomCourse = new ArrayList <WebLinkCourse>();

				if( action.equalsIgnoreCase(MANAGE_CUSTOM_COURSES_SEARCH_ACTION) ) {
					String searchStr = "";
					if ( !StringUtils.isBlank(request.getParameter("simpleSearchCriteria")) ) {
						searchStr = request.getParameter("simpleSearchCriteria");
					}
					context.put("simpleSearchcriteria", request.getParameter("simpleSearchCriteria"));

					listOfCustomCourse = courseAndCourseGroupService.findCustomCourseByName(searchStr,customer);
					log.debug("course size = "+listOfCustomCourse.size());
					context.put("listOfCustomCourse", listOfCustomCourse);

				} else if( action.equalsIgnoreCase(MANAGE_CUSTOM_COURSES_ALLCUSTOMCOURSES_ACTION) ) {
					String searchStr = "";
					if( !StringUtils.isBlank(request.getParameter("simpleSearchCriteria")) ) {
						searchStr = request.getParameter("simpleSearchCriteria");
					} 
					context.put("simpleSearchcriteria", request.getParameter("simpleSearchCriteria"));
					if( customer != null ) {
						listOfCustomCourse = courseAndCourseGroupService.findCustomCourseByName(searchStr, customer);
						context.put("listOfCustomCourse", listOfCustomCourse);
					}
				} else if( action.equalsIgnoreCase(MANAGE_CUSTOM_COURSES_DELETE_ACTION) ) {
					
					deleteCustmCourses( request , context) ;
					return new ModelAndView(customCoursesDeletionResultTemplate, "context", context);
				}
				context.put("sortDirection", 0);
				context.put("sortColumnIndex", 0);

				/**
				 *  added by Dyutiman for sorting purpose
				 */
				if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {
					// sorting against invitation name
					request.setAttribute("PagerAttributeMap", PagerAttributeMap);
					if( sortColumnIndex.equalsIgnoreCase("0") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {
							for( int i = 0 ; i < listOfCustomCourse.size() ; i ++ ) {
								for( int j = 0 ; j < listOfCustomCourse.size() ; j ++ ) {
									if( i != j ) {
										WebLinkCourse courseName1 =  listOfCustomCourse.get(i);
										WebLinkCourse courseName2 =  listOfCustomCourse.get(j);
										if( courseName1.getCourseTitle().compareTo(courseName2.getCourseTitle()) > 0 ) {
											WebLinkCourse tempMap = listOfCustomCourse.get(i);
											listOfCustomCourse.set(i, listOfCustomCourse.get(j));
											listOfCustomCourse.set(j, tempMap);
										}
									}
								}
							}
							context.put("showAll", showAll);
							context.put("sortDirection", 0);
							context.put("sortColumnIndex", 0);
							session.setAttribute("sortDirection", 0);
							session.setAttribute("sortColumnIndex", 0);
						} else {
							for( int i = 0 ; i < listOfCustomCourse.size() ; i ++ ) {
								for( int j = 0 ; j < listOfCustomCourse.size() ; j ++ ) {
									if( i != j ) {
										WebLinkCourse courseName1 =  listOfCustomCourse.get(i);
										WebLinkCourse courseName2 =  listOfCustomCourse.get(j);
										if( courseName1.getCourseTitle().compareTo(courseName2.getCourseTitle()) < 0 ) {
											WebLinkCourse tempMap = listOfCustomCourse.get(i);
											listOfCustomCourse.set(i, listOfCustomCourse.get(j));
											listOfCustomCourse.set(j, tempMap);
										}
									}
								}
							}
							context.put("sortDirection", 1);
							context.put("sortColumnIndex", 0);
							session.setAttribute("sortDirection", 1);
							session.setAttribute("sortColumnIndex", 0);
						}
					}				
				}
				return new ModelAndView(searchCustomCoursesTemplate,"context",context);
			}else{
				request.setAttribute("newPage","true");
			}
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchCustomCoursesTemplate);
	}

	private  void deleteCustmCourses(HttpServletRequest request, Map<Object , Object> context ) {
 		Vector<Long> inUseCourses = new Vector<Long>();  
		Vector<Long> unUsedCourses = new Vector<Long>();

		Vector<CustomerEntitlement> validEntitlementsForDeletion = new Vector<CustomerEntitlement>();
		Vector<Course> deletedCourses = new Vector<Course>();
		Vector<Long> invalidEntitlementsForDeletion = new Vector<Long>();
		
		//Map<Object, Object> context = new HashMap<Object, Object>();		
		List<Map> courseDetails = new ArrayList<Map>();
		
		// below function will populate the upper two arrays 
		populateListOfUsedandUnsedCourses(  inUseCourses , unUsedCourses , request );

		searchCustomCourseEntitlementForDeletion(validEntitlementsForDeletion , invalidEntitlementsForDeletion , unUsedCourses , courseDetails);
		
		log.debug("valid Entitlements For Deletion array size" + validEntitlementsForDeletion.size() ) ;		
		
		populateCourseDetails(inUseCourses ,validEntitlementsForDeletion , courseDetails ) ; // for result summary		
		deleteValidEntitlements(validEntitlementsForDeletion);  
		
		context.put("totalAttempted" , getTotalAttempted(request));
		context.put("totalCoursesUnableToDelete" ,getTotalCoursesUnableToDelete( inUseCourses ,  invalidEntitlementsForDeletion ));		
		context.put("totalDeleted" ,validEntitlementsForDeletion.size());		
		context.put("courseDetails" ,courseDetails);

	}
	
	
	void populateListOfUsedandUnsedCourses( Vector<Long> inUseCourses , Vector<Long> unUsedCourses ,HttpServletRequest request) {
		// fetch course ids from request 
		String[] selectedCustomCourseValues = request.getParameterValues("selectCustomCourse");
		long customCourseIdIdArray[] = new long[selectedCustomCourseValues.length];
		
		if( selectedCustomCourseValues != null ){ // check if it is null
			
			List<LearnerEnrollment> numberOfEnrollments = null ;
			
			for( int i=0; i<selectedCustomCourseValues.length; i++ ) { // loop through custom course ids
				
				String customCourseID = selectedCustomCourseValues[i];
				
				if( StringUtils.isNotBlank(customCourseID)) {					
				
					customCourseIdIdArray[i]=Long.parseLong(selectedCustomCourseValues[i]);
					
					// execute SQL to check if any custom course enrollment exist for this course 
					
					numberOfEnrollments = (List<LearnerEnrollment>) enrollmentService.getNumberofEnrollmentsOfThisCourse(customCourseIdIdArray[i]);
					if( numberOfEnrollments  != null ){
						if( numberOfEnrollments.size() > 0 ){ // if yes 
							inUseCourses.add(customCourseIdIdArray[i]);
						}
						else { // if not 
							unUsedCourses.add(customCourseIdIdArray[i]) ; 
						}						
					}						
					else { // if not 
						unUsedCourses.add(customCourseIdIdArray[i]) ; 
					}
				}				
			}
		}	

	}
	
	void searchCustomCourseEntitlementForDeletion( Vector<CustomerEntitlement> validEntitlements , Vector<Long>  invalidEntitlements , Vector<Long> unUsedCourses , List<Map> errors){
		
		if ( unUsedCourses != null){
			
			for(Long customCourseId : unUsedCourses) {
				List<CustomerEntitlement> customerEntitlementsList = entitlementService.getCustomerEntitlementsByCourseId( customCourseId ) ;
				log.debug("delete customer Entitlements List " + customerEntitlementsList.size() );
				
				if (customerEntitlementsList != null ) {
					if( customerEntitlementsList.size() == 1 ) {

						for(CustomerEntitlement custEntitlements: customerEntitlementsList){
							int totalNoOfCoursesInEntitlement =  entitlementService.getUniqueCourses(custEntitlements).size();//custEntitlements.getUniqueCourses().size() ; 
							log.debug(" Entitlements has following list of courses " + totalNoOfCoursesInEntitlement );
							if( totalNoOfCoursesInEntitlement > 1){
								// error handling to be added
								addErrorDetails(errors , "Customer Entitlement has more than one course" , CUSTOM_COURSE_DELETION_RESULT_UNSUCCESSFUL , customCourseId);
								invalidEntitlements.add(0L);
							}
							else{
								validEntitlements.add(custEntitlements);
							}
						}
					}
					else if (customerEntitlementsList.size() == 0){
						// error handling to be added
						addErrorDetails(errors , "Customer Entitlement not found" , CUSTOM_COURSE_DELETION_RESULT_UNSUCCESSFUL , customCourseId);
						invalidEntitlements.add(0L);

					}
					else  if (customerEntitlementsList.size() > 1){
						addErrorDetails(errors , "More than 1 Customer Entitlements related to this course." , CUSTOM_COURSE_DELETION_RESULT_UNSUCCESSFUL , customCourseId);
						invalidEntitlements.add(0L);
					}
				}
				else{
					// error handling to be added
					addErrorDetails(errors , "Customer Entitlement not found." , CUSTOM_COURSE_DELETION_RESULT_UNSUCCESSFUL ,customCourseId);
					invalidEntitlements.add(0L);
				}
			}
		}
		
	}
	
	void deleteValidEntitlements(Vector<CustomerEntitlement> validEntitlementsForDeletion){
		
		List<CourseCustomerEntitlementItem> items = null;
		for( CustomerEntitlement obj : validEntitlementsForDeletion ){
			CourseCustomerEntitlement cCE=(CourseCustomerEntitlement)obj;
			items = entitlementService.getItemsByEntitlement(cCE);
			for(CourseCustomerEntitlementItem item : items){ 
				courseAndCourseGroupService.deleteCourse(item.getCourse());
			
			}
			entitlementService.deleteCustomerEntitlement(obj);
		}		
	}
	
	int getTotalAttempted(HttpServletRequest  req) {
		String[] selectedCustomCourseValues = req.getParameterValues("selectCustomCourse");
		if( selectedCustomCourseValues  != null){
			return selectedCustomCourseValues.length;
		}
		return 0 ;
	}
	
	int getTotalCoursesUnableToDelete( Vector<Long> inUseCourses , Vector<Long> invalidEntitlementsForDeletion ) {
		if(inUseCourses != null && invalidEntitlementsForDeletion != null ) {
			return inUseCourses.size() + invalidEntitlementsForDeletion.size() ; 
		}
		return 0;
	}

	void addErrorDetails(List<Map> courseDetails, String message ,String statusType, Long id ) {
		
		Map<String,Object>  context  = new HashMap<String,Object>();
		Course course = courseAndCourseGroupService.getCourseById(id);
		
		context.put("message",message);
		context.put("status",statusType);		
		context.put("course",course);
		
		courseDetails.add(context);
	}
	
	void populateCourseDetails(Vector<Long> inUseCourses ,Vector<CustomerEntitlement> validEntitlementsForDeletion ,List<Map> courseDetails ) {
		
		if( inUseCourses != null) {
			for( Long cid  : inUseCourses) {
				addErrorDetails(courseDetails, "Enrollment Present" ,CUSTOM_COURSE_DELETION_RESULT_UNSUCCESSFUL, cid );
			}
		}

		if( validEntitlementsForDeletion != null) {
			for( CustomerEntitlement ce  : validEntitlementsForDeletion) {
				// as we know there is one only course in course entitlement 
				addErrorDetails(courseDetails, "" ,CUSTOM_COURSE_DELETION_RESULT_SUCCESSFUL, ((Course)ce.getUniqueCourses().toArray()[0]).getId());
			}
		}

	}
	
	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}

	public String getSearchCustomCoursesTemplate() {
		return searchCustomCoursesTemplate;
	}

	public void setSearchCustomCoursesTemplate(String searchCustomCoursesTemplate) {
		this.searchCustomCoursesTemplate = searchCustomCoursesTemplate;
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

	/**
	 * @return the enrollmentService
	 */
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	/**
	 * @param enrollmentService the enrollmentService to set
	 */
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	/**
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	/**
	 * @return the customCoursesDeletionResultTemplate
	 */
	public String getCustomCoursesDeletionResultTemplate() {
		return customCoursesDeletionResultTemplate;
	}

	/**
	 * @param customCoursesDeletionResultTemplate the customCoursesDeletionResultTemplate to set
	 */
	public void setCustomCoursesDeletionResultTemplate(
			String customCoursesDeletionResultTemplate) {
		this.customCoursesDeletionResultTemplate = customCoursesDeletionResultTemplate;
	}

}