package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ManageCategory  implements ILMSBaseInterface{
	private long id;
	private String name;
	private String categoryType;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	
	private static final long serialVersionUID = 1L;
}
