package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Nationalized;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "RESOURCE")
public class LcmsResource implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private Long id;
	
    @Column(name = "LOCALE_ID")
	private Integer localeId;
	
    @Column(name = "RESOURCEKEY")
	private String resourceKey;
	
    @Nationalized
    @Column(name = "RESOURCEVALUE")
	private String resourceValue;
	
	@OneToOne
    @JoinColumn(name="LANGUAGE_ID")
	private Language language;
	
	@OneToOne
    @JoinColumn(name="BRANDING_ID")
	private Brand brand;	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Integer getLocaleId() {
		return localeId;
	}


	public void setLocaleId(Integer localeId) {
		this.localeId = localeId;
	}


	public String getResourceKey() {
		return resourceKey;
	}


	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}


	public String getResourceValue() {
		return resourceValue;
	}


	public void setResourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
	}

	
	public Language getLanguage() {
		return language;
	}


	public void setLanguage(Language language) {
		this.language = language;
	}


	public Brand getBrand() {
		return brand;
	}


	public void setBrand(Brand brand) {
		this.brand = brand;
	}


	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
