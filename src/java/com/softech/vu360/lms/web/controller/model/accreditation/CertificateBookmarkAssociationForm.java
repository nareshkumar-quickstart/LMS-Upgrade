package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.model.CertificateBookmarkAssociation;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CertificateBookmarkAssociationForm implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;	
	private CertificateBookmarkAssociation  certificateBookmarkAssociation;
	
	public CertificateBookmarkAssociation getCertificateBookmarkAssociation() {
		return certificateBookmarkAssociation;
	}
	public void setCertificateBookmarkAssociation(
			CertificateBookmarkAssociation certificateBookmarkAssociation) {
		this.certificateBookmarkAssociation = certificateBookmarkAssociation;
	}				
	
		
	
	private String id;
	private String bookmarkLabel=null;
	private String  associatedField=null;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBookmarkLabel() {
		return bookmarkLabel;
	}
	public void setBookmarkLabel(String bookmarkLabel) {
		this.bookmarkLabel = bookmarkLabel;
	}
	public String getAssociatedField() {
		return associatedField;
	}
	public void setAssociatedField(String associatedField) {
		this.associatedField = associatedField;
	}
			
	public void reset(){
		this.setId(null);
		this.setBookmarkLabel(null);
		this.setAssociatedField(null);
	}

}
