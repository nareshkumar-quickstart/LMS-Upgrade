package com.softech.vu360.lms.web.controller.accreditation;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.AffidavitTemplate;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.AffidavitTemplateService;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.util.AssetUtil;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.AssetForm;
import com.softech.vu360.lms.webservice.client.LCMSClientWS;
import com.softech.vu360.util.DocumentSort;
import com.softech.vu360.util.HtmlEncoder;

/**
 * @author Dyutian
 * created on 2-july-2009
 */
public class ManageAndEditAssetController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageAndEditAssetController.class.getName());

	private AccreditationService accreditationService;
	private AuthorService authorService;

	private String searchAssetTemplate = null;
	private String editAssetTemplate = null;
	private String assetType =null;
	private LCMSClientWS lcmsClientWS = null;
	
	private AffidavitTemplateService affidavitTemplateService = null;
	
	public ManageAndEditAssetController() {
		super();
	}
	public ManageAndEditAssetController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		request.setAttribute("newPage","true");
		log.debug(" IN handleNoSuch Method ");
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("assetType", getAssetType());
		
		return new ModelAndView(getSearchAssetTemplate(), "context", context);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		
		AssetForm form = (AssetForm)command;
		
		if( methodName.equals("edit") ){
		
			String id = request.getParameter("id");

			if( id != null ) {

				if(getAssetType().equals(Asset.ASSET_TYPE_CERTIFICATE)){
					form.setAsset(accreditationService.getCertificateById(Long.parseLong(id)));
				}else if(getAssetType().equals(Asset.ASSET_TYPE_AFFIDAVIT)){
					form.setAsset(accreditationService.getAffidavitById(Long.parseLong(id)));
					ArrayList<AffidavitTemplate> templateList = getAffidavitTemplateService().getAllAffidavitTemplates();
					form.setTemplatesList(templateList);
				}
				
				form.setNoOfDocumentsPerPage(((Document)form.getAsset()).getNoOfDocumentsPerPage() + "");
			} 
			
		}else if(methodName.equals("save") || methodName.equals("preview")){
			
			if( request.getParameter("id") != null ) {				
				if(getAssetType().equals(Asset.ASSET_TYPE_CERTIFICATE)){
					form.setAsset(accreditationService.loadForUpdateCertificate(Long.parseLong(request.getParameter("id"))));
				}else if(getAssetType().equals(Asset.ASSET_TYPE_AFFIDAVIT)){
					form.setAsset(accreditationService.loadForUpdateAffidavit(Long.parseLong(request.getParameter("id"))));
				}
			}
			
			if ( form.getFile() == null || form.getFile().isEmpty() ) {
				log.debug(" file is null ");
			} else {
				form.setFileData(form.getFile().getBytes());
			}
			
		}
	}

	public ModelAndView search( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();

			//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			String name = (request.getParameter("name") == null) ? "" : request.getParameter("name");

			List assets = null;
			if(getAssetType().equals(Asset.ASSET_TYPE_CERTIFICATE)){
				assets = accreditationService.getCertficatesByName(name);
			}else if(getAssetType().equals(Asset.ASSET_TYPE_AFFIDAVIT)){
				assets = accreditationService.getAffidavitsByName(name);
			}
			
			if(assets == null){
				assets = new ArrayList();
			}
			
			name = HtmlEncoder.escapeHtmlFull(name).toString();
			context.put("name", name);
			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			String sortDirection = request.getParameter("sortDirection");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";

			context.put("showAll", showAll);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						DocumentSort sort = new DocumentSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(assets, sort);
					} else {
						DocumentSort sort = new DocumentSort();
						sort.setSortBy("name");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(assets, sort);
					}
				} 
			}
			
			context.put("sortDirection", sortDirection == null ? "0" : sortDirection);
			context.put("sortColumnIndex",  sortColumnIndex == null ? "0" : sortColumnIndex);
			
			context.put("assets", assets);
			context.put("assetType", getAssetType());
			
			return new ModelAndView(getSearchAssetTemplate(), "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(getSearchAssetTemplate());
	}

	public ModelAndView edit( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map <Object, Object> context = new HashMap <Object, Object>();
		context.put("assetType", getAssetType());
		return new ModelAndView(getEditAssetTemplate(), "context", context);
	}

	public ModelAndView delete( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try {
			
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("assetType", getAssetType());
			
			//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			String name = (request.getParameter("name") == null) ? "" : request.getParameter("name");
			
			int count = 0;
			boolean linkedWithCourseApproval = false;

			if( request.getParameterValues("assetIds") != null ) {
				Long[] id = new Long[request.getParameterValues("assetIds").length];
				String []checkID = request.getParameterValues("assetIds");
				for ( int i = 0 ; i < id.length ; i++ ) {
					id[i] = Long.valueOf(checkID[i]);					
				}
				log.debug("TO BE MARKED AS INACTIVE ::- "+id.length);
				
				if(getAssetType().equals(Asset.ASSET_TYPE_AFFIDAVIT)){
					linkedWithCourseApproval = getAccreditationService().isAffidavitLinkedWithCourseApproval(id);
				}else if(getAssetType().equals(Asset.ASSET_TYPE_CERTIFICATE)){
					linkedWithCourseApproval = getAccreditationService().isCertificateLinkedWithCourseApproval(id);
				}
				
				if(!linkedWithCourseApproval){
//					LCMSClientWS client = new LCMSClientWSImpl();
//					count = client.updateAssetStatus(id, false, loggedInUser.getId());
					
					for(long assetId : id){
							
						if(getAssetType().equals(Asset.ASSET_TYPE_AFFIDAVIT)){
							Affidavit affidavit = getAccreditationService().loadForUpdateAffidavit(assetId);
							affidavit.setActive(false);
							getAccreditationService().saveAffidavit(affidavit);
						}else if(getAssetType().equals(Asset.ASSET_TYPE_CERTIFICATE)){
							Certificate certificate = getAccreditationService().loadForUpdateCertificate(assetId);
							certificate.setActive(false);
							getAccreditationService().saveCertificate(certificate);
						}
						
						count++;
					}

				}else{
					errors.reject("error." + getAssetType().toLowerCase() + ".delete.linkedWithCourseApproval");
				}
				
			}
			
			if(!linkedWithCourseApproval && count ==  0){
				errors.reject("error." + getAssetType().toLowerCase() + ".delete.failure");
			}
			
			return new ModelAndView(getSearchAssetTemplate(), "context", context);
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(getSearchAssetTemplate());
	}

	public ModelAndView save( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {

		AssetForm form = (AssetForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("assetType", form.getAsset().getAssetType());
		
		try {
			
			Asset asset = form.getAsset();
			ContentOwner contentOwner = null;
			
			Document document = null;
			if(form.getAsset() instanceof Certificate)
				document = accreditationService.loadForUpdateCertificate(asset.getId());
			else if (form.getAsset() instanceof Affidavit)
				document = accreditationService.loadForUpdateAffidavit(asset.getId());
			
			if(document!=null)
				 contentOwner = document.getContentowner();
			
			if(document!=null && contentOwner!=null && !isValidUser(contentOwner.getId())){
				errors.reject("error.user.invalidOperation");
			}
			
			if( errors.hasErrors() ) {
				return new ModelAndView(getEditAssetTemplate());
			}
			
			boolean flag;
			if( form.getAsset() instanceof Document){
				flag = saveDocument(request, (AssetForm)command, errors);
			}

		} catch (Exception e) {
			log.debug("exception", e);
		}
		
		return new ModelAndView(getSearchAssetTemplate(), "context", context);
	}
	
	private boolean saveDocument(HttpServletRequest request, AssetForm form, BindException errors){		
		Document document = null;
		
		if(form.getAsset() instanceof Certificate){			
			document = accreditationService.loadForUpdateCertificate(form.getAsset().getId());
		}else if(form.getAsset() instanceof Affidavit){
			document = accreditationService.loadForUpdateAffidavit(form.getAsset().getId());
			Affidavit objAffidavit = (Affidavit)document;
			objAffidavit.setContent(((Affidavit)form.getAsset()).getContent());
			objAffidavit.setContent2(((Affidavit)form.getAsset()).getContent2());
			objAffidavit.setContent3(((Affidavit)form.getAsset()).getContent3());
			document = objAffidavit;
		}
		
		document.setName(form.getAsset().getName().trim());
		
		//Append target attribute in HTML link element;
		if(document instanceof Affidavit){
			Affidavit affidavit_ = (Affidavit) document;
			affidavit_.setContent(StringUtils.replace(affidavit_.getContent(), "href=", " target=\"_blank\" href="));
			affidavit_.setContent2(StringUtils.replace(affidavit_.getContent2(), "href=", " target=\"_blank\" href="));
			affidavit_.setContent3(StringUtils.replace(affidavit_.getContent3(), "href=", " target=\"_blank\" href="));
		}
		//Append target attribute in HTML link element;

		
//		LCMSClientWS lcmsClient = LCMSClientWS.getDefaultClient();
		boolean uploaded = false; 
			
		if(form.getFile() != null && form.getFile().getSize() > 0){
		
			document.setFileName(form.getFile().getOriginalFilename());
			uploaded = getLcmsClientWS().uploadAsset(document.getFileName(), form.getFileData());
			
			if(!uploaded){			
				errors.reject("error." +form.getAsset().getAssetType().toLowerCase()+ ".upload.failure");
				return false;
			}
			
		}else if(form.getAsset() instanceof Affidavit){
			document.setFileName("");
		}
		
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		
		document.setContentowner(contentOwner);
		
		long newAssetId = getLcmsClientWS().saveAsset(document, uploaded, loggedInUser.getId());
		
		if(newAssetId <= 0){			
			errors.reject("error." +form.getAsset().getAssetType().toLowerCase()+ ".save.failure");
			return false;
		}

		if(form.getAsset() instanceof Certificate){
			form.setAsset(accreditationService.getCertificateById(newAssetId, true));
		}else if(form.getAsset() instanceof Affidavit){
			form.setAsset(accreditationService.getAffidavitById(newAssetId, true));
		}
		
		form.setFile(null);
		
		return true;
	}

	public ModelAndView preview( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		try {
			
			AssetForm form = (AssetForm) command;
			
			URL url = AssetUtil.getAssetFilePathURL(form.getAsset());
			InputStream is = url.openStream();
			
			byte[] filedata = IOUtils.toByteArray(is);
			
//			byte[] filedata = client.downloadAssetStatus(form.getAsset().getId());
			if(filedata != null && filedata.length > 0){				
				form.setFileData(filedata);
				response.reset();
				response.setContentType("application/pdf");
				response.setContentLength(filedata.length);
				response.setHeader("Content-Disposition", "inline; filename="+((Document)form.getAsset()).getFileName());
				ServletOutputStream outputStream = response.getOutputStream();
	
				outputStream.write(filedata);
				outputStream.flush();
				
				outputStream.close();
				is.close();
			}else{
				errors.reject("error." + getAssetType().toLowerCase() + ".preview.filenotfound");
			}
			
			

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(getEditAssetTemplate());
	}

	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		if( methodName.equals("save")) {
			this.getValidator().validate(command, errors);
		}
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getSearchAssetTemplate() {
		return searchAssetTemplate;
	}

	public void setSearchAssetTemplate(String searchAssetTemplate) {
		this.searchAssetTemplate = searchAssetTemplate;
	}

	public String getEditAssetTemplate() {
		return editAssetTemplate;
	}

	public void setEditAssetTemplate(String editAssetTemplate) {
		this.editAssetTemplate = editAssetTemplate;
	}
	public String getAssetType() {
		return assetType;
	}
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
		
	public AuthorService getAuthorService() {
		return authorService;
	}
	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}
	private boolean isValidUser(long contentOwnerId){
		boolean flag = true;
		
		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
//		if(contentOwnerId > 0){
//			flag = loggedInUser.getContentOwner().getId() == contentOwnerId;
//		}
		
//		if(flag){
//			flag = getAuthorService().isAuthor(loggedInUser.getId());
//		}
		
		return flag;
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