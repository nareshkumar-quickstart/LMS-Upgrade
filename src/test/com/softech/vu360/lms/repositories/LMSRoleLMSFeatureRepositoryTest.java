package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LMSRoleLMSFeatureRepositoryTest {

	@Inject
	LMSRoleLMSFeatureRepository lmsRoleLmsFeatureRepository;
	@Inject
	CustomerRepository customerRepository;

	//@Test
	public void getAvailablePermissions() {
		Customer customer = customerRepository.findOne(53l);
		String roleType = "ROLE_LEARNER";
		List<LMSRoleLMSFeature> lrlfList = lmsRoleLmsFeatureRepository
				.getAvailablePermissionsByRoleType(
						customer, roleType);
		if (lrlfList != null && lrlfList.size() > 0) {
			for (LMSRoleLMSFeature lf : lrlfList) {
				System.out.println(lf.getKey());
			}
		}
		
	}
	

	//@Test
	public void getAllPermissions() {
		Customer customer = customerRepository.findOne(53l);
		String roleType = "ROLE_LEARNER";
		List<LMSRoleLMSFeature> lrlfList = lmsRoleLmsFeatureRepository
				.getAllPermissionsByRoleType(
						customer, roleType);
		if (lrlfList != null && lrlfList.size() > 0) {
			for (LMSRoleLMSFeature lf : lrlfList) {
				System.out.println(lf.getKey());
			}
		}
		
	}
	
	//@Test
	public void findOne(){
		LMSRoleLMSFeature lrlf = lmsRoleLmsFeatureRepository.findOne(8698l);
		if(lrlf!=null){
			System.out.println(lrlf.getKey());
		}
		
	}
	
	//@Test
	public void Save(){
		LMSRoleLMSFeature lrlf=lmsRoleLmsFeatureRepository.findOne(8698l);
		lmsRoleLmsFeatureRepository.save(lrlf);	
	}
	
}
