/**
 * 
 */
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

import org.apache.log4j.Logger;

/**
 * @author jason
 * 
 */
@Entity
@Table(name = "LMSADMINISTRATOR")
public class LMSAdministrator implements SearchableKey {

    private static final long serialVersionUID = -666704884103823556L;
	
	@Id
	@javax.persistence.TableGenerator(name = "LMSADMINISTRATOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LMSADMINISTRATOR", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LMSADMINISTRATOR_ID")
	private Long id;
	
	
	@Column(name = "GLOBALADMINISTRATORTF")
	private  Boolean globalAdministrator = false;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "VU360USER_ID")
	private VU360User vu360User ;
	
	/*@Transient*/
	@ManyToMany
    @JoinTable(name="LMSADMINISTRATOR_DISTRIBUTORGROUP", joinColumns = @JoinColumn(name="LMSADMINISTRATOR_ID",referencedColumnName="ID"),inverseJoinColumns = @JoinColumn(name="DISTRIBUTORGROUP_ID",referencedColumnName="ID"))
    private List<DistributorGroup> distributorGroups = new ArrayList<DistributorGroup>();

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
	 * @return the globalAdministrator
	 */
	public  Boolean isGlobalAdministrator() {
		return globalAdministrator;
	}

	/**
	 * @param globalAdministrator
	 *            the globalAdministrator to set
	 */
	public void setGlobalAdministrator(Boolean globalAdministrator) {
		this.globalAdministrator = globalAdministrator;
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
		this.vu360User=vu360User;
	}

	/**
	 * @return the distributorGroups
	 */
	public List<DistributorGroup> getDistributorGroups() {
		return distributorGroups;
	}

	/**
	 * @param distributorGroups
	 *            the distributorGroups to set
	 */
	public void setDistributorGroups(List<DistributorGroup> distributorGroups) {
		this.distributorGroups = distributorGroups;
	}

}
