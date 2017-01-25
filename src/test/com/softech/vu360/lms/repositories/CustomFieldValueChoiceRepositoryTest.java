package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.web.controller.model.customfield.CustomField;

/**
 * 
 * @author haider.ali
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CustomFieldValueChoiceRepositoryTest {

	@Inject
	private CustomFieldValueRepository customFieldValueRepository;

	@Inject
	private CustomFieldRepository customFieldRepository;
	
	//@Test
	public void Save() {

		CustomFieldValue vd = customFieldValueRepository.findOne(43L);
		CustomFieldValue cs = customFieldValueRepository.save(vd);
		System.out.print(cs);
	}

	//@Test
	public void findByCustomField() {

		com.softech.vu360.lms.model.CustomField vd = customFieldRepository.findOne(3L);
		List<CustomFieldValue> cs = customFieldValueRepository.findByCustomField(vd);
		System.out.print(cs);
	}

}
