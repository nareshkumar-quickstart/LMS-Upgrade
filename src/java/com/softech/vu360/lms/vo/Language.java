package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class Language implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String language = DEFAULT_LANG;
	private String country = null;
	private String variant = null;
	private String displayName = null;

	public static final String DEFAULT_LANG = "en";

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
	public String toString() {
		return this.getLanguage();
	}
}
