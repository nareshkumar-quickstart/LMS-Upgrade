/**
 * 
 */
package com.softech.vu360.lms.web.controller.accreditation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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
import com.softech.vu360.lms.model.PurchaseCertificateNumber;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.AddPurchasedCertificateForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddPurchasedCertificateValidator;

import au.com.bytecode.opencsv.CSVReader;


/**
 * @author syed.mahmood
 *
 */
public class AddPurchasedCertificateWizardController  extends AbstractWizardFormController{

	
	private static final Logger log = Logger.getLogger(AddPurchasedCertificateWizardController.class.getName());

	private AccreditationService accreditationService = null;
	private MultipartResolver multipartResolver=null;
	private String finishTemplate = null;
	private String closeTemplate = null;
	
	public AddPurchasedCertificateWizardController() {
		super();
		setCommandName("addPurchasedCertificateForm");
		setCommandClass(AddPurchasedCertificateForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/approvals/editApproval/addPurchasedCertificateNumber/edit_approval_add_PurchasedCertificateStep1"
				, "accreditation/approvals/editApproval/addPurchasedCertificateNumber/edit_approval_add_PurchasedCertificateStep2"
		});
	}
	
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		AddPurchasedCertificateForm form = (AddPurchasedCertificateForm) command;

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
				byte[] filed=file.getBytes();
				form.setFileData(filed);
			}
			break;

		default:
			break;
		}
		return super.referenceData(request, page);
	}
	
	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {
			AddPurchasedCertificateForm form = (AddPurchasedCertificateForm) command;
			String id = request.getParameter("id");
			String entity = request.getParameter("entity");

			CourseApproval cApproval = accreditationService.loadForUpdateCourseApproval(Long.parseLong(id));
			form.setCourseApproval(cApproval);
			form.setEntity(AddPurchasedCertificateForm.COURSE);
			
			String file = request.getParameter("file");
			log.debug("getting file object in 'formBackingObject' method "+file);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}
	
	@Override
	protected ModelAndView processFinish(HttpServletRequest arg0,
			HttpServletResponse arg1, Object command, BindException arg3)
			throws Exception {
		
		log.debug("IN processFinish");
		AddPurchasedCertificateForm form = (AddPurchasedCertificateForm) command;
		Set<PurchaseCertificateNumber> newPurchaseCertificates = new HashSet <PurchaseCertificateNumber>();
		
		//Parse the file and create a collection of new Purchase Certificates and add them to the original collection to persist.
		CourseApproval cApproval = form.getCourseApproval();
		cApproval = accreditationService.getCourseApprovalById(cApproval.getId());//Due to Lazy Exception
		newPurchaseCertificates = parseSavePurchasedCertificateNumbersFile(cApproval, form);
		//Going to add purchase certificate numbers

//		cApproval.getPurchaseCertificateNumbers().addAll(newPurchaseCertificates);
		form.setCourseApproval(cApproval);
//		cApproval=	accreditationService.saveCourseApproval(cApproval);
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "showCourseApprovalPurchasedCertificate");
		return new ModelAndView(closeTemplate, "context", context);
	}
	
	/**
	 * Parse the csv file and return the final purchase certificate numbers collection after checking 
	 * duplicate certificate numbers
	 * @param form
	 * @return
	 */
	private Set<PurchaseCertificateNumber> parseSavePurchasedCertificateNumbersFile(CourseApproval courseApproval, AddPurchasedCertificateForm form) {
		
		Set<PurchaseCertificateNumber> purchasedCertificateNumbers = new HashSet<>();
		PurchaseCertificateNumber certificateNumber = null;
		if(courseApproval != null )
		{
			 CSVReader reader;
			try {
				reader = new CSVReader(new InputStreamReader(form.getFile().getInputStream()), '\t', '\'', 1);  // 1 means skip first line
			    String [] nextLine;
			    while ((nextLine = reader.readNext()) != null) {
			    	if(null!=nextLine[0] && (! StringUtils.isBlank(nextLine[0])) && (! nextLine[0].equals("")))
			    	{
				    	certificateNumber = new PurchaseCertificateNumber();
				    	certificateNumber.setCertificateNumber(nextLine[0]);
				    	certificateNumber.setUsed(false);
				    	certificateNumber.setCourseApproval(courseApproval);
				    	//certificateNumber.setNumericCertificateNumber(getLongFromString(certificateNumber.getCertificateNumber()));
				    	//certificateNumber.setCourseApproval(courseApproval);
				    	//Check certificate existence in course approval
				    	if(!alreadyAssociated(nextLine[0],courseApproval))
				    	{
				    		//LMS-15309 - Purchased Certificate Number will save one by one in database
				    		certificateNumber = accreditationService.addPurchaseCertificateNumber(certificateNumber);
				    		purchasedCertificateNumbers.add(certificateNumber);
				    	}
			    	}
			    }
			} catch (FileNotFoundException e) {
				log.debug("exception", e);
			} catch (IOException e) {
				log.debug("exception", e);
			}
		}
		return purchasedCertificateNumbers;
	}
	
	//LMS-15309  - field 'NumericCertificateNumber' in table 'purchasecertificate' is now useless
	/*private long getLongFromString(String  strValue) {
		try {
			return Long.valueOf(strValue.trim());
		} catch(NumberFormatException e) {
			return 0;
		}
	}*/
	
	/**
	 * checks whether the certificate number already associated with course approval
	 * @param currCertificateNumber
	 * @param certificateNumber
	 * @return
	 */
	public boolean alreadyAssociated(String currCertificateNumber,CourseApproval courseApproval)
	{
		boolean alreadyAssociated = false;
		//Set<PurchaseCertificateNumber> certificateNumber = courseApproval.getPurchaseCertificateNumbers();
		PurchaseCertificateNumber p = accreditationService.checkForPurchaseNumberAssociation(courseApproval, currCertificateNumber);
		if(p != null){
			alreadyAssociated = true;
		}
//		if(currCertificateNumber != null && !currCertificateNumber.equals("") && certificateNumber != null && certificateNumber.size() > 0){
//			Iterator iterator = certificateNumber.iterator();
//			PurchaseCertificateNumber purchaseCertificateNumber = null;
//			while(iterator.hasNext()){
//				purchaseCertificateNumber = (PurchaseCertificateNumber)iterator.next();
//				if(currCertificateNumber.equals(purchaseCertificateNumber.getCertificateNumber())){
//					alreadyAssociated = true;
//					break;
//				}
//			}
//		}
		return alreadyAssociated;
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		
		
		AddPurchasedCertificateForm form = (AddPurchasedCertificateForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("target", "showCourseApprovalPurchasedCertificate");
		return new ModelAndView(closeTemplate, "context", context);
	}
	
	protected void validatePage(Object command, Errors errors, int page) {

		//TODO AddDocumentInRegulatorValidator validator = (AddDocumentInRegulatorValidator)this.getValidator();
		AddPurchasedCertificateValidator validator = (AddPurchasedCertificateValidator)this.getValidator();
		AddPurchasedCertificateForm form = (AddPurchasedCertificateForm)command;
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


	/**
	 * @return the finishTemplate
	 */
	public String getFinishTemplate() {
		return finishTemplate;
	}


	/**
	 * @param finishTemplate the finishTemplate to set
	 */
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
	
	

}
