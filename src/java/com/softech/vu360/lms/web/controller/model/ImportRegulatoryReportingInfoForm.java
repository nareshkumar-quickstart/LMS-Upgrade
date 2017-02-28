package com.softech.vu360.lms.web.controller.model;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ImportRegulatoryReportingInfoForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	
	private MultipartFile file;
	private String fileName;
	private byte[] fileData;
	private boolean hasErrors = Boolean.FALSE;
	private String fileErrors;
	private String errorFileName;
	private String reportingMethod = "courseReportingDate";
	
	public String getErrorFileName() {
		return errorFileName;
	}
	public void setErrorFileName(String errorFileName) {
		this.errorFileName = errorFileName;
	}
	public String getFileErrors() {
		return fileErrors;
	}
	public void setFileErrors(String fileErrors) {
		this.fileErrors = fileErrors;
	}
	public boolean isHasErrors() {
		return hasErrors;
	}
	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	public String getReportingMethod() {
		return reportingMethod;
	}
	public void setReportingMethod(String reportingMethod) {
		this.reportingMethod = reportingMethod;
	}

	
	
}
