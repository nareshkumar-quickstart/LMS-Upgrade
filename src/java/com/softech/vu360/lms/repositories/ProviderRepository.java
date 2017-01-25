package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Provider;

public interface ProviderRepository extends CrudRepository<Provider, Long>, ProviderRepositoryCustom {
	
	/*@Query("SELECT p FROM  Provider p WHEREp.contentOwner.regulatoryAnalysts =:ra")
	List<Provider> findProvidersByRegulatoryAnalyst(@Param("ra") String ra);*/
	
	 
}
