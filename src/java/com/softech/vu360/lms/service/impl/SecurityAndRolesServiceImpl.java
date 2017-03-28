package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.repositories.CustomerLMSFeatureRepository;
import com.softech.vu360.lms.repositories.DistributorLMSFeatureRepository;
import com.softech.vu360.lms.repositories.LMSFeatureRepository;
import com.softech.vu360.lms.repositories.LMSRoleLMSFeatureRepository;
import com.softech.vu360.lms.repositories.LMSRoleRepository;
import com.softech.vu360.lms.service.SecurityAndRolesService;


public class SecurityAndRolesServiceImpl implements SecurityAndRolesService {

	@Inject
	private LMSRoleLMSFeatureRepository lmsRoleLmsFeatureRepository;
	@Inject
	private DistributorLMSFeatureRepository distributorLmsFeatureRepository;
	@Inject
	private CustomerLMSFeatureRepository customerLmsFeatureRepository;
	@Inject
	private LMSRoleRepository lmsRoleRepository;
	@Inject
	private LMSFeatureRepository lmsFeatureRepository;

	
	public List<LMSRoleLMSFeature> getUniqueAvailablePermissionByRoleType(Customer customer,String roleType)
	{
		List<LMSRoleLMSFeature> permissions=lmsRoleLmsFeatureRepository.getAvailablePermissionsByRoleType(customer, roleType);
		List<LMSRoleLMSFeature> uniquePermissions=new ArrayList<LMSRoleLMSFeature>();
		HashMap<Long,LMSRoleLMSFeature> permissionMap=new HashMap<Long,LMSRoleLMSFeature>();
		LMSFeature feature=null;
		for(LMSRoleLMSFeature permission:permissions)
		{
			feature=permission.getLmsFeature();
			if(permissionMap.get(feature.getId())==null)
			{
				uniquePermissions.add(permission);
				permissionMap.put(feature.getId(), permission);
			}
		}
		return uniquePermissions;
	}
	public LMSRole getFicticousRoleForLoginAsManager(Customer c){
		Distributor d=c.getDistributor();
		List<LMSRoleLMSFeature> permissions=this.getDefaultPermissionByDistributor(d, LMSRole.ROLE_TRAININGMANAGER);
		LMSRole fictitousRole=new LMSRole();
		fictitousRole.setRoleName("Fictitous Role For Login As Manager");
		fictitousRole.setRoleType(LMSRole.ROLE_TRAININGMANAGER);
		fictitousRole.setDefaultForRegistration(false);
		fictitousRole.setLmsPermissions(permissions);
		fictitousRole.setOwner(c);
		fictitousRole.setSystemCreated(true);
		return fictitousRole;
	}
	
	/**
	 * DEPRECATED by S M Humayun | 8 Apr 2011 
	 * @autho Faisal A. Siddiqui
	 * @param Distributor distributor
	 * @param String roleType type of role you want to retrieve default permission of
	 * @return List<LMSRoleLMSFeature> list of enabled permission specified by given parameters
	 * @deprecated Use {@link #getDistributorLMSFeatures(Distributor, String)}
	 */
	public List<LMSRoleLMSFeature> getDefaultPermissionByDistributor(Distributor distributor,String roleType){
		List<DistributorLMSFeature> defaultEnabledPermissions = distributorLmsFeatureRepository.getAllByDistributorAndLmsFeature_RoleType(distributor, roleType);
		List<LMSRoleLMSFeature> lmsPermissions= new ArrayList<LMSRoleLMSFeature>();
		
		if(defaultEnabledPermissions!=null){
			for (DistributorLMSFeature permission: defaultEnabledPermissions){
				if(permission.getEnabled()){
					LMSRoleLMSFeature lmspermission=new LMSRoleLMSFeature();
					//lmspermission.setId(permission.getId());
					lmspermission.setLmsFeature(permission.getLmsFeature());
					lmspermission.setEnabled(permission.getEnabled());
					lmsPermissions.add(lmspermission);
				}
			}
		}
		return lmsPermissions;

	}
	
