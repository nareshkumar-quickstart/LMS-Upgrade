package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "ALERTTRIGGERFILTER")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE")
public class AlertTriggerFilter  implements Serializable, SearchableKey{
	
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "ALERTTRIGGERFILTER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ALERTTRIGGERFILTER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ALERTTRIGGERFILTER_ID")
	private Long id;
	
	@Column(name = "FILTERNAME")
	private String filterName;
	
	@OneToOne
    @JoinColumn(name="ALERTTRIGGER_ID")
	private AlertTrigger alerttrigger;
	
	
	@Column(name = "ISDELETE")
	private Boolean isDelete = Boolean.FALSE;

	@Transient
	private String type;
	
	public AlertTrigger getAlertTrigger() {
		return this.alerttrigger;
	}

	public void setAlertTrigger(AlertTrigger alerttrigger) {
		this.alerttrigger = alerttrigger;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return id.toString();
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public  Boolean isDelete() {
		return isDelete;
	}

	public void setDelete(Boolean isDelete) {
		if(isDelete==null){
			this.isDelete=Boolean.FALSE;
		}
		else{
			this.isDelete = isDelete;
		}
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
