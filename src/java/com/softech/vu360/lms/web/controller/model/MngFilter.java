package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.AlertTriggerFilter;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class MngFilter  implements ILMSBaseInterface{
	private long id = 0;
	private String filterName ="";
	private String filterType ="";
	private static final long serialVersionUID = 1L;
	private AlertTriggerFilter filter=new AlertTriggerFilter();
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	public String getFilterType() {
		return filterType;
	}
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	public AlertTriggerFilter getFilter() {
		return filter;
	}
	public void setFilter(AlertTriggerFilter filter) {
		this.filter = filter;
	}
	
	
}
