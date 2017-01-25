package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Resource;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.instructor.ScheduleResourceForm;
import com.softech.vu360.util.ManageResourcesSort;

/**
 * @author Tahir Mehmood
 * created on 7th May 2010
 * 
 */
public class ManagerManageSynchronousSessionResourceController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManagerManageSynchronousSessionResourceController.class.getName());
	private ResourceService resourceService;
	private SynchronousClassService classService;
	
	private String manageResourceTemplate = null;
	
	public ManagerManageSynchronousSessionResourceController() {
		super();
	}

	public ManagerManageSynchronousSessionResourceController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod( NoSuchRequestHandlingMethodException ex, HttpServletRequest request, 
			HttpServletResponse response ) throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		String synClassId = request.getParameter("id"); 
		log.debug("SynClassId - "+synClassId);
		SynchronousClass synClass = classService.getSynchronousClassById(Long.parseLong(synClassId));
		//SynchronousCourse synCourse = synClass.getSynchronousCourse();
		Course synCourse = synClass.getCourse();
		
		// TODO SynchronousSession schedule = classService.getSynchronousSessionByClassId(Long.parseLong(SynClassId));
		List<Resource> resources = synClass.getResources();
		context.put("resources", resources);
		context.put("reNumber", resources.size());
		context.put("synClassId", synClassId);
		context.put("scheduleId", synClass.getId());
		context.put("synchClass", synClass);
		context.put("synchCourseId", synCourse.getId());
		return new ModelAndView(manageResourceTemplate, "context", context);
	}
	
	protected void onBind( HttpServletRequest request, Object command, String methodName ) throws Exception {
		if( command instanceof ScheduleResourceForm ){
			ScheduleResourceForm form = (ScheduleResourceForm)command;
			if( methodName.equals("showResources") ) {
				// do nothing
			} else if( methodName.equals("removeResources") ) {
				String synClassId = request.getParameter("id"); 
				SynchronousClass synClass = classService.getSynchronousClassById(Long.parseLong(synClassId));
				form.setSynClass(synClass);
			}
		}
	}

	public ModelAndView showResources( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			ScheduleResourceForm form = (ScheduleResourceForm)command;
			String synClassId = request.getParameter("id");
//			HttpSession session = request.getSession();
			SynchronousClass synClass = classService.getSynchronousClassById(Long.parseLong(synClassId));
			// SynchronousCourse synCourse = synClass.getSynchronousCourse();
			Course synCourse = synClass.getCourse();
			// TODO SynchronousSession schedule = classService.getSynchronousSessionByClassId(Long.parseLong(SynClassId));
			form.setSynClass(synClass);
			List<Resource> resources = synClass.getResources();

			// For sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			
			if( sortColumnIndex != null && sortDirection != null ) {
				// sorting against name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ManageResourcesSort sort = new ManageResourcesSort();
						sort.setSortBy("resourceName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resources,sort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);

					} else {
						ManageResourcesSort sort = new ManageResourcesSort();
						sort.setSortBy("resourceName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resources,sort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);

					}
					// sorting against resource type
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ManageResourcesSort sort = new ManageResourcesSort();
						sort.setSortBy("resourceType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resources,sort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 1);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						ManageResourcesSort sort=new ManageResourcesSort();
						sort.setSortBy("resourceType");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resources,sort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 1);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
					// sorting against tag-number
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						ManageResourcesSort sort=new ManageResourcesSort();
						sort.setSortBy("tagNumber");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resources,sort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 2);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
					} else {
						ManageResourcesSort sort=new ManageResourcesSort();
						sort.setSortBy("tagNumber");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(resources,sort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 2);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
					}
				}
			}
			context.put("resources", resources);
			context.put("reNumber", resources.size());
			context.put("synClassId", synClassId);
			context.put("scheduleId", synClass.getId());
			context.put("synchClass", synClass);
			context.put("synchCourseId", synCourse.getId());
			return new ModelAndView(manageResourceTemplate, "context", context);
		} catch ( Exception e ) {
			return new ModelAndView(manageResourceTemplate);
		}
	}
	
	public ModelAndView removeResources( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try {
			log.debug("In remove");
			ScheduleResourceForm form = (ScheduleResourceForm)command;
			SynchronousClass synClass = form.getSynClass();
			String[] resTobeDeleted = request.getParameterValues("resources");
			List<Long> toBeDeletedIds = new ArrayList<Long>();
			for( String id : resTobeDeleted ) {
				toBeDeletedIds.add(Long.parseLong(id));
			}
			List<Resource> remainingResources = new ArrayList<Resource>();
			List<Resource> presentResources = form.getSynClass().getResources();
			for( Resource r : presentResources ) {
				if( !toBeDeletedIds.contains(r.getId()) ) {
					remainingResources.add(r);
				}
			}
			synClass.setResources(remainingResources);
			classService.saveSynchronousClass(synClass);
			//TODO classService.removeResoursesFromSchedule(resTobeDeleted);
			return (this.showResources(request, response, command, errors));
		} catch ( Exception e ) {
			return new ModelAndView(manageResourceTemplate);
		}
	}
	
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// do nothing...
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public SynchronousClassService getClassService() {
		return classService;
	}

	public void setClassService(SynchronousClassService classService) {
		this.classService = classService;
	}

	public String getManageResourceTemplate() {
		return manageResourceTemplate;
	}

	public void setManageResourceTemplate(String manageResourceTemplate) {
		this.manageResourceTemplate = manageResourceTemplate;
	}

}