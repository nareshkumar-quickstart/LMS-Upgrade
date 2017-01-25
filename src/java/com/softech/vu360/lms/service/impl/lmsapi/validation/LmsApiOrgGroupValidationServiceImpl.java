package com.softech.vu360.lms.service.impl.lmsapi.validation;

import static com.softech.vu360.lms.webservice.validation.lmsapi.LmsApiOrganizationalGroupsPredicate.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.lmsapi.LmsApiOrgGroupService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiOrgGroupValidationService;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;

@Service
public class LmsApiOrgGroupValidationServiceImpl implements LmsApiOrgGroupValidationService {

	@Autowired
	private LmsApiOrgGroupService lmsApiOrgGroupService;
	
	public void setLmsApiOrgGroupService(LmsApiOrgGroupService lmsApiOrgGroupService) {
		this.lmsApiOrgGroupService = lmsApiOrgGroupService;
	}

	@Override
	public Map<Boolean, List<String>> getOrgGroupHierarchyMap(VU360User manager, OrganizationalGroups organizationalGroups) {
		
		List<String> validOrgGroupHierarchies = new ArrayList<>();
		List<String> invalidOrgGroupHierarchies = new ArrayList<>();
		Map<Boolean, List<String>> orgGroupHierarchyMap = new LinkedHashMap<>();
		
		if (isValidOrgGroupHierarchy(organizationalGroups)) {
			List<String> orgGroupHierarchies = organizationalGroups.getOrgGroupHierarchy();
			for (String orgGroupHierarchy: orgGroupHierarchies) {
				if (isManagerHasSecurityRightsForOrganization(manager, orgGroupHierarchy)) {
					validOrgGroupHierarchies.add(orgGroupHierarchy);
				} else {
					invalidOrgGroupHierarchies.add(orgGroupHierarchy);
				}
			}
		}
		
		if (!CollectionUtils.isEmpty(validOrgGroupHierarchies)) {
			orgGroupHierarchyMap.put(Boolean.TRUE, validOrgGroupHierarchies);
		}
		
		if (!CollectionUtils.isEmpty(invalidOrgGroupHierarchies)) {
			orgGroupHierarchyMap.put(Boolean.FALSE, invalidOrgGroupHierarchies);
		}
		
		return orgGroupHierarchyMap;
	}

	@Override
	public List<String> getValidOrgGroupHierarchies(Map<Boolean, List<String>> orgGroupHierarchyMap) {
		return getOrgGroupHierarchies(orgGroupHierarchyMap, Boolean.TRUE);
	}
	
	@Override
	public Map<String, String> getInvalidOrgGroupHierarchies(VU360User manager, String customerCode, Map<Boolean, List<String>> orgGroupHierarchyMap) {
		Map<String, String> invalidOrgGroupHierarchiesMap = null;
		List<String> invalidOrgGroupHierarchies = getOrgGroupHierarchies(orgGroupHierarchyMap, Boolean.FALSE);
		if (!CollectionUtils.isEmpty(invalidOrgGroupHierarchies)) {
			invalidOrgGroupHierarchiesMap = new HashMap<>();
			for (String orgGroupHierarchy: invalidOrgGroupHierarchies) {
				if (!isValidOrgGroupHierarchy(orgGroupHierarchy)) {
					invalidOrgGroupHierarchiesMap.put(orgGroupHierarchy, "OrgGroupHierarchy can not be empty or blank");
				} else if (!isManagerHasSecurityRightsForOrganization(manager, orgGroupHierarchy)) {
					invalidOrgGroupHierarchiesMap.put(orgGroupHierarchy, "Manager for customer: " + customerCode + " do not have rigths to create organizational group: " + orgGroupHierarchy);
				}
			}
			
		}
		return invalidOrgGroupHierarchiesMap;
	}
	
	@Override
	public List<OrganizationalGroup> getValidOrgGroups(Map<String, OrganizationalGroup> orgGroupsMap) {
		List<OrganizationalGroup> orgGroups = null;
		if (!CollectionUtils.isEmpty(orgGroupsMap)) {
			orgGroups = new ArrayList<>();
			for (Map.Entry<String, OrganizationalGroup> entry : orgGroupsMap.entrySet()) {
				OrganizationalGroup orgGroup = entry.getValue();
				if (orgGroup != null) {
					orgGroups.add(orgGroup);
				}
			}
		}
		return orgGroups;
	}
	
	@Override
	public Map<String, String> getInvalidOrgGroups(Customer customer, VU360User manager, Map<String, OrganizationalGroup> orgGroups) {
		Map<String, String> invalidOrgGroups = null;
		if (!CollectionUtils.isEmpty(orgGroups)) {
			invalidOrgGroups = new HashMap<>();
			for (Map.Entry<String, OrganizationalGroup> entry : orgGroups.entrySet()) {
				String orgGroupHierarchy = entry.getKey();
				OrganizationalGroup orgGroup = entry.getValue();
				if (orgGroup == null) {
					OrganizationalGroup rootOrganizationalGroup = lmsApiOrgGroupService.getRootOrganizationalGroup(customer);
					String rootGroupName = lmsApiOrgGroupService.getRootOrgGroupName(orgGroupHierarchy);
					if (!isRootOrganizationalGroupExist(rootOrganizationalGroup, rootGroupName)) {
						invalidOrgGroups.put(orgGroupHierarchy, "Root Orgnization Group does not exists: " + rootGroupName);
					} else {
						Map<String, OrganizationalGroup> allOrganizationalGroups = lmsApiOrgGroupService.getAllOrganizationalGroups(customer);
						OrganizationalGroup orgGroupMatchWithOrgGroupHierarchy = lmsApiOrgGroupService.getOrgGroupMatchWithOrgGroupHierarchy(orgGroupHierarchy, allOrganizationalGroups);
						if(!isOrgGroupHierarchyMatchWithOrganizationalGroup(orgGroupHierarchy, orgGroupMatchWithOrgGroupHierarchy)) {
							if(!isManagerHasPermissionToCreateOrgGroup(manager) || !hasPermissionToCreateOrgGroup(customer, manager, orgGroupMatchWithOrgGroupHierarchy)) {
								invalidOrgGroups.put(orgGroupHierarchy, "No permissions to create org group: " + rootGroupName);
							}
						}
					}
				}
			}
		}
		return invalidOrgGroups;
	}

	private List<String> getOrgGroupHierarchies(Map<Boolean, List<String>> orgGroupHierarchyMap, Boolean key) {
		
		List<String> orgGroupHierarchies = null;
		if (!CollectionUtils.isEmpty(orgGroupHierarchyMap)) {
			orgGroupHierarchies = orgGroupHierarchyMap.get(key);
		}
	
		return orgGroupHierarchies;
		
	}

}
