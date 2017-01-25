package com.softech.vu360.lms.repositories;

//import UnitOfWork;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorCourse;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.web.controller.model.instructor.InstructorItemForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class InstructorCourseRepositoryTest {
	
	@Inject
	private InstructorCourseRepository instructorCourseRepository;
	
	
	//@Test
	public void findById(){
		Long id = new Long(1051);
		System.out.println("1051 = "+instructorCourseRepository.findOne(id).getId());
		
	}

	//@Test
	public void findByIds(){
		
		Long[] ids = new Long[2];
		ids[0] = new Long(1051);
		ids[1] = new Long(1052);
		int size = instructorCourseRepository.findByIdIn(ids).size();
		System.out.println("2 = "+size);
		
	}
	
	//@Test
	public void updateCourseInstructorRole(){
		Long id = new Long(1051);
		InstructorCourse courseInstructor = this.instructorCourseRepository.findOne(id);
		courseInstructor.setInstructorType("Lead");
		boolean result = this.instructorCourseRepository.save(courseInstructor).getInstructorType().equals("Lead") ;
		System.out.print(result);
		
	}
	
	/*@Test
	public void findByIdAndInstructorType(){
		Long id = new Long(1051);
		Vector<Long> courseInstructor = this.instructorCourseRepository.findByIdAndInstructorType(id,"Lead");
		System.out.print(courseInstructor.size());
		
	}*/
	
	
	
	
	
	//@Test
	public void delete(){
		Long[] ids = new Long[2];
		ids[0] = new Long(1802);
		ids[1] = new Long(1803);
		List<InstructorCourse> courseInstructorVector = instructorCourseRepository.findByIdIn(ids);
		try{
			this.instructorCourseRepository.delete(courseInstructorVector);
			//this.instructorCourseRepository.delete(ids[1]);
		}catch(Exception ex){
			System.out.print("**********************************Error");
		}
		
		System.out.print("Success");
		
		
	}
	
	//@Test
	public void findByCourseIdAndInstructorTypeAndfindByCourseId(){
		String instType="";
		Long synchId=new Long(63880); 
		List<InstructorCourse> objs = new ArrayList();
		List<Long> instrIds = new ArrayList();
		if ( StringUtils.isNotBlank(instType)) {
			objs = instructorCourseRepository.findByCourseIdAndInstructorType(synchId,instType);
		}else{
			objs = instructorCourseRepository.findByCourseId(synchId);
		}
		
		
		for(InstructorCourse obj : objs){
			instrIds.add(obj.getInstructor().getId());
			System.out.print("*********************** "+obj.getInstructorType());
		}
		
		System.out.print("*********************** "+instrIds.size());
		
		
		
	}
	
	
	//@Test 
	public void findCourseInstructorByCoursefirstNamelastNameIntructorType(){
		
		long courseId =90193;
		String firstName= null;//"as";
		String lastName=null;//"as";
		String instructorType="Lead";
		Map<Object,Object> list; 
		//Long expectedResult = list.get(0).getId();
		List<InstructorCourse> ls = instructorCourseRepository.findCourseInstructorByCoursefirstNamelastNameIntructorType(courseId, firstName, lastName, instructorType, 2, 1, 1, "firstName");
		System.out.println("653 = "+ls.get(0).getInstructor().getUser().getUsername());
		
		
		
		
	}
	
	//Added By Marium Saud
	@Test
	public void InstructorCourse_should_findBy_InstructorIdEquals_And_Course_CourseMediaType_EqualsIgnoreCase_And_Course_Retired_Equals_False_OrderBy_Course_CourseTitle_Asc(){
		List<InstructorCourse> ls = instructorCourseRepository.findByInstructorIdEqualsAndCourseCourseMediaTypeEqualsIgnoreCaseAndCourseRetiredFalseOrderByCourseCourseTitleAsc(1302L,"DFC");
		System.out.println("Record Size is "+ls.size());
	}
	
	
	
	
	
	
}
