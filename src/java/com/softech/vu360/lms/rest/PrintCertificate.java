package com.softech.vu360.lms.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softech.vu360.lms.exception.NoCertificateNumberFoundException;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.service.CertificateService;
import com.softech.vu360.lms.service.EntitlementService;

@RestController
public class PrintCertificate {
	
	@Autowired
	private CertificateService certificateService;
	@Autowired
	private EntitlementService entitlementService;

	@RequestMapping(value="/certificate/{learnerEnrollmentId}/{coureName}.pdf", produces = "application/pdf")
	public @ResponseBody ResponseEntity<byte[]> getCertificatePDF(@PathVariable long learnerEnrollmentId, @PathVariable("coureName") String courseName)
			throws IOException, NoCertificateNumberFoundException, URISyntaxException, com.lowagie.text.DocumentException {
		
		LearnerEnrollment learnerEnrollment;
		
		ByteArrayOutputStream baos;
		HttpHeaders header;
		ResponseEntity<byte[]> response;
		
		baos = null;
		header = null;

		try {
			learnerEnrollment = entitlementService.getLearnerEnrollmentById(learnerEnrollmentId);
			baos = certificateService.generateCertificate(learnerEnrollment);
	
			header = new HttpHeaders();
			header.setExpires(0);
			header.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			header.setPragma("public");
			header.setContentType(MediaType.APPLICATION_PDF);
			// the content length is needed for MSIE!!!
			header.setContentLength(baos.size());
			response = new ResponseEntity<byte[]>(baos.toByteArray(), header, HttpStatus.OK);
		} 
		finally {
			if(null != baos) {
				baos.close();
				baos.reset();
				baos = null;
			}
			if(null != header)
				header = null;
		}
		
		return response;
	}
}
