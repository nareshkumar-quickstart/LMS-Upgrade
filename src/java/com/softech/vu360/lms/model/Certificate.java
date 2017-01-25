package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;


/**
 * 
 * @author marium.saud
 *
 */
@Entity
@DiscriminatorValue("Certificate")
public class Certificate extends Document {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Transient
	private String certificateType = null;
	
	public Certificate(){
		super(ASSET_TYPE_CERTIFICATE);
	}
	/**
	 * @return the certificateType
	 */
	public String getCertificateType() {
		return certificateType;
	}
	/**
	 * @param certificateType the certificateType to set
	 */
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	/**
	 * @return the noOfCertificatePerPage
	 */
	public Integer getNoOfCertificatePerPage() {
		return super.getNoOfDocumentsPerPage();
	}
	/**
	 * @param noOfCertificatePerPage the noOfCertificatePerPage to set
	 */
	public void setNoOfCertificatePerPage(Integer noOfCertificatePerPage) {
		this.setNoOfDocumentsPerPage(noOfCertificatePerPage);
	}

}