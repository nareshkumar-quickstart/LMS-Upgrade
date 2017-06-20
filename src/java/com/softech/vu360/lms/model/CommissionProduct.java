package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COMMISSIONPRODUCT")
public class CommissionProduct implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "COMMISSIONPRODUCT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COMMISSIONPRODUCT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COMMISSIONPRODUCT_ID")
	private Long id;
	
	@Column(name = "PRODUCTCODE")
	private String productCode;
	
	@OneToOne
    @JoinColumn(name="COMMISSIONPRODUCTCATEGORY_ID")
	private CommissionProductCategory commissionProductCategory ;
	
	@OneToOne
    @JoinColumn(name="COMMISSION_ID")
	private Commission commission ;
	
	@Column(name = "NAME")
	private String name;
	
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

	public CommissionProductCategory getCommissionProductCategory() {
		return this.commissionProductCategory;
	}

	public void setCommissionProductCategory(CommissionProductCategory commissionProductCategory) {
		this.commissionProductCategory = commissionProductCategory;
		
	}

	public Commission getCommission() {
		return this.commission;
	}

	public void setCommission(Commission commission) {
		this.commission = commission;
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
