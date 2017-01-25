/**
 * 
 */
package com.softech.vu360.lms.web.controller.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.SessionForm;
import com.softech.vu360.lms.web.controller.validator.AddSynchronousSessionRecurrenceValidator;

/**
 * @author Tahir Mehmood
 *
 */


public class ManagerAddSynchronousSessionRecurrenceController extends AbstractWizardFormController{
	private static final Logger log = Logger.getLogger(ManagerAddSynchronousSessionRecurrenceController.class.getName());
	
	private SynchronousClassService synchronousClassService;
	
	private VelocityEngine velocityEngine;
	private String closeTemplate = null;
	

	public ManagerAddSynchronousSessionRecurrenceController() {
		super();
		setCommandName("sessionForm");
		setCommandClass(SessionForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"manager/manageSynchronousClass/addRecurrence/Step1",
				"manager/manageSynchronousClass/addRecurrence/Step2"});
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
		SessionForm sessionForm = (SessionForm)command;
		log.debug("formBackingObject startDate : " + sessionForm.getStartDate());
		log.debug("formBackingObject endDate : " + sessionForm.getEndDate());

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
		log.debug("IN referenceData");
		SessionForm sessionForm = (SessionForm)command;
		
		log.debug("referenceData startDateTime : " + sessionForm.getStartDateTime());
		log.debug("referenceData endDateTime : " + sessionForm.getEndDateTime());
		
		Map model = new HashMap();

		switch(page){
		case 0:
			log.debug("page 0 referenceData statDate : " + sessionForm.getStartDate());
			log.debug("page 0 referenceData endDate : " + sessionForm.getEndDate());
			break;

		case 1:
			break;
		case 2:
			log.debug("page 1 referenceData statDate : " + sessionForm.getStartDate());
			log.debug("page 1 referenceData endDate : " + sessionForm.getEndDate());

//			sessionForm.initClassSessionList();
//			sessionForm.generateSessions();
//			model.put("sessionList", classForm.getClassSessionList());
//			classForm.initWeekDaysList();
			break;

		default:
			break;
		}
		
		return super.referenceData(request, page);
		//return model;
	}

	/**
	 * The Validator's default validate method WILL NOT BE CALLED by a wizard form controller!
	 * We need to do implementation specific - page by page - validation
	 * by explicitly calling the validateXXX function in the validator
	 */
	protected void validatePage(Object command, Errors errors, int page) {

		log.debug("IN validatePage");

		// TODO Auto-generated method stub
		SessionForm sessionForm = (SessionForm) command;

		AddSynchronousSessionRecurrenceValidator addSynSessionRecurrenceValidator = (AddSynchronousSessionRecurrenceValidator) getValidator();
		log.debug("startDate " + sessionForm.getStartDate());
		log.debug("endDate " + sessionForm.getEndDate());
		log.debug("startTime " + sessionForm.getStartTime());
		log.debug("endTime " + sessionForm.getEndTime());
		

		errors.setNestedPath("");
		switch (page) {

		case 0:
			addSynSessionRecurrenceValidator.validatePage1(sessionForm, errors);
			break;
		default:
			break;
		}
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
		SessionForm sessionForm = (SessionForm)command;
		
		log.debug("onBind StartDate : " + sessionForm.getStartDate());
		log.debug("onBind EndDate : " + sessionForm.getEndDate());

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
		if(currentPage==1 
				&& request.getParameter("action")!=null
				&& request.getParameter("action").equals("getLearnerGroup"))
			return 1;
		return super.getTargetPage(request, command, errors, currentPage);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {
		log.debug("IN processCancel");
		// TODO Auto-generated method stub
		//return super.processCancel(request, response, command, error);
		return new ModelAndView(closeTemplate);
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException error) throws Exception {

		List<SynchronousSession> synchronousSessions = null;
		SessionForm sessionForm = (SessionForm)command;
		/**
		 * this getId() will return the Synchronous ClassId
		 * */
		Long classId = sessionForm.getId();
		
		/**
		 * this function will first see if the session already exists 
		 * it will not create any duplicate sessions
		 * Within the classId, session can't be duplicated 
		 * SessionForm has startDateTime and endDateTime, so it 
		 * will be easy to get the unique startDateTime and endDateTime
		 * within the class (id)
		 * */
		synchronousSessions = synchronousClassService.findSynchronousSessionByClassId(classId, sessionForm);
		if (synchronousSessions.size() == 0)
			synchronousClassService.addSynchronousSession(sessionForm);
		else 
			synchronousClassService.saveSynchronousSession(synchronousSessions.get(0));
		
			Map<Object, Object> context = new HashMap<Object, Object>();
//		context.put("Id", classForm.getId());
		return new ModelAndView("redirect:mgr_editSynchronousClassSchedule.do?id="+classId);
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
