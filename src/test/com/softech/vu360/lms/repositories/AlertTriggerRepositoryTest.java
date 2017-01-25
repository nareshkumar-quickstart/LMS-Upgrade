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
public class AlertTriggerRepositoryTest {
	
	@Inject
	private AlertTriggerRepository alertTriggerRepository;
	
	@Test
	public void AlertTrigger_should_findByAvailableAlertEventsId() {
		alertTriggerRepository.findByAvailableAlertEventsId(1);
	}
	
	@Test
	public void AlertTrigger_should_findByAlertIdAndIsDeleteIsFalse() {
		alertTriggerRepository.findByAlertIdAndIsDeleteIsFalse(14303L);
	}
	
}
