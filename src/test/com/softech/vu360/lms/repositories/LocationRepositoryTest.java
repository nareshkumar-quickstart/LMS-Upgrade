package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Location;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LocationRepositoryTest {

	@Inject
	private LocationRepository locationRepository;
	
	
	//@Test
	public void findById(){
		
		System.out.print("*************************"+locationRepository.findOne(new Long(1001)).getName());
		
	}
	
	
	//@Test
	public void delete(){
		long[] arr = new long[2];
		arr[0] = 1001;
		arr[1] = 1002;
		List<Location> locations = locationRepository.findByIdIn(arr);
		for(Location obj : locations){
			obj.setActive(false);
		}
		
		locationRepository.save(locations);
		
		//System.out.print("*************************"+locationRepository.findOne(new Long(1001)).getName());
		
	}
	
	@Test
	public void findByContentOwnerIdAndNameLikeAndAddressCityAndAddressStateAndAddressCountryAndAddressZipcodeAndIsActive(){
		
		
		long contentOwnerId=454;
		String locationName="fh";
		String city="dfghdf";
		String state="aa";
		String country="us";
		String zip ="99999-9999";
		List<Location> lst = locationRepository.findContentOwnerIdAndNameLikeAndAddressCityLikeAndAddressStateLikeAndAddressCountryLikeAndAddressZipcodeLikeAndIsActive(contentOwnerId, locationName, city, state, country, zip, false);
		for(Location loc : lst){
			System.out.println("*******************"+loc.getName());
		}
		
	}
	
	
}
