package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 
 * @author marium.saud
 *
 */

@Entity
@DiscriminatorValue("Image")
public class Image extends Asset {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Transient
	private static String assetType = "Image";
	
	public Image() {
	//	super(ASSET_TYPE_DOCUMENT);
	}
	
	public Image(String assetType){
	//	super(assetType);
	}

	

}