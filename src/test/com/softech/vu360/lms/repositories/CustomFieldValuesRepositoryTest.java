package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CustomFieldValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CustomFieldValuesRepositoryTest {

	@Inject
	private CustomFieldValueRepository customFieldValueRepository;
	
	@Test
	public void saveOption() {

		CustomFieldValue cs = customFieldValueRepository.findOne(5L);
		cs.setId(null);
		customFieldValueRepository.save(cs);
		System.out.print(cs);
	}


}
