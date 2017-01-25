package com.softech.vu360.lms.model;

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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@Table(name = "TRAININGADMINISTRATOR")
public class TrainingAdministrator implements SearchableKey {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "TRAININGADMINISTRATOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "TRAININGADMINISTRATOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TRAININGADMINISTRATOR_ID")
	private Long id;
	
	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "VU360USER_ID")
	private VU360User vu360User ;
	
	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer ;
	
	@Column(name = "MANAGEALLORGANIZATIONALGROUPTF")
	private Boolean managesAllOrganizationalGroups = true;
	
	
	@ManyToMany
	@JoinTable(name="TRININGADMINISTRATOR_ORGANISATIONALGROUP", joinColumns = @JoinColumn(name="TRAININGADMINISTRATOR_ID"),inverseJoinColumns = @JoinColumn(name="ORGANISATIONALGROUP_ID"))
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<OrganizationalGroup> managedGroups = new ArrayList<OrganizationalGroup>();

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
	 * @return the managedGroups
	 */
	public List<OrganizationalGroup> getManagedGroups() {
		List<OrganizationalGroup> prefferedOrgGroup = new ArrayList<OrganizationalGroup>();
		try{
			if(managedGroups!=null){
				for (OrganizationalGroup orgGrp : managedGroups) {
					prefferedOrgGroup.add(orgGrp);
					if (orgGrp.getChildrenOrgGroups() != null) {
						prefferedOrgGroup.addAll(orgGrp.getChildrenOrgGroups());
						this.arrangeOrgGroups(orgGrp.getChildrenOrgGroups(), prefferedOrgGroup);
					}
				}
			}	
		}
		catch(Exception e){
			System.out.println("!!!!!!!!!!!!!!!!!!!! Exception Caught while getting manged groups from getManagedGroups() in TrainingAdministrator !!!!!!!!!!!!!!");
			// Use the following code in case of exception.
			//List tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
			e.printStackTrace();
			throw e;
		}
		return prefferedOrgGroup;
	}

	private List<OrganizationalGroup> arrangeOrgGroups(List<OrganizationalGroup> orgGroups, List<OrganizationalGroup> orgGrpList) {
		
		for (OrganizationalGroup orgGrp : orgGroups) {
			orgGrpList.add(orgGrp);
			if (orgGrp.getChildrenOrgGroups() != null) {
				orgGrpList.addAll(orgGrp.getChildrenOrgGroups());
				this.arrangeOrgGroups(orgGrp.getChildrenOrgGroups(), orgGrpList);
			}
		}
		return orgGrpList;
	}

	/**
	 * @param orgGroup
	 *            the orgGroup to add to the managedGroups set
	 */
	public void addManagedGroup(OrganizationalGroup orgGroup) {
		managedGroups.add(orgGroup);
	}

	/**
	 * @param managedGroups
	 *            the managedGroups to set
	 */
	public void setManagedGroups(List<OrganizationalGroup> managedGroups) {
		this.managedGroups = managedGroups;
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
	 * @return the vu360User
	 */
	public VU360User getVu360User() {
		return this.vu360User;
	}

	/**
	 * @param vu360User
	 *            the vu360User to set
	 */
	public void setVu360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return this.customer ;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the managesAllOrganizationalGroups
	 */
	public  Boolean isManagesAllOrganizationalGroups() {
		return managesAllOrganizationalGroups;
	}

	/**
	 * @param managesAllOrganizationalGroups
	 *            the managesAllOrganizationalGroups to set
	 */
	public void setManagesAllOrganizationalGroups(
			 Boolean managesAllOrganizationalGroups) {
		this.managesAllOrganizationalGroups = managesAllOrganizationalGroups;
	}

}
