package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Dyutiman
 */
public class SearchMemberItem  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String name = "";
	private String type = "";
	private String id = "";
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}