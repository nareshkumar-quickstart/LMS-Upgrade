/**
 * 
 */
package com.softech.vu360.lms.web.controller.helper;

/**
 * @author marium.saud
 *
 */
public enum InstructorSearchEnum {
	
	INSTRUCTOR{

		@Override
		public String getSortBy(String sortColumnIndex) {
			
			String sortBy="";
			
			if(sortColumnIndex.equalsIgnoreCase("0")){
				sortBy="firstName";
			}
			else if(sortColumnIndex.equalsIgnoreCase("1")){
				sortBy="lastName";
			}
			else if(sortColumnIndex.equalsIgnoreCase("2")){
				sortBy="emailAddress";
			}
			else if(sortColumnIndex.equalsIgnoreCase("3")){
				sortBy="phone";
			}
			
			return sortBy;
		}
		
	};
	
	//The method will receive sortColumnIndex and return respective sortBy value
	public abstract String getSortBy(String sortColumnIndex);

}
