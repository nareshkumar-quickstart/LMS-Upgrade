package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.AffidavitTemplate;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.AffidavitTemplateService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.AssetForm;
import com.softech.vu360.lms.webservice.client.LCMSClientWS;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

/**
 * @author muhammad.ashrafi
 * created on 03rd Feb, 2012
 *
 */
public class AddAssetWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddAssetWizardController.class.getName());
	
	private AccreditationService accreditationService;
	private String cancelTemplate = null;
	private String finishTemplate = null;
	private String assetType = null;
	private LCMSClientWS lcmsClientWS = null;
	
	private AffidavitTemplateService affidavitTemplateService = null;
	
	public AddAssetWizardController() {
		super();
		setCommandName("assetForm");
		setCommandClass(AssetForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {"accreditation/approvals/asset/add_asset_step1", "accreditation/approvals/asset/add_asset_step2"});
	}
	
	protected void onBindOnNewForm(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		if(request.getParameter("assetType")!=null && request.getParameter("assetType").equalsIgnoreCase("Affidavit")){
			Brander brand = VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			  
			AssetForm form = (AssetForm)command;
			Affidavit objAffidavit = new Affidavit();
			objAffidavit.setContent(brand.getBrandElement("lms.accraditation.addAffidavit.content1.defaultText"));
			objAffidavit.setContent2(brand.getBrandElement("lms.accraditation.addAffidavit.content2.defaultText"));
			objAffidavit.setContent3(brand.getBrandElement("lms.accraditation.addAffidavit.content3.defaultText"));
			form.setAsset(objAffidavit);
		}
		super.onBindOnNewForm(request, command, errors);
	}

	
	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		
		try {
			
			AssetForm form = (AssetForm) command;
			
			if(getAssetType().equals(Asset.ASSET_TYPE_CERTIFICATE)){
				form.setAsset(new Certificate());
			}else if(getAssetType().equals(Asset.ASSET_TYPE_AFFIDAVIT)){
				form.setAsset(new Affidavit());
			}

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		Map <Object, Object>model = new HashMap <Object, Object>();

		AssetForm form = (AssetForm) command;
		
		switch(page){

		case 0:
			ArrayList<AffidavitTemplate> templateList = affidavitTemplateService.getAllAffidavitTemplates();
			form.setTemplatesList(templateList);
			break;
		case 1:
			if ( form.getFile() == null || form.getFile().isEmpty() ) {
				log.debug(" file is null ");
			} else {
				log.debug(" SAVING FILE ");
				form.setFileData(form.getFile().getBytes());
				model.put("assetFileName", form.getFile().getOriginalFilename());
			}
			return model;
		default:
			break;
		}
		return super.referenceData(request, page);
	}
	
	protected void validatePage(Object command, Errors errors, int page) {
		switch(page) {
		case 0:
			this.getValidator().validate(command, errors);
			break;
		case 1:
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}
	
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception {

		log.debug("IN processFinish");
		AssetForm form = (AssetForm) command;
		
		if(form.getAsset() instanceof Document){
			saveDocument(form);
		}
		
		Map <Object, Object> context = new HashMap <Object, Object>();
		context.put("assetType", getAssetType());
		return new ModelAndView(finishTemplate, "context", context);
	}
	
	private void saveDocument(AssetForm form){
		
		Document document = (Document)form.getAsset();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
//		LCMSClientWS lcmsClient = LCMSClientWS.getDefaultClient();
		
		
		//Append target attribute in HTML link element;
		if(document instanceof Affidavit){
			Affidavit affidavit_ = (Affidavit) document;
			affidavit_.setContent(StringUtils.replace(affidavit_.getContent(), "href=", " target=\"_blank\" href="));
			affidavit_.setContent2(StringUtils.replace(affidavit_.getContent2(), "href=", " target=\"_blank\" href="));
			affidavit_.setContent3(StringUtils.replace(affidavit_.getContent3(), "href=", " target=\"_blank\" href="));
		}
		//Append target attribute in HTML link element;
		
		document.setName(document.getName().trim());
		document.setContentowner(contentOwner);
		
		if(form.getFile()!=null && form.getFile().getOriginalFilename()!=null)
			document.setFileName(form.getFile().getOriginalFilename());
		else
			document.setFileName("");
		
		if(StringUtils.isNotBlank(form.getNoOfDocumentsPerPage())){
			document.setNoOfDocumentsPerPage(Integer.parseInt(form.getNoOfDocumentsPerPage()));
		}
		
		
		if ( form.getFile() == null || form.getFile().isEmpty() ) {
			log.debug(" file is null ");
		} else {
			log.debug(" SAVING FILE ");
			getLcmsClientWS().uploadAsset(document.getFileName(), form.getFileData());
		}
		
		long id = getLcmsClientWS().saveAsset(document, 
					form.getFile() != null && form.getFile().getSize() > 0,	// File uploaded or not? 
				loggedInUser.getId());
		
		if(document instanceof Certificate){
			form.setAsset(accreditationService.getCertificateById(id, true));
		}else if(document instanceof Affidavit){
			form.setAsset(accreditationService.getAffidavitById(id, true));
		}
	}
	@Override
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("IN processCancel");
		Map <Object, Object> context = new HashMap <Object, Object>();
		context.put("assetType", getAssetType());
		return new ModelAndView(cancelTemplate, "context", context);
	}
	
	
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	public String getCancelTemplate() {
		return cancelTemplate;
	}
	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}
	public String getFinishTemplate() {
		return finishTemplate;
	}
	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public LCMSClientWS getLcmsClientWS() {
		return lcmsClientWS;
	}

	public void setLcmsClientWS(LCMSClientWS lcmsClientWS) {
		this.lcmsClientWS = lcmsClientWS;
	}

	public AffidavitTemplateService getAffidavitTemplateService() {
		return affidavitTemplateService;
	}

	public void setAffidavitTemplateService(AffidavitTemplateService affidavitTemplateService) {
		this.affidavitTemplateService = affidavitTemplateService;
	}
	
	
	
	
}