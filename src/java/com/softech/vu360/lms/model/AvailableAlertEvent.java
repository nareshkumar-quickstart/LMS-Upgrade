package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "AVAILABLEALERTEVENT")
public class AvailableAlertEvent implements Serializable{
	
	
	private static final long serialVersionUID = -8486627564470868004L;
	
	@Id
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "AVAILBLEALERTMODE")
	private Boolean availableLearnerMode = Boolean.TRUE;
	
	@Column(name = "DBCOLUMNNAME")
	private String dbColumnName = null;
	
	@Column(name = "DBTABLENAME")
	private String dbTableName = null;
	
	@Column(name = "DBDISPLAYNAME")
	private String dbDisplayName = null;

	public Boolean getAvailableLearnerMode() {
		return availableLearnerMode;
	}

	public void setAvailableLearnerMode(Boolean availableLearnerMode) {
		if(availableLearnerMode==null){
			this.availableLearnerMode=Boolean.FALSE;
		}
		else{
			this.availableLearnerMode = availableLearnerMode;
		}
	}

	public String getDbColumnName() {
		return dbColumnName;
	}

	public void setDbColumnName(String dbColumnName) {
		this.dbColumnName = dbColumnName;
	}

	public String getDbTableName() {
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	public String getDbDisplayName() {
		return dbDisplayName;
	}

	public void setDbDisplayName(String dbDisplayName) {
		this.dbDisplayName = dbDisplayName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
