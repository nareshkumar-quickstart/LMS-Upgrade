package com.softech.vu360.lms.vo;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class RegulatoryReportingParamSerialized implements Serializable{
	
	private static final long serialVersionUID = 2L;
	
	private MultipartFile file;
	
	private String fileName;
	
	public RegulatoryReportingParamSerialized(MultipartFile file, String fileName){
		this.file = file;
		this.fileName = fileName;
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
}
