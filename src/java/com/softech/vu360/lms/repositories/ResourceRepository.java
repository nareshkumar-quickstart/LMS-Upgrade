package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.InstructorCourse;
import com.softech.vu360.lms.model.Resource;

public interface ResourceRepository extends CrudRepository<Resource, Long>{
	
	@Query("SELECT a FROM  Resource a WHERE a.contentOwner.id=:coid and a.name LIKE %:name% AND a.assetTagNumber like %:assetTagNumber% AND a.resourceType.id = :resourceTypeId and a.isActive =:isActive")
	public List<Resource> findContentOwnerIdandNameAndAssetTagNumberandResourceTypeIdandIsActive(@Param("coid") Long coid,@Param("name") String name,@Param("assetTagNumber") String assetTagNumber,@Param("resourceTypeId") Long resourceTypeId,@Param("isActive") boolean isActive);
	
	

	public List<Resource> findByContentOwnerIdAndIsActive(Long contentOwnerId,boolean isActive);
	public List<Resource> findByIdIn(long[] ids);
	
}
