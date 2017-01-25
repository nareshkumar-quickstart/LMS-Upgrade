package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
//This stored procedure returns a int value and has one input parameter USERNAME.
@NamedStoredProcedureQuery(
 name = "Author.isThisAuthor",
 procedureName = "LCMS_GET_VU360USER_BY_USERNAME",
 parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "USERNAME")
 }
)
@NamedQueries({
	@NamedQuery(name="Author.getAuthorsForUsers",query="SELECT a FROM Author a WHERE a.vu360User.id IN (:lstUserId)"),
	@NamedQuery(name="Author.isAuthor",query="SELECT a FROM Author a WHERE a.vu360User.id =:userId"),
})
@Table(name = "LCMSAUTHOR")
public class Author implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "LCMSAUTHOR_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LCMSAUTHOR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LCMSAUTHOR_ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VU360USER_ID")
	private VU360User vu360User ;
	
	@ManyToMany
    @JoinTable(name="LCMSAUTHOR_LCMSAUTHORGROUP", joinColumns = @JoinColumn(name="LCMSAUTHOR_ID"),inverseJoinColumns = @JoinColumn(name="LCMSAUTHORGROUP_ID"))
	private List<AuthorGroup> groups = new ArrayList<AuthorGroup>();
		
	
	@ManyToMany
    @JoinTable(name="LCMSAUTHOR_CONTENTOWNER", joinColumns = @JoinColumn(name="LCMSAUTHOR_ID"),inverseJoinColumns = @JoinColumn(name="CONTENTOWNER_ID"))
	private List<ContentOwner> contentOwners = new ArrayList<ContentOwner>();
	
	@Column(name = "GUID")
	private String guid = null;

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
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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


	/**
	 * @return the groups
	 */
	public List<AuthorGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<AuthorGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the contentOwners
	 */
	public List<ContentOwner> getContentOwners() {
		return contentOwners;
	}

	/**
	 * @param contentOwners the contentOwners to set
	 */
	public void setContentOwners(List<ContentOwner> contentOwners) {
		this.contentOwners = contentOwners;
	}
	/**
	 * 
	 * @return the vu360User
	 */
	public VU360User getVu360User() {
		return vu360User;
	}
	/**
	 * 
	 * @param vu360User the vu360User to set
	 */
	public void setVu360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}
	


}
