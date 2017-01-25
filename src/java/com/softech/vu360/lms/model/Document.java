package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.Nationalized;

/**
 * 
 * @author marium.saud
 *
 */

@Entity
@DiscriminatorValue("Document")
public class Document extends Asset {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ACTIVETF")
	private Boolean active = true;
	
	@Nationalized
	@Column(name = "DESCRIPTION")
	private String description = null;
	
	@Column(name = "FILENAME")
	private String fileName = null;
	
	@Column(name = "NUMOFCERTIFICATEPERPAGE")
	private Integer noOfDocumentsPerPage=1;

	@Transient
	private static String assetType = "Document";
	
	public Document() {
		super(ASSET_TYPE_DOCUMENT);
	}
	
	public Document(String assetType){
		super(assetType);
	}

	/**
	 * @return the active
	 */
	public  Boolean isActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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

	public Integer getNoOfDocumentsPerPage() {
		return noOfDocumentsPerPage;
	}

	public void setNoOfDocumentsPerPage(Integer noOfDocumentsPerPage) {
		this.noOfDocumentsPerPage = noOfDocumentsPerPage;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		Document.assetType = assetType;
	}
	
	

}