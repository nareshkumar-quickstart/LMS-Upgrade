/**
 * 
 */
package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.AdminSearchMember;

/**
 * @author tapas
 *
 */
public class AdminSort  implements Comparator<AdminSearchMember>{
	
	String sortBy="name";
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
		
		public int compare(AdminSearchMember arg0, AdminSearchMember arg1) {
			
			if(sortBy.equalsIgnoreCase("name")){
				
			//	if(arg0.getVu360User().getFirstName().compareTo(arg1.getVu360User().getFirstName())!=0)
				if(sortDirection==0)
					return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
				else
					return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());
			}else if(sortBy.equalsIgnoreCase("email")){
				if (arg0.getEMail() == null)
					arg0.setEMail("");
				if (arg1.getEMail() == null)
					arg1.setEMail("");
				if(sortDirection==0)
					return arg0.getEMail().trim().toUpperCase().compareTo(arg1.getEMail().trim().toUpperCase());
				else
					return arg1.getEMail().trim().toUpperCase().compareTo(arg0.getEMail().trim().toUpperCase());
			}else if(sortBy.equalsIgnoreCase("status")){
				
				if(sortDirection==0)
					return new Boolean(arg1.isStatus()).compareTo(new Boolean(arg0.isStatus()));
				else
					return new Boolean(arg0.isStatus()).compareTo(new Boolean(arg1.isStatus()));
				
			}else if(sortBy.equalsIgnoreCase("currentSearchType")){
				
				if(sortDirection==0)
					return arg0.getCurrentSearchType().compareTo(arg1.getCurrentSearchType());
				else
					return arg1.getCurrentSearchType().compareTo(arg0.getCurrentSearchType());
				
				
			} else if(sortBy.equalsIgnoreCase("originalContractCreationDate")){
				
					if(sortDirection==0) {
						
						if(arg0.originalContractCreationDatetime !=null && !arg0.originalContractCreationDatetime.equals("")) 
						return arg0.originalContractCreationDatetime.compareTo(arg1.originalContractCreationDatetime);
					} else {
						if(arg1.originalContractCreationDatetime !=null && !arg1.originalContractCreationDatetime.equals(""))
						return arg1.originalContractCreationDatetime.compareTo(arg0.originalContractCreationDatetime);
					}
				
				
			} else if(sortBy.equalsIgnoreCase("recentContractCreationDate")){
				
					if(sortDirection==0) {
						if(arg0.recentContractCreationDatetime != null && !arg0.recentContractCreationDatetime.equals("")) 
						return arg0.recentContractCreationDatetime.compareTo(arg1.recentContractCreationDatetime);
					} else {
						if(arg1.recentContractCreationDatetime != null && !arg1.recentContractCreationDatetime.equals("")) 
						return arg1.recentContractCreationDatetime.compareTo(arg0.recentContractCreationDatetime);
					}
				
			}
				
				
				return 0;
					
		}

}
