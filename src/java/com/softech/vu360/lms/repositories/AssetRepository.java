package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Asset;

public interface AssetRepository extends CrudRepository<Asset, Long> {
	@Query("SELECT a FROM  Asset a WHERE a.contentowner.id=:coid and a.name LIKE %:name% AND a.keywords LIKE %:keywords%")
	List<Asset> findAssetByContentownerAndNameAndKeywords(@Param("coid") Long coid, @Param("name") String name, @Param("keywords") String keywords);
}
