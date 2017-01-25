package com.softech.vu360.lms.model;

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

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "ALERTRECIPIENT")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE")
public class AlertRecipient implements SearchableKey{

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "ALERTRECIPIENT_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ALERTRECIPIENT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ALERTRECIPIENT_ID")
	private Long id;
	
	@Column(name = "ALERTRECIPIENTGROUPNAME")
	private String alertRecipientGroupName="";
	
	@OneToOne
    @JoinColumn(name="ALERT_ID")
	private Alert alert ;
	
	@Column(name = "ISDELETE")
	private Boolean isDelete=Boolean.FALSE;
	
	

	/**
	 * @return the alert
	 */
	public Alert getAlert() {
		return alert;
	}

	/**
	 * @param alert the alert to set
	 */
	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public void setAlertRecipientGroupName(String alertRecipientGroupName) {
		this.alertRecipientGroupName = alertRecipientGroupName;
	}

	public String getAlertRecipientGroupName() {
		return alertRecipientGroupName;
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

	public enum Type {
		LEARNER("LearnerAlertRecipient"), LEARNERGROUP("LearnerGroupAlertRecipient"),
		ORGGROUP("OrgGroupAlertRecipient"), EMAILADDRESS("EmailAddressAlertRecipient"),
		DEFAULT("AlertRecipient");
		
		private String value;
		
		private Type(String value) {
			this.value = value;
		}
		
		public String getValue() {
	        return this.value;
	    }
	}
}
