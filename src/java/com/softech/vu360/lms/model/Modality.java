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
@Table(name = "MODALITY")
public class Modality implements SearchableKey {

	@Id
	private long id;
	
	@Column(name = "NAME")
	private String name;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
