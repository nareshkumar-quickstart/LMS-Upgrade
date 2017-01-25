package com.softech.vu360.lms.web.controller.model;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class DocumentUploadForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String DOCUMENT_COURSE = "Course";
	public static final String DOCUMENT_PROVIDER = "Provider";
	public static final String DOCUMENT_INSTRUCTOR = "Instructor";
	public static final String DOCUMENT_REGULATOR = "Regulator";
	
	private Regulator regulator = null;
	private CourseApproval courseApproval = null;
	private ProviderApproval providerApproval = null;
	private InstructorApproval instructorApproval = null;
	private Document document;
	private MultipartFile file;
	private String fileName; 
	private String entity = null;
	private byte[] fileData; 
	public Regulator getRegulator() {
		return regulator;
	}
	public void setRegulator(Regulator regulator) {
		this.regulator = regulator;
	}
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
	public CourseApproval getCourseApproval() {
		return courseApproval;
	}
	public void setCourseApproval(CourseApproval courseApproval) {
		this.courseApproval = courseApproval;
	}
	public ProviderApproval getProviderApproval() {
		return providerApproval;
	}
	public void setProviderApproval(ProviderApproval providerApproval) {
		this.providerApproval = providerApproval;
	}
	public InstructorApproval getInstructorApproval() {
		return instructorApproval;
	}
	public void setInstructorApproval(InstructorApproval instructorApproval) {
		this.instructorApproval = instructorApproval;
	}
	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
	/**
	 * @return the fileData
	 */
	public byte[] getFileData() {
		return fileData;
	}
	/**
	 * @param fileData the fileData to set
	 */
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
}