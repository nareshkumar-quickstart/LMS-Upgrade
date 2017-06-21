package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COMMISSIONPRODUCTCATEGORY")
public class CommissionProductCategory implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "COMMISSIONPRODUCTCATEGORY_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COMMISSIONPRODUCTCATEGORY", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COMMISSIONPRODUCTCATEGORY_ID")
	private Long id;
	
	@Column(name = "PRODUCTCATEGORYCODE")
	private String productCategoryCode;
	
	@Column(name = "NAME")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="COMMISSION_ID")
	private Commission commission ;
	
	@OneToOne
    @JoinColumn(name="PARENT_ID")
	private CommissionProductCategory parentCommissionProductCategory ;;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getProductCategoryCode() {
		return productCategoryCode;
	}


	public void setProductCategoryCode(String productCategoryCode) {
		this.productCategoryCode = productCategoryCode;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public Commission getCommission() {
		return this.commission;
	}

	public void setCommission(Commission commission) {
		this.commission = commission;
	}

	public CommissionProductCategory getParentCommissionProductCategory() {
		return this.parentCommissionProductCategory; 
	}

	public void setParentCommissionProductCategory(CommissionProductCategory parentCommissionProductCategory) {
		this.parentCommissionProductCategory = parentCommissionProductCategory;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
