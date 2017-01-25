package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.WebLinkCourse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class WebLinkCourseRepositoryTest {

	@Inject
	private WebLinkCourseRepository webLinkCourseRepository;
	
	@Inject
	private CustomerRepository customerRepository  ;
	
	
	@Test
	public void Course_Should_find() {
	

		
		try{
			List<WebLinkCourse> course =  webLinkCourseRepository.findByCourseTitleIgnoreCaseLikeAndContentOwner_Customer("%fad%"
					,this.customerRepository.findOne(105L));
				System.out.println(course.size());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}

}
