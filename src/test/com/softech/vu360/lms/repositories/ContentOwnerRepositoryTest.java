package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.LearnerValidationAnswers;
import com.softech.vu360.lms.model.RegulatoryAnalyst;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class ContentOwnerRepositoryTest {

	@Inject
	private ContentOwnerRepository repoContentOwner;
	
	@Inject
	private CustomerRepository customerRepository;
	
	//@Test
		public void ContentOwner_Should_find() {
		

			
			try{
					ContentOwner contentOwner =  repoContentOwner.findByCustomer(customerRepository.findOne(103L)) ;
					System.out.println(contentOwner);
				
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			
		
		}
	
	//@Test
	public void contentOwner_should_find_by_regulatoryAnalyst() {
		
		Long regulatorId = 1302L;
		
		RegulatoryAnalyst ra = new RegulatoryAnalyst();
		ra.setId(regulatorId);
		
		List<ContentOwner> lstContact = repoContentOwner.findByRegulatoryAnalysts(ra);
		
		System.out.println("..........");
	}
	
	//Added By Marium Saud
	
	@Test
	public void contentOwner_should_findByDistributorIdsIn(){
		
		Vector<Long> ids = new Vector<Long>();
		ids.add(103L);
		ids.add(104L);
		ids.add(305L);
		List<Map<Object, Object>> results=repoContentOwner.getContentOwnerByDistributorIDs(ids);
		for(Map map : results){
			System.out.println("ID : "+(Long)map.get("ID"));
			System.out.println("NAME : "+map.get("NAME").toString());
			System.out.println("GUID : "+map.get("GUID").toString());
		}
	
	}
	
	//@Test
	public void contentOwner_should_findById(){
		ContentOwner contentOwner=repoContentOwner.findOne(1L);
		System.out.println("ID : "+contentOwner.getId()+" Name : "+contentOwner.getName());
		
	}
	
	//@Test
	public void contentOwner_should_findAll(){
		Iterable<ContentOwner> ownerList=repoContentOwner.findAll();
		System.out.println("ContentOwnerList contains = "+((Collection<ContentOwner>)ownerList).size());
	}
}
