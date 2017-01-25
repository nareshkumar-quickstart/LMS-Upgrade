package com.softech.vu360.lms.web.controller.instructor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ResourceType;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.instructor.ResourceTypeForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.ResourceTypeValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author Saptarshi
 */
public class AddResourceTypeWizardController extends AbstractWizardFormController{
	
	private static final Logger log = Logger.getLogger(AddResourceTypeWizardController.class.getName());

	private String closeTemplate = null;
	private ResourceService resourceService;
	public AddResourceTypeWizardController() {
		super();
		setCommandName("resourceTypeForm");
		setCommandClass(ResourceTypeForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"instructor/manageResources/resourceType/addResourceType/step1"
				, "instructor/manageResources/resourceType/addResourceType/step2"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		ResourceTypeForm form = (ResourceTypeForm) command;
		switch(page){

		case 0:
			break;
		case 1:
			break;
		}
		return super.referenceData(request, page);
	}

	/**
	 * method used to correct the country and state labels in form bean.
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) 
	throws Exception {

		super.postProcessPage(request, command, errors, page);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		ResourceTypeForm form = (ResourceTypeForm) command;
		try {
			if(form.getResourceType() != null) {
				VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
				
				ResourceType resourceType = new ResourceType();
				resourceType.setName(form.getResourceType().getName());
				resourceType.setDescription(form.getResourceType().getDescription());
				resourceType.setActive(true);
				
				resourceType.setContentOwner(loggedInUser.getLearner().getCustomer().getContentOwner());
				
				resourceType=resourceService.saveResourceType(resourceType);
			}
		} catch (Exception e) {
			log.debug(e);
		}
		return new ModelAndView(closeTemplate);
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		ResourceTypeValidator validator = (ResourceTypeValidator)this.getValidator();
		ResourceTypeForm form = (ResourceTypeForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			validator.validateAddResouceType(form, errors);
			break;
		case 1:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(closeTemplate);
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

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

}
