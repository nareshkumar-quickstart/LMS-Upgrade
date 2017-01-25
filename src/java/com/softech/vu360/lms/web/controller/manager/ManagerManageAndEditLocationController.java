package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Location;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.instructor.LocationForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.LocationValidator;
import com.softech.vu360.util.ManageLocationsSort;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Saptarshi
 */
public class ManagerManageAndEditLocationController extends VU360BaseMultiActionController {
	
private static final Logger log = Logger.getLogger(ManagerManageAndEditLocationController.class.getName());
	private ResourceService resourceService=null;
	private String manageLocationTemplate = null;
	private String editLocationTemplate = null;

	public ManagerManageAndEditLocationController() {
		super();
	}

	public ManagerManageAndEditLocationController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		log.debug(" IN handleNoSuch Method ");
		return new ModelAndView(manageLocationTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof LocationForm ){
			LocationForm form = (LocationForm)command;
			if( methodName.equals("editLocation") ) {
				
				Location location=null;
				String id = request.getParameter("id");
				if( id != null ) {
					location = resourceService.getLocationById(Long.parseLong(id));
					form.setLocation(location);
					form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));
					form.setId(location.getId());
				} 
			} 
		}
		
	}
	
	protected void validate(HttpServletRequest request, Object command,	BindException errors, String methodName) throws Exception {
		LocationValidator validator = (LocationValidator)this.getValidator();
		LocationForm form = (LocationForm)command;
		if( methodName.equals("saveLocation")) {
			validator.validateLocationPage(form, errors);
	}
	}
	
	public ModelAndView searchLocation( HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			Long contentOwnerId;
			
			HttpSession session= request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Location> locationList = new ArrayList<Location>();
			String locationName = null;
			String city = null;
			String state = null;
			String country = null;
			String zip = null;
			
			if (request.getParameter("showAll") !=null && request.getParameter("showAll").equalsIgnoreCase("true")  )
			{
				 locationName =(session.getAttribute("locationName") == null) ? "" : session.getAttribute("locationName").toString() ; 
				 city = (session.getAttribute("city") == null) ? "" : session.getAttribute("city").toString() ;
				 state = (session.getAttribute("state") == null) ? "" : session.getAttribute("state").toString() ;
				 country = (session.getAttribute("country") == null) ? "" : session.getAttribute("country").toString() ;
				 zip = (session.getAttribute("zip") == null) ? "" : session.getAttribute("zip").toString() ;
				
				
			}else{
				 locationName = (request.getParameter("locationName") == null) ? "" : request.getParameter("locationName");
				 city = (request.getParameter("city") == null) ? "" : request.getParameter("city");
				 state = (request.getParameter("state") == null) ? "" : request.getParameter("state");
				 country = (request.getParameter("country") == null) ? "" : request.getParameter("country");
				 zip = (request.getParameter("zip") == null) ? "" : request.getParameter("zip");
				 
				
				 session.setAttribute("locationName", locationName);
				 session.setAttribute("city", city);
				 session.setAttribute("state", state);
				 session.setAttribute("country", country);
				 session.setAttribute("zip", zip);
			}
			
			
			context.put("locationName", locationName);
			context.put("city", city);
			context.put("state", state);
			context.put("country", country);
			context.put("zip", zip);
			
			
			locationList=resourceService.findLocations(loggedInUser.getContentOwner().getId(),locationName,city,state,country,zip);
		
			
			//============================For Sorting============================
			Map<String,String> pagerAttributeMap = new HashMap<String,String>();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
			if( sortDirection == null && session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = request.getParameter("pageCurrIndex");
			if( pageIndex == null ) pageIndex = session.getAttribute("pageCurrIndex").toString();

			if( sortColumnIndex != null && sortDirection != null ) {

				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("locationName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 0);
					} else {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("locationName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 0);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("city");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 1);
					} else {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("city");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 1);
					}
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("state");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 2);
					} else {
						ManageLocationsSort sort = new ManageLocationsSort();
						sort.setSortBy("state");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(locationList,sort);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 2);
					}
				}
			}	
			
			context.put("locationList", locationList);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			context.put("showAll", request.getParameter("showAll"));
			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			return new ModelAndView(manageLocationTemplate,"context",context);
		}catch(Exception e){
			log.debug(e);
		}
		return new ModelAndView(manageLocationTemplate);
	}
	
	public ModelAndView deleteLocation(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String[] selectedLocationIds = request.getParameterValues("location");
		
		if (selectedLocationIds != null) {
			long locationIdArray[] = new long[selectedLocationIds.length];
			for (int loop = 0; loop < selectedLocationIds.length; loop++) {
				String delLocationId = selectedLocationIds[loop];
				if (delLocationId != null)
					locationIdArray[loop] = Long.parseLong(delLocationId);
			}
			/*
			 * this will Inactive Resource
			 */
			resourceService.deleteLocation(locationIdArray);
			
		}
		
		return (this.searchLocation(request, response));
	}
	
	public ModelAndView editLocation( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		LocationForm form = (LocationForm)command;
		
		return new ModelAndView(editLocationTemplate);
	}
	
	public ModelAndView saveLocation( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		LocationForm form = (LocationForm)command;
		if(errors.hasErrors()){
			form.setLocation(form.getLocation());
			return new ModelAndView(editLocationTemplate);
		}
		try {
			
			
			Location location=form.getLocation();
			location.setId(form.getId());
			location = resourceService.saveLocation(location);
			
		}catch (Exception e) {
			log.debug("exception: ", e);
		}
		
		return new ModelAndView(manageLocationTemplate);
	}

	/**
	 * @return the manageLocationTemplate
	 */
	public String getManageLocationTemplate() {
		return manageLocationTemplate;
	}

	/**
	 * @param manageLocationTemplate the manageLocationTemplate to set
	 */
	public void setManageLocationTemplate(String manageLocationTemplate) {
		this.manageLocationTemplate = manageLocationTemplate;
	}

	/**
	 * @return the editLocationTemplate
	 */
	public String getEditLocationTemplate() {
		return editLocationTemplate;
	}

	/**
	 * @param editLocationTemplate the editLocationTemplate to set
	 */
	public void setEditLocationTemplate(String editLocationTemplate) {
		this.editLocationTemplate = editLocationTemplate;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}


}
