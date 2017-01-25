package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.DistributorService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LMSAdministratorRepositoryTest{

	@Inject
	private LMSAdministratorRepository lmsAdministratorRepository;

	@Inject
	private VU360UserRepository vU360UserRepository;

	@Inject
	private LMSAdministratorAllowedDistributorRepository lMSAdministratorAllowedDistributorRepository;

	@Inject
	private DistributorGroupRepository distributorGroupRepository;
	
	
	@Autowired
	private DistributorService distributorService;
	
	
	//@Test
	public void getLMSAdministratorsByDistributorGroupId(){
		
		LMSAdministrator lmsadmin = lmsAdministratorRepository.findBydistributorGroupsId(4005L);
		Assert.assertNotNull(lmsadmin);
	}


	//@Test
	//@Transactional
	public void assignLMSAdministratorsToDistributorGroup(){
		
		Long[] lmsAdminIds = new Long[]{654L };
		Long distId = new Long(206L);
		String[] allowResellers = new String[]{"454"};
		
		distributorService.assignLMSAdministratorsToDistributorGroup(lmsAdminIds, distId, allowResellers );

		
	}
	
	//@Test
	//@Transactional
	public void unassignLMSAdministratorsToDistributorGroup(){

		Long[] lmsAdminIds = new Long[]{654L };
		Long distributorGroupId = new Long(206L);
		
		distributorService.unassignLMSAdministratorsToDistributorGroup(lmsAdminIds, distributorGroupId );
	}
	
	//@Test
	//@Transactional
	public void findLMSAdministrators(){

		VU360User u = vU360UserRepository.findOne(new Long(1220));
		List<LMSAdministrator> s = lmsAdministratorRepository.findLMSAdministrators("faisal%","faisal%","faisal%", u);
		System.out.println("dddddddddddddddddd"+s.size());
		Assert.assertNotNull(s);

		
	}

	@Test
	@Transactional
	public void findByDistributorGroups_IdIn(){

		List<Long> l = new ArrayList<Long>();
		l.add(7309L);
		List<LMSAdministrator> s = lmsAdministratorRepository.findByDistributorGroups_IdIn(l);
		System.out.println("dddddddddddddddddd"+s.size());
		Assert.assertNotNull(s);

		
	}


}
