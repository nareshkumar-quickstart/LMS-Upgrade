package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.saleem
 * modified by marium.saud
 *
 */
@Entity
@Table(name = "DISTRIBUTORGROUP")
public class DistributorGroup implements SearchableKey {

	private static final long serialVersionUID = 1704957211920063166L;

	@Id
	@javax.persistence.TableGenerator(name = "DISTRIBUTORGROUP_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "DISTRIBUTORGROUP", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "DISTRIBUTORGROUP_ID")
	private Long id;

	 
	@ManyToMany
    @JoinTable(name="DISTRIBUTOR_DISTRIBUTORGROUP", joinColumns = @JoinColumn(name="DISTRIBUTORGROUP_ID",referencedColumnName="ID"),inverseJoinColumns = @JoinColumn(name="DISTRIBUTOR_ID",referencedColumnName="ID"))
    private List<Distributor> distributors = new ArrayList<Distributor>();
	
	@Column(name = "NAME")
	private String name = null;
	
	@OneToOne
    @JoinColumn(name = "BRAND_ID")
	private Brand brand = null;
	
	@Column(name = "ACTIVE")
	private Boolean active = true;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the distributors
	 */
	public List<Distributor> getDistributors() {
		return distributors;
	}

	/**
	 * @param dist
	 *            the distributor to add to the distributors set
	 */
	public void addDistributor(Distributor dist) {
		distributors.add(dist);
	}

	/**
	 * @param distributors
	 *            the distributors to set
	 */
	public void setDistributors(List<Distributor> distributors) {
		this.distributors = distributors;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the brand
	 */
	public Brand getBrand() {
		return brand;
	}

	/**
	 * @param brand
	 *            the brand to set
	 */
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
	}

	/**
	 * @return the active
	 */
	public  Boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
}
