package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author haider.ali
 * @dated 11/11/2015
 */
@Entity
@Table(name = "WIDGETFILTER")
public class WidgetFilter implements SearchableKey {
	private static final long serialVersionUID = -5243097047629990669L;
	
	@Id
	@javax.persistence.TableGenerator(name = "WIDGETFILTER_ID", table = "WIDGETFILTER", pkColumnName = "ID", valueColumnName = "ID", pkColumnValue = "ID", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "WIDGETFILTER_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "value")
	private String value;
	@Column(name = "label")
	private String label;

	public WidgetFilter() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getKey() {
		return id.toString();
	}

	@Override
	public String toString() {
		return "WidgetFilter [id=" + id + ", value=" + value + ", label="
				+ label + "]";
	}
	
}
