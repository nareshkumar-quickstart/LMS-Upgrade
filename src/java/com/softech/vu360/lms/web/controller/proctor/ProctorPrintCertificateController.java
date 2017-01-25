/**
 * 
 */
package com.softech.vu360.lms.web.controller.proctor;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.service.CertificateService;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.util.VU360Properties;


/**
 * @author syed.mahmood
 *
 */
public class ProctorPrintCertificateController implements Controller{

	private static final Logger log = Logger.getLogger(ProctorPrintCertificateController.class.getName());
	private String errorTemplate = null;
	private String templatePrefix = null;
	private CertificateService certificateService= null;
	private ProctorService proctorService;
	
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) {
		
		List<LearnerEnrollment> learnerEnrollmentList = null;
		Long[] proctorEnrollmentIds = null;
		ByteArrayOutputStream baos = null;
		PdfReader reader = null;
		int contentLenght=0;
		String password=VU360Properties.getVU360Property("lms.server.certificate.SecurityKey");
		try{
			//Get all selected Ids from request
			String[] selectedProctorEnrollmentIds = request.getParameterValues("proctorEnrollmentId");
			if(selectedProctorEnrollmentIds != null && selectedProctorEnrollmentIds.length > 0)
			 {
				//Convert string array to long array to be passed to DAO layer
				proctorEnrollmentIds = new Long[selectedProctorEnrollmentIds.length];
				 for(int i =0; i < selectedProctorEnrollmentIds.length;i++)
				 {
					 proctorEnrollmentIds[i] = Long.valueOf(selectedProctorEnrollmentIds[i]);
				 }
			 
				//Get List of all selected Objects
				 learnerEnrollmentList = proctorService.getProctorEnrollmentsByIds(proctorEnrollmentIds);
				if(learnerEnrollmentList != null && learnerEnrollmentList.size() > 0)
				{
					//Write ByteArrayOutputStream to the ServletOutputStream
					ServletOutputStream out = response.getOutputStream();
					PdfCopyFields copy = new PdfCopyFields(out);
					copy.setEncryption(null, password.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
					for(LearnerEnrollment learnerEnrollment : learnerEnrollmentList){
						baos = certificateService.generateCertificate(learnerEnrollment);
						if(baos != null)
						{
							contentLenght = contentLenght + baos.size();
							reader = new PdfReader(baos.toByteArray(), password.getBytes());
							copy.addDocument(reader);
							reader.close();
						}
					}
					copy.close();
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					// setting the content type
					response.setContentType("application/pdf");
					// the content length is needed for MSIE!!!
					response.setContentLength(contentLenght);                                                                                              
					out.flush();
					out.close();
				}
			 }
			 return null;
		}
		catch (Exception ex) 
		{
				log.error(ex.getMessage(), ex);
				//throw new RuntimeException(ex);
				Map<Object, Object> context = new HashMap<Object, Object>();
				context.put("error","lms.learner.mycourses.printCertificate.certificateError" );
				//return new ModelAndView(templatePrefix+errorTemplate, "context", context);
		}
		Map<Object, Object> context = new HashMap<Object, Object>();
		return new ModelAndView(errorTemplate,"context",context);
	}

	/**
	 * @return the errorTemplate
	 */
	public String getErrorTemplate() {
		return errorTemplate;
	}

	/**
	 * @param errorTemplate the errorTemplate to set
	 */
	public void setErrorTemplate(String errorTemplate) {
		this.errorTemplate = errorTemplate;
	}

	/**
	 * @return the templatePrefix
	 */
	public String getTemplatePrefix() {
		return templatePrefix;
	}

	/**
	 * @param templatePrefix the templatePrefix to set
	 */
	public void setTemplatePrefix(String templatePrefix) {
		this.templatePrefix = templatePrefix;
	}

	/**
	 * @return the certificateService
	 */
	public CertificateService getCertificateService() {
		return certificateService;
	}

	/**
	 * @param certificateService the certificateService to set
	 */
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	/**
	 * @return the proctorService
	 */
	public ProctorService getProctorService() {
		return proctorService;
	}

	/**
	 * @param proctorService the proctorService to set
	 */
	public void setProctorService(ProctorService proctorService) {
		this.proctorService = proctorService;
	}
	
	

}
