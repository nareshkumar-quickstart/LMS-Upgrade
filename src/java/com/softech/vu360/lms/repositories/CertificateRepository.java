package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Certificate;

public interface CertificateRepository extends CrudRepository<Certificate, Long>, CertificateRepositoryCustom {
	
	
	public List<Certificate> findByNameLikeIgnoreCaseAndActiveIsTrue(@Param("name") String name);
}
