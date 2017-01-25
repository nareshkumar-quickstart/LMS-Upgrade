package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;

public interface SynchronousSessionRepository extends
		CrudRepository<SynchronousSession, Long>,
		SynchronousSessionRepositoryCustom {

	public List<SynchronousSession> findBySynchronousClass(
			SynchronousClass synchronousClass);

	//public List<SynchronousSession> findByResourcesId(Long resourceId);
	
	// @Query("Select p from SynchronousSession p where p.id=:Id")
	// public SynchronousSession findSynchClassSessionBySessionId(@Param("Id")
	// long Id);

}
