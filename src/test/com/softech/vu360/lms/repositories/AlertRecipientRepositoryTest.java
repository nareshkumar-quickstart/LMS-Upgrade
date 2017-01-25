package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.LmsTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@Transactional
public class AlertRecipientRepositoryTest {

	@Inject
	private AlertRecipientRepository alertRecipientRepository;
	
	@Test
	public void AlertRecipient_should_findByAlertId() {
		alertRecipientRepository.findByAlertId(14303L);
	}
	
	@Test
	public void AlertRecipient_should_deleteByIdIn() {
		alertRecipientRepository.deleteByIdIn(new Long[] {8753L, 8803L, 8853L});
	}
	
	@Test
	public void AlertRecipient_should_findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse() {
		alertRecipientRepository.findByAlertRecipientGroupNameIgnoreCaseLikeAndAlertIdAndIsDeleteIsFalse("saaliz", 14303L);
	}
	
}
