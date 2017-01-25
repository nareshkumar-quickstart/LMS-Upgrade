package com.softech.vu360.lms.web.restful.request.customer.contract.add.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "courseGroups"
})
public class CourseGroupDetail {

	@XmlElement(name = "CourseGroups")
	private CourseGroups courseGroups;

	public CourseGroups getCourseGroups() {
		return courseGroups;
	}

	public void setCourseGroups(CourseGroups courseGroups) {
		this.courseGroups = courseGroups;
	}

	@Override
	public String toString() {
		return "CourseGroupDetail [courseGroups=" + courseGroups + "]";
	}
	
}
