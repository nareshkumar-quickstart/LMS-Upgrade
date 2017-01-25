/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("SSNCUSTOMFIELD")
public class SSNCustomFiled extends TextCustomField {

	/**
	 * 
	 */
	public SSNCustomFiled() {
		super();
	}

}
