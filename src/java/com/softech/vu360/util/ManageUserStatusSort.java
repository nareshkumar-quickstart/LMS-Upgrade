package com.softech.vu360.util;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.ManageUserStatus;

public class ManageUserStatusSort implements Comparator<ManageUserStatus>{

	String sortBy="firstName";
	int sortDirection = 0;	
	
	@Override
	public int compare(ManageUserStatus o1, ManageUserStatus o2) {
		
		if(sortBy.equalsIgnoreCase("firstName")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getFirstName()).toUpperCase().compareTo(o2.getFirstName().toUpperCase());
			return (StringUtils.EMPTY + o2.getFirstName()).toUpperCase().compareTo(o1.getFirstName().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("lastName")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getLastName()).toUpperCase().compareTo(o2.getLastName().toUpperCase());
			return (StringUtils.EMPTY + o2.getLastName()).toUpperCase().compareTo(o1.getLastName().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("emailAddress1")){
			
			if(sortDirection==0)
				return o1.getEmailAddress().toUpperCase().compareTo(o2.getEmailAddress().toUpperCase());
			return o2.getEmailAddress().toUpperCase().compareTo(o1.getEmailAddress().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("phoneNumber")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getPhoneNumber()).toUpperCase().compareTo(o2.getPhoneNumber().toUpperCase());
			return (StringUtils.EMPTY + o2.getPhoneNumber()).toUpperCase().compareTo(o1.getPhoneNumber().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("courseName")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getCourseName()).toUpperCase().compareTo(o2.getCourseName().toUpperCase());
			return (StringUtils.EMPTY + o2.getCourseName()).toUpperCase().compareTo(o1.getCourseName().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("courseId")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getCourseId()).toUpperCase().compareTo(o2.getCourseId().toUpperCase());
			return (StringUtils.EMPTY + o2.getCourseId()).toUpperCase().compareTo(o1.getCourseId().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("courseStatus")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getCourseStatus()).toUpperCase().compareTo(o2.getCourseStatus().toUpperCase());
			return (StringUtils.EMPTY + o2.getCourseStatus()).toUpperCase().compareTo(o1.getCourseStatus().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("courseType")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getCourseType()).toUpperCase().compareTo(o2.getCourseType().toUpperCase());
			return o2.getCourseType().toUpperCase().compareTo(o1.getCourseType().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("completeDate")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getCompleteDate()).toUpperCase().compareTo(o2.getCompleteDate().toUpperCase());
			return o2.getCompleteDate().toUpperCase().compareTo(o1.getCompleteDate().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("enrollmentDate")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getEnrollmentDate()).toUpperCase().compareTo(o2.getEnrollmentDate().toUpperCase());
			return (StringUtils.EMPTY + o2.getEnrollmentDate()).toUpperCase().compareTo(o1.getEnrollmentDate().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("firstAccessDate")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getFirstAccessDate()).toUpperCase().compareTo(o2.getFirstAccessDate().toUpperCase());
			return (StringUtils.EMPTY + o2.getFirstAccessDate()).toUpperCase().compareTo(o1.getFirstAccessDate().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("holdingRegulatorName")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getHoldingRegulatorName()).toUpperCase().compareTo(o2.getHoldingRegulatorName().toUpperCase());
			return (StringUtils.EMPTY + o2.getHoldingRegulatorName()).toUpperCase().compareTo(o1.getHoldingRegulatorName().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("regulatoryCategory")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getRegulatoryCategory()).toUpperCase().compareTo(o2.getRegulatoryCategory().toUpperCase());
			return (StringUtils.EMPTY + o2.getRegulatoryCategory().toUpperCase()).compareTo(o1.getRegulatoryCategory().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("affidavitType")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getAffidavitType()).toUpperCase().compareTo(o2.getAffidavitType().toUpperCase());
			return (StringUtils.EMPTY + o2.getAffidavitType()).toUpperCase().compareTo(o1.getAffidavitType().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("lastUserStatusChange")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getLastUserStatusChange()).toUpperCase().compareTo(o2.getLastUserStatusChange().toUpperCase());
			return  (StringUtils.EMPTY + o2.getLastUserStatusChange().toUpperCase()).compareTo(o1.getLastUserStatusChange().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("lastUserStatusChangeDate")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getLastUserStatusChangeDate()).toUpperCase().compareTo(o2.getLastUserStatusChangeDate().toUpperCase());
			return (StringUtils.EMPTY + o2.getLastUserStatusChangeDate()).toUpperCase().compareTo(o1.getLastUserStatusChangeDate().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("lastUserAffidavitUpload")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getLastUserAffidavitUpload()).toUpperCase().compareTo(o2.getLastUserAffidavitUpload().toUpperCase());
			return (StringUtils.EMPTY + o2.getLastUserAffidavitUpload()).toUpperCase().compareTo(o1.getLastUserAffidavitUpload().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("lastUserAffidavitUploadDate")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getLastUserAffidavitUploadDate()).toUpperCase().compareTo(o2.getLastUserAffidavitUploadDate().toUpperCase());
			return (StringUtils.EMPTY + o2.getLastUserAffidavitUploadDate()).toUpperCase().compareTo(o1.getLastUserAffidavitUploadDate().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("address1")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getAddress1()).toUpperCase().compareTo(o2.getAddress1().toUpperCase());
			return (StringUtils.EMPTY + o2.getAddress1()).toUpperCase().compareTo(o1.getAddress1().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("city1")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getCity()).toUpperCase().compareTo(o2.getCity().toUpperCase());
			return (StringUtils.EMPTY + o2.getCity()).toUpperCase().compareTo(o1.getCity().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("state")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getState()).toUpperCase().compareTo(o2.getState().toUpperCase());
			return (StringUtils.EMPTY + o2.getState()).toUpperCase().compareTo(o1.getState().toUpperCase());
			
		}
		else if(sortBy.equalsIgnoreCase("zipCode")){
			
			if(sortDirection==0)
				return (StringUtils.EMPTY + o1.getZipCode()).toUpperCase().compareTo(o2.getZipCode().toUpperCase());
			return (StringUtils.EMPTY + o2.getZipCode()).toUpperCase().compareTo(o1.getZipCode().toUpperCase());
			
		}

		
		
		return 0;
			
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public int getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	
}
