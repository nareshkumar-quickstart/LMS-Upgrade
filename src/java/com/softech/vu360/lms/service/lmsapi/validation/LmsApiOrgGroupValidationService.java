package com.softech.vu360.lms.service.lmsapi.validation;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.webservice.message.lmsapi.types.orggroup.OrganizationalGroups;

public interface LmsApiOrgGroupValidationService {

	Map<Boolean, List<String>> getOrgGroupHierarchyMap(VU360User manager, OrganizationalGroups organizationalGroups);
	List<String> getValidOrgGroupHierarchies(Map<Boolean, List<String>> orgGroupHierarchyMap);
	Map<String, String> getInvalidOrgGroupHierarchies(VU360User manager, String customerCode, Map<Boolean, List<String>> orgGroupHierarchyMap);
	List<OrganizationalGroup> getValidOrgGroups(Map<String, OrganizationalGroup> orgGroups);
	Map<String,String> getInvalidOrgGroups(Customer customer, VU360User manager, Map<String, OrganizationalGroup> orgGroups);
	
}
