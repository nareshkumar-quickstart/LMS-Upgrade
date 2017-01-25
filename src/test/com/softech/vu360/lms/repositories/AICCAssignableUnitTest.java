/**
 * 
 */
package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.AICCAssignableUnit;

/**
 * @author marium.saud
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class AICCAssignableUnitTest {

	@Inject
	AICCAssignableUnitRepository aiccAssignableUnitRepository;
	
	@Test
	public void AICCAssignableUnit_should_findByCourseId(){
		AICCAssignableUnit unit=aiccAssignableUnitRepository.findByCourseId(94785L);
		if(unit!=null){
			System.out.println("Id is "+ unit.getId());
		}
		else {
			System.out.println("AICCAssignableUnit not found");
		}

	}
}

