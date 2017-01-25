package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LearnerLicenseType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LearnerLicenseTypeRepositoryTest {

	@Inject
	private LearnerLicenseTypeRepository objLLT;
	
	@Test
	public void findAll() {
		List<LearnerLicenseType> lh = objLLT.findByLearnerId(1037844L);
		System.out.println(lh.get(0).getLicenseType().getLicenseType());
	}
}
