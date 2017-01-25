package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LicenseIndustry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LicenseIndustryRepositoryTest {

	@Inject
	private LicenseIndustryRepository objLLR;
	
	@Test
	public void findAll() {
		List<LicenseIndustry> lh = objLLR.findAll();
		System.out.println(lh.get(0).getName());
	}
}
