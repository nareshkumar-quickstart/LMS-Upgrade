package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Regulator;

public interface RegulatorRepository extends CrudRepository<Regulator, Long>, RegulatorRepositoryCustom {

	List<Regulator> findByActiveOrderByNameAsc(Boolean option);
}
