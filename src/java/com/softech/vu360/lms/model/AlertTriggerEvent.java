package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "ALERTTRIGGEREVENT")
public class AlertTriggerEvent implements SearchableKey{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private Long id;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getKey() {
		return id.toString();
	}
	
	


	
}
