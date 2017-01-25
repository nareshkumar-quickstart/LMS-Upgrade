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
public class AlertTriggerFilterRepositoryTest {
	
	@Inject
	private AlertTriggerFilterRepository alertTriggerFilterRepository;
	
	@Test
	public void findOne(){
		alertTriggerFilterRepository.findOne(5003L).getFilterName();
	}
	
	@Test
	public void AlertTriggerFilter_should_findByAlertTriggerId() {
		alertTriggerFilterRepository.findByAlerttriggerId(10955L);
	}

}
