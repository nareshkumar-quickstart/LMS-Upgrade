package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialForm;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialTrainingCourses;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.TrainingCourseByCredentialSort;

public class ManageCredentialTrainingCoursesController extends VU360BaseMultiActionController{



	private static final Logger log = Logger.getLogger(ManageCredentialController.class.getName());
	
	private AccreditationService accreditationService = null;


	public static final String CUSTOMFIELD_ENTITY_CREDENTIALS = "CUSTOMFIELD_CREDENTIAL";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALREQUIREMENT = "CUSTOMFIELD_CREDENTIALREQUIREMENT";
	public static final String CUSTOMFIELD_ENTITY_CREDENTIALCATEGORY = "CUSTOMFIELD_CREDENTIALCATEGORY";
			
	private String showAlltrainingCoursesByCredentialTemplate;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	
	
	public ManageCredentialTrainingCoursesController() {
		super();
	}

	public ManageCredentialTrainingCoursesController(Object delegate) {
		super(delegate);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.web.controller.VU360BaseMultiActionController#onBind(javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String)
	 */
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {

		if(command instanceof CredentialForm){
			/*String idStr = request.getParameter("cid");
			Long id= null;
			
			if(idStr!=null && idStr.equalsIgnoreCase("null")==false && idStr.length()>0){
                id= Long.parseLong(idStr);
             }*/
			
			CredentialForm form = (CredentialForm)command;
			if(methodName.equals("showAlltrainingCoursesByCredential")){
				Credential credential = accreditationService.loadForUpdateCredential(form.getCid());
				form.setCredential(credential);
			}
		}
	}
	
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.handleNoSuchRequestHandlingMethod(ex, request, response);
	}
	
	
	public ModelAndView showAlltrainingCoursesByCredential(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) 
	throws Exception {
		CredentialForm form = (CredentialForm)command;
		List<Course> courseList = (List<Course>)form.getCredential().getTrainingCourses();
		
		List<CredentialTrainingCourses> lstCredTrainingCourse = new ArrayList<CredentialTrainingCourses>();
		CredentialTrainingCourses cCourse = null;
		
		for(Course course : courseList)
		{
			cCourse = new CredentialTrainingCourses();
			cCourse.setId(course.getId());
			cCourse.setCourseTitle(course.getCourseTitle());
			cCourse.setBussinesskey(course.getBussinesskey());
			lstCredTrainingCourse.add(cCourse);
		}

		HttpSession session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();

		String pageIndex = request.getParameter("pageIndex");
		if( pageIndex == null ) pageIndex = "0";

		PagerAttributeMap.put("pageIndex",pageIndex);
		if( form.getCredential().getCustomfields().size() <= 10 ) {
			PagerAttributeMap.put("pageIndex", "0");
		}

		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";
		context.put("showAll", showAll);

		

		// sorting against officialLicenseName name
		if( sortColumnIndex != null && sortDirection != null ) {
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					TrainingCourseByCredentialSort trainingCourseSort = new TrainingCourseByCredentialSort();
					trainingCourseSort.setSortBy("courseTitle");
					trainingCourseSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(lstCredTrainingCourse,trainingCourseSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
				} else {
					TrainingCourseByCredentialSort trainingCourseSort = new TrainingCourseByCredentialSort();
					trainingCourseSort.setSortBy("courseTitle");
					trainingCourseSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(lstCredTrainingCourse,trainingCourseSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against jurisdiction
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					TrainingCourseByCredentialSort trainingCourseSort = new TrainingCourseByCredentialSort();
					trainingCourseSort.setSortBy("bussinesskey");
					trainingCourseSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(lstCredTrainingCourse,trainingCourseSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
				} else {
					TrainingCourseByCredentialSort trainingCourseSort = new TrainingCourseByCredentialSort();
					trainingCourseSort.setSortBy("bussinesskey");
					trainingCourseSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(lstCredTrainingCourse,trainingCourseSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
				}
			}
		}
		form.setCredentialTrainingCourses(lstCredTrainingCourse);
		return new ModelAndView(showAlltrainingCoursesByCredentialTemplate, "context", context);
	}
	
	
	public ModelAndView deleteTrainingCourses( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
			CredentialForm form = (CredentialForm)command;
		try {
				if(request.getParameterValues("selectedCourses")!=null){
					Long[] fieldsToDelete=FormUtil.getStringAsLong(request.getParameterValues("selectedCourses"));
			
			
					if (fieldsToDelete.length > 0) {
						
						Credential credential = accreditationService.loadForUpdateCredential(form.getCid());
						if (credential.getTrainingCourses() != null	&& credential.getTrainingCourses().size() > 0) {
							
							for (int index = 0; index < fieldsToDelete.length; index++) {
								
									if(fieldsToDelete[index]!=null && fieldsToDelete[index]>0){
										Course course = courseAndCourseGroupService.getCourseById(fieldsToDelete[index]);
										credential.getTrainingCourses().remove(course);
									}	
		
							}
							
							this.accreditationService.saveCredential(credential);
						}
						//form.setCredential(credential);
					}
				}
		}catch(Exception e){
			log.debug("exception", e);
		}
		
		return new ModelAndView("redirect:acc_ManageCredentialTrainingCourses.do?method=showAlltrainingCoursesByCredential&cid="+form.getCid());
		
	}
	
	
	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	public String getShowAlltrainingCoursesByCredentialTemplate() {
		return showAlltrainingCoursesByCredentialTemplate;
	}

	public void setShowAlltrainingCoursesByCredentialTemplate(
			String showAlltrainingCoursesByCredentialTemplate) {
		this.showAlltrainingCoursesByCredentialTemplate = showAlltrainingCoursesByCredentialTemplate;
	}

	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	
	
	
}