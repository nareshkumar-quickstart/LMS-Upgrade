package com.softech.vu360.lms.service.impl.lmsapi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.service.lmsapi.OrgGroupServiceLmsApi;
import com.softech.vu360.lms.service.lmsapi.VU360UserServiceLmsApi;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.AddOrgGroupByParentIdOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.DeletedOrganizationalGroup;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.InvalidOrgGroupValueWithError;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.InvalidOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.NewOrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.ResponseOrganizationalGroup;

public class OrgGroupServiceImplLmsApi implements OrgGroupServiceLmsApi {
	
	private static final Logger log = Logger.getLogger(OrgGroupServiceImplLmsApi.class.getName());
	private static final String NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR  = "No manager found for customer";
	private static final String GROUP_SPLITTER = ">";
	
	private VU360UserServiceLmsApi vu360UserServiceLmsApi;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private LearnerService learnerService;
	private EntitlementService entitlementService;
	private VU360UserService vu360UserService;

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public void setVu360UserServiceLmsApi(VU360UserServiceLmsApi vu360UserServiceLmsApi) {
		this.vu360UserServiceLmsApi = vu360UserServiceLmsApi;
	}
	
	public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}
	
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public boolean isOrganizationalGroupExist(Customer customer, String orgGroupHierarchy) throws Exception {
		
		boolean isOrgExists = false;
		OrganizationalGroup organizationalGroup = getExistingOrganizationalGroup(customer, orgGroupHierarchy);
		if (organizationalGroup != null) {
			isOrgExists = true;
		}
		return isOrgExists;
		
	}
	
	public String getOrgGrouptHierarchy(String orgGroupHierarchy) {
		
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

	@Override
	public OrganizationalGroup getExistingOrganizationalGroup(Customer customer, String orgGroupHierarchy) throws Exception {
		VU360User manager = vu360UserServiceLmsApi.getVU360UserByCustomerGUID(customer);
		if (manager == null) {
			String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customer.getCustomerCode() ;
			throwException(errorMessage);
		}
        orgGroupHierarchy = getOrgGrouptHierarchy(orgGroupHierarchy);
		Map<String,OrganizationalGroup> managedGroupsMap = new HashMap<String, OrganizationalGroup>();
		if(manager.getTrainingAdministrator()!= null && !manager.getTrainingAdministrator().getManagedGroups().isEmpty()){
			for(OrganizationalGroup managedGroups : manager.getTrainingAdministrator().getManagedGroups()){
				managedGroupsMap.put(managedGroups.getName().toUpperCase(), managedGroups);
			}
		}
		
		OrganizationalGroup organizationalGroup = null;
		String[] orgInBatchFile = orgGroupHierarchy.split(">");
		for(int i=0;i <orgInBatchFile.length ; i++) {
			if(managedGroupsMap.containsKey(orgInBatchFile[i].toUpperCase())) {
				organizationalGroup = managedGroupsMap.get(orgInBatchFile[i].toUpperCase());
			}	
		}
		return organizationalGroup;
	}
	
	private void throwException(String error) throws Exception {
		log.debug(error);
		throw new Exception(error);
	}

	@Override
	public boolean isRootOrganizationalGroupExist(Customer customer,String orgGroupHierarchy) throws Exception {
		
		OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
		String rootGroup = getRootOrgGroupName(orgGroupHierarchy);
        log.info("rootGroup = " + rootGroup);
        if(!rootOrganizationalGroup.getName().equalsIgnoreCase(rootGroup)) {
            log.error("ERROR >> rootGroup does not exists");
            return false;
        }
        return true;
	}

	@Override
	public String getRootOrgGroupName(String orgGroupHierarchy) throws Exception {
		
		String rootGroup = null;
        if (orgGroupHierarchy.indexOf(GROUP_SPLITTER) > 0) {
        	rootGroup = orgGroupHierarchy.substring(0, orgGroupHierarchy.indexOf(GROUP_SPLITTER)).trim();
        } else {
        	rootGroup = orgGroupHierarchy.trim();
        }
        
        return rootGroup;
	}

	@Override
	public OrganizationalGroup getRootOrganizationalGroup(Customer customer)throws Exception {
		OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
        rootOrgGroup = orgGroupLearnerGroupService.loadForUpdateOrganizationalGroup(rootOrgGroup.getId());
        return rootOrgGroup;
	}
	
	public Boolean hasManagerSecurityRightsForOrganization(Customer customer,	String orgGroupHierarchy) throws Exception {
		 
		VU360User manager = vu360UserServiceLmsApi.getVU360UserByCustomerGUID(customer);
		
		Map<String,String> managedGroupsMap=new HashMap<String, String>();
		if(manager.getTrainingAdministrator()!= null && !manager.getTrainingAdministrator().getManagedGroups().isEmpty()){
			for(OrganizationalGroup managedGroups : manager.getTrainingAdministrator().getManagedGroups()){
				managedGroupsMap.put(managedGroups.getName().toUpperCase(), managedGroups.getName().toUpperCase());
			}
			String[] orgInBatchFile=orgGroupHierarchy.split(">");
			for(int i=0;i <orgInBatchFile.length ; i++){
				if(!managedGroupsMap.containsKey(orgInBatchFile[i].toUpperCase())){
					return true;
				}
			}
		}
		return false;
	}
	
	public OrganizationalGroup getOrganizationalGroupFromAllOrganizationalGroups(Customer customer, String orgGroupHierarchy) throws Exception {
		
		OrganizationalGroup organizationalGroup = null;
		OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
		Map<String, OrganizationalGroup> allOrganizationalGroups = getAllOrganizationalGroups(customer);
		
        log.info("rootOrgGroup.getName() = " + rootOrganizationalGroup.getName());
        log.info("allOrganizationalGroups() = " + allOrganizationalGroups);
		
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
		
	} //end of getOrganizationalGroupFromAllOrganizationalGroups()
	
	public Map<String, OrganizationalGroup> getAllOrganizationalGroups(Customer customer) throws Exception {
		
		Map<String, OrganizationalGroup> allOrganizationalGroups = new HashMap<String, OrganizationalGroup>();
		OrganizationalGroup rootOrgGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
        allOrganizationalGroups = this.getOrganizationalGroupMap(allOrganizationalGroups, rootOrgGroup);
        return allOrganizationalGroups;
	}
	
	private Map<String, OrganizationalGroup> getOrganizationalGroupMap (Map<String, OrganizationalGroup> orgGroupMap, OrganizationalGroup rootOrgGroup){
    	orgGroupMap.put(rootOrgGroup.toString().toUpperCase(), rootOrgGroup);
        List<OrganizationalGroup> childrenOrgGroups = rootOrgGroup.getChildrenOrgGroups();
        if(childrenOrgGroups != null && childrenOrgGroups.size() > 0) {
        	 for (OrganizationalGroup childOrgGroup : childrenOrgGroups) {
             	orgGroupMap = this.getOrganizationalGroupMap(orgGroupMap, childOrgGroup);
             }
        }        
        return orgGroupMap;
    }
	
	public OrganizationalGroup createMissingOrgGroup(Customer customer, OrganizationalGroup organizationalGroup, 
			String orgGroupHierarchy) throws Exception {
		
		VU360User manager = vu360UserServiceLmsApi.getVU360UserByCustomerGUID(customer);
		if (manager == null) {
			String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customer.getCustomerCode() ;
			throw new Exception(errorMessage);
		}
		OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
		log.info("creating missing groups...");
        orgGroupHierarchy = orgGroupHierarchy.substring(organizationalGroup.toString().length() + GROUP_SPLITTER.length());
        log.info("missing orgGroupHierarchy = " + orgGroupHierarchy);
        String[] orgGroupNames = orgGroupHierarchy.split(GROUP_SPLITTER);
        OrganizationalGroup newOrgGroup = null;
        
        for(String orgGroupName : orgGroupNames) {
        	
        	try {
        		newOrgGroup = createNewOrgGroup(customer, manager, orgGroupName, organizationalGroup);
        	} catch (Exception e) {
        		String errorMessage = e.getMessage();
        		throw new Exception(errorMessage);
        	}
        	
            log.info("new org group created");
       	 	organizationalGroup.getChildrenOrgGroups().add(newOrgGroup);
            newOrgGroup.setRootOrgGroup(rootOrganizationalGroup);
            newOrgGroup.setParentOrgGroup(organizationalGroup);
            log.info("new org group added");
            organizationalGroup = newOrgGroup;
            
        } //end of for()
        
        return newOrgGroup;
		
	} //end of createMissingOrgGroup()
	
	public OrganizationalGroup createNewOrgGroup (Customer customer, VU360User manager, String orgGroupName, OrganizationalGroup organizationalGroup) throws Exception {
		
		if (manager == null) {
			manager = vu360UserServiceLmsApi.getVU360UserByCustomerGUID(customer);
			if (manager == null) {
				String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customer.getCustomerCode() ;
				throw new Exception(errorMessage);
			}
		}
	
		log.info("orgGroupName = " + orgGroupName);
        
        OrganizationalGroup newOrgGroup = null;
        
        if(vu360UserService.hasAdministratorRole(manager) || manager.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
        	log.info("creating org group '" + orgGroupName + "'...");
        	newOrgGroup = createNewOrganizationalGroup(customer, orgGroupName, organizationalGroup);
        } else {
        	boolean hasPermission = hasPermissionToCreateOrgGroup(customer, organizationalGroup);
        	if(hasPermission) {
        		log.info("creating org group '" + orgGroupName + "'...");
                newOrgGroup = createNewOrganizationalGroup(customer, orgGroupName, organizationalGroup);
        	} else {
        		log.error("ERROR >> No permissions to create org group.");
        		String errorMessage = "No permissions to create org group";
        		throw new Exception(errorMessage);
        	}
        }
        
        return newOrgGroup;
    	
	}
	
	public OrganizationalGroup createNewOrgGroup(Customer customer, String orgGroupName, String orgGroupHierarchy) throws Exception {
		
		VU360User manager = vu360UserServiceLmsApi.getVU360UserByCustomerGUID(customer);
		if (manager == null) {
			String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customer.getCustomerCode() ;
			throw new Exception(errorMessage);
		}
		
		log.info("orgGroupName = " + orgGroupName);
        log.info("manager.getTrainingAdministrator().isManagesAllOrganizationalGroups() = " + manager.getTrainingAdministrator().isManagesAllOrganizationalGroups());
        
        OrganizationalGroup organizationalGroup = getOrganizationalGroupFromAllOrganizationalGroups(customer, orgGroupHierarchy);
    	if(organizationalGroup != null && !organizationalGroup.toString().equalsIgnoreCase(orgGroupHierarchy)) {
    		return createNewOrgGroup(customer, manager, orgGroupName, organizationalGroup);
        } else {
        	return null;	
        }
        	
	}
	
	public boolean hasPermissionToCreateOrgGroup(Customer customer, OrganizationalGroup organizationalGroup) throws Exception{
		
		log.info("checking permission...");
		VU360User manager = vu360UserServiceLmsApi.getVU360UserByCustomerGUID(customer);
		if (manager == null) {
			String errorMessage = NO_MANAGER_FOUND_FOR_CUSTOMER_ERROR + ": " + customer.getCustomerCode() ;
			throw new Exception(errorMessage);
		}
        boolean hasPermission = false;
        List<OrganizationalGroup> managedGroups = manager.getTrainingAdministrator().getManagedGroups();
        log.info("managedGroups = " + managedGroups);
        for(OrganizationalGroup permitedOrgGroup : manager.getTrainingAdministrator().getManagedGroups()) {
        	log.info("\tchecking group '" + permitedOrgGroup.getName() + "'...");
            if(permitedOrgGroup.getName().equals(organizationalGroup.getName())) {
                hasPermission = true;
                break;
            }
        }
        
        return hasPermission;
		
	}
	
	public OrganizationalGroup createNewOrganizationalGroup(Customer customer, String orgGroupName, OrganizationalGroup organizationalGroup) throws Exception {
		
		OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
		OrganizationalGroup newOrgGroup = orgGroupLearnerGroupService.createOrgGroup2(customer,orgGroupName, rootOrganizationalGroup, organizationalGroup);
		return newOrgGroup;
	}
	
	public OrganizationalGroup createNewOrganizationalGroup(Customer customer, String orgGroupName, String orgGroupHierarchy) throws Exception {
		
		OrganizationalGroup newOrgGroup = null;
		
		OrganizationalGroup rootOrganizationalGroup = getRootOrganizationalGroup(customer);
		OrganizationalGroup organizationalGroup = getOrganizationalGroupFromAllOrganizationalGroups(customer, orgGroupHierarchy);
    	if(organizationalGroup != null && organizationalGroup.toString().equalsIgnoreCase(orgGroupHierarchy)) {
    	    newOrgGroup = orgGroupLearnerGroupService.createOrgGroup2(customer,orgGroupName, rootOrganizationalGroup, organizationalGroup);
        } 
    
		return newOrgGroup;
	}
	
	public Boolean organizationalGroupsValidation(OrganizationalGroups organizationalGroups) throws Exception {
		
		String errorMessage = null;
		if (organizationalGroups != null) {
			List<String> orgGroupHierarchyList = organizationalGroups.getOrgGroupHierarchy();	
			if (!orgGroupHierarchyList.isEmpty()) {
				
			} else {
				errorMessage = "Atleast one OrgGroupHierarchy is required";
				throw new Exception(errorMessage);
			}
		} else {
			errorMessage = "OrganizationalGroups elemnt is required";
			throw new Exception(errorMessage);
		}
		return true;
	}
	
	public Map<String, Object> getOrgGroupHierarchyMap(Customer customer,List<String> orgGroupHierarchyList) throws Exception {
	
		Map<String, Object> OrgGroupHierarchyMap = new HashMap<String, Object>();
		Map<String, String> inValidOrgGroupHierarchyMap = new HashMap<String, String>();
		List<String> validOrgGroupHierarchyList = new ArrayList<String>();
		
		for (String orgGroupHierarchy: orgGroupHierarchyList) {
			try {
				String validOrgGroupHierarchy = getValidOrgGroupHierarchy(customer, orgGroupHierarchy);
				validOrgGroupHierarchyList.add(validOrgGroupHierarchy);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				inValidOrgGroupHierarchyMap.put(orgGroupHierarchy, errorMessage);
			}
		} //end of for()
	
		if (!validOrgGroupHierarchyList.isEmpty()) {
			OrgGroupHierarchyMap.put("validOrgGroupHierarchyList", validOrgGroupHierarchyList);
		}
		
		if (!inValidOrgGroupHierarchyMap.isEmpty()) {
			OrgGroupHierarchyMap.put("inValidOrgGroupHierarchyMap", inValidOrgGroupHierarchyMap);
		}
		
		return OrgGroupHierarchyMap;
	}
	
	private String getValidOrgGroupHierarchy(Customer customer, String orgGroupHierarchy) throws Exception {
		
		if (StringUtils.isEmpty(orgGroupHierarchy) || StringUtils.isBlank(orgGroupHierarchy)) {
			String errorMessage = "OrgGroupHierarchy can not be empty or blank";
			throw new Exception(errorMessage);
		} else {
			//check hierarchy is seperated with correct delimiter i.e., <
			if(hasManagerSecurityRightsForOrganization(customer,orgGroupHierarchy)){
				String errorMessage = "Manager for customer: " + customer.getCustomerCode() + " do not have rigths to create organizational group: " + orgGroupHierarchy;
				throw new Exception(errorMessage);
			}
		} 
		return orgGroupHierarchy;
	}
	
	@Override
	public Map<String, Object> getOrganizationalGroupMap(Customer customer, List<String> validOrgGroupHierarchyList) throws Exception {
		
		Map<String, Object> organizationalGroupMap = new HashMap<String, Object>();
		Map<String, String> organizationalGroupErrorMap = new HashMap<String, String>();
		//List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorListForOrganizationalGroup = new ArrayList<RegisterOrganizationalGroup>();
		List<OrganizationalGroup> validOrganizationalGroupList = new ArrayList<OrganizationalGroup>();
		List<String> orgGroupHierarchyListForResponse = new ArrayList<String>();
		for (String orgGroupHierarchy: validOrgGroupHierarchyList) {
			try {
				OrganizationalGroup organizationalGroup = getOrganizationalGroup(customer, orgGroupHierarchy);
				validOrganizationalGroupList.add(organizationalGroup);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				organizationalGroupErrorMap.put(orgGroupHierarchy, errorMessage);
				continue;
			}
			
			orgGroupHierarchyListForResponse.add(orgGroupHierarchy);
			
			//String errorMessage = "";
			//RegisterOrganizationalGroup registerOrgGroup = getRegisterOrganizationalGroup("0", errorMessage, orgGroupHierarchy);
			//registerOrganizationalGroupErrorListForOrganizationalGroup.add(registerOrgGroup);
			//continue;
		}
		
		if (!validOrganizationalGroupList.isEmpty()) {
			organizationalGroupMap.put("validOrganizationalGroupList", validOrganizationalGroupList);
		}
		
		if (!orgGroupHierarchyListForResponse.isEmpty()) {
			organizationalGroupMap.put("orgGroupHierarchyListForResponse", orgGroupHierarchyListForResponse);
		}
		
		if (!organizationalGroupErrorMap.isEmpty()) {
			organizationalGroupMap.put("organizationalGroupErrorMap", organizationalGroupErrorMap);
		}
		
		
		
		return organizationalGroupMap;
	}
	
	@Override
	public Map<String, Object> getExistingOrganizationalGroupMap(Customer customer, List<String> validOrgGroupHierarchyList) throws Exception {
		
		Map<String, Object> organizationalGroupMap = new HashMap<String, Object>();
		Map<String, String> organizationalGroupErrorMap = new HashMap<String, String>();
		//List<RegisterOrganizationalGroup> registerOrganizationalGroupErrorListForOrganizationalGroup = new ArrayList<RegisterOrganizationalGroup>();
		List<OrganizationalGroup> validOrganizationalGroupList = new ArrayList<OrganizationalGroup>();
		List<String> orgGroupHierarchyListForResponse = new ArrayList<String>();
		for (String orgGroupHierarchy: validOrgGroupHierarchyList) {
			try {
				OrganizationalGroup organizationalGroup = getExistingOrganizationalGroup(customer, orgGroupHierarchy);
				validOrganizationalGroupList.add(organizationalGroup);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				organizationalGroupErrorMap.put(orgGroupHierarchy, errorMessage);
				continue;
			}
			
			orgGroupHierarchyListForResponse.add(orgGroupHierarchy);
			
		}
		
		if (!validOrganizationalGroupList.isEmpty()) {
			organizationalGroupMap.put("validOrganizationalGroupList", validOrganizationalGroupList);
		}
		
		if (!orgGroupHierarchyListForResponse.isEmpty()) {
			organizationalGroupMap.put("orgGroupHierarchyListForResponse", orgGroupHierarchyListForResponse);
		}
		
		if (!organizationalGroupErrorMap.isEmpty()) {
			organizationalGroupMap.put("organizationalGroupErrorMap", organizationalGroupErrorMap);
		}
		
		
		
		return organizationalGroupMap;
	}
	
	private OrganizationalGroup getOrganizationalGroup(Customer customer, String orgGroupHierarchy) throws Exception {
		
		OrganizationalGroup organizationalGroup = null;
        boolean isOrgGroupExist = isOrganizationalGroupExist(customer, orgGroupHierarchy);
    	if (!isOrgGroupExist) {
    		organizationalGroup = createOrganizationalGroup(customer, orgGroupHierarchy);
    		if (organizationalGroup != null) {
                log.debug("org group(s) created !");
    	    } else {
    			log.error("ERROR >> invalid  Org group or not permitted");
    			String errorMessage = "invalid Org group or not permitted";
    			throw new Exception(errorMessage); 
    		}
    	} else {
    		organizationalGroup = getExistingOrganizationalGroup(customer, orgGroupHierarchy);
    		if (organizationalGroup == null) {
    			log.debug("orgGroup not found");
    			String errorMessage = "Organizational Group exist but no organizational group is returning";
   				 throw new Exception(errorMessage);
    		}      
    	}	
     
		return organizationalGroup;
	}
	
	private OrganizationalGroup createOrganizationalGroup(Customer customer, String orgGroupHierarchy) throws Exception {
		   
		log.info("orgGroupHierarchy = " + orgGroupHierarchy);
        OrganizationalGroup organizationalGroup = null;
        boolean isRootGroupExist = isRootOrganizationalGroupExist(customer, orgGroupHierarchy);
        if (isRootGroupExist) { 
        	organizationalGroup = getOrganizationalGroupFromAllOrganizationalGroups(customer, orgGroupHierarchy);
        	if(organizationalGroup != null && !organizationalGroup.toString().equalsIgnoreCase(orgGroupHierarchy)) {
            	organizationalGroup = createMissingOrgGroup(customer, organizationalGroup, orgGroupHierarchy);
            } else {
            	log.info("No new groups/subgroups to create, group already exists");
            }   
        } else {  //end of if (isRootGroupExist)
        	String rootGroup =  getRootOrgGroupName(orgGroupHierarchy);
        	log.error("ERROR >> root Orgnization Group does not exists: " + rootGroup);
        	String errorMessage = "Root Orgnization Group does not exists: " + rootGroup;
        	throw new Exception(errorMessage);   
        }  
        return organizationalGroup;
	}
	
	public boolean isValidParentOrgGroupHierarchy(Customer customer, String parentOrgGroupHierarchy) throws Exception {
		
		OrganizationalGroup organizationalGroup = null;
		String errorMessage = null;
		
		if (StringUtils.isEmpty(parentOrgGroupHierarchy)) {
			errorMessage = "ParentOrgGroupHierarchy can not be empty or blanck";
			throwException(errorMessage);
		}
		
		String rootGroup =  getRootOrgGroupName(parentOrgGroupHierarchy);
		boolean isRootGroupExist = isRootOrganizationalGroupExist(customer, parentOrgGroupHierarchy);
		if (isRootGroupExist) { 
			organizationalGroup = getOrganizationalGroupFromAllOrganizationalGroups(customer, parentOrgGroupHierarchy);
			long parentId = organizationalGroup.getId();
			System.out.println();
		}
        	
		//boolean isParentOrgGroupHierarchyExist = isOrganizationalGroupExist(customer, parentOrgGroupHierarchy);
		if (organizationalGroup == null) {
			errorMessage = "Invalid ParentOrgGroupHierarchy";
			throwException(errorMessage);
		}
		
		return true;
		
	}
	
	public Map<String, Object> isValidOrgGroupNames(Customer customer, List<String> orgGroupNames) throws Exception {
		
		List<String> validOrgGroupNameList = new ArrayList<String>();
		Map<String, Object> orgGroupMap = new LinkedHashMap<String, Object>();
		Map<String, String> orgGroupErrorMap = new LinkedHashMap<String, String>();
		
		for(String orgGroupName : orgGroupNames) {
			try {
				isValidOrgGroupName(customer, orgGroupName);
				validOrgGroupNameList.add(orgGroupName);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				orgGroupErrorMap.put(orgGroupName, errorMessage);
			}
		}
		
		if (!validOrgGroupNameList.isEmpty()) {
			orgGroupMap.put("validOrgGroupNameList", validOrgGroupNameList);
		}
		
		if (!orgGroupErrorMap.isEmpty()) {
			orgGroupMap.put("orgGroupErrorMap", orgGroupErrorMap);
		}
		
		return orgGroupMap;
		
	}
	
	private boolean isValidOrgGroupName(Customer customer, String orgGroupName) throws Exception {
		
		String errorMessage = null;
		
		if (StringUtils.isEmpty(orgGroupName)) {
			errorMessage = "OrgGroupName can not be empty or blanck";
			throwException(errorMessage);
		}
		
		List<OrganizationalGroup> existingOrgGroups = orgGroupLearnerGroupService.getOrgGroupByNames(new String[]{orgGroupName}, customer);
		
		if(!existingOrgGroups.isEmpty()){
			errorMessage = "OrgGroupName already exist";
			throwException(errorMessage);
		}
		
		return true;
		
	}
	
public Map<String, Object> validateAddOrgGroupByParentIdRequest(Customer customer, List<AddOrgGroupByParentIdOrganizationalGroup>  organizationGroupsList) throws Exception {
		
		List<AddOrgGroupByParentIdOrganizationalGroup> validOrgGroupList = new ArrayList<AddOrgGroupByParentIdOrganizationalGroup>();
		Map<String, Object> orgGroupMap = new LinkedHashMap<String, Object>();
		Map<Object, String> orgGroupErrorMap = new LinkedHashMap<Object, String>();
		
		for (AddOrgGroupByParentIdOrganizationalGroup organizationGroup : organizationGroupsList) {
			
			try {
				BigInteger parentId = organizationGroup.getParentId();
				String orgGroupName = organizationGroup.getOrgGroupName();
				
				validateAddOrgGroupByParentIdRequest(customer, parentId, orgGroupName);
				validOrgGroupList.add(organizationGroup);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				orgGroupErrorMap.put(organizationGroup, errorMessage);
			}
		}
		
		if (!validOrgGroupList.isEmpty()) {
			orgGroupMap.put("validOrgGroupList", validOrgGroupList);
		}
		
		if (!orgGroupErrorMap.isEmpty()) {
			orgGroupMap.put("orgGroupErrorMap", orgGroupErrorMap);
		}
		
		return orgGroupMap;
	}

	private boolean validateAddOrgGroupByParentIdRequest(Customer customer, BigInteger parentId, String orgGroupName) throws Exception {
	
		String errorMessage = null;
	
		if (parentId == null) {
			errorMessage = "parentId can not be null or blank";
			throwException(errorMessage);
		}
	
		OrganizationalGroup parentOrganizationalGroup = learnerService.loadForUpdateOrganizationalGroup(parentId.longValue());
		if (parentOrganizationalGroup == null) {
			errorMessage = "Invalid ParentId";
			throwException(errorMessage);
		}
	
		isValidOrgGroupName(customer, orgGroupName);
	
		return true;
	
	}
	
	public Map<String, Object> processOrgGroupMap(Customer customer, String parentOrgGroupHierarchy, Map<String, Object> orgGroupMap) throws Exception {
		
		List<String> validOrgGroupNameList = (List<String>)orgGroupMap.get("validOrgGroupNameList");
		
		if (validOrgGroupNameList != null) {
			List<OrganizationalGroup> newOrgGroupList = createNewOrganizationGroups(customer, parentOrgGroupHierarchy, validOrgGroupNameList);
			if (!newOrgGroupList.isEmpty()) {
				orgGroupMap.put("newOrgGroupList", newOrgGroupList);
			}
		} 
		return orgGroupMap;
	}
	
	public Map<String, Object> processOrgGroupMap(Customer customer, Map<String, Object> orgGroupMap) throws Exception {
		
		List<AddOrgGroupByParentIdOrganizationalGroup> validOrgGroupList = (List<AddOrgGroupByParentIdOrganizationalGroup>)orgGroupMap.get("validOrgGroupList");
		
		if (validOrgGroupList != null) {
			List<OrganizationalGroup> newOrgGroupList = createNewOrganizationGroups(customer, validOrgGroupList);
			if (!newOrgGroupList.isEmpty()) {
				orgGroupMap.put("newOrgGroupList", newOrgGroupList);
			}
		} 
		return orgGroupMap;
	}
	
	public NewOrganizationalGroups processOrgGroupMapForResponse(String parentOrgGroupHierarchy, Map<String, Object> orgGroupMap) throws Exception {
		
		List<ResponseOrganizationalGroup> newOrganizationalGroupList = new ArrayList<ResponseOrganizationalGroup>();
		
		NewOrganizationalGroups newOrganizationalGroups = new NewOrganizationalGroups();
		List<OrganizationalGroup> newOrgGroupList = (List<OrganizationalGroup>)orgGroupMap.get("newOrgGroupList");
		Map<String, String> orgGroupErrorMap = (Map<String, String>)orgGroupMap.get("orgGroupErrorMap");
		
		if (orgGroupErrorMap != null) {
			List<ResponseOrganizationalGroup> errorOrganizationalGroupList = processOrgGroupErrorMap(parentOrgGroupHierarchy, orgGroupErrorMap);
			newOrganizationalGroupList.addAll(errorOrganizationalGroupList);
			
		} 
			
		if (newOrgGroupList != null) {
			List<ResponseOrganizationalGroup> newResponseOrgGroupList = getNewOrgGroupResponse(parentOrgGroupHierarchy, newOrgGroupList);
			newOrganizationalGroupList.addAll(newResponseOrgGroupList);
		}
	
		newOrganizationalGroups.setNewOrganizationalGroup(newOrganizationalGroupList);
		return newOrganizationalGroups;
		
	}
	
	public NewOrganizationalGroups processOrgGroupMapForResponse(Map<String, Object> orgGroupMap) throws Exception {
		
		List<ResponseOrganizationalGroup> newOrganizationalGroupList = new ArrayList<ResponseOrganizationalGroup>();
		
		NewOrganizationalGroups newOrganizationalGroups = new NewOrganizationalGroups();
		List<OrganizationalGroup> newOrgGroupList = (List<OrganizationalGroup>)orgGroupMap.get("newOrgGroupList");
		Map<String, String> orgGroupErrorMap = (Map<String, String>)orgGroupMap.get("orgGroupErrorMap");
		
		if (orgGroupErrorMap != null) {
			List<ResponseOrganizationalGroup> errorOrganizationalGroupList = processOrgGroupErrorMap(orgGroupErrorMap);
			newOrganizationalGroupList.addAll(errorOrganizationalGroupList);
			
		} 
			
		if (newOrgGroupList != null) {
			List<ResponseOrganizationalGroup> newResponseOrgGroupList = getNewOrgGroupResponse(newOrgGroupList);
			newOrganizationalGroupList.addAll(newResponseOrgGroupList);
		}
	
		newOrganizationalGroups.setNewOrganizationalGroup(newOrganizationalGroupList);
		return newOrganizationalGroups;
		
	}
	
	private List<ResponseOrganizationalGroup> getNewOrgGroupResponse(String parentOrgGroupHierarchy, List<OrganizationalGroup> newOrgGroupList) {
		
		List<ResponseOrganizationalGroup> newOrganizationalGroupList = new ArrayList<ResponseOrganizationalGroup>();
		
		for (OrganizationalGroup newOrgGroup : newOrgGroupList) {
			Long id = newOrgGroup.getId();
			String orgGroupName = newOrgGroup.getName();
			
			// Convert Long to String.
			String stringId = id.toString();
			BigInteger bigIntId = new BigInteger( stringId );
			Long parentId = newOrgGroup.getParentOrgGroup().getId();
			String stringParentId = parentId.toString();
			BigInteger bigIntParentId = new BigInteger( stringParentId );
			ResponseOrganizationalGroup responseOrganizationalGroup = getResponseOrganizationalGroup(bigIntId, orgGroupName, parentOrgGroupHierarchy, bigIntParentId, "0", "");
			newOrganizationalGroupList.add(responseOrganizationalGroup);
		}
		return newOrganizationalGroupList;
	}
	
	private List<ResponseOrganizationalGroup> getNewOrgGroupResponse(List<OrganizationalGroup> newOrgGroupList) {
		
		List<ResponseOrganizationalGroup> newOrganizationalGroupList = new ArrayList<ResponseOrganizationalGroup>();
		
		for (OrganizationalGroup newOrgGroup : newOrgGroupList) {
			Long id = newOrgGroup.getId();
			
			// Convert Long to String.
			String stringId = id.toString();
			BigInteger bigIntId = new BigInteger( stringId );
			
			String orgGroupName = newOrgGroup.getName();
			String newOrgGroupHierarchy = newOrgGroup.toString();
			String parentOrgGroupHierarchy = newOrgGroupHierarchy.substring(0, newOrgGroupHierarchy.lastIndexOf(">"));
			
			Long parentId = newOrgGroup.getParentOrgGroup().getId();
			String stringParentId = parentId.toString();
			BigInteger bigIntParentId = new BigInteger( stringParentId );
			
			ResponseOrganizationalGroup responseOrganizationalGroup = getResponseOrganizationalGroup(bigIntId, orgGroupName, parentOrgGroupHierarchy, bigIntParentId, "0", "");
			newOrganizationalGroupList.add(responseOrganizationalGroup);
		}
		
		return newOrganizationalGroupList;
		
	}
	
	private List<ResponseOrganizationalGroup> processOrgGroupErrorMap(String parentOrgGroupHierarchy, Map<String, String> orgGroupErrorMap) throws Exception {
		
		List<ResponseOrganizationalGroup> newOrganizationalGroupList = new ArrayList<ResponseOrganizationalGroup>();
		
		for (Map.Entry<String, String> orgGroupErrorMapEntry : orgGroupErrorMap.entrySet()) {
			String orgGroupName = orgGroupErrorMapEntry.getKey();
			String errorMessage = orgGroupErrorMapEntry.getValue();
			
			ResponseOrganizationalGroup responseOrganizationalGroup = getResponseOrganizationalGroup(null, orgGroupName, parentOrgGroupHierarchy, null, "1", errorMessage);
			newOrganizationalGroupList.add(responseOrganizationalGroup);
		}
		
		return newOrganizationalGroupList;
	}
	
	private List<ResponseOrganizationalGroup> processOrgGroupErrorMap(Map<String, String> orgGroupErrorMap) throws Exception {
		
		List<ResponseOrganizationalGroup> newOrganizationalGroupList = new ArrayList<ResponseOrganizationalGroup>();
		
		for (Map.Entry<String, String> orgGroupErrorMapEntry : orgGroupErrorMap.entrySet()) {
			Object key = orgGroupErrorMapEntry.getKey();
			if (key instanceof AddOrgGroupByParentIdOrganizationalGroup) {
				AddOrgGroupByParentIdOrganizationalGroup organizationGroup = (AddOrgGroupByParentIdOrganizationalGroup)key;
				BigInteger parentId = organizationGroup.getParentId();
				String orgGroupName = organizationGroup.getOrgGroupName();
				String errorMessage = orgGroupErrorMapEntry.getValue();
				ResponseOrganizationalGroup responseOrganizationalGroup = getResponseOrganizationalGroup(parentId, orgGroupName, null, null, "1", errorMessage);
				newOrganizationalGroupList.add(responseOrganizationalGroup);
				
			}
			
		}
		
		return newOrganizationalGroupList;
	}
	
	private List<OrganizationalGroup> createNewOrganizationGroups(Customer customer, String parentOrgGroupHierarchy, List<String> validOrgGroupNameList) throws Exception {
		
		OrganizationalGroup parentOrganizationalGroup = getOrganizationalGroupFromAllOrganizationalGroups(customer, parentOrgGroupHierarchy);
		
		List<OrganizationalGroup> newOrgGroupList = new ArrayList<OrganizationalGroup>();
		
		for (String orgGroupName : validOrgGroupNameList) {
			//OrganizationalGroup newOrgGroup = createNewOrganizationalGroup(customer, orgGroupName, parentOrgGroupHierarchy);
		
			OrganizationalGroup newOrgGroup = createNewOrgGroup(customer, parentOrgGroupHierarchy, orgGroupName, parentOrganizationalGroup);
			newOrgGroupList.add(newOrgGroup);
		}
		
		return newOrgGroupList;
	}
	
	private List<OrganizationalGroup> createNewOrganizationGroups(Customer customer, List<AddOrgGroupByParentIdOrganizationalGroup> validOrgGroupList) throws Exception {
		
		List<OrganizationalGroup> newOrgGroupList = new ArrayList<OrganizationalGroup>();
		
		for (AddOrgGroupByParentIdOrganizationalGroup organizationGroup : validOrgGroupList) {
			
			BigInteger parentId = organizationGroup.getParentId();
			String orgGroupName = organizationGroup.getOrgGroupName();
			
			OrganizationalGroup newOrgGroup = createNewOrgGroup(customer, parentId.longValue(), orgGroupName);
			newOrgGroupList.add(newOrgGroup);
		}
		
		return newOrgGroupList;
	}
	
	private OrganizationalGroup createNewOrgGroup(Customer customer, long parentOrgGroupId, String orgGroupName) throws Exception {
		
		//OrganizationalGroup parentOrganizationalGroup = getOrganizationalGroupFromAllOrganizationalGroups(customer, parentOrgGroupHierarchy);
		OrganizationalGroup rootGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
		OrganizationalGroup parentOrganizationalGroup = learnerService.loadForUpdateOrganizationalGroup(parentOrgGroupId);
		
		OrganizationalGroup newOrganizationalGroup = new OrganizationalGroup();
		newOrganizationalGroup.setName(orgGroupName);
		newOrganizationalGroup.setCustomer(customer);
		newOrganizationalGroup.setRootOrgGroup(rootGroup);
		newOrganizationalGroup.setParentOrgGroup(parentOrganizationalGroup);
		parentOrganizationalGroup.getChildrenOrgGroups().add(newOrganizationalGroup);
		newOrganizationalGroup = orgGroupLearnerGroupService.addOrgGroup(parentOrganizationalGroup);
		//newOrganizationalGroup = orgGroupLearnerGroupService.addOrgGroup(newOrganizationalGroup);
		
		String newOrganizationalGroupName = newOrganizationalGroup.toString();
		String orgGroupHierarchy = newOrganizationalGroupName + ">" + orgGroupName;
		OrganizationalGroup organizationalGroup = getOrganizationalGroupFromAllOrganizationalGroups(customer, orgGroupHierarchy);
		newOrganizationalGroup = organizationalGroup;
		
		return newOrganizationalGroup;
		
	}
	
	private OrganizationalGroup createNewOrgGroup(Customer customer, String parentOrgGroupHierarchy, String orgGroupName, OrganizationalGroup parentOrganizationalGroup) throws Exception {
		
		OrganizationalGroup rootGroup = orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
		
		OrganizationalGroup newOrganizationalGroup = new OrganizationalGroup();
		newOrganizationalGroup.setName(orgGroupName);
		newOrganizationalGroup.setCustomer(customer);
		newOrganizationalGroup.setRootOrgGroup(rootGroup);
		newOrganizationalGroup.setParentOrgGroup(parentOrganizationalGroup);
		parentOrganizationalGroup.getChildrenOrgGroups().add(newOrganizationalGroup);
		newOrganizationalGroup = orgGroupLearnerGroupService.addOrgGroup(newOrganizationalGroup);
		
		return newOrganizationalGroup;
		
	}
	
	public boolean validateGetOrgGroupIdByNameRequest(Customer customer, String parentOrgGroupHierarchy, String orgGroupName) throws Exception {
		
		String errorMessage = null;
		
		isValidParentOrgGroupHierarchy(customer, parentOrgGroupHierarchy);
		
		if (StringUtils.isEmpty(orgGroupName)) {
			errorMessage = "OrgGroupName can not be empty or blanck";
			throwException(errorMessage);
		}
		
		List<OrganizationalGroup> existingOrgGroups = orgGroupLearnerGroupService.getOrgGroupByNames(new String[]{orgGroupName}, customer);
		
		if(existingOrgGroups.isEmpty()){
			errorMessage = "OrgGroupName does not exist";
			throwException(errorMessage);
		}
		
		return true;
		
	}
	
	public OrganizationalGroup createNewOrganizationGroup(Customer customer, String parentOrgGroupHierarchy, String orgGroupName) throws Exception {
		
		OrganizationalGroup parentOrganizationalGroup = getOrganizationalGroupFromAllOrganizationalGroups(customer, parentOrgGroupHierarchy);
		return createNewOrgGroup(customer, parentOrgGroupHierarchy, orgGroupName, parentOrganizationalGroup);
		
	}
	
	public OrganizationalGroup validateUpdateOrganizationRequest(Customer customer, long orgGroupId, String newOrgGroupName) throws Exception {
		
		String errorMessage = null;
		
		OrganizationalGroup organizationalGroup = findOrganizationalGroupById(orgGroupId);
		
		if(!(organizationalGroup.getCustomer().getId().equals(customer.getId()))){
			errorMessage = "Invalid OrgGroup Id";
			throwException(errorMessage);
		}
		
		if (StringUtils.isEmpty(newOrgGroupName)) {
			errorMessage = "NewOrgGroupName can not be empty or blank";
			throwException(errorMessage);
		}
		
		return organizationalGroup;
	}
	
	public OrganizationalGroup findOrganizationalGroupById(long orgGroupId) throws Exception {
		
		OrganizationalGroup organizationalGroup =learnerService.getOrganizationalGroupById(orgGroupId);
		if (organizationalGroup == null) {
			String errorMessage = "Invalid Id: " + orgGroupId ;
			throwException(errorMessage);
		}
		
		return organizationalGroup;
	}
	
	public OrganizationalGroup updateOrganizationGroup(long orgGroupId, String newOrgGroupName) {
		
		OrganizationalGroup organizationalGroup =learnerService.getOrganizationalGroupById(orgGroupId);
		return updateOrganizationGroup(organizationalGroup, newOrgGroupName);
		
	}
	
	public OrganizationalGroup updateOrganizationGroup(OrganizationalGroup organizationalGroup, String newOrgGroupName) {
		
		OrganizationalGroup updatedOrganizationalGroup = organizationalGroup;
		
		updatedOrganizationalGroup.setName(newOrgGroupName);
		updatedOrganizationalGroup = orgGroupLearnerGroupService.saveOrganizationalGroup(updatedOrganizationalGroup);
		return updatedOrganizationalGroup;
		
	}
	
	public ResponseOrganizationalGroup getUpdatedOrganizationGroupResponse(OrganizationalGroup updatedOrganizationalGroup) {
		
		Long orgGroupId = updatedOrganizationalGroup.getId();
		
		// Convert Long to String.
		String stringId = orgGroupId.toString();
		BigInteger bigIntId = new BigInteger( stringId );
		String updatedName = updatedOrganizationalGroup.getName();
		
		String updatedOrgGroupHierarchy = updatedOrganizationalGroup.toString();
		String parentOrgGroupHierarchy = updatedOrgGroupHierarchy.substring(0, updatedOrgGroupHierarchy.lastIndexOf(">"));
		
		Long parentId = updatedOrganizationalGroup.getParentOrgGroup().getId();
		String stringParentId = parentId.toString();
		BigInteger bigIntParentId = new BigInteger( stringParentId );
		
		return getResponseOrganizationalGroup(bigIntId, updatedName, null, null, "0", "");
		
	}
	
	public ResponseOrganizationalGroup getOrgGroupByIdResponse(OrganizationalGroup organizationalGroup) throws Exception {
		
Long orgGroupId = organizationalGroup.getId();
		
		// Convert Long to String.
		String stringId = orgGroupId.toString();
		BigInteger bigIntId = new BigInteger( stringId );
		
		String parentOrgGroupHierarchy = null;
		String orgGroupName = organizationalGroup.getName();
		String orgGroupHierarchy = organizationalGroup.toString();
		if (orgGroupHierarchy.lastIndexOf(">") != -1) {
			parentOrgGroupHierarchy = orgGroupHierarchy.substring(0, orgGroupHierarchy.lastIndexOf(">"));
		} else {
			parentOrgGroupHierarchy = orgGroupHierarchy;
		}
		
		Long parentId = null;
		
		OrganizationalGroup parentOrganizationalGroup = organizationalGroup.getParentOrgGroup();
		if (parentOrganizationalGroup != null) {
			parentId = organizationalGroup.getParentOrgGroup().getId();
		} else {
			parentId = organizationalGroup.getId();
		}
		
		String stringParentId = parentId.toString();
		BigInteger bigIntParentId = new BigInteger( stringParentId );
		
		return getResponseOrganizationalGroup(bigIntId, orgGroupName, parentOrgGroupHierarchy, bigIntParentId, "0", "");
		
	}
	
	public ResponseOrganizationalGroup getOrgGroupIdByNameResponse(OrganizationalGroup organizationalGroup) throws Exception {
		return getOrgGroupByIdResponse(organizationalGroup);
	}
	
	public Map<String, Object> validateDeleteOrganizationGroupRequest(String orgGroupIdArray[], Customer customer) throws Exception {
		
		OrganizationalGroup rootOrgGroup=orgGroupLearnerGroupService.getRootOrgGroupForCustomer(customer.getId());
		
		Map<String, Object> deleteIdsMap = new LinkedHashMap<String, Object>();
		Map<String, String> idsErrorMap = new LinkedHashMap<String, String>();
		List<Long> validIdsList = new ArrayList<Long>();
		
		for(int index =0; index<orgGroupIdArray.length;index++){
			String id = orgGroupIdArray[index];
			try {
				 Long orgGroupId = Long.parseLong(id);
				 isIdValidForDelete(orgGroupId, rootOrgGroup);
				 validIdsList.add(orgGroupId);
			} catch (Exception e) {
				String errorMessage = e.getMessage();
				idsErrorMap.put(id, errorMessage);
			}
		}
		
		if (!validIdsList.isEmpty()) {
			
			String[] validIds = new String[validIdsList.size()];
			for (int i=0; i<validIdsList.size(); i++) {
				validIds[i] = validIdsList.get(i).toString();
			}
			
			List<OrganizationalGroup> organizationalGroups = orgGroupLearnerGroupService.getOrgGroupsById(validIds);
			
			if (validIdsList.size() != organizationalGroups.size()) {
			    // remove not found ids from validId list
			}
			
			for(OrganizationalGroup organizationalGroup : organizationalGroups){
				Long orgGroupId = organizationalGroup.getId();
				try {
					isOrgGroupValidForDelete(organizationalGroup);
					if (!validIdsList.contains(orgGroupId)) {
						validIdsList.add(orgGroupId);
					}
				} catch (Exception e) {
					String errorMessage = e.getMessage();
					validIdsList.remove(orgGroupId);
					idsErrorMap.put(String.valueOf(orgGroupId), errorMessage);
				}
			}
			
		}
		
		if (!validIdsList.isEmpty()) {
			deleteIdsMap.put("validIdsList", validIdsList);
		}
		
		if (!idsErrorMap.isEmpty()) {
			deleteIdsMap.put("idsErrorMap", idsErrorMap);
		}
		
		return deleteIdsMap;
		
	}
	
	public Map<String, Object> processDeleteIdsMap(Map<String, Object> deleteIdsMap) throws Exception {
		
		List<Long> validIdsList = (List<Long>)deleteIdsMap.get("validIdsList");
		List<Long> deletedIdsList = new ArrayList<Long>();
		
		if (validIdsList != null) {
			
			long orgGroupIdArray[] = new long[validIdsList.size()];
			
			for (int i=0; i<validIdsList.size(); i++) {
				orgGroupIdArray[i] = validIdsList.get(i);
			}
		
			orgGroupLearnerGroupService.deleteOrgGroups(orgGroupIdArray);
			
			for (int i=0; i<orgGroupIdArray.length; i++) {
				deletedIdsList.add(orgGroupIdArray[i]);
			}
		}
		
		
		if (!deletedIdsList.isEmpty()) {
			deleteIdsMap.put("deletedIdsList", deletedIdsList);
		}
		
		return deleteIdsMap;
	}
	
	public Map<String, Object> processDeleteIdsMapForResponse(Map<String, Object> deleteIdsMap) throws Exception {
		
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Long> deletedIdsList = (List<Long>)deleteIdsMap.get("deletedIdsList");
		Map<String, String> idsErrorMap = (Map<String, String>)deleteIdsMap.get("idsErrorMap");
		
		if (idsErrorMap != null) {
			InvalidOrganizationalGroups invalidOrganizationalGroups = processIdsErrorMap(idsErrorMap);
			responseMap.put("InvalidOrganizationalGroups", invalidOrganizationalGroups);
		}
		
		if (deletedIdsList != null) {
			
			List<BigInteger> bigIntIdList = new ArrayList<BigInteger>();
			
			for (Long deletedId : deletedIdsList) {
				
				// Convert Long to String.
				String stringId = deletedId.toString();
				BigInteger bigIntId = new BigInteger( stringId );
				bigIntIdList.add(bigIntId);
			}
			
			DeletedOrganizationalGroup deletedOrganizationalGroup = new DeletedOrganizationalGroup();
			deletedOrganizationalGroup.setId(bigIntIdList);
			deletedOrganizationalGroup.setErrorCode("0");
			deletedOrganizationalGroup.setErrorMessage("");
			
			responseMap.put("deletedOrganizationalGroup", deletedOrganizationalGroup);
			
		}
		
		return responseMap;
		
	}
	
	private InvalidOrganizationalGroups processIdsErrorMap(Map<String, String> idsErrorMap) {
		
		InvalidOrganizationalGroups invalidOrganizationalGroups = new InvalidOrganizationalGroups();
		
		List<InvalidOrgGroupValueWithError> invalidOrganizationalGroupList = new ArrayList<InvalidOrgGroupValueWithError>();
		
		for (Map.Entry<String, String> idsErrorMapEntry : idsErrorMap.entrySet()) {
			String id = idsErrorMapEntry.getKey();
			String errorMessage = idsErrorMapEntry.getValue();
			
			InvalidOrgGroupValueWithError InvalidOrgGroup = new InvalidOrgGroupValueWithError();
			InvalidOrgGroup.setId(new BigInteger(id));
			InvalidOrgGroup.setErrorMessage(errorMessage);
			
			invalidOrganizationalGroupList.add(InvalidOrgGroup);
			
		}
		
		invalidOrganizationalGroups.setInvalidOrganizationalGroup(invalidOrganizationalGroupList);
		return invalidOrganizationalGroups;
		
	}
	
	private boolean isIdValidForDelete(long orgGroupId, OrganizationalGroup rootOrgGroup) throws Exception {
		
		String errorMessage = null;
		
		if(rootOrgGroup.getId() == orgGroupId){
			errorMessage = "You cannot delete the root Organization Group";
			throwException(errorMessage);
		}
		
		return true;
	}
	
	private boolean isOrgGroupValidForDelete(OrganizationalGroup organizationalGroup) throws Exception {
		
		String errorMessage = null;
		
		List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByOrgGroup(organizationalGroup.getId());
		if(CollectionUtils.isNotEmpty(learnerGroups)){
			
			if(CollectionUtils.isNotEmpty(learnerGroups)){
				errorMessage = "User Group(s) exist for the selected Organization Group(s)";
				throwException(errorMessage);
			}
			
			if(organizationalGroup.getChildrenOrgGroups() != null && organizationalGroup.getChildrenOrgGroups().size() > 0 )
				errorMessage = "Children Group(s) exist for the selected Organization Group(s)";
				throwException(errorMessage);
			}
		
			List<Learner> learners = orgGroupLearnerGroupService.getLearnersByOrgGroupId(organizationalGroup.getId());
			if(CollectionUtils.isNotEmpty(learners)){
				errorMessage = "Learner(s) exist for the selected Organization Group(s)";
				throwException(errorMessage);
			}
			
			if( entitlementService.getOrgGroupEntitlementByOrgGroupId(organizationalGroup.getId()) != null){
				errorMessage = "Org Group Entitlements(s) exist for the selected Organization Group(s)";
				throwException(errorMessage);
			}
			
		return true;
			
	}
		
	private ResponseOrganizationalGroup getResponseOrganizationalGroup(BigInteger id, String orgGroupName, 
			String parentOrgGroupHierarchy, BigInteger parentId, String errorCode, String errorMessage) {
		
		ResponseOrganizationalGroup responseOrganizationalGroup = new ResponseOrganizationalGroup();
		responseOrganizationalGroup.setId(id);
		responseOrganizationalGroup.setName(orgGroupName);
		responseOrganizationalGroup.setParentOrgGroupHierarchy(parentOrgGroupHierarchy);
		responseOrganizationalGroup.setParentId(parentId);
		responseOrganizationalGroup.setErrorCode(errorCode);
		responseOrganizationalGroup.setErrorMessage(errorMessage);
		
		return responseOrganizationalGroup;
		
	}
	
}
