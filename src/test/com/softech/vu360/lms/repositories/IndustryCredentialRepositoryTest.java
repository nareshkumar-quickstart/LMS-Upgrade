package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class IndustryCredentialRepositoryTest {

	@Inject
	private IndustryCredentialRepository objLCR;
	
	@Test
	public void findByLearnerLicenseIds(){
		List<Map<Object, Object>> results=objLCR.findByLearnerLicenseIds(1L, "TX");
		for(Map map : results){
			System.out.println("CREDENTIALS_ID	 : "+map.get("CREDENTIALS_ID"));
			System.out.println("ID : "+map.get("id"));
			System.out.println("INDUSTRY_ID : "+map.get("INDUSTRY_ID"));
		}
	}
}
