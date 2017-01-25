package com.softech.vu360.lms.web.controller.accreditation;

import java.util.Collections;
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

import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.helper.InstructorSearchEnum;
import com.softech.vu360.lms.web.controller.model.accreditation.InstructorForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.EditInstructorValidator;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.InstructorSort;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddApprovalInstructorWizardController extends AbstractWizardFormController {

	public static final String SEARCH_REGULATOR = "search";

	private AccreditationService accreditationService;
	private String closeApprovalTemplate = null;

	public AddApprovalInstructorWizardController() {
		super();
		setCommandName("instructorForm");
		setCommandClass(InstructorForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/approvals/editApproval/addInstructor/add_instructor_approval_instructor"
				, "accreditation/approvals/editApproval/addInstructor/add_instructor_approval_instructor_confirm"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		InstructorForm form=(InstructorForm) command;

		if (request.getParameter("approvalId") != null) {
			long approvalId = Long.parseLong(request.getParameter("approvalId"));
			InstructorApproval instructorApproval = accreditationService.getInstructorApprovalById(approvalId);
			form.setInstructorApproval(instructorApproval);
		}

		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		EditInstructorValidator validator = (EditInstructorValidator)this.getValidator();
		InstructorForm form = (InstructorForm)command;
		if(this.getTargetPage(request, page) == 1) {
			if ( !StringUtils.isBlank(form.getInstId()) ) {
				Instructor instructor = accreditationService.getInstructorByID(Long.parseLong(form.getInstId()));
				form.setInstructor(instructor);
			}
			validator.validateInstructor(form, errors);
		}
		super.onBindAndValidate(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		// TODO Auto-generated method stub
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		InstructorForm form = (InstructorForm)command;
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();
		if (page == 0) {
			if (request.getParameter("action") != null) {
				if (request.getParameter("action").equalsIgnoreCase(SEARCH_REGULATOR) 
						&& this.getTargetPage(request, page) != 1) {
					com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					
					form.setFirstName(HtmlEncoder.escapeHtmlFull(form.getFirstName()).toString());
					form.setLastName(HtmlEncoder.escapeHtmlFull(form.getLastName()).toString());
					form.setEmailAdd(HtmlEncoder.escapeHtmlFull(form.getEmailAdd()).toString());
					String sortColumnIndex = form.getSortColumnIndex();
					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
					String sortDirection = form.getSortDirection();
					if( sortDirection == null && session.getAttribute("sortDirection") != null )
						sortDirection = session.getAttribute("sortDirection").toString();
					String pageIndex = form.getPageCurrIndex();
					if( pageIndex == null ) pageIndex = form.getPageIndex();
					
					String sortBy = null;
					if( sortColumnIndex != null && sortDirection != null ) {
						
						sortBy = InstructorSearchEnum.INSTRUCTOR.getSortBy(sortColumnIndex);
						form.setSortDirection(sortDirection);
						form.setSortColumnIndex(sortColumnIndex);
						session.setAttribute("sortDirection", sortDirection);
						session.setAttribute("sortColumnIndex", sortColumnIndex);
					}
					
					List<Instructor> instructors = accreditationService.findInstructor(form.getFirstName(), form.getLastName(), form.getEmailAdd(), loggedInUser, sortBy, sortDirection);
					
					pagerAttributeMap.put("pageIndex", pageIndex);
					pagerAttributeMap.put("showAll", form.getShowAll());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
					form.setInstructors(instructors);
				}
			}else{
				request.setAttribute("newPage","true");
			}
		}

		super.postProcessPage(request, command, errors, page);
	}

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
		InstructorForm form = (InstructorForm) command;
		InstructorApproval instructorApproval = accreditationService.loadForUpdateInstructorApproval(form.getInstructorApproval().getId());
		instructorApproval.setInstructor(form.getInstructor());
		accreditationService.saveInstructorApproval(instructorApproval);

		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "showInstructorApprovalSummary");
		return new ModelAndView(closeApprovalTemplate, "context", context);
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		InstructorForm form = (InstructorForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
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
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "showInstructorApprovalSummary");
		return new ModelAndView(closeApprovalTemplate, "context", context);
	}

	/**
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
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
	
}
