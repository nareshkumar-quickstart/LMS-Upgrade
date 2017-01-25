package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * User: Rehan
 * Date: June 15, 2011
 */
public class CommissionCategoryForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String id;
	private String productCategoryCode;
	private String name;
	private String commission;
	private String parentCommissionProductCategory;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public String getParentCommissionProductCategory() {
		return parentCommissionProductCategory;
	}
	public void setParentCommissionProductCategory(
			String parentCommissionProductCategory) {
		this.parentCommissionProductCategory = parentCommissionProductCategory;
	}
}
