package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.TrainingPlan;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class TrainingPlanRepositoryTest {

	@Inject
	private TrainingPlanRepository trainingPlanRepository;
	
	@Inject
	private CustomerRepository customerRepository;
	
	@Inject
	private CourseRepository courseRepository;
	
	//@Test
	public void TrainingPlan_Should_find_By_Name() {
	

		
		try{
			//Learning Program3
			
			List<TrainingPlan> tp =  trainingPlanRepository.findByNameIgnoreCaseAndCustomer("Learning Program3", customerRepository.findOne(103L));
				//ContentOwner contentOwner =  contentOwnerRepository.findByCustomer(customerRepository.findOne(103L)) ;
				System.out.println(tp.get(0).getName());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}
	
	//@Test
	public void TrainingPlan_Should_find_By_Course() {
	

		
		try{
			//Learning Program3
			
			List<TrainingPlan> tp =  trainingPlanRepository.findByCoursesCourseAndCustomer(courseRepository.findOne(78299L), customerRepository.findOne(14204L));
				//ContentOwner contentOwner =  contentOwnerRepository.findByCustomer(customerRepository.findOne(103L)) ;
				System.out.println(tp.get(0).getCourses().size());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}
	
	//@Test
	public void deleteByIdIn(){
		long[] trainingPlanIds = new long[2];
		trainingPlanIds[0]=10225;
		trainingPlanIds[1]=10227;
		
		trainingPlanRepository.deleteByIdIn(trainingPlanIds);
		
	}
	
	//@Test
	public void save(){
		TrainingPlan obj = trainingPlanRepository.findOne(10223L);
		//obj.setId(224L);
		Customer cu = customerRepository.findOne(2L);
		//cu.setId(103L);
		obj.setName("New Test");
		obj.setCustomer(cu);
		trainingPlanRepository.save(obj);
		
	}
	
	//@Test
	public void findByNameAndCustomerId(){
		String name="training";
		long cusId=103;
		List<TrainingPlan> objs = trainingPlanRepository.findByNameAndCustomerId(name, cusId);
		for(TrainingPlan obj : objs){
			System.out.println(obj.getId());
		}
		
	}
	
	//@Test
	public void findById(){
		long id=1;
		TrainingPlan obj = trainingPlanRepository.findOne(id);
		System.out.println("**************** Name = "+obj.getName());
		
		
	}
	
	//@Test
	public void findByCustomerId(){
		List<TrainingPlan> objs = trainingPlanRepository.findByCustomerId(103L);
		for(TrainingPlan obj : objs){
			System.out.println(obj.getId());
		}
		
		
	}
	
	//@Test
	public void getTrainingPlansForCourseCatalog(){
		List<Map<Object,Object>> listMap = trainingPlanRepository.getTrainingPlansForCourseCatalog(3L, "");
		int count=0;
		for(Map map : listMap){
			System.out.println((++count)+"================"+map.get("TRAININGPLAN_ID"));
		}
	}
	
	@Test
	public void countLearnerByTrainingPlan(){
		Long[] anArray = {  105L, 2301L, 2551L, 2851L, 4151L};
		List<Object[]> listMap = trainingPlanRepository.countLearnerByTrainingPlan(anArray);
		
		
		for (Object[] objects : listMap) {
			System.out.println("traing plan Id    "+objects[0]);
			System.out.println("count "+objects[1]);

		}
	}
}
