/**
 * 
 */
package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.AdminLearnerEnrollmentSearch;

/**
 * @author tapas
 *
 */
public class AdminLearnerEnrollmentSort  implements Comparator<AdminLearnerEnrollmentSearch>{
	
	String sortBy="firstName";
	int sortDirection =0;
		public int getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}
		public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
		
		public int compare(AdminLearnerEnrollmentSearch arg0, AdminLearnerEnrollmentSearch arg1) {
			
			if(sortBy.equalsIgnoreCase("firstName"))
			{
				if (arg0.getFirstName() == null)
					arg0.setFirstName("");
				if (arg1.getFirstName() == null)
					arg1.setFirstName("");
			//	if(arg0.getVu360User().getFirstName().compareTo(arg1.getVu360User().getFirstName())!=0)
				if(sortDirection==0)
					return arg0.getFirstName().trim().toUpperCase().compareTo(arg1.getFirstName().trim().toUpperCase());
				else
					return arg1.getFirstName().trim().toUpperCase().compareTo(arg0.getFirstName().trim().toUpperCase());
			}
			else 
				if(sortBy.equalsIgnoreCase("lastName"))
				{
					if (arg0.getLastName() == null)
						arg0.setLastName("");
					if (arg1.getLastName() == null)
						arg1.setLastName("");
				//	if(arg0.getVu360User().getFirstName().compareTo(arg1.getVu360User().getFirstName())!=0)
					if(sortDirection==0)
						return arg0.getLastName().trim().toUpperCase().compareTo(arg1.getLastName().trim().toUpperCase());
					else
						return arg1.getLastName().trim().toUpperCase().compareTo(arg0.getLastName().trim().toUpperCase());
				}
				else
			if(sortBy.equalsIgnoreCase("email"))
			{
				if (arg0.getEMail() == null)
					arg0.setEMail("");
				if (arg1.getEMail() == null)
					arg1.setEMail("");
				if(sortDirection==0)
					return arg0.getEMail().trim().toUpperCase().compareTo(arg1.getEMail().trim().toUpperCase());
				else
					return arg1.getEMail().trim().toUpperCase().compareTo(arg0.getEMail().trim().toUpperCase());
			}				
				return 0;
					
		}
 
		

}
