package com.softech.vu360.lms.web.controller.model.instructor;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class AddActivityForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String name = "";
	private String type = "";
	private String desc = "";
	private String gradebookId = "";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getGradebookId() {
		return gradebookId;
	}
	public void setGradebookId(String gradebookId) {
		this.gradebookId = gradebookId;
	}
	
}