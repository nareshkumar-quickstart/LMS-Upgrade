package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class ResourceRepositoryTest {

	@Inject
	private ResourceRepository resourceRepository;
	
	
	//@Test
	public void findByIdAndContentOwnerIdAndNameLikeIgnoreCaseAndAssetTagNumberLikeIgnorCaseAndResourceTypeIdAndIsActive(){
		
		Long contentOwnerId = new Long(3);
		Long resourceId = new Long(1001);
		String name="asd";
		String assetTagNumber = "asd"; 
		boolean isActive = true;
		
		List<Resource> resources = resourceRepository.findByContentOwnerIdAndIsActive(contentOwnerId, isActive);
		
		System.out.print("5="+resources.size());
		
		
		
	}
	
	//@Test
	public void findById(){
		
		
		Long resourceId = new Long(1051);
		
		Resource resource = resourceRepository.findOne(resourceId);
		
		System.out.print("************* ID ="+resource.getName());
		
		
		
	}
	
	//@Test
	public void findByContentOwnerIdAndNameLikeAndAssetTagNumberLikeAndResourceTypeIdAndIsActive(){
		
		Long contentOwnerId = new Long(3);
		Long resourceId = new Long(1001);
		String name="asd";
		String assetTagNumber = "asd"; 
		boolean isActive = true;
		
		List<Resource> resources = resourceRepository.findContentOwnerIdandNameAndAssetTagNumberandResourceTypeIdandIsActive(contentOwnerId,name,assetTagNumber,resourceId, isActive);
		
		System.out.print("1="+resources.size());
		System.out.print("**************** Desc...."+resources.get(0).getDescription());
		
		
		
		
	}

	
	//@Test
	public void findByIds(){
		
		long[] arr = new long[2];
		arr[0]=new Long(1051);
		arr[1]=new Long(1052); 
		
		List<Resource> resources = resourceRepository.findByIdIn(arr);
		
		for(Resource obj : resources){
			
			obj.setActive(false);
		}
		
		resourceRepository.save(resources);
		
		System.out.print("5="+resources.size());
		
		
		
	}
	
	
	@Test
	public void findByCustomFields(){
		
		
		Long resourceId = new Long(1051);
		
		Resource resource = resourceRepository.findOne(resourceId);
		
		resource.getCustomfieldValues();
		System.out.print("************* ID ="+resource.getName());
		
		
		
	}
	
}
