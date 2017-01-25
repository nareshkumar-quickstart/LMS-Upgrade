package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/*
 *	
 *	@author kaunain.wajeeh
 *	
 */

@Entity
@Table(name = "LmsApi")
public class LmsApi {

	@Id
	@javax.persistence.TableGenerator(name = "LMSAPI_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSAPI", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSAPI_ID")
	private long id;

	/**
	 * A one-to-one mapping is triggered by the fact that LmsApi is declared an
	 * entity and includes the Customer entity as an attribute.
	 */
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Customer_ID")
	private Customer customer;

	@Column(name = "Api_Key")
	private String apiKey;

	@Column(name = "Environment")
	private String environment;

	@Column(name = "Privilege")
	private String privilege;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
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

}
