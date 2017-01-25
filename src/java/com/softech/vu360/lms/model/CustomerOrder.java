package com.softech.vu360.lms.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "Orderinfo")
public class CustomerOrder {

	    @OneToOne
	    @JoinColumn(name="Customer_ID")
		private Customer customer;

		@Id
	    @javax.persistence.TableGenerator(name = "CustomerOrder_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ORDERINFO", allocationSize = 1)
	    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CustomerOrder_ID")
		private Long id;
		
	    @Column(name = "Amount")
		private Float orderAmount;
	    
	    @Column(name = "OrderDate")
		private Date orderDate;
	    
	    @Column(name = "OrderGUID")
		private String orderGUID;
	    
	    @Column(name = "TransactionGUID")
		private String transactionGUID;
	    
		@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy="orderInfo" )
		private List<LineItem> lineItem;
	
	    public List<LineItem> getLineItem() {
			return lineItem;
		}

		public void setLineItem(List<LineItem> lineItem) {
			this.lineItem = lineItem;
		}

		public CustomerOrder() {
			//this.customer = new ValueHolder();;
		}
		
		public Long getId() {
			return this.id;
		}
		
		public Float getOrderAmount() {
			return this.orderAmount;
		}
		
		public Date getOrderDate() {
			return this.orderDate;
		}
		
		public String getOrderGUID() {
			return this.orderGUID;
		}
		
		public String getTransactionGUID() {
			return this.transactionGUID;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public void setOrderAmount(Float orderAmount) {
			this.orderAmount = orderAmount;
		}
		
		public void setOrderDate(Date orderDate) {
			this.orderDate = orderDate;
		}
		
		public void setOrderGUID(String orderGUID) {
			this.orderGUID = orderGUID;
		}
		
		public void setTransactionGUID(String transactionGUID) {
			this.transactionGUID = transactionGUID;
		}
	
		public Customer getCustomer() {
			return customer;
		}
	
		public void setCustomer(Customer customer) {
			this.customer = customer;
		}
	
	
}
