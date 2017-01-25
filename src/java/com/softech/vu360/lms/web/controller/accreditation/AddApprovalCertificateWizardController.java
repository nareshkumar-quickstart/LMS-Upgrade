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

import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CertificateForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddApprovalCertificateValidator;
import com.softech.vu360.util.CertificateSort;
import com.softech.vu360.util.HtmlEncoder;

/**
 * @author Saptarshi
 *
 */
public class AddApprovalCertificateWizardController  extends AbstractWizardFormController {


	public static final String SEARCH_CERTIFICATE = "search";

	private AccreditationService accreditationService;
	private String closeApprovalTemplate = null;

	public AddApprovalCertificateWizardController() {
		super();
		setCommandName("certificateForm");
		setCommandClass(CertificateForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
			"accreditation/approvals/editApproval/addApprovalCertificate/change_certificate"
			, "accreditation/approvals/editApproval/addApprovalCertificate/change_certificate_confirm"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		CertificateForm form=(CertificateForm) command;
		if ( request.getParameter("entity") != null )
			form.setEntity(request.getParameter("entity"));
		if ( form.getEntity().equalsIgnoreCase(CertificateForm.COURSE_APPROVAL) ) {
			long courseApprovalId = Long.parseLong(request.getParameter("approvalId"));
			CourseApproval courseApproval = accreditationService.loadForUpdateCourseApproval(courseApprovalId);
			form.setCourseApproval(courseApproval);
		}
                if(StringUtils.isNotEmpty(request.getParameter("redirectTo"))){
                    String redirectURL = request.getParameter("redirectTo");
                    logger.debug("redirectURL: " + redirectURL);
                    form.setReturnURL(redirectURL);
                    
                    if("editCourseConfiguration".equalsIgnoreCase(redirectURL)){
                        form.setTemplateId(Long.parseLong(request.getParameter("templateId")));
                    }
                }
                
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		CertificateForm form = (CertificateForm)command;
		AddApprovalCertificateValidator validator = (AddApprovalCertificateValidator)this.getValidator();
		if(this.getTargetPage(request, page) == 1) {
			if ( !StringUtils.isBlank(form.getSelectedCertificateID()) ) {
				Certificate certificate = accreditationService.getCertificateById(Long.parseLong(form.getSelectedCertificateID()));
				form.setCertificate(certificate);
			}
			validator.validateCertificate(form, errors);
		}
		super.onBindAndValidate(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		CertificateForm form = (CertificateForm)command;
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();
		if (page == 0) {
			if (request.getParameter("action") != null) {
				if (request.getParameter("action").equalsIgnoreCase(SEARCH_CERTIFICATE) 
						&& this.getTargetPage(request, page) != 1) {
					List<Certificate> certificates = accreditationService.getCertficatesByName(form.getCertificateName());
					form.setCertificateName(HtmlEncoder.escapeHtmlFull(form.getCertificateName()).toString());
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

								CertificateSort sort = new CertificateSort();
								sort.setSortBy("name");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(certificates,sort);
								form.setSortDirection("0");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
							} else {
								CertificateSort sort = new CertificateSort();
								sort.setSortBy("name");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(certificates,sort);
								form.setSortDirection("1");
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
							}
							// sorting against alias
						} 
					}	
					form.setCertificates(certificates);
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
		CertificateForm form = (CertificateForm) command;
		Certificate certificate = form.getCertificate();
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		if (form.getEntity().equalsIgnoreCase(CertificateForm.COURSE_APPROVAL)) {
			CourseApproval courseApproval = form.getCourseApproval();
			courseApproval.setCertificate(certificate);
			accreditationService.saveCourseApproval(courseApproval);
			context.put("target", "showCourseApprovalSummary");			
			return new ModelAndView(closeApprovalTemplate, "context", context);
		}else if(form.getEntity().equalsIgnoreCase(CertificateForm.COURSE_CONFIG)){
                    context.put("certificate", form.getCertificate());
                    context.put("returnURL", form.getReturnURL());
                    context.put("templateId", form.getTemplateId());
                    return new ModelAndView(closeApprovalTemplate, "context", context);
                }
		return null;
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {

		//AddProviderValidator validator = (AddProviderValidator)this.getValidator();
		//CertificateForm form = (CertificateForm)command;
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
		CertificateForm form=(CertificateForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if (form.getEntity().equalsIgnoreCase(CertificateForm.COURSE_APPROVAL)) {
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