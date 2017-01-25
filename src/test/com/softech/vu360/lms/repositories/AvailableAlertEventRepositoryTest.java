package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.AvailableAlertEvent;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class AvailableAlertEventRepositoryTest {
	
	@Inject
	private AvailableAlertEventRepository alertAvailableAlertEventRepository;
	
	
	@Test
	public void findAll(){
		System.out.print(((List<AvailableAlertEvent>) alertAvailableAlertEventRepository.findAll()).size());
	}
	

}
