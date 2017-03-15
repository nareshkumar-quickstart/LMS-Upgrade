package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.saleem
 * modified by marium.saud
 *
 */

@Entity
@Table(name = "ORGANIZATIONALGROUP")
public class OrganizationalGroup implements SearchableKey,Comparable<OrganizationalGroup> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@javax.persistence.TableGenerator(name = "ORGANIZATIONALGROUP_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ORGANIZATIONALGROUP", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ORGANIZATIONALGROUP_ID")
	private Long id;
	
	@Column(name = "NAME")
	private String name = null;
	
	
	@OneToOne
	@JoinColumn(name = "ROOTORGGROUP_ID")
	private OrganizationalGroup rootOrgGroup = null;
	
	
	@ManyToOne (cascade =  CascadeType.MERGE , fetch=FetchType.LAZY)
	@JoinColumn(name = "PARENTORGGROUP_ID")
	private OrganizationalGroup parentOrgGroup = null;
	
	
//	@OneToMany(mappedBy="parentOrgGroup" , cascade =  {CascadeType.ALL} )
    @OneToMany(cascade={CascadeType.MERGE, CascadeType.REMOVE})// BatchImport IS THE Impact Area IF changing FetchType.EAGER
    @JoinColumn(name="PARENTORGGROUP_ID")
    //@LazyCollection(LazyCollectionOption.FALSE)
	private List<OrganizationalGroup> childrenOrgGroups = new ArrayList<OrganizationalGroup>();
	
	
	@OneToOne//@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer ;

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
	 * @return the rootOrgGroup
	 */
	public OrganizationalGroup getRootOrgGroup() {
		return rootOrgGroup;
	}

	/**
	 * @param rootOrgGroup
	 *            the rootOrgGroup to set
	 */
	public void setRootOrgGroup(OrganizationalGroup rootOrgGroup) {
		this.rootOrgGroup = rootOrgGroup;
	}

	/**
	 * @return the parentOrgGroup
	 */
	public OrganizationalGroup getParentOrgGroup() {
		return parentOrgGroup;
	}

	/**
	 * @param parentOrgGroup
	 *            the parentOrgGroup to set
	 */
	public void setParentOrgGroup(OrganizationalGroup parentOrgGroup) {
		this.parentOrgGroup = parentOrgGroup;
	}

	public void addChildOrgGroup(OrganizationalGroup orgGroup) {
		orgGroup.setParentOrgGroup(this);
		orgGroup.setRootOrgGroup(this.rootOrgGroup);
		childrenOrgGroups.add(orgGroup);
	}

	/**
	 * @return the childrenOrgGroups
	 */
	public List<OrganizationalGroup> getChildrenOrgGroups() {
		return childrenOrgGroups;
	}

	/**
	 * @param childrenOrgGroups
	 *            the childrenOrgGroups to set
	 */
	public void setChildrenOrgGroups(List<OrganizationalGroup> childrenOrgGroups) {
		this.childrenOrgGroups = childrenOrgGroups;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return this.customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer ;
	}

    @Override
    public String toString() {
        return (this.getParentOrgGroup() != null ? this.getParentOrgGroup().toString() + ">" : "") + this.getName();
    }

	@Override
	public int compareTo(OrganizationalGroup o) {
		if(this.getName().compareTo(o.getName())<0)return -1;		
		return 1;
	}
	
	public OrganizationalGroup(Long id){
		this.id=id;
	}

	public OrganizationalGroup() {
		// TODO Auto-generated constructor stub
	}
}
