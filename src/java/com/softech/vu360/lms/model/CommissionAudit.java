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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COMMISSION_AUDIT")
public class CommissionAudit implements SearchableKey {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "COMMISSION_AUDIT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COMMISSION_AUDIT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COMMISSION_AUDIT_ID")
	private Long id;
	
	@Column(name = "COMMENTS")
    private String comments;
	
	@Column(name = "CHANGEDON")
    private Date changedOn;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VU360USER_ID")
	private VU360User vu360User ;
	
	@Column(name = "OLDVALUE")
    private String oldValue;
	
	@Column(name = "NEWVALUE")
    private String newValue;
	
	@OneToOne
    @JoinColumn(name="COMMISSION_ID")
    private Commission commission ;

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKey() {
        return String.valueOf(id);
    }
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getChangedOn() {
		return changedOn;
	}
	public void setChangedOn(Date changedOn) {
		this.changedOn = changedOn;
	}
	public VU360User getVu360User() {
		return this.vu360User;
	}
	public void setVu360User(VU360User vu360User) {
		this.vu360User = vu360User;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public Commission getCommission() {
		return this.commission;
	}
	
	public void setCommission(Commission commission) {
		this.commission = commission;
	}
        
    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
