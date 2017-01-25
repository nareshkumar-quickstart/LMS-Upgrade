package com.softech.vu360.lms.web.controller.instructor;

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
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.instructor.LocationForm;
import com.softech.vu360.lms.web.controller.validator.Instructor.LocationValidator;
import com.softech.vu360.util.ManageLocationsSort;

/**
 * @author Saptarshi
 */
public class ManageSynchronousClassLocationController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageAndEditLocationController.class.getName());
	
	private SynchronousClassService synchronousClassService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private ResourceService resourceService = null;
	private String manageLocationTemplate = null;
	private String addLocationTemplate = null;
	private String editClassRedirectTemplate = null;

	public ManageSynchronousClassLocationController() {
		super();
	}

	public String getEditClassRedirectTemplate() {
		return editClassRedirectTemplate;
	}

	public void setEditClassRedirectTemplate(String editClassRedirectTemplate) {
		this.editClassRedirectTemplate = editClassRedirectTemplate;
	}

	public ManageSynchronousClassLocationController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		log.debug(" IN handleNoSuch Method ");
		return new ModelAndView(manageLocationTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {

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
			Long classId=new Long(0);
			HttpSession session=request.getSession(true);
			if(request.getParameter("id")!=null){
				classId=new Long(Long.parseLong(request.getParameter("id").toString()));
			}

			Map<Object, Object> context = new HashMap<Object, Object>();
			//Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String locationName = null;
			String city = null;
			String state = null;
			String zip = null;

			if (request.getParameter("showAll") !=null && request.getParameter("showAll").equalsIgnoreCase("true")  )
			{
				locationName =(session.getAttribute("locationName") == null) ? "" : session.getAttribute("locationName").toString() ; 
				city = (session.getAttribute("city") == null) ? "" : session.getAttribute("city").toString() ;
				state = (session.getAttribute("state") == null) ? "" : session.getAttribute("state").toString() ;
				zip = (session.getAttribute("zip") == null) ? "" : session.getAttribute("zip").toString() ;


			}else{
				locationName = (request.getParameter("locationName") == null) ? "" : request.getParameter("locationName");
				city = (request.getParameter("city") == null) ? "" : request.getParameter("city");
				state = (request.getParameter("state") == null) ? "" : request.getParameter("state");
				zip = (request.getParameter("zip") == null) ? "" : request.getParameter("zip");


				session.setAttribute("locationName", locationName);
				session.setAttribute("city", city);
				session.setAttribute("state", state);
				session.setAttribute("zip", zip);
			}


			context.put("locationName", locationName);
			context.put("city", city);
			context.put("state", state);
			context.put("zip", zip);

			SynchronousClass synchronousClass=synchronousClassService.getSynchronousClassById(classId);

			List<Location> locationList = new ArrayList<Location>();
			if(synchronousClass.getLocation()!=null) {
				locationList.add(synchronousClass.getLocation());
			}

			//============================For Sorting============================
			Map<String,String> pagerAttributeMap = new HashMap<String,String>();
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
			if( sortDirection == null && session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = request.getParameter("pageCurrIndex");
			if( pageIndex == null && session.getAttribute("pageCurrIndex") != null) pageIndex = session.getAttribute("pageCurrIndex").toString();

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
			context.put("synchClass",synchronousClass);
			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			return new ModelAndView(manageLocationTemplate,"context",context);
		}catch(Exception e){
			log.debug(e);
		}
		return new ModelAndView(manageLocationTemplate);
	}

	public ModelAndView addLocation( HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			//Long contentOwnerId;
			Long classId=new Long(0);
			HttpSession session=request.getSession(true);
			if(request.getParameter("id")!=null){
				classId=new Long(Long.parseLong(request.getParameter("id").toString()));
			}
			Map<Object, Object> context = new HashMap<Object, Object>();
			//Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
			getPrincipal();
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
			if( pageIndex == null && session.getAttribute("pageCurrIndex") != null) pageIndex = session.getAttribute("pageCurrIndex").toString();

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
			context.put("classId", classId);
			session.setAttribute("pageCurrIndex", pageIndex);
			pagerAttributeMap.put("showAll", (request.getParameter("showAll") == null) ? "false" : request.getParameter("showAll"));
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
			return new ModelAndView(addLocationTemplate,"context",context);
		}catch(Exception e){
			log.debug(e);
		}
		return new ModelAndView(addLocationTemplate);
	}

	public ModelAndView associateLocation( HttpServletRequest request, HttpServletResponse response) throws Exception {
		long locId = 0L;
		if( request.getParameter("locationId") != null ) {
			locId = Long.parseLong(request.getParameter("locationId").toString());
		}
		Long classId = new Long(0);
		if( request.getParameter("classId") != null ) {
			classId = new Long(Long.parseLong(request.getParameter("classId").toString()));
		}
		Location location = resourceService.getLocationById(locId);
		SynchronousClass synchronousClass = synchronousClassService.loadForUpdateSynchronousClass(classId);
		synchronousClass.setLocation(location);
		synchronousClassService.saveSynchronousClass(synchronousClass);
		return new ModelAndView(editClassRedirectTemplate,"classId",classId);
	}

	public ModelAndView deassociateLocation(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//long locId=0L;
		Long classId = new Long(0);
		if( request.getParameter("id") != null ) {
			classId = new Long(Long.parseLong(request.getParameter("id").toString()));
		}
		SynchronousClass synchronousClass = synchronousClassService.loadForUpdateSynchronousClass(classId);
		synchronousClass.setLocation(null);
		synchronousClassService.saveSynchronousClass(synchronousClass);
		return (this.searchLocation(request,response));
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public String getManageLocationTemplate() {
		return manageLocationTemplate;
	}

	public void setManageLocationTemplate(String manageLocationTemplate) {
		this.manageLocationTemplate = manageLocationTemplate;
	}

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	public String getAddLocationTemplate() {
		return addLocationTemplate;
	}

	public void setAddLocationTemplate(String addLocationTemplate) {
		this.addLocationTemplate = addLocationTemplate;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

}