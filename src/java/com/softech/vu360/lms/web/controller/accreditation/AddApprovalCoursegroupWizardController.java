package com.softech.vu360.lms.web.controller.accreditation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalCourseForm;
import com.softech.vu360.util.TreeNode;

/**
 * @author Saptarshi
 *
 */
public class AddApprovalCoursegroupWizardController extends AbstractWizardFormController {


	private AccreditationService accreditationService;
	private CourseAndCourseGroupService courseAndCourseGroupService;
	private String closeApprovalTemplate = null;
	
	public AddApprovalCoursegroupWizardController() {
		super();
		setCommandName("courseForm");
		setCommandClass(ApprovalCourseForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
			 "accreditation/approvals/editApproval/addApprovalCoursegroup/add_instructor_approval_courseGroup"	
			, "accreditation/approvals/editApproval/addApprovalCoursegroup/add_instructor_approval_courses_confirm"});
	}
	
	
	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		ApprovalCourseForm form=(ApprovalCourseForm) command;

		if(request.getParameter("courseId")!=null){
			form.setCourseId(request.getParameter("courseId"));
		}
		
		if(request.getParameter("approvalId")!=null){
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
		/* TPMO-914 - Course group setting on CourseApproval object before save data in DB */
		if(this.getTargetPage(request, page) == 1) {
			//if ( request.getParameter("rdoCourseGroups")!=null ) {
			if ( form.getCourseGroupId()!=null && !form.getCourseGroupId().equals("") && Long.parseLong(form.getCourseGroupId())>0) {
				//String idStr = request.getParameter("rdoCourseGroups")==null?"0":request.getParameter("rdoCourseGroups");
				CourseGroup objCourseGroup = courseAndCourseGroupService.getCourseGroupById(Long.parseLong(form.getCourseGroupId()));
				if(objCourseGroup!=null)
					form.setLstCourseGroups( Arrays.asList( objCourseGroup ) );
				//form.setCourseGroupId(idStr);
			}else{
					errors.rejectValue("courseGroupId", "error.editApproval.coursegroup.required");
			}
			
		}
		super.onBindAndValidate(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		
		ApprovalCourseForm form = (ApprovalCourseForm)command;
		if(page == 0){
			if (request.getParameter("action") != null) {
				if (this.getTargetPage(request, page) != 1) {
						
					Long selectCourseId = Long.parseLong(form.getCourseId());
					String varCourseGroupName = request.getParameter("varCourseGroupName");

						List<CourseGroup> courseGroupList = courseAndCourseGroupService.getAllCourseGroupsByCourseId
																							(selectCourseId, varCourseGroupName);
		                List<List<TreeNode>> courseGroupTreeList = courseAndCourseGroupService.getCourseGroupTreeList2( courseGroupList, false );
		                form.setCourseGroupTreeList(courseGroupTreeList);
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

		switch(page){
		case 0:
			break;
		case 1:
			break;
		default:
			break;
		}
		return super.referenceData(request, page);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
	
		
		ApprovalCourseForm form = (ApprovalCourseForm) command;
		
		if(form.getLstCourseGroups()!=null  && form.getLstCourseGroups().size()>0 ) {
			CourseApproval courseApproval = form.getCourseApproval();
			courseApproval.setCourseGroups(form.getLstCourseGroups());
			accreditationService.saveCourseApproval(courseApproval);
		}
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "showCourseApprovalSummary");
		return new ModelAndView(closeApprovalTemplate, "context", context);
		
	}


	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "showCourseApprovalSummary");
		return new ModelAndView(closeApprovalTemplate, "context", context);
		
	}

	

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}


	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}


	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}


	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}


	public String getCloseApprovalTemplate() {
		return closeApprovalTemplate;
	}


	public void setCloseApprovalTemplate(String closeApprovalTemplate) {
		this.closeApprovalTemplate = closeApprovalTemplate;
	}
	
	
	

}