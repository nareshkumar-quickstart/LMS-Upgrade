package com.softech.vu360.lms.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Location;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.instructor.LocationForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.LocationValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Dyutiman
 * created on 25 Mar 2010
 * Modified by: PG changed from MultiActionController to VU360BaseMultiActionController
 *
 */
public class AddLocationController extends VU360BaseMultiActionController {
	
	private ResourceService resourceService = null;
	private String addLocationTemplate = null;
	private String redirectTemplate = null;
	private String manageLocationTemplate = null;
	
	
	public String getManageLocationTemplate() {
		return manageLocationTemplate;
	}

	public void setManageLocationTemplate(String manageLocationTemplate) {
		this.manageLocationTemplate = manageLocationTemplate;
	}

	public AddLocationController() {
		super();
		setCommandName("locationForm");
		setCommandClass(LocationForm.class);
	}

	public AddLocationController(Object delegate) {
		super(delegate);
		setCommandName("locationForm");
		setCommandClass(LocationForm.class);
	}
	
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, 
			HttpServletResponse response ) throws Exception {
		LocationForm form = new LocationForm();
		return new ModelAndView(addLocationTemplate,"locationForm",form);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof LocationForm ){
			LocationForm form = (LocationForm)command;
			if( methodName.equals("editLocation") ) {
				
				Location location = null;
				String id = request.getParameter("id");
				if( id != null ) {
					location = resourceService.getLocationById(Long.parseLong(id));
					form.setLocation(location);
					form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
				} else {
					//form.setLocation(resourceType);
				}
			} 
		}
	}
	
	protected void validate(HttpServletRequest request, Object command,	BindException errors, String methodName) throws Exception {
		LocationValidator validator = (LocationValidator)this.getValidator();
		LocationForm form = (LocationForm)command;
		form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
		if( methodName.equals("saveLocation")) {
			validator.validateLocationPage(form, errors);
		}
	}
	
	public ModelAndView saveLocation( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		LocationForm form = (LocationForm)command;
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		if( errors.hasErrors() ) {
			form.setLocation(form.getLocation());
			return new ModelAndView(addLocationTemplate);
		} else {
			try {
				
				Location location = new Location();
				location.setName(form.getLocation().getName());
				
				Address address = new Address();
				address.setStreetAddress(form.getStreetAddress());
				address.setStreetAddress2(form.getStreetAddress2());
				address.setCity(form.getCity());
				address.setCountry(form.getCountry());
				address.setState(form.getState());
				address.setZipcode(form.getZipcode());
				location.setAddress(address);
				
				location.setPhone(form.getLocation().getPhone());
				location.setContentowner(loggedInUser.getLearner().getCustomer().getContentOwner());
				location.setDescription(form.getDescription());
				// Saving the location...
				location = resourceService.saveLocation(location);
				return new ModelAndView(manageLocationTemplate);
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		return new ModelAndView(addLocationTemplate);
	}

	public String getAddLocationTemplate() {
		return addLocationTemplate;
	}

	public void setAddLocationTemplate(String addLocationTemplate) {
		this.addLocationTemplate = addLocationTemplate;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
}