package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "LICENSETYPE")
public class LicenseType  implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private Long Id;
	
    @Column(name = "LICENSETYPE")
	private String licenseType;
	
	
	public Long getId() {
		return Id;
	}



	public void setId(Long id) {
		Id = id;
	}



	public String getLicenseType() {
		return licenseType;
	}



	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}



	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
