package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.TrainingPlan;

public interface TrainingPlanRepository extends CrudRepository<TrainingPlan, Long>,TrainingPlanRepositoryCustom {
	
	List<TrainingPlan> findByNameIgnoreCaseAndCustomer(String name,Customer customer);
	List<TrainingPlan> findByCoursesCourseAndCustomer(Course course,Customer customer);
	public void deleteByIdIn(long[] ids);

	@Query("SELECT tp FROM  TrainingPlan tp WHERE tp.name LIKE %:name% and tp.customer.id = :customerId")
	List<TrainingPlan> findByNameAndCustomerId(@Param("name") String name,@Param("customerId") Long customerId);
	
	public List<TrainingPlan> findByCustomerId(Long customerId);
}
