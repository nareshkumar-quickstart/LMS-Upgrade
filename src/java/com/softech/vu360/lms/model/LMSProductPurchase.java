package com.softech.vu360.lms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "LMSPRODUCT_PURCHASE")
public class LMSProductPurchase   implements SearchableKey{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "LMSPRODUCT_PURCHASE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSPRODUCT_PURCHASE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSPRODUCT_PURCHASE_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="CUSTOMER_ID")
	private Customer customer;
	
	@OneToOne
    @JoinColumn(name="PRODUCT_ID")
	private LMSProducts lmsProduct ;
	
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LMSProducts getLmsProduct() {
		return lmsProduct;
	}

	public void setLmsProduct(LMSProducts lmsProduct) {
		this.lmsProduct = lmsProduct;
	}
}
