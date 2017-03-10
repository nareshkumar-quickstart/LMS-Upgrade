package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.RegulatoryAnalystService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalForm;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRegulator;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRegulatorCategory;
import com.softech.vu360.lms.web.controller.validator.Accreditation.EditApprovalValidator;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.RegulatorSort;

/**
 * @author Saptarshi
 *
 */
public class AddApprovalRegulatorWizardController extends AbstractWizardFormController {

	public static final String COURSE_APPROVAL = "Course";
	public static final String PROVIDER_APPROVAL = "Provider";
	public static final String INSTRUCTOR_APPROVAL = "Instructor";
	public static final String SEARCH_REGULATOR = "search";

	private AccreditationService accreditationService = null;
	@Inject
	private RegulatoryAnalystService regulatoryAnalystService;

	private String closeTemplate = null;

	public AddApprovalRegulatorWizardController() {
		super();
		setCommandName("approvalForm");
		setCommandClass(ApprovalForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
			"accreditation/approvals/editApproval/addRegulatorApproval/add_approval_regulator"
			, "accreditation/approvals/editApproval/addRegulatorApproval/add_approval_regulator_confirm"
		});
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Object command = super.formBackingObject(request);
		ApprovalForm form = (ApprovalForm)command;
		if(request.getParameter("entity") != null ) {
			if (request.getParameter("entity").equalsIgnoreCase(PROVIDER_APPROVAL)) {
				if (request.getParameter("approvalId") != null ) {
					ProviderApproval providerApproval = accreditationService.loadForUpdateProviderApproval(Long.parseLong(request.getParameter("approvalId")));
					form.setProviderApproval(providerApproval);
				}
				form.setEntity(PROVIDER_APPROVAL);
			} else if (request.getParameter("entity").equalsIgnoreCase(INSTRUCTOR_APPROVAL)) {
				if (request.getParameter("approvalId") != null ) {
					InstructorApproval instructorApproval = accreditationService.loadForUpdateInstructorApproval(Long.parseLong(request.getParameter("approvalId")));
					form.setInstructorApproval(instructorApproval);
				}
				form.setEntity(INSTRUCTOR_APPROVAL);
			} else if (request.getParameter("entity").equalsIgnoreCase(COURSE_APPROVAL)) {
				if (request.getParameter("approvalId") != null ) {
					CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(Long.parseLong(request.getParameter("approvalId")));
					form.setCourseApproval(courseApproval);
				}
				form.setEntity(COURSE_APPROVAL);
			} 
		}
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		// Auto-generated method stub
		super.onBindAndValidate(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
		// Auto-generated method stub
		return super.getTargetPage(request, command, errors, currentPage);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		
		ApprovalForm form = (ApprovalForm)command;
		EditApprovalValidator validator = (EditApprovalValidator)this.getValidator();
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();
		
		if ( page == 0 ) {
			if (request.getParameter("action") != null) {
				if (request.getParameter("action").equalsIgnoreCase(SEARCH_REGULATOR) 
						&& this.getTargetPage(request, page) != 1) {
					com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					List<Regulator> regulators = accreditationService.findRegulator(form.getRegulatorName(), form.getAlias(), form.getEmailAddress(), regulatoryAnalystService.getRegulatoryAnalystById(loggedInUserVO.getRegulatoryAnalyst().getId()));
					form.setRegulatorName(HtmlEncoder.escapeHtmlFull(form.getRegulatorName()).toString());
					form.setAlias(HtmlEncoder.escapeHtmlFull(form.getAlias()).toString());
					form.setEmailAddress(HtmlEncoder.escapeHtmlFull(form.getEmailAddress()).toString());
					String sortColumnIndex = form.getSortColumnIndex();
					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
					String sortDirection = form.getSortDirection();
					if( sortDirection == null && session.getAttribute("sortDirection") != null )
						sortDirection = session.getAttribute("sortDirection").toString();
					String pageIndex = form.getPageCurrIndex();
					if( pageIndex == null ) pageIndex = form.getPageIndex();
					
					if( sortColumnIndex != null && sortDirection != null ) {
						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								RegulatorSort RegulatorSort = new RegulatorSort();
								RegulatorSort.setSortBy("name");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(regulators,RegulatorSort);
								form.setSortDirection("0");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
								
							} else {
								RegulatorSort RegulatorSort = new RegulatorSort();
								RegulatorSort.setSortBy("name");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(regulators,RegulatorSort);
								form.setSortDirection("1");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
								
							}
							// sorting against alias
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								RegulatorSort RegulatorSort = new RegulatorSort();
								RegulatorSort.setSortBy("alias");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(regulators,RegulatorSort);
								form.setSortDirection("0");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
							} else {
								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("alias");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(regulators,RegulatorSort);
								form.setSortDirection("1");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 1);
							}
							// sorting against email - address
						} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("emailAddress");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(regulators,RegulatorSort);
								form.setSortDirection("0");
								form.setSortColumnIndex("2");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 2);
							} else {
								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("emailAddress");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(regulators,RegulatorSort);
								form.setSortDirection("1");
								form.setSortColumnIndex("2");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 2);
							}
							// sorting against jurisdiction...
						} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("jurisdiction");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(regulators,RegulatorSort);
								form.setSortDirection("0");
								form.setSortColumnIndex("3");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 3);
							} else {
								RegulatorSort RegulatorSort=new RegulatorSort();
								RegulatorSort.setSortBy("jurisdiction");
								RegulatorSort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(regulators,RegulatorSort);
								form.setSortDirection("1");
								form.setSortColumnIndex("3");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 3);
							}
						}
					}
					
					pagerAttributeMap.put("pageIndex", pageIndex);
					pagerAttributeMap.put("showAll", form.getShowAll());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
					List<ApprovalRegulator> regulatorList = new ArrayList<ApprovalRegulator>();
					for (Regulator regulator : regulators) {
						ApprovalRegulator appRegulator = new ApprovalRegulator();
						appRegulator.setRegulator(regulator);
						appRegulator.setSelected(false);
						regulatorList.add(appRegulator);
					}
					form.setRegulators(regulatorList);
				}
			}else{
				request.setAttribute("newPage","true");
			}
			if (this.getTargetPage(request, page) == 1) {
				validator.validateRegulator(form, errors);
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if (form.getEntity().equalsIgnoreCase(PROVIDER_APPROVAL)) {
			context.put("target", "showProviderApprovalRegulator");
		} else if (form.getEntity().equalsIgnoreCase(INSTRUCTOR_APPROVAL)) {
			context.put("target", "showInstructorApprovalRegulator");
		} else if (form.getEntity().equalsIgnoreCase(COURSE_APPROVAL)) {
			context.put("target", "showCourseApprovalRegulator");
		}
		return new ModelAndView(closeTemplate, "context", context);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3)
	throws Exception {
		ApprovalForm form = (ApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if (form.getEntity().equalsIgnoreCase(PROVIDER_APPROVAL)) {
			ProviderApproval providerApproval = form.getProviderApproval();
			if (providerApproval != null) {
				List<RegulatorCategory> regulatorCategories = providerApproval.getRegulatorCategories();
				if (form.getRegulators() != null)
					for (ApprovalRegulatorCategory approvalRegulatorCategory : form.getRegulatorCategories()) {
						if (approvalRegulatorCategory.isSelected())
							regulatorCategories.add(approvalRegulatorCategory.getCategory());
					}
				providerApproval.setRegulatorCategories(regulatorCategories);
				accreditationService.saveProviderApproval(providerApproval);
			}
			context.put("target", "showProviderApprovalRegulator");
		} else if (form.getEntity().equalsIgnoreCase(INSTRUCTOR_APPROVAL)) {
			InstructorApproval instructorApproval = form.getInstructorApproval();
			if (instructorApproval != null) {
				List<RegulatorCategory> regulatorCategories = instructorApproval.getRegulatorCategories();
				if (form.getRegulators() != null)
					for (ApprovalRegulatorCategory approvalRegulatorCategory : form.getRegulatorCategories()) {
						if (approvalRegulatorCategory.isSelected())
							regulatorCategories.add(approvalRegulatorCategory.getCategory());
					}
				instructorApproval.setRegulatorCategories(regulatorCategories);
				accreditationService.saveInstructorApproval(instructorApproval);
			}
			context.put("target", "showInstructorApprovalRegulator");
		} else if (form.getEntity().equalsIgnoreCase(COURSE_APPROVAL)) {
			CourseApproval courseApproval = form.getCourseApproval();
			if (courseApproval != null) {
				List<RegulatorCategory> regulatorCategories = courseApproval.getRegulatorCategories();
 				if (form.getRegulators() != null)
					for (ApprovalRegulatorCategory approvalRegulatorCategory : form.getRegulatorCategories()) {
						if (approvalRegulatorCategory.isSelected()) {
							regulatorCategories.add(approvalRegulatorCategory.getCategory());
						}
					}
 				courseApproval.setRegulatorCategories(regulatorCategories);
				accreditationService.saveCourseApproval(courseApproval);
			}
			context.put("target", "showCourseApprovalRegulator");
		}
		return new ModelAndView(closeTemplate, "context", context);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		switch(page){
		case 0:
			break;
		case 1:
			break;
		}
		return super.referenceData(request, command, errors, page);
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		EditApprovalValidator validator = (EditApprovalValidator)this.getValidator();
		ApprovalForm form = (ApprovalForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			break;
		case 1:
			break;
		}
		super.validatePage(command, errors, page, finish);
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
	 * @return the closeTemplate
	 */
	public String getCloseTemplate() {
		return closeTemplate;
	}

	/**
	 * @param closeTemplate the closeTemplate to set
	 */
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}

}