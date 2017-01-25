/**
 * 
 */
package com.softech.vu360.lms.web.controller.instructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AssignInstructorForm;
import com.softech.vu360.util.InstructorSort;

/**
 * @author Noman
 *
 */


public class AddSynchronousClassInstructorController extends AbstractWizardFormController{
	private static final Logger log = Logger.getLogger(AddSynchronousClassInstructorController.class.getName());
	
	private SynchronousClassService synchronousClassService;
	
	private VelocityEngine velocityEngine;
	private String closeTemplate = null;
	private static final String MANAGE_INSTRUCTOR_ADVANCESEARCH_ACTION = "advanceSearch";

	public AddSynchronousClassInstructorController() {
		super();
		setCommandName("assignInstructorForm");
		setCommandClass(AssignInstructorForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"instructor/manageSynchronousClass/addInstructor/Step1",
				"instructor/manageSynchronousClass/addInstructor/Step2", 
				"instructor/manageSynchronousClass/addInstructor/Step3"});
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
		log.debug(request.getParameter("id"));
		log.debug("IN formBackingObject in addSynchrounous class ");
		Object command = super.formBackingObject(request);
		AssignInstructorForm assignInstructorForm = (AssignInstructorForm)command;
		log.debug("formBackingObject classId : " + assignInstructorForm.getId());

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
		// TODO Auto-generated method stub
		super.initBinder(request, binder);
	}

	/**
	 * Step 3.
	 * We do not need to override this method now.
	 * Since we have bindOnNewForm property set to true in the constructor
	 * this method will be called when the first request is processed.
	 * We can add custom implentation here later to populate the command object
	 * accordingly.
	 * Called on the first request to this form wizard.
	 */
	protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException binder) throws Exception {
		log.debug("IN onBindOnNewForm");
		// TODO Auto-generated method stub
		
		super.onBindOnNewForm(request, command, binder);
	}

	/**
	 * Step 4.
	 * Shows the first form view.
	 * Called on the first request to this form wizard.
	 */
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException binder) throws Exception {
		log.debug("IN showForm");
		// check for customer selection
		
		ModelAndView modelNView = super.showForm(request, response, binder);
		String view = modelNView.getViewName();
		log.debug("OUT showForm for view = "+view);
		return modelNView;
	}

	/**
	 * Called by showForm and showPage ... get some standard data for this page
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		String action = null; 
		
		AssignInstructorForm assignInstructorForm = (AssignInstructorForm)command;
		
		log.debug("referenceData classId : " + assignInstructorForm.getId());
		
		Map model = new HashMap();

		switch(page){
		case 0:
/*			action = request.getParameter("action");
			action = (action==null)?MANAGE_INSTRUCTOR_ADVANCESEARCH_ACTION:action;
			
			if (action.equalsIgnoreCase(MANAGE_INSTRUCTOR_ADVANCESEARCH_ACTION)) {
*/				
			assignInstructorForm.setInstructors(null);
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			Long classId = assignInstructorForm.getId();
			
			log.debug("page 0 referenceData classId : " + assignInstructorForm.getId());
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
			
			List<Instructor> instructors = synchronousClassService.getInstructors2AssignSynchClass(classId, firstName, lastName, loggedInUser.getContentOwner());
			assignInstructorForm.setInstructors(instructors);
//			model.put("instructors", instructors);
			break;

		case 1:
			log.debug("its in page 1");

			Vector<Long> instructorIds = new Vector<Long>();
			List<Instructor> selectedInstructors = new ArrayList<Instructor>();
			
			if (assignInstructorForm.getSelectedInstructorIds() != null && assignInstructorForm.getSelectedInstructorIds().length > 0) {
				for (String selectedInstructorId:assignInstructorForm.getSelectedInstructorIds()) {
					Long instructorId = new Long(selectedInstructorId);
					for (Instructor instructor:assignInstructorForm.getInstructors()) {
						if (instructorId.equals(instructor.getId())) 
							selectedInstructors.add(instructor);
					}
				}
				assignInstructorForm.setSelectedInstructors(selectedInstructors);
			} 
			break;
			
		case 2:
			log.debug("page 1 referenceData classId : " + assignInstructorForm.getId());
/*			classForm.initClassSessionList();
			classForm.generateSessions();
			model.put("sessionList", classForm.getClassSessionList());
			classForm.initWeekDaysList();
*/			break;

		default:
			break;
		}
		
		return super.referenceData(request, page);
