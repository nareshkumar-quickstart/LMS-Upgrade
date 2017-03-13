package com.softech.vu360.lms.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface RegulatoryReportingService {
	
	public Map<String,Object> ReadCSVfile(MultipartFile file, String reportingMethod);
	public ByteArrayOutputStream getRegulatoryReportingErrorsCSV(List<Map<String,String>> errorList, String reportingMehtod);
	public boolean UpdateEnrollmentInfo(MultipartFile file);
}
