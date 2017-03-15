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

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.ProviderForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddProviderValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.ProviderSort;

/**
 * @author Saptarshi
 *
 */
public class AddApprovalProviderWizardController extends AbstractWizardFormController {

	public static final String SEARCH_PROVIDER = "search";

	private AccreditationService accreditationService;
	private String closeApprovalTemplate = null;

	public AddApprovalProviderWizardController() {
		super();
		setCommandName("providerForm");
		setCommandClass(ProviderForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
			"accreditation/approvals/editApproval/addApprovalProvider/add_regulator_provider"
			, "accreditation/approvals/editApproval/addApprovalProvider/add_regulator_provider_confirm"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		ProviderForm form=(ProviderForm) command;

		if (request.getParameter("entity") != null)
			form.setEntity(request.getParameter("entity"));
		if (form.getEntity().equalsIgnoreCase(ProviderForm.PROVIDER_APPROVAL)) {
			long providerApprovalId = Long.parseLong(request.getParameter("approvalId"));
			ProviderApproval providerApproval = accreditationService.loadForUpdateProviderApproval(providerApprovalId);
			form.setProviderApproval(providerApproval);
		} else if (form.getEntity().equalsIgnoreCase(ProviderForm.INSTRUCTOR_APPROVAL)) {
			long instructorApprovalId = Long.parseLong(request.getParameter("approvalId"));
			InstructorApproval instructorApproval = accreditationService.loadForUpdateInstructorApproval(instructorApprovalId);
			form.setInstructorApproval(instructorApproval);
		} else if (form.getEntity().equalsIgnoreCase(ProviderForm.COURSE_APPROVAL)) {
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
		ProviderForm form = (ProviderForm)command;
		AddProviderValidator validator = (AddProviderValidator)this.getValidator();
		if(this.getTargetPage(request, page) == 1) {
			if ( !StringUtils.isBlank(form.getProviderId()) ) {
				Provider provider = accreditationService.getProviderById(Long.parseLong(form.getProviderId()));
				form.setProvider(provider);
			}
			validator.validateApprovalProvider(form, errors);
		}
		super.onBindAndValidate(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		ProviderForm form = (ProviderForm)command;
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();
		if ( page == 0 ) {
			if (request.getParameter("action") != null) {
				if (request.getParameter("action").equalsIgnoreCase(SEARCH_PROVIDER) 
						&& this.getTargetPage(request, page) != 1) {
					VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
					List<Provider> providers = accreditationService.findProviders(form.getName(), loggedInUser.getRegulatoryAnalyst());
					form.setName(HtmlEncoder.escapeHtmlFull(form.getName()).toString());
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

								ProviderSort sort = new ProviderSort();
								sort.setSortBy("name");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(providers,sort);
								form.setSortDirection("0");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
							} else {
								ProviderSort sort = new ProviderSort();
								sort.setSortBy("name");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(providers,sort);
								form.setSortDirection("1");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
							}
							// sorting against alias
						}  
					}	
					pagerAttributeMap.put("pageIndex", pageIndex);
					pagerAttributeMap.put("showAll", form.getShowAll());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
					form.setProviders(providers);
				}
			}else{
				request.setAttribute("newPage","true");
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
		ProviderForm form = (ProviderForm) command;
		Provider provider = form.getProvider();
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		if (form.getEntity().equalsIgnoreCase(ProviderForm.PROVIDER_APPROVAL)) {
			ProviderApproval providerApproval = form.getProviderApproval();
			providerApproval.setProvider(provider);
			accreditationService.saveProviderApproval(providerApproval);
			context.put("target", "showProviderApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		} else if (form.getEntity().equalsIgnoreCase(ProviderForm.INSTRUCTOR_APPROVAL)) {
			InstructorApproval instructorApproval = form.getInstructorApproval();
			instructorApproval.setProvider(provider);
			accreditationService.saveInstructorApproval(instructorApproval);
			context.put("target", "showInstructorApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		} else if (form.getEntity().equalsIgnoreCase(ProviderForm.COURSE_APPROVAL)) {
			CourseApproval courseApproval = form.getCourseApproval();
			courseApproval.setProvider(provider);
			accreditationService.saveCourseApproval(courseApproval);
			context.put("target", "showCourseApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		}
		return null;
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		//AddProviderValidator validator = (AddProviderValidator)this.getValidator();
		//ProviderForm form = (ProviderForm)command;
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
		ProviderForm form=(ProviderForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if (form.getEntity().equalsIgnoreCase(ProviderForm.PROVIDER_APPROVAL)) {
			context.put("target", "showProviderApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		} else if (form.getEntity().equalsIgnoreCase(ProviderForm.INSTRUCTOR_APPROVAL)) {
			context.put("target", "showInstructorApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		} else if (form.getEntity().equalsIgnoreCase(ProviderForm.COURSE_APPROVAL)) {
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

}