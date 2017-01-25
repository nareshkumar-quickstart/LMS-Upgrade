package com.softech.vu360.lms.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import com.lowagie.text.DocumentException;
import com.softech.vu360.lms.exception.NoCertificateNumberFoundException;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;

public interface CertificateService {
	public ByteArrayOutputStream generateCertificate(LearnerEnrollment le)throws DocumentException,IOException,NoCertificateNumberFoundException, URISyntaxException;
	public ByteArrayOutputStream generateCertificate(LearnerEnrollment le, boolean isSelfReported)throws DocumentException,IOException,NoCertificateNumberFoundException, URISyntaxException;
	public String getCertificateURL(Long learnerEnrollmentId);
	public ByteArrayInputStream fetchCertificateFromURL (String urlToFetch,LearnerEnrollment learnerEnrollment) throws ClientProtocolException, IOException;
	public void updateCertificateIssueDate(LearnerCourseStatistics lcs);
	public void assignCompletionCertificateNo(LearnerCourseStatistics lcs,CourseApproval courseApproval) throws NoCertificateNumberFoundException;
}
