package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.DocumentUploadForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddDocumentInRegulatorValidator;
import com.softech.vu360.util.StoreDocument;

/**
 * @author Dyutiman
 * created on 26-june-2009
 *
 */
public class AddDocumentInRegulator extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddDocumentInRegulator.class.getName());

	private AccreditationService accreditationService = null;
	private MultipartResolver multipartResolver=null;
	private String finishTemplate = null;
	private String closeTemplate = null;

	public AddDocumentInRegulator() {
		super();
		setCommandName("documentUploadForm");
		setCommandClass(DocumentUploadForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/regulator/editRegulator/edit_regulator_add_documentStep1"
				, "accreditation/regulator/editRegulator/edit_regulator_add_documentStep2"
		});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {
			DocumentUploadForm form = (DocumentUploadForm) command;
			String id = request.getParameter("id");
			String entity = request.getParameter("entity");

			if( !StringUtils.isBlank(entity) ) {
				if( entity.equalsIgnoreCase(DocumentUploadForm.DOCUMENT_COURSE) ) {
					CourseApproval cApproval = accreditationService.loadForUpdateCourseApproval(Long.parseLong(id));
					form.setCourseApproval(cApproval);
					form.setEntity(DocumentUploadForm.DOCUMENT_COURSE);
				} else if( entity.equalsIgnoreCase(DocumentUploadForm.DOCUMENT_PROVIDER) ) {
					ProviderApproval pApproval = accreditationService.loadForUpdateProviderApproval(Long.parseLong(id));
					form.setProviderApproval(pApproval);
					form.setEntity(DocumentUploadForm.DOCUMENT_PROVIDER);
				} else if( entity.equalsIgnoreCase(DocumentUploadForm.DOCUMENT_INSTRUCTOR)) {
					InstructorApproval iApproval = accreditationService.loadForUpdateInstructorApproval(Long.parseLong(id));
					form.setInstructorApproval(iApproval);
					form.setEntity(DocumentUploadForm.DOCUMENT_INSTRUCTOR);
				} 
			} else {
				Regulator regulator = accreditationService.loadForUpdateRegulator(Long.parseLong(id));
				form.setRegulator(regulator);
				form.setEntity(DocumentUploadForm.DOCUMENT_REGULATOR);
			}
			Document doc = new Document();
			form.setDocument(doc);

			String file = request.getParameter("file");
			log.debug("getting file object in 'formBackingObject' method "+file);
			// this is useless here
			//form.setFileName(file);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		DocumentUploadForm form = (DocumentUploadForm) command;

		switch( page ) {

		case 0:
			break;
		case 1:
			// let's see if there's content there
			MultipartFile file = form.getFile();
			if ( file == null || file.isEmpty() ) {
				log.debug(" file is null ");
			} else {
				log.debug(" FILE SIZE - "+file.getSize());
				form.setFileName(file.getOriginalFilename());
				/*StoreDocument store = new StoreDocument();
				store.createForceDirectory("Regulator");
				store.createFile( file.getOriginalFilename(), file.getBytes());*/
				byte[] filed=file.getBytes();
				form.setFileData(filed);
			}
			break;

		default:
			break;
		}
		return super.referenceData(request, page);
	}

	protected ModelAndView processFinish( HttpServletRequest request,
			HttpServletResponse responce, Object command, BindException errors)	throws Exception {

		log.debug("IN processFinish");
		DocumentUploadForm form = (DocumentUploadForm) command;
		Document newDocument = form.getDocument();
		newDocument.setFileName(form.getFileName());
		List<Document> presentDocs = new ArrayList <Document>();
		String directoryName = form.getEntity() + "/";
		String docId=null;
		if( form.getRegulator() != null ) {
			/**
			 * Modified By Marium Saud
			 * Fix for ticket LMS-18781
			 * 1. UnTransient State for Document Object 
			 * 2. 'Violation of PRIMARY KEY constraint 'PK_ASSET'' 
			 * 3. failed to lazily initialize a collection of role: com.softech.vu360.lms.model.Regulator.customfieldValues, could not initialize proxy - no Session
			 * 4. After saving Document, load new document id from DB by passing name.
			 * Separating DBRegulator and form Regulator , set the required fields from dbRegulator to form and then pass it for save. 
			 */
			Regulator regulator = form.getRegulator(); 
			//presentDocs = regulator.getDocuments(); //previously fetched -- results in lazy initialization exception
			long regulatorId=regulator.getId(); //get id of regulator 
			Regulator dbRegulator=accreditationService.getRegulatorById(regulatorId); //fetch regulator from service
			presentDocs=dbRegulator.getDocuments();
			presentDocs.add(newDocument);
			regulator.setDocuments(presentDocs);
			regulator.setCustomfieldValues(dbRegulator.getCustomfieldValues());
			directoryName = directoryName + regulator.getId().toString();
			regulator=accreditationService.saveRegulator(regulator);

		} else if( form.getCourseApproval() != null ) {
			CourseApproval cApproval = accreditationService.getCourseApprovalById(form.getCourseApproval().getId());
			presentDocs = cApproval.getDocuments();
			presentDocs.add(newDocument);
			cApproval.setDocuments(presentDocs);
			directoryName = directoryName + cApproval.getId().toString();
			cApproval=	accreditationService.saveCourseApproval(cApproval);
		} else if( form.getProviderApproval() != null ) {
			ProviderApproval pApproval = accreditationService.getProviderApprovalDeleteFalseById(form.getProviderApproval().getId());
			presentDocs = pApproval.getDocuments();
			presentDocs.add(newDocument);
			pApproval.setDocuments(presentDocs);
			directoryName = directoryName + pApproval.getId().toString();
			pApproval=accreditationService.saveProviderApproval(pApproval);
		} else if( form.getInstructorApproval() != null ) {
			InstructorApproval iApproval = accreditationService.getInstructorApprovalDeleteFalseById(form.getInstructorApproval().getId());
			presentDocs = iApproval.getDocuments();
			presentDocs.add(newDocument);
			iApproval.setDocuments(presentDocs);
			directoryName = directoryName + iApproval.getId().toString();
			iApproval=accreditationService.saveInstructorApproval(iApproval);
		}
		MultipartFile file = form.getFile();
		 // byte[] file1  = form.getFile().getBytes();
		if ( file == null || file.isEmpty() ) {
			log.debug(" file is null ");
		} else {
			newDocument = accreditationService.loadDocumentByName(newDocument.getName());
			docId=newDocument.getId().toString();
			log.debug(" SAVING FILE ");
			StoreDocument store = new StoreDocument();
			store.createForceDirectory(directoryName);
			store.createFile(docId.toString() + "_" + file.getOriginalFilename(), form.getFileData());
		}
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		if(form.getEntity().equalsIgnoreCase(form.DOCUMENT_PROVIDER)){
			context.put("target", "showProviderApprovalDocuments");
			return new ModelAndView(closeTemplate, "context", context);

		}else if(form.getEntity().equalsIgnoreCase(form.DOCUMENT_INSTRUCTOR)){
			context.put("target", "showInstructorApprovalDocuments");
			return new ModelAndView(closeTemplate, "context", context);

		}else if(form.getEntity().equalsIgnoreCase(form.DOCUMENT_COURSE)){
			context.put("target", "showCourseApprovalDocuments");
			return new ModelAndView(closeTemplate, "context", context);
			
		}else {
			return new ModelAndView(finishTemplate);

		} 
			
		
	}

	protected void validatePage(Object command, Errors errors, int page) {

		//TODO AddDocumentInRegulatorValidator validator = (AddDocumentInRegulatorValidator)this.getValidator();
		AddDocumentInRegulatorValidator validator = (AddDocumentInRegulatorValidator)this.getValidator();
		DocumentUploadForm form = (DocumentUploadForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			validator.validateFirstPage(form, errors);
			break;
		case 1:
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		
		
		DocumentUploadForm form = (DocumentUploadForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		if(form.getEntity().equalsIgnoreCase(form.DOCUMENT_PROVIDER)){
			context.put("target", "showProviderApprovalDocuments");
			return new ModelAndView(closeTemplate, "context", context);

		} else if(form.getEntity().equalsIgnoreCase(form.DOCUMENT_INSTRUCTOR)){
			context.put("target", "showInstructorApprovalDocuments");
			return new ModelAndView(closeTemplate, "context", context);
			
		}else if(form.getEntity().equalsIgnoreCase(form.DOCUMENT_COURSE)){
			context.put("target", "showCourseApprovalDocuments");
			return new ModelAndView(closeTemplate, "context", context);
			
		}else {
			return new ModelAndView(finishTemplate);

		}
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
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

	/**
	 * @return the multipartResolver
	 */
	public MultipartResolver getMultipartResolver() {
		return multipartResolver;
	}

	/**
	 * @param multipartResolver the multipartResolver to set
	 */
	public void setMultipartResolver(MultipartResolver multipartResolver) {
		this.multipartResolver = multipartResolver;
	}

}