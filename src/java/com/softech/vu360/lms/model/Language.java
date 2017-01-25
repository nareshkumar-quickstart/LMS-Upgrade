package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Haider.ali
 * 
 */

@Entity
@Table(name = "LANGUAGE")
public class Language implements SearchableKey {

	private static final long serialVersionUID = -6131255633489835842L;

	@Id
	@javax.persistence.TableGenerator(name = "LANGUAGE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LANGUAGE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LANGUAGE_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "LANGUAGE")
	private String language = null;
	@Column(name = "COUNTRY")
	private String country = null;
	@Column(name = "VARIANT")
	private String variant = null;
	@Transient
	public static final String DEFAULT_LANG="en";
	@Column(name = "DISPLAYNAME")
	private String displayName = null;
	
	public Language() {
		language = DEFAULT_LANG;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getKey() {
		return id.toString();
	}

	@Override
	public String toString() {
		return this.getLanguage();
	}
}
