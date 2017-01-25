package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.TrainingPlan;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.TrainingPlanService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddTrainingPlanForm;
import com.softech.vu360.lms.web.controller.validator.AddTrainingPlanValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Saptarshi
 *
 */
public class AddTrainingPlanCoursesWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddTrainingPlanCoursesWizardController.class.getName());

	private TrainingPlanService trainingPlanService;
	private EntitlementService entitlementService = null;
		

	private static final int SEARCH_COURSE_PAGE_SIZE = 10;
	

	private String closeTemplate = null;
	private String summaryTemplate = null;

	HttpSession session=null;

	public AddTrainingPlanCoursesWizardController() {

		super();
		setCommandName("trainingPlanForm");
		setCommandClass(AddTrainingPlanForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/trainingPlan/trainingPlanCourses"});
	}

	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		AddTrainingPlanForm form = (AddTrainingPlanForm)command;
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		if( page == 0){
			coursesSearch(form, customer,request, errors);
			coursesSearchMove(form);
			coursesSearchShowAll(form);
		}
		super.postProcessPage(request, command, errors, page);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException error) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		AddTrainingPlanForm addTrainingPlanForm = (AddTrainingPlanForm)command;
		StringBuilder selectedCourseIdBuilder = new StringBuilder("");
		String selectedCourseIds = "";
		TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanByID(Long.valueOf(addTrainingPlanForm.getSelectedTrainingPlanId()));
		
		List<TrainingPlanCourse> trainingPlanCourses = trainingPlanService.getTrainingPlanCourses(trainingPlan);
		for(EnrollmentCourseView courseView : addTrainingPlanForm.getEnrollmentCourseViewList()) {			
			if(courseView.isSelected()){
				boolean addCourse = true;
				for (TrainingPlanCourse trainingPlanCourse : trainingPlanCourses) {
					if (trainingPlanCourse.getCourse().getId().equals(courseView.getCourseId())){
						addCourse = false;
						break;
					}
				}
				
				if (addCourse) {
					selectedCourseIdBuilder.append(courseView.getCourseId() + ",");
					/*course=new Course();
					course.setId(courseView.getCourseId());
					selectedCourses.add(course);*/
				}
			}
		}
		
		selectedCourseIds = StringUtils.removeEnd(selectedCourseIdBuilder.toString(), ",");
		
		/*for( Course c : selectedCourses) {			
			TrainingPlanCourse tpcourse = new TrainingPlanCourse();
			tpcourse.setCourse(c);
			tpcourse.setTrainingPlan(trainingPlan);
			trainingPlanCourses.add(tpcourse);
		}*/
		
		//trainingPlan= trainingPlanService.addTrainingPlan(trainingPlan);
		context.put("trainingPlanId", addTrainingPlanForm.getSelectedTrainingPlanId());
		context.put("selectedCourseIds", selectedCourseIds);
		
		return new ModelAndView(closeTemplate, "context", context);
	}

	protected Object formBackingObject(HttpServletRequest request)throws Exception {

		Object command = super.formBackingObject(request);
		AddTrainingPlanForm addTrainingPlanForm = (AddTrainingPlanForm)command;
		
		if (StringUtils.isNotBlank(request.getParameter("Id"))) {
			addTrainingPlanForm.setSelectedTrainingPlanId(request.getParameter("Id").trim());
			if (StringUtils.isNotBlank(request.getParameter("action")))
				addTrainingPlanForm.setAction(request.getParameter("action").trim());
		}
		
		return command;
	}

	protected Map referenceData(HttpServletRequest request,
			Object command,	Errors errors, int page) throws Exception {

		AddTrainingPlanForm addTrainingPlanForm = (AddTrainingPlanForm)command;		
		addTrainingPlanForm.setEditMode(true);
		return super.referenceData(request, page);
	}

	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		AddTrainingPlanForm form = (AddTrainingPlanForm)command;
		AddTrainingPlanValidator validator = (AddTrainingPlanValidator)this.getValidator();
		
		switch (page) {
		case 0:
			if( finish ) {
				validator.validateAddEditCoursePage(form, errors);
				if (!errors.hasErrors()) {
					validator.validateCourses(form,errors);
				}
			}			
			break;
		}
		
		super.validatePage(command, errors, page, finish);
	}
	
	
	private void coursesSearch(AddTrainingPlanForm form,Customer customer ,HttpServletRequest request, Errors errors ) {
		
		// if course  search  is requested 
		session = request.getSession();
		if( form.getCourseSearchType() != null && ( form.getCourseSearchType().trim().equalsIgnoreCase("advanceSearch")
					|| form.getCourseSearchType().trim().equalsIgnoreCase("showAll") ) ) {
			
			int intLimit  = 100;
			Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			String limit = brander.getBrandElement("lms.resultSet.showAll.Limit");
			
			if(StringUtils.isNumeric(limit.trim()))
				intLimit  = Integer.parseInt(limit);
			

			// get parameter from request and pass to service layer
			// enrollmentStartDate = formatter.parse(saveEnrollmentParam.getStartDate());
			String strEntDate=form.getSearchDateLimit();
			Date entitlementDate=null;
			if(!StringUtils.isBlank(strEntDate))
				entitlementDate = DateUtil.getDateObject(strEntDate);
			
			List<EnrollmentCourseView> trainingPlanCourseViewList = entitlementService.getCoursesForEnrollmentByCustomer(customer , form.getSearchCourseName() , form.getSearchCourseId() , form.getSearchEntitlementName() ,
					entitlementDate, intLimit);
			
			//for Sorting:
			Map<Object, Object> context = new HashMap<Object, Object>();
			String sortDirection = request.getParameter("sortDirection");
			if( (StringUtils.isBlank(sortDirection)) && session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			if( (StringUtils.isBlank(sortColumnIndex)) && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex="0";
			String pageIndex = request.getParameter("pageCurrIndex");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			Map PagerAttributeMap = new HashMap();
			
			if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {
				// sorting against invitation name
				request.setAttribute("PagerAttributeMap", PagerAttributeMap);
				
				if( sortDirection.equalsIgnoreCase("0") ) {
					for( int i = 0 ; i < trainingPlanCourseViewList.size() ; i ++ ) {
						for( int j = 0 ; j < trainingPlanCourseViewList.size() ; j ++ ) {
							if( i != j ) {
								EnrollmentCourseView courseName1 =  trainingPlanCourseViewList.get(i);
								EnrollmentCourseView courseName2 =  trainingPlanCourseViewList.get(j);
								
								if(sortColumnIndex.equalsIgnoreCase("0")){
									if( courseName1.getCourseName().compareTo(courseName2.getCourseName()) > 0 ) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("1")){
									if( courseName1.getCourseId() > courseName2.getCourseId()) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("2")){
									if( courseName1.getTotalSeats() > courseName2.getTotalSeats()) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("3")){
									if( courseName1.getSeatsUsed() > courseName2.getSeatsUsed()) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("4")){
									if( courseName1.getSeatsRemaining() > courseName2.getSeatsRemaining()) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("5")){
									if( courseName1.getExpirationDate().after(courseName2.getExpirationDate())) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								
							}
						}
					}
					context.put("showAll", showAll);
					form.setSortDirection(1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", sortColumnIndex);
					form.setSortColumnIndex(Integer.parseInt(sortColumnIndex));
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
				} else {
					for( int i = 0 ; i < trainingPlanCourseViewList.size() ; i ++ ) {
						for( int j = 0 ; j < trainingPlanCourseViewList.size() ; j ++ ) {
							if( i != j ) {
								EnrollmentCourseView courseName1 =  trainingPlanCourseViewList.get(i);
								EnrollmentCourseView courseName2 =  trainingPlanCourseViewList.get(j);
								
								if(sortColumnIndex.equalsIgnoreCase("0")){
									if( courseName1.getCourseName().compareTo(courseName2.getCourseName()) < 0 ) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("1")){
									if( courseName1.getCourseId() < courseName2.getCourseId()) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("2")){
									if( courseName1.getTotalSeats() < courseName2.getTotalSeats()) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("3")){
									if( courseName1.getSeatsUsed() < courseName2.getSeatsUsed()) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("4")){
									if( courseName1.getSeatsRemaining() < courseName2.getSeatsRemaining()) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
								if(sortColumnIndex.equalsIgnoreCase("5")){
									if( courseName1.getExpirationDate().before(courseName2.getExpirationDate())) {
										EnrollmentCourseView tempMap = trainingPlanCourseViewList.get(i);
										trainingPlanCourseViewList.set(i, trainingPlanCourseViewList.get(j));
										trainingPlanCourseViewList.set(j, tempMap);
									}
								}
							}
						}
					}
					context.put("sortDirection", 0);
					form.setSortDirection(0);
					context.put("sortColumnIndex", sortColumnIndex);
					form.setSortColumnIndex(Integer.parseInt(sortColumnIndex));
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
				}
			}
			// end sorting
			
			form.setEnrollmentCourseViewList(trainingPlanCourseViewList );
			
			if( form.getEnrollmentCourseViewList().size() <= SEARCH_COURSE_PAGE_SIZE )
				form.setCourseSearchEnd( form.getEnrollmentCourseViewList().size()-1);
			else
				form.setCourseSearchEnd( (SEARCH_COURSE_PAGE_SIZE -1));
			
			form.setCourseSearchStart(0);
			form.setCourseSearchPageNumber(0);
			form.setCourseSearchShowAll( "" );		
		}
		
	}
	

	void coursesSearchShowAll(AddTrainingPlanForm form){
		// show all courses is requested 
		if( form.getCourseSearchType()!=null && form.getCourseSearchType().trim().equalsIgnoreCase("showAll") ){

			form.setCourseSearchEnd( form.getEnrollmentCourseViewList().size()-1);
			form.setCourseSearchStart(0);
			form.setCourseSearchShowAll( "showAll" );
			form.setCourseSearchPageNumber(0);
		}
		
	}
	
	void coursesSearchMove(AddTrainingPlanForm form){
		// show all courses is requested 
		if( form.getCourseSearchType()!=null && form.getCourseSearchType().trim().equalsIgnoreCase("move") ){
			int total = form.getEnrollmentCourseViewList().size();
		
			if( form.getCourseSearchDirection().trim().equalsIgnoreCase("next")){
				int newPageNumber = form.getCourseSearchPageNumber() + 1 ;
				if( ( newPageNumber * SEARCH_COURSE_PAGE_SIZE ) > total ){
					log.debug( newPageNumber +" greater than  "+total );
					// nochange
				}
				else{
					int lastPageLimit = (form.getCourseSearchPageNumber()+2) *  SEARCH_COURSE_PAGE_SIZE ;
					form.setCourseSearchStart( form.getCourseSearchEnd()+1 );
					form.setCourseSearchPageNumber( form.getCourseSearchPageNumber() + 1) ;
					if( total >= lastPageLimit  ) {
						form.setCourseSearchEnd( (lastPageLimit-1) );
					}
					else 
						form.setCourseSearchEnd( ( total-1) );
				}
				
			}
			else if( form.getCourseSearchDirection().trim().equalsIgnoreCase("prev") ){
				int newPageNumber = form.getCourseSearchPageNumber() - 1 ;
				if( ( newPageNumber * SEARCH_COURSE_PAGE_SIZE ) < 0 ){
					log.debug( newPageNumber +" greater than  "+total );
					// nochange
				}
				else {
					int lastPageLimit = ( form.getCourseSearchPageNumber() - 1 ) *  SEARCH_COURSE_PAGE_SIZE ;
					
					if( newPageNumber == 0 ) {
						form.setCourseSearchStart(0 );
					}
					else if( newPageNumber > 0 ) {
						form.setCourseSearchStart(  ((newPageNumber  *  SEARCH_COURSE_PAGE_SIZE) ) );
					}

					else{
						form.setCourseSearchStart( 0 );
					}
					
					form.setCourseSearchEnd(  ( ((newPageNumber  *  SEARCH_COURSE_PAGE_SIZE) + ( SEARCH_COURSE_PAGE_SIZE -1) )) );
					form.setCourseSearchPageNumber( form.getCourseSearchPageNumber() - 1 ) ; 
				}
			}

		}
		
		form.setCourseSearchShowAll("");
	}
	
	
	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
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

	public String getSummaryTemplate() {
		return summaryTemplate;
	}

	public void setSummaryTemplate(String summaryTemplate) {
		this.summaryTemplate = summaryTemplate;
	}

	/**
	 * @return the trainingPlanService
	 */
	public TrainingPlanService getTrainingPlanService() {
		return trainingPlanService;
	}

	/**
	 * @param trainingPlanService the trainingPlanService to set
	 */
	public void setTrainingPlanService(TrainingPlanService trainingPlanService) {
		this.trainingPlanService = trainingPlanService;
	}

}