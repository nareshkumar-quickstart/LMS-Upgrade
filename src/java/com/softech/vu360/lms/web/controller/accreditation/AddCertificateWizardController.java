package com.softech.vu360.lms.web.controller.accreditation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.DocumentForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.CertificateValidator;
import com.softech.vu360.util.StoreDocument;

/**
 * @author Dyutiman
 * created on 3rd july
 *
 */
public class AddCertificateWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddCertificateWizardController.class.getName());
	
	private AccreditationService accreditationService;
	private String cancelTemplate = null;
	private String finishTemplate = null;
	
	public AddCertificateWizardController() {
		super();
		setCommandName("certificateForm");
		setCommandClass(DocumentForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/approvals/addCertificate/add_certificate_step1"
				, "accreditation/approvals/addCertificate/add_certificate_step2"});
	}
	
	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {
			DocumentForm form = (DocumentForm) command;
			Certificate certificate = new Certificate();
			form.setDocument(certificate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		Map <Object, Object>model = new HashMap <Object, Object>();
		DocumentForm form = (DocumentForm) command;
		
		switch(page){

		case 0:
			break;
		case 1:
			MultipartFile file = form.getFile();
			if ( file == null || file.isEmpty() ) {
				log.debug(" file is null ");
			} else {
				log.debug(" SAVING FILE ");
				byte[] filedata = file.getBytes();
				form.setFileData(filedata);
				model.put("certificateName", file.getOriginalFilename());
			}
			return model;
		default:
			break;
		}
		return super.referenceData(request, page);
	}
	
	protected void validatePage(Object command, Errors errors, int page) {

		CertificateValidator validator = (CertificateValidator)this.getValidator();
		DocumentForm form = (DocumentForm)command;
		errors.setNestedPath("");
		switch(page) {
		case 0:
			validator.validateAddPage(form, errors);
			break;
		case 1:
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}
	
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {

		log.debug("IN processFinish");
		DocumentForm form = (DocumentForm) command;
		Certificate certificate = (Certificate) form.getDocument();
		certificate.setName(form.getDocument().getName().trim());
		MultipartFile file = form.getFile();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
		certificate.setContentowner(contentOwner);
		certificate.setFileName(file.getOriginalFilename());
		//certificate.setContentowner();
		//certificate.setNoOfCertificatePerPage(Integer.parseInt(form.getNoOfCertificatePerPage()));
		if ( file == null || file.isEmpty() ) {
			log.debug(" file is null ");
		} else {
			log.debug(" SAVING FILE ");
			StoreDocument store = new StoreDocument();
			//store.createForceDirectory(regulator.getId().toString());
			store.createFile( file.getOriginalFilename(), form.getFileData());
		}
		accreditationService.saveCertificate(certificate);
		return new ModelAndView(finishTemplate);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(cancelTemplate);
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
	
}