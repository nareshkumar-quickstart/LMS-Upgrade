package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
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
 * @author muhammad.saleem
 * modified by marium.saud
 *
 */
@Entity
@Table(name = "SINGLESELECTCUSTOMFIELDOPTION")
public class CustomFieldValueChoice implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
	@javax.persistence.TableGenerator(name = "SINGLESELECTCUSTOMFIELDOPTION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SINGLESELECTCUSTOMFIELDOPTION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SINGLESELECTCUSTOMFIELDOPTION_ID")
	private Long id;
	
	@Column(name="DISPLAYORDER")
	private Integer displayOrder = 0;
	
	@Column(name="LABEL")
	private String label = null;
	
	@Column(name="VALUE")
	private String value = null;
	
	@OneToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="CUSTOMFIELD_ID")
	private CustomField customField = null;
	
	public String getKey() {
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField1) {
		customField = customField1;
	}

}
