package com.softech.vu360.lms.web.restful.request.customer.contract.add.types;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "guid"
})
public class CourseGroups {

	@XmlElement(name = "Guid", required = true)
	protected List<String> guid;

	public List<String> getGuid() {
		return guid;
	}

	public void setGuid(List<String> guid) {
		this.guid = guid;
	}

	@Override
	public String toString() {
		return "CourseGroups [guid=" + guid + "]";
	}
	
}
