package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.RegulatorCategory;

public interface RegulatorCategoryRepository extends CrudRepository<RegulatorCategory, Long>, RegulatorCategoryRepositoryCustom {

	@Query("SELECT rc FROM  RegulatorCategory rc WHERE rc.regulator.id = :regulatorId Order By displayName asc")
	List<RegulatorCategory> findByRegulator(@Param("regulatorId") Long regulatorId);

	List<RegulatorCategory> findAllByOrderByDisplayNameAsc();
	
	
	
}
