/**
 * 
 */
package com.softech.vu360.lms.model;

/**
 * @author Ashis
 *
 */
public class CustomReportingFieldValue implements SearchableKey {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private CustomReportingField reportingCustomField = null;
	private Object value = null;
	private Learner learner = null;
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
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
	public CustomReportingField getReportingCustomField() {
		return reportingCustomField;
	}

	/**
	 * @param reportingCustomField the reportingCustomField to set
	 */
	public void setReportingCustomField(CustomReportingField reportingCustomField) {
		this.reportingCustomField = reportingCustomField;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the learner
	 */
	public Learner getLearner() {
		return learner;
	}

	/**
	 * @param learner the learner to set
	 */
	public void setLearner(Learner learner) {
		this.learner = learner;
	}

}
