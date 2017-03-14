package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "AFFIDAVITTEMPLATE")
public class AffidavitTemplate implements SearchableKey {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "AFFIDAVITTEMPLATE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "AFFIDAVITTEMPLATE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AFFIDAVITTEMPLATE_ID")
	private Long id;
	
	@Column(name = "TEMPLATE_GUID")
	private String templateGUID;
	
	@Column(name = "TEMPLATE_NAME")
	private String templateName;
	
	@Column(name = "STATUSTF")
	private Boolean status;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CREATEDVU360USER_ID")
	private VU360UserNew createdVU360User = null;
	
	@Column(name = "CREATEDATE")
	private Date createdDate = new Date();


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTemplateGUID() {
		return templateGUID;
	}


	public void setTemplateGUID(String templateGUID) {
		this.templateGUID = templateGUID;
	}


	public  Boolean isStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public VU360UserNew getCreatedVU360User() {
		return createdVU360User;
	}


	public void setCreatedVU360User(VU360UserNew createdVU360User) {
		this.createdVU360User = createdVU360User;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	@Override
	public String getKey() {
		return null;
	}


	public String getTemplateName() {
		return templateName;
	}


	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}




	
}
