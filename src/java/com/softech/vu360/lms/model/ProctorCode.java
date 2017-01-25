/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "PROCTORCODE")
public class ProctorCode implements SearchableKey {
	
	@Id
	private Long id;
	
	@Column(name = "PROCTORCODEVALUE")
	private String proctorcodevalue = null;
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the proctorcodevalue
	 */
	public String getProctorcodevalue() {
		return proctorcodevalue;
	}
	/**
	 * @param proctorcodevalue the proctorcodevalue to set
	 */
	public void setProctorcodevalue(String proctorcodevalue) {
		this.proctorcodevalue = proctorcodevalue;
	}

}
