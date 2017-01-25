package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

/**
 * @author haider.ali m.Saleem
 * 
 */

@Entity
//This stored procedure has two input parameter BRAND_NAME and BRAND_KEY.
@NamedStoredProcedureQuery(name = "Brand.addNewBrand", procedureName = "ADD_NEW_BRAND", parameters = {
		  @StoredProcedureParameter(mode = ParameterMode.IN, name = "BRAND_NAME", type = String.class),
		  @StoredProcedureParameter(mode = ParameterMode.IN, name = "BRAND_KEY", type = String.class) })
@Table(name = "BRAND")
public class Brand implements SearchableKey {

	private static final long serialVersionUID = 3037247226627282417L;

	@Id
	private Long id;
	
	@Nationalized
	@Column(name="NAME")
	private String brandName = null;
	
	@Column(name="BRANDKEY")
	private String brandKey = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandKey() {
		return brandKey;
	}

	public void setBrandKey(String brandKey) {
		this.brandKey = brandKey;
	}

	@Override
	public String getKey() {
		return id.toString();
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", brandName=" + brandName + ", brandKey="
				+ brandKey + "]";
	}

	
}
