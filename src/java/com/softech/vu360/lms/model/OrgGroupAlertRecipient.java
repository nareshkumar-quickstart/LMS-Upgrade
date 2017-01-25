package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
/**
 * 
 * @author marium.saud
 *
 */
@Entity
@DiscriminatorValue("OrgGroupAlertRecipient")
public class OrgGroupAlertRecipient extends AlertRecipient{

	//@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@ManyToMany
    @JoinTable(name="ALERTRECIPIENT_ORGANIZATIONALGROUP", joinColumns = @JoinColumn(name="ALERTRECIPIENT_ID"),inverseJoinColumns = @JoinColumn(name="ORGANIZATIONALGROUP_ID"))	
	private List<OrganizationalGroup> organizationalGroups = new ArrayList<OrganizationalGroup>();

	public List<OrganizationalGroup> getOrganizationalGroups() {
		return organizationalGroups;
	}

	public void setOrganizationalGroups(
			List<OrganizationalGroup> organizationalGroups) {
		this.organizationalGroups = organizationalGroups;
	}

}
