package com.softech.vu360.lms.web.controller.instructor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Resource;
import com.softech.vu360.lms.model.ResourceType;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.instructor.AddResourceForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.AddResourceValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author Dyutiman
 * created on 23 Mar 2010
 *
 */
public class AddResourceController extends AbstractWizardFormController {

	private String finishTemplate = null;	
	private ResourceService resourceService;
	public AddResourceController() {
		super();
		setCommandName("addResourceForm");
		setCommandClass(AddResourceForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"instructor/manageResources/addResources/addResourceStep1"
				, "instructor/manageResources/addResources/addResourceStep2"});
	}
	@SuppressWarnings("unchecked")
	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
			getPrincipal();
			AddResourceForm form = (AddResourceForm)command;
			List<ResourceType> resourceTypes= resourceService.getAllResourceTypes(loggedInUser.getLearner().getCustomer().getContentOwner().getId());
			
			form.setResourceTypes(resourceTypes);
		
		} catch (Exception e) {
			//log.debug("exception", e);
		}
		return command;
	}
	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData( HttpServletRequest request, Object command, 
			Errors errors, int page) throws Exception {

		AddResourceForm form = (AddResourceForm)command;

		switch(page) {

		case 0:
			
			
			break;
		case 1:
			
			break;
		default:
			break;
		}
		return super.referenceData(request, page);
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command,
			BindException errors, int page) throws Exception {

		AddResourceValidator validator = (AddResourceValidator)this.getValidator();
		AddResourceForm form = (AddResourceForm)command;
		
		switch(page) {
		case 0:
			validator.validateFirstPage(form, errors);
			break;
		case 1:
			break;
		default:
			break;
		}	
		super.onBindAndValidate(request, command, errors, page);
	}
	
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception {
		
		try {

			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			AddResourceForm form = (AddResourceForm)command;
			Resource resource = new Resource();
			resource.setAssetTagNumber(form.getAssetTagNumber());
			resource.setDescription(form.getDescription());
			resource.setName(form.getName());
			resource.setContentowner(loggedInUser.getLearner().getCustomer().getContentOwner());
			for(ResourceType resourceType: form.getResourceTypes()){
				
				if(resourceType.getId().longValue() ==  form.getResourceTypeId().longValue()){
					
					resource.setResourceType(resourceType);
					break;
				}
			}
			resource=resourceService.saveResource(resource);
		
		} catch (Exception e) {
			//log.debug("exception", e);
		}
		return new ModelAndView(finishTemplate);
	}
	
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		return new ModelAndView(finishTemplate);
	}
	
	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}
	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

}