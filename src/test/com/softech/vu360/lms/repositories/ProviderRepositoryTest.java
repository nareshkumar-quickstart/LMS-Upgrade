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
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.RegulatoryAnalyst;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class ProviderRepositoryTest {

	@Inject
	private ProviderRepository repoProvider;
	@Inject
	private ContentOwnerRepository contentOwnerRepository;
	
	//@Test
	public void provider_should_save() {
		Provider saveProvider = repoProvider.findOne(new Long(2105));
		saveProvider.setId(null);
		
		Provider savedProvider = repoProvider.save(saveProvider);
		
		System.out.println("..........");
	}
	
	//@Test
	public void provider_should_find() {
		Provider saveProvider = repoProvider.findOne(new Long(2));
		
		System.out.println("..........");
	}
	
	//@Test
	public void provider_should_deleteAll(List<Provider> objProviders) {
		repoProvider.delete(objProviders);
		
		System.out.println("..........");
	}

	@Test
	public void providers_should_find_by_name_regulatoryAnalyst_regulatorAnalystOwnerIds() {
		
		ContentOwner co = contentOwnerRepository.findOne(1L)     ;
		String name = "NA-Provider";
		List<ContentOwner> listContentOwner = new ArrayList<ContentOwner>();
		listContentOwner.add(co);
		
		//List<Provider> providers = repoProvider.findProviders(name, null, listContentOwner);
		
		System.out.println("..........");
	}
	
	@Test
	public void accreditation_search_Provider(){
		ContentOwner co = contentOwnerRepository.findOne(1L);
		List<ContentOwner> listContentOwner = new ArrayList<ContentOwner>();
		listContentOwner.add(co);
		
		List<Provider> lstProvider= repoProvider.searchProviders("", listContentOwner);		
		System.out.println("..........");
	}
}
