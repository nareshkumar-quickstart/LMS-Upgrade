package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.ContentOwner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class AffidavitRepositoryTest {

	@Inject
	private AffidavitRepository affidavitRepository;
	
	//@Test
	public void affidavit_should_find_By_Id() {
		
		Affidavit savedAffidavit = affidavitRepository.findOne(97343L);
		
		System.out.println("..........");
	}
	
	@Test
	public void affidavit_should_find_By_Name_contentOwners() {
		
		String name = "haider";
		
		List<Affidavit> savedAffidavit = affidavitRepository.findByNameLikeIgnoreCaseAndActiveIsTrue('%'+name+'%');
		
		System.out.println("..........");
	}
	
	
}
