package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "CERTIFICATEBOOKMARKASSOCIATION")
public class CertificateBookmarkAssociation  {

	@Id
    @javax.persistence.TableGenerator(name = "CERTIFICATEBOOKMARKASSOCIATION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CERTIFICATEBOOKMARKASSOCIATION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CERTIFICATEBOOKMARKASSOCIATION_ID")
	private Long id;
	
	@Column(name = "BOOKMARK_LABEL")
	private String bookmarkLabel=null;
	
	@OneToOne
    @JoinColumn(name="REPORTINGFIELD_ID")
	private CreditReportingField  reportingField;
	
	@OneToOne
    @JoinColumn(name="CUSTOMFIELD_ID")
	private CustomField  customField;
	
	@Column(name = "LMSFIELD_NAME")
	private String  LMSFieldName=null;
			
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	/**
	 * @return the reportingField
	 */
	public CreditReportingField getReportingField() {
		return reportingField;
	}
	/**
	 * @param reportingField the reportingField to set
	 */
	public void setReportingField(CreditReportingField reportingField) {
		this.reportingField = reportingField;
	}
	/**
	 * @return the customField
	 */
	public CustomField getCustomField() {
		return customField;
	}
	/**
	 * @param customField the customField to set
	 */
	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}
	public String getBookmarkLabel() {
		return bookmarkLabel;
	}
	public void setBookmarkLabel(String bookmarkLabel) {
		this.bookmarkLabel = bookmarkLabel;
	}
	public String getLMSFieldName() {
		return LMSFieldName;
	}
	public void setLMSFieldName(String lMSFieldName) {
		LMSFieldName = lMSFieldName;
	}
	
	/**
	 * utility method to ease extraction of field name,since we are only persisting foreign keys in database.
	 * @return
	 */
	public String getValue(){		
		if(this.LMSFieldName !=null && this.LMSFieldName.length()>0){
			 return this.LMSFieldName;
		}else if(this.getReportingField()!=null){
			return this.getReportingField().getFieldLabel();			
		}else if(this.getCustomField()!=null){
			return this.getCustomField().getFieldLabel();
		} 				
		return null;
	}
	
	public String getValueSelectionCode(){		
		if(this.LMSFieldName !=null && this.LMSFieldName.length()>0){
			 return "L:".concat(this.LMSFieldName);
		}else if(this.getReportingField()!=null){
			return "R:".concat(this.getReportingField().getId().toString());			
		}else if(this.getCustomField()!=null){
			return "C:".concat(this.getCustomField().getId().toString());
		} 				
		return null;
	}
}
