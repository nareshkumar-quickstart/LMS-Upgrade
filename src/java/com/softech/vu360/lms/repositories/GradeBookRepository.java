package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Gradebook;
import com.softech.vu360.lms.model.SynchronousClass;

public interface GradeBookRepository extends CrudRepository<Gradebook, Long>{
	
	@Query("SELECT gb FROM  Gradebook gb WHERE gb.synchronousClass.id in (:syncIds) and gb.synchronousClass.sectionName LIKE %:sectionName%  and synchronousClass.classStartDate >= :classStartDate and synchronousClass.classEndDate <= :classEndDate")
	public List<Gradebook> findsynchronousClassandsectionNameandstartDateandendDate(@Param("syncIds") List<Long> syncIds,@Param("sectionName") String sectionName,@Param("classStartDate") Date classStartDate,@Param("classEndDate") Date classEndDate);
	

}
