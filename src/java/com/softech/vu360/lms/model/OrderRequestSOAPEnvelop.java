/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "ORDER_REQUEST_SOAP_ENVELOP")
public class OrderRequestSOAPEnvelop implements SearchableKey {

	private static final long serialVersionUID = 1L;

	@Id
    @javax.persistence.TableGenerator(name = "ORDER_REQUEST_SOAP_ENVELOP_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ORDER_REQUEST_SOAP_ENVELOP", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ORDER_REQUEST_SOAP_ENVELOP_ID")
	private Long id;
	
    @Column(name="ORDERID")
	private String orderId;
	
    @Column(name="CUSTOMERNAME")
	private String customerName;
	
    @Column(name="TRANSACTIONGUID")
	private String transactionGUID;
	
    @Column(name="EVENTDATE")
	private Date eventDate;
	
    @Column(name="SOAPENVELOP")
	private String soapEnvelop;
	
    @Column(name="ORDER_ERROR")
	private String orderError;
	
	
	@Override
	public String getKey() {		
		return id.toString();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getTransactionGUID() {
		return transactionGUID;
	}


	public void setTransactionGUID(String transactionGUID) {
		this.transactionGUID = transactionGUID;
	}


	public Date getEventDate() {
		return eventDate;
	}


	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}


	public String getSoapEnvelop() {
		return soapEnvelop;
	}


	public void setSoapEnvelop(String soapEnvelop) {
		this.soapEnvelop = soapEnvelop;
	}


	public String getOrderError() {
		return orderError;
	}


	public void setOrderError(String orderError) {
		this.orderError = orderError;
	}
	
	
	
	

}
