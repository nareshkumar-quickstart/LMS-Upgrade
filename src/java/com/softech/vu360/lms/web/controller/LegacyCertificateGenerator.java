package com.softech.vu360.lms.web.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.softech.vu360.lms.model.LegacyCredential;
import com.softech.vu360.lms.service.impl.LegacyCertificateService;
import com.softech.vu360.util.VU360Properties;

public class LegacyCertificateGenerator implements Controller{

	private LegacyCertificateService service;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LegacyCredential credential = service.getCertificateCredential(Integer.parseInt(request.getParameter("enrollmentId")));
		generatePDF(response, credential);
		return null;
	}

	private void generatePDF(HttpServletResponse response, LegacyCredential credential) {
		try {
			ByteArrayOutputStream baos = generateCertificate(credential);
			// setting some response headers
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			// setting the content type
			response.setContentType("application/pdf");
			// the content length is needed for MSIE!!!
			response.setContentLength(baos.size());
			// write ByteArrayOutputStream to the ServletOutputStream
			ServletOutputStream out = response.getOutputStream();
			baos.writeTo(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ByteArrayOutputStream generateCertificate(LegacyCredential credential) throws Exception {
		//URL url = new URL(credential.getCertificateTemplate());
		String url = credential.getCertificateTemplate();
		StringTokenizer tokens = new StringTokenizer(url, "/");
		BufferedInputStream certificateIO = new BufferedInputStream(new FileInputStream(VU360Properties.getVU360Property("lms.vu.certificate.url") + "/" + tokens.nextToken().toLowerCase() + "/" + tokens.nextToken())); 
		//InputStream certificateIO = url.openStream();
		String password=VU360Properties.getVU360Property("lms.server.certificate.SecurityKey");
		PdfReader reader = new PdfReader(certificateIO);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper stamp2 = new PdfStamper(reader, baos);

		stamp2.setEncryption(null, password.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);

		// filling in the form
		AcroFields formField = stamp2.getAcroFields();
		formField.setField("name",credential.getName());
		formField.setField("dob",credential.getDob());
		formField.setField("comp_date",credential.getCompDate());
		formField.setField("SID",String.valueOf(credential.getLegacyStudentID()));
		formField.setField("course_hours",credential.getCourseHours());
		formField.setField("career_title_id",credential.getCareerTitleId());
		formField.setField("drivers_license",credential.getDriversLicense());
		formField.setField("employee_id",credential.getEmployeeId());
		formField.setField("career",credential.getCareer());
		formField.setField("comp_zip",credential.getCompZip());
		formField.setField("comp_state",credential.getCompState());
		formField.setField("comp_city",credential.getCompCity());
		formField.setField("comp_address",credential.getCompAddress());
		formField.setField("license_exp",credential.getLicenseExp());
		formField.setField("reg_date",credential.getRegDate());
		formField.setField("approval_date_end",credential.getApprovalDateEnd());
		formField.setField("approval_date_start",credential.getApprovalDateStart());
		formField.setField("gender",credential.getGender());
		formField.setField("male",credential.getMale());
		formField.setField("female",credential.getFemale());
		formField.setField("ticket_num",credential.getTicketNum());
		formField.setField("court",credential.getCourt());
		formField.setField("course_num",credential.getCourseNum());
		formField.setField("Certificate_Number",credential.getCertificateNumber());
		formField.setField("comp_date3UK",credential.getCompDate3UK());
		formField.setField("comp_dateUK",credential.getCompDateUK());
		formField.setField("company",credential.getCompany());
		formField.setField("student_Company",credential.getStudentCompany());
		formField.setField("ssn",credential.getSsn());
		formField.setField("lastname",credential.getLastName());
		formField.setField("mi",credential.getMiddleName());
		formField.setField("email",credential.getEmail());
		formField.setField("firstname",credential.getFirstName());
		formField.setField("course_name",credential.getCourseName());
		formField.setField("course_number",credential.getCourseNumber());
		formField.setField("phone",credential.getPhone());
		formField.setField("license_num",credential.getLicenseNum());
		formField.setField("address",credential.getAddress());
		formField.setField("fulladdress",credential.getFullAddress1());
		formField.setField("full_address",credential.getFullAddress2());
		formField.setField("address2",credential.getAddress2());
		formField.setField("city_state",credential.getCityState());
		formField.setField("zip",credential.getZip());
		formField.setField("state",credential.getState());
		formField.setField("height",credential.getHeight());
		formField.setField("city",credential.getCity());
		formField.setField("address",credential.getAddress());
		formField.setField("certificate_num",credential.getCertificateNum());
		formField.setField("certificate_id",credential.getCertificateId());
		formField.setField("start_date",credential.getStartDate());
		formField.setField("comp_date6",credential.getCompDate6());
		formField.setField("comp_date5",credential.getCompDate5());
		formField.setField("comp_date4",credential.getCompDate4());
		formField.setField("comp_date3",credential.getCompDate3());
		formField.setField("comp_date2",credential.getCompDate2());
		formField.setField("courseSeq",credential.getCourseSeq());
		formField.setField("grade",credential.getGrade());

		stamp2.setFormFlattening(true);
		stamp2.close();

		if (certificateIO != null) {
			certificateIO.close();
			reader.close();
		}
		return baos;
	}

	public LegacyCertificateService getService() {
		return service;
	}

	public void setService(LegacyCertificateService service) {
		this.service = service;
	}
}
