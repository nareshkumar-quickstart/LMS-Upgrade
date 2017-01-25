package com.softech.vu360.lms.repositories;

import java.util.HashSet;
import java.util.Vector;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Commission;
import com.softech.vu360.lms.model.LockedCourse;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.ProctorService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LockedCourseRepositoryTest {
	@Inject
	private LockedCourseRepository lockedCourseRepository;
	
	EnrollmentService enrollmentService;
	
	 @Autowired 
     ApplicationContext context;
	
	@Before
	public void CustomerRepositoryInit(){
		enrollmentService = (EnrollmentService) context.getBean("enrollmentService");
    }

	//@Test
	public void LockedCourse_should_setCourses_Unlocked() {
		Vector<Long> id=new Vector<Long>();
		id.add(4624L);
		id.add(4626L);
			enrollmentService.setCourseStatus(id,"");
	}
	
	@Test
	public void LockedCourse_should_getLastLockedCourse(){
		LockedCourse lockedCourse=lockedCourseRepository.findFirstByEnrollmentIdAndCourselockedTrueOrderByIdDesc(259692L);
		System.out.println("******************* ID = "+lockedCourse.getId());
	}
}