//		return model;
	}

	/**
	 * Added by Dyutiman
	 * Used for sorting...
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		AssignInstructorForm form = (AssignInstructorForm)command;
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
//		HttpSession session = null;
//		session = request.getSession();

		if( page == 0 ) {
			if( !form.getAction().isEmpty() && form.getAction().equalsIgnoreCase("sort") ) {

				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				

				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				Long classId = form.getId();

				List<Instructor> instructors = synchronousClassService.getInstructors2AssignSynchClass(classId, firstName, lastName, loggedInUser.getContentOwner());
				
				String sortColumnIndex = form.getSortColumnIndex();
//				if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//					sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
				String sortDirection = form.getSortDirection();
//				if( sortDirection == null && session.getAttribute("sortDirection") != null )
//					sortDirection = session.getAttribute("sortDirection").toString();
				String pageIndex = form.getPageCurrIndex();
				if( pageIndex == null ) pageIndex = form.getPageIndex();

				if( sortColumnIndex != null && sortDirection != null ) {
					// sorting against First name
					if( sortColumnIndex.equalsIgnoreCase("0") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {

							InstructorSort sort = new InstructorSort();
							sort.setSortBy("firstName");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(instructors,sort);
							form.setSortDirection("0");
							form.setSortColumnIndex("0");
//							session.setAttribute("sortDirection", 0);
//							session.setAttribute("sortColumnIndex", 0);

						} else {
							InstructorSort sort = new InstructorSort();
							sort.setSortBy("firstName");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(instructors,sort);
							form.setSortDirection("1");
							form.setSortColumnIndex("0");
//							session.setAttribute("sortDirection", 1);
//							session.setAttribute("sortColumnIndex", 0);

						}
						// sorting against Last name
					} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {

							InstructorSort sort = new InstructorSort();
							sort.setSortBy("lastName");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(instructors,sort);
							form.setSortDirection("0");
							form.setSortColumnIndex("1");
//							session.setAttribute("sortDirection", 0);
//							session.setAttribute("sortColumnIndex", 1);
						} else {
							InstructorSort sort=new InstructorSort();
							sort.setSortBy("lastName");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(instructors,sort);
							form.setSortDirection("1");
							form.setSortColumnIndex("1");
//							session.setAttribute("sortDirection", 1);
//							session.setAttribute("sortColumnIndex", 1);
						}
					}
				}
				pagerAttributeMap.put("pageIndex", pageIndex);
				pagerAttributeMap.put("showAll", form.getShowAll());
				request.setAttribute("PagerAttributeMap", pagerAttributeMap);

				form.setInstructors(instructors);
			}
		}
		super.postProcessPage(request, command, errors, page);
	}
	
	/**
	 * The Validator's default validate method WILL NOT BE CALLED by a wizard form controller!
	 * We need to do implementation specific - page by page - validation
	 * by explicitly calling the validateXXX function in the validator
	 */
	protected void validatePage(Object command, Errors errors, int page) {

		log.debug("There is no validation in assign instructor wizard");
		log.debug("IN validatePage");

/*		AssignInstructorForm assignInstructorForm = (AssignInstructorForm)command;
		AddSynchronousClassScheduleValidator addSynClassValidator = (AddSynchronousClassScheduleValidator) getValidator();

		errors.setNestedPath("");
		switch (page) {

		case 0:
			addSynClassValidator.validatePage1(classForm, errors);
			break;
		case 1:
			addSynClassValidator.validatePage2(classForm, errors);
			break;
		default:
			break;
		}
*/		
		errors.setNestedPath("");
	
	}
	
	//super.validatePage(command, errors, page);

	/**
	 * we can do custom processing after binding and validation
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException error, int page) throws Exception {
		log.debug("IN onBindAndValidate");
		// TODO Auto-generated method stub
		super.onBindAndValidate(request, command, error, page);
	}

	//16.01.2009
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		// TODO Auto-generated method stub
		AssignInstructorForm assignInstructorForm = (AssignInstructorForm)command;
		
		log.debug("onBind classId : " + assignInstructorForm.getId());

		int Page=getCurrentPage(request);
		if (Page==1){
			
		}
			super.onBind(request, command, errors);
	}

	//16.01.2009
	@Override
	protected int getCurrentPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return super.getCurrentPage(request);
	}

	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		/*if(currentPage==1 
				&& request.getParameter("action")!=null
				&& request.getParameter("action").equals("getLearnerGroup"))
			return 1; */
		return super.getTargetPage(request, command, errors, currentPage);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		// TODO Auto-generated method stub
		//return super.processCancel(request, response, command, error);
		return new ModelAndView(closeTemplate);
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		AssignInstructorForm assignInstructorForm = (AssignInstructorForm)command;
		
		synchronousClassService.assignInstructors2SynchClass(assignInstructorForm);
		Map<Object, Object> context = new HashMap<Object, Object>();
//		context.put("Id", classForm.getId());
		return new ModelAndView("redirect:ins_editSynchronousClassInstructor.do?id="+assignInstructorForm.getId());
	}

	public String getCloseTemplate() {
		return closeTemplate;
	}

	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	
	
}
