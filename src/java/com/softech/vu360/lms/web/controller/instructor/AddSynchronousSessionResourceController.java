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
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Resource;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.instructor.ManageResource;
import com.softech.vu360.lms.web.controller.model.instructor.ScheduleResourceForm;
import com.softech.vu360.util.ManageResourcesSort;

/**
 * @author Dyutiman
 * created on 2nd Apr 2010
 *
 */
public class AddSynchronousSessionResourceController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddSynchronousSessionResourceController.class.getName());
	private ResourceService resourceService;
	private SynchronousClassService classService;

	private String finishTemplate = null;

	public AddSynchronousSessionResourceController() {
		super();
		setCommandName("schResourceForm");
		setCommandClass(ScheduleResourceForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"instructor/manageSynchronousClass/addResources/addResourceToSession1"
				, "instructor/manageSynchronousClass/addResources/addResourceToSession2"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		ScheduleResourceForm form = (ScheduleResourceForm)command;
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			String synClassId = request.getParameter("scheduleId");
			SynchronousClass synClass = classService.getSynchronousClassById(Long.parseLong(synClassId));
			// SynchronousSession schedule = classService.getSynchronousSessionsById(synSessionId);
			List<Resource> presentResources = synClass.getResources();
			List<Long> presentResourceIds = new ArrayList<Long>();
			for( Resource r : presentResources ) {
				presentResourceIds.add(r.getId());
			}
			List<Resource> allResources = resourceService.findAllResources(loggedInUser.getLearner().getCustomer().getContentOwner().getId());

			List<ManageResource> notPresentResources = new ArrayList<ManageResource>();
			for( Resource r : allResources ) {
				if( !presentResourceIds.contains(r.getId()) ) {
					ManageResource mResource = new ManageResource();
					mResource.setResource(r);
					mResource.setSelected(false);
					notPresentResources.add(mResource);
				}
			}
			form.setResources(notPresentResources);
			//form.setResources(presentResources);
			form.setSynClass(synClass);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		ScheduleResourceForm form = (ScheduleResourceForm)command;
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();

		if( page == 0 ) {
			if( !form.getAction().isEmpty() && form.getAction().equalsIgnoreCase("sort") ) {

				com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				List<Resource> allResources = resourceService.findAllResources(loggedInUser.getLearner().getCustomer().getContentOwner().getId());
				String sortColumnIndex = form.getSortColumnIndex();
				if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
					sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
				String sortDirection = form.getSortDirection();
				if( sortDirection == null && session.getAttribute("sortDirection") != null )
					sortDirection = session.getAttribute("sortDirection").toString();
				String pageIndex = form.getPageCurrIndex();
				if( pageIndex == null ) pageIndex = form.getPageIndex();

				if( sortColumnIndex != null && sortDirection != null ) {
					// sorting against name
					if( sortColumnIndex.equalsIgnoreCase("0") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {

							ManageResourcesSort sort = new ManageResourcesSort();
							sort.setSortBy("resourceName");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(allResources,sort);
							form.setSortDirection("0");
							form.setSortColumnIndex("0");
//							session.setAttribute("sortDirection", 0);
//							session.setAttribute("sortColumnIndex", 0);

						} else {
							ManageResourcesSort sort = new ManageResourcesSort();
							sort.setSortBy("resourceName");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(allResources,sort);
							form.setSortDirection("1");
							form.setSortColumnIndex("0");
//							session.setAttribute("sortDirection", 1);
//							session.setAttribute("sortColumnIndex", 0);

						}
						// sorting against resource type
					} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {

							ManageResourcesSort sort = new ManageResourcesSort();
							sort.setSortBy("resourceType");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(allResources,sort);
							form.setSortDirection("0");
							form.setSortColumnIndex("1");
//							session.setAttribute("sortDirection", 0);
//							session.setAttribute("sortColumnIndex", 1);
						} else {
							ManageResourcesSort sort=new ManageResourcesSort();
							sort.setSortBy("resourceType");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(allResources,sort);
							form.setSortDirection("1");
							form.setSortColumnIndex("1");
//							session.setAttribute("sortDirection", 1);
//							session.setAttribute("sortColumnIndex", 1);
						}
						// sorting against tag-number
					} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
						if( sortDirection.equalsIgnoreCase("0") ) {

							ManageResourcesSort sort=new ManageResourcesSort();
							sort.setSortBy("tagNumber");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(allResources,sort);
							form.setSortDirection("0");
							form.setSortColumnIndex("2");
//							session.setAttribute("sortDirection", 0);
//							session.setAttribute("sortColumnIndex", 2);
						} else {
							ManageResourcesSort sort=new ManageResourcesSort();
							sort.setSortBy("tagNumber");
							sort.setSortDirection(Integer.parseInt(sortDirection));
							Collections.sort(allResources,sort);
							form.setSortDirection("1");
							form.setSortColumnIndex("2");
//							session.setAttribute("sortDirection", 1);
//							session.setAttribute("sortColumnIndex", 2);
						}
					}
				}
				pagerAttributeMap.put("pageIndex", pageIndex);
				pagerAttributeMap.put("showAll", form.getShowAll());
				request.setAttribute("PagerAttributeMap", pagerAttributeMap);

				List<Resource> presentResources = form.getSynClass().getResources();
				List<Long> presentResourceIds = new ArrayList<Long>();
				for( Resource r : presentResources ) {
					presentResourceIds.add(r.getId());
				}				
				List<ManageResource> notPresentResources = new ArrayList<ManageResource>();			
				for( Resource r : allResources ) {
					if( !presentResourceIds.contains(r.getId()) ) {
						ManageResource mResource = new ManageResource();
						mResource.setResource(r);
						mResource.setSelected(false);
						notPresentResources.add(mResource);
					}
				}
				form.setResources(notPresentResources);
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {

		ScheduleResourceForm form = (ScheduleResourceForm)command;
		boolean selected = false;
		switch(page) {
		case 0:
			if( form.getAction().equalsIgnoreCase("validate") ) {
				List<ManageResource> allResources = form.getResources();
				for( ManageResource r : allResources ) {
					if( r.isSelected() ) {
						selected = true;
						break;
					}
				}
				if( !selected ) {
					errors.rejectValue("resources", "error.addResourceToSession.selection.required", "");
				}
			}
			break;
		case 1:
			break;
		default:
			break;
		}	
		super.onBindAndValidate(request, command, errors, page);
	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse responce, Object command, BindException errors)
	throws Exception {

		Map<Object, Object> context = new HashMap<Object, Object>();
		ScheduleResourceForm form = (ScheduleResourceForm)command;
		ArrayList<ManageResource> resTobeAdded = new ArrayList<ManageResource>();
		List<ManageResource> allResources = form.getResources();
		for( ManageResource r : allResources ) {
			if( r.isSelected() ) {
				resTobeAdded.add(r);
			}
		}
		SynchronousClass synClass = form.getSynClass();
		List<Resource> tobeAdded = synClass.getResources();
		for( ManageResource r : resTobeAdded ) {
			Resource resource = r.getResource();
			tobeAdded.add(resource);
		}
		synClass.setResources(tobeAdded);
		classService.saveSynchronousClass(synClass);
		context.put("sessionId", form.getSynClass().getId());
		return new ModelAndView(finishTemplate, "context", context);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse responce, Object command, BindException errors)
	throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		ScheduleResourceForm form = (ScheduleResourceForm)command;
		context.put("sessionId", form.getSynClass().getId());
		return new ModelAndView(finishTemplate, "context", context);
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

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

}