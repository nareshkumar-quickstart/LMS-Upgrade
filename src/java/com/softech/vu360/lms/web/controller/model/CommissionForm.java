package com.softech.vu360.lms.web.controller.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * User: Rehan
 * Date: May 15, 2011
 */
public class CommissionForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String id;
    private String distributor;
	private String applyToAllProducts;
    private String defaults;   
    private String type;
    private String payOnNetIncome;
    private String flat;
    private String flatFeeAmount;
    private String percentage;
    private String deleted; 
    private String name;
    private String commissionType;
	private String paymentMode;
    
    
    public void reset ()
    {
		this.setApplyToAllProducts(null);
		this.setDefaults(null);
		this.setDeleted(null);
		this.setDistributor(null);
		this.setFlat(null);
		this.setFlatFeeAmount(null);
		this.setId(null);
		this.setName(null);
		this.setPayOnNetIncome(null);
		this.setPercentage(null);
		this.setType(null);
		this.setPaymentMode(null);
    }

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setApplyToAllProducts(String applyToAllProducts) {
		this.applyToAllProducts = applyToAllProducts;
	}

	public String getApplyToAllProducts() {
		return applyToAllProducts;
	}

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}

	public String getDefaults() {
		return defaults;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setPayOnNetIncome(String payOnNetIncome) {
		this.payOnNetIncome = payOnNetIncome;
	}

	public String getPayOnNetIncome() {
		return payOnNetIncome;
	}

	public void setFlat(String flat) {
		this.flat = flat;
	}

	public String getFlat() {
		return flat;
	}

	public void setFlatFeeAmount(String flatFeeAmount) {
		this.flatFeeAmount = flatFeeAmount;
	}

	public String getFlatFeeAmount() {
		return flatFeeAmount;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

	public String getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
    
    
    
}
