/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "AVAILABLEPERSONALINFORMATIONFIELDITEM")
public class AvailablePersonalInformationfieldItem implements SearchableKey {
	
	@Id
    @javax.persistence.TableGenerator(name = "AVAILABLEPERSONALINFORMATIONFIELDITEM_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AVAILABLEPERSONALINFORMATIONFIELDITEM", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AVAILABLEPERSONALINFORMATIONFIELDITEM_ID")
	private Long id;
	
	@Column(name = "COLUMNNAME")
	private String dbColumnName = null;
	
	@Column(name = "TABLENAME")
	private String dbTableName = null;
	
	@Column(name = "DISPLAYNAME")
	private String displayName = null;
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
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
	 * @return the dbColumnName
	 */
	public String getDbColumnName() {
		return dbColumnName;
	}
	/**
	 * @param dbColumnName the dbColumnName to set
	 */
	public void setDbColumnName(String dbColumnName) {
		this.dbColumnName = dbColumnName;
	}
	/**
	 * @return the dbTableName
	 */
	public String getDbTableName() {
		return dbTableName;
	}
	/**
	 * @param dbTableName the dbTableName to set
	 */
	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
