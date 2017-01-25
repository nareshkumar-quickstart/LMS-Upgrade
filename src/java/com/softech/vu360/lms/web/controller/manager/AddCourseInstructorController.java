/**
 * 
 */
package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.InstructorService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.instructor.CourseInstructorForm;
import com.softech.vu360.lms.web.controller.model.instructor.InstructorItemForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

/**
 * @author sana.majeed
 * [9/22/2010] LMS-7106 :: Manager Mode > Associate Instructor with DFC Course
 */
public class AddCourseInstructorController extends AbstractWizardFormController {
	
	private static final Logger log = Logger.getLogger(AddCourseInstructorController.class.getName());
	private InstructorService instructorService = null;
	private VelocityEngine velocityEngine = null;
	private String closeTemplate = null;
	
	private static final int SEARCH_RESULT_PAGE_SIZE = 10;	

	private static final String MANAGE_USER_SHOW_ALL_ACTION = "showAll";
	private static final String MANAGE_USER_SHOW_NEXT_ACTION = "showNext";
	private static final String MANAGE_USER_SHOW_PREV_ACTION = "showPrev";
	private static final String MANAGE_USER_SHOW_DEFAULT_ACTION = "showNone";
	
	private static final String MANAGE_USER_ASSIGNEMENT_TYPE_ALL = "all";
	
