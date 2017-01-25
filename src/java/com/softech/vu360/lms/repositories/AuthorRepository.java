package com.softech.vu360.lms.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long>, AuthorRepositoryCustom {

	Author findByVu360UserId(Long id);

	// instead of List<Author> getAuthorsForUsers(List<VU360User> vu360users) call the following method 
	//@Query("SELECT a FROM  #{#entityName} a join fetch a.vu360User u WHERE u.id IN (:ids)")
	Collection<Author> findByVu360UserIdIn(@Param("ids")Collection<Long> ids);
	
	
}
