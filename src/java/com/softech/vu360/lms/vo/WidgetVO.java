package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.List;

import com.softech.vu360.lms.model.WidgetFilter;

public class WidgetVO implements Serializable {

	private static final long serialVersionUID = -6542321523465393938L;

	private String expression;
	private String type;
	private String description;
	private String title;
	private Long id;
	private Long zoneId;
	private List<WidgetFilter> filters; //LMS-14316

	public WidgetVO() {
	}
	
	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<WidgetFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<WidgetFilter> filters) {
		this.filters = filters;
	}
	
	
}