	public AddCourseInstructorController() {
		super();
		setCommandName("courseInstructorForm");
		setCommandClass(CourseInstructorForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/manageSynchronousCourse/addInstructor/selectInstructor",
				"manager/manageSynchronousCourse/addInstructor/selectInstructorRole",
				"manager/manageSynchronousCourse/addInstructor/confirmation"	});	
	}
	
	/**
	 * Step 1.
	 * We do not need to override this method now.
	 * This method basically lets us hook in to the point
	 * before the request data is bound into the form/command object
	 * This is called first when a new request is made and then on
	 * every subsequent request. However in our case, 
	 * since the bindOnNewForm is true this 
	 * will NOT be called on subsequent requests...
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {		
		
		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		
		return command;
	}
	
	/**
	 * Step 2.
	 * We do not need to override this method now.
	 * This method lets us hook in to the point
	 * before the request data is bound into the form/command object
	 * and just before the binder is initialized...
	 * We can have customized binders used here to interpret the request fields
	 * according to our requirements. It allows us to register 
	 * custom editors for certain fields.
	 * Called on the first request to this form wizard.
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		
		log.debug("IN initBinder");		
		super.initBinder(request, binder);
	}
	
	/**
	 * Step 3.
	 * We do not need to override this method now.
	 * Since we have bindOnNewForm property set to true in the constructor
	 * this method will be called when the first request is processed.
	 * We can add custom implementation here later to populate the command object
	 * accordingly.
	 * Called on the first request to this form wizard.
	 */
	protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException binder) throws Exception {
		
		log.debug("IN onBindOnNewForm");
		super.onBindOnNewForm(request, command, binder);
	}
	
	/**
	 * Step 4.
	 * Shows the first form view.
	 * Called on the first request to this form wizard.
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
		
		log.debug("IN showForm");
		ModelAndView modelNView = super.showForm(request, response, binder);
		log.debug("OUT showForm for view: " + modelNView.getViewName());
		return modelNView;
	}
	
	/**
	 * we can do custom processing after binding and validation
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException error, int page) throws Exception {
		
		log.debug("IN onBindAndValidate");		
		super.onBindAndValidate(request, command, error, page);
	}
	
	/**
	 * The Validator's default validate method WILL NOT BE CALLED by a wizard form controller!
	 * We need to do implementation specific - page by page - validation
	 * by explicitly calling the validateXXX function in the validator
	 */
	protected void validatePage(Object command, Errors errors, int page) {
		
		log.debug("IN validatePage");
		errors.setNestedPath("");	
	}
	
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		
		log.debug("IN getTargetPage");		
		return super.getTargetPage(request, command, errors, currentPage);
	}
	
	/**
	 * Called by showForm and showPage ... get some standard data for this page
	 */
	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		
		log.debug("IN referenceData > page: " + page);
		CourseInstructorForm form = (CourseInstructorForm)command;
		
		switch(page){		
		case 0:
			if( form.getAction().equals(MANAGE_USER_SHOW_DEFAULT_ACTION) ) {
				request.setAttribute("newPage", "true");				
				form.setCourseId( new Long( request.getParameter("id") ));
				form.setSearchResultsPageSize(SEARCH_RESULT_PAGE_SIZE);
			}
			else {
				// Save selected instructors before performing any other actions
				this.setSelectedInstructorItems(form);
				
				// Search instructor for given action
				this.searchInstructor(form, request);
				
				// Restore the already selected instructors back to page list
				this.restoreInstructorSelection(form);
				
			}			
			break;
		
		case 1:
			// Save selected instructors before performing any other actions
			this.setSelectedInstructorItems(form);
			
			break;
			
		case 2:
			if (form.getAssignmentType().equals(MANAGE_USER_ASSIGNEMENT_TYPE_ALL)) {
				// Assign same type to all selected instructors
				for (int index = 0; index < form.getSelectedInstructors().size(); index++ ) {
					form.getSelectedInstructors().get(index).setInstructorType( form.getInstructorType() );
				}
			}
			break;

		default:
			break;
		}
		
		return super.referenceData(request, page);
	}
	
	private void restoreInstructorSelection (CourseInstructorForm form) {
		
		if ( form.getSelectedInstructors().size() > 0 ) {
			for ( InstructorItemForm selInstructor : form.getSelectedInstructors() ) {
				for (int index = 0; index < form.getInstructors().size(); index++ ) {
					if ( form.getInstructors().get(index).compareTo( selInstructor ) == 0 ) { 
						form.getInstructors().get(index).setSelected(true);
					}
				}
			}
		}
	}
	
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		
		log.debug("IN processCancel");
		
		CourseInstructorForm form = (CourseInstructorForm) command;		
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		// Fill out following information required for closeTemplate
		context.put("courseId", form.getCourseId());
		context.put("action", "showNone");
		
		return new ModelAndView(closeTemplate, "context", context);
	}
	

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		log.debug("IN processFinish");
		
		CourseInstructorForm form = (CourseInstructorForm) command;		
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		int totalRecords = -1;
		int recordShowing = 0;
		
		List<InstructorCourse> courseInstructorList = new ArrayList<InstructorCourse>();
		if (form.getSelectedInstructors().size() > 0) {
			courseInstructorList = this.instructorService.associateCourseInstructors( form.getCourseId(), form.getSelectedInstructors() );			
		}
		
		if (courseInstructorList.size() > 0) {
			totalRecords = courseInstructorList.size();
			recordShowing = totalRecords;
		}
		else if ( (form.getSelectedInstructors().size() > 0) && (courseInstructorList.size() <= 0) ) {
			errors.rejectValue("selectedInstructors", "error.instructor.addCourseInstructor.dfcCourse.save.failed");
			log.debug("Add Instructor-Course association failed. " + errors.getModel().size());
			
			return showPage(request, errors, getCurrentPage(request));
		}
		
		// Fill out following information required for closeTemplate
		context.put("courseId", form.getCourseId());
		context.put("action", "advanceSearch");		

		return new ModelAndView(closeTemplate, "context", context);	
	}	
	
	private void setSelectedInstructorItems (CourseInstructorForm form) {
		
		if ( form.getInstructors().size() > 0 ) {
			List<InstructorItemForm> selectedInstructorList = form.getSelectedInstructors();
			
			for ( InstructorItemForm instructorItem : form.getInstructors() ) {
				boolean found = false;
				int index  = -1;
				for (InstructorItemForm selIns : selectedInstructorList ){
					if (selIns.compareTo(instructorItem) == 0) {
						found = true;
						index = selectedInstructorList.indexOf(selIns);
						break;
					}
				}
				
				if ( (!found) && (instructorItem.isSelected()) ) {
					// Add to selected list only IF it doesn't exists to avoid duplicates
					selectedInstructorList.add(instructorItem);
				}
				else if ( found && (!instructorItem.isSelected()) ) {
					//Remove the item from selected list
					if (index > -1) {
						selectedInstructorList.remove(index);
					}
				}
			}
			// Save the updated list
			form.setSelectedInstructors(selectedInstructorList);			
		}
	}
	
	@SuppressWarnings("unchecked")
	private void searchInstructor (CourseInstructorForm form, HttpServletRequest request ) {
		
		log.debug("IN searchInstructor");
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		form.setSearchResultsPageSize( SEARCH_RESULT_PAGE_SIZE );
				
		// Override parameters based on search type		
		if ( form.getAction().equals(MANAGE_USER_SHOW_ALL_ACTION) ) {
			Brander brander = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			String showLimit = brander.getBrandElement("lms.resultSet.showAll.Limit").trim();
			form.setSearchResultsPageSize( Integer.parseInt(showLimit) );
			form.setPageIndex(0);
		}
		else if ( form.getAction().equals(MANAGE_USER_SHOW_PREV_ACTION) ) {
			int pIndex = form.getPageIndex() <= 0 ? 0 : (form.getPageIndex() - 1);
			form.setPageIndex( pIndex );			
		}
		else if ( form.getAction().equals(MANAGE_USER_SHOW_NEXT_ACTION) ) {
			int pIndex = form.getPageIndex() < 0 ? 0 : (form.getPageIndex() + 1);
			form.setPageIndex( pIndex );
		}
		
		/* [9/24/2010] LMS-7106 :: Content Owner should be one of
		 *	1. Content owner tied to the customer of the learner
		 *	2. Content owner tied to the reseller of the customer of the learner
		 */
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		com.softech.vu360.lms.vo.ContentOwner contentOwner = loggedInUser.getLearner().getCustomer().getContentOwner(); 
		if ( contentOwner == null ) {
			contentOwner = loggedInUser.getLearner().getCustomer().getDistributor().getContentOwner();
		}		
		Long contentOwnerId = ( contentOwner == null ) ? 0 : contentOwner.getId();
		
		List<InstructorItemForm> instructorItemList = new ArrayList<InstructorItemForm>();
		result = this.instructorService.getInstructorsForCourseAssociation(form.getCourseId(), form.getSearchFirstName(), form.getSearchLastName(), contentOwnerId, form.getPageIndex(), form.getSearchResultsPageSize(), form.getSortDirection(), form.getSortColumn());
		if (! result.isEmpty() ) {
			List<Instructor> instructorList = (List<Instructor>) result.get("instructorList");
			instructorItemList = (List<InstructorItemForm>) instructorService.getInstructorItemListFromInstructors(instructorList);
			
			form.setInstructors( instructorItemList );
			form.setTotalRecords( Integer.parseInt( result.get("totalRecords").toString() ) );
			
			if ( form.getAction().equals(MANAGE_USER_SHOW_ALL_ACTION) ) {
				form.setRecordShowing(form.getInstructors().size());		
			}
			else {
				int recordShowing = form.getInstructors().size() < SEARCH_RESULT_PAGE_SIZE ? form.getTotalRecords() : (form.getPageIndex() + 1) * SEARCH_RESULT_PAGE_SIZE;
				form.setRecordShowing(recordShowing);
			}
		}
	}

	/**
	 * @param instructorService the instructorService to set
	 */
	public void setInstructorService(InstructorService instructorService) {
		this.instructorService = instructorService;
	}

	/**
	 * @return the instructorService
	 */
	public InstructorService getInstructorService() {
		return instructorService;
	}

	/**
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	/**
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

}
