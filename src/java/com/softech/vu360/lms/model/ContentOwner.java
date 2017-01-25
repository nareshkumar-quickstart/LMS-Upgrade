package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @Date 14-09-2015
 * @author haider.ali
 *
 */

@Entity
@Table(name = "CONTENTOWNER")
public class ContentOwner extends Owner implements Serializable{

	private static final long serialVersionUID = 7672870353608364180L;

	@Id
	@javax.persistence.TableGenerator(name = "CONTENTOWNER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CONTENTOWNER", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CONTENTOWNER_ID")
	private Long id;
	
	@Column(name = "NAME")
	private String name = null;
	@Column(name = "GUID")
	private String guid = null;
	@Column(name = "PLANTYPE_ID")
	private Long planTypeId  = null;
	@Column(name = "MAXAUTHORCOUNT")
	private Long maxAuthorCount  = null;
	@Column(name = "MAXCOURSECOUNT")
	private Long maxCourseCount  = null;
	@Column(name = "CURRENTAUTHORCOUNT")
	private Long currentAuthorCount  = null;
	@Column(name = "CURRENTCOURSECOUNT")
	private Long currentCourseCount  = null;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_ID")
	private Customer customer;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="CONTENTOWNER_REGULATORYANALYST", joinColumns = @JoinColumn(name="CONTENTOWNER_ID"),inverseJoinColumns = @JoinColumn(name="REGULATORYANALYST_ID"))
	private List<RegulatoryAnalyst> regulatoryAnalysts = new ArrayList<RegulatoryAnalyst>();
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String getOwnerType() {
		return "CONTENTOWNER";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public  boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContentOwner other = (ContentOwner) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getPlanTypeId() {
		return planTypeId;
	}

	public void setPlanTypeId(Long planTypeId) {
		this.planTypeId = planTypeId;
	}

	public Long getMaxAuthorCount() {
		return maxAuthorCount;
	}

	public void setMaxAuthorCount(Long maxAuthorCount) {
		this.maxAuthorCount = maxAuthorCount;
	}

	public Long getMaxCourseCount() {
		return maxCourseCount;
	}

	public void setMaxCourseCount(Long maxCourseCount) {
		this.maxCourseCount = maxCourseCount;
	}

	public Long getCurrentAuthorCount() {
		return currentAuthorCount;
	}

	public void setCurrentAuthorCount(Long currentAuthorCount) {
		this.currentAuthorCount = currentAuthorCount;
	}

	public Long getCurrentCourseCount() {
		return currentCourseCount;
	}

	public void setCurrentCourseCount(Long currentCourseCount) {
		this.currentCourseCount = currentCourseCount;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public void initializeOwnerParams(){//This method will be used to set values for Owner while saving survey
		super.setId(this.id);
		super.setOwnerType(getOwnerType());
	}

	public List<RegulatoryAnalyst> getRegulatoryAnalysts() {
		return regulatoryAnalysts;
	}

	public void setRegulatoryAnalysts(List<RegulatoryAnalyst> regulatoryAnalysts) {
		this.regulatoryAnalysts = regulatoryAnalysts;
	}


}
