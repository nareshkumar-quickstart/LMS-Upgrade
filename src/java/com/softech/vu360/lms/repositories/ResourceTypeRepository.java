package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.ResourceType;

public interface ResourceTypeRepository  extends CrudRepository<ResourceType, Long>{
	
	public List<ResourceType> findByContentOwnerIdAndIsActive(Long contentOwnerId,boolean isActive);
	public List<ResourceType> findByIdIn(long[] resourceTypeId);

}