	public List<LMSRoleLMSFeature> getPermissionByDistributor(Distributor distributor,String roleType){
		List<DistributorLMSFeature> defaultEnabledPermissions = distributorLmsFeatureRepository.getAllByDistributorAndLmsFeature_RoleType(distributor, roleType);
		List<LMSRoleLMSFeature> lmsPermissions= new ArrayList<LMSRoleLMSFeature>();
		
		if(defaultEnabledPermissions!=null){
			for (DistributorLMSFeature permission: defaultEnabledPermissions){
					LMSRoleLMSFeature lmspermission=new LMSRoleLMSFeature();
					lmspermission.setId(permission.getId());
					lmspermission.setLmsFeature(permission.getLmsFeature());
					lmspermission.setEnabled(permission.getEnabled());
					lmsPermissions.add(lmspermission);
				
			}
		}
		return lmsPermissions;

	}
	
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
	@Override
	public List<CustomerLMSFeature> getCustomerLMSFeatures (
			Customer customer, String roleType)
	{
		return customerLmsFeatureRepository.findByCustomerAndLmsFeature_RoleTypeOrderByLmsFeature_FeatureCode(customer, roleType);
	}

	/**
	 * Returns {@link List} of disabled {@link CustomerLMSFeature}
	 * @param customer
	 * @return {@link List} of disabled {@link CustomerLMSFeature}
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	@Override
	public List<CustomerLMSFeature> getDisabledCustomerLMSFeatures (
			Long customerId)	
	{
		return customerLmsFeatureRepository.findByCustomerAndEnabledFalse(customerId);
	}

	/**
	 * Returns {@link List} of all {@link DistributorLMSFeature} (enabled or 
	 * disabled) defined against given <code>distributor</code>
	 * @param distributor
	 * @return {@link List} of all {@link DistributorLMSFeature}
	 * @author sm.humayun
	 * @since 4.16.3 {LMS-11041}
	 */
	@Override
	public List<DistributorLMSFeature> getAllDistributorLMSFeatures (Distributor distributor)
	{
		return distributorLmsFeatureRepository.getAllByDistributor(distributor);
	}

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
	@Override
	public List<DistributorLMSFeature> getDistributorLMSFeatures (
			Distributor distributor, String roleType)
	{
		return distributorLmsFeatureRepository.getAllByDistributorAndLmsFeature_RoleType(distributor
				, roleType);
	}

	/**
	 * Returns {@link List} of enabled {@link DistributorLMSFeature} defined 
	 * against given <code>distributor</code>
	 * @param distributor
	 * @return {@link List} of enabled {@link DistributorLMSFeature}
	 * @author sm.humayun
	 * @since 4.13 {LMS-10055}
	 */
	@Override
	public List<DistributorLMSFeature> getEnabledDistributorLMSFeatures (
			Distributor distributor)
	{
		return distributorLmsFeatureRepository.getAllByDistributorAndEnabledTrue(
				distributor);
	}
	
	/**
	 * Returns {@link List} of disabled {@link DistributorLMSFeature}
	 * @param distributor
	 * @return {@link List} of disabled {@link DistributorLMSFeature}
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	@Override
	public List<DistributorLMSFeature> getDisabledDistributorLMSFeatures (
			Long distributorId)
	{
		return distributorLmsFeatureRepository.getAllByDistributorAndEnabledFalse(
				distributorId);
	}
	
	/**
	 * Returns {@link List} of missing {@link LMSFeature}
	 * @param customer, roleType
	 * @return {@link List} of missing {@link LMSFeature}
	 * @author muhammad.rehan
	 * @since 4.25.3 {LMS-15470}
	 */
	public List<LMSFeature> getMissingCustomerLMSFeatures (Customer customer, String roleType){
		Long [] availableCustomerFeature;
		List<CustomerLMSFeature> customerLMSFeature = this.getCustomerLMSFeatures(customer, roleType);
		
		if(customerLMSFeature!=null && customerLMSFeature.size()>0){
			availableCustomerFeature = new Long[customerLMSFeature.size()];
			int count = 0;
			for (CustomerLMSFeature customerLMSFeat : customerLMSFeature){
				availableCustomerFeature[count]= customerLMSFeat.getLmsFeature().getId();
				count=count+1;
			}
		}else{
			availableCustomerFeature = new Long[0];
			availableCustomerFeature[0]= new Long(0);
			
		}
		List<Long> availableCustomerFeatureList=Arrays.asList(availableCustomerFeature);
		return lmsFeatureRepository.findByRoleTypeAndIdNotIn(roleType, availableCustomerFeatureList);
	}

