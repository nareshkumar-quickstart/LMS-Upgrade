package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="AuthorGroup.findAuthorGroupsByContentOwner",query="SELECT ag FROM AuthorGroup ag WHERE ag.contentOwner=:contentOwner ORDER BY ag.id ASC")
})
@Table(name = "LCMSAUTHORGROUP")
public class AuthorGroup implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "LCMSAUTHORGROUP_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LCMSAUTHORGROUP", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LCMSAUTHORGROUP_ID")
	private Long id;
	
	@Column(name = "GUID")
	private String guid;
	
	@Nationalized
	@Column(name = "NAME")
	private String name;
	
	@Nationalized
	@Column(name = "DESCRIPTION")
	private String description;
	
	@OneToOne
    @JoinColumn(name="CONTENTOWNER_ID")
	private ContentOwner contentOwner ;
	
	@OneToMany(mappedBy="authorGroup" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<AuthorPermission> permissions = new ArrayList<AuthorPermission>();
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "LastUpdatedDate")
	private Date lastUpdatedDate;
	
	@Column(name = "CreateUserId")
	private Long createdUserId;
	
	@Column(name = "LastUpdateUser")
	private Long lastUpdatedUserId;
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the lastUpdatedDate
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	/**
	 * @return the createdUserId
	 */
	public Long getCreatedUserId() {
		return createdUserId;
	}
	/**
	 * @param createdUserId the createdUserId to set
	 */
	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}
	/**
	 * @return the lastUpdatedUserId
	 */
	public Long getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	/**
	 * @param lastUpdatedUserId the lastUpdatedUserId to set
	 */
	public void setLastUpdatedUserId(Long lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the contentOwner
	 */
	public ContentOwner getContentOwner() {
		return contentOwner;
	}
	/**
	 * @param contentOwner the contentOwner to set
	 */
	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}
	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}
	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		ContentOwner localCO=getContentOwner();
		result = prime * result
				+ ((localCO == null) ? 0 : localCO.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public  boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorGroup other = (AuthorGroup) obj;
		ContentOwner localCO=getContentOwner();
		if (localCO == null) {
			if (other.getContentOwner() != null)
				return false;
		} else if (!localCO.equals(other.getContentOwner()))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the permissions
	 */
	public List<AuthorPermission> getPermissions() {
		return permissions;
	}
	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(List<AuthorPermission> permissions) {
		this.permissions = permissions;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



}
