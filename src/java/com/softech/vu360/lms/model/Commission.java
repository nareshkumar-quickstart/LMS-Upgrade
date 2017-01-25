package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COMMISSION")
public class Commission implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "COMMISSION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COMMISSION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COMMISSION_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="DISTRIBUTOR_ID")
    private Distributor distributor ;
    
    @Column(name = "APPLYTOALLPRODUCTSTF")
	private Boolean applyToAllProducts;
    
    @Column(name = "DEFAULTTF")
    private Boolean defaults;
    
    @Column(name = "TYPE")
    private Integer type;
    
    @Column(name = "PAYONNETINCOMETF")
    private Boolean payOnNetIncome;
    
    @Column(name = "FLATTF")
    private Boolean flat;
    
    @Column(name = "FLATFEEAMOUNT")
    private Double flatFeeAmount;
    
    @Column(name = "PERCENTAGE")
    private Double percentage;
    
    @Column(name = "DELETEDTF")
    private Boolean deleted; 
    
    @Column(name = "NAME")
    private String name;
    
    @Column(name = "COMMISSIONTYPE")
    private Integer commissionType;
    
    @Column(name = "PAYMENTMODETF")
    private Boolean paymentMode;
    
    @Override
	public String getKey() {
		return this.id.toString();
	}
    
    public Long getId() {
		return id;
	}
    
    public void setId(Long id) {
		this.id = id;
	}
    
    public Distributor getDistributor() {
		return this.distributor;
	}
    
    public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}
    
    public Boolean getApplyToAllProducts() {
		return applyToAllProducts;
	}
    
    public void setApplyToAllProducts(Boolean applyToAllProducts) {
		this.applyToAllProducts = applyToAllProducts;
	}
    
    public Boolean getDefaults() {
		return defaults;
	}
    
    public void setDefaults(Boolean defaults) {
		this.defaults = defaults;
	}
    
    public Integer getType() {
		return type;
	}
    
    public void setType(Integer type) {
		this.type = type;
	}
    
    public Boolean getPayOnNetIncome() {
		return payOnNetIncome;
	}
    
    public void setPayOnNetIncome(Boolean payOnNetIncome) {
		this.payOnNetIncome = payOnNetIncome;
	}
    
    public Boolean getFlat() {
		return flat;
	}
    
    public void setFlat(Boolean flat) {
		this.flat = flat;
	}
    
    public Double getFlatFeeAmount() {
		return flatFeeAmount;
	}
    
    public void setFlatFeeAmount(Double flatFeeAmount) {
		this.flatFeeAmount = flatFeeAmount;
	}
    
    public Double getPercentage() {
		return percentage;
	}
    
    public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
    
    public Boolean getDeleted() {
		return deleted;
	}
    
    public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
    
    public String getName() {
		return name;
	}
    
    public void setName(String name) {
		this.name = name;
	}
    
    
    public Integer getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(Integer commissionType) {
		this.commissionType = commissionType;
	}

	@Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

	public Boolean getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(Boolean paymentMode) {
		this.paymentMode = paymentMode;
	}
	
	
}