	public List<LMSRole> getUniqueRolesByCustomer(Customer customer)
	{
		List<LMSRole> roles=lmsRoleRepository.getByOwner(customer);
		List<LMSRole> uniqueRoles=new ArrayList<LMSRole>();
		HashMap<String,LMSRole> roleMap=new HashMap<String,LMSRole>();
		for(LMSRole role:roles)
		{
			if(roleMap.get(role.getRoleType())==null)
			{
				uniqueRoles.add(role);
				roleMap.put(role.getRoleType(), role);
			}
			
		}
		return uniqueRoles;
	}

	public LMSRole getLearnerRoleTemplateByCustomer(Customer customer){
		return lmsRoleRepository.findByOwnerAndRoleTypeAndIsSystemCreatedTrue(customer, LMSRole.ROLE_LEARNER);
	}
	public HashSet<String> getTemplateTypesByDistributor(Distributor distritbutor){
		List<DistributorLMSFeature> distributorLMSFeatures=distributorLmsFeatureRepository.getAllByDistributor(distritbutor);
		HashSet<String> roleTypes=new HashSet<String>();
		for(DistributorLMSFeature distributorLMSFeature:distributorLMSFeatures){
			if(distributorLMSFeature.getEnabled().booleanValue())
				roleTypes.add(distributorLMSFeature.getLmsFeature().getRoleType());
		}
		return roleTypes;
	}
	/*
	 * This method will create instructor role by the INSTRUCTOR ROLE template if specified by distributor. else INSTRUCTOR role
	 * will be created with all permissions available in system. 
	 */
	public LMSRole addSystemInstructorRoleIfNotExist(Customer customer){
		LMSRole role = lmsRoleRepository.findByOwnerAndRoleTypeAndIsSystemCreatedTrue(customer, LMSRole.ROLE_INSTRUCTOR);
		if(role == null){
			role = this.addSystemInstructorRoleByTemplate(customer);
		}
		return role;
	}
	/*
	 * This method will be exposed to external classes or mark public as and when required. currently its supposed to be used only by 
	 * addSystemInstructorRoleIfNotExist method
	 */
	private LMSRole addSystemInstructorRoleByTemplate(Customer customer){
		LMSRole instructorRole = new LMSRole();
		instructorRole.setOwner(customer);
		instructorRole.setRoleName("INSTRUCTOR"); // TODO in future we can also parameterized it or brandable
		instructorRole.setRoleType(LMSRole.ROLE_INSTRUCTOR);
		instructorRole.setDefaultForRegistration(true);
		instructorRole.setSystemCreated(true);
		
		List<LMSRoleLMSFeature> permissions = lmsRoleLmsFeatureRepository.getAllPermissionsByRoleType(customer, LMSRole.ROLE_INSTRUCTOR);
		
		
		if(CollectionUtils.isEmpty(permissions)){
			permissions = getCustomerFeaturesByRole(customer, LMSRole.ROLE_INSTRUCTOR);
		}
		if(CollectionUtils.isEmpty(permissions)){//if still empty
			permissions = getSystemPermissionsByRoleType(LMSRole.ROLE_INSTRUCTOR);
		}
		
		for(LMSRoleLMSFeature permission:permissions){
			permission.setLmsRole(instructorRole);
		}
		instructorRole.setLmsPermissions(permissions);
		return lmsRoleRepository.save(instructorRole);

	}
	/*
	 * This utility method transforms DefaultPermissions specified by distributor into LMSRoleLMSFeature 
	 */
	private List<LMSRoleLMSFeature> getSystemPermissionsByRoleType(String roleType){
		List<LMSRoleLMSFeature> lmsPermissions = new ArrayList<LMSRoleLMSFeature>();	
		List<LMSFeature> 	lmsFeatures =  lmsFeatureRepository.findByRoleType(roleType);
		for (LMSFeature lmsFeature: lmsFeatures){
			LMSRoleLMSFeature lmsPermission= new LMSRoleLMSFeature();
			lmsPermission.setLmsFeature(lmsFeature);
			lmsPermission.setEnabled(true);
			lmsPermissions.add(lmsPermission);
		}
		return lmsPermissions;
	}
	
