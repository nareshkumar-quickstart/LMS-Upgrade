package com.softech.vu360.lms.service.impl.lmsapi;

import static com.softech.vu360.lms.webservice.validation.lmsapi.LmsApiOrganizationalGroupsPredicate.isRootOrganizationalGroupExist;
import static com.softech.vu360.lms.webservice.validation.lmsapi.LmsApiOrganizationalGroupsPredicate.isManagerHasPermissionToCreateOrgGroup;
import static com.softech.vu360.lms.webservice.validation.lmsapi.LmsApiOrganizationalGroupsPredicate.hasPermissionToCreateOrgGroup;
import static com.softech.vu360.lms.webservice.validation.lmsapi.LmsApiOrganizationalGroupsPredicate.isOrgGroupHierarchyMatchWithOrganizationalGroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.lmsapi.LmsApiOrgGroupService;

@Service
public class LmsApiOrgGroupServiceImpl implements LmsApiOrgGroupService {

	private static final Logger log = Logger.getLogger(LmsApiOrgGroupServiceImpl.class.getName());
	private static final String GROUP_SPLITTER = ">";
	
	@Autowired
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	
	public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	@Override
	public Map<String, OrganizationalGroup> getOrgGroupsMap(Customer customer, VU360User manager, List<String> orgGroupHierarchies) {
		
		Map<String, OrganizationalGroup> orgGroups = null;
		if (!CollectionUtils.isEmpty(orgGroupHierarchies)) {
			orgGroups = new HashMap<>();
			for (String orgGroupHierarchy : orgGroupHierarchies) {
				try {
					OrganizationalGroup orgGroup = createOrganizationalGroup(customer, manager, orgGroupHierarchy);
					orgGroups.put(orgGroupHierarchy, orgGroup);
				} catch (Exception e) {
					orgGroups.put(orgGroupHierarchy, null);
				}
			} //end of for
		}
		return orgGroups;
	}
	
