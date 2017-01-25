package com.softech.vu360.lms.web.controller.model.accreditation;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Dyutiman
 * created on 26-june-2009
 *
 */
public class DocumentForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Document document = null;
	private boolean selected = false;
	private MultipartFile file;
	private byte[] fileData;
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
}