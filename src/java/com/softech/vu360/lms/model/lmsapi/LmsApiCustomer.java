package com.softech.vu360.lms.model.lmsapi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.softech.vu360.lms.model.Customer;


/**
 * 
 * @author haider.ali
 * 
 */

@Entity
@Table(name = "LMS_API_CUSTOMER")
public class LmsApiCustomer {
	
    @Id
	@javax.persistence.TableGenerator(name = "LMS_API_CUSTOMER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMS_API_CUSTOMER_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LMS_API_CUSTOMER_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private long id;
	
	@OneToOne
    @JoinColumn(name="CUSTOMER_ID")
	private Customer customer;
	
	@Column(name = "API_KEY")
	private String apiKey;
	
	@Column(name = "ENVIRONMENT")
	private String environment;
	
	@Column(name = "PRIVILEGE")
	private String privilege;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public String getEnvironment() {
		return environment;
	}
	
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	public String getPrivilege() {
		return privilege;
	}
	
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	@Override
	public String toString() {
		return "LmsApiCustomer [id=" + id + ", customer=" + customer
				+ ", apiKey=" + apiKey + ", environment=" + environment
				+ ", privilege=" + privilege + "]";
	}
	
}
