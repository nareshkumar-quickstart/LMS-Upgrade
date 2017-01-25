package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author haider.ali
 * @dated 11/11/2015
 */
@Entity
@Table(name = "WIDGET")
public class Widget implements SearchableKey {

	private static final long serialVersionUID = -4243097047629990669L;

	@Id
	private Long id;

	@Column(name = "EXPRESSION")
	private String expression;
	
	@Column(name = "WIDGETTYPE")
	private String type;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "TITLE")
	private String title;

	@OneToMany()
	@JoinColumn(name="widgetId")
	private List<WidgetFilter> filters = new ArrayList<WidgetFilter>();

	public Widget() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public String getExpression() {
		return this.expression;
	}

	public Long getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<WidgetFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<WidgetFilter> filters) {
		this.filters = filters;
	}

	@Override
	public String getKey() {
		return id.toString();
	}

	@Override
	public String toString() {
		return "Widget [id=" + id + ", expression=" + expression + ", type="
				+ type + ", description=" + description + ", title=" + title
				+ ", filters=" + filters + "]";
	}

}
