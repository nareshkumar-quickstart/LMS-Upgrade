package com.softech.vu360.lms.repositories;

import java.util.HashSet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Commission;

public interface CommissionRepository extends CrudRepository<Commission, Long> {
	@Query("SELECT c FROM  #{#entityName} c WHERE c.distributor.id=:distId AND (c.deleted IS NULL OR c.deleted=0)")
	HashSet<Commission> getCommissions(@Param("distId") Long distributorId);
}
