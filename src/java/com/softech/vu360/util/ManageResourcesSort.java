package com.softech.vu360.util;

import java.util.Comparator;

import com.softech.vu360.lms.model.Resource;

public class ManageResourcesSort implements Comparator<Resource>{

	String sortBy = "resourceName";
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

	public int compare(Resource arg0, Resource arg1) {

		if( sortBy.equalsIgnoreCase("resourceName") ) {
			if ( arg0.getName() == null )
				arg0.setName("");
			if ( arg1.getName() == null )
				arg1.setName("");
			if( sortDirection == 0 )
				return arg0.getName().trim().toUpperCase().compareTo(arg1.getName().trim().toUpperCase());
			else
				return arg1.getName().trim().toUpperCase().compareTo(arg0.getName().trim().toUpperCase());

		} else if( sortBy.equalsIgnoreCase("resourceType") ) {
			
			if ( arg0.getResourceType().getName() == null )
				arg0.getResourceType().setName("");
			if ( arg1.getResourceType().getName() == null )
				arg1.getResourceType().setName("");
			
			
			if( sortDirection == 0 )
				return arg0.getResourceType().getName().toUpperCase().compareTo(arg1.getResourceType().getName().toUpperCase());
			else
				return arg1.getResourceType().getName().toUpperCase().compareTo(arg0.getResourceType().getName().toUpperCase());
		}
		else if( sortBy.equalsIgnoreCase("tagNumber") ) {
			
			
			if ( arg0.getAssetTagNumber() == null )
				arg0.setAssetTagNumber("");
			if ( arg1.getAssetTagNumber() == null )
				arg1.setAssetTagNumber("");
			
			
			if( sortDirection == 0 )
				return arg0.getAssetTagNumber().toUpperCase().compareTo(arg1.getAssetTagNumber().toUpperCase());
			else
				return arg1.getAssetTagNumber().toUpperCase().compareTo(arg0.getAssetTagNumber().toUpperCase());
		}

//		} else if( sortBy.equalsIgnoreCase("resourceType") ) {
//			if ( arg0.getResourceType() == null )
//				arg0.se("");
//			if ( arg1.getResourceType() == null )
//				arg1.setResourceType("");
//			if( sortDirection == 0 )
//				return arg0.getResourceType().trim().toUpperCase().compareTo(arg1.getResourceType().trim().toUpperCase());
//			else
//				return arg1.getResourceType().trim().toUpperCase().compareTo(arg0.getResourceType().trim().toUpperCase());
//
//		} 
		return 0;
	}
}