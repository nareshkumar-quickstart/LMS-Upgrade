package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.DistributorGroup;

/**
 * 
 * @author haider.ali
 *
 */
public interface DistributorGroupRepository
		extends CrudRepository<DistributorGroup, Long>, DistributorGroupRepositoryCustom {

	public DistributorGroup findByName(String name);

	public List<DistributorGroup> findByNameLikeIgnoreCaseOrderByNameAsc(String name);

	public DistributorGroup findById(Long Id);

	public List<DistributorGroup> findByDistributorsId(Long Id);

	public List<DistributorGroup> findById(List<Long> id);

}
