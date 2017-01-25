package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "SUBSCRIPTION")
public class Subscription  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5633731256685828312L;

	@Id
    @javax.persistence.TableGenerator(name = "SUBSCRIPTION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SUBSCRIPTION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SUBSCRIPTION_ID")
	private Long id;
	
	@OneToOne
    @JoinColumn(name="CUSTOMERENTITLEMENT_ID")
	private CustomerEntitlement customerEntitlement ;
	
	@Column(name = "SUBSCRIPTION_CODE")
	private String subscriptionCode;
	
	@Column(name = "SUBSCRIPTION_NAME")
	private String subscriptionName;
	
	@Column(name = "SUBSCRIPTION_TYPE")
	private String subscriptionType;
	
	@Column(name = "SUBSCRIPTION_STATUS")
	private String subscriptionStatus;
	
	@Column(name = "SUBSCRIPTION_QTY")
	private Integer subscriptionQty;
	
	@Column(name = "USERNAME")
	private String userName;
	
	@Column(name = "CREATEDATE")
	private Date createDate;
	
	@OneToMany(mappedBy = "subscription") //No usage of set method
	private List<SubscriptionCourse> subscriptionCourses;
	
	@OneToOne
    @JoinColumn(name="SUBSCRIPTION_KIT_ID")
	private SubscriptionKit subscriptionKit = null;
	
	@Column(name = "CUSTOMER_TYPE")
	private String customerType;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST})
    @JoinTable(name="SUBSCRIPTION_USER", joinColumns = @JoinColumn(name="SUBSCRIPTION_ID"),inverseJoinColumns = @JoinColumn(name="VU360USER_ID"))
    private Set<VU360User> vu360User = new HashSet<VU360User>();
	
	//ID bigint NOT NULL,
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	//SUBSCRIPTION_CODE varchar(255) NOT NULL,
	public String getSubscriptionCode() {
		return subscriptionCode;
	}
	public void setSubscriptionCode(String subscriptionCode) {
		this.subscriptionCode = subscriptionCode;
	}
	
	//SUBSCRIPTION_TYPE varchar(255) NULL,
	public String getSubscriptionType() {
		return subscriptionType;
	}
	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
	
	//SUBSCRIPTION_STATUS varchar(50) NULL,
	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}
	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}
	
	//SUBSCRIPTION_QTY int NULL,
	public Integer getSubscriptionQty() {
		return subscriptionQty;
	}
	public void setSubscriptionQty(Integer subscriptionQty) {
		this.subscriptionQty = subscriptionQty;
	}

	//USERNAME varchar(255) NULL,
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	//CREATEDATE datetime NULL,
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	//CUSTOMERENTITLEMENT_ID BIGINT NOT NULL,
	public CustomerEntitlement getCustomerEntitlement() {
		return this.customerEntitlement;
	}
	public void setCustomerEntitlement(CustomerEntitlement customerEntitlement) {
		this.customerEntitlement =  customerEntitlement;
	}
	
	//SUBSCRIPTION_NAME varchar(100) NULL,
	public String getSubscriptionName() {
		return subscriptionName;
	}
	public void setSubscriptionName(String subscriptionName) {
		this.subscriptionName = subscriptionName;
	}
	
	public List<SubscriptionCourse> getSubscriptionCourses() {
		return subscriptionCourses;
	}
	public void setSubscriptionCourses(List<SubscriptionCourse> subscriptionCourses) {
		this.subscriptionCourses = subscriptionCourses;
	}
	public SubscriptionKit getSubscriptionKit() {
		return subscriptionKit;
	}
	public void setSubscriptionKit(SubscriptionKit subscriptionKit) {
		this.subscriptionKit = subscriptionKit;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public Set<VU360User> getVu360User() {
		return vu360User;
	}
	public void setVu360User(Set<VU360User> vu360User) {
		this.vu360User = vu360User;
	}
	
}
