package com.softech.vu360.lms.service.lmsapi;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;

public interface LmsApiOrgGroupService {

	Map<String, OrganizationalGroup> getOrgGroupsMap(Customer customer, VU360User manager, List<String> orgGroupHierarchies);
	OrganizationalGroup createOrganizationalGroup(Customer customer, VU360User manager, String orgGroupHierarchy) throws Exception;
	OrganizationalGroup getExistingOrganizationalGroup(VU360User manager, String orgGroupHierarchy);
	OrganizationalGroup getRootOrganizationalGroup(Customer customer);
	String getRootOrgGroupName(String orgGroupHierarchy);
	Map<String, OrganizationalGroup> getAllOrganizationalGroups(Customer customer);
	OrganizationalGroup createMissingOrgGroup(VU360User manager, Customer customer, OrganizationalGroup rootOrganizationalGroup, OrganizationalGroup orgGroupMatchWithOrgGroupHierarchy, String orgGroupHierarchy, List<String> orgGroupNames) throws Exception;
	OrganizationalGroup getOrgGroupMatchWithOrgGroupHierarchy(String orgGroupHierarchy, Map<String, OrganizationalGroup> allOrganizationalGroups);
	List<String> getMissingOrgGroupNames(OrganizationalGroup orgGroupMatchWithOrgGroupHierarchy, String orgGroupHierarchy);
		
}
