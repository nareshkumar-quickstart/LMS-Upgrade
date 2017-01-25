package com.softech.vu360.lms.web.controller.model;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * 
 * @author haider.ali
 *
 */

public class ImportAffidavitTemplateIdsForm implements ILMSBaseInterface{

	
	private static final long serialVersionUID = 1L;
	public static final String COURSE = "Course";
	public static final String PROVIDER = "Provider";
	public static final String INSTRUCTOR = "Instructor";
	public static final String REGULATOR = "Regulator";
	
	private Document document;
	private MultipartFile file;
	private String fileName; 
	private String entity = null;
	private byte[] fileData;
	
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
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
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static String getCourse() {
		return COURSE;
	}
	public static String getProvider() {
		return PROVIDER;
	}
	public static String getInstructor() {
		return INSTRUCTOR;
	}
	public static String getRegulator() {
		return REGULATOR;
	}

	
	
}
