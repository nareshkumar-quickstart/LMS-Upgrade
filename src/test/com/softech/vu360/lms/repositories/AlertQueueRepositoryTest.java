package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.AlertQueue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class AlertQueueRepositoryTest {

	
	@Inject
	private AlertQueueRepository alertQueueRepository;
	
	
	//@Test
	public void findOne(){
		System.out.print(alertQueueRepository.findOne(24764L).getEmailAddress());
	}
	
	//@Test
	public void findAll(){
		List<AlertQueue> lst = (List<AlertQueue>)alertQueueRepository.findAll();
		System.out.print("*****************************"+lst.size());
	}
	
	//@Test
	public void findByUserId(){
		System.out.print(alertQueueRepository.findByUserId(194146L).get(0).getEmailAddress());
	}
	
	//@Test
	public void findByTableNameAndTableNameIdAndTrigger_IdAndlearnerId(){
		System.out.print("************* ID = "+alertQueueRepository.findByTableNameAndTableNameIdAndTriggerIdAndLearnerId(null, 192196L, 10603L, 1038094L).getId());
	}
	
	//@Test
	public void findByTriggerTypeAndLearnerId(){
		System.out.print("************* ID = "+alertQueueRepository.findByTriggerTypeAndLearnerId("dateDrivenTrigger", 1038094L).getId());
	}

	@Test
	public void findByPendingMailStatus(){
		System.out.print("************* ID = "+alertQueueRepository.findByPendingMailStatus(true).get(0).getId());
	}
	
}
