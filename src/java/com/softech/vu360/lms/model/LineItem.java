package com.softech.vu360.lms.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "ORDERLINEITEM")
public class LineItem {

	@Id
    @javax.persistence.TableGenerator(name = "OrderLineItem_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ORDERLINEITEM", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "OrderLineItem_ID")
	private Long id;
	
    @Column(name = "ITEMGUID")
	private String itemGUID;
	
    @Column(name = "Quantity")
	private Integer quantity;
	
	@OneToOne
    @JoinColumn(name="ORDER_ID")
	private CustomerOrder orderInfo ;
	
	@OneToOne
    @JoinColumn(name="ENTITLEMENT_ID")
	private CustomerEntitlement  customerEntitlement ;
	
    @Column(name = "REFUND_QUANTITY")
	private Integer refundQty;
	
    @Transient
	private String subItem;


    public String getSubItem() {
		return subItem;
	}

	public void setSubItem(String subItem) {
		this.subItem = subItem;
	}

	public Integer getRefundQty() {
		return refundQty;
	}

	public void setRefundQty(Integer refuntQty) {
		this.refundQty = refuntQty;
	}

	public CustomerEntitlement getCustomerEntitlement() {
		return customerEntitlement;
	}

	public void setCustomerEntitlement(CustomerEntitlement customerEntitlement) {
		this.customerEntitlement = customerEntitlement;
	}

	public LineItem() {
		//this.orderInfo = new ValueHolder() ;
	}
	
	public void addToRefundLineItem(Object anObject) {
		// Fill in method body here.
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getItemGUID() {
		return this.itemGUID;
	}
	
	public Integer getQuantity() {
		return this.quantity;
	}
	
	protected List getRefundLineItem() {
		// Fill in method body here.
		return null;
	}
	
	public void removeFromRefundLineItem(Object anObject) {
		// Fill in method body here.
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setItemGUID(String itemGUID) {
		this.itemGUID = itemGUID;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	protected void setRefundLineItem(List aList) {
		// Fill in method body here.
	}

	public CustomerOrder getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(CustomerOrder orderInfo) {
		this.orderInfo = orderInfo;
	}
	

}
