package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalCredential;
import com.softech.vu360.lms.web.controller.model.accreditation.ApprovalRequirement;
import com.softech.vu360.lms.web.controller.model.accreditation.RequirementForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddApprovalRequirementValidator;
import com.softech.vu360.util.CredentialSort;
import com.softech.vu360.util.HtmlEncoder;

/**
 * 
 * @author Saptarshi
 *
 */
public class AddApprovalRequirementWizardController  extends AbstractWizardFormController {


	public static final String SEARCH_REQUIREMENT = "search";

	private AccreditationService accreditationService;
	private String closeApprovalTemplate = null;

	public AddApprovalRequirementWizardController() {
		super();
		setCommandName("requirementForm");
		setCommandClass(RequirementForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/approvals/editApproval/addApprovalRequirement/add_course_approval_requirement"
				, "accreditation/approvals/editApproval/addApprovalRequirement/add_course_approval_requirement_requirements"
				, "accreditation/approvals/editApproval/addApprovalRequirement/add_course_approval_requirement_confirm"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		RequirementForm form=(RequirementForm) command;

		if (request.getParameter("entity") != null)
			form.setEntity(request.getParameter("entity"));
		if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
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
		AddApprovalRequirementValidator validator = (AddApprovalRequirementValidator)this.getValidator();
		RequirementForm form = (RequirementForm)command;
		if(this.getTargetPage(request, page) == 1) {
			List<ApprovalCredential> appCredentialList = form.getApprovalCredential();
			if(appCredentialList != null) {
				for(ApprovalCredential appCredential : appCredentialList) {
					if(appCredential.isSelected()) {
						List <CredentialCategoryRequirement> rqurmnts = accreditationService.getCredentialCategoryRequirementsByCredential(appCredential.getCredential().getId());
						if (rqurmnts != null) {
							List<ApprovalRequirement> approvalRequirements = new ArrayList <ApprovalRequirement>();
							for(CredentialCategoryRequirement requirement : rqurmnts) {
								ApprovalRequirement appRequirement = new ApprovalRequirement();
								appRequirement.setRequirement(requirement);
								appRequirement.setSelected(false);
								approvalRequirements.add(appRequirement);
							}
							appCredential.setRequirements(approvalRequirements);
						}
					}
				}
				form.setApprovalCredential(appCredentialList);
			}
			validator.validateCredential(form, errors);
		} else if(this.getTargetPage(request, page) == 2) {
			List<CredentialCategoryRequirement> requirementList = new ArrayList<CredentialCategoryRequirement>();
			List<ApprovalCredential> appCredentialList = form.getApprovalCredential();
			if(appCredentialList != null) {
				for(ApprovalCredential appCredential : appCredentialList) {
					if(appCredential.isSelected()) {
						if(appCredential.getRequirements() != null && appCredential.getRequirements().size() >0)
							for(ApprovalRequirement appRequirement : appCredential.getRequirements()) {
								if (appRequirement.isSelected())
									requirementList.add(appRequirement.getRequirement());
							}
					}
				}
				form.setRequirements(requirementList);
			}
			validator.validateRequirement(form, errors);
		}

		super.onBindAndValidate(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		RequirementForm form = (RequirementForm)command;
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();
		
		if (page == 0) {
			if (request.getParameter("action") != null) {
				if (request.getParameter("action").equalsIgnoreCase(SEARCH_REQUIREMENT) 
						&& this.getTargetPage(request, page) != 1) {
					com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					List<Credential> credentials = accreditationService.findCredential(form.getCredentialName(), form.getCredentialType(), loggedInUser.getRegulatoryAnalyst());
					
					form.setCredentialName(HtmlEncoder.escapeHtmlFull(form.getCredentialName()).toString());
					form.setCredentialType(HtmlEncoder.escapeHtmlFull(form.getCredentialType()).toString());
					
					String sortColumnIndex = form.getSortColumnIndex();
					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
					String sortDirection = form.getSortDirection();
					if( sortDirection == null && session.getAttribute("sortDirection") != null )
						sortDirection = session.getAttribute("sortDirection").toString();
					String pageIndex = form.getPageCurrIndex();
					if( pageIndex == null ) pageIndex = form.getPageIndex();
					
					if( sortColumnIndex != null && sortDirection != null ) {

						// sorting against regulator name
						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								CredentialSort sort = new CredentialSort();
								sort.setSortBy("officialLicenseName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(credentials,sort);
								form.setSortDirection("0");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
							} else {
								CredentialSort sort = new CredentialSort();
								sort.setSortBy("officialLicenseName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(credentials,sort);
								form.setSortDirection("1");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
							}
							// sorting against alias
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								CredentialSort sort = new CredentialSort();
								sort.setSortBy("shortLicenseName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(credentials,sort);
								form.setSortDirection("0");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
							} else {
								
								CredentialSort sort = new CredentialSort();
								sort.setSortBy("shortLicenseName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(credentials,sort);
								form.setSortDirection("1");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 1);
							}
							// sorting against contact person
						} 
					}	
				//	form.setCourses(courses);
					pagerAttributeMap.put("pageIndex", pageIndex);
					pagerAttributeMap.put("showAll", form.getShowAll());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
					
					if (credentials != null) {
						List<ApprovalCredential> appCredentialList = new ArrayList<ApprovalCredential>();
						for (Credential credential : credentials) {
							ApprovalCredential appCredential = new ApprovalCredential();
							appCredential.setCredential(credential);
							appCredential.setSelected(false);
							appCredentialList.add(appCredential);
						}
						form.setApprovalCredential(appCredentialList);
					}
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
		RequirementForm form = (RequirementForm) command;
		List<CredentialCategoryRequirement> requirements = form.getRequirements();

		Map<Object, Object> context = new HashMap<Object, Object>();
		if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
			CourseApproval courseApproval = form.getCourseApproval();
			courseApproval= accreditationService.loadForUpdateCourseApproval(form.getCourseApproval().getId());
			courseApproval.getRequirements().addAll(requirements);
			accreditationService.saveCourseApproval(courseApproval);
			context.put("target", "showCourseApprovalRequirement");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		}

		return null;
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		//AddProviderValidator validator = (AddProviderValidator)this.getValidator();
		RequirementForm form = (RequirementForm)command;
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
		RequirementForm form=(RequirementForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if (form.getEntity().equalsIgnoreCase(form.COURSE_APPROVAL)) {
			context.put("target", "showCourseApprovalRequirement");
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
}
