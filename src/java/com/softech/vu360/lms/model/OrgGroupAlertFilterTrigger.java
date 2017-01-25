package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * 
 * @author raja.ali
 *
 */
@Entity
@DiscriminatorValue("OrgGroupAlertFilterTrigger")
public class OrgGroupAlertFilterTrigger extends AlertTriggerFilter implements Serializable {

	private static final long serialVersionUID = -8596332822414136222L;
	
	@ManyToMany
    @JoinTable(name="ALERTTRIGGERFILTER_ORGANIZATIONALGROUP", joinColumns = @JoinColumn(name="ALERTTRIGGERFILTER_ID"),inverseJoinColumns = @JoinColumn(name="ORGANIZATIONALGROUP_ID"))
    private List<OrganizationalGroup> orggroup = new ArrayList<OrganizationalGroup>();

	public List<OrganizationalGroup> getOrgGroups() {
		return orggroup;
	}

	public void setOrggroup(List<OrganizationalGroup> orggroup) {
		this.orggroup = orggroup;
	}
}
