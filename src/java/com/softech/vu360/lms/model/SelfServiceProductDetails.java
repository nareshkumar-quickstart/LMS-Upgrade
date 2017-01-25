package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/*
 *	
 *	@author kaunain.wajeeh
 *	
*/

@Entity
@Table(name = "SELFSERVICEPRODUCTDETAILS")
public class SelfServiceProductDetails {
	
	@Id
	@javax.persistence.TableGenerator(name = "SELFSERVICEPRODUCTDETAILS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SELFSERVICEPRODUCTDETAILS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SELFSERVICEPRODUCTDETAILS_ID")
	private Long id;
	
	@Column(name = "PRODUCTCODE")
	private String productCode;
	
	@Column(name = "PRODUCTTYPE")
	private String productType;
	
	@Column(name = "PRODUCTCATEGORY")
	private String productCategory;
	
	@Column(name = "EXERNALORGINIZATION_ID")
	private Long exernalOrginizationId;
	
	@Column(name = "EFFECTIVEFROM")
	private Date effectiveFrom;
	
	@Column(name = "EFFECTIVETO")
	private Date effectiveTo;
	
	@Column(name = "PURCHASEDATE")
	private Date purchaseDate;
	
	@Column(name = "ACTUALPRODUCTTYPE")
	private String actualProductType;
	
	@Column(name = "ACTUALPRODUCT_ID")
	private Long actualproductId;
	
	@Column(name = "USERNAME")
	private String username;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public Long getExernalOrginizationId() {
		return exernalOrginizationId;
	}
	public void setExernalOrginizationId(Long exernalOrginizationId) {
		this.exernalOrginizationId = exernalOrginizationId;
	}
	public Date getEffectiveFrom() {
		return effectiveFrom;
	}
	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}
	public Date getEffectiveTo() {
		return effectiveTo;
	}
	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getActualProductType() {
		return actualProductType;
	}
	public void setActualProductType(String actualProductType) {
		this.actualProductType = actualProductType;
	}
	public Long getActualproductId() {
		return actualproductId;
	}
	public void setActualproductId(Long actualproductId) {
		this.actualproductId = actualproductId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
