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
@DiscriminatorValue("SINGLELINETEXTCUSTOMFIELD")
public class SingleLineTextCustomFiled extends TextCustomField {

	/**
	 * 
	 */
	public SingleLineTextCustomFiled() {
		super();
	}
   
}
