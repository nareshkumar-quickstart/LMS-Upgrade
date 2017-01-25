package com.softech.vu360.lms.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WidgetData implements Serializable {

	private static final long serialVersionUID = 1949228889944055617L;

	protected Long id;
	protected Map<String, Object> dataMap = new HashMap<String, Object>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
}
