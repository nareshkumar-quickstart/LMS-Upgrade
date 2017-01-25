package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Haider.ali
 * @since 18 Nov. , 2015
 *  
 */
@Entity
@Table(name = "LICENSE_INDUSTRIES")
public class LicenseIndustry implements SearchableKey {

	private static final long serialVersionUID = 1L;
	@Id
	@javax.persistence.TableGenerator(name = "LICENSE_INDUSTRIES_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LICENSE_INDUSTRIES", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LICENSE_INDUSTRIES_ID")
	private Long Id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="SHORTNAME")
	private String shortName;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
