package com.softech.vu360.lms.service;

import java.util.HashSet;
import java.util.List;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;

/**
 * SecurityAndRolesService defines the set of interfaces 
 * to control the interactions and business logic
 * of configuring the security and roles within the LMS.
 * 
 * @author jason
 *
 */
public interface SecurityAndRolesService {
		
	public List<LMSRoleLMSFeature> getUniqueAvailablePermissionByRoleType(Customer customer,String roleType);
	public List<LMSRole> getUniqueRolesByCustomer(Customer customer);
	public LMSRole getLearnerRoleTemplateByCustomer(Customer customer);
	
	/**
	 * DEPRECATED by S M Humayun | 8 Apr 2011 
	 * @param distributor
	 * @param roleType
	 * @return
	 * @deprecated
	 * @see #getDistributorLMSFeatures(Distributor, String)
	 */
	public List<LMSRoleLMSFeature> getDefaultPermissionByDistributor(
			Distributor distributor,String roleType);
	
	public HashSet<String> getTemplateTypesByDistributor(Distributor distritbutor);
	public LMSRole getFicticousRoleForLoginAsManager(Customer c);
	public List<LMSRoleLMSFeature> getPermissionByDistributor(Distributor distributor,String roleType);

	/**
	 * Returns {@link List} of {@link CustomerLMSFeature} (enabled or disabled) 
	 * defined against given <code>customer</code> and for given 
	 * <code>roleType</code>
	 * @param customer
	 * @param roleType
	 * @return {@link List} of {@link CustomerLMSFeature}
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public List<CustomerLMSFeature> getCustomerLMSFeatures (
			Customer customer, String roleType);
	
	/**
	 * Returns {@link List} of disabled {@link CustomerLMSFeature}
	 * @param customer
	 * @return {@link List} of disabled {@link CustomerLMSFeature}
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public List<CustomerLMSFeature> getDisabledCustomerLMSFeatures (
			Long customerId);

	/**
	 * Returns {@link List} of all {@link DistributorLMSFeature} (enabled or 
	 * disabled) defined against given <code>distributor</code>
	 * @param distributor
	 * @return {@link List} of all {@link DistributorLMSFeature}
	 * @author sm.humayun
	 * @since 4.16.3 {LMS-11041}
	 */
	public List<DistributorLMSFeature> getAllDistributorLMSFeatures (Distributor distributor);

	/**
	 * Returns {@link List} of {@link DistributorLMSFeature} (enabled or 
	 * disabled) defined against given <code>distributor</code> and for given 
	 * <code>roleType</code>
	 * @param distributor
	 * @param roleType
	 * @return {@link List} of {@link DistributorLMSFeature}
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public List<DistributorLMSFeature> getDistributorLMSFeatures (
			Distributor distributor, String roleType);

	/**
	 * Returns {@link List} of enabled {@link DistributorLMSFeature} defined 
	 * against given <code>distributor</code>
	 * @param distributor
	 * @return {@link List} of enabled {@link DistributorLMSFeature}
	 * @author sm.humayun
	 * @since 4.13 {LMS-10055}
	 */
	public List<DistributorLMSFeature> getEnabledDistributorLMSFeatures (
			Distributor distributor);

	/**
	 * Returns {@link List} of disabled {@link DistributorLMSFeature}
	 * @param distributor
	 * @return {@link List} of disabled {@link DistributorLMSFeature}
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public List<DistributorLMSFeature> getDisabledDistributorLMSFeatures (
			Long distributorId);

	/**
	 * Returns {@link List} of missing {@link LMSFeature}
	 * @param customer, roleType
	 * @return {@link List} of missing {@link LMSFeature}
	 * @author muhammad.rehan
	 * @since 4.25.3 {LMS-15470}
	 */
	public List<LMSFeature> getMissingCustomerLMSFeatures (Customer customer, String roleType);

	public LMSRole addSystemInstructorRoleIfNotExist(Customer customer);
	public List<LMSRoleLMSFeature> getPermissionByCustomer(Customer customer,String roleType);
	LMSRoleLMSFeature loadForUpdateLMSRoleLMSFeature(long id);
	public void saveCustomerLMSFeature (LMSRoleLMSFeature lmsRoleLMSFeature);
	public CustomerLMSFeature getCustomerLMSFeatureByFeatureCode(Long customerID,String featureCode);
	public DistributorLMSFeature getDistributorLMSFeatureByFeatureCode(Long distributorID,String featureCode);
	
	/*
	 * This function will return all LMSRoleLMSFeature for a user, customer and role type 
	 */
	List<LMSRoleLMSFeature> findLMSRoleLMSFeatureByUser(Long loggedInUserId, Long customerId, String roleType);

	public List<CustomerLMSFeature> getAllCustomerLMSFeatures (Customer customer);
	public List<CustomerLMSFeature> getAllByCustomerAndEnabledTrue(Customer customer);
	public List<String> findDistinctEnabledFeatureFeatureGroupsForDistributorAndCustomer(Long distributorId, Long customerId);
	
	List<LMSFeature> findAllActiveLMSFeaturesByUser(Long loggedInUserId, Long customerId, String roleType);
	
	String getAnyEnabledFeatureCodeInDisplayOrderByRoleType(Long userId, String roleType,
			List<String> disabledFeatureCode);
	
}
