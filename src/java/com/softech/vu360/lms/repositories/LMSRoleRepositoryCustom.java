package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.VU360User;

public interface LMSRoleRepositoryCustom {


	/*@kaunain - moved from VU360User - in a separate Interface*/
	public LMSRole  getDefaultRole(Customer customer);
	public LMSRole  getOptimizedBatchImportLearnerDefaultRole(Customer customer);
	public List<LMSRole>  getAllRoles(Customer customer,VU360User loggedInUser);
	public List<LMSRole> getLMSRolesByUserById(long id);
	public List<LMSRole> getSystemRolesByCustomer(Customer customer);
	public List<LMSRole> findRolesByName( String name, Customer customer, VU360User loggedInUser);
	public int checkNoOfBefaultReg(Customer customer);
	public Map<String, String> findDistinctEnabledFeatureFeatureGroupsForDistributorAndCustomer(Long distributorId, Long customerId);
	Map<String, String> countLearnerByRoles(Long [] roleIds);
}
