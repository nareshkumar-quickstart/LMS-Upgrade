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

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.AssetForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddApprovalAssetValidator;
import com.softech.vu360.util.CertificateSort;
import com.softech.vu360.util.HtmlEncoder;

/**
 * @author Saptarshi
 *
 */
public class LinkAssetWizardController  extends AbstractWizardFormController {


	private AccreditationService accreditationService;
	private String closeApprovalTemplate = null;
	private String assetType = null;
	private String targetAction = null;
	private String targetMethod = null;

	public LinkAssetWizardController() {
		super();
		setCommandName("assetForm");
		setCommandClass(AssetForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
			"accreditation/approvals/editApproval/addApprovalAsset/change_asset"
			, "accreditation/approvals/editApproval/addApprovalAsset/change_asset_confirm"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);

		AssetForm form=(AssetForm) command;
		
		if(getAssetType().equals(Asset.ASSET_TYPE_AFFIDAVIT)){
			long courseApprovalId = Long.parseLong(request.getParameter("approvalId"));
			if(courseApprovalId > 0){
				form.setReferenceId(courseApprovalId);
				form.setReferenceType(AssetForm.REFERENCE_TYPE_COURSE_APPROVAL);
				form.setReferenceObject(getAccreditationService().loadForUpdateCourseApproval(courseApprovalId));
			}
			
			if(form.getAsset() == null){
				form.setAsset(new Affidavit());
			}
		}
		
		return command;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		AssetForm form = (AssetForm)command;
		AddApprovalAssetValidator validator = (AddApprovalAssetValidator)this.getValidator();
		
		if(this.getTargetPage(request, page) == 1){
			if(StringUtils.isBlank(form.getSelectedAssetID())){
				if(getAssetType().equals(Asset.ASSET_TYPE_AFFIDAVIT)){
					errors.rejectValue("selectedAssetID", "error.affidavit.required");
				}else if(getAssetType().equals(Asset.ASSET_TYPE_CERTIFICATE)){
					errors.rejectValue("selectedAssetID", "error.certificate.required");
				}
			}
		}

		if(form.getAsset() instanceof Affidavit){
			if ( !StringUtils.isBlank(form.getSelectedAssetID()) ) {
				form.setAsset(getAccreditationService().getAffidavitById(Long.parseLong(form.getSelectedAssetID())));
			}
		}
		super.onBindAndValidate(request, command, errors, page);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		
		if (page == 0) {
			if (request.getParameter("action") != null && request.getParameter("action").equals("search")) {
				search(request, command, errors, page);
			}
		}
		super.postProcessPage(request, command, errors, page);
	}

	private void search(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
			
		if(this.getTargetPage(request, page) == 1){
			request.setAttribute("newPage","true");
			return;
		}
		
		AssetForm form = (AssetForm)command;
		HttpSession session = request.getSession();
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();

		List assets = null;
		
		//@MariumSaud : Not in Use
		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(getAssetType().equals(Asset.ASSET_TYPE_AFFIDAVIT)){
			assets = getAccreditationService().getAffidavitsByName(form.getAssetName());
		}
		
		form.setAssetName(HtmlEncoder.escapeHtmlFull(form.getAssetName()).toString());

		if( form.getSortColumnIndex() == null && session.getAttribute("sortColumnIndex") != null ){
			form.setSortColumnIndex(session.getAttribute("sortColumnIndex").toString());			
		}
		
		if( form.getSortDirection() == null && session.getAttribute("sortDirection") != null ){
					form.setSortDirection(session.getAttribute("sortDirection").toString());
		}
		
		if( form.getPageCurrIndex() == null ) {
			form.setPageCurrIndex(form.getPageIndex());
		}

		if(form.getAsset() instanceof Document){
		if( form.getSortColumnIndex() != null && form.getSortDirection() != null ) {

			if( form.getSortColumnIndex().equalsIgnoreCase("0")) {
					if(form.getSortDirection().equalsIgnoreCase("0")){
						CertificateSort sort = new CertificateSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(form.getSortDirection()));
						Collections.sort(assets,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("0");
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 0);
					} else {
						CertificateSort sort = new CertificateSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(form.getSortDirection()));
						Collections.sort(assets,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("0");
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 0);
					}
					// sorting against alias
				} 
			}	
			form.setAssets(assets);
			pagerAttributeMap.put("pageIndex", form.getPageIndex());
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {

		AssetForm form = (AssetForm)command;
		
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

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception {
	
		AssetForm form = (AssetForm) command;
		
		if(form.getAsset() instanceof Affidavit){
			if(form.getReferenceType().equals(AssetForm.REFERENCE_TYPE_COURSE_APPROVAL)){
				CourseApproval courseApproval = (CourseApproval)form.getReferenceObject();
				courseApproval.setAffidavit((Affidavit)form.getAsset());
				getAccreditationService().saveCourseApproval(courseApproval);
				
			}
		}
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("targetAction", getTargetAction());
		context.put("targetMethod", getTargetMethod());
		return new ModelAndView(closeApprovalTemplate, "context", context);
		
	}


	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		super.validatePage(command, errors, page, finish);
	}

	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("targetAction", getTargetAction());
		context.put("targetMethod", getTargetMethod());
		return new ModelAndView(closeApprovalTemplate, "context", context);

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
	 * @return the assetType
	 */
	public String getAssetType() {
		return assetType;
	}

	/**
	 * @param assetType the assetType to set
	 */
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	/**
	 * @return the targetAction
	 */
	public String getTargetAction() {
		return targetAction;
	}

	/**
	 * @param targetAction the targetAction to set
	 */
	public void setTargetAction(String targetAction) {
		this.targetAction = targetAction;
	}

	/**
	 * @return the targetMethod
	 */
	public String getTargetMethod() {
		return targetMethod;
	}

	/**
	 * @param targetMethod the targetMethod to set
	 */
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	
	
}