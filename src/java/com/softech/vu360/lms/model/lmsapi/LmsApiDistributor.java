package com.softech.vu360.lms.model.lmsapi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.softech.vu360.lms.model.Distributor;

@Entity
@Table(name = "LMS_API_DISTRIBUTOR")
public class LmsApiDistributor {
	
	@Id
    @javax.persistence.TableGenerator(name = "LMS_API_DISTRIBUTOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMS_API_DISTRIBUTOR_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LMS_API_DISTRIBUTOR_ID")
	private long id;
	
	/**
	 * A one-to-one mapping is triggered by the fact that LmsApi is declared an entity and includes the Distributor entity as 
	 * an attribute.
	 */
	@OneToOne
    @JoinColumn(name="DISTRIBUTOR_ID")
	private Distributor distributor;
	
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
	
	public Distributor getDistributor() {
		return distributor;
	}
	
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
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
