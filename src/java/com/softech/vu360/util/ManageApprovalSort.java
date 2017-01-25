package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.accreditation.ManageApproval;

public class ManageApprovalSort implements Comparator<ManageApproval>{

	String sortBy = "approvalName";
	int sortDirection = 0;

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

	public int compare(ManageApproval arg0, ManageApproval arg1) {

		if( sortBy.equalsIgnoreCase("approvalName") ) {
			if ( arg0.getApprovalName() == null )
				arg0.setApprovalName("");
			if ( arg1.getApprovalName() == null )
				arg1.setApprovalName("");
			if( sortDirection == 0 )
				return arg0.getApprovalName().trim().toUpperCase().compareTo(arg1.getApprovalName().trim().toUpperCase());
			else
				return arg1.getApprovalName().trim().toUpperCase().compareTo(arg0.getApprovalName().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("approvalNumber") ) {
			Long appNumber_0 = 0l;
			Long appNumber_1 = 0l;
			
			if ( arg0.getApprovalNumber() == null )
				arg0.setApprovalNumber("");
			if ( arg1.getApprovalNumber() == null )
				arg1.setApprovalNumber("");
						
			/*appNumber_0 = Long.parseLong(arg0.getApprovalNumber());
			//appNumber_1 = Long.parseLong(arg1.getApprovalNumber());
			if( sortDirection == 0 )
				return appNumber_0.compareTo(appNumber_1);
			else
				return appNumber_1.compareTo(appNumber_0);*/
			if( sortDirection == 0 )
				return arg0.getApprovalNumber().trim().toUpperCase().compareTo(arg1.getApprovalNumber().trim().toUpperCase());
			else
				return arg1.getApprovalNumber().trim().toUpperCase().compareTo(arg0.getApprovalNumber().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("approvalType") ) {
			if ( arg0.getApprovalType() == null )
				arg0.setApprovalType("");
			if ( arg1.getApprovalType() == null )
				arg1.setApprovalType("");
			if( sortDirection == 0 )
				return arg0.getApprovalType().trim().toUpperCase().compareTo(arg1.getApprovalType().trim().toUpperCase());
			else
				return arg1.getApprovalType().trim().toUpperCase().compareTo(arg0.getApprovalType().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("regulatorName") ) {
			if ( arg0.getRegulatorName() != null && arg1.getRegulatorName() != null ) {
				if( sortDirection == 0 )
					return arg0.getRegulatorName().trim().toUpperCase().compareTo(arg1.getRegulatorName().trim().toUpperCase());
				else
					return arg1.getRegulatorName().trim().toUpperCase().compareTo(arg0.getRegulatorName().trim().toUpperCase());
			}
		} else if( sortBy.equalsIgnoreCase("active") ) {
			if ( arg0.getActive() != null && arg1.getActive() != null ) {
				if( sortDirection == 0 )
					return arg0.getActive().trim().toUpperCase().compareTo(arg1.getActive().trim().toUpperCase());
				else
					return arg1.getActive().trim().toUpperCase().compareTo(arg0.getActive().trim().toUpperCase());
			}
		}
		return 0;
	}
}