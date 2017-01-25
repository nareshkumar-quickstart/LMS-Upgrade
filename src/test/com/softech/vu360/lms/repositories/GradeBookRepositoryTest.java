package com.softech.vu360.lms.repositories;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.model.SynchronousClass;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class GradeBookRepositoryTest {

	
	@Inject
	private GradeBookRepository gradeBookRepository;
	
	@Inject
	private InstructorSynchronousClassRepository instructorSynchronousClassRepository;
	
	
	
	
	
	//@Test
	public void findById(){
		
		System.out.print(gradeBookRepository.findOne(new Long(1)).getSynchronousClass().getId());
		
	}
	
	@Test
	public void findBySynchronousClass(){
		
		long[] arr = new long[2];
		arr[0] =852;
		arr[1]=1360;
		
		List<InstructorSynchronousClass> objs = instructorSynchronousClassRepository.findByInstructorId(new Long(1354));
		//List<SynchronousClass> synchronousClasses = new ArrayList();
		List<Long> synchronousClasses = new ArrayList();
		for(InstructorSynchronousClass obj : objs){
			synchronousClasses.add(obj.getSynchronousClass().getId());
			System.out.println("************** ID = "+obj.getSynchronousClass().getId());
		}
		
		try{
		
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	
			java.util.Date startRange =null; 
			java.util.Date endRange = null;
			String startDate="09/24/2010";
			String endDate="09/24/2010";
			startRange=format.parse(startDate);
			endRange = format.parse(endDate);
			
			System.out.print("------------------- ID ="+gradeBookRepository.findsynchronousClassandsectionNameandstartDateandendDate(synchronousClasses,"tt",startRange,endRange).get(0).getId());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}

	
	
}
