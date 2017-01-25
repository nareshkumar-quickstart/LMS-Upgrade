package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Proctor;

public interface ProctorRepository extends CrudRepository<Proctor, Long> {
	
	Proctor findByUserId(Long vu360UserId);
	List<Proctor> findByCredentialsIdAndStatusNot(Long credentialId, String status);
	
}
