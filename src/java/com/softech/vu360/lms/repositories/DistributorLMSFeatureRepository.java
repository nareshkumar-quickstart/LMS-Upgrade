package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorLMSFeature;

public interface DistributorLMSFeatureRepository extends
		CrudRepository<DistributorLMSFeature, Long>{

	public List<DistributorLMSFeature> getAllByDistributor(
			Distributor distributor);

	public List<DistributorLMSFeature> getAllByDistributorAndLmsFeature_RoleType(
			Distributor distributor, String roleType);

	public List<DistributorLMSFeature> getAllByDistributorAndEnabledTrue(
			Distributor distributor);
	
	@Cacheable(value="getAllByDistributorAndEnabledFalse", key="#p0" )
	@Query(" SELECT p FROM DistributorLMSFeature p INNER JOIN FETCH p.lmsFeature f WHERE p.distributor.id = :distId AND p.enabled = 0 ")
	public List<DistributorLMSFeature> getAllByDistributorAndEnabledFalse(@Param("distId") Long distId);

	public List<DistributorLMSFeature> getAllByDistributorAndLmsFeatureRoleTypeAndEnabledTrue(Distributor distributor, String roleType);
	public List<DistributorLMSFeature> getAllByDistributorAndLmsFeatureRoleType(Distributor distributor, String roleType);

	@Override
	@CacheEvict(value = "getAllByDistributorAndEnabledFalse", key = "#p0.distributor.id")
	public <S extends DistributorLMSFeature> S save(S entity);
	
	public DistributorLMSFeature findByLmsFeatureFeatureCodeEqualsAndDistributorIdEquals(String featureCode, Long customerID);


}
