package com.softech.vu360.lms.web.controller.accreditation;

import java.text.SimpleDateFormat;
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

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfigurationTemplate;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CourseConfigForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddCourseConfigValidator;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.TemplateSort;

/**
 * @author Saptarshi
 *
 */
public class AddApprovalCourseConfigWizardController extends AbstractWizardFormController {

	public static final String SEARCH_COURSE_CONFIG = "search";

	private AccreditationService accreditationService;
	private String closeApprovalTemplate = null;

	public AddApprovalCourseConfigWizardController() {
		super();
		setCommandName("courseConfigForm");
		setCommandClass(CourseConfigForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
			"accreditation/approvals/editApproval/addCourseConfigurationTemplate/change_template"
			, "accreditation/approvals/editApproval/addCourseConfigurationTemplate/change_template_confirm"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		CourseConfigForm form=(CourseConfigForm) command;

		if (request.getParameter("entity") != null)
			form.setEntity(request.getParameter("entity"));
		if (form.getEntity().equalsIgnoreCase(CourseConfigForm.COURSE_APPROVAL)) {
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
		CourseConfigForm form = (CourseConfigForm)command;
		AddCourseConfigValidator validator = (AddCourseConfigValidator)this.getValidator();
		if( this.getTargetPage(request, page) == 1 ) {
			if ( !StringUtils.isBlank(form.getSelectedCourseConfigID()) ) {
				CourseConfigurationTemplate courseConfigurationTemplate = accreditationService.getTemplateById(Long.parseLong(form.getSelectedCourseConfigID()));
				form.setCourseConfigurationTemplate(courseConfigurationTemplate);
			}
			validator.validateApprovalCourseConfig(form, errors);
		}
		super.onBindAndValidate(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		
		CourseConfigForm form = (CourseConfigForm)command;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
		getPrincipal();
		ContentOwner contentOwner = null;
		if( loggedInUser.getRegulatoryAnalyst() != null )

		contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		session = request.getSession();
		if ( page == 0 ) {
			if (request.getParameter("action") != null) {
				if (request.getParameter("action").equalsIgnoreCase(SEARCH_COURSE_CONFIG) 
						&& this.getTargetPage(request, page) != 1) {
					List<CourseConfigurationTemplate> courseConfigurationTemplates = null;
					try {
						if ( !StringUtils.isBlank(form.getLastUpdated()) )
							courseConfigurationTemplates = accreditationService.findTemplates(form.getTemplateName(), formatter.parse(form.getLastUpdated()), contentOwner.getId());
							// below trunk version commented and above 1 is taken from 4.5.2 branch
							//courseConfigurationTemplates = accreditationService.findTemplates(form.getTemplateName(), null);
						else
							courseConfigurationTemplates = accreditationService.findTemplates(form.getTemplateName(), null, contentOwner.getId());
							// below trunk version commented and above 1 is taken from 4.5.2 branch
							//courseConfigurationTemplates = accreditationService.findTemplates(form.getTemplateName(), formatter.parse(form.getLastUpdated()));
					} catch (Exception e) {
						//log.debug("Exception: "+ e);
					}
					form.setCourseConfigurationTemplates(courseConfigurationTemplates);
					form.setTemplateName(HtmlEncoder.escapeHtmlFull(form.getTemplateName()).toString());
					form.setLastUpdated(HtmlEncoder.escapeHtmlFull(form.getLastUpdated()).toString());

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

								TemplateSort sort = new TemplateSort();
								sort.setSortBy("name");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(courseConfigurationTemplates,sort);
								form.setSortDirection("0");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
							} else {
								TemplateSort sort = new TemplateSort();
								sort.setSortBy("name");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(courseConfigurationTemplates,sort);
								form.setSortDirection("1");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
							}
							// sorting against alias
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								TemplateSort sort = new TemplateSort();
								sort.setSortBy("lastUpdatedDate");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(courseConfigurationTemplates,sort);
								form.setSortDirection("0");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
							} else {
								TemplateSort sort = new TemplateSort();
								sort.setSortBy("lastUpdatedDate");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(courseConfigurationTemplates,sort);
								form.setSortDirection("1");
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 1);
							}
							// sorting against contact person
						} 
					}	
					form.setCourseConfigurationTemplates(courseConfigurationTemplates);
					pagerAttributeMap.put("pageIndex", pageIndex);
					pagerAttributeMap.put("showAll", form.getShowAll());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
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
		CourseConfigForm form = (CourseConfigForm) command;
		CourseConfigurationTemplate courseConfigurationTemplate = form.getCourseConfigurationTemplate();
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		if (form.getEntity().equalsIgnoreCase(CourseConfigForm.COURSE_APPROVAL)) {
			CourseApproval courseApproval = form.getCourseApproval();
			courseApproval.setTemplate(courseConfigurationTemplate);
			accreditationService.saveCourseApproval(courseApproval);
			context.put("target", "showCourseApprovalSummary");
			return new ModelAndView(closeApprovalTemplate, "context", context);
		}
		return null;
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		//AddProviderValidator validator = (AddProviderValidator)this.getValidator();
		//CourseConfigForm form = (CourseConfigForm)command;
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
		CourseConfigForm form=(CourseConfigForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if (form.getEntity().equalsIgnoreCase(CourseConfigForm.COURSE_APPROVAL)) {
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