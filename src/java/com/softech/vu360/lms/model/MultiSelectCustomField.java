/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("MULTISELECTCUSTOMFIELD")
public class MultiSelectCustomField extends CustomField {
	
	@Column(name = "CHECKBOXTF")	
	private  Boolean checkBox = Boolean.FALSE;
	public MultiSelectCustomField() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the checkBox
	 */
	public  Boolean getCheckBox() {
		if(checkBox==null){
			checkBox=Boolean.FALSE;
		}
		return checkBox;
	}
	/**
	 * @param checkBox the checkBox to set
	 */
	public void setCheckBox(Boolean checkBox) {
		if(checkBox==null){
			this.checkBox = Boolean.FALSE;
		}else{
			this.checkBox = checkBox;
		}
	}

}
