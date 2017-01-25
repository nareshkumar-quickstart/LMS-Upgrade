/**
 * 
 */
package com.softech.vu360.lms.model;

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
 * @author raja.ali
 *
 */
@Entity
@Table(name = "SURVEYFLAGNOTIFICATION")
public class SurveyFlagNotification implements SearchableKey {

	private static final long serialVersionUID = 6938545992447154011L;

	@Id
	@javax.persistence.TableGenerator(name = "SURVEYFLAGNOTIFICATION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SURVEYFLAGNOTIFICATION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SURVEYFLAGNOTIFICATION_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "EMAILADDRESS")
	private String emailaddress;
	
	@OneToOne
    @JoinColumn(name="SURVEYFLAGTEMPLATE_ID")
	private SurveyFlagTemplate flagTemplate = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	public String getKey() {
		return id.toString();
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

	/**
	 * @return the emailaddress
	 */
	public String getEmailaddress() {
		return emailaddress;
	}

	/**
	 * @param emailaddress
	 *            the emailaddress to set
	 */
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	/**
	 * @return the flagTemplate
	 */
	public SurveyFlagTemplate getFlagTemplate() {
		return flagTemplate;
	}

	/**
	 * @param flagTemplate
	 *            the flagTemplate to set
	 */
	public void setFlagTemplate(SurveyFlagTemplate flagTemplate) {
		this.flagTemplate = flagTemplate;
	}

}
