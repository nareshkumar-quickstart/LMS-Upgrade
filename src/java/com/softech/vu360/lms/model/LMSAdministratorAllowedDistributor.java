package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author Haider.ali
 * @since 18 Nov. , 2015
 *  
 */
@Entity
@Table(name = "LMSADMINISTRATOR_ALLOWED_DISTRIBUTOR")
public class LMSAdministratorAllowedDistributor implements Serializable {

	private static final long serialVersionUID = -7476899343547155643L;

	@Id
	@javax.persistence.TableGenerator(name = "LMSADMINISTRATOR_ALLOWED_DISTRIBUTOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSADMINISTRATOR_ALLOWED_DISTRIBUTOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSADMINISTRATOR_ALLOWED_DISTRIBUTOR_ID")
	private Long Id;
	
	@Column(name="ALLOWED_DISTRIBUTOR_ID")	
	private Long allowedDistributorId;
	@Column(name="LMSADMINISTRATOR_ID")
	private Long lmsAdministratorId;
	@Column(name="DISTRIBUTORGROUP_ID")
	private Long distributorGroupId;

	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public Long getAllowedDistributorId() {
		return allowedDistributorId;
	}
	public void setAllowedDistributorId(Long allowedDistributorId) {
		this.allowedDistributorId = allowedDistributorId;
	}
	public Long getLmsAdministratorId() {
		return lmsAdministratorId;
	}
	public void setLmsAdministratorId(Long lmsAdministratorId) {
		this.lmsAdministratorId = lmsAdministratorId;
	}
	public Long getDistributorGroupId() {
		return distributorGroupId;
	}
	public void setDistributorGroupId(Long distributorGroupId) {
		this.distributorGroupId = distributorGroupId;
	}
	

	

}
