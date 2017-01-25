package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.VU360User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LmsTestConfig.class)
public class LMSRoleRepositoryTest {

	@Inject
	private LMSRoleRepository lmsRoleRepository;
	@Inject
	private CustomerRepository customerRepository;
	@Inject
	private VU360UserRepository vu360UserRepository;
	@Inject
	private LMSFeatureRepository featureRepository;
/*	@Inject
	private LearnerService learnerService;
*/
	@Autowired
	ApplicationContext context;

	@Before
	public void CustomerRepositoryInit() {
		//learnerService = (LearnerService) context.getBean("learnerService");
	}

	// @Test
	public void getDefaultRole() {
		Customer customer = customerRepository.findOne(13l);
		LMSRole role = lmsRoleRepository.getDefaultRole(customer);
		System.out.println(role.getRoleName());
	}

//	@Test
	public void getDefaultRoleOPt() {
		Customer customer = customerRepository.findOne(13l);
		LMSRole role = lmsRoleRepository.findByOwnerIdAndIsSystemCreatedTrue(customer.getId());
		System.out.println(role.getRoleName());
	}

	
	// @Test
	public void getAllRoles() {
		VU360User user = vu360UserRepository.getUserById(13l);
		Customer customer = customerRepository.findOne(13l);
		List<LMSRole> roles = lmsRoleRepository.getAllRoles(customer, user);
		if (roles != null && roles.size() > 0) {
			for (LMSRole role : roles) {
				System.out.println(role.getRoleName());
			}
		}
	}

	// @Test
	public void getLMSRolesByVU360UserId() {

		List<LMSRole> lmsRole = lmsRoleRepository.getLMSRolesByUserById(13l);
		if (lmsRole != null && lmsRole.size() > 0) {
			for (LMSRole role : lmsRole) {
				System.out.println(role.getRoleName());
			}
		}

	}

	// @Test
	public void getSystemRolesByCustomer() {
		Customer customer = customerRepository.findOne(13l);
		List<LMSRole> lmsRole = lmsRoleRepository
				.getSystemRolesByCustomer(customer);

		if (lmsRole != null && lmsRole.size() > 0) {
			for (LMSRole role : lmsRole) {
				System.out.println(role.getRoleName());
			}
		}
	}

//	@Test
	public void findRolesByName() {
		String name = "dmi";
		Customer customer = customerRepository.findOne(3l);
		VU360User loggedInUser = vu360UserRepository.getUserById(3l);
		List<LMSRole> lmsRole = lmsRoleRepository.findRolesByName(name,
				customer, loggedInUser);
		if (lmsRole != null && lmsRole.size() > 0) {
			for (LMSRole role : lmsRole) {
				System.out.println(role.getRoleName());
			}
		}
	}

	// @Test
	public void findByRoleNameAndCustomer() {
		Customer customer = customerRepository.findOne(104l);
		String rolename = "MANAGER";
		LMSRole lmsRole = lmsRoleRepository.findByRoleNameAndOwner(rolename,
				customer);
		if (lmsRole != null) {
			lmsRole.getRoleType();
		}
	}

 @Test
	public void findByRoleTypeAndCustomer() {
		Customer customer = customerRepository.findOne(3L);
		String roleType = "ROLE_TRAININGADMINISTRATOR";
		List<LMSRole> lmsRole = lmsRoleRepository.findByRoleTypeAndOwnerId(
				roleType, customer.getId());
		if (lmsRole != null && lmsRole.size() > 0) {
			for (LMSRole role : lmsRole) {
				System.out.println(role.getRoleName());
			}

		}
	}

	// @Test
	public void checkNoOfBefaultReg() {
		Customer customer = customerRepository.findOne(5503l);
		int count = lmsRoleRepository.checkNoOfBefaultReg(customer);
		System.out.println(count);
	}

	// Added By Marium Saud
	// @Test
	// @org.springframework.transaction.annotation.Transactional
	public void LMSRole_should_add() {
		LMSRole objRole = new LMSRole();
		try {
			final String INSTRUCTOR_ROLE_TYPE = "ROLE_INSTRUCTOR";
			final String INSTRUCTOR_ROLE_NAME = "INSTRUCTOR";

			Customer customer = customerRepository.findOne(1L);

			List<LMSRoleLMSFeature> permissionList = null;

			List<LMSFeature> featureList = featureRepository
					.findByRoleType(INSTRUCTOR_ROLE_TYPE);

			if (featureList != null) {
				permissionList = new ArrayList<LMSRoleLMSFeature>();
				for (LMSFeature feature : featureList) {
					LMSRoleLMSFeature objPermission = new LMSRoleLMSFeature();
					objPermission.setLmsRole(objRole);
					objPermission.setLmsFeature(feature);
					objPermission.setEnabled(true);

					permissionList.add(objPermission);
				}
			}

			objRole.setRoleName(INSTRUCTOR_ROLE_NAME);
			objRole.setRoleType(INSTRUCTOR_ROLE_TYPE);
			objRole.setLmsPermissions(permissionList);
			objRole.setOwner(customer);
			objRole.setDefaultForRegistration(true);

			objRole = lmsRoleRepository.save(objRole);
			System.out.println("LMSRole : ID = " + objRole.getId()
					+ " Role Name = " + objRole.getRoleName());

		} catch (Exception e) {

		}
	}

	// @Test
	public void LMSRole_should_update() {
		LMSRole lmsRole = lmsRoleRepository.findOne(78122L);
		//learnerService.updateRole(lmsRole, lmsRole.getOwner());

	}

	//@Test
	public void getRoleByCustomer() {
		Customer customer = customerRepository.findOne(53l);
		List<LMSRole> lmsRoleList = lmsRoleRepository.getByOwner(customer);
		if (lmsRoleList != null && lmsRoleList.size() > 0) {
			for (LMSRole role : lmsRoleList) {
				System.out.println(role.getKey());
			}
		}
	}

	
	// @Kaunain - another method used in different dao with different parameters
	// also returns single role
	//@Test
	public void getSystemRoleByCustomer() {
		Customer customer = customerRepository.findOne(53l);
		String roleType="ROLE_LEARNER";
		LMSRole lmsRole = lmsRoleRepository.findByOwnerAndRoleTypeAndIsSystemCreatedTrue(customer, roleType);

		if (lmsRole != null) {
			System.out.println(lmsRole.getKey());
		}
	}

	//@Test
	public void LMSRoleLMSFeature_should_update() {
			LMSRole objRole = lmsRoleRepository.findOne(80825L);
			List<LMSRoleLMSFeature> permissionList = new ArrayList<LMSRoleLMSFeature>();
			
			LMSFeature feature = featureRepository.findOne(193L);

			LMSRoleLMSFeature objPermission = new LMSRoleLMSFeature();
			objPermission.setLmsRole(objRole);
			objPermission.setLmsFeature(feature);
			objPermission.setEnabled(true);

			permissionList.add(objPermission);
			
			//objRole.setLmsPermissions(permissionList);
			lmsRoleRepository.save(objRole);
			
	}
	
	@Test
	public void LMSRole_should_read_CustomerDefaultRole_With_LMSRoleLMSFeature(){
		Customer customer = customerRepository.findOne(13l);
		LMSRole lmsRole=lmsRoleRepository.getOptimizedBatchImportLearnerDefaultRole(customer);
		if (lmsRole != null) {
			if(lmsRole.getLmsPermissions().size()>0){
				System.out.println(lmsRole.getLmsPermissions().get(0).getId());
			}
				}
	}
}
