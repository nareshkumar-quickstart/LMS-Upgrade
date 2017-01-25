package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LMSAdministratorAllowedDistributor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LMSAdministratorAllowedDistributorRepositoryTest {
	
	@Inject
	private LMSAdministratorAllowedDistributorRepository lMSAdministratorAllowedDistributorRepository;
	

	@Test
	public void addDistributorWithGroupIdAndAdministratorId() {
		LMSAdministratorAllowedDistributor d =lMSAdministratorAllowedDistributorRepository.findOne(25913L);
		d.setId(null);
		d = lMSAdministratorAllowedDistributorRepository.save(d);
		
	}

}
