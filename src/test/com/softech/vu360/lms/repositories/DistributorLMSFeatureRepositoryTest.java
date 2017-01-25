package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.service.DistributorService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class DistributorLMSFeatureRepositoryTest {

	@Inject
	private LMSFeatureRepository lMSFeatureRepository;
	@Inject
	private DistributorLMSFeatureRepository dlfRepository;
	@Inject
	private DistributorRepository distributorRepository;
	
	@Autowired
	private DistributorService distributorService = null;

	//@Test
	public void getAllByDistributorId() {

		Distributor distributor = distributorRepository.findOne(3l);

		List<DistributorLMSFeature> dlfList = dlfRepository
				.getAllByDistributor(distributor);
		if (dlfList != null && dlfList.size() > 0) {
			for (DistributorLMSFeature feature : dlfList) {
				System.out.println(feature.getKey());
			}
		}
	}
	
	//@Test
	public void getAllByDistributorAndLMSFeature_RoleType() {

		Distributor distributor = distributorRepository.findOne(3l);

		List<DistributorLMSFeature> dlfList = dlfRepository
				.getAllByDistributorAndLmsFeature_RoleType(distributor,"ROLE_LMSADMINISTRATOR");
		if (dlfList != null && dlfList.size() > 0) {
			for (DistributorLMSFeature feature : dlfList) {
				System.out.println(feature.getKey());
			}
		}
	}
	
	//@Test
	public void getAllByDistributorAndEnabledTrue() {

		Distributor distributor = distributorRepository.findOne(3l);

		List<DistributorLMSFeature> dlfList = dlfRepository
				.getAllByDistributorAndEnabledTrue(distributor);
		if (dlfList != null && dlfList.size() > 0) {
			for (DistributorLMSFeature feature : dlfList) {
				System.out.println(feature.getKey());
			}
		}
	}

	//@Test
	public void getAllByDistributorAndEnabledFalseOrEnabledIsNull() {

		Distributor distributor = distributorRepository.findOne(3l);

		List<DistributorLMSFeature> dlfList = dlfRepository
				.getAllByDistributorAndEnabledFalse(distributor.getId());
		if (dlfList != null && dlfList.size() > 0) {
			for (DistributorLMSFeature feature : dlfList) {
				System.out.println(feature.getKey());
			}
		}
	}
	
	//@Test
	public void getDefaultEnablePermissions() {

		/*// ROLE TYPES
		public static final String ROLE_LEARNER = "ROLE_LEARNER";
		public static final String ROLE_TRAININGMANAGER = "ROLE_TRAININGADMINISTRATOR";
		public static final String ROLE_LMSADMINISTRATOR = "ROLE_LMSADMINISTRATOR";
		public static final String ROLE_REGULATORYANALYST = "ROLE_REGULATORYANALYST";
		public static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
		public static final String ROLE_PROCTOR = "ROLE_PROCTOR";*/
		
		Distributor distributor = distributorRepository.findOne(3l);
		List<DistributorLMSFeature> dlfList = dlfRepository.getAllByDistributorAndLmsFeatureRoleTypeAndEnabledTrue(distributor, "ROLE_LEARNER");
		Assert.assertNotNull(dlfList);
	}

	
	//@Test
	public void getPermissions() {

		Distributor distributor = distributorRepository.findOne(3l);
		List<DistributorLMSFeature> dlfList = distributorService.getPermissions(distributor, "ROLE_LEARNER");
		Assert.assertNotNull(dlfList);
	}
	
	@Test
	@Transactional
	public void saveFeatureForDistributor() {
		DistributorLMSFeature d = new DistributorLMSFeature();
		d.setDistributor(distributorRepository.findOne(1L));
		d.setLmsFeature(lMSFeatureRepository.findOne(200L));
		d.setEnabled(false);
		dlfRepository.save(d);
		
	}

	
}
