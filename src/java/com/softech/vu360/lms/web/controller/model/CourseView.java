package com.softech.vu360.lms.web.controller.model;

import java.math.BigInteger;
import java.util.Map;

import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


public class CourseView  implements ILMSBaseInterface{
	String code;
	String name;
	String type;
	String groupName;
	Long entitlementId;
	Long enrollmentId;
	Integer courseId;
	String enrollmentStatus;
	String courseIconToShow;
	String courseStatus;
	double courseHours;
	double duration;

	private static final long serialVersionUID = 1L;


	public CourseView() {
		// TODO Auto-generated constructor stub
	}

	public CourseView(Map map) {
		groupName = (String) map.get("COURSEGROUPNAME");
		name = (String) map.get("COURSENAME");
		code = (String) map.get("COURSECODE");
		type = (String) map.get("COURSETYPE");
		enrollmentId = map.get("ENROLLMENT_ID")==null?null:Long.parseLong(map.get("ENROLLMENT_ID").toString());
		if(map.get("CUSTOMERENTITLEMENT_ID") instanceof BigInteger){
			BigInteger entitlementIdBigInt = (BigInteger) map.get("CUSTOMERENTITLEMENT_ID");
			entitlementId = entitlementIdBigInt==null?null : entitlementIdBigInt.longValue();
		}
		else{
			entitlementId = (Long) map.get("CUSTOMERENTITLEMENT_ID");
		}
		
		Long courseIdL = Long.parseLong( map.get("COURSE_ID").toString() );
		courseId=(Integer) courseIdL.intValue();
		enrollmentStatus=(String)map.get("EnrollmentStatus");
		enrollmentStatus=(enrollmentStatus==null?LearnerCourseStatistics.NOT_STARTED:enrollmentStatus);
		courseStatus=(String) map.get("COURSE_STATUS");
		if(map.get("APPROVEDCOURSEHOURS")!=null)
		{
			courseHours = Double.valueOf(map.get("APPROVEDCOURSEHOURS").toString());
		}
		
		if(map.get("CEUS") != null)
		{
			duration = Double.valueOf( map.get("CEUS").toString());
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(Long entitlementId) {
		this.entitlementId = entitlementId;
	}

	public Long getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(Long enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result
				+ ((enrollmentId == null) ? 0 : enrollmentId.hashCode());
		result = prime
				* result
				+ ((enrollmentStatus == null) ? 0 : enrollmentStatus.hashCode());
		result = prime * result
				+ ((entitlementId == null) ? 0 : entitlementId.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseView other = (CourseView) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		} else if (!courseId.equals(other.courseId))
			return false;
		if (enrollmentId == null) {
			if (other.enrollmentId != null)
				return false;
		} else if (!enrollmentId.equals(other.enrollmentId))
			return false;
		if (enrollmentStatus == null) {
			if (other.enrollmentStatus != null)
				return false;
		} else if (!enrollmentStatus.equals(other.enrollmentStatus))
			return false;
		if (entitlementId == null) {
			if (other.entitlementId != null)
				return false;
		} else if (!entitlementId.equals(other.entitlementId))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public String getCourseIconToShow() {
		return courseIconToShow;
	}

	public void setCourseIconToShow(String courseIconToShow) {
		this.courseIconToShow = courseIconToShow;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public double getCourseHours(){
		return courseHours;
	}
	public void setCourseHours(double courseHours) {
		this.courseHours = courseHours;
	}
	
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append(name);builder.append(",");
		builder.append(code);builder.append(",");
		builder.append(type);builder.append(",");
		builder.append(code);builder.append("}");
		return builder.toString();
	}
}
