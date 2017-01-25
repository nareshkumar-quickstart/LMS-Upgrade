package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * @author Haider.ali
 * @since 18 Nov. , 2015
 *  
 */
@Entity
@NamedQueries({
	@NamedQuery(name="BrandDomain.findBrandByDomain",query="SELECT a FROM BrandDomain a WHERE a.domain like (:domain)")
})
@Table(name = "BRANDDOMAIN")
public class BrandDomain {

	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "BRANDDOMAIN_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "BRANDDOMAIN", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "BRANDDOMAIN_ID")
	private long id;

	@Column(name="LANGUAGE")
	private String language;
	@Column(name="BRAND")
	private String brand;
	@Column(name="DOMAIN")
	private String domain;

public BrandDomain() {
}

public String getBrand() {
	return this.brand;
}

public String getDomain() {
	return this.domain;
}

public long getId() {
	return this.id;
}

public String getLanguage() {
	return this.language;
}

public void setBrand(String brand) {
	this.brand = brand;
}

public void setDomain(String domain) {
	this.domain = domain;
}

public void setId(long id) {
	this.id = id;
}

public void setLanguage(String language) {
	this.language = language;
}

}
