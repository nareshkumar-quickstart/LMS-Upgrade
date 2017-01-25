package com.softech.vu360.lms.webservice.validation.lmsapi;

import static com.softech.vu360.lms.webservice.validation.lmsapi.LmsApiUserPredicate.isCustomerHasManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;

public class LmsApiOrganizationalGroupsPredicate {

	public static boolean isValidOrganizationalGroups(OrganizationalGroups organizationalGroups) {
		return organizationalGroups != null;
	}
	
	public static boolean isValidOrgGroupHierarchy(OrganizationalGroups organizationalGroups) {
		boolean valid = false;
		if (isValidOrganizationalGroups(organizationalGroups)) {
			List<String> orgGroupHierarchyList = organizationalGroups.getOrgGroupHierarchy();
			if (!orgGroupHierarchyList.isEmpty()) {
				valid = true;
			}
		} 
		
		return valid;
	}
	
	public static boolean isValidOrgGroupHierarchy(String orgGroupHierarchy) {
		return isValid(orgGroupHierarchy);
	}
	
	public static boolean isManagerHasSecurityRightsForAtleastOneOrgGroupHierarchy(VU360User manager, List<String> orgGroupHierarchies) {
		boolean hasManagerSecurityRightsForAtleastOneOrgGroupHierarchy = false;
		if (!CollectionUtils.isEmpty(orgGroupHierarchies)) {
			for (String orgGroupHierarchy: orgGroupHierarchies) {
				//check hierarchy is seperated with correct delimiter i.e., <
				if (isManagerHasSecurityRightsForOrganization(manager, orgGroupHierarchy)) {
					hasManagerSecurityRightsForAtleastOneOrgGroupHierarchy = true;
					break;
				}
			}
		}
		return hasManagerSecurityRightsForAtleastOneOrgGroupHierarchy;
	}
	
	public static boolean isManagerHasSecurityRightsForOrganization(VU360User manager, String orgGroupHierarchy) {
		boolean rights = true;
		if (isCustomerHasManager(manager) && isValidOrgGroupHierarchy(orgGroupHierarchy)) {
			TrainingAdministrator trainingAdministrator = manager.getTrainingAdministrator();
			if (trainingAdministrator != null) {
				List<OrganizationalGroup> managedGroups = trainingAdministrator.getManagedGroups();
				if(!CollectionUtils.isEmpty(managedGroups)){
					Map<String,String> managedGroupsMap=new HashMap<String, String>();
					for(OrganizationalGroup managedGroup : managedGroups){
						if (managedGroup != null) {
							String managedGroupName = managedGroup.getName().toUpperCase();
							managedGroupsMap.put(managedGroupName, managedGroupName);
						}
					} // end of for
					
					String[] orgInBatchFile = orgGroupHierarchy.split(">");
					for(int i=0; i < orgInBatchFile.length; i++){
						if(!managedGroupsMap.containsKey(orgInBatchFile[i].toUpperCase())){
							rights = false;
						} 
					}
				} 	
			} 
		} else {
			rights = false;
		}
		return rights;
	}
	
	public static boolean isRootOrganizationalGroupExist(OrganizationalGroup rootOrganizationalGroup, String rootGroupName) {
		boolean exist = false;
	    if (rootOrganizationalGroup != null) {
	    	 if(rootOrganizationalGroup.getName().trim().equalsIgnoreCase(rootGroupName)) {
	         	exist = true;
	         }
	    }
       
        return exist;
	}
	
	public static boolean isManagerHasPermissionToCreateOrgGroup(VU360User manager) {
		boolean permission = false;
		if (manager != null) {
			permission = manager.isLMSAdministrator() || manager.getTrainingAdministrator().isManagesAllOrganizationalGroups();
		}
		return permission;
	}
	
	public static boolean isOrgGroupHierarchyMatchWithOrganizationalGroup(String orgGroupHierarchy, OrganizationalGroup orgGroupMatchWithOrgGroupHierarchy) {
		boolean match = false;
		if (StringUtils.isNotBlank(orgGroupHierarchy) && orgGroupMatchWithOrgGroupHierarchy != null) {
			match = orgGroupHierarchy.equalsIgnoreCase(orgGroupMatchWithOrgGroupHierarchy.toString()); 
		}
		return match;
	}
	
	public static boolean hasPermissionToCreateOrgGroup(Customer customer, VU360User manager, OrganizationalGroup organizationalGroup) {
		
        boolean hasPermission = false;
        if (manager != null && organizationalGroup != null) {
        	TrainingAdministrator trainingAdministrator = manager.getTrainingAdministrator();
        	if (trainingAdministrator != null) {
        		List<OrganizationalGroup> managedGroups = trainingAdministrator.getManagedGroups();
        		if (!CollectionUtils.isEmpty(managedGroups)) {
        			for(OrganizationalGroup managedGroup : managedGroups) {
        				String managedGroupName = managedGroup.getName();
        	            if(managedGroupName.equals(organizationalGroup.getName())) {
        	                hasPermission = true;
        	                break;
        	            }
        			} //end of for
        		}
        	}
        }
      
        return hasPermission;
	}
	
	private static boolean isValid(String value) {
		return StringUtils.isNotEmpty(value) || StringUtils.isNotBlank(value);
	}
	
}
