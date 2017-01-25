package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LineItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LineItemRepositoryTest {
	@Inject
	private LineItemRepository lineItemRepository;
	
	@Test
	public void findByOrderGUID() {
		Long orderId=1L;
		String courseGUID="item GUID . . .. . .";
		LineItem entity = lineItemRepository.findByOrderInfoIdAndItemGUID(orderId, courseGUID);
		System.out.println(entity);
	}

}
