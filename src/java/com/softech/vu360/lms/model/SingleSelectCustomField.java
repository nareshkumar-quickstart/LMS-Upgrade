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
@DiscriminatorValue("SINGLESELECTCUSTOMFIELD")
public class SingleSelectCustomField extends CustomField {
	
	public SingleSelectCustomField() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