	private List<LMSRoleLMSFeature> getCustomerFeaturesByRole(Customer customer, String roleType){
		List<LMSRoleLMSFeature> lmsPermissions = new ArrayList<LMSRoleLMSFeature>();	
		List<CustomerLMSFeature> 	lmsFeatures =  customerLmsFeatureRepository.findByCustomerAndLmsFeature_RoleTypeOrderByLmsFeature_FeatureCode(customer, roleType);
		for (CustomerLMSFeature lmsFeature: lmsFeatures){
			LMSRoleLMSFeature lmsPermission= new LMSRoleLMSFeature();
			
			lmsPermission.setLmsFeature(lmsFeature.getLmsFeature());
			lmsPermission.setEnabled(lmsFeature.getEnabled());
			lmsPermissions.add(lmsPermission);
		}	
		
		return lmsPermissions;
	}
	@Override
	public List<LMSRoleLMSFeature> getPermissionByCustomer(Customer customer,String roleType){
		List<LMSRoleLMSFeature> permissions = lmsRoleLmsFeatureRepository.getAllPermissionsByRoleType(customer, roleType);
		return permissions;
	}
	
	@Override
	public LMSRoleLMSFeature loadForUpdateLMSRoleLMSFeature(long id){
		return lmsRoleLmsFeatureRepository.findOne(id);
	}
	
	@Override
	public void saveCustomerLMSFeature(LMSRoleLMSFeature lmsRoleLMSFeature) {
		lmsRoleLmsFeatureRepository.save(lmsRoleLMSFeature);
		
	}
	
	@Override
	public List<LMSRoleLMSFeature> findLMSRoleLMSFeatureByUser(Long loggedInUserId, Long customerId, String roleType) {
		return lmsRoleLmsFeatureRepository.findLMSRoleLMSFeatureByUser(loggedInUserId, customerId, roleType);
	}
	@Override
	public List<CustomerLMSFeature> getAllCustomerLMSFeatures(Customer customer) {
		return customerLmsFeatureRepository.findByCustomer(customer);
	}
	@Override
	public List<CustomerLMSFeature> getAllByCustomerAndEnabledTrue(Customer customer) {
		return customerLmsFeatureRepository.getAllByCustomerAndEnabledTrue(customer);
	}
	@Override
	public Map<String, String> findDistinctEnabledFeatureFeatureGroupsForDistributorAndCustomer(Long distributorId,
			Long customerId) {
		return lmsRoleRepository.findDistinctEnabledFeatureFeatureGroupsForDistributorAndCustomer(distributorId, customerId);
	}
	@Override
	public List<LMSFeature> findAllActiveLMSFeaturesByUser(Long loggedInUserId, Long customerId, String roleType) {
		return lmsFeatureRepository.findAllActiveLMSFeaturesByUser(loggedInUserId, customerId, roleType);
	}
	@Override
	public String getAnyEnabledFeatureCodeInDisplayOrderByRoleType(Long userId, String roleType, List<String> disabledFeatureCode) {
		return lmsFeatureRepository.getAnyEnabledFeatureCodeInDisplayOrderByRoleType(userId, roleType, disabledFeatureCode);
	}
	@Override
	public CustomerLMSFeature getCustomerLMSFeatureByFeatureCode(Long customerID,String featureCode){
	    return customerLmsFeatureRepository.findByLmsFeatureFeatureCodeEqualsAndCustomerIdEquals(featureCode,customerID);
	}
	@Override
	public DistributorLMSFeature getDistributorLMSFeatureByFeatureCode(Long distributorID, String featureCode) {
		return distributorLmsFeatureRepository.findByLmsFeatureFeatureCodeEqualsAndDistributorIdEquals(featureCode, distributorID);
	}
}
