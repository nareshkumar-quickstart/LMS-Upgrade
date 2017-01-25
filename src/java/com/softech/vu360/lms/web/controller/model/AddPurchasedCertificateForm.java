/**
 * 
 */
package com.softech.vu360.lms.web.controller.model;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author syed.mahmood
 *
 */
public class AddPurchasedCertificateForm  implements ILMSBaseInterface{
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
	private CourseApproval courseApproval = null;
	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}
	/**
	 * @param document the document to set
	 */
	public void setDocument(Document document) {
		this.document = document;
	}
	/**
	 * @return the file
	 */
	public MultipartFile getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	/**
	 * @return the courseApproval
	 */
	public CourseApproval getCourseApproval() {
		return courseApproval;
	}
	/**
	 * @param courseApproval the courseApproval to set
	 */
	public void setCourseApproval(CourseApproval courseApproval) {
		this.courseApproval = courseApproval;
	} 
	
	
}
