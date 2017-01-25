package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CourseActivity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CourseActivityRepositoryTest {
	
	@Inject
	private CourseActivityRepository courseActivityRepository;
	
	@Test
	public void findById(){
		System.out.print("********************** = "+courseActivityRepository.findOne(new Long(1451)).getActivityName());
	}
	
	@Test
	public void delete(){
		long[] arr = new long[2];
		arr[0]=3;
		arr[1]=4;
		//List<CourseActivity> courseActivities = courseActivityRepository.findByIdIn(arr);
		courseActivityRepository.deleteByIdIn(arr);
		
		System.out.print("********************** = "+courseActivityRepository.findOne(new Long(1001)).getActivityName());
	}
	
	
	//@Test
	public void findByGradeBookId(){
		long gradeBookId=1;
		List<CourseActivity> courseActivities = courseActivityRepository.findByGradeBookId(gradeBookId);
		for(CourseActivity obj : courseActivities){
			System.out.println("************* "+obj.getId());
		}
		
		
	}
	
	//@Test
	public void findactivityNameLike(){
		System.out.print(courseActivityRepository.findActivityNameLike("test").size());
	}
	
	//@Test
	//@Transactional
	public void deleteByIdIn(){
		long[] arr= new long[2];
		arr[0]=1001;
		arr[1]=1501;
		/*List<CourseActivity> lst = new ArrayList();
		CourseActivity ca = new CourseActivity();
		ca.setId(new Long(1001));
		CourseActivity ca1 = new CourseActivity();
		ca1.setId(new Long(1501));
		lst.add(ca);
		lst.add(ca1);*/
		
		
		List<Long> lst = new ArrayList<Long>();
		lst.add(new Long(1001));
		lst.add(new Long(1501));
		
		//courseActivityRepository.deleteByIdIn(lst);
		
		
	}
	
}
