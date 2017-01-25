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
@DiscriminatorValue("MULTIPLELINETEXTCUSTOMFIELD")
public class MultipleLineTextCustomfield extends TextCustomField {

	/**
	 * 
	 */
	public MultipleLineTextCustomfield() {
		super();
	}

}
