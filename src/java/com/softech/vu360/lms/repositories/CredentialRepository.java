package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Credential;

public interface CredentialRepository extends CrudRepository<Credential, Long>, CredentialRepositoryCustom {

	@Query("SELECT c FROM  Credential c WHERE c.id In :cos and c.officialLicenseName LIKE %:officialLicenseName% AND c.shortLicenseName LIKE %:shortLicenseName% ")
	List<Credential> findByCredentialNameAndCredentialIds(@Param("cos") List<Long> cos,
			@Param("officialLicenseName") String officialLicenseName,
			@Param("shortLicenseName") String shortLicenseName);
}
