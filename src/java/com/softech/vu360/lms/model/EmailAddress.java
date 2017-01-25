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
@Table(name = "EMAILADDRESS")
public class EmailAddress implements SearchableKey{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "EMAILADDRESS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "EMAILADDRESS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "EMAILADDRESS_ID")
	private Long id;
	
	@Column(name = "EMAIL")
	private String email;
	
	public String getKey() {
		return id.toString();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
