package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalCourseForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddApprovalCourseValidator;
import com.softech.vu360.util.CourseSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.TreeNode;

/**
 * @author Saptarshi
 *
 */
public class AddApprovalCourseWizardController extends AbstractWizardFormController {

	public static final String SEARCH_COURSE = "search";
	public static final String SEARCH_COURSE_GROUP = "searchCourseGroup";
	

	private AccreditationService accreditationService;
	private CourseAndCourseGroupService courseAndCourseGroupService;

	private String closeApprovalTemplate = null;
	private String redirectToCredCategoryTemplate;

	public AddApprovalCourseWizardController() {
		super();
		setCommandName("courseForm");
		setCommandClass(ApprovalCourseForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
			  "accreditation/approvals/editApproval/addApprovalCourse/add_instructor_approval_courses"
			, "accreditation/approvals/editApproval/addApprovalCourse/add_instructor_approval_courseGroup"	
			, "accreditation/approvals/editApproval/addApprovalCourse/add_instructor_approval_courses_confirm"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		ApprovalCourseForm form=(ApprovalCourseForm) command;

		if (request.getParameter("entity") != null)
			form.setEntity(request.getParameter("entity"));
		if (form.getEntity().equalsIgnoreCase(ApprovalCourseForm.INSTRUCTOR_APPROVAL)) {
			long instructorApprovalId = Long.parseLong(request.getParameter("approvalId"));
			InstructorApproval instructorApproval = accreditationService.loadForUpdateInstructorApproval(instructorApprovalId);
			form.setInstructorApproval(instructorApproval);
		} else if (form.getEntity().equalsIgnoreCase(ApprovalCourseForm.COURSE_APPROVAL)) {
			long courseApprovalId = Long.parseLong(request.getParameter("approvalId"));
			CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(courseApprovalId);
			form.setCourseApproval(courseApproval);
		}
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		ApprovalCourseForm form = (ApprovalCourseForm)command;
		AddApprovalCourseValidator validator = (AddApprovalCourseValidator)this.getValidator();
		if(this.getTargetPage(request, page) == 1) {
			if ( !StringUtils.isBlank(form.getSelectedCourseId()) ) {
				
				// This logic applied because if user select course in step 1 and search course group, 
				// and if he move back and select different course, so course group list should become empty
				if(form.getCourse() != null && form.getCourse().getId()!=null){
					if(! form.getCourse().getId().toString().equals(form.getSelectedCourseId())){
						form.setCourseGroupTreeList(null);
					}
				}
				
				Course course = courseAndCourseGroupService.getCourseById(Long.parseLong(form.getSelectedCourseId()));
				form.setCourse(course);
				
			}
			validator.validateCourse(form, errors);
		}
		
		/* TPMO-914 - Course group setting on CourseApproval object before save data in DB */
		if(this.getTargetPage(request, page) == 2) {
			//if ( request.getParameter("rdoCourseGroups")!=null ) {
			if ( form.getCourseGroupId()!=null && !form.getCourseGroupId().equals("") && Long.parseLong(form.getCourseGroupId())>0) {
				//String idStr = request.getParameter("rdoCourseGroups")==null?"0":request.getParameter("rdoCourseGroups");
				CourseGroup objCourseGroup = courseAndCourseGroupService.getCourseGroupById(Long.parseLong(form.getCourseGroupId()));
				if(objCourseGroup!=null) {
					List<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
					courseGroups.add(objCourseGroup);
					form.setLstCourseGroups(courseGroups);
				}
				//form.setCourseGroupId(idStr);
			}
			//LMS-16004 - Course Group has become non-mandatory
			/*else{
					errors.rejectValue("courseGroupId", "error.editApproval.coursegroup.required");
			}*/
			
		}
		super.onBindAndValidate(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		ApprovalCourseForm form = (ApprovalCourseForm)command;
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();
		if( page == 0 ) {
			if (request.getParameter("action") != null) {
				if (request.getParameter("action").equalsIgnoreCase(SEARCH_COURSE) 
						&& this.getTargetPage(request, page) != 1) {
					com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					List<Course> courses = accreditationService.findCoursesByRegulatoryAnalyst(form.getCourseName(), form.getCourseId(), loggedInUser.getRegulatoryAnalyst());
					form.setCourseName(HtmlEncoder.escapeHtmlFull(form.getCourseName()).toString());
					form.setCourseId(HtmlEncoder.escapeHtmlFull(form.getCourseId()).toString());
					String sortColumnIndex = form.getSortColumnIndex();
					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
					String sortDirection = form.getSortDirection();
					if( sortDirection == null && session.getAttribute("sortDirection") != null )
						sortDirection = session.getAttribute("sortDirection").toString();
					String pageIndex = form.getPageCurrIndex();
					if( pageIndex == null ) pageIndex = form.getPageIndex();

					if( sortColumnIndex != null && sortDirection != null ) {

						// sorting against course name
						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								CourseSort sort = new CourseSort();
								sort.setSortBy("courseTitle");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(courses,sort);
								form.setSortDirection("0");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
							} else {
								CourseSort sort = new CourseSort();
								sort.setSortBy("courseTitle");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(courses,sort);
								form.setSortDirection("1");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
							}
							// sorting against business key
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								CourseSort sort = new CourseSort();
								sort.setSortBy("courseId");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(courses,sort);
								form.setSortDirection("0");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
							} else {
								CourseSort sort = new CourseSort();
								sort.setSortBy("courseId");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(courses,sort);
								form.setSortDirection("1");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 1);
							}
						} 
					}
					form.setCourses(courses);
					pagerAttributeMap.put("pageIndex", pageIndex);
					pagerAttributeMap.put("showAll", form.getShowAll());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
			}else{
				request.setAttribute("newPage","true");
			}
		}else if(page == 1){
			if (request.getParameter("action") != null) {
				if (request.getParameter("action").equalsIgnoreCase(SEARCH_COURSE_GROUP) && this.getTargetPage(request, page) != 2) {
						
					Long selectCourseId = Long.parseLong(form.getSelectedCourseId());
					String varCourseGroupName = request.getParameter("varCourseGroupName");
					//Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
					
					//if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
					//	VU360User loggedInUser = (VU360User)auth.getPrincipal();
						// getting content owner
					//	ContentOwner contentOwner = null;
					//	if( loggedInUser.getRegulatoryAnalyst() != null ) {
					//		contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
					//	}
						
						List<CourseGroup> courseGroupList = courseAndCourseGroupService.getAllCourseGroupsByCourseId
																							(selectCourseId, varCourseGroupName);
		                List<List<TreeNode>> courseGroupTreeList = courseAndCourseGroupService.getCourseGroupTreeList2( courseGroupList, false );
		                form.setCourseGroupTreeList(courseGroupTreeList);
		                form.setCourseGroupId("-1");
					//}
				}else{
					request.setAttribute("newPage","true");
				}
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		ApprovalCourseForm form = (ApprovalCourseForm)command;
		
		switch(page){
		case 0:
			break;
		case 1:
			if(form.getPreviouslySelectedCourseId() != null && !form.getPreviouslySelectedCourseId().equals(form.getSelectedCourseId())){
				form.setPreviouslySelectedCourseId(form.getSelectedCourseId());
				form.setCourseGroupId("0");
				form.setCourseGroupTreeList(new ArrayList<List<TreeNode>>() );
			}
			break;
		default:
			break;
		}
		return super.referenceData(request, page);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		ApprovalCourseForm form = (ApprovalCourseForm) command;
		Course course = form.getCourse();
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		if (form.getEntity().equalsIgnoreCase(ApprovalCourseForm.INSTRUCTOR_APPROVAL)) {
			InstructorApproval instructorApproval = form.getInstructorApproval();
			instructorApproval.setCourse(course);
			accreditationService.saveInstructorApproval(instructorApproval);
			context.put("target", "showInstructorApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		} else if (form.getEntity().equalsIgnoreCase(ApprovalCourseForm.COURSE_APPROVAL)) {
			CourseApproval courseApproval = form.getCourseApproval();
			courseApproval.setCourse(course);
			
			/* TPMO-914 - Course group setting with CourseApproval object before save data in DB */
			if(form.getLstCourseGroups()!=null && form.getLstCourseGroups().size()>0 ) {
				courseApproval.setCourseGroups(form.getLstCourseGroups());
				//model.put("courseGroupName", objCourseGroup.getName());
			}else
				courseApproval.setCourseGroups(null);
			
			accreditationService.saveCourseApproval(courseApproval);
			context.put("target", "showCourseApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		}
		
		return null;
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		//AddProviderValidator validator = (AddProviderValidator)this.getValidator();
		ApprovalCourseForm form = (ApprovalCourseForm) command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			if (form.getEntity().equalsIgnoreCase(ApprovalCourseForm.COURSE_APPROVAL)) {
				validateCourseApprovalCreated(form, errors);
			}
			break;
		case 1:
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		ApprovalCourseForm form=(ApprovalCourseForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if (form.getEntity().equalsIgnoreCase(ApprovalCourseForm.INSTRUCTOR_APPROVAL)) {
			context.put("target", "showInstructorApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		} else if (form.getEntity().equalsIgnoreCase(ApprovalCourseForm.COURSE_APPROVAL)) {
			context.put("target", "showCourseApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		}
		return null;
	}


	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	/**
	 * @return the closeApprovalTemplate
	 */
	public String getCloseApprovalTemplate() {
		return closeApprovalTemplate;
	}

	/**
	 * @param closeApprovalTemplate the closeApprovalTemplate to set
	 */
	public void setCloseApprovalTemplate(String closeApprovalTemplate) {
		this.closeApprovalTemplate = closeApprovalTemplate;
	}

	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	public String getRedirectToCredCategoryTemplate() {
		return redirectToCredCategoryTemplate;
	}

	public void setRedirectToCredCategoryTemplate(
			String redirectToCredCategoryTemplate) {
		this.redirectToCredCategoryTemplate = redirectToCredCategoryTemplate;
	}
	private void validateCourseApprovalCreated(ApprovalCourseForm form, Errors errors){
		form.setCourseApproval(accreditationService.loadForUpdateCourseApproval(form.getCourseApproval().getId()));
		long regulatorCategoryId = form.getCourseApproval().getRegulatorCategory().getId();
		long courseId = 0;
		Date startDate = form.getCourseApproval().getCourseApprovalEffectivelyStartDate();
		Date endDate = form.getCourseApproval().getCourseApprovalEffectivelyEndsDate();

		if(StringUtils.isNotBlank(form.getSelectedCourseId())){
			courseId = Long.parseLong(form.getSelectedCourseId());
		}

		if(regulatorCategoryId > 0 && courseId > 0 && startDate != null && endDate != null){
			if(getAccreditationService().isCourseAlreadyAssociatedWithRegulatorAuthority(courseId, regulatorCategoryId, startDate, endDate, form.getCourseApproval().getId())){
				errors.rejectValue("selectedCourseId", "error.approval.course.alreadyAssociated","");
			}
		}
		
	}
	
}