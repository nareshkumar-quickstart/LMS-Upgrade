package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.AffidavitTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class AffidavitTemplateRepositoryTest {
	@Inject
	private AffidavitTemplateRepository affidavitTemplateRepository;
	
	//@Test
	public void findOne() {
		AffidavitTemplate entity = affidavitTemplateRepository.findOne(307L);
		if(entity!=null)
			System.out.println(entity.getTemplateGUID());
	}
//	@Test
	public void findAll() {
		List<AffidavitTemplate> aList = (List<AffidavitTemplate>) affidavitTemplateRepository.findAll();
		if(aList!=null)
			System.out.println(aList.size());
	}

	//@Test
	public void findByTemplateIdGUIDIgnoreCase() {
		AffidavitTemplate entity = affidavitTemplateRepository.findByTemplateGUIDIgnoreCase("F7F27C12-BF28-4630-AAC6-525940088C70");
		if(entity!=null)
			System.out.println(entity.getTemplateName());
	}

	@Test
	public void delete() {
		AffidavitTemplate entity = affidavitTemplateRepository.findOne(307L);
		affidavitTemplateRepository.delete(entity);
		if(entity!=null)
			System.out.println(entity.getTemplateName());
	}
}
