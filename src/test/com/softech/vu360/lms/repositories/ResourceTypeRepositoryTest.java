package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.ResourceType;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class ResourceTypeRepositoryTest {
	
	@Inject
	private ResourceTypeRepository resourceTypeRepository;

	
	
	//@Test
	public void findById(){
		System.out.print("*****************************"+resourceTypeRepository.findOne(new Long(1051)).getName());
		
	}
	
	
	//@Test
	public void findByContentOwnerIdAndIsActive(){
		
		List<ResourceType> resouresourceTypes =   resourceTypeRepository.findByContentOwnerIdAndIsActive(new Long(3),true);
		
		System.out.print("*****************************"+resouresourceTypes.size());
		
	}
	
	
	@Test
	public void delete(){
		long[] arr = new long[2];
		arr[0] = new Long(1051);
		arr[1] = new Long(1052);
		
		List<ResourceType> resouresourceTypes =   resourceTypeRepository.findByIdIn(arr);
		for(ResourceType obj : resouresourceTypes){
			
			obj.setActive(true);
			
		}
		
		
		resourceTypeRepository.save(resouresourceTypes);
		System.out.print("*****************************"+resouresourceTypes.size());
		
	}
}
