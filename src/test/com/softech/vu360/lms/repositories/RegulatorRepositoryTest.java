package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Regulator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class RegulatorRepositoryTest {

	@Inject
	private RegulatorRepository regulatorRepository;
	@Inject
	private ContentOwnerRepository contentOwnerRepository;
	
	
	//@Test
	public void regulators_should_find_by_active() {
		//List<Regulator> listRegulators = regulatorRepository.findByActiveOrderByNameAsc(true);
		
		System.out.println("..........");
	}
	
	//@Test
	public void all_regulators_should_find() {
		List<Regulator> listRegulators = (List<Regulator>) regulatorRepository.findAll();
		
		System.out.println("..........");
	}
	
	@Test
	public void regulators_should_find_by_name_alias_emailAddress_contentOwners() {
		
		ContentOwner co = contentOwnerRepository.findOne(1L);
		List<ContentOwner> listContentOwner = new ArrayList<ContentOwner>();
		listContentOwner.add(co);
		
		String name = "Texas Real Estate Commission";
		String alias="SJR";
		String emailAddress="test@test.com";
		
		
		//List<Regulator> listRegulators = (List<Regulator>) regulatorRepository.findRegulator(name, alias, emailAddress, null, listContentOwner);
		
		System.out.println("..........");
	}
	
	@Test
	public void accreditation_search_Regualator(){
		ContentOwner co = contentOwnerRepository.findOne(1L);
		List<ContentOwner> listContentOwner = new ArrayList<ContentOwner>();
		listContentOwner.add(co);
		
		List<Regulator> lstRegulator = regulatorRepository.searchRegulator("", listContentOwner);
		System.out.println("..........");
	}

}