	@Override
	public OrganizationalGroup createOrganizationalGroup(Customer customer, VU360User manager, String orgGroupHierarchy) throws Exception {
		OrganizationalGroup orgGroup = null;
		OrganizationalGroup existingOrganizationalGroup = getExistingOrganizationalGroup(manager, orgGroupHierarchy);
		if (existingOrganizationalGroup == null) {
			OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
			String rootGroupName = getRootOrgGroupName(orgGroupHierarchy);
			if (isRootOrganizationalGroupExist(rootOrganizationalGroup, rootGroupName)) {
				Map<String, OrganizationalGroup> allOrganizationalGroups = getAllOrganizationalGroups(customer);
				OrganizationalGroup orgGroupMatchWithOrgGroupHierarchy = getOrgGroupMatchWithOrgGroupHierarchy(orgGroupHierarchy, allOrganizationalGroups);
				if(!isOrgGroupHierarchyMatchWithOrganizationalGroup(orgGroupHierarchy, orgGroupMatchWithOrgGroupHierarchy)) {
					List<String> missingOrgGroupNames = getMissingOrgGroupNames(orgGroupMatchWithOrgGroupHierarchy, orgGroupHierarchy);
					try {
						OrganizationalGroup	newOrganizationalGroup = createMissingOrgGroup(manager, customer, rootOrganizationalGroup, orgGroupMatchWithOrgGroupHierarchy, orgGroupHierarchy, missingOrgGroupNames);
						orgGroup = newOrganizationalGroup;
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
	            } else {
	            	log.info("No new groups/subgroups to create, group already exists");
	            	orgGroup = orgGroupMatchWithOrgGroupHierarchy;
	            } 
			} else {
				throw new Exception("Root Orgnization Group does not exists: " + rootGroupName);
			}
		} else {
			orgGroup = existingOrganizationalGroup;
		}
		return orgGroup;
	}

	@Override
	public OrganizationalGroup getExistingOrganizationalGroup(VU360User manager, String orgGroupHierarchy) {
		
		orgGroupHierarchy = getOrgGrouptHierarchy(orgGroupHierarchy);
		Map<String,OrganizationalGroup> managedGroupsMap = new HashMap<String, OrganizationalGroup>();
		TrainingAdministrator trainingAdministrator = manager.getTrainingAdministrator();
		if (trainingAdministrator != null) {
			List<OrganizationalGroup> managedGroups = trainingAdministrator.getManagedGroups();
			if(!CollectionUtils.isEmpty(managedGroups)){
				for(OrganizationalGroup managedGroup : managedGroups){
					if (managedGroup != null) {
						String managedGroupName = managedGroup.getName().toUpperCase();
						managedGroupsMap.put(managedGroupName, managedGroup);
					}
				}
			}
		}
	
		OrganizationalGroup organizationalGroup = getOrganizationalGroup(orgGroupHierarchy, managedGroupsMap);
		return organizationalGroup;
		
	}
	
	@Override
	public OrganizationalGroup getRootOrganizationalGroup(Customer customer) {
		 OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
        rootOrgGroup = orgGroupLearnerGroupService.loadForUpdateOrganizationalGroup(rootOrgGroup.getId());
        return rootOrgGroup;   
	}
	
	@Override
	public String getRootOrgGroupName(String orgGroupHierarchy) {
		
		String rootGroupName = null;
		if (StringUtils.isNotBlank(orgGroupHierarchy)) {
		    if (orgGroupHierarchy.indexOf(GROUP_SPLITTER) > 0) {
		        rootGroupName = orgGroupHierarchy.substring(0, orgGroupHierarchy.indexOf(GROUP_SPLITTER)).trim();
		    } else {
		        rootGroupName = orgGroupHierarchy.trim();
		    }
		}
       
        return rootGroupName;
	}
	
	@Override
	public Map<String, OrganizationalGroup> getAllOrganizationalGroups(Customer customer) {
		
		Map<String, OrganizationalGroup> allOrganizationalGroups = new HashMap<String, OrganizationalGroup>();
		OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
        allOrganizationalGroups = this.getOrganizationalGroupMap(allOrganizationalGroups, rootOrgGroup);
        return allOrganizationalGroups;
	}
	
	
	@Override
	public OrganizationalGroup getOrgGroupMatchWithOrgGroupHierarchy(String orgGroupHierarchy, Map<String, OrganizationalGroup> allOrganizationalGroups) {
		
		OrganizationalGroup organizationalGroup = null;
		String hierarchy = orgGroupHierarchy;
		do {
            log.info("lookup group '" + hierarchy + "'...");
            organizationalGroup = allOrganizationalGroups.get(hierarchy.trim().toUpperCase());
            if(organizationalGroup == null) {
                log.info("not found");
                if(hierarchy.indexOf(GROUP_SPLITTER) > 0) {
             	   hierarchy = hierarchy.substring(0, hierarchy.lastIndexOf(GROUP_SPLITTER));
                } else {
             	   hierarchy = ""; 
                }    
                log.info("hierarchy = " + hierarchy);
            }
            else {
         	   log.info("found"); 
            }                   
            log.info("hierarchy.length() = "  + hierarchy.length());
            log.info("organizationalGroup = " + organizationalGroup);
            
        } while(organizationalGroup == null && hierarchy.length() > 0);
		
		return organizationalGroup;
	}

	@Override
	public List<String> getMissingOrgGroupNames(OrganizationalGroup organizationalGroup, String orgGroupHierarchy) {
		
		String missingOrgGroupHierarchy = orgGroupHierarchy.substring(organizationalGroup.toString().length() + GROUP_SPLITTER.length());
		String[] orgGroupNames = missingOrgGroupHierarchy.split(GROUP_SPLITTER);
		List<String> missingOrgGroupNames = Arrays.asList(orgGroupNames);
		return missingOrgGroupNames;
		
	}
	
	public OrganizationalGroup createMissingOrgGroup(VU360User manager, Customer customer, OrganizationalGroup rootOrganizationalGroup, OrganizationalGroup orgGroupMatchWithOrgGroupHierarchy, String orgGroupHierarchy, List<String> orgGroupNames) throws Exception {
		
        OrganizationalGroup newOrgGroup = null;
        
        for(String orgGroupName : orgGroupNames) {
        	try {
        	    newOrgGroup = createNewOrgGroup(customer, manager, orgGroupName, rootOrganizationalGroup, orgGroupMatchWithOrgGroupHierarchy);
        		log.info("new org group created");
                orgGroupMatchWithOrgGroupHierarchy.getChildrenOrgGroups().add(newOrgGroup);
                newOrgGroup.setRootOrgGroup(rootOrganizationalGroup);
                newOrgGroup.setParentOrgGroup(orgGroupMatchWithOrgGroupHierarchy);
                log.info("new org group added");
                orgGroupMatchWithOrgGroupHierarchy = newOrgGroup;
        	} catch (Exception e) {
        		String errorMessage = e.getMessage();
        		throw new Exception(errorMessage);
        	}
        } //end of for()
        
        return newOrgGroup;
		
	} //end of createMissingOrgGroup()
	
	private OrganizationalGroup createNewOrgGroup(Customer customer, VU360User manager, String orgGroupName, OrganizationalGroup rootOrganizationalGroup, OrganizationalGroup organizationalGroup) throws Exception {
		
        OrganizationalGroup newOrgGroup = null;
        
        if(isManagerHasPermissionToCreateOrgGroup(manager)) {
        	log.info("creating org group '" + orgGroupName + "'...");
        	newOrgGroup = orgGroupLearnerGroupService.createOrgGroup2(customer,orgGroupName, rootOrganizationalGroup, organizationalGroup);
        } else if(hasPermissionToCreateOrgGroup(customer, manager, organizationalGroup)) {
    		log.info("creating org group '" + orgGroupName + "'...");
            newOrgGroup = orgGroupLearnerGroupService.createOrgGroup2(customer,orgGroupName, rootOrganizationalGroup, organizationalGroup);
    	} else {
    		log.error("ERROR >> No permissions to create org group.");
    		String errorMessage = "No permissions to create org group";
    		throw new Exception(errorMessage);
    	} 
        return newOrgGroup;
	}
	
	private Map<String, OrganizationalGroup> getOrganizationalGroupMap(Map<String, OrganizationalGroup> orgGroupMap, OrganizationalGroup rootOrgGroup){
		orgGroupMap.put(rootOrgGroup.toString().toUpperCase(), rootOrgGroup);
        List<OrganizationalGroup> childrenOrgGroups = rootOrgGroup.getChildrenOrgGroups();
        if(!CollectionUtils.isEmpty(childrenOrgGroups)) {
        	 for (OrganizationalGroup childOrgGroup : childrenOrgGroups) {
             	orgGroupMap = getOrganizationalGroupMap(orgGroupMap, childOrgGroup);
             }
        }        
        return orgGroupMap;
    }
	
	private OrganizationalGroup getOrganizationalGroup(String orgGroupHierarchy, Map<String,OrganizationalGroup> managedGroupsMap) {
		
		OrganizationalGroup organizationalGroup = null;
		if (!CollectionUtils.isEmpty(managedGroupsMap)) {
			String[] orgInBatchFile = orgGroupHierarchy.split(">");
			for(int i=0;i <orgInBatchFile.length ; i++) {
				if(managedGroupsMap.containsKey(orgInBatchFile[i].toUpperCase())) {
					organizationalGroup = managedGroupsMap.get(orgInBatchFile[i].toUpperCase());
				}	
			}
		}
		return organizationalGroup;
	}
	
	private String getOrgGrouptHierarchy(String orgGroupHierarchy) {
		
		String hierarchy = "";
        String[] groupNames = orgGroupHierarchy.split(GROUP_SPLITTER);
        for(String orgGroupName : groupNames) {
        	if(hierarchy == "") {
        		hierarchy = orgGroupName.trim();
        	} else {
        		hierarchy = hierarchy+ ">" + orgGroupName.trim();
        	}		
        } //end of for()
        
        return hierarchy;
	}

}
