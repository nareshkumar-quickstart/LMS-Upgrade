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
@DiscriminatorValue("DATETIMECUSTOMFIELD")
public class DateTimeCustomField extends CustomField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateTimeCustomField() {
		super();
		// TODO Auto-generated constructor stub
	}

}
