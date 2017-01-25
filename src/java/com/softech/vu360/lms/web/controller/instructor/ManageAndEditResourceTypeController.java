package com.softech.vu360.lms.web.controller.instructor;

import java.util.Collections;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Resource;
import com.softech.vu360.lms.model.ResourceType;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.instructor.ResourceTypeForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.ResourceTypeValidator;
import com.softech.vu360.util.ManageResourceTypeSort;

/**
 * @author Saptarshi
 */
public class ManageAndEditResourceTypeController extends VU360BaseMultiActionController {
	
	private static final Logger log = Logger.getLogger(ManageAndEditResourceTypeController.class.getName());
	
	private String manageResourceTypeTemplate = null;
	private String editResourceTypeTemplate = null;
	private String redirectTemplate=null;
	private ResourceService resourceService;
	
	private HttpSession session = null;
	
	public ManageAndEditResourceTypeController() {
		super();
	}

	public ManageAndEditResourceTypeController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, 
			HttpServletResponse response ) throws Exception {

        return resourceType(request, response, null, null);

	}

    public ModelAndView resourceType( HttpServletRequest request,HttpServletResponse response, 	Object command, BindException errors ) throws Exception {
        try {
            session = request.getSession();
            log.debug(" IN handleNoSuch Method ");
            com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<ResourceType> resourceTypes = resourceService.getAllResourceTypes(loggedInUser.getLearner().getCustomer().getContentOwner().getId());
            Map<Object, Object> context = new HashMap<Object, Object>();
            Map<String,String> pagerAttributeMap = new HashMap<String,String>();

            context.put("resourceTypes", resourceTypes);
            //context.put("showAll", true);
            //		session.setAttribute("pageCurrIndex", 0);
            pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
            request.setAttribute("PagerAttributeMap", pagerAttributeMap);

            context.put("deleteError", false);
            if( session.getAttribute("deleteError") != null )
                context.put("deleteError", session.getAttribute("deleteError"));

            return new ModelAndView(manageResourceTypeTemplate,"context",context);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView(manageResourceTypeTemplate);
    }

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof ResourceTypeForm ){
			ResourceTypeForm form = (ResourceTypeForm)command;
			if( methodName.equals("editResourceType")){
				ResourceType resourceType = new ResourceType();
				String id = request.getParameter("resourceTypeId");
				if( id != null ) {
					resourceType = resourceService.loadForUpdateResourceType(Long.parseLong(id));
					form.setId(Long.parseLong(id));
					form.setResourceType(resourceType);
				} else {
					//resourceType = accreditationService.getProviderById(form.getProvId());
					form.setResourceType(resourceType);
				}
			}
		}
	}
	
	protected void validate(HttpServletRequest request, Object command,	BindException errors, String methodName) throws Exception {
		ResourceTypeValidator validator = (ResourceTypeValidator)this.getValidator();
		ResourceTypeForm form = (ResourceTypeForm)command;
		if( methodName.equals("saveResourceType")) {
			validator.validateAddResouceType(form, errors);
		}
	}
	
	public ModelAndView searchResourceTypes( HttpServletRequest request,HttpServletResponse response ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		try {
//			session = request.getSession();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<ResourceType> resourceTypes = resourceService.getAllResourceTypes(loggedInUser.getLearner().getCustomer().getContentOwner().getId());
			//Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			Map<String,String> pagerAttributeMap = new HashMap<String,String>();
			
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = request.getParameter("pageCurrIndex");
//			if( pageIndex == null ) pageIndex = session.getAttribute("pageCurrIndex").toString();

			if( sortColumnIndex != null && sortDirection != null ) {

				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageResourceTypeSort sort = new ManageResourceTypeSort();
						sort.setSortBy("resourceTypeName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resourceTypes,sort);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						ManageResourceTypeSort sort = new ManageResourceTypeSort();
						sort.setSortBy("resourceTypeName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resourceTypes,sort);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				} 
			}	
			//context.put("resourceList", resourceTypes);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			context.put("showAll",  (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
//			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			context.put("resourceTypes", resourceTypes);
			context.put("deleteError", false);
			if( session.getAttribute("deleteError") != null )
				context.put("deleteError", session.getAttribute("deleteError"));

			session.removeAttribute("deleteError");
		}catch(Exception e){
			log.debug(e);
		}
		return new ModelAndView(manageResourceTypeTemplate,"context",context);
	}
	
	public ModelAndView editResourceType( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		//ResourceTypeForm form = (ResourceTypeForm)command;
		return new ModelAndView(editResourceTypeTemplate);
	}
	
	public ModelAndView saveResourceType( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		
		ResourceTypeForm form = (ResourceTypeForm) command;
		if( errors.hasErrors() ) {
			return new ModelAndView(editResourceTypeTemplate);
		}
		try {
			if(form.getResourceType() != null) {
				ResourceType resourceType = form.getResourceType();
				resourceService.saveResourceType(resourceType);
			}
		} catch (Exception e) {
			log.debug(e);
			return new ModelAndView(editResourceTypeTemplate);
		}
		return new ModelAndView(redirectTemplate);
	}

	public ModelAndView deleteResourceTypes( HttpServletRequest request,HttpServletResponse response ) throws Exception {
		
		session = request.getSession();
		String[] selectedResourceTypeIds = request.getParameterValues("selectedResourceTypes");
		
		if ( selectedResourceTypeIds != null ) {
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long resourceTypeIdArray[] = new long[selectedResourceTypeIds.length];
			boolean hasResources = false;
			for( int loop = 0 ; loop < selectedResourceTypeIds.length ; loop++ ) {
				String delResourceTypeId = selectedResourceTypeIds[loop];
				if( !StringUtils.isBlank(delResourceTypeId) ) {
					List<Resource> resources = resourceService.findResources( loggedInUser.getLearner().getCustomer().getContentOwner().getId(), "", "",
							delResourceTypeId );
					if( resources == null || resources.size() == 0 ) {
						resourceTypeIdArray[loop] = Long.parseLong(delResourceTypeId);
					} else {
						hasResources = true;
					}
				}
			}
			if( hasResources ) {
				session.setAttribute("deleteError", "error.instructor.deleteResourceType.associated");
				return this.searchResourceTypes(request, response);
			} else {
				resourceService.deleteResourceType(resourceTypeIdArray);
			}
		}
		return new ModelAndView(redirectTemplate);
	}
	
	/*
	 * getters & setters 
	 */
	
	/**
	 * @return the manageResourceTypeTemplate
	 */
	public String getManageResourceTypeTemplate() {
		return manageResourceTypeTemplate;
	}

	/**
	 * @param manageResourceTypeTemplate the manageResourceTypeTemplate to set
	 */
	public void setManageResourceTypeTemplate(String manageResourceTypeTemplate) {
		this.manageResourceTypeTemplate = manageResourceTypeTemplate;
	}

	/**
	 * @return the editResourceTypeTemplate
	 */
	public String getEditResourceTypeTemplate() {
		return editResourceTypeTemplate;
	}

	/**
	 * @param editResourceTypeTemplate the editResourceTypeTemplate to set
	 */
	public void setEditResourceTypeTemplate(String editResourceTypeTemplate) {
		this.editResourceTypeTemplate = editResourceTypeTemplate;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

}