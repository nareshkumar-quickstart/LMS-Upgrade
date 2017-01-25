package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.IndustryCredential;

public interface IndustryCredentialRepository extends CrudRepository<IndustryCredential, Long> {
		@Query(value = " SELECT new map (IC.licenseindustry.id as INDUSTRY_ID, IC.credential.id as CREDENTIALS_ID, IC.id as id) " +
					   " FROM IndustryCredential IC JOIN IC.credential C JOIN IC.licenseindustry LI " +
					   " WHERE IC.licenseindustry.id = :Industryid and IC.credential.jurisdiction = :State ")  
		List<Map<Object, Object>> findByLearnerLicenseIds(@Param("Industryid") Long industryid, @Param("State") String state);
}
