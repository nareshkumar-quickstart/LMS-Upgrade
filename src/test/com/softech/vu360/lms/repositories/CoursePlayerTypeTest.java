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
import com.softech.vu360.lms.model.CoursePlayerType;

/**
 * @author marium.saud
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CoursePlayerTypeTest {

	@Inject
	CoursePlayerTypeRepository coursePlayerTypeRepository;
	
	@Test
	public void CoursePlayerType_should_findByCourseId(){
		CoursePlayerType playerType= coursePlayerTypeRepository.findByCourseId(101886L);
		if(playerType!=null){
			System.out.println("Id is "+ playerType.getId());	
		}
		else {
			System.out.println("CoursePlayerType not found");
		}
		
	}
}
