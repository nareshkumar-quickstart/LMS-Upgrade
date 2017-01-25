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
public class AlertRepositoryTest {

	@Inject
	private AlertRepository alertRepository;
	
	@Test
	public void Alert_should_findByCreatedByAndAlertNameIgnoreCaseLikeAndIsDeleteIsFalse() {
		alertRepository.findByCreatedByIdAndAlertNameIgnoreCaseLikeAndIsDeleteIsFalse(14303L, "Some Alert");
	}
	
	//@Test
	public void Alert_should_deleteByIdIn() {
		alertRepository.deleteByIdIn(new Long[] {1L, 2L});
	}
	
	//@Test
	public void Alert_should_findByCreatedById() {
		alertRepository.findByCreatedByIdAndIsDeleteIsFalse(1L);
	}
	
}
