package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AuthorGroup;
import com.softech.vu360.lms.model.ContentOwner;

public interface AuthorGroupRepository extends CrudRepository<AuthorGroup, Long> {
	List<AuthorGroup> findByContentOwner(ContentOwner contentOwner);

}
