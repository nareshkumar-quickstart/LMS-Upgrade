package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import com.softech.vu360.util.ObjectToStringConverter;
import com.softech.vu360.util.SecurityUtil;

/**
 * 
 * @author muhammad.saleem
 * modified by marium.saud
 *
 */
@Entity
@NamedStoredProcedureQuery(
		 name = "CreditReportingFieldValue.storeEncryptedValue",
		 procedureName = "LMS_CREDIT_REPORTING_VALUE_ENCRYPTION",
		 parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, type = Long.class, name = "PARAM_CREDITREPORTINGVALUE_ID"),
				@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "PARAM_CREDITREPORTINGVALUE")
		 }
		)
@Table(name = "CREDITREPORTINGFIELDVALUE")
public class CreditReportingFieldValue implements SearchableKey {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "CREDITREPORTINGFIELDVALUE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CREDITREPORTINGFIELDVALUE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "CREDITREPORTINGFIELDVALUE_ID")
	private Long id;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="CUSTOMFIELDVALUE_ID")
	private CreditReportingField reportingCustomField = null;
	
	@Column (name = "VALUE")
	@Convert(converter=ObjectToStringConverter.class)
	private Object value = null;
	
	@Column (name = "VALUE2")
	@Convert(converter=ObjectToStringConverter.class)
	private Object value2 = null;
	
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="LEARNERPROFILE_ID")
	private LearnerProfile learnerprofile = null;
	
	@ManyToMany
	@JoinTable(name="CREDITREPORTINGFIELDVALUE_CREDITREPORTINGFIELDVALUECHOICE", joinColumns = @JoinColumn(name="CREDITREPORTINGFIELDVALUE_ID"),inverseJoinColumns = @JoinColumn(name="CREDITREPORTINGFIELDVALUECHOICE_ID"))
	private List<CreditReportingFieldValueChoice> creditReportingValueChoices = new ArrayList<CreditReportingFieldValueChoice>();
	

	@Override
	public String getKey() {
		// Auto-generated method stub
		return null;
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
	 * @return the reportingCustomField
	 */
	public CreditReportingField getReportingCustomField() {
		return reportingCustomField;
	}

	/**
	 * @param reportingCustomField the reportingCustomField to set
	 */
	public void setReportingCustomField(CreditReportingField reportingCustomField) {
		this.reportingCustomField = reportingCustomField;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		if( reportingCustomField != null ) {
			if( reportingCustomField.isFieldEncrypted() ) {
				try {
					SecurityUtil su = SecurityUtil.getInstance();
					return su.decrypt(value!=null ? value.toString() : "");
				} catch ( Exception e ) {
					e.printStackTrace();
				}	
			}
		}
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		if( reportingCustomField != null ) {
			if( reportingCustomField.isFieldEncrypted() ) {
				try {
					SecurityUtil su = SecurityUtil.getInstance();
					value = su.encryptData(value.toString().getBytes());
				} catch ( Exception e ) {
					e.printStackTrace();
				}	
			}
		}
		this.value = value;
	}

	public Object getValue2() {
		return value2;
	}

	public void setValue2(Object value2) {
		this.value2 = value2;
	}

	/**
	 * @return the learner profile
	 */
	public LearnerProfile getLearnerprofile() {
		return learnerprofile;
	}

	/**
	 * @param learnerprofile the learner profile to set
	 */
	public void setLearnerprofile(LearnerProfile learnerprofile) {
		this.learnerprofile = learnerprofile;
	}

	/**
	 * @return the creditReportingValueChoices
	 */
	public List<CreditReportingFieldValueChoice> getCreditReportingValueChoices() {
		return creditReportingValueChoices;
	}

	/**
	 * @param creditReportingValueChoices the creditReportingValueChoices to set
	 */
	public void setCreditReportingValueChoices(
			List<CreditReportingFieldValueChoice> creditReportingValueChoices) {
		this.creditReportingValueChoices = creditReportingValueChoices;
	}

}